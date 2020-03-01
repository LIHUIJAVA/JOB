package com.px.mis.ec.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.px.mis.ec.entity.LogisticsTab;
import com.px.mis.ec.entity.PlatOrder;
import com.px.mis.ec.entity.PlatOrders;
import com.px.mis.purc.entity.SellSnglSub;

public interface LogisticsTabService {

	public String insert(LogisticsTab logisticsTab);

	public String update(LogisticsTab logisticsTab);

	public String delete(String ordrNum,String accNum);

	public String select(Integer ordrNum);

	public String selectList(Map map);

	public String insertList(List<LogisticsTab> logisticsTabList);

	/**
	 * ���ݶ����ŷ��ض�Ӧ��ݹ�˾����
	 * 
	 * @param platOrder  ��������
	 * @param platOrders ��ϸ�б�
	 * @return Map flag:true Ϊ�ɹ� false Ϊʧ�� expressCode����ݹ�˾���� ��flagΪfalseʱû�д�ֵ
	 *         templateId:��ӡģ��id ��flagΪfalseʱû�д�ֵ message ��ʾ��Ϣ
	 */
	public Map findExpressCodeByOrderId(PlatOrder platOrder, List<PlatOrders> platOrders);

	/**
	 * ����������ȡ�����浥
	 * 
	 * @param logisticsNum ����������id,���ŷָ�
	 * @param accNum       ��ǰ����Ա�˺�
	 */
	public String achieveECExpressOrder(String logisticsNum, String accNum);
	
	/**
	 *	��������µ�
	 * 
	 * @param logisticsNum ����������id,���ŷָ�
	 * @param accNum       ��ǰ����Ա�˺�
	 */
	public String achieveJDWLOrder(String logisticsNum, String accNum);

	/**
	 * ƽ̨��������
	 * 
	 * @param logisticsNum ��Ҫ������������id���ö��ŷָ�
	 * @param accNum       ��ǰ����Ա�˺�
	 * @return
	 */
	public String platOrderShip(String logisticsNum, String accNum);

	/**
	 * ����������ȡ�������浥
	 * 
	 * @param ����������id,���ŷָ�
	 */
	public String cancelECExpressOrder(String ordrNum,String accNum);

	/**
	 * ���ݵ��Ų�ѯ���
	 */
	public String selectPrint(String ordrNum);

	/**
	 * �������ӡ ����������Ĵ�ӡ״̬δ�Ѵ�ӡ
	 * 
	 * @param orderNum ����������id�����ŷָ�
	 * @return
	 */
	public String print(String orderNum, String accNum);

	/**
	 * �ƴ�ӡ
	 */
	public String achieveECExpressCloudPrint(String ordrNum, String accNum);
	
	/**
	 * ǿ�Ʒ���
	 * @param ordrNum ���ŷָ�����������
	 * @param accNum ������id
	 * @return ������
	 */
	public String forceShip(String ordrNum,String accNum);
	
	/**
	 * ȡ������
	 * @param ordrNum ���ŷָ�����������
	 * @param accNum ������id
	 * @return ������
	 */
	public String cancelShip(String ordrNum,String accNum);
	
	public String exportList(Map map);
	/**
	 * �����޸Ŀ�ݹ�˾����
	 * @param accNum
	 * @param ordrNum
	 * @param expressEncd
	 * @return
	 */
	public String batchUpdate(String accNum,String ordrNum,String expressEncd);
	/**
	 * �޸������ݵ���
	 * @param ordrNum
	 * @param expressCode
	 */
	public String updateExpressCode(String ordrNum,String expressCode);
	
	/**
	 * ���¼��״̬
	 * @param accNum ����Ա����
	 * @param orderNums �ö��ŷָ�����������
	 * @param type 1����Ϊ������ 2����Ϊδ��ʼ���
	 * @return
	 */
	public String updatePick(String accNum, String orderNums,String type);
	
	/**
	 * �����ݵ���
	 * @param file
	 * @param userId
	 * @return
	 */
	public String importExpressCode(MultipartFile  file,String userId);
	
	public void loadExpressCode();
	
	/**
	 * ȡ����ݵ��� ��ֱӪ�Ϳ�ݹ�˾
	 * @param ordrNum ���ŷָ�������������
	 * @param accNum  �û��˺�
	 * @return
	 */
	public String cancelExpressOrder(String ordrNum,String accNum);

}
