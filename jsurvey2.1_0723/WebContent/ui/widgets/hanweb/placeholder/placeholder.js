(function($){
	/**
	 * config.class=
	 * config.style=
	 */
	jQuery.fn.placeholder = function(config) {
		$.each(this, function(index, element){
			if('placeholder' in document.createElement('input')){
				return false;
			}
			var $this = $(element);
			var text = $this.attr('placeholder');
			if(text && $.trim(text) != ''){
				$this.removeAttr('placeholder');
				var $wrap = $('<div class="placeholder-wrap"></div>');
				$this.wrap($wrap);
				var height = $this.parent().outerHeight();
				var $cover = $('<div class="placeholder-cover">' + text + '</div>');
				if($this.is('input')){
					$cover.css({
						'line-height':height + 'px'
					});
				}
				if($this.css('font-size')){
					$cover.css({
						'font-size':$this.css('font-size')
					});
				}
				// 处理事件逻辑
				$cover.click(function(){
					$this.focus();
				});
				$this.blur(function(){
					if($this.val() == ''){
						$cover.show();
					}
				}).focus(function(){
					$cover.hide();
				})
				$this.bind('input propertychange change', function() {  
					if($this.val() == ''){
						$cover.show();
					}else{
						$cover.hide();
					}
				});
				if($this.val() == ''){
					$cover.show();
				}
				// 加入
				$this.parent().append($cover);
			}
		})
	};
})(jQuery);
$(function(){
	$('input,textarea').placeholder();
})