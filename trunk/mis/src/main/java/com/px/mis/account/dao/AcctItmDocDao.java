package com.px.mis.account.dao;

import com.px.mis.account.entity.AcctItmDoc;

import java.util.List;
import java.util.Map;

public interface AcctItmDocDao {
	
	int insertAcctItmDoc(AcctItmDoc acctItmDoc);
	
	int updateAcctItmDocBySubjId(AcctItmDoc acctItmDoc);
	
	Integer deleteAcctItmDocBySubjId(List<String> subjId);

	AcctItmDoc selectAcctItmDocBySubjId(String subjId);
	
	String selectAcctItmDocByPId(String pId);
	
    List<AcctItmDoc> selectAcctItmDocList(Map map);
    
    int selectAcctItmDocCount(Map map);
    //���ݴ������,���������������Ŀ
	List<AcctItmDoc> selectStockByClsEncd(Map<String, Object> map);
	
	//���ݴ������,������������Ӧ��Ŀ
  	List<AcctItmDoc> selectOtherByClsEncd(Map<String, Object> map);
  	//��ѯӦ����Ŀ
	List<AcctItmDoc> selectPaySub(Map<String, Object> map);
	
	//�Ƿ���ĩ����Ŀ
	int selectIsFinalSubj(String debitId);
	//导入新增
    int  insertTolead(Map<String, String> promap);
}
