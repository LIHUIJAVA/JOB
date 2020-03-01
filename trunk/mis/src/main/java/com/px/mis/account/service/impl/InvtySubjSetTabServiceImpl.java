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
//�����Ŀ���ñ�ʵ��
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
			on.put("message", "���������"+invtySubjSetTab.getInvtyBigClsEncd()+"�����ڣ�����ʧ�ܣ�");
		}else if(acctItmDocDao.selectAcctItmDocBySubjId(invtySubjSetTab.getInvtySubjId())==null){
			on.put("isSuccess", false);
			on.put("message", "�����Ŀ���"+invtySubjSetTab.getInvtySubjId()+"�����ڣ�����ʧ�ܣ�");
		}else if(acctItmDocDao.selectAcctItmDocBySubjId(invtySubjSetTab.getEntrsAgnSubjId())==null){
			on.put("isSuccess", false);
			on.put("message", "ί�д�����Ŀ���"+invtySubjSetTab.getEntrsAgnSubjId()+"�����ڣ�����ʧ�ܣ�");
		}else {
			if(invtySubSetTabDao.selectInvtyBigClsEncd(invtySubjSetTab.getInvtyBigClsEncd())==null) {
				int insertResult = invtySubSetTabDao.insertInvtySubjSetTab(invtySubjSetTab);
				if(insertResult==1) {
					on.put("isSuccess", true);
					on.put("message", "�����ɹ�");
				}else {
					on.put("isSuccess", false);
					on.put("message", "����ʧ��");
				}
			}else {
				on.put("isSuccess", false);
				on.put("message", "���ó����ô�����������ÿ�Ŀ!");
			}
		}
		
		return on;
	}

	//�޸Ŀ�Ŀ
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
				on.put("message", "����ʧ��,��������Ϊ��");
			}else if(invtySubSetTabDao.selectInvtySubjSetTabByOrdrNum(invtySubjSetTab.getOrdrNum())==null) {
				on.put("isSuccess", false);
				on.put("message", "����ʧ�ܣ����"+invtySubjSetTab.getOrdrNum()+"�����ڣ�");
			}else if(invtyClsDao.selectInvtyClsByInvtyClsEncd(invtySubjSetTab.getInvtyBigClsEncd())==null){
				on.put("isSuccess", false);
				on.put("message", "���������"+invtySubjSetTab.getInvtyBigClsEncd()+"�����ڣ�����ʧ�ܣ�");
			}else if(acctItmDocDao.selectAcctItmDocBySubjId(invtySubjSetTab.getInvtySubjId())==null){
				on.put("isSuccess", false);
				on.put("message", "�����Ŀ���"+invtySubjSetTab.getInvtySubjId()+"�����ڣ�����ʧ�ܣ�");
			}else if(acctItmDocDao.selectAcctItmDocBySubjId(invtySubjSetTab.getEntrsAgnSubjId())==null){
				on.put("isSuccess", false);
				on.put("message", "ί�д�����Ŀ���"+invtySubjSetTab.getEntrsAgnSubjId()+"�����ڣ�����ʧ�ܣ�");
			}else {
				int updateResult = invtySubSetTabDao.updateInvtySubjSetTabByOrdrNum(invtySubjSetTab);
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

	//����ɾ��
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
			on.put("message", "ɾ��ʧ�ܣ����"+ordrNum+"�����ڣ�");
		}else {
			int deleteResult = invtySubSetTabDao.deleteInvtySubjSetTabByOrdrNum(ordrNum);
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

	//���տ�Ŀ��Ų�ѯ
	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public InvtySubjSetTab selectInvtySubjSetTabByOrdrNum(Integer vouchCateId) {
		InvtySubjSetTab selectByOrdrNum = invtySubSetTabDao.selectInvtySubjSetTabByOrdrNum(vouchCateId);
		return selectByOrdrNum;
	}

	//��ҳ��ѯ
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
			resp=BaseJson.returnRespList("/account/invtySubjSetTab/selectInvtySubjSetTab", true, "��ѯ�ɹ���", count,pageNo,pageSize,listNum,pages, list);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	//����ɾ��
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
			    message="ɾ���ɹ���";
			}else {
				isSuccess=false;
				message="ɾ��ʧ�ܣ�";
			}
			
			resp=BaseJson.returnRespObj("account/invtySubjSetTab/deleteInvtySubjSetTabList", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}
	//��ҳ��ѯ��ӡ
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public String selectInvtySubjSetTabPrint(Map map) {
        String resp = "";
        List<InvtySubjSetTab> list = invtySubSetTabDao.selectInvtySubjSetTabList(map);     
        try {
            resp = BaseJson.returnRespListAnno("/account/invtySubjSetTab/selectInvtySubjSetTabListPrint", true, "��ѯ�ɹ���", list);
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
                throw new RuntimeException("�����ظ����� " + entry.getValue().getInvtyBigClsEncd() + " ����");
            }

            try {
                invtySubSetTabDao.insertInvtySubjSetTab(entry.getValue());
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                throw new RuntimeException("����sql����");
            }
        }

        isSuccess = true;
        message = "���������ɹ���";
        try {
            resp = BaseJson.returnRespObj("account/invtySubjSetTab/uploadFileAddDb", isSuccess, message, null);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return resp;

    }

    // ����excle
    private Map<String, InvtySubjSetTab> uploadScoreInfo(MultipartFile file) {
        Map<String, InvtySubjSetTab> temp = new HashMap<>();
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

                String orderNo = GetCellData(r, "����������");
                // ����ʵ����
                InvtySubjSetTab invtySubjSetTab = new InvtySubjSetTab();
                if (temp.containsKey(orderNo)) {
                    invtySubjSetTab = temp.get(orderNo);
                }
                // r.getCell(0).setCellType(Cell.CELL_TYPE_NUMERIC);
//                      System.out.println(r.getCell(0));
                // ȡ����ǰ�е�1����Ԫ�����ݣ�����װ��pursOrdrʵ��ĸ���������
//                private Integer ordrNum;//���
                invtySubjSetTab.setInvtyBigClsEncd(orderNo);// ���������
                invtySubjSetTab.setInvtySubjId(GetCellData(r, "�����Ŀ����"));// �����Ŀ���
                invtySubjSetTab.setEntrsAgnSubjId(GetCellData(r, "ί�д�����Ŀ����"));// ί�д�����Ŀ���
                invtySubjSetTab.setMemo(GetCellData(r, "��ע"));// ��ע
//                private String invtyClsNm;//�����������
//                private String invtySubjNm;//�����Ŀ����
//                private String entrsAgnSubjNm;//ί�д�����Ŀ����

                temp.put(orderNo, invtySubjSetTab);

            }
            fileIn.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("������ʽ�����" + j + "��"+e.getMessage());
        }
        return temp;
    }

}
