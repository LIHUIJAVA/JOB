package com.px.mis.purc.service.impl;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.px.mis.account.dao.SellComnInvDao;
import com.px.mis.account.dao.SellComnInvSubDao;
import com.px.mis.account.entity.SellComnInv;
import com.px.mis.account.entity.SellComnInvSub;
import com.px.mis.purc.dao.EntrsAgnDelvDao;
import com.px.mis.purc.dao.EntrsAgnDelvSubDao;
import com.px.mis.purc.dao.EntrsAgnStlDao;
import com.px.mis.purc.dao.EntrsAgnStlSubDao;
import com.px.mis.purc.entity.EntrsAgnDelvSub;
import com.px.mis.purc.entity.EntrsAgnStl;
import com.px.mis.purc.entity.EntrsAgnStlSub;
import com.px.mis.purc.service.EntrsAgnStlService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.CommonUtil;
import com.px.mis.util.GetOrderNo;
import com.px.mis.util.poiTool;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
/*委托代销结算单*/
@Transactional
@Service
public class EntrsAgnStlServiceImpl extends poiTool  implements EntrsAgnStlService {
	@Autowired
	private EntrsAgnStlDao ead;//委托代销结算单主表
	@Autowired
	private EntrsAgnStlSubDao easd;//委托代销结算单子表
	//订单号
	@Autowired
	private GetOrderNo getOrderNo;
	@Autowired
	private SellComnInvDao sellComnInvDao;//销售发票主表
	@Autowired
	private SellComnInvSubDao sellComnInvSubDao;//销售发票子表
	@Autowired
	private EntrsAgnDelvDao entrsAgnDelvDao;//委托代销发货单主表
	@Autowired
	private EntrsAgnDelvSubDao entrsAgnDelvSubDao;//委托代销发货单子表
	
	public static class zizhu{
		//主表
		@JsonProperty("结算单编码")
		public String stlSnglId;//结算单编码
		@JsonProperty("结算单日期")
		public String stlSnglDt;//结算单日期
		@JsonProperty("单据类型编码")
		public String formTypEncd;//单据类型编码
		@JsonProperty("销售类型编号")
		public String sellTypId;//销售类型编号
		@JsonProperty("业务类型编号")
		public String bizTypId;//业务类型编号
		@JsonProperty("客户编号")
		public String custId;//客户编号
		@JsonProperty("用户编号")
		public String accNum;//用户编号
		@JsonProperty("用户名称")
		public String userName;//用户名称
		@JsonProperty("部门编号")
		public String deptId;//部门编号
		@JsonProperty("部门名称")
		public String deptName;//部门名称
		@JsonProperty("发货单号")
		public String sellOrdrId;//发货单号
		@JsonProperty("创建人")
		public String setupPers;//创建人
		@JsonProperty("创建时间  ")
		public String setupTm;//创建时间  
		@JsonProperty("修改人")
		public String mdfr;//修改人
		@JsonProperty("修改时间")
		public String modiTm;//修改时间
		@JsonProperty("是否开票")
		public Integer isNtBllg;//是否开票
		@JsonProperty("是否审核")
		public Integer isNtChk;//是否审核
		@JsonProperty("审核人")
		public String chkr;//审核人
		@JsonProperty("审核时间")
		public String chkDt;//审核时间
		@JsonProperty("发票编号")
		public String invId;//发票编号
		@JsonProperty("客户订单号")
		public String custOrdrNum;//客户订单号
		@JsonProperty("是否退货")
		public Integer isNtRtnGood;//是否退货
		@JsonProperty("来源单据类型编号")
		public String toFormTypId;//来源单据类型编码
		@JsonProperty("客户开户银行")
		public String custOpnBnk;//客户开户银行
		@JsonProperty("银行账号")
		public String bkatNum;//银行账号
		@JsonProperty("本单位银行编号")
		public String dvlprBankId;//本单位银行编号
		@JsonProperty("表头备注  ")
		public String memo;//备注  
		@JsonProperty("表体备注  ")
		public String memos;//备注  
		@JsonProperty("客户名称")
		public String custNm;//客户名称
		@JsonProperty("业务类型名称")
		public String bizTypNm;//业务类型名称
		@JsonProperty("单据类型名称")
		public String formTypName;//单据类型名称
		@JsonProperty("销售类型名称")
		public String sellTypNm;//销售类型名称
		@JsonProperty("来源单据类型编码")
		public String toFormTypEncd;//来源单据类型编码
		@JsonProperty("是否生成凭证")
		public Integer isNtMakeVouch;//是否生成凭证
		@JsonProperty("制凭证人")
		public String makVouchPers;//制凭证人
		@JsonProperty("制凭证时间")
		public String makVouchTm;//制凭证时间
		@JsonProperty("序号")
		public Long ordrNum;//序号
		@JsonProperty("仓库编码")
		public String whsEncd;//仓库编码
		@JsonProperty("存货编码")
		public String invtyEncd;//存货编码
		@JsonProperty("数量")
		public BigDecimal qty;//数量
		@JsonProperty("无税单价")
		public BigDecimal noTaxUprc;//无税单价
		@JsonProperty("无税金额")
		public BigDecimal noTaxAmt;//无税金额
		@JsonProperty("税额")
		public BigDecimal taxAmt;//税额
		@JsonProperty("税率")
		public BigDecimal taxRate;//税率
		@JsonProperty("含税单价")
		public BigDecimal cntnTaxUprc;//含税单价
		@JsonProperty("价税合计")
		public BigDecimal prcTaxSum;//价税合计
		@JsonProperty("生产日期")
		public String prdcDt;//生产日期
		@JsonProperty("失效日期")
		public String invldtnDt;//失效日期
		@JsonProperty("批号")
		public String batNum;//批号
		@JsonProperty("国际批次")
		public String intlBat;//国际批次
		@JsonProperty("是否赠品")
		public Integer isComplimentary;//是否赠品
		@JsonProperty("存货名称")
		public String invtyNm;//存货名称
		@JsonProperty("规格型号")
		public String spcModel;//规格型号
		@JsonProperty("存货代码")
		public String invtyCd;//存货代码
		@JsonProperty("箱规")
		public BigDecimal bxRule;//箱规
		@JsonProperty("进项税率")
		public BigDecimal iptaxRate;//进项税率
		@JsonProperty("销项税率")
		public BigDecimal optaxRate;//销项税率
		@JsonProperty("最高进价")
		public BigDecimal highestPurPrice;//最高进价
		@JsonProperty("最低售价")
		public BigDecimal loSellPrc;//最低售价
		@JsonProperty("参考成本")
		public BigDecimal refCost;//参考成本
		@JsonProperty("参考售价")
		public BigDecimal refSellPrc;//参考售价
		@JsonProperty("最新成本")
		public BigDecimal ltstCost;//最新成本
		@JsonProperty("计量单位名称")
		public String measrCorpNm;//计量单位名称
		@JsonProperty("仓库名称")
		public String whsNm;//仓库名称
		@JsonProperty("对应条形码")
		public String crspdBarCd;//对应条形码
		@JsonProperty("未开票数量")
		public BigDecimal unBllgQty;//未开票数量
		@JsonProperty("来源子表序号")
		public Long toOrdrNum;//来源子表序号
		@JsonProperty("箱数")
		public BigDecimal bxQty;//箱数
		@JsonProperty("保质期")
		public Integer baoZhiQi;//保质期
	}
	
	//新增委托代销结算单
	@Override
	public String addEntrsAgnStl(String userId,EntrsAgnStl entrsAgnStl,List<EntrsAgnStlSub> entrsAgnStlSubList,String loginTime) {
		String message="";
		Boolean isSuccess=true;
		String resp="";
		try {
			//获取订单号
			String number=getOrderNo.getSeqNo("WJ", userId,loginTime);
			if (ead.selectEntrsAgnStlByStlSnglId(number)!=null){
				message="编号"+number+"已存在，请重新输入！";
				isSuccess=false;
			}else {
				entrsAgnStl.setStlSnglId(number);
				ead.insertEntrsAgnStl(entrsAgnStl);
				for(EntrsAgnStlSub entrsAgnStlSub: entrsAgnStlSubList) {
					entrsAgnStlSub.setStlSnglId(entrsAgnStl.getStlSnglId());
					entrsAgnStlSub.setUnBllgQty(entrsAgnStlSub.getQty().abs());
					if (entrsAgnStlSub.getPrdcDt() == "") {
						entrsAgnStlSub.setPrdcDt(null);
					}
					if (entrsAgnStlSub.getInvldtnDt() == "") {
						entrsAgnStlSub.setInvldtnDt(null);
					}
				}
				
				easd.insertEntrsAgnStlSub(entrsAgnStlSubList);
				message="新增成功！";
				isSuccess=true;
			}
			resp=BaseJson.returnRespObj("purc/EntrsAgnStl/addEntrsAgnStl", isSuccess, message, entrsAgnStl);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resp;
	}

	//修改委托代销结算单
	@Override
	public String editEntrsAgnStl(EntrsAgnStl entrsAgnStl,List<EntrsAgnStlSub> entrsAgnStlSubList) {
		String message="";
		Boolean isSuccess=true;
		String resp="";
		easd.deleteEntrsAgnStlSubByStlSnglId(entrsAgnStl.getStlSnglId());
		ead.updateEntrsAgnStlByStlSnglId(entrsAgnStl);
		for(EntrsAgnStlSub entrsAgnStlSub: entrsAgnStlSubList) {
			entrsAgnStlSub.setStlSnglId(entrsAgnStl.getStlSnglId());
			entrsAgnStlSub.setUnBllgQty(entrsAgnStlSub.getQty().abs());
			if (entrsAgnStlSub.getPrdcDt() == "") {
				entrsAgnStlSub.setPrdcDt(null);
			}
			if (entrsAgnStlSub.getInvldtnDt() == "") {
				entrsAgnStlSub.setInvldtnDt(null);
			}
		}
		easd.insertEntrsAgnStlSub(entrsAgnStlSubList);
		message="更新成功！";
		
		try {
			resp=BaseJson.returnRespObj("purc/EntrsAgnStl/editEntrsAgnStl", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}
	
	//单个删除委托代销结算单
	@Override
	public String deleteEntrsAgnStl(String stlSnglId) {
		String message="";
		Boolean isSuccess=true;
		String resp="";
		if(ead.selectEntrsAgnStlByStlSnglId(stlSnglId)!=null) {
			ead.deleteEntrsAgnStlByStlSnglId(stlSnglId);
			isSuccess=true;
			message="删除成功！";
		}else {
			isSuccess=false;
			message="编号"+stlSnglId+"不存在！";
		}
		
		try {
			resp=BaseJson.returnRespObj("purc/EntrsAgnStl/deleteEntrsAgnStl", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	//查询委托代销结算单详情
	@Override
	public String queryEntrsAgnStl(String stlSnglId) {
		// TODO Auto-generated method stub
		String message="";
		Boolean isSuccess=true;
		String resp="";
		List<EntrsAgnStlSub> entrsAgnStlSub = new ArrayList<>();
		EntrsAgnStl entrsAgnStl = ead.selectEntrsAgnStlByStlSnglId(stlSnglId);
		if(entrsAgnStl!=null) {
			entrsAgnStlSub = easd.selectEntrsAgnStlSubByStlSnglId(stlSnglId);
			message="查询成功！";
		}else {
			isSuccess=false;
			message="编号"+stlSnglId+"不存在！";
		}
		
		try {
			resp=BaseJson.returnRespObjList("purc/EntrsAgnStl/queryEntrsAgnStl", isSuccess, message, entrsAgnStl, entrsAgnStlSub);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}
	
	//查询委托代销结算单列表
	@Override
	public String queryEntrsAgnStlList(Map map) {
		String resp="";
		List<EntrsAgnStl> poList = ead.selectEntrsAgnStlList(map);
		int count = ead.selectEntrsAgnStlCount(map);
		
		int pageNo = Integer.parseInt(map.get("pageNo").toString());
		int pageSize = Integer.parseInt(map.get("pageSize").toString());
		int listNum = poList.size();
		int pages = count / pageSize + 1;
		
		try {
			resp=BaseJson.returnRespList("purc/EntrsAgnStl/queryEntrsAgnStl", true, "查询成功！", count,
					pageNo, pageSize,
					listNum, pages,poList);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	//批量删除委托代销结算单
	@Override
	public String deleteEntrsAgnStlList(String stlSnglId) {
		String message="";
		Boolean isSuccess=true;
		String resp="";
		try {
			List<String> lists = getList(stlSnglId);
			List<String> lists2 =  new ArrayList<>();
			List<String> lists3 =  new ArrayList<>();
			for(String list:lists) {
				if(ead.selectEntrsAgnStlByIsNtChk(list)==0) {
					lists2.add(list);
				}else {
					lists3.add(list);
				}
			}
			if(lists2.size()>0){
				int a = ead.deleteEntrsAgnStlList(lists2);
				if(a>=1) {
				    isSuccess=true;
				    message+="单据号为："+lists2.toString()+"的订单删除成功!\n";
				}else {
					isSuccess=false;
					message+="单据号为："+lists2.toString()+"的订单删除失败！\n";
				}
			}
			if(lists3.size()>0) {
				isSuccess=false;
				message+="单据号为："+lists3.toString()+"的订单已被审核，无法删除！\n";
			}
			resp=BaseJson.returnRespObj("purc/EntrsAgnStl/deleteEntrsAgnStlList", isSuccess, message, null);
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
		if(StringUtils.isNotEmpty(id)) {
			if(id.contains(",")) {
				String[] str = id.split(",");
				for (int i = 0; i < str.length; i++) {
					list.add(str[i]);
				}	
			}else{
				if(StringUtils.isNotEmpty(id)) {
					list.add(id);
				}
			}
		}
		return list;
	}

	//打印输入输出委托代销结算单
	@Override
	public String printingEntrsAgnStlList(Map map) {
		String resp="";
		List<EntrsAgnStl> entrsAgnStlList = ead.printingEntrsAgnStlList(map);
		try {
//			
			resp=BaseJson.returnRespObjListAnno("purc/EntrsAgnStl/printingEntrsAgnStlList", true, "查询成功！", null,entrsAgnStlList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
	}

	//委托代销结算单审的核及弃审
	@Override
	public Map<String,Object> updateEntrsAgnStlIsNtChk(String userId,EntrsAgnStl entrsAgnStl,String loginTime) {
		StringBuilder message = new StringBuilder();
		Boolean isSuccess = true;
		Map<String,Object> map = new HashMap<>();
		try {
			// 前端传的值为1时，说明要进行审核操作
			if (entrsAgnStl.getIsNtChk() == 1) {
                message.append(updateEntrsAgnStlIsNtChkOK(userId,entrsAgnStl,loginTime).get("message"));
			} else if (entrsAgnStl.getIsNtChk() == 0) {
                message.append(updateEntrsAgnStlIsNtChkNO(entrsAgnStl).get("message"));
			} else {
				isSuccess = false;
				message.append("审核状态错误，无法审核！\n");
			}
			map.put("isSuccess", isSuccess);
			map.put("message", message.toString());	
		}catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return map;
	}
	
	//审核委托代销结算单
	public Map<String,Object> updateEntrsAgnStlIsNtChkOK(String userId,EntrsAgnStl entrsAgnStl,String loginTime) throws RuntimeException {
		String message = "";
		Boolean isSuccess = true;
		Map<String,Object> map = new HashMap<>();
		if(ead.selectEntrsAgnStlByIsNtChk(entrsAgnStl.getStlSnglId()) == 0) {
			EntrsAgnStl entAgStl = ead.selectEntrsAgnStlByStlSnglId(entrsAgnStl.getStlSnglId());//查询委托代销结算单主表信息
			List<EntrsAgnStlSub> entrsAgnStlSubList = entAgStl.getEntrsAgnStlSub();//查询委托代销结算单子表信息
			if(entAgStl.getSellOrdrId()!=null && !entAgStl.getSellOrdrId().equals("") ) {
				map.put("delvSnglId", entAgStl.getSellOrdrId());//获取发货单号
				List<EntrsAgnDelvSub> entrsAgnDelvSubList=entrsAgnDelvSubDao.selectEntrsAgnDelvSubByInvBatWhs(map);//根据发货单号查询发货单子表信息
				
				for(EntrsAgnStlSub entrsAgnStlSub:entrsAgnStlSubList) {
					map.put("qty", entrsAgnStlSub.getQty().abs());//数量
					map.put("prcTaxSum", entrsAgnStlSub.getPrcTaxSum().abs());//含税金额
					if(entrsAgnStlSub.getToOrdrNum()!=null && entrsAgnStlSub.getToOrdrNum() != 0){
						map.put("ordrNum",entrsAgnStlSub.getToOrdrNum());//来源单据子表序号
						EntrsAgnDelvSub aaa = entrsAgnDelvSubDao.selectEntDeSubToOrdrNum(entrsAgnStlSub.getToOrdrNum());//根据发货子表序号查询发货单子表信息
						//修改委托代销发货单上的数量、单价、金额
						if(entrsAgnStlSub.getQty().compareTo(aaa.getQty())==1) {
							isSuccess = false;
							message +="编码为："+entrsAgnStl.getStlSnglId() + "的委托代销结算单中部分明细结算数量大于发货数量，无法审核！\n";
							throw new RuntimeException(message); 
						}else {
							entrsAgnDelvSubDao.updateEntrsAgnDelvSubByEntrsAgnStlSubJia(map);

						}
					}
				}
				List<EntrsAgnDelvSub> entrsAgnDelvSubLists=entrsAgnDelvSubDao.selectEntrsAgnDelvSubByInvBatWhs(map);//根据发货单号查询发货单子表信息
				//新增销售专用发票
				insertSellComnInv(userId,loginTime,entAgStl,entrsAgnStlSubList);
				//根据存货、仓库、批次、委托代销发货单号查询委托代销发货单子表数据
				if(entrsAgnDelvSubList.size()>0) {
					int num=0;
					for(EntrsAgnDelvSub entrsAgnDelvSub: entrsAgnDelvSubLists) {
						if(entrsAgnDelvSub.getStlQty().compareTo(entrsAgnDelvSub.getQty())==0) {
							num++;
						}	
					}
					if(num==entrsAgnDelvSubList.size()) {
						entrsAgnDelvDao.updateEntrsAgnDelvToIsNtStlOK(entAgStl.getSellOrdrId());//修改发货单中的结算状态
					}
				}
				int a = ead.updateEntrsAgnStlIsNtChk(entrsAgnStl);
				if (a >= 1) {
						isSuccess = true;
						message += entrsAgnStl.getStlSnglId() +"委托代销结算单审核成功！\n";
				} else {
					isSuccess = false;
					message += entrsAgnStl.getStlSnglId() + "委托代销结算单审核失败！\n";
					throw new RuntimeException(message);
				}
			}else {
				isSuccess = false;
				message +="编码为："+entrsAgnStl.getStlSnglId() + "的委托代销结算单没有对应的发货单，无法审核！\n";
				throw new RuntimeException(message);
			}
		}else {
			isSuccess=false;
			message += entrsAgnStl.getStlSnglId()+ "该单据已审核，不需要重复审核\n";
			throw new RuntimeException(message);
		}
		map.put("isSuccess", isSuccess);
		map.put("message", message);
		return map;
		
	}
	//弃审委托代销结算单
	public Map<String,Object> updateEntrsAgnStlIsNtChkNO(EntrsAgnStl entrsAgnStl) throws RuntimeException {
		String message = "";
		Boolean isSuccess = true;
		Map<String,Object> map = new HashMap<>();
		if (ead.selectEntrsAgnStlByIsNtChk(entrsAgnStl.getStlSnglId()) == 1) {
			EntrsAgnStl entAgStl = ead.selectEntrsAgnStlByStlSnglId(entrsAgnStl.getStlSnglId());//查询委托代销结算单主表信息
			List<EntrsAgnStlSub> entrsAgnStlSubList = entAgStl.getEntrsAgnStlSub();//查询委托代销结算单子表信息
			if(entAgStl.getSellOrdrId()!=null && !entAgStl.getSellOrdrId().equals("") ) {
				for(EntrsAgnStlSub entrsAgnStlSub:entrsAgnStlSubList) {
					map.put("qty", entrsAgnStlSub.getQty().abs());//数量
					map.put("prcTaxSum", entrsAgnStlSub.getPrcTaxSum().abs());//无税金额
					if(entrsAgnStlSub.getToOrdrNum()!=null && entrsAgnStlSub.getToOrdrNum() != 0){
						map.put("ordrNum",entrsAgnStlSub.getToOrdrNum());//来源单据子表序号
						//根据结算单来源子表序号查询发货单子表信息
						EntrsAgnDelvSub entrsAgnDelvSub = entrsAgnDelvSubDao.selectEntDeSubToOrdrNum(entrsAgnStlSub.getToOrdrNum());
						if(entrsAgnStlSub.getQty().compareTo(entrsAgnDelvSub.getStlQty())==0 || 
								entrsAgnStlSub.getQty().compareTo(entrsAgnDelvSub.getStlQty())==1) {
							//修改委托代销发货单上的数量、单价、金额
							entrsAgnDelvSubDao.updateEntrsAgnDelvSubByEntrsAgnStlSubJian(map);
						}else {
							isSuccess=false;
							message += "单据号为"+entrsAgnStl.getStlSnglId()+ "结算单中存货【"+entrsAgnStlSub.getInvtyEncd()+"】批次：【"+entrsAgnStlSub.getIntlBat()+"】的结算数量大于发货数量，无法弃审\n";
							throw new RuntimeException(message);
						}
					}
					
				}
				//修改委托代销发货单的结算状态
				entrsAgnDelvDao.updateEntrsAgnDelvToIsNtStlNO(entAgStl.getSellOrdrId());//修改发货单中的结算状态
				//根据委托代销结算单编码删除对应发票信息
				sellComnInvDao.deleteSellComnInvBySellInvNum(entAgStl.getStlSnglId());
				
				int a = ead.updateEntrsAgnStlIsNtChk(entrsAgnStl);
				if (a >= 1) {
						isSuccess = true;
						message += entrsAgnStl.getStlSnglId() +"委托代销结算单弃审成功！\n";
				} else {
					isSuccess = false;
					message += entrsAgnStl.getStlSnglId() + "委托代销结算单弃审失败！\n";
					throw new RuntimeException(message);
				}
			}else {
				isSuccess = false;
				message +="编码为："+entrsAgnStl.getStlSnglId() + "的委托代销结算单没有对应的发货单，无法弃审！\n";
				throw new RuntimeException(message);
			}
		} else {
			isSuccess = false;
			message += entrsAgnStl.getStlSnglId() + "该单据未审核，请先审核该单据！\n";
			throw new RuntimeException(message);
		}
		map.put("isSuccess", isSuccess);
		map.put("message", message);
		return map;
	}
	
	//审核委托代销结算单的时候生成销售专用发票
	private Map<String,Object> insertSellComnInv(String userId,String loginTime,EntrsAgnStl entrsAgnStl,List<EntrsAgnStlSub> entrsAgnStlSubList) {
		Boolean isSuccess=true;
		String message = "";
		Map<String,Object> map = new HashMap<>();
		
		SellComnInv sellComnInv = new SellComnInv();//销售专用发票主表
		List<SellComnInvSub> sellComnInvSubList = new ArrayList<>();//销售专用发票子表集合
		try {
			BeanUtils.copyProperties(sellComnInv, entrsAgnStl);//将委托代销主表复制给销售专用发票
			// 获取订单号
			String number = getOrderNo.getSeqNo("XSFP", userId,loginTime);
			if(sellComnInvDao.selectSellComnInvBySellInvNum(number) != null) {
				isSuccess=false;
				message+="销售发票号"+number+"已存在，自动生成销售发票失败！";
			}else {
				sellComnInv.setSellInvNum(number);//自动生成销售发票编码
				sellComnInv.setBllgDt(CommonUtil.getLoginTime(loginTime));//生成发票时间默认等于登录时间
				sellComnInv.setInvTyp("销售专用发票");//发票类型
				sellComnInv.setBizTypId("1");//业务类型：委托代销
				if (entrsAgnStl.getBizTypId().equals("1")) {
					sellComnInv.setRecvSendCateId("7");// 收发类型编号，线下销售
				}
				if (entrsAgnStl.getBizTypId().equals("2")) {
					sellComnInv.setRecvSendCateId("6");// 收发类型编号，线上销售
				}
				sellComnInv.setSetupPers(entrsAgnStl.getChkr());//创建人--默认等于审核人
				sellComnInv.setSetupTm(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));//创建时间
				sellComnInv.setSellSnglNum(entrsAgnStl.getStlSnglId());//委托代销结算单编码
				sellComnInv.setFormTypEncd("021");//单据类型编码
				sellComnInv.setToFormTypEncd(entrsAgnStl.getFormTypEncd());//来源单据类型
				
				for(EntrsAgnStlSub entrsAgnStlSub:entrsAgnStlSubList) {
					SellComnInvSub sellComnInvSub = new SellComnInvSub();//销售专用发票子表
					BeanUtils.copyProperties(sellComnInvSub, entrsAgnStlSub);//将委托代销结算单子表复制给销售专用发票子表
					sellComnInvSub.setSellInvNum(sellComnInv.getSellInvNum());//将销售专用发票主表编码set给子表中的销售专用发票编码
					sellComnInvSub.setSellSnglNums(entrsAgnStlSub.getStlSnglId());//发货单号
					sellComnInvSub.setSellSnglSubId(entrsAgnStlSub.getOrdrNum());//来源单据子表序号
					
					sellComnInvSubList.add(sellComnInvSub);
				}
				sellComnInvDao.insertSellComnInv(sellComnInv);//新增销售专用发票主表
				sellComnInvSubDao.insertSellComnInvSubList(sellComnInvSubList);//新增销售专用发票子表
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		map.put("isSuccess", isSuccess);
		map.put("message", message);
		return map;
	}

	@Override
	public String uploadFileAddDb(MultipartFile file) {
		String message="";
		Boolean isSuccess=true;
		String resp="";
		Map<String,EntrsAgnStl> entrsAgnStlMap = uploadScoreInfo(file);
		
		for(Map.Entry<String,EntrsAgnStl> entry : entrsAgnStlMap.entrySet()) {
			if(ead.selectStlSnglId(entry.getValue().getStlSnglId())!=null) {
				isSuccess=false;
				message="委托代销结算单中部分订单编号已存在，无法导入，请查明再导入！";
				throw new RuntimeException(message);
			}else {
				ead.insertEntrsAgnStlUpload(entry.getValue());
				for(EntrsAgnStlSub entrsAgnStlSub: entry.getValue().getEntrsAgnStlSub()) {
					entrsAgnStlSub.setStlSnglId(entry.getValue().getStlSnglId());
				}
				easd.insertEntrsAgnStlSub(entry.getValue().getEntrsAgnStlSub());
				isSuccess=true;
				message="委托代销结算单导入成功！";
			}
		}
		try {
			resp=BaseJson.returnRespObj("purc/EntrsAgnStl/uploadEntrsAgnStlFile", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}
	
	//导入excle
	private Map<String, EntrsAgnStl> uploadScoreInfo(MultipartFile file) {
		Map<String, EntrsAgnStl> temp = new HashMap<>();
		int j = 0;
		try {
			InputStream fileIn = file.getInputStream();
			// 根据指定的文件输入流导入Excel从而产生Workbook对象
			Workbook wb0 = new HSSFWorkbook(fileIn);
			// 获取Excel文档中的第一个表单
			Sheet sht0 = wb0.getSheetAt(0);
			// 获得当前sheet的开始行
			int firstRowNum = sht0.getFirstRowNum();
			// 获取文件的最后一行
			int lastRowNum = sht0.getLastRowNum();
			// 设置中文字段和下标映射
			SetColIndex(sht0.getRow(firstRowNum));
			// 对Sheet中的每一行进行迭代
			for (int i = firstRowNum + 1; i <= lastRowNum; i++) {
				j++;
				Row r = sht0.getRow(i);
				// 如果当前行的行号（从0开始）未达到2（第三行）则从新循环
				if (r.getRowNum() < 1) {
					continue;
				}
				String orderNo = GetCellData(r, "结算单号");
				// 创建实体类
				EntrsAgnStl entrsAgnStl = new EntrsAgnStl();
				if (temp.containsKey(orderNo)) {
					entrsAgnStl = temp.get(orderNo);
				}
				// 取出当前行第1个单元格数据，并封装在pursOrdr实体的各个属性上
				entrsAgnStl.setStlSnglId(orderNo);// 采购订单编码
				if (GetCellData(r, "结算日期") == null || GetCellData(r, "结算日期").equals("")) {
					entrsAgnStl.setStlSnglDt(null);
				} else {
					entrsAgnStl.setStlSnglDt(GetCellData(r, "结算日期").replaceAll("[^0-9:-]", " "));// 采购订单日期
				}
				entrsAgnStl.setCustId(GetCellData(r, "客户编码"));// 供应商编码
				entrsAgnStl.setDeptId(GetCellData(r, "部门编码"));// 部门编码
				entrsAgnStl.setAccNum(GetCellData(r, "业务员编码"));// 用户编码
				entrsAgnStl.setUserName(GetCellData(r, "业 务 员"));// 用户名称
				entrsAgnStl.setCustOrdrNum(GetCellData(r, "客户订单号"));;// 供应商订单号
				entrsAgnStl.setSellTypId("2");// 销售类型编码
				entrsAgnStl.setBizTypId("1");//业务类型
				entrsAgnStl.setFormTypEncd("025");// 单据类型编码
				entrsAgnStl.setSellOrdrId(GetCellData(r, "发货单号"));//发货单号
				entrsAgnStl.setSetupPers(GetCellData(r, "制单人"));// 创建人
				if (GetCellData(r, "制单时间") == null || GetCellData(r, "制单时间").equals("")) {
					entrsAgnStl.setSetupTm(null);
				} else {
					entrsAgnStl.setSetupTm(GetCellData(r, "制单时间").replaceAll("[^0-9:-]", " "));// 创建时间
				}
				entrsAgnStl.setIsNtChk(0);// 是否审核
				entrsAgnStl.setIsNtBllg(0);//是否开票
				if (GetCellData(r, "退货标识") != null && GetCellData(r, "退货标识").equals("否")) {
					entrsAgnStl.setIsNtRtnGood(0); //是否退货
				} else if (GetCellData(r, "退货标识") != null && GetCellData(r, "退货标识").equals("是")) {
					entrsAgnStl.setIsNtRtnGood(1); //是否退货
				}
				if (GetCellData(r, "退货标识") != null && GetCellData(r, "退货标识").equals("否")) {
					entrsAgnStl.setFormTypEncd("023");// 单据类型编码
				} else if (GetCellData(r, "退货标识") != null && GetCellData(r, "退货标识").equals("是")) {
					entrsAgnStl.setFormTypEncd("024");// 单据类型编码
				}
				entrsAgnStl.setChkr(GetCellData(r, "审核人"));// 审核人
				if (GetCellData(r, "审核时间") == null || GetCellData(r, "审核时间").equals("")) {
					entrsAgnStl.setChkDt(null);
				} else {
					entrsAgnStl.setChkDt(GetCellData(r, "审核时间").replaceAll("[^0-9:-]", " "));// 审核时间
				}
				entrsAgnStl.setMdfr(GetCellData(r, "修改人")); // 修改人
				if (GetCellData(r, "修改时间") == null || GetCellData(r, "修改时间").equals("")) {
					entrsAgnStl.setModiTm(null);
				} else {
					entrsAgnStl.setModiTm(GetCellData(r, "修改时间").replaceAll("[^0-9:-]", " "));// 修改时间
				}
				entrsAgnStl.setMemo(GetCellData(r, "备 注"));// 备注
				entrsAgnStl.setIsNtMakeVouch(0);//是否生成凭证
				entrsAgnStl.setMakVouchPers(GetCellData(r, "制凭证人"));//制凭证人
				entrsAgnStl.setMakVouchTm(GetCellData(r, "制凭证时间"));//制凭证人
				List<EntrsAgnStlSub> entrsAgnStlSubList = entrsAgnStl.getEntrsAgnStlSub();//委托代销结算单子表
				if (entrsAgnStlSubList == null) {
					entrsAgnStlSubList = new ArrayList<>();
				}
				EntrsAgnStlSub entrsAgnStlSub = new EntrsAgnStlSub();
				entrsAgnStlSub.setOrdrNum(Long.parseLong(GetCellData(r, "序号")));
				entrsAgnStlSub.setInvtyEncd(GetCellData(r, "存货编码"));// 存货编码
				entrsAgnStlSub.setWhsEncd(GetCellData(r, "仓库编号"));// 仓库编码
				entrsAgnStlSub.setQty(GetBigDecimal(GetCellData(r, "数量"), 8));// 8表示小数位数 //数量
				entrsAgnStlSub.setBxQty(GetBigDecimal(GetCellData(r, "箱数"), 8));// 8表示小数位数 //箱数
				entrsAgnStlSub.setCntnTaxUprc(GetBigDecimal(GetCellData(r, "含税单价"), 8));// 8表示小数位数 //含税单价
				entrsAgnStlSub.setNoTaxUprc(GetBigDecimal(GetCellData(r, "无税单价"), 8));// 8表示小数位数 //无税单价
				entrsAgnStlSub.setNoTaxAmt(GetBigDecimal(GetCellData(r, "无税金额"), 8));// 8表示小数位数 //无税金额
				entrsAgnStlSub.setTaxAmt(GetBigDecimal(GetCellData(r, "税额"), 8));// 8表示小数位数 //税额
				entrsAgnStlSub.setPrcTaxSum(GetBigDecimal(GetCellData(r, "价税合计"), 8));// 8表示小数位数 //价税合计
				entrsAgnStlSub.setTaxRate(GetBigDecimal(GetCellData(r, "税率（%）"), 8));// 8表示小数位数 //税率
				entrsAgnStlSub.setUnBllgQty(GetBigDecimal(GetCellData(r, "未开票数量"), 8));// 8表示小数位数 //未开票数量
				
				entrsAgnStlSub.setIntlBat(GetCellData(r, "国际批号")); // 国际批次
				entrsAgnStlSub.setBatNum(GetCellData(r, "批号")); // '批次',
				if (GetCellData(r, "生产日期") == null || GetCellData(r, "生产日期").equals("")) {
					entrsAgnStlSub.setPrdcDt(null);
				} else {
					entrsAgnStlSub.setPrdcDt(GetCellData(r, "生产日期").replaceAll("[^0-9:-]", " "));// 计划到货时间
				}
				if (GetCellData(r, "失效日期") == null || GetCellData(r, "失效日期").equals("")) {
					entrsAgnStlSub.setInvldtnDt(null);
				} else {
					entrsAgnStlSub.setInvldtnDt(GetCellData(r, "失效日期").replaceAll("[^0-9:-]", " "));// 计划到货时间
				}
				if (GetCellData(r, "赠品") != null && GetCellData(r, "赠品").equals("否")) {
					entrsAgnStlSub.setIsComplimentary(0); //是否赠品
				} else if (GetCellData(r, "赠品") != null && GetCellData(r, "赠品").equals("是")) {
					entrsAgnStlSub.setIsComplimentary(1); //是否赠品
				}
				entrsAgnStlSub.setBaoZhiQi(new Double(GetCellData(r, "保质期")).intValue()); // '保质期',
				entrsAgnStlSub.setMemo(GetCellData(r, "表体备注")); //'表体备注',
				entrsAgnStlSub.setSellOrdrIds(GetCellData(r, "发货单号")); //发货单号
				entrsAgnStlSub.setToOrdrNum(Long.parseLong(GetCellData(r, "来源子表序号")));
				entrsAgnStlSubList.add(entrsAgnStlSub);
				entrsAgnStl.setEntrsAgnStlSub(entrsAgnStlSubList);
				temp.put(orderNo, entrsAgnStl);

			}
			fileIn.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("文件的第" + j + "行导入格式有误，无法导入!" + e.getMessage());
		}
		return temp;
	}
	//分页+排序查询委托代销结算单列表
		@Override
		public String queryEntrsAgnStlListOrderBy(Map map) {
			String resp="";
			
			List<?> poList;	
			
			if (map.get("sort") == null||map.get("sort").equals("") ||
				map.get("sortOrder") == null||map.get("sortOrder").equals("")) {
				map.put("sort","eas.stl_sngl_dt");
				map.put("sortOrder","desc");
			}
				poList = ead.selectEntrsAgnStlListOrderBy(map);
				 Map tableSums = ead.selectEntrsAgnStlListSums(map);
					if (null!=tableSums) {
						DecimalFormat df = new DecimalFormat("#,##0.00");
						for(Map.Entry entry : (Set<Entry>)tableSums.entrySet()) {
							String s = df.format((BigDecimal) entry.getValue()==null?new BigDecimal(0.00):entry.getValue());
							entry.setValue(s);
						}
					}
			
		
			int count = ead.selectEntrsAgnStlCount(map);
			
			int pageNo = Integer.parseInt(map.get("pageNo").toString());
			int pageSize = Integer.parseInt(map.get("pageSize").toString());
			int listNum = poList.size();
			int pages = count / pageSize + 1;
			
			try {
				resp=BaseJson.returnRespList("purc/EntrsAgnStl/queryPayApplFormListOrderBy", true, "查询成功！", count,
						pageNo, pageSize,
						listNum, pages,poList,tableSums);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return resp;
		}
	

}
