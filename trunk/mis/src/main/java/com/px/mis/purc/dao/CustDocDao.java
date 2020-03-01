package com.px.mis.purc.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.purc.entity.CustCls;
import com.px.mis.purc.entity.CustDoc;

public interface CustDocDao {

	// �����ͻ�����
	int insertCustDoc(CustDoc custDoc);

	// ����ʱ�����ͻ�����
	int insertCustDocUpload(CustDoc custDoc);

	// �޸Ŀ�Ŀ����
	int updateCustDocByCustId(CustDoc custDoc);

	// ɾ�������ͻ�����
	int deleteCustDocByCustId(String custId);

	// ��ѯ�ͻ���������
	CustCls selectCustDocByCustId(String custId);

	// ��ҳ��ѯ�ͻ�����
	List<CustCls> selectCustDocList(Map map);

	// ��ѯ�ͻ�����������
	int selectCustDocCount(Map map);

	// ������ҳ�Ĳ�ѯ���пͻ�����
	List<CustCls> printingCustDocList(Map map);

	// ���տͻ���������ѯ�����Ŀͻ�������Ϣ
	String selectClsId(String custId);

	// ����ɾ���ͻ�����
	int deleteCustDocList(List<String> custId);

	// ��ѯ�ϼ��ͻ�
	CustDoc selectCustTotalByCustId(String custId);

	List<CustDocDao> selectHaving(Map map);

	//����ѡ��ͻ�
	List<CustCls> selectCustDocListByItv(Map map);
	
	

}