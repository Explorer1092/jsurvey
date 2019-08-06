package com.hanweb.complat.service;

import java.io.File;
import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.hanweb.common.util.FileUtil;
import com.hanweb.common.util.StringUtil;
import com.hanweb.complat.constant.Settings;
import com.hanweb.complat.dao.TempFileDAO;
import com.hanweb.complat.entity.TempFile;

/**
 * 多文件上传管理器
 * 
 * @author 李杰
 * 
 */
public class TempFileService {
	private Log logger = LogFactory.getLog(getClass());

	@Autowired
	private TempFileDAO tempFileDAO;

	/**
	 * 新增临时文件
	 * 
	 * @param tempFile
	 *            临时文件
	 * @return
	 */
	public String add(TempFile tempFile) {
		return tempFileDAO.insert(tempFile);
	}

	/**
	 * 通用uuid找到临时文件
	 * 
	 * @param uuid
	 * @return
	 */
	public File findFileByUuid(String uuid) {
		File file = null;
		if (StringUtil.isEmpty(uuid)) {
			return file;
		}
		TempFile tempFile = tempFileDAO.queryForEntityById(uuid);
		if (tempFile != null) {
			Settings settings = Settings.getSettings();
			file = new File(settings.getFileTmp() + tempFile.getNewName());
			if (!file.exists()) {
				file = null;
			}
		}
		return file;
	}

	/**
	 * 删除前一天的所有临时文件
	 */
	public void removeYesterday() {
		List<TempFile> fileTmps = tempFileDAO.findLessthanDay(new Date());
		if (fileTmps != null && fileTmps.size() > 0) {
			String filePath = null;
			for (TempFile tempFile : fileTmps) {
				filePath = tempFile.getTmpPath() + tempFile.getNewName();
				try {
					FileUtil.deleteFile(new File(filePath));
					tempFileDAO.deleteById(tempFile.getUuid());
				} catch (Exception e) {
					logger.error("删除暂存附件错误", e);
				}
			}
		}
	}
}
