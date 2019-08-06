package com.hanweb.dczj.controller.log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hanweb.common.basedao.LikeType;
import com.hanweb.common.basedao.Query;
import com.hanweb.common.util.DateUtil;
import com.hanweb.common.util.StringUtil;
import com.hanweb.log.dao.LogDAO;
import com.hanweb.log.entity.LogEntity;
import com.hanweb.log.service.LogService;
import com.hanweb.log.util.LogUtil;

@Controller
@RequestMapping("manager/log")
public class OprLogController {

	@Autowired
	private LogService logService;
	@Autowired
	private LogDAO logDAO;
	
	/**
	 * 删除操作记录列表
	 * @param ids
	 * @return
	 */
//	@RequestMapping("remove")
//	@ResponseBody
//	public JsonResult remove(String ids) {
//		JsonResult jsonResult = JsonResult.getInstance();
//		
//		boolean bl = logService.delete(StringUtil.toIntegerList(ids));
//		return bl ? jsonResult.set(ResultState.REMOVE_SUCCESS) : jsonResult
//				.set(ResultState.REMOVE_FAIL);
//	}
	
	/**
	 * 打开操作日志页面
	 */
	@RequestMapping("logtable")
	public ModelAndView logtable() {
		ModelAndView modelAndView = new ModelAndView("/dczj/log/log_list");
		String date = DateUtil.currDay();
		modelAndView.addObject("date", date);
		return modelAndView;
	}
	
	/**
	 * layui 数据接口
	 * @param loguser 操作人
	 * @param page 页码
	 * @param limit 每页条数
	 * @param searchstartdate 开始时间
	 * @param searchdate 结束时间
	 * @return
	 */
	@ResponseBody
	@RequestMapping("logjson")
	public String logjson(String loguser,int page, int limit,String searchstartdate, String searchdate) {
//		ModelAndView modelAndView = new ModelAndView("/dczj/log/log_json");
		String json = "";
		String data = "";
		
		//跨站脚本过滤
		loguser = StringUtil.trim(loguser);
		searchstartdate = StringUtil.trim(searchstartdate);
		searchdate = StringUtil.trim(searchdate); 
		
		if(StringUtil.isNotEmpty(searchstartdate) && StringUtil.isNotEmpty(searchdate)){
			Date startDate = DateUtil.stringtoDate(searchstartdate, "yyyy-MM-dd");
			Date endDate = DateUtil.stringtoDate(searchdate, "yyyy-MM-dd");
			ArrayList<LogEntity> newListData = new ArrayList<LogEntity>();
			long dateNumber = DateUtil.dayDiff(startDate,endDate);
			int allcount = 0;
			
			int a = 1;
			if(dateNumber > 0){
				a = (int) (dateNumber + 1);
			}else if(dateNumber < 0){
				a = (int) (1 - dateNumber);
			}
			String sql = "";
			for (int m = 0; m < a ;m++){
				Date nextday = DateUtil.nextDay(endDate, -m);
				String day = DateUtil.dateToString(nextday, "yyyy-MM-dd");
				day = day.replace("-", "");
				String tablename = LogUtil.getTableName(day);
				if(!logDAO.existsTable(tablename)){     //判断日志表是否存在
					continue;
				}
				String strsql = "select count(1) from " + tablename+" where 1 = 1";
				if(StringUtil.isNotEmpty(loguser)){
					strsql += " and loguser LIKE:loguser";
				}
				Query query = logDAO.createQuery(strsql);
				if(StringUtil.isNotEmpty(loguser)){
					query.addParameter("loguser", loguser,LikeType.LR);
				}
				int count = logService.findInt(query); //查询当前表日志的数量
				
				allcount += count;
				
				if(StringUtil.isNotEmpty(sql) && sql.length()>0 ){
					sql += " UNION ALL (select oprtime,description,loguser,ipaddr from " + tablename+" where 1 = 1";
					if(StringUtil.isNotEmpty(loguser)){
						sql += " and loguser LIKE:loguser";
					}
					sql += ")";
				}else{
					sql += "(select oprtime,description,loguser,ipaddr from " + tablename+" where 1 = 1";
					if(StringUtil.isNotEmpty(loguser)){
						sql += " and loguser LIKE:loguser";
					}
					sql += ")";
				}
	
			}
			if(StringUtil.isNotEmpty(sql)){
				sql += " order by oprtime desc";
				Query query1 = logDAO.createQuery(sql);
				query1.setPageNo(page);
				query1.setPageSize(limit);
				if(StringUtil.isNotEmpty(loguser)){
					query1.addParameter("loguser", loguser,LikeType.LR);
				}
				List<LogEntity> listData = logService.findLogs(query1);
				if(listData != null && listData.size()>0){
					for (int i = 0; i <= listData.size() - 1; i++) {
						newListData.add(listData.get(i));
					}
				}
				if(newListData != null && newListData.size()>0){
					for(int j =0; j < newListData.size(); j++){
						String oprtime = DateUtil.dateToString(newListData.get(j).getOprTime(), "yyyy-MM-dd HH:mm:ss");
						data += "{\"loguser\":\""+newListData.get(j).getLogUser()+"\",\"ipaddr\":\""+newListData.get(j).getIpAddr()+"\",\"oprtime\":\""+oprtime+"\",\"description\":\""+newListData.get(j).getDescription()+"\"},";
					}
				}
			}
			if(StringUtil.isNotEmpty(data)){
				data = data.substring(0,data.length()-1);
			}else{
				data = "{\"loguser\":\"\",\"ipaddr\":\"\",\"oprtime\":\"\",\"description\":\"\"}";
			}
			json = "{\"code\":0,\"msg\":\"\",\"count\":"+allcount+",\"data\":["+data+"]}";
		}else{
			String date = DateUtil.currDay();
			date = date.replace("-", "");
			String tablename = LogUtil.getTableName(date);
			int count = 0;
			
			if( logDAO.existsTable(tablename)){
				String strsql = "select count(1) from " + tablename+" where 1 = 1";
				if(StringUtil.isNotEmpty(loguser)){
					strsql += " and loguser LIKE:loguser";
				}
				Query query = logDAO.createQuery(strsql);
				if(StringUtil.isNotEmpty(loguser)){
					query.addParameter("loguser", loguser, LikeType.LR);
				}
				count = logService.findInt(query);
				
				String sql = "select oprtime,description,loguser,ipaddr from " + tablename+" where 1 = 1";
				if(StringUtil.isNotEmpty(loguser)){
					sql += " and loguser LIKE:loguser";
				}
				sql += " order by oprtime desc";
				Query query1 = logDAO.createQuery(sql);
				if(StringUtil.isNotEmpty(loguser)){
					query1.addParameter("loguser", loguser,LikeType.LR);
				}
				int a ,b ;
				a = (page-1) * limit;
				b = a + limit;
				String[][] logListData = logDAO.queryForArrays(query1, a, b);
				
				if(logListData != null && logListData.length>0){
					String oprtime = "";
					for (int i = 0; i <= logListData.length - 1; i++) {
						oprtime = logListData[i][0].substring(0,19);
						data += "{\"loguser\":\""+logListData[i][2]+"\",\"ipaddr\":\""+logListData[i][3]+"\",\"oprtime\":\""+oprtime+"\",\"description\":\""+logListData[i][1]+"\"},";
					}
				}
			}
			
			if(StringUtil.isNotEmpty(data)){
				data = data.substring(0,data.length()-1);
			}else{
				data = "{\"loguser\":\"\",\"ipaddr\":\"\",\"oprtime\":\"\",\"description\":\"\"}";
			}
			json = "{\"code\":0,\"msg\":\"\",\"count\":"+count+",\"data\":["+data+"]}";
		}
//		modelAndView.addObject("json", json);
		return json;
	}
}
