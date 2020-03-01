package com.px.mis.purc.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.px.mis.purc.dao.EntrsAgnAdjDao;
import com.px.mis.purc.dao.EntrsAgnAdjSubDao;
import com.px.mis.purc.dao.EntrsAgnDelvDao;
import com.px.mis.purc.entity.EntrsAgnAdj;
import com.px.mis.purc.entity.EntrsAgnAdjSub;
import com.px.mis.purc.entity.EntrsAgnDelv;
import com.px.mis.purc.entity.EntrsAgnDelvSub;
import com.px.mis.purc.service.EntrsAgnAdjService;
import com.px.mis.purc.util.CalcAmt;
import com.px.mis.util.BaseJson;
import com.px.mis.util.GetOrderNo;
/*委托代销调整单*/
@Transactional
@Service
public class EntrsAgnAdjServiceImpl implements EntrsAgnAdjService {
	@Autowired
	private EntrsAgnAdjDao eaad;//委托代销调整单主
	@Autowired
	private EntrsAgnAdjSubDao eaasd;//委托代销调整单子
	@Autowired
	private EntrsAgnDelvDao eadd;//委托代销发货单主
	//订单号
	@Autowired
	private GetOrderNo getOrderNo;

	//委托代销调整单页面查询接口
	@Override
	public String queryEntrsAgnAdjOrEntrsAgn(Map map) {
		String resp="";
		try {
			List<EntrsAgnDelv> list =  new ArrayList<>();
			List<EntrsAgnDelv> entrsAgnDelvList = eadd.selectEntrsAgnDelvByEntrsAgnAdjList(map);//委托代销发货单
			for(EntrsAgnDelv entrsAgnDelv:entrsAgnDelvList) {
				//根据委托代销调整单号查询是否存在委托代销调整单
				EntrsAgnAdj  entrsAgnAdj = eaad.selectAdjSnglIdByDelvSnglId(entrsAgnDelv.getDelvSnglId());
				//如果不存在查询委托代销发货单   如果存在查询委托代销调整单
				if(entrsAgnAdj==null) {
					list.add(entrsAgnDelv);
				}else {
					EntrsAgnDelv delv = new EntrsAgnDelv();
					BeanUtils.copyProperties(delv, entrsAgnAdj);
					List<EntrsAgnAdjSub> adj = entrsAgnAdj.getEntrsAgnAdjSub();//委托代销调整单子
					List<EntrsAgnDelvSub> agns = new ArrayList<>();//委托代销发货单子
					for(EntrsAgnAdjSub agn :adj) {
						EntrsAgnDelvSub delvs = new EntrsAgnDelvSub();
						BeanUtils.copyProperties(delvs, agn);
						agns.add(delvs);
					}
					delv.setEntrsAgnDelvSub(agns);
					list.add(delv);
				}
			}
			resp=BaseJson.returnRespObjList("purc/EntrsAgnAdj/queryEntrsAgnAdjOrEntrsAgn", true, "查询成功！", null,list);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
	}
	//委托代销调整单中调整结算价功能
	@Override
	public String addEntrsAgnAdj(String userId,List<EntrsAgnAdj> entrsAgnAdjList,String loginTime) {
		String message="";
		Boolean isSuccess=true;
		String resp="";
		try {
			//委托代销调整单子表
			List<EntrsAgnAdjSub> EntrsAgnAdjSubList = new ArrayList<EntrsAgnAdjSub>();
			for(EntrsAgnAdj entrsAgnAdj:entrsAgnAdjList) {
				//根据委托代销发货单编号判断是否有委托代销调整单
				if(eaad.selectAdjSnglIdByDelvSnglId(entrsAgnAdj.getDelvSnglId())==null){
					//查询委托代销发货单的审核状态
					if(eadd.selectEntrsAgnDelvIsNtChk(entrsAgnAdj.getDelvSnglId())==1) {
						//查询委托代销发货单的结算状态
						if(eadd.selectEntrsAgnDelvIsNtStl(entrsAgnAdj.getDelvSnglId())==0) {
							//获取订单号
							String number=getOrderNo.getSeqNo("WTTZ", userId, loginTime);
							if (eaad.selectEntrsAgnAdjByAdjSnglId(number)!=null) {
								message+="编号"+number+"已存在，请重新输入！";
								isSuccess=false;
							}else {
								//将自动生成的委托代销调整单号set到委托代销调整单中
								entrsAgnAdj.setAdjSnglId(number);
								//新增委托代销调整单主表
								int a = eaad.insertEntrsAgnAdj(entrsAgnAdj);
								//获取委托代销子表
								EntrsAgnAdjSubList = entrsAgnAdj.getEntrsAgnAdjSub();
								for(EntrsAgnAdjSub entrsAgnAdjSub:EntrsAgnAdjSubList) {
									BigDecimal noTaxUprc = entrsAgnAdjSub.getNoTaxUprc();//无税单价
									BigDecimal qty = entrsAgnAdjSub.getQty(); //数量
									//获取税率  页面税率未整数，我们需要将税率/100
									entrsAgnAdjSub.setTaxRate(entrsAgnAdjSub.getTaxRate().divide(new BigDecimal(100)));
									BigDecimal taxRate = entrsAgnAdjSub.getTaxRate(); //税率
									//计算未税金额  金额=未税数量*未税单价
									entrsAgnAdjSub.setNoTaxAmt(CalcAmt.noTaxAmt(noTaxUprc, qty));
									//计算税额  税额=未税金额*税率
									entrsAgnAdjSub.setTaxAmt(CalcAmt.taxAmt(noTaxUprc, qty, taxRate));
									//计算含税单价  含税单价=无税单价*税率+无税单价
									entrsAgnAdjSub.setCntnTaxUprc(CalcAmt.cntnTaxUprc(noTaxUprc, qty, taxRate));
									//计算价税合计  价税合计=无税金额*税率+无税金额=税额+无税金额
									entrsAgnAdjSub.setPrcTaxSum(CalcAmt.prcTaxSum(noTaxUprc, qty, taxRate));
									
									entrsAgnAdjSub.setAdjSnglId(entrsAgnAdj.getAdjSnglId());
								}
								//新增委托代销调整单子表
								int b = eaasd.insertEntrsAgnAdjSub(EntrsAgnAdjSubList);
								if(a>=1 && b>=1) {
									isSuccess=true;
									message+="保存成功！";
								}else {
									isSuccess=false;
									message+="调整单："+entrsAgnAdj.getAdjSnglId()+"保存失败！";
									throw new RuntimeException(message);
								}
							}
								
						}else {
							isSuccess=false;
							message+="委托代销发货单号为"+entrsAgnAdj.getDelvSnglId()+"的单据已结算，不能进行调整！！！";
						}
					}else {
						isSuccess=false;
						message+="委托代销发货单号为"+entrsAgnAdj.getDelvSnglId()+"的单据未审核，请先去审核再进行调整！！！";
					}
				}else {
					//修改委托代销调整单主表
					int a = eaad.updateEntrsAgnAdjByAdjSnglId(entrsAgnAdj);
					//删除委托代销调整单子表
					int b = eaasd.deleteEntrsAgnAdjSubByAdjSnglId(entrsAgnAdj.getAdjSnglId());
					EntrsAgnAdjSubList = entrsAgnAdj.getEntrsAgnAdjSub();
					for(EntrsAgnAdjSub entrsAgnAdjSub:EntrsAgnAdjSubList) {
						entrsAgnAdjSub.setAdjSnglId(entrsAgnAdj.getAdjSnglId());
					}
					//新增委托代销调整单子表
					int c = eaasd.insertEntrsAgnAdjSub(EntrsAgnAdjSubList);
					if(a>=1 && b>=1 && c>=1) {
						isSuccess=true;
						message+="保存成功";
					}else {
						isSuccess=false;
						message+="调整单："+entrsAgnAdj.getAdjSnglId()+"保存失败";
						throw new RuntimeException(message);
					}
				}
			}
			resp=BaseJson.returnRespList("purc/EntrsAgnAdj/addEntrsAgnAdj", isSuccess, message, entrsAgnAdjList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resp;
	}

	@Override
	public String editEntrsAgnAdj(EntrsAgnAdj entrsAgnAdj,List<EntrsAgnAdjSub> entrsAgnAdjSubList) {
		String message="";
		Boolean isSuccess=true;
		String resp="";
		eaasd.deleteEntrsAgnAdjSubByAdjSnglId(entrsAgnAdj.getAdjSnglId());
		eaad.updateEntrsAgnAdjByAdjSnglId(entrsAgnAdj);
		eaasd.insertEntrsAgnAdjSub(entrsAgnAdjSubList);
		message="更新成功！";
		
		try {
			resp=BaseJson.returnRespObj("purc/EntrsAgnAdj/editEntrsAgnAdj", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}
	
	@Override
	public String deleteEntrsAgnAdj(String adjSnglId) {
		String message="";
		Boolean isSuccess=true;
		String resp="";
		if(eaad.selectEntrsAgnAdjByAdjSnglId(adjSnglId)!=null){
			eaad.deleteEntrsAgnAdjByAdjSnglId(adjSnglId);
			eaasd.deleteEntrsAgnAdjSubByAdjSnglId(adjSnglId);
			isSuccess=true;
			message="删除成功！";
		}else {
			isSuccess=false;
			message="编号"+adjSnglId+"不存在！";
		}
		
		try {
			resp=BaseJson.returnRespObj("purc/EntrsAgnAdj/deleteEntrsAgnAdj", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	@Override
	public String queryEntrsAgnAdj(String adjSnglId) {
		// TODO Auto-generated method stub
		String message="";
		Boolean isSuccess=true;
		String resp="";
		List<EntrsAgnAdjSub> entrsAgnAdjSub = new ArrayList<>();
		EntrsAgnAdj entrsAgnAdj=eaad.selectEntrsAgnAdjByAdjSnglId(adjSnglId);
		if(entrsAgnAdj!=null) {
			entrsAgnAdjSub=eaasd.selectEntrsAgnAdjSubByAdjSnglId(adjSnglId);
			message="查询成功！";
		}else {
			isSuccess=false;
			message="编号"+adjSnglId+"不存在！";
		}
		try {
			resp=BaseJson.returnRespObjList("purc/EntrsAgnAdj/queryEntrsAgnAdj", isSuccess, message, entrsAgnAdj, entrsAgnAdjSub);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}
	
	@Override
	public String queryEntrsAgnAdjList(Map map) {
		String resp="";
		List<EntrsAgnAdj> poList= eaad.selectEntrsAgnAdjList(map);
		int count = eaad.selectEntrsAgnAdjCount(map);
		
		int pageNo = Integer.parseInt(map.get("pageNo").toString());
		int pageSize = Integer.parseInt(map.get("pageSize").toString());
		int listNum = poList.size();
		int pages = count / pageSize + 1;
		
		try {
			resp=BaseJson.returnRespList("purc/EntrsAgnAdj/queryEntrsAgnAdjList", true, "查询成功！", count, pageNo, pageSize,
					listNum, pages,poList);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	@Override
	public String deleteEntrsAgnAdjList(String adjSnglId) {
		String message="";
		Boolean isSuccess=true;
		String resp="";
		List<String> list = getList(adjSnglId);
		int a = eaad.deleteEntrsAgnAdjList(list);
		if(a>=1) {
		    isSuccess=true;
		    message="删除成功！";
		}else {
			isSuccess=false;
			message="删除失败！";
		}
		try {
			resp=BaseJson.returnRespObj("purc/EntrsAgnAdj/deleteEntrsAgnAdjList", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}
	/**
	 * id放入list
	 * 
	 * @param id
	 *            id(多个已逗号分隔)
	 * @return List集合
	*/
	public List<String> getList(String id) {
		List<String> list = new ArrayList<String>();
		String[] str = id.split(",");
		for (int i = 0; i < str.length; i++) {
		    list.add(str[i]);
	    }
		return list;
   }

	//打印及输入输出时查看委托代销调整单信息
	@Override
	public String printingEntrsAgnAdjList(Map map) {
		String resp="";
		List<EntrsAgnAdj> entrsAgnAdjList = eaad.printingEntrsAgnAdjList(map);
		try {
			resp=BaseJson.returnRespObjList("purc/EntrsAgnAdj/printingEntrsAgnAdjList", true, "查询成功！", null,entrsAgnAdjList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
	}

	
}
