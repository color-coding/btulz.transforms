package org.colorcoding.tools.btulz.transformers;

import java.lang.reflect.Method;

import org.colorcoding.tools.btulz.models.Domain;
import org.colorcoding.tools.btulz.models.IBusinessObject;
import org.colorcoding.tools.btulz.models.IBusinessObjectItem;
import org.colorcoding.tools.btulz.models.IDomain;
import org.colorcoding.tools.btulz.models.IModel;
import org.colorcoding.tools.btulz.models.IProperty;
import org.colorcoding.tools.btulz.models.data.emBORelation;
import org.colorcoding.tools.btulz.models.data.emDataSubType;
import org.colorcoding.tools.btulz.models.data.emDataType;
import org.colorcoding.tools.btulz.models.data.emModelType;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 默认解释器
 * 
 * @author Niuren.Zhu
 *
 */
class XmlParser implements IXmlParser {

	protected String xml_element_sign_domain = "Domain";
	protected String xml_element_sign_model = "Model";
	protected String xml_element_sign_property = "Property";
	protected String xml_element_sign_businessobject = "BusinessObject";
	protected String xml_element_sign_businessobject_item = "RelatedBO";

	@Override
	public IDomain parse(Node root) {
		if (root == null) {
			return null;
		}
		if (root.getNodeType() != Node.ELEMENT_NODE) {
			// 非元素节点不处理
			return null;
		}
		// 领域赋值
		IDomain domain = new Domain();
		this.setValues(root.getAttributes(), domain);
		for (int i = 0; i < root.getChildNodes().getLength(); i++) {
			Node node = root.getChildNodes().item(i);
			if (node.getNodeType() != Node.ELEMENT_NODE) {
				// 非元素节点不处理
				continue;
			}
			// 模型赋值
			if (node.getNodeName().equals(this.xml_element_sign_model)) {
				IModel model = domain.getModels().create();
				this.setValues(node.getAttributes(), model);
				for (int j = 0; j < node.getChildNodes().getLength(); j++) {
					Node childNode = node.getChildNodes().item(j);
					if (childNode.getNodeType() != Node.ELEMENT_NODE) {
						// 非元素节点不处理
						continue;
					}
					if (!childNode.getNodeName().equals(this.xml_element_sign_property)) {
						continue;
					}
					IProperty property = model.getProperties().create();
					this.setValues(childNode.getAttributes(), property);
				}
			}
			// 业务对象赋值
			if (node.getNodeName().equals(this.xml_element_sign_businessobject)) {
				IBusinessObject businessObject = domain.getBusinessObjects().create();
				this.setValues(node.getAttributes(), businessObject);
				this.setValues(node.getChildNodes(), businessObject);
			}
		}
		return domain;
	}

	protected void setValues(NodeList nodes, IBusinessObject businessObject) {
		for (int j = 0; j < nodes.getLength(); j++) {
			Node childNode = nodes.item(j);
			if (childNode.getNodeType() != Node.ELEMENT_NODE) {
				// 非元素节点不处理
				continue;
			}
			if (!childNode.getNodeName().equals(this.xml_element_sign_businessobject_item)) {
				continue;
			}
			IBusinessObject subBO = businessObject.getRelatedBOs().create();
			this.setValues(childNode.getAttributes(), subBO);
			this.setValues(childNode.getChildNodes(), subBO);
		}
	}

	protected void setValues(NamedNodeMap arrts, Object object) {
		if (arrts == null || object == null) {
			return;
		}
		Class<?> tmpClass = object.getClass();
		Method[] methods = tmpClass.getMethods();
		for (int i = 0; i < arrts.getLength(); i++) {
			Node childNode = arrts.item(i);
			if (childNode.getNodeType() != Node.ATTRIBUTE_NODE) {
				// 非属性节点不处理
				continue;
			}
			for (Method method : methods) {
				if (!method.getName().equals(String.format("set%s", arrts.item(i).getNodeName()))) {
					continue;
				}
				if (method.getParameterTypes().length != 1) {
					continue;
				}
				Class<?> type = method.getParameterTypes()[0];
				if (type.isInterface() || type.isLocalClass()) {
					continue;
				}
				try {
					method.invoke(object, this.convert(type, childNode.getNodeValue()));
				} catch (Exception e) {
					System.out.println(String.format("setting [%s]'s value occurred error %s",
							arrts.item(i).getNodeName(), e.getMessage()));
				}
				break;
			}
		}
	}
}

/**
 * 
 * 旧版解释器
 * 
 * @author Niuren.Zhu
 *
 */
class XmlParser1 extends XmlParser {
	public XmlParser1() {
		this.xml_element_sign_domain = "DomainModel";
		this.xml_element_sign_model = "Table";
		this.xml_element_sign_property = "Field";
		this.xml_element_sign_businessobject = "BusinessObject";
		this.xml_element_sign_businessobject_item = "ChildTables";
	}

	@Override
	protected void setValues(NamedNodeMap arrts, Object object) {
		if (object instanceof IDomain) {
			this.setValues(arrts, (IDomain) object);
		} else if (object instanceof IModel) {
			this.setValues(arrts, (IModel) object);
		} else if (object instanceof IProperty) {
			this.setValues(arrts, (IProperty) object);
		} else if (object instanceof IBusinessObject) {
			this.setValues(arrts, (IBusinessObject) object);
		}
	}

	@SuppressWarnings("unchecked")
	public <P> P convert(Class<P> type, String value) {
		if (type == emModelType.class) {
			if (value != null) {
				if (value.equals("bott_MasterData")) {
					return (P) emModelType.MasterData;
				} else if (value.equals("bott_MasterDataLines")) {
					return (P) emModelType.MasterDataLine;
				} else if (value.equals("bott_Document")) {
					return (P) emModelType.Document;
				} else if (value.equals("bott_DocumentLines")) {
					return (P) emModelType.DocumentLine;
				} else if (value.equals("bott_SimpleObject")) {
					return (P) emModelType.Simple;
				} else if (value.equals("bott_SimpleObjectLines")) {
					return (P) emModelType.SimpleLine;
				} else if (value.equals("bott_SimpleBusinessObject")) {
					return (P) emModelType.Simple;
				} else if (value.equals("bott_SimpleBusinessObjectLines")) {
					return (P) emModelType.SimpleLine;
				}
			}
			return (P) emModelType.Unspecified;
		} else if (type == emDataType.class) {
			if (value != null) {
				if (value.equals("db_Alpha")) {
					return (P) emDataType.Alphanumeric;
				} else if (value.equals("db_Memo")) {
					return (P) emDataType.Memo;
				} else if (value.equals("db_Numeric")) {
					return (P) emDataType.Numeric;
				} else if (value.equals("db_Date")) {
					return (P) emDataType.Date;
				} else if (value.equals("db_Float")) {
					return (P) emDataType.Decimal;
				} else if (value.equals("db_Bytes")) {
					return (P) emDataType.Bytes;
				}
			}
			return (P) emDataType.Unknown;
		} else if (type == emDataSubType.class) {
			if (value != null) {
				if (value.equals("st_None")) {
					return (P) emDataSubType.Default;
				} else if (value.equals("st_Address")) {
					return (P) emDataSubType.Address;
				} else if (value.equals("st_Phone")) {
					return (P) emDataSubType.Phone;
				} else if (value.equals("st_Time")) {
					return (P) emDataSubType.Time;
				} else if (value.equals("st_Rate")) {
					return (P) emDataSubType.Rate;
				} else if (value.equals("st_Sum")) {
					return (P) emDataSubType.Sum;
				} else if (value.equals("st_Price")) {
					return (P) emDataSubType.Price;
				} else if (value.equals("st_Quantity")) {
					return (P) emDataSubType.Quantity;
				} else if (value.equals("st_Percentage")) {
					return (P) emDataSubType.Percentage;
				} else if (value.equals("st_Measurement")) {
					return (P) emDataSubType.Measurement;
				} else if (value.equals("st_Link")) {
					return (P) emDataSubType.Link;
				} else if (value.equals("st_Image")) {
					return (P) emDataSubType.Image;
				} else if (value.equals("st_Email")) {
					return (P) emDataSubType.Email;
				} else if (value.equals("st_Website")) {
					return (P) emDataSubType.Link;
				}
			}
			return (P) emDataSubType.Default;
		} else if (type == emBORelation.class) {
			return (P) emBORelation.OneToMany;
		} else {
			return super.convert(type, value);
		}
	}

	protected void setValues(NamedNodeMap arrts, IDomain object) {
		for (int i = 0; i < arrts.getLength(); i++) {
			Node node = arrts.item(i);
			if (node.getNodeType() != Node.ATTRIBUTE_NODE) {
				continue;
			}
			if (node.getNodeName().equals("Name")) {
				object.setName(this.convert(String.class, node.getNodeValue()));
			} else if (node.getNodeName().equals("ShortName")) {
				object.setShortName(this.convert(String.class, node.getNodeValue()));
			}
		}
	}

	protected void setValues(NamedNodeMap arrts, IModel object) {
		for (int i = 0; i < arrts.getLength(); i++) {
			Node node = arrts.item(i);
			if (node.getNodeType() != Node.ATTRIBUTE_NODE) {
				continue;
			}
			if (node.getNodeName().equals("PropertyName")) {
				object.setName(this.convert(String.class, node.getNodeValue()));
			} else if (node.getNodeName().equals("Description")) {
				object.setDescription(this.convert(String.class, node.getNodeValue()));
			} else if (node.getNodeName().equals("Name")) {
				object.setMapped(this.convert(String.class, node.getNodeValue()));
			} else if (node.getNodeName().equals("Type")) {
				object.setModelType(this.convert(emModelType.class, node.getNodeValue()));
			}
		}
	}

	protected void setValues(NamedNodeMap arrts, IProperty object) {
		for (int i = 0; i < arrts.getLength(); i++) {
			Node node = arrts.item(i);
			if (node.getNodeType() != Node.ATTRIBUTE_NODE) {
				continue;
			}
			if (node.getNodeName().equals("PropertyName")) {
				object.setName(this.convert(String.class, node.getNodeValue()));
			} else if (node.getNodeName().equals("Description")) {
				object.setDescription(this.convert(String.class, node.getNodeValue()));
			} else if (node.getNodeName().equals("Name")) {
				object.setMapped(this.convert(String.class, node.getNodeValue()));
			} else if (node.getNodeName().equals("DataType")) {
				object.setDataType(this.convert(emDataType.class, node.getNodeValue()));
			} else if (node.getNodeName().equals("SubType")) {
				object.setDataSubType(this.convert(emDataSubType.class, node.getNodeValue()));
			} else if (node.getNodeName().equals("EditSize")) {
				object.setEditSize(this.convert(int.class, node.getNodeValue()));
			} else if (node.getNodeName().equals("IsPrimaryKey")) {
				object.setPrimaryKey(this.convert(boolean.class, node.getNodeValue()));
			} else if (node.getNodeName().equals("IsUnique")) {
				object.setUniqueKey(this.convert(boolean.class, node.getNodeValue()));
			} else if (node.getNodeName().equals("Linked")) {
				object.setLinked(this.convert(String.class, node.getNodeValue()));
			} else if (node.getNodeName().equals("FixedDataType")) {
				if (node.getNodeValue() != null) {
					String value = node.getNodeValue();
					if (value.indexOf(".") > 0) {
						// 去除前缀
						String[] tmps = value.split("\\.");
						value = tmps[tmps.length - 1];
					}
					if (value.startsWith("em")) {
						// 仅枚举类型时赋值
						object.setDeclaredType(this.convert(String.class, value));
					}
				}
			}
		}
	}

	protected void setValues(NamedNodeMap arrts, IBusinessObject object) {
		for (int i = 0; i < arrts.getLength(); i++) {
			Node node = arrts.item(i);
			if (node.getNodeType() != Node.ATTRIBUTE_NODE) {
				continue;
			}
			if (node.getNodeName().equals("PropertyName")) {
				object.setMappedModel(this.convert(String.class, node.getNodeValue()));
			} else if (node.getNodeName().equals("Name")) {
				object.setDescription(this.convert(String.class, node.getNodeValue()));
			} else if (node.getNodeName().equals("Code")) {
				object.setShortName(this.convert(String.class, node.getNodeValue()));
			}
		}
	}

	@Override
	protected void setValues(NodeList nodes, IBusinessObject businessObject) {
		for (int j = 0; j < nodes.getLength(); j++) {
			Node childNode = nodes.item(j);
			if (childNode.getNodeType() != Node.ELEMENT_NODE) {
				// 非元素节点不处理
				continue;
			}
			if (!childNode.getNodeName().equals(this.xml_element_sign_businessobject_item)) {
				continue;
			}
			for (int i = 0; i < childNode.getChildNodes().getLength(); i++) {
				Node node = childNode.getChildNodes().item(i);
				if (node.getNodeType() != Node.ELEMENT_NODE) {
					// 非元素节点不处理
					continue;
				}
				Node atr = node.getAttributes().getNamedItem("TableName");
				if (atr != null) {
					IBusinessObject subBO = businessObject.getRelatedBOs().create();
					subBO.setShortName(atr.getNodeValue());
				}
			}

		}
	}

	@Override
	public IDomain parse(Node root) {
		IDomain domain = super.parse(root);
		if (domain != null) {
			// 修正业务对象子项
			for (IBusinessObject bo : domain.getBusinessObjects()) {
				for (IBusinessObjectItem boItem : bo.getRelatedBOs()) {
					if ((boItem.getMappedModel() == null || boItem.getMappedModel().isEmpty())
							&& boItem.getShortName() != null && !boItem.getShortName().isEmpty()) {
						for (IModel model : domain.getModels()) {
							if (model.getMapped() != null && model.getMapped().equals(boItem.getShortName())) {
								boItem.setShortName(null);
								boItem.setMappedModel(model);
							}
						}
					}
				}
			}
		}
		return domain;
	}

}