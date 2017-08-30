<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ page import="com.jspeedbox.web.servlet.action.IAction"%>
<%@ page import="com.jspeedbox.web.servlet.controller.rest.utils.RestServiceConstants"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title><%=IAction.TITLE_BUILD_PI_SPRINT_DATES%></title>
<jsp:include page="./mediaContent.jsp"/>
	
	<script language="javascript">
		var editPI = false;
		var editSP = false;
		
		var TreeItems = new Array();
		
		$(document).ready(function(event){
			$.ajax({
		        type: 'GET',
		        url:  'http://localhost:8080/RB-Dashboard-Profiler/rest/<%=RestServiceConstants.GET_PI_SPRINT_CONFIG%>',
				dataType: 'json',
		        async: false,
		        cache: false,
		        success: function(spintPIDashboard) {
		        	
		        	if(spintPIDashboard!=null){
		        		try{
		        			$.each($.parseJSON(JSON.stringify(spintPIDashboard.programmeIncrements)), function(key,value){
		        				var ti = new TreeItem(value.name, value.startDate, value.endDate);
		        				$.each($.parseJSON(JSON.stringify(value.sprints)), function(key2,value2){
		        					var sp = new TreeItem(value2.name, value2.startDate, value2.endDate);
		        					ti.addSubItem(sp);
		        				});
		        				TreeItems.push(ti);
		        			});
		        			jsonTree();
		        		}catch(e){
		        			errorMsg(wrap(e));
		        		}
		        	}
		        },
		        error: function(jqXHR, textStatus, errorThrown) {
		        	errorMsg(wrap(jqXHR.status + " " + jqXHR.responseText));
		        }
		    });
		});
		
		function TreeItem(name, startDate, endDate){
			
			this.name=name;
			this.startDate=startDate;
			this.endDate=endDate;
			
			this.subItems = new Array();
			
			this.setName = function(name){
				this.name = name;	
			}
			this.getName = function(){
				return this.name;
			}
			this.setStartDate = function(startDate){
				this.startDate=startDate;
			}
			this.getStartDate = function(){
				return this.startDate;
			}
			this.setEndDate = function(endDate){
				this.endDate=endDate;
			}
			this.getEndDate = function(){
				return this.endDate;
			}
			this.addSubItem = function(subItem){
				this.subItems.push(subItem);
			}
			this.getSubItems = function(){
				return this.subItems;
			}
		}
		
		function validateSPDates(start, end){
			var aStart = start.split("/");
			var aEnd = end.split("/");
			
			var sDate = new Date(aStart[2], (aStart[1]-1), aStart[0]);
			var eDate = new Date(aEnd[2], (aEnd[1]-1), aEnd[0]);
			
			var msg = '';
			if(sDate>eDate){
				msg+=wrap('Sprint start date should be before Sprint end date');
			}
			if(msg.length>0){
				errorMsg(msg);
				return false;
			}
			return true;
		}
		
		function validatePIDates(start, end){
			var aStart = start.split("/");
			var aEnd = end.split("/");
			
			var sDate = new Date(aStart[2], (aStart[1]-1), aStart[0]);
			var eDate = new Date(aEnd[2], (aEnd[1]-1), aEnd[0]);
			
			var msg = '';
			if(sDate>eDate){
				msg+=wrap('PI start date should be before PI end date');
			}
			if(msg.length>0){
				errorMsg(msg);
				return false;
			}
			return true;
		}
		
		function addSPItem(){
			if(!validateSPDates($("#spStart").val(), $("#spEnd").val())){
				return false;
			}
			for(i=0;i<TreeItems.length;i++){
				if(TreeItems[i].getName()==selectedPIName){
					TreeItems[i].addSubItem(new TreeItem($("#spName").val(), $("#spStart").val(), $("#spEnd").val()));
					break;
				}
			}
			return true;
		}
		
		function addPIItem(){
			if(!validatePIDates($("#piStart").val(), $("#piEnd").val())){
				return false;
			}
			TreeItems.push(new TreeItem($("#piName").val(), $("#piStart").val(), $("#piEnd").val()));
			return true;
		}
		
		function editPI(){
			alert();
		}
		
		function setPI(item){
			$("#addPIDataRow").html('<span class="glyphicon glyphicon-plus"></span> &nbsp;EDIT');
			$("#addPIDataRow").removeAttr("onclick");
			$("#addPIDataRow").attr("onclick","editPI()");
			$("#piName").val(item.getName());
			$("#piStart").val(item.getStartDate());
			$("#piEnd").val(item.getEndDate());
		}
		
		var jsonRoot = null;
		
		function isParentItem(piName){
			disablePI();
			disableSP();
			var enable = false;
			for(i=0;i<TreeItems.length;i++){
				if(TreeItems[i].getName()==piName){
					enable = true;
					setPI(TreeItems[i]);
					enablePI();
						
				}
			}
			if(enable){
				enableSP();
			}
		}
		
		var selectedPIName = null;
		function bindJsonTree(){
			selectedPIName = null;
			$(document).on("click", ".list-group-item", function() {
				selectedPIName = $(this).text().split("-")[0].trim();
				var classlist = $(this).attr('class');
				if(classlist.indexOf('node-selected')>=0){
					disablePI();
					disableSP();
				}else{
					isParentItem(selectedPIName);
				}
			});
		}
		
		function rebuildTree(){
			$('#piSprintTree').html('');
			$('#piSprintTree').treeview({
		        levels: 99,
		        data: jsonRoot
		    });
			bindJsonTree();
		}
		function jsonTree(){
			jsonRoot = [];
			for(i=0;i<TreeItems.length;i++){
				var item = {};
				item["text"]=TreeItems[i].getName() + ' - ' + TreeItems[i].getStartDate() + ' - ' + TreeItems[i].getEndDate();
				item["href"]='"#parent'+(i+1)+'"';
				var subItems=[];
				for(j=0;j<TreeItems[i].getSubItems().length;j++){
					var si = TreeItems[i].getSubItems()[j];
					var subItem = {};
					subItem["text"]=si.getName() + ' - ' + si.getStartDate() + ' - ' + si.getEndDate();
					subItem["href"]='"#child'+(j+1)+'"';
					subItems.push(subItem);
				}
				item["nodes"]=subItems;
				jsonRoot.push(item);
			}
			rebuildTree();
		}
		
		function validatePI(){
			var msg = ''
			if($("#piName").val()==''){
				msg+=wrap('Enter PI Name');
			}
			if($("#piStart").val()==''){
				msg+=wrap('Enter PI Start Date');
			}
			if($("#piEnd").val()==''){
				msg+=wrap('Enter PI End Date');
			}
			if(msg.length>0){
				errorMsg(msg);
				return false;
			}
			return true;
		}
		
		function validateSP(){
			var msg = ''
			if($("#spName").val()==''){
				msg+=wrap('Enter Sprint Name');
			}
			if($("#spStart").val()==''){
				msg+=wrap('Enter Sprint Start Date');
			}
			if($("#spEnd").val()==''){
				msg+=wrap('Enter Sptint End Date');
			}
			if(msg.length>0){
				errorMsg(msg);
				return false;
			}
			return true;
		}
		
		function enableSave(){
			if(TreeItems.length>0){
				$("#saveTree").css("display","block");
			}else{
				$("#saveTree").css("display","none");
			}
		}
		
		function addSP(){
			if(editSP){
				if(!validateSP()){
					return;
				}
				if(!addSPItem()){
					return;
				}
				jsonTree();
				disablePI();
				disableSP();
				enableSave();
			}
		}
		
		function addPI(){
			if(editPI){
				if(!validatePI()){
					return;
				}
				if(!addPIItem()){
					return;
				}
				jsonTree();
				disablePI();
				enableSave();
			}
		}
		
		function disableSP(){
			editSP = false;
			$("#spName").val('');
			$("#spStart").val('');
			$("#spEnd").val('');
			$("#spName").prop("disabled", "disabled");
			$("#spStart").prop("disabled", "disabled");
			$("#spEnd").prop("disabled", "disabled");
		}
		
		function enableSP(){
			editSP = true;
			$("#spName").removeAttr("disabled");
			$("#spStart").removeAttr("disabled");
			$("#spEnd").removeAttr("disabled");
		}
		
		function enablePI(){
			editPI = true;
			$("#piName").removeAttr("disabled");
			$("#piStart").removeAttr("disabled");
			$("#piEnd").removeAttr("disabled");			
		}
		
		function disablePI(){
			editPI = false;
			$("#piName").val('');
			$("#piStart").val('');
			$("#piEnd").val('');
			$("#piName").prop("disabled", "disabled");
			$("#piStart").prop("disabled", "disabled");
			$("#piEnd").prop("disabled", "disabled");
			$("#addPIDataRow").html('<span class="glyphicon glyphicon-plus"></span> &nbsp;ADD');
			$("#addPIDataRow").removeAttr("onclick");
			$("#addPIDataRow").attr("onclick","addPI()");
		}
		
		function save(){
			var progammeIncrementDashboard = {};
			var programmeIncrements = [];
			
			for(i=0;i<TreeItems.length;i++){
				var programmeIncrement = {};
				programmeIncrement.name=TreeItems[i].getName();
				programmeIncrement.startDate=TreeItems[i].getStartDate();
				programmeIncrement.endDate=TreeItems[i].getEndDate();
				programmeIncrement.sprints = [];
				for(j=0;j<TreeItems[i].getSubItems().length;j++){
					var sub = TreeItems[i].getSubItems()[j];
					var sprint = {};
					sprint.name=sub.getName();
					sprint.startDate=sub.getStartDate();
					sprint.endDate=sub.getEndDate();
					programmeIncrement.sprints.push(sprint);
				}
				programmeIncrements.push(programmeIncrement);
			}
		
			progammeIncrementDashboard.programmeIncrements=programmeIncrements;
			
			$.ajax({
				type : "POST",
				contentType : "application/json",
				url : 'http://localhost:8080/RB-Dashboard-Profiler/rest/<%=RestServiceConstants.CREATE_PI_SPRINT_CONFIG%>',
				data : JSON.stringify(progammeIncrementDashboard),
				dataType : 'json',
				async: false,
				success : function(jsonSyndicate) {
					if(jsonSyndicate.success==false){
						errorMsg(wrap(jsonSyndicate.msg));
					}else{
						doAction('./profiler.jsp');
					}
				},
				error : function(e) {
					errorMsg(wrap(jqXHR.status + " " + jqXHR.responseText));
				},
				done : function(e) {
					
				}
			});
			
			
		}
		
		$(document).ready(function(event){
			$('#piStartPicker').datetimepicker({format: 'DD/MM/YYYY'});
			$('#piEndPicker').datetimepicker({format: 'DD/MM/YYYY'});
			$('#spStartPicker').datetimepicker({format: 'DD/MM/YYYY'});
			$('#spEndPicker').datetimepicker({format: 'DD/MM/YYYY'});
		});
	</script>
	
</head>
<body>
	<div id="container">
		<jsp:include page="./errorContainer.jsp"/>
		<div class="row">
			<div class="col-lg-12 jumbotron">
				<h2>&nbsp;&nbsp;<span class="glyphicon glyphicon-cog"></span> PI Sprint Dates Builder</h2>
				<p>
					&nbsp;&nbsp;&nbsp;<span class="glyphicon glyphicon-user"></span> Create PI Dates and Apply Sprint dates!
				</p>
			</div>
		</div>
		
		<jsp:include page="./navigation.jsp"/>
		
		<div class="row">
			<div class="col-lg-4">
				<div class="panel panel-default">
					<div class="panel-heading">
						<div class="card sb-card">
							<div class="card-block">
								<h4>PI Sprint Dates Tree View</h4>
								<p>Click to modify</p>
							</div>
						</div>
					</div>
					<div class="panel-body">
						<div align="right">
							<button type="Submit" class="btn btn-primary " name="addPIRow" id="addPIRow" onclick="javascript:enablePI();">
								<span class="glyphicon glyphicon-plus"></span>
							</button>
						</div>
						<br/>
						<div id="piSprintTree" class="treeview"></div>
						<br/>
						<div align="right" id="saveTree" style="display:none;">
							<button type="Submit" class="btn btn-primary " name="save" id="save" onclick="javascript:save();">
								<span class="glyphicon glyphicon-save"></span>
								Save
							</button>
						</div>
					</div>
				</div>	
			</div>
			<div class="col-lg-8">
				<div class="panel panel-default">
					<div class="panel-heading">
						<div class="card sb-card">
							<div class="card-block">
								<h4>PI Sprint Dates Tree View</h4>
								<p>View, click to modify modify</p>
							</div>
						</div>
					</div>
					<div class="panel-body">
						<div class="form-group">
							<label for="dashboardName">Enter new PI Sprint Dates here, or modify existing</label>
							<div class="well">
								<div class="input-group">
									<label for="piName">Programme Increment Name</label>
									<div class="input-group">
										<div class="input-group-addon">
											<span class="fa fa-cubes"></span>
										</div>
										<input name="piName" type="text" class="form-control" id="piName" placeholder="PI Name" disabled="disabled"/>
									</div>
								</div>
							
								<div class="row">
									<div class="col-lg-4">
										<div class="input-group">
											<label for="piStart">PI Start Date</label>
											<div class="input-group date" id="piStartPicker">
												<div class="input-group-addon">
													<span class="glyphicon glyphicon-calendar"></span>
												</div>
												<input name="piStart" type="text" class="form-control" id="piStart" placeholder="PI Start Date" disabled="disabled"/>
											</div>
										</div>
									</div>
									<div class="col-lg-4">
										<div class="input-group">
											<label for="piEnd">PI End Date</label>
											<div class="input-group date" id="piEndPicker">
												<div class="input-group-addon">
													<span class="glyphicon glyphicon-calendar"></span>
												</div>
												<input name="piEnd" type="text" class="form-control" id="piEnd" placeholder="PI End Date" disabled="disabled"/>
											</div>
										</div>
									</div>
								</div>
								
								<div align="right">
									<button type="Submit" class="btn btn-primary " name="addPIDataRow" id="addPIDataRow" onclick="javascript:addPI();">
										<span class="glyphicon glyphicon-plus"></span>
										ADD
									</button>
								</div>
								
							</div>
							
							<label for="dashboardName">Enter new PI Sprint Dates here, or modify existing</label>
							<div class="well">
								<div class="input-group">
									<label for="piName">Sprint Name</label>
									<div class="input-group">
										<div class="input-group-addon">
											<span class="fa fa-pie-chart"></span>
										</div>
										<input name="spName" type="text" class="form-control" id="spName" placeholder="Sprint Name" disabled="disabled"/>
									</div>
								</div>
								
								<div class="row">
									<div class="col-lg-4">
										<div class="input-group">
											<label for="spStart">Sprint Start Date</label>
											<div class="input-group date" id="spStartPicker">
												<div class="input-group-addon">
													<span class="glyphicon glyphicon-calendar"></span>
												</div>
												<input name="spStart" type="text" class="form-control" id="spStart" placeholder="Sprint Start Date" disabled="disabled"/>
											</div>
										</div>
									</div>
									<div class="col-lg-4">
										<div class="input-group">
											<label for="spEnd">Sprint End Date</label>
											<div class="input-group date" id="spEndPicker">
												<div class="input-group-addon">
													<span class="glyphicon glyphicon-calendar"></span>
												</div>
												<input name="spEnd" type="text" class="form-control" id="spEnd" placeholder="Sprint End Date" disabled="disabled"/>
											</div>
										</div>
									</div>
								</div>
								
								<div align="right">
									<button type="Submit" class="btn btn-primary " name="addSPDataRow" id="addSPDataRow" onclick="javascript:addSP();">
										<span class="glyphicon glyphicon-plus"></span>
										ADD
									</button>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>