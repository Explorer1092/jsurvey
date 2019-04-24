(function ($) {
    $.fn.chosen = function (opt) {
    	var $this = $(this);
    	$this.each(function(index, element){
    		var $select = $(element);
    		var defaultValue = $select.attr('data-value');
    		var onSelected = $select.attr('data-select');
    		var multiple = $select.attr('multiple');
        	if(multiple == 'multiple'){
        		multiple = true;
        		if(defaultValue){
        			defaultValue = defaultValue.split(',');
        		}
        	}else{
        		multiple = false;
        	}
        	var $options = $('option[value]', $select);
        	var checkStyle = '<span>';
        	$options.each(function(index, option){
        		var $option = $(option);
        		var text = $option.html();
        		var value = $option.attr('value');
        		if(!value || value == ''){
        			return true;
        		}
        		var selected = '';
        		var checkbox = '';
        		if(multiple){
        			$.each(defaultValue, function(index, defaultVal){
        				if(defaultVal == value){
        					selected = ' checked';
        					return false;
        				}
        			});
        			checkbox = ' checkbox';
        		}else{
        			if(defaultValue && defaultValue != ''){
        				if(defaultValue == value){
        					selected = ' checked';
        				}
        			}else if($option.attr('selected')){
        				selected = ' checked';
        			}
        		}
        		checkStyle += '<a class="chkbox' + checkbox + selected + '" data-value="' + value + '"><span class="check-image"></span><span class="radiobox-content">' + text + '</span></a>';
        	});
        	checkStyle += '</span>';
        	checkStyle = $(checkStyle);
        	var $options = $('.chkbox', checkStyle);
        	$options.click(function(){
        		var $option = $(this);
        		if(!multiple){// 如果是单选
        			//$('.check-image', $options).removeClass('check');
        			if($option.hasClass('checked')){
        				return;
        			}
        			$options.removeClass('checked');
        		}
        		//$('.check-image', $option).toggleClass('check');
        		$option.toggleClass('checked');
        		var text = $('radiobox-content', $option).html();
        		var valueArray = new Array();
        		var selectedOptions = new Array();
        		$('.chkbox.checked', checkStyle).each(function(){
        			var value = $(this).attr('data-value');
        			var text = $(this).find('.radiobox-content').html();
        			valueArray.push(value);
        			var selectedOption = {
        					value:value,
        					text:text
        			}
        			selectedOptions.push(selectedOption);
        		});
        		$select.val(valueArray);
        		if(onSelected && onSelected != ''){
        			window[onSelected]($select, selectedOptions);
        		}
        	});
        	$select.after(checkStyle);
        	$select.hide();
        	if(defaultValue && defaultValue != ''){
        		$select.val(defaultValue);
        	} 
    	});
    	
        /**
        //判断是否选中
        rdochecked(tag);

        //单选or多选
        if (tag2 == "rdo") {
            //单选
            $(".rdobox").click(function () {
                $(this).prev().prop("checked", "checked");
                rdochecked(tag);
            });
        } else {
            //多选
            $(".chkbox").click(function () {
                //
                if ($(this).prev().prop("checked") == true) {
                    $(this).prev().removeAttr("checked");
                }
                else {
                    $(this).prev().prop("checked", "checked");
                }
                rdochecked(tag);
            });
        }

        //判断是否选中
        function rdochecked(tag) {
            $('.' + tag).each(function (i) {
                var rdobox = $('.' + tag).eq(i).next();
                if ($('.' + tag).eq(i).prop("checked") == false) {
                    rdobox.removeClass("checked");
                    rdobox.addClass("unchecked");
                    rdobox.find(".check-image").css("background", "url(images/input-unchecked.png)");
                }
                else {
                    rdobox.removeClass("unchecked");
                    rdobox.addClass("checked");
                    rdobox.find(".check-image").css("background", "url(images/input-checked.png)");
                }
            });
        }
        */
    }
}(jQuery));
$(function(){
	$('select.chosen').chosen();
});