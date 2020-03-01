package com.px.mis.purc.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.px.mis.purc.entity.EntrsAgnDelv;
import com.px.mis.purc.entity.EntrsAgnDelvSub;

public interface EntrsAgnDelvService {

	public String addEntrsAgnDelv(String userId,EntrsAgnDelv entrsAgnDelv,List<EntrsAgnDelvSub> entrsAgnDelvSubList,String loginTime);
	
	public String editEntrsAgnDelv(EntrsAgnDelv entrsAgnDelv,List<EntrsAgnDelvSub> entrsAgnDelvSubList);
	
	public String deleteEntrsAgnDelv(String delvSnglId);
	
	public String queryEntrsAgnDelv(String delvSnglId);
	
	public String queryEntrsAgnDelvList(Map map);
	
	String deleteEntrsAgnDelvList(String delvSnglId);
	
	String printingEntrsAgnDelvList(Map map);
	
	Map<String,Object> updateEntrsAgnDelvIsNtChkList (String userId,EntrsAgnDelv entrsAgnDelv,String loginTime) throws Exception;
	
	//����ί�д���������
	public String uploadFileAddDb(MultipartFile  file);
	
	//����ʱ��ѯί�д���������Ϣ
	String queryEntrsAgnDelvLists(Map map);
	
	//ί�д����˻�������ʱ����ί�д����������ӱ���Ϣ
    public String selectEntDeSubUnBllgRtnGoodsQty(String delvSnglId);
    
    public String uploadFileAddDb(MultipartFile file,int i);

	String queryEntrsAgnDelvListOrderBy(Map map);

}
