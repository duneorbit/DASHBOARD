	var primedDashboard = null;
	var logger = null;
	
	$(document).ready(function(event){
		logger = log4javascript.getLogger();
		var popUpAppender = new log4javascript.PopUpAppender();
		popUpAppender.setFocusPopUp(true);
		popUpAppender.setNewestMessageAtTop(true);
		logger.addAppender(popUpAppender);
	});
	
	function displayButton(appender, button){
		appender.append(button);
	}
	
	function buildButton(piName, comments){
		var button = getCommentSummaryButton();
		button = button.replace("@commentsNum@", comments);
		button = button.replace("@piName@", piName);
		return button;
	}
	
	function decodeSyndication(syndicate){
		syndicate = syndicate.replace("\"","");
		syndicate = syndicate.replace("\"","");
		syndicate = CryptoJS.enc.Base64.parse(syndicate);
		var snippet = CryptoJS.enc.Utf8.stringify(syndicate);
		return snippet;
	}
	
	function serializeDB(){
		var j={};
		j.dashboardName=$("#dashboardName").val();
		j.users=getDevelopers();
		j.success=false;
		return j;
	}
	
	function doAction(action){
		location.href="/RB-Dashboard-Profiler/controller?action="+action
	}
	
	function modifyDashBoard(dashboard){
		if(dashboard=='abort'){
			return;
		}
		alert();
	}
	
	function primer(dashboard){
		primedDashboard = dashboard;
	}
	
	function wrap(msg){
		return '<li>'+msg+'</li>'
	}
	
	function successMsg(msg){
		msg='<ul>'+msg+'</ul>';
		$("#successContainerMsg").html(msg);
		$("#successContainer").fadeIn(300).delay(3000).fadeOut(1000);
	}
	
	function errorMsg(msg){
		msg='<ul>'+msg+'</ul>';
		$("#errorContainerMsg").html(msg);
		$("#errorContainer").fadeIn(300).delay(3000).fadeOut(1000);
	}
	
//	function ctp(){
//		var template = '';
//		template+='<div class="row">';
//		template+='	<div class="col-lg-9">';
//		template+='		<i class="glyphicon glyphicon-user fa-1x"></i>';
//		template+='		<label>Compile Progress</label>';
//		template+='	</div>';
//		template+='</div>';
//		template+='<div class="row">';
//		template+='	<div class="col-lg-9">';
//		template+='		<div class="progress">';
//		template+='			<div class="progress-bar" role="progressbar" aria-valuenow="0"';
//		template+='				aria-valuemin="0" aria-valuemax="100" style="width: 0%" id="compileStatus">';
//		template+='				<span class="sr-only">0% Complete</span>';
//		template+='			</div>';
//		template+='		</div>';
//		template+='	</div>';
//		template+='</div>';
//		return template;
//	}

	function getThreadProgressTemplate(){
		var template = '';
		template+='<div class="row">';
		template+='<div class="col-lg-4">';
		template+='<i class="glyphicon glyphicon-user fa-1x"></i>';
		template+='	<label>@developer@</label>';
		template+='</div>';
		template+='<div class="col-lg-5">';
		template+='	<div class="progress">';
		template+='		<div class="progress-bar" role="progressbar" aria-valuenow="0"';
		template+='			aria-valuemin="0" aria-valuemax="100" style="width: 0%" id="@thread@">';
		template+='			<span class="sr-only">0% Complete</span>';
		template+='		</div>';
		template+='	</div>';
		template+='</div>';
		template+='</div>';
		return template;
	}
	
	function getCommentSummaryButton(){
		var template = '';
		template+='<div class="col-lg-2 col-md-3">';
		template+='<div class="panel @color@">';
		template+='	<div class="panel-heading">';
		template+='		<div class="row">';
		template+='			<div class="col-xs-3">';
		template+='				<i class="fa fa-comment fa-fw"></i>';
		template+='			</div>';
		template+='			<div class="col-xs-9 text-right">';
		template+='				<div class="medium">@commentsNum@ Comments!</div>';
		template+='				<div>@piName@</div>';
		template+='			</div>';
		template+='		</div>';
		template+='	</div>';
		template+='</div>';
		template+='</div>';
		return template;
	}
	
	function getDashboardButtonTemplateOptions(){
		var template = '';
		template+='<div class="col-lg-2 col-md-3">';
		template+='<div class="panel @color@">';
		template+='	<div class="panel-heading">';
		template+='		<div class="row">';
		template+='			<div class="col-xs-3">';
		template+='				<i class="glyphicon glyphicon-stats fa-3x"></i>';
		template+='			</div>';
		template+='			<div class="col-xs-9 text-right">';
		template+='				<div class="medium">@developersNum@ Developers!</div>';
		template+='				<div>@dashboardName@</div>';
		template+='			</div>';
		template+='		</div>';
		template+='	</div>';
		template+='	<a onmouseover="javascript:primer(\'@dashboard@\')">';
		template+='		<div class="panel-footer">';
		template+='			<button class="btn btn-primary" data-toggle="dashboard-action" data-title="Dashboard Actions!" data-placement="right">Action</button> <span';
		template+='				class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>';
		template+='			<div class="clearfix"></div>';
		template+='		</div>';
		template+='	</a>';
		template+='</div>';
		template+='</div>';
		return template;
	}
	
	function getDashboardButtonTemplate(){
		var template = '';
		template+='<div class="col-lg-2 col-md-3">';
		template+='<div class="panel @color@">';
		template+='	<div class="panel-heading">';
		template+='		<div class="row">';
		template+='			<div class="col-xs-3">';
		template+='				<i class="glyphicon glyphicon-stats fa-3x"></i>';
		template+='			</div>';
		template+='			<div class="col-xs-9 text-right">';
		template+='				<div class="medium">@developersNum@ Developers!</div>';
		template+='				<div>@dashboardName@</div>';
		template+='			</div>';
		template+='		</div>';
		template+='	</div>';
		template+='	<a href="javascript:modifyDashBoard(\'@dashboard@\')">';
		template+='		<div class="panel-footer">';
		template+='			<span class="pull-left">@options@</span> <span';
		template+='				class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>';
		template+='			<div class="clearfix"></div>';
		template+='		</div>';
		template+='	</a>';
		template+='</div>';
		template+='</div>';
		return template;
	}