package org.colorcoding.tools.btulz.test.model;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.colorcoding.tools.btulz.model.Domain;
import org.colorcoding.tools.btulz.model.IBusinessObject;
import org.colorcoding.tools.btulz.model.IBusinessObjectItem;
import org.colorcoding.tools.btulz.model.IDomain;
import org.colorcoding.tools.btulz.model.IModel;
import org.colorcoding.tools.btulz.model.IProperty;
import org.colorcoding.tools.btulz.model.data.emBORelation;
import org.colorcoding.tools.btulz.model.data.emDataType;
import org.colorcoding.tools.btulz.model.data.emModelType;

import junit.framework.TestCase;

public class TestModels extends TestCase {

	public IDomain createDomain() throws ClassNotFoundException {
		IDomain domain = new Domain();
		domain.setName("TrainingTesting");
		domain.setShortName("TT");
		domain.setDescription("培训&测试");

		IProperty propertyData;

		IModel orderModel = domain.getModels().create();
		orderModel.setModelType(emModelType.Document);
		orderModel.setName("SalesOrder");
		orderModel.setDescription("销售订单");
		orderModel.setMapped("CC_TT_ORDR");
		propertyData = orderModel.getProperties().create();
		propertyData.setName("DocumentEntry");
		propertyData.setDescription("单据编号");
		propertyData.setDataType(emDataType.Numeric);
		propertyData.setPrimaryKey(true);
		propertyData.setMapped("DocEntry");

		IModel userModel = domain.getModels().create();
		userModel.setModelType(emModelType.MasterData);
		userModel.setName("User");
		userModel.setDescription("用户");
		userModel.setMapped("CC_TT_OUSR");
		propertyData = userModel.getProperties().create();
		propertyData.setName("UserCode");
		propertyData.setDescription("用户编码");
		propertyData.setDataType(emDataType.Alphanumeric);
		propertyData.setPrimaryKey(true);
		propertyData.setMapped("Code");

		IModel modelLine = domain.getModels().create();
		modelLine.setModelType(emModelType.MasterData);
		modelLine.setName("SalesOrderLine");
		modelLine.setDescription("销售订单行");
		modelLine.setMapped("CC_TT_RDR1");
		propertyData = modelLine.getProperties().create();
		propertyData.setName("DocumentEntry");
		propertyData.setDescription("单据编号");
		propertyData.setDataType(emDataType.Numeric);
		propertyData.setPrimaryKey(true);
		propertyData.setMapped("DocEntry");
		propertyData = modelLine.getProperties().create();
		propertyData.setName("DocumentLine");
		propertyData.setDescription("单据行号");
		propertyData.setDataType(emDataType.Numeric);
		propertyData.setPrimaryKey(true);
		propertyData.setMapped("LineId");

		// 构建业务对象
		IBusinessObject bo = domain.getBusinessObjects().create();
		bo.setMappedModel(orderModel);
		bo.setShortName("CC_ORDER");
		IBusinessObjectItem boItem = bo.getRelatedBOs().create();
		boItem.setMappedModel(userModel);// 1:1
		boItem.setRelation(emBORelation.OneToOne);
		boItem = bo.getRelatedBOs().create();
		boItem.setMappedModel(modelLine);// 1:n
		boItem.setRelation(emBORelation.OneToMany);
		boItem = boItem.getRelatedBOs().create();
		boItem.setMappedModel(userModel);// 1：1，孙子
		boItem.setRelation(emBORelation.OneToOne);

		propertyData.clone();
		userModel.clone();
		bo.clone();
		domain.clone();
		return domain;
	}

	public void testDomainModels()
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, JAXBException {
		IDomain domain = this.createDomain();

		JAXBContext context = JAXBContext.newInstance(domain.getClass());
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
		StringWriter writer = new StringWriter();
		marshaller.marshal(domain, writer);
		String oldXML = writer.toString();
		System.out.println("序列化输出：");
		System.out.println(oldXML);

	}

}
