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

//货位管理
@Service
@Transactional
public class GdsBitServiceImpl extends poiTool implements GdsBitService {

    @Autowired
    GdsBitMapper gdsBitMapper;

    // 新增货位档案
    @Override
    public ObjectNode insertGdsBit(GdsBit record) {
        ObjectNode on = null;
        try {
            on = JacksonUtil.getObjectNode("");
//		if (record.getRegnEncd() != null && record.getRegnEncd().length() > 0) {
//			record.setGdsBitEncd(record.getRegnEncd() + "-" + record.getGdsBitEncd());
//		} else {
//			on.put("isSuccess", false);
//			on.put("message", "请输入区域编码");
//			return on;
//		}
        GdsBit records = gdsBitMapper.selectGdsBitbyId(record.getGdsBitEncd());
        if (records != null) {
            on.put("isSuccess", false);
            on.put("message", record.getGdsBitEncd() + " 货位编码重复！");
            return on;
        }
        int gdsBit = gdsBitMapper.insertGdsBit(record);
        if (gdsBit == 1) {
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

    // 修改货位档案
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
            on.put("message", "修改成功！");
        } else {
            on.put("isSuccess", false);
            on.put("message", "修改失败！");
        }
        return on;
    }

    // 删除货位档案
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
            on.put("message", "删除成功！");
        } else {
            on.put("isSuccess", false);
            on.put("message", "删除失败！");
        }
        return on;
    }

    // 简单查 货位档案
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

    // 批删
    @Override
    public String deleteGdsBitList(String gdsBitEncd) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";

        List<String> list = getList(gdsBitEncd);

        if (gdsBitMapper.selectGBitList(list).size() > 0) {
            gdsBitMapper.deleteGdsBitList(list);
            isSuccess = true;
            message = "删除成功！";
        } else {
            isSuccess = false;
            message = "编号" + gdsBitEncd + "不存在！";
        }

        try {
            resp = BaseJson.returnRespObj("whs/gds_bit/deleteGdsBitList", isSuccess, message, null);
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

    // 分页查询
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
            resp = BaseJson.returnRespList("whs/gds_bit/queryList", true, "查询成功！", count, pageNo, pageSize, listNum,
                    pages, aList);
        } catch (IOException e) {

        }
        return resp;
    }

    // 查询所有货位类型
    @Override
    public String selectgTypList() {
        String resp = "";
        List<GdsBitTyp> gList = gdsBitMapper.selectgTypList();

        try {
            resp = BaseJson.returnRespObjList("whs/gds_bit/selectgTypList", true, "查询成功！", null, gList);
        } catch (IOException e) {

        }
        return resp;
    }

    // 打印
    @Override
    public String queryListDaYin(Map map) {
        String resp = "";
        List<GdsBit> aList = gdsBitMapper.selectListDaYin(map);
        aList.add(new GdsBit());

        try {
            resp = BaseJson.returnRespObjListAnno("whs/gds_bit/queryListDaYin", true, "查询成功！", null, aList);
        } catch (IOException e) {

        }
        return resp;
    }

    // 导入
    public String uploadFileAddDb(MultipartFile file) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        Map<String, GdsBit> gdsBitMap = uploadScoreInfo(file);
        for (Map.Entry<String, GdsBit> entry : gdsBitMap.entrySet()) {
            if (gdsBitMapper.selectGdsBit(entry.getValue().getGdsBitEncd()) != null) {
                isSuccess = false;
                message = "货位档案中部分档案已存在，无法导入，请查明再导入！";
                throw new RuntimeException(message);
            } else {
                gdsBitMapper.insertGdsBit(entry.getValue());
                isSuccess = true;
                message = "货位档案导入成功！";
            }
        }
        try {
            resp = BaseJson.returnRespObj("whs/gds_bit/uploadGdsBitFile", isSuccess, message, null);
        } catch (IOException e) {

        }
        return resp;
    }

    // 导入excle
    private Map<String, GdsBit> uploadScoreInfo(MultipartFile file) {
        int j = 1;
        Map<String, GdsBit> temp = new HashMap<>();
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
                String orderNo = GetCellData(r, "货位编码");
                // 创建实体类
                GdsBit gdsBit = new GdsBit();
                if (temp.containsKey(orderNo)) {
                    gdsBit = temp.get(orderNo);
                }
                // 取出当前行第1个单元格数据，并封装在实体的各个属性上
                gdsBit.setGdsBitEncd(orderNo);// 货位编码
//                gdsBit.setRegnEncd(GetCellData(r, "区域编码"));// 区域编码
                gdsBit.setGdsBitNm(GetCellData(r, "货位名称"));// 货位名称
                gdsBit.setGdsBitTyp(GetCellData(r, "货位类型编码"));// 货位类型
//                gdsBit.setGdsBitTypEncd(GetCellData(r, "货位类型"));// 货位类型编码
                gdsBit.setGdsBitCd(GetCellData(r, "货位代码"));// 货位代码
                gdsBit.setGdsBitQty(GetBigDecimal(GetCellData(r, "货位存放量"), 8));// 货位存放量
                gdsBit.setMemo(GetCellData(r, "备注"));// 备注
//                gdsBit.setGdsBitCdEncd(GetCellData(r, "货位码编码"));// 货位码编码
                gdsBit.setSetupPers(GetCellData(r, "创建人"));// 创建人
                if (GetCellData(r, "创建时间") == null || GetCellData(r, "创建时间").equals("")) {
                    gdsBit.setSetupTm(null);
                } else {
                    gdsBit.setSetupTm(GetCellData(r, "创建时间"));// 创建时间
                }
                gdsBit.setMdfr(GetCellData(r, "修改人"));// 修改人
                if (GetCellData(r, "修改时间") == null || GetCellData(r, "修改时间").equals("")) {
                    gdsBit.setModiTm(null);
                } else {
                    gdsBit.setModiTm(GetCellData(r, "修改时间"));// 修改时间
                }
                temp.put(orderNo, gdsBit);
            }
            fileIn.close();
        } catch (Exception e) {

            throw new RuntimeException("文件的第" + j + "行导入格式有误" +e.getMessage());
        }
        return temp;
    }

}
