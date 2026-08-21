package org.colorcoding.tools.btulz.command;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.colorcoding.tools.btulz.Environment;
import org.colorcoding.tools.btulz.transformer.JarTransformer;

/**
 * jar包中数据结构和SQL脚本的执行命令
 * 
 * @author Niuren.Zhu
 *
 */
@Prompt(Command4DsJar.COMMAND_PROMPT)
public class Command4DsJar extends Command4Release<Command4DsJar> {

	/**
	 * 命令符
	 */
	public final static String COMMAND_PROMPT = "dsJar";

	/**
	 * 返回值，300，转换错误
	 */
	public static final int RETURN_VALUE_TRANSFORM_FAILED = 300;

	public Command4DsJar() {
		this.setName(COMMAND_PROMPT);
		this.setDescription("Execute data structures and SQL in jar package");
	}

	@Override
	protected String getResourceSign(Argument releaseArgument) {
		return "datastructures/";// 仅释放datastructures文件夹下资源
	}

	@Override
	protected File getReleaseFolder(Argument releaseArgument) {
		return new File(Environment.getWorkingFolder());// 输出到当前工作目录
	}

	@Override
	protected boolean isRequiredArguments() {
		return true;// 有参数才调用
	}

	private Argument jarArgument = new Argument("-jarFile", "Jar package to analyze");

	@Override
	protected File getJarFile() throws UnsupportedEncodingException {
		if (this.jarArgument == null || this.jarArgument.getValue() == null) {
			return null;
		}
		File file = new File(jarArgument.getValue());
		return file;
	}

	@Override
	protected Argument[] createArguments() {
		ArrayList<Argument> arguments = new ArrayList<>();
		for (Argument argument : super.createArguments()) {
			// 保留基类参数
			arguments.add(argument);
		}
		// 添加自身参数
		arguments.add(this.jarArgument);// "-jarFile", "待分析的jar包"，全局变量
		arguments.add(new Argument("-dsTemplate", "Template to use"));
		arguments.add(new Argument("-sqlFilter", "SQL filter tag"));
		arguments.add(new Argument("-company", "Company tag for database object prefix"));
		arguments.add(new Argument("-dbServer", "Database server address"));
		arguments.add(new Argument("-dbPort", "Database port"));
		arguments.add(new Argument("-dbSchema", "Database schema"));
		arguments.add(new Argument("-dbName", "Database name"));
		arguments.add(new Argument("-dbUser", "Database user"));
		arguments.add(new Argument("-dbPassword", "Database user password"));
		return arguments.toArray(new Argument[] {});
	}

	/**
	 * 为帮助添加调用jar包数据结构的示例
	 */
	@Override
	protected void moreHelps(StringBuilder stringBuilder) {
		stringBuilder.append("Example:");
		stringBuilder.append(NEW_LINE);
		stringBuilder.append("  ");
		stringBuilder.append(COMMAND_PROMPT);
		stringBuilder.append(" ");
		stringBuilder.append("-dsTemplate=ds_mysql_ibas_classic.xml");
		stringBuilder.append(" ");
		stringBuilder.append("-sqlFilter=sql_mysql");
		stringBuilder.append(" ");
		stringBuilder.append("-jarFile=D:\\initialization\\ibas.trainingtesting-0.0.1.jar");
		stringBuilder.append(" ");
		stringBuilder.append("-company=CC");
		stringBuilder.append(" ");
		stringBuilder.append("-dbServer=ibas-dev-mysql");
		stringBuilder.append(" ");
		stringBuilder.append("-dbPort=3306");
		stringBuilder.append(" ");
		stringBuilder.append("-dbSchema=");
		stringBuilder.append(" ");
		stringBuilder.append("-dbName=ibas_demo");
		stringBuilder.append(" ");
		stringBuilder.append("-dbUser=root");
		stringBuilder.append(" ");
		stringBuilder.append("-dbPassword=1q2w3e");
		super.moreHelps(stringBuilder);
	}

	@Override
	protected int go(Argument[] arguments) {
		try {
			JarTransformer jarTransformer = new JarTransformer();
			for (Argument argument : arguments) {
				if (!argument.isInputed()) {
					// 没有输入的参数不做处理
					continue;
				}
				if (argument.getName().equalsIgnoreCase("-dsTemplate")) {
					jarTransformer.setDsTemplate(argument.getValue());
				} else if (argument.getName().equalsIgnoreCase("-sqlFilter")) {
					jarTransformer.setSqlFilter(argument.getValue());
				} else if (argument.getName().equalsIgnoreCase("-jarFile")) {
					jarTransformer.setJarFile(argument.getValue());
				} else if (argument.getName().equalsIgnoreCase("-company")) {
					jarTransformer.setCompany(argument.getValue());
				} else if (argument.getName().equalsIgnoreCase("-dbServer")) {
					jarTransformer.setDbServer(argument.getValue());
				} else if (argument.getName().equalsIgnoreCase("-dbPort")) {
					jarTransformer.setDbPort(argument.getValue());
				} else if (argument.getName().equalsIgnoreCase("-dbSchema")) {
					jarTransformer.setDbSchema(argument.getValue());
				} else if (argument.getName().equalsIgnoreCase("-dbName")) {
					jarTransformer.setDbName(argument.getValue());
				} else if (argument.getName().equalsIgnoreCase("-dbUser")) {
					jarTransformer.setDbUser(argument.getValue());
				} else if (argument.getName().equalsIgnoreCase("-dbPassword")) {
					jarTransformer.setDbPassword(argument.getValue());
				}
			}
			if (jarTransformer != null && jarTransformer.getDsTemplate() != null
					&& jarTransformer.getJarFile() != null) {
				// 必要参数赋值后才可运行
				jarTransformer.transform();
				return RETURN_VALUE_SUCCESS;
			}
			// 没有执行方法
			return RETURN_VALUE_NO_COMMAND_EXECUTION;
		} catch (Exception e) {
			this.print(e);
			return RETURN_VALUE_TRANSFORM_FAILED;
		}
	}

}
