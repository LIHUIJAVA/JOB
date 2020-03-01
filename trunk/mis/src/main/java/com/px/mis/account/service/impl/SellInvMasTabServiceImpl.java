package com.px.mis.account.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.dao.SellInvMasTabDao;
import com.px.mis.account.dao.SellInvSubTabDao;
import com.px.mis.account.entity.SellComnInv;
import com.px.mis.account.entity.SellInvMasTab;
import com.px.mis.account.entity.SellInvSubTab;
import com.px.mis.account.service.SellInvMasTabService;
import com.px.mis.account.service.SellInvSubTabService;
import com.px.mis.purc.dao.CustDocDao;
import com.px.mis.purc.dao.EntrsAgnStlDao;
import com.px.mis.purc.dao.EntrsAgnStlSubDao;
import com.px.mis.purc.dao.RtnGoodsDao;
import com.px.mis.purc.dao.RtnGoodsSubDao;
import com.px.mis.purc.dao.SellSnglDao;
import com.px.mis.purc.dao.SellSnglSubDao;
import com.px.mis.purc.entity.EntrsAgnStlSub;
import com.px.mis.purc.entity.RtnGoodsSub;
import com.px.mis.purc.entity.SellSnglSub;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;
import com.px.mis.util.poiTool;
//销售发票主表
@Service
@Transactional
public class SellInvMasTabServiceImpl extends poiTool implements SellInvMasTabService {
	@Autowired
	private SellInvMasTabDao sellInvMasTabDao;
//	@Autowired
//	private SellTypeDao sellTypeDao;
//	@Autowired
//	private BizTypeDao bizTypeDao;
//	@Autowired
//	private BizMemDocDao bizMemDocDao;
	@Autowired
	private CustDocDao custDocDao;
	@Autowired
	private SellInvSubTabDao SellInvSubTabDao;
	@Autowired
	private SellInvSubTabService sellInvSubTabService;
	@Autowired
	private SellSnglSubDao sellSnglSubDao;
	@Autowired
	private SellSnglDao sellSnglDao;
	@Autowired
	private RtnGoodsDao rtnGoodsDao;//退货单主表
	@Autowired
	private RtnGoodsSubDao rtnGoodsSubDao;//退货单子表
	@Autowired
	private EntrsAgnStlDao entrsAgnStlDao;//委托代销结算单主表
	@Autowired
	private EntrsAgnStlSubDao entrsAgnStlSubDao;//委托代销结算单子表
	
	@Override
	public ObjectNode insertSellInvMasTab(SellInvMasTab sellInvMasTab,String userId) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sellInvNum = sellInvMasTab.getSellInvNum();
		System.out.println("dfghjklsdfghjkl"+sellInvNum);
		if(sellInvNum==null || sellInvNum.equals("")) {
			on.put("isSuccess", false);
			on.put("message", "新增失败,销售发票号不能为空");
		}else if(sellInvMasTabDao.selectSellInvMasTabBySellInvNum(sellInvNum)!=null){
			on.put("isSuccess", false);
			on.put("message", "主表中销售发票号"+sellInvNum+"已存在，新增失败！");
		}else if(custDocDao.selectCustDocByCustId(sellInvMasTab.getCustId())==null) {
			on.put("isSuccess", false);
			on.put("message", "主表中客户编号"+sellInvMasTab.getCustId()+"不存在，新增失败！");
		}else {
			int insertResult = sellInvMasTabDao.insertSellInvMasTab(sellInvMasTab);
			if(insertResult==1) {
				List<SellInvSubTab> sellSubList = sellInvMasTab.getSellSubList();
				for(SellInvSubTab sellInvSubTab:sellSubList) {
					sellInvSubTab.setSellInvNum(sellInvNum);
					on = sellInvSubTabService.insertSellInvSubTab(sellInvSubTab);
				}
			}else {
				on.put("isSuccess", false);
				on.put("message", "销售发票新增处理失败");
			}
		}
		return on;
	}

	@Override
	public ObjectNode updateSellInvMasTab(SellInvMasTab sellInvMasTab,List<SellInvSubTab> sellInvSubTabList) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}	
		String sellInvNum = sellInvMasTab.getSellInvNum();
		if(sellInvNum==null || sellInvNum.equals("")) {
			on.put("isSuccess", false);
			on.put("message", "更新失败,销售发票号不能为空");
		}else if(sellInvMasTabDao.selectSellInvMasTabBySellInvNum(sellInvNum)==null){
			on.put("isSuccess", false);
			on.put("message", "主表中销售发票号"+sellInvNum+"不存在，更新失败！");
		}else if(custDocDao.selectCustDocByCustId(sellInvMasTab.getCustId())==null) {
			on.put("isSuccess", false);
			on.put("message", "主表中客户编号"+sellInvMasTab.getCustId()+"不存在，更新失败！");
		}else {
			int updateResult = sellInvMasTabDao.updateSellInvMasTabBySellInvNum(sellInvMasTab);
			int deleteResult = sellInvSubTabService.deleteSellInvSubTabBySellInvNumv(sellInvMasTab.getSellInvNum());
			if(updateResult==1 && deleteResult >=1) {
				for(SellInvSubTab sellInvSubTab:sellInvSubTabList) {
					sellInvSubTab.setSellInvNum(sellInvNum);
					SellInvSubTabDao.insertSellInvSubTab(sellInvSubTab);
				}
				on.put("isSuccess", true);
				on.put("message", "销售发票更新处理成功");
			}else {
				on.put("isSuccess", false);
				on.put("message", "销售发票更新处理失败");
			}
		}
		return on;
	}

	@Override
	public ObjectNode deleteSellInvMasTabBySellInvNum(String sellInvNum) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}	
			
		Integer deleteResult = sellInvSubTabService.deleteSellInvSubTabBySellInvNumv(sellInvNum);
		deleteResult = sellInvMasTabDao.deleteSellInvMasTabBySellInvNum(sellInvNum);
		if(deleteResult==1) {
			on.put("isSuccess", true);
			on.put("message", "处理成功");
		}else if(deleteResult==0){
			on.put("isSuccess", true);
			on.put("message", "没有要删除的数据");		
		}else {
			on.put("isSuccess", false);
			on.put("message", "处理失败");
		}
		return on;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public SellInvMasTab selectSellInvMasTabBySellInvNum(String sellInvNum) {
		SellInvMasTab sellInvMasTab = sellInvMasTabDao.selectSellInvMasTabBySellInvNum(sellInvNum);
		return sellInvMasTab;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public String selectSellInvMasTabList(Map map) {
		String resp="";
		List<SellInvMasTab> list = sellInvMasTabDao.selectSellInvMasTabList(map);
		int count = sellInvMasTabDao.selectSellInvMasTabCount(map);
		int listNum=0;
		if(list!=null) {
			listNum=list.size();
		}
		try {
			int pageNo = (int) map.get("pageNo");
			int pageSize = (int) map.get("pageSize");
			int pages = (count+pageSize-1)/pageSize;
			resp=BaseJson.returnRespList("/account/sellInvMasTab/selectSellInvMasTab", true, "查询成功！", count,pageNo,pageSize,listNum,pages, list);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	//批量删除
	@Override
	public String deleteSellInvMasTabList(String sellInvNum) {
		String message="";
		Boolean isSuccess=true;
		String resp="";
		List<String> list = getList(sellInvNum);
		if(sellInvMasTabDao.selectSellInvMasTabIsNtChk(sellInvNum)==0) {
			int a = sellInvMasTabDao.deleteSellInvMasTabList(list);
			if(a>=1) {
			    isSuccess=true;
			    message="删除成功！";
			}else {
				isSuccess=false;
				message="删除失败！";
			}
		}else {
			isSuccess=false;
			message="单据号为："+sellInvNum+"的发票已被审核，无法删除！";
		}
		try {
			resp=BaseJson.returnRespObj("account/sellInvMasTab/deleteSellInvMasTabList", isSuccess, message, null);
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
	
	//发票审核
	@Override
	public String updateSellInvMasTabIsNtChkList(List<SellInvMasTab> sellInvMasTabList) {
		String message="";
		Boolean isSuccess=true;
		String resp="";
		for(SellInvMasTab sellInvMasTab:sellInvMasTabList) {
			String sellSnglNum=sellInvMasTab.getSellSnglNum();//获取对应单据号
			String toFormTypEncd=sellInvMasTab.getToFormTypEncd();//获取对应单据类型
			if(sellInvMasTab.getIsNtChk()==1) {
				if(sellInvMasTabDao.selectSellInvMasTabIsNtChk(sellInvMasTab.getSellInvNum())==0) {
					if(sellSnglNum!=null && sellSnglNum!="") {
						if(toFormTypEncd!=null && toFormTypEncd!="") {
							if(toFormTypEncd.equals("007")) {
								List<String> sellSnglIdList = getSellSnglList(sellSnglNum);//获取销售单编号
								for(String sellSnglId : sellSnglIdList) {	
									sellSnglDao.updateSellSnglIsNtBllgOK(sellSnglId);//修改开票状态
								}
							}else if(toFormTypEncd.equals("008")) {
								List<String> sellSnglIdList = getSellSnglList(sellSnglNum);//获取退货单单编号
								for(String rtnGoodsId : sellSnglIdList) {	
									rtnGoodsDao.updateRtnGoodsIsNtBllgOK(rtnGoodsId);//修改开票状态
								}
							}else if(toFormTypEncd.equals("025")) {
								List<String> sellSnglIdList = getSellSnglList(sellSnglNum);//获取委托代销结算单编号
								for(String stlSnglId : sellSnglIdList) {	
									entrsAgnStlDao.updateEntrsAgnStlIsNtBllgOK(stlSnglId);//修改开票状态
								}
							}
						}else {
							isSuccess=false;
							message +="单据号为"+sellInvMasTab.getSellInvNum()+ "的发票对应业务单据的单据类型为空，无法审核！\n";
						}
						
					}
					//修改审核状态
					int a =sellInvMasTabDao.updateSellInvMasTabIsNtChk(sellInvMasTab);
					if(a>=1) {
					    isSuccess=true;
					    message+="单据号为"+sellInvMasTab.getSellInvNum()+ "的销售专用发票审核成功！\n";
					}else {
						isSuccess=false;
						message +="单据号为"+sellInvMasTab.getSellInvNum()+ "的销售专用发票审核失败！\n";
					}
				}else if(sellInvMasTabDao.selectSellInvMasTabIsNtChk(sellInvMasTab.getSellInvNum())==1){
					isSuccess=false;
					message += "单据号为"+sellInvMasTab.getSellInvNum()+ "的销售专用发票已审核，不需要重复审核\n";
				}
			}else if(sellInvMasTab.getIsNtChk()==0) {
				if(sellInvMasTabDao.selectSellInvMasTabIsNtBookEntry(sellInvMasTab.getSellInvNum())==0) {
					if(sellInvMasTabDao.selectSellInvMasTabIsNtChk(sellInvMasTab.getSellInvNum())==1) {
						if(sellSnglNum!=null && sellSnglNum!="" ) {
							if(toFormTypEncd!=null && toFormTypEncd!="") {
								if(toFormTypEncd.equals("007")) {
									List<String> sellSnglIdList = getSellSnglList(sellSnglNum);//获取销售单编号
									for(String sellSnglId : sellSnglIdList) {	
										sellSnglDao.updateSellSnglIsNtBllgNO(sellSnglId);//修改开票状态
									}
								}else if(toFormTypEncd.equals("008")) {
									List<String> sellSnglIdList = getSellSnglList(sellSnglNum);//获取退货单单编号
									for(String rtnGoodsId : sellSnglIdList) {	
										rtnGoodsDao.updateRtnGoodsIsNtBllgNO(rtnGoodsId);//修改开票状态
									}
								}else if(toFormTypEncd.equals("025")) {
									List<String> sellSnglIdList = getSellSnglList(sellSnglNum);//获取委托代销结算单编号
									for(String stlSnglId : sellSnglIdList) {	
										entrsAgnStlDao.updateEntrsAgnStlIsNtBllgNO(stlSnglId);//修改开票状态
									}
								}
							}else {
								isSuccess=false;
								message +="单据号为"+sellInvMasTab.getSellInvNum()+ "的发票对应业务单据的单据类型为空，无法审核！\n";
							}
						}
						int a = sellInvMasTabDao.updateSellInvMasTabIsNtChk(sellInvMasTab);
						if(a>=1){
						    isSuccess=true;
						    message+="单据号为"+sellInvMasTab.getSellInvNum()+ "的销售专用发票弃审成功！\n";
						}else {
							isSuccess=false;
							message +="单据号为"+sellInvMasTab.getSellInvNum()+ "的销售专用发票弃审失败！\n";
						}
					}else{
						isSuccess=false;
						message += "单据号为"+sellInvMasTab.getSellInvNum()+ "的销售专用发票未审核，请先审核该单据\n";
					}
				}else {
					isSuccess=false;
					message += "单据号为"+sellInvMasTab.getSellInvNum()+ "的销售专用发票已记账，无法弃审！\n";
				}
				
			}
		}
	    try {
			resp=BaseJson.returnRespObj("/account/sellInvMasTab/updateSellInvMasTabIsNtChk", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}
	
	/**
	 * id放入list
	 * 
	 * @param id id(多个已逗号分隔)
	 * @return List集合
	 */
	public List<String> getSellSnglList(String id) {
		List<String> list = new ArrayList<String>();
		String[] str = id.split(",");
		for (int i = 0; i < str.length; i++) {
			list.add(str[i]);
		}
		return list;
	}
	
	//多张销售出库单生成一张销售发票
	@RequestMapping(value="selectSellInvMasTabBingList",method = RequestMethod.POST)
	@ResponseBody
	public String selectSellInvMasTabBingList(List<SellComnInv> sellSnglList) {
		String resp="";
		String message="";
		Boolean isSuccess=true;
		try {
			//实例化一个新的对象    销售普通发票主表
			SellInvMasTab sellInvMasTab = new SellInvMasTab();
			List<SellInvSubTab> sellInvSubTabList = new ArrayList<>();
			Map<String,Object> map  = new HashMap<>();
			//获取List集合中第一个对应，并放入map中
			SellComnInv sellSngl = sellSnglList.get(0);
			//将客户编码，部门编码，业务员编码拼接起来
			String str1 = sellSngl.getCustId()+sellSngl.getDeptId()+sellSngl.getAccNum();
			map.put("uniqInd", str1);
			String sellSnglId="";
			for(SellComnInv sellSng: sellSnglList) {
				sellSnglId+=sellSng.getSellInvNum()+",";
				String str2 = sellSng.getCustId()+sellSng.getDeptId()+sellSng.getAccNum();
				if(str2.equals(map.get("uniqInd").toString())==false) {
					isSuccess=false;
					message="必须选择相同客户、部门、业务员的单据进行生单，请重新选择！";
				}else if(str2.equals(map.get("uniqInd").toString())==true){
					BeanUtils.copyProperties(sellInvMasTab,sellSng);//将销售出库单主表单拷贝给销售发票主表
					sellInvMasTab.setSellSnglNum(sellSnglId.substring(0,sellSnglId.length()-1));
					if(sellInvMasTab.getSellSnglNum()!=null) {
						if(sellSng.getFormTypEncd()==null || sellSng.getFormTypEncd().equals("")) {
							isSuccess=false;
							throw new RuntimeException("单据选择错误,可能由于所参照的单据无单据类型！");
						}else if(sellSng.getFormTypEncd().equals("007")) {//销售单
							//根据主表主键查询相关的子表信息
							List<SellSnglSub> sellSnglSnglList = sellSnglSubDao.selectSellSnglSubBySellSnglId(sellSng.getSellInvNum());
							for(SellSnglSub sellSnglSub:sellSnglSnglList) {
								SellInvSubTab sellInvSubTab = new SellInvSubTab();
								BeanUtils.copyProperties(sellInvSubTab,sellSnglSub);//将销售单子表拷贝给销售发票子表
								sellInvSubTabList.add(sellInvSubTab);
							}
						}else if(sellSng.getFormTypEncd().equals("008")) {//退货单
							//根据主表主键查询相关的子表信息
							List<RtnGoodsSub> rtnGoodsSubList = rtnGoodsSubDao.selectRtnGoodsSubByRtnGoodsId(sellSng.getSellInvNum());
							for(RtnGoodsSub rtnGoodsSub:rtnGoodsSubList) {
								SellInvSubTab sellInvSubTab = new SellInvSubTab();
								BeanUtils.copyProperties(sellInvSubTab,rtnGoodsSub);//将退货单子表拷贝给销售发票子表
								sellInvSubTabList.add(sellInvSubTab);
							}
						}else if(sellSng.getFormTypEncd().equals("025")) {//委托代销结算单
							//根据主表主键查询相关的子表信息
							List<EntrsAgnStlSub> entrsAgnStlSubList = entrsAgnStlSubDao.selectEntrsAgnStlSubByStlSnglId(sellSng.getSellInvNum());
							for(EntrsAgnStlSub entrsAgnStlSub:entrsAgnStlSubList) {
								SellInvSubTab sellInvSubTab = new SellInvSubTab();
								BeanUtils.copyProperties(sellInvSubTab,entrsAgnStlSub);//将委托代销结算单拷贝给销售发票子表
								sellInvSubTabList.add(sellInvSubTab);
							}
						}
					}else {
						isSuccess=false;
						throw new RuntimeException("单据选择错误,可能由于所参照的单据无订单编号！");
					}
					isSuccess=true;
					message="生单成功！";
				}
			}
			sellInvMasTab.setSellSubList(sellInvSubTabList);
			resp=BaseJson.returnRespObj("/account/sellInvMasTab/selectSellInvMasTabBingList", isSuccess, message,sellInvMasTab);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resp;
	}

	//导入
	@Override
	public String uploadFileAddDb(MultipartFile file) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		Map<String, SellInvMasTab> sellInvMasTabMap = uploadScoreInfo(file);

		for (Map.Entry<String, SellInvMasTab> entry : sellInvMasTabMap.entrySet()) {
			SellInvMasTab sellInvMasTab =sellInvMasTabDao.selectSellInvMasTabBySellInvNum(entry.getValue().getSellInvNum());
			if (sellInvMasTab != null) {
				isSuccess = false;
				message = "销售发票中部分单据编号已存在，无法导入，请查明再导入！";
				throw new RuntimeException(message);
			}
			sellInvMasTabDao.insertSellInvMasTabUpload(entry.getValue());
			for (SellInvSubTab sellInvSubTab : entry.getValue().getSellSubList()) {
				sellInvSubTab.setSellInvNum(entry.getValue().getSellInvNum());
				
			}
			SellInvSubTabDao.insertSellInvSubTabList(entry.getValue().getSellSubList());
			isSuccess = true;
			message = "销售发票导入成功！";
		}

		try {
			resp = BaseJson.returnRespObj("purc/sellInvMasTab/uploadSellInvMasTabFile", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// 导入excle
	private Map<String, SellInvMasTab> uploadScoreInfo(MultipartFile file) {
		Map<String, SellInvMasTab> temp = new HashMap<>();
		int j = 0;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
				String orderNo = GetCellData(r, "销售发票编码");
				// 创建实体类
				SellInvMasTab sellInvMasTab = new SellInvMasTab();
				if (temp.containsKey(orderNo)) {
					sellInvMasTab = temp.get(orderNo);
				}
				// 取出当前行第1个单元格数据，并封装在pursOrdr实体的各个属性上
				sellInvMasTab.setSellInvNum(orderNo);//销售发票编码
				if (GetCellData(r, "开票日期") == null || GetCellData(r, "开票日期").equals("")) {
					sellInvMasTab.setBllgDt(null);
				} else {
					sellInvMasTab.setBllgDt(GetCellData(r, "开票日期").replaceAll("[^0-9:-]", " "));//开票日期
				}
				sellInvMasTab.setAccNum(GetCellData(r, "业务员编码")); // 业务员编码', varchar(200
				sellInvMasTab.setUserName(GetCellData(r, "供应商编码")); // '供应商编码', varchar(200
				sellInvMasTab.setBizTypId(GetCellData(r, "业务类型编码"));
				sellInvMasTab.setSellTypId(GetCellData(r, "销售类型编码")); // '销售类型编码', varchar(200
				sellInvMasTab.setDeptId(GetCellData(r, "部门编码")); // '部门编码', varchar(200
				sellInvMasTab.setFormTypEncd(GetCellData(r, "单据类型编码"));// 单据类型编码
				sellInvMasTab.setToFormTypEncd(GetCellData(r, "对应单据类型编码"));//对应单据类型编码
				sellInvMasTab.setIsNtChk(new Double(GetCellData(r, "是否审核")).intValue()); // '是否审核', int(11
				sellInvMasTab.setChkr(GetCellData(r, "审核人")); // '审核人', varchar(200
				if (GetCellData(r, "审核时间") == null || GetCellData(r, "审核时间").equals("")) {
					sellInvMasTab.setChkTm(null);
				} else {
					sellInvMasTab.setChkTm(GetCellData(r, "审核时间").replaceAll("[^0-9:-]", " "));// 审核时间
				}
				
				sellInvMasTab.setIsNtBookEntry(new Double(GetCellData(r,"是否记账")).intValue()); //是否记账
				sellInvMasTab.setBookEntryPers(GetCellData(r,"记账人")); // 记账人',
				if(GetCellData(r,"记账时间")==null || GetCellData(r,"记账时间").equals("")) {
					sellInvMasTab.setBookEntryTm(null); 
				}else {
					sellInvMasTab.setBookEntryTm(GetCellData(r,"记账时间").replaceAll("[^0-9:-]"," "));//记账时间 
				}
				 
				sellInvMasTab.setSetupPers(GetCellData(r, "创建人")); // '创建人', varchar(200
				if (GetCellData(r, "创建时间") == null || GetCellData(r, "创建时间").equals("")) {
					sellInvMasTab.setSetupTm(null);
				} else {
					sellInvMasTab.setSetupTm(GetCellData(r, "创建时间").replaceAll("[^0-9:-]", " "));// 记账时间
				}
				sellInvMasTab.setMdfr(GetCellData(r, "修改人")); // '修改人', varchar(200
				if (GetCellData(r, "修改时间") == null || GetCellData(r, "修改时间").equals("")) {
					sellInvMasTab.setModiTm(null);
				} else {
					sellInvMasTab.setModiTm(GetCellData(r, "修改时间").replaceAll("[^0-9:-]", " "));// 记账时间
				}
				sellInvMasTab.setSellSnglNum(GetCellData(r, "来源单据号"));
				sellInvMasTab.setMemo(GetCellData(r, "主备注")); // '备注', varchar(2000
				List<SellInvSubTab> sellInvSubTabList = sellInvMasTab.getSellSubList();//销售发票子表
				if (sellInvSubTabList == null) {
					sellInvSubTabList = new ArrayList<>();
				}
				SellInvSubTab sellInvSubTab = new SellInvSubTab();

				sellInvSubTab.setWhsEncd(GetCellData(r, "仓库编码")); // '仓库编码',
				sellInvSubTab.setInvtyEncd(GetCellData(r, "存货编码")); // '存货编码',
				sellInvSubTab.setQty(GetBigDecimal(GetCellData(r, "数量"), 8)); // '数量',
				sellInvSubTab.setBxQty(GetBigDecimal(GetCellData(r, "箱数"), 8)); // '箱数',
				sellInvSubTab.setBxRule(GetBigDecimal(GetCellData(r, "箱规"), 8));//箱规
				sellInvSubTab.setCntnTaxUprc(GetBigDecimal(GetCellData(r, "含税单价"), 8)); // '含税单价',
				sellInvSubTab.setPrcTaxSum(GetBigDecimal(GetCellData(r, "价税合计"), 8)); // '价税合计',
				sellInvSubTab.setNoTaxUprc(GetBigDecimal(GetCellData(r, "无税单价"), 8)); // '无税单价',
				sellInvSubTab.setNoTaxAmt(GetBigDecimal(GetCellData(r, "无税金额"), 8)); // '无税金额',
				sellInvSubTab.setTaxAmt(GetBigDecimal(GetCellData(r, "税额"), 8)); // '税额',
				sellInvSubTab.setTaxRate(GetBigDecimal(GetCellData(r, "税率"), 8)); // '税率',
				sellInvSubTab.setIntlBat(GetCellData(r, "国际批号")); // 国际批号
				sellInvSubTab.setBatNum(GetCellData(r, "批号")); // '批号',
				sellInvSubTab.setMemo(GetCellData(r, "子备注")); // '备注',
				sellInvSubTabList.add(sellInvSubTab);//子表
				sellInvMasTab.setSellSubList(sellInvSubTabList);;
				temp.put(orderNo, sellInvMasTab);
			}
			fileIn.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("文件的第" + j + "行导入格式有误，无法导入!" + e.getMessage());
		}
		return temp;
	}

	@Override
	public String upLoadSellInvMasTabList(Map map) {
		String resp = "";
		List<SellInvMasTab> poList = sellInvMasTabDao.printingSellInvMasTabList(map);
		try {
			resp = BaseJson.returnRespObjList("purc/sellInvMasTab/printingSellInvMasTabList", true, "查询成功！", null, poList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
	}

}
