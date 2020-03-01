package com.px.mis.system.service;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.system.entity.MisUser;

public interface MisUserService {

	public String login(String accNum,String password,String accSet);
	
	public ObjectNode insert(MisUser misUser);
	
	public ObjectNode delete(String accNum);
	
	public String edit(String jsonBody);
	
	public String queryList(Map map);
	
	public String userTree();
	
	public String permAss(String jsonBody);
	
	public ObjectNode delMisUser(String accNum);
	
	public String updateBatch(String jsonBody);

	public String uploadFileAddDb(MultipartFile file);
	
	public String query(String accNum);

	public void selectUserWhs(String jsonBody,Map map);

	public String selectUserLogicWhs(String jsonBody) throws Exception;

	public String userWhsAdd(String jsonBody) throws Exception;

	public String userWhsUpdate(String jsonBody) throws Exception ;

	String queryListExport(Map map);
	
	
}
