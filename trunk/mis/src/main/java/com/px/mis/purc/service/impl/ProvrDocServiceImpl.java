package com.px.mis.purc.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.px.mis.purc.dao.ProvrClsDao;
import com.px.mis.purc.dao.ProvrDocDao;
import com.px.mis.purc.entity.ProvrCls;
import com.px.mis.purc.entity.ProvrDoc;
import com.px.mis.purc.service.ProvrDocService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.poiTool;

/*��Ӧ�̵�������*/
@Transactional
@Service
public class ProvrDocServiceImpl<E> extends poiTool implements ProvrDocService {
	@Autowired
	private ProvrDocDao pdd;
	@Autowired
	private ProvrClsDao pcd;

	// ������Ӧ�̵���
	@Override
	public String insertProvrDoc(ProvrDoc provrDoc) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			if (provrDoc.getProvrId() != "" && provrDoc.getProvrId() != null) {
				if (pcd.selectProvrClsByProvrClsId(provrDoc.getProvrClsId()) != null) {
					if (pdd.selectProvrId(provrDoc.getProvrId()) != null) {
						isSuccess = false;
						message = "���" + provrDoc.getProvrId() + "�Ѵ��ڣ����������룡";
					} else {
						if (provrDoc.getDevDt() == "") {
							provrDoc.setDevDt(null);
						}
						int a = pdd.insertProvrDoc(provrDoc);
						if (a >= 1) {
							isSuccess = true;
							message = "��Ӧ�̵��������ɹ���";
						} else {
							isSuccess = false;
							message = "��Ӧ�̵�������ʧ�ܣ�";
						}

					}
				} else {
					isSuccess = false;
					message = "��Ӧ�̵������಻���ڣ�";
				}
			} else {
				isSuccess = false;
				message = "��Ӧ�̱��벻��Ϊ�գ�";
			}
			resp = BaseJson.returnRespObj("purc/ProvrDoc/insertProvrDoc", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// �޸Ĺ�Ӧ�̵���
	@Override
	public String updateProvrDocByProvrId(ProvrDoc provrDoc) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			if (provrDoc.getProvrId() != "" && provrDoc.getProvrId() != null) {
				if (pcd.selectProvrClsByProvrClsId(provrDoc.getProvrClsId()) != null) {
					pdd.updateProvrDocByProvrId(provrDoc);
					isSuccess = true;
					message = "��Ӧ�̵����޸ĳɹ���";
				} else {
					isSuccess = false;
					message = "��Ӧ�̵������಻���ڣ�";
				}
			} else {
				isSuccess = false;
				message = "��Ӧ�̱��벻��Ϊ�գ�";
			}
			resp = BaseJson.returnRespObj("purc/ProvrDoc/updateProvrDocByProvrId", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// ���ձ�Ų�ѯ ��Ӧ�̵���
	@Override
	public ProvrCls selectProvrDocByProvrId(String provrId) {
		ProvrCls provrCls = pdd.selectProvrDocByProvrId(provrId);
		return provrCls;
	}

	// ��ѯȫ����Ӧ�̵���
	@Override
	public String selectProvrDocList(Map map) {
		String resp = "";
		try {
			List<String> provrIdList = getList((String) map.get("provrId"));// ��Ӧ�̱���
			map.put("provrIdList", provrIdList);
			String provrClsId = (String) map.get("provrClsId");
			if (provrClsId != null && provrClsId != "") {
				if (pcd.selectProvrClsByProvrClsId(provrClsId).getLevel() == 0) {
					map.put("provrClsId", "");
				}
			}
			List<ProvrCls> provrDocList = new ArrayList<ProvrCls>();
			// ��Ӧ�������ѯ����
			if (map.get("start") != null && map.get("end") != null && !map.get("start").equals("")
					&& !map.get("end").equals("")) {
				provrDocList = pdd.selectProvrDocListByItv(map);
			} else {
				provrDocList = pdd.selectProvrDocList(map);
			}
			int count = pdd.selectProvrDocCount(map);
			int pageNo = Integer.parseInt(map.get("pageNo").toString());
			int pageSize = Integer.parseInt(map.get("pageSize").toString());
			int listNum = provrDocList.size();
			int pages = count / pageSize + 1;
			resp = BaseJson.returnRespList("purc/ProvrDoc/selectProvrDocList", true, "��ѯ�ɹ���", count, pageNo, pageSize,
					listNum, pages, provrDocList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
	}

	// ��ӡ�����������Ӧ�̵���
	@Override
	public String printingProvrDocList(Map map) {
		String resp = "";
		List<String> provrIdList = getList((String) map.get("provrId"));// ��Ӧ�̱���
		map.put("provrIdList", provrIdList);
		String provrClsId = (String) map.get("provrClsId");
		if (provrClsId != null && provrClsId != "") {
			if (pcd.selectProvrClsByProvrClsId(provrClsId).getLevel() == 0) {
				map.put("provrClsId", "");
			}
		}
		List<ProvrCls> provrDocList = pdd.printingProvrDocList(map);
		try {
			resp = BaseJson.returnRespObjList("purc/ProvrDoc/printingProvrDocList", true, "��ѯ�ɹ���", null, provrDocList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
	}

	// ����ɾ����Ӧ�̵���
	@Override
	public String deleteProvrDocList(String provrId) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			List<String> list = getList(provrId);
			int a = pdd.deleteProvrDocList(list);
			if (a >= 1) {
				isSuccess = true;
				message = "ɾ���ɹ���";
			} else {
				isSuccess = false;
				message = "ɾ��ʧ�ܣ�";
			}
			resp = BaseJson.returnRespObj("purc/ProvrDoc/deleteProvrDocList", isSuccess, message, null);
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

	// ����
	public String uploadFileAddDb(MultipartFile file) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		Map<String, ProvrDoc> provrDocMap = uploadScoreInfo(file);
		for (Map.Entry<String, ProvrDoc> entry : provrDocMap.entrySet()) {
			if (pdd.selectProvrId(entry.getValue().getProvrId()) != null) {
				isSuccess = false;
				message = "��Ӧ�̵����в��ֿͻ��Ѵ��ڣ��޷����룬������ٵ��룡";
				throw new RuntimeException(message);
			} else {
				pdd.insertProvrDocUpload(entry.getValue());
				isSuccess = true;
				message = "��Ӧ�̵�������ɹ���";
			}
		}
		try {
			resp = BaseJson.returnRespObj("purc/ProvrDoc/uploadProvrDocFile", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// ����excle
	private Map<String, ProvrDoc> uploadScoreInfo(MultipartFile file) {
		Map<String, ProvrDoc> temp = new HashMap<>();
		int j = 0;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
				String orderNo = GetCellData(r, "��Ӧ�̱���");
				// ����ʵ����
				ProvrDoc provrDoc = new ProvrDoc();
				if (temp.containsKey(orderNo)) {
					provrDoc = temp.get(orderNo);
				}
				// ȡ����ǰ�е�1����Ԫ�����ݣ�����װ��pursOrdrʵ��ĸ���������
				provrDoc.setProvrId(orderNo);// ��Ӧ�̱���
				provrDoc.setProvrNm(GetCellData(r, "��Ӧ������"));// ��Ӧ������
				provrDoc.setProvrShtNm(GetCellData(r, "��Ӧ�̼��"));// ��Ӧ�̼��
				provrDoc.setProvrClsId(GetCellData(r, "��Ӧ�̷������"));// ��Ӧ�̷������
				provrDoc.setOpnBnk(GetCellData(r, "��������"));// ��������
				provrDoc.setBkatNum(GetCellData(r, "�����˺�"));// �����˺�
				provrDoc.setBankEncd(GetCellData(r, "�������б���"));// �������б���
				provrDoc.setRgstCap(GetBigDecimal(GetCellData(r, "ע���ʽ�"), 8));// 8��ʾС��λ�� //ע���ʽ�
				provrDoc.setLpr(GetCellData(r, "����"));// ����
				provrDoc.setContcr(GetCellData(r, "��ϵ��"));// ��ϵ��
				provrDoc.setTel(GetCellData(r, "��ϵ�绰"));// ��ϵ�绰
				provrDoc.setZipCd(GetCellData(r, "��������"));// ��������
				provrDoc.setAddr(GetCellData(r, "��ַ"));// ��ַ
				provrDoc.setTaxRate(GetBigDecimal(GetCellData(r, "˰��"), 8));// 8��ʾС��λ�� //˰��
				provrDoc.setIsNtPurs(new Double(GetCellData(r, "�Ƿ�ɹ�")).intValue());// �Ƿ�ɹ�
				provrDoc.setIsOutsource(new Double(GetCellData(r, "�Ƿ�ί��")).intValue());// �Ƿ�ί��
				provrDoc.setIsNtServ(new Double(GetCellData(r, "�Ƿ����")).intValue());// �Ƿ����
				if (GetCellData(r, "��չ����") == null || GetCellData(r, "��չ����").equals("")) {
					provrDoc.setDevDt(null);
				} else {
					provrDoc.setDevDt(GetCellData(r, "��չ����"));// ��չ����
				}
				provrDoc.setSetupPers(GetCellData(r, "������"));// ������
				if (GetCellData(r, "����ʱ��") == null || GetCellData(r, "����ʱ��").equals("")) {
					provrDoc.setSetupDt(null);
				} else {
					provrDoc.setSetupDt(GetCellData(r, "����ʱ��"));// ����ʱ��
				}
				provrDoc.setMdfr(GetCellData(r, "�޸���"));// �޸���
				if (GetCellData(r, "�޸�ʱ��") == null || GetCellData(r, "�޸�ʱ��").equals("")) {
					provrDoc.setModiDt(null);
				} else {
					provrDoc.setModiDt(GetCellData(r, "�޸�ʱ��"));// �޸�ʱ��
				}
				provrDoc.setMemo(GetCellData(r, "��ע"));// ��ע

				temp.put(orderNo, provrDoc);
			}
			fileIn.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("�ļ��ĵ�" + j + "�е����ʽ�����޷�����!" + e.getMessage());
		}
		return temp;
	}

}
