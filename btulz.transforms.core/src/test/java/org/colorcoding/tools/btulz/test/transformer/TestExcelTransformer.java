package org.colorcoding.tools.btulz.test.transformer;

import java.io.File;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.colorcoding.tools.btulz.model.Domain;
import org.colorcoding.tools.btulz.model.IDomain;
import org.colorcoding.tools.btulz.test.Environment;
import org.colorcoding.tools.btulz.transformer.ExcelTransformer;

import junit.framework.TestCase;

/**
 * Excel转换器测试
 *
 * 覆盖： - ExcelTransformer：从Excel加载域模型并转换为XML - ignoreSheet：是否忽略注释表格 -
 * interruptOnError：出错是否中断
 *
 * 注意：不支持旧版xls模板，有需要转成xml再使用
 */
public class TestExcelTransformer extends TestCase {

	/** 使用ExcelTransformer加载Excel并转换为域模型XML */
	public void testTransformer() throws Exception {
		ExcelTransformer excelTransformer = new ExcelTransformer();
		excelTransformer.setIgnoreSheet(false);// 不忽略注释表格
		excelTransformer.setInterruptOnError(true);
		excelTransformer.load((new File(Environment.getWorkingFolder())).getParent() + Environment.getExcelModelsFile(),
				true);
		JAXBContext context = JAXBContext.newInstance(Domain.class);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);

		System.out.println("序列化输出：");
		for (IDomain item : excelTransformer.getWorkingDomains()) {
			StringWriter writer = new StringWriter();
			marshaller.marshal(item, writer);
			String oldXML = writer.toString();
			System.out.println(oldXML);
		}
		excelTransformer.transform();// 保存为xml
	}

}
