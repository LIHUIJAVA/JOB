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
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.dao.PursInvMasTabDao;
import com.px.mis.account.dao.PursInvSubTabDao;
import com.px.mis.account.entity.PursInvMasTab;
import com.px.mis.account.entity.PursInvSubTab;
import com.px.mis.account.service.PursInvMasTabService;
import com.px.mis.account.service.PursInvSubTabService;
import com.px.mis.purc.dao.IntoWhsDao;
import com.px.mis.purc.dao.IntoWhsSubDao;
import com.px.mis.purc.dao.ProvrDocDao;
import com.px.mis.purc.entity.IntoWhs;
import com.px.mis.purc.entity.IntoWhsSub;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;
import com.px.mis.util.poiTool;
//采购专用发票主表
@Service
@Transactional
public class PursInvMasTabServiceImpl extends poiTool implements PursInvMasTabService {
	
	@Autowired
	private PursInvMasTabDao pursInvMasTabDao;
	@Autowired
	private PursInvSubTabDao pursInvSubTabDao;
	@Autowired
	private IntoWhsSubDao intoWhsSubDao;//采购入库单子表
	@Autowired
	private IntoWhsDao intoWhsDao;//采购入库单子表
	@Autowired
	private ProvrDocDao provrDocDao;
	@Autowired
	private PursInvSubTabService pursInvSubTabService;
	
	//新增采购发票
	@Override
	public ObjectNode insertPursInvMasTab(PursInvMasTab pursInvMasTab) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		String pursInvNum = pursInvMasTab.getPursInvNum();
		if(pursInvNum==null || pursInvNum.equals("")) {
			on.put("isSuccess", false);
			on.put("message", "新增失败,采购发票号不能为空");
		}else if(pursInvMasTabDao.selectPursInvMasTabById(pursInvNum)!=null){
			on.put("isSuccess", false);
			on.put("message", "采购发票号"+pursInvNum+"已存在，新增失败！");
		}else if(provrDocDao.selectProvrDocByProvrId(pursInvMasTab.getProvrId())==null) {
			on.put("isSuccess", false);
			on.put("message", "该供应商"+pursInvMasTab.getProvrId()+"不存在，新增失败！");
		}else {
			int InsertResult = pursInvMasTabDao.insertPursInvMasTab(pursInvMasTab);
			if(InsertResult==1) {
				List<PursInvSubTab> pursList = pursInvMasTab.getPursList();
				for(PursInvSubTab pursInvSubTab:pursList) {
					pursInvSubTab.setPursInvNum(pursInvNum);
					on = pursInvSubTabService.insertPursInvSubTab(pursInvSubTab);
				}
			}else {
				on.put("isSuccess", false);
				on.put("message", "采购发票新增失败");
			}
		}
		return on;
	}

	//修改采购发票
	@Override
	public ObjectNode updatePursInvMasTabById(PursInvMasTab pursInvMasTab,List<PursInvSubTab> pursInvSubTabList) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		String pursInvNum = pursInvMasTab.getPursInvNum();
		if(pursInvNum==null || pursInvNum.equals("")) {
			on.put("isSuccess", false);
			on.put("message", "更新失败,采购发票号不能为空");
		}else if(pursInvMasTabDao.selectPursInvMasTabById(pursInvNum)==null){
			on.put("isSuccess", false);
			on.put("message", "主表中采购发票号"+pursInvNum+"不存在，更新失败！");
		}else if(provrDocDao.selectProvrDocByProvrId(pursInvMasTab.getProvrId())==null) {
			on.put("isSuccess", false);
			on.put("message", "主表中供应商编号"+pursInvMasTab.getProvrId()+"不存在，更新失败！");
		}else {
			int updateResult = pursInvMasTabDao.updatePursInvMasTabById(pursInvMasTab);
			int deleteResult = pursInvSubTabService.deletePursInvSubTabByPursInvNum(pursInvMasTab.getPursInvNum());
			if(updateResult==1 && deleteResult >=1) {
				for(PursInvSubTab pursInvSubTab:pursInvSubTabList) {
					pursInvSubTab.setPursInvNum(pursInvNum);
					pursInvSubTabDao.insertPursInvSubTab(pursInvSubTab);
				}
				on.put("isSuccess", true);
				on.put("message", "采购发票更新成功");
			}else {
				on.put("isSuccess", false);
				on.put("message", "采购发票更新失败");
			}
		}
			
		return on;
	}

	//单个删除采购发票
	@Override
	public ObjectNode deletePursInvMasTabByPursInvNum(String pursInvNum) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}	
		Integer deleteResult = pursInvSubTabService.deletePursInvSubTabByPursInvNum(pursInvNum);
		deleteResult = pursInvMasTabDao.deletePursInvMasTabById(pursInvNum);
		if(deleteResult==1) {
			on.put("isSuccess", true);
			on.put("message", "删除成功");
		}else if(deleteResult==0){
			on.put("isSuccess", true);
			on.put("message", "没有要删除的数据");		
		}else {
			on.put("isSuccess", false);
			on.put("message", "删除失败");
		}
			
		return on;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public PursInvMasTab selectPursInvMasTabByPursInvNum(String pursInvNum) {
		PursInvMasTab pursInvMasTab = pursInvMasTabDao.selectPursInvMasTabById(pursInvNum);
		return pursInvMasTab;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public String selectPursInvMasTabList(Map map) {
		String resp="";
		List<PursInvMasTab> list = pursInvMasTabDao.selectPursInvMasTabList(map);
		int count = pursInvMasTabDao.selectPursInvMasTabCount(map);
		int listNum=0;
		if(list!=null) {
			listNum=list.size();
		}
		try {
			int pageNo = (int) map.get("pageNo");
			int pageSize = (int) map.get("pageSize");
			int pages = (count+pageSize-1)/pageSize;
			resp=BaseJson.returnRespList("/account/pursInvMasTab/selectPursInvMasTab", true, "查询成功！", count,pageNo,pageSize,listNum,pages, list);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	//批量删除采购发票
	@Override
	public String deletePursInvMasTabList(String pursInvNum) {
		String message="";
		Boolean isSuccess=true;
		String resp="";
		List<String> list = getList(pursInvNum);
		if(pursInvMasTabDao.selectPursInvMasTabIsNtChk(pursInvNum)==0) {
			int a = pursInvMasTabDao.deletePursInvMasTabList(list);
			if(a>=1) {
			    isSuccess=true;
			    message="删除成功！";
			}else {
				isSuccess=false;
				message="删除失败！";
			}
		}else {
			isSuccess=false;
			message="单据号为："+pursInvNum+"的发票已被审核，无法删除！";
		}		
		try {
			resp=BaseJson.returnRespObj("/account/pursInvMasTab/deletePursInvMasTabList", isSuccess, message, null);
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
	
	@Override
	public String updatePursInvMasTabIsNtChkList(List<PursInvMasTab> pursInvMasTabList) {
		String message="";
		Boolean isSuccess=true;
		String resp="";
		for(PursInvMasTab pursInvMasTab:pursInvMasTabList) {
			if(pursInvMasTab.getIsNtChk()==1) {
				if(pursInvMasTabDao.selectPursInvMasTabIsNtChk(pursInvMasTab.getPursInvNum())==0) {
					//查询采购入库单号
					String intoWhsSnglId = pursInvMasTabDao.selectIntoWhsSnglIdByPursInvMasTab(pursInvMasTab.getPursInvNum());
					if(intoWhsSnglId!=null) {
						List<String> intoWhsIdList = getintoWhsList(intoWhsSnglId);//获取入库单编号
						for(String intoWhsId : intoWhsIdList) {
							//修改是否开票状态
							intoWhsDao.updateIntoWhsIsNtBllgOK(intoWhsId);
						}
					}
					//修改审核状态
					int a = pursInvMasTabDao.updatePursInvMasTabIsNtChk(pursInvMasTab);
					if(a>=1) {
					    isSuccess=true;
					    message+="单据号为"+pursInvMasTab.getPursInvNum()+ "的采购专用发票审核成功！\n";
					}else {
						isSuccess=false;
						message +="单据号为"+pursInvMasTab.getPursInvNum()+ "的采购专用发票审核失败！\n";
					}
				}else if(pursInvMasTabDao.selectPursInvMasTabIsNtChk(pursInvMasTab.getPursInvNum())==1){
					isSuccess=false;
					message += "单据号为"+pursInvMasTab.getPursInvNum()+ "的采购专用发票已审核，不需要重复审核\n";
				}
			}else if(pursInvMasTab.getIsNtChk()==0) {
				if(pursInvMasTabDao.selectPursInvMasTabIsNtBookEntry(pursInvMasTab.getPursInvNum())==0){
					if(pursInvMasTabDao.selectPursInvMasTabIsNtChk(pursInvMasTab.getPursInvNum())==1) {
						//查询采购入库单号
						String intoWhsSnglId = pursInvMasTabDao.selectIntoWhsSnglIdByPursInvMasTab(pursInvMasTab.getPursInvNum());
						if(intoWhsSnglId != null) {
							List<String> intoWhsIdList = getintoWhsList(intoWhsSnglId);//获取入库单编号
							for(String intoWhsId : intoWhsIdList) {
								//修改是否开票状态
								intoWhsDao.updateIntoWhsIsNtBllgNO(intoWhsId);
							}
						}
						int a = pursInvMasTabDao.updatePursInvMasTabIsNtChk(pursInvMasTab);
						if(a>=1){
						    isSuccess=true;
						    message+="单据号为"+pursInvMasTab.getPursInvNum()+ "的采购专用发票弃审成功！\n";
						}else {
							isSuccess=false;
							message +="单据号为"+pursInvMasTab.getPursInvNum()+ "的采购专用发票弃审失败！\n";
						}
					}else{
						isSuccess=false;
						message += "单据号为"+pursInvMasTab.getPursInvNum()+ "的采购专用发票未审核，请先审核该单据\n";
					}
				}else {
					isSuccess=false;
					message += "单据号为"+pursInvMasTab.getPursInvNum()+ "的采购专用发票已记账无法弃审\n";
				}
				
			}
		}
	    try {
			resp=BaseJson.returnRespObj("/account/pursInvMasTab/updatePursInvMasTabIsNtChk", isSuccess, message, null);
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
	public List<String> getintoWhsList(String id) {
		List<String> list = new ArrayList<String>();
		String[] str = id.split(",");
		for (int i = 0; i < str.length; i++) {
			list.add(str[i]);
		}
		return list;
	}

	//合并采购入库单并入采购发票中
	@Override
	public String selectPursInvMasTabBingList(List<IntoWhs> intoWhsList) {
		String resp="";
		String message="";
		Boolean isSuccess=true;
		//实例化一个新的对象    采购普通发票主表
		PursInvMasTab pursInvMasTab = new PursInvMasTab();
		List<PursInvSubTab> pursInvSubTabList = new ArrayList<>();
		Map<String,Object> map  = new HashMap<>();
		//获取List集合中第一个对应，并放入map中
		IntoWhs intoWhs = intoWhsList.get(0);
		//将供应商编码，部门编码，业务员编码拼接起来
		String str1 = intoWhs.getProvrId()+intoWhs.getDeptId()+intoWhs.getAccNum();
		map.put("uniqInd", str1);
		String intoWhsId="";
		for(IntoWhs intoWh: intoWhsList) {
			intoWhsId+=intoWh.getIntoWhsSnglId()+",";
			String str2 = intoWh.getProvrId()+intoWh.getDeptId()+intoWh.getAccNum();
			if(str2.equals(map.get("uniqInd").toString())==false) {
				isSuccess=false;
				message="必须选择相同供应商、部门、业务员的单据进行生单，请重新选择！";
			}else if(str2.equals(map.get("uniqInd").toString())==true){
				try {
					BeanUtils.copyProperties(pursInvMasTab,intoWh);//将采购入库主表单拷贝给采购专用发票主表
					pursInvMasTab.setCrspdIntoWhsSnglNum(intoWhsId.substring(0,intoWhsId.length()-1));
					//根据主表主键查询相关的子表信息
					List<IntoWhsSub> intoWhsSubList = intoWhsSubDao.selectIntoWhsSubByIntoWhsSnglId(intoWh.getIntoWhsSnglId());
					
					for(IntoWhsSub intoWhsSub:intoWhsSubList) {
						PursInvSubTab pursInvSubTab = new PursInvSubTab();
						BeanUtils.copyProperties(pursInvSubTab,intoWhsSub);//将采购入库单子表拷贝给采购专用发票子表
						pursInvSubTabList.add(pursInvSubTab);
					}
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					throw new RuntimeException("查询数据有误，请检查再进行生单！");
				}
				
				isSuccess=true;
				message="生单成功！";
			}
			
		}
		pursInvMasTab.setPursList(pursInvSubTabList);
		try {
			resp=BaseJson.returnRespObj("/account/pursInvMasTab/selectPursInvMasTabBingList", isSuccess, message,pursInvMasTab);
		} catch (IOException e) {
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
		Map<String, PursInvMasTab> pursInvMasTabMap = uploadScoreInfo(file);

		for (Map.Entry<String, PursInvMasTab> entry : pursInvMasTabMap.entrySet()) {
			PursInvMasTab pursInvMasTab =pursInvMasTabDao.selectPursInvMasTabById(entry.getValue().getPursInvNum());
			if (pursInvMasTab != null) {
				isSuccess = false;
				message = "采购发票中部分单据编号已存在，无法导入，请查明再导入！";
				throw new RuntimeException(message);
			}
			pursInvMasTabDao.insertPursInvMasTabUpload(entry.getValue());
			for (PursInvSubTab pursInvSubTab : entry.getValue().getPursList()) {
				pursInvSubTab.setPursInvNum(entry.getValue().getPursInvNum());
				
			}
			pursInvSubTabDao.insertPursInvSubTabUpload(entry.getValue().getPursList());
			isSuccess = true;
			message = "采购发票导入成功！";
		}

		try {
			resp = BaseJson.returnRespObj("purc/pursInvMasTab/uploadPursInvMasTabFile", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// 导入excle
	private Map<String, PursInvMasTab> uploadScoreInfo(MultipartFile file) {
		Map<String, PursInvMasTab> temp = new HashMap<>();
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
				String orderNo = GetCellData(r, "采购发票编码");
				// 创建实体类
				PursInvMasTab pursInvMasTab = new PursInvMasTab();
				if (temp.containsKey(orderNo)) {
					pursInvMasTab = temp.get(orderNo);
				}
				// 取出当前行第1个单元格数据，并封装在pursOrdr实体的各个属性上
				pursInvMasTab.setPursInvNum(orderNo);//采购发票编码
				if (GetCellData(r, "开票日期") == null || GetCellData(r, "开票日期").equals("")) {
					pursInvMasTab.setBllgDt(null);
				} else {
					pursInvMasTab.setBllgDt(GetCellData(r, "开票日期").replaceAll("[^0-9:-]", " "));//开票日期
				}
				pursInvMasTab.setAccNum(GetCellData(r, "业务员编码")); // 业务员编码', varchar(200
				pursInvMasTab.setUserName(GetCellData(r, "供应商编码")); // '供应商编码', varchar(200
				pursInvMasTab.setPursTypId(GetCellData(r, "采购类型编码")); // '采购类型编码', varchar(200
				pursInvMasTab.setDeptId(GetCellData(r, "部门编码")); // '部门编码', varchar(200
				pursInvMasTab.setFormTypEncd(GetCellData(r, "单据类型编码"));// 单据类型编码

				pursInvMasTab.setIsNtChk(new Double(GetCellData(r, "是否审核")).intValue()); // '是否审核', int(11
				pursInvMasTab.setChkr(GetCellData(r, "审核人")); // '审核人', varchar(200
				if (GetCellData(r, "审核时间") == null || GetCellData(r, "审核时间").equals("")) {
					pursInvMasTab.setChkTm(null);
				} else {
					pursInvMasTab.setChkTm(GetCellData(r, "审核时间").replaceAll("[^0-9:-]", " "));// 审核时间
				}
				
				pursInvMasTab.setIsNtBookEntry(new Double(GetCellData(r,"是否记账")).intValue()); //是否记账
				pursInvMasTab.setBookEntryPers(GetCellData(r,"记账人")); // 记账人',
				if(GetCellData(r,"记账时间")==null || GetCellData(r,"记账时间").equals("")) {
					pursInvMasTab.setBookEntryTm(null); 
				}else {
					pursInvMasTab.setBookEntryTm(GetCellData(r,"记账时间").replaceAll("[^0-9:-]"," "));//记账时间 
				}
				 
				pursInvMasTab.setSetupPers(GetCellData(r, "创建人")); // '创建人', varchar(200
				if (GetCellData(r, "创建时间") == null || GetCellData(r, "创建时间").equals("")) {
					pursInvMasTab.setSetupTm(null);
				} else {
					pursInvMasTab.setSetupTm(GetCellData(r, "创建时间").replaceAll("[^0-9:-]", " "));// 记账时间
				}
				pursInvMasTab.setMdfr(GetCellData(r, "修改人")); // '修改人', varchar(200
				if (GetCellData(r, "修改时间") == null || GetCellData(r, "修改时间").equals("")) {
					pursInvMasTab.setModiTm(null);
				} else {
					pursInvMasTab.setModiTm(GetCellData(r, "修改时间").replaceAll("[^0-9:-]", " "));// 记账时间
				}
//				pursInvMasTab.setPursOrdrId(GetCellData(r, "采购订单编码"));
				pursInvMasTab.setCrspdIntoWhsSnglNum(GetCellData(r, "采购入库单编码"));
				pursInvMasTab.setMemo(GetCellData(r, "主备注")); // '备注', varchar(2000
				List<PursInvSubTab> pursInvSubTabList = pursInvMasTab.getPursList();//采购发票子表
				if (pursInvSubTabList == null) {
					pursInvSubTabList = new ArrayList<>();
				}
				PursInvSubTab pursInvSubTab = new PursInvSubTab();

				pursInvSubTab.setWhsEncd(GetCellData(r, "仓库编码")); // '仓库编码',
				pursInvSubTab.setInvtyEncd(GetCellData(r, "存货编码")); // '存货编码',
				pursInvSubTab.setQty(GetBigDecimal(GetCellData(r, "数量"), 8)); // '数量',
				pursInvSubTab.setBxQty(GetBigDecimal(GetCellData(r, "箱数"), 8)); // '箱数',
				pursInvSubTab.setBxRule(GetBigDecimal(GetCellData(r, "箱规"), 8));//箱规
				pursInvSubTab.setCntnTaxUprc(GetBigDecimal(GetCellData(r, "含税单价"), 8)); // '含税单价',
				pursInvSubTab.setPrcTaxSum(GetBigDecimal(GetCellData(r, "价税合计"), 8)); // '价税合计',
				pursInvSubTab.setNoTaxUprc(GetBigDecimal(GetCellData(r, "无税单价"), 8)); // '无税单价',
				pursInvSubTab.setNoTaxAmt(GetBigDecimal(GetCellData(r, "无税金额"), 8)); // '无税金额',
				pursInvSubTab.setTaxAmt(GetBigDecimal(GetCellData(r, "税额"), 8)); // '税额',
				pursInvSubTab.setTaxRate(GetBigDecimal(GetCellData(r, "税率"), 8)); // '税率',
				pursInvSubTab.setIntlBat(GetCellData(r, "国际批次")); // 国际批号
				pursInvSubTab.setBatNum(GetCellData(r, "批次")); // '批号',
				pursInvSubTab.setMemo(GetCellData(r, "子备注")); // '备注',
				pursInvSubTabList.add(pursInvSubTab);//子表
				pursInvMasTab.setPursList(pursInvSubTabList);
				temp.put(orderNo, pursInvMasTab);
			}
			fileIn.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("文件的第" + j + "行导入格式有误，无法导入!" + e.getMessage());
		}
		return temp;
	}

	@Override
	public String upLoadPursInvMasTabList(Map map) {
		String resp = "";
		List<PursInvMasTab> poList = pursInvMasTabDao.printingPursInvMasTabList(map);
		try {
			resp = BaseJson.returnRespObjList("purc/pursInvMasTab/printingPursInvMasTabList", true, "查询成功！", null, poList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
	}


}
