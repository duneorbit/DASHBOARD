var basic_words = [
	{text: "Lorem", weight: 13},
	{text: "Ipsum", weight: 10.5},
	{text: "Dolor", weight: 9.4},
	{text: "Sit", weight: 8},
	{text: "Amet", weight: 6.2},
	{text: "Consectetur", weight: 5},
	{text: "Adipiscing", weight: 5},
	{text: "Elit", weight: 5},
	{text: "Nam et", weight: 5},
	{text: "Leo", weight: 4},
	{text: "Sapien", weight: 4},
	{text: "Pellentesque", weight: 3},
	{text: "habitant", weight: 3},
	{text: "morbi", weight: 3},
	{text: "tristisque", weight: 3},
	{text: "senectus", weight: 3},
	{text: "et netus", weight: 3},
	{text: "et malesuada", weight: 3},
	{text: "fames", weight: 2},
	{text: "ac turpis", weight: 2},
	{text: "egestas", weight: 2},
	{text: "Aenean", weight: 2},
	{text: "vestibulum", weight: 2},
	{text: "elit", weight: 2},
	{text: "sit amet", weight: 2},
	{text: "metus", weight: 2},
	{text: "adipiscing", weight: 2},
	{text: "ut ultrices", weight: 2},
	{text: "justo", weight: 1},
	{text: "dictum", weight: 1},
	{text: "Ut et leo", weight: 1},
	{text: "metus", weight: 1},
	{text: "at molestie", weight: 1},
	{text: "purus", weight: 1},
	{text: "Curabitur", weight: 1},
	{text: "diam", weight: 1},
	{text: "dui", weight: 1},
	{text: "ullamcorper", weight: 1},
	{text: "id vuluptate ut", weight: 1},
	{text: "mattis", weight: 1},
	{text: "et nulla", weight: 1},
	{text: "Sed", weight: 1}
];

var linked_words = $.extend(true, [], basic_words);
linked_words.splice(0, 3, 
	{text: "Lorem", weight: 13, link: 'http://github.com/mistic100/jQCloud'},
	{text: "Ipsum", weight: 10.5, link: 'http://www.strangeplanet.fr'},
	{text: "Dolor", weight: 9.4, link: 'http://piwigo.org'}
);

var handlers_words = $.extend(true, [], basic_words);
handlers_words.splice(0, 1, 
	{text: "Lorem", weight: 13, handlers: {
		click: function() {
			alert('You clicked the word !');
		}
	}}
);