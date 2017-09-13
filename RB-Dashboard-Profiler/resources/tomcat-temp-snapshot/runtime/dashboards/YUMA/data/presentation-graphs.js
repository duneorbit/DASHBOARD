$(function() {

	
    Morris.Area({
        element: 'morris-area-chart',
        "data":[{"period":"1 Ballina 1","torress":"42","diash":"7","geeachanv":"3","jmartinez":"0","galendea":"1","delgadod":"7","gurriet":"0","higginst":"3"},{"period":"2 Ballina 2","torress":"14","diash":"78","geeachanv":"4","jmartinez":"0","galendea":"27","delgadod":"25","gurriet":"0","higginst":"10"},{"period":"3 Ballina 3","torress":"2","diash":"12","geeachanv":"5","jmartinez":"0","galendea":"6","delgadod":"21","gurriet":"10","higginst":"9"},{"period":"4 Ballina 4","torress":"7","diash":"35","geeachanv":"0","jmartinez":"16","galendea":"0","delgadod":"16","gurriet":"7","higginst":"1"},{"period":"5 Ballina 5","torress":"8","diash":"22","geeachanv":"0","jmartinez":"16","galendea":"6","delgadod":"6","gurriet":"6","higginst":"1"},{"period":"6 Donegal 1","torress":"44","diash":"5","geeachanv":"12","jmartinez":"39","galendea":"36","delgadod":"0","gurriet":"0","higginst":"8"},{"period":"7 Donegal 2","torress":"24","diash":"11","geeachanv":"15","jmartinez":"17","galendea":"11","delgadod":"1","gurriet":"3","higginst":"6"},{"period":"8 Donegal 3","torress":"7","diash":"13","geeachanv":"2","jmartinez":"21","galendea":"24","delgadod":"0","gurriet":"3","higginst":"8"},{"period":"9 Donegal 4","torress":"9","diash":"24","geeachanv":"29","jmartinez":"8","galendea":"15","delgadod":"0","gurriet":"1","higginst":"12"},{"period":"10 Donegal 5","torress":"24","diash":"100","geeachanv":"0","jmartinez":"39","galendea":"3","delgadod":"13","gurriet":"3","higginst":"40"},{"period":"11 Gortahork 1","torress":"30","diash":"43","geeachanv":"4","jmartinez":"25","galendea":"2","delgadod":"0","gurriet":"0","higginst":"12"},{"period":"12 Gortahork 2","torress":"37","diash":"20","geeachanv":"26","jmartinez":"108","galendea":"19","delgadod":"32","gurriet":"6","higginst":"13"},{"period":"13 Gortahork 3","torress":"27","diash":"38","geeachanv":"9","jmartinez":"27","galendea":"13","delgadod":"11","gurriet":"8","higginst":"20"},{"period":"14 Gortahork 4","torress":"19","diash":"28","geeachanv":"0","jmartinez":"43","galendea":"13","delgadod":"17","gurriet":"11","higginst":"30"},{"period":"15 Gortahork 5","torress":"20","diash":"15","geeachanv":"0","jmartinez":"20","galendea":"19","delgadod":"34","gurriet":"14","higginst":"9"},{"period":"16 Derry 1","torress":"39","diash":"87","geeachanv":"0","jmartinez":"0","galendea":"0","delgadod":"12","gurriet":"0","higginst":"15"},{"period":"17 Derry 2","torress":"0","diash":"26","geeachanv":"1","jmartinez":"2","galendea":"4","delgadod":"11","gurriet":"0","higginst":"20"},{"period":"18 Derry 3","torress":"0","diash":"0","geeachanv":"3","jmartinez":"0","galendea":"0","delgadod":"1","gurriet":"0","higginst":"0"},{"period":"19 Derry 4","torress":"0","diash":"0","geeachanv":"0","jmartinez":"0","galendea":"0","delgadod":"0","gurriet":"0","higginst":"0"},{"period":"20 Derry 5","torress":"0","diash":"0","geeachanv":"0","jmartinez":"0","galendea":"0","delgadod":"0","gurriet":"0","higginst":"0"}],
		xkey: 'period',
        ykeys: ['delgadod','diash','galendea','geeachanv','gurriet','higginst','jmartinez','torress'],
        labels: ['delgadod','diash','galendea','geeachanv','gurriet','higginst','jmartinez','torress'],
        hideHover: 'auto',
		hoverCallback: function(index, options, content, row){
			primer(index, row);
			return getLabel(row, content);
		},
        resize: true
    });
	
	Morris.Bar({
        element: 'morris-bar-chart-developer',
        data: [{"y":"Ballina","torress":73,"diash":154,"geeachanv":12,"jmartinez":32,"galendea":40,"delgadod":75,"gurriet":23,"higginst":24},{"y":"Derry","torress":39,"diash":113,"geeachanv":4,"jmartinez":2,"galendea":4,"delgadod":24,"higginst":35},{"y":"Donegal","torress":108,"diash":153,"geeachanv":58,"jmartinez":124,"galendea":89,"delgadod":14,"gurriet":10,"higginst":74},{"y":"Gortahork","torress":133,"diash":144,"geeachanv":39,"jmartinez":223,"galendea":66,"delgadod":94,"gurriet":39,"higginst":84}],
        xkey: 'y',
        ykeys: ['torress','diash','geeachanv','jmartinez','galendea','delgadod','gurriet','higginst'],
        labels: ['torress','diash','geeachanv','jmartinez','galendea','delgadod','gurriet','higginst'],
        hideHover: 'auto',
        resize: true
    });
	
	Morris.Bar({
        element: 'morris-bar-chart-new-vs-existing',
        data: [{"y":"Ballina","a":198,"b":235},{"y":"Derry","a":21,"b":200},{"y":"Donegal","a":200,"b":430},{"y":"Gortahork","a":213,"b":609}],
        xkey: 'y',
        ykeys: ['a', 'b'],
        labels: ['New Files', 'Existing Files'],
        hideHover: 'auto',
        resize: true
    });
	
	Morris.Bar({
        element: 'morris-bar-chart',
        data: [{"y":"Ballina","a":3,"b":7,"c":50,"d":85},{"y":"Derry","a":3,"b":2,"c":3,"d":33},{"y":"Donegal","a":39,"b":16,"c":26,"d":79},{"y":"Gortahork","a":13,"b":21,"c":7,"d":97}],
        xkey: 'y',
        ykeys: ['a', 'b', 'c', 'd'],
        labels: ['Performance', 'Cosmetic', 'Duplicate', 'Undetermined'],
        hideHover: 'auto',
        resize: true
    });

    Morris.Donut({
        element: 'morris-donut-chart',
        data: [{"label":"torress","value":353},{"label":"diash","value":564},{"label":"geeachanv","value":113},{"label":"jmartinez","value":381},{"label":"galendea","value":199},{"label":"delgadod","value":207},{"label":"gurriet","value":72},{"label":"higginst","value":217}],
        resize: true
    });
	
	Morris.Donut({
        element: 'morris-donut-chart-contributors',
        data: [{"label":"John Canning","value":3},{"label":"Peter Dunne","value":8},{"label":"Maciej Lewalski","value":16},{"label":"Aisling Clinch","value":93},{"label":"Daniel Doherty","value":1},{"label":"Eoin Cusack","value":1},{"label":"ReportsUser","value":3},{"label":"Rob Say","value":3},{"label":"Hugo Dias","value":260},{"label":"Mahendra Dash","value":3},{"label":"Vinod Geeachan","value":69},{"label":"Priti Mahajan","value":1},{"label":"Filipe Rodrigues","value":54},{"label":"Dorota Mikolajczak","value":3},{"label":"Alberto Galende","value":192},{"label":"Tom Gurrie","value":109},{"label":"Jose Martinez","value":353},{"label":"Colin Doyle","value":9},{"label":"Sergio Torres","value":159},{"label":"Sebastian Dabrowski","value":9},{"label":"Daniel Dardis","value":3},{"label":"Tadhg Hamill","value":3},{"label":"Theo Douglas","value":1},{"label":"Drazen Dostal","value":1},{"label":"Stephen Gallagher","value":1},{"label":"Robert Treacy","value":1},{"label":"Brian Cowzer","value":11},{"label":"Katarzyna Kurszewska","value":74},{"label":"Abel Mendivil","value":2},{"label":"Ruairi White","value":2},{"label":"Gil Matias","value":69},{"label":"Hiren Rana","value":3},{"label":"Conor Hogan","value":19},{"label":"Daniel Delgado","value":304},{"label":"Killian Farrell","value":1},{"label":"Trevor Higgins","value":976}],
        resize: true
    });

    
    
});
