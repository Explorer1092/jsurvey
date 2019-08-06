/*
* Accordion v1.0
* Developed by Yuriy Vaskevich
* Simple jQuery Accordion plugin
* Date: 02/24/2013
* Usege: $("dl").SimleAccordion();
* This work is licensed under a Creative Commons Attribution-ShareAlike 3.0
* Unported License: http://creativecommons.org/licenses/by-sa/3.0/
*/
(function( $ ) {
	$.fn.SimpleAccordion = function () {
		return $(this).each(function(index, element){
			// Cache element
			var accordion = $(element);
			// Fade in on load
			accordion.hide().fadeIn(0);
			// Open active panel
			accordion.find(".active").show(0);
			// Listen to onClick
			accordion.find("dt").on("click", function (){
				// Cache current
				var current = $(this).next("dd");
				// Check if not active
				if (current.is(":hidden")) {
					// Open curren panel
					current.slideDown(200).siblings("dd").slideUp(200);
				}
			});
			var menus = accordion.find("dd a.accordion-sub-menu");
			menus.click(function(){
				menus.removeClass('selected');
				$(this).addClass('selected');
			});
		});
	};
})( jQuery );

$(function(){
	$(".accordion").SimpleAccordion();
})