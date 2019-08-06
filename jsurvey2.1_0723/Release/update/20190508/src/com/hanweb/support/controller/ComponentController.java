package com.hanweb.support.controller;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import com.hanweb.common.BaseInfo;
import com.hanweb.common.Constants;
import com.hanweb.common.util.JsonUtil;
import com.hanweb.common.util.StringUtil;
import com.hanweb.common.util.mvc.ControllerUtil;
import com.hanweb.common.util.mvc.MultipartFileInfo;
import com.hanweb.common.view.progressbar.ProgressBarContent;
import com.hanweb.complat.listener.UserSessionInfo;
import com.hanweb.complat.constant.Settings;
import com.hanweb.complat.entity.TempFile;
import com.hanweb.complat.service.TempFileService;

/**
 * 组件
 * 
 * @author 李杰
 *
 */
@Controller
@RequestMapping("manager/component")
public class ComponentController {
	private Set<String> imagesType = new HashSet<String>();

	private Set<String> attachmentType = new HashSet<String>();

	@Autowired
	private TempFileService tempFileService;

	public ComponentController() {
		super();
		imagesType.add(".jpg");
		imagesType.add(".gif");
		imagesType.add(".jpeg");
		imagesType.add(".bmp");
		imagesType.add(".png");

		attachmentType.add(".zip");
		attachmentType.add(".rar");
		attachmentType.add(".doc");
		attachmentType.add(".docx");
		attachmentType.add(".xls");
		attachmentType.add(".xlsx");
	}

	@RequestMapping("multifileupload")
	public ModelAndView add(HttpSession session) {
		ModelAndView modelAndView = new ModelAndView("support/upload");
		modelAndView.addObject("uploadUrl", BaseInfo.getContextPath() + "/manager/component/fileupload.do");
		modelAndView.addObject("sessionid", session.getId());
		return modelAndView;
	}

	@ResponseBody
	@RequestMapping("fileupload")
	public String upload(MultipartFile file) throws Exception {
		String result = "{}";
		if (!MultipartFileInfo.isValid(file)) {
			return result;
		}
		Settings settings = Settings.getSettings();
		String fileName = file.getOriginalFilename();
		String newName = StringUtil.getUUIDString();
		String fileType = null;
		long fileSize = file.getSize();
		String ponit = ".";
		if (fileName.contains(ponit)) {
			int index = fileName.lastIndexOf(ponit) + 1;
			fileType = fileName.substring(index);
			newName = newName + ponit + fileType.toLowerCase();
		}
		boolean isSuccess = ControllerUtil.writeMultipartFileToFile(new File(settings.getFileTmp() + newName), file);

		if (isSuccess) {
			TempFile tempFile = new TempFile();
			tempFile.setTmpPath(settings.getFileTmp());
			tempFile.setOldName(fileName);
			tempFile.setNewName(newName);
			tempFile.setUploadDate(new Date());
			if (!StringUtil.isEmpty(fileType)) {
				tempFile.setFileType(fileType.toUpperCase());
			}
			CurrentUser currentUser = UserSessionInfo.getCurrentUser();
			if (currentUser != null) {
				tempFile.setLoginName(currentUser.getLoginName());
			}
			tempFile.setFileSize(fileSize);
			String uuid = tempFileService.add(tempFile);

			result = "{\"uuid\" : \"" + uuid + "\",\"newName\":\"" + newName + "\"}";
		} else {
			result = "{}";
		}
		return result;
	}

	@RequestMapping("image_upload")
	@ResponseBody
	public String imageUpload(MultipartFile upfile) {
		Map<String, String> message = new HashMap<String, String>(Constants.MAP_INIT_SIZE);
		if (upfile == null || upfile.isEmpty()) {
			message.put("state", "没有上传文件");
		} else {
			MultipartFileInfo multipartFileInfo = MultipartFileInfo.getInstance(upfile);
			String point = ".";
			if (imagesType.contains(point + multipartFileInfo.getFileType())) {
				// 新的文件名称
				String fileName = StringUtil.getUUIDString() + point + multipartFileInfo.getFileType();
				// 文件物理路径
				String targetFilePath = Settings.getSettings().getImageDir() + fileName;
				// 文件虚拟相对路径
				String absTargetFilePath = targetFilePath.replaceAll(BaseInfo.getRealPath(), "");
				File targetFile = new File(targetFilePath);
				ControllerUtil.writeMultipartFileToFile(targetFile, upfile);
				message.put("original", multipartFileInfo.getFileFullName());
				message.put("url", absTargetFilePath);
				message.put("title", fileName);
				message.put("state", "SUCCESS");
			} else {
				message.put("state", "请上传图片文件：jpg、gif、jpeg、bmp、png");
			}
		}
		return JsonUtil.objectToString(message);
	}

	@RequestMapping("attachment_upload_show")
	public ModelAndView showAttachmentUpload(HttpSession session) {
		ModelAndView modelAndView = new ModelAndView("support/attachment");
		modelAndView.addObject("sessionid", session.getId());
		return modelAndView;
	}

	@RequestMapping("attachment_upload")
	@ResponseBody
	public String attachmentUpload(MultipartFile upfile) {
		Map<String, String> message = new HashMap<String, String>(Constants.MAP_INIT_SIZE);
		if (upfile == null || upfile.isEmpty()) {
			message.put("state", "没有上传文件");
		} else {
			MultipartFileInfo multipartFileInfo = MultipartFileInfo.getInstance(upfile);
			String point = ".";
			if (attachmentType.contains(point + multipartFileInfo.getFileType())) {
				// 新的文件名称
				String fileName = StringUtil.getUUIDString() + point + multipartFileInfo.getFileType();
				// 文件物理路径
				String targetFilePath = Settings.getSettings().getAttachmentDir() + fileName;
				// 文件虚拟相对路径
				String absTargetFilePath = targetFilePath.replaceAll(BaseInfo.getRealPath(), "");
				File targetFile = new File(targetFilePath);
				ControllerUtil.writeMultipartFileToFile(targetFile, upfile);
				message.put("original", multipartFileInfo.getFileFullName());
				message.put("url", absTargetFilePath);
				message.put("title", fileName);
				message.put("state", "SUCCESS");
			} else {
				message.put("state", "请上传正确的附件：rar、zip");
			}
		}
		return JsonUtil.objectToString(message);
	}

	@ResponseBody
	@RequestMapping("progressbar")
	public String getProgressBarValue(@RequestParam(value = "var", required = false) String var) {
		return ProgressBarContent.getProgressBarValue(var) + "";
	}
}
