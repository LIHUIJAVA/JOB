package com.px.mis.account.service.impl;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.dao.AcctItmDocDao;
import com.px.mis.account.dao.TaxSubjDao;
import com.px.mis.account.entity.TaxSubj;
import com.px.mis.account.service.TaxSubjService;
import com.px.mis.purc.dao.ProvrClsDao;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;
import com.px.mis.util.poiTool;

import org.apache.commons.lang.StringUtils;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
@Transactional
public class TaxSubjServiceImpl extends poiTool  implements TaxSubjService {
	@Autowired
	private TaxSubjDao taxSubjDao;
	@Autowired
	private ProvrClsDao provrClsDao;
	@Autowired
	private AcctItmDocDao acctItmDocDao;
	@Override
	public ObjectNode insertTaxSubj(TaxSubj taxSubj) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(acctItmDocDao.selectAcctItmDocBySubjId(taxSubj.getPursSubjEncd())==null){
			on.put("isSuccess", false);
			on.put("message", "采购科目编码"+taxSubj.getPursSubjEncd()+"不存在，新增失败！");
		}else if(provrClsDao.selectProvrClsByProvrClsId(taxSubj.getProvrClsEncd())==null){
			on.put("isSuccess", false);
			on.put("message", "供应商分类编码"+taxSubj.getProvrClsEncd()+"不存在，新增失败！");
		}else if(acctItmDocDao.selectAcctItmDocBySubjId(taxSubj.getPursTaxEncd())==null){
			on.put("isSuccess", false);
			on.put("message", "采购税金编码"+taxSubj.getPursTaxEncd()+"不存在，新增失败！");
		}else {
			int insertResult = taxSubjDao.insertTaxSubj(taxSubj);
			if(insertResult==1) {
				on.put("isSuccess", true);
				on.put("message", "新增成功");
			}else {
				on.put("isSuccess", false);
				on.put("message", "新增失败");
			}
		}
			
		return on;
	}

	@Override
	public ObjectNode updateTaxSubjById(List<TaxSubj> taxSubjList) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		for(TaxSubj taxSubj:taxSubjList) {
			if (taxSubjDao.selectTaxSubjById(taxSubj.getAutoId())==null) {
				on.put("isSuccess", false);
				on.put("message", "更新失败，序号"+taxSubj.getAutoId()+"不存在！");
			}else if(provrClsDao.selectProvrClsByProvrClsId(taxSubj.getProvrClsEncd())==null){
				on.put("isSuccess", false);
				on.put("message", "供应商分类编码"+taxSubj.getProvrClsEncd()+"不存在，更新失败！");
			}else if(acctItmDocDao.selectAcctItmDocBySubjId(taxSubj.getPursSubjEncd())==null){
				on.put("isSuccess", false);
				on.put("message", "采购科目编码"+taxSubj.getPursSubjEncd()+"不存在，更新失败！");
			}else if(acctItmDocDao.selectAcctItmDocBySubjId(taxSubj.getPursTaxEncd())==null){
				on.put("isSuccess", false);
				on.put("message", "采购税金编码"+taxSubj.getPursTaxEncd()+"不存在，更新失败！");
			}else {
				int updateResult = taxSubjDao.updateTaxSubjById(taxSubj);
				if(updateResult==1) {
					on.put("isSuccess", true);
					on.put("message", "更新成功");
				}else {
					on.put("isSuccess", false);
					on.put("message", "更新失败");
				}
			}
		}
		return on;
	}

	@Override
	public ObjectNode deleteTaxSubjById(String autoId) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<String> list = getList(autoId);
		int deleteResult = taxSubjDao.deleteTaxSubjById(list);
		if(deleteResult>=1) {
			on.put("isSuccess", true);
			on.put("message", "处理成功");
		}else if(deleteResult==0){
			on.put("isSuccess", true);
			on.put("message", "没有要删除的数据");		
		}else {
			on.put("isSuccess", false);
			on.put("message", "删除失败");
		}
		
		return on;
	}
	/**
	 * id放入list
	 * 
	 * @param id id(多个已逗号分隔)
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

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public TaxSubj selectTaxSubjById(Integer id) {
		TaxSubj taxSubj = taxSubjDao.selectTaxSubjById(id);
		return taxSubj;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public String selectTaxSubjList(Map map) {
		String resp="";
		List<TaxSubj> list = taxSubjDao.selectTaxSubjList(map);
		int count = taxSubjDao.selectTaxSubjCount();
		int listNum=0;
		if(list!=null) {
			listNum=list.size();
		}
		try {
			int pageNo = (int) map.get("pageNo");
			int pageSize = (int) map.get("pageSize");
			int pages = (count+pageSize-1)/pageSize;
			resp=BaseJson.returnRespList("/account/taxSubj/selectTaxSubj", true, "查询成功！", count,pageNo,pageSize,listNum,pages, list);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}
	//打印
	@Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public String selectTaxSubjPrint(Map map) {
        String resp = "";
        List<TaxSubj> list = taxSubjDao.selectTaxSubjList(map);

        try {
            resp = BaseJson.returnRespListAnno("/account/taxSubj/selectTaxSubjListPrint", true, "查询成功！",list);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resp;
    }

	   @Override
    public String uploadFileAddDb(MultipartFile file) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        Map<String, TaxSubj> pusOrderMap = uploadScoreInfo(file);
        System.out.println(pusOrderMap.size());
        for (Map.Entry<String, TaxSubj> entry : pusOrderMap.entrySet()) {
//	            if (outIntoWhsAdjSnglDao.selectOutIntoWhsAdjSnglByFormNum(entry.getValue().getFormNum()) != null) {
//	                throw new RuntimeException("插入重复单号 " + entry.getValue().getFormNum() + " 请检查");
//	            }
            try {
                taxSubjDao.insertTaxSubj(entry.getValue());
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                throw new RuntimeException("插入sql问题");
            }
			try {
				Map<String,String> promap = new HashMap<>();
				promap.put("provrId",entry.getValue().getProvrClsEncd());
				promap.put("provrNm",entry.getValue().getProvrClsNm());
//				provrClsDao.insertProvrClsToLead(promap);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				throw new RuntimeException("插入sql问题");
			}
			try {
				Map<String,String> promap = new HashMap<>();
				promap.put("pursId",entry.getValue().getPursSubjEncd());
				promap.put("pursNm",entry.getValue().getPursSubjNm());
				acctItmDocDao.insertTolead(promap);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				throw new RuntimeException("插入sql问题");
			}
        }

        isSuccess = true;
        message = "档案新增成功！";
        try {
            resp = BaseJson.returnRespObj("account/taxSubj/uploadFileAddDb", isSuccess, message, null);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return resp;
    }

	    // 导入excle
    private Map<String, TaxSubj> uploadScoreInfo(MultipartFile file) {
        Map<String, TaxSubj> temp = new HashMap<>();
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
                String orderNo = GetCellData(r, "编号");
                Double orderNO1 = Double.parseDouble(orderNo);
                int orderNo2 = orderNO1.intValue();
                // 创建实体类
                TaxSubj taxSubj = new TaxSubj();
                if (temp.containsKey(orderNo)) {
                    taxSubj = temp.get(orderNo);
                }

                // r.getCell(0).setCellType(Cell.CELL_TYPE_NUMERIC);
//	                      System.out.println(r.getCell(0));
                // 取出当前行第1个单元格数据，并封装在pursOrdr实体的各个属性上
                taxSubj.setAutoId(Integer.valueOf(orderNo2));// 自动id
                taxSubj.setProvrClsEncd(GetCellData(r, "供应商分类编码"));// 供应商分类编码；
				taxSubj.setProvrClsNm(GetCellData(r,"供应商分类名称"));//供应商匪类名称
                taxSubj.setPursSubjEncd(GetCellData(r, "采购科目编码"));// 采购科目编码；
				taxSubj.setPursSubjNm(GetCellData(r,"采购科目名称"));//采购科目名称
                taxSubj.setPursTaxEncd(GetCellData(r, "采购税金编码"));// 采购税金编码；
                taxSubj.setPursTaxNm(GetCellData(r, "采购税金科目"));// 采购税金编码；



//	                 String provrClsNm;//供应商分类名称
//	                 String pursSubjNm;//采购科目名称
//	                 String pursTaxNm;//采购税金名称

                temp.put(orderNo, taxSubj);

            }
            fileIn.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("解析格式问题第" + j + "行"+e.getMessage());
        }
        return temp;
    }

}
