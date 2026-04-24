package org.colorcoding.tools.btulz.test.transformer;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.colorcoding.tools.btulz.Serializer;
import org.colorcoding.tools.btulz.model.IDomain;
import org.colorcoding.tools.btulz.template.Parameter;
import org.colorcoding.tools.btulz.template.Parameters;
import org.colorcoding.tools.btulz.test.Environment;
import org.colorcoding.tools.btulz.transformer.DataStructureOrchestration;
import org.colorcoding.tools.btulz.transformer.XmlTransformer;
import org.colorcoding.tools.btulz.transformer.region.ParametersFactory;
import org.colorcoding.tools.btulz.transformer.region.RegionDomain;
import org.colorcoding.tools.btulz.transformer.region.model.OutputMappingList;

import junit.framework.TestCase;

/**
 * RegionDomain数据结构导出测试
 *
 * 覆盖： - 以MSSQL为代表：加载域模型 → 设置RegionDomain模板 → 导出编排XML → 反序列化并执行 -
 * MYSQL/PGSQL/HANA/SQLITE流程一致，仅模板和连接参数不同
 *
 * 注意：依赖外部数据库环境
 */
public class TestRegionDomain extends TestCase {

	/** MSSQL数据结构编排生成与执行（代表所有数据库类型） */
	public void testMSSQL() throws Exception {
		XmlTransformer xmlTransformer = new XmlTransformer();
		xmlTransformer.load(Environment.getWorkingFolder() + Environment.getXmlModelsFileOld(), false);

		JAXBContext context = JAXBContext.newInstance(DataStructureOrchestration.class);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		for (IDomain domain : xmlTransformer.getWorkingDomains()) {
			String tpltFile = Environment.getResource("ds/ds_mssql_ibas_classic.xml").getPath();
			RegionDomain template = new RegionDomain();
			template.setTemplateFile(tpltFile);
			File outputFile = new File(
					Environment.getWorkingFolder() + File.separator + "ds_mssql_ibas_classic.out.xml");
			Parameters parameters = new Parameters();
			parameters.add(new Parameter("Company", "CC"));
			parameters.add(new Parameter("DbServer", "ibas-db-mssql"));
			parameters.add(new Parameter("DbPort", "1433"));
			parameters.add(new Parameter("DbName", "ibas_demo" + "_" + domain.hashCode()));
			parameters.add(new Parameter("DbSchema", "dbo"));
			parameters.add(new Parameter("AppName", "btulz.transforms"));
			parameters.add(new Parameter("DbUser", "sa"));
			parameters.add(new Parameter("DbPassword", "1q2w3e"));
			parameters.add(new Parameter(RegionDomain.REGION_DELIMITER, domain));
			parameters.add(new Parameter(ParametersFactory.PARAMETER_NAME_OUTPUT_MAPPING,
					OutputMappingList.create(Environment.getResource("ds/dm_mssql_ibas_classic.xml").getPath())));
			template.export(parameters, outputFile);
			DataStructureOrchestration orchestration = (DataStructureOrchestration) unmarshaller.unmarshal(outputFile);
			System.out.println("orchestration: ");
			System.out.println(Serializer.toXmlString(orchestration, true));
			orchestration.execute();
		}
	}

}
