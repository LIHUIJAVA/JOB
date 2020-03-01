package com.px.mis.account.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.account.entity.InvtyDetail;
import com.px.mis.account.entity.InvtyMthTermBgnTab;

public interface InvtySendMthTermBgnTabDao {
	// ��ѯ��������ڳ�
	List<InvtyMthTermBgnTab> selectSendMthByMthTerm(Map<String, Object> paramMap);

	List<InvtyMthTermBgnTab> selectSendMthByMth(Map map);

	// ��ѯ��������ڳ�
	List<InvtyMthTermBgnTab> selectMthTermList(Map<String, Object> paramMap);

	int countSelectByMthTerm(Map<String, Object> paramMap);

	int insertSendMth(InvtyMthTermBgnTab mth);

	// �޸Ľ������/���
	int updateSendMth(InvtyMthTermBgnTab mth);

	List<InvtyMthTermBgnTab> selectIsFinalDeal(Map map);

	// �����޸�
	int updateSendMthList(List<InvtyMthTermBgnTab> termList);

	// ��������
	int insertMthList(List<InvtyMthTermBgnTab> list);

	// ����ɾ��
	int deleteSendMthList(List<InvtyMthTermBgnTab> mthList);

	// ɾ��
	int deleteByordrNum(int ordrNum);

	List<InvtyMthTermBgnTab> selectMthTermByInvty(Map map);

	/**
	 * ��ѯ�ڳ�����--������Ʒ����
	 */
	List<InvtyMthTermBgnTab> sendProductMthPool(Map map);

//	//��������
//	List<InvtyMthTermBgnTab> sendProductMthPoolExport(Map map);

	int countSendProductMthPool(Map map);

}
