package com.px.mis.account.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.dao.OutIntoWhsAdjSnglDao;
import com.px.mis.account.dao.OutIntoWhsAdjSnglSubTabDao;
import com.px.mis.account.entity.OutIntoWhsAdjSngl;
import com.px.mis.account.entity.OutIntoWhsAdjSnglSubTab;
import com.px.mis.account.service.OutIntoWhsAdjSnglService;
import com.px.mis.account.service.OutIntoWhsAdjSnglSubTabService;
import com.px.mis.purc.dao.RecvSendCateDao;
import com.px.mis.purc.entity.PursOrdrSub;
import com.px.mis.util.BaseJson;
import com.px.mis.util.GetOrderNo;
import com.px.mis.util.JacksonUtil;
import com.px.mis.util.poiTool;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//��������������ʵ����
@Transactional
@Service
public class OutIntoWhsAdjSnglServiceImpl extends poiTool implements OutIntoWhsAdjSnglService {
	@Autowired
	private OutIntoWhsAdjSnglDao outIntoWhsAdjSnglDao;
	@Autowired
	private OutIntoWhsAdjSnglSubTabDao outIntoWhsAdjSnglSubTabDao;
	@Autowired
	private RecvSendCateDao RecvSendCateDao;
	// ������
	@Autowired
	private GetOrderNo getOrderNo;
	@Autowired
	private OutIntoWhsAdjSnglSubTabService outIntoWhsAdjSnglSubTabService;

	@Override
	public ObjectNode insertOutIntoWhsAdjSngl(String userId, OutIntoWhsAdjSngl outIntoWhsAdjSngl, String loginTime) {
		ObjectNode on = null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		// ��ȡ������
		String number = getOrderNo.getSeqNo("TZ", userId, loginTime);
		if (number == null) {
			on.put("isSuccess", false);
			on.put("message", "��������������ʧ��,��������Ϊ��");
		} else if (outIntoWhsAdjSnglDao.selectOutIntoWhsAdjSnglByFormNum(number) != null) {
			on.put("isSuccess", false);
			on.put("message", "�������������ݺ�" + number + "�Ѵ��ڣ�����ʧ�ܣ�");
		} else {
			outIntoWhsAdjSngl.setFormNum(number);
			int outIntoResult = outIntoWhsAdjSnglDao.insertOutIntoWhsAdjSngl(outIntoWhsAdjSngl);
			if (outIntoResult == 1) {
				List<OutIntoWhsAdjSnglSubTab> subTabList = outIntoWhsAdjSngl.getOutIntoList();
				for (OutIntoWhsAdjSnglSubTab subTab : subTabList) {
					subTab.setFormNum(number);
					on = outIntoWhsAdjSnglSubTabService.insertOutIntoWhsAdjSnglSubTab(subTab);
				}
			} else {
				on.put("isSuccess", false);
				on.put("message", "����������������ʧ��");
			}
		}
		return on;
	}

	@Override
	public ObjectNode updateOutIntoWhsAdjSnglByFormNum(OutIntoWhsAdjSngl outIntoWhsAdjSngl) {
		ObjectNode on = null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		String formNum = outIntoWhsAdjSngl.getFormNum();
		if (formNum == null) {
			on.put("isSuccess", false);
			on.put("message", "���������������޸�ʧ��,��������Ϊ��");
		} else if (outIntoWhsAdjSnglDao.selectOutIntoWhsAdjSnglByFormNum(formNum) == null) {
			on.put("isSuccess", false);
			on.put("message", "���������������б��" + formNum + "�����ڣ�����ʧ�ܣ�");
		} else if (RecvSendCateDao.selectRecvSendCateByRecvSendCateId(outIntoWhsAdjSngl.getRecvSendCateId()) == null) {
			on.put("isSuccess", false);
			on.put("message", "�����������������շ������" + outIntoWhsAdjSngl.getRecvSendCateId() + "�����ڣ�����ʧ�ܣ�");
		} else {
			int updateResult = outIntoWhsAdjSnglDao.updateOutIntoWhsAdjSnglByFormNum(outIntoWhsAdjSngl);
			int deleteResult = outIntoWhsAdjSnglSubTabService
					.deleteOutIntoWhsAdjSnglSubTabByFormNum(outIntoWhsAdjSngl.getFormNum());
			if (updateResult == 1 && deleteResult >= 1) {
				List<OutIntoWhsAdjSnglSubTab> outIntoList = outIntoWhsAdjSngl.getOutIntoList();
				for (OutIntoWhsAdjSnglSubTab subTab : outIntoList) {
					subTab.setFormNum(formNum);
					outIntoWhsAdjSnglSubTabDao.insertOutIntoWhsAdjSnglSubTab(subTab);
				}
				on.put("isSuccess", true);
				on.put("message", "�޸ĳɹ�");
			} else {
				on.put("isSuccess", false);
				on.put("message", "�޸�ʧ��");
			}
		}
		return on;
	}

	// ����ɾ��
	@Override
	public ObjectNode deleteOutIntoWhsAdjSnglByFormNum(String formNums) {
		ObjectNode on = null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<String> lists = getList(formNums);
		List<String> lists2 = new ArrayList<>();
		List<String> lists3 = new ArrayList<>();
		for (String formNum : lists) {
			if (outIntoWhsAdjSnglDao.selectOutIntoWhsAdjSnglIsNtBookEntry(formNum) == 0) {
				lists2.add(formNum);
			} else {
				lists3.add(formNum);
			}
		}
		if (lists2.size() > 0) {
			int a = outIntoWhsAdjSnglDao.deleteOutIntoWhsAdjSnglList(lists2);// dao�� ����ɾ��
			if (a >= 1) {
				on.put("isSuccess", true);
				on.put("message", "���ݺ�Ϊ��" + lists2.toString() + "�Ķ���ɾ���ɹ�!\n");
			} else {
				on.put("isSuccess", false);
				on.put("message", "���ݺ�Ϊ��" + lists2.toString() + "�Ķ���ɾ��ʧ��!\n");
			}
		}
		if (lists3.size() > 0) {
			on.put("isSuccess", false);
			on.put("message", "���ݺ�Ϊ��" + lists3.toString() + "�Ķ����ѱ���ˣ��޷�ɾ����\n");
		}
		return on;
	}

	/**
	 * id����list
	 * 
	 * @param id id(����Ѷ��ŷָ�)
	 * @return List����
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
	@Transactional(propagation = Propagation.SUPPORTS)
	public OutIntoWhsAdjSngl selectOutIntoWhsAdjSnglByFormNum(String formNum) {
		OutIntoWhsAdjSngl selectOne = outIntoWhsAdjSnglDao.selectOutIntoWhsAdjSnglByFormNum(formNum);
		return selectOne;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public String selectOutIntoWhsAdjSnglList(Map map) {
		String resp = "";
		List<OutIntoWhsAdjSngl> list = outIntoWhsAdjSnglDao.selectOutIntoWhsAdjSnglList(map);
		int count = outIntoWhsAdjSnglDao.selectOutIntoWhsAdjSnglCount(map);
		int listNum = 0;
		if (list != null) {
			listNum = list.size();
		}
		try {
			int pageNo = (int) map.get("pageNo");
			int pageSize = (int) map.get("pageSize");
			int pages = (count + pageSize - 1) / pageSize;
			resp = BaseJson.returnRespList("/account/outIntoWhsAdjSngl/selectOutIntoWhsAdjSngl", true, "��ѯ�ɹ���", count,
					pageNo, pageSize, listNum, pages, list);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public String selectOutIntoWhsAdjSnglPrint(Map map) {
		String resp = "";
		try {
			List<OutIntoWhsAdjSngl> list = outIntoWhsAdjSnglDao.selectOutIntoWhsAdjSnglList(map);
			if(list.size() == 0) {
				OutIntoWhsAdjSngl s = new OutIntoWhsAdjSngl();
				List<OutIntoWhsAdjSnglSubTab> outIntoList = new ArrayList<>();
				OutIntoWhsAdjSnglSubTab ss = new OutIntoWhsAdjSnglSubTab();
				outIntoList.add(ss);
				s.setOutIntoList(outIntoList);
				list.add(s);
			}
			ArrayList<JsonNode> flattenList = flattening(list);
			resp = BaseJson.returnRespList("/account/outIntoWhsAdjSngl/selectOutIntoWhsAdjSnglPrint", true, "��ѯ�ɹ���",
					flattenList);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return resp;
	}

	// ����excel
	@Override
	public String uploadFileAddDb(MultipartFile file) throws Exception {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";

		Map<String, OutIntoWhsAdjSngl> pusOrderMap = uploadScoreInfo(file);

		for (Map.Entry<String, OutIntoWhsAdjSngl> entry : pusOrderMap.entrySet()) {

			if (outIntoWhsAdjSnglDao.selectOutIntoWhsAdjSnglByFormNum(entry.getValue().getFormNum()) != null) {
				isSuccess = false;
				message = "�����ظ����� " + entry.getValue().getFormNum() + " ����";
				throw new RuntimeException("�����ظ����� " + entry.getValue().getFormNum() + " ����");
			} else {
				
				List<OutIntoWhsAdjSnglSubTab> subTabList = entry.getValue().getOutIntoList();
				if(subTabList.size() > 0) {
					outIntoWhsAdjSnglDao.EXinsertOutIntoWhsAdjSngl(entry.getValue());
					for (OutIntoWhsAdjSnglSubTab subTab : subTabList) {
						outIntoWhsAdjSnglSubTabDao.insertOutIntoWhsAdjSnglSubTab(subTab);
						// outIntoWhsAdjSnglSubTabService.insertOutIntoWhsAdjSnglSubTab(subTab);
						isSuccess = true;
						message = "����������ɹ���";
					}
				} else {
					isSuccess = false;
					message = "����ʧ�ܣ������ļ����ݣ�";
				}
				
			}

			
		}


		resp = BaseJson.returnRespObj("account/outIntoWhsAdjSngl/uploadFileAddDb", isSuccess, message, null);

		return resp;

	}

	// ����excel (��װ)
	private Map<String, OutIntoWhsAdjSngl> uploadScoreInfo(MultipartFile file) {
		Map<String, OutIntoWhsAdjSngl> temp = new HashMap<>();
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

				String orderNo = GetCellData(r, "���ݺ�");
				// ����ʵ����
				OutIntoWhsAdjSngl outIntoWhsAdjSngl = new OutIntoWhsAdjSngl();
				if (temp.containsKey(orderNo)) {
					outIntoWhsAdjSngl = temp.get(orderNo);
				}
				List<OutIntoWhsAdjSnglSubTab> snglSubTabs = outIntoWhsAdjSngl.getOutIntoList();// �����ӱ�
				if (snglSubTabs == null) {
					snglSubTabs = new ArrayList<>();
				}
				OutIntoWhsAdjSnglSubTab sub = new OutIntoWhsAdjSnglSubTab();
				sub.setInvtyEncd(GetCellData(r, "�������"));
				sub.setInvtyNm(GetCellData(r, "�������"));
				sub.setWhsEncd(GetCellData(r, "�ֿ����"));
				sub.setWhsNm(GetCellData(r, "�ֿ�����"));
				sub.setBatNum(GetCellData(r, "����"));
				sub.setSpcModel(GetCellData(r, "����ͺ�"));
				sub.setBxRule(GetBigDecimal(GetCellData(r, "���"), 8));
				sub.setAmt(GetBigDecimal(GetCellData(r, "���"), 8));
				sub.setMeasrCorpNm(GetCellData(r, "������λ����"));
				if(StringUtils.isNotEmpty(GetCellData(r, "��Դ�ӱ����"))) {
					sub.setToOrdrNum(Long.valueOf(GetCellData(r, "��Դ�ӱ����")));
				}
				sub.setProjEncd(GetCellData(r, "��Ŀ����"));
				sub.setProjNm(GetCellData(r, "��Ŀ����"));
				sub.setFormNum(orderNo);
				snglSubTabs.add(sub);
				outIntoWhsAdjSngl.setOutIntoList(snglSubTabs);
				

			
				// ȡ����ǰ�е�1����Ԫ�����ݣ�����װ��pursOrdrʵ��ĸ���������
				outIntoWhsAdjSngl.setFormNum(orderNo); // ���ݺ�
				// ��������
				if (GetCellData(r, "��������") == null) {
					outIntoWhsAdjSngl.setFormTm(null); // ��������
				} else {
					outIntoWhsAdjSngl.setFormTm(GetCellData(r, "��������").replaceAll("[^0-9:-]", " ").length() == 0 ? null
							: GetCellData(r, "��������").replaceAll("[^0-9:-]", " "));
				}
				outIntoWhsAdjSngl.setOutIntoList(snglSubTabs);
				// �շ�������
				outIntoWhsAdjSngl.setRecvSendCateId(GetCellData(r, "�շ�������"));
				// �û�����
				outIntoWhsAdjSngl.setAccNum(GetCellData(r, "�û�����"));
				// �û�����
				outIntoWhsAdjSngl.setUserName(GetCellData(r, "�û�����"));
				// ���ű���
				outIntoWhsAdjSngl.setDeptId(GetCellData(r, "���ű���"));
				// �ͻ�����
				outIntoWhsAdjSngl.setCustId(GetCellData(r, "�ͻ�����"));
				// ��Ӧ�̱���
				outIntoWhsAdjSngl.setProvrId(GetCellData(r, "��Ӧ�̱���"));
				// �����
				outIntoWhsAdjSngl.setIsFifoAdjBan(
						(new Double(GetCellData(r, "�����") == null || GetCellData(r, "�����").equals("") ? "0"
								: GetCellData(r, "�����"))).intValue());
				// �Ƿ����
				outIntoWhsAdjSngl.setIsCrspdOutWhsSngl(
						(new Double(GetCellData(r, "�Ƿ����") == null || GetCellData(r, "�Ƿ����").equals("") ? "0"
								: GetCellData(r, "�Ƿ����"))).intValue());
				// ������
				outIntoWhsAdjSngl.setBookEntryTm(GetCellData(r, "������"));

				// ����ʱ��
				if (GetCellData(r, "����ʱ��") == null) {
					outIntoWhsAdjSngl.setBookEntryTm(null);
				} else {
					outIntoWhsAdjSngl
							.setBookEntryTm(GetCellData(r, "����ʱ��").replaceAll("[^0-9:-]", " ").length() == 0 ? null
									: GetCellData(r, "����ʱ��").replaceAll("[^0-9:-]", " ")); // ����ʱ��
				}
				// �޸���
				outIntoWhsAdjSngl.setMdfrPers(GetCellData(r, "�޸���"));
				// �޸�ʱ��
				if (GetCellData(r, "�޸�ʱ��") == null) {
					outIntoWhsAdjSngl.setBookEntryTm(null);
				} else {
					outIntoWhsAdjSngl
							.setBookEntryTm(GetCellData(r, "�޸�ʱ��").replaceAll("[^0-9:-]", " ").length() == 0 ? null
									: GetCellData(r, "�޸�ʱ��").replaceAll("[^0-9:-]", " ")); // �޸�ʱ��
				}
				// ������
				outIntoWhsAdjSngl.setSetupPers(GetCellData(r, "������"));
				// ����ʱ��
				if (GetCellData(r, "����ʱ��") == null) {
					outIntoWhsAdjSngl.setBookEntryTm(null);
				} else {
					outIntoWhsAdjSngl
							.setBookEntryTm(GetCellData(r, "����ʱ��").replaceAll("[^0-9:-]", " ").length() == 0 ? null
									: GetCellData(r, "����ʱ��").replaceAll("[^0-9:-]", " ")); // ����ʱ��
				}
				// ��ͷ��ע
				outIntoWhsAdjSngl.setMemo(GetCellData(r, "��ͷ��ע"));
				// �Ƿ��Ƚ��ȳ�
				outIntoWhsAdjSngl.setIsFifoAdjBan(
						GetCellData(r, "�Ƿ��Ƚ��ȳ�") == null || GetCellData(r, "�Ƿ��Ƚ��ȳ�").equals("") ? 0 : 1);
				// �Ƿ��Ӧ���ⵥ
				outIntoWhsAdjSngl.setIsCrspdOutWhsSngl(
						GetCellData(r, "�Ƿ��Ӧ���ⵥ") == null || GetCellData(r, "�Ƿ��Ӧ���ⵥ").equals("") ? 0 : 1);
				// ���������ݺ�
				outIntoWhsAdjSngl.setBeadjOutIntoWhsMastabInd(GetCellData(r, "���������ݺ�"));
				// �Ƿ�����
				outIntoWhsAdjSngl
						.setIsNtSell(GetCellData(r, "�Ƿ�����") == null || GetCellData(r, "�Ƿ�����").equals("") ? 0 : 1);
				// �շ����
				outIntoWhsAdjSngl.setRecvSendCateNm(GetCellData(r, "�շ����"));
				// ��������
				outIntoWhsAdjSngl.setDeptNm(GetCellData(r, "��������"));
				// �ͻ�����
				outIntoWhsAdjSngl.setCustNm(GetCellData(r, "�ͻ�����"));
				// ��Ӧ��
				outIntoWhsAdjSngl.setProvrNm(GetCellData(r, "��Ӧ��"));
				// ��Դ�������ͱ���
				outIntoWhsAdjSngl.setToFormTypEncd(GetCellData(r, "��Դ�������ͱ���"));
				// �Ƿ�����ƾ֤
				outIntoWhsAdjSngl.setIsNtMakeVouch(
						GetCellData(r, "�Ƿ�����ƾ֤") == null || GetCellData(r, "�Ƿ�����ƾ֤").equals("") ? 0 : 1);
				// ��ƾ֤��
				outIntoWhsAdjSngl.setMakVouchPers(GetCellData(r, "��ƾ֤��"));
				// ��ƾ֤ʱ��
				if (GetCellData(r, "��ƾ֤ʱ��") == null) {
					outIntoWhsAdjSngl.setMakVouchTm(null);
				} else {
					outIntoWhsAdjSngl
							.setBookEntryTm(GetCellData(r, "��ƾ֤ʱ��").replaceAll("[^0-9:-]", " ").length() == 0 ? null
									: GetCellData(r, "��ƾ֤ʱ��").replaceAll("[^0-9:-]", " ")); // ��ƾ֤ʱ��
				}
				// �������ͱ���
				outIntoWhsAdjSngl.setFormTypEncd(GetCellData(r, "�������ͱ���"));
				// ������������
				outIntoWhsAdjSngl.setFormTypName(GetCellData(r, "������������"));
				// �򼯺�����Ӷ���
				temp.put(orderNo, outIntoWhsAdjSngl);
			}
			// �ر�������
			fileIn.close();
		} catch (Exception e) {

			e.printStackTrace();

			throw new RuntimeException("������ʽ�����" + j + "��" + e.getMessage());
		}
		return temp;
	}

	private <T> ArrayList<JsonNode> flattening(List<T> itList) throws IOException {
		try {
			ArrayList<JsonNode> nodeList = new ArrayList<JsonNode>();
			JacksonUtil.turnOnAnno();
			for (int i = 0; i < itList.size(); i++) {
				OutIntoWhsAdjSngl AdjSngl = (OutIntoWhsAdjSngl) itList.get(i);
				List<OutIntoWhsAdjSnglSubTab> outIntoList = AdjSngl.getOutIntoList();

				for (OutIntoWhsAdjSnglSubTab item : outIntoList) {
					AdjSngl.setOutIntoList(null);
					ObjectNode bond = JacksonUtil.getObjectNode("");
					ObjectNode mainNode = JacksonUtil.getObjectNode(AdjSngl);
					ObjectNode subNode = JacksonUtil.getObjectNode(item);
					bond.setAll(mainNode);
					bond.setAll(subNode);
					nodeList.add(bond);
				}
			}
			return nodeList;
		} finally {
			JacksonUtil.turnOffAnno();
		}
	}

}
