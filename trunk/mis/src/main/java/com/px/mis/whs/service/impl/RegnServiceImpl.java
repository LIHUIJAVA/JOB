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

    // 新增区域档案
    @Override
    public ObjectNode insertRegn(Regn record) {
        ObjectNode on = null;
        try {
            on = JacksonUtil.getObjectNode("");

//			if (record.getWhsEncd() != null && record.getWhsEncd().length() > 0) {
//				record.setRegnEncd(record.getWhsEncd() + "-" + record.getRegnEncd());
//			} else {
//				on.put("isSuccess", false);
//				on.put("message", "请输入仓库编码");
//				return on;
//			}
            Regn records = regnMapper.selectRegn(record.getRegnEncd());
            if (records != null) {
                on.put("isSuccess", false);
                on.put("message", record.getRegnEncd() + " 区域编码重复！");
                return on;
            }

            int Regn = regnMapper.insertRegn(record);
            if (Regn == 1) {
                on.put("isSuccess", true);
                on.put("message", "新增成功！");
            } else {
                on.put("isSuccess", false);
                on.put("message", "新增失败！");
            }
        } catch (Exception e) {
            throw new RuntimeException("插入失败");

        }
        return on;
    }

    // 修改区域档案
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
            on.put("message", "修改成功！");
        } else {
            on.put("isSuccess", false);
            on.put("message", "修改失败！");
        }
        return on;
    }

    // 删除区域档案
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
            on.put("message", "删除成功！");
        } else {
            on.put("isSuccess", false);
            on.put("message", "删除失败！");
        }
        return on;
    }

    // 简单查 区域档案
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

    // 批删
    @Override
    public String deleteRegnList(String regnEncd) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";

        List<String> list = getList(regnEncd);

        if (regnMapper.selectRegnAllList(list).size() > 0) {
            regnMapper.deleteRegnList(list);
            isSuccess = true;
            message = "删除成功！";
        } else {
            isSuccess = false;
            message = "编号" + regnEncd + "不存在！";
        }

        try {
            resp = BaseJson.returnRespObj("whs/regn/deleteRegnList", isSuccess, message, null);
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
            resp = BaseJson.returnRespList("whs/regn/queryList", true, "查询成功！", count, pageNo, pageSize, listNum, pages,
                    aList);
        } catch (IOException e) {

        }
        return resp;
    }

    // 打印
    @Override
    public String queryListDaYin(Map map) {
        String resp = "";
        List<Regn> aList = regnMapper.selectListDaYin(map);
        aList.add(new Regn());
        try {
            resp = BaseJson.returnRespObjListAnno("whs/regn/queryListDaYin", true, "查询成功！", null, aList);
        } catch (IOException e) {

        }
        return resp;
    }

    // 导入
    public String uploadFileAddDb(MultipartFile file) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        Map<String, Regn> regnMap = uploadScoreInfo(file);
        for (Map.Entry<String, Regn> entry : regnMap.entrySet()) {
            if (regnMapper.selectRegn(entry.getValue().getRegnEncd()) != null) {
                isSuccess = false;
                message = "区域档案中部分档案已存在，无法导入，请查明再导入！";
                throw new RuntimeException(message);
            } else {
                regnMapper.insertRegn(entry.getValue());
                isSuccess = true;
                message = "区域档案导入成功！";
            }
        }
        try {
            resp = BaseJson.returnRespObj("whs/regn/uploadRegnFile", isSuccess, message, null);
        } catch (IOException e) {

        }
        return resp;
    }

    // 导入excle
    private Map<String, Regn> uploadScoreInfo(MultipartFile file) {
        Map<String, Regn> temp = new HashMap<>();
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
                String orderNo = GetCellData(r, "区域编码");
                // 创建实体类
                Regn regn = new Regn();
                if (temp.containsKey(orderNo)) {
                    regn = temp.get(orderNo);
                }
                // 取出当前行第1个单元格数据，并封装在实体的各个属性上
                regn.setRegnEncd(orderNo);// 区域编码
//                regn.setWhsEncd(GetCellData(r, "仓库编码"));// 仓库编码
                regn.setRegnNm(GetCellData(r, "区域名称"));// 区域名称
                regn.setLongs(
                        (new Double(GetCellData(r, "长") == null || GetCellData(r, "长").equals("") ? "0"
                                : GetCellData(r, "长"))).intValue()+"");// 长
                regn.setWide(
                        (new Double(GetCellData(r, "宽") == null || GetCellData(r, "宽").equals("") ? "0"
                                : GetCellData(r, "宽"))).intValue()+"");// 宽
                regn.setSiteQty(GetBigDecimal(GetCellData(r, "地堆数量"), 8));// 地堆数量
                regn.setMemo(GetCellData(r, "备注"));// 备注
                regn.setSetupPers(GetCellData(r, "创建人"));// 创建人
                if (GetCellData(r, "创建时间") == null || GetCellData(r, "创建时间").equals("")) {
                    regn.setSetupTm(null);
                } else {
                    regn.setSetupTm(GetCellData(r, "创建时间"));// 创建时间
                }
                regn.setMdfr(GetCellData(r, "修改人"));// 修改人
                if (GetCellData(r, "修改时间") == null || GetCellData(r, "修改时间").equals("")) {
                    regn.setModiTm(null);
                } else {
                    regn.setModiTm(GetCellData(r, "创建时间"));// 修改时间
                }
                temp.put(orderNo, regn);
            }
            fileIn.close();
        } catch (Exception e) {

            throw new RuntimeException("文件的第" + j + "行导入格式有误" +e.getMessage());
        }
        return temp;
    }

}
