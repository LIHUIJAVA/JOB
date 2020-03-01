package com.px.mis.account.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.account.entity.PursStlSnglMasTab;

public interface PursStlSnglMasTabDao {
	
	//�����ɹ�����
	int insertPursStlSnglMasTab(PursStlSnglMasTab pursStlSnglMasTab);
	
	//�޸Ĳɹ�����
	int updatePursStlSnglMasTab(PursStlSnglMasTab pursStlSnglMasTab);
	
	//����ɾ��
	int deletePursStlSnglMasTabByStlSnglId(String stlSnglId);
	
	//����ɾ��
	int deletePursStlSnglMasTabList(List<String> stlSnglId);
	
	//���ղɹ����������ϸ��Ϣ
	PursStlSnglMasTab selectPursStlSnglMasTabByStlSnglId(String stlSnglId);
	
	//������ҳ��ѯ���вɹ�����
	List<PursStlSnglMasTab> printingPursStlSnglMasTabList(Map map);

	//������ѯ���вɹ�����
    List<PursStlSnglMasTab> selectPursStlSnglMasTabList(Map map);
    
    int selectPursStlSnglMasTabCount();
    
    //��ѯ���״̬
    int selectPursStlSnglMasTabIsNtChk(String stlSnglId);
}
