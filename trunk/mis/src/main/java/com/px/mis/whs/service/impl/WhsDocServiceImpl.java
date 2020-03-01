package com.px.mis.whs.service.impl;

import java.io.IOException;
import java.io.InputStream;
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
import com.px.mis.system.dao.MisUserDao;
import com.px.mis.system.entity.MisUser;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;
import com.px.mis.util.poiTool;
import com.px.mis.whs.dao.WhsDocMapper;
import com.px.mis.whs.entity.AvalQtyCtrlMode;
import com.px.mis.whs.entity.City;
import com.px.mis.whs.entity.RealWhs;
import com.px.mis.whs.entity.RealWhsMap;
import com.px.mis.whs.entity.UserWhs;
import com.px.mis.whs.entity.ValtnMode;
import com.px.mis.whs.entity.WhsAttr;
import com.px.mis.whs.entity.WhsDoc;
import com.px.mis.whs.entity.WhsGds;
import com.px.mis.whs.service.WhsDocService;

@Service
@Transactional
public class WhsDocServiceImpl extends poiTool implements WhsDocService {

    @Autowired
    WhsDocMapper whsDocMapper;
    @Autowired
    MisUserDao misUserDao;

    // 添加仓库档案
    @Override
    public ObjectNode insertWhsDoc(WhsDoc record) {
        ObjectNode on = null;
        try {
            on = JacksonUtil.getObjectNode("");
        } catch (Exception e) {

        }

        int count = whsDocMapper.selectWDoc(record.getWhsEncd());
        if (count != 0) {
            on.put("isSuccess", false);
            on.put("message", "仓库编码不能重复！");
        } else {
            if (record.getStpUseDt() == "") {
                record.setStpUseDt(null);
            }
            int whsDoc = whsDocMapper.insertWhsDoc(record);
            if (whsDoc == 1) {
                on.put("isSuccess", true);
                on.put("message", "新增成功！");
            } else {
                on.put("isSuccess", false);
                on.put("message", "新增失败！");
            }
        }

        return on;
    }

    // 修改仓库档案
    @Override
    public ObjectNode updateWhsDoc(WhsDoc record) {
        ObjectNode on = null;
        try {
            on = JacksonUtil.getObjectNode("");
        } catch (Exception e) {

        }

        if (record.getStpUseDt() == "") {
            record.setStpUseDt(null);
        }
        int whsDoc = whsDocMapper.updateWhsDoc(record);
        if (whsDoc == 1) {
            on.put("isSuccess", true);
            on.put("message", "修改成功！");
        } else {
            on.put("isSuccess", false);
            on.put("message", "修改失败！");
        }
        return on;
    }

    // 删除仓库档案
    @Override
    public ObjectNode deleteWhsDoc(String whsEncd) {
        ObjectNode on = null;
        try {
            on = JacksonUtil.getObjectNode("");
        } catch (Exception e) {

        }

        int whsDoc = whsDocMapper.deleteWhsDoc(whsEncd);
        if (whsDoc == 1) {
            on.put("isSuccess", true);
            on.put("message", "删除成功！");
        } else {
            on.put("isSuccess", false);
            on.put("message", "删除失败！");
        }
        return on;
    }

    // 批量删除
    @Override
    public String deleteWDocList(String whsEncd) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";

        List<String> list = getList(whsEncd);

        if (whsDocMapper.selectAllWDocList(list).size() > 0) {
            whsDocMapper.deleteWDocList(list);
            isSuccess = true;
            message = "删除成功！";
        } else {
            isSuccess = false;
            message = "编号" + whsEncd + "不存在！";
        }

        try {
            resp = BaseJson.returnRespObj("whs/whs_doc/deleteWDocList", isSuccess, message, null);
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
        if (id == null || id.equals("")) {
            return null;
        }
        List<String> list = new ArrayList<String>();
        String[] str = id.split(",");
        for (int i = 0; i < str.length; i++) {
            list.add(str[i]);
        }
        return list;
    }

    // 简单查 仓库档案
    @Override
    public WhsDoc selectWhsDoc(String whsEncd) throws IOException {

        WhsDoc wDoc = whsDocMapper.selectWhsDoc(whsEncd);
        return wDoc;
    }

    @Override
    public String selectWhsDocList(String whsEncd, String accNum) throws IOException {
        String resp = "";

        MisUser misUser = misUserDao.select(accNum);
        boolean is = true;

//		if (misUser.getDepId().equals("009")) {
//			List<String> list = new ArrayList<String>();
//			Map map = new HashMap();
//			map.put("accNum", accNum);
//
//			List<UserWhs> userWhsList = whsDocMapper.selectUserWhsList(map);
//
//			for (UserWhs userWhs : userWhsList) {
//				list.add(userWhs.getRealWhs());
//			}
//			if (list.size() == 0) {
//				try {
//					resp = BaseJson.returnRespObj("whs/whs_doc/selectWhsDocList", true, "查询失败,没有分配仓库权限", null);
//				} catch (IOException e) {
//
//					
//				}
//				return resp;
//			}
//			List<String> aList = whsDocMapper.selectRealWhsList(list);
//			if (aList.size() == 0) {
//				try {
//					resp = BaseJson.returnRespObj("whs/whs_doc/selectWhsDocList", true, "查询失败,未分配逻辑仓库权限", null);
//				} catch (IOException e) {
//
//					
//				}
//				return resp;
//			}
//			for (String string : aList) {
//				if (whsEncd.equals(string)) {
//					is = false;
//				}
//
//			}
//			if (is) {
//				resp = BaseJson.returnRespObj("whs/whs_doc/selectWhsDocList", false, "查询失败！", null);
//				return resp;
//			}
//		}

        List<WhsDoc> wDocList = whsDocMapper.selectWhsDocList(whsEncd);

        try {
            if (wDocList.size() > 0) {
                resp = BaseJson.returnRespObjList("whs/whs_doc/selectWhsDocList", true, "查询成功！", null, wDocList);
            } else {
                resp = BaseJson.returnRespObjList("whs/whs_doc/selectWhsDocList", false, "查询失败！", null, wDocList);
            }
        } catch (IOException e) {

        }
        return resp;
    }

    public void mapWhs(String jsonBody, Map map) {
        String accNum = null;
        try {
            accNum = BaseJson.getReqHead(jsonBody).get("accNum").asText();
        } catch (Exception e) {

        }
        MisUser misUser = misUserDao.select(accNum);

        if (misUser.getDepId().equals("009")) {
            List<String> list = new ArrayList<String>();
            Map maps = new HashMap();
            maps.put("accNum", accNum);
            List<UserWhs> userWhsList = whsDocMapper.selectUserWhsList(maps);

            for (UserWhs userWhs : userWhsList) {
                list.add(userWhs.getRealWhs());
            }
            if (list.size() == 0) {
                throw new RuntimeException("查询失败,没有分配仓库权限");
            }
            List<String> aList = whsDocMapper.selectRealWhsList(list);
            if (aList.size() == 0) {
                throw new RuntimeException("查询失败,未分配逻辑仓库权限");
            }
            map.put("whs", aList);
            return;
        }
    }

    // 分页查
    @Override
    public String queryList(Map map) {
        String resp = "";
        /*
         * String accNum = map.get("accNum").toString();
         *
         * MisUser misUser = misUserDao.select(accNum);
         *
         * if (misUser.getDepId().equals("009")) { List<String> list = new
         * ArrayList<String>();
         *
         * List<UserWhs> userWhsList = whsDocMapper.selectUserWhsList(map);
         *
         * for (UserWhs userWhs : userWhsList) { list.add(userWhs.getRealWhs()); } if
         * (list.size() == 0) { try { resp =
         * BaseJson.returnRespObj("whs/whs_doc/queryList", true, "查询失败,没有分配仓库权限", null);
         * } catch (IOException e) {
         *  } return resp; } List<String> aList =
         * whsDocMapper.selectRealWhsList(list); if (aList.size() == 0) { try { resp =
         * BaseJson.returnRespObj("whs/whs_doc/queryList", true, "查询失败,未分配逻辑仓库权限",
         * null); } catch (IOException e) {
         *  } return resp; } map.put("whs", aList); }
         */
//		whsDocMapper
        List<String> whsId = getList((String) map.get("whsId"));
        map.put("whsId", whsId);
        List<WhsDoc> aList = whsDocMapper.selectList(map);
        int count = whsDocMapper.selectCount(map);

        int pageNo = Integer.parseInt(map.get("pageNo").toString());
        int pageSize = Integer.parseInt(map.get("pageSize").toString());
        int listNum = aList.size();
        int pages = count / pageSize;
        if (count % pageSize > 0) {
            pages += 1;
        }
//		for(WhsDoc a : aList) {
//			String s=a.getWhsEncd();
//			
//		}
        try {
            resp = BaseJson.returnRespList("whs/whs_doc/queryList", true, "查询成功！", count, pageNo, pageSize, listNum,
                    pages, aList);
        } catch (IOException e) {

        }
        return resp;
    }

    // 打印
    @Override
    public String queryListDaYin(Map map) {
        String resp = "";

        List<String> whsId = getList((String) map.get("whsId"));
        map.put("whsId", whsId);
        List<WhsDoc> aList = whsDocMapper.selectListDaYin(map);
        aList.add(new WhsDoc());
        try {
            resp = BaseJson.returnRespObjListAnno("whs/whs_doc/queryListDaYin", true, "查询成功！", null, aList);
        } catch (IOException e) {

        }
        return resp;
    }

    // 省 市 县
    @Override
    public String selectCity(City city) {
        String resp = "";
        String message = "";
        Boolean isSuccess = true;

        List<City> cList = whsDocMapper.selectCity(city);
        for (City cty : cList) {
            // 省
            if (Integer.parseInt(cty.getCodeLevel()) == 0) {
                // 市
                if (Integer.parseInt(cty.getCodeLevel()) == 1) {
                    // 县
                    if (Integer.parseInt(cty.getCodeLevel()) == 2) {

                    }
                }
            }
        }
        message = "查询成功！";

        try {
            resp = BaseJson.returnRespObjList("whs/whs_doc/selectCity", isSuccess, message, null, cList);
        } catch (IOException e) {

        }
        return resp;
    }

    // 省
    @Override
    public String selectProvinces() {
        String resp = "";
        String message = "";
        Boolean isSuccess = true;

        List<City> cList = whsDocMapper.selectProvinces();

        message = "查询成功！";

        try {
            resp = BaseJson.returnRespObjList("whs/whs_doc/selectProvinces", isSuccess, message, null, cList);
        } catch (IOException e) {

        }
        return resp;
    }

    // 市
    @Override
    public String selectCities(String superiorCode) {
        String resp = "";
        String message = "";
        Boolean isSuccess = true;

        List<City> cList = whsDocMapper.selectCities(superiorCode);

        message = "查询成功！";

        try {
            resp = BaseJson.returnRespObjList("whs/whs_doc/selectCities", isSuccess, message, null, cList);
        } catch (IOException e) {

        }
        return resp;
    }

    // 县
    @Override
    public String selectCounties(String superiorCode) {
        String resp = "";
        String message = "";
        Boolean isSuccess = true;

        List<City> cList = whsDocMapper.selectCounties(superiorCode);

        message = "查询成功！";

        try {
            resp = BaseJson.returnRespObjList("whs/whs_doc/selectCounties", isSuccess, message, null, cList);
        } catch (IOException e) {

        }
        return resp;
    }

    // 计价方式
    @Override
    public String selectValtnMode() {
        String resp = "";
        String message = "";
        Boolean isSuccess = true;

        List<ValtnMode> vList = whsDocMapper.selectValtnMode();

        message = "查询成功！";

        try {
            resp = BaseJson.returnRespObjList("whs/whs_doc/selectValtnMode", isSuccess, message, null, vList);
        } catch (IOException e) {

        }
        return resp;
    }

    // 仓库属性
    @Override
    public String selectWhsAttr() {
        String resp = "";
        String message = "";
        Boolean isSuccess = true;

        List<WhsAttr> wAttrsList = whsDocMapper.selectWhsAttr();

        message = "查询成功！";

        try {
            resp = BaseJson.returnRespObjList("whs/whs_doc/selectWhsAttr", isSuccess, message, null, wAttrsList);
        } catch (IOException e) {

        }
        return resp;
    }

    // 可用量控制方式
    @Override
    public String selectAMode() {
        String resp = "";
        String message = "";
        Boolean isSuccess = true;

        List<AvalQtyCtrlMode> aList = whsDocMapper.selectAMode();

        message = "查询成功！";

        try {
            resp = BaseJson.returnRespObjList("whs/whs_doc/selectAMode", isSuccess, message, null, aList);
        } catch (IOException e) {

        }
        return resp;
    }

    @Override
    public String uploadFileAddDb(MultipartFile file) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        Map<String, WhsDoc> pusOrderMap = uploadScoreInfo(file);

        for (Map.Entry<String, WhsDoc> entry : pusOrderMap.entrySet()) {

            WhsDoc wDoc = whsDocMapper.selectWhsDoc(entry.getValue().getWhsEncd());

            if (wDoc != null) {
                throw new RuntimeException("插入重复单号 " + entry.getValue().getWhsEncd() + " 请检查");

            }
            try {
                // System.out.println(entry.getValue().getWhsNm());

                whsDocMapper.exInsertWhsDoc(entry.getValue());

            } catch (Exception e) {


                throw new RuntimeException("插入sql问题");
            }

        }

        isSuccess = true;
        message = "仓库档案新增成功！";
        try {
            resp = BaseJson.returnRespObj("whs/whs_doc/uploadFileAddDb", isSuccess, message, null);
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
     * result = "0"; if(ColNameCN.equals("是否退货")) result = "0";
     *
     * } return result==null?result:result.trim(); }
     */
    // 导入excle
    private Map<String, WhsDoc> uploadScoreInfo(MultipartFile file) {
        Map<String, WhsDoc> temp = new HashMap<>();
        int j = 0;
        try {
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
                Row r = sht0.getRow(i);
                // 如果当前行的行号（从0开始）未达到2（第三行）则从新循环
                if (r.getRowNum() < 1) {
                    continue;
                }
                String orderNo = GetCellData(r, "仓库编码");
                // 创建实体类
                WhsDoc whsDoc = new WhsDoc();
                if (temp.containsKey(orderNo)) {
                    whsDoc = temp.get(orderNo);
                }
                // r.getCell(0).setCellType(Cell.CELL_TYPE_NUMERIC);
//					// System.out.println(r.getCell(0));
                // 取出当前行第1个单元格数据，并封装在pursOrdr实体的各个属性上

                whsDoc.setWhsEncd(orderNo); // 1 仓库编码,
                whsDoc.setWhsNm(GetCellData(r, "仓库名称")); // 1 仓库名称,
                whsDoc.setDeptEncd(GetCellData(r, "部门编码")); // 部门编号,
                whsDoc.setWhsAddr(GetCellData(r, "仓库地址")); // 1 仓库地址,
                whsDoc.setTel(GetCellData(r, "电话")); // 1 电话,
                whsDoc.setPrinc(GetCellData(r, "负责人")); // 1 负责人,
                whsDoc.setValtnMode(GetCellData(r, "计价方式")); // 1 计价方式,
                whsDoc.setCrspdBarCd(GetCellData(r, "对应条形码")); // 1 对应条形码,
                whsDoc.setIsNtPrgrGdsBitMgmt(new Double(GetCellData(r, "是否货位管理")==null || GetCellData(r, "是否货位管理").equals("")? "0":GetCellData(r, "是否货位管理") ).intValue()); // 1 是否进行货位管理,
                whsDoc.setWhsAttr(GetCellData(r, "仓库属性")); // 1 仓库属性,
                whsDoc.setSellAvalQtyCtrlMode(GetCellData(r, "销售可用量控制方式")); // 1 销售可用量控制方式,
                whsDoc.setInvtyAvalQtyCtrlMode(GetCellData(r, "库存可用量控制方式")); // 1 库存可用量控制方式,
                whsDoc.setMemo(GetCellData(r, "备注")); // 1 备注,
                whsDoc.setIsNtShop(new Double(GetCellData(r, "是否门店")==null || GetCellData(r, "是否门店").equals("")? "0":GetCellData(r, "是否门店") ).intValue()); // 1 是否门店,
                if (GetCellData(r, "停用日期") == null || GetCellData(r, "停用日期").equals("")) {
                    whsDoc.setSetupTm(null);
                } else {
                    whsDoc.setStpUseDt(GetCellData(r, "停用日期")); // 1 停用日期,
                }
                whsDoc.setProv(GetCellData(r, "省/直辖市")); // 1 省/直辖市,
                whsDoc.setCty(GetCellData(r, "市")); // 1 市,
                whsDoc.setCnty(GetCellData(r, "区县")); // 1 县,
                whsDoc.setDumyWhs(new Double(GetCellData(r, "是否虚拟仓")).intValue()); // 虚拟仓,,
                whsDoc.setSetupPers(GetCellData(r, "创建人")); // 创建人,
                whsDoc.setRealWhs(GetCellData(r, "实体仓库")); // 实体仓库,
                if (GetCellData(r, "创建时间") == null || GetCellData(r, "创建时间").equals("")) {
                    whsDoc.setSetupTm(null);
                } else {
                    whsDoc.setSetupTm(GetCellData(r, "创建时间")); // 创建时间,
                }
                whsDoc.setMdfr(GetCellData(r, "修改人")); // 修改人,
                if (GetCellData(r, "修改时间") == null || GetCellData(r, "修改时间").equals("")) {
                    whsDoc.setModiTm(null);
                } else {
                    whsDoc.setModiTm(GetCellData(r, "修改时间")); // 修改时间,
                }
                temp.put(orderNo, whsDoc);

            }
            fileIn.close();
        } catch (Exception e) {
            throw new RuntimeException("文件的第" + j + "行导入格式有误" +e.getMessage());
        }
        return temp;
    }

    @Override
    public ObjectNode insertUserWhs(List<UserWhs> userWhs) {
        ObjectNode on = null;
        try {
            on = JacksonUtil.getObjectNode("");
        } catch (Exception e) {

        }

//		List<UserWhs> count = whsDocMapper.selectUserWhs(userWhs);
//		if (count.size() != 0) {
//			on.put("isSuccess", false);
//			on.put("message", "用户仓库已存在");
//		} else {}

        int whs = whsDocMapper.insertUserWhs(userWhs);
        if (whs != 0) {
            on.put("isSuccess", true);
            on.put("message", "新增成功！");
        } else {
            on.put("isSuccess", false);
            on.put("message", "新增失败！");
        }

        return on;
    }

    @Override
    public ObjectNode updateUserWhs(UserWhs wDoc) {
        ObjectNode on = null;
        try {
            on = JacksonUtil.getObjectNode("");
        } catch (Exception e) {

        }

        int whsDoc = whsDocMapper.updateUserWhs(wDoc);
        if (whsDoc == 1) {
            on.put("isSuccess", true);
            on.put("message", "修改成功！");
        } else {
            on.put("isSuccess", false);
            on.put("message", "修改失败！");
        }
        return on;
    }

    @Override
    public String deleteUserWhsList(String id) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";

        List<String> list = getList(id);

        int i = whsDocMapper.deleteUserWhsList(list);
        if (i > 0) {

            isSuccess = true;
            message = "删除成功！";
        } else {
            isSuccess = false;
            message = "编号" + id + "不存在！";
        }

        try {
            resp = BaseJson.returnRespObj("whs/whs_doc/deleteWDocList", isSuccess, message, null);
        } catch (IOException e) {

        }
        return resp;
    }

    @Override
    public String selectUserWhsList(Map map) {
        String resp = "";
        List<UserWhs> aList = whsDocMapper.selectUserWhsList(map);
//		int count = whsDocMapper.selectUserWhsCount(map);
        if (aList.size() > 0) {
            try {
                resp = BaseJson.returnRespList("whs/whs_doc/queryList", true, "查询成功！", aList);
            } catch (IOException e) {

            }
        } else {

            try {
                resp = BaseJson.returnRespList("whs/whs_doc/queryList", false, "查询失败", aList);
            } catch (IOException e) {

            }
        }

        return resp;
    }

    @Override
    public String deleteWhsGds(List<String> id) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";

//		List<String> list = getList(id);

        int i = whsDocMapper.deleteWhsGds(id);
        if (i > 0) {

            isSuccess = true;
            message = "删除成功！";
        } else {
            isSuccess = false;
            message = "编号" + id + "不存在！";
        }

        try {
            resp = BaseJson.returnRespObj("whs/whs_doc/deleteWhsGds", isSuccess, message, null);
        } catch (IOException e) {

        }
        return resp;
    }

    @Override
    public ObjectNode insertWhsGds(List<WhsGds> userWhs) {
        ObjectNode on = null;
        try {
            on = JacksonUtil.getObjectNode("");
        } catch (Exception e) {

        }

        for (WhsGds gds : userWhs) {
            WhsGds count = whsDocMapper.selectWhsGds(gds);
            if (count != null) {
                on.put("isSuccess", false);
                on.put("message", "仓库货位已存在");
                return on;
            }
        }

        int whs = whsDocMapper.insertWhsGds(userWhs);
        if (whs != 0) {
            on.put("isSuccess", true);
            on.put("message", "新增成功！");
        } else {
            on.put("isSuccess", false);
            on.put("message", "新增失败！");
        }

        return on;
    }

    @Override
    public String selectWhsGdsList(Map map) {
        String resp = "";
        int pageNo = (int) map.get("pageNo");
        int pageSize = (int) map.get("pageSize");
        map.put("index", (pageNo - 1) * pageSize);
        map.put("num", pageSize);
        List<WhsGds> aList = whsDocMapper.selectWhsGdsList(map);
        int count = whsDocMapper.selectWhsGdsListCount(map);
        int listNum = aList.size();
        int pages = count / pageSize;
        if (count % pageSize > 0) {
            pages += 1;
        }

        if (aList.size() > 0) {
            try {
                resp = BaseJson.returnRespList("whs/whs_doc/selectWhsGds", true, "查询成功！", count, pageNo, pageSize,
                        listNum, pages, aList);
            } catch (IOException e) {

            }
        } else {

            try {
                resp = BaseJson.returnRespList("whs/whs_doc/selectWhsGds", false, "查询失败", aList);
            } catch (IOException e) {

            }
        }

        return resp;
    }

    @Override
    public String selectWhsGdsRealList(Map map) {
        String resp = "";
        List<WhsGds> aList = whsDocMapper.selectWhsGdsRealList(map);
//		int count = whsDocMapper.selectUserWhsCount(map);
        if (aList.size() > 0) {
            try {
                resp = BaseJson.returnRespList("whs/whs_doc/selectWhsGdsRealList", true, "查询成功！", aList);
            } catch (IOException e) {

            }
        } else {

            try {
                resp = BaseJson.returnRespList("whs/whs_doc/selectWhsGdsRealList", false, "查询失败", aList);
            } catch (IOException e) {

            }
        }

        return resp;
    }

    // 总仓
    @Override
    public ObjectNode insertRealWhs(RealWhs wDoc) {
        ObjectNode on = null;
        try {
            on = JacksonUtil.getObjectNode("");
        } catch (Exception e) {

        }

        RealWhs count = whsDocMapper.selectRealWhs(wDoc.getRealWhs());
        if (count != null) {
            on.put("isSuccess", false);
            on.put("message", "仓库编码不能重复！");
        } else {
            if (wDoc.getStpUseDt() == "") {
                wDoc.setStpUseDt(null);
            }
            int whsDoc = whsDocMapper.insertRealWhs(wDoc);
            if (whsDoc == 1) {
                on.put("isSuccess", true);
                on.put("message", "新增成功！");
            } else {
                on.put("isSuccess", false);
                on.put("message", "新增失败！");
            }
        }

        return on;
    }

    @Override
    public ObjectNode updateRealWhs(RealWhs record) {
        ObjectNode on = null;
        try {
            on = JacksonUtil.getObjectNode("");
        } catch (Exception e) {

        }

        if (record.getStpUseDt() == "") {
            record.setStpUseDt(null);
        }
        int whsDoc = whsDocMapper.updateRealWhs(record);
        if (whsDoc == 1) {
            on.put("isSuccess", true);
            on.put("message", "修改成功！");
        } else {
            on.put("isSuccess", false);
            on.put("message", "修改失败！");
        }
        return on;
    }

    @Override
    public String deleteRealWhsList(String whsEncd) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";

        List<String> list = getList(whsEncd);

        if (whsDocMapper.deleteRealWhsList(list) > 0) {

            isSuccess = true;
            message = "删除成功！";
        } else {
            isSuccess = false;
            message = "编号" + whsEncd + "不存在！";
        }

        try {
            resp = BaseJson.returnRespObj("whs/whs_doc/deleteWDocList", isSuccess, message, null);
        } catch (IOException e) {

        }
        return resp;
    }

    @Override
    public RealWhs selectRealWhs(String realWhs) {

        RealWhs wDoc = whsDocMapper.selectRealWhs(realWhs);
        return wDoc;
    }

    @Override
    public String queryRealWhsListDaYin(Map map) {

        String resp = "";
        List<RealWhs> aList = whsDocMapper.queryRealWhsListDaYin(map);
        aList.add(new RealWhs());
        try {
            resp = BaseJson.returnRespObjList("whs/whs_doc/queryListDaYin", true, "查询成功！", null, aList);
        } catch (IOException e) {

        }
        return resp;

    }

    @Override
    public String queryRealWhsList(Map map) {
        String resp = "";
        List<RealWhs> aList = whsDocMapper.queryRealWhsList(map);
        int count = whsDocMapper.queryRealWhsCount(map);

        int pageNo = Integer.parseInt(map.get("pageNo").toString());
        int pageSize = Integer.parseInt(map.get("pageSize").toString());
        int listNum = aList.size();
        int pages = count / pageSize;
        if (count % pageSize > 0) {
            pages += 1;
        }
        try {
            resp = BaseJson.returnRespList("whs/whs_doc/queryList", true, "查询成功！", count, pageNo, pageSize, listNum,
                    pages, aList);
        } catch (IOException e) {

        }
        return resp;
    }
    // 仓库与总仓RealWhsMap

    @Override
    public ObjectNode insertRealWhsMap(RealWhsMap realWhsMap) {
        ObjectNode on = null;
        try {
            on = JacksonUtil.getObjectNode("");
        } catch (Exception e) {

        }

        RealWhsMap count = whsDocMapper.selectRealWhsMap(realWhsMap);
        if (count != null) {
            on.put("isSuccess", false);
            on.put("message", "仓库已存在总仓");
            return on;
        }

        int whs = whsDocMapper.insertRealWhsMap(realWhsMap);
        if (whs != 0) {
            on.put("isSuccess", true);
            on.put("message", "新增成功！");
        } else {
            on.put("isSuccess", false);
            on.put("message", "新增失败！");
        }

        return on;
    }

    @Override
    public String selectRealWhsMap(Map map) {
        String resp = "";
        List<RealWhsMap> aList = whsDocMapper.selectRealWhsMapList(map);

        if (aList.size() > 0) {
            try {
                resp = BaseJson.returnRespList("whs/whs_doc/selectRealWhsMap", true, "查询成功！", aList);
            } catch (IOException e) {

            }
        } else {

            try {
                resp = BaseJson.returnRespList("whs/whs_doc/selectRealWhsMap", false, "查询失败", aList);
            } catch (IOException e) {

            }
        }

        return resp;
    }

    @Override
    public String deleteRealWhsMap(List<String> list) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";

//		List<String> list = getList(id);

        int i = whsDocMapper.deleteRealWhsMap(list);
        if (i > 0) {

            isSuccess = true;
            message = "删除成功！";
        } else {
            isSuccess = false;
            message = "编号" + list + "不存在！";
        }

        try {
            resp = BaseJson.returnRespObj("whs/whs_doc/deleteRealWhsMap", isSuccess, message, null);
        } catch (IOException e) {

        }
        return resp;

    }

}
