package org.colorcoding.tools.btulz.transformers;

import java.io.File;
import java.util.UUID;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.colorcoding.tools.btulz.Environment;
import org.colorcoding.tools.btulz.templates.Parameter;
import org.colorcoding.tools.btulz.templates.Parameters;

/**
 * 数据库相关的转换器
 * 
 * @author Niuren.Zhu
 *
 */
public abstract class DbTransformer extends Transformer {

	private String dbServer;

	public String getDbServer() {
		return dbServer;
	}

	public void setDbServer(String dbServer) {
		this.dbServer = dbServer;
	}

	private String dbPort;

	public String getDbPort() {
		return dbPort;
	}

	public void setDbPort(String dbPort) {
		this.dbPort = dbPort;
	}

	private String dbName;

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	private String dbUser;

	public String getDbUser() {
		return dbUser;
	}

	public void setDbUser(String dbUser) {
		this.dbUser = dbUser;
	}

	private String dbPassword;

	public String getDbPassword() {
		return dbPassword;
	}

	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}

	private String dbSchema;

	public String getDbSchema() {
		return dbSchema;
	}

	public void setDbSchema(String dbSchema) {
		this.dbSchema = dbSchema;
	}

	private String company;

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public void useDb(String dbServer, String dbPort, String dbName, String dbUser, String dbPassword) {
		this.setDbServer(dbServer);
		this.setDbPort(dbPort);
		this.setDbName(dbName);
		this.setDbUser(dbUser);
		this.setDbPassword(dbPassword);
	}

	/**
	 * 获取运行时参数，可重载添加新的
	 * 
	 * @return
	 */
	protected Parameters getRuntimeParameters() {
		Parameters parameters = new Parameters();
		parameters.add(new Parameter("AppName", "btulz.transforms"));
		parameters.add(new Parameter("DbServer", this.getDbServer()));
		parameters.add(new Parameter("DbPort", this.getDbPort()));
		parameters.add(new Parameter("DbName", this.getDbName()));
		parameters.add(new Parameter("DbUser", this.getDbUser()));
		parameters.add(new Parameter("DbPassword", this.getDbPassword()));
		parameters.add(new Parameter("DbSchema", this.getDbSchema()));
		parameters.add(new Parameter("Company", this.getCompany()));
		return parameters;
	}

	private String templateFile;

	public String getTemplateFile() {
		return templateFile;
	}

	public boolean setTemplateFile(String templateFile) {
		this.templateFile = templateFile;
		return true;
	}

	/**
	 * 获取输出文件名称
	 * 
	 * @return
	 */
	protected String getOutputFile() {
		File file = new File(this.getTemplateFile());
		return this.getOutputFile(file.isFile() ? file.getName() : this.getTemplateFile());
	}

	protected String getOutputFile(String tpltName) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(Environment.getWorkingFolder());
		stringBuilder.append(File.separator);
		stringBuilder.append("history");
		stringBuilder.append(File.separator);
		stringBuilder.append("ds");
		stringBuilder.append(File.separator);
		if (tpltName.indexOf(".") > 0) {
			stringBuilder.append(tpltName.substring(0, tpltName.lastIndexOf(".")));
		}
		// stringBuilder.append("_");
		// SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd_HHmmsss");
		// stringBuilder.append(dateFormat.format(new Date()));
		// stringBuilder.append("_");
		// stringBuilder.append(this.getDbServer());
		stringBuilder.append("_");
		stringBuilder.append(this.getDbName());
		stringBuilder.append("-");
		stringBuilder.append(UUID.randomUUID().toString());
		if (tpltName.indexOf(".") > 0) {
			stringBuilder.append(tpltName.substring(tpltName.lastIndexOf(".")));
		}
		return stringBuilder.toString();
	}

	private Unmarshaller unmarshaller = null;

	private Unmarshaller createUnmarshaller() throws JAXBException {
		if (unmarshaller == null) {
			JAXBContext context = JAXBContext.newInstance(DataStructureOrchestration.class);
			unmarshaller = context.createUnmarshaller();
		}
		return this.unmarshaller;
	}

	/**
	 * 执行编排
	 * 
	 * @param dsFile
	 *            数据文件
	 * @throws Exception
	 */
	public void execute(File dsFile) throws Exception {
		if (dsFile == null || !dsFile.isFile() || !dsFile.exists()) {
			throw new Exception("data structure orchestration file is not exists.");
		}
		Environment.getLogger().info(String.format("try to execute orchestration file [%s].", dsFile.getPath()));
		DataStructureOrchestration orchestration = (DataStructureOrchestration) this.createUnmarshaller()
				.unmarshal(dsFile);
		orchestration.execute();
	}

}
