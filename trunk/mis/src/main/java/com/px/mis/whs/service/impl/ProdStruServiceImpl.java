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

    // 新增产品结构
    @Override
    public String insertProdStru(ProdStru prodStru, List<ProdStruSubTab> pList) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        try {

            InvtyDoc invty = Optional.ofNullable(invtyDocDao.selectInvtyDocByInvtyDocEncd(prodStru.getMomEncd()))
                    .orElseThrow(() -> new RuntimeException("该存货不存在"));
            Integer BOM = Optional.ofNullable(invty.getAllowBomMain())
                    .orElseThrow(() -> new RuntimeException("BOM主件属于为空"));

            if (BOM == 0) {
                resp = BaseJson.returnRespObj("whs/prod_stru/insertProdStru", false, "该存货不属于BOM主件", null);
                return resp;
            }

            if (prodStruMapper.selectMomEncd(prodStru.getMomEncd()) != null) {
                message = "母编号" + prodStru.getMomEncd() + "已存在，请重新输入！";
                isSuccess = false;
            } else if (prodStruMapper.selectProdStru(prodStru.getOrdrNum()) != null) {
                message = "编号" + prodStru.getOrdrNum() + "已存在，请重新输入！";
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

                message = "新增成功！";
                isSuccess = true;
            }

            resp = BaseJson.returnRespObj("whs/prod_stru/insertProdStru", isSuccess, message, prodStru);
        } catch (Exception e) {

            try {
                resp = BaseJson.returnRespObj("whs/prod_stru/insertProdStru", false, "新增失败", prodStru);
            } catch (IOException e1) {


            }
            throw new RuntimeException("新增失败");
        }
        return resp;
    }

    // 修改产品结构
    @Override
    public String updateProdStru(ProdStru prodStru, List<ProdStruSubTab> pList) {

        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        ProdStru prodStrus = prodStruMapper.selectMomEncd(prodStru.getMomEncd());
        if (prodStrus.getIsNtChk() == null) {
            throw new RuntimeException("单号审核状态异常");
        } else if (prodStrus.getIsNtChk() == 1) {
            throw new RuntimeException("单号已审核不允许修改");
        }
        InvtyDoc invty = Optional.ofNullable(invtyDocDao.selectInvtyDocByInvtyDocEncd(prodStru.getMomEncd()))
                .orElseThrow(() -> new RuntimeException("该存货不存在"));
        Integer BOM = Optional.ofNullable(invty.getAllowBomMain()).orElseThrow(() -> new RuntimeException("BOM主件属于为空"));

        if (BOM == 0) {
            try {
                resp = BaseJson.returnRespObj("whs/prod_stru/insertProdStru", false, "该存货不属于BOM主件", null);
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
        message = "更新成功！";

        try {
            resp = BaseJson.returnRespObj("whs/prod_stru/updateProdStru", isSuccess, message, null);
        } catch (IOException e) {

        }
        return resp;

    }

    // 删除产品结构
    // 批量删除
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
            message = "删除成功！";
            if (lists2.size() > 0) {
                message = "存在已审单据";
            }
        } else {
            message = "单据已审";
        }

        try {
            resp = BaseJson.returnRespObj("whs/prod_stru/deleteAllProdStru", isSuccess, message, null);
        } catch (IOException e) {

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
        List<String> list = new ArrayList<String>();
        String[] str = id.split(",");
        for (int i = 0; i < str.length; i++) {
            list.add(str[i]);
        }
        return list;
    }

    // 简单查 产品结构

    // 查询、分页查
    @Override
    public String query(String ordrNum) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        List<ProdStruSubTab> pSubTabList = new ArrayList<>();
        ProdStru prodStru = prodStruMapper.selectProdStru(ordrNum);
        if (prodStru != null) {
            pSubTabList = prodStruMapper.selectProdStruSubTabList(ordrNum);
            message = "查询成功！";
        } else {
            isSuccess = false;
            message = "编号" + ordrNum + "不存在！";
        }

        try {
            resp = BaseJson.returnRespObjList("whs/prod_stru/query", isSuccess, message, prodStru, pSubTabList);
        } catch (IOException e) {

        }
        return resp;
    }

    // 分页查询
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
            resp = BaseJson.returnRespList("whs/prod_stru/queryList", true, "查询成功！", count, pageNo, pageSize, listNum,
                    pages, proList);
        } catch (IOException e) {

        }
        return resp;
    }

    // 打印
    @Override
    public String queryListPrint(Map map) {
        String resp = "";
        List<ProdStruMap> proList = prodStruMapper.queryListPrint(map);
        proList.add(new ProdStruMap());
        try {
            resp = BaseJson.returnRespObjListAnno("whs/prod_stru/queryListPrint", true, "查询成功！", null, proList);
        } catch (IOException e) {

        }
        return resp;
    }

    // 查询 获取该母件的产品结构信息
    @Override
    public String selectProdStruByMom(String ordrNum) {
        String resp = "";
        ProdStru proList = prodStruMapper.selectMomEncd(ordrNum);
        try {
            if (proList != null) {
                resp = BaseJson.returnRespObj("whs/prod_stru/selectProdStruByMom", true, "查询成功！", proList);
            } else {
                resp = BaseJson.returnRespObj("whs/prod_stru/selectProdStruByMom", false, "查询失败！", proList);
            }

        } catch (IOException e) {

        }
        return resp;
    }

    // 审核
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
                messages += prodStru.getMomEncd() + "已审核!  ";
            } else {
                prodStru.setChkr(chkr);
                asd.add(prodStru);
            }
        }
        if (asd.size() > 0) {
            int i = prodStruMapper.updatePStruChk(asd);
            if (i >= 1) {
                message = "审核成功！";
                isSuccess = true;
            } else {
                message = "审核失败！";
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

    // 弃审
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
                messages += prodStru.getMomEncd() + "已弃核!  ";
            } else {
                asd.add(prodStru);
            }
        }
        if (asd.size() > 0) {
            int i = prodStruMapper.updatePStruNoChk(cList);
            if (i >= 1) {
                message = "弃审成功！";
                isSuccess = true;
            } else {
                message = "弃审失败！";
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

    // 导入
    public String uploadFileAddDb(MultipartFile file) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        Map<String, ProdStru> regnMap = uploadScoreInfo(file);
        for (Map.Entry<String, ProdStru> entry : regnMap.entrySet()) {
            // System.out.println(entry);

            if (prodStruMapper.selectMomEncd(entry.getValue().getMomEncd()) != null) {
                isSuccess = false;
                message = "档案中部分已存在，无法导入，请查明再导入！";
                throw new RuntimeException(message);
            } else {
                prodStruMapper.exIinsertProdStru(entry.getValue());
                for (ProdStruSubTab prodStruSubTab : entry.getValue().getStruSubList()) {
                    prodStruSubTab.setOrdrNum(entry.getValue().getOrdrNum());
                }
                prodStruMapper.insertProdStruSubTab(entry.getValue().getStruSubList());

                isSuccess = true;
                message = "档案导入成功！";
            }
        }
        try {
            resp = BaseJson.returnRespObj("whs/prod_stru/uploadRegnFile", isSuccess, message, null);
        } catch (IOException e) {

        }
        return resp;
    }

    // 导入excle
    private Map<String, ProdStru> uploadScoreInfo(MultipartFile file) {
        Map<String, ProdStru> temp = new HashMap<>();
        int j = 1;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            InputStream fileIn = file.getInputStream();
            // 根据指定的文件输入流导入Excel从而产生Workbook对象
            Workbook wb0 = new HSSFWorkbook(fileIn);
            // 获取Excel文档中的第一个表单
            Sheet sht0 = wb0.getSheetAt(0);
            // 获得当前sheet的开始行
            int firstRowNum = sht0.getFirstRowNum();
            // 获取文件的最后一行
            int lastRowNum = sht0.getLastRowNum();
            // 设置中文字段和下标映射
            SetColIndex(sht0.getRow(firstRowNum));
            // 对Sheet中的每一行进行迭代
            for (int i = firstRowNum + 1; i <= lastRowNum; i++) {
                j++;
                Row r = sht0.getRow(i);
                // 如果当前行的行号（从0开始）未达到2（第三行）则从新循环
                if (r.getRowNum() < 1) {
                    continue;
                }
                String orderNo = GetCellData(r, "母件编码");
                // 创建实体类
                ProdStru prodStru = new ProdStru();
                if (temp.containsKey(orderNo)) {
                    prodStru = temp.get(orderNo);
                }
                // 取出当前行第1个单元格数据，并封装在实体的各个属性上
//				prodStru.setOrdrNum(ordrNum);		//	bigint(20	'序号',
                prodStru.setMomEncd(orderNo); // varchar(200 '母件编码',
                prodStru.setMomNm(GetCellData(r, "母件名称")); // varchar(200 '母件名称',
                prodStru.setEdComnt(GetCellData(r, "版本说明")); // varchar(200 '版本说明',
                prodStru.setMomSpc(GetCellData(r, "母件规格")); // varchar(200 '母件规格',
                prodStru.setMemo(GetCellData(r, "母件备注")); // varchar(2000 '备注',
                prodStru.setSetupPers(GetCellData(r, "创建人")); // varchar(200 '创建人',
                prodStru.setSetupTm(
                        GetCellData(r, "创建时间") == null ? "" : GetCellData(r, "创建时间")); // datetime
                // '创建时间',
                prodStru.setMdfr(GetCellData(r, "修改人")); // varchar(200 '修改人',
                prodStru.setModiTm(
                        GetCellData(r, "修改时间") == null ? "" : GetCellData(r, "修改时间")); // datetime
                // '修改时间',
                prodStru.setChkr(GetCellData(r, "审核人")); // varchar(200 '审核人',
                prodStru.setChkTm(
                        GetCellData(r, "审核时间") == null ? "" : GetCellData(r, "审核时间")); // datetime
                // '审核时间',
//				prodStru.setIsNtWms((new Double(GetCellData(r, "是否向WMS上传"))).intValue());		//	int(11	'是否向WMS上传',
                prodStru.setIsNtChk(
                        (new Double(GetCellData(r, "是否审核") == null || GetCellData(r, "是否审核").equals("") ? "0"
                                : GetCellData(r, "是否审核"))).intValue()); // int(11 '是否审核',
//                prodStru.setIsNtCmplt(
//                        (new Double(GetCellData(r, "是否完成") == null || GetCellData(r, "是否完成").equals("") ? "0"
//                                : GetCellData(r, "是否完成"))).intValue()); // int(11 '是否完成',
//                prodStru.setIsNtClos(
//                        (new Double(GetCellData(r, "是否关闭") == null || GetCellData(r, "是否关闭").equals("") ? "0"
//                                : GetCellData(r, "是否关闭"))).intValue()); // int(11 '是否关闭',
//                prodStru.setPrintCnt(
//                        (new Double(GetCellData(r, "打印次数") == null || GetCellData(r, "打印次数").equals("") ? "0"
//                                : GetCellData(r, "打印次数"))).intValue()); // int(11 '打印次数',

                List<ProdStruSubTab> ProdStruSubTabs = prodStru.getStruSubList();// 子表
                if (ProdStruSubTabs == null) {
                    ProdStruSubTabs = new ArrayList<>();
                }
                ProdStruSubTab prodStruSubTab = new ProdStruSubTab();
//				prodStruSubTab.setOrdrNumSub(ordrNumSub);		//	bigint(20	'序号',	
                prodStruSubTab.setSubEncd(GetCellData(r, "子件编码")); // varchar(200 '子件编码',
//				prodStruSubTab.setOrdrNum(ordrNum);		//	bigint(20	'产品结构主标识',	
                prodStruSubTab.setMomEncd(GetCellData(r, "母件编码")); // varchar(200 '母件编码',
                prodStruSubTab.setSubNm(GetCellData(r, "子件名称")); // varchar(200 '子件名称',
                prodStruSubTab.setSubSpc(GetCellData(r, "子件规格")); // varchar(200 '子件规格',
                prodStruSubTab.setMeasrCorp(GetCellData(r, "子件计量单位编码")); // varchar(200 '子件计量单位编码',
                prodStruSubTab.setSubQty(
                        new BigDecimal(GetCellData(r, "子件数量") == null || GetCellData(r, "子件数量").equals("") ? "0"
                                : GetCellData(r, "子件数量")).setScale(8, BigDecimal.ROUND_HALF_UP)); // decimal(20,8
                // '子件数量',
                prodStruSubTab.setMomQty(
                        new BigDecimal(GetCellData(r, "母件数量") == null || GetCellData(r, "母件数量").equals("") ? "0"
                                : GetCellData(r, "母件数量")).setScale(8, BigDecimal.ROUND_HALF_UP)); // decimal(20,8
                // '母件数量',
                prodStruSubTab.setMemo(GetCellData(r, "子件备注")); // varchar(2000 '备注',

                prodStruSubTab.setBxRule(new BigDecimal(
                        GetCellData(r, "子件箱规") == null || GetCellData(r, "子件箱规").equals("") ? "0" : GetCellData(r, "子件箱规"))
                        .setScale(8, BigDecimal.ROUND_HALF_UP)); // decimal(20,8
                // '箱规',
                ProdStruSubTabs.add(prodStruSubTab);

                prodStru.setStruSubList(ProdStruSubTabs);
                temp.put(orderNo, prodStru);

            }
            fileIn.close();
        } catch (Exception e) {
            throw new RuntimeException("文件的第" + j + "行导入格式有误" +e.getMessage());
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
            message = "查询成功！";
        } else {
            isSuccess = false;
            message = "编号" + map.get("momEncd") + "不存在！";
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
                    message = "查询成功！";
                } else {
                    isSuccess = false;
                    message = "编号" + map.get("momEncd") + "不存在！";
                }
                return resp = BaseJson.returnRespList("whs/prod_stru/queryAmbDisambSngl", true, "查询成功！", count, pageNo, pageSize, listNum,
                        pages, proList);
            } else {
                return resp = BaseJson.returnRespObjList("whs/prod_stru/queryAmbDisambSngl", isSuccess, message, null, proList);
            }

        } catch (IOException e) {

        }
        return resp;
    }
}
