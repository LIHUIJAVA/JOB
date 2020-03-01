package com.px.mis.account.service.impl;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.dao.AcctItmDocDao;
import com.px.mis.account.dao.InvtyPayblSubjDao;
import com.px.mis.account.entity.InvtyPayblSubj;
import com.px.mis.account.service.InvtyPayblSubjService;
import com.px.mis.purc.dao.ProvrClsDao;
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
//存货应付科目表实体
@Service
@Transactional
public class InvtyPayblSubjServiceImpl extends poiTool  implements InvtyPayblSubjService {
	@Autowired
	private InvtyPayblSubjDao invtyPayblSubjDao;
	@Autowired
	private ProvrClsDao provrClsDao;
	@Autowired
	private AcctItmDocDao acctItmDocDao;
	@Override
	public ObjectNode insertInvtyPayblSubj(InvtyPayblSubj invtyPayblSubj) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}	
		if(provrClsDao.selectProvrClsByProvrClsId(invtyPayblSubj.getProvrClsEncd())==null){
			on.put("isSuccess", false);
			on.put("message", "供应商分类编码"+invtyPayblSubj.getProvrClsEncd()+"不存在，新增失败！");
		}else if(acctItmDocDao.selectAcctItmDocBySubjId(invtyPayblSubj.getPayblSubjEncd())==null){
			on.put("isSuccess", false);
			on.put("message", "应付科目编码"+invtyPayblSubj.getPayblSubjEncd()+"不存在，新增失败！");
		}else if(acctItmDocDao.selectAcctItmDocBySubjId(invtyPayblSubj.getPrepySubjEncd())==null){
			on.put("isSuccess", false);
			on.put("message", "预付科目编码"+invtyPayblSubj.getPrepySubjEncd()+"不存在，新增失败！");
		}else {
			if(invtyPayblSubjDao.selectProvrClsEncd(invtyPayblSubj.getProvrClsEncd())==null) {
				int insertResult = invtyPayblSubjDao.insertInvtyPayblSubj(invtyPayblSubj);
				if(insertResult==1) {
					on.put("isSuccess", true);
					on.put("message", "新增成功");
				}else {
					on.put("isSuccess", false);
					on.put("message", "新增失败");
				}
			}else {
				on.put("isSuccess", false);
				on.put("message", "设置出错，该供应商分类已设置科目!");
			}
		}
		return on;
	}

	@Override
	public ObjectNode updateInvtyPayblSubjById(List<InvtyPayblSubj> invtyPayblSubjList) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}	
		for(InvtyPayblSubj invtyPayblSubj:invtyPayblSubjList) {
			Integer incrsId = invtyPayblSubj.getIncrsId();
			if(incrsId==null) {
				on.put("isSuccess", false);
				on.put("message", "更新失败,主键不能为空");
			}else if (invtyPayblSubjDao.selectInvtyPayblSubjById(invtyPayblSubj.getIncrsId())==null) {
				on.put("isSuccess", false);
				on.put("message", "更新失败，序号"+invtyPayblSubj.getIncrsId()+"不存在！");
			}else {
				int updateResult = invtyPayblSubjDao.updateInvtyPayblSubjById(invtyPayblSubj);
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
	public ObjectNode deleteInvtyPayblSubjById(Integer incrsId) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}	
		if (invtyPayblSubjDao.deleteInvtyPayblSubjById(incrsId)==null) {
			on.put("isSuccess", false);
			on.put("message", "删除失败，编号"+incrsId+"不存在！");
		}else {
			Integer deleteResult = invtyPayblSubjDao.deleteInvtyPayblSubjById(incrsId);
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

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public InvtyPayblSubj selectInvtyPayblSubjById(Integer incrsId) {
		InvtyPayblSubj invtyPayblSubj = invtyPayblSubjDao.selectInvtyPayblSubjById(incrsId);
		return invtyPayblSubj;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public String selectInvtyPayblSubjList(Map map) {
		String resp="";
		List<InvtyPayblSubj> list = invtyPayblSubjDao.selectInvtyPayblSubjList(map);
		int count = invtyPayblSubjDao.selectInvtyPayblSubjCount();
		int listNum=0;
		if(list!=null) {
			listNum=list.size();
		}
		try {
			int pageNo = (int) map.get("pageNo");
			int pageSize = (int) map.get("pageSize");
			int pages = (count+pageSize-1)/pageSize;
			resp=BaseJson.returnRespList("/account/invtyPayblSubj/selectInvtyPayblSubj", true, "查询成功！", count,pageNo,pageSize,listNum,pages, list);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}
	
	//批量删除
	@Override
	public String deleteInvtyPayblSubjList(String incrsId) {
		String message="";
		Boolean isSuccess=true;
		String resp="";
		try {
			Map map=new HashMap<>();
			map.put("incrsId", incrsId);
			int a = invtyPayblSubjDao.deleteInvtyPayblSubjList(map);
			if(a>=1) {
			    isSuccess=true;
			    message="删除成功！";
			}else {
				isSuccess=false;
				message="删除失败！";
			}
			
			resp=BaseJson.returnRespObj("account/invtyPayblSubj/deleteInvtyPayblSubjList", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}
	//打印
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public String selectInvtyPayblSubjListPrint(Map map) {
        String resp = "";
        List<InvtyPayblSubj> list = invtyPayblSubjDao.selectInvtyPayblSubjList(map);

        try {
            resp = BaseJson.returnRespListAnno("/account/invtyPayblSubj/selectInvtyPayblSubjListPrint", true, "查询成功！", list);
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
        Map<String, InvtyPayblSubj> pusOrderMap = uploadScoreInfo(file);
        System.out.println(pusOrderMap.size());
        for (Map.Entry<String, InvtyPayblSubj> entry : pusOrderMap.entrySet()) {
            String invtyPayblSubj = invtyPayblSubjDao.selectProvrClsEncd(entry.getValue().getProvrClsEncd());

            if (invtyPayblSubj != null) {
                throw new RuntimeException("插入重复单号 " + entry.getValue().getProvrClsEncd() + " 请检查");
            }

            try {
                invtyPayblSubjDao.insertInvtyPayblSubj(entry.getValue());
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                throw new RuntimeException("插入sql问题");

            }
        }

        isSuccess = true;
        message = "档案新增成功！";
        try {
            resp = BaseJson.returnRespObj("account/invtyPayblSubj/uploadFileAddDb", isSuccess, message, null);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return resp;

    }

    // 导入excle
    private Map<String, InvtyPayblSubj> uploadScoreInfo(MultipartFile file) {
        Map<String, InvtyPayblSubj> temp = new HashMap<>();
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

                String orderNo = GetCellData(r, "供应商分类编码");
                // 创建实体类
                InvtyPayblSubj invtyPayblSubj = new InvtyPayblSubj();
                if (temp.containsKey(orderNo)) {
                    invtyPayblSubj = temp.get(orderNo);
                }
                // r.getCell(0).setCellType(Cell.CELL_TYPE_NUMERIC);
//                      System.out.println(r.getCell(0));
                // 取出当前行第1个单元格数据，并封装在pursOrdr实体的各个属性上
//                invtyPayblSubj.setIncrsId(null);//自增id
                invtyPayblSubj.setProvrClsEncd(orderNo);// 供应商分类编码
                invtyPayblSubj.setPayblSubjEncd(GetCellData(r, "应付科目编码"));// 应付科目编码
                invtyPayblSubj.setPrepySubjEncd(GetCellData(r, "预付科目编码"));// 预付科目编码
//              invtyPayblSubj.setProvrClsNm(null);//供应商分类名称
//              invtyPayblSubj.setPayblSubjNm(null);//应付科目名称
//              invtyPayblSubj.setPrepySubjNm(null);//预付科目名称

                temp.put(orderNo, invtyPayblSubj);

            }
            fileIn.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("解析格式问题第" + j + "行"+e.getMessage());
        }
        return temp;
    }

}
