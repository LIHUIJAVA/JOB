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
	 * 新增
	 * @param EcAccountList
	 */
	public void insert(List<EcAccount> EcAccountList);
	/**
	 * 根据id list删除对账单
	 * 未勾兑的对账单可支持删除
	 * @param ids
	 */
	public void delete(@Param("list")List<String> ids);
	/**
	 * 对账单修改
	 * 支持修改状态，不支持修改对账单数据
	 * @param ecAccount
	 */
	public void update(EcAccount ecAccount);
	/**
	 * 对账单列表查询
	 * @param map
	 * @return
	 */
	public List<EcAccount> selectList(Map map);
	public int selectCount(Map map);
	/**
	 * 根据对账单id查询
	 * @param billNo
	 * @return
	 */
	public EcAccount selectByBillNo(String billNo);
	/**
	 * 根据平台订单号查询对账单明细
	 * 过滤条件包含属于勾兑项和勾兑结果为未勾兑
	 * 
	 * @param ecOrderId 平台订单号
	 * @param startDate 对账单的开始时间
	 * @param endDate 对账单的结束时间
	 * @return 返回结果list.size等于0是说明订单已经执行勾兑
	 */
	public List<EcAccount> selectByEcOrderId(@Param("ecOrderId")String ecOrderId,@Param("startDate")String startDate,@Param("endDate")String endDate);
	/**
	 * 自动勾兑
	 * @param checker 勾兑人id
	 * @param checkTime 勾兑时间 用户登录时间
	 * @return
	 */
	public void autoCheck(@Param("checker")String checker,@Param("checkTime")String checkTime);
	/**
	 * 勾兑页面查询未勾兑
	 * @param storeId 店铺编号
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @param storeName 店铺名称
	 * @return
	 */
	public List<EcAccountDiff> goToCheck(Map map);
	public int goToCheckCount(Map map);
	/**
	 * 根据平台id查询对应平台的是否勾兑项
	 * @param platId
	 * @return
	 */
	public List<EcAccountMapp> selectMapp(String platId);
	
}
