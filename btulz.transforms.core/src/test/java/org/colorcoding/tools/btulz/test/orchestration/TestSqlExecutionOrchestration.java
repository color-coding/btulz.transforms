package org.colorcoding.tools.btulz.test.orchestration;

import org.colorcoding.tools.btulz.Serializer;
import org.colorcoding.tools.btulz.orchestration.ISqlExecutionAction;
import org.colorcoding.tools.btulz.orchestration.ISqlExecutionActionStep;
import org.colorcoding.tools.btulz.orchestration.SqlExecutionOrchestration;

import junit.framework.TestCase;

/**
 * SQL执行编排测试
 *
 * 覆盖： - 创建SqlExecutionOrchestration编排，属性设置 - 添加Action和Step，序列化输出 -
 * 设置Step的条件执行（runOnValue），执行编排
 *
 * 注意：依赖外部MSSQL数据库环境
 */
public class TestSqlExecutionOrchestration extends TestCase {

	/** 编排属性设置与序列化 */
	public void testOrchestrationPropertiesAndSerialization() throws Exception {
		SqlExecutionOrchestration orchestration = new SqlExecutionOrchestration();
		orchestration.setName("serialization_test");
		orchestration.setDescription("序列化测试");
		orchestration.setDriverName("com.mysql.jdbc.Driver");
		orchestration.setDbUrl("jdbc:mysql://localhost:3306/test");
		orchestration.setDbUser("root");
		orchestration.setDbPassword("123456");

		assertEquals("serialization_test", orchestration.getName());
		assertEquals("com.mysql.jdbc.Driver", orchestration.getDriverName());

		ISqlExecutionAction action = orchestration.getActions().create();
		action.setName("test_action");
		ISqlExecutionActionStep step = action.getSteps().create();
		step.setName("test_step");
		step.setScript("SELECT 1");

		String xml = Serializer.toXmlString(orchestration, true);
		assertNotNull(xml);
		assertTrue(xml.contains("serialization_test"));
		assertTrue(xml.contains("test_action"));
	}

	/** 完整的SQL执行编排：创建编排→添加步骤→执行→序列化输出 */
	public void testExecution() throws Exception {
		SqlExecutionOrchestration orchestration = new SqlExecutionOrchestration();
		orchestration.setName("test");
		orchestration.setDescription("MSSQL的测试");
		orchestration.setDriverName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		orchestration.setDbUrl(String.format("jdbc:sqlserver://%s;DatabaseName=%s;ApplicationName=%s;encrypt=false",
				"localhost", "ibas_demo", "btulz_transforms"));
		orchestration.setDbUser("sa");
		orchestration.setDbPassword("1q2w3e");
		ISqlExecutionAction action = orchestration.getActions().create();
		action.setName("query");
		ISqlExecutionActionStep step = action.getSteps().create();
		step.setName("check db exists");
		step.setScript("select 0 from sys.databases where name = 'ibas_demo'");
		step = action.getSteps().create();
		step.setRunOnValue("0");// 状态值为0时运行此步骤
		step.setName("db exists and check table exists");
		step.setScript("select 1 from sys.sysobjects where name = 'cc_tt_user' and type = 'u'");
		step = action.getSteps().create();
		step.setRunOnValue("1");// 状态值为1时运行此步骤
		step.setName("table exists and query");
		step.setScript("select * from ibas_demo..cc_tt_user");
		orchestration.execute();

		System.out.println(Serializer.toXmlString(orchestration, true));
	}
}
