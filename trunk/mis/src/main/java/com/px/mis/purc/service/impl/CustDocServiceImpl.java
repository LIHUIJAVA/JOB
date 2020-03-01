package com.px.mis.purc.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.PredicatedMap;
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
import com.px.mis.purc.dao.CustClsDao;
import com.px.mis.purc.dao.CustDocDao;
import com.px.mis.purc.entity.CustCls;
import com.px.mis.purc.entity.CustDoc;
import com.px.mis.purc.service.CustDocService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;
import com.px.mis.util.poiTool;

/*�ͻ���������ʵ��*/
@Transactional
@Service
public class CustDocServiceImpl<E> extends poiTool implements CustDocService {
	@Autowired
	private CustDocDao cdd;
	@Autowired
	private CustClsDao ccd;

	@Override
	public String insertCustDoc(CustDoc custDoc) {

		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			if (custDoc.getCustId() != "" && custDoc.getCustId() != null) {
				if (ccd.selectCustClsByClsId(custDoc.getClsId()) != null) {
					if (cdd.selectClsId(custDoc.getCustId()) != null) {
						isSuccess = false;
						message = "���" + custDoc.getCustId() + "�Ѵ��ڣ����������룡";
					} else {
						if (custDoc.getLtstInvTm() == "") {
							custDoc.setLtstInvTm(null);
						}
						if (custDoc.getLtstRecvTm() == "") {
							custDoc.setLtstRecvTm(null);
						}
						if (custDoc.getDevDt() == "") {
							custDoc.setDevDt(null);
							;
						}
						cdd.insertCustDoc(custDoc);
						isSuccess = true;
						message = "�ͻ����������ɹ���";
					}
				} else {
					isSuccess = false;
					message = "�ͻ��������಻���ڣ�";
				}
			} else {
				isSuccess = false;
				message = "�ͻ����벻��Ϊ�գ�";
			}
			resp = BaseJson.returnRespObj("purc/CustDoc/insertCustDoc", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	@Override
	public ObjectNode updateCustDocByCustId(CustDoc custDoc) {

		ObjectNode on = null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (cdd.selectCustDocByCustId(custDoc.getCustId()) != null) {
			if (custDoc.getLtstInvTm() == "") {
				custDoc.setLtstInvTm(null);
			}
			if (custDoc.getLtstRecvTm() == "") {
				custDoc.setLtstRecvTm(null);
			}
			if (custDoc.getDevDt() == "") {
				custDoc.setDevDt(null);
				;
			}
			int a = cdd.updateCustDocByCustId(custDoc);
			if (a == 1) {
				on.put("isSuccess", true);
				on.put("message", "�ͻ������޸ĳɹ�");
			} else {
				on.put("isSuccess", false);
				on.put("message", "�ͻ������޸�ʧ��");
			}
		} else {
			on.put("isSuccess", false);
			on.put("message", "�ͻ�������Ų����ڣ�");
		}
		return on;
	}

	@Override
	public String deleteCustDocList(String custId) {

		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			List<String> list = getList(custId);
			int a = cdd.deleteCustDocList(list);
			if (a >= 1) {
				isSuccess = true;
				message = "ɾ���ɹ���";
			} else {
				isSuccess = false;
				message = "ɾ��ʧ�ܣ�";
			}
			resp = BaseJson.returnRespObj("purc/CustDoc/deleteCustDocList", isSuccess, message, null);
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

	// ���ձ�ż򵥲� �ͻ�����
	@Override
	public String selectCustDocByCustId(String custId) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			CustCls custCls = cdd.selectCustDocByCustId(custId);
			if (custCls == null) {
				isSuccess = false;
				message = "�ÿͻ������ڣ�";
			}
			resp = BaseJson.returnRespObj("purc/CustDoc/selectCustDocByCustId", isSuccess, message, custCls);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
	}

	// ��ѯȫ���ͻ�����
	@Override
	public String selectCustDocList(Map map) {
		String resp = "";
		List<String> custIdList = getList((String) map.get("custId"));// �ͻ�����
		map.put("custIdList", custIdList);

		List<String> custTotlCorpIdList = getList((String) map.get("custTotlCorpId"));// �ͻ��ܹ�˾����
		map.put("custTotlCorpIdList", custTotlCorpIdList);
		String clsId = (String) map.get("clsId");
		if (clsId != null && clsId != "") {
			if (ccd.selectCustClsByClsId(clsId).getLevel() == 0) {
				map.put("clsId", "");
			}
		}
		List<CustCls> custDocList = new ArrayList<CustCls>();
		if (map.get("start") != null && map.get("end") != null && ((String) map.get("start")).trim().length() > 0
				&& ((String) map.get("end")).trim().length() > 0) {
			// �˴��������ѯ
			String startStr = (String) map.get("start");
			map.put("tag", startStr.contains("-") ? startStr.split("-", 2)[0] : "");
			custDocList = cdd.selectCustDocListByItv(map);
		} else {
			custDocList = cdd.selectCustDocList(map);
		}

		int count = cdd.selectCustDocCount(map);

		int pageNo = Integer.parseInt(map.get("pageNo").toString());
		int pageSize = Integer.parseInt(map.get("pageSize").toString());
		int listNum = custDocList.size();
		int pages = count / pageSize + 1;
		try {
			resp = BaseJson.returnRespList("purc/CustDoc/selectCustDocList", true, "��ѯ�ɹ���", count, pageNo, pageSize,
					listNum, pages, custDocList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
	}

	// ��ӡ�����������ѯȫ���ͻ�����
	@Override
	public String printingCustDocList(Map map) {
		String resp = "";
		List<String> custIdList = getList((String) map.get("custId"));// �ͻ�����
		map.put("custIdList", custIdList);
		String clsId = (String) map.get("clsId");
		if (clsId != null && clsId != "") {
			if (ccd.selectCustClsByClsId(clsId).getLevel() == 0) {
				map.put("clsId", "");
			}
		}
		List<CustCls> custDocList = cdd.printingCustDocList(map);
		try {
			resp = BaseJson.returnRespObjList("purc/CustDoc/printingCustDocList", true, "��ѯ�ɹ���", null, custDocList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
	}

	// ����
	public String uploadFileAddDb(MultipartFile file) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		Map<String, CustDoc> custDocMap = uploadScoreInfo(file);
		for (Map.Entry<String, CustDoc> entry : custDocMap.entrySet()) {
			if (cdd.selectClsId(entry.getValue().getCustId()) != null) {
				isSuccess = false;
				message = "�ͻ������в��ֿͻ��Ѵ��ڣ��޷����룬������ٵ��룡";
				throw new RuntimeException(message);
			} else {
				cdd.insertCustDocUpload(entry.getValue());
				isSuccess = true;
				message = "�ͻ���������ɹ���";
			}
		}
		try {
			resp = BaseJson.returnRespObj("purc/CustDoc/uploadCustDocFile", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// ����excle
	private Map<String, CustDoc> uploadScoreInfo(MultipartFile file) {
		Map<String, CustDoc> temp = new HashMap<>();
		int j = 1;
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
				j++;
				Row r = sht0.getRow(i);
				// �����ǰ�е��кţ���0��ʼ��δ�ﵽ2�������У������ѭ��
				if (r.getRowNum() < 1) {
					continue;
				}
				String orderNo = GetCellData(r, "�ͻ�����");// �ͻ�����
				// ����ʵ����
				CustDoc custDoc = new CustDoc();
				if (temp.containsKey(orderNo)) {
					custDoc = temp.get(orderNo);
				}
				// ȡ����ǰ�е�1����Ԫ�����ݣ�����װ��pursOrdrʵ��ĸ���������
				custDoc.setCustId(orderNo);
				custDoc.setCustNm(GetCellData(r, "�ͻ�����"));// �ͻ�����
				custDoc.setCustShtNm(GetCellData(r, "�ͻ����"));// �ͻ����
				custDoc.setBllgCorp(GetCellData(r, "��Ʊ��λ"));// ��Ʊ��λ
				custDoc.setClsId(GetCellData(r, "�ͻ��������"));// �ͻ��������
				custDoc.setOpnBnk(GetCellData(r, "��������"));// ��������
				custDoc.setBkatNum(GetCellData(r, "�����˺�"));// �����˺�
				custDoc.setBankEncd(GetCellData(r, "�������б���"));// �������б���
				custDoc.setSctyCrdtCd(GetCellData(r, "ͳһ������ô���"));// ͳһ������ô���
				custDoc.setContcr(GetCellData(r, "��ϵ��"));// ��ϵ��
				custDoc.setTel(GetCellData(r, "�绰"));// �绰
				custDoc.setAddr(GetCellData(r, "��ַ"));// ��ַ
				custDoc.setCustTotlCorpId(GetCellData(r, "�ͻ��ܹ�˾����"));
				custDoc.setCustTotlCorp(GetCellData(r, "�ͻ��ܹ�˾����"));
				custDoc.setDelvAddr(GetCellData(r, "������ַ"));// ������ַ
				if (GetCellData(r, "��չ����") == null || GetCellData(r, "��չ����").equals("")) {
					custDoc.setDevDt(null);
				} else {
					custDoc.setDevDt(GetCellData(r, "��չ����"));// ��չ����
				}
				custDoc.setSetupPers(GetCellData(r, "������"));// ������
				if (GetCellData(r, "����ʱ��") == null || GetCellData(r, "����ʱ��").equals("")) {
					custDoc.setSetupDt(null);
				} else {
					custDoc.setSetupDt(GetCellData(r, "����ʱ��"));// ����ʱ��
				}
				custDoc.setMdfr(GetCellData(r, "�޸���"));// �޸���
				if (GetCellData(r, "�޸�ʱ��") == null || GetCellData(r, "�޸�ʱ��").equals("")) {
					custDoc.setModiDt(null);
				} else {
					custDoc.setModiDt(GetCellData(r, "�޸�ʱ��"));// �޸�ʱ��
				}
				custDoc.setMemo(GetCellData(r, "��ע"));// ��ע
				custDoc.setTaxRate(GetBigDecimal(GetCellData(r, "˰��"), 8));// 8��ʾС��λ�� //˰��
				if (GetCellData(r, "�����Ʊʱ��") == null || GetCellData(r, "�����Ʊʱ��").equals("")) {
					custDoc.setLtstInvTm(null);
				} else {
					custDoc.setLtstInvTm(GetCellData(r, "�����Ʊʱ��"));// �����Ʊʱ��
				}
				custDoc.setLtstInvAmt(GetBigDecimal(GetCellData(r, "�����Ʊ���"), 8));// 8��ʾС��λ�� //�����Ʊ���
				if (GetCellData(r, "����տ�ʱ��") == null || GetCellData(r, "����տ�ʱ��").equals("")) {
					custDoc.setLtstRecvTm(null);
				} else {
					custDoc.setLtstRecvTm(GetCellData(r, "����տ�ʱ��"));// ����տ�ʱ��
				}
				custDoc.setLtstRecvAmt(GetBigDecimal(GetCellData(r, "����տ���"), 8));// 8��ʾС��λ�� //����տ���
				custDoc.setRecvblBal(GetBigDecimal(GetCellData(r, "Ӧ�����"), 8));// 8��ʾС��λ�� //Ӧ�����
				temp.put(orderNo, custDoc);
			}
			fileIn.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("�ļ��ĵ�" + j + "�е����ʽ�����޷�����!" + e.getMessage());
		}
		return temp;
	}

}
