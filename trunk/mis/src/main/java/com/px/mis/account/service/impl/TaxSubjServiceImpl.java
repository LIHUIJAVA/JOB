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
			on.put("message", "�ɹ���Ŀ����"+taxSubj.getPursSubjEncd()+"�����ڣ�����ʧ�ܣ�");
		}else if(provrClsDao.selectProvrClsByProvrClsId(taxSubj.getProvrClsEncd())==null){
			on.put("isSuccess", false);
			on.put("message", "��Ӧ�̷������"+taxSubj.getProvrClsEncd()+"�����ڣ�����ʧ�ܣ�");
		}else if(acctItmDocDao.selectAcctItmDocBySubjId(taxSubj.getPursTaxEncd())==null){
			on.put("isSuccess", false);
			on.put("message", "�ɹ�˰�����"+taxSubj.getPursTaxEncd()+"�����ڣ�����ʧ�ܣ�");
		}else {
			int insertResult = taxSubjDao.insertTaxSubj(taxSubj);
			if(insertResult==1) {
				on.put("isSuccess", true);
				on.put("message", "�����ɹ�");
			}else {
				on.put("isSuccess", false);
				on.put("message", "����ʧ��");
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
				on.put("message", "����ʧ�ܣ����"+taxSubj.getAutoId()+"�����ڣ�");
			}else if(provrClsDao.selectProvrClsByProvrClsId(taxSubj.getProvrClsEncd())==null){
				on.put("isSuccess", false);
				on.put("message", "��Ӧ�̷������"+taxSubj.getProvrClsEncd()+"�����ڣ�����ʧ�ܣ�");
			}else if(acctItmDocDao.selectAcctItmDocBySubjId(taxSubj.getPursSubjEncd())==null){
				on.put("isSuccess", false);
				on.put("message", "�ɹ���Ŀ����"+taxSubj.getPursSubjEncd()+"�����ڣ�����ʧ�ܣ�");
			}else if(acctItmDocDao.selectAcctItmDocBySubjId(taxSubj.getPursTaxEncd())==null){
				on.put("isSuccess", false);
				on.put("message", "�ɹ�˰�����"+taxSubj.getPursTaxEncd()+"�����ڣ�����ʧ�ܣ�");
			}else {
				int updateResult = taxSubjDao.updateTaxSubjById(taxSubj);
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
			on.put("message", "����ɹ�");
		}else if(deleteResult==0){
			on.put("isSuccess", true);
			on.put("message", "û��Ҫɾ��������");		
		}else {
			on.put("isSuccess", false);
			on.put("message", "ɾ��ʧ��");
		}
		
		return on;
	}
	/**
	 * id����list
	 * 
	 * @param id id(����Ѷ��ŷָ�)
	 * @return List����
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
			resp=BaseJson.returnRespList("/account/taxSubj/selectTaxSubj", true, "��ѯ�ɹ���", count,pageNo,pageSize,listNum,pages, list);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}
	//��ӡ
	@Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public String selectTaxSubjPrint(Map map) {
        String resp = "";
        List<TaxSubj> list = taxSubjDao.selectTaxSubjList(map);

        try {
            resp = BaseJson.returnRespListAnno("/account/taxSubj/selectTaxSubjListPrint", true, "��ѯ�ɹ���",list);
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
//	                throw new RuntimeException("�����ظ����� " + entry.getValue().getFormNum() + " ����");
//	            }
            try {
                taxSubjDao.insertTaxSubj(entry.getValue());
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                throw new RuntimeException("����sql����");
            }
			try {
				Map<String,String> promap = new HashMap<>();
				promap.put("provrId",entry.getValue().getProvrClsEncd());
				promap.put("provrNm",entry.getValue().getProvrClsNm());
//				provrClsDao.insertProvrClsToLead(promap);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				throw new RuntimeException("����sql����");
			}
			try {
				Map<String,String> promap = new HashMap<>();
				promap.put("pursId",entry.getValue().getPursSubjEncd());
				promap.put("pursNm",entry.getValue().getPursSubjNm());
				acctItmDocDao.insertTolead(promap);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				throw new RuntimeException("����sql����");
			}
        }

        isSuccess = true;
        message = "���������ɹ���";
        try {
            resp = BaseJson.returnRespObj("account/taxSubj/uploadFileAddDb", isSuccess, message, null);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return resp;
    }

	    // ����excle
    private Map<String, TaxSubj> uploadScoreInfo(MultipartFile file) {
        Map<String, TaxSubj> temp = new HashMap<>();
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
                String orderNo = GetCellData(r, "���");
                Double orderNO1 = Double.parseDouble(orderNo);
                int orderNo2 = orderNO1.intValue();
                // ����ʵ����
                TaxSubj taxSubj = new TaxSubj();
                if (temp.containsKey(orderNo)) {
                    taxSubj = temp.get(orderNo);
                }

                // r.getCell(0).setCellType(Cell.CELL_TYPE_NUMERIC);
//	                      System.out.println(r.getCell(0));
                // ȡ����ǰ�е�1����Ԫ�����ݣ�����װ��pursOrdrʵ��ĸ���������
                taxSubj.setAutoId(Integer.valueOf(orderNo2));// �Զ�id
                taxSubj.setProvrClsEncd(GetCellData(r, "��Ӧ�̷������"));// ��Ӧ�̷�����룻
				taxSubj.setProvrClsNm(GetCellData(r,"��Ӧ�̷�������"));//��Ӧ�̷�������
                taxSubj.setPursSubjEncd(GetCellData(r, "�ɹ���Ŀ����"));// �ɹ���Ŀ���룻
				taxSubj.setPursSubjNm(GetCellData(r,"�ɹ���Ŀ����"));//�ɹ���Ŀ����
                taxSubj.setPursTaxEncd(GetCellData(r, "�ɹ�˰�����"));// �ɹ�˰����룻
                taxSubj.setPursTaxNm(GetCellData(r, "�ɹ�˰���Ŀ"));// �ɹ�˰����룻



//	                 String provrClsNm;//��Ӧ�̷�������
//	                 String pursSubjNm;//�ɹ���Ŀ����
//	                 String pursTaxNm;//�ɹ�˰������

                temp.put(orderNo, taxSubj);

            }
            fileIn.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("������ʽ�����" + j + "��"+e.getMessage());
        }
        return temp;
    }

}
