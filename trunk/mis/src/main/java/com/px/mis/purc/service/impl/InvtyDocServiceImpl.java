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

//�����������
@Transactional
@Service
public class InvtyDocServiceImpl extends poiTool implements InvtyDocService {
    @Autowired
    private InvtyDocDao idd;
    @Autowired
    private InvtyClsDao icd;

    //�������������Ϣ
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
                        message = "���" + invtyDoc.getInvtyEncd() + "�Ѵ��ڣ����������룡";
                    } else {
                        if (invtyDoc.getIsNtBarCdMgmt() == null) {
                            invtyDoc.setIsNtBarCdMgmt(0);//�Ƿ����������
                        }
                        if (invtyDoc.getIsDomSale() == null) {
                            invtyDoc.setIsDomSale(0);//�Ƿ�����
                        }
                        if (invtyDoc.getIsNtDiscnt() == null) {
                            invtyDoc.setIsNtDiscnt(0);//�Ƿ��ۿ�
                        }
                        if (invtyDoc.getIsNtPurs() == null) {
                            invtyDoc.setIsNtPurs(0);//�Ƿ�ɹ�
                        }
                        if (invtyDoc.getIsNtSell() == null) {
                            invtyDoc.setIsNtSell(0);//�Ƿ�����
                        }
                        if (invtyDoc.getPto() == null) {
                            invtyDoc.setPto(0);//�Ƿ�PTO
                        }
                        if (invtyDoc.getIsQuaGuaPer() == null) {
                            invtyDoc.setIsQuaGuaPer(0);//�Ƿ����ڹ���
                        }
                        if (invtyDoc.getShdTaxLabour() == null) {
                            invtyDoc.setShdTaxLabour(0);
                            ;//�Ƿ�Ӧ˰����
                        }
                        if (invtyDoc.getAllowBomMain() == null) {
                            invtyDoc.setAllowBomMain(0);//�Ƿ�����BOM����
                        }
                        if (invtyDoc.getAllowBomMinor() == null) {
                            invtyDoc.setAllowBomMinor(0);//�Ƿ�����BOM�Ӽ�
                        }
                        int a = idd.insertInvtyDoc(invtyDoc);
                        if (a > 0) {
                            isSuccess = true;
                            message = "������������ɹ���";
                        } else {
                            isSuccess = false;
                            message = "�����������ʧ�ܣ�";
                        }
                    }
                } else {
                    isSuccess = false;
                    message = "������಻���ڣ�";
                }
            } else {
                isSuccess = false;
                message = "������벻��Ϊ�գ�";
            }
            resp = BaseJson.returnRespObj("purc/InvtyDoc/insertInvtyDoc", isSuccess, message, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resp;
    }

    //�޸Ĵ��������Ϣ
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
                on.put("message", "�޸Ĵ�������ɹ�");
            } else {
                on.put("isSuccess", false);
                on.put("message", "�޸Ĵ������ʧ��");
            }
        } else {
            on.put("isSuccess", false);
            on.put("message", "������಻���ڣ�");
        }
        return on;
    }

    //ɾ�����������Ϣ
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
                message = "ɾ���ɹ���";
            } else {
                isSuccess = false;
                message = "ɾ��ʧ�ܣ�";
            }
            resp = BaseJson.returnRespObj("purc/InvtyDoc/deleteInvtyDocList", isSuccess, message, null);
        } catch (IOException e) {
            e.printStackTrace();
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

    //����id  ��ѯ�������
    @Override
    public InvtyDoc selectInvtyDocByInvtyDocEncd(String invtyEncd) {
        InvtyDoc invtyDoc = idd.selectInvtyDocByInvtyDocEncd(invtyEncd);
        return invtyDoc;
    }

    //��ѯ���������������Ϣ
    @Override
    public String selectInvtyDocList(Map map) {
        String resp = "";
        List<String> invtyEncdList = getList((String) map.get("invtyEncd"));//�������
        map.put("invtyEncdList", invtyEncdList);
        String invtyClsEncd = (String) map.get("invtyClsEncd");
        if (invtyClsEncd != null && invtyClsEncd != "") {
            InvtyCls invtyCls = icd.selectInvtyClsByInvtyClsEncd(invtyClsEncd);
            if (invtyCls != null && invtyCls.getLevel() == 0) {
                map.put("invtyClsEncd", "");
            }
            //δ�鵽�����������Ӧ������� ��Ӧ
            if (invtyCls == null) {
                String message = "����������" + invtyClsEncd + "������";
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
            resp = BaseJson.returnRespList("purc/InvtyDoc/selectInvtyDocList", true, "��ѯ�ɹ���", count, pageNo, pageSize,
                    listNum, pages, invtyDocList);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return resp;
    }
    //���õ����ӿ�
    @Override
    public String printingInvtyDocList(Map map) {
        String resp = "";
        List<String> invtyEncdList = getList((String) map.get("invtyEncd"));//�������
        map.put("invtyEncdList", invtyEncdList);
        String invtyClsEncd = (String) map.get("invtyClsEncd");
        if (invtyClsEncd != null && invtyClsEncd != "") {
            if (icd.selectInvtyClsByInvtyClsEncd(invtyClsEncd).getLevel() == 0) {
                map.put("invtyClsEncd", "");
            }
        }
        List<InvtyDoc> invtyDocList = idd.printingInvtyDocList(map);
        try {
            resp = BaseJson.returnRespObjList("purc/InvtyDoc/printingInvtyDocList", true, "��ѯ�ɹ���", null, invtyDocList);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return resp;
    }

    //����
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
            message = "������쳣����";
            throw new RuntimeException(message);
        }
        for (Map.Entry<String, InvtyDoc> entry : invtyDocMap.entrySet()) {
            if (idd.selectInvtyEncd(entry.getValue().getInvtyEncd()) != null) {
                isSuccess = false;
                message = "��������в��ִ���Ѵ��ڣ��޷����룬������ٵ��룡";
                throw new RuntimeException(message);
            } else {
                idd.insertInvtyDoc(entry.getValue());
                isSuccess = true;
                message = "�����������ɹ���";
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

    //����excle
    private Map<String, InvtyDoc> uploadScoreInfo(MultipartFile file) {
        Map<String, InvtyDoc> temp = new HashMap<>();
        int j = 1;
        try {
            InputStream fileIn = file.getInputStream();
            //����ָ�����ļ�����������Excel�Ӷ�����Workbook����
            Workbook wb0 = new HSSFWorkbook(fileIn);
            //��ȡExcel�ĵ��еĵ�һ����
            Sheet sht0 = wb0.getSheetAt(0);
            //��õ�ǰsheet�Ŀ�ʼ��
            int firstRowNum = sht0.getFirstRowNum();
            //��ȡ�ļ������һ��
            int lastRowNum = sht0.getLastRowNum();
            //���������ֶκ��±�ӳ��
            SetColIndex(sht0.getRow(firstRowNum));
            //����������
            getCellNames();
            //��Sheet�е�ÿһ�н��е���
            for (int i = firstRowNum + 1; i <= lastRowNum; i++) {
                j++;
                Row r = sht0.getRow(i);
                //�����ǰ�е��кţ���0��ʼ��δ�ﵽ2�������У������ѭ��
                if (r.getRowNum() < 1) {
                    continue;
                }
                String orderNo = GetCellData(r, "�������");
                //����ʵ����
                InvtyDoc invtyDoc = new InvtyDoc();
                if (temp.containsKey(orderNo)) {
                    invtyDoc = temp.get(orderNo);
                }
                //ȡ����ǰ�е�1����Ԫ�����ݣ�����װ��pursOrdrʵ��ĸ���������
                invtyDoc.setInvtyEncd(orderNo);//�������
                invtyDoc.setInvtyNm(GetCellData(r, "�������"));//�������
                invtyDoc.setInvtyClsEncd(GetCellData(r, "����������"));//����������
                invtyDoc.setSpcModel(GetCellData(r, "����ͺ�"));//����ͺ�
                invtyDoc.setInvtyCd(GetCellData(r, "�������"));//�������
//				invtyDoc.setProvrId(GetCellData(r,"��Ӧ�̱���"));//��Ӧ�̱���
                invtyDoc.setMeasrCorpId(GetCellData(r, "������λ����"));//����������
                invtyDoc.setVol(GetCellData(r, "���"));//���
                invtyDoc.setWeight(GetBigDecimal(GetCellData(r, "����"), 8));//8��ʾС��λ��  //����
                invtyDoc.setLongs(GetBigDecimal(GetCellData(r, "��"), 8));//8��ʾС��λ��  //��
                invtyDoc.setWide(GetBigDecimal(GetCellData(r, "��"), 8));//8��ʾС��λ��  //��
                invtyDoc.setHght(GetBigDecimal(GetCellData(r, "��"), 8));//8��ʾС��λ�� //��
                invtyDoc.setBxRule(GetBigDecimal(GetCellData(r, "���"), 8));//8��ʾС��λ��  //���
                invtyDoc.setBaoZhiQiEarWarn(GetCellData(r, "������Ԥ������"));//������Ԥ������
                invtyDoc.setBaoZhiQiDt(GetCellData(r, "����������"));//����������
                invtyDoc.setPlaceOfOrigin(GetCellData(r, "����"));//����
                invtyDoc.setRgstBrand(GetCellData(r, "ע���̱�"));//ע���̱�
                invtyDoc.setCretNum(GetCellData(r, "�ϸ�֤��"));//�ϸ�֤��
                invtyDoc.setMakeLicsNum(GetCellData(r, "�������֤"));//�������֤��
                invtyDoc.setTraySize(GetBigDecimal(GetCellData(r, "�мܳߴ�"), 8));//8��ʾС��λ��//�мܳߴ�
                invtyDoc.setQuantity(GetCellData(r, "����/��"));//����/��
                invtyDoc.setValtnMode(GetCellData(r, "�Ƽ۷�ʽ"));//�Ƽ۷�ʽ
                invtyDoc.setIptaxRate(GetBigDecimal(GetCellData(r, "����˰��"), 8));//8��ʾС��λ�� //����˰��
                invtyDoc.setOptaxRate(GetBigDecimal(GetCellData(r, "����˰��"), 8));//8��ʾС��λ�� //����˰��
                invtyDoc.setHighestPurPrice(GetBigDecimal(GetCellData(r, "��߽���"), 8));//8��ʾС��λ�� //��߽���
                invtyDoc.setLoSellPrc(GetBigDecimal(GetCellData(r, "����ۼ�"), 8));//8��ʾС��λ�� //����ۼ�
                invtyDoc.setLtstCost(GetBigDecimal(GetCellData(r, "���³ɱ�"), 8));//8��ʾС��λ�� //���³ɱ�
                invtyDoc.setRefCost(GetBigDecimal(GetCellData(r, "�ο��ɱ�"), 8));//8��ʾС��λ�� //�ο��ɱ�
                invtyDoc.setRefSellPrc(GetBigDecimal(GetCellData(r, "�ο��ۼ�"), 8));//8��ʾС��λ�� //�ο��ۼ�
//				invtyDoc.setIsNtSell(new Double(GetCellData(r,"�Ƿ�����")).intValue());//�Ƿ�����
                invtyDoc.setIsNtPurs(new Double(GetCellData(r, "�Ƿ�ɹ�")).intValue());//�Ƿ�ɹ�
                invtyDoc.setIsDomSale(new Double(GetCellData(r, "�Ƿ�����")).intValue());//�Ƿ�����
                invtyDoc.setPto(new Double(GetCellData(r, "PTO")).intValue());//�Ƿ�PTO
                invtyDoc.setIsQuaGuaPer(new Double(GetCellData(r, "�Ƿ����ڹ���")).intValue());//�Ƿ����ڹ���
                invtyDoc.setAllowBomMain(new Double(GetCellData(r, "����BOM����")).intValue());//�Ƿ�����BOM����
                invtyDoc.setAllowBomMinor(new Double(GetCellData(r, "����BOM�Ӽ�")).intValue());//�Ƿ�����BOM�Ӽ�
                invtyDoc.setIsNtBarCdMgmt(new Double(GetCellData(r, "�Ƿ����������")).intValue());//�Ƿ����������
                invtyDoc.setCrspdBarCd(GetCellData(r, "��Ӧ������"));//��Ӧ������
                invtyDoc.setSetupPers(GetCellData(r, "������"));//������
                if (GetCellData(r, "��������") == null || GetCellData(r, "��������").equals("")) {
                    invtyDoc.setSetupDt(null);
                } else {
                    invtyDoc.setSetupDt(GetCellData(r, "��������"));//����ʱ��
                }
                invtyDoc.setMdfr(GetCellData(r, "�޸���"));//�޸���
                if (GetCellData(r, "�޸�����") == null || GetCellData(r, "�޸�����").equals("")) {
                    invtyDoc.setModiDt(null);
                } else {
                    invtyDoc.setModiDt(GetCellData(r, "�޸�����"));//�޸�ʱ��
                }
                invtyDoc.setMemo(GetCellData(r, "��ע"));//��ע
                invtyDoc.setShdTaxLabour(new Double(GetCellData(r, "Ӧ˰����")).intValue());//Ӧ˰����
                invtyDoc.setIsNtDiscnt(new Double(GetCellData(r, "�Ƿ��ۿ�")).intValue());//�Ƿ��ۿ�

                temp.put(orderNo, invtyDoc);

            }
            fileIn.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("�ļ��ĵ�" + j + "�е����ʽ�����޷�����!" + e.getMessage());
        }
        return temp;
    }

    //����excle
    private Map<String, InvtyDoc> uploadScoreInfoU8(MultipartFile file) {
        Map<String, InvtyDoc> temp = new HashMap<>();
        int j = 1;
        try {
            InputStream fileIn = file.getInputStream();
            //����ָ�����ļ�����������Excel�Ӷ�����Workbook����
            Workbook wb0 = new HSSFWorkbook(fileIn);
            //��ȡExcel�ĵ��еĵ�һ����
            Sheet sht0 = wb0.getSheetAt(0);
            //��õ�ǰsheet�Ŀ�ʼ��
            int firstRowNum = sht0.getFirstRowNum();
            //��ȡ�ļ������һ��
            int lastRowNum = sht0.getLastRowNum();
            //���������ֶκ��±�ӳ��
            SetColIndex(sht0.getRow(firstRowNum));
            //��Sheet�е�ÿһ�н��е���
            for (int i = firstRowNum + 1; i <= lastRowNum; i++) {
                j++;
                Row r = sht0.getRow(i);
                //�����ǰ�е��кţ���0��ʼ��δ�ﵽ2�������У������ѭ��
                if (r.getRowNum() < 1) {
                    continue;
                }
                String orderNo = GetCellData(r, "�������");
                //����ʵ����
                InvtyDoc invtyDoc = new InvtyDoc();
                if (temp.containsKey(orderNo)) {
                    invtyDoc = temp.get(orderNo);
                }
                //ȡ����ǰ�е�1����Ԫ�����ݣ�����װ��pursOrdrʵ��ĸ���������
                invtyDoc.setInvtyEncd(orderNo);//�������
                invtyDoc.setInvtyNm(GetCellData(r, "�������"));//�������
                invtyDoc.setInvtyClsEncd(GetCellData(r, "����������"));//����������
                invtyDoc.setSpcModel(GetCellData(r, "����ͺ�"));//����ͺ�
                invtyDoc.setInvtyCd(GetCellData(r, "�������"));//�������
//				invtyDoc.setProvrId(GetCellData(r,"��Ӧ�̱���"));//��Ӧ�̱���
                invtyDoc.setMeasrCorpId(GetCellData(r, "��������λ����"));//����������
                invtyDoc.setVol(GetCellData(r, "��λ���"));//���
                invtyDoc.setWeight(GetBigDecimal(GetCellData(r, "������λ����"), 8));//8��ʾС��λ��  //����
                invtyDoc.setLongs(GetBigDecimal(GetCellData(r, "��"), 8));//8��ʾС��λ��  //��
                invtyDoc.setWide(GetBigDecimal(GetCellData(r, "��"), 8));//8��ʾС��λ��  //��
                invtyDoc.setHght(GetBigDecimal(GetCellData(r, "��"), 8));//8��ʾС��λ�� //��
                invtyDoc.setBxRule(GetBigDecimal(GetCellData(r, "���"), 8));//8��ʾС��λ��  //���
                invtyDoc.setBaoZhiQiEarWarn(GetCellData(r, "������Ԥ������"));//������Ԥ������
                invtyDoc.setBaoZhiQiDt(GetCellData(r, "������"));//����������
                invtyDoc.setPlaceOfOrigin(GetCellData(r, "���س���"));//����
                invtyDoc.setRgstBrand(GetCellData(r, "ע���̱�"));//ע���̱�
                invtyDoc.setCretNum(GetCellData(r, "�ϸ�֤��"));//�ϸ�֤��
                invtyDoc.setMakeLicsNum(GetCellData(r, "���֤��"));//�������֤��
//				invtyDoc.setTraySize(GetBigDecimal(GetCellData(r,"�мܳߴ�"),8));//8��ʾС��λ��//�мܳߴ�
//				invtyDoc.setQuantity(GetCellData(r,"����/��"));//����/��
                invtyDoc.setValtnMode(GetCellData(r, "�Ƽ۷�ʽ"));//�Ƽ۷�ʽ
                invtyDoc.setIptaxRate(GetBigDecimal(GetCellData(r, "����˰��%"), 8));//8��ʾС��λ�� //����˰��
                invtyDoc.setOptaxRate(GetBigDecimal(GetCellData(r, "����˰��%"), 8));//8��ʾС��λ�� //����˰��
                invtyDoc.setHighestPurPrice(GetBigDecimal(GetCellData(r, "��߽���"), 8));//8��ʾС��λ�� //��߽���
                invtyDoc.setLoSellPrc(GetBigDecimal(GetCellData(r, "����ۼ�"), 8));//8��ʾС��λ�� //����ۼ�
                invtyDoc.setLtstCost(GetBigDecimal(GetCellData(r, "���³ɱ�"), 8));//8��ʾС��λ�� //���³ɱ�
                invtyDoc.setRefCost(GetBigDecimal(GetCellData(r, "�ο��ɱ�"), 8));//8��ʾС��λ�� //�ο��ɱ�
                invtyDoc.setRefSellPrc(GetBigDecimal(GetCellData(r, "�ο��ۼ�"), 8));//8��ʾС��λ�� //�ο��ۼ�
//				invtyDoc.setIsNtSell(new Double(GetCellData(r,"�Ƿ�����")).intValue());//�Ƿ�����
                if (GetCellData(r, "�Ƿ�ɹ�") != null && GetCellData(r, "�Ƿ�ɹ�").equals("��")) {
                    invtyDoc.setIsNtPurs(0); // �Ƿ�ɹ�
                } else if (GetCellData(r, "�Ƿ�ɹ�") != null && GetCellData(r, "�Ƿ�ɹ�").equals("��")) {
                    invtyDoc.setIsNtPurs(1);  // �Ƿ�ɹ�',
                }
                if (GetCellData(r, "�Ƿ�����") != null && GetCellData(r, "�Ƿ�����").equals("��")) {
                    invtyDoc.setIsDomSale(0); //�Ƿ�����',
                } else if (GetCellData(r, "�Ƿ�����") != null && GetCellData(r, "�Ƿ�����").equals("��")) {
                    invtyDoc.setIsDomSale(1);  //�Ƿ�����,
                }
                if (GetCellData(r, "PTO") != null && GetCellData(r, "PTO").equals("��")) {
                    invtyDoc.setPto(0); // PTO,
                } else if (GetCellData(r, "PTO") != null && GetCellData(r, "PTO").equals("��")) {
                    invtyDoc.setPto(1);  // PTO,
                }
                if (GetCellData(r, "�Ƿ����ڹ���") != null && GetCellData(r, "�Ƿ����ڹ���").equals("��")) {
                    invtyDoc.setIsQuaGuaPer(0); // '�Ƿ����ڹ���,
                } else if (GetCellData(r, "�Ƿ����ڹ���") != null && GetCellData(r, "�Ƿ����ڹ���").equals("��")) {
                    invtyDoc.setIsQuaGuaPer(1);  // �Ƿ����ڹ���,
                }
                if (GetCellData(r, "����BOMĸ��") != null && GetCellData(r, "����BOMĸ��").equals("��")) {
                    invtyDoc.setAllowBomMain(0); // ����BOMĸ��,
                } else if (GetCellData(r, "����BOMĸ��") != null && GetCellData(r, "����BOMĸ��").equals("��")) {
                    invtyDoc.setAllowBomMain(1);  // ����BOMĸ��,
                }
                if (GetCellData(r, "����BOM�Ӽ�") != null && GetCellData(r, "����BOM�Ӽ�").equals("��")) {
                    invtyDoc.setAllowBomMinor(0); // ����BOM�Ӽ�,
                } else if (GetCellData(r, "����BOM�Ӽ�") != null && GetCellData(r, "����BOM�Ӽ�").equals("��")) {
                    invtyDoc.setAllowBomMinor(1);  // ����BOM�Ӽ�
                }
                if (GetCellData(r, "���������") != null && GetCellData(r, "���������").equals("��")) {
                    invtyDoc.setIsNtBarCdMgmt(0); // ���������,
                } else if (GetCellData(r, "���������") != null && GetCellData(r, "�Ƿ�����").equals("��")) {
                    invtyDoc.setIsNtBarCdMgmt(1);  // ���������,
                }
                invtyDoc.setCrspdBarCd(GetCellData(r, "��Ӧ������"));//��Ӧ������
                invtyDoc.setSetupPers(GetCellData(r, "������"));//������
                if (GetCellData(r, "��������") == null || GetCellData(r, "��������").equals("")) {
                    invtyDoc.setSetupDt(null);
                } else {
                    invtyDoc.setSetupDt(GetCellData(r, "��������"));//����ʱ��
                }
                invtyDoc.setMdfr(GetCellData(r, "�����"));//�޸���
                if (GetCellData(r, "�������") == null || GetCellData(r, "�������").equals("")) {
                    invtyDoc.setModiDt(null);
                } else {
                    invtyDoc.setModiDt(GetCellData(r, "�������"));//�޸�ʱ��
                }
//				invtyDoc.setMemo(GetCellData(r,"��ע"));//��ע
                if (GetCellData(r, "�Ƿ�Ӧ˰����") != null && GetCellData(r, "�Ƿ�Ӧ˰����").equals("��")) {
                    invtyDoc.setShdTaxLabour(0); // �Ƿ�Ӧ˰����,
                } else if (GetCellData(r, "�Ƿ�Ӧ˰����") != null && GetCellData(r, "�Ƿ�Ӧ˰����").equals("��")) {
                    invtyDoc.setShdTaxLabour(1);  // �Ƿ�Ӧ˰����,
                }
                if (GetCellData(r, "�Ƿ��ۿ�") != null && GetCellData(r, "�Ƿ��ۿ�").equals("��")) {
                    invtyDoc.setIsNtDiscnt(0);//Ӧ˰����,
                } else if (GetCellData(r, "�Ƿ��ۿ�") != null && GetCellData(r, "�Ƿ��ۿ�").equals("��")) {
                    invtyDoc.setIsNtDiscnt(1); //�Ƿ��ۿ�
                }

                temp.put(orderNo, invtyDoc);

            }
            fileIn.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("�ļ��ĵ�" + j + "�е����ʽ�����޷�����!" + e.getMessage());
        }
        return temp;
    }

    @Override
    public String selectInvtyEncdLike(Map map) {
        String resp = "";
        List<InvtyDoc> invtyDocList = idd.selectInvtyEncdLike(map);
        try {
            resp = BaseJson.returnRespObjList("purc/InvtyDoc/selectInvtyEncdLike", true, "��ѯ�ɹ���", null, invtyDocList);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return resp;
    }

}
