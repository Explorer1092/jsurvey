package com.hanweb.dczj.controller.title;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.hanweb.common.BaseInfo;
import com.hanweb.common.util.DateUtil;
import com.hanweb.common.util.FileUtil;
import com.hanweb.common.util.Properties;
import com.hanweb.common.util.StringUtil;
import com.hanweb.common.util.mvc.ControllerUtil;
import com.hanweb.common.util.mvc.FileResource;
import com.hanweb.complat.entity.TempFile;
import com.hanweb.complat.listener.UserSessionInfo;
import com.hanweb.complat.service.TempFileService;
import com.hanweb.dczj.entity.Dczj_Setting;
import com.hanweb.dczj.entity.TitleInfo;
import com.hanweb.dczj.service.SettingService;
import com.hanweb.dczj.service.TitleInfoService;
import com.hanweb.dczj.service.TotalRecoService;
import com.hanweb.support.controller.CurrentUser;

@Controller
@RequestMapping("manager/dczj")
public class ListTitleController {

	@Autowired
	private TitleInfoService titleInfoService;
	@Autowired
	private TempFileService tempFileService;
	@Autowired
	private TotalRecoService totalRecoService;
	@Autowired
	SettingService settingService;
	
	
	@RequestMapping("titlelist")
	public ModelAndView showTitleList(int webid) {
		ModelAndView modelAndView = new ModelAndView("dczj/title/title_list");
		modelAndView.addObject("webid", webid);
		return modelAndView;
	}
	
	
	@RequestMapping("titleshare")
	public ModelAndView showTitleShare(int dczjid) {
		ModelAndView modelAndView = new ModelAndView("dczj/title/titleshare");
		String format="png";
		Properties pro = new Properties(BaseInfo.getRealPath() + "/WEB-INF/config/setup.properties");
		String basePath = pro.getString("domain");
		String pccontent = basePath+"/jsurvey/questionnaire/jsurvey_"+dczjid+".html";
		String phonecontent = basePath+"/jsurvey/questionnaire/phonejsurvey_"+dczjid+".html";
		String phonedezjid = StringUtil.getString(dczjid);
		Dczj_Setting setting = settingService.getEntityBydczjid(phonedezjid);
		if(setting != null && setting.getIslimituser() == 1){
			phonecontent = 	basePath+"/front/jsurvey/userlogin_phone.do?dczjid="+phonedezjid;

		}
		Map<EncodeHintType, Object> map = new HashMap<EncodeHintType, Object>();
		map.put(EncodeHintType.CHARACTER_SET, "utf-8");
		map.put(EncodeHintType.ERROR_CORRECTION,ErrorCorrectionLevel.M);
		map.put(EncodeHintType.MARGIN, 0);
		try {
			
			String wjj = BaseInfo.getRealPath()+"/tempfile";
			File file1 = new File(wjj);
			/*if(FileUtil.isDirExsit(new File(wjj), false)) {
			  FileUtil.createDir(BaseInfo.getRealPath()+"/tempfile");	
			  
			}*/
			if(!file1.exists() && !file1.isDirectory()){       
			   file1.mkdir();   
			}
//			BitMatrix bm = new MultiFormatWriter().encode(phonecontent, BarcodeFormat.QR_CODE, width, height);
			BitMatrix bm = new QRCodeWriter().encode(phonecontent,BarcodeFormat.QR_CODE, 150, 150,map);
			String picname = StringUtil.getUUIDString()+".png";
			String path = BaseInfo.getRealPath()+"/tempfile/"+picname;
			File file = new File(path);
			MatrixToImageWriter.writeToFile(bm, format, file);
			
			TempFile tempFile = new TempFile();
			tempFile.setTmpPath(BaseInfo.getRealPath()+"/tempfile/");
			tempFile.setOldName("phone.png");
			tempFile.setNewName(picname);
			tempFile.setUploadDate(new Date());
			tempFile.setFileType(format.toUpperCase());
			CurrentUser currentUser = UserSessionInfo.getCurrentUser();
			if (currentUser != null) {
				tempFile.setLoginName(currentUser.getLoginName());
			}
			tempFileService.add(tempFile);
			modelAndView.addObject("picpath", basePath+"/tempfile/"+picname);
			modelAndView.addObject("picname",picname);
		} catch (WriterException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		modelAndView.addObject("pccontent", pccontent);
		modelAndView.addObject("dczjid", dczjid);
		return modelAndView;
	}
	
	@RequestMapping("downloadFile")
	public FileResource downloadFile(String picname) {
		File file = new File(BaseInfo.getRealPath()+"/tempfile/"+picname);
		return ControllerUtil.getFileResource(file, "phone.png");
	}
	
	@RequestMapping("copycode")
	public void copycode(String href) {
		Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();  
        Transferable tText = new StringSelection(href);  
        clip.setContents(tText, null); 
	}
	
	@RequestMapping("titlejson")
	public ModelAndView logjson(int webid,int dczjtype,int dczjstate,String titlename,int page, int limit) {
		ModelAndView modelAndView = new ModelAndView("/dczj/title/title_json");
		CurrentUser currentUser = UserSessionInfo.getCurrentUser();
		titlename = StringUtil.getSafeString(titlename);
		String json = "";
		String data = "";
		String state = "";
		int count = 0;
		List<TitleInfo> infoList = null;
		if(currentUser.isSysAdmin() && webid == 0) {
			infoList = titleInfoService.findInfoList(dczjtype,dczjstate,titlename,page,limit);
			count = titleInfoService.findNum(dczjtype,dczjstate,titlename);
		}else {
			infoList = titleInfoService.findInfoListByWebid(webid,dczjtype,dczjstate,titlename,page,limit);
			count = titleInfoService.findNumByWebid(dczjtype,dczjstate,titlename,webid);
		}
		
		if(infoList != null && infoList.size()>0 ) {
			int titlenum = 0;
			for(TitleInfo infoEn : infoList) {
				String name = "<span class='table-pic' onclick='titleinfoedit("+infoEn.getIid()+")'>"+infoEn.getTitlename()+"</span>";
				String creattime = DateUtil.dateToString(infoEn.getCreatetime(), "yyyy-MM-dd HH:mm:ss");
				String edit = "<img src=\\\"../../resources/dczj/images/u304.png\\\"  class='table-pic' onclick='titleedit("+infoEn.getIid()+");' />";
				String preview = "<img src=\\\"../../resources/dczj/images/u412.png\\\" class='table-pic' onclick='titlepreview("+infoEn.getIid()+");'/>";
				String share = "<img src=\\\"../../resources/dczj/images/u432.png\\\" class='table-pic' onclick='titleshare("+infoEn.getIid()+");' />";
				String publish = "";
				if(infoEn.getIspublish() == 0){
					publish = "<img src=\\\"../../resources/dczj/images/u522.png\\\" class='table-pic' onclick='titlepublish("+infoEn.getIid()+");'/>";
				}else if(infoEn.getIspublish() == 1) {
					publish = "<img src=\\\"../../resources/dczj/images/u520.png\\\" class='table-pic' />";
				}
				String operation = "<img src=\\\"../../resources/dczj/images/u452.png\\\" title='上移'  class='sort-pic' onclick='sort("+infoEn.getIid()+","+infoEn.getType()+",1);' />"
						         + "<img src=\\\"../../resources/dczj/images/u454.png\\\" title='下移'  class='sort-pic' onclick='sort("+infoEn.getIid()+","+infoEn.getType()+",2);'/>"
						         + "<img src=\\\"../../resources/dczj/images/u324.png\\\" title='删除'  class='delete-pic' onclick='remove("+infoEn.getIid()+");'/>"
						         + "<img src=\\\"../../resources/dczj/images/u321.png\\\" title='置顶'  class='top-pic' onclick='totop("+infoEn.getIid()+","+infoEn.getType()+");' />";
				
				if(infoEn.getState() == 0) {
					state = "<img src=\\\"../../resources/dczj/images/u218.png\\\" style='padding-right: 8px;'/>正在进行"; 
				}else if(infoEn.getState() == 1) {
					state = "<img src=\\\"../../resources/dczj/images/u222.png\\\" style='padding-right: 8px;'/>尚未开始"; 
				}else if(infoEn.getState() == 2) {
					state = "<img src=\\\"../../resources/dczj/images/u220.png\\\" style='padding-right: 8px;'/>已经结束"; 
				}
				
				titlenum = totalRecoService.findRecoIsHaveSubmitByDczjid(infoEn.getIid()+"");
				data +="{\"titlename\":\""+name+"\",\"state\":\""+state+"\",\"edit\":\""+edit+"\","
						+ "\"preview\":\""+preview+"\",\"share\":\""+share+"\",\"number\":\""+titlenum+"\","
						+ "\"publish\":\""+publish+"\",\"operation\":\""+operation+"\",\"creattime\":\""+creattime+"\"},";
			}
		}
		if(StringUtil.isNotEmpty(data)) {
			data = data.substring(0,data.length()-1);
		}
		json = "{\"code\":0,\"msg\":\"\",\"count\":"+count+",\"data\":["+data+"]}";
		modelAndView.addObject("json", json);
		return modelAndView;
	}
}
