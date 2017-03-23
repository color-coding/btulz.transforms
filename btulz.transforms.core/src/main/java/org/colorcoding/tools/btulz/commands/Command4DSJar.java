package org.colorcoding.tools.btulz.commands;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.colorcoding.tools.btulz.Environment;
import org.colorcoding.tools.btulz.transformers.JarTransformer;

/**
 * jar包中数据结构和SQL脚本的执行命令
 * 
 * @author Niuren.Zhu
 *
 */
@Prompt(Command4DSJar.COMMAND_PROMPT)
public class Command4DSJar extends Command4Release<Command4DSJar> {

	/**
	 * 命令符
	 */
	public final static String COMMAND_PROMPT = "dsJar";

	/**
	 * 返回值，300，转换错误
	 */
	public static final int RETURN_VALUE_TRANSFORM_FAILD = 300;

	public Command4DSJar() {
		this.setName(COMMAND_PROMPT);
		this.setDescription("执行jar包中的数据结构和SQL说明");
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

	private Argument jarArgument = new Argument("-JarFile", "待分析的jar包");

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
		arguments.add(this.jarArgument);// "-JarFile", "待分析的jar包"，全局变量
		arguments.add(new Argument("-DsTemplate", "使用的模板"));
		arguments.add(new Argument("-SqlFilter", "SQL过滤标记"));
		arguments.add(new Argument("-Company", "公司标记，用于数据库对象前缀"));
		arguments.add(new Argument("-DbServer", "数据库地址"));
		arguments.add(new Argument("-DbPort", "数据库端口"));
		arguments.add(new Argument("-DbSchema", "应用架构"));
		arguments.add(new Argument("-DbName", "数据库名称"));
		arguments.add(new Argument("-DbUser", "数据库用户"));
		arguments.add(new Argument("-DbPassword", "数据库用户密码"));
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
		stringBuilder.append("-DsTemplate=ds_mysql_ibas_classic.xml");
		stringBuilder.append(" ");
		stringBuilder.append("-SqlFilter=sql_mysql");
		stringBuilder.append(" ");
		stringBuilder.append("-JarFile=D:\\initialization\\ibas.trainingtesting-0.0.1.jar");
		stringBuilder.append(" ");
		stringBuilder.append("-Company=CC");
		stringBuilder.append(" ");
		stringBuilder.append("-DbServer=ibas-dev-mysql");
		stringBuilder.append(" ");
		stringBuilder.append("-DbPort=3306");
		stringBuilder.append(" ");
		stringBuilder.append("-DbSchema=");
		stringBuilder.append(" ");
		stringBuilder.append("-DbName=ibas_demo");
		stringBuilder.append(" ");
		stringBuilder.append("-DbUser=root");
		stringBuilder.append(" ");
		stringBuilder.append("-DbPassword=1q2w3e");
		super.moreHelps(stringBuilder);
	}

	@Override
	protected int go(Argument[] arguments) {
		try {
			JarTransformer jarTransformer = new JarTransformer();
			for (Argument argument : arguments) {
				if (!argument.isInputed()) {
					// 没有输出的参数不做处理
					continue;
				}
				if (argument.getName().equalsIgnoreCase("-DsTemplate")) {
					jarTransformer.setDsTemplate(argument.getValue());
				} else if (argument.getName().equalsIgnoreCase("-SqlFilter")) {
					jarTransformer.setSqlFilter(argument.getValue());
				} else if (argument.getName().equalsIgnoreCase("-JarFile")) {
					jarTransformer.setJarFile(argument.getValue());
				} else if (argument.getName().equalsIgnoreCase("-Company")) {
					jarTransformer.setCompany(argument.getValue());
				} else if (argument.getName().equalsIgnoreCase("-DbServer")) {
					jarTransformer.setDbServer(argument.getValue());
				} else if (argument.getName().equalsIgnoreCase("-DbPort")) {
					jarTransformer.setDbPort(argument.getValue());
				} else if (argument.getName().equalsIgnoreCase("-DbSchema")) {
					jarTransformer.setDbSchema(argument.getValue());
				} else if (argument.getName().equalsIgnoreCase("-DbName")) {
					jarTransformer.setDbName(argument.getValue());
				} else if (argument.getName().equalsIgnoreCase("-DbUser")) {
					jarTransformer.setDbUser(argument.getValue());
				} else if (argument.getName().equalsIgnoreCase("-DbPassword")) {
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
			return RETURN_VALUE_TRANSFORM_FAILD;
		}
	}

}
