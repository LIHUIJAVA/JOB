package com.px.mis.purc.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.px.mis.purc.dao.ProvrClsDao;
import com.px.mis.purc.dao.ProvrDocDao;
import com.px.mis.purc.entity.ProvrCls;
import com.px.mis.purc.entity.ProvrDoc;
import com.px.mis.purc.service.ProvrDocService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.poiTool;

/*供应商档案功能*/
@Transactional
@Service
public class ProvrDocServiceImpl<E> extends poiTool implements ProvrDocService {
	@Autowired
	private ProvrDocDao pdd;
	@Autowired
	private ProvrClsDao pcd;

	// 新增供应商档案
	@Override
	public String insertProvrDoc(ProvrDoc provrDoc) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			if (provrDoc.getProvrId() != "" && provrDoc.getProvrId() != null) {
				if (pcd.selectProvrClsByProvrClsId(provrDoc.getProvrClsId()) != null) {
					if (pdd.selectProvrId(provrDoc.getProvrId()) != null) {
						isSuccess = false;
						message = "编号" + provrDoc.getProvrId() + "已存在，请重新输入！";
					} else {
						if (provrDoc.getDevDt() == "") {
							provrDoc.setDevDt(null);
						}
						int a = pdd.insertProvrDoc(provrDoc);
						if (a >= 1) {
							isSuccess = true;
							message = "供应商档案新增成功！";
						} else {
							isSuccess = false;
							message = "供应商档案新增失败！";
						}

					}
				} else {
					isSuccess = false;
					message = "供应商档案分类不存在！";
				}
			} else {
				isSuccess = false;
				message = "供应商编码不能为空！";
			}
			resp = BaseJson.returnRespObj("purc/ProvrDoc/insertProvrDoc", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// 修改供应商档案
	@Override
	public String updateProvrDocByProvrId(ProvrDoc provrDoc) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			if (provrDoc.getProvrId() != "" && provrDoc.getProvrId() != null) {
				if (pcd.selectProvrClsByProvrClsId(provrDoc.getProvrClsId()) != null) {
					pdd.updateProvrDocByProvrId(provrDoc);
					isSuccess = true;
					message = "供应商档案修改成功！";
				} else {
					isSuccess = false;
					message = "供应商档案分类不存在！";
				}
			} else {
				isSuccess = false;
				message = "供应商编码不能为空！";
			}
			resp = BaseJson.returnRespObj("purc/ProvrDoc/updateProvrDocByProvrId", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// 按照编号查询 供应商档案
	@Override
	public ProvrCls selectProvrDocByProvrId(String provrId) {
		ProvrCls provrCls = pdd.selectProvrDocByProvrId(provrId);
		return provrCls;
	}

	// 查询全部供应商档案
	@Override
	public String selectProvrDocList(Map map) {
		String resp = "";
		try {
			List<String> provrIdList = getList((String) map.get("provrId"));// 供应商编码
			map.put("provrIdList", provrIdList);
			String provrClsId = (String) map.get("provrClsId");
			if (provrClsId != null && provrClsId != "") {
				if (pcd.selectProvrClsByProvrClsId(provrClsId).getLevel() == 0) {
					map.put("provrClsId", "");
				}
			}
			List<ProvrCls> provrDocList = new ArrayList<ProvrCls>();
			// 供应商区间查询代码
			if (map.get("start") != null && map.get("end") != null && !map.get("start").equals("")
					&& !map.get("end").equals("")) {
				provrDocList = pdd.selectProvrDocListByItv(map);
			} else {
				provrDocList = pdd.selectProvrDocList(map);
			}
			int count = pdd.selectProvrDocCount(map);
			int pageNo = Integer.parseInt(map.get("pageNo").toString());
			int pageSize = Integer.parseInt(map.get("pageSize").toString());
			int listNum = provrDocList.size();
			int pages = count / pageSize + 1;
			resp = BaseJson.returnRespList("purc/ProvrDoc/selectProvrDocList", true, "查询成功！", count, pageNo, pageSize,
					listNum, pages, provrDocList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
	}

	// 打印、输入输出供应商档案
	@Override
	public String printingProvrDocList(Map map) {
		String resp = "";
		List<String> provrIdList = getList((String) map.get("provrId"));// 供应商编码
		map.put("provrIdList", provrIdList);
		String provrClsId = (String) map.get("provrClsId");
		if (provrClsId != null && provrClsId != "") {
			if (pcd.selectProvrClsByProvrClsId(provrClsId).getLevel() == 0) {
				map.put("provrClsId", "");
			}
		}
		List<ProvrCls> provrDocList = pdd.printingProvrDocList(map);
		try {
			resp = BaseJson.returnRespObjList("purc/ProvrDoc/printingProvrDocList", true, "查询成功！", null, provrDocList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
	}

	// 批量删除供应商档案
	@Override
	public String deleteProvrDocList(String provrId) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			List<String> list = getList(provrId);
			int a = pdd.deleteProvrDocList(list);
			if (a >= 1) {
				isSuccess = true;
				message = "删除成功！";
			} else {
				isSuccess = false;
				message = "删除失败！";
			}
			resp = BaseJson.returnRespObj("purc/ProvrDoc/deleteProvrDocList", isSuccess, message, null);
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
	public List<String> getList(String id) {
		List<String> list = new ArrayList<String>();
		if (StringUtils.isNotEmpty(id)) {
			if (id.contains(",")) {
				String[] str = id.split(",");
				for (int i = 0; i < str.length; i++) {
					list.add(str[i]);
				}
			} else {
				if (StringUtils.isNotEmpty(id)) {
					list.add(id);
				}
			}
		}
		return list;
	}

	// 导入
	public String uploadFileAddDb(MultipartFile file) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		Map<String, ProvrDoc> provrDocMap = uploadScoreInfo(file);
		for (Map.Entry<String, ProvrDoc> entry : provrDocMap.entrySet()) {
			if (pdd.selectProvrId(entry.getValue().getProvrId()) != null) {
				isSuccess = false;
				message = "供应商档案中部分客户已存在，无法导入，请查明再导入！";
				throw new RuntimeException(message);
			} else {
				pdd.insertProvrDocUpload(entry.getValue());
				isSuccess = true;
				message = "供应商档案导入成功！";
			}
		}
		try {
			resp = BaseJson.returnRespObj("purc/ProvrDoc/uploadProvrDocFile", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// 导入excle
	private Map<String, ProvrDoc> uploadScoreInfo(MultipartFile file) {
		Map<String, ProvrDoc> temp = new HashMap<>();
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
				String orderNo = GetCellData(r, "供应商编码");
				// 创建实体类
				ProvrDoc provrDoc = new ProvrDoc();
				if (temp.containsKey(orderNo)) {
					provrDoc = temp.get(orderNo);
				}
				// 取出当前行第1个单元格数据，并封装在pursOrdr实体的各个属性上
				provrDoc.setProvrId(orderNo);// 供应商编码
				provrDoc.setProvrNm(GetCellData(r, "供应商名称"));// 供应商名称
				provrDoc.setProvrShtNm(GetCellData(r, "供应商简称"));// 供应商简称
				provrDoc.setProvrClsId(GetCellData(r, "供应商分类编码"));// 供应商分类编码
				provrDoc.setOpnBnk(GetCellData(r, "开户银行"));// 开户银行
				provrDoc.setBkatNum(GetCellData(r, "银行账号"));// 银行账号
				provrDoc.setBankEncd(GetCellData(r, "所属银行编码"));// 所属银行编码
				provrDoc.setRgstCap(GetBigDecimal(GetCellData(r, "注册资金"), 8));// 8表示小数位数 //注册资金
				provrDoc.setLpr(GetCellData(r, "法人"));// 法人
				provrDoc.setContcr(GetCellData(r, "联系人"));// 联系人
				provrDoc.setTel(GetCellData(r, "联系电话"));// 联系电话
				provrDoc.setZipCd(GetCellData(r, "邮政编码"));// 邮政编码
				provrDoc.setAddr(GetCellData(r, "地址"));// 地址
				provrDoc.setTaxRate(GetBigDecimal(GetCellData(r, "税率"), 8));// 8表示小数位数 //税率
				provrDoc.setIsNtPurs(new Double(GetCellData(r, "是否采购")).intValue());// 是否采购
				provrDoc.setIsOutsource(new Double(GetCellData(r, "是否委外")).intValue());// 是否委外
				provrDoc.setIsNtServ(new Double(GetCellData(r, "是否服务")).intValue());// 是否服务
				if (GetCellData(r, "发展日期") == null || GetCellData(r, "发展日期").equals("")) {
					provrDoc.setDevDt(null);
				} else {
					provrDoc.setDevDt(GetCellData(r, "发展日期"));// 发展日期
				}
				provrDoc.setSetupPers(GetCellData(r, "创建人"));// 创建人
				if (GetCellData(r, "创建时间") == null || GetCellData(r, "创建时间").equals("")) {
					provrDoc.setSetupDt(null);
				} else {
					provrDoc.setSetupDt(GetCellData(r, "创建时间"));// 创建时间
				}
				provrDoc.setMdfr(GetCellData(r, "修改人"));// 修改人
				if (GetCellData(r, "修改时间") == null || GetCellData(r, "修改时间").equals("")) {
					provrDoc.setModiDt(null);
				} else {
					provrDoc.setModiDt(GetCellData(r, "修改时间"));// 修改时间
				}
				provrDoc.setMemo(GetCellData(r, "备注"));// 备注

				temp.put(orderNo, provrDoc);
			}
			fileIn.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("文件的第" + j + "行导入格式有误，无法导入!" + e.getMessage());
		}
		return temp;
	}

}
