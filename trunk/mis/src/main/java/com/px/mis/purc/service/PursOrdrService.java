package com.px.mis.purc.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.px.mis.purc.entity.PursOrdr;
import com.px.mis.purc.entity.PursOrdrSub;

public interface PursOrdrService {

	String addPursOrdr(String userId,PursOrdr pursOrdr,List<PursOrdrSub> pursOrdrSubList,String loginTime) throws Exception;
	
	String editPursOrdr(PursOrdr pursOrdr,List<PursOrdrSub> pursOrdrSubList) throws Exception;
	
	String deletePursOrdr(String pursOrdrId);
	
	String queryPursOrdr(String pursOrdrId);
	
	String queryPursOrdrList(Map map);

	String deletePursOrdrList(String pursOrdrId);
	
	String updatePursOrdrIsNtChkList(List<PursOrdr> pursOrdr);
	
	String printingPursOrdrList(Map map);
	
	public String uploadFileAddDb(MultipartFile  file,int i,HashMap map);
	
	//�ɹ���ϸ
	String queryPursOrdrByInvtyEncd(Map map);
	
	//����ʱ��ѯ����δ���븶����Ĳɹ������ӱ���Ϣ
	public String selectUnApplPayQtyByPursOrdrId(String pursOrdrId);
	
	//����ʱ��ѯ����δ���������Ĳɹ������ӱ���Ϣ
    public String selectUnToGdsQtyByPursOrdrId(String pursOrdrId);
    
     //���������������뵥����ʱ������ѯ�ɹ�����������Ϣ
    String queryPursOrdrLists(Map map);

	String queryPursOrdrListOrderBy(Map map);

	String queryPursOrdrByInvtyEncdPrint(Map map);
}
