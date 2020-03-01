package com.px.mis.account.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.px.mis.account.entity.FormBookEntry;
import com.px.mis.account.entity.FormBookEntrySub;
import com.px.mis.purc.entity.IntoWhs;

public interface FormBookEntryDao {
	//���ݵ��ݺŲ��ҵ���
	FormBookEntry selectByFormNum(String formNum);
	//��������
	int insertFormList(List<FormBookEntry> list);
	//��ѯ����
	List<FormBookEntry> selectMap(Map<String,Object> map);
	//�����޸ĵ���
	int updateIsNtBook(List<FormBookEntry> list);
	//�����޸ĵ���
	int delectFormBookList(List<FormBookEntry> formList);
	
	int insertForm(FormBookEntry form);
	//�����޸�������ƾ֤״̬
	int updateFormVouch(List<FormBookEntry> list);
	//����ƾ֤-��ϵ���
	void updateGruopFormVouch(List<FormBookEntry> formBookList);
	
	//��ѯ��ˮ��
	List<FormBookEntry> selectStreamMap(Map map);
	
	int countSelectStreamMap(Map map);
	
	List<FormBookEntry> selectMapAndPage(Map map);
	List<FormBookEntry> selectFormNoVoucherGeneratedList(Map map);
	int countFormNoVoucherGeneratedList(Map map);
	
	//�ϲ��Ƶ�
	List<FormBookEntry> selectMergeMapAndPage(Map map);
	
	int countSelectMapAndPage(Map map);
	//sum�ɹ�/��������������ͽ��
	FormBookEntrySub sumFormBookQtyAndAmt(Map map);
	
	//����ƾ֤-ѡ��-δ����ƾ֤�б�-���۵�
	List<FormBookEntry> selectSellSnglNoVoucherGeneratedList(Map map);
	int countSellSnglNoVoucherGeneratedList(Map map);
	void updateSellSnglFormVouch(List<FormBookEntry> sellSnglList);
	
	//����ƾ֤-ѡ��-δ����ƾ֤�б�-�ɹ���Ʊ
	List<FormBookEntry> selectPursInvMasTabNoVoucherGeneratedList(Map map);
	int countPursInvMasTabNoVoucherGeneratedList(Map map);
	void updatePursInvMasFormVouch(List<FormBookEntry> pursInvMasList);
	
	//����ƾ֤-ѡ��-δ����ƾ֤�б�-���۷�Ʊ(ר��/��ͨ)
	List<FormBookEntry> selectSellInvMasTabNoVoucherGeneratedList(Map map);
	int countSellInvMasTabNoVoucherGeneratedList(Map map);
	void updateSellInvMasFormVouch(List<FormBookEntry> sellInvMasList);
	
	//����ƾ֤-ѡ��-δ����ƾ֤�б�-����������
	List<FormBookEntry> selectOutIntoWhsAdjSnglNoVoucherGeneratedList(Map map);
	int countOutIntoWhsAdjSnglNoVoucherGeneratedList(Map map);
	void updateIntoWhsAdjFormVouch(List<FormBookEntry> intoWhsAdjSnglList);
	
	//����ƾ֤-ѡ��-δ����ƾ֤�б�-�������ֻس嵥
	List<FormBookEntry> selectFormBackFlushNoVoucherGeneratedList(Map map);
	int countFormBackFlushNoVoucherGeneratedList(Map map);
	void updateFormBackFlushFormVouch(List<FormBookEntry> formBackFlushList);
	
	//����ƾ֤-ѡ��-δ����ƾ֤�б�-�˻���
	List<FormBookEntry> selectRtnGoodsNoVoucherGeneratedList(Map map);
	int countRtnGoodsNoVoucherGeneratedList(Map map);
	void updateRtnGoodsFormVouch(List<FormBookEntry> formBackFlushList);
	
	//����ƾ֤-ѡ��-δ����ƾ֤�б�-ί�д���������/�˻���
	List<FormBookEntry> selectEntrsAgnDelvNoVoucherGeneratedList(Map map);
	int countEntrsAgnDelvNoVoucherGeneratedList(Map map);
	void updateEntrsAgnDelvFormVouch(List<FormBookEntry> formBackFlushList);

	
	//��ˮ�ʲ�ѯ
	List<FormBookEntry> selectStreamALLList(Map map);
	int selectStreamALLCount(Map map);
	
	//δ���˵���-�ɹ�-����-���������
	List<FormBookEntry> selectToBookForm(Map map);
	
	void updateSellOutWhsBookEntryList(List<FormBookEntry> li);
	void updateIntoWhsBookEntryList(List<FormBookEntry> li);
	void updateOutInWhsBookEntry(List<FormBookEntry> li);
	//�洢����-���㵥��
	
	BigDecimal updateInUprc(Map<String, Object> parmMap);
	
	BigDecimal updateOutUprc(Map<String, Object> parmMap);
	
	List<FormBookEntrySub> selectA();
	List<FormBookEntrySub> selectB();
	

	
	
	
	
	

	
	
		

}
