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
//���Ӧ����Ŀ��ʵ��
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
			on.put("message", "��Ӧ�̷������"+invtyPayblSubj.getProvrClsEncd()+"�����ڣ�����ʧ�ܣ�");
		}else if(acctItmDocDao.selectAcctItmDocBySubjId(invtyPayblSubj.getPayblSubjEncd())==null){
			on.put("isSuccess", false);
			on.put("message", "Ӧ����Ŀ����"+invtyPayblSubj.getPayblSubjEncd()+"�����ڣ�����ʧ�ܣ�");
		}else if(acctItmDocDao.selectAcctItmDocBySubjId(invtyPayblSubj.getPrepySubjEncd())==null){
			on.put("isSuccess", false);
			on.put("message", "Ԥ����Ŀ����"+invtyPayblSubj.getPrepySubjEncd()+"�����ڣ�����ʧ�ܣ�");
		}else {
			if(invtyPayblSubjDao.selectProvrClsEncd(invtyPayblSubj.getProvrClsEncd())==null) {
				int insertResult = invtyPayblSubjDao.insertInvtyPayblSubj(invtyPayblSubj);
				if(insertResult==1) {
					on.put("isSuccess", true);
					on.put("message", "�����ɹ�");
				}else {
					on.put("isSuccess", false);
					on.put("message", "����ʧ��");
				}
			}else {
				on.put("isSuccess", false);
				on.put("message", "���ó����ù�Ӧ�̷��������ÿ�Ŀ!");
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
				on.put("message", "����ʧ��,��������Ϊ��");
			}else if (invtyPayblSubjDao.selectInvtyPayblSubjById(invtyPayblSubj.getIncrsId())==null) {
				on.put("isSuccess", false);
				on.put("message", "����ʧ�ܣ����"+invtyPayblSubj.getIncrsId()+"�����ڣ�");
			}else {
				int updateResult = invtyPayblSubjDao.updateInvtyPayblSubjById(invtyPayblSubj);
				if(updateResult==1) {
					on.put("isSuccess", true);
					on.put("message", "���³ɹ�");
				}else {
					on.put("isSuccess", false);
					on.put("message", "����ʧ��");
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
			on.put("message", "ɾ��ʧ�ܣ����"+incrsId+"�����ڣ�");
		}else {
			Integer deleteResult = invtyPayblSubjDao.deleteInvtyPayblSubjById(incrsId);
			if(deleteResult==1) {
				on.put("isSuccess", true);
				on.put("message", "ɾ���ɹ�");
			}else if(deleteResult==0){
				on.put("isSuccess", true);
				on.put("message", "û��Ҫɾ��������");		
			}else {
				on.put("isSuccess", false);
				on.put("message", "ɾ��ʧ��");
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
			resp=BaseJson.returnRespList("/account/invtyPayblSubj/selectInvtyPayblSubj", true, "��ѯ�ɹ���", count,pageNo,pageSize,listNum,pages, list);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}
	
	//����ɾ��
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
			    message="ɾ���ɹ���";
			}else {
				isSuccess=false;
				message="ɾ��ʧ�ܣ�";
			}
			
			resp=BaseJson.returnRespObj("account/invtyPayblSubj/deleteInvtyPayblSubjList", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}
	//��ӡ
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public String selectInvtyPayblSubjListPrint(Map map) {
        String resp = "";
        List<InvtyPayblSubj> list = invtyPayblSubjDao.selectInvtyPayblSubjList(map);

        try {
            resp = BaseJson.returnRespListAnno("/account/invtyPayblSubj/selectInvtyPayblSubjListPrint", true, "��ѯ�ɹ���", list);
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
                throw new RuntimeException("�����ظ����� " + entry.getValue().getProvrClsEncd() + " ����");
            }

            try {
                invtyPayblSubjDao.insertInvtyPayblSubj(entry.getValue());
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                throw new RuntimeException("����sql����");

            }
        }

        isSuccess = true;
        message = "���������ɹ���";
        try {
            resp = BaseJson.returnRespObj("account/invtyPayblSubj/uploadFileAddDb", isSuccess, message, null);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return resp;

    }

    // ����excle
    private Map<String, InvtyPayblSubj> uploadScoreInfo(MultipartFile file) {
        Map<String, InvtyPayblSubj> temp = new HashMap<>();
        int j = 1;
        try {
            InputStream fileIn = file.getInputStream();
            // ����ָ�����ļ�����������Excel�Ӷ�����Workbook����
            Workbook wb0 = new HSSFWorkbook(fileIn);
            // ��ȡExcel�ĵ��еĵ�һ����
            Sheet sht0 = wb0.getSheetAt(0);
            // ��õ�ǰsheet�Ŀ�ʼ��
            int firstRowNum = sht0.getFirstRowNum();
            // ��ȡ�ļ������һ��
            int lastRowNum = sht0.getLastRowNum();
            // ���������ֶκ��±�ӳ��
            SetColIndex(sht0.getRow(firstRowNum));
            // ��Sheet�е�ÿһ�н��е���
            for (int i = firstRowNum + 1; i <= lastRowNum; i++) {
                j++;
                Row r = sht0.getRow(i);
                // �����ǰ�е��кţ���0��ʼ��δ�ﵽ2�������У������ѭ��
                if (r.getRowNum() < 1) {
                    continue;
                }

                String orderNo = GetCellData(r, "��Ӧ�̷������");
                // ����ʵ����
                InvtyPayblSubj invtyPayblSubj = new InvtyPayblSubj();
                if (temp.containsKey(orderNo)) {
                    invtyPayblSubj = temp.get(orderNo);
                }
                // r.getCell(0).setCellType(Cell.CELL_TYPE_NUMERIC);
//                      System.out.println(r.getCell(0));
                // ȡ����ǰ�е�1����Ԫ�����ݣ�����װ��pursOrdrʵ��ĸ���������
//                invtyPayblSubj.setIncrsId(null);//����id
                invtyPayblSubj.setProvrClsEncd(orderNo);// ��Ӧ�̷������
                invtyPayblSubj.setPayblSubjEncd(GetCellData(r, "Ӧ����Ŀ����"));// Ӧ����Ŀ����
                invtyPayblSubj.setPrepySubjEncd(GetCellData(r, "Ԥ����Ŀ����"));// Ԥ����Ŀ����
//              invtyPayblSubj.setProvrClsNm(null);//��Ӧ�̷�������
//              invtyPayblSubj.setPayblSubjNm(null);//Ӧ����Ŀ����
//              invtyPayblSubj.setPrepySubjNm(null);//Ԥ����Ŀ����

                temp.put(orderNo, invtyPayblSubj);

            }
            fileIn.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("������ʽ�����" + j + "��"+e.getMessage());
        }
        return temp;
    }

}
