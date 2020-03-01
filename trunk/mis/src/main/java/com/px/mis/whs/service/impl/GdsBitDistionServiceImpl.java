package com.px.mis.whs.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.px.mis.util.BaseJson;
import com.px.mis.whs.dao.GdsBitDistionMapper;
import com.px.mis.whs.dao.InvtyNumMapper;
import com.px.mis.whs.dao.OthOutIntoWhsMapper;
import com.px.mis.whs.dao.WhsDocMapper;
import com.px.mis.whs.entity.GdsBit;
import com.px.mis.whs.entity.MovBitList;
import com.px.mis.whs.entity.MovBitTab;
import com.px.mis.whs.entity.Regn;
import com.px.mis.whs.entity.WhsDoc;
import com.px.mis.whs.service.GdsBitDistionService;

@Service
@Transactional
public class GdsBitDistionServiceImpl implements GdsBitDistionService {

    @Autowired
    GdsBitDistionMapper gdsBitDistionMapper;

    @Autowired
    OthOutIntoWhsMapper othOutIntoWhsMapper;

    @Autowired
    InvtyNumMapper invtyNumMapper;

    @Autowired
    WhsDocMapper whsDocMapper;

    // ��ѯ�ֿ⡢���򡢻�λ�������Ϣ
    @Override
    public String selectWDoc() {
        String resp = "";
        List<WhsDoc> wDocList = gdsBitDistionMapper.selectWDoc();
        // System.out.println("wDocList.size()" + wDocList.size());
        try {
            if (wDocList.size() > 0) {
                resp = BaseJson.returnRespObjList("whs/gds_bit_distion/selectWDoc", true, "��ѯ�ɹ���", null, wDocList);
            } else {
                resp = BaseJson.returnRespObjList("whs/gds_bit_distion/selectWDoc", false, "��ѯʧ�ܣ�", null, wDocList);
            }

        } catch (Exception e) {

        }
        return resp;
    }

    @Override
    public String selectRegn(String whsEncd) {
        String resp = "";
        List<Regn> rList = gdsBitDistionMapper.selectRegn(whsEncd);
        // System.out.println(rList.size());
        try {
            if (rList.size() > 0) {
                resp = BaseJson.returnRespObjList("whs/gds_bit_distion/selectRegn", true, "��ѯ�ɹ���", null, rList);
            } else {
                resp = BaseJson.returnRespObjList("whs/gds_bit_distion/selectRegn", false, "��ѯʧ�ܣ�", null, rList);
            }

        } catch (IOException e) {

        }
        return resp;
    }

    @Override
    public String selectBit(String regnEncd) {
        String resp = "";
        List<GdsBit> gdsBitList = gdsBitDistionMapper.selectBit(regnEncd);
        // System.out.println(gdsBitList.size());
        try {
            if (gdsBitList.size() > 0) {
                resp = BaseJson.returnRespObjList("whs/gds_bit_distion/selectBit", true, "��ѯ�ɹ���", null, gdsBitList);
            } else {
                resp = BaseJson.returnRespObjList("whs/gds_bit_distion/selectBit", false, "��ѯʧ�ܣ�", null, gdsBitList);
            }

        } catch (IOException e) {

        }
        return resp;
    }

    @Override
    public String selectInvty(String gdsBitEncd) {
        String resp = "";
        List<MovBitTab> movList = gdsBitDistionMapper.selectInvty(gdsBitEncd);
        // System.out.println(movList.size());
        try {
            if (movList.size() > 0) {
                resp = BaseJson.returnRespObjList("whs/gds_bit_distion/selectInvty", true, "��ѯ�ɹ���", null, movList);
            } else {
                resp = BaseJson.returnRespObjList("whs/gds_bit_distion/selectInvty", false, "��ѯʧ�ܣ�", null, movList);
            }

        } catch (IOException e) {

        }
        return resp;
    }

    @Override
    public String selectInvtyWhs(Map map) {
        String resp = "";
        int pageNo = Integer.parseInt(map.get("pageNo").toString());
        int pageSize = Integer.parseInt(map.get("pageSize").toString());
        map.put("whsId", getList((String) map.get("whsId")));
        List<Map> movList = gdsBitDistionMapper.selectInvtyWhs(map);
        int count =gdsBitDistionMapper. selectInvtyWhsCount(map);
        int listNum = movList.size();
        int pages = count / pageSize;
        if (count % pageSize > 0) {
            pages += 1;
        }
        try {
            if (movList.size() > 0) {
                resp = BaseJson.returnRespList("whs/gds_bit_distion/selectInvtyWhs", true, "��ѯ�ɹ���",  count, pageNo, pageSize,
                        listNum, pages, movList);
            } else {
                resp = BaseJson.returnRespObjList("whs/gds_bit_distion/selectInvtyWhs", false, "��ѯʧ�ܣ�", null, movList);
            }

        } catch (IOException e) {

        }
        return resp;
    }

    // PDA ��λչʾ
    @Override
    public String selectMTab(String whsEncd) {
        String resp = "";
        List<MovBitTab> movList = gdsBitDistionMapper.selectMTab(whsEncd);
        try {
            if (movList.size() > 0) {
                resp = BaseJson.returnRespObjList("whs/gds_bit_distion/selectMTab", true, "��ѯ�ɹ���", null, movList);
            } else {
                resp = BaseJson.returnRespObjList("whs/gds_bit_distion/selectMTab", false, "��ѯʧ�ܣ�", null, movList);
            }

        } catch (IOException e) {

        }
        return resp;
    }

    // PDA ��λ�ش�
    @Override
    public String updateMovbit(List<MovBitList> movBitList, List<MovBitTab> movBitTab) {

        String resp = "";

        // ��ѯ��ԭʼ��λ ��ԭ��Ʒ��λ��������
        int i = gdsBitDistionMapper.updateMovbitTab(movBitTab);// �޸���λ��(����)
        if (i == 0) {
            throw new RuntimeException("�Ļ�λû�и����Ŵ��");
        }
        // �Ƶ����ڵĻ�λ ����Ʒ��λ��������(����Ƶ��Ļ�λ�иô�������������� û�иô�����л�λ����)
        if (invtyNumMapper.selectAllMbit(movBitTab).size() > 0) {
            gdsBitDistionMapper.updateMovbitTabJia(movBitTab);// �޸���λ��(����)
        } else {
            invtyNumMapper.insertMovBitTab(movBitTab);// ������λ��

        }

        gdsBitDistionMapper.insertMovBitList(movBitList);// ������λ�嵥

        try {
            resp = BaseJson.returnRespObj("whs/gds_bit_distion/updateMovbit", true, "��λ�ɹ���", null);
        } catch (IOException e) {

        }

        return resp;
    }

    // PDA չʾ�б�
    @Override
    public String selectMTabList(String whsEncd) {
        String resp = "";

        List<String> list = getList(whsEncd);

        List<MovBitList> movList = gdsBitDistionMapper.selectMTabList(list);
        try {
            if (movList.size() > 0) {
                resp = BaseJson.returnRespObjList("whs/gds_bit_distion/selectMTabList", true, "��ѯ�ɹ���", null, movList);
            } else {
                resp = BaseJson.returnRespObjList("whs/gds_bit_distion/selectMTabList", false, "��ѯʧ�ܣ�", null, movList);
            }

        } catch (IOException e) {

        }
        return resp;
    }

    // PDA �ش�����
    @Override
    public String updateMBitList(MovBitList movBitList) {
        String resp = "";
        List<String> list = new ArrayList<String>();
        list.add(movBitList.getOrdrNum().toString());

        List<MovBitList> movBitTab = gdsBitDistionMapper.selectMTabListId(list);
        int a = gdsBitDistionMapper.updateMBitList(movBitList);

        if (movBitTab.size() == 0) {
            throw new RuntimeException("��λ���ݴ���\n");
        }
        for (MovBitList bitList : movBitTab) {
            if (bitList.getIsOporFish() == 1) {
                throw new RuntimeException("��λ���������\n");
            }
            MovBitTab tab = new MovBitTab();
            tab.setInvtyEncd(bitList.getInvtyEncd());
            tab.setWhsEncd(bitList.getWhsEncd());
            tab.setBatNum(bitList.getBatNum());
            tab.setGdsBitEncd(bitList.getOalBit());// ԭʼ��λ
            tab.setQty(bitList.getOalBitNum());
            tab.setRegnEncd(bitList.getRegnEncd());
            tab.setPrdcDt(bitList.getPrdcDt());
            MovBitTab whsTab = invtyNumMapper.selectMbit(tab);

            if (whsTab == null) {
                throw new RuntimeException(bitList.getOalBit() + "��λ��û��" + bitList.getInvtyEncd() + "���\n");
            } else if (whsTab.getQty().compareTo(tab.getQty()) >= 0) {
                tab.setMovBitEncd(whsTab.getMovBitEncd());
                invtyNumMapper.updateJianMbit(tab);// ��

                // �л���λ
                tab.setGdsBitEncd(bitList.getTargetBit());
                MovBitTab whsTabs = invtyNumMapper.selectMbit(tab);
                if (whsTabs == null) {
                    invtyNumMapper.insertMovBitTabJia(tab);// ����
                } else {
                    tab.setMovBitEncd(whsTabs.getMovBitEncd());
                    invtyNumMapper.updateJiaMbit(tab);// ���
                }

            } else {
                throw new RuntimeException(bitList.getOalBit() + "��λ��" + bitList.getInvtyEncd() + "�������\n");
            }

        }


        resp = "�ش��ɹ���\n";

        return resp;
    }

    @Override
    public String selectMBitList(Map map) {
        // TODO �Զ����ɵķ������
        String resp = "";
        List<Map> movList = gdsBitDistionMapper.selectInvtyWhs(map);
        if (movList.size() == 0) {
            map.remove("invtyClsEncd");
            map.remove("batNum");
            movList = gdsBitDistionMapper.selectInvtyWhs(map);
        }
//		if(movList.size()==0) {
//			map.remove("invtyClsEncd");
//			map.remove("batNum");
//			movList = gdsBitDistionMapper.selectInvtyWhs(map);
//		}
        try {
            if (movList.size() > 0) {
                resp = BaseJson.returnRespObjList("whs/gds_bit_distion/selectInvtyWhs", true, "��ѯ�ɹ���", null, movList);
            } else {
                resp = BaseJson.returnRespObjList("whs/gds_bit_distion/selectInvtyWhs", false, "��ѯʧ�ܣ�û�ж�Ӧ�����λ�Ƽ�", null,
                        movList);
            }

        } catch (IOException e) {

        }
        return resp;
    }

    @Override
    public String insertMovbitPC(List<MovBitList> movBitList) {

        String resp = "";
//		����
        for (MovBitList oSubTab : movBitList) {
            String gList = whsDocMapper.selectWhsGdsReal(oSubTab.getWhsEncd(), oSubTab.getOalBit());
            String gLists = whsDocMapper.selectWhsGdsReal(oSubTab.getWhsEncd(), oSubTab.getTargetBit());
            if (gList == null || gLists == null) {
                throw new RuntimeException(oSubTab.getOalBit() + "��λ��" + oSubTab.getTargetBit() + "��λ�����ڸòֿ�");

            }
        }
        gdsBitDistionMapper.insertMovBitList(movBitList);// ������λ�嵥

        try {
            resp = BaseJson.returnRespObj("whs/gds_bit_distion/updateMovbit", true, "��λ�����ɹ���", null);
        } catch (IOException e) {

        }

        return resp;
    }

    // ɾ����λ
    @Override
    public String deleteMovbit(String ordrNum) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";

        List<String> list = getList(ordrNum);
        List<String> listto = new ArrayList<String>();

        for (MovBitList bitList : gdsBitDistionMapper.selectMTabListId(list)) {
            if (bitList.getIsOporFish() == null || bitList.getIsOporFish().equals(0)) {
                listto.add(bitList.getOrdrNum().toString());
            }

        }
        if (listto.size() > 0) {
            gdsBitDistionMapper.deleteMTabList(listto);
            isSuccess = true;
            message = "ɾ���ɹ���";
        } else {
            isSuccess = false;
            message = "������λ������ɣ�";
        }

        try {
            resp = BaseJson.returnRespObj("whs/gd_flow_crop/deleteGFlowCorpList", isSuccess, message, null);
        } catch (IOException e) {

        }
        return resp;
    }

    // չʾ�б�
    @Override
    public String selectMTabLists(Map map) {
        String resp = "";

        map.put("targetBit", getList((String) map.get("targetBit")));
        map.put("oalBit", getList((String) map.get("oalBit")));
        map.put("batNum", getList((String) map.get("batNum")));
        map.put("invtyEncd", getList((String) map.get("invtyEncd")));
        map.put("whsEncd", getList((String) map.get("whsEncd")));
        map.put("regnEncd", getList((String) map.get("regnEncd")));
        map.put("targetRegnEncd", getList((String) map.get("targetRegnEncd")));
        map.put("whsId", getList((String) map.get("whsId")));

        if (map.containsKey("pageNo") && map.containsKey("pageSize")) {
            int pageNo = (int) map.get("pageNo");
            int pageSize = (int) map.get("pageSize");
            map.put("index", (pageNo - 1) * pageSize);
            map.put("num", pageSize);

            List<MovBitList> movList = gdsBitDistionMapper.selectMTabLists(map);
            int count = gdsBitDistionMapper.selectMTabListsCount(map);

            int listNum = movList.size();
            int pages = count / pageSize;
            if (count % pageSize > 0) {
                pages += 1;
            }
            try {

                resp = BaseJson.returnRespList("whs/gds_bit_distion/selectMTabLists", true, "��ѯ�ɹ���", count, pageNo, pageSize,
                        listNum, pages, movList);


            } catch (IOException e) {

            }
        } else {
            List<MovBitList> movList = gdsBitDistionMapper.selectMTabLists(map);

            try {
                if (movList.size() > 0) {
                    resp = BaseJson.returnRespObjList("whs/gds_bit_distion/selectMTabLists", true, "��ѯ�ɹ���", null,
                            movList);
                } else {
                    resp = BaseJson.returnRespObjList("whs/gds_bit_distion/selectMTabLists", false, "��ѯʧ�ܣ�", null,
                            movList);
                }

            } catch (IOException e) {

            }
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
        if (id == null || id.equals("")) {
            return null;
        }
        List<String> list = new ArrayList<String>();
        String[] str = id.split(",");
        for (int i = 0; i < str.length; i++) {
//
            list.add(str[i]);
        }
        return list;
    }

    // ������ѯ�ֿ�����л�λ
    @Override
    public String selectWhsgds(Map map) {
        String resp = "";
        map.put("whsId", getList((String) map.get("whsId")));

        List<Map<String, Object>> movList = gdsBitDistionMapper.selectWhsgds(map);
        try {
            if (movList.size() > 0) {
                resp = BaseJson.returnRespObjList("whs/gds_bit_distion/selectWhsgds", true, "��ѯ�ɹ���", null, movList);
            } else {
                resp = BaseJson.returnRespObjList("whs/gds_bit_distion/selectWhsgds", false, "��ѯʧ�ܣ�", null, movList);
            }

        } catch (IOException e) {

        }
        return resp;
    }
}
