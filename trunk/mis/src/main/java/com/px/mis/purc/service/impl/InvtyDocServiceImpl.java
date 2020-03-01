package com.px.mis.purc.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.px.mis.purc.entity.InvtyCls;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.purc.dao.InvtyClsDao;
import com.px.mis.purc.dao.InvtyDocDao;
import com.px.mis.purc.entity.InvtyDoc;
import com.px.mis.purc.service.InvtyDocService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;
import com.px.mis.util.poiTool;

//存货档案功能
@Transactional
@Service
public class InvtyDocServiceImpl extends poiTool implements InvtyDocService {
    @Autowired
    private InvtyDocDao idd;
    @Autowired
    private InvtyClsDao icd;

    //新增存货档案信息
    @Override
    public String insertInvtyDoc(InvtyDoc invtyDoc) {

        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        try {
            if (invtyDoc.getInvtyEncd() != "" && invtyDoc.getInvtyEncd() != null) {
                if (icd.selectInvtyClsByInvtyClsEncd(invtyDoc.getInvtyClsEncd()) != null) {
                    if (idd.selectInvtyEncd(invtyDoc.getInvtyEncd()) != null) {
                        isSuccess = false;
                        message = "编号" + invtyDoc.getInvtyEncd() + "已存在，请重新输入！";
                    } else {
                        if (invtyDoc.getIsNtBarCdMgmt() == null) {
                            invtyDoc.setIsNtBarCdMgmt(0);//是否条形码管理
                        }
                        if (invtyDoc.getIsDomSale() == null) {
                            invtyDoc.setIsDomSale(0);//是否内销
                        }
                        if (invtyDoc.getIsNtDiscnt() == null) {
                            invtyDoc.setIsNtDiscnt(0);//是否折扣
                        }
                        if (invtyDoc.getIsNtPurs() == null) {
                            invtyDoc.setIsNtPurs(0);//是否采购
                        }
                        if (invtyDoc.getIsNtSell() == null) {
                            invtyDoc.setIsNtSell(0);//是否销售
                        }
                        if (invtyDoc.getPto() == null) {
                            invtyDoc.setPto(0);//是否PTO
                        }
                        if (invtyDoc.getIsQuaGuaPer() == null) {
                            invtyDoc.setIsQuaGuaPer(0);//是否保质期管理
                        }
                        if (invtyDoc.getShdTaxLabour() == null) {
                            invtyDoc.setShdTaxLabour(0);
                            ;//是否应税劳务
                        }
                        if (invtyDoc.getAllowBomMain() == null) {
                            invtyDoc.setAllowBomMain(0);//是否允许BOM主件
                        }
                        if (invtyDoc.getAllowBomMinor() == null) {
                            invtyDoc.setAllowBomMinor(0);//是否允许BOM子件
                        }
                        int a = idd.insertInvtyDoc(invtyDoc);
                        if (a > 0) {
                            isSuccess = true;
                            message = "存货档案新增成功！";
                        } else {
                            isSuccess = false;
                            message = "存货档案新增失败！";
                        }
                    }
                } else {
                    isSuccess = false;
                    message = "存货分类不存在！";
                }
            } else {
                isSuccess = false;
                message = "存货编码不能为空！";
            }
            resp = BaseJson.returnRespObj("purc/InvtyDoc/insertInvtyDoc", isSuccess, message, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resp;
    }

    //修改存货档案信息
    @Override
    public ObjectNode updateInvtyDocByInvtyDocEncd(InvtyDoc invtyDoc) {

        ObjectNode on = null;
        try {
            on = JacksonUtil.getObjectNode("");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (icd.selectInvtyClsByInvtyClsEncd(invtyDoc.getInvtyClsEncd()) != null) {
            int a = idd.updateInvtyDocByInvtyDocEncd(invtyDoc);

            if (a == 1) {
                on.put("isSuccess", true);
                on.put("message", "修改存货档案成功");
            } else {
                on.put("isSuccess", false);
                on.put("message", "修改存货档案失败");
            }
        } else {
            on.put("isSuccess", false);
            on.put("message", "存货分类不存在！");
        }
        return on;
    }

    //删除存货档案信息
    @Override
    public String deleteInvtyDocList(String invtyEncd) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        try {
            List<String> list = getList(invtyEncd);
            int a = idd.deleteInvtyDocList(list);
            if (a >= 1) {
                isSuccess = true;
                message = "删除成功！";
            } else {
                isSuccess = false;
                message = "删除失败！";
            }
            resp = BaseJson.returnRespObj("purc/InvtyDoc/deleteInvtyDocList", isSuccess, message, null);
        } catch (IOException e) {
            e.printStackTrace();
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
        if (StringUtils.isNotEmpty(id)) {
            if (id.contains(",")) {
                String[] str = id.split(",");
                for (int i = 0; i < str.length; i++) {
                    list.add(str[i]);
                }
            } else {
                if (StringUtils.isNotEmpty(id)) {
                    list.add(id);
                }
            }
        }
        return list;
    }

    //按照id  查询存货档案
    @Override
    public InvtyDoc selectInvtyDocByInvtyDocEncd(String invtyEncd) {
        InvtyDoc invtyDoc = idd.selectInvtyDocByInvtyDocEncd(invtyEncd);
        return invtyDoc;
    }

    //查询存货档案的所有信息
    @Override
    public String selectInvtyDocList(Map map) {
        String resp = "";
        List<String> invtyEncdList = getList((String) map.get("invtyEncd"));//存货编码
        map.put("invtyEncdList", invtyEncdList);
        String invtyClsEncd = (String) map.get("invtyClsEncd");
        if (invtyClsEncd != null && invtyClsEncd != "") {
            InvtyCls invtyCls = icd.selectInvtyClsByInvtyClsEncd(invtyClsEncd);
            if (invtyCls != null && invtyCls.getLevel() == 0) {
                map.put("invtyClsEncd", "");
            }
            //未查到存货分类编码对应存货分类 响应
            if (invtyCls == null) {
                String message = "存货分类编码" + invtyClsEncd + "不存在";
                try {
                    resp = BaseJson.returnRespObj("purc/InvtyDoc/selectInvtyDocList", false, message, null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return resp;
            }
        }
        List<InvtyDoc> invtyDocList = idd.selectInvtyDocList(map);
        int count = idd.selectInvtyDocCount(map);
        int pageNo = Integer.parseInt(map.get("pageNo").toString());
        int pageSize = Integer.parseInt(map.get("pageSize").toString());
        int listNum = invtyDocList.size();
        int pages = count / pageSize + 1;

        try {
            resp = BaseJson.returnRespList("purc/InvtyDoc/selectInvtyDocList", true, "查询成功！", count, pageNo, pageSize,
                    listNum, pages, invtyDocList);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return resp;
    }
    //现用导出接口
    @Override
    public String printingInvtyDocList(Map map) {
        String resp = "";
        List<String> invtyEncdList = getList((String) map.get("invtyEncd"));//存货编码
        map.put("invtyEncdList", invtyEncdList);
        String invtyClsEncd = (String) map.get("invtyClsEncd");
        if (invtyClsEncd != null && invtyClsEncd != "") {
            if (icd.selectInvtyClsByInvtyClsEncd(invtyClsEncd).getLevel() == 0) {
                map.put("invtyClsEncd", "");
            }
        }
        List<InvtyDoc> invtyDocList = idd.printingInvtyDocList(map);
        try {
            resp = BaseJson.returnRespObjList("purc/InvtyDoc/printingInvtyDocList", true, "查询成功！", null, invtyDocList);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return resp;
    }

    //导入
    public String uploadFileAddDb(MultipartFile file, int i) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        Map<String, InvtyDoc> invtyDocMap = null;
        if (i == 0) {
            invtyDocMap = uploadScoreInfo(file);
        } else if (i == 1) {
            invtyDocMap = uploadScoreInfoU8(file);
        } else {
            isSuccess = false;
            message = "导入出异常啦！";
            throw new RuntimeException(message);
        }
        for (Map.Entry<String, InvtyDoc> entry : invtyDocMap.entrySet()) {
            if (idd.selectInvtyEncd(entry.getValue().getInvtyEncd()) != null) {
                isSuccess = false;
                message = "存货档案中部分存货已存在，无法导入，请查明再导入！";
                throw new RuntimeException(message);
            } else {
                idd.insertInvtyDoc(entry.getValue());
                isSuccess = true;
                message = "存货档案导入成功！";
            }

        }
        try {
            if (i == 0) {
                resp = BaseJson.returnRespObj("purc/InvtyDoc/uploadInvtyDocFile", isSuccess, message, null);
            } else if (i == 1) {
                resp = BaseJson.returnRespObj("purc/InvtyDoc/uploadInvtyDocFileU8", isSuccess, message, null);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return resp;
    }

    //导入excle
    private Map<String, InvtyDoc> uploadScoreInfo(MultipartFile file) {
        Map<String, InvtyDoc> temp = new HashMap<>();
        int j = 1;
        try {
            InputStream fileIn = file.getInputStream();
            //根据指定的文件输入流导入Excel从而产生Workbook对象
            Workbook wb0 = new HSSFWorkbook(fileIn);
            //获取Excel文档中的第一个表单
            Sheet sht0 = wb0.getSheetAt(0);
            //获得当前sheet的开始行
            int firstRowNum = sht0.getFirstRowNum();
            //获取文件的最后一行
            int lastRowNum = sht0.getLastRowNum();
            //设置中文字段和下标映射
            SetColIndex(sht0.getRow(firstRowNum));
            //设置列名称
            getCellNames();
            //对Sheet中的每一行进行迭代
            for (int i = firstRowNum + 1; i <= lastRowNum; i++) {
                j++;
                Row r = sht0.getRow(i);
                //如果当前行的行号（从0开始）未达到2（第三行）则从新循环
                if (r.getRowNum() < 1) {
                    continue;
                }
                String orderNo = GetCellData(r, "存货编码");
                //创建实体类
                InvtyDoc invtyDoc = new InvtyDoc();
                if (temp.containsKey(orderNo)) {
                    invtyDoc = temp.get(orderNo);
                }
                //取出当前行第1个单元格数据，并封装在pursOrdr实体的各个属性上
                invtyDoc.setInvtyEncd(orderNo);//存货编码
                invtyDoc.setInvtyNm(GetCellData(r, "存货名称"));//存货名称
                invtyDoc.setInvtyClsEncd(GetCellData(r, "存货分类编码"));//存货分类编码
                invtyDoc.setSpcModel(GetCellData(r, "规格型号"));//规格型号
                invtyDoc.setInvtyCd(GetCellData(r, "存货代码"));//存货代码
//				invtyDoc.setProvrId(GetCellData(r,"供应商编码"));//供应商编码
                invtyDoc.setMeasrCorpId(GetCellData(r, "计量单位编码"));//主计量编码
                invtyDoc.setVol(GetCellData(r, "体积"));//体积
                invtyDoc.setWeight(GetBigDecimal(GetCellData(r, "重量"), 8));//8表示小数位数  //重量
                invtyDoc.setLongs(GetBigDecimal(GetCellData(r, "长"), 8));//8表示小数位数  //长
                invtyDoc.setWide(GetBigDecimal(GetCellData(r, "宽"), 8));//8表示小数位数  //宽
                invtyDoc.setHght(GetBigDecimal(GetCellData(r, "高"), 8));//8表示小数位数 //高
                invtyDoc.setBxRule(GetBigDecimal(GetCellData(r, "箱规"), 8));//8表示小数位数  //箱规
                invtyDoc.setBaoZhiQiEarWarn(GetCellData(r, "保质期预警天数"));//保质期预警天数
                invtyDoc.setBaoZhiQiDt(GetCellData(r, "保质期天数"));//保质期天数
                invtyDoc.setPlaceOfOrigin(GetCellData(r, "产地"));//产地
                invtyDoc.setRgstBrand(GetCellData(r, "注册商标"));//注册商标
                invtyDoc.setCretNum(GetCellData(r, "合格证号"));//合格证号
                invtyDoc.setMakeLicsNum(GetCellData(r, "生产许可证"));//生产许可证号
                invtyDoc.setTraySize(GetBigDecimal(GetCellData(r, "托架尺寸"), 8));//8表示小数位数//托架尺寸
                invtyDoc.setQuantity(GetCellData(r, "数量/拖"));//数量/拖
                invtyDoc.setValtnMode(GetCellData(r, "计价方式"));//计价方式
                invtyDoc.setIptaxRate(GetBigDecimal(GetCellData(r, "进项税率"), 8));//8表示小数位数 //进项税率
                invtyDoc.setOptaxRate(GetBigDecimal(GetCellData(r, "销项税率"), 8));//8表示小数位数 //销项税率
                invtyDoc.setHighestPurPrice(GetBigDecimal(GetCellData(r, "最高进价"), 8));//8表示小数位数 //最高进价
                invtyDoc.setLoSellPrc(GetBigDecimal(GetCellData(r, "最低售价"), 8));//8表示小数位数 //最低售价
                invtyDoc.setLtstCost(GetBigDecimal(GetCellData(r, "最新成本"), 8));//8表示小数位数 //最新成本
                invtyDoc.setRefCost(GetBigDecimal(GetCellData(r, "参考成本"), 8));//8表示小数位数 //参考成本
                invtyDoc.setRefSellPrc(GetBigDecimal(GetCellData(r, "参考售价"), 8));//8表示小数位数 //参考售价
//				invtyDoc.setIsNtSell(new Double(GetCellData(r,"是否销售")).intValue());//是否销售
                invtyDoc.setIsNtPurs(new Double(GetCellData(r, "是否采购")).intValue());//是否采购
                invtyDoc.setIsDomSale(new Double(GetCellData(r, "是否内销")).intValue());//是否内销
                invtyDoc.setPto(new Double(GetCellData(r, "PTO")).intValue());//是否PTO
                invtyDoc.setIsQuaGuaPer(new Double(GetCellData(r, "是否保质期管理")).intValue());//是否保质期管理
                invtyDoc.setAllowBomMain(new Double(GetCellData(r, "允许BOM主件")).intValue());//是否允许BOM主件
                invtyDoc.setAllowBomMinor(new Double(GetCellData(r, "允许BOM子件")).intValue());//是否允许BOM子件
                invtyDoc.setIsNtBarCdMgmt(new Double(GetCellData(r, "是否条形码管理")).intValue());//是否条形码管理
                invtyDoc.setCrspdBarCd(GetCellData(r, "对应条形码"));//对应条形码
                invtyDoc.setSetupPers(GetCellData(r, "创建人"));//创建人
                if (GetCellData(r, "创建日期") == null || GetCellData(r, "创建日期").equals("")) {
                    invtyDoc.setSetupDt(null);
                } else {
                    invtyDoc.setSetupDt(GetCellData(r, "创建日期"));//创建时间
                }
                invtyDoc.setMdfr(GetCellData(r, "修改人"));//修改人
                if (GetCellData(r, "修改日期") == null || GetCellData(r, "修改日期").equals("")) {
                    invtyDoc.setModiDt(null);
                } else {
                    invtyDoc.setModiDt(GetCellData(r, "修改日期"));//修改时间
                }
                invtyDoc.setMemo(GetCellData(r, "备注"));//备注
                invtyDoc.setShdTaxLabour(new Double(GetCellData(r, "应税劳务")).intValue());//应税劳务
                invtyDoc.setIsNtDiscnt(new Double(GetCellData(r, "是否折扣")).intValue());//是否折扣

                temp.put(orderNo, invtyDoc);

            }
            fileIn.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("文件的第" + j + "行导入格式有误，无法导入!" + e.getMessage());
        }
        return temp;
    }

    //导入excle
    private Map<String, InvtyDoc> uploadScoreInfoU8(MultipartFile file) {
        Map<String, InvtyDoc> temp = new HashMap<>();
        int j = 1;
        try {
            InputStream fileIn = file.getInputStream();
            //根据指定的文件输入流导入Excel从而产生Workbook对象
            Workbook wb0 = new HSSFWorkbook(fileIn);
            //获取Excel文档中的第一个表单
            Sheet sht0 = wb0.getSheetAt(0);
            //获得当前sheet的开始行
            int firstRowNum = sht0.getFirstRowNum();
            //获取文件的最后一行
            int lastRowNum = sht0.getLastRowNum();
            //设置中文字段和下标映射
            SetColIndex(sht0.getRow(firstRowNum));
            //对Sheet中的每一行进行迭代
            for (int i = firstRowNum + 1; i <= lastRowNum; i++) {
                j++;
                Row r = sht0.getRow(i);
                //如果当前行的行号（从0开始）未达到2（第三行）则从新循环
                if (r.getRowNum() < 1) {
                    continue;
                }
                String orderNo = GetCellData(r, "存货编码");
                //创建实体类
                InvtyDoc invtyDoc = new InvtyDoc();
                if (temp.containsKey(orderNo)) {
                    invtyDoc = temp.get(orderNo);
                }
                //取出当前行第1个单元格数据，并封装在pursOrdr实体的各个属性上
                invtyDoc.setInvtyEncd(orderNo);//存货编码
                invtyDoc.setInvtyNm(GetCellData(r, "存货名称"));//存货名称
                invtyDoc.setInvtyClsEncd(GetCellData(r, "存货大类编码"));//存货分类编码
                invtyDoc.setSpcModel(GetCellData(r, "规格型号"));//规格型号
                invtyDoc.setInvtyCd(GetCellData(r, "存货代码"));//存货代码
//				invtyDoc.setProvrId(GetCellData(r,"供应商编码"));//供应商编码
                invtyDoc.setMeasrCorpId(GetCellData(r, "主计量单位编码"));//主计量编码
                invtyDoc.setVol(GetCellData(r, "单位体积"));//体积
                invtyDoc.setWeight(GetBigDecimal(GetCellData(r, "重量单位编码"), 8));//8表示小数位数  //重量
                invtyDoc.setLongs(GetBigDecimal(GetCellData(r, "长"), 8));//8表示小数位数  //长
                invtyDoc.setWide(GetBigDecimal(GetCellData(r, "宽"), 8));//8表示小数位数  //宽
                invtyDoc.setHght(GetBigDecimal(GetCellData(r, "高"), 8));//8表示小数位数 //高
                invtyDoc.setBxRule(GetBigDecimal(GetCellData(r, "箱规"), 8));//8表示小数位数  //箱规
                invtyDoc.setBaoZhiQiEarWarn(GetCellData(r, "保质期预警天数"));//保质期预警天数
                invtyDoc.setBaoZhiQiDt(GetCellData(r, "保质期"));//保质期天数
                invtyDoc.setPlaceOfOrigin(GetCellData(r, "产地厂牌"));//产地
                invtyDoc.setRgstBrand(GetCellData(r, "注册商标"));//注册商标
                invtyDoc.setCretNum(GetCellData(r, "合格证号"));//合格证号
                invtyDoc.setMakeLicsNum(GetCellData(r, "许可证号"));//生产许可证号
//				invtyDoc.setTraySize(GetBigDecimal(GetCellData(r,"托架尺寸"),8));//8表示小数位数//托架尺寸
//				invtyDoc.setQuantity(GetCellData(r,"数量/拖"));//数量/拖
                invtyDoc.setValtnMode(GetCellData(r, "计价方式"));//计价方式
                invtyDoc.setIptaxRate(GetBigDecimal(GetCellData(r, "进项税率%"), 8));//8表示小数位数 //进项税率
                invtyDoc.setOptaxRate(GetBigDecimal(GetCellData(r, "销项税率%"), 8));//8表示小数位数 //销项税率
                invtyDoc.setHighestPurPrice(GetBigDecimal(GetCellData(r, "最高进价"), 8));//8表示小数位数 //最高进价
                invtyDoc.setLoSellPrc(GetBigDecimal(GetCellData(r, "最低售价"), 8));//8表示小数位数 //最低售价
                invtyDoc.setLtstCost(GetBigDecimal(GetCellData(r, "最新成本"), 8));//8表示小数位数 //最新成本
                invtyDoc.setRefCost(GetBigDecimal(GetCellData(r, "参考成本"), 8));//8表示小数位数 //参考成本
                invtyDoc.setRefSellPrc(GetBigDecimal(GetCellData(r, "参考售价"), 8));//8表示小数位数 //参考售价
//				invtyDoc.setIsNtSell(new Double(GetCellData(r,"是否销售")).intValue());//是否销售
                if (GetCellData(r, "是否采购") != null && GetCellData(r, "是否采购").equals("否")) {
                    invtyDoc.setIsNtPurs(0); // 是否采购
                } else if (GetCellData(r, "是否采购") != null && GetCellData(r, "是否采购").equals("是")) {
                    invtyDoc.setIsNtPurs(1);  // 是否采购',
                }
                if (GetCellData(r, "是否内销") != null && GetCellData(r, "是否内销").equals("否")) {
                    invtyDoc.setIsDomSale(0); //是否内销',
                } else if (GetCellData(r, "是否内销") != null && GetCellData(r, "是否内销").equals("是")) {
                    invtyDoc.setIsDomSale(1);  //是否内销,
                }
                if (GetCellData(r, "PTO") != null && GetCellData(r, "PTO").equals("否")) {
                    invtyDoc.setPto(0); // PTO,
                } else if (GetCellData(r, "PTO") != null && GetCellData(r, "PTO").equals("是")) {
                    invtyDoc.setPto(1);  // PTO,
                }
                if (GetCellData(r, "是否保质期管理") != null && GetCellData(r, "是否保质期管理").equals("否")) {
                    invtyDoc.setIsQuaGuaPer(0); // '是否保质期管理,
                } else if (GetCellData(r, "是否保质期管理") != null && GetCellData(r, "是否保质期管理").equals("是")) {
                    invtyDoc.setIsQuaGuaPer(1);  // 是否保质期管理,
                }
                if (GetCellData(r, "允许BOM母件") != null && GetCellData(r, "允许BOM母件").equals("否")) {
                    invtyDoc.setAllowBomMain(0); // 允许BOM母件,
                } else if (GetCellData(r, "允许BOM母件") != null && GetCellData(r, "允许BOM母件").equals("是")) {
                    invtyDoc.setAllowBomMain(1);  // 允许BOM母件,
                }
                if (GetCellData(r, "允许BOM子件") != null && GetCellData(r, "允许BOM子件").equals("否")) {
                    invtyDoc.setAllowBomMinor(0); // 允许BOM子件,
                } else if (GetCellData(r, "允许BOM子件") != null && GetCellData(r, "允许BOM子件").equals("是")) {
                    invtyDoc.setAllowBomMinor(1);  // 允许BOM子件
                }
                if (GetCellData(r, "条形码管理") != null && GetCellData(r, "条形码管理").equals("否")) {
                    invtyDoc.setIsNtBarCdMgmt(0); // 条形码管理,
                } else if (GetCellData(r, "条形码管理") != null && GetCellData(r, "是否内销").equals("是")) {
                    invtyDoc.setIsNtBarCdMgmt(1);  // 条形码管理,
                }
                invtyDoc.setCrspdBarCd(GetCellData(r, "对应条形码"));//对应条形码
                invtyDoc.setSetupPers(GetCellData(r, "建档人"));//创建人
                if (GetCellData(r, "建档日期") == null || GetCellData(r, "建档日期").equals("")) {
                    invtyDoc.setSetupDt(null);
                } else {
                    invtyDoc.setSetupDt(GetCellData(r, "建档日期"));//创建时间
                }
                invtyDoc.setMdfr(GetCellData(r, "变更人"));//修改人
                if (GetCellData(r, "变更日期") == null || GetCellData(r, "变更日期").equals("")) {
                    invtyDoc.setModiDt(null);
                } else {
                    invtyDoc.setModiDt(GetCellData(r, "变更日期"));//修改时间
                }
//				invtyDoc.setMemo(GetCellData(r,"备注"));//备注
                if (GetCellData(r, "是否应税劳务") != null && GetCellData(r, "是否应税劳务").equals("否")) {
                    invtyDoc.setShdTaxLabour(0); // 是否应税劳务,
                } else if (GetCellData(r, "是否应税劳务") != null && GetCellData(r, "是否应税劳务").equals("是")) {
                    invtyDoc.setShdTaxLabour(1);  // 是否应税劳务,
                }
                if (GetCellData(r, "是否折扣") != null && GetCellData(r, "是否折扣").equals("否")) {
                    invtyDoc.setIsNtDiscnt(0);//应税劳务,
                } else if (GetCellData(r, "是否折扣") != null && GetCellData(r, "是否折扣").equals("是")) {
                    invtyDoc.setIsNtDiscnt(1); //是否折扣
                }

                temp.put(orderNo, invtyDoc);

            }
            fileIn.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("文件的第" + j + "行导入格式有误，无法导入!" + e.getMessage());
        }
        return temp;
    }

    @Override
    public String selectInvtyEncdLike(Map map) {
        String resp = "";
        List<InvtyDoc> invtyDocList = idd.selectInvtyEncdLike(map);
        try {
            resp = BaseJson.returnRespObjList("purc/InvtyDoc/selectInvtyEncdLike", true, "查询成功！", null, invtyDocList);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return resp;
    }

}
