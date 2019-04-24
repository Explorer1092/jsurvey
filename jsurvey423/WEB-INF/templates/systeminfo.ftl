<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>系统监控</title>
${jslib}
<style>
body{
	font-family:"微软雅黑";
	font-size:14px;
	padding: 10px;
}
th,td{
	border-color:#B8B8B8;
	border-style:solid;
	background-color:#00CCFF;
	color:#FFFFFF;
	padding:5px;
	text-align:center;
}
td{
	background-color:#FFFFFF;
	color:#53365C;
}
a{
	text-decoration:underline;
	cursor:pointer;
}
button{
	margin:2px 2px;
}
</style>
<script>
function pauseTask(taskId,taskGroup){
	$.ajax({
		type: "post",
		url: 'pause.do',
		data:{taskId:taskId,taskGroup:taskGroup},
		success : function(){
			window.location.reload();
		},
		error : function(){
			alert('操作出错');
			window.location.reload();
		}
	});
}
function resumeTask(taskId,taskGroup){
	$.ajax({
		type: "post",
		url: 'resume.do',
		data:{taskId:taskId,taskGroup:taskGroup},
		success : function(){
			window.location.reload();
		},
		error : function(){
			alert('操作出错');
			window.location.reload();
		}
	});
}
function removeTask(taskId,taskGroup){
	$.ajax({
		type: "post",
		url: 'remove.do',
		data:{taskId:taskId,taskGroup:taskGroup},
		success : function(){
			window.location.reload();
		},
		error : function(){
			alert('操作出错');
			window.location.reload();
		}
	});
}
</script>
</head>

<body>
<h3 style="margin-top: 0;">当前时间：${current_date}</h3>
<div class="thread">
<h3 style="border-bottom:1px dashed #53365C">
任务池状态
</h3>
<h4>
奔跑区
</h4>
<table width="100%" border="1" cellpadding="0" cellspacing="0">
	<tr>
    	<th>所属组</th>
        <th>唯一ID</th>
        <th>说明</th>
        <th>启动时间</th>
        <th>上一次启动时间</th>
        <th>下一次启动时间</th>
        <th>执行次数</th>
        <th>失败次数</th>
        <th>优先级</th>
        <th>状态</th>
        <th>操作</th>
    </tr>
    <#list runningTask as task>
    <tr>
    	<td>${task.group}</td>
        <td>${task.id}</td>
        <td>${task.desc}</td>
        <td>${task.start_time}</td>
        <td>${task.pre_fire_time}</td>
        <td>${task.next_fire_time}</td>
        <td>${task.fire_count}</td>
        <td>${task.fire_count_error}</td>
        <td>${task.priority}</td>
        <td>${task.state}</td>
        <td>
        	<button onClick="pauseTask('${task.id}','${task.group}');" ${task.pause?string('','disabled="disabled"')}>暂停</button>
            <button onClick="resumeTask('${task.id}','${task.group}');" ${task.pause?string('','disabled="disabled"')}>恢复</button>
            <button onClick="removeTask('${task.id}','${task.group}');" ${task.remove?string('','disabled="disabled"')}>删除</button>
        </td>
    </tr>
    </#list>
</table>
<h4>
休息区
</h4>
<table width="100%" border="1" cellpadding="0" cellspacing="0">
	<tr>
    	<th>所属组</th>
        <th>唯一ID</th>
        <th>说明</th>
        <th>启动时间</th>
        <th>上一次启动时间</th>
        <th>下一次启动时间</th>
        <th>执行次数</th>
        <th>失败次数</th>
        <th>优先级</th>
        <th>状态</th>
        <th>操作</th>
    </tr>
    <#list waitingTask as task>
    <tr>
    	<td>${task.group}</td>
        <td>${task.id}</td>
        <td>${task.desc}</td>
        <td>${task.start_time}</td>
        <td>${task.pre_fire_time}</td>
        <td>${task.next_fire_time}</td>
        <td>${task.fire_count}</td>
        <td>${task.fire_count_error}</td>
        <td>${task.priority}</td>
        <td>${task.state}</td>
        <td>
        	<button onClick="pauseTask('${task.id}','${task.group}');" ${task.pause?string('','disabled="disabled"')}>暂停</button>
            <button onClick="resumeTask('${task.id}','${task.group}');" ${task.pause?string('','disabled="disabled"')}>恢复</button>
            <button onClick="removeTask('${task.id}','${task.group}');" ${task.remove?string('','disabled="disabled"')}>删除</button>
        </td>
    </tr>
    </#list>
</table>
</div>
<div class="system-info">
    <h3 style="border-bottom:1px dashed #53365C">系统状态</h3>
    <h4>缓存文件夹</h4>
    <div>${tmp_dir}</div>
    <h4>JVM内存状态</h4>
    <div>JVM可使用最大系统内存:${jvm_max}</div>
    <div>JVM占用系统内存:${jvm_occupy}</div>
    <div>JVM剩余内存:${jvm_remain}</div>
    <h4>系统线程状态</h4>
    <#list threads as thread>
    <#if thread?exists>
    <div>${thread.name}[${thread.state}] : ${thread.class.name}</div>
    </#if>
    </#list>
</div>
</body>
</html>
