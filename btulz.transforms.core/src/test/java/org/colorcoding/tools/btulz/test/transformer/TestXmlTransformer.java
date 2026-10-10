package org.colorcoding.tools.btulz.test.transformer;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;

import org.colorcoding.tools.btulz.model.Domain;
import org.colorcoding.tools.btulz.model.IDomain;
import org.colorcoding.tools.btulz.test.Environment;
import org.colorcoding.tools.btulz.test.model.TestModels;
import org.colorcoding.tools.btulz.transformer.MultiTransformException;
import org.colorcoding.tools.btulz.transformer.TransformException;
import org.colorcoding.tools.btulz.transformer.XmlTransformer;

import junit.framework.TestCase;

/**
 * XML转换器测试
 *
 * 覆盖： - XmlTransformer：从内存Domain对象加载并保存 - XmlTransformer：从XML文件加载域模型 -
 * XML元素和属性顺序 - 转换器属性设置(interruptOnError/keepResults)
 *
 * 注意：XmlTransformer被TestRegionDomain等高级功能使用
 */
public class TestXmlTransformer extends TestCase {

	/** 获取旧版XML文件路径，不存在则返回null */
	private String getOldXmlPath() {
		String path = Environment.getWorkingFolder() + File.separator + Environment.getXmlModelsFileOld();
		if (new File(path).exists()) {
			return path;
		}
		return null;
	}

	/** 从内存Domain对象加载，保存后再加载，验证往返正确 */
	public void testSaveReadXml()
			throws ClassNotFoundException, TransformException, MultiTransformException, JAXBException {
		IDomain domain = (new TestModels()).createDomain();
		XmlTransformer xmlTransformer = new XmlTransformer();
		xmlTransformer.load(domain);
		xmlTransformer.setInterruptOnError(true);
		xmlTransformer.save();
		xmlTransformer.setKeepResults(false);

		xmlTransformer.load(Environment.getWorkingFolder()
				+ "/out/TrainingTesting/ibas.trainingtesting/src/main/resources/datastructures".replace("/",
						File.separator),
				false);
		xmlTransformer.save();
		JAXBContext context = JAXBContext.newInstance(Domain.class);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);

		System.out.println("序列化输出：");
		for (IDomain item : xmlTransformer.getWorkingDomains()) {
			StringWriter writer = new StringWriter();
			marshaller.marshal(item, writer);
			String oldXML = writer.toString();
			System.out.println(oldXML);
		}
	}

	/** 保存时保持约定的属性顺序、集合顺序和任意深度的业务对象关系。 */
	public void testSavePreservesOrderAndEscaping() throws Exception {
		String expected = """
				<Domain Name="顺序测试" ShortName="ST">
				  <Model Name="ZModel" Description="中文 &amp; &lt;模型&gt; &quot;引用&quot;" ModelType="Document" Mapped="Z_TABLE" Entity="No">
				    <Property Name="ZProperty" Description="" DataType="Alphanumeric" DataSubType="Default" EditSize="20" DeclaredType="String" Mapped="Z_COLUMN" PrimaryKey="Yes" UniqueKey="Yes" SearchKey="Yes" Linked="Other" DefaultValue="A&amp;B"/>
				    <Property Name="AProperty" DataType="Numeric" DataSubType="Default" EditSize="10" Mapped="A_COLUMN"/>
				    <Index Name="ZIndex" ShortName="ZI" Description="索引" IndexType="Unique">
				      <IndexProperty Name="ZProperty" Direction="DESC"/>
				      <IndexProperty Name="AProperty"/>
				    </Index>
				    <Index Name="AIndex" IndexType="NonClustered"/>
				  </Model>
				  <Model Name="AModel" Description="" ModelType="Unspecified"/>
				  <BusinessObject Name="ZBusiness" Description="业务对象" ShortName="ZB" MappedModel="ZModel">
				    <RelatedBO Relation="OneToMany" Name="ZChild" Description="子对象" ShortName="ZC" MappedModel="AModel">
				      <RelatedBO Relation="OneToOne" MappedModel="ZModel" ShortName="GC">
				        <RelatedBO Relation="OneToMany" MappedModel="AModel"/>
				      </RelatedBO>
				    </RelatedBO>
				    <RelatedBO Relation="OneToOne" MappedModel="AModel"/>
				  </BusinessObject>
				  <BusinessObject MappedModel="AModel" ShortName="AB"/>
				</Domain>
				""";
		// 输入属性故意采用不同排列，输出应使用写入实现约定的排列。
		String input = expected.replace("Name=\"顺序测试\" ShortName=\"ST\"", "ShortName=\"ST\" Name=\"顺序测试\"")
				.replace("<IndexProperty Name=\"AProperty\"/>", "<IndexProperty Name=\"AProperty\" Direction=\"ASC\"/>");
		Path folder = Files.createTempDirectory("btulz-xml-order-");
		Path file = folder.resolve("ds_st_顺序测试.xml");
		try {
			XmlTransformer transformer = new XmlTransformer();
			transformer.setGroupingFile(false);
			transformer.setInterruptOnError(true);
			transformer.load(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
			transformer.save(folder.toString());
			String output = Files.readString(file, StandardCharsets.UTF_8);
			assertTrue(output.startsWith("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"));
			assertEquals(expected, output.substring(output.indexOf("<Domain")));
			// 再次读取验证转义与深层嵌套没有丢失，并确认重复保存稳定。
			XmlTransformer restored = new XmlTransformer();
			restored.setGroupingFile(false);
			restored.setInterruptOnError(true);
			restored.load(file.toFile(), false);
			IDomain domain = restored.getWorkingDomains()[0];
			assertEquals("中文 & <模型> \"引用\"", domain.getModels().get(0).getDescription());
			assertEquals("A&B", domain.getModels().get(0).getProperties().get(0).getDefaultValue());
			assertEquals("DESC", domain.getModels().get(0).getIndexes().get(0).getIndexProperties().get(0).getDirection());
			assertEquals("ASC", domain.getModels().get(0).getIndexes().get(0).getIndexProperties().get(1).getDirection());
			assertEquals("AModel", domain.getBusinessObjects().get(0).getRelatedBOs().get(0)
					.getRelatedBOs().get(0).getRelatedBOs().get(0).getMappedModel());
			restored.save(folder.toString());
			assertEquals(output, Files.readString(file, StandardCharsets.UTF_8));
		} finally {
			Files.deleteIfExists(file);
			Files.deleteIfExists(folder);
		}
	}

	/** 从XML文件加载并验证域/模型/业务对象 */
	public void testLoadFromXml() throws TransformException, MultiTransformException {
		String xmlPath = getOldXmlPath();
		if (xmlPath == null) {
			System.out.println("跳过：旧版XML文件不存在");
			return;
		}
		XmlTransformer xmlTransformer = new XmlTransformer();
		xmlTransformer.load(xmlPath, false);
		IDomain[] domains = xmlTransformer.getWorkingDomains();
		assertNotNull("加载后应有域对象", domains);
		assertTrue("应至少包含一个域", domains.length > 0);
		assertEquals("TrainingTesting", domains[0].getName());
		assertTrue(domains[0].getModels().size() > 0);
		assertTrue(domains[0].getBusinessObjects().size() > 0);
	}
}
