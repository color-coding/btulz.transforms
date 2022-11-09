package org.colorcoding.tools.btulz.test.template;

import java.io.File;
import java.io.UnsupportedEncodingException;

import org.colorcoding.tools.btulz.model.IBusinessObject;
import org.colorcoding.tools.btulz.model.IDomain;
import org.colorcoding.tools.btulz.model.IModel;
import org.colorcoding.tools.btulz.template.Parameter;
import org.colorcoding.tools.btulz.template.Parameters;
import org.colorcoding.tools.btulz.test.Environment;
import org.colorcoding.tools.btulz.transformer.XmlTransformer;
import org.colorcoding.tools.btulz.transformer.region.RegionBusinessObject;
import org.colorcoding.tools.btulz.transformer.region.RegionDomain;
import org.colorcoding.tools.btulz.transformer.region.RegionModel;
import org.colorcoding.tools.btulz.util.URIEncoder;

import junit.framework.TestCase;

public class TestTemplate extends TestCase {

	private static String template_file = "/eclipse/ibas_classic/{artifactid}.{domain.name}/src/main/java/{groupid}/{artifactid}/{domain.name}/bo/Template_Model.{Model.Name}.java.txt";

	public void testJudgmentRegion() throws Exception {
		XmlTransformer xmlTransformer = new XmlTransformer();
		xmlTransformer.load(Environment.getXmlModelsFileOld(), false);
		String tpltFile = Environment.getResource("code").getPath();
		RegionDomain template = new RegionDomain();
		template.setTemplateFile(tpltFile + File.separator + template_file);
		for (IDomain domain : xmlTransformer.getWorkingDomains()) {
			for (IBusinessObject businessObject : domain.getBusinessObjects()) {
				for (IModel model : domain.getModels()) {
					if (!model.getName().equals(businessObject.getMappedModel())) {
						continue;
					}
					File outputFile = new File(Environment.getWorkingFolder() + File.separator
							+ String.format("%s.%s.out.txt", domain.getName(), model.getName()));
					Parameters parameters = new Parameters();
					parameters.add(new Parameter(RegionDomain.REGION_PARAMETER_NAME, domain));
					parameters.add(new Parameter(RegionBusinessObject.REGION_PARAMETER_NAME, businessObject));
					parameters.add(new Parameter(RegionModel.REGION_PARAMETER_NAME, model));
					template.export(parameters, outputFile);
				}
			}
		}

	}

	public void testURL() throws UnsupportedEncodingException {
		String urlString = "jdbc:mysql://localhost:3306/mysql?useUnicode=true&characterEncoding=utf-8&useSSL=false&allowPublicKeyRetrieval=true&timezone=GMT+08:00&NONE&NONE=&TEST=\"demo\"";
		System.out.println(URIEncoder.encodeURI(urlString));
		System.out.println(URIEncoder.encodeURIComponent(urlString));
		System.out.println(URIEncoder.encodeURIParameters(urlString));
		System.out.println(URIEncoder.encodeURIParameters("timezone=GMT+08:00"));
		System.out.println(URIEncoder.encodeURIParameters("GMT+08:00"));
	}
}
