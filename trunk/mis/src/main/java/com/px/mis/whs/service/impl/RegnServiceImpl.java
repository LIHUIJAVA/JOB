package com.px.mis.whs.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.px.mis.whs.entity.ProdStruMap;
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
import com.px.mis.whs.dao.RegnMapper;
import com.px.mis.whs.entity.Regn;
import com.px.mis.whs.service.RegnService;

@Service
@Transactional
public class RegnServiceImpl extends poiTool implements RegnService {

    @Autowired
    RegnMapper regnMapper;

    // �������򵵰�
    @Override
    public ObjectNode insertRegn(Regn record) {
        ObjectNode on = null;
        try {
            on = JacksonUtil.getObjectNode("");

//			if (record.getWhsEncd() != null && record.getWhsEncd().length() > 0) {
//				record.setRegnEncd(record.getWhsEncd() + "-" + record.getRegnEncd());
//			} else {
//				on.put("isSuccess", false);
//				on.put("message", "������ֿ����");
//				return on;
//			}
            Regn records = regnMapper.selectRegn(record.getRegnEncd());
            if (records != null) {
                on.put("isSuccess", false);
                on.put("message", record.getRegnEncd() + " ��������ظ���");
                return on;
            }

            int Regn = regnMapper.insertRegn(record);
            if (Regn == 1) {
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

    // �޸����򵵰�
    @Override
    public ObjectNode updateRegn(Regn record) {
        ObjectNode on = null;
        try {
            on = JacksonUtil.getObjectNode("");
        } catch (Exception e) {

        }


        int Regn = regnMapper.updateRegn(record);
        if (Regn == 1) {
            on.put("isSuccess", true);
            on.put("message", "�޸ĳɹ���");
        } else {
            on.put("isSuccess", false);
            on.put("message", "�޸�ʧ�ܣ�");
        }
        return on;
    }

    // ɾ�����򵵰�
    @Override
    public ObjectNode deleteRegn(String regnEncd) {
        ObjectNode on = null;
        try {
            on = JacksonUtil.getObjectNode("");
        } catch (Exception e) {

        }

        int Regn = regnMapper.deleteRegn(regnEncd);
        if (Regn == 1) {
            on.put("isSuccess", true);
            on.put("message", "ɾ���ɹ���");
        } else {
            on.put("isSuccess", false);
            on.put("message", "ɾ��ʧ�ܣ�");
        }
        return on;
    }

    // �򵥲� ���򵵰�
    @Override
    public Regn selectRegn(String regnEncd) {

        Regn regn = regnMapper.selectRegn(regnEncd);
        return regn;
    }

    @Override
    public List<Regn> selectRegnList(String regnEncd) {

        List<Regn> regnList = regnMapper.selectRegnList(regnEncd);
        return regnList;
    }

    // ��ɾ
    @Override
    public String deleteRegnList(String regnEncd) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";

        List<String> list = getList(regnEncd);

        if (regnMapper.selectRegnAllList(list).size() > 0) {
            regnMapper.deleteRegnList(list);
            isSuccess = true;
            message = "ɾ���ɹ���";
        } else {
            isSuccess = false;
            message = "���" + regnEncd + "�����ڣ�";
        }

        try {
            resp = BaseJson.returnRespObj("whs/regn/deleteRegnList", isSuccess, message, null);
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

    @Override
    public String queryList(Map map) {
        String resp = "";
        List<Regn> aList = regnMapper.selectList(map);
        int count = regnMapper.selectCount(map);

        int pageNo = Integer.parseInt(map.get("pageNo").toString());
        int pageSize = Integer.parseInt(map.get("pageSize").toString());
        int listNum = aList.size();
        int pages = count / pageSize;
        if (count % pageSize > 0) {
            pages += 1;
        }
        try {
            resp = BaseJson.returnRespList("whs/regn/queryList", true, "��ѯ�ɹ���", count, pageNo, pageSize, listNum, pages,
                    aList);
        } catch (IOException e) {

        }
        return resp;
    }

    // ��ӡ
    @Override
    public String queryListDaYin(Map map) {
        String resp = "";
        List<Regn> aList = regnMapper.selectListDaYin(map);
        aList.add(new Regn());
        try {
            resp = BaseJson.returnRespObjListAnno("whs/regn/queryListDaYin", true, "��ѯ�ɹ���", null, aList);
        } catch (IOException e) {

        }
        return resp;
    }

    // ����
    public String uploadFileAddDb(MultipartFile file) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        Map<String, Regn> regnMap = uploadScoreInfo(file);
        for (Map.Entry<String, Regn> entry : regnMap.entrySet()) {
            if (regnMapper.selectRegn(entry.getValue().getRegnEncd()) != null) {
                isSuccess = false;
                message = "���򵵰��в��ֵ����Ѵ��ڣ��޷����룬������ٵ��룡";
                throw new RuntimeException(message);
            } else {
                regnMapper.insertRegn(entry.getValue());
                isSuccess = true;
                message = "���򵵰�����ɹ���";
            }
        }
        try {
            resp = BaseJson.returnRespObj("whs/regn/uploadRegnFile", isSuccess, message, null);
        } catch (IOException e) {

        }
        return resp;
    }

    // ����excle
    private Map<String, Regn> uploadScoreInfo(MultipartFile file) {
        Map<String, Regn> temp = new HashMap<>();
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
                String orderNo = GetCellData(r, "�������");
                // ����ʵ����
                Regn regn = new Regn();
                if (temp.containsKey(orderNo)) {
                    regn = temp.get(orderNo);
                }
                // ȡ����ǰ�е�1����Ԫ�����ݣ�����װ��ʵ��ĸ���������
                regn.setRegnEncd(orderNo);// �������
//                regn.setWhsEncd(GetCellData(r, "�ֿ����"));// �ֿ����
                regn.setRegnNm(GetCellData(r, "��������"));// ��������
                regn.setLongs(
                        (new Double(GetCellData(r, "��") == null || GetCellData(r, "��").equals("") ? "0"
                                : GetCellData(r, "��"))).intValue()+"");// ��
                regn.setWide(
                        (new Double(GetCellData(r, "��") == null || GetCellData(r, "��").equals("") ? "0"
                                : GetCellData(r, "��"))).intValue()+"");// ��
                regn.setSiteQty(GetBigDecimal(GetCellData(r, "�ض�����"), 8));// �ض�����
                regn.setMemo(GetCellData(r, "��ע"));// ��ע
                regn.setSetupPers(GetCellData(r, "������"));// ������
                if (GetCellData(r, "����ʱ��") == null || GetCellData(r, "����ʱ��").equals("")) {
                    regn.setSetupTm(null);
                } else {
                    regn.setSetupTm(GetCellData(r, "����ʱ��"));// ����ʱ��
                }
                regn.setMdfr(GetCellData(r, "�޸���"));// �޸���
                if (GetCellData(r, "�޸�ʱ��") == null || GetCellData(r, "�޸�ʱ��").equals("")) {
                    regn.setModiTm(null);
                } else {
                    regn.setModiTm(GetCellData(r, "����ʱ��"));// �޸�ʱ��
                }
                temp.put(orderNo, regn);
            }
            fileIn.close();
        } catch (Exception e) {

            throw new RuntimeException("�ļ��ĵ�" + j + "�е����ʽ����" +e.getMessage());
        }
        return temp;
    }

}
