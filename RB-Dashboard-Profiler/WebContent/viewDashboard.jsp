<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="com.jspeedbox.web.servlet.action.IAction"%>
<%@ page import="com.jspeedbox.web.servlet.controller.rest.utils.RestServiceConstants"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<%
		String dashboard = request.getParameter(IAction.PARAM_DASHBOARD);
		Boolean root = (Boolean)request.getAttribute(IAction.REQUEST_ATTRIBUTE_NO_DASHBOARD_ROOT);
	%>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title><%=IAction.TITLE_TEAM_DASHBOARD_VIEW%></title>
	<jsp:include page="./mediaContent.jsp"/>
	<script src="http://localhost:8080/RB-Dashboard-Profiler/rest/dashboards/presentation/graphs/data/<%=dashboard%>"></script>
	<script src="http://localhost:8080/RB-Dashboard-Profiler/rest/dashboards/presentation/indexing/data/<%=dashboard%>"></script>
	
	<script language="javascript">
		$(document).ready(function(event){
			
			var hasRoot = <%=root.booleanValue()%>;
			
			if(hasRoot){
				
				$('#cosmetic').jQCloud(cosmetic_words);
				$('#performance').jQCloud(performance_words);
				
				$.ajax({
					type: "GET",
					url: 'http://localhost:8080/RB-Dashboard-Profiler/rest/dashboards/presentation/tables/metrics/summary/data/<%=dashboard%>',
					dataType: "json",
					async: false,
					cache: false,
					success: function(syndicate){
						if(syndicate!=null){
							try{
								if(syndicate.success==true){
									var decodedContent = decodeSyndication(JSON.stringify(syndicate.encodedContent));
									$("#metricsBreakdown").append(decodedContent);
								}else{
									errorMsg(wrap(syndicate.msg));
								}
							}catch(e){
								errorMsg(wrap("problem with json syndication properties["+e+"]"));
							}
						}else{
							errorMsg(wrap("failed to retrieve content for["+fileName+"]"));
						}
					},
					error: function(jqXHR, textStatus, errorThrown){
						errorMsg(wrap(jqXHR.status + " " + jqXHR.responseText));
					}
				});
			
				$.ajax({
					type: "GET",
					url: 'http://localhost:8080/RB-Dashboard-Profiler/rest/dashboards/presentation/tables/metrics/reviewers/data/<%=dashboard%>',
					dataType: "json",
					async: false,
					cache: false,
					success: function(syndicate){
						if(syndicate!=null){
							try{
								if(syndicate.success==true){
									var decodedContent = decodeSyndication(JSON.stringify(syndicate.encodedContent));
									$("#reviewersBreakdown").append(decodedContent);
								}else{
									errorMsg(wrap(syndicate.msg));
								}
							}catch(e){
								errorMsg(wrap("problem with json syndication properties["+e+"]"));
							}
						}else{
							errorMsg(wrap("failed to retrieve content for["+fileName+"]"));
						}
					},
					error: function(jqXHR, textStatus, errorThrown){
						errorMsg(wrap(jqXHR.status + " " + jqXHR.responseText));
					}
				});
			
				$.ajax({
					type: "GET",
					url: 'http://localhost:8080/RB-Dashboard-Profiler/rest/dashboards/presentation/buttons/picomments/<%=dashboard%>',
					dataType: "json",
					async: false,
					cache: false,
					success: function(buutonsJSON){
						if(buutonsJSON!=null){
							try{
								$.each($.parseJSON(JSON.stringify(buutonsJSON.sprintCommentButtons)), function(key,value){
									var b = buildButton(value.sprintName, value.comments)
									displayButton($("#sprintCommentsButtons"),b);
								});
							}catch(e){
								errorMsg(wrap("problem with json for comments summary buttons"));
							}
						}else{
							errorMsg(wrap("failed to retrieve content for comments summary buttons"));
						}
					},
					error: function(jqXHR, textStatus, errorThrown){
						errorMsg(wrap(jqXHR.status + " " + jqXHR.responseText));
					}
				});
			}else{
				errorMsg(wrap("Dashboard <%=dashboard%> has not been built"));
			}
		});
	</script>
	<script language="javascript">
		var fileSuffix;
		function primer(index, row){
			var period = row.period;
			fileSuffix = period.substring(period.indexOf(" "), period.length)+"-snippet.xml";
			fileSuffix = fileSuffix.trim();
		}
		function doTableSummary(key){
			var fileName = key+"-"+fileSuffix;
			$.ajax({
				type: "GET",
				url: 'http://localhost:8080/RB-Dashboard-Profiler/rest/dashboards/interactive/developers/reviewstable/<%=dashboard%>/'+fileName,
				dataType: "json",
				async: false,
				cache: false,
				success: function(syndicate){
					if(syndicate!=null){
						try{
							if(syndicate.success==true){
								var decodedContent = decodeSyndication(JSON.stringify(syndicate.encodedContent));
								$("#developerSprintReviewSummary").html(decodedContent);
							}else{
								errorMsg(wrap(syndicate.msg));
							}
						}catch(e){
							errorMsg(wrap("problem with json syndication properties["+e+"]"));
						}
					}else{
						errorMsg(wrap("failed to retrieve content for["+fileName+"]"));
					}
				},
				error: function(jqXHR, textStatus, errorThrown){
					errorMsg(wrap(jqXHR.status + " " + jqXHR.responseText));
				}
			});
		}
		
		function getLabel(row, content){
			var label = ''; 
			$.each(row, function(key, value){
				if(key!='period'){
					content = content.replace(key,'<a href="javascript:doTableSummary(\''+key+'\');">'+key+'</a>');
				}
			});
			//document.write(content);
			//var $content = $(content);
			//$content.wrapAll($('<div>'));
			//$content.find("div").each(function(){
				//alert();
			//});
			//document.write($content.html());
			return content;
		}
		
	</script>
</head>
<body>
	<div id="container">
			<jsp:include page="./errorContainer.jsp"/>
            <div class="row">
                <div class="col-lg-12">
                	<div class="jumbotron container-fluid">
	                	<div class="row">
	                		<div class="col-lg-5">
		                		<h2>&nbsp;&nbsp;<span class="glyphicon glyphicon-cog"></span> <%=dashboard%> Delivery Dashboard Metrics</h2>
								<p>&nbsp;&nbsp;&nbsp;<span class="glyphicon glyphicon-time"></span> For time period </p>
	                		</div>
	                		<div class="col-lg-7">
	                			<div id="sprintCommentsButtons" class="row"></div>	
	                		</div>
	                    </div>
	            	</div>
                </div>
            </div>
           	
           	<jsp:include page="./navigation.jsp"/>
           	
            <div class="row">
                
				<div class="col-lg-12">
					<p>
						Breakdown of <%=dashboard%> delivery over last 3 PI's.
						Please note this breaksdown 5 developers, not the full team. Also the statics only goes as deep as One revision for each file checked in.
						This data was manually datamined, comprising of 167 json data files. The intention is to automate this and have it available System wide.
					</p>
				</div>
				
				<div class="col-lg-12">
					<div class="card sb-card">
						<div class="card-block">
							<h2>Summary Detail of Code Reviews</h2>
							<p>At a quick glance what is happening, where are the trends.</p>
						</div>
					</div>
				</div>
				
                <!-- /.col-lg-8 -->
				<div class="col-lg-8">
					<div class="panel panel-default">
						<div class="table-responsive">
							<div class="card sb-card">
								<div class="card-block">
									<h3>Delivery metrics summary, Lets see the woods for the trees</h3>
									<p>This gives a quick indicator as to what is being commented on, what is feedback comments, and what is actual code review comments.</p>
								</div>
							</div>
							
							<div class="panel-body">
								<table class="table" id="metricsBreakdown">
									<thead>
										<tr>
											<th>
												Metric Type
											</th>
											<th>
												Figure
											</th>
											<th>
												Description
											</th>
										</tr>
									</thead>
									
								</table>
							</div>
						</div>
					</div>
				</div>
                <div class="col-lg-4">
                    
                    <!-- /.panel -->
                    <div class="panel panel-default">
                        <div class="panel-heading">
							<div class="card sb-card">
								<div class="card-block">
									<h3>Developer Code Reviews for this time period</h3>
									<p>This gives a quick indicator as to what developer's are getting the most comments.</p>
								</div>
							</div>
                            <i class="fa fa-bar-chart-o fa-fw"></i> Total Review Comments per Developer
                        </div>
                        <div class="panel-body">
                            <div id="morris-donut-chart"></div>
                            <a href="#" class="btn btn-default btn-block">View Details</a>
                        </div>
                        
                    </div>
                   
                </div>
                
            </div>
			
			<div>
				<div class="col-lg-5">
					<div class="panel panel-default">
                        <div class="panel-heading">
							<div class="card sb-card">
								<div class="card-block">
									<h3>New Files vs Existing files</h3>
									<p>Is the team providing consistency with existing code vs creating new code.</p>
								</div>
							</div>
                            <i class="fa fa-bar-chart-o fa-fw"></i> Comments made on commit types
                        </div>
                        <div class="panel-body">
                            <div id="morris-bar-chart-new-vs-existing"></div>
                        </div>
                        
                    </div>
				</div>
				
				<div class="col-lg-7">
					<div class="panel panel-default">
                        <div class="panel-heading">
							<div class="card sb-card">
								<div class="card-block">
									<h3>Developer comments shown accross each PI</h3>
									<p>Is the team getting better or worse.</p>
								</div>
							</div>
                            <i class="fa fa-bar-chart-o fa-fw"></i> Comments made per developer per PI
                        </div>
                        <div class="panel-body">
                            <div id="morris-bar-chart-developer"></div>
                        </div>
                        
                    </div>
				</div>
			</div>
			
			<div class="row">
				<div class="col-lg-12">
					<div class="panel panel-default">
                        <div class="panel-heading">
							<div class="card sb-card">
								<div class="card-block">
									<h3>Comments received by team members per <strong>sprint</strong> for each PI</h3>
									<p>Where is there room for improvement.</p>
								</div>
							</div>
                            <i class="fa fa-bar-chart-o fa-fw"></i> Comments per developer / per sprint
                        </div>
                        <div class="panel-body">
                            <div id="morris-area-chart"></div>
                        </div>
                        
                    </div>
				</div>
			</div>
			
			<div class="row">
				<div class="col-lg-4" id="developerSprintReviewSummary">
					
				</div>
				<div class="col-lg-8" id="developerReviewTimeline">
				
				</div>
			</div>
			
			<div class="row">
				<div class="col-lg-12">
					<div class="card sb-card">
						<div class="card-block">
							<h2>Detailed break down analysis of review comments by reviewers</h2>
							<p>This shows contribution from all reviewers to give us a <strong>review behaviour profile</strong></p>
						</div>
					</div>
				</div>
				
				<div class="col-lg-6">
					<div class="panel panel-default">
						<div class="table-responsive">
							<div class="card sb-card">
								<div class="card-block">
									<h3>Delivery metrics summary, Lets see the woods for the trees</h3>
									<p>This gives a quick indicator as to what is being commented on, what is feedback comments, and what is actual code review comments.</p>
								</div>
							</div>
							
							<div class="panel-body">
								<table class="table" id="reviewersBreakdown">
									<thead>
										<tr>
											<th>
												#
											</th>
											<th>
												Reviewer
											</th>
											<th>
												Performance
											</th>
											<th>
												Cosmetic
											</th>
											<th>
												Miscellaneous
											</th>
											<th>
												Total
											</th>
											<th>
												Duplicates
											</th>
										</tr>
									</thead>
									
								</table>
							</div>
						</div>
					</div>
				</div>
				
				<div class="col-lg-6">
					<div class="panel panel-default">
                        <div class="panel-heading">
							<div class="card sb-card">
								<div class="card-block">
									<h3>Visual Indicator of key word indexing</h3>
									<p>A visual picture of the of the significance of the comments being generated.</p>
								</div>
							</div>
                            <i class="fa fa-bar-chart-o fa-fw"></i> Key word indexing picture
                        </div>
                        <div class="panel-body">
                            <div id="morris-bar-chart"></div>
                        </div>
                    </div>

					<div class="panel panel-default">
                        <div class="panel-heading">
							<div class="card sb-card">
								<div class="card-block">
									<h3>Visual Indicator of developer contributions towards TAJ reviews</h3>
									<p>
										Its clear from the visual that one developer is clearly focusing on the team vehemently. It will be good to start team dahsboard comparsion metrics to see if high reviewers are spending as much time on other teams.
									</p>
								</div>
							</div>
                            <i class="fa fa-bar-chart-o fa-fw"></i> TAJ Reviewers focus picture
                        </div>
                        <div class="panel-body">
                            <div id="morris-donut-chart-contributors"></div>
                        </div>
                    </div>
				</div>
				
			</div>
			
			<div class="row">
				<div class="col-lg-12">
					<div class="card sb-card">
						<div class="card-block">
							<h2>Whats being said</h2>
							<p>A breakdown of what is being said, how often <strong>key word/phrase profiling, is there a lot of dangerous code, or is it cosmetic</strong></p>
						</div>
					</div>
				</div>
			</div>
			
			<div class="row">
				<div class="col-lg-6">
					<div class="panel panel-default">
						<div class="panel-heading">
							<div class="card sb-card">
								<div class="card-block">
									<h3>Visual Cloud of <strong>Cosmetic</strong> keyword index</h3>
									<p>
										Amount of comments swaying towards cosmetic is 
									</p>
								</div>
							</div>
                            <i class="fa fa-bar-chart-o fa-fw"></i> <strong>Cosmetic</strong>
                        </div>
                        <div class="panel-body">
							<div id="cosmetic" class="demo"></div>
						</div>
					</div>
					
				</div>
				
				<div class="col-lg-6">
					<div class="panel panel-default">
						<div class="panel-heading">
							<div class="card sb-card">
								<div class="card-block">
									<h3>Visual Cloud of <strong>Performance</strong> keyword index</h3>
									<p>
										Amount of comments swaying towards performance is 
									</p>
								</div>
							</div>
                            <i class="fa fa-bar-chart-o fa-fw"></i> <strong>Performance</strong>
                        </div>
                        <div class="panel-body">
							<div id="performance" class="demo"></div>
						</div>
					</div>
				</div>
			</div>
            
        </div>
	</body>
</html>