package com.px.mis.whs.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.px.mis.whs.entity.CheckSnglMap;
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
import com.px.mis.whs.dao.GdsBitMapper;
import com.px.mis.whs.entity.GdsBit;
import com.px.mis.whs.entity.GdsBitTyp;
import com.px.mis.whs.service.GdsBitService;

//��λ����
@Service
@Transactional
public class GdsBitServiceImpl extends poiTool implements GdsBitService {

    @Autowired
    GdsBitMapper gdsBitMapper;

    // ������λ����
    @Override
    public ObjectNode insertGdsBit(GdsBit record) {
        ObjectNode on = null;
        try {
            on = JacksonUtil.getObjectNode("");
//		if (record.getRegnEncd() != null && record.getRegnEncd().length() > 0) {
//			record.setGdsBitEncd(record.getRegnEncd() + "-" + record.getGdsBitEncd());
//		} else {
//			on.put("isSuccess", false);
//			on.put("message", "�������������");
//			return on;
//		}
        GdsBit records = gdsBitMapper.selectGdsBitbyId(record.getGdsBitEncd());
        if (records != null) {
            on.put("isSuccess", false);
            on.put("message", record.getGdsBitEncd() + " ��λ�����ظ���");
            return on;
        }
        int gdsBit = gdsBitMapper.insertGdsBit(record);
        if (gdsBit == 1) {
            on.put("isSuccess", true);
            on.put("message", "�����ɹ���");
        } else {
            on.put("isSuccess", false);
            on.put("message", "����ʧ�ܣ�");
        }
    } catch (Exception e) {
        throw new RuntimeException("����ʧ��");

    }
        return on;
    }

    // �޸Ļ�λ����
    @Override
    public ObjectNode updateGdsBit(GdsBit record) {
        ObjectNode on = null;
        try {
            on = JacksonUtil.getObjectNode("");
        } catch (Exception e) {

        }

        int gdsBit = gdsBitMapper.updateGdsBit(record);
        if (gdsBit == 1) {
            on.put("isSuccess", true);
            on.put("message", "�޸ĳɹ���");
        } else {
            on.put("isSuccess", false);
            on.put("message", "�޸�ʧ�ܣ�");
        }
        return on;
    }

    // ɾ����λ����
    @Override
    public ObjectNode deleteGdsBit(String gdsBitEncd) {
        ObjectNode on = null;
        try {
            on = JacksonUtil.getObjectNode("");
        } catch (Exception e) {

        }

        int gdsBit = gdsBitMapper.deleteGdsBit(gdsBitEncd);
        if (gdsBit == 1) {
            on.put("isSuccess", true);
            on.put("message", "ɾ���ɹ���");
        } else {
            on.put("isSuccess", false);
            on.put("message", "ɾ��ʧ�ܣ�");
        }
        return on;
    }

    // �򵥲� ��λ����
    @Override
    public GdsBit selectGdsBit(String gdsBitEncd) {
        GdsBit gCorp = gdsBitMapper.selectGdsBit(gdsBitEncd);
        return gCorp;
    }

    @Override
    public List<GdsBit> selectGdsBitList(String gdsBitEncd) {
        List<GdsBit> gList = gdsBitMapper.selectGdsBitList(gdsBitEncd);
        return gList;
    }

    // ��ɾ
    @Override
    public String deleteGdsBitList(String gdsBitEncd) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";

        List<String> list = getList(gdsBitEncd);

        if (gdsBitMapper.selectGBitList(list).size() > 0) {
            gdsBitMapper.deleteGdsBitList(list);
            isSuccess = true;
            message = "ɾ���ɹ���";
        } else {
            isSuccess = false;
            message = "���" + gdsBitEncd + "�����ڣ�";
        }

        try {
            resp = BaseJson.returnRespObj("whs/gds_bit/deleteGdsBitList", isSuccess, message, null);
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

    // ��ҳ��ѯ
    @Override
    public String queryList(Map map) {
        String resp = "";
        List<GdsBit> aList = gdsBitMapper.selectList(map);
        int count = gdsBitMapper.selectCount(map);

        int pageNo = Integer.parseInt(map.get("pageNo").toString());
        int pageSize = Integer.parseInt(map.get("pageSize").toString());
        int listNum = aList.size();
        int pages = count / pageSize;
        if (count % pageSize > 0) {
            pages += 1;
        }
        try {
            resp = BaseJson.returnRespList("whs/gds_bit/queryList", true, "��ѯ�ɹ���", count, pageNo, pageSize, listNum,
                    pages, aList);
        } catch (IOException e) {

        }
        return resp;
    }

    // ��ѯ���л�λ����
    @Override
    public String selectgTypList() {
        String resp = "";
        List<GdsBitTyp> gList = gdsBitMapper.selectgTypList();

        try {
            resp = BaseJson.returnRespObjList("whs/gds_bit/selectgTypList", true, "��ѯ�ɹ���", null, gList);
        } catch (IOException e) {

        }
        return resp;
    }

    // ��ӡ
    @Override
    public String queryListDaYin(Map map) {
        String resp = "";
        List<GdsBit> aList = gdsBitMapper.selectListDaYin(map);
        aList.add(new GdsBit());

        try {
            resp = BaseJson.returnRespObjListAnno("whs/gds_bit/queryListDaYin", true, "��ѯ�ɹ���", null, aList);
        } catch (IOException e) {

        }
        return resp;
    }

    // ����
    public String uploadFileAddDb(MultipartFile file) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        Map<String, GdsBit> gdsBitMap = uploadScoreInfo(file);
        for (Map.Entry<String, GdsBit> entry : gdsBitMap.entrySet()) {
            if (gdsBitMapper.selectGdsBit(entry.getValue().getGdsBitEncd()) != null) {
                isSuccess = false;
                message = "��λ�����в��ֵ����Ѵ��ڣ��޷����룬������ٵ��룡";
                throw new RuntimeException(message);
            } else {
                gdsBitMapper.insertGdsBit(entry.getValue());
                isSuccess = true;
                message = "��λ��������ɹ���";
            }
        }
        try {
            resp = BaseJson.returnRespObj("whs/gds_bit/uploadGdsBitFile", isSuccess, message, null);
        } catch (IOException e) {

        }
        return resp;
    }

    // ����excle
    private Map<String, GdsBit> uploadScoreInfo(MultipartFile file) {
        int j = 1;
        Map<String, GdsBit> temp = new HashMap<>();
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
                String orderNo = GetCellData(r, "��λ����");
                // ����ʵ����
                GdsBit gdsBit = new GdsBit();
                if (temp.containsKey(orderNo)) {
                    gdsBit = temp.get(orderNo);
                }
                // ȡ����ǰ�е�1����Ԫ�����ݣ�����װ��ʵ��ĸ���������
                gdsBit.setGdsBitEncd(orderNo);// ��λ����
//                gdsBit.setRegnEncd(GetCellData(r, "�������"));// �������
                gdsBit.setGdsBitNm(GetCellData(r, "��λ����"));// ��λ����
                gdsBit.setGdsBitTyp(GetCellData(r, "��λ���ͱ���"));// ��λ����
//                gdsBit.setGdsBitTypEncd(GetCellData(r, "��λ����"));// ��λ���ͱ���
                gdsBit.setGdsBitCd(GetCellData(r, "��λ����"));// ��λ����
                gdsBit.setGdsBitQty(GetBigDecimal(GetCellData(r, "��λ�����"), 8));// ��λ�����
                gdsBit.setMemo(GetCellData(r, "��ע"));// ��ע
//                gdsBit.setGdsBitCdEncd(GetCellData(r, "��λ�����"));// ��λ�����
                gdsBit.setSetupPers(GetCellData(r, "������"));// ������
                if (GetCellData(r, "����ʱ��") == null || GetCellData(r, "����ʱ��").equals("")) {
                    gdsBit.setSetupTm(null);
                } else {
                    gdsBit.setSetupTm(GetCellData(r, "����ʱ��"));// ����ʱ��
                }
                gdsBit.setMdfr(GetCellData(r, "�޸���"));// �޸���
                if (GetCellData(r, "�޸�ʱ��") == null || GetCellData(r, "�޸�ʱ��").equals("")) {
                    gdsBit.setModiTm(null);
                } else {
                    gdsBit.setModiTm(GetCellData(r, "�޸�ʱ��"));// �޸�ʱ��
                }
                temp.put(orderNo, gdsBit);
            }
            fileIn.close();
        } catch (Exception e) {

            throw new RuntimeException("�ļ��ĵ�" + j + "�е����ʽ����" +e.getMessage());
        }
        return temp;
    }

}
