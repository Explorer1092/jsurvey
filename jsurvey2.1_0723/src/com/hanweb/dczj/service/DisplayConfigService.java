package com.hanweb.dczj.service;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;

import com.hanweb.common.BaseInfo;
import com.hanweb.common.util.FileUtil;
import com.hanweb.common.util.StringUtil;
import com.hanweb.dczj.dao.DisplayConfigDAO;
import com.hanweb.dczj.entity.DisplayConfig;

public class DisplayConfigService {

	@Autowired
	private DisplayConfigDAO displayConfigDAO;
	
	public DisplayConfig findEntityByDczjid(Integer dczjid) {
		return displayConfigDAO.findEntityByDczjid(dczjid);
	}

	public DisplayConfig addNewConfig(Integer dczjid) {
		DisplayConfig displayConfig = new DisplayConfig();
		String cssstyle = "";
		String stylepath = BaseInfo.getRealPath()+"/resources/dczj/template/jsurvey.css";
		File file = new File(stylepath);
		cssstyle = FileUtil.readFileToString(file);
		if(StringUtil.isNotEmpty(cssstyle)) {
			cssstyle = "<style type=\"text/css\">"+cssstyle+"</style >";
		}
		displayConfig.setDczjid(dczjid);
		displayConfig.setChooseframe_style(0);
		displayConfig.setContentsize("20");
		displayConfig.setCssstyle(cssstyle);
		displayConfig.setIsopencontent(0);
		displayConfig.setIsprogress(0);
		displayConfig.setIstitlenumber(0);
		displayConfig.setIsshowscore(0);
		int iid = displayConfigDAO.insert(displayConfig);
		displayConfig.setIid(iid);
		return displayConfig;
	}

	public boolean update(DisplayConfig configEn) {
		return displayConfigDAO.update(configEn);
	}

}
