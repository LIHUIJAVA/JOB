package com.px.mis.ec.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.px.mis.ec.entity.Aftermarket;
import com.px.mis.ec.entity.EcAccount;
import com.px.mis.ec.entity.EcAccountDiff;
import com.px.mis.ec.entity.EcAccountMapp;
import com.px.mis.whs.entity.ProdStruSubTab;

public interface EcAccountDao {
	/**
	 * ����
	 * @param EcAccountList
	 */
	public void insert(List<EcAccount> EcAccountList);
	/**
	 * ����id listɾ�����˵�
	 * δ���ҵĶ��˵���֧��ɾ��
	 * @param ids
	 */
	public void delete(@Param("list")List<String> ids);
	/**
	 * ���˵��޸�
	 * ֧���޸�״̬����֧���޸Ķ��˵�����
	 * @param ecAccount
	 */
	public void update(EcAccount ecAccount);
	/**
	 * ���˵��б��ѯ
	 * @param map
	 * @return
	 */
	public List<EcAccount> selectList(Map map);
	public int selectCount(Map map);
	/**
	 * ���ݶ��˵�id��ѯ
	 * @param billNo
	 * @return
	 */
	public EcAccount selectByBillNo(String billNo);
	/**
	 * ����ƽ̨�����Ų�ѯ���˵���ϸ
	 * ���������������ڹ�����͹��ҽ��Ϊδ����
	 * 
	 * @param ecOrderId ƽ̨������
	 * @param startDate ���˵��Ŀ�ʼʱ��
	 * @param endDate ���˵��Ľ���ʱ��
	 * @return ���ؽ��list.size����0��˵�������Ѿ�ִ�й���
	 */
	public List<EcAccount> selectByEcOrderId(@Param("ecOrderId")String ecOrderId,@Param("startDate")String startDate,@Param("endDate")String endDate);
	/**
	 * �Զ�����
	 * @param checker ������id
	 * @param checkTime ����ʱ�� �û���¼ʱ��
	 * @return
	 */
	public void autoCheck(@Param("checker")String checker,@Param("checkTime")String checkTime);
	/**
	 * ����ҳ���ѯδ����
	 * @param storeId ���̱��
	 * @param startDate ��ʼʱ��
	 * @param endDate ����ʱ��
	 * @param storeName ��������
	 * @return
	 */
	public List<EcAccountDiff> goToCheck(Map map);
	public int goToCheckCount(Map map);
	/**
	 * ����ƽ̨id��ѯ��Ӧƽ̨���Ƿ񹴶���
	 * @param platId
	 * @return
	 */
	public List<EcAccountMapp> selectMapp(String platId);
	
}
