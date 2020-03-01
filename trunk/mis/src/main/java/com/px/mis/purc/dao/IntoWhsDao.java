package com.px.mis.purc.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.px.mis.account.entity.FormBookEntry;
import com.px.mis.purc.entity.IntoWhs;
import com.px.mis.purc.service.impl.PurcIntoWhsServiceImpl;
import com.px.mis.purc.service.impl.PurcIntoWhsServiceImpl.zizhu;
import com.px.mis.whs.service.impl.IntoWhsServiceImpl;

public interface IntoWhsDao {

	
    int deleteIntoWhsByIntoWhsSnglId(String intoWhsSnglId);

    int insertIntoWhs(IntoWhs intoWhs);
    //�h���r�򣬂�ݵ��U����һ��
    int insertIntoWhsDl(List<String> list2);
    
    int insertIntoWhsUpload(List<IntoWhs> intoWhs);

    int updateIntoWhsByIntoWhsSnglId(IntoWhs intoWhs);
    
    IntoWhs selectIntoWhsByIntoWhsSnglId(String intoWhsSnglId);
    
    IntoWhs selectIntoWhsById(String intoWhsSnglId);
    
    List<IntoWhs> selectIntoWhsList(Map map);
    
    List<PurcIntoWhsServiceImpl.zizhu> selectIntoWhsListOrderBy(Map map);
	
	int selectIntoWhsCount(Map map);
	
	int selectIntoWhsCountLess(Map map);
	
	int deleteIntoWhsList(List<String> intoWhsSnglId);
	
	int updateIntoWhsIsNtChk(List<IntoWhs> intoWhs);
	
	int updateIntoWhsIsNtChkByIntoWhs(IntoWhs intoWhs);
	
	List<zizhu> printingIntoWhsList(Map map);
	
	//��ѯ�ɹ���ⵥ�����״̬
	int selectIntoWhsIsNtChk(String intoWhsSnglId);
	//��ѯ�ɹ���ⵥ�ļ���״̬
	int selectIntoWhsIsNtBookEntry(String intoWhsSnglId);
	//��ѯ�ɹ���ⵥ�Ŀ���Ʊ״̬
	int selectIntoWhsIsNtBllg(String intoWhsSnglId);
	//��ѯ�ɹ���ⵥ�Ľ���״̬
	int selectIntoWhsIsNtStl(String intoWhsSnglId);
	//��ѯ�ɹ���ⵥ���˻�״̬
	int selectIntoWhsIsNtRtn(String intoWhsSnglId);
	
	//���ݵ�������Ų�ѯ��ⵥ��Ϣ
	List<IntoWhs> selectIntoWhsByToGdsSnglId(String ToGdsSnglId);
	
	//�ɹ���ⵥ��ϸ��ѯ
	List<Map> selectIntoWhsByInvtyEncd(Map map);
	
	int selectIntoWhsByInvtyEncdCount(Map map);
	
	//�ɹ����ջ�ͳ�Ʊ�չʾ
	List<Map> selectIntoWhsAndPursOrdr(Map map);
	
	/**
	 *  ��ѯδ���˵Ĳɹ���ⵥ
	 */
	List<IntoWhs> selectIntoWhsIsInvty(Map map);
	
	//�޸Ĳɹ���ⵥ�ļ���״̬
	int updateIntoWhsBookEntryList(List<IntoWhs> intoWhs);
	
	//�޸Ŀ�Ʊ״̬Ϊ�ѿ�
	int updateIntoWhsIsNtBllgOK(String intoWhsSnglId);
	//�޸Ŀ�Ʊ״̬Ϊδ��
    int updateIntoWhsIsNtBllgNO(String intoWhsSnglId);
	
	//�޸Ľ���״̬
	int updateIntoWhsIsNtStl(String intoWhsSnglId);
	//��ҳ
	List<IntoWhs> selectIntoWhsStream(Map map);
	int countSelectIntoWhsStream(Map map);
	
	//��ѯ���һ����ⵥ��
	BigDecimal selectUnTaxUprc(Map map);
	//��ѯ�ɹ���ⵥԭ���Ŀ�Ʊ����
	List<IntoWhs> selectIntoWhsByInvty(Map<String, Object> map);
	
	//����ʱ��ѯ������Ϣ
    List<IntoWhs> selectIntoWhsLists(Map map);
	int selectIntoWhsCounts(Map map);

	Map selectIntoWhsListSum(Map map);

	Map selectIntoWhsAndPursOrdrSums(Map map);
	
	// ��ѯ�ɹ�������Ӧ�Ĳɹ���ⵥ������Ϊ����
	List<Map> selectIntoWhsQtyByPursOrdrId(Map map);
	//�����ϸ��-�����ӿ�
	List<Map> selectIntoWhsByInvtyEncdPrint(Map map);

	
	
	
	
}