package com.hanweb.dczj.dao;

import com.hanweb.common.basedao.BaseJdbcDAO;
import com.hanweb.common.basedao.Query;
import com.hanweb.dczj.constant.Tables;
import com.hanweb.dczj.entity.MessageContent;

public class MessageContentDAO extends BaseJdbcDAO<Integer, MessageContent>{

	public MessageContent getEntityBydczjid(Integer dczjid) {
		String sql = "SELECT iid,dczjid,content FROM "
				+Tables.MESSAGECONTENT+" WHERE dczjid=:dczjid";
		Query query = createQuery(sql);
		query.addParameter("dczjid", dczjid);
		return queryForEntity(query);
	}
	
	public Integer modify(MessageContent messageContent) {
		String sql = "UPDATE "+Tables.MESSAGECONTENT+" SET content=:content WHERE iid=:iid";
		Query query = createQuery(sql);
		query.addParameter("content", messageContent.getContent());
		query.addParameter("iid", messageContent.getIid());
		return execute(query);
	}
}
