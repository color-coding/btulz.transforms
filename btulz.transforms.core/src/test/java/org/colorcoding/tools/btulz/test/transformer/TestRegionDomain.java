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

public class TestRegionDomain extends TestCase {

	public void testMSSQL() throws Exception {
		XmlTransformer xmlTransformer = new XmlTransformer();
		xmlTransformer.load(Environment.getWorkingFolder() + Environment.getXmlModelsFileOld(), false);

		JAXBContext context = JAXBContext.newInstance(DataStructureOrchestration.class);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		// 测试MSSQL
		for (IDomain domain : xmlTransformer.getWorkingDomains()) {
			String tpltFile = Environment.getResource("ds/ds_mssql_ibas_classic.xml").getPath();
			RegionDomain template = new RegionDomain();
			template.setTemplateFile(tpltFile);
			File outputFile = new File(
					Environment.getWorkingFolder() + File.separator + "ds_mssql_ibas_classic.out.xml");
			Parameters parameters = new Parameters();
			parameters.add(new Parameter("Company", "CC"));
			parameters.add(new Parameter("DbServer", "ibas-dev-mssql"));
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

	public void testMYSQL() throws Exception {
		XmlTransformer xmlTransformer = new XmlTransformer();
		xmlTransformer.load(Environment.getWorkingFolder() + Environment.getXmlModelsFileOld(), false);

		JAXBContext context = JAXBContext.newInstance(DataStructureOrchestration.class);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		// 测试MSSQL
		for (IDomain domain : xmlTransformer.getWorkingDomains()) {
			String tpltFile = Environment.getResource("ds/ds_mysql_ibas_classic.xml").getPath();
			RegionDomain template = new RegionDomain();
			template.setTemplateFile(tpltFile);
			File outputFile = new File(
					Environment.getWorkingFolder() + File.separator + "ds_mysql_ibas_classic.out.xml");
			Parameters parameters = new Parameters();
			parameters.add(new Parameter("Company", "CC"));
			parameters.add(new Parameter("DbServer", "ibas-dev-mysql"));
			parameters.add(new Parameter("DbPort", "3306"));
			parameters.add(new Parameter("DbName", "ibas_demo" + "_" + domain.hashCode()));
			parameters.add(new Parameter("AppName", "btulz.transforms"));
			parameters.add(new Parameter("DbUser", "root"));
			parameters.add(new Parameter("DbPassword", "1q2w3e"));
			parameters.add(new Parameter(RegionDomain.REGION_DELIMITER, domain));
			parameters.add(new Parameter(ParametersFactory.PARAMETER_NAME_OUTPUT_MAPPING,
					OutputMappingList.create(Environment.getResource("ds/dm_mysql_ibas_classic.xml").getPath())));
			template.export(parameters, outputFile);
			DataStructureOrchestration orchestration = (DataStructureOrchestration) unmarshaller.unmarshal(outputFile);
			System.out.println("orchestration: ");
			System.out.println(Serializer.toXmlString(orchestration, true));
			orchestration.execute();
		}
	}

	public void testPGSQL() throws Exception {
		XmlTransformer xmlTransformer = new XmlTransformer();
		xmlTransformer.load(Environment.getWorkingFolder() + Environment.getXmlModelsFileOld(), false);

		JAXBContext context = JAXBContext.newInstance(DataStructureOrchestration.class);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		// 测试MSSQL
		for (IDomain domain : xmlTransformer.getWorkingDomains()) {
			String tpltFile = Environment.getResource("ds/ds_pgsql_ibas_classic.xml").getPath();
			RegionDomain template = new RegionDomain();
			template.setTemplateFile(tpltFile);
			File outputFile = new File(
					Environment.getWorkingFolder() + File.separator + "ds_pgsql_ibas_classic.out.xml");
			Parameters parameters = new Parameters();
			parameters.add(new Parameter("Company", "CC"));
			parameters.add(new Parameter("DbServer", "ibas-dev-pgsql"));
			parameters.add(new Parameter("DbPort", "5432"));
			parameters.add(new Parameter("DbName", "ibas_demo" + "_" + domain.hashCode()));
			parameters.add(new Parameter("AppName", "btulz.transforms"));
			parameters.add(new Parameter("DbUser", "postgres"));
			parameters.add(new Parameter("DbPassword", "1q2w3e"));
			parameters.add(new Parameter(RegionDomain.REGION_DELIMITER, domain));
			parameters.add(new Parameter(ParametersFactory.PARAMETER_NAME_OUTPUT_MAPPING,
					OutputMappingList.create(Environment.getResource("ds/dm_pgsql_ibas_classic.xml").getPath())));
			template.export(parameters, outputFile);
			DataStructureOrchestration orchestration = (DataStructureOrchestration) unmarshaller.unmarshal(outputFile);
			System.out.println("orchestration: ");
			System.out.println(Serializer.toXmlString(orchestration, true));
			orchestration.execute();
		}
	}

	public void testHANA() throws Exception {
		XmlTransformer xmlTransformer = new XmlTransformer();
		xmlTransformer.load(Environment.getWorkingFolder() + Environment.getXmlModelsFileOld(), false);

		JAXBContext context = JAXBContext.newInstance(DataStructureOrchestration.class);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		// 测试MSSQL
		for (IDomain domain : xmlTransformer.getWorkingDomains()) {
			String tpltFile = Environment.getResource("ds/ds_hana_ibas_classic.xml").getPath();
			RegionDomain template = new RegionDomain();
			template.setTemplateFile(tpltFile);
			File outputFile = new File(
					Environment.getWorkingFolder() + File.separator + "ds_hana_ibas_classic.out.xml");
			Parameters parameters = new Parameters();
			parameters.add(new Parameter("Company", "CC"));
			parameters.add(new Parameter("DbServer", "ibas-dev-hana"));
			parameters.add(new Parameter("DbPort", "30015"));
			parameters.add(new Parameter("DbName", "ibas_demo" + "_" + domain.hashCode()));
			parameters.add(new Parameter("AppName", "btulz.transforms"));
			parameters.add(new Parameter("DbUser", "SYSTEM"));
			parameters.add(new Parameter("DbPassword", "AVAtech2015!"));
			parameters.add(new Parameter("DbTableType", "COLUMN"));
			parameters.add(new Parameter(RegionDomain.REGION_DELIMITER, domain));
			parameters.add(new Parameter(ParametersFactory.PARAMETER_NAME_OUTPUT_MAPPING,
					OutputMappingList.create(Environment.getResource("ds/dm_hana_ibas_classic.xml").getPath())));
			template.export(parameters, outputFile);
			DataStructureOrchestration orchestration = (DataStructureOrchestration) unmarshaller.unmarshal(outputFile);
			System.out.println("orchestration: ");
			System.out.println(Serializer.toXmlString(orchestration, true));
			orchestration.execute();
		}
	}

	public void testSQLITE() throws Exception {
		XmlTransformer xmlTransformer = new XmlTransformer();
		xmlTransformer.load(Environment.getWorkingFolder() + Environment.getXmlModelsFileOld(), false);

		JAXBContext context = JAXBContext.newInstance(DataStructureOrchestration.class);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		// 测试MSSQL
		for (IDomain domain : xmlTransformer.getWorkingDomains()) {
			String tpltFile = Environment.getResource("ds/ds_sqlite_ibas_classic.xml").getPath();
			RegionDomain template = new RegionDomain();
			template.setTemplateFile(tpltFile);
			File outputFile = new File(
					Environment.getWorkingFolder() + File.separator + "ds_sqlite_ibas_classic.out.xml");
			Parameters parameters = new Parameters();
			parameters.add(new Parameter("Company", "CC"));
			parameters.add(new Parameter("DbServer", "file:///"));
			parameters.add(new Parameter("DbPort", "-1"));
			parameters.add(new Parameter("DbName", "ibas_demo" + "_" + domain.hashCode()));
			parameters.add(new Parameter("AppName", "btulz.transforms"));
			parameters.add(new Parameter("DbUser", "admin"));
			parameters.add(new Parameter("DbPassword", "1q2w3e"));
			parameters.add(new Parameter(RegionDomain.REGION_DELIMITER, domain));
			parameters.add(new Parameter(ParametersFactory.PARAMETER_NAME_OUTPUT_MAPPING,
					OutputMappingList.create(Environment.getResource("ds/dm_sqlite_ibas_classic.xml").getPath())));
			template.export(parameters, outputFile);
			DataStructureOrchestration orchestration = (DataStructureOrchestration) unmarshaller.unmarshal(outputFile);
			System.out.println("orchestration: ");
			System.out.println(Serializer.toXmlString(orchestration, true));
			orchestration.execute();
		}

	}

}
