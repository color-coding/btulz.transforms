package org.colorcoding.tools.btulz.commands;

import java.io.File;
import java.util.ArrayList;

import org.colorcoding.tools.btulz.Environment;
import org.colorcoding.tools.btulz.transformers.ExcelTransformer;

/**
 * excel解析命令
 * 
 * @author Niuren.Zhu
 *
 */
@Prompt(Command4Excel.COMMAND_PROMPT)
public class Command4Excel extends Command4Release<Command4Excel> {

	/**
	 * 命令符
	 */
	public final static String COMMAND_PROMPT = "excel";

	/**
	 * 返回值，300，转换错误
	 */
	public static final int RETURN_VALUE_TRANSFORM_FAILD = 300;

	public Command4Excel() {
		this.setName(COMMAND_PROMPT);
		this.setDescription("根据excel文件输出模型。");
	}

	@Override
	protected String getResourceSign(Argument releaseArgument) {
		return "templates/";// 仅释放templates文件夹下资源
	}

	@Override
	protected File getReleaseFolder(Argument releaseArgument) {
		return new File(Environment.getWorkingFolder());// 输出到当前工作目录
	}

	@Override
	protected boolean isRequiredArguments() {
		return true;// 有参数才调用
	}

	@Override
	protected Argument[] createArguments() {
		ArrayList<Argument> arguments = new ArrayList<>();
		for (Argument argument : super.createArguments()) {
			// 保留基类参数
			arguments.add(argument);
		}
		// 添加自身参数
		arguments.add(new Argument("-ExcelFile", "解析的excel文件"));
		arguments.add(new Argument("-OutputFolder", "模型输出的目录"));
		arguments.add(new Argument("-IgnoreSheet", "忽略注释的表格，即“!--”前缀表格"));
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
		stringBuilder.append(COMMAND_PROMPT); // 命令
		stringBuilder.append(" ");
		stringBuilder.append("-ExcelFile=D:\\temp\\domain_models_template_v4.0.xlsx"); // 使用的文件
		stringBuilder.append(" ");
		stringBuilder.append("-OutputFolder=D:\\temp"); // 输出目录
		stringBuilder.append(" ");
		stringBuilder.append("-IgnoreSheet=yes"); // 忽略注释表格
		super.moreHelps(stringBuilder);
	}

	@Override
	protected int go(Argument[] arguments) {
		try {
			ExcelTransformer excelTransformer = null;
			String fileName = null;
			for (Argument argument : arguments) {
				if (!argument.isInputed()) {
					// 没有输出的参数不做处理
					continue;
				}
				if (excelTransformer == null) {
					excelTransformer = new ExcelTransformer();
					excelTransformer.setInterruptOnError(true);
				}
				if (argument.getName().equalsIgnoreCase("-ExcelFile")) {
					fileName = argument.getValue();
				} else if (argument.getName().equalsIgnoreCase("-OutputFolder")) {
					excelTransformer.setOutputFolder(argument.getValue());
				} else if (argument.getName().equalsIgnoreCase("-IgnoreSheet")) {
					if ("no".equalsIgnoreCase(argument.getValue())) {
						excelTransformer.setIgnoreSheet(false);
					}
				}
			}
			excelTransformer.load(fileName, false);
			excelTransformer.transform();
			// 没有执行方法
			return RETURN_VALUE_NO_COMMAND_EXECUTION;
		} catch (Exception e) {
			this.print(e);
			return RETURN_VALUE_TRANSFORM_FAILD;
		}
	}

}
