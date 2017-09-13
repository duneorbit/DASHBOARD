$(function() {

	
    Morris.Area({
        element: 'morris-area-chart',
        "data":[{"period":"1 Ballina 1","mcCreeshj":"14","clincha":"2","dardisd":"0","kumarb":"0","rodriguesf":"11","dmccaffrey":"5","treacyr":"14","matiasg":"0","cowzerb":"13"},{"period":"2 Ballina 2","mcCreeshj":"0","clincha":"6","dardisd":"0","kumarb":"17","rodriguesf":"3","dmccaffrey":"14","treacyr":"0","matiasg":"0","cowzerb":"7"},{"period":"3 Ballina 3","mcCreeshj":"0","clincha":"10","dardisd":"11","kumarb":"0","rodriguesf":"20","dmccaffrey":"0","treacyr":"0","matiasg":"65","cowzerb":"10"},{"period":"4 Ballina 4","mcCreeshj":"0","clincha":"19","dardisd":"12","kumarb":"0","rodriguesf":"30","dmccaffrey":"0","treacyr":"34","matiasg":"31","cowzerb":"14"},{"period":"5 Ballina 5","mcCreeshj":"0","clincha":"9","dardisd":"15","kumarb":"9","rodriguesf":"0","dmccaffrey":"0","treacyr":"22","matiasg":"38","cowzerb":"19"},{"period":"6 Donegal 1","mcCreeshj":"4","clincha":"14","dardisd":"15","kumarb":"5","rodriguesf":"3","dmccaffrey":"0","treacyr":"42","matiasg":"34","cowzerb":"36"},{"period":"7 Donegal 2","mcCreeshj":"9","clincha":"2","dardisd":"12","kumarb":"6","rodriguesf":"5","dmccaffrey":"0","treacyr":"5","matiasg":"15","cowzerb":"23"},{"period":"8 Donegal 3","mcCreeshj":"0","clincha":"8","dardisd":"4","kumarb":"0","rodriguesf":"0","dmccaffrey":"0","treacyr":"12","matiasg":"26","cowzerb":"5"},{"period":"9 Donegal 4","mcCreeshj":"0","clincha":"28","dardisd":"12","kumarb":"33","rodriguesf":"4","dmccaffrey":"0","treacyr":"11","matiasg":"17","cowzerb":"18"},{"period":"10 Donegal 5","mcCreeshj":"0","clincha":"17","dardisd":"87","kumarb":"0","rodriguesf":"0","dmccaffrey":"0","treacyr":"3","matiasg":"23","cowzerb":"42"},{"period":"11 Gortahork 1","mcCreeshj":"19","clincha":"11","dardisd":"21","kumarb":"0","rodriguesf":"2","dmccaffrey":"0","treacyr":"14","matiasg":"48","cowzerb":"6"},{"period":"12 Gortahork 2","mcCreeshj":"0","clincha":"19","dardisd":"25","kumarb":"6","rodriguesf":"19","dmccaffrey":"0","treacyr":"26","matiasg":"116","cowzerb":"15"},{"period":"13 Gortahork 3","mcCreeshj":"0","clincha":"0","dardisd":"53","kumarb":"0","rodriguesf":"5","dmccaffrey":"3","treacyr":"24","matiasg":"33","cowzerb":"9"},{"period":"14 Gortahork 4","mcCreeshj":"4","clincha":"14","dardisd":"16","kumarb":"1","rodriguesf":"5","dmccaffrey":"0","treacyr":"0","matiasg":"28","cowzerb":"25"},{"period":"15 Gortahork 5","mcCreeshj":"2","clincha":"17","dardisd":"32","kumarb":"0","rodriguesf":"20","dmccaffrey":"0","treacyr":"52","matiasg":"0","cowzerb":"0"},{"period":"16 Derry 1","mcCreeshj":"3","clincha":"4","dardisd":"22","kumarb":"7","rodriguesf":"29","dmccaffrey":"3","treacyr":"0","matiasg":"80","cowzerb":"23"},{"period":"17 Derry 2","mcCreeshj":"38","clincha":"18","dardisd":"33","kumarb":"25","rodriguesf":"12","dmccaffrey":"12","treacyr":"4","matiasg":"1","cowzerb":"0"},{"period":"18 Derry 3","mcCreeshj":"3","clincha":"2","dardisd":"17","kumarb":"4","rodriguesf":"3","dmccaffrey":"0","treacyr":"5","matiasg":"0","cowzerb":"26"},{"period":"19 Derry 4","mcCreeshj":"0","clincha":"0","dardisd":"0","kumarb":"0","rodriguesf":"0","dmccaffrey":"0","treacyr":"0","matiasg":"0","cowzerb":"0"},{"period":"20 Derry 5","mcCreeshj":"0","clincha":"0","dardisd":"0","kumarb":"0","rodriguesf":"0","dmccaffrey":"0","treacyr":"0","matiasg":"0","cowzerb":"0"}],
		xkey: 'period',
        ykeys: ['clincha','cowzerb','dardisd','dmccaffrey','kumarb','matiasg','mcCreeshj','rodriguesf','treacyr'],
        labels: ['clincha','cowzerb','dardisd','dmccaffrey','kumarb','matiasg','mcCreeshj','rodriguesf','treacyr'],
        hideHover: 'auto',
		hoverCallback: function(index, options, content, row){
			primer(index, row);
			return getLabel(row, content);
		},
        resize: true
    });
	
	Morris.Bar({
        element: 'morris-bar-chart-developer',
        data: [{"y":"Ballina","mcCreeshj":14,"clincha":46,"dardisd":38,"kumarb":26,"rodriguesf":64,"dmccaffrey":19,"treacyr":70,"matiasg":134,"cowzerb":63},{"y":"Derry","mcCreeshj":44,"clincha":24,"dardisd":72,"kumarb":36,"rodriguesf":44,"dmccaffrey":15,"treacyr":9,"matiasg":81,"cowzerb":49},{"y":"Donegal","mcCreeshj":13,"clincha":69,"dardisd":130,"kumarb":44,"rodriguesf":12,"treacyr":73,"matiasg":115,"cowzerb":124},{"y":"Gortahork","mcCreeshj":25,"clincha":61,"dardisd":147,"kumarb":7,"rodriguesf":51,"dmccaffrey":3,"treacyr":116,"matiasg":225,"cowzerb":55}],
        xkey: 'y',
        ykeys: ['mcCreeshj','clincha','dardisd','kumarb','rodriguesf','dmccaffrey','treacyr','matiasg','cowzerb'],
        labels: ['mcCreeshj','clincha','dardisd','kumarb','rodriguesf','dmccaffrey','treacyr','matiasg','cowzerb'],
        hideHover: 'auto',
        resize: true
    });
	
	Morris.Bar({
        element: 'morris-bar-chart-new-vs-existing',
        data: [{"y":"Ballina","a":186,"b":288},{"y":"Derry","a":171,"b":203},{"y":"Donegal","a":148,"b":432},{"y":"Gortahork","a":214,"b":476}],
        xkey: 'y',
        ykeys: ['a', 'b'],
        labels: ['New Files', 'Existing Files'],
        hideHover: 'auto',
        resize: true
    });
	
	Morris.Bar({
        element: 'morris-bar-chart',
        data: [{"y":"Ballina","a":52,"b":50,"c":21,"d":186},{"y":"Derry","a":13,"b":26,"c":28,"d":93},{"y":"Donegal","a":24,"b":41,"c":7,"d":164},{"y":"Gortahork","a":56,"b":51,"c":39,"d":232}],
        xkey: 'y',
        ykeys: ['a', 'b', 'c', 'd'],
        labels: ['Performance', 'Cosmetic', 'Duplicate', 'Undetermined'],
        hideHover: 'auto',
        resize: true
    });

    Morris.Donut({
        element: 'morris-donut-chart',
        data: [{"label":"mcCreeshj","value":96},{"label":"clincha","value":200},{"label":"dardisd","value":387},{"label":"kumarb","value":113},{"label":"rodriguesf","value":171},{"label":"dmccaffrey","value":37},{"label":"treacyr","value":268},{"label":"matiasg","value":555},{"label":"cowzerb","value":291}],
        resize: true
    });
	
	Morris.Donut({
        element: 'morris-donut-chart-contributors',
        data: [{"label":"Deirdre McCaffrey","value":23},{"label":"John Canning","value":1},{"label":"Peter Dunne","value":23},{"label":"Beulah Shanti Kumar","value":34},{"label":"Aisling Clinch","value":461},{"label":"Daniel Doherty","value":8},{"label":"Katarzyna Moloniewicz","value":18},{"label":"Jonathan Kernan","value":16},{"label":"Richard Fanning","value":1},{"label":"Kevin Lawlor","value":1},{"label":"Hugo Dias","value":83},{"label":"Jemma McCreesh","value":23},{"label":"Paul Fitzgerald","value":2},{"label":"George Mavroudis","value":4},{"label":"Vinod Geeachan","value":4},{"label":"Filipe Rodrigues","value":160},{"label":"Tom Gurrie","value":88},{"label":"Alberto Galende","value":16},{"label":"Jose Martinez","value":92},{"label":"Sergio Torres","value":9},{"label":"Daniel Dardis","value":367},{"label":"Louise Miller","value":5},{"label":"Drazen Dostal","value":1},{"label":"Robert Treacy","value":267},{"label":"Brian Cowzer","value":186},{"label":"Katarzyna Kurszewska","value":10},{"label":"Gil Matias","value":320},{"label":"Conor Hogan","value":4},{"label":"Maciej Wyrzykowski","value":2},{"label":"Ferghal Smyth","value":7},{"label":"Daniel Delgado","value":29},{"label":"Killian Farrell","value":25},{"label":"Trevor Higgins","value":539}],
        resize: true
    });

    
    
});
