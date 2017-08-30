package com.jspeedbox.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;

import javax.servlet.ServletOutputStream;

import org.apache.commons.io.FileUtils;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.fasterxml.jackson.databind.JsonNode;
import com.jspeedbox.utils.zip.ZipUtil;

public class IOUtils {
	
	public static final String APPLICATION_JAVASCRIPT = "application/javascript";
	
	public static final String URL_PART_USER_SEARCH = "users/?start=%s&max-results=%s";
	
	public static final String TMP_DIR = System.getProperty("java.io.tmpdir");
	
	public static final String KEY_RB_DOMAIN = "rbDomain";
	public static final String KEY_RB_APISUFFIX = "rbApiSuffix";
	public static final String KEY_DASHBOARD = "dashboard";
	public static final String KEY_JOBNAME = "jobname";
	public static final String KEY_NIGHTLY = "nightly";
	
	public static final String PREFIX_NIGHTLY = "NIGHTLY-";
	
	public static final String DIRECTORY_DATA = "data";
	public static final String DIRECTORY_TIMELINES = "timelines";
	public static final String DIRECTORY_SNIPPETS_TABLES = DIRECTORY_TIMELINES + "/snippets/tables";
	public static final String DIRECTORY_DATAMINED = "datamined";
	public static final String DIRECTORY_DATAMINED_ARCHIVED = "datamined-archived";
	
	private static final String DIRECTORY_RB_USERS = "RBUsers";
	private static final String DIRECTORY_RB_TEAMS_DASHBOARDS = "RBTeamsDashboards";
	private static final String DIRECTORY_PI_SPRINT_DATES = "PISprintDates";
	private static final String DIRECTORY_SCHEDULED_JOBS = "scheduled";
	private static final String RUNTIME = "runtime/dashboards";

	
	private static final String FILE_RB_USERS = "RBUsers.xml";
	private static final String FILE_RB_TEAMS_DASHBOARDS = "RBTeamsDashboards.xml";
	private static final String FILE_PI_SPRINT_DATES_CONIG_XML = "PIAndSprintDatesConfig.xml";
	private static final String FILE_SCHEDULED_JOBS_DATAMINE_CONIG_XML = "scheduledDatamineJobs.xml";
	private static final String FILE_PRESENTATION_GRAPHS = "presentation-graphs.js";
	private static final String FILE_CLOUD_METRICS = "cloud-metrics.js";
	private static final String FILE_SPRINT_BUTTONS_JSON = "sprintButtons.json";
	
	public static final String FILE_METRICS_SUMMARY_TABLE = "MetricsSummaryTable.xml";
	public static final String FILE_METRICS_REVIEWERS_TABLE = "ReviewersMetricsSummaryTable.xml";
	
	public static final String CLASSPATH_FILE_PRESENTATION_GRAPHS = "/com/jspeedbox/utils/templates/" + FILE_PRESENTATION_GRAPHS;
	public static final String CLASSPATH_FILE_CLOUD_METRICS = "/com/jspeedbox/utils/templates/" + FILE_CLOUD_METRICS;
	
	private static final DateTimeFormatter PI_SOURCE_FORMAT = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZ");
	private static final SimpleDateFormat DATE_TO_STRING_FORMATTER = new SimpleDateFormat("dd/MM/yyyy");
	private static final DateFormat DAY_MONTH_YEAR_FORMATTER = new SimpleDateFormat("dd-MM-yyyy");
	
	public static boolean compressFolder(File sourceFolder, File archiveFolder) throws Exception{
		if(!directoryExists(sourceFolder)){
			throw new Exception("sourceFolder["+sourceFolder+"] does not exist");
		}
		if(!directoryExists(archiveFolder)){
			throw new Exception("archiveFolder["+archiveFolder+"] does not exist");
		}
		ZipUtil zipUtil = new ZipUtil(sourceFolder, archiveFolder);
		zipUtil.zipIt();
		return true;
	}
	
	public static File getDirectory(String directory) throws Exception{
		if(!directoryExists(directory)){
			throw new Exception("directory["+directory+"] does not exist");
		}
		return new File(directory);
	}
	
	public static boolean directoryExists(File folder){
		if(!folder.exists()){
			return false;
		}
		if(!folder.isDirectory()){
			return false;
		}
		return true;
	}
	
	public static boolean directoryExists(String folder){
		File sourceFolder = new File(folder);
		return directoryExists(sourceFolder);
	}
	
	public static File getSprintButtonsJSONFile(String dashboard){
		StringBuffer dirPath = new StringBuffer(getGraphDataPath(dashboard));
		dirPath.append("/").append(FILE_SPRINT_BUTTONS_JSON);
		return new File(dirPath.toString());
	}
	
	public static File getNewFileInTablesDirectory(String fileName, String dashboard){
		StringBuffer dirPath = new StringBuffer();
		dirPath.append(TMP_DIR).append("/").append(RUNTIME).append("/").append(dashboard);
		dirPath.append("/").append(DIRECTORY_SNIPPETS_TABLES).append("/").append(fileName);
		return new File(dirPath.toString());
	}
	
	public static File getNewFileInTimelinesDirectory(String fileName, String dashboard){
		StringBuffer dirPath = new StringBuffer();
		dirPath.append(TMP_DIR).append("/").append(RUNTIME).append("/").append(dashboard);
		dirPath.append("/").append(DIRECTORY_TIMELINES).append("/").append(fileName);
		return new File(dirPath.toString());
	}
	
	public static int getContentLength(File file){
		Double bytes = new Double(file.length());
		return new Integer(bytes.intValue()).intValue();
	}
	
	public static boolean createDashboardRoot(String dashboard){
		StringBuffer dashboardRootPath = new StringBuffer();
		StringBuffer dataPath = new StringBuffer();
		StringBuffer dataminePath = new StringBuffer();
		StringBuffer datamineArchivePath = new StringBuffer();
		StringBuffer timelinesPath = new StringBuffer();
		dashboardRootPath.append(TMP_DIR).append("/").append(RUNTIME).append("/").append(dashboard);
		dataPath.append(dashboardRootPath).append("/").append(DIRECTORY_DATA);
		dataminePath.append(dashboardRootPath).append("/").append(DIRECTORY_DATAMINED);
		datamineArchivePath.append(dashboardRootPath).append("/").append(DIRECTORY_DATAMINED_ARCHIVED);
		timelinesPath.append(dashboardRootPath).append("/").append(DIRECTORY_TIMELINES);
		
		File root = new File(dashboardRootPath.toString());
		File data = new File(dataPath.toString());
		File datamined = new File(dataminePath.toString());
		File dataminedArchive = new File(datamineArchivePath.toString());
		File timelines = new File(timelinesPath.toString());
		
		try{
			FileUtils.forceMkdir(root);
			FileUtils.forceMkdir(data);
			FileUtils.forceMkdir(datamined);
			FileUtils.forceMkdir(dataminedArchive);
			FileUtils.forceMkdir(timelines);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return (directoryExists(root) && directoryExists(data) && 
				directoryExists(datamined) && directoryExists(dataminedArchive) && directoryExists(timelines));
		
	}
	
	public static File getDashBoardRoot(String dashboard) throws IOException{
		StringBuffer dirPath = new StringBuffer();
		dirPath.append(TMP_DIR).append("/").append(RUNTIME).append("/").append(dashboard);
		File directory = new File(dirPath.toString());
		if((!directory.exists())||(!directory.isDirectory())){
			throw ioException("Could not locate dashboard directory["+directory.getAbsolutePath()+"]");
		}
		return directory;
	}
	
	public static File getDataMinePath(String dashboard) throws IOException{
		StringBuffer dirPath = new StringBuffer();
		dirPath.append(TMP_DIR).append("/").append(RUNTIME).append("/").append(dashboard).append("/").append(DIRECTORY_DATAMINED);
		File directory = new File(dirPath.toString());
		if((!directory.exists())||(!directory.isDirectory())){
			throw ioException("Could not locate datamine directory["+directory.getAbsolutePath()+"]");
		}
		return directory;
	}
	
	public static File getDataMineArchivedPath(String dashboard) throws IOException{
		StringBuffer dirPath = new StringBuffer();
		dirPath.append(TMP_DIR).append("/").append(RUNTIME).append("/").append(dashboard).append("/").append(DIRECTORY_DATAMINED_ARCHIVED);
		File directory = new File(dirPath.toString());
		return directory;
	}
	
	public static File getDataMinedFile(String fileName, String dashboard) throws IOException{
		StringBuffer filePath = new StringBuffer(getDataMinePath(dashboard).getAbsolutePath());
		filePath.append("/").append(fileName);
		return new File(filePath.toString());
	}
	
	public static boolean rbUsersExists(){
		System.out.println(TMP_DIR);
		StringBuffer dirPath = new StringBuffer();
		dirPath.append(TMP_DIR).append("/").append(DIRECTORY_RB_USERS);
		File rbusers = new File(dirPath.toString());
		if(rbusers.exists() && rbusers.isDirectory()){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean makeRbUsers(){
		StringBuffer dirPath = new StringBuffer();
		dirPath.append(TMP_DIR).append("/").append(DIRECTORY_RB_USERS);
		File rbusers = new File(dirPath.toString());
		return rbusers.mkdir();
	}
	
	public static String getRbUsersPath(){
		StringBuffer dirPath = new StringBuffer();
		dirPath.append(TMP_DIR).append("/").append(DIRECTORY_RB_USERS);
		System.out.println(dirPath.toString());
		return dirPath.toString();
	}
	
	public static String getRbTeamsDashboardPath(){
		StringBuffer dirPath = new StringBuffer();
		dirPath.append(TMP_DIR).append("/").append(DIRECTORY_RB_TEAMS_DASHBOARDS);
		return dirPath.toString();
	}
	
	public static String getPISprintDatesConigXMLPath(){
		StringBuffer dirPath = new StringBuffer();
		dirPath.append(TMP_DIR).append("/").append(DIRECTORY_PI_SPRINT_DATES);
		return dirPath.toString();
	}
	
	public static String getScheduledJobsConfigXMLPath(){
		StringBuffer dirPath = new StringBuffer();
		dirPath.append(TMP_DIR).append("/").append(DIRECTORY_SCHEDULED_JOBS);
		return dirPath.toString();
	}
	
	public static File getPresentationGraphsFile(String dashboard){
		StringBuffer filePath = new StringBuffer(getGraphDataPath(dashboard));
		filePath.append("/").append(FILE_PRESENTATION_GRAPHS);
		File file = new File(filePath.toString());
		return file;
	}
	
	public static File getCloudMetricsFile(String dashboard){
		StringBuffer filePath = new StringBuffer(getGraphDataPath(dashboard));
		filePath.append("/").append(FILE_CLOUD_METRICS);
		File file = new File(filePath.toString());
		return file;
	}
	
	public static File getRbUsersXML(){
		StringBuffer filePath = new StringBuffer(getRbUsersPath());
		filePath.append("/").append(FILE_RB_USERS);
		File file = new File(filePath.toString());
		return file;
	}
	
	public static File getDashBoardsSummaryXml(){
		StringBuffer filePath = new StringBuffer(getRbTeamsDashboardPath());
		filePath.append("/").append(FILE_RB_TEAMS_DASHBOARDS);
		File file = new File(filePath.toString());
		return file;
	}
	
	public static File getPISprintDatesConigXML(){
		StringBuffer filePath = new StringBuffer(getPISprintDatesConigXMLPath());
		filePath.append("/").append(FILE_PI_SPRINT_DATES_CONIG_XML);
		File file = new File(filePath.toString());
		return file;
	}
	
	public static File getScheduledJobsConfigXML(boolean create) throws IOException{
		StringBuffer filePath = new StringBuffer(getScheduledJobsConfigXMLPath());
		filePath.append("/").append(FILE_SCHEDULED_JOBS_DATAMINE_CONIG_XML);
		File file = new File(filePath.toString());
		if(!file.exists()){
			if(create){
				File dirPath = new File(getScheduledJobsConfigXMLPath());
				if(!dirPath.exists()){
					dirPath.mkdirs();
				}
				if(!file.createNewFile()){
					throw ioException("Failed to create file["+file.getAbsolutePath()+"]");
				}
			}else{
				throw ioException("Cannot locate file["+file.getAbsolutePath()+"]");
			}
		}
		return file;
	}
	
	private static String getGraphDataPath(String team){
		StringBuffer dirPath = new StringBuffer();
		dirPath.append(TMP_DIR).append("/runtime/dashboards/").append(team).append("/data");
		return dirPath.toString();
	}
	
	private static File getGraphDataFile(String team) throws IOException{
		StringBuffer filePath = new StringBuffer(getGraphDataPath(team));
		filePath.append("/").append(FILE_PRESENTATION_GRAPHS);
		File file = new File(filePath.toString());
		if(!file.exists()){
			throw ioException("Cannot locate file[presentation-graphs.js] for team["+team+"]");
		}
		return file;
	}
	
	private static File getIndexingDataFile(String team) throws IOException{
		StringBuffer filePath = new StringBuffer(getGraphDataPath(team));
		filePath.append("/").append(FILE_CLOUD_METRICS);
		File file = new File(filePath.toString());
		if(!file.exists()){
			throw ioException("Cannot locate file[documents.js] for team["+team+"]");
		}
		return file;
	}
	
	public static File readPresentationGraphs(String team) throws IOException{
		return getGraphDataFile(team);
	}
	
	public static File readIndexingDataFile(String team) throws IOException{
		return getIndexingDataFile(team);
	}
	
	public static boolean makeRbUsersXML() throws IOException{
		return getRbUsersXML().createNewFile();
	}
	
	public static IOException ioException(String message){
		System.out.println("----------------- " +getRbUsersXML().getAbsolutePath());
		IOException e = new IOException(message);
		e.printStackTrace();
		return e;
	}
	
	public static boolean isNotNull(Object... params){
		boolean notNull = true;
		for(Object param : params){
			if(param!=null){
				if(param instanceof JsonNode){
					JsonNode node = (JsonNode)param;
					if(isNullOrEmpty(node.asText())){
						notNull = false;
					}	
				}
			}else{
				notNull = false;
			}
		}
		return notNull;
	}
	
	public static void copy(File file, ServletOutputStream outputStream) throws FileNotFoundException, IOException{
		org.apache.commons.io.IOUtils.copy(new FileInputStream(file), outputStream);
	}
	
	private static boolean isNullOrEmpty(String param){
		return (param==null||param.trim().equals("")) ? true : false;
	}
	
	public static String encode(String data){
		return Base64.getEncoder().encodeToString(data.getBytes());
	}
	
	private static String getTimelineSnippetsPath(String team){
		StringBuffer dirPath = new StringBuffer();
		dirPath.append(TMP_DIR).append("/runtime/dashboards/").append(team).append("/timelines/snippets/tables");
		return dirPath.toString();
	}
	
	private static String getDevelopersReviewTable(String team, String fileName) throws IOException{
		StringBuffer filePath = new StringBuffer(getTimelineSnippetsPath(team));
		filePath.append("/").append(fileName);
		File file = new File(filePath.toString());
		if(!file.exists()){
			throw ioException("Cannot locate file["+file.getAbsolutePath()+"] for team["+team+"]");
		}
		return FileUtils.readFileToString(file);
	}
	
	private static String getMetricsSummaryTable(String team) throws IOException{
		StringBuffer filePath = new StringBuffer(getGraphDataPath(team));
		filePath.append("/").append(FILE_METRICS_SUMMARY_TABLE);
		File file = new File(filePath.toString());
		if(!file.exists()){
			throw ioException("Cannot locate file["+file.getAbsolutePath()+"] for team["+team+"]");
		}
		return FileUtils.readFileToString(file);
	}
	
	private static String getSprintButtonsJSON(String team) throws IOException{
		StringBuffer filePath = new StringBuffer(getGraphDataPath(team));
		filePath.append("/").append(FILE_SPRINT_BUTTONS_JSON);
		File file = new File(filePath.toString());
		if(!file.exists()){
			throw ioException("Cannot locate file["+file.getAbsolutePath()+"] for team["+team+"]");
		}
		return FileUtils.readFileToString(file);
	}
	
	private static String getMetricsReviewersTable(String team) throws IOException{
		StringBuffer filePath = new StringBuffer(getGraphDataPath(team));
		filePath.append("/").append(FILE_METRICS_REVIEWERS_TABLE);
		File file = new File(filePath.toString());
		if(!file.exists()){
			throw ioException("Cannot locate file["+file.getAbsolutePath()+"] for team["+team+"]");
		}
		return FileUtils.readFileToString(file);
	}
	
	public static String readMetricsSummaryTableXML(String team) throws IOException{
		return getMetricsSummaryTable(team);
	}
	
	public static String readSprintButtonsJSON(String team) throws IOException{
		return getSprintButtonsJSON(team);
	}
	
	public static String readMetricsReviewersTableXML(String team) throws IOException{
		return getMetricsReviewersTable(team);
	}
	
	public static String readDevelopersReviewTableXML(String team, String fileName) throws IOException{
		return getDevelopersReviewTable(team, fileName);
	}

	public static DateTimeFormatter getPI_SOURCE_FORMAT() {
		return PI_SOURCE_FORMAT;
	}

	public static SimpleDateFormat getDATE_TO_STRING_FORMATTER() {
		return DATE_TO_STRING_FORMATTER;
	}
	
	public static DateFormat getDAY_MONTH_YEAR_FORMATTER(){
		return DAY_MONTH_YEAR_FORMATTER;
	}

}
