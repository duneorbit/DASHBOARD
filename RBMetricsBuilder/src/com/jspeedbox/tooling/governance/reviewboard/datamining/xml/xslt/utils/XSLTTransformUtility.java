package com.jspeedbox.tooling.governance.reviewboard.datamining.xml.xslt.utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jspeedbox.utils.logging.LoggingUtils;

public class XSLTTransformUtility {
	
	private static Logger LOGGER_ = LoggerFactory.getLogger(XSLTTransformUtility.class);
	
	private static InputStream loadTemplate(String template){
		return XSLTTransformUtility.class.getResourceAsStream(template);
	}
	
	private static StreamSource getTimeLineTableForDeveloperXSL(){
		String template = "/com/jspeedbox/tooling/governance/reviewboard/datamining/xml/xslt/developerTimeTable.xsl";
		return new StreamSource(loadTemplate(template));
	}
	
	private static StreamSource getCodeReviewSummaryXSL(){
		String template = "/com/jspeedbox/tooling/governance/reviewboard/datamining/xml/xslt/codeReviewSummary.xsl";
		return new StreamSource(loadTemplate(template));
	}
	
	private static StreamSource getReviewersBreakDownXSL(){
		String template = "/com/jspeedbox/tooling/governance/reviewboard/datamining/xml/xslt/reviewersBreakDown.xsl";
		return new StreamSource(loadTemplate(template));
	}
	
	public static String getReviewersBreakDownSnippet(String xml){
		TransformerFactory factory = TransformerFactory.newInstance();
		try{
			Transformer transformer = factory.newTransformer(getReviewersBreakDownXSL());
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			transformer.transform(new StreamSource(new StringReader(xml)), new StreamResult(stream));
			return new String(stream.toByteArray());
		}catch(Exception e){
			LOGGER_.error("Method[{}] Exception[{}] ", "getReviewersBreakDownSnippet", e);
		}
		return "";
	}
	
	public static String getCodeReviewSummarySnippet(Map<String, String> params){
		TransformerFactory factory = TransformerFactory.newInstance();
		try{
			Transformer transformer = factory.newTransformer(getCodeReviewSummaryXSL());
			Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
			while(iterator.hasNext()){
				Entry<String, String> entry = iterator.next();
				transformer.setParameter(entry.getKey(), entry.getValue());
			}
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			transformer.transform(new StreamSource(new StringReader("<CodeReviewSummary/>")), new StreamResult(stream));
			return new String(stream.toByteArray());
		}catch(Exception e){
			LOGGER_.error("Method[{}] Exception[{}] ", "getCodeReviewSummarySnippet", e);
		}
		return "";
	}
	
	public static String getTimeLineTableForDeveloperSnippet(String xml){
		TransformerFactory factory = TransformerFactory.newInstance();
		try{
			Transformer transformer = factory.newTransformer(getTimeLineTableForDeveloperXSL());
			
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			transformer.transform(new StreamSource(new StringReader(xml)), new StreamResult(stream));
			return new String(stream.toByteArray());
		}catch(Exception e){
			LOGGER_.error("Method[{}] Exception[{}] ", "getTimeLineTableForDeveloperSnippet", e);
		}
		return "";
	}

}
