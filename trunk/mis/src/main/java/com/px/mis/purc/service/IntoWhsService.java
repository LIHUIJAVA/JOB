package com.px.mis.purc.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.px.mis.purc.entity.IntoWhs;
import com.px.mis.purc.entity.IntoWhsSub;
import com.px.mis.purc.entity.SellOutWhsSub;

public interface IntoWhsService {

	//�����ɹ���ⵥ
	public String addIntoWhs(String userId, IntoWhs intoWhs,List<IntoWhsSub> intoWhsSubList,String loginTime) throws Exception;
	
	//�޸Ĳɹ���ⵥ
	public String editIntoWhs(IntoWhs intoWhs,List<IntoWhsSub> intoWhsSubList) throws Exception;
	
	//����ɾ���ɹ���ⵥ
	public String deleteIntoWhs(String intoWhsSnglId);
	
	//������ѯ�ɹ���ⵥ
	public String queryIntoWhs(String intoWhsSnglId);
	
	//��ҳ��ѯ�ɹ���ⵥ
	public String queryIntoWhsList(Map map);

	//ɾ���ɹ���ⵥ
	String deleteIntoWhsList(String intoWhsSnglId);
	
	//�ɹ���ⵥ���
	Map<String,Object> updateIntoWhsIsNtChk(IntoWhs intoWhs) throws Exception;
	
	//��ӡ�ɹ���ⵥ
	String printingIntoWhsList(Map map);
	
	//���뵽����
	public String uploadFileAddDb(MultipartFile  file,int i);
	
	//�����ϸ��
	String queryIntoWhsByInvtyEncd(Map map);
	
	//�ɹ����ջ������ѯ
	String selectIntoWhsAndPursOrdr(Map map);
	
	//�ɹ���Ʊ����ʱ��ѯ�ɹ���ⵥ�ӱ���Ϣ
	public String queryIntoWhsByIntoWhsIds(String intoWhsSnglId);
	
	//����ʱ��ѯ�ɹ���ⵥ������Ϣ
	public String queryIntoWhsLists(Map map);
	
	//�ɹ��˻�������ʱ��ѯ�ɹ���ⵥ�ӱ���Ϣ
    public String selectIntoWhsSubByUnReturnQty(String intoWhsSnglId);

	String queryIntoWhsListOrderBy(Map map);
	//�����ϸ��-����
	public String queryIntoWhsByInvtyEncdPrint(Map map);
	
}
