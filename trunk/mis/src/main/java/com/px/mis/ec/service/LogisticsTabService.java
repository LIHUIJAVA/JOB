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
	 * 根据订单号返回对应快递公司编码
	 * 
	 * @param platOrder  订单主表
	 * @param platOrders 明细列表
	 * @return Map flag:true 为成功 false 为失败 expressCode：快递公司编码 当flag为false时没有此值
	 *         templateId:打印模板id 当flag为false时没有此值 message 提示消息
	 */
	public Map findExpressCodeByOrderId(PlatOrder platOrder, List<PlatOrders> platOrders);

	/**
	 * 跟据物流表取电子面单
	 * 
	 * @param logisticsNum 物流表主键id,逗号分隔
	 * @param accNum       当前操作员账号
	 */
	public String achieveECExpressOrder(String logisticsNum, String accNum);
	
	/**
	 *	京东快递下单
	 * 
	 * @param logisticsNum 物流表主键id,逗号分隔
	 * @param accNum       当前操作员账号
	 */
	public String achieveJDWLOrder(String logisticsNum, String accNum);

	/**
	 * 平台订单发货
	 * 
	 * @param logisticsNum 需要发货的物流表id，用逗号分隔
	 * @param accNum       当前操作员账号
	 * @return
	 */
	public String platOrderShip(String logisticsNum, String accNum);

	/**
	 * 跟据物流表取消电子面单
	 * 
	 * @param 物流表主键id,逗号分隔
	 */
	public String cancelECExpressOrder(String ordrNum,String accNum);

	/**
	 * 根据单号查询多个
	 */
	public String selectPrint(String ordrNum);

	/**
	 * 物流表打印 更新物流表的打印状态未已打印
	 * 
	 * @param orderNum 物流表主键id，逗号分隔
	 * @return
	 */
	public String print(String orderNum, String accNum);

	/**
	 * 云打印
	 */
	public String achieveECExpressCloudPrint(String ordrNum, String accNum);
	
	/**
	 * 强制发货
	 * @param ordrNum 逗号分隔的物流单号
	 * @param accNum 操作人id
	 * @return 处理结果
	 */
	public String forceShip(String ordrNum,String accNum);
	
	/**
	 * 取消发货
	 * @param ordrNum 逗号分隔的物流单号
	 * @param accNum 操作人id
	 * @return 处理结果
	 */
	public String cancelShip(String ordrNum,String accNum);
	
	public String exportList(Map map);
	/**
	 * 批量修改快递公司编码
	 * @param accNum
	 * @param ordrNum
	 * @param expressEncd
	 * @return
	 */
	public String batchUpdate(String accNum,String ordrNum,String expressEncd);
	/**
	 * 修改填入快递单号
	 * @param ordrNum
	 * @param expressCode
	 */
	public String updateExpressCode(String ordrNum,String expressCode);
	
	/**
	 * 更新拣货状态
	 * @param accNum 操作员编码
	 * @param orderNums 用逗号分隔的物流单号
	 * @param type 1更新为拣货完成 2更新为未开始拣货
	 * @return
	 */
	public String updatePick(String accNum, String orderNums,String type);
	
	/**
	 * 导入快递单号
	 * @param file
	 * @param userId
	 * @return
	 */
	public String importExpressCode(MultipartFile  file,String userId);
	
	public void loadExpressCode();
	
	/**
	 * 取消快递单号 限直营型快递公司
	 * @param ordrNum 逗号分隔的物流单主键
	 * @param accNum  用户账号
	 * @return
	 */
	public String cancelExpressOrder(String ordrNum,String accNum);

}
