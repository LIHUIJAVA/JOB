package com.px.mis.purc.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.px.mis.account.entity.FormBookEntrySub;
import com.px.mis.purc.entity.SellOutWhs;
import com.px.mis.purc.entity.SellOutWhsSub;
import com.px.mis.purc.service.impl.SellOutWhsServiceImpl.zizhu;

public interface SellOutWhsDao {
	
	Integer deleteSellOutWhsByOutWhsId(String outWhsId);
	
	Integer deleteSellOutWhsBySellOrdrInd(String sellOrdrInd);

    Integer insertSellOutWhs(SellOutWhs sellOutWhs);
//    ɾ��֮ǰ������һ��
    Integer insertSellOutWhsDl(List<String> lists2);
    
    Integer insertSellOutWhsUpload(SellOutWhs sellOutWhs);
    
    Integer batchInsertSellOutWhs(List<SellOutWhs> sellOutWhsList);

    SellOutWhs selectSellOutWhsByOutWhsId(String outWhsId);
    
    SellOutWhs selectSellOutWhsById(String outWhsId);

    Integer updateSellOutWhsByOutWhsId(SellOutWhs sellOutWhs);

    List<SellOutWhs> selectSellOutWhsList(Map map);
    //��ҳ+����
    List<com.px.mis.purc.service.impl.SellOutWhsServiceImpl.zizhu> selectSellOutWhsListOrderBy(Map map);
    
    Integer selectSellOutWhsCount(Map map);
	
    Integer deleteSellOutWhsList(List<String> outWhsId);
	
	Integer updateSellOutWhsIsNtChkList(List<SellOutWhs> sellOutWhsList);
	
	Integer updateSellOutWhsIsNtChk(SellOutWhs sellOutWhs);
	
	Integer selectIsNtBllgByOutWhsId(String outWhsId);
	
	Integer selectIsNtStlByOutWhsId(String outWhsId);
	
	Integer selectIsNtBookEntryByOutWhsId(String outWhsId);
	
	Integer selectIsNtRtnGoodByOutWhsId(String outWhsId);
	
	List<zizhu> printingSellOutWhsList(Map map);
	//�������۱�ʶ��ѯ���۳��ⵥ�����״̬
	Integer selectIsNtChkBySellOrdrInd(String sellOrdrInd);
	//�������۳��ⵥ�Ų�ѯ���۳��ⵥ�����״̬
	Integer selectIsNtChkByOutWhsId(String outWhsId);
	//�������۱�ʶ��ѯ���۳��ⵥ���
	List<SellOutWhs> selectOutWhsIdBySellOrdrInd(String SellOrdrInd);
	//�����˻�����Ų�ѯ���۳��ⵥ�е���Ϣ
	SellOutWhs selectSellOutWhsByRtnGoodsId(String rtnGoodsId);
	//�������۱�ʶ��ѯ���۳��ⵥ���ӱ��е�����
	SellOutWhs selectSellOutWhsBySellOrdrInd(String SellOrdrInd);
	
	//���ݲֿ���롢������롢���Ų��ӱ����� 
	List<BigDecimal> selectSellOutWhsSubByInWhBn(Map map);
	
	//=================================zds create=================================
	SellOutWhs selectSellOutWhsBySellSnglId(String sellSnglId);
	
	//������ϸ���ѯ
    List<Map> selectSellOutWhsByInvtyEncd(Map map);
    Integer selectSellOutWhsByInvtyEncdCount(Map map);
	
	/**
	 *  ��ѯδ���˵����۳��ⵥ
	 */
	List<SellOutWhs> selectSellOutWhsIsInvty(Map map);
	
	//�����޸ļ���״̬
	Integer updateSellOutWhsBookEntryList(List<SellOutWhs> sellOutWhsList);
	
	//����ί�д����˻��������۳��ⵥ�Ĳֿ���롢������롢���Ų��ӱ����� 
	List<BigDecimal> selectSellOutWhsSubByEntAgnDel(Map map);
	//��ѯδ���˵����۳��ⵥ��ҳ
	List<SellOutWhs> selectSellOutWhsStream(Map map);
	int countSellOutWhsIsInvty(Map map);
	//����ʱ�������۳ɱ�
	void updateCost(List<FormBookEntrySub> subList);
	//������Դ�ӱ���Ų�ѯ��˰����(�˻������ʱʹ��)
	BigDecimal selectSellOutWhsSubByToOrdrNum(Map map);
	
	//ֻ�������ӱ���Ϣ
	SellOutWhs selectSellOutWhsAndSub(String outWhsId);

	Map selectSellOutWhsListSums(Map map);

	Map selectSellOutWhsByInvtyEncdSums(Map map);
	//������ϸ��-����
	List<?> selectSellOutWhsByInvtyEncdPrint(Map map);
	
}