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
    
    var errorWrap = null;
    
    $.validity.outputs.showErr = {
    		
        start:function() {
    		errorMsg = '';
    		if(focusObj){
    			focusObj.next('.validity-error').hide();
    		}
    		focusObj = null;
            $(errors).removeClass(erroneous);
            if(errorWrap){
            	errorWrap.hide();
            }
        },

        end:function(results) {
        	if(results.errors > 0){
        		focusObj.focus();
        		if(errorMsg != null && errorMsg != ''){
        			alert(errorMsg);
        		}
        	}
        	hasError = false;
        },

        raise:function($obj, msg) {
        	if(focusObj == null){
        		focusObj = $obj;
        	}
        	errorWrap = null;
        	var errorWrap = $obj.next('.validity-error');
        	var validityErrorId = $obj.attr('validity-error')
        	if($.trim(validityErrorId) == ''){
        		errorWrap = $obj.next('.validity-error');
        	}else{
        		errorWrap = $('#' + validityErrorId);
        	}
    		if(errorWrap.size() > 0){
    			errorWrap.html(msg).show();
    		}else{
    			errorMsg += msg + '<br/>';
    		}
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