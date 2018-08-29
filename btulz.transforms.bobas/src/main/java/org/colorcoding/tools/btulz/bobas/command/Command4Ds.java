package org.colorcoding.tools.btulz.bobas.command;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.colorcoding.ibas.bobas.MyConfiguration;
import org.colorcoding.ibas.bobas.configuration.ConfigurationManagerFile;
import org.colorcoding.tools.btulz.command.Argument;
import org.colorcoding.tools.btulz.command.Command;
import org.colorcoding.tools.btulz.command.Prompt;
import org.colorcoding.tools.btulz.transformer.JarTransformer;

/**
 * 数据结构命令
 * 
 * @author Niuren.Zhu
 *
 */
@Prompt(Command4Ds.COMMAND_PROMPT)
public class Command4Ds extends Command<Command4Ds> {

	/**
	 * 命令符
	 */
	public final static String COMMAND_PROMPT = "ds";

	protected final static String MSG_INVAILD_ARGUMENT = "invaild argument [%s].";

	public Command4Ds() {
		this.setName(COMMAND_PROMPT);
		this.setDescription("创建数据结构");
	}

	@Override
	protected Argument[] createArguments() {
		ArrayList<Argument> arguments = new ArrayList<>();
		// 添加自身参数
		arguments.add(new Argument("-data", "模型数据，待解析jar文件"));
		arguments.add(new Argument("-config", "配置文件"));
		arguments.add(new Argument("-dbSign", "数据库标记（可忽略）"));
		arguments.add(new Argument("-template", "数据结构解析模板（可忽略）"));
		arguments.add(new Argument("-sql", "数据脚本文件（可忽略）"));
		arguments.add(new Argument("-dbValue", "数据库值说明文件（可忽略）"));
		arguments.add(new Argument("-ignore", "忽略错误"));
		return arguments.toArray(new Argument[] {});
	}

	/**
	 * 为帮助添加调用代码的示例
	 */
	@Override
	protected void moreHelps(StringBuilder stringBuilder) {
		stringBuilder.append("示例：");
		stringBuilder.append(NEW_LINE);
		stringBuilder.append("  ");
		stringBuilder.append(COMMAND_PROMPT);
		stringBuilder.append(" ");
		stringBuilder.append("-data=D:\\tomcat\\data\\ibas.app.jar");
		stringBuilder.append(" ");
		stringBuilder.append("-config=D:\\tomcat\\config\\app.xml");
		stringBuilder.append(" ");
		stringBuilder.append("-ignore");
		super.moreHelps(stringBuilder);
	}

	@Override
	protected boolean isRequiredArguments() {
		return true;
	}

	@Override
	protected int run(Argument[] arguments) {
		try {
			String argData = "";
			String argConfig = "";
			String argTemplate = "";
			String argSql = "";
			String argDbValue = "";
			String argDbSign = "Master";
			boolean ignore = false;
			for (Argument argument : arguments) {
				if (!argument.isInputed()) {
					// 没有输出的参数不做处理
					continue;
				}
				if (argument.getName().equalsIgnoreCase("-data")) {
					argData = argument.getValue();
				} else if (argument.getName().equalsIgnoreCase("-emplate")) {
					argTemplate = argument.getValue();
				} else if (argument.getName().equalsIgnoreCase("-sql")) {
					argSql = argument.getValue();
				} else if (argument.getName().equalsIgnoreCase("-config")) {
					argConfig = argument.getValue();
				} else if (argument.getName().equalsIgnoreCase("-dbValue")) {
					argDbValue = argument.getValue();
				} else if (argument.getName().equalsIgnoreCase("-dbSign")) {
					argDbSign = argument.getValue();
					if (argDbSign == null) {
						argDbSign = "";
					}
				} else if (argument.getName().equalsIgnoreCase("-ignore")) {
					ignore = true;
				}
			}
			File file = new File(argConfig);
			if (!file.isFile() || !file.exists()) {
				throw new IOException("invaild config file.");
			}
			// 读取配置文件
			ConfigurationManagerFile config = new ConfigurationManagerFile();
			config.setConfigFile(file.getPath());
			config.update();
			// 加载数据库说明值
			DbValues dbValues = DbValues.create(argDbValue);
			if (dbValues == null || dbValues.isEmpty()) {
				throw new RuntimeException(String.format(MSG_INVAILD_ARGUMENT, "DbValues"));
			}
			// 数据解析模板
			if (argTemplate == null || argTemplate.isEmpty()) {
				argTemplate = dbValues.getValue(config.getConfigValue(argDbSign + MyConfiguration.CONFIG_ITEM_DB_TYPE),
						"DsTemplate");
			}
			if (argTemplate == null || argTemplate.isEmpty()) {
				throw new RuntimeException(String.format(MSG_INVAILD_ARGUMENT, "DsTemplate"));
			}
			// 脚本文件
			if (argSql == null || argSql.isEmpty()) {
				argSql = dbValues.getValue(config.getConfigValue(argDbSign + MyConfiguration.CONFIG_ITEM_DB_TYPE),
						"SqlFilter");
			}
			if (argSql == null || argSql.isEmpty()) {
				throw new RuntimeException(String.format(MSG_INVAILD_ARGUMENT, "SqlFilter"));
			}
			// 待解析的文件
			if (argData == null || argData.isEmpty()) {
				throw new RuntimeException(String.format(MSG_INVAILD_ARGUMENT, "JarFile"));
			}
			// 公司标记
			String argCompany = config.getConfigValue(MyConfiguration.CONFIG_ITEM_COMPANY);
			if (argCompany == null || argCompany.isEmpty()) {
				throw new RuntimeException(String.format(MSG_INVAILD_ARGUMENT, "Company"));
			}
			// 数据库端口
			String argDbPort = dbValues.getValue(config.getConfigValue(argDbSign + MyConfiguration.CONFIG_ITEM_DB_TYPE),
					"DbPort");
			if (argDbPort == null || argDbPort.isEmpty()) {
				throw new RuntimeException(String.format(MSG_INVAILD_ARGUMENT, "DbPort"));
			}
			// 数据库框架
			String argDbSchema = dbValues
					.getValue(config.getConfigValue(argDbSign + MyConfiguration.CONFIG_ITEM_DB_TYPE), "DbSchema");
			if (argDbSchema == null) {
				throw new RuntimeException(String.format(MSG_INVAILD_ARGUMENT, "DbSchema"));
			}
			// 数据库地址
			String argDbServer = config.getConfigValue(argDbSign + MyConfiguration.CONFIG_ITEM_DB_SERVER);
			if (argDbServer == null || argDbServer.isEmpty()) {
				throw new RuntimeException(String.format(MSG_INVAILD_ARGUMENT, "DbServer"));
			}
			// 提取端口号
			if (argDbServer.indexOf(":") > 0) {
				String values[] = argDbServer.split(":");
				argDbServer = values[0];
				argDbPort = values[1];
			}
			// 数据库名称
			String argDbName = config.getConfigValue(argDbSign + MyConfiguration.CONFIG_ITEM_DB_NAME);
			if (argDbName == null || argDbName.isEmpty()) {
				throw new RuntimeException(String.format(MSG_INVAILD_ARGUMENT, "DbName"));
			}
			// 数据库用户
			String argDbUser = config.getConfigValue(argDbSign + MyConfiguration.CONFIG_ITEM_DB_USER_ID);
			if (argDbUser == null || argDbUser.isEmpty()) {
				throw new RuntimeException(String.format(MSG_INVAILD_ARGUMENT, "DbUser"));
			}
			// 数据库用户密码
			String argDbPassword = config.getConfigValue(argDbSign + MyConfiguration.CONFIG_ITEM_DB_USER_PASSWORD);
			if (argDbPassword == null || argDbPassword.isEmpty()) {
				throw new RuntimeException(String.format(MSG_INVAILD_ARGUMENT, "DbPassword"));
			}
			JarTransformer jarTransformer = new JarTransformer();
			jarTransformer.setDsTemplate(argTemplate);
			jarTransformer.setSqlFilter(argSql);
			jarTransformer.setJarFile(argData);
			jarTransformer.setCompany(argCompany);
			jarTransformer.setDbServer(argDbServer);
			jarTransformer.setDbPort(argDbPort);
			jarTransformer.setDbSchema(argDbSchema);
			jarTransformer.setDbName(argDbName);
			jarTransformer.setDbUser(argDbUser);
			jarTransformer.setDbPassword(argDbPassword);
			jarTransformer.setInterruptOnError(!ignore);
			jarTransformer.transform();
			return RETURN_VALUE_SUCCESS;
		} catch (Exception e) {
			this.print(e);
			return RETURN_VALUE_COMMAND_EXECUTION_FAILD;
		}
	}

}
