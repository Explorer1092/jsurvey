package com.hanweb.complat.controller.banlist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hanweb.common.annotation.Permission;
import com.hanweb.common.util.mvc.JsonResult;
import com.hanweb.common.util.mvc.ResultState;
import com.hanweb.complat.service.BanListService;

/**
 * 封停操作控制器
 * 
 * @author 李杰
 * 
 */
@Controller
@Permission(module = "banlist")
@RequestMapping("manager/banlist")
public class OprBanListController {
	@Autowired
	private BanListService banListService;

	/**
	 * 移除用户
	 * 
	 * @param ids
	 *            用户ID串 如:1,2,3,4
	 * @return
	 */
	@Permission(module = "remove")
	@RequestMapping("remove")
	@ResponseBody
	public JsonResult remove(@RequestParam("ids") String ids) {
		banListService.removeByIds(ids);
		JsonResult jsonResult = JsonResult.getInstance();
		jsonResult.set(ResultState.REMOVE_SUCCESS);
		return jsonResult;
	}
}
