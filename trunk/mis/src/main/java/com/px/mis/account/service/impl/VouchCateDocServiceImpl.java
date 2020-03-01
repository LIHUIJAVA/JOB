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
	//���
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
			on.put("message", "����ʧ��,��������Ϊ��");
		}else if(vouchCateDocDao.selectVouchCateDocByVouchCateWor(vouchCateWor)!=null){
			on.put("isSuccess", false);
			on.put("message", "����"+vouchCateWor+"�Ѵ��ڣ�����ʧ�ܣ�");
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
				on.put("message", "��������ʧ��");
			}
		}
		return on;
	}
	//����
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
				on.put("message", "����ʧ��,��������Ϊ��");
			}else if(vouchCateDocDao.selectVouchCateDocByVouchCateWor(vouchCateWor)==null){
				on.put("isSuccess", false);
				on.put("message", "����"+vouchCateWor+"�����ڣ�����ʧ�ܣ�");
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
					on.put("message", "���´���ɹ�");
				}else {
					on.put("isSuccess", false);
					on.put("message", "���´���ʧ��");
				}
			}
		}
		
		return on;
	}
	//ɾ��
	@Override
	public String deleteVouchCateDocByVouchCateWor(String vouchCateWor) throws IOException {
		String resp = "";
		String message = "";
		boolean isSuccess = true;
		List<String> list = getList(vouchCateWor);
		Integer deleteResult = vouchCateDocDao.deleteVouchCateDocByVouchCateWor(list);
/*		deleteResult = vouchCateDocDao.deleteVouchCateDocByVouchCateWor(vouchCateWor);*/
		if(deleteResult>=1) {
			message = "����ɹ�";
		}else if(deleteResult==0){
			isSuccess = false;
			message = "û��Ҫɾ��������";
		}
		
		resp = BaseJson.returnRespObj("/account/vouchCateDoc/deleteVouchCateDoc", isSuccess, message, null);
		return resp;
		
	}
	/**
	 * id����list
	 * 
	 * @param id id(����Ѷ��ŷָ�)
	 * @return List����
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

	//���������ҳ��ѯ
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
			resp=BaseJson.returnRespList("/account/vouchCateDoc/selectVouchCateDoc", true, "��ѯ�ɹ���", count,pageNo,pageSize,listNum,pages, list);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}
	//���������ҳ��ѯ��ӡ
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public String selectVouchCateDocPrint(Map map) {
        String resp = "";
    	map.put("index",null);
		map.put("num",null);
        List<VouchCateDoc> list = vouchCateDocDao.selectVouchCateDocList(map);
        try {
            resp = BaseJson.returnRespList("/account/vouchCateDoc/selectVouchCateDocPrint", true, "��ѯ�ɹ���", list);
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
                throw new RuntimeException("�����ظ����� " + entry.getValue().getVouchCateNm() + " ����");
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
                throw new RuntimeException("����sql����");
            }
        }

        isSuccess = true;
        message = "ƾ֤�����ɹ���";
        try {
            resp = BaseJson.returnRespObj("account/vouchCateDoc/uploadFileAddDb", isSuccess, message, null);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return resp;

    }

    // ����excle
    private Map<String, VouchCateDoc> uploadScoreInfo(MultipartFile file) {
        Map<String, VouchCateDoc> temp = new HashMap<>();
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

                String orderNo = GetCellData(r, "ƾ֤�����");
                // ����ʵ����
                VouchCateDoc vouchCateDoc = new VouchCateDoc();
                if (temp.containsKey(orderNo)) {
                    vouchCateDoc = temp.get(orderNo);
                }
                List<VouchCateDocSubTab> vouchCateDocSubTabs = vouchCateDoc.getLmtSubjList();// �����ӱ�
                if (vouchCateDocSubTabs == null) {
                    vouchCateDocSubTabs = new ArrayList<>();
                }

                // r.getCell(0).setCellType(Cell.CELL_TYPE_NUMERIC);
//                      System.out.println(r.getCell(0));
                // ȡ����ǰ�е�1����Ԫ�����ݣ�����װ��pursOrdrʵ��ĸ���������
//                vouchCateDoc.setOrdrNum(Integer.valueOf(GetCellData(r, "���")));//���
                vouchCateDoc.setVouchCateWor(orderNo);//ƾ֤����֣�
                vouchCateDoc.setVouchCateNm(GetCellData(r, "ƾ֤�������"));//ƾ֤�������
//                vouchCateDoc.setVouchCateSortNum(GetCellData(r, "ƾ֤��������"));//ƾ֤��������
                vouchCateDoc.setLmtMode(GetCellData(r, "��������"));//���Ʒ�ʽ
                vouchCateDoc.setMemo(GetCellData(r, "��ע"));//��ע
                
                
                VouchCateDocSubTab vouchCateDocSubTab=new VouchCateDocSubTab();
                vouchCateDocSubTab.setVouchCateWor(orderNo);//ƾ֤�������
//                vouchCateDocSubTab.setLmtSubjId(GetCellData(r, "���޿�Ŀ���"));//���޿�Ŀ���
//                vouchCateDocSubTab.setOrdrNum(ordrNum);

                vouchCateDocSubTabs.add(vouchCateDocSubTab);

                vouchCateDoc.setLmtSubjList(vouchCateDocSubTabs);

                temp.put(orderNo, vouchCateDoc);

            }
            fileIn.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("������ʽ�����" + j + "��"+e.getMessage());
        }
        return temp;
    }
}
