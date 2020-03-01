package com.px.mis.account.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.entity.PursInvMasTab;
import com.px.mis.account.entity.PursInvSubTab;
import com.px.mis.purc.entity.IntoWhs;

public interface PursInvMasTabService {

	//�����ɹ�ר�÷�Ʊ
	ObjectNode insertPursInvMasTab(PursInvMasTab pursInvMasTab);
	
	//�޸Ĳɹ�ר�÷�Ʊ
	ObjectNode updatePursInvMasTabById(PursInvMasTab pursInvMasTab,List<PursInvSubTab> pursInvSubTabList);
	
	//ɾ���ɹ�ר�÷�Ʊ
	ObjectNode deletePursInvMasTabByPursInvNum(String pursInvNum);
	
	//��ѯ�ɹ�ר�÷�Ʊ��ϸ��Ϣ
	PursInvMasTab selectPursInvMasTabByPursInvNum(String pursInvNum);
	
	//��ѯ���вɹ�ר�÷�Ʊ
    String selectPursInvMasTabList(Map map);
    
    //����ɾ���ɹ�ר�÷�Ʊ
    String deletePursInvMasTabList(String pursInvNum);
    
   //��˲ɹ�ר�÷�Ʊ
   String  updatePursInvMasTabIsNtChkList(List<PursInvMasTab> pursInvMasTabList);
   
   //�ɹ���Ʊ����ʱ�����ղɹ���ⵥ��������Ųɹ���ⵥ
   String selectPursInvMasTabBingList(List<IntoWhs> intoWhsList);
   
   //�������۳��ⵥ
   public String uploadFileAddDb(MultipartFile  file);
 	
   //����ʱ��ʹ�õĲ�ѯ�ӿ�
   String upLoadPursInvMasTabList(Map map);
}
