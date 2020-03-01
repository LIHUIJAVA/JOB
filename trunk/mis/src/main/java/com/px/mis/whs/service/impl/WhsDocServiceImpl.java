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

    // ��Ӳֿ⵵��
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
            on.put("message", "�ֿ���벻���ظ���");
        } else {
            if (record.getStpUseDt() == "") {
                record.setStpUseDt(null);
            }
            int whsDoc = whsDocMapper.insertWhsDoc(record);
            if (whsDoc == 1) {
                on.put("isSuccess", true);
                on.put("message", "�����ɹ���");
            } else {
                on.put("isSuccess", false);
                on.put("message", "����ʧ�ܣ�");
            }
        }

        return on;
    }

    // �޸Ĳֿ⵵��
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
            on.put("message", "�޸ĳɹ���");
        } else {
            on.put("isSuccess", false);
            on.put("message", "�޸�ʧ�ܣ�");
        }
        return on;
    }

    // ɾ���ֿ⵵��
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
            on.put("message", "ɾ���ɹ���");
        } else {
            on.put("isSuccess", false);
            on.put("message", "ɾ��ʧ�ܣ�");
        }
        return on;
    }

    // ����ɾ��
    @Override
    public String deleteWDocList(String whsEncd) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";

        List<String> list = getList(whsEncd);

        if (whsDocMapper.selectAllWDocList(list).size() > 0) {
            whsDocMapper.deleteWDocList(list);
            isSuccess = true;
            message = "ɾ���ɹ���";
        } else {
            isSuccess = false;
            message = "���" + whsEncd + "�����ڣ�";
        }

        try {
            resp = BaseJson.returnRespObj("whs/whs_doc/deleteWDocList", isSuccess, message, null);
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

    // �򵥲� �ֿ⵵��
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
//					resp = BaseJson.returnRespObj("whs/whs_doc/selectWhsDocList", true, "��ѯʧ��,û�з���ֿ�Ȩ��", null);
//				} catch (IOException e) {
//
//					
//				}
//				return resp;
//			}
//			List<String> aList = whsDocMapper.selectRealWhsList(list);
//			if (aList.size() == 0) {
//				try {
//					resp = BaseJson.returnRespObj("whs/whs_doc/selectWhsDocList", true, "��ѯʧ��,δ�����߼��ֿ�Ȩ��", null);
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
//				resp = BaseJson.returnRespObj("whs/whs_doc/selectWhsDocList", false, "��ѯʧ�ܣ�", null);
//				return resp;
//			}
//		}

        List<WhsDoc> wDocList = whsDocMapper.selectWhsDocList(whsEncd);

        try {
            if (wDocList.size() > 0) {
                resp = BaseJson.returnRespObjList("whs/whs_doc/selectWhsDocList", true, "��ѯ�ɹ���", null, wDocList);
            } else {
                resp = BaseJson.returnRespObjList("whs/whs_doc/selectWhsDocList", false, "��ѯʧ�ܣ�", null, wDocList);
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
                throw new RuntimeException("��ѯʧ��,û�з���ֿ�Ȩ��");
            }
            List<String> aList = whsDocMapper.selectRealWhsList(list);
            if (aList.size() == 0) {
                throw new RuntimeException("��ѯʧ��,δ�����߼��ֿ�Ȩ��");
            }
            map.put("whs", aList);
            return;
        }
    }

    // ��ҳ��
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
         * BaseJson.returnRespObj("whs/whs_doc/queryList", true, "��ѯʧ��,û�з���ֿ�Ȩ��", null);
         * } catch (IOException e) {
         *  } return resp; } List<String> aList =
         * whsDocMapper.selectRealWhsList(list); if (aList.size() == 0) { try { resp =
         * BaseJson.returnRespObj("whs/whs_doc/queryList", true, "��ѯʧ��,δ�����߼��ֿ�Ȩ��",
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
            resp = BaseJson.returnRespList("whs/whs_doc/queryList", true, "��ѯ�ɹ���", count, pageNo, pageSize, listNum,
                    pages, aList);
        } catch (IOException e) {

        }
        return resp;
    }

    // ��ӡ
    @Override
    public String queryListDaYin(Map map) {
        String resp = "";

        List<String> whsId = getList((String) map.get("whsId"));
        map.put("whsId", whsId);
        List<WhsDoc> aList = whsDocMapper.selectListDaYin(map);
        aList.add(new WhsDoc());
        try {
            resp = BaseJson.returnRespObjListAnno("whs/whs_doc/queryListDaYin", true, "��ѯ�ɹ���", null, aList);
        } catch (IOException e) {

        }
        return resp;
    }

    // ʡ �� ��
    @Override
    public String selectCity(City city) {
        String resp = "";
        String message = "";
        Boolean isSuccess = true;

        List<City> cList = whsDocMapper.selectCity(city);
        for (City cty : cList) {
            // ʡ
            if (Integer.parseInt(cty.getCodeLevel()) == 0) {
                // ��
                if (Integer.parseInt(cty.getCodeLevel()) == 1) {
                    // ��
                    if (Integer.parseInt(cty.getCodeLevel()) == 2) {

                    }
                }
            }
        }
        message = "��ѯ�ɹ���";

        try {
            resp = BaseJson.returnRespObjList("whs/whs_doc/selectCity", isSuccess, message, null, cList);
        } catch (IOException e) {

        }
        return resp;
    }

    // ʡ
    @Override
    public String selectProvinces() {
        String resp = "";
        String message = "";
        Boolean isSuccess = true;

        List<City> cList = whsDocMapper.selectProvinces();

        message = "��ѯ�ɹ���";

        try {
            resp = BaseJson.returnRespObjList("whs/whs_doc/selectProvinces", isSuccess, message, null, cList);
        } catch (IOException e) {

        }
        return resp;
    }

    // ��
    @Override
    public String selectCities(String superiorCode) {
        String resp = "";
        String message = "";
        Boolean isSuccess = true;

        List<City> cList = whsDocMapper.selectCities(superiorCode);

        message = "��ѯ�ɹ���";

        try {
            resp = BaseJson.returnRespObjList("whs/whs_doc/selectCities", isSuccess, message, null, cList);
        } catch (IOException e) {

        }
        return resp;
    }

    // ��
    @Override
    public String selectCounties(String superiorCode) {
        String resp = "";
        String message = "";
        Boolean isSuccess = true;

        List<City> cList = whsDocMapper.selectCounties(superiorCode);

        message = "��ѯ�ɹ���";

        try {
            resp = BaseJson.returnRespObjList("whs/whs_doc/selectCounties", isSuccess, message, null, cList);
        } catch (IOException e) {

        }
        return resp;
    }

    // �Ƽ۷�ʽ
    @Override
    public String selectValtnMode() {
        String resp = "";
        String message = "";
        Boolean isSuccess = true;

        List<ValtnMode> vList = whsDocMapper.selectValtnMode();

        message = "��ѯ�ɹ���";

        try {
            resp = BaseJson.returnRespObjList("whs/whs_doc/selectValtnMode", isSuccess, message, null, vList);
        } catch (IOException e) {

        }
        return resp;
    }

    // �ֿ�����
    @Override
    public String selectWhsAttr() {
        String resp = "";
        String message = "";
        Boolean isSuccess = true;

        List<WhsAttr> wAttrsList = whsDocMapper.selectWhsAttr();

        message = "��ѯ�ɹ���";

        try {
            resp = BaseJson.returnRespObjList("whs/whs_doc/selectWhsAttr", isSuccess, message, null, wAttrsList);
        } catch (IOException e) {

        }
        return resp;
    }

    // ���������Ʒ�ʽ
    @Override
    public String selectAMode() {
        String resp = "";
        String message = "";
        Boolean isSuccess = true;

        List<AvalQtyCtrlMode> aList = whsDocMapper.selectAMode();

        message = "��ѯ�ɹ���";

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
                throw new RuntimeException("�����ظ����� " + entry.getValue().getWhsEncd() + " ����");

            }
            try {
                // System.out.println(entry.getValue().getWhsNm());

                whsDocMapper.exInsertWhsDoc(entry.getValue());

            } catch (Exception e) {


                throw new RuntimeException("����sql����");
            }

        }

        isSuccess = true;
        message = "�ֿ⵵�������ɹ���";
        try {
            resp = BaseJson.returnRespObj("whs/whs_doc/uploadFileAddDb", isSuccess, message, null);
        } catch (IOException e) {


        }

        return resp;

    }

    /*
     * //�ж��Ƿ��ֶ� public String GetCellData(Row row,String ColNameCN) throws Exception
     * { Map<String,Integer> ColMap= new HashMap<String, Integer>(); Object obj =
     * ColMap.get(ColNameCN.trim()); if(obj==null) { throw new Exception("���������� [" +
     * ColNameCN +"],����"); } String result = getValue(row.getCell((int)obj));
     * if((result == null)||result.trim().equals("")) { if(ColNameCN.equals("�Ƿ����"))
     * result = "0"; if(ColNameCN.equals("�Ƿ��˻�")) result = "0";
     *
     * } return result==null?result:result.trim(); }
     */
    // ����excle
    private Map<String, WhsDoc> uploadScoreInfo(MultipartFile file) {
        Map<String, WhsDoc> temp = new HashMap<>();
        int j = 0;
        try {
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
                Row r = sht0.getRow(i);
                // �����ǰ�е��кţ���0��ʼ��δ�ﵽ2�������У������ѭ��
                if (r.getRowNum() < 1) {
                    continue;
                }
                String orderNo = GetCellData(r, "�ֿ����");
                // ����ʵ����
                WhsDoc whsDoc = new WhsDoc();
                if (temp.containsKey(orderNo)) {
                    whsDoc = temp.get(orderNo);
                }
                // r.getCell(0).setCellType(Cell.CELL_TYPE_NUMERIC);
//					// System.out.println(r.getCell(0));
                // ȡ����ǰ�е�1����Ԫ�����ݣ�����װ��pursOrdrʵ��ĸ���������

                whsDoc.setWhsEncd(orderNo); // 1 �ֿ����,
                whsDoc.setWhsNm(GetCellData(r, "�ֿ�����")); // 1 �ֿ�����,
                whsDoc.setDeptEncd(GetCellData(r, "���ű���")); // ���ű��,
                whsDoc.setWhsAddr(GetCellData(r, "�ֿ��ַ")); // 1 �ֿ��ַ,
                whsDoc.setTel(GetCellData(r, "�绰")); // 1 �绰,
                whsDoc.setPrinc(GetCellData(r, "������")); // 1 ������,
                whsDoc.setValtnMode(GetCellData(r, "�Ƽ۷�ʽ")); // 1 �Ƽ۷�ʽ,
                whsDoc.setCrspdBarCd(GetCellData(r, "��Ӧ������")); // 1 ��Ӧ������,
                whsDoc.setIsNtPrgrGdsBitMgmt(new Double(GetCellData(r, "�Ƿ��λ����")==null || GetCellData(r, "�Ƿ��λ����").equals("")? "0":GetCellData(r, "�Ƿ��λ����") ).intValue()); // 1 �Ƿ���л�λ����,
                whsDoc.setWhsAttr(GetCellData(r, "�ֿ�����")); // 1 �ֿ�����,
                whsDoc.setSellAvalQtyCtrlMode(GetCellData(r, "���ۿ��������Ʒ�ʽ")); // 1 ���ۿ��������Ʒ�ʽ,
                whsDoc.setInvtyAvalQtyCtrlMode(GetCellData(r, "�����������Ʒ�ʽ")); // 1 �����������Ʒ�ʽ,
                whsDoc.setMemo(GetCellData(r, "��ע")); // 1 ��ע,
                whsDoc.setIsNtShop(new Double(GetCellData(r, "�Ƿ��ŵ�")==null || GetCellData(r, "�Ƿ��ŵ�").equals("")? "0":GetCellData(r, "�Ƿ��ŵ�") ).intValue()); // 1 �Ƿ��ŵ�,
                if (GetCellData(r, "ͣ������") == null || GetCellData(r, "ͣ������").equals("")) {
                    whsDoc.setSetupTm(null);
                } else {
                    whsDoc.setStpUseDt(GetCellData(r, "ͣ������")); // 1 ͣ������,
                }
                whsDoc.setProv(GetCellData(r, "ʡ/ֱϽ��")); // 1 ʡ/ֱϽ��,
                whsDoc.setCty(GetCellData(r, "��")); // 1 ��,
                whsDoc.setCnty(GetCellData(r, "����")); // 1 ��,
                whsDoc.setDumyWhs(new Double(GetCellData(r, "�Ƿ������")).intValue()); // �����,,
                whsDoc.setSetupPers(GetCellData(r, "������")); // ������,
                whsDoc.setRealWhs(GetCellData(r, "ʵ��ֿ�")); // ʵ��ֿ�,
                if (GetCellData(r, "����ʱ��") == null || GetCellData(r, "����ʱ��").equals("")) {
                    whsDoc.setSetupTm(null);
                } else {
                    whsDoc.setSetupTm(GetCellData(r, "����ʱ��")); // ����ʱ��,
                }
                whsDoc.setMdfr(GetCellData(r, "�޸���")); // �޸���,
                if (GetCellData(r, "�޸�ʱ��") == null || GetCellData(r, "�޸�ʱ��").equals("")) {
                    whsDoc.setModiTm(null);
                } else {
                    whsDoc.setModiTm(GetCellData(r, "�޸�ʱ��")); // �޸�ʱ��,
                }
                temp.put(orderNo, whsDoc);

            }
            fileIn.close();
        } catch (Exception e) {
            throw new RuntimeException("�ļ��ĵ�" + j + "�е����ʽ����" +e.getMessage());
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
//			on.put("message", "�û��ֿ��Ѵ���");
//		} else {}

        int whs = whsDocMapper.insertUserWhs(userWhs);
        if (whs != 0) {
            on.put("isSuccess", true);
            on.put("message", "�����ɹ���");
        } else {
            on.put("isSuccess", false);
            on.put("message", "����ʧ�ܣ�");
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
            on.put("message", "�޸ĳɹ���");
        } else {
            on.put("isSuccess", false);
            on.put("message", "�޸�ʧ�ܣ�");
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
            message = "ɾ���ɹ���";
        } else {
            isSuccess = false;
            message = "���" + id + "�����ڣ�";
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
                resp = BaseJson.returnRespList("whs/whs_doc/queryList", true, "��ѯ�ɹ���", aList);
            } catch (IOException e) {

            }
        } else {

            try {
                resp = BaseJson.returnRespList("whs/whs_doc/queryList", false, "��ѯʧ��", aList);
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
            message = "ɾ���ɹ���";
        } else {
            isSuccess = false;
            message = "���" + id + "�����ڣ�";
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
                on.put("message", "�ֿ��λ�Ѵ���");
                return on;
            }
        }

        int whs = whsDocMapper.insertWhsGds(userWhs);
        if (whs != 0) {
            on.put("isSuccess", true);
            on.put("message", "�����ɹ���");
        } else {
            on.put("isSuccess", false);
            on.put("message", "����ʧ�ܣ�");
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
                resp = BaseJson.returnRespList("whs/whs_doc/selectWhsGds", true, "��ѯ�ɹ���", count, pageNo, pageSize,
                        listNum, pages, aList);
            } catch (IOException e) {

            }
        } else {

            try {
                resp = BaseJson.returnRespList("whs/whs_doc/selectWhsGds", false, "��ѯʧ��", aList);
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
                resp = BaseJson.returnRespList("whs/whs_doc/selectWhsGdsRealList", true, "��ѯ�ɹ���", aList);
            } catch (IOException e) {

            }
        } else {

            try {
                resp = BaseJson.returnRespList("whs/whs_doc/selectWhsGdsRealList", false, "��ѯʧ��", aList);
            } catch (IOException e) {

            }
        }

        return resp;
    }

    // �ܲ�
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
            on.put("message", "�ֿ���벻���ظ���");
        } else {
            if (wDoc.getStpUseDt() == "") {
                wDoc.setStpUseDt(null);
            }
            int whsDoc = whsDocMapper.insertRealWhs(wDoc);
            if (whsDoc == 1) {
                on.put("isSuccess", true);
                on.put("message", "�����ɹ���");
            } else {
                on.put("isSuccess", false);
                on.put("message", "����ʧ�ܣ�");
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
            on.put("message", "�޸ĳɹ���");
        } else {
            on.put("isSuccess", false);
            on.put("message", "�޸�ʧ�ܣ�");
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
            message = "ɾ���ɹ���";
        } else {
            isSuccess = false;
            message = "���" + whsEncd + "�����ڣ�";
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
            resp = BaseJson.returnRespObjList("whs/whs_doc/queryListDaYin", true, "��ѯ�ɹ���", null, aList);
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
            resp = BaseJson.returnRespList("whs/whs_doc/queryList", true, "��ѯ�ɹ���", count, pageNo, pageSize, listNum,
                    pages, aList);
        } catch (IOException e) {

        }
        return resp;
    }
    // �ֿ����ܲ�RealWhsMap

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
            on.put("message", "�ֿ��Ѵ����ܲ�");
            return on;
        }

        int whs = whsDocMapper.insertRealWhsMap(realWhsMap);
        if (whs != 0) {
            on.put("isSuccess", true);
            on.put("message", "�����ɹ���");
        } else {
            on.put("isSuccess", false);
            on.put("message", "����ʧ�ܣ�");
        }

        return on;
    }

    @Override
    public String selectRealWhsMap(Map map) {
        String resp = "";
        List<RealWhsMap> aList = whsDocMapper.selectRealWhsMapList(map);

        if (aList.size() > 0) {
            try {
                resp = BaseJson.returnRespList("whs/whs_doc/selectRealWhsMap", true, "��ѯ�ɹ���", aList);
            } catch (IOException e) {

            }
        } else {

            try {
                resp = BaseJson.returnRespList("whs/whs_doc/selectRealWhsMap", false, "��ѯʧ��", aList);
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
            message = "ɾ���ɹ���";
        } else {
            isSuccess = false;
            message = "���" + list + "�����ڣ�";
        }

        try {
            resp = BaseJson.returnRespObj("whs/whs_doc/deleteRealWhsMap", isSuccess, message, null);
        } catch (IOException e) {

        }
        return resp;

    }

}
