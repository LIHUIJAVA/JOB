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

    // 查询仓库、区域、货位、存货信息
    @Override
    public String selectWDoc() {
        String resp = "";
        List<WhsDoc> wDocList = gdsBitDistionMapper.selectWDoc();
        // System.out.println("wDocList.size()" + wDocList.size());
        try {
            if (wDocList.size() > 0) {
                resp = BaseJson.returnRespObjList("whs/gds_bit_distion/selectWDoc", true, "查询成功！", null, wDocList);
            } else {
                resp = BaseJson.returnRespObjList("whs/gds_bit_distion/selectWDoc", false, "查询失败！", null, wDocList);
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
                resp = BaseJson.returnRespObjList("whs/gds_bit_distion/selectRegn", true, "查询成功！", null, rList);
            } else {
                resp = BaseJson.returnRespObjList("whs/gds_bit_distion/selectRegn", false, "查询失败！", null, rList);
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
                resp = BaseJson.returnRespObjList("whs/gds_bit_distion/selectBit", true, "查询成功！", null, gdsBitList);
            } else {
                resp = BaseJson.returnRespObjList("whs/gds_bit_distion/selectBit", false, "查询失败！", null, gdsBitList);
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
                resp = BaseJson.returnRespObjList("whs/gds_bit_distion/selectInvty", true, "查询成功！", null, movList);
            } else {
                resp = BaseJson.returnRespObjList("whs/gds_bit_distion/selectInvty", false, "查询失败！", null, movList);
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
                resp = BaseJson.returnRespList("whs/gds_bit_distion/selectInvtyWhs", true, "查询成功！",  count, pageNo, pageSize,
                        listNum, pages, movList);
            } else {
                resp = BaseJson.returnRespObjList("whs/gds_bit_distion/selectInvtyWhs", false, "查询失败！", null, movList);
            }

        } catch (IOException e) {

        }
        return resp;
    }

    // PDA 移位展示
    @Override
    public String selectMTab(String whsEncd) {
        String resp = "";
        List<MovBitTab> movList = gdsBitDistionMapper.selectMTab(whsEncd);
        try {
            if (movList.size() > 0) {
                resp = BaseJson.returnRespObjList("whs/gds_bit_distion/selectMTab", true, "查询成功！", null, movList);
            } else {
                resp = BaseJson.returnRespObjList("whs/gds_bit_distion/selectMTab", false, "查询失败！", null, movList);
            }

        } catch (IOException e) {

        }
        return resp;
    }

    // PDA 移位回传
    @Override
    public String updateMovbit(List<MovBitList> movBitList, List<MovBitTab> movBitTab) {

        String resp = "";

        // 查询出原始货位 将原商品货位数量减少
        int i = gdsBitDistionMapper.updateMovbitTab(movBitTab);// 修改移位表(减少)
        if (i == 0) {
            throw new RuntimeException("改货位没有改批号存货");
        }
        // 移到现在的货位 将商品货位数量增加(如果移到的货位有该存货进行数量增加 没有该存货进行货位新增)
        if (invtyNumMapper.selectAllMbit(movBitTab).size() > 0) {
            gdsBitDistionMapper.updateMovbitTabJia(movBitTab);// 修改移位表(增加)
        } else {
            invtyNumMapper.insertMovBitTab(movBitTab);// 增加移位表

        }

        gdsBitDistionMapper.insertMovBitList(movBitList);// 增加移位清单

        try {
            resp = BaseJson.returnRespObj("whs/gds_bit_distion/updateMovbit", true, "移位成功！", null);
        } catch (IOException e) {

        }

        return resp;
    }

    // PDA 展示列表
    @Override
    public String selectMTabList(String whsEncd) {
        String resp = "";

        List<String> list = getList(whsEncd);

        List<MovBitList> movList = gdsBitDistionMapper.selectMTabList(list);
        try {
            if (movList.size() > 0) {
                resp = BaseJson.returnRespObjList("whs/gds_bit_distion/selectMTabList", true, "查询成功！", null, movList);
            } else {
                resp = BaseJson.returnRespObjList("whs/gds_bit_distion/selectMTabList", false, "查询失败！", null, movList);
            }

        } catch (IOException e) {

        }
        return resp;
    }

    // PDA 回传数据
    @Override
    public String updateMBitList(MovBitList movBitList) {
        String resp = "";
        List<String> list = new ArrayList<String>();
        list.add(movBitList.getOrdrNum().toString());

        List<MovBitList> movBitTab = gdsBitDistionMapper.selectMTabListId(list);
        int a = gdsBitDistionMapper.updateMBitList(movBitList);

        if (movBitTab.size() == 0) {
            throw new RuntimeException("移位单据错误\n");
        }
        for (MovBitList bitList : movBitTab) {
            if (bitList.getIsOporFish() == 1) {
                throw new RuntimeException("移位单据已完成\n");
            }
            MovBitTab tab = new MovBitTab();
            tab.setInvtyEncd(bitList.getInvtyEncd());
            tab.setWhsEncd(bitList.getWhsEncd());
            tab.setBatNum(bitList.getBatNum());
            tab.setGdsBitEncd(bitList.getOalBit());// 原始货位
            tab.setQty(bitList.getOalBitNum());
            tab.setRegnEncd(bitList.getRegnEncd());
            tab.setPrdcDt(bitList.getPrdcDt());
            MovBitTab whsTab = invtyNumMapper.selectMbit(tab);

            if (whsTab == null) {
                throw new RuntimeException(bitList.getOalBit() + "货位上没有" + bitList.getInvtyEncd() + "存货\n");
            } else if (whsTab.getQty().compareTo(tab.getQty()) >= 0) {
                tab.setMovBitEncd(whsTab.getMovBitEncd());
                invtyNumMapper.updateJianMbit(tab);// 减

                // 切换货位
                tab.setGdsBitEncd(bitList.getTargetBit());
                MovBitTab whsTabs = invtyNumMapper.selectMbit(tab);
                if (whsTabs == null) {
                    invtyNumMapper.insertMovBitTabJia(tab);// 新增
                } else {
                    tab.setMovBitEncd(whsTabs.getMovBitEncd());
                    invtyNumMapper.updateJiaMbit(tab);// 入库
                }

            } else {
                throw new RuntimeException(bitList.getOalBit() + "货位上" + bitList.getInvtyEncd() + "存货不足\n");
            }

        }


        resp = "回传成功！\n";

        return resp;
    }

    @Override
    public String selectMBitList(Map map) {
        // TODO 自动生成的方法存根
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
                resp = BaseJson.returnRespObjList("whs/gds_bit_distion/selectInvtyWhs", true, "查询成功！", null, movList);
            } else {
                resp = BaseJson.returnRespObjList("whs/gds_bit_distion/selectInvtyWhs", false, "查询失败，没有对应存货货位推荐", null,
                        movList);
            }

        } catch (IOException e) {

        }
        return resp;
    }

    @Override
    public String insertMovbitPC(List<MovBitList> movBitList) {

        String resp = "";
//		新增
        for (MovBitList oSubTab : movBitList) {
            String gList = whsDocMapper.selectWhsGdsReal(oSubTab.getWhsEncd(), oSubTab.getOalBit());
            String gLists = whsDocMapper.selectWhsGdsReal(oSubTab.getWhsEncd(), oSubTab.getTargetBit());
            if (gList == null || gLists == null) {
                throw new RuntimeException(oSubTab.getOalBit() + "货位或" + oSubTab.getTargetBit() + "货位不属于该仓库");

            }
        }
        gdsBitDistionMapper.insertMovBitList(movBitList);// 增加移位清单

        try {
            resp = BaseJson.returnRespObj("whs/gds_bit_distion/updateMovbit", true, "移位新增成功！", null);
        } catch (IOException e) {

        }

        return resp;
    }

    // 删除移位
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
            message = "删除成功！";
        } else {
            isSuccess = false;
            message = "所有移位单已完成！";
        }

        try {
            resp = BaseJson.returnRespObj("whs/gd_flow_crop/deleteGFlowCorpList", isSuccess, message, null);
        } catch (IOException e) {

        }
        return resp;
    }

    // 展示列表
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

                resp = BaseJson.returnRespList("whs/gds_bit_distion/selectMTabLists", true, "查询成功！", count, pageNo, pageSize,
                        listNum, pages, movList);


            } catch (IOException e) {

            }
        } else {
            List<MovBitList> movList = gdsBitDistionMapper.selectMTabLists(map);

            try {
                if (movList.size() > 0) {
                    resp = BaseJson.returnRespObjList("whs/gds_bit_distion/selectMTabLists", true, "查询成功！", null,
                            movList);
                } else {
                    resp = BaseJson.returnRespObjList("whs/gds_bit_distion/selectMTabLists", false, "查询失败！", null,
                            movList);
                }

            } catch (IOException e) {

            }
        }

        return resp;
    }

    /**
     * id放入list
     *
     * @param id id(多个已逗号分隔)
     * @return List集合
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

    // 条件查询仓库的所有货位
    @Override
    public String selectWhsgds(Map map) {
        String resp = "";
        map.put("whsId", getList((String) map.get("whsId")));

        List<Map<String, Object>> movList = gdsBitDistionMapper.selectWhsgds(map);
        try {
            if (movList.size() > 0) {
                resp = BaseJson.returnRespObjList("whs/gds_bit_distion/selectWhsgds", true, "查询成功！", null, movList);
            } else {
                resp = BaseJson.returnRespObjList("whs/gds_bit_distion/selectWhsgds", false, "查询失败！", null, movList);
            }

        } catch (IOException e) {

        }
        return resp;
    }
}
