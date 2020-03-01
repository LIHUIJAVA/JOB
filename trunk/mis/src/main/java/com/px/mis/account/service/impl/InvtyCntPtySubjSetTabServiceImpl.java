package com.px.mis.account.service.impl;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.dao.AcctItmDocDao;
import com.px.mis.account.dao.InvtyCntPtySubjSetTabDao;
import com.px.mis.account.entity.InvtyCntPtySubjSetTab;
import com.px.mis.account.service.InvtyCntPtySubjSetTabService;
import com.px.mis.purc.dao.DeptDocDao;
import com.px.mis.purc.dao.InvtyClsDao;
import com.px.mis.purc.dao.InvtyDocDao;
import com.px.mis.purc.dao.RecvSendCateDao;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;
import com.px.mis.util.poiTool;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//存货对方科目设置表
@Service
@Transactional
public class InvtyCntPtySubjSetTabServiceImpl extends poiTool implements InvtyCntPtySubjSetTabService {
	@Autowired
	private InvtyCntPtySubjSetTabDao invtyCntPtSubjSetTabDao;
	@Autowired
	private InvtyClsDao invtyClsDao;
	@Autowired
	private InvtyDocDao invtyDocDao;
	@Autowired
	private DeptDocDao deptDocDao;
//	private BizMemDocDao bizMemDoc;
	@Autowired
	private RecvSendCateDao recvSendCateDao;
	@Autowired
	private AcctItmDocDao acctItmDocDao;

	@Override
	public ObjectNode insertInvtyCntPtySubjSetTab(InvtyCntPtySubjSetTab invtyCntPtySubjSetTab) {
		ObjectNode on = null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*
		 * if(invtyDocDao.selectInvtyDocByInvtyDocEncd(invtyCntPtySubjSetTab.
		 * getInvtyEncd())==null){ on.put("isSuccess", false); on.put("message",
		 * "存货编码"+invtyCntPtySubjSetTab.getInvtyEncd()+"不存在，新增失败！"); }else
		 */
		if (invtyClsDao.selectInvtyClsByInvtyClsEncd(invtyCntPtySubjSetTab.getInvtyBigClsEncd()) == null) {
			on.put("isSuccess", false);
			on.put("message", "存货大类编码" + invtyCntPtySubjSetTab.getInvtyBigClsEncd() + "不存在，新增失败！");
		} else/*
				 * if(deptDocDao.selectDeptDocByDeptEncd(invtyCntPtySubjSetTab.getDeptId())==
				 * null){ on.put("isSuccess", false); on.put("message",
				 * "部门编号"+invtyCntPtySubjSetTab.getDeptId()+"不存在，新增失败！"); }else
				 */if (recvSendCateDao
				.selectRecvSendCateByRecvSendCateId(invtyCntPtySubjSetTab.getRecvSendCateId()) == null) {
			on.put("isSuccess", false);
			on.put("message", "收发类别编号" + invtyCntPtySubjSetTab.getRecvSendCateId() + "不存在，新增失败！");
		} else if (acctItmDocDao.selectAcctItmDocBySubjId(invtyCntPtySubjSetTab.getCntPtySubjId()) == null) {
			on.put("isSuccess", false);
			on.put("message", "对方科目编码" + invtyCntPtySubjSetTab.getCntPtySubjId() + "不存在，新增失败！");
		} else if (acctItmDocDao.selectAcctItmDocBySubjId(invtyCntPtySubjSetTab.getTeesSubjEncd()) == null) {
			on.put("isSuccess", false);
			on.put("message", "暂估科目编码" + invtyCntPtySubjSetTab.getTeesSubjEncd() + "不存在，新增失败！");
		} else {
			int insertResult = invtyCntPtSubjSetTabDao.insertInvtyCntPtySubjSetTab(invtyCntPtySubjSetTab);
			if (insertResult == 1) {
				on.put("isSuccess", true);
				on.put("message", "新增成功");
			} else {
				on.put("isSuccess", false);
				on.put("message", "新增失败");
			}
		}
		return on;
	}

	@Override
	public ObjectNode updateInvtyCntPtySubjSetTabByOrdrNum(List<InvtyCntPtySubjSetTab> invtyCntPtySubjSetTabList) {
		ObjectNode on = null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (InvtyCntPtySubjSetTab invtyCntPtySubjSetTab : invtyCntPtySubjSetTabList) {
			Integer ordrNum = invtyCntPtySubjSetTab.getOrdrNum();
			if (ordrNum == null) {
				on.put("isSuccess", false);
				on.put("message", "更新失败,主键不能为空");
			} else if (invtyCntPtSubjSetTabDao
					.selectInvtyCntPtySubjSetTabByOrdrNum(invtyCntPtySubjSetTab.getOrdrNum()) == null) {
				on.put("isSuccess", false);
				on.put("message", "更新失败，序号" + invtyCntPtySubjSetTab.getOrdrNum() + "不存在！");
			} else if (invtyClsDao.selectInvtyClsByInvtyClsEncd(invtyCntPtySubjSetTab.getInvtyBigClsEncd()) == null) {
				on.put("isSuccess", false);
				on.put("message", "存货大类编码" + invtyCntPtySubjSetTab.getInvtyBigClsEncd() + "不存在，更新失败！");
			} else if (recvSendCateDao
					.selectRecvSendCateByRecvSendCateId(invtyCntPtySubjSetTab.getRecvSendCateId()) == null) {
				on.put("isSuccess", false);
				on.put("message", "收发类别编号" + invtyCntPtySubjSetTab.getRecvSendCateId() + "不存在，更新失败！");
			} else if (acctItmDocDao.selectAcctItmDocBySubjId(invtyCntPtySubjSetTab.getCntPtySubjId()) == null) {
				on.put("isSuccess", false);
				on.put("message", "对方科目编码" + invtyCntPtySubjSetTab.getCntPtySubjId() + "不存在，更新失败！");
			} else if (acctItmDocDao.selectAcctItmDocBySubjId(invtyCntPtySubjSetTab.getTeesSubjEncd()) == null) {
				on.put("isSuccess", false);
				on.put("message", "暂估科目编码" + invtyCntPtySubjSetTab.getTeesSubjEncd() + "不存在，更新失败！");
			} else {
				int updateResult = invtyCntPtSubjSetTabDao.updateInvtyCntPtySubjSetTabByOrdrNum(invtyCntPtySubjSetTab);
				if (updateResult == 1) {
					on.put("isSuccess", true);
					on.put("message", "更新成功");
				} else {
					on.put("isSuccess", false);
					on.put("message", "更新失败");
				}
			}
		}
		return on;
	}

	@Override
	public ObjectNode deleteInvtyCntPtySubjSetTabByOrdrNum(Integer ordrNum) {
		ObjectNode on = null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (invtyCntPtSubjSetTabDao.selectInvtyCntPtySubjSetTabByOrdrNum(ordrNum) == null) {
			on.put("isSuccess", false);
			on.put("message", "删除失败，编号" + ordrNum + "不存在！");
		} else {
			Integer deleteResult = invtyCntPtSubjSetTabDao.deleteInvtyCntPtySubjSetTabByOrdrNum(ordrNum);
			if (deleteResult == 1) {
				on.put("isSuccess", true);
				on.put("message", "删除成功");
			} else if (deleteResult == 0) {
				on.put("isSuccess", true);
				on.put("message", "没有要删除的数据");
			} else {
				on.put("isSuccess", false);
				on.put("message", "删除失败");
			}
		}
		return on;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public InvtyCntPtySubjSetTab selectInvtyCntPtySubjSetTabByOrdrNum(Integer ordrNum) {
		InvtyCntPtySubjSetTab selectByOrdrNum = invtyCntPtSubjSetTabDao.selectInvtyCntPtySubjSetTabByOrdrNum(ordrNum);
		return selectByOrdrNum;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public String selectInvtyCntPtySubjSetTabList(Map map) {
		String resp = "";
		List<InvtyCntPtySubjSetTab> list = invtyCntPtSubjSetTabDao.selectInvtyCntPtySubjSetTabList(map);
		int count = invtyCntPtSubjSetTabDao.selectInvtyCntPtySubjSetTabCount();
		int listNum = 0;
		if (list != null) {
			listNum = list.size();
		}
		try {
			int pageNo = (int) map.get("pageNo");
			int pageSize = (int) map.get("pageSize");
			int pages = (count + pageSize - 1) / pageSize;
			resp = BaseJson.returnRespList("/account/invtyCntPtySubjSetTab/selectInvtyCntPtySubjSetTab", true, "查询成功！",
					count, pageNo, pageSize, listNum, pages, list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resp;
	}

	// 批量删除
	@Override
	public String deleteInvtyCntPtySubjSetTabList(String ordrNum) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			Map map = new HashMap<>();
			map.put("ordrNum", ordrNum);
			int a = invtyCntPtSubjSetTabDao.deleteInvtyCntPtySubjSetTabList(map);
			if (a >= 1) {
				isSuccess = true;
				message = "删除成功！";
			} else {
				isSuccess = false;
				message = "删除失败！";
			}

			resp = BaseJson.returnRespObj("account/invtyCntPtySubjSetTab/deleteInvtyCntPtySubjSetTabList", isSuccess,
					message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	@Override
	public String selectInvtyCntPtySubjSetTabListPrint(Map map) {
		String resp = "";
		List<InvtyCntPtySubjSetTab> list = invtyCntPtSubjSetTabDao.selectInvtyCntPtySubjSetTabList(map);

		try {
			resp = BaseJson.returnRespListAnno("/account/invtyCntPtySubjSetTab/selectInvtyCntPtySubjSetTabPrint", true,
					"查询成功！", list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resp;
	}

	@Override
	public String uploadFileAddDb(MultipartFile file) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		Map<String, InvtyCntPtySubjSetTab> pusOrderMap = uploadScoreInfo(file);
		System.out.println(pusOrderMap.size());
		for (Map.Entry<String, InvtyCntPtySubjSetTab> entry : pusOrderMap.entrySet()) {
			InvtyCntPtySubjSetTab selectByOrdrNum = invtyCntPtSubjSetTabDao
					.selectInvtyCntPtySubjSetTabByOrdrNum(entry.getValue().getOrdrNum());

			if (selectByOrdrNum != null) {
				throw new RuntimeException("插入重复单号 " + entry.getValue().getOrdrNum() + " 请检查");
			}

			try {
				invtyCntPtSubjSetTabDao.insertInvtyCntPtySubjSetTab(entry.getValue());

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				throw new RuntimeException("插入sql问题");

			}
		}

		isSuccess = true;
		message = "档案新增成功！";
		try {
			resp = BaseJson.returnRespObj("account/invtyCntPtySubjSetTab/uploadFileAddDb", isSuccess, message, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return resp;

	}

	// 导入excle
	private Map<String, InvtyCntPtySubjSetTab> uploadScoreInfo(MultipartFile file) {
		Map<String, InvtyCntPtySubjSetTab> temp = new HashMap<>();
		int j = 1;
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

				String orderNo = GetCellData(r, "序号");
				//将String装换为Double
				Double orderNo1 = Double.parseDouble(orderNo);
				//将Double装换为int;
				int orderNo2 = orderNo1.intValue();
				// 创建实体类
				InvtyCntPtySubjSetTab invtyCntPtySubjSetTab = new InvtyCntPtySubjSetTab();
				if (temp.containsKey(orderNo)) {
					invtyCntPtySubjSetTab = temp.get(orderNo);
				}
				// r.getCell(0).setCellType(Cell.CELL_TYPE_NUMERIC);
//						System.out.println(r.getCell(0));
				// 取出当前行第1个单元格数据，并封装在pursOrdr实体的各个属性上

				invtyCntPtySubjSetTab.setOrdrNum(Integer.valueOf(orderNo2));// 序号Integer
				invtyCntPtySubjSetTab.setDeptId(GetCellData(r,"部门编码"));
				invtyCntPtySubjSetTab.setRecvSendCateId(GetCellData(r,"收发类别编码"));
				invtyCntPtySubjSetTab.setRecvSendCateNm(GetCellData(r,"收发类别名称"));
				invtyCntPtySubjSetTab.setInvtyEncd(GetCellData(r,"存货编码"));
				invtyCntPtySubjSetTab.setInvtyNm(GetCellData(r,"存货名称"));
				invtyCntPtySubjSetTab.setInvtyBigClsEncd(GetCellData(r,"存货分类编码"));
				invtyCntPtySubjSetTab.setInvtyBigClsNm(GetCellData(r,"存货分类名称"));
				invtyCntPtySubjSetTab.setCntPtySubjId(GetCellData(r,"对方科目编码"));
				invtyCntPtySubjSetTab.setCntPtySubjNm(GetCellData(r,"对方科目名称"));
				invtyCntPtySubjSetTab.setTeesSubjEncd(GetCellData(r,"暂估科目编码"));
				invtyCntPtySubjSetTab.setTeesSubjNm(GetCellData(r,"暂估科目名称"));
				invtyCntPtySubjSetTab.setMemo(GetCellData(r,"备注"));
				invtyCntPtySubjSetTab.setDeptNm(GetCellData(r,"部门名称"));
				/*invtyCntPtySubjSetTab.setInvtyEncd(GetCellData(r, "存货编码"));// 存货编码
				invtyCntPtySubjSetTab.setInvtyBigClsEncd(GetCellData(r, "存货分类编码"));// 存货大类编码
//				invtyCntPtySubjSetTab.setDeptId(GetCellData(r, "部门编号"));// 部门编号
				invtyCntPtySubjSetTab.setRecvSendCateId(GetCellData(r, "收发类别编码"));// 收发类别编号
				invtyCntPtySubjSetTab.setCntPtySubjId(GetCellData(r, "对方科目编码"));// 对方科目编码
				invtyCntPtySubjSetTab.setTeesSubjEncd(GetCellData(r, "暂估科目编码"));// 暂估科目编码
				invtyCntPtySubjSetTab.setMemo(GetCellData(r, "备注"));// 备注*/
				/*invtyCntPtySubjSetTab.setInvtyNm(GetCellData(r, "存货名称"));// 存货名称
				invtyCntPtySubjSetTab.setInvtyBigClsNm(GetCellData(r, "存货大类名称"));// 存货大类名称
				invtyCntPtySubjSetTab.setDeptNm(GetCellData(r, "部门名称"));// 部门名称
				invtyCntPtySubjSetTab.setRecvSendCateNm(GetCellData(r, "收发类别名称"));// 收发类别名称
				invtyCntPtySubjSetTab.setCntPtySubjNm(GetCellData(r, "对方科目名称"));// 对方科目名称
				invtyCntPtySubjSetTab.setTeesSubjNm(GetCellData(r, "暂估科目名称"));// 暂估科目名称*/

				temp.put(orderNo, invtyCntPtySubjSetTab);

			}
			fileIn.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("解析格式问题第" + j + "行"+e.getMessage());
		}
		return temp;
	}

}
