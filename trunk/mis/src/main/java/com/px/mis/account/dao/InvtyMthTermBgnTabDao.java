package com.px.mis.account.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.account.entity.InvtyDetail;
import com.px.mis.account.entity.InvtyMthTermBgnTab;

public interface InvtyMthTermBgnTabDao {
	//��ѯ��������ڳ�
	List<InvtyMthTermBgnTab> selectByMthTerm(Map<String, Object> paramMap);
	//��ѯ��������ڳ�
	List<InvtyMthTermBgnTab> selectMthTermList(Map<String, Object> paramMap);
		
	int countSelectByMthTerm(Map<String,Object> paramMap);
	
	int insertMth(InvtyMthTermBgnTab mth);
	//�޸Ľ������/���
	int updateMth(InvtyMthTermBgnTab mth);

	List<InvtyMthTermBgnTab> selectIsFinalDeal(Map map);
	//�����޸�
	int updateMthList(List<InvtyMthTermBgnTab> termList);
	
	//��������
	int insertMthList(List<InvtyMthTermBgnTab> list);
	//����ɾ��
	int deleteMthList(List<InvtyMthTermBgnTab> mthList);
	//ɾ��
	int deleteByordrNum(int ordrNum);
	/**
	 * ��ѯ�ڳ�����--��ϸ��
	 */
	List<InvtyMthTermBgnTab> selectMthTermByInvty(Map map);
}
