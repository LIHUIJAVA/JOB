//Sliding Effect Control
//head.js("../assets/js/skin-select/jquery.cookie.js");
//head.js("../skin-select/skin-select.js");

////右侧菜单展开动画效果
head.js("assets/js/custom/scriptbreaker-multiple-accordion-1.js", function() {
	$(".topnav").accordionze({
		accordionze: true,
		speed: 500,
		closedSign: '<img src="assets/img/down.jpg">',
		openedSign: '<img src="assets/img/pull.jpg">'
	});
});

//header部分 时间日期设置
var weekarry = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'];
showtime();
setInterval(function() {
	showtime();
}, 1000)

function showtime() {
	var thatDate = new Date();
	var year = thatDate.getFullYear();
	var Month = thatDate.getMonth() + 1;
	var toweek = thatDate.getDay();
	var toDay = thatDate.getDate();
	var Hours = thatDate.getHours();
	var Minutes = thatDate.getMinutes();
	var Seconds = thatDate.getSeconds();
	$('.year').html((year + ' - ' + time(Month) + ' - ' + time(toDay)));
	$('.hours').html((weekarry[toweek] + ' &nbsp ' + time(Hours) + ' : ' + time(Minutes) + ' : ' + time(Seconds)));
}

function time(i) {
	if(i < 10) {
		return i = '0' + i;
	} else {
		return i = i;
	}
}

//搜索菜单
head.js("assets/js/search/jquery.quicksearch.js", function() {

	$('input.id_search').quicksearch('#menu-showhide li, .menu-left-nest li');

});