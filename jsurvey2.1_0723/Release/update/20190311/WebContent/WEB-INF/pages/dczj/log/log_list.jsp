<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="h" uri="/hanweb-tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>选项卡</title>
<h:head pagetype="page" grid="true" calendar="true"></h:head>
<h:import type="js" path="/ui/script/ui.js"></h:import>
<link type="text/css" rel="stylesheet" href="${contextPath}/resources/dczj/layui/css/layui.css">
<script src="${contextPath}/resources/dczj/layui/layui.js"></script>
<script type="text/javascript">
$(function(){
    // 设置结束时间的最大可取值为today
    $('#searchdate').unbind("click");
    $('#searchdate').bind("click",function(){
        WdatePicker({
            readOnly:true,
            dateFmt:'yyyy-MM-dd ',
            minDate:'#F{$dp.$D(\'searchstartdate\')}',
            maxDate:'#F{$dp.$D(\'date\')}'
        });
    });
});

// 开始时间值改变时,改变结束时间值得范围
function setEndTimeDurationWhileStartTimeChange(){
    var start = $('#searchstartdate').val(); // 格式：2017-05-01 
    
    // 如果选中了开始时间
    if(start != "" && start != null){
        // endMaxDate = 开始时间+29天
        start = start.replace(/-/g,"/");
        var endMaxDate = new Date(start);
        endMaxDate.setDate(endMaxDate.getDate()+6); 
        
        // today = 今天
        var today = new Date();
        today.setHours(0);    
        today.setMinutes(0);    
        today.setSeconds(0);    
        today.setMilliseconds(0);
        
        // 如果endMaxDate>today
        if(endMaxDate.getTime()>today.getTime()){
            // 设置结束时间的最大可取值为today
            $('#searchdate').unbind("click");
            $('#searchdate').bind("click",function(){
                WdatePicker({
                    readOnly:true,
                    dateFmt:'yyyy-MM-dd ',
                    minDate:'#F{$dp.$D(\'searchstartdate\')}',
                    maxDate:'#F{$dp.$D(\'date\')}'
                });
            });
        }else{
            // 设置结束时间的最大可取值为endMaxDate
            $('#searchdate').unbind("click");
            $('#searchdate').bind("click",function(){
                WdatePicker({
                    readOnly:true,
                    dateFmt:'yyyy-MM-dd ',
                    minDate:'#F{$dp.$D(\'searchstartdate\')}',
                    maxDate:'#F{$dp.$D(\'searchstartdate\',{d:6})}'
                });
            });
        }
    }
    // 如果清空了开始时间,重设结束时间的范围,到今天为止
    else{
        // 设置结束时间的最大可取值为today
        $('#searchdate').unbind("click");
        $('#searchdate').bind("click",function(){
            WdatePicker({
                readOnly:true,
                dateFmt:'yyyy-MM-dd ',
                minDate:'#F{$dp.$D(\'searchstartdate\')}',
                maxDate:'#F{$dp.$D(\'date\')}'
            });
        });
    }
}
</script>
</head>
<body>
	<div id="page-title">
		系统管理 / <span id="page-location">操作日志</span>
	</div>

	<div class="demoTable" style="padding-top: 20px; padding-left: 30px;">
	<input type="hidden" name="date" id="date" value="${date }"/>
		操作人：<input class="input-text" name="id" id="demoReload" autocomplete="off" placeholder="请输入操作人" style="width:150px;margin:0 30px 0 10px;" maxlength="50">
		开始日期：<input type="text" id="searchstartdate" name="searchstartdate" placeholder="请选择开始时间" value="${searchstartdate }" class="jcalendar input-text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'searchdate\',{d:-6});}',maxDate:'#F{$dp.$D(\'date\')||$dp.$D(\'searchdate\',{d:6});}'});" style="width:150px;margin:0 30px 0 10px;" readonly="readonly" onchange="setEndTimeDurationWhileStartTimeChange();"/>
		截止日期：<input type="text" id="searchdate" name="searchdate" placeholder="请选择截止时间" value="${searchdate }" class="jcalendar input-text" style="width:150px;margin:0 30px 0 10px;" readonly="readonly"/>
		<button class="btn btn-primary" data-type="reload" type="button" onclick="reloadtable();">搜索</button>
	</div>
	<div style="padding-top: 20px; padding-left: 30px;">
		<table class="layui-hide" id="LAY_table_user" lay-filter="testReload"></table>
	</div>


	<!-- 注意：如果你直接复制所有代码到本地，上述js路径需要改成你本地的 -->

	<script>
	var table;
		layui.use('table', function() {
			table = layui.table;
			reloadtable();
/* 
			var $ = layui.$, active = {
				reload : function() {
					var demoReload = $('#demoReload');
                    var searchstartdate = $('#searchstartdate');
                    var searchdate = $('#searchdate');
					//执行重载
					table.reload('testReload', {
						page : {
							curr : 1
						//重新从第 1 页开始
						},
						where : {
							loguser : demoReload.val(),
							searchstartdate : searchstartdate.val(),
							searchdate : searchdate.val()
						}
					});
				}
			}; */
// 			table.on('tool(testReload)', function(obj){
// 				 if(obj.event === 'setSign'){
// 					 location.href="http://192.168.89.95:8085/dczj1.5/front/dczj/list.do?formids=1,2,3,4&&topicids=1,2,3,4&&voteids=1,2,3,4";
// 				 }
// 			});
			
			
		/* 	$('.demoTable .btn-primary').on('click', function() {
				var searchstartdate = $('#searchstartdate');
                var searchdate = $('#searchdate');
                if(searchstartdate.val() != "" && searchdate.val() == ""){
                	alert("请选择截止时间！");
                }else if(searchstartdate.val() == "" && searchdate.val() != ""){
                	alert("请选择开始时间！");
                }else if(searchstartdate.val() != "" && searchdate.val() != "" && searchstartdate.val() > searchdate.val()){
                	alert("开始时间不能大于截止时间！");
                }else{
                	var type = $(this).data('type');
    				active[type] ? active[type].call(this) : '';
                }
			}); */
		});
		layui.use(['form', 'element', 'layer'], function(){
			  var form = layui.form
			  ,layer = layui.layer
			  ,element = layui.element;
	  
			});
		function reloadtable() {
			var demoReload = $('#demoReload');
            var searchstartdate = $('#searchstartdate');
            var searchdate = $('#searchdate');	
            if(searchstartdate.val() != "" && searchdate.val() == ""){
            	alert("请选择截止时间！");
            }else if(searchstartdate.val() == "" && searchdate.val() != ""){
            	alert("请选择开始时间！");
            }else if(searchstartdate.val() != "" && searchdate.val() != "" && searchstartdate.val() > searchdate.val()){
            	alert("开始时间不能大于截止时间！");
            }
			table.render({
				elem : '#LAY_table_user',
				url : 'logjson.do',
				cols : [ [ {
					field : 'loguser',
					title : '操作人',
	//				width : '10%'
				}, {
					field : 'ipaddr',
					title : '操作人IP',
//					width : '10%'
				}, {
					field : 'oprtime',
					title : '操作人时间',
//					width : '15%'
				}, {
					field : 'description',
					title : '操作名称',
//					width : '65%',
					event: 'setSign',
					style:'cursor: pointer;'
				}] ],
	//			id : 'testReload',
				page : true,
				limits : [10,15,30],
				where : {
					loguser : demoReload.val(),
					searchstartdate : searchstartdate.val(),
					searchdate : searchdate.val()
				}
			});
		}
	</script>

</body>
</html>