package org.colorcoding.tools.btulz.test.template;

import org.colorcoding.tools.btulz.template.Parameter;
import org.colorcoding.tools.btulz.template.Parameters;

import junit.framework.TestCase;

/**
 * 参数(Parameter)与参数集合(Parameters)测试
 *
 * 覆盖： - Parameter构造、create工厂、路径取值getValue(path) - Parameters去重、按名获取、类型获取、批量添加
 *
 * 注意：Parameter/Parameters被CodeTransformer、RegionDomain等高级功能广泛使用
 */
public class TestParameter extends TestCase {

	/** Parameter构造与create工厂方法 */
	public void testParameterCreateAndConstructor() {
		Parameter p = new Parameter("Company", "CC");
		assertEquals("Company", p.getName());
		assertEquals("CC", p.getValue());

		Parameter p2 = Parameter.create("{\"name\":\"Company\",\"value\":\"CC\"}");
		assertNotNull(p2);
		assertEquals("Company", p2.getName());
		assertEquals("CC", p2.getValue());

		assertNull(Parameter.create(null));
		assertNull(Parameter.create(""));
	}

	/** Parameter路径取值：调用对象方法 */
	public void testGetValue_MethodPath() throws Exception {
		Parameter p = new Parameter("Test", "hello world");
		assertEquals("hello world", p.getValue(""));
		assertEquals("hello world", p.getValue((String) null));
		assertEquals(11, p.getValue("length()"));
		assertEquals("hello", p.getValue("toString()"));
	}

	/** Parameters去重、按名获取、类型获取、批量添加 */
	public void testParametersOperations() {
		Parameters params = new Parameters();
		params.add(new Parameter("Company", "CC"));
		params.add(new Parameter("Company", "XX")); // 同名替换
		params.add(null); // 忽略null
		assertEquals(1, params.size());
		assertEquals("XX", params.get("Company").getValue());
		assertEquals("XX", params.get("company").getValue()); // 忽略大小写
		assertNull(params.get("NotExist"));

		params.add(new Parameter("Count", 42));
		assertEquals(Integer.valueOf(42), params.getValue("Count", Integer.class));
		assertNull(params.getValue("Count", String.class)); // 类型不匹配

		Parameters params2 = new Parameters();
		params2.add(new Parameter("A", "10"));
		params2.add(new Parameter("C", "3"));
		params.addAll(params2);
		assertEquals(4, params.size());
	}
}
