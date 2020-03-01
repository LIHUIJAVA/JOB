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

    // 新增物流公司
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
            on.put("message", record.getGdFlowEncd() + " 编码重复！");
            return on;
        }
        int gdFlow = gdFlowCorpMapper.insertGdFlowCorp(record);
        if (gdFlow == 1) {
            on.put("isSuccess", true);
            on.put("message", "新增成功！");
        } else {
            on.put("isSuccess", false);
            on.put("message", "新增失败！");
        }
        return on;
    }

    // 修改物流公司
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
            on.put("message", "修改成功！");
        } else {
            on.put("isSuccess", false);
            on.put("message", "修改失败！");
        }
        return on;
    }

    // 删除物流公司
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
            on.put("message", "删除成功！");
        } else {
            on.put("isSuccess", false);
            on.put("message", "删除失败！");
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

    // 分页查询
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
            resp = BaseJson.returnRespList("whs/gd_flow_crop/queryList", true, "查询成功！", count, pageNo, pageSize,
                    listNum, pages, aList);
        } catch (IOException e) {

        }
        return resp;
    }

    // 批删
    @Override
    public String deleteGFlowCorpList(String gdFlowEncd) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";

        List<String> list = getList(gdFlowEncd);

        if (gdFlowCorpMapper.selectGFlowCorpAllList(list).size() > 0) {
            gdFlowCorpMapper.deleteGFlowCorpList(list);
            isSuccess = true;
            message = "删除成功！";
        } else {
            isSuccess = false;
            message = "编号" + gdFlowEncd + "不存在！";
        }

        try {
            resp = BaseJson.returnRespObj("whs/gd_flow_crop/deleteGFlowCorpList", isSuccess, message, null);
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

    // 打印
    @Override
    public String queryListDaYin(Map map) {
        String resp = "";
        List<GdFlowCorpMap> aList = gdFlowCorpMapper.selectListDaYin(map);
        aList.add(new GdFlowCorpMap());
        try {
            resp = BaseJson.returnRespObjListAnno("whs/gd_flow_crop/queryListDaYin", true, "查询成功！", null, aList);
        } catch (IOException e) {

        }
        return resp;
    }

    // 导入
    public String uploadFileAddDb(MultipartFile file) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        Map<String, GdFlowCorp> gdFlowCropMap = uploadScoreInfo(file);
        for (Map.Entry<String, GdFlowCorp> entry : gdFlowCropMap.entrySet()) {
            if (gdFlowCorpMapper.selectGdFlowCorp(entry.getValue().getGdFlowEncd()) != null) {
                isSuccess = false;
                message = "物流档案中部分档案已存在，无法导入，请查明再导入！";
                throw new RuntimeException(message);
            } else {
                gdFlowCorpMapper.insertGdFlowCorp(entry.getValue());
                isSuccess = true;
                message = "物流档案导入成功！";
            }
        }
        try {
            resp = BaseJson.returnRespObj("whs/gd_flow_crop/uploadGdFlowCropFile", isSuccess, message, null);
        } catch (IOException e) {

        }
        return resp;
    }

    /*
     * //判断是否字段 public String GetCellData(Row row,String ColNameCN) throws Exception
     * { Map<String,Integer> ColMap= new HashMap<String, Integer>(); Object obj =
     * ColMap.get(ColNameCN.trim()); if(obj==null) { throw new Exception("不存在列名 [" +
     * ColNameCN +"],请检查"); } String result = getValue(row.getCell((int)obj));
     * if((result == null)||result.trim().equals("")) { if(ColNameCN.equals("是否审核"))
     * result = "0"; } return result==null?result:result.trim(); }
     */
    // 导入excle
    private Map<String, GdFlowCorp> uploadScoreInfo(MultipartFile file) {
        Map<String, GdFlowCorp> temp = new HashMap<>();
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
                String orderNo = GetCellData(r, "物流公司编码");
                // 创建实体类
                GdFlowCorp gdFlowCorp = new GdFlowCorp();
                if (temp.containsKey(orderNo)) {
                    gdFlowCorp = temp.get(orderNo);
                }
                // 取出当前行第1个单元格数据，并封装在实体的各个属性上
                gdFlowCorp.setGdFlowEncd(orderNo);// 物流公司编号
                gdFlowCorp.setGdFlowNm(GetCellData(r, "物流公司名称"));// 物流公司名称
                gdFlowCorp.setTraffMode(GetCellData(r, "运输方式"));// 运输方式
                gdFlowCorp.setTraffVehic(GetCellData(r, "运输车辆"));// 运输车辆
//                gdFlowCorp.setTraffFormNum(GetCellData(r, "运输单号"));// 运输单号
//                gdFlowCorp.setPrintCnt(new Double(GetCellData(r, "打印次数")).intValue());// 打印次数
                gdFlowCorp.setMemo(GetCellData(r, "备注"));// 备注
//                gdFlowCorp.setIsNtChk(new Double(GetCellData(r, "是否审核")).intValue());// 是否审核
                gdFlowCorp.setSetupPers(GetCellData(r, "创建人"));// 创建人
                if (GetCellData(r, "创建时间") == null || GetCellData(r, "创建时间").equals("")) {
                    gdFlowCorp.setSetupTm(null);
                } else {
                    gdFlowCorp.setSetupTm(GetCellData(r, "创建时间"));// 创建时间
                }
                gdFlowCorp.setMdfr(GetCellData(r, "修改人"));// 修改人
                if (GetCellData(r, "修改时间") == null || GetCellData(r, "修改时间").equals("")) {
                    gdFlowCorp.setModiTm(null);
                } else {
                    gdFlowCorp.setModiTm(GetCellData(r, "修改时间"));// 修改时间
                }
//                gdFlowCorp.setChkr(GetCellData(r, "审核人"));// 审核人
//                if (GetCellData(r, "审核时间") == null || GetCellData(r, "审核时间").equals("")) {
//                    gdFlowCorp.setChkTm(null);
//                } else {
//                    gdFlowCorp.setChkTm(GetCellData(r, "审核时间"));// 审核时间
//                }
                temp.put(orderNo, gdFlowCorp);
            }
            fileIn.close();
        } catch (Exception e) {
            throw new RuntimeException("文件的第" + j + "行导入格式有误" +e.getMessage());
        }
        return temp;
    }

}
