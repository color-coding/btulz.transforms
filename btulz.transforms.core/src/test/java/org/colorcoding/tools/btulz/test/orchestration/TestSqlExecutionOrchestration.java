package org.colorcoding.tools.btulz.test.orchestration;

import org.colorcoding.tools.btulz.Serializer;
import org.colorcoding.tools.btulz.orchestration.ISqlExecutionAction;
import org.colorcoding.tools.btulz.orchestration.ISqlExecutionActionStep;
import org.colorcoding.tools.btulz.orchestration.SqlExecutionOrchestration;

import junit.framework.TestCase;

public class TestSqlExecutionOrchestration extends TestCase {

	public void testExecution() throws Exception {

		SqlExecutionOrchestration orchestration = new SqlExecutionOrchestration();
		orchestration.setName("test");
		orchestration.setDescription("MSSQL的测试");
		orchestration.setDriverName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		orchestration.setDbUrl(String.format("jdbc:sqlserver://%s;DatabaseName=%s;ApplicationName=%s", "localhost",
				"ibcp_demo", "btulz_transforms"));
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
