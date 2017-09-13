$(function() {

	
    Morris.Area({
        element: 'morris-area-chart',
        "data":[{"period":"1 Ballina 1","v_mohanp":"0","v_ranah":"0","v_mahajanp":"0","v_jaybhays":"23","v_dashm":"17","v_kevittm":"1","v_padapanar":"1","v_marwaha":"0"},{"period":"2 Ballina 2","v_mohanp":"22","v_ranah":"44","v_mahajanp":"0","v_jaybhays":"0","v_dashm":"3","v_kevittm":"0","v_padapanar":"50","v_marwaha":"0"},{"period":"3 Ballina 3","v_mohanp":"22","v_ranah":"5","v_mahajanp":"0","v_jaybhays":"34","v_dashm":"33","v_kevittm":"15","v_padapanar":"22","v_marwaha":"6"},{"period":"4 Ballina 4","v_mohanp":"56","v_ranah":"25","v_mahajanp":"3","v_jaybhays":"17","v_dashm":"5","v_kevittm":"0","v_padapanar":"25","v_marwaha":"0"},{"period":"5 Ballina 5","v_mohanp":"23","v_ranah":"13","v_mahajanp":"0","v_jaybhays":"21","v_dashm":"6","v_kevittm":"0","v_padapanar":"30","v_marwaha":"0"},{"period":"6 Donegal 1","v_mohanp":"0","v_ranah":"14","v_mahajanp":"0","v_jaybhays":"7","v_dashm":"0","v_kevittm":"1","v_padapanar":"28","v_marwaha":"0"},{"period":"7 Donegal 2","v_mohanp":"8","v_ranah":"8","v_mahajanp":"1","v_jaybhays":"40","v_dashm":"0","v_kevittm":"4","v_padapanar":"16","v_marwaha":"0"},{"period":"8 Donegal 3","v_mohanp":"25","v_ranah":"27","v_mahajanp":"60","v_jaybhays":"36","v_dashm":"13","v_kevittm":"0","v_padapanar":"45","v_marwaha":"12"},{"period":"9 Donegal 4","v_mohanp":"52","v_ranah":"94","v_mahajanp":"0","v_jaybhays":"1","v_dashm":"0","v_kevittm":"2","v_padapanar":"7","v_marwaha":"0"},{"period":"10 Donegal 5","v_mohanp":"21","v_ranah":"48","v_mahajanp":"0","v_jaybhays":"25","v_dashm":"0","v_kevittm":"54","v_padapanar":"3","v_marwaha":"14"},{"period":"11 Gortahork 1","v_mohanp":"16","v_ranah":"21","v_mahajanp":"9","v_jaybhays":"3","v_dashm":"0","v_kevittm":"68","v_padapanar":"15","v_marwaha":"5"},{"period":"12 Gortahork 2","v_mohanp":"90","v_ranah":"15","v_mahajanp":"8","v_jaybhays":"0","v_dashm":"3","v_kevittm":"9","v_padapanar":"17","v_marwaha":"0"},{"period":"13 Gortahork 3","v_mohanp":"92","v_ranah":"33","v_mahajanp":"19","v_jaybhays":"2","v_dashm":"0","v_kevittm":"0","v_padapanar":"42","v_marwaha":"12"},{"period":"14 Gortahork 4","v_mohanp":"6","v_ranah":"27","v_mahajanp":"10","v_jaybhays":"15","v_dashm":"5","v_kevittm":"0","v_padapanar":"28","v_marwaha":"6"},{"period":"15 Gortahork 5","v_mohanp":"33","v_ranah":"58","v_mahajanp":"8","v_jaybhays":"50","v_dashm":"12","v_kevittm":"41","v_padapanar":"43","v_marwaha":"3"},{"period":"16 Derry 1","v_mohanp":"1","v_ranah":"4","v_mahajanp":"0","v_jaybhays":"0","v_dashm":"12","v_kevittm":"14","v_padapanar":"7","v_marwaha":"0"},{"period":"17 Derry 2","v_mohanp":"0","v_ranah":"40","v_mahajanp":"0","v_jaybhays":"13","v_dashm":"3","v_kevittm":"1","v_padapanar":"0","v_marwaha":"6"},{"period":"18 Derry 3","v_mohanp":"15","v_ranah":"3","v_mahajanp":"3","v_jaybhays":"14","v_dashm":"4","v_kevittm":"0","v_padapanar":"1","v_marwaha":"3"},{"period":"19 Derry 4","v_mohanp":"0","v_ranah":"0","v_mahajanp":"0","v_jaybhays":"0","v_dashm":"0","v_kevittm":"0","v_padapanar":"0","v_marwaha":"0"},{"period":"20 Derry 5","v_mohanp":"0","v_ranah":"0","v_mahajanp":"0","v_jaybhays":"0","v_dashm":"0","v_kevittm":"0","v_padapanar":"0","v_marwaha":"0"}],
		xkey: 'period',
        ykeys: ['v_dashm','v_jaybhays','v_kevittm','v_mahajanp','v_marwaha','v_mohanp','v_padapanar','v_ranah'],
        labels: ['v_dashm','v_jaybhays','v_kevittm','v_mahajanp','v_marwaha','v_mohanp','v_padapanar','v_ranah'],
        hideHover: 'auto',
		hoverCallback: function(index, options, content, row){
			primer(index, row);
			return getLabel(row, content);
		},
        resize: true
    });
	
	Morris.Bar({
        element: 'morris-bar-chart-developer',
        data: [{"y":"Ballina","v_mohanp":123,"v_ranah":87,"v_mahajanp":3,"v_jaybhays":95,"v_dashm":64,"v_kevittm":16,"v_padapanar":128,"v_marwaha":6},{"y":"Derry","v_mohanp":16,"v_ranah":47,"v_mahajanp":3,"v_jaybhays":27,"v_dashm":19,"v_kevittm":15,"v_padapanar":8,"v_marwaha":9},{"y":"Donegal","v_mohanp":106,"v_ranah":191,"v_mahajanp":61,"v_jaybhays":109,"v_dashm":13,"v_kevittm":61,"v_padapanar":99,"v_marwaha":26},{"y":"Gortahork","v_mohanp":237,"v_ranah":154,"v_mahajanp":54,"v_jaybhays":70,"v_dashm":20,"v_kevittm":118,"v_padapanar":145,"v_marwaha":26}],
        xkey: 'y',
        ykeys: ['v_mohanp','v_ranah','v_mahajanp','v_jaybhays','v_dashm','v_kevittm','v_padapanar','v_marwaha'],
        labels: ['v_mohanp','v_ranah','v_mahajanp','v_jaybhays','v_dashm','v_kevittm','v_padapanar','v_marwaha'],
        hideHover: 'auto',
        resize: true
    });
	
	Morris.Bar({
        element: 'morris-bar-chart-new-vs-existing',
        data: [{"y":"Ballina","a":200,"b":322},{"y":"Derry","a":2,"b":142},{"y":"Donegal","a":283,"b":383},{"y":"Gortahork","a":301,"b":523}],
        xkey: 'y',
        ykeys: ['a', 'b'],
        labels: ['New Files', 'Existing Files'],
        hideHover: 'auto',
        resize: true
    });
	
	Morris.Bar({
        element: 'morris-bar-chart',
        data: [{"y":"Ballina","a":87,"b":72,"c":72,"d":332},{"y":"Derry","a":13,"b":15,"c":10,"d":57},{"y":"Donegal","a":56,"b":114,"c":51,"d":353},{"y":"Gortahork","a":84,"b":112,"c":40,"d":373}],
        xkey: 'y',
        ykeys: ['a', 'b', 'c', 'd'],
        labels: ['Performance', 'Cosmetic', 'Duplicate', 'Undetermined'],
        hideHover: 'auto',
        resize: true
    });

    Morris.Donut({
        element: 'morris-donut-chart',
        data: [{"label":"v_mohanp","value":482},{"label":"v_ranah","value":479},{"label":"v_mahajanp","value":121},{"label":"v_jaybhays","value":301},{"label":"v_dashm","value":116},{"label":"v_kevittm","value":210},{"label":"v_padapanar","value":380},{"label":"v_marwaha","value":67}],
        resize: true
    });
	
	Morris.Donut({
        element: 'morris-donut-chart-contributors',
        data: [{"label":"Beulah Shanti Kumar","value":1},{"label":"Peter Dunne","value":6},{"label":"Aisling Clinch","value":221},{"label":"Anna Zasadzinska","value":10},{"label":"John McCosker","value":140},{"label":"Rob Say","value":8},{"label":"Mohan Pattabhiramaswamy","value":346},{"label":"Hugo Dias","value":81},{"label":"Jemma McCreesh","value":2},{"label":"Sachin Jaybhay","value":222},{"label":"George Mavroudis","value":1},{"label":"Mahendra Dash","value":452},{"label":"Vinod Geeachan","value":6},{"label":"Priti Mahajan","value":115},{"label":"Filipe Rodrigues","value":29},{"label":"Dorota Mikolajczak","value":3},{"label":"Tom Gurrie","value":136},{"label":"Alberto Galende","value":58},{"label":"Jose Martinez","value":87},{"label":"Sergio Torres","value":27},{"label":"Daniel Dardis","value":45},{"label":"Ramana Padapana","value":204},{"label":"Theo Douglas","value":1},{"label":"Brian Cowzer","value":32},{"label":"Katarzyna Kurszewska","value":15},{"label":"Gil Matias","value":36},{"label":"Anita Marwah","value":71},{"label":"Hiren Rana","value":346},{"label":"Conor Hogan","value":3},{"label":"Daniel Delgado","value":27},{"label":"Killian Farrell","value":27},{"label":"Mark Kevitt","value":40},{"label":"Trevor Higgins","value":666}],
        resize: true
    });

    
    
});
