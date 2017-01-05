package org.colorcoding.tools.btulz.test.transformers;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.colorcoding.tools.btulz.Serializer;
import org.colorcoding.tools.btulz.models.IDomain;
import org.colorcoding.tools.btulz.models.data.emDataSubType;
import org.colorcoding.tools.btulz.models.data.emDataType;
import org.colorcoding.tools.btulz.templates.Parameter;
import org.colorcoding.tools.btulz.templates.Parameters;
import org.colorcoding.tools.btulz.test.Environment;
import org.colorcoding.tools.btulz.transformers.DataStructureOrchestration;
import org.colorcoding.tools.btulz.transformers.XmlTransformer;
import org.colorcoding.tools.btulz.transformers.regions.RegionDomain;
import org.colorcoding.tools.btulz.transformers.regions.models.DataTypeMapping;
import org.colorcoding.tools.btulz.transformers.regions.models.DataTypeMappings;
import org.colorcoding.tools.btulz.transformers.regions.models.Property;

import junit.framework.TestCase;

public class testRegionDomain extends TestCase {

	public void testMSSQL() throws Exception {
		XmlTransformer xmlTransformer = new XmlTransformer();
		xmlTransformer.load(Environment.getXmlModelsFileOld(), false);

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
			template.export(parameters, outputFile);
			DataStructureOrchestration orchestration = (DataStructureOrchestration) unmarshaller.unmarshal(outputFile);
			System.out.println("orchestration: ");
			System.out.println(Serializer.toXmlString(orchestration, true));
			orchestration.execute();
		}

	}

	public void testMYSQL() throws Exception {
		XmlTransformer xmlTransformer = new XmlTransformer();
		xmlTransformer.load(Environment.getXmlModelsFileOld(), false);

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
			String tpltFile = Environment.getResource("ds/ds_mysql_ibas_classic.xml").getPath();
			RegionDomain template = new RegionDomain();
			template.setTemplateFile(tpltFile);
			File outputFile = new File(
					Environment.getWorkingFolder() + File.separator + "ds_mysql_ibas_classic.out.xml");
			dataTypeMappings = DataTypeMappings.create(
					template.getTemplateFile().replace("ds_mysql_ibas_classic.xml", "dm_mysql_ibas_classic.xml"));
			Parameters parameters = new Parameters();
			parameters.add(new Parameter("Company", "CC"));
			parameters.add(new Parameter("DbServer", "ibas-dev-mysql"));
			parameters.add(new Parameter("DbPort", "3306"));
			parameters.add(new Parameter("DbName", "ibas_demo" + "_" + domain.hashCode()));
			parameters.add(new Parameter("AppName", "btulz.transforms"));
			parameters.add(new Parameter("DbUser", "root"));
			parameters.add(new Parameter("DbPassword", "1q2w3e"));
			parameters.add(new Parameter(RegionDomain.REGION_DELIMITER, domain));
			parameters.add(new Parameter(Property.PARAMETER_NAME_MAPPED_TYPE, dataTypeMappings));
			template.export(parameters, outputFile);
			DataStructureOrchestration orchestration = (DataStructureOrchestration) unmarshaller.unmarshal(outputFile);
			System.out.println("orchestration: ");
			System.out.println(Serializer.toXmlString(orchestration, true));
			orchestration.execute();
		}
	}

	public void testPGSQL() throws Exception {
		XmlTransformer xmlTransformer = new XmlTransformer();
		xmlTransformer.load(Environment.getXmlModelsFileOld(), false);

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
			String tpltFile = Environment.getResource("ds/ds_pgsql_ibas_classic.xml").getPath();
			RegionDomain template = new RegionDomain();
			template.setTemplateFile(tpltFile);
			File outputFile = new File(
					Environment.getWorkingFolder() + File.separator + "ds_pgsql_ibas_classic.out.xml");
			dataTypeMappings = DataTypeMappings.create(
					template.getTemplateFile().replace("ds_pgsql_ibas_classic.xml", "dm_pgsql_ibas_classic.xml"));
			Parameters parameters = new Parameters();
			parameters.add(new Parameter("Company", "CC"));
			parameters.add(new Parameter("DbServer", "ibas-dev-pgsql"));
			parameters.add(new Parameter("DbPort", "5432"));
			parameters.add(new Parameter("DbName", "ibas_demo" + "_" + domain.hashCode()));
			parameters.add(new Parameter("AppName", "btulz.transforms"));
			parameters.add(new Parameter("DbUser", "postgres"));
			parameters.add(new Parameter("DbPassword", "1q2w3e"));
			parameters.add(new Parameter(RegionDomain.REGION_DELIMITER, domain));
			parameters.add(new Parameter(Property.PARAMETER_NAME_MAPPED_TYPE, dataTypeMappings));
			template.export(parameters, outputFile);
			DataStructureOrchestration orchestration = (DataStructureOrchestration) unmarshaller.unmarshal(outputFile);
			System.out.println("orchestration: ");
			System.out.println(Serializer.toXmlString(orchestration, true));
			orchestration.execute();
		}
	}

	public void testHANA() throws Exception {
		XmlTransformer xmlTransformer = new XmlTransformer();
		xmlTransformer.load(Environment.getXmlModelsFileOld(), false);

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
			String tpltFile = Environment.getResource("ds/ds_hana_ibas_classic.xml").getPath();
			RegionDomain template = new RegionDomain();
			template.setTemplateFile(tpltFile);
			File outputFile = new File(
					Environment.getWorkingFolder() + File.separator + "ds_hana_ibas_classic.out.xml");
			dataTypeMappings = DataTypeMappings
					.create(template.getTemplateFile().replace("ds_hana_ibas_classic.xml", "dm_hana_ibas_classic.xml"));
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
			parameters.add(new Parameter(Property.PARAMETER_NAME_MAPPED_TYPE, dataTypeMappings));
			template.export(parameters, outputFile);
			DataStructureOrchestration orchestration = (DataStructureOrchestration) unmarshaller.unmarshal(outputFile);
			System.out.println("orchestration: ");
			System.out.println(Serializer.toXmlString(orchestration, true));
			orchestration.execute();
		}

	}
}
