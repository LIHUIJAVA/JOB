package com.px.mis.whs.service.impl;

import com.px.mis.purc.dao.InvtyDocDao;
import com.px.mis.purc.entity.InvtyDoc;
import com.px.mis.util.BaseJson;
import com.px.mis.util.poiTool;
import com.px.mis.whs.dao.ProdStruMapper;
import com.px.mis.whs.entity.ProdStru;
import com.px.mis.whs.entity.ProdStruMap;
import com.px.mis.whs.entity.ProdStruSubTab;
import com.px.mis.whs.service.ProdStruService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
public class ProdStruServiceImpl extends poiTool implements ProdStruService {

    @Autowired
    ProdStruMapper prodStruMapper;
    @Autowired
    private InvtyDocDao invtyDocDao;

    // ������Ʒ�ṹ
    @Override
    public String insertProdStru(ProdStru prodStru, List<ProdStruSubTab> pList) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        try {

            InvtyDoc invty = Optional.ofNullable(invtyDocDao.selectInvtyDocByInvtyDocEncd(prodStru.getMomEncd()))
                    .orElseThrow(() -> new RuntimeException("�ô��������"));
            Integer BOM = Optional.ofNullable(invty.getAllowBomMain())
                    .orElseThrow(() -> new RuntimeException("BOM��������Ϊ��"));

            if (BOM == 0) {
                resp = BaseJson.returnRespObj("whs/prod_stru/insertProdStru", false, "�ô��������BOM����", null);
                return resp;
            }

            if (prodStruMapper.selectMomEncd(prodStru.getMomEncd()) != null) {
                message = "ĸ���" + prodStru.getMomEncd() + "�Ѵ��ڣ����������룡";
                isSuccess = false;
            } else if (prodStruMapper.selectProdStru(prodStru.getOrdrNum()) != null) {
                message = "���" + prodStru.getOrdrNum() + "�Ѵ��ڣ����������룡";
                isSuccess = false;
            } else {
                if (prodStruMapper.selectProdStruSubTabList(prodStru.getOrdrNum()).size() > 0) {
                    prodStruMapper.deleteProdStruSubTab(prodStru.getOrdrNum());
                }
                prodStruMapper.insertProdStru(prodStru);
                // System.out.println(prodStru.getOrdrNum());
                for (ProdStruSubTab tab : pList) {
                    tab.setOrdrNum(prodStru.getOrdrNum());
                    tab.setMomQty(new BigDecimal(1));
                }
                prodStruMapper.insertProdStruSubTab(pList);

                message = "�����ɹ���";
                isSuccess = true;
            }

            resp = BaseJson.returnRespObj("whs/prod_stru/insertProdStru", isSuccess, message, prodStru);
        } catch (Exception e) {

            try {
                resp = BaseJson.returnRespObj("whs/prod_stru/insertProdStru", false, "����ʧ��", prodStru);
            } catch (IOException e1) {


            }
            throw new RuntimeException("����ʧ��");
        }
        return resp;
    }

    // �޸Ĳ�Ʒ�ṹ
    @Override
    public String updateProdStru(ProdStru prodStru, List<ProdStruSubTab> pList) {

        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        ProdStru prodStrus = prodStruMapper.selectMomEncd(prodStru.getMomEncd());
        if (prodStrus.getIsNtChk() == null) {
            throw new RuntimeException("�������״̬�쳣");
        } else if (prodStrus.getIsNtChk() == 1) {
            throw new RuntimeException("��������˲������޸�");
        }
        InvtyDoc invty = Optional.ofNullable(invtyDocDao.selectInvtyDocByInvtyDocEncd(prodStru.getMomEncd()))
                .orElseThrow(() -> new RuntimeException("�ô��������"));
        Integer BOM = Optional.ofNullable(invty.getAllowBomMain()).orElseThrow(() -> new RuntimeException("BOM��������Ϊ��"));

        if (BOM == 0) {
            try {
                resp = BaseJson.returnRespObj("whs/prod_stru/insertProdStru", false, "�ô��������BOM����", null);
            } catch (IOException e) {


            }
            return resp;
        }

        prodStruMapper.deleteProdStruSubTab(prodStru.getOrdrNum());
        int i = prodStruMapper.updateProdStru(prodStru);
        for (ProdStruSubTab tab : pList) {
            tab.setOrdrNum(prodStru.getOrdrNum());
            tab.setMomQty(new BigDecimal(1));
        }
        prodStruMapper.insertProdStruSubTab(pList);
        message = "���³ɹ���";

        try {
            resp = BaseJson.returnRespObj("whs/prod_stru/updateProdStru", isSuccess, message, null);
        } catch (IOException e) {

        }
        return resp;

    }

    // ɾ����Ʒ�ṹ
    // ����ɾ��
    @Override
    public String deleteAllProdStru(String ordrNum) {

        String message = "";
        Boolean isSuccess = true;
        String resp = "";// ordrNum

        List<String> list = getList(ordrNum);
        List<String> lists = new ArrayList<String>();
        List<String> lists2 = new ArrayList<String>();
        for (String string : list) {
            ProdStru prodStru = prodStruMapper.selectProdStru(string);
            if (prodStru != null && prodStru.getIsNtChk() == 0) {
                lists.add(prodStru.getOrdrNum());
            } else if (prodStru != null && prodStru.getIsNtChk() == 1) {
                lists2.add(prodStru.getOrdrNum());
            }
        }
        if (lists.size() > 0) {
            prodStruMapper.deleteAllProdStru(lists);
            isSuccess = true;
            message = "ɾ���ɹ���";
            if (lists2.size() > 0) {
                message = "�������󵥾�";
            }
        } else {
            message = "��������";
        }

        try {
            resp = BaseJson.returnRespObj("whs/prod_stru/deleteAllProdStru", isSuccess, message, null);
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

    // �򵥲� ��Ʒ�ṹ

    // ��ѯ����ҳ��
    @Override
    public String query(String ordrNum) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        List<ProdStruSubTab> pSubTabList = new ArrayList<>();
        ProdStru prodStru = prodStruMapper.selectProdStru(ordrNum);
        if (prodStru != null) {
            pSubTabList = prodStruMapper.selectProdStruSubTabList(ordrNum);
            message = "��ѯ�ɹ���";
        } else {
            isSuccess = false;
            message = "���" + ordrNum + "�����ڣ�";
        }

        try {
            resp = BaseJson.returnRespObjList("whs/prod_stru/query", isSuccess, message, prodStru, pSubTabList);
        } catch (IOException e) {

        }
        return resp;
    }

    // ��ҳ��ѯ
    @Override
    public String queryList(Map map) {
        String resp = "";
        List<ProdStru> proList = prodStruMapper.selectList(map);
        int count = prodStruMapper.selectCount(map);

        int pageNo = Integer.parseInt(map.get("pageNo").toString());
        int pageSize = Integer.parseInt(map.get("pageSize").toString());
        int listNum = proList.size();
        int pages = count / pageSize;
        if (count % pageSize > 0) {
            pages += 1;
        }
        try {
            resp = BaseJson.returnRespList("whs/prod_stru/queryList", true, "��ѯ�ɹ���", count, pageNo, pageSize, listNum,
                    pages, proList);
        } catch (IOException e) {

        }
        return resp;
    }

    // ��ӡ
    @Override
    public String queryListPrint(Map map) {
        String resp = "";
        List<ProdStruMap> proList = prodStruMapper.queryListPrint(map);
        proList.add(new ProdStruMap());
        try {
            resp = BaseJson.returnRespObjListAnno("whs/prod_stru/queryListPrint", true, "��ѯ�ɹ���", null, proList);
        } catch (IOException e) {

        }
        return resp;
    }

    // ��ѯ ��ȡ��ĸ���Ĳ�Ʒ�ṹ��Ϣ
    @Override
    public String selectProdStruByMom(String ordrNum) {
        String resp = "";
        ProdStru proList = prodStruMapper.selectMomEncd(ordrNum);
        try {
            if (proList != null) {
                resp = BaseJson.returnRespObj("whs/prod_stru/selectProdStruByMom", true, "��ѯ�ɹ���", proList);
            } else {
                resp = BaseJson.returnRespObj("whs/prod_stru/selectProdStruByMom", false, "��ѯʧ�ܣ�", proList);
            }

        } catch (IOException e) {

        }
        return resp;
    }

    // ���
    @Override
    public String updatePStruChk(List<ProdStru> cList) {
        String message = "";
        String messages = "";
        Boolean isSuccess = true;
        String resp = "";
        String chkr = null;

        for (ProdStru stru : cList) {
            chkr = stru.getChkr();
        }
        List<ProdStru> proList = prodStruMapper.selectProdStruList(cList);
        ArrayList<ProdStru> asd = new ArrayList<>();
        for (ProdStru prodStru : proList) {
            if (prodStru.getIsNtChk() == 1) {
                messages += prodStru.getMomEncd() + "�����!  ";
            } else {
                prodStru.setChkr(chkr);
                asd.add(prodStru);
            }
        }
        if (asd.size() > 0) {
            int i = prodStruMapper.updatePStruChk(asd);
            if (i >= 1) {
                message = "��˳ɹ���";
                isSuccess = true;
            } else {
                message = "���ʧ�ܣ�";
                isSuccess = false;
            }
        } else {
            isSuccess = false;
        }

        try {
            resp = BaseJson.returnRespObj("whs/prod_stru/updatePStruChk", isSuccess, messages + message, null);
        } catch (IOException e) {

        }
        return resp;
    }

    // ����
    @Override
    public String updatePStruNoChk(List<ProdStru> cList) {
        String message = "";
        String messages = "";
        Boolean isSuccess = true;
        String resp = "";

        List<ProdStru> proList = prodStruMapper.selectProdStruList(cList);

        ArrayList<ProdStru> asd = new ArrayList<>();
        for (ProdStru prodStru : proList) {
            if (prodStru.getIsNtChk() == 0) {
                messages += prodStru.getMomEncd() + "������!  ";
            } else {
                asd.add(prodStru);
            }
        }
        if (asd.size() > 0) {
            int i = prodStruMapper.updatePStruNoChk(cList);
            if (i >= 1) {
                message = "����ɹ���";
                isSuccess = true;
            } else {
                message = "����ʧ�ܣ�";
                isSuccess = false;
            }
        } else {
            isSuccess = false;

        }

        try {
            resp = BaseJson.returnRespObj("whs/prod_stru/updatePStruNoChk", isSuccess, messages + message, null);
        } catch (IOException e) {

        }
        return resp;
    }

    // ����
    public String uploadFileAddDb(MultipartFile file) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        Map<String, ProdStru> regnMap = uploadScoreInfo(file);
        for (Map.Entry<String, ProdStru> entry : regnMap.entrySet()) {
            // System.out.println(entry);

            if (prodStruMapper.selectMomEncd(entry.getValue().getMomEncd()) != null) {
                isSuccess = false;
                message = "�����в����Ѵ��ڣ��޷����룬������ٵ��룡";
                throw new RuntimeException(message);
            } else {
                prodStruMapper.exIinsertProdStru(entry.getValue());
                for (ProdStruSubTab prodStruSubTab : entry.getValue().getStruSubList()) {
                    prodStruSubTab.setOrdrNum(entry.getValue().getOrdrNum());
                }
                prodStruMapper.insertProdStruSubTab(entry.getValue().getStruSubList());

                isSuccess = true;
                message = "��������ɹ���";
            }
        }
        try {
            resp = BaseJson.returnRespObj("whs/prod_stru/uploadRegnFile", isSuccess, message, null);
        } catch (IOException e) {

        }
        return resp;
    }

    // ����excle
    private Map<String, ProdStru> uploadScoreInfo(MultipartFile file) {
        Map<String, ProdStru> temp = new HashMap<>();
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
                String orderNo = GetCellData(r, "ĸ������");
                // ����ʵ����
                ProdStru prodStru = new ProdStru();
                if (temp.containsKey(orderNo)) {
                    prodStru = temp.get(orderNo);
                }
                // ȡ����ǰ�е�1����Ԫ�����ݣ�����װ��ʵ��ĸ���������
//				prodStru.setOrdrNum(ordrNum);		//	bigint(20	'���',
                prodStru.setMomEncd(orderNo); // varchar(200 'ĸ������',
                prodStru.setMomNm(GetCellData(r, "ĸ������")); // varchar(200 'ĸ������',
                prodStru.setEdComnt(GetCellData(r, "�汾˵��")); // varchar(200 '�汾˵��',
                prodStru.setMomSpc(GetCellData(r, "ĸ�����")); // varchar(200 'ĸ�����',
                prodStru.setMemo(GetCellData(r, "ĸ����ע")); // varchar(2000 '��ע',
                prodStru.setSetupPers(GetCellData(r, "������")); // varchar(200 '������',
                prodStru.setSetupTm(
                        GetCellData(r, "����ʱ��") == null ? "" : GetCellData(r, "����ʱ��")); // datetime
                // '����ʱ��',
                prodStru.setMdfr(GetCellData(r, "�޸���")); // varchar(200 '�޸���',
                prodStru.setModiTm(
                        GetCellData(r, "�޸�ʱ��") == null ? "" : GetCellData(r, "�޸�ʱ��")); // datetime
                // '�޸�ʱ��',
                prodStru.setChkr(GetCellData(r, "�����")); // varchar(200 '�����',
                prodStru.setChkTm(
                        GetCellData(r, "���ʱ��") == null ? "" : GetCellData(r, "���ʱ��")); // datetime
                // '���ʱ��',
//				prodStru.setIsNtWms((new Double(GetCellData(r, "�Ƿ���WMS�ϴ�"))).intValue());		//	int(11	'�Ƿ���WMS�ϴ�',
                prodStru.setIsNtChk(
                        (new Double(GetCellData(r, "�Ƿ����") == null || GetCellData(r, "�Ƿ����").equals("") ? "0"
                                : GetCellData(r, "�Ƿ����"))).intValue()); // int(11 '�Ƿ����',
//                prodStru.setIsNtCmplt(
//                        (new Double(GetCellData(r, "�Ƿ����") == null || GetCellData(r, "�Ƿ����").equals("") ? "0"
//                                : GetCellData(r, "�Ƿ����"))).intValue()); // int(11 '�Ƿ����',
//                prodStru.setIsNtClos(
//                        (new Double(GetCellData(r, "�Ƿ�ر�") == null || GetCellData(r, "�Ƿ�ر�").equals("") ? "0"
//                                : GetCellData(r, "�Ƿ�ر�"))).intValue()); // int(11 '�Ƿ�ر�',
//                prodStru.setPrintCnt(
//                        (new Double(GetCellData(r, "��ӡ����") == null || GetCellData(r, "��ӡ����").equals("") ? "0"
//                                : GetCellData(r, "��ӡ����"))).intValue()); // int(11 '��ӡ����',

                List<ProdStruSubTab> ProdStruSubTabs = prodStru.getStruSubList();// �ӱ�
                if (ProdStruSubTabs == null) {
                    ProdStruSubTabs = new ArrayList<>();
                }
                ProdStruSubTab prodStruSubTab = new ProdStruSubTab();
//				prodStruSubTab.setOrdrNumSub(ordrNumSub);		//	bigint(20	'���',	
                prodStruSubTab.setSubEncd(GetCellData(r, "�Ӽ�����")); // varchar(200 '�Ӽ�����',
//				prodStruSubTab.setOrdrNum(ordrNum);		//	bigint(20	'��Ʒ�ṹ����ʶ',	
                prodStruSubTab.setMomEncd(GetCellData(r, "ĸ������")); // varchar(200 'ĸ������',
                prodStruSubTab.setSubNm(GetCellData(r, "�Ӽ�����")); // varchar(200 '�Ӽ�����',
                prodStruSubTab.setSubSpc(GetCellData(r, "�Ӽ����")); // varchar(200 '�Ӽ����',
                prodStruSubTab.setMeasrCorp(GetCellData(r, "�Ӽ�������λ����")); // varchar(200 '�Ӽ�������λ����',
                prodStruSubTab.setSubQty(
                        new BigDecimal(GetCellData(r, "�Ӽ�����") == null || GetCellData(r, "�Ӽ�����").equals("") ? "0"
                                : GetCellData(r, "�Ӽ�����")).setScale(8, BigDecimal.ROUND_HALF_UP)); // decimal(20,8
                // '�Ӽ�����',
                prodStruSubTab.setMomQty(
                        new BigDecimal(GetCellData(r, "ĸ������") == null || GetCellData(r, "ĸ������").equals("") ? "0"
                                : GetCellData(r, "ĸ������")).setScale(8, BigDecimal.ROUND_HALF_UP)); // decimal(20,8
                // 'ĸ������',
                prodStruSubTab.setMemo(GetCellData(r, "�Ӽ���ע")); // varchar(2000 '��ע',

                prodStruSubTab.setBxRule(new BigDecimal(
                        GetCellData(r, "�Ӽ����") == null || GetCellData(r, "�Ӽ����").equals("") ? "0" : GetCellData(r, "�Ӽ����"))
                        .setScale(8, BigDecimal.ROUND_HALF_UP)); // decimal(20,8
                // '���',
                ProdStruSubTabs.add(prodStruSubTab);

                prodStru.setStruSubList(ProdStruSubTabs);
                temp.put(orderNo, prodStru);

            }
            fileIn.close();
        } catch (Exception e) {
            throw new RuntimeException("�ļ��ĵ�" + j + "�е����ʽ����" +e.getMessage());
        }
        return temp;
    }

    @Override
    public String queryAmbDisambSngl(Map map) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        List<ProdStru> proList = prodStruMapper.selectMomEncdAmbDisambSngl(map);
        int count = prodStruMapper.countMomEncdAmbDisambSngl(map);


        if (proList.size() > 0) {
//			pSubTabList = prodStruMapper.selectProdStruSubTabList(ordrNum);
            message = "��ѯ�ɹ���";
        } else {
            isSuccess = false;
            message = "���" + map.get("momEncd") + "�����ڣ�";
        }

        try {
            if (map.containsKey("pageNo") && map.containsKey("pageSize")) {
                int pageNo = Integer.parseInt(map.get("pageNo").toString());
                int pageSize = Integer.parseInt(map.get("pageSize").toString());
                int listNum = proList.size();
                int pages = count / pageSize;
                if (count % pageSize > 0) {
                    pages += 1;
                }

                if (proList.size() > 0) {
//			pSubTabList = prodStruMapper.selectProdStruSubTabList(ordrNum);
                    message = "��ѯ�ɹ���";
                } else {
                    isSuccess = false;
                    message = "���" + map.get("momEncd") + "�����ڣ�";
                }
                return resp = BaseJson.returnRespList("whs/prod_stru/queryAmbDisambSngl", true, "��ѯ�ɹ���", count, pageNo, pageSize, listNum,
                        pages, proList);
            } else {
                return resp = BaseJson.returnRespObjList("whs/prod_stru/queryAmbDisambSngl", isSuccess, message, null, proList);
            }

        } catch (IOException e) {

        }
        return resp;
    }
}
