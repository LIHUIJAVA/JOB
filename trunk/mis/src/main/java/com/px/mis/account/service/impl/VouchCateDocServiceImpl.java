package com.px.mis.account.service.impl;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.dao.VouchCateDocDao;
import com.px.mis.account.dao.VouchCateDocSubTabDao;
import com.px.mis.account.entity.VouchCateDoc;
import com.px.mis.account.entity.VouchCateDocSubTab;
import com.px.mis.account.service.VouchCateDocService;
import com.px.mis.account.service.VouchCateDocSubTabService;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Transactional
@Service
public class VouchCateDocServiceImpl  extends poiTool implements VouchCateDocService {
	@Autowired
	private VouchCateDocDao vouchCateDocDao;
	@Autowired
	private VouchCateDocSubTabDao vouchCateDocSubTabDao;
	@Autowired
	private VouchCateDocSubTabService vouchCateDocSubTabService;
	//添加
	@Override
	public ObjectNode insertVouchCateDoc(VouchCateDoc vouchCateDoc) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		String vouchCateWor = vouchCateDoc.getVouchCateWor();
		if(vouchCateWor==null) {
			on.put("isSuccess", false);
			on.put("message", "新增失败,主键不能为空");
		}else if(vouchCateDocDao.selectVouchCateDocByVouchCateWor(vouchCateWor)!=null){
			on.put("isSuccess", false);
			on.put("message", "主键"+vouchCateWor+"已存在，新增失败！");
		}else {
			int vcdInsertResult = vouchCateDocDao.insertVouchCateDoc(vouchCateDoc);
			if(vcdInsertResult==1) {
				System.out.println("oooooooooooooooooooo"+vouchCateDoc.getVouchCateWor());
				List<VouchCateDocSubTab> lmtSubjList = vouchCateDoc.getLmtSubjList();
				System.out.println("pppppppppppppp"+vouchCateDoc.getLmtSubjList());
				for(VouchCateDocSubTab vouchCateDocSubTab:lmtSubjList) {
					vouchCateDocSubTab.setVouchCateWor(vouchCateWor);
					on = vouchCateDocSubTabService.insertVouchCateSubTabDoc(vouchCateDocSubTab);
				}
				
			}else {
				on.put("isSuccess", false);
				on.put("message", "新增处理失败");
			}
		}
		return on;
	}
	//更新
	@Override
	public ObjectNode updateVouchCateDocById(List<VouchCateDoc> vouchCateDocList) {
		
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}	
		for(VouchCateDoc vouchCateDoc:vouchCateDocList) {
			String vouchCateWor = vouchCateDoc.getVouchCateWor();
			if(vouchCateWor==null) {
				on.put("isSuccess", false);
				on.put("message", "更新失败,主键不能为空");
			}else if(vouchCateDocDao.selectVouchCateDocByVouchCateWor(vouchCateWor)==null){
				on.put("isSuccess", false);
				on.put("message", "主键"+vouchCateWor+"不存在，更新失败！");
			}else {
				int updateResult = vouchCateDocDao.updateVouchCateDocByVouchCateWor(vouchCateDoc);
				int deleteResult = vouchCateDocSubTabService.deleteVouchCateDocSubTabByVouchCateWor(vouchCateDoc.getVouchCateWor());
				if(updateResult==1 && deleteResult >=1) {
					List<VouchCateDocSubTab> lmtSubjList = vouchCateDoc.getLmtSubjList();
					for(VouchCateDocSubTab vouchCateSubTab:lmtSubjList) {
						vouchCateSubTab.setVouchCateWor(vouchCateWor);
						vouchCateDocSubTabDao.insertVouchCateSubTabDoc(vouchCateSubTab);
					}
					on.put("isSuccess", true);
					on.put("message", "更新处理成功");
				}else {
					on.put("isSuccess", false);
					on.put("message", "更新处理失败");
				}
			}
		}
		
		return on;
	}
	//删除
	@Override
	public String deleteVouchCateDocByVouchCateWor(String vouchCateWor) throws IOException {
		String resp = "";
		String message = "";
		boolean isSuccess = true;
		List<String> list = getList(vouchCateWor);
		Integer deleteResult = vouchCateDocDao.deleteVouchCateDocByVouchCateWor(list);
/*		deleteResult = vouchCateDocDao.deleteVouchCateDocByVouchCateWor(vouchCateWor);*/
		if(deleteResult>=1) {
			message = "处理成功";
		}else if(deleteResult==0){
			isSuccess = false;
			message = "没有要删除的数据";
		}
		
		resp = BaseJson.returnRespObj("/account/vouchCateDoc/deleteVouchCateDoc", isSuccess, message, null);
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
		String[] str = id.split(",");
		for (int i = 0; i < str.length; i++) {
			list.add(str[i]);
		}
		return list;
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public VouchCateDoc selectVouchCateDocById(String vouchCateId) {
		VouchCateDoc selectOne = vouchCateDocDao.selectVouchCateDocByVouchCateWor(vouchCateId);
		return selectOne;
	}

	//根据主表分页查询
	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public String selectVouchCateDocList(Map map) {
		String resp="";
		List<VouchCateDoc> list = vouchCateDocDao.selectVouchCateDocList(map);
		int count = vouchCateDocDao.selectVouchCateDocCount();
		int listNum=0;
		if(list!=null) {
			listNum=list.size();
		}
		try {
			int pageNo = (int) map.get("pageNo");
			int pageSize = (int) map.get("pageSize");
			int pages = (count+pageSize-1)/pageSize;
			resp=BaseJson.returnRespList("/account/vouchCateDoc/selectVouchCateDoc", true, "查询成功！", count,pageNo,pageSize,listNum,pages, list);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}
	//根据主表分页查询打印
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public String selectVouchCateDocPrint(Map map) {
        String resp = "";
    	map.put("index",null);
		map.put("num",null);
        List<VouchCateDoc> list = vouchCateDocDao.selectVouchCateDocList(map);
        try {
            resp = BaseJson.returnRespList("/account/vouchCateDoc/selectVouchCateDocPrint", true, "查询成功！", list);
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
        Map<String, VouchCateDoc> pusOrderMap = uploadScoreInfo(file);
        System.out.println(pusOrderMap.size());
        for (Map.Entry<String, VouchCateDoc> entry : pusOrderMap.entrySet()) {

            if (vouchCateDocDao.selectVouchCateDocByVouchCateWor(entry.getValue().getVouchCateNm()) != null) {
                throw new RuntimeException("插入重复单号 " + entry.getValue().getVouchCateNm() + " 请检查");
            }

            try {
                vouchCateDocDao.insertVouchCateDoc(entry.getValue());
                List<VouchCateDocSubTab> lmtSubjList = entry.getValue().getLmtSubjList();
                for (VouchCateDocSubTab subTab : lmtSubjList) {
                    vouchCateDocSubTabService.insertVouchCateSubTabDoc(subTab);
                }
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                throw new RuntimeException("插入sql问题");
            }
        }

        isSuccess = true;
        message = "凭证类别导入成功！";
        try {
            resp = BaseJson.returnRespObj("account/vouchCateDoc/uploadFileAddDb", isSuccess, message, null);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return resp;

    }

    // 导入excle
    private Map<String, VouchCateDoc> uploadScoreInfo(MultipartFile file) {
        Map<String, VouchCateDoc> temp = new HashMap<>();
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

                String orderNo = GetCellData(r, "凭证类别字");
                // 创建实体类
                VouchCateDoc vouchCateDoc = new VouchCateDoc();
                if (temp.containsKey(orderNo)) {
                    vouchCateDoc = temp.get(orderNo);
                }
                List<VouchCateDocSubTab> vouchCateDocSubTabs = vouchCateDoc.getLmtSubjList();// 订单子表
                if (vouchCateDocSubTabs == null) {
                    vouchCateDocSubTabs = new ArrayList<>();
                }

                // r.getCell(0).setCellType(Cell.CELL_TYPE_NUMERIC);
//                      System.out.println(r.getCell(0));
                // 取出当前行第1个单元格数据，并封装在pursOrdr实体的各个属性上
//                vouchCateDoc.setOrdrNum(Integer.valueOf(GetCellData(r, "序号")));//序号
                vouchCateDoc.setVouchCateWor(orderNo);//凭证类别字；
                vouchCateDoc.setVouchCateNm(GetCellData(r, "凭证类别名称"));//凭证类别名称
//                vouchCateDoc.setVouchCateSortNum(GetCellData(r, "凭证类别排序号"));//凭证类别排序号
                vouchCateDoc.setLmtMode(GetCellData(r, "限制类型"));//限制方式
                vouchCateDoc.setMemo(GetCellData(r, "备注"));//备注
                
                
                VouchCateDocSubTab vouchCateDocSubTab=new VouchCateDocSubTab();
                vouchCateDocSubTab.setVouchCateWor(orderNo);//凭证类别名称
//                vouchCateDocSubTab.setLmtSubjId(GetCellData(r, "受限科目编号"));//受限科目编号
//                vouchCateDocSubTab.setOrdrNum(ordrNum);

                vouchCateDocSubTabs.add(vouchCateDocSubTab);

                vouchCateDoc.setLmtSubjList(vouchCateDocSubTabs);

                temp.put(orderNo, vouchCateDoc);

            }
            fileIn.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("解析格式问题第" + j + "行"+e.getMessage());
        }
        return temp;
    }
}
