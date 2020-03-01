package com.px.mis.whs.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.px.mis.whs.entity.CheckPrftLossMap;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.ec.entity.ExpressCodeAndName;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;
import com.px.mis.util.poiTool;
import com.px.mis.whs.dao.ExpressCorpMapper;
import com.px.mis.whs.entity.ExpressCorp;
import com.px.mis.whs.entity.ExpressCorpMap;
import com.px.mis.whs.service.ExpressCorpService;

@Service
@Transactional
public class ExpressCorpServiceImpl extends poiTool implements ExpressCorpService {

    @Autowired
    ExpressCorpMapper expressCorpMapper;

    // ������ݹ�˾
    @Override
    public ObjectNode insertExpressCorp(ExpressCorp record) {

        ObjectNode on = null;
        try {
            on = JacksonUtil.getObjectNode("");
        } catch (Exception e) {

        }

        ExpressCorp records = expressCorpMapper.selectExpressCorp(record.getExpressEncd());
        if (records != null) {
            on.put("isSuccess", false);
            on.put("message", record.getExpressEncd() + " �����ظ���");
            return on;
        }
        int expressCorp = expressCorpMapper.insertExpressCorp(record);
        if (expressCorp == 1) {
            on.put("isSuccess", true);
            on.put("message", "�����ɹ���");
        } else {
            on.put("isSuccess", false);
            on.put("message", "����ʧ�ܣ�");
        }
        return on;
    }

    // �޸Ŀ�ݹ�˾
    @Override
    public ObjectNode updateExpressCorp(ExpressCorp record) {

        ObjectNode on = null;
        try {
            on = JacksonUtil.getObjectNode("");
        } catch (Exception e) {

        }

        int expressCorp = expressCorpMapper.updateExpressCorp(record);
        if (expressCorp == 1) {
            on.put("isSuccess", true);
            on.put("message", "�޸ĳɹ���");
        } else {
            on.put("isSuccess", false);
            on.put("message", "�޸�ʧ�ܣ�");
        }
        return on;
    }

    // ɾ����ݹ�˾
    @Override
    public ObjectNode deleteExpressCorp(String expressEncd) {

        ObjectNode on = null;
        try {
            on = JacksonUtil.getObjectNode("");
        } catch (Exception e) {

        }

        int expressCorp = expressCorpMapper.deleteExpressCorp(expressEncd);
        if (expressCorp == 1) {
            on.put("isSuccess", true);
            on.put("message", "ɾ���ɹ���");
        } else {
            on.put("isSuccess", false);
            on.put("message", "ɾ��ʧ�ܣ�");
        }
        return on;
    }

    // �򵥲� ��ݹ�˾
    @Override
    public ExpressCorp selectExpressCorp(String expressEncd) {
        ExpressCorp expressCorp = expressCorpMapper.selectExpressCorp(expressEncd);
        return expressCorp;
    }

    @Override
    public List<ExpressCorp> selectExpressCorpList(String expressEncd) {
        List<ExpressCorp> expressCorpList = expressCorpMapper.selectExpressCorpList(expressEncd);
        return expressCorpList;
    }

    // ��ҳ��ѯ
    @Override
    public String queryList(Map map) {
        String resp = "";
        List<ExpressCorp> aList = expressCorpMapper.selectList(map);
        int count = expressCorpMapper.selectCount(map);

        int pageNo = Integer.parseInt(map.get("pageNo").toString());
        int pageSize = Integer.parseInt(map.get("pageSize").toString());
        int listNum = aList.size();
        int pages = count / pageSize;
        if (count % pageSize > 0) {
            pages += 1;
        }
        try {
            resp = BaseJson.returnRespList("whs/express_crop/queryList", true, "��ѯ�ɹ���", count, pageNo, pageSize,
                    listNum, pages, aList);
        } catch (IOException e) {

        }
        return resp;
    }

    // ��ɾ
    @Override
    public String deleteECorpList(String expressEncd) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";

        List<String> list = getList(expressEncd);

        if (expressCorpMapper.selectECorpAllList(list).size() > 0) {
            expressCorpMapper.deleteECorpList(list);
            isSuccess = true;
            message = "ɾ���ɹ���";
        } else {
            isSuccess = false;
            message = "���" + expressEncd + "�����ڣ�";
        }

        try {
            resp = BaseJson.returnRespObj("whs/express_crop/deleteECorpList", isSuccess, message, null);
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
        List<ExpressCorpMap> aList = expressCorpMapper.selectListDaYin(map);
        aList.add(new ExpressCorpMap());

        try {
            resp = BaseJson.returnRespObjListAnno("whs/express_crop/queryListDaYin", true, "��ѯ�ɹ���", null, aList);
        } catch (IOException e) {

        }
        return resp;
    }

    // ����
    public String uploadFileAddDb(MultipartFile file) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        Map<String, ExpressCorp> expressCorpMap = uploadScoreInfo(file);
        for (Map.Entry<String, ExpressCorp> entry : expressCorpMap.entrySet()) {
            if (expressCorpMapper.selectExpressCorp(entry.getValue().getExpressEncd()) != null) {
                isSuccess = false;
                message = "��ݵ����в��ֵ����Ѵ��ڣ��޷����룬������ٵ��룡";
                throw new RuntimeException(message);
            } else {
                expressCorpMapper.insertExpressCorp(entry.getValue());
                isSuccess = true;
                message = "��ݵ�������ɹ���";
            }
        }
        try {
            resp = BaseJson.returnRespObj("whs/express_crop/uploadExpressCorpFile", isSuccess, message, null);
        } catch (IOException e) {

        }
        return resp;
    }

    /*
     * //�ж��Ƿ��ֶ� public String GetCellData(Row row,String ColNameCN) throws Exception
     * { Map<String,Integer> ColMap= new HashMap<String, Integer>(); Object obj =
     * ColMap.get(ColNameCN.trim()); if(obj==null) { throw new Exception("���������� [" +
     * ColNameCN +"],����"); } String result = getValue(row.getCell((int)obj));
     * if((result == null)||result.trim().equals("")) {
     * if(ColNameCN.equals("�Ƿ�֧�ֻ�������")) result = "0"; if(ColNameCN.equals("�Ƿ�ͣ��"))
     * result = "0"; if(ColNameCN.equals("�Ƿ����")) result = "0"; } return
     * result==null?result:result.trim(); }
     *
     */
    // ����excle
    private Map<String, ExpressCorp> uploadScoreInfo(MultipartFile file) {
        Map<String, ExpressCorp> temp = new HashMap<>();
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
                String orderNo = GetCellData(r, "��ݹ�˾����");
                // ����ʵ����
                ExpressCorp expressCorp = new ExpressCorp();
                if (temp.containsKey(orderNo)) {
                    expressCorp = temp.get(orderNo);
                }
                // ȡ����ǰ�е�1����Ԫ�����ݣ�����װ��ʵ��ĸ���������
                expressCorp.setExpressEncd(orderNo);// ��ݹ�˾���
                expressCorp.setExpressNm(GetCellData(r, "��ݹ�˾����"));// ��ݹ�˾����
//                expressCorp.setExpressTyp(GetCellData(r, "�������"));// �������
//                expressCorp.setExpOrderId(GetCellData(r, "��ݵ���"));// ��ݵ���
//                expressCorp.setSellHomeInfo(GetCellData(r, "������Ϣ"));// ������Ϣ
//                expressCorp.setBuyHomeInfo(GetCellData(r, "�����Ϣ"));// �����Ϣ
//                expressCorp.setIsNtSprtGdsToPay(new Double(GetCellData(r, "�Ƿ�֧�ֻ�������")).intValue());// �Ƿ�֧�ֻ�������
//                expressCorp.setGdsToPayServCost(GetBigDecimal(GetCellData(r, "������������"), 8));// ������������
                expressCorp.setIsNtStpUse(new Double(GetCellData(r, "�Ƿ�ͣ��")==null || GetCellData(r, "�Ƿ�ͣ��").equals("")? "0":GetCellData(r, "�Ƿ�ͣ��") ).intValue());// �Ƿ�ͣ��
                expressCorp.setFirstWet(GetBigDecimal(GetCellData(r, "����"), 8));// ����
                expressCorp.setFirstWetStrPrice(GetBigDecimal(GetCellData(r, "�������"), 8));// �������
//                expressCorp.setPrintCnt(new Double(GetCellData(r, "��ӡ����")).intValue());// ��ӡ����
                expressCorp.setMemo(GetCellData(r, "��ע"));// ��ע
//                expressCorp.setIsNtChk(new Double(GetCellData(r, "�Ƿ����")).intValue());// �Ƿ����
                expressCorp.setSetupPers(GetCellData(r, "������"));// ������
                if (GetCellData(r, "����ʱ��") == null || GetCellData(r, "����ʱ��").equals("")) {
                    expressCorp.setSetupTm(null);
                } else {
                    expressCorp.setSetupTm(GetCellData(r, "����ʱ��"));// ����ʱ��
                }
                expressCorp.setMdfr(GetCellData(r, "�޸���"));// �޸���
                if (GetCellData(r, "�޸�ʱ��") == null || GetCellData(r, "�޸�ʱ��").equals("")) {
                    expressCorp.setModiTm(null);
                } else {
                    expressCorp.setModiTm(GetCellData(r, "�޸�ʱ��"));// �޸�ʱ��
                }
//                expressCorp.setChkr(GetCellData(r, "�����"));// �����
//                if (GetCellData(r, "���ʱ��") == null || GetCellData(r, "���ʱ��").equals("")) {
//                    expressCorp.setChkTm(null);
//                } else {
//                    expressCorp.setChkTm(GetCellData(r, "���ʱ��"));// ���ʱ��
//                }
                expressCorp.setDeliver(GetCellData(r, "����������"));// ����������
                expressCorp.setDeliverPhone(GetCellData(r, "�������ֻ�"));// �������ֻ�
                expressCorp.setDeliverMobile(GetCellData(r, "����������"));// ����������
                expressCorp.setCompanyCode(GetCellData(r, "��ݹ�˾����"));// ��ݹ�˾����
                expressCorp.setProvince(GetCellData(r, "������ʡ"));// ������ʡ
                expressCorp.setCity(GetCellData(r, "��������"));// ��������
                expressCorp.setCountry(GetCellData(r, "��������"));// ��������
                expressCorp.setDetailedAddress(GetCellData(r, "��ϸ��ַ"));// ��ϸ��ַ
                temp.put(orderNo, expressCorp);
            }
            fileIn.close();
        } catch (Exception e) {

            throw new RuntimeException("�ļ��ĵ�" + j + "�е����ʽ����" +e.getMessage());
        }
        return temp;
    }

    @Override
    public String queryExpressCodeAndNameList() {
        String resp = "";

        try {
            List<ExpressCodeAndName> aList = expressCorpMapper.queryExpressCodeAndNameList();
            resp = BaseJson.returnRespObjList("whs/express_crop/queryExpressCodeAndNameList", true, "��ѯ�ɹ���", null, aList);
        } catch (IOException e) {

        }
        return resp;
    }

}
