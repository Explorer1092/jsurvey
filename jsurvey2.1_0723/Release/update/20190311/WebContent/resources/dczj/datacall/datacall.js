var pagecountnumber = $('#pageNumCount').val();
var pageno_lui = $('#pageno_lui').val();
var pageNum = $('#pageNum').val();
layui.use(['laypage', 'layer'], function(){
  var laypage = layui.laypage
  ,layer = layui.layer; 

laypage.render({
    elem: 'jactpage'
    ,count: pagecountnumber
    ,limit : pageNum
    ,curr : pageno_lui
    ,layout: ['count', 'prev', 'page', 'next', 'skip']
    ,jump: function(obj,first){
	curr = obj.curr;//当前页码
	if(!first){
		 gopagelui(curr,pageNum);
	}
}
  });
  
});

function gopagelui(curr,pageNum){
	var jactid = $("#jactid").val();
	
	var url = '../../front/datacallpage/gopageanswer.do?jactid='+jactid+"&pageno="+curr+"&pageNum="+pageNum;
	$.ajax({
		url: url,
		cache: false,
		success: function(result){
			$('#jact_table').empty();
			$('#jact_table').append(result.message);
		}
	});
}