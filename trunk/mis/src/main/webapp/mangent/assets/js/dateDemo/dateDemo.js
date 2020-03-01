$(function() {
	var date = new Date();
	var init = {
		year: date.getFullYear(), // 现年
		month: date.getMonth() + 1 //现月
	};
	var yearFunc = {
		init: function() {
			this.showYear();
			this.weeks();
			this.nowMonth();
			this.monthDay();
		},
		showYear: function() {
			var nowYear = init.year,
				yearHtml = "<h1 class='years'>" + nowYear + "</h1>";
			$('.nowYear').html(yearHtml);
		},
		nowMonth: function() {
			var nowMonth = init.month,
				monthHtml = "<h1 class='months'>" + nowMonth + "</h1>";
			if(nowMonth < 10) {
				nowMonth = "0" + nowMonth;
			}
			$('.nowMonth').html(monthHtml);
		},
		weeks: function() {
			var weeksArr = ["日/SUN", "一/MON", "二/TUE", "三/WEB", "四/THU", "五/FRI", "六/SAT"];
			var weeksDay = '';
			for(var i = 0; i < weeksArr.length; i++) {
				weeksDay += "<span>" + weeksArr[i] + "</span>";
			}
			$('.weeks').html(weeksDay);
		},
		monthDay: function(year, month) {
			year = $('.years').text();
			month = $('.months').text();

			var setDate = new Date();
			setDate.setFullYear(year); // 设置年份
			setDate.setMonth(month - 1); //设置月份，因为系统的月份都是比真是的-1
			setDate.setDate(1); // 设置当前月第一天

			var nums = setDate.getDay(); // 得到本月第一天是星期几
			setDate.setMonth(month);
			var lastDate = new Date(setDate.getTime() - 1000 * 60 * 60 * 24); // 计算当前月最后一天的日期格式
			var lastDay = lastDate.getDate(); // 获取本月最后一天是几号

			// 利用得到的当前月总天数来循环当月日历
			var monthDay = '';
			for(var i = 1; i <= lastDay; i++) {
				monthDay += "<span>" + i + "</span>";
			}
			$('.dates').html(monthDay);

			var firstDay = $('.dates span:first'); // 获取不到元素的宽度
			firstDay.css('marginLeft', 55 * nums + 'px');

			var nowDate = new Date(),
				nowYear = nowDate.getFullYear(),
				nowMonth = nowDate.getMonth() + 1,
				today = nowDate.getDate(); //获取当前是几号
			if(nowYear == year && nowMonth == month) {
				$('.dates span').eq(today - 1).addClass('todayBg'); // 标出今天
			}
		}
	}
	yearFunc.init(); //运行

});