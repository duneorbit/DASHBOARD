package com.jspeedbox.tooling.governance.reviewboard.datamining;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jspeedbox.tooling.governance.dashboard.SprintSummaryDashBoard;
import com.jspeedbox.tooling.governance.reviewboard.User;
import com.jspeedbox.tooling.governance.reviewboard.datamining.xml.DataminedReviewArtefacts;
import com.jspeedbox.tooling.governance.reviewboard.datamining.xml.ReviewArtefact;
import com.jspeedbox.tooling.governance.reviewboard.datamining.xml.ReviewSearchCriteria;
import com.jspeedbox.tooling.governance.reviewboard.datamining.xml.Revision;
import com.jspeedbox.tooling.governance.reviewboard.datamining.xml.Users;
import com.jspeedbox.utils.IOUtils;
import com.jspeedbox.utils.JSONUtils;
import com.jspeedbox.utils.logging.LoggingUtils;
import com.jspeedbox.web.servlet.Config;

public class ReviewBoardDataMineService implements Runnable{
	
	private String user = null;
	private String runningDashboard = null;
	
	StringBuffer baseURL = new StringBuffer();
	
	private DataminedReviewArtefacts dataminedReviewArtefacts = null;
	
	private List<ReviewSearchCriteria> submittedReviews = new ArrayList<ReviewSearchCriteria>();
	
	private HttpAdapter httpAdapter = null;
	
	private static Logger LOGGER_ = LoggerFactory.getLogger(ReviewBoardDataMineService.class);
	
	public ReviewBoardDataMineService(String user, String runningDashboard){
		this.user = user;
		this.runningDashboard = runningDashboard;
		httpAdapter = new HttpAdapter();
		ProcessingStatus.getInstance().setStartThreadPoolMonitoring(true);
	}
	
	public ReviewBoardDataMineService(){
		httpAdapter = new HttpAdapter();
	}
	
	public ByteArrayOutputStream processURL(String url, String method){
		try{
			return httpAdapter.call(url, method);
		}catch(Exception e){
			LOGGER_.error("Method[{}] Exception[{}] ", "processURL", e);
		}
		return new ByteArrayOutputStream();
	}
	
	private boolean isNumeric(String revision){
		try{
			Integer.parseInt(revision);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	private void dataMineDiff(String url){
		ReviewArtefact reviewArtefact = new ReviewArtefact();
		this.dataminedReviewArtefacts.addReviewArtefact(reviewArtefact);
		
		boolean noRevisionHistory = true;
		ByteArrayOutputStream stream = processURL(Config.getInstance().getParam(IOUtils.KEY_RB_DOMAIN)+"/"+url, "GET");
		
		Element root = Jsoup.parse(new String(stream.toByteArray()));
		
		//only way to get rivision history is from script tag
		Elements scriptElements = root.getElementsByTag("script");
		Iterator<Element> scriptItr = scriptElements.iterator();
		while(scriptItr.hasNext()){
			Element scriptElement = scriptItr.next();
			for(DataNode node : scriptElement.dataNodes()){
				if(node.getWholeData().contains("\"revision\":")){
					noRevisionHistory = false;
					String revision = node.getWholeData().substring((node.getWholeData().indexOf("\"revision\":")+12),
							(node.getWholeData().indexOf("\"revision\":")+13));
					if(isNumeric(revision)){
						int revisions = Integer.parseInt(revision);
						String baseURL = url.replace("#index_header", "");
						dataMineRevision(revisions, baseURL, reviewArtefact);
					}
				}
			}
		}
	}
	
	private void dataMineRevision(int revisions, String base, ReviewArtefact reviewArtefact){
		for(int i=1; i<=revisions; i++){
			StringBuffer url = new StringBuffer();
			url.append(Config.getInstance().getParam(IOUtils.KEY_RB_DOMAIN)).append("/").append(base).append(i);
			ByteArrayOutputStream stream = processURL(url.toString(), "GET");
			Element root = Jsoup.parse(new String(stream.toByteArray()));
			dataMineRevision(root, reviewArtefact, i);
		}
	}
	
	private void dataMineRevision(Element root, ReviewArtefact reviewArtefact, int revision){
		Elements elements = root.getElementsByTag("script");
		Iterator<Element> scriptItr = elements.iterator();
		while(scriptItr.hasNext()){
			Element scriptElement = scriptItr.next();
			for(DataNode node : scriptElement.dataNodes()){
				if(node.getWholeData().contains("RB.PageManager.setPage")){
					String data = node.getWholeData().replace("RB.PageManager.setPage(new RB.DiffViewerPageView(", "");
					data = data.substring(0,data.lastIndexOf("));"));
					
					String filesJSON = data.substring((data.indexOf("model: new RB.DiffViewerPageModel({")+35),
							data.trim().indexOf("{parse: true}),"));
					filesJSON = filesJSON.substring(0, filesJSON.lastIndexOf(","));
					filesJSON = filesJSON.substring(0, filesJSON.lastIndexOf("}"));
					
					String lastActivity = data.substring(data.indexOf("lastActivityTimestamp:"), data.indexOf("el: document.body"));
					lastActivity = lastActivity.trim().substring(0, lastActivity.lastIndexOf(","));
					lastActivity = lastActivity.replace("lastActivityTimestamp:", "\"lastActivityTimestamp\":");
					
					String[] temp = lastActivity.split(":");
					StringBuffer buffer = new StringBuffer();
					buffer.append(temp[0]).append(":").append("\"").append(convertToTimestamp(startDate)).append("\"");
					
					lastActivity = buffer.toString();
					
					String summary = data.trim().substring(data.trim().indexOf("summary:"), data.trim().indexOf("targetGroups: ["));
					summary = summary.trim().substring(0, summary.lastIndexOf(","));
					summary = summary.replace("summary:", "\"summary\":");
					
					Revision reviewRevision = new Revision();
					reviewRevision.setRevisionJSON(new String(
							Base64.getEncoder().encode(serializeRBFiles(filesJSON, lastActivity, summary).getBytes())));
					reviewRevision.setRevisionNum(String.valueOf(revision));
					reviewArtefact.addRevisionHistory(reviewRevision);
				}
			}
		}
	}
	
	private String getMonth(String monthIn){
		String monthOut = null;
		monthIn = monthIn.toLowerCase();
		if(monthIn.contains("jan")){
			return "01";
		}else if(monthIn.contains("feb")){
			return "02";
		}else if(monthIn.contains("mar")){
			return "03";
		}else if(monthIn.contains("apr")){
			return "04";
		}else if(monthIn.contains("may")){
			return "05";
		}else if(monthIn.contains("jun")){
			return "06";
		}else if(monthIn.contains("jul")){
			return "07";
		}else if(monthIn.contains("aug")){
			return "08";
		}else if(monthIn.contains("sep")){
			return "09";
		}else if(monthIn.contains("oct")){
			return "10";
		}else if(monthIn.contains("nov")){
			return "11";
		}else{
			return "12";
		}
	}
	
	private String getDay(String dayIn){
		String dayOut = null;
		for(int i=0;i<dayIn.length();i++){
			try{
				String temp = dayIn.substring(0,(i+1)); 
				Integer.parseInt(temp);
				dayOut = temp;
			}catch(Exception e){
				break;
			}
		}
		return dayOut;
	}
	
	private String getHour(String hourIn){
		String hourOut = hourIn;
		if(hourIn.length()==1){
			return "0"+hourOut;
		}
		return hourOut;
	}
	
	private String convertToTimestamp(String inDate){
		String[] monthDayYearTime = inDate.split(",");
		String[] monthDay = monthDayYearTime[0].split(" ");
		String day = getDay(monthDay[1]);
		String month = getMonth(monthDay[0]);
		String year = monthDayYearTime[1].trim();
		String time = monthDayYearTime[2];
		String[] hm = time.trim().split(" ");
		String hour = getHour(time);
		String minute = time;
		String outDate = "%s-%s-%sT%s:00Z";
		return String.format(outDate, year, month, day, "00:00");
	}
	
	public String serializeRBFiles(String filesJSON, String lastActivity, String summary){
		StringBuffer rbFilesJSON = new StringBuffer();
		rbFilesJSON.append("{").append(lastActivity)
					.append(",")
					.append(filesJSON)
					.append(",")
					.append(summary)
					.append("}");
		return rbFilesJSON.toString();
	}
	
	String startDate = null;
	
	private void dataMineReviews(){
		for(ReviewSearchCriteria url : submittedReviews){
			startDate = url.getStartDate();
			ByteArrayOutputStream stream = processURL(Config.getInstance().getParam(IOUtils.KEY_RB_DOMAIN)+"/"+url.getUrl(), "GET");
			Element root = Jsoup.parse(new String(stream.toByteArray()));
			Elements elements = root.getElementsByTag("ul");
			Iterator<Element> ulItr = elements.iterator();
			while(ulItr.hasNext()){
				Element element = ulItr.next();
				if(element.hasAttr("class") 
						&& element.attr("class").equals("actions page-tabs")
						&& element.getElementsByTag("li").size()==2){
					String diffURL = element.getElementsByTag("li").get(1).getElementsByTag("a").first().attr("href");
					dataMineDiff(diffURL);
				}
			}
			ProcessingStatus.getInstance().getStatus().get(this.user).incComplete();
		}
	}
	
	private void dataMinePagination(Element div, String baseURL){
		Elements aTags = div.getElementsByTag("a");
		Iterator<Element> aTagItr = aTags.iterator();
		List<String> paginations = new ArrayList<String>();
		while(aTagItr.hasNext()){
			Element aTag = aTagItr.next();
			if(!paginations.contains(aTag.attr("href"))){
				paginations.add(aTag.attr("href"));
			}
		}
		for(String pagination : paginations){
			ByteArrayOutputStream stream = processURL(baseURL + pagination, "GET");
			Element root = Jsoup.parse(new String(stream.toByteArray()));
			buildReviewDataMineIndex(root);
		}
	}
	
	private void buildReviewDataMineIndex(Element root){
		Elements trElements = root.getElementsByTag("tr");
		Iterator<Element> trItr = trElements.iterator();
		while(trItr.hasNext()){
			Element divElement = trItr.next();
			Elements tdElements = divElement.getElementsByTag("td");
			boolean isReview = false;
			String url = null;
			String startDate = null;
			if(tdElements.size()==5){
				if(tdElements.get(1).getElementsByTag("a")!=null && 
						tdElements.get(1).getElementsByTag("a").get(0).getElementsByTag("div")!=null){
					Element div = tdElements.get(1).getElementsByTag("a").get(0).getElementsByTag("div").first();
					if(div.getElementsByTag("label")!= null 
							&& div.getElementsByTag("label").size()==1
							&& div.getElementsByTag("label").hasAttr("class")
							&& div.getElementsByTag("label").attr("class").equals("label-submitted")){
						isReview = true;
						url = tdElements.get(1).getElementsByTag("a").get(0).attr("href");
						startDate = tdElements.get(3).getElementsByTag("div").first().text();
						ReviewSearchCriteria criteria = new ReviewSearchCriteria(url, startDate);
						submittedReviews.add(criteria);
					}
				}		
			}
		}
	}
	
	private void openXMLDocument(String developer){
		this.dataminedReviewArtefacts = new DataminedReviewArtefacts();
		this.dataminedReviewArtefacts.setDeveloper(developer);
	}
	
	private void saveXMLDocument(String developer){
		try{
			JAXBContext context = JAXBContext.newInstance(DataminedReviewArtefacts.class);
			Marshaller marshaller = context.createMarshaller();
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			marshaller.marshal(this.dataminedReviewArtefacts, stream);
			
			BufferedWriter writer = null;
			
			StringBuffer fileName = new StringBuffer();
			fileName.append(IOUtils.getDataMinePath(runningDashboard))
					.append("/")
					.append(developer)
					.append("-")
					.append(System.currentTimeMillis())
					.append(".xml");
			
			try{
				File file = new File(fileName.toString());
				if(file.createNewFile()){
					writer = new BufferedWriter(new FileWriter(file));
					writer.write(new String(stream.toByteArray()));
				}else{
					LOGGER_.warn(LoggingUtils.buildParamsPlaceHolders("method", 
							"could not create file"), "saveXMLDocument", file.getAbsolutePath());
				}
			}finally{
				if(writer!=null){
					writer.close();
				}
			}
			
		}catch(Exception e){
			LOGGER_.error("Method[{}] Exception[{}] ", "saveXMLDocument", e);
		}
	}
	
	private void dataMineUser(){
		if(this.user!=null){
			dataMineUser(this.user);
		}
	}
	
	private void dataMineUser(String user){
		openXMLDocument(user);
		
		baseURL.append(Config.getInstance().getParam(IOUtils.KEY_RB_DOMAIN)).append("/reviewboard/users/").append(user).append("/");
		ByteArrayOutputStream stream = processURL(baseURL.toString(), "GET");

		Element root = Jsoup.parse(new String(stream.toByteArray()));
		buildReviewDataMineIndex(root);
		
		Elements divElements = root.getElementsByTag("div");
		Iterator<Element> divItr = divElements.iterator();
		while(divItr.hasNext()){
			Element div = divItr.next();
			if(div.hasAttr("class") 
					&& div.attr("class").equals("paginator")
					&& div.getElementsByTag("a").size()>0){
				dataMinePagination(div, baseURL.toString());
			}
		}
		//now deal with each review individually
		if(submittedReviews.size()>0){
			ProcessingStatus.getInstance().getStatus().get(user).setAll(submittedReviews.size());
			dataMineReviews();
		}
		
		saveXMLDocument(user);
		
		LOGGER_.debug(LoggingUtils.buildParamsPlaceHolders("method", "thread completed"), 
				"dataMineUser", Thread.currentThread().getName());
	}
	
	public Users mineUsers(){
		int start = 1;
		boolean dataMine = true;
		Users users = new Users();
		
		while(dataMine){
			
			StringBuffer url = new StringBuffer();
			url.append(Config.getInstance().getParam(IOUtils.KEY_RB_DOMAIN)).append("/")
				.append(Config.getInstance().getParam(IOUtils.KEY_RB_APISUFFIX))
				.append(String.format(IOUtils.URL_PART_USER_SEARCH, String.valueOf(start), "100"));
			
			String response = new String(processURL(url.toString(), "GET").toByteArray());
			ObjectNode node = JSONUtils.jsonStringToObjectNode(response);
			JsonNode jNode = node.get("users");
			if(jNode.isArray() && jNode.size() > 0){
				for(final JsonNode currNode : jNode){
					JsonNode fName = currNode.get("first_name");
					JsonNode lName = currNode.get("last_name");
					JsonNode uName = currNode.get("username");
					
					if(IOUtils.isNotNull(fName, lName, uName)){
						User user = new User();
						StringBuffer name = new StringBuffer();
						
						name.append(currNode.get("first_name").asText()).append(" ").append(currNode.get("last_name").asText());
						user.setName(name.toString());
						user.setUsername(currNode.get("username").asText());
						
						users.getUsers().add(user);
						if(jNode.size()<99){
							dataMine = false;
						}
					}
				}
			}else{
				dataMine = false;
			}
			
			start = start + 100;
		}
		
		return users;
	}

	public void run() {
		
		LOGGER_.debug(LoggingUtils.buildParamsPlaceHolders("method", "starting dataminer for"), 
				"run", user);
		dataMineUser();
	}

}
