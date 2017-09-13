package com.jspeedbox.tooling.governance.reviewboard;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.FileUtils;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jspeedbox.tooling.governance.dashboard.CommitReview;
import com.jspeedbox.tooling.governance.dashboard.CosmeticWordWrapper;
import com.jspeedbox.tooling.governance.dashboard.DeveloperHistory;
import com.jspeedbox.tooling.governance.dashboard.DeveloperSprintSummaryWrapper;
import com.jspeedbox.tooling.governance.dashboard.DeveloperSummaryDashBoard;
import com.jspeedbox.tooling.governance.dashboard.FontWeightBucket;
import com.jspeedbox.tooling.governance.dashboard.PIDashBoard;
import com.jspeedbox.tooling.governance.dashboard.PerformanceWordWrapper;
import com.jspeedbox.tooling.governance.dashboard.ProjectDashBoard;
import com.jspeedbox.tooling.governance.dashboard.ReviewBoardHelper;
import com.jspeedbox.tooling.governance.dashboard.ReviewHistory;
import com.jspeedbox.tooling.governance.dashboard.Reviewer;
import com.jspeedbox.tooling.governance.dashboard.SprintSummaryDashBoard;
import com.jspeedbox.tooling.governance.dashboard.Totals;
import com.jspeedbox.tooling.governance.dashboard.WordWrapper;
import com.jspeedbox.tooling.governance.dashboard.xml.CommentEntry;
import com.jspeedbox.tooling.governance.dashboard.xml.FileEntry;
import com.jspeedbox.tooling.governance.dashboard.xml.ReviewTimeLine;
import com.jspeedbox.tooling.governance.dashboard.xml.RowSummary;
import com.jspeedbox.tooling.governance.dashboard.xml.TableReviewSummary;
import com.jspeedbox.tooling.governance.dashboard.xml.TimeLine;
import com.jspeedbox.tooling.governance.dashboard.xml.TimeLineEntry;
import com.jspeedbox.tooling.governance.dashboard.xml.TimeLineOwner;
import com.jspeedbox.tooling.governance.dashboard.xml.TimeLines;
import com.jspeedbox.tooling.governance.reviewboard.datamining.ProcessingStatus;
import com.jspeedbox.tooling.governance.reviewboard.datamining.xml.DataminedReviewArtefacts;
import com.jspeedbox.tooling.governance.reviewboard.datamining.xml.ReviewArtefact;
import com.jspeedbox.tooling.governance.reviewboard.datamining.xml.ReviewersMetrics;
import com.jspeedbox.tooling.governance.reviewboard.datamining.xml.Revision;
import com.jspeedbox.tooling.governance.reviewboard.datamining.xml.xslt.utils.XSLTTransformUtility;
import com.jspeedbox.utils.IOUtils;
import com.jspeedbox.utils.XMLUtils;

public class DashboardCompiler {
	
	private boolean log = true;
	private boolean compileError = false;
	
	private int reviewID = 0;
	
	private String compileErrorMsg = null;
	private String currRevision = null;
	private String dashboard = null;
	
	private StringBuffer yKeysSprintAreaGraph = new StringBuffer();
	private StringBuffer yKeysSprintAreaGraph2 = new StringBuffer();

	private String currProcessingFile = null;
	private String userKey = null;
	
	//String[] devs = new String[]{"hiren","mark","mohan","ram","sach"};
	//String[] devs2 = new String[]{"v_mohanp","v_ranah","v_jaybhays","v_kevittm","v_padapanar"};
	
	DateFormat sourceFormat = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat dateToStringFormatter = new SimpleDateFormat("dd/MM/yyyy");
	DateTimeFormatter piSourceFormat = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZ");
	
	private List<User> teamUsers = null;
	
	private ProjectDashBoard projectDashBoard = null;
	
	private static Logger LOGGER_ = LoggerFactory.getLogger(DashboardCompiler.class);
	
	private void initTeamDashBoard(String dashboard){
		ReviewBoardHelper.getInstance().setTeamDashboard(XMLUtils.getTeamDashboard(dashboard));
	}
	
	private void initProjectDashBoard(){
		this.projectDashBoard = new ProjectDashBoard();
	}
	
	private String defineKey(String name){
		String userKey = null;
		for(User user : ReviewBoardHelper.getInstance().getTeamDashboard().getUsers()){
			if(name.equalsIgnoreCase(user.getUsername())){
				userKey = user.getUsername();
				break;
			}
		}
		return userKey;
	}
	
	private String defineKey(){
		String userKey = null;
		for(User user : ReviewBoardHelper.getInstance().getTeamDashboard().getUsers()){
			if(currProcessingFile.contains(user.getUsername())){
				userKey = user.getUsername();
				break;
			}
		}
		return userKey;
	}
	
	private String definePIKey(String timestamp) throws ParseException{
		
		return ReviewBoardHelper.getInstance().getPiDashboard().getPINameFromTimestamp(timestamp);
		
	}
	int failed = 0;
	BigDecimal temp = new BigDecimal(0);
	BigDecimal filesProcessed = new BigDecimal(0);
	BigDecimal allCapComments = new BigDecimal(0);
	BigDecimal ownComments = new BigDecimal(0);
	private BigDecimal getOverAllComments(PIDashBoard dashboard, RBFiles mappedObjects) throws Exception{
		
		//get developer commit
		String summary = mappedObjects.getSummary();
		String date = mappedObjects.getLastActivityTimestamp();
		List<RBFile> files = mappedObjects.getFiles();
		BigDecimal overAllComments = new BigDecimal(0);
		BigDecimal teamComments = new BigDecimal(0);
		
		try{
			SprintSummaryDashBoard spintDashboard = dashboard.getSprintDashboard(date);
			
			if(spintDashboard!=null){
				
				//get the developer history for the sprint
				DeveloperHistory sprintDeveloperHistory = spintDashboard.getDevelopersHistory(defineKey());
				StringBuffer key = new StringBuffer();
				key.append(defineKey()).append(":").append(String.valueOf(reviewID));
				//get the current review being processsed
				ReviewHistory reviewHistory = sprintDeveloperHistory.getReviewHistory(key.toString());
				reviewHistory.setSummary(summary);
				reviewHistory.setLastActivityDate(date);
				//get the current revision of this review
				CommitReview commitReview = reviewHistory.getCommitReview(currRevision);
				commitReview.setLastActivityTimestamp(date);
				commitReview.setCommittedFiles(files);
				
				for(RBFile rbFile : files){
					
					spintDashboard.getDevelopers().get(userKey).incTotalFiles();
					
					if(currProcessingFile.toLowerCase().contains("v_ranah")){
						filesProcessed = filesProcessed.add(new BigDecimal(1));
					}
					
					List<CommentsCounts> commnetCounts = rbFile.getComment_counts();
					
					for(CommentsCounts commentCount : commnetCounts){
						
						overAllComments = overAllComments.add(new BigDecimal(commentCount.getComments().size()));
						for(Comments comment: commentCount.getComments()){
							
							if(currProcessingFile.toLowerCase().contains("v_ranah")){
								allCapComments = allCapComments.add(new BigDecimal(1));
							}
							
							Reviewer reviewer = dashboard.getReviewer(comment.getUser().getName());
							reviewer.addToAllComments(comment.getText());
							if(isUserComment(comment.getUser().getUsername())){
								
								if(currProcessingFile.toLowerCase().contains("v_ranah")){
									ownComments = ownComments.add(new BigDecimal(1));
								}
								
								teamComments = teamComments.add(new BigDecimal(1));
								commitReview.incTeamCoummentCount();
								reviewer.setMemberOfReviewTeam(true);
							}
							if(!isUserComment(comment.getUser().getUsername())){
								if(currProcessingFile.toLowerCase().contains("v_ranah")){
									temp = temp.add(new BigDecimal(1));
								}
								commitReview.incReviewersCommentCount();
								dashboard.updateCommentsCountForDeveloper(defineKey(), date);
								
								DeveloperSummaryDashBoard devDashboard = dashboard.getDeveloperSummaryDashBoard(defineKey());
								devDashboard.incDeveloperTotalComments();
								if(rbFile.isNewfile()){
									dashboard.incNewFileComment();
								}else{
									dashboard.incExistingFileComment();
								}
							}
						}
					}
				}
				dashboard.addAllTeamComments(teamComments);
				dashboard.incOverAllCommits();
			}
		}catch(Exception e){
			throw e;
		}
		
		return overAllComments;
	}
	
	private boolean isUserComment(String user){
		if(currProcessingFile.toLowerCase().contains(user.toLowerCase())){
			return true;
		}
		return false;
	}
	
	private boolean isTajComment(String user){
		for(User teamUser : teamUsers){
			if(teamUser.getName().contains(user)){
				return true;
			}
		}
		return false;
	}
	
	private BigDecimal getOverAllCommentsJava(PIDashBoard dashboard, RBFiles mappedObjects){
		List<RBFile> files = mappedObjects.getFiles();
		BigDecimal overAllCommentsJava = new BigDecimal(0);
		BigDecimal teamComments = new BigDecimal(0);
		int sql = 0;
		int java = 0;
		for(RBFile rbFile : files){
			if(rbFile.getDest_filename().endsWith(".sql")){
				sql = sql + 1;
			}else{
				java = java + 1;
			}
			List<CommentsCounts> commentCounts = rbFile.getComment_counts();
			for(CommentsCounts commentCount : commentCounts){
				overAllCommentsJava = overAllCommentsJava.add(new BigDecimal(commentCount.getComments().size()));
				for(Comments comment: commentCount.getComments()){
					if(isTajComment(comment.getUser().getName())){
						teamComments = teamComments.add(new BigDecimal(1));
					}
				}
			}
		}
		if(sql>java){
			dashboard.incSqlCommits();
			dashboard.addTotalTeamCommentsSQL(teamComments);
			overAllCommentsJava = new BigDecimal(0);
		}
		return overAllCommentsJava;
	}
	
	private BigDecimal getOverAllCommentsSQL(PIDashBoard dashboard, RBFiles mappedObjects){
		List<RBFile> files = mappedObjects.getFiles();
		BigDecimal overAllCommentsSQL = new BigDecimal(0);
		BigDecimal teamComments = new BigDecimal(0);
		int sql = 0;
		int java = 0;
		for(RBFile rbFile : files){
			if(rbFile.getDest_filename().endsWith(".sql")){
				sql = sql + 1;
			}else{
				java = java + 1;
			}
			List<CommentsCounts> commnetCounts = rbFile.getComment_counts();
			for(CommentsCounts commentCount : commnetCounts){
				overAllCommentsSQL = overAllCommentsSQL.add(new BigDecimal(commentCount.getComments().size()));
				for(Comments comment: commentCount.getComments()){
					if(isTajComment(comment.getUser().getName())){
						teamComments = teamComments.add(new BigDecimal(1));
					}
				}
			}
		}
		if(java>sql){
			dashboard.incJavaCommits();
			dashboard.addTotalTeamCommentsJava(teamComments);
			overAllCommentsSQL = new BigDecimal(0);
		}
		return overAllCommentsSQL;
	}
	
	private void buildSummaryDashboard(PIDashBoard dashboard, RBFiles mappedObjects) throws Exception{
		dashboard.addTotalComments(getOverAllComments(dashboard, mappedObjects));
		dashboard.addTotalCommentsJava(getOverAllCommentsJava(dashboard, mappedObjects));
		dashboard.addTotalCommentsSQL(getOverAllCommentsSQL(dashboard, mappedObjects));
	}
	
	private void compileMetrics(RBFiles mappedObjects) throws Exception{
		
		String key = definePIKey(mappedObjects.getLastActivityTimestamp());
		
		PIDashBoard dashboard = this.projectDashBoard.getPIDashBoard(key);
		if(dashboard==null){
			this.projectDashBoard.addPIDashBoard(key, new PIDashBoard(
					ReviewBoardHelper.getInstance().getPiDashboard().getPiName(), 
					ReviewBoardHelper.getInstance().getPiDashboard().getPistartStr(), 
					ReviewBoardHelper.getInstance().getPiDashboard().getPiendStr()));
			dashboard = this.projectDashBoard.getPIDashBoard(key);
		}
		buildSummaryDashboard(dashboard, mappedObjects);
		
	}
	
	private void processJSON(String json) throws JsonParseException, JsonMappingException, IOException{
		ObjectMapper mapper = new ObjectMapper();
		RBFiles template = null;
		try{
			template = mapper.readValue(json, RBFiles.class);
			compileMetrics(template);
		}catch(Exception e){
			LOGGER_.error("Method[{}] Exception[{}] ", "processJSON", e);
		}
	}
	
	private int getTotalJSON(String[] processingFiles, String dashboard) throws IOException{
		int total = 0;
		for(String file : processingFiles){
			File currFile = IOUtils.getDataMinedFile(file, dashboard);
			if(currFile.isFile()){
				try{
					JAXBContext context = JAXBContext.newInstance(DataminedReviewArtefacts.class);
					Unmarshaller unmarshaller = context.createUnmarshaller();
					DataminedReviewArtefacts reviewArtefacts = (DataminedReviewArtefacts)unmarshaller.unmarshal(new FileInputStream(currFile));
					for(ReviewArtefact reviewArtefact : reviewArtefacts.getReviewArtefacts()){
						for(Revision revision : reviewArtefact.getRevisionHistory()){
							total = total + 1;
						}
					}
				}catch(Exception e){
					LOGGER_.error("Method[{}] Exception[{}] ", "getTotalJSON", e);
				}
			}
		}
		
		return total;
	}
	
	private String getUserKey(){
		for(User user : teamUsers){
			if(currProcessingFile.toLowerCase().contains(user.getUsername().toLowerCase())){
				return user.getUsername();
			}
		}
		return null;
	}
	
	BigDecimal jsonFiles = new BigDecimal(0);
	private void startMetricsCapture(String dashboard) throws IOException{
		File dir = IOUtils.getDataMinePath(dashboard);
		
		ProcessingStatus.getInstance().add(getTotalJSON(dir.list(), dashboard));
		
		for(String file : dir.list()){
			currProcessingFile = file;
			
			userKey = getUserKey();
			
			File currFile = IOUtils.getDataMinedFile(file, dashboard);
			
			if(currFile.isFile()){
				
				try{
					JAXBContext context = JAXBContext.newInstance(DataminedReviewArtefacts.class);
					Unmarshaller unmarshaller = context.createUnmarshaller();
					DataminedReviewArtefacts reviewArtefacts = (DataminedReviewArtefacts)unmarshaller.unmarshal(new FileInputStream(currFile));
					reviewID = 0;
					for(ReviewArtefact reviewArtefact : reviewArtefacts.getReviewArtefacts()){
						for(Revision revision : reviewArtefact.getRevisionHistory()){
							currRevision = revision.getRevisionNum();
							String json = new String(Base64.getDecoder().decode(revision.getRevisionJSON().getBytes()));
							try{
								processJSON(json);
								if(currProcessingFile.toLowerCase().contains("v_ranah")){
									jsonFiles = jsonFiles.add(new BigDecimal(1));
								}
							}catch(Exception e){
								
							}finally{
								ProcessingStatus.getInstance().getCompileStatus().incComplete();
							}
						}
						reviewID = reviewID + 1;
					}
				}catch(Exception e){
					LOGGER_.error("Method[{}] Exception[{}] ", "startMetricsCapture", e);
				}
			}
		}
		
	}
	
	private Map<String, Totals> getReviewerMetrics() throws Exception{
		Map<String, Totals> reviewers = new HashMap<String, Totals>();
		
		try{
			Map<String, PIDashBoard> sortedPiDashboards = new TreeMap<String, PIDashBoard>(projectDashBoard.getPiDashboards());
			Iterator<Entry<String, PIDashBoard>> spDashBoardsItr = sortedPiDashboards.entrySet().iterator();
			
			while(spDashBoardsItr.hasNext()){
				Entry<String, PIDashBoard> spDashBoardEntry = spDashBoardsItr.next();
				
				Iterator<Entry<String, Reviewer>> devStatsItr = spDashBoardEntry.getValue().getDevelopersStats().entrySet().iterator();
				while(devStatsItr.hasNext()){
					Entry<String, Reviewer> entry = devStatsItr.next();
					if(reviewers.get(entry.getKey())==null){
						reviewers.put(entry.getKey(), new Totals());
					}
					Totals total = reviewers.get(entry.getKey());
					total.updateTotal(entry.getValue().getAllComments().size());
					total.addPerf(entry.getValue().getPeformanceComments());
					total.addCos(entry.getValue().getCosmeticComments());
					total.addMisc(entry.getValue().getMiscComments());
					total.addDup(entry.getValue().getDuplicateComments());
					total.setDeveloperReply(entry.getValue().isMemberOfReviewTeam());
				}
			}
			
		}catch(Exception e){
			setCompileError(true);
			setCompileErrorMsg(e.getMessage());
			throw e;
		}
		
		return reviewers;
	}
	
	private void reviewersBreakDownTable(String dashboard) throws Exception{
		
		ReviewersMetrics reviewersMetrics = new ReviewersMetrics(getReviewerMetrics());
		
		try{
			
			JAXBContext context = JAXBContext.newInstance(ReviewersMetrics.class);
			Marshaller marshaller = context.createMarshaller();
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			
			marshaller.marshal(reviewersMetrics, stream);
			
			String snippet = XSLTTransformUtility.getReviewersBreakDownSnippet(new String(stream.toByteArray()));
			
			StringBuffer data = new StringBuffer(getPresentationDirectoryWeb(dashboard).getAbsolutePath());
			data.append("/").append(IOUtils.DIRECTORY_DATA).append("/").append(IOUtils.FILE_METRICS_REVIEWERS_TABLE);
			
			FileUtils.writeByteArrayToFile(new File(data.toString()), snippet.getBytes());
			
		}catch(Exception e){
			LOGGER_.error("Method[{}] Exception[{}] ", "reviewersBreakDownTable", e);
		}
	}
	
	private void codeReviewSummaryTable(String dashboard) throws IOException{
		Map<String, String> params = new HashMap<String, String>();
		params.put("overAllComments", String.valueOf(projectDashBoard.getOverAllCommentCount()));
		params.put("teamComments", String.valueOf(projectDashBoard.getTeamCommentCount()));
		params.put("otherTeamComments", String.valueOf(projectDashBoard.getOtherTeamCommentCount()));
		params.put("overAllJavaComments", String.valueOf(projectDashBoard.getOverAllJavaCommentCount()));
		params.put("overAllDataModelComments", String.valueOf(projectDashBoard.getOverAllSQLCommentCount()));
		params.put("overAllTeamJavaComments", String.valueOf(projectDashBoard.getTeamJavaCommentCount()));
		params.put("overAllTeamDataModelComments", String.valueOf(projectDashBoard.getTeamSQLCommentCount()));
		params.put("otherTeamJavaComments", String.valueOf(projectDashBoard.getOtherTeamJavaCommentCount()));
		params.put("otherTeamDataModelComments", String.valueOf(projectDashBoard.getOtherTeamSQLCommentCount()));
		params.put("overAllCommits", String.valueOf(projectDashBoard.getOverAllCommits()));
		params.put("overAllJavaCommits", String.valueOf(projectDashBoard.getJavaCommits()));
		params.put("overAllDataModelCommits", String.valueOf(projectDashBoard.getSQLCommits()));
		
		String snippet = XSLTTransformUtility.getCodeReviewSummarySnippet(params);
		
		StringBuffer data = new StringBuffer(getPresentationDirectoryWeb(dashboard).getAbsolutePath());
		data.append("/").append(IOUtils.DIRECTORY_DATA).append("/").append(IOUtils.FILE_METRICS_SUMMARY_TABLE);
		
		FileUtils.writeByteArrayToFile(new File(data.toString()), snippet.getBytes());
		
	}
	
	private void createPIDates() throws ParseException{
		
		ReviewBoardHelper.getInstance().setPiDashboard(XMLUtils.getPISprintDashboard());
		
	}
	
	private void toJSON() throws JsonGenerationException, JsonMappingException, IOException{
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(stream, projectDashBoard);
	}
	
	
	public DashboardCompiler(){
		ReviewBoardHelper helper = ReviewBoardHelper.getInstance();
	}
	
	private void cosmeticCloudJSON() throws JsonGenerationException, JsonMappingException, IOException{
		FontWeightBucket.reset();
		WordWrapper cosmeticWordWrapper = new CosmeticWordWrapper();
		FontWeightBucket.buildSizeIndex(ReviewBoardHelper.getInstance().getCosmeticCommentsLookupRaw(), cosmeticWordWrapper);
		
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(stream, cosmeticWordWrapper);
		
	}
	
	private void performanceCloudJSON() throws JsonGenerationException, JsonMappingException, IOException{
		FontWeightBucket.reset();
		WordWrapper perfWordWrapper = new PerformanceWordWrapper();
		FontWeightBucket.buildSizeIndex(ReviewBoardHelper.getInstance().getPerformanceCommentsLookupRaw(), perfWordWrapper);
		
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(stream, perfWordWrapper);
		
	}
	
	private void createSprintTimelineXML(SprintSummaryDashBoard spintDashboard){
		
		Map<String, DeveloperHistory> sortedDevHistory = new TreeMap<String, DeveloperHistory>(spintDashboard.getDevelopersHistory());
		
		Iterator<Entry<String, DeveloperHistory>> dhItr = sortedDevHistory.entrySet().iterator();
		
		StringBuffer xmlFileName = new StringBuffer();
		xmlFileName.append("Timelines-").append(spintDashboard.getSpirntName()).append(".xml");
		
		//timelines represents a one to many of timeline owners (developers per sprint)
		TimeLines timeLines = new TimeLines(spintDashboard.getSpirntName());
		
		while(dhItr.hasNext()){
			Entry<String, DeveloperHistory> developerHistoryEntry = dhItr.next();
			
			//owner of the timeline
			//an owner contains one to many timeline reviews
			TimeLineOwner timeLineOwner = new TimeLineOwner();
			
			timeLines.getTimeLineOwners().add(timeLineOwner);
			
			timeLineOwner.setOwner(developerHistoryEntry.getKey());
			
			DeveloperHistory developerHistory = developerHistoryEntry.getValue();
			Map<String, ReviewHistory> reviewHistoryMap = new TreeMap<String, ReviewHistory>(developerHistory.getReviewHistory());
			Iterator<Entry<String, ReviewHistory>> reviewHistoryMapSortedItr = reviewHistoryMap.entrySet().iterator();
			
			int cr = 1;
			while(reviewHistoryMapSortedItr.hasNext()){
				
				//this is the actual review timeline, with all revisions,
				//files per revision, comments per file, and VQI key indicators
				//suggesting description of comment verbiage with regards to software quality
				ReviewTimeLine reviewTimeLine = new ReviewTimeLine(cr);
				
				timeLineOwner.getReviewTimeLines().add(reviewTimeLine);
				
				Entry<String, ReviewHistory> reviewHistoryEntry = reviewHistoryMapSortedItr.next();
				ReviewHistory reviewHistory = reviewHistoryEntry.getValue();
				Map<String, CommitReview> commitReviews = new TreeMap<String, CommitReview>(reviewHistory.getCommitReviews());
				Iterator<Entry<String, CommitReview>> commitReviewsSortedItr = commitReviews.entrySet().iterator();
				
				//actual timeline itself
				TimeLine timeLine = new TimeLine();
				reviewTimeLine.getTimeline().add(timeLine);
				reviewTimeLine.setLastActivityDate(reviewHistory.getLastActivityDate());
				reviewTimeLine.setSummary(reviewHistory.getSummary());
				
				//time entrypoints for the time line
				TimeLineEntry timeLineEntry = null;
				int crv = 1;
				while(commitReviewsSortedItr.hasNext()){
					Entry<String, CommitReview> commitReviewEntry = commitReviewsSortedItr.next();
					CommitReview commitReview = commitReviewEntry.getValue();
					List<RBFile> fliesList = commitReview.getCommittedFiles();
					
					timeLineEntry = new TimeLineEntry(crv);
					timeLine.getTimeLineEntries().add(timeLineEntry);
					FileEntry fileEntry = null;
					for(RBFile file : fliesList){
						fileEntry = new FileEntry(file.getDest_filename());
						for(CommentsCounts commentCount : file.getComment_counts()){
							for(Comments comment : commentCount.getComments()){
								CommentEntry commentEntry = new CommentEntry();
								commentEntry.setAuthor(comment.getUser().getName());
								commentEntry.setComment(comment.getText());
								fileEntry.getComments().add(commentEntry);
							}
						}
						timeLineEntry.getFiles().add(fileEntry);
					}
					crv = crv + 1;
				}
				
				cr = cr + 1;
			}
		}
		
		timeLineUIPerDevloperReview(timeLines, spintDashboard.getSpirntName());
		timeLineTableForDeveloper(timeLines, spintDashboard.getSpirntName());
		
		
		timelineXMLDocument(xmlFileName.toString(), timeLines);
		
		
		
	}
	
	private String formatDate(String timestamp){
		Date reviewDate = piSourceFormat.parseDateTime(timestamp).toDate();
		String t = dateToStringFormatter.format(reviewDate);
		return t;
	}
	
	private void timeLineTableForDeveloper(TimeLines timelines, String spintName){
		TableReviewSummary tableSummary = new TableReviewSummary();
		for(TimeLineOwner owner : timelines.getTimeLineOwners()){
			tableSummary.setDeveloper(owner.getOwner());
			tableSummary.setSprint(spintName);
			List<ReviewTimeLine> sortedList = owner.getReviewTimeLines();
			
			Collections.sort(sortedList, new Comparator<ReviewTimeLine>(){
				public int compare(ReviewTimeLine first, ReviewTimeLine second){
					return first.getParsedDate().compareTo(second.getParsedDate());
				}
			});
			
			for(ReviewTimeLine reviewTimeLine : sortedList){
				RowSummary summary = new RowSummary();
				summary.setDate(formatDate(reviewTimeLine.getLastActivityDate()));
				summary.setRevisions(String.valueOf(getRevisionCount(reviewTimeLine.getTimeline())));
				summary.setSummary(reviewTimeLine.getSummary());
				tableSummary.getRowSummary().add(summary);
			}
			
			Collections.sort(tableSummary.getRowSummary(), new Comparator<RowSummary>(){
				public int compare(RowSummary first, RowSummary second){
					return first.getParsedDate().compareTo(second.getParsedDate());
				}
			});
			
			StringBuffer fileName = new StringBuffer();
			fileName.append(owner.getOwner()).append("-").append(spintName).append("-snippet.xml");
			
			try{
				JAXBContext context = JAXBContext.newInstance(TableReviewSummary.class);
				Marshaller marshaller = context.createMarshaller();
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				marshaller.marshal(tableSummary, stream);
				
				String snippet = XSLTTransformUtility.getTimeLineTableForDeveloperSnippet(new String(stream.toByteArray()));
				
				FileUtils.writeByteArrayToFile(IOUtils.getNewFileInTablesDirectory(fileName.toString(), this.dashboard), snippet.getBytes());
				
			}catch(Exception e){
				LOGGER_.error("Method[{}] Exception[{}] ", "timeLineTableForDeveloper", e);
			}
		}
	}
	
	private int getRevisionCount(List<TimeLine> timelines){
		int revision = 0;
		if(timelines.size()>0){
			return timelines.get(0).getTimeLineEntries().size();
		}
		return revision;
	}
	
	private void timeLineUIPerDevloperReview(TimeLines timelines, String spintName){
		
	}
	
	private void timelineXMLDocument(String filename, TimeLines timelines){
		try{
			JAXBContext context = JAXBContext.newInstance(TimeLines.class);
			Marshaller marshaller = context.createMarshaller();
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			marshaller.marshal(timelines, stream);
			
			FileUtils.writeByteArrayToFile(IOUtils.getNewFileInTimelinesDirectory(filename, this.dashboard), stream.toByteArray());
			
		}catch(Exception e){
			LOGGER_.error("Method[{}] Exception[{}] ", "timelineXMLDocument", e);
		}
	}
	
	private String createJSON(Object sessions) throws JsonGenerationException, JsonMappingException, IOException{
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(stream, sessions);
		
		String json = new String(stream.toByteArray());
		
		return json;
	}
	
	private void filesBySprintAreaGraph(String dashboard) throws JsonGenerationException, JsonMappingException, IOException{
		
	}
	
	
	private void commentsBySprintAreaGraph(String dashboard) throws JsonGenerationException, JsonMappingException, IOException{
		
		DeveloperSprintSummaryWrapper wrapper = new DeveloperSprintSummaryWrapper();
		Map<String, PIDashBoard> piSortedDashBoards = PIDashBoard.sortByPIStartDate(projectDashBoard.getPiDashboards());
				//new TreeMap<String, PIDashBoard>(projectDashBoard.getPiDashboards());
		Iterator<Entry<String, PIDashBoard>> itr = piSortedDashBoards.entrySet().iterator();
		
		int itc = 1;
		while(itr.hasNext()){
			Entry<String, PIDashBoard> entry = itr.next();
			Map<String,SprintSummaryDashBoard> unSortedDashBoards = entry.getValue().getPiSprintDashboard();
			Map<String, SprintSummaryDashBoard> sortedDashBoards = new TreeMap<String, SprintSummaryDashBoard>(unSortedDashBoards);
			Iterator<Entry<String, SprintSummaryDashBoard>> spItr = sortedDashBoards.entrySet().iterator();
			int spi = 1;
			while(spItr.hasNext()){
				Entry<String, SprintSummaryDashBoard> spEntry = spItr.next();
				
				
				String piName = entry.getValue().getPiName();
				StringBuffer period = new StringBuffer();
				period.append(itc).append(" ").append(piName).append(" ").append(spi);
				
				createSprintTimelineXML(spEntry.getValue());
				
				ObjectNode item = JsonNodeFactory.instance.objectNode();
				item.put("period", period.toString());
				
				Iterator<Entry<String, Totals>> devItr = spEntry.getValue().getDevelopers().entrySet().iterator();
				while(devItr.hasNext()){
					Entry<String, Totals> totEntry = devItr.next();
					item.put(defineKey(totEntry.getKey()), String.valueOf(totEntry.getValue().getTotal()));
				}
				
				wrapper.addData(item);
				spi = spi + 1;
				itc = itc + 1;
			}
		}
		
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(stream, wrapper);
		
		String json = new String(stream.toByteArray());
		json = json.substring((json.indexOf("{")+1), json.lastIndexOf("}"));
		
		String fileContents = readFile(IOUtils.getPresentationGraphsFile(dashboard));
		fileContents = fileContents.replace("@developer-comments-per-sprint-data@", json)
						.replace("@developer-comments-per-sprint-ykeys@",yKeysSprintAreaGraph2.toString())
						.replace("@developer-comments-per-sprint-labels@",yKeysSprintAreaGraph2.toString());
		
		updateFile(IOUtils.getPresentationGraphsFile(dashboard), fileContents);
	}
	
	private void newVsExistingBarChart(String dashboard) throws JsonGenerationException, JsonMappingException, IOException{
		ArrayNode jsonArray = JsonNodeFactory.instance.arrayNode();
		
		Map<String, PIDashBoard> sortedPiDashboards = new TreeMap<String, PIDashBoard>(projectDashBoard.getPiDashboards());
		Iterator<Entry<String, PIDashBoard>> pisFileTypesItr = sortedPiDashboards.entrySet().iterator();
		
		while(pisFileTypesItr.hasNext()){
			Entry<String, PIDashBoard> entry = pisFileTypesItr.next();
			ObjectNode item = JsonNodeFactory.instance.objectNode();
			item.put("y", entry.getValue().getPiName());
			item.put("a", entry.getValue().getNewFileCommentCount());
			item.put("b", entry.getValue().getExistingFileCommentCount());
			jsonArray.add(item);
		}
		
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(stream, jsonArray);
		
		String json = new String(stream.toByteArray());
		
		String fileContents = readFile(IOUtils.getPresentationGraphsFile(dashboard));
		fileContents = fileContents.replace("@new-old-file-json@", json);
		
		updateFile(IOUtils.getPresentationGraphsFile(dashboard), fileContents);
	}
	
	private void performanceVsCosmeticBarChart(String dashboard) throws JsonGenerationException, JsonMappingException, IOException{
		ArrayNode jsonArray = JsonNodeFactory.instance.arrayNode();
		Map<String, PIDashBoard> sortedPiDashBoards = new TreeMap<String, PIDashBoard>(projectDashBoard.getPiDashboards());
		Iterator<Entry<String, PIDashBoard>> pisItr = sortedPiDashBoards.entrySet().iterator();
		while(pisItr.hasNext()){
			Entry<String, PIDashBoard> entry = pisItr.next();
			ObjectNode item = JsonNodeFactory.instance.objectNode();
			item.put("y", entry.getValue().getPiName());
			
			Iterator<Entry<String, Reviewer>> revItr = entry.getValue().getDevelopersStats().entrySet().iterator();
			int piPerf = 0;
			int piCos = 0;
			int piDup = 0;
			int piMisc = 0;
			while(revItr.hasNext()){
				Entry<String, Reviewer> revEntry = revItr.next();
				if(!isTajComment(revEntry.getValue().getName())){
					piPerf = piPerf + revEntry.getValue().getPeformanceComments();
					piCos = piCos + revEntry.getValue().getCosmeticComments();
					piDup = piDup + revEntry.getValue().getDuplicateComments();
					piMisc = piMisc + revEntry.getValue().getMiscComments();
				}
			}
			
			item.put("a", piPerf);
			item.put("b", piCos);
			item.put("c", piDup);
			item.put("d", piMisc);
			jsonArray.add(item);
		}
		
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(stream, jsonArray);
		
		String json = new String(stream.toByteArray());
		
		String fileContents = readFile(IOUtils.getPresentationGraphsFile(dashboard));
		fileContents = fileContents.replace("@performanance-cosmetic-json@", json);
		
		updateFile(IOUtils.getPresentationGraphsFile(dashboard), fileContents);
	}
	
	private void updateFile(File file, String content){
		FileWriter fileWriter = null;
		BufferedWriter bufferedWriter = null;
		try{
			fileWriter = new FileWriter(file);
			bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write(content);
		}catch(Exception e){
			LOGGER_.error("Method[{}] Exception[{}] ", "updateFile", e);
		}finally{
			try{
				bufferedWriter.close();
				fileWriter.close();
			}catch(Exception e){
				LOGGER_.error("Method[{}] Exception[{}] ", "updateFile", e);
			}
		}
	}
	
	private String readFile(File file){
		StringBuffer data = new StringBuffer();
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;
		try{
			fileReader = new FileReader(file);
			bufferedReader = new BufferedReader(fileReader);
			String line = null;
			while((line=bufferedReader.readLine())!=null){
				line = line + "\n";
				data.append(line);
			}
		}catch(Exception e){
			LOGGER_.error("Method[{}] Exception[{}] ", "readFile", e);
		}finally{
			try{
				if(bufferedReader!=null){
					bufferedReader.close();
				}
				if(fileReader!=null){
					fileReader.close();
				}
			}catch(Exception e){
				LOGGER_.error("Method[{}] Exception[{}] ", "readFile", e);
			}
		}
		return data.toString();
	}
	
	private void yKeysSprintAreaGraph(){
		for(User teamUser : teamUsers){
			if(yKeysSprintAreaGraph.length()>0){
				yKeysSprintAreaGraph.append(",");
			}
			try{
				yKeysSprintAreaGraph.append("'").append(teamUser.getName().split(" ")[0]).append("'");
			}catch(Exception e){
				yKeysSprintAreaGraph.append("'").append(teamUser.getName()).append("'");
			}
		}
	}
	
	private void yKeysSprintAreaGraph2(){
		for(User teamUser : teamUsers){
			if(yKeysSprintAreaGraph2.length()>0){
				yKeysSprintAreaGraph2.append(",");
			}
			yKeysSprintAreaGraph2.append("'").append(teamUser.getUsername()).append("'");
		}
	}
	//
	private void piCommentsBarChart(String dashboard) throws JsonGenerationException, JsonMappingException, IOException{
		ArrayNode jsonArray = JsonNodeFactory.instance.arrayNode();
		Map<String, PIDashBoard> sortedPiDashboards = new TreeMap<String, PIDashBoard>(projectDashBoard.getPiDashboards());
		Iterator<Entry<String, PIDashBoard>> developersPICommentsItr = sortedPiDashboards.entrySet().iterator();
		
		List<String> xKeysLabels = new ArrayList<String>();
		while(developersPICommentsItr.hasNext()){
			Entry<String, PIDashBoard> entry = developersPICommentsItr.next();
			ObjectNode item = JsonNodeFactory.instance.objectNode();
			item.put("y", entry.getValue().getPiName());

			Iterator<Entry<String, DeveloperSummaryDashBoard>> devDashboards = entry.getValue().getDeveloperDashBoards().entrySet().iterator();
			while(devDashboards.hasNext()){
				Entry<String, DeveloperSummaryDashBoard> entries = devDashboards.next();
				if(!xKeysLabels.contains(entries.getValue().getDeveloperName())){
					xKeysLabels.add(entries.getValue().getDeveloperName());
				}
				item.put(entries.getValue().getDeveloperName(), entries.getValue().getDeveloperTotalComments());
			}
			jsonArray.add(item);
		}
		
		StringBuffer xKeysLabelsBuffer = new StringBuffer();
		for(String xKeyLabel : xKeysLabels){
			if(xKeysLabelsBuffer.length()>0){
				xKeysLabelsBuffer.append(",");
			}
			xKeysLabelsBuffer.append("'").append(xKeyLabel).append("'");
		}
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(stream, jsonArray);
		
		String json = new String(stream.toByteArray());
		
		String fileContents = readFile(IOUtils.getPresentationGraphsFile(dashboard));
		fileContents = fileContents.replace("@developer-comment-summary-per-pi@", json)
						.replace("@developer-comment-summary-per-pi-ykeys@",xKeysLabelsBuffer.toString())
						.replace("@developer-comment-summary-per-pi-labels@",xKeysLabelsBuffer.toString());
		
		updateFile(IOUtils.getPresentationGraphsFile(dashboard), fileContents);
	}
	
	private void developerCodeReviewsDonut(String dashboard) throws JsonGenerationException, JsonMappingException, IOException{
		ArrayNode jsonArray = JsonNodeFactory.instance.arrayNode();
		Map<String, PIDashBoard> sortedPiDashboards = new TreeMap<String, PIDashBoard>(projectDashBoard.getPiDashboards());
		Iterator<Entry<String, PIDashBoard>> developersPICommentsItr = sortedPiDashboards.entrySet().iterator();
		
		Map<String, BigDecimal> developers = new HashMap<String, BigDecimal>();
		while(developersPICommentsItr.hasNext()){
			Entry<String, PIDashBoard> entry = developersPICommentsItr.next();

			Iterator<Entry<String, DeveloperSummaryDashBoard>> devDashboards = entry.getValue().getDeveloperDashBoards().entrySet().iterator();
			while(devDashboards.hasNext()){
				Entry<String, DeveloperSummaryDashBoard> entries = devDashboards.next();
				if(developers.get(entries.getValue().getDeveloperName())==null){
					developers.put(entries.getValue().getDeveloperName(), new BigDecimal(0));
				}

				BigDecimal amount = developers.get(entries.getValue().getDeveloperName());
				amount = amount.add(new BigDecimal(entries.getValue().getDeveloperTotalComments()));
				developers.put(entries.getValue().getDeveloperName(), amount);
			}
		}
		
		Iterator<Entry<String, BigDecimal>> devItr = developers.entrySet().iterator();
		while(devItr.hasNext()){
			Entry<String, BigDecimal> devEntry = devItr.next();
			ObjectNode item = JsonNodeFactory.instance.objectNode();
			item.put("label", devEntry.getKey());
			item.put("value", devEntry.getValue().intValue());
			jsonArray.add(item);
		}
		
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(stream, jsonArray);
		
		String json = new String(stream.toByteArray());
		
		String fileContents = readFile(IOUtils.getPresentationGraphsFile(dashboard));
		fileContents = fileContents.replace("@team-comments-all@", json);
		
		updateFile(IOUtils.getPresentationGraphsFile(dashboard), fileContents);

	}
	
	private void contributorReviewersDonut(String dashboard) throws JsonGenerationException, JsonMappingException, IOException{
		Map<String, PIDashBoard> sortedPiDashboards = new TreeMap<String, PIDashBoard>(projectDashBoard.getPiDashboards());
		Iterator<Entry<String, PIDashBoard>> spDashBoardsItr = sortedPiDashboards.entrySet().iterator();
		
		Map<String, BigDecimal> reviewers = new HashMap<String, BigDecimal>();
		while(spDashBoardsItr.hasNext()){
			Entry<String, PIDashBoard> spDashBoardEntry = spDashBoardsItr.next();
			
			Iterator<Entry<String, Reviewer>> devStatsItr = spDashBoardEntry.getValue().getDevelopersStats().entrySet().iterator();
			while(devStatsItr.hasNext()){
				Entry<String, Reviewer> entry = devStatsItr.next();
				if(reviewers.get(entry.getKey())==null){
					reviewers.put(entry.getKey(), new BigDecimal(0));
				}

				BigDecimal total = reviewers.get(entry.getKey());
				total = total.add(new BigDecimal(entry.getValue().getAllComments().size()));
				reviewers.put(entry.getKey(), total);
			}
		}
		
		ArrayNode jsonArray = JsonNodeFactory.instance.arrayNode();
		Iterator<Entry<String, BigDecimal>> totalsItr = reviewers.entrySet().iterator();
		while(totalsItr.hasNext()){
			Entry<String, BigDecimal> entry = totalsItr.next();
			ObjectNode item = JsonNodeFactory.instance.objectNode();
			item.put("label", entry.getKey());
			item.put("value", entry.getValue().intValue());
			jsonArray.add(item);
		}
		
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(stream, jsonArray);
		
		String json = new String(stream.toByteArray());
		
		String fileContents = readFile(IOUtils.getPresentationGraphsFile(dashboard));
		fileContents = fileContents.replace("@others-teams-comments-all@", json);
		updateFile(IOUtils.getPresentationGraphsFile(dashboard), fileContents);

	}
	
	private void cleanWebDirectory(File directory) throws IOException{
		StringBuffer data = new StringBuffer(directory.getAbsolutePath());
		StringBuffer timelines = new StringBuffer(directory.getAbsolutePath());
		data.append("/").append(IOUtils.DIRECTORY_DATA);
		timelines.append("/").append(IOUtils.DIRECTORY_TIMELINES);
		
		FileUtils.cleanDirectory(new File(data.toString()));
		FileUtils.cleanDirectory(new File(timelines.toString()));
	}
	
	private void copy(String dashboard) throws IOException{
		FileUtils.copyURLToFile(DashboardCompiler.class.getResource(IOUtils.CLASSPATH_FILE_PRESENTATION_GRAPHS), 
				IOUtils.getPresentationGraphsFile(dashboard));
		FileUtils.copyURLToFile(DashboardCompiler.class.getResource(IOUtils.CLASSPATH_FILE_CLOUD_METRICS), 
				IOUtils.getCloudMetricsFile(dashboard));
	}
	
	private static File getPresentationDirectoryWeb(String dashboard) throws IOException{
		return IOUtils.getDashBoardRoot(dashboard);
	}
	
	private void commentsButtonsSummary(String dashboard) throws IOException{
		ObjectNode itemRoot = JsonNodeFactory.instance.objectNode();
		ArrayNode jsonArray = JsonNodeFactory.instance.arrayNode();
		Map<String, PIDashBoard> sorted = PIDashBoard.sortByPIStartDate(projectDashBoard.getPiDashboards());
		Iterator<Entry<String, PIDashBoard>> itr = sorted.entrySet().iterator();
		while(itr.hasNext()){
			Entry<String, PIDashBoard> entry = itr.next();
			ObjectNode item = JsonNodeFactory.instance.objectNode();
			item.put("sprintName", entry.getValue().getPiName());
			item.put("comments", entry.getValue().getTotalComments().intValue());
			jsonArray.add(item);
		}
		itemRoot.put("sprintCommentButtons", jsonArray);
		
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(stream, itemRoot);
		
		FileUtils.writeByteArrayToFile(IOUtils.getSprintButtonsJSONFile(dashboard), stream.toByteArray());
	}
	
	public boolean isCompileError() {
		return compileError;
	}

	public void setCompileError(boolean compileError) {
		this.compileError = compileError;
	}

	public String getCompileErrorMsg() {
		return compileErrorMsg;
	}

	public void setCompileErrorMsg(String compileErrorMsg) {
		this.compileErrorMsg = compileErrorMsg;
	}

	public void runWebCompiler(String dashboard) throws Exception{
		this.dashboard = dashboard;
		teamUsers = XMLUtils.getTeamDashboard(dashboard).getUsers();
		cleanWebDirectory(getPresentationDirectoryWeb(dashboard));
		copy(dashboard);
		initTeamDashBoard(dashboard);
		initProjectDashBoard();
		createPIDates();
		startMetricsCapture(dashboard);
		
		buildData(dashboard);
	}
	
	private void buildData(String dashboard) throws Exception{
		codeReviewSummaryTable(dashboard);
		reviewersBreakDownTable(dashboard);
		yKeysSprintAreaGraph();
		yKeysSprintAreaGraph2();
		cosmeticCloudJSON();
		performanceCloudJSON();
		commentsBySprintAreaGraph(dashboard);
		filesBySprintAreaGraph(dashboard);
		developerCodeReviewsDonut(dashboard);
		piCommentsBarChart(dashboard);
		newVsExistingBarChart(dashboard);
		performanceVsCosmeticBarChart(dashboard);
		contributorReviewersDonut(dashboard);
		commentsButtonsSummary(dashboard);
		
	}
	

}
