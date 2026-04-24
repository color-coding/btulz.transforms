package org.colorcoding.tools.btulz.test.template;

import org.colorcoding.tools.btulz.template.Variable;

import junit.framework.TestCase;

/**
 * 变量(Variable)测试
 *
 * 覆盖： - discerning：从字符串中识别${...}变量，解析名称和路径 - isLowerCase：判断值路径是否全小写 - 原始值保留、值设置
 *
 * 注意：Variable被模板系统（RegionDomain等）广泛使用
 */
public class TestVariable extends TestCase {

	/** 变量识别：简单变量、带路径、多变量、边界值 */
	public void testDiscerning() {
		Variable[] vars = Variable.discerning("Hello ${Name} World");
		assertEquals(1, vars.length);
		assertEquals("Name", vars[0].getName());
		assertNull(vars[0].getValuePath());

		vars = Variable.discerning("${Model.getMapped()}");
		assertEquals(1, vars.length);
		assertEquals("Model", vars[0].getName());
		assertEquals("getMapped()", vars[0].getValuePath());

		vars = Variable.discerning("${Model.getName()}-${Property.getMapped()}");
		assertEquals(2, vars.length);
		assertEquals("Model", vars[0].getName());
		assertEquals("Property", vars[1].getName());

		assertEquals(0, Variable.discerning("Hello World").length);
		assertEquals(0, Variable.discerning(null).length);
		assertEquals(0, Variable.discerning("").length);
	}

	/** isLowerCase与原始值保留 */
	public void testIsLowerCaseAndOriginal() {
		Variable var = new Variable();
		var.setValuePath("name");
		assertTrue(var.isLowerCase());
		var.setValuePath("getName()");
		assertFalse(var.isLowerCase());
		var.setValuePath(null);
		assertFalse(var.isLowerCase());

		Variable[] vars = Variable.discerning("${Model.getName()}");
		assertEquals("${Model.getName()}", vars[0].getOriginal());
	}
}
