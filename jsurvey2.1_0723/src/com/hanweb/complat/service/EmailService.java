package com.hanweb.complat.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hanweb.common.BaseInfo;
import com.hanweb.common.util.FileUtil;
import com.hanweb.common.util.StringUtil;
import com.hanweb.common.util.log.LogWriter;
import com.hanweb.common.util.security.QrcodeMaker;
import com.hanweb.common.util.security.SecurityUtil;
import com.hanweb.complat.app.Attach;
import com.hanweb.complat.app.SystemMailSender;
import com.hanweb.complat.constant.Settings;
import com.hanweb.complat.constant.StaticValues;
import com.hanweb.complat.dao.UserDAO;
import com.hanweb.complat.entity.User;

/**
 * 另起一个service为了userservice干净些
 * 
 * @author 李杰
 *
 */
@Service
public class EmailService {

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private UserService userService;

	/**
	 * 获得所有有email的用户id
	 * 
	 * @param ids
	 * @return
	 */
	public List<Integer> findUserIdHasEmail(String ids) {
		List<Integer> idList = StringUtil.toIntegerList(ids);
		return userDAO.findUserIdHasEmail(idList);
	}

	/**
	 * 重置所有用户的
	 * 
	 * @param iid
	 * @return
	 */
	public boolean modifyDynamicCodeAndSendEmail(Integer iid) {
		boolean success = false;
		String filePath = null;
		try {
			User user = userService.findByIid(iid);
			String code = SecurityUtil.createAuthKey();
			filePath = Settings.getSettings().getImageDir() + StringUtil.getUUIDString() + ".jpg";
			getQrcode(code, filePath);
			List<Attach> fileList = new ArrayList<Attach>();
			Attach attach = new Attach();
			Attach attach1 = new Attach();
			attach.setFile(new File(StaticValues.QRCODE_REAL_PATH));
			attach.setName("step1.png");
			attach1.setFile(new File(filePath));
			attach1.setName("step2.jpg");
			fileList.add(attach);
			fileList.add(attach1);
			success = SystemMailSender.send("申领/重置动态验证码密钥",
					"<h3>亲爱的用户：</h3><div>您已申请动态码密钥申领/重置服务，新的动态码密钥如下：</div><div><a href=\"#\" style=\"color:red;font-weight: bold;\">"
							+ code + "</a></div><div></div>二维码见附件，扫step1可以进入公众号，进入小程序中开启扫一扫功能扫step2，"
							+ "会自动开启小程序输入密钥界面并自动输入密钥并自动生成动态码<div></div><h5>【注意】本邮件为系统自动发送，请不要回复。</h5>",
					fileList, user.getEmail());
			if (success) {
				userDAO.updateDynamicCode(iid, code);
			}
		} catch (Exception e) {
			LogWriter.error("rest DynamicCode email send error", e);
		} finally {
			FileUtil.deleteFile(filePath);
		}
		return success;
	}

	/**
	 * 获取自己的二维码 格式为：otpauth://totp/JIS?secret=NLRODV2DJSCHOATL
	 * @param code	
	 * @param path
	 */
	public void getQrcode(String code, String path) {
		QrcodeMaker maker = new QrcodeMaker(200);
		maker.drawLogoQRCode("otpauth://totp/" + BaseInfo.getAppName() + "?secret=" + code, new File(path));
	}
}
