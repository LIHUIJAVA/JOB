package com.px.mis.account.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.px.mis.account.entity.FormBookEntry;
import com.px.mis.account.entity.FormBookEntrySub;

public interface FormBookEntrySubDao {
	
	//��������
	int insertSubList(List<FormBookEntrySub> list);

	//��ѯ��ϸ��������
	int selectDetailCount(Map map);
	
	//��ѯ��ϸ��
	List<FormBookEntrySub> selectDetailList(Map map);
	//���������
	BigDecimal countBxQty(Map<String, String> dataMap);
	//�����޸Ľ���ɱ�
	int updateNoTaxUprc(List<FormBookEntrySub> dataList);

	int insertFormSub(FormBookEntrySub formSub);

	int updateSubj(List<FormBookEntrySub> list);

	List<FormBookEntrySub> selectSubByFormNum(String formNum);
	//����ɾ��
	int deleteList(List<FormBookEntrySub> list);
	
	
	
}
