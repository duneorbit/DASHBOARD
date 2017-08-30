$(function() {

	
    Morris.Area({
        element: 'morris-area-chart',
        @developer-comments-per-sprint-data@,
		xkey: 'period',
        ykeys: [@developer-comments-per-sprint-ykeys@],
        labels: [@developer-comments-per-sprint-labels@],
        hideHover: 'auto',
		hoverCallback: function(index, options, content, row){
			primer(index, row);
			return getLabel(row, content);
		},
        resize: true
    });
	
	Morris.Bar({
        element: 'morris-bar-chart-developer',
        data: @developer-comment-summary-per-pi@,
        xkey: 'y',
        ykeys: [@developer-comment-summary-per-pi-ykeys@],
        labels: [@developer-comment-summary-per-pi-labels@],
        hideHover: 'auto',
        resize: true
    });
	
	Morris.Bar({
        element: 'morris-bar-chart-new-vs-existing',
        data: @new-old-file-json@,
        xkey: 'y',
        ykeys: ['a', 'b'],
        labels: ['New Files', 'Existing Files'],
        hideHover: 'auto',
        resize: true
    });
	
	Morris.Bar({
        element: 'morris-bar-chart',
        data: @performanance-cosmetic-json@,
        xkey: 'y',
        ykeys: ['a', 'b', 'c', 'd'],
        labels: ['Performance', 'Cosmetic', 'Duplicate', 'Undetermined'],
        hideHover: 'auto',
        resize: true
    });

    Morris.Donut({
        element: 'morris-donut-chart',
        data: @team-comments-all@,
        resize: true
    });
	
	Morris.Donut({
        element: 'morris-donut-chart-contributors',
        data: @others-teams-comments-all@,
        resize: true
    });

    
    
});
