package org.colorcoding.tools.btulz.test.model;

import org.colorcoding.tools.btulz.model.BusinessObject;
import org.colorcoding.tools.btulz.model.Domain;
import org.colorcoding.tools.btulz.model.IBusinessObject;
import org.colorcoding.tools.btulz.model.IBusinessObjectItem;
import org.colorcoding.tools.btulz.model.IDomain;
import org.colorcoding.tools.btulz.model.IModel;
import org.colorcoding.tools.btulz.model.IProperty;
import org.colorcoding.tools.btulz.model.Model;
import org.colorcoding.tools.btulz.model.Property;
import org.colorcoding.tools.btulz.model.data.emBORelation;
import org.colorcoding.tools.btulz.model.data.emDataSubType;
import org.colorcoding.tools.btulz.model.data.emDataType;
import org.colorcoding.tools.btulz.model.data.emModelType;

import junit.framework.TestCase;

/**
 * 领域模型测试
 *
 * 覆盖： - Domain/Model/Property/BusinessObject的创建、属性、clone、buildMapping
 *
 * 注意：Domain等模型被XmlTransformer、CodeTransformer、RegionDomain等高级功能广泛使用，
 * 此处仅保留核心代表性测试
 */
public class TestModels extends TestCase {

	/**
	 * 创建标准测试用域对象 包含：Document模型、MasterData模型、行模型，以及1:1和1:n关联
	 */
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

	/** Domain完整构建与克隆、buildMapping */
	public void testDomainBuildAndClone() throws ClassNotFoundException {
		IDomain domain = this.createDomain();
		assertEquals("TrainingTesting", domain.getName());
		assertEquals(3, domain.getModels().size());
		assertEquals(1, domain.getBusinessObjects().size());

		// Domain克隆：深拷贝
		IDomain cloned = domain.clone();
		assertEquals(domain.getName(), cloned.getName());
		cloned.setName("ClonedDomain");
		assertFalse(domain.getName().equals(cloned.getName()));

		// Domain克隆(无子对象)
		IDomain clonedNoChilds = domain.clone(true);
		assertEquals(domain.getName(), clonedNoChilds.getName());
		assertEquals(0, clonedNoChilds.getModels().size());

		// buildMapping
		domain.buildMapping();
		for (IBusinessObject bo : domain.getBusinessObjects()) {
			assertNotNull(bo.getMappedModel());
		}

		// Domain自动推导ShortName
		Domain d = new Domain();
		d.setName("TestDomain");
		IBusinessObject bo2 = d.getBusinessObjects().create();
		bo2.setShortName("CC_TT_ORDER");
		assertEquals("TT", d.getShortName());
	}

	/** Model/Property/BusinessObject创建、属性、clone */
	public void testModelPropertyBusinessObject() {
		// Model
		Model model = new Model();
		model.setName("TestModel");
		model.setModelType(emModelType.Document);
		model.setMapped("CC_TEST_TABLE");
		model.setEntity(true);
		assertEquals("TestModel", model.getName());
		assertEquals(emModelType.Document, model.getModelType());
		assertEquals("CC_TEST_TABLE", model.getMapped());
		IModel clonedModel = model.clone();
		assertEquals(model.getName(), clonedModel.getName());

		// Property
		Property prop = new Property();
		prop.setName("DocEntry");
		prop.setDataType(emDataType.Numeric);
		prop.setPrimaryKey(true);
		prop.setUniqueKey(true);
		prop.setSearchKey(true);
		prop.setDeclaredType("Integer");
		prop.setMapped("DocEntry");
		assertTrue(prop.isPrimaryKey());
		assertTrue(prop.isUniqueKey());
		assertTrue(prop.isSearchKey());
		assertEquals(emDataType.Numeric, prop.getDataType());
		IProperty clonedProp = prop.clone();
		assertEquals(prop.getName(), clonedProp.getName());
		assertTrue(clonedProp.isPrimaryKey());

		// Property默认值
		Property defaultProp = new Property();
		assertEquals(emDataType.Alphanumeric, defaultProp.getDataType());
		assertEquals(emDataSubType.Default, defaultProp.getDataSubType());
		assertFalse(defaultProp.isPrimaryKey());

		// BusinessObject
		BusinessObject bo = new BusinessObject();
		bo.setName("SalesOrder");
		bo.setShortName("CC_ORDER");
		bo.setMappedModel("SalesOrder");
		assertEquals("SalesOrder", bo.getName());
		assertEquals("CC_ORDER", bo.getShortName());
		// getName()在name为null时返回mappedModel
		BusinessObject bo2 = new BusinessObject();
		bo2.setMappedModel("Order");
		assertEquals("Order", bo2.getName());
		// 通过Model设置映射
		BusinessObject bo3 = new BusinessObject();
		Model m = new Model();
		m.setName("Order");
		m.setDescription("订单");
		bo3.setMappedModel(m);
		assertEquals("Order", bo3.getMappedModel());
		assertEquals("订单", bo3.getDescription());
		IBusinessObject clonedBo = bo.clone();
		assertEquals(bo.getName(), clonedBo.getName());
	}
}
