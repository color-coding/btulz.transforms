package org.colorcoding.tools.btulz.test.transformers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import junit.framework.TestCase;

public class testExcelTransformer extends TestCase {

	public void testJDBC() throws ClassNotFoundException, SQLException {
		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
		Connection connection = DriverManager.getConnection(
				"jdbc:odbc:driver={Microsoft Excel Driver (*.xls, *.xlsx, *.xlsm, *.xlsb)};DBQ=c:\\l_f3.201004.xlsx"); // 不设置数据源

		Statement statement = connection.createStatement();
		String query = "SELECT * FROM [sc$] ";
		ResultSet rs = statement.executeQuery(query);
		int i = 0;
		while (rs.next()) {
			i++;
			System.out.println(rs.getString("用户到达清单") + i);// 用户到达清单为列名
		}
	}
}
