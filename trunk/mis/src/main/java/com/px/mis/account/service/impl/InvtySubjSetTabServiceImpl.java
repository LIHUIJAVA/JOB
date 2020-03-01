package com.px.mis.account.service.impl;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.dao.AcctItmDocDao;
import com.px.mis.account.dao.InvtySubjSetTabDao;
import com.px.mis.account.entity.InvtySubjSetTab;
import com.px.mis.account.service.InvtySubjSetTabService;
import com.px.mis.purc.dao.InvtyClsDao;
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
//存货科目设置表实体
@Service
@Transactional
public class InvtySubjSetTabServiceImpl extends poiTool  implements InvtySubjSetTabService {

	@Autowired
	private InvtySubjSetTabDao invtySubSetTabDao;
	@Autowired
	private InvtyClsDao invtyClsDao;
	@Autowired
	private AcctItmDocDao acctItmDocDao;
	@Override
	public ObjectNode insertInvtySubjSetTab(InvtySubjSetTab invtySubjSetTab) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}	
		
		if(invtyClsDao.selectInvtyClsByInvtyClsEncd(invtySubjSetTab.getInvtyBigClsEncd())==null){
			on.put("isSuccess", false);
			on.put("message", "存货分类编号"+invtySubjSetTab.getInvtyBigClsEncd()+"不存在，新增失败！");
		}else if(acctItmDocDao.selectAcctItmDocBySubjId(invtySubjSetTab.getInvtySubjId())==null){
			on.put("isSuccess", false);
			on.put("message", "存货科目编号"+invtySubjSetTab.getInvtySubjId()+"不存在，新增失败！");
		}else if(acctItmDocDao.selectAcctItmDocBySubjId(invtySubjSetTab.getEntrsAgnSubjId())==null){
			on.put("isSuccess", false);
			on.put("message", "委托代销科目编号"+invtySubjSetTab.getEntrsAgnSubjId()+"不存在，新增失败！");
		}else {
			if(invtySubSetTabDao.selectInvtyBigClsEncd(invtySubjSetTab.getInvtyBigClsEncd())==null) {
				int insertResult = invtySubSetTabDao.insertInvtySubjSetTab(invtySubjSetTab);
				if(insertResult==1) {
					on.put("isSuccess", true);
					on.put("message", "新增成功");
				}else {
					on.put("isSuccess", false);
					on.put("message", "新增失败");
				}
			}else {
				on.put("isSuccess", false);
				on.put("message", "设置出错，该存货分类已设置科目!");
			}
		}
		
		return on;
	}

	//修改科目
	@Override
	public ObjectNode updateInvtySubjSetTabById(List<InvtySubjSetTab> invtySubjSetTabList) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}	
		
		for(InvtySubjSetTab invtySubjSetTab:invtySubjSetTabList) {
			Integer ordrNum = invtySubjSetTab.getOrdrNum();
			if(ordrNum==null) {
				on.put("isSuccess", false);
				on.put("message", "更新失败,主键不能为空");
			}else if(invtySubSetTabDao.selectInvtySubjSetTabByOrdrNum(invtySubjSetTab.getOrdrNum())==null) {
				on.put("isSuccess", false);
				on.put("message", "更新失败，序号"+invtySubjSetTab.getOrdrNum()+"不存在！");
			}else if(invtyClsDao.selectInvtyClsByInvtyClsEncd(invtySubjSetTab.getInvtyBigClsEncd())==null){
				on.put("isSuccess", false);
				on.put("message", "存货分类编号"+invtySubjSetTab.getInvtyBigClsEncd()+"不存在，更新失败！");
			}else if(acctItmDocDao.selectAcctItmDocBySubjId(invtySubjSetTab.getInvtySubjId())==null){
				on.put("isSuccess", false);
				on.put("message", "存货科目编号"+invtySubjSetTab.getInvtySubjId()+"不存在，更新失败！");
			}else if(acctItmDocDao.selectAcctItmDocBySubjId(invtySubjSetTab.getEntrsAgnSubjId())==null){
				on.put("isSuccess", false);
				on.put("message", "委托代销科目编号"+invtySubjSetTab.getEntrsAgnSubjId()+"不存在，更新失败！");
			}else {
				int updateResult = invtySubSetTabDao.updateInvtySubjSetTabByOrdrNum(invtySubjSetTab);
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

	//单个删除
	@Override
	public ObjectNode deleteInvtySubjSetTabByOrdrNum(Integer ordrNum) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}	
		if (invtySubSetTabDao.selectInvtySubjSetTabByOrdrNum(ordrNum)==null) {
			on.put("isSuccess", false);
			on.put("message", "删除失败，编号"+ordrNum+"不存在！");
		}else {
			int deleteResult = invtySubSetTabDao.deleteInvtySubjSetTabByOrdrNum(ordrNum);
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
		}
		return on;
	}

	//按照科目编号查询
	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public InvtySubjSetTab selectInvtySubjSetTabByOrdrNum(Integer vouchCateId) {
		InvtySubjSetTab selectByOrdrNum = invtySubSetTabDao.selectInvtySubjSetTabByOrdrNum(vouchCateId);
		return selectByOrdrNum;
	}

	//分页查询
	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public String selectInvtySubjSetTabList(Map map) {
		String resp="";
		List<InvtySubjSetTab> list = invtySubSetTabDao.selectInvtySubjSetTabList(map);
		int count = invtySubSetTabDao.selectInvtySubjSetTabCount();
		int listNum=0;
		if(list!=null) {
			listNum=list.size();
		}
		try {
			int pageNo = (int) map.get("pageNo");
			int pageSize = (int) map.get("pageSize");
			int pages = (count+pageSize-1)/pageSize;
			resp=BaseJson.returnRespList("/account/invtySubjSetTab/selectInvtySubjSetTab", true, "查询成功！", count,pageNo,pageSize,listNum,pages, list);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	//批量删除
	@Override
	public String deleteInvtySubjSetTabList(String ordrNum) {
		String message="";
		Boolean isSuccess=true;
		String resp="";
		try {
			Map map =new HashMap<>();
			map.put("ordrNum", ordrNum);
			int a = invtySubSetTabDao.deleteInvtySubjSetTabList(map);
			if(a>=1) {
			    isSuccess=true;
			    message="删除成功！";
			}else {
				isSuccess=false;
				message="删除失败！";
			}
			
			resp=BaseJson.returnRespObj("account/invtySubjSetTab/deleteInvtySubjSetTabList", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}
	//分页查询打印
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public String selectInvtySubjSetTabPrint(Map map) {
        String resp = "";
        List<InvtySubjSetTab> list = invtySubSetTabDao.selectInvtySubjSetTabList(map);     
        try {
            resp = BaseJson.returnRespListAnno("/account/invtySubjSetTab/selectInvtySubjSetTabListPrint", true, "查询成功！", list);
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
        Map<String, InvtySubjSetTab> pusOrderMap = uploadScoreInfo(file);
        System.out.println(pusOrderMap.size());
        for (Map.Entry<String, InvtySubjSetTab> entry : pusOrderMap.entrySet()) {
            String string = invtySubSetTabDao.selectInvtyBigClsEncd(entry.getValue().getInvtyBigClsEncd());
            if (string != null) {
                throw new RuntimeException("插入重复单号 " + entry.getValue().getInvtyBigClsEncd() + " 请检查");
            }

            try {
                invtySubSetTabDao.insertInvtySubjSetTab(entry.getValue());
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                throw new RuntimeException("插入sql问题");
            }
        }

        isSuccess = true;
        message = "档案新增成功！";
        try {
            resp = BaseJson.returnRespObj("account/invtySubjSetTab/uploadFileAddDb", isSuccess, message, null);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return resp;

    }

    // 导入excle
    private Map<String, InvtySubjSetTab> uploadScoreInfo(MultipartFile file) {
        Map<String, InvtySubjSetTab> temp = new HashMap<>();
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

                String orderNo = GetCellData(r, "存货分类编码");
                // 创建实体类
                InvtySubjSetTab invtySubjSetTab = new InvtySubjSetTab();
                if (temp.containsKey(orderNo)) {
                    invtySubjSetTab = temp.get(orderNo);
                }
                // r.getCell(0).setCellType(Cell.CELL_TYPE_NUMERIC);
//                      System.out.println(r.getCell(0));
                // 取出当前行第1个单元格数据，并封装在pursOrdr实体的各个属性上
//                private Integer ordrNum;//序号
                invtySubjSetTab.setInvtyBigClsEncd(orderNo);// 存货大类编号
                invtySubjSetTab.setInvtySubjId(GetCellData(r, "存货科目编码"));// 存货科目编号
                invtySubjSetTab.setEntrsAgnSubjId(GetCellData(r, "委托代销科目编码"));// 委托代销科目编号
                invtySubjSetTab.setMemo(GetCellData(r, "备注"));// 备注
//                private String invtyClsNm;//存货分类名称
//                private String invtySubjNm;//存货科目名称
//                private String entrsAgnSubjNm;//委托代销科目名称

                temp.put(orderNo, invtySubjSetTab);

            }
            fileIn.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("解析格式问题第" + j + "行"+e.getMessage());
        }
        return temp;
    }

}
