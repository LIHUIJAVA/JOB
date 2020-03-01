package com.px.mis.whs.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.px.mis.whs.entity.ExpressCorpMap;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;
import com.px.mis.util.poiTool;
import com.px.mis.whs.dao.GdFlowCorpMapper;
import com.px.mis.whs.entity.GdFlowCorp;
import com.px.mis.whs.entity.GdFlowCorpMap;
import com.px.mis.whs.service.GdFlowCorpService;

@Service
@Transactional
public class GdFlowCorpServiceImpl extends poiTool implements GdFlowCorpService {

    @Autowired
    GdFlowCorpMapper gdFlowCorpMapper;

    // ����������˾
    @Override
    public ObjectNode insertGdFlowCorp(GdFlowCorp record) {

        ObjectNode on = null;
        try {
            on = JacksonUtil.getObjectNode("");
        } catch (Exception e) {

        }
        GdFlowCorp records = gdFlowCorpMapper.selectGdFlowCorp(record.getGdFlowEncd());
        if (records != null) {
            on.put("isSuccess", false);
            on.put("message", record.getGdFlowEncd() + " �����ظ���");
            return on;
        }
        int gdFlow = gdFlowCorpMapper.insertGdFlowCorp(record);
        if (gdFlow == 1) {
            on.put("isSuccess", true);
            on.put("message", "�����ɹ���");
        } else {
            on.put("isSuccess", false);
            on.put("message", "����ʧ�ܣ�");
        }
        return on;
    }

    // �޸�������˾
    @Override
    public ObjectNode updateGdFlowCorp(GdFlowCorp record) {

        ObjectNode on = null;
        try {
            on = JacksonUtil.getObjectNode("");
        } catch (Exception e) {

        }

        int gdFlow = gdFlowCorpMapper.updateGdFlowCorp(record);
        if (gdFlow == 1) {
            on.put("isSuccess", true);
            on.put("message", "�޸ĳɹ���");
        } else {
            on.put("isSuccess", false);
            on.put("message", "�޸�ʧ�ܣ�");
        }
        return on;
    }

    // ɾ��������˾
    @Override
    public ObjectNode deleteGdFlowCorp(String gdFlowEncd) {

        ObjectNode on = null;
        try {
            on = JacksonUtil.getObjectNode("");
        } catch (Exception e) {

        }

        int gdFlow = gdFlowCorpMapper.deleteGdFlowCorp(gdFlowEncd);
        if (gdFlow == 1) {
            on.put("isSuccess", true);
            on.put("message", "ɾ���ɹ���");
        } else {
            on.put("isSuccess", false);
            on.put("message", "ɾ��ʧ�ܣ�");
        }
        return on;
    }

    @Override
    public GdFlowCorp selectGdFlowCorp(String gdFlowEncd) {
        GdFlowCorp gdFlowCorp = gdFlowCorpMapper.selectGdFlowCorp(gdFlowEncd);
        return gdFlowCorp;
    }

    @Override
    public List<GdFlowCorp> selectGdFlowCorpList(String gdFlowEncd) {
        List<GdFlowCorp> gdFlowCorpList = gdFlowCorpMapper.selectGdFlowCorpList(gdFlowEncd);
        return gdFlowCorpList;
    }

    // ��ҳ��ѯ
    @Override
    public String queryList(Map map) {
        String resp = "";
        List<GdFlowCorp> aList = gdFlowCorpMapper.selectList(map);
        int count = gdFlowCorpMapper.selectCount(map);

        int pageNo = Integer.parseInt(map.get("pageNo").toString());
        int pageSize = Integer.parseInt(map.get("pageSize").toString());
        int listNum = aList.size();
        int pages = count / pageSize;
        if (count % pageSize > 0) {
            pages += 1;
        }
        try {
            resp = BaseJson.returnRespList("whs/gd_flow_crop/queryList", true, "��ѯ�ɹ���", count, pageNo, pageSize,
                    listNum, pages, aList);
        } catch (IOException e) {

        }
        return resp;
    }

    // ��ɾ
    @Override
    public String deleteGFlowCorpList(String gdFlowEncd) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";

        List<String> list = getList(gdFlowEncd);

        if (gdFlowCorpMapper.selectGFlowCorpAllList(list).size() > 0) {
            gdFlowCorpMapper.deleteGFlowCorpList(list);
            isSuccess = true;
            message = "ɾ���ɹ���";
        } else {
            isSuccess = false;
            message = "���" + gdFlowEncd + "�����ڣ�";
        }

        try {
            resp = BaseJson.returnRespObj("whs/gd_flow_crop/deleteGFlowCorpList", isSuccess, message, null);
        } catch (IOException e) {

        }
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

    // ��ӡ
    @Override
    public String queryListDaYin(Map map) {
        String resp = "";
        List<GdFlowCorpMap> aList = gdFlowCorpMapper.selectListDaYin(map);
        aList.add(new GdFlowCorpMap());
        try {
            resp = BaseJson.returnRespObjListAnno("whs/gd_flow_crop/queryListDaYin", true, "��ѯ�ɹ���", null, aList);
        } catch (IOException e) {

        }
        return resp;
    }

    // ����
    public String uploadFileAddDb(MultipartFile file) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        Map<String, GdFlowCorp> gdFlowCropMap = uploadScoreInfo(file);
        for (Map.Entry<String, GdFlowCorp> entry : gdFlowCropMap.entrySet()) {
            if (gdFlowCorpMapper.selectGdFlowCorp(entry.getValue().getGdFlowEncd()) != null) {
                isSuccess = false;
                message = "���������в��ֵ����Ѵ��ڣ��޷����룬������ٵ��룡";
                throw new RuntimeException(message);
            } else {
                gdFlowCorpMapper.insertGdFlowCorp(entry.getValue());
                isSuccess = true;
                message = "������������ɹ���";
            }
        }
        try {
            resp = BaseJson.returnRespObj("whs/gd_flow_crop/uploadGdFlowCropFile", isSuccess, message, null);
        } catch (IOException e) {

        }
        return resp;
    }

    /*
     * //�ж��Ƿ��ֶ� public String GetCellData(Row row,String ColNameCN) throws Exception
     * { Map<String,Integer> ColMap= new HashMap<String, Integer>(); Object obj =
     * ColMap.get(ColNameCN.trim()); if(obj==null) { throw new Exception("���������� [" +
     * ColNameCN +"],����"); } String result = getValue(row.getCell((int)obj));
     * if((result == null)||result.trim().equals("")) { if(ColNameCN.equals("�Ƿ����"))
     * result = "0"; } return result==null?result:result.trim(); }
     */
    // ����excle
    private Map<String, GdFlowCorp> uploadScoreInfo(MultipartFile file) {
        Map<String, GdFlowCorp> temp = new HashMap<>();
        int j = 1;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
                String orderNo = GetCellData(r, "������˾����");
                // ����ʵ����
                GdFlowCorp gdFlowCorp = new GdFlowCorp();
                if (temp.containsKey(orderNo)) {
                    gdFlowCorp = temp.get(orderNo);
                }
                // ȡ����ǰ�е�1����Ԫ�����ݣ�����װ��ʵ��ĸ���������
                gdFlowCorp.setGdFlowEncd(orderNo);// ������˾���
                gdFlowCorp.setGdFlowNm(GetCellData(r, "������˾����"));// ������˾����
                gdFlowCorp.setTraffMode(GetCellData(r, "���䷽ʽ"));// ���䷽ʽ
                gdFlowCorp.setTraffVehic(GetCellData(r, "���䳵��"));// ���䳵��
//                gdFlowCorp.setTraffFormNum(GetCellData(r, "���䵥��"));// ���䵥��
//                gdFlowCorp.setPrintCnt(new Double(GetCellData(r, "��ӡ����")).intValue());// ��ӡ����
                gdFlowCorp.setMemo(GetCellData(r, "��ע"));// ��ע
//                gdFlowCorp.setIsNtChk(new Double(GetCellData(r, "�Ƿ����")).intValue());// �Ƿ����
                gdFlowCorp.setSetupPers(GetCellData(r, "������"));// ������
                if (GetCellData(r, "����ʱ��") == null || GetCellData(r, "����ʱ��").equals("")) {
                    gdFlowCorp.setSetupTm(null);
                } else {
                    gdFlowCorp.setSetupTm(GetCellData(r, "����ʱ��"));// ����ʱ��
                }
                gdFlowCorp.setMdfr(GetCellData(r, "�޸���"));// �޸���
                if (GetCellData(r, "�޸�ʱ��") == null || GetCellData(r, "�޸�ʱ��").equals("")) {
                    gdFlowCorp.setModiTm(null);
                } else {
                    gdFlowCorp.setModiTm(GetCellData(r, "�޸�ʱ��"));// �޸�ʱ��
                }
//                gdFlowCorp.setChkr(GetCellData(r, "�����"));// �����
//                if (GetCellData(r, "���ʱ��") == null || GetCellData(r, "���ʱ��").equals("")) {
//                    gdFlowCorp.setChkTm(null);
//                } else {
//                    gdFlowCorp.setChkTm(GetCellData(r, "���ʱ��"));// ���ʱ��
//                }
                temp.put(orderNo, gdFlowCorp);
            }
            fileIn.close();
        } catch (Exception e) {
            throw new RuntimeException("�ļ��ĵ�" + j + "�е����ʽ����" +e.getMessage());
        }
        return temp;
    }

}
