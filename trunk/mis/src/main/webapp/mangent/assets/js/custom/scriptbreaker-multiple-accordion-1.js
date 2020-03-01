/*
 * jQuery UI Multilevel accordionze v.1
 * 
 * Copyright (c) 2011 Pieter Pareit
 *
 * http://www.scriptbreaker.com
 *
 */

//plugin definition
(function($) {
	$.fn.extend({

		//pass the options variable to the function
		accordionze: function(options) {

			var defaults = {
				accordionze: 'true',
				speed: 300,
				closedSign: '[+]',
				openedSign: '[-]'
			};

			// Extend our default options with those provided.
			var opts = $.extend(defaults, options);
			//Assign current element to variable, in this case is UL element
			var $this = $(this);

			//add a mark [+] to a multilevel menu
			$this.find("li").each(function() {
				if($(this).find("ul").size() != 0) {
					//add the multilevel sign next to the link
					$(this).find("a:first").append("<h4>" + opts.closedSign + "</h4>");

					//avoid jumping to the top of the page when the href is an #
					if($(this).find("a:first").attr('href') == "#") {
						$(this).find("a:first").click(function() {
							return false;
						});
					}
				}
			});

			//open active level
			$this.find("li.active").each(function() {
				$(this).parents("ul").slideDown(opts.speed);
				$(this).parents("ul").parent("li").find("h4:first").html(opts.openedSign);
			});

			$this.find("li a").on("click", function() {
				//			$("#dialog").load('Components/dialog_public.html');
				if($(this).parent().find("ul").size() != 0) {
					if(opts.accordionze) {
						//Do nothing when the list is open
						if(!$(this).parent().find("ul").is(':visible')) {
							parents = $(this).parent().parents("ul");
							visible = $this.find("ul:visible");

							visible.each(function(visibleIndex) {
								var close = true;
								parents.each(function(parentIndex) {
									if(parents[parentIndex] == visible[visibleIndex]) {
										close = false;
										return false;
									}
								});
								if(close) {
									if($(this).parent().find("ul") != visible[visibleIndex]) {
										$(visible[visibleIndex]).slideUp(opts.speed, function() {
											$(this).parent("li").find("h4:first").html(opts.closedSign);
										});

									}
								}
							});

						}
						if($(this).parent().find("ul:first").is(":visible")) {
							$(this).parent().find("ul:first").slideUp(opts.speed, function() {
								$(this).parent("li").find("h4:first").delay(opts.speed).html(opts.closedSign);
								 $(".paper-wrap").css("height","auto");
								var h = $(".paper-wrap").outerHeight(true);
								var skinHeight = $("#skin-select").outerHeight(true);
								if(h>skinHeight){
								  $(".paper-wrap").animate({
									height: (h) + 'px'
								  }, 600); //设置主要内容区域高度	
								}else {
									 $(".paper-wrap").animate({
									height: (skinHeight) + 'px'
								  }, 600); //设置主要内容区域高度	
								}
							});

						} else {
							$(this).parent().find("li").css("display", "block");
							$(this).parent().find("ul:first").slideDown(opts.speed, function() {
								$(this).parent("li").find("h4:first").delay(opts.speed).html(opts.openedSign);
								 $(".paper-wrap").css("height","auto");
								var h = $(".paper-wrap").outerHeight(true);
								var skinHeight = $("#skin-select").outerHeight(true);
								if(h>skinHeight){
								  $(".paper-wrap").animate({
									height: (h) + 'px'
								  }, 600); //设置主要内容区域高度	
								}else {
									 $(".paper-wrap").animate({
									height: (skinHeight) + 'px'
								  }, 600); //设置主要内容区域高度	
								}
							});
						}
					}
				}
			});
		}
	});
})(jQuery);