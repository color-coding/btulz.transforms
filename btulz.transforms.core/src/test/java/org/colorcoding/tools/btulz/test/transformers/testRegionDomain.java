package org.colorcoding.tools.btulz.test.transformers;

import java.io.File;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.colorcoding.tools.btulz.Environment;
import org.colorcoding.tools.btulz.Serializer;
import org.colorcoding.tools.btulz.models.IDomain;
import org.colorcoding.tools.btulz.models.data.emDataSubType;
import org.colorcoding.tools.btulz.models.data.emDataType;
import org.colorcoding.tools.btulz.templates.Parameter;
import org.colorcoding.tools.btulz.transformers.DataStructureOrchestration;
import org.colorcoding.tools.btulz.transformers.XmlTransformer;
import org.colorcoding.tools.btulz.transformers.regions.RegionDomain;
import org.colorcoding.tools.btulz.transformers.regions.models.DataTypeMapping;
import org.colorcoding.tools.btulz.transformers.regions.models.DataTypeMappings;

import junit.framework.TestCase;

public class testRegionDomain extends TestCase {

	private static String domain_file = testXmlTransformer.old_xml_path + File.separator + "domain_models_old.xml";

	public void testMSSQL() throws Exception {
		XmlTransformer xmlTransformer = new XmlTransformer();
		xmlTransformer.load(Environment.getWorkingFolder() + domain_file, false);

		JAXBContext context = JAXBContext.newInstance(DataStructureOrchestration.class);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
		Unmarshaller unmarshaller = context.createUnmarshaller();

		// 测试MSSQL
		for (IDomain domain : xmlTransformer.getWorkingDomains()) {
			String tpltFile = Environment.getResource("ds").getPath();
			RegionDomain template = new RegionDomain();
			template.setTemplateFile(tpltFile + File.separator + "ds_mssql_ibas.xml");
			File outputFile = new File(Environment.getWorkingFolder() + File.separator + "ds_mssql_ibas.out.xml");
			ArrayList<Parameter> parameters = new ArrayList<>();
			parameters.add(new Parameter("Company", "CC"));
			parameters.add(new Parameter("DbServer", "ibas-dev-mssql"));
			parameters.add(new Parameter("DbPort", "1433"));
			parameters.add(new Parameter("DbName", "ibas_demo" + "_" + domain.hashCode()));
			parameters.add(new Parameter("DbSchema", "dbo"));
			parameters.add(new Parameter("AppName", "btulz.transforms"));
			parameters.add(new Parameter("DbUser", "sa"));
			parameters.add(new Parameter("DbPassword", "1q2w3e"));
			parameters.add(new Parameter(RegionDomain.REGION_DELIMITER, domain));
			template.export(parameters, outputFile);
			DataStructureOrchestration orchestration = (DataStructureOrchestration) unmarshaller.unmarshal(outputFile);
			System.out.println("orchestration: ");
			System.out.println(Serializer.toXmlString(orchestration, true));
			orchestration.execute();
		}

	}

	public void testMYSQL() throws Exception {
		XmlTransformer xmlTransformer = new XmlTransformer();
		xmlTransformer.load(Environment.getWorkingFolder() + domain_file, false);

		JAXBContext context = JAXBContext.newInstance(DataStructureOrchestration.class);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		DataTypeMappings dataTypeMappings = new DataTypeMappings();
		dataTypeMappings
				.add(new DataTypeMapping(emDataType.Memo, emDataSubType.Default, "text(${Property.getEditSize()})"));
		System.out.println(Serializer.toXmlString(dataTypeMappings, true));
		// 测试MYSQL
		for (IDomain domain : xmlTransformer.getWorkingDomains()) {
			String tpltFile = Environment.getResource("ds").getPath();
			RegionDomain template = new RegionDomain();
			template.setTemplateFile(tpltFile + File.separator + "ds_mysql_ibas.xml");
			File outputFile = new File(Environment.getWorkingFolder() + File.separator + "ds_mysql_ibas.out.xml");
			dataTypeMappings = DataTypeMappings
					.create(template.getTemplateFile().replace("ds_mysql_ibas.xml", "dm_mysql_ibas.xml"));
			ArrayList<Parameter> parameters = new ArrayList<>();
			parameters.add(new Parameter("Company", "CC"));
			parameters.add(new Parameter("DbServer", "ibas-dev-mysql"));
			parameters.add(new Parameter("DbPort", "3306"));
			parameters.add(new Parameter("DbName", "ibas_demo" + "_" + domain.hashCode()));
			parameters.add(new Parameter("AppName", "btulz.transforms"));
			parameters.add(new Parameter("DbUser", "root"));
			parameters.add(new Parameter("DbPassword", "1q2w3e"));
			parameters.add(new Parameter(RegionDomain.REGION_DELIMITER, domain));
			parameters.add(new Parameter(DataTypeMapping.PARAMETER_NAME, dataTypeMappings));
			template.export(parameters, outputFile);
			DataStructureOrchestration orchestration = (DataStructureOrchestration) unmarshaller.unmarshal(outputFile);
			System.out.println("orchestration: ");
			System.out.println(Serializer.toXmlString(orchestration, true));
			orchestration.execute();
		}
	}

	public void testPGSQL() throws Exception {
		XmlTransformer xmlTransformer = new XmlTransformer();
		xmlTransformer.load(Environment.getWorkingFolder() + domain_file, false);

		JAXBContext context = JAXBContext.newInstance(DataStructureOrchestration.class);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		DataTypeMappings dataTypeMappings = new DataTypeMappings();
		dataTypeMappings
				.add(new DataTypeMapping(emDataType.Memo, emDataSubType.Default, "text(${Property.getEditSize()})"));
		System.out.println(Serializer.toXmlString(dataTypeMappings, true));
		// 测试PGSQL
		for (IDomain domain : xmlTransformer.getWorkingDomains()) {
			String tpltFile = Environment.getResource("ds").getPath();
			RegionDomain template = new RegionDomain();
			template.setTemplateFile(tpltFile + File.separator + "ds_pgsql_ibas.xml");
			File outputFile = new File(Environment.getWorkingFolder() + File.separator + "ds_pgsql_ibas.out.xml");
			dataTypeMappings = DataTypeMappings
					.create(template.getTemplateFile().replace("ds_pgsql_ibas.xml", "dm_pgsql_ibas.xml"));
			ArrayList<Parameter> parameters = new ArrayList<>();
			parameters.add(new Parameter("Company", "CC"));
			parameters.add(new Parameter("DbServer", "ibas-dev-pgsql"));
			parameters.add(new Parameter("DbPort", "5432"));
			parameters.add(new Parameter("DbName", "ibas_demo" + "_" + domain.hashCode()));
			parameters.add(new Parameter("AppName", "btulz.transforms"));
			parameters.add(new Parameter("DbUser", "postgres"));
			parameters.add(new Parameter("DbPassword", "1q2w3e"));
			parameters.add(new Parameter(RegionDomain.REGION_DELIMITER, domain));
			parameters.add(new Parameter(DataTypeMapping.PARAMETER_NAME, dataTypeMappings));
			template.export(parameters, outputFile);
			DataStructureOrchestration orchestration = (DataStructureOrchestration) unmarshaller.unmarshal(outputFile);
			System.out.println("orchestration: ");
			System.out.println(Serializer.toXmlString(orchestration, true));
			orchestration.execute();
		}
	}

	public void testHANA() throws Exception {
		XmlTransformer xmlTransformer = new XmlTransformer();
		xmlTransformer.load(Environment.getWorkingFolder() + domain_file, false);

		JAXBContext context = JAXBContext.newInstance(DataStructureOrchestration.class);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		DataTypeMappings dataTypeMappings = new DataTypeMappings();
		dataTypeMappings
				.add(new DataTypeMapping(emDataType.Memo, emDataSubType.Default, "text(${Property.getEditSize()})"));
		System.out.println(Serializer.toXmlString(dataTypeMappings, true));
		// 测试PGSQL
		for (IDomain domain : xmlTransformer.getWorkingDomains()) {
			String tpltFile = Environment.getResource("ds").getPath();
			RegionDomain template = new RegionDomain();
			template.setTemplateFile(tpltFile + File.separator + "ds_hana_ibas.xml");
			File outputFile = new File(Environment.getWorkingFolder() + File.separator + "ds_hana_ibas.out.xml");
			dataTypeMappings = DataTypeMappings
					.create(template.getTemplateFile().replace("ds_hana_ibas.xml", "dm_hana_ibas.xml"));
			ArrayList<Parameter> parameters = new ArrayList<>();
			parameters.add(new Parameter("Company", "CC"));
			parameters.add(new Parameter("DbServer", "ibas-dev-hana"));
			parameters.add(new Parameter("DbPort", "30015"));
			parameters.add(new Parameter("DbName", "ibas_demo" + "_" + domain.hashCode()));
			parameters.add(new Parameter("AppName", "btulz.transforms"));
			parameters.add(new Parameter("DbUser", "SYSTEM"));
			parameters.add(new Parameter("DbPassword", "AVAtech2015!"));
			parameters.add(new Parameter("DbTableType", "COLUMN"));
			parameters.add(new Parameter(RegionDomain.REGION_DELIMITER, domain));
			parameters.add(new Parameter(DataTypeMapping.PARAMETER_NAME, dataTypeMappings));
			template.export(parameters, outputFile);
			DataStructureOrchestration orchestration = (DataStructureOrchestration) unmarshaller.unmarshal(outputFile);
			System.out.println("orchestration: ");
			System.out.println(Serializer.toXmlString(orchestration, true));
			orchestration.execute();
		}
	}
}
