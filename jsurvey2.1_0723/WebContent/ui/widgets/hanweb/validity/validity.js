// 验证提示的模式，1：全部，0：单步
var validity_msg_mode = 1;
// Install the summary output
(function($) {
    var 
        // Erroneous refers to an input with an invalid value,
        // not the error message itself.
        erroneous = "error-border",
        
        // Selector for erroneous inputs.
        errors = "." + erroneous,
        
        hasError = false;
    
    var errorMsg = '';
    
    var focusObj = null;
    
    $.validity.outputs.showErr = {
    		
        start:function() {
    		errorMsg = '';
    		focusObj = null;
            $(errors).removeClass(erroneous);
        },

        end:function(results) {
        	if(results.errors > 0){
        		errorMsg = '<div style="padding-left:40px">' + errorMsg + '</div>';
            	alert(errorMsg,'warning',function(){
            		focusObj.focus();
        		});
        	}
        	hasError = false;
        },

        raise:function($obj, msg) {
        	if(focusObj == null){
        		focusObj = $obj;
        	}
        	errorMsg += msg + '<br/>';
            $obj.addClass(erroneous);
            if($obj.hasClass('select-val')){
            	var id = $obj.attr('id');
            	if(id){
            		$('#select_' + id).addClass(erroneous);
            	}
            }
        },

        raiseAggregate:function($obj, msg) {
        	if(hasError == false){
        		this.raise($obj, msg);
        		hasError = true;
        	}
        }
    };
})(jQuery);