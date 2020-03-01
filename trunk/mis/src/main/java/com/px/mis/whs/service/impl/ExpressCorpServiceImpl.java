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

    // 新增快递公司
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
            on.put("message", record.getExpressEncd() + " 编码重复！");
            return on;
        }
        int expressCorp = expressCorpMapper.insertExpressCorp(record);
        if (expressCorp == 1) {
            on.put("isSuccess", true);
            on.put("message", "新增成功！");
        } else {
            on.put("isSuccess", false);
            on.put("message", "新增失败！");
        }
        return on;
    }

    // 修改快递公司
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
            on.put("message", "修改成功！");
        } else {
            on.put("isSuccess", false);
            on.put("message", "修改失败！");
        }
        return on;
    }

    // 删除快递公司
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
            on.put("message", "删除成功！");
        } else {
            on.put("isSuccess", false);
            on.put("message", "删除失败！");
        }
        return on;
    }

    // 简单查 快递公司
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

    // 分页查询
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
            resp = BaseJson.returnRespList("whs/express_crop/queryList", true, "查询成功！", count, pageNo, pageSize,
                    listNum, pages, aList);
        } catch (IOException e) {

        }
        return resp;
    }

    // 批删
    @Override
    public String deleteECorpList(String expressEncd) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";

        List<String> list = getList(expressEncd);

        if (expressCorpMapper.selectECorpAllList(list).size() > 0) {
            expressCorpMapper.deleteECorpList(list);
            isSuccess = true;
            message = "删除成功！";
        } else {
            isSuccess = false;
            message = "编号" + expressEncd + "不存在！";
        }

        try {
            resp = BaseJson.returnRespObj("whs/express_crop/deleteECorpList", isSuccess, message, null);
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
        List<ExpressCorpMap> aList = expressCorpMapper.selectListDaYin(map);
        aList.add(new ExpressCorpMap());

        try {
            resp = BaseJson.returnRespObjListAnno("whs/express_crop/queryListDaYin", true, "查询成功！", null, aList);
        } catch (IOException e) {

        }
        return resp;
    }

    // 导入
    public String uploadFileAddDb(MultipartFile file) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        Map<String, ExpressCorp> expressCorpMap = uploadScoreInfo(file);
        for (Map.Entry<String, ExpressCorp> entry : expressCorpMap.entrySet()) {
            if (expressCorpMapper.selectExpressCorp(entry.getValue().getExpressEncd()) != null) {
                isSuccess = false;
                message = "快递档案中部分档案已存在，无法导入，请查明再导入！";
                throw new RuntimeException(message);
            } else {
                expressCorpMapper.insertExpressCorp(entry.getValue());
                isSuccess = true;
                message = "快递档案导入成功！";
            }
        }
        try {
            resp = BaseJson.returnRespObj("whs/express_crop/uploadExpressCorpFile", isSuccess, message, null);
        } catch (IOException e) {

        }
        return resp;
    }

    /*
     * //判断是否字段 public String GetCellData(Row row,String ColNameCN) throws Exception
     * { Map<String,Integer> ColMap= new HashMap<String, Integer>(); Object obj =
     * ColMap.get(ColNameCN.trim()); if(obj==null) { throw new Exception("不存在列名 [" +
     * ColNameCN +"],请检查"); } String result = getValue(row.getCell((int)obj));
     * if((result == null)||result.trim().equals("")) {
     * if(ColNameCN.equals("是否支持货到付款")) result = "0"; if(ColNameCN.equals("是否停用"))
     * result = "0"; if(ColNameCN.equals("是否审核")) result = "0"; } return
     * result==null?result:result.trim(); }
     *
     */
    // 导入excle
    private Map<String, ExpressCorp> uploadScoreInfo(MultipartFile file) {
        Map<String, ExpressCorp> temp = new HashMap<>();
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
                String orderNo = GetCellData(r, "快递公司编码");
                // 创建实体类
                ExpressCorp expressCorp = new ExpressCorp();
                if (temp.containsKey(orderNo)) {
                    expressCorp = temp.get(orderNo);
                }
                // 取出当前行第1个单元格数据，并封装在实体的各个属性上
                expressCorp.setExpressEncd(orderNo);// 快递公司编号
                expressCorp.setExpressNm(GetCellData(r, "快递公司名称"));// 快递公司名称
//                expressCorp.setExpressTyp(GetCellData(r, "快递类型"));// 快递类型
//                expressCorp.setExpOrderId(GetCellData(r, "快递单号"));// 快递单号
//                expressCorp.setSellHomeInfo(GetCellData(r, "卖家信息"));// 卖家信息
//                expressCorp.setBuyHomeInfo(GetCellData(r, "买家信息"));// 买家信息
//                expressCorp.setIsNtSprtGdsToPay(new Double(GetCellData(r, "是否支持货到付款")).intValue());// 是否支持货到付款
//                expressCorp.setGdsToPayServCost(GetBigDecimal(GetCellData(r, "货到付款服务费"), 8));// 到货付款服务费
                expressCorp.setIsNtStpUse(new Double(GetCellData(r, "是否停用")==null || GetCellData(r, "是否停用").equals("")? "0":GetCellData(r, "是否停用") ).intValue());// 是否停用
                expressCorp.setFirstWet(GetBigDecimal(GetCellData(r, "首重"), 8));// 首重
                expressCorp.setFirstWetStrPrice(GetBigDecimal(GetCellData(r, "首重起价"), 8));// 首重起价
//                expressCorp.setPrintCnt(new Double(GetCellData(r, "打印次数")).intValue());// 打印次数
                expressCorp.setMemo(GetCellData(r, "备注"));// 备注
//                expressCorp.setIsNtChk(new Double(GetCellData(r, "是否审核")).intValue());// 是否审核
                expressCorp.setSetupPers(GetCellData(r, "创建人"));// 创建人
                if (GetCellData(r, "创建时间") == null || GetCellData(r, "创建时间").equals("")) {
                    expressCorp.setSetupTm(null);
                } else {
                    expressCorp.setSetupTm(GetCellData(r, "创建时间"));// 创建时间
                }
                expressCorp.setMdfr(GetCellData(r, "修改人"));// 修改人
                if (GetCellData(r, "修改时间") == null || GetCellData(r, "修改时间").equals("")) {
                    expressCorp.setModiTm(null);
                } else {
                    expressCorp.setModiTm(GetCellData(r, "修改时间"));// 修改时间
                }
//                expressCorp.setChkr(GetCellData(r, "审核人"));// 审核人
//                if (GetCellData(r, "审核时间") == null || GetCellData(r, "审核时间").equals("")) {
//                    expressCorp.setChkTm(null);
//                } else {
//                    expressCorp.setChkTm(GetCellData(r, "审核时间"));// 审核时间
//                }
                expressCorp.setDeliver(GetCellData(r, "发货人姓名"));// 发货人姓名
                expressCorp.setDeliverPhone(GetCellData(r, "发货人手机"));// 发货人手机
                expressCorp.setDeliverMobile(GetCellData(r, "发货人座机"));// 发货人座机
                expressCorp.setCompanyCode(GetCellData(r, "快递公司代码"));// 快递公司代码
                expressCorp.setProvince(GetCellData(r, "发货地省"));// 发货地省
                expressCorp.setCity(GetCellData(r, "发货地市"));// 发货地市
                expressCorp.setCountry(GetCellData(r, "发货地区"));// 发货地区
                expressCorp.setDetailedAddress(GetCellData(r, "详细地址"));// 详细地址
                temp.put(orderNo, expressCorp);
            }
            fileIn.close();
        } catch (Exception e) {

            throw new RuntimeException("文件的第" + j + "行导入格式有误" +e.getMessage());
        }
        return temp;
    }

    @Override
    public String queryExpressCodeAndNameList() {
        String resp = "";

        try {
            List<ExpressCodeAndName> aList = expressCorpMapper.queryExpressCodeAndNameList();
            resp = BaseJson.returnRespObjList("whs/express_crop/queryExpressCodeAndNameList", true, "查询成功！", null, aList);
        } catch (IOException e) {

        }
        return resp;
    }

}
