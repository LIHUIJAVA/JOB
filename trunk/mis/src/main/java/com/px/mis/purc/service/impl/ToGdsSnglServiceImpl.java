package com.px.mis.purc.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;
import com.px.mis.purc.dao.IntoWhsDao;
import com.px.mis.purc.dao.InvtyDocDao;
import com.px.mis.purc.dao.InvtyTabDao;
import com.px.mis.purc.dao.PursOrdrSubDao;
import com.px.mis.purc.dao.ToGdsSnglDao;
import com.px.mis.purc.dao.ToGdsSnglSubDao;
import com.px.mis.purc.entity.IntoWhs;
import com.px.mis.purc.entity.InvtyDoc;
import com.px.mis.purc.entity.PursOrdr;
import com.px.mis.purc.entity.PursOrdrSub;
import com.px.mis.purc.entity.ToGdsSngl;
import com.px.mis.purc.entity.ToGdsSnglSub;
import com.px.mis.purc.service.ToGdsSnglService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.GetOrderNo;
import com.px.mis.util.poiTool;
import com.px.mis.whs.entity.InvtyTab;

/*����������*/
@Transactional
@Service
public class ToGdsSnglServiceImpl extends poiTool implements ToGdsSnglService {
	@Autowired
	private ToGdsSnglDao tgsd;
	@Autowired
	private ToGdsSnglSubDao tgssd;
	@Autowired
	private InvtyTabDao itd;
	@Autowired
	private IntoWhsDao iwd;
	@Autowired
	private PursOrdrSubDao pursOrdrSubDao;
	@Autowired
	private ToGdsSnglSubDao toGdsSnglSubDao;
	@Autowired
	private InvtyDocDao invtyDocDao;
	// ������
	@Autowired
	private GetOrderNo getOrderNo;
		
	// ������������Ϣ
	@Override
	public String addToGdsSngl(String userId, ToGdsSngl toGdsSngl, List<ToGdsSnglSub> toGdsSnglSubList,String loginTime) throws Exception{
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
//			TreeSet<ToGdsSnglSub> toGdsSnglSubSet = new TreeSet();
//			toGdsSnglSubSet.addAll(toGdsSnglSubList);
//			if (toGdsSnglSubSet.size() < toGdsSnglSubList.size()) {
//				resp = BaseJson.returnRespObj("purc/ToGdsSngl/addToGdsSngl", false, "��������ϸ������ͬһ�ֿ��д���ͬһ�������ͬ���Σ�", null);
//				return resp;
//			}
			// ��ȡ������
			String number = getOrderNo.getSeqNo("DH", userId,loginTime);
			if (tgsd.selectToGdsSnglById(number) != null) {
				message = "����" + number + "�Ѵ��ڣ����������룡";
				isSuccess = false;
			} else {
				toGdsSngl.setToGdsSnglId(number);
				for (ToGdsSnglSub toGdsSnglSub : toGdsSnglSubList) {
					toGdsSnglSub.setToGdsSnglId(toGdsSngl.getToGdsSnglId());
					toGdsSnglSub.setUnIntoWhsQty(toGdsSnglSub.getQty().abs());// δ�������
					if (toGdsSngl.getIsNtRtnGood() == 0) {
						toGdsSnglSub.setReturnQty(toGdsSnglSub.getQty());// δ��������
					}
					InvtyDoc invtyDoc = invtyDocDao.selectAllByInvtyEncd(toGdsSnglSub.getInvtyEncd());
					if (invtyDoc.getIsNtPurs() == null) {
						isSuccess = false;
						message = "�õ�������Ӧ�Ĵ��:" + toGdsSnglSub.getInvtyEncd()
								+ "û�������Ƿ�ɹ����ԣ��޷����棡";
						throw new RuntimeException(message);
					} else if (invtyDoc.getIsNtPurs().intValue() != 1) {
						isSuccess = false;
						message = "�õ�������Ӧ�Ĵ��:" + toGdsSnglSub.getInvtyEncd()
								+ "�ǿɲɹ�������޷����棡";
						throw new RuntimeException(message);
					}
					if (invtyDoc.getIsQuaGuaPer() == null) {
						isSuccess = false;
						message = "�õ�������Ӧ�Ĵ��:" + toGdsSnglSub.getInvtyEncd()
								+ "û�������Ƿ����ڹ������ԣ��޷����棡";
						throw new RuntimeException(message);
					} else if(invtyDoc.getIsQuaGuaPer() == 1){
						if (toGdsSnglSub.getPrdcDt() == "" || toGdsSnglSub.getPrdcDt() == null) {
							isSuccess = false;
							message = "�õ�������Ӧ�Ĵ��:" + toGdsSnglSub.getInvtyEncd()
									+ "�Ǳ����ڹ����������������ڣ�";
							throw new RuntimeException(message);
						}
						if (toGdsSnglSub.getInvldtnDt() == ""|| toGdsSnglSub.getInvldtnDt() == null) {
							isSuccess = false;
							message = "�õ�������Ӧ�Ĵ��:" + toGdsSnglSub.getInvtyEncd()
									+ "�Ǳ����ڹ���������ʧЧ���ڣ�";
							throw new RuntimeException(message);
						}
					}
					if (toGdsSnglSub.getPrdcDt() == "") {
						toGdsSnglSub.setPrdcDt(null);
					}
					if (toGdsSnglSub.getInvldtnDt() == "") {
						toGdsSnglSub.setInvldtnDt(null);
					}
				}
				int a  = tgsd.insertToGdsSngl(toGdsSngl);
				int b = tgssd.insertToGdsSnglSub(toGdsSnglSubList);
				if(a>=1 && b>=1) {
					message = "�����ɹ���";
					isSuccess = true;
				}else {
					isSuccess = false;
					message = "����ʧ�ܣ�";
					throw new RuntimeException(message);
				}
			}
			resp = BaseJson.returnRespObj("purc/ToGdsSngl/addToGdsSngl", isSuccess, message, toGdsSngl);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e.getMessage());
		}
		return resp;
	}

	// �޸ĵ�������Ϣ
	@Override
	public String editToGdsSngl(ToGdsSngl toGdsSngl, List<ToGdsSnglSub> toGdsSnglSubList) throws Exception {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
//			TreeSet<ToGdsSnglSub> toGdsSnglSubSet = new TreeSet();
//			toGdsSnglSubSet.addAll(toGdsSnglSubList);
//			if (toGdsSnglSubSet.size() < toGdsSnglSubList.size()) {
//				resp = BaseJson.returnRespObj("purc/ToGdsSngl/editToGdsSngl", false, "��������ϸ������ͬһ�ֿ��д���ͬһ�������ͬ���Σ�",
//						null);
//				return resp;
//			}
			for (ToGdsSnglSub toGdsSnglSub : toGdsSnglSubList) {
				toGdsSnglSub.setToGdsSnglId(toGdsSngl.getToGdsSnglId());
				toGdsSnglSub.setUnIntoWhsQty(toGdsSnglSub.getQty().abs());// δ�������
				if (toGdsSngl.getIsNtRtnGood() == 0) {
					toGdsSnglSub.setReturnQty(toGdsSnglSub.getQty());// δ��������
				}
				InvtyDoc invtyDoc = invtyDocDao.selectAllByInvtyEncd(toGdsSnglSub.getInvtyEncd());
				if (invtyDoc.getIsNtPurs() == null || invtyDoc.getIsNtPurs().equals("")) {
					isSuccess = false;
					message = "�õ�������Ӧ�Ĵ��:" + toGdsSnglSub.getInvtyEncd()
							+ "û�������Ƿ�ɹ����ԣ��޷����棡";
					throw new RuntimeException(message);
				} else if (invtyDoc.getIsNtPurs().intValue() != 1) {
					isSuccess = false;
					message = "�õ�������Ӧ�Ĵ��:" + toGdsSnglSub.getInvtyEncd()
							+ "�ǿɲɹ�������޷����棡";
					throw new RuntimeException(message);
				}
				if (invtyDoc.getIsQuaGuaPer() == null) {
					isSuccess = false;
					message = "�õ�������Ӧ�Ĵ��:" + toGdsSnglSub.getInvtyEncd()
							+ "û�������Ƿ����ڹ������ԣ��޷����棡";
					throw new RuntimeException(message);
				} else if(invtyDoc.getIsQuaGuaPer() == 1){
					if (toGdsSnglSub.getPrdcDt() == "" || toGdsSnglSub.getPrdcDt() == null) {
						isSuccess = false;
						message = "�õ�������Ӧ�Ĵ��:" + toGdsSnglSub.getInvtyEncd()
								+ "�Ǳ����ڹ����������������ڣ�";
						throw new RuntimeException(message);
					}
					if (toGdsSnglSub.getInvldtnDt() == ""|| toGdsSnglSub.getInvldtnDt() == null) {
						isSuccess = false;
						message = "�õ�������Ӧ�Ĵ��:" + toGdsSnglSub.getInvtyEncd()
								+ "�Ǳ����ڹ���������ʧЧ���ڣ�";
						throw new RuntimeException(message);
					}
				}
				if (toGdsSnglSub.getPrdcDt() == "") {
					toGdsSnglSub.setPrdcDt(null);
				}
				if (toGdsSnglSub.getInvldtnDt() == "") {
					toGdsSnglSub.setInvldtnDt(null);
				}
			}
			int a = tgssd.deleteToGdsSnglSubByToGdsSnglId(toGdsSngl.getToGdsSnglId());// ɾ��������������Ϣ
			int b = tgsd.updateToGdsSnglByToGdsSnglId(toGdsSngl);// �޸ĵ�����������Ϣ
			int c = tgssd.insertToGdsSnglSub(toGdsSnglSubList);// �����������ӱ���Ϣ
			if(a>=1 && b>=1 && c>=1) {
				message = "���³ɹ���";
				isSuccess = true;
			}else {
				isSuccess = false;
				message = "����ʧ�ܣ�";
				throw new RuntimeException(message);
			}
			resp = BaseJson.returnRespObj("purc/ToGdsSngl/editToGdsSngl", isSuccess, message, null);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
		return resp;
	}

	// ɾ����������Ϣ
	@Override
	public String deleteToGdsSngl(String toGdsSnglId) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		if (tgsd.selectToGdsSnglByToGdsSnglId(toGdsSnglId) != null) {
			tgsd.deleteToGdsSnglByToGdsSnglId(toGdsSnglId);
			tgssd.deleteToGdsSnglSubByToGdsSnglId(toGdsSnglId);
			isSuccess = true;
			message = "ɾ���ɹ���";
		} else {
			isSuccess = false;
			message = "����" + toGdsSnglId + "�����ڣ�";
		}
		try {
			resp = BaseJson.returnRespObj("purc/ToGdsSngl/deleteToGdsSngl", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// ���ݵ����������ѯ��������Ϣ
	@Override
	public String queryToGdsSngl(String toGdsSnglId) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		/* List<ToGdsSnglSub> toGdsSnglSub = new ArrayList<>(); */
		ToGdsSngl toGdsSngl = tgsd.selectToGdsSnglByToGdsSnglId(toGdsSnglId);
		if (toGdsSngl != null) {
			/* toGdsSnglSub=tgssd.selectToGdsSnglSubByToGdsSnglId(toGdsSnglId); */
			message = "��ѯ�ɹ���";
		} else {
			isSuccess = false;
			message = "����" + toGdsSnglId + "�����ڣ�";
		}
		try {
			resp = BaseJson.returnRespObj("purc/ToGdsSngl/queryToGdsSngl", isSuccess, message, toGdsSngl);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// ��ҳ��ѯ���е�������Ϣ
	@Override
	public String queryToGdsSnglList(Map map) {
		String resp = "";
		List<String> toGdsSnglIdList = getList((String) map.get("toGdsSnglId"));// ��������
		List<String> provrIdList = getList((String) map.get("provrId"));// �ͻ�����
		List<String> accNumList = getList((String) map.get("accNum"));// ҵ��Ա����
		List<String> invtyClsEncdList = getList((String) map.get("invtyClsEncd"));// ����������
		List<String> invtyCdList = getList((String) map.get("invtyCd"));// �������
		List<String> deptIdList = getList((String) map.get("deptId"));// ���ű���
		List<String> provrOrdrNumList = getList((String) map.get("provrOrdrNum"));// ��Ӧ�̶�����
		List<String> invtyEncdList = getList((String) map.get("invtyEncd"));// �������
		List<String> whsEncdList = getList((String) map.get("whsEncd"));// �ֿ����
		List<String> batNumList = getList((String) map.get("batNum"));// ����
		List<String> intlBatList = getList((String) map.get("intlBat"));// ��������
		List<String> pursOrdrIdList = getList((String) map.get("pursOrdrId"));// �ɹ���������

		map.put("toGdsSnglIdList", toGdsSnglIdList);
		map.put("provrIdList", provrIdList);
		map.put("accNumList", accNumList);
		map.put("invtyClsEncdList", invtyClsEncdList);
		map.put("invtyCdList", invtyCdList);
		map.put("deptIdList", deptIdList);
		map.put("provrOrdrNumList", provrOrdrNumList);
		map.put("invtyEncdList", invtyEncdList);
		map.put("whsEncdList", whsEncdList);
		map.put("batNumList", batNumList);
		map.put("intlBatList", intlBatList);
		map.put("pursOrdrIdList", pursOrdrIdList);
		List<ToGdsSngl> poList = tgsd.selectToGdsSnglList(map);
		int count = tgsd.selectToGdsSnglCount(map);

		int pageNo = Integer.parseInt(map.get("pageNo").toString());
		int pageSize = Integer.parseInt(map.get("pageSize").toString());
		int listNum = poList.size();
		int pages = count / pageSize + 1;

		try {
			resp = BaseJson.returnRespList("purc/ToGdsSngl/queryToGdsSnglList", true, "��ѯ�ɹ���", count, pageNo, pageSize,
					listNum, pages, poList);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// ����ɾ���������б�
	@Override
	public String deleteToGdsSnglList(String toGdsSnglId) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			List<String> lists = getList(toGdsSnglId);
			List<String> lists2 = new ArrayList<>();
			List<String> lists3 = new ArrayList<>();
			for (String list : lists) {
				if (tgsd.selectToGdsSnglIsNtChk(list) == 0) {
					lists2.add(list);
				} else {
					lists3.add(list);
				}
			}
			if (lists2.size() > 0) {
				int a = 0;
				try {
					a = deleteToGdsSnglList(lists2);
				} catch (Exception e) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					isSuccess = false;
					message += "���ݺ�Ϊ��" + lists2.toString() + "ɾ��ʧ�ܣ�\n";
				}
				if (a >= 1) {
					isSuccess = true;
					message += "���ݺ�Ϊ��" + lists2.toString() + "�Ķ���ɾ���ɹ�!\n";
				} else {
					isSuccess = false;
					message += "���ݺ�Ϊ��" + lists2.toString() + "ɾ��ʧ�ܣ�\n";
				}
			}
			if (lists3.size() > 0) {
				isSuccess = false;
				message += "���ݺ�Ϊ��" + lists3.toString() + "�Ķ����ѱ���ˣ��޷�ɾ����\n";
			}
			resp = BaseJson.returnRespObj("purc/ToGdsSngl/deleteToGdsSnglList", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	@Transactional
	public int deleteToGdsSnglList(List<String> lists2) {
			tgsd.insertToGdsSnglDl(lists2);
			tgssd.insertToGdsSnglSubDl(lists2);
		return tgsd.deleteToGdsSnglList(lists2);
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

	// ��˹���
	@Override
	public Map<String, Object> updateToGdsSnglIsNtChkList(ToGdsSngl tGdsSngl) throws Exception {
		StringBuilder message = new StringBuilder();
		Boolean isSuccess = true;
		Map<String, Object> map = new HashMap<>();
		try {
			ToGdsSngl toGdsSngl = tgsd.selectToGdsSnglByToGdsSnglId(tGdsSngl.getToGdsSnglId());
			if (toGdsSngl.getIsNtRtnGood() == 0) {
				if (tGdsSngl.getIsNtChk() == 1) {
					message.append(updateToGdsSnglIsNtChkOK(tGdsSngl).get("message"));
				} else if (tGdsSngl.getIsNtChk() == 0) {
					message.append(updateToGdsSnglIsNtChkNO(tGdsSngl).get("message"));
				} else {
					isSuccess = false;
					message.append("���״̬�����޷���ˣ�\n");
				}
			} else if (toGdsSngl.getIsNtRtnGood() == 1) {
				if (tGdsSngl.getIsNtChk() == 1) {
					message.append(updateReturnToGdsSnglIsNtChkOK(tGdsSngl).get("message"));
				} else if (tGdsSngl.getIsNtChk() == 0) {
					message.append(updateReturnToGdsSnglIsNtChkNO(tGdsSngl).get("message"));
				} else {
					isSuccess = false;
					message.append("���״̬�����޷���ˣ�\n");
				}
			}
			map.put("isSuccess", isSuccess);
			map.put("message", message.toString());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return map;
	}

	// ���������
	public Map<String, Object> updateToGdsSnglIsNtChkOK(ToGdsSngl tGdsSngl) {
		String message = "";
		Boolean isSuccess = true;
		Map<String, Object> map = new HashMap<>();
		if (tgsd.selectToGdsSnglIsNtChk(tGdsSngl.getToGdsSnglId()) == 0) {
			ToGdsSngl toGdsSngl = tgsd.selectToGdsSnglByToGdsSnglId(tGdsSngl.getToGdsSnglId());// ����������
			if (toGdsSngl.getPursOrdrId() != null) {
				List<ToGdsSnglSub> toGdsSnglSub = toGdsSngl.getToGdsSnglSub();// ��õ������ӱ� 
				for (ToGdsSnglSub toGdsSub : toGdsSnglSub) {
					// ���ʵ��
//					InvtyTab invtyTab = new InvtyTab();
//					invtyTab.setWhsEncd(toGdsSub.getWhsEncd());
//					invtyTab.setInvtyEncd(toGdsSub.getInvtyEncd());
//					invtyTab.setBatNum(toGdsSub.getBatNum());
//					InvtyTab invtyTab1 = itd.selectInvtyTabsByTerm(invtyTab);
//					if (invtyTab1 == null) {
//						itd.insertInvtyTabByToGdsSngl(toGdsSub);
//					} else {
//						invtyTab1.setAvalQty(toGdsSub.getQty());
//						itd.updateInvtyTabAvalQtyJia(invtyTab1);
//					}
					if (toGdsSub.getToOrdrNum() != null && toGdsSub.getToOrdrNum() != 0) {
						map.put("ordrNum", toGdsSub.getToOrdrNum());// ���
						BigDecimal unToGdsQty = pursOrdrSubDao.selectUnToGdsQtyByInvtyEncd(map);// ���ݴ������Ͳɹ����������ѯδ��������
						if (unToGdsQty != null) {
							if (unToGdsQty.compareTo(toGdsSub.getQty()) == 1
									|| unToGdsQty.compareTo(toGdsSub.getQty()) == 0) {
								map.put("unToGdsQty", toGdsSub.getQty());// �޸�δ��������
								pursOrdrSubDao.updatePursOrdrSubByInvtyEncd(map);// ���ݴ�������޸Ĳɹ������е�δ��������
							} else {
								isSuccess = false;
								message += "���ݺ�Ϊ��" + tGdsSngl.getToGdsSnglId() + "�ĵ������д����" + toGdsSub.getInvtyEncd()
										+ "���ۼƵ����������ڶ����������޷���ˣ�\n";
								throw new RuntimeException(message);
							}
						}
					}
				}
				int a = tgsd.updateToGdsSnglIsNtChk(tGdsSngl);
				if (a >= 1) {
					isSuccess = true;
					message += "���ݺ�Ϊ��" + tGdsSngl.getToGdsSnglId() + "�ĵ�������˳ɹ���\n";
				} else {
					isSuccess = false;
					message += "���ݺ�Ϊ��" + tGdsSngl.getToGdsSnglId() + "�ĵ��������ʧ�ܣ�\n";
					throw new RuntimeException(message);
				}
			} else {
				isSuccess = false;
				message += "���ݺ�Ϊ��" + tGdsSngl.getToGdsSnglId() + "�ĵ�������Ӧ�Ĳɹ����������ڣ��޷����\n";
			}
		} else {
			isSuccess = false;
			message += "���ݺ�Ϊ��" + tGdsSngl.getToGdsSnglId() + "�ĵ���������ˣ�����Ҫ�ظ����\n";
		}
		map.put("isSuccess", isSuccess);
		map.put("message", message);
		return map;
	}

	// ����������
	public Map<String, Object> updateToGdsSnglIsNtChkNO(ToGdsSngl tGdsSngl) {
		String message = "";
		Boolean isSuccess = true;
		Map<String, Object> map = new HashMap<>();
		// �������ӱ���
		List<ToGdsSnglSub> toGdsSnglSub = new ArrayList<>();
		if (tgsd.selectToGdsSnglIsNtChk(tGdsSngl.getToGdsSnglId()) == 1) {
			List<IntoWhs> intoWhsList = iwd.selectIntoWhsByToGdsSnglId(tGdsSngl.getToGdsSnglId());
			ToGdsSngl toGdsSngl = tgsd.selectToGdsSnglByToGdsSnglId(tGdsSngl.getToGdsSnglId());// ������۵�����
			toGdsSnglSub = toGdsSngl.getToGdsSnglSub();// ������۵��ӱ�
			// �жϲɹ��������û�и��ݸõ�����������ⵥ�����ж����״̬
			if (intoWhsList.size() > 0) {
				isSuccess = false;
				message += "���ݺ�Ϊ��" + tGdsSngl.getToGdsSnglId() + "�ĵ������Ѵ������ε��ݡ��ɹ���ⵥ�����޷�����\n";
			} else {
				for (ToGdsSnglSub toGdsSub : toGdsSnglSub) {
					// ���ʵ��
//					InvtyTab invtyTab = new InvtyTab();
//					invtyTab.setWhsEncd(toGdsSub.getWhsEncd());
//					invtyTab.setInvtyEncd(toGdsSub.getInvtyEncd());
//					invtyTab.setBatNum(toGdsSub.getBatNum());
//					invtyTab = itd.selectInvtyTabsByTerm(invtyTab);
//					if (invtyTab.getAvalQty().compareTo(toGdsSub.getQty()) == 1
//							|| invtyTab.getAvalQty().compareTo(toGdsSub.getQty()) == 0) {
//						invtyTab.setAvalQty(toGdsSub.getQty());
//						itd.updateInvtyTabAvalQtyJian(invtyTab);
//					} else if (invtyTab.getAvalQty().compareTo(toGdsSub.getQty()) == -1) {
//						isSuccess = false;
//						message += "���ݺ�Ϊ��" + tGdsSngl.getToGdsSnglId() + "�ĵ�������,�ֿ�:"+ toGdsSub.getWhsEncd()+ "�����" + toGdsSub.getInvtyEncd()
//								+ "�����Σ�" + toGdsSub.getBatNum() + "�Ŀ�����������㣬�޷�����\n";
//						throw new RuntimeException(message);
//					}
					if (toGdsSub.getToOrdrNum() != null && toGdsSub.getToOrdrNum() != 0) {
						map.put("ordrNum", toGdsSub.getToOrdrNum());// ���
						BigDecimal unToGdsQty = pursOrdrSubDao.selectUnToGdsQtyByInvtyEncd(map);// ���ݴ������Ͳɹ����������ѯδ��������
						if (unToGdsQty != null) {
							map.put("unToGdsQty", toGdsSub.getQty().multiply(new BigDecimal(-1)));
							pursOrdrSubDao.updatePursOrdrSubByInvtyEncd(map);// ��������޸Ĳɹ������е�δ��������
						} else {
							isSuccess = false;
							message += "���ݺ�Ϊ��" + tGdsSngl.getToGdsSnglId() + "�ĵ�������Ӧ�Ĳɹ�������δ�������������ڣ��޷�����\n";
							throw new RuntimeException(message);
						}
					}
				}
				int a = tgsd.updateToGdsSnglIsNtChk(tGdsSngl);
				if (a >= 1) {
					isSuccess = true;
					message += "���ݺ�Ϊ��" + tGdsSngl.getToGdsSnglId() + "�ĵ���������ɹ���\n";
				} else {
					isSuccess = false;
					message += "���ݺ�Ϊ��" + tGdsSngl.getToGdsSnglId() + "�ĵ���������ʧ�ܣ�\n";
					throw new RuntimeException(message);//ͨ�����״̬�ж������Ϸ���
				}
			}
		} else {
			isSuccess = false;
			message += tGdsSngl.getToGdsSnglId() + "����δ��ˣ�������˸õ���\n";
		}
		map.put("isSuccess", isSuccess);
		map.put("message", message);
		return map;
	}

	// �������յ����
	public Map<String, Object> updateReturnToGdsSnglIsNtChkOK(ToGdsSngl tGdsSngl) {
		String message = "";
		Boolean isSuccess = true;
		Map<String, Object> map = new HashMap<>();
		if (tgsd.selectToGdsSnglIsNtChk(tGdsSngl.getToGdsSnglId()) == 0) {
			// �������ӱ���
			ToGdsSngl toGdsSngl = tgsd.selectToGdsSnglByToGdsSnglId(tGdsSngl.getToGdsSnglId());// ��õ���������
			List<ToGdsSnglSub> toGdsSnglSub = toGdsSngl.getToGdsSnglSub();// ������������ӱ�
			for (ToGdsSnglSub toGdsSub : toGdsSnglSub) {
				// ���ʵ��
//				InvtyTab invtyTab = new InvtyTab();
//				invtyTab.setWhsEncd(toGdsSub.getWhsEncd());
//				invtyTab.setInvtyEncd(toGdsSub.getInvtyEncd());
//				invtyTab.setBatNum(toGdsSub.getBatNum());
//				invtyTab = itd.selectInvtyTabsByTerm(invtyTab);
//				if (invtyTab == null) {
//					isSuccess = false;
//					message += "���ݺ�Ϊ:" + tGdsSngl.getToGdsSnglId() + "�ĵ������յ����ʧ�ܣ�����û�иõ��ݶ�Ӧ�Ĵ����Ϣ���������еĴ���Ƿ���ڣ�\n";
//					throw new RuntimeException(message);
//				} else {
//					if (invtyTab.getAvalQty().compareTo(toGdsSub.getQty().abs()) == 1
//							|| invtyTab.getAvalQty().compareTo(toGdsSub.getQty().abs()) == 0) {
//						invtyTab.setAvalQty(toGdsSub.getQty());
//						itd.updateInvtyTabAvalQtyJia(invtyTab);
//					} else if (invtyTab.getAvalQty().compareTo(toGdsSub.getQty().abs()) == -1) {
//						isSuccess = false;
//						message += tGdsSngl.getToGdsSnglId() + "��," + "���" + invtyTab.getInvtyEncd() + "������"
//								+ invtyTab.getBatNum() + "�Ŀ�����������㣬�޷����\n";
//						throw new RuntimeException(message);
//					}
					if (toGdsSub.getToOrdrNum() != null && toGdsSub.getToOrdrNum() != 0) {
						map.put("ordrNum", toGdsSub.getToOrdrNum());// ���
						BigDecimal returnQty = toGdsSnglSubDao.selectUnReturnQtyByOrdrNum(map);// �����ӱ���Ų�ѯδ��������
						if (returnQty != null) {
							if (returnQty.compareTo(toGdsSub.getQty().abs()) == 1
									|| returnQty.compareTo(toGdsSub.getQty().abs()) == 0) {
								map.put("returnQty", toGdsSub.getQty().abs());
								toGdsSnglSubDao.updateToGdsSnglSubUnReturnQty(map);// ��������޸ĵ������е�δ��������
							} else {
								isSuccess = false;
								message += "���ݺ�Ϊ��" + tGdsSngl.getToGdsSnglId() + "�ĵ������յ��д����" + toGdsSub.getInvtyEncd()
										+ "���ۼƾ����������ڵ����������޷���ˣ�\n";
								throw new RuntimeException(message);
							}

						} else {
							isSuccess = false;
							message += "���ݺ�Ϊ��" + tGdsSngl.getToGdsSnglId() + "�ĵ������յ���Ӧ�ĵ�������δ�������������ڣ��޷���ˣ�\n";
							throw new RuntimeException(message);
						}
					} else {
						isSuccess = false;
						message += "���ݺ�Ϊ��" + tGdsSngl.getToGdsSnglId() + "�ĵ������յ�û����Դ�ӱ���ţ��޷���ˣ�\n";
						throw new RuntimeException(message);
					}
//				}
			}
			int a = tgsd.updateToGdsSnglIsNtChk(tGdsSngl);
			if (a >= 1) {
				isSuccess = true;
				message += "����Ϊ" + tGdsSngl.getToGdsSnglId() + "�ĵ������յ���˳ɹ���\n";
			} else {
				isSuccess = false;
				message += "����Ϊ" + tGdsSngl.getToGdsSnglId() + "�ĵ������յ����ʧ��!��ֹ�ظ����\\n";
			}
		} else {
			isSuccess = false;
			message += "����Ϊ" + tGdsSngl.getToGdsSnglId() + "�ĵ������յ�����ˣ�����Ҫ�ظ����\n";
		}

		map.put("isSuccess", isSuccess);
		map.put("message", message);
		return map;
	}

	// �������յ�����
	public Map<String, Object> updateReturnToGdsSnglIsNtChkNO(ToGdsSngl tGdsSngl) {
		String message = "";
		Boolean isSuccess = true;
		Map<String, Object> map = new HashMap<>();
		if (tgsd.selectToGdsSnglIsNtChk(tGdsSngl.getToGdsSnglId()) == 1) {
			// �������ӱ���
			ToGdsSngl toGdsSngl = tgsd.selectToGdsSnglByToGdsSnglId(tGdsSngl.getToGdsSnglId());// ��õ���������
			List<ToGdsSnglSub> toGdsSnglSub = toGdsSngl.getToGdsSnglSub();// ������������ӱ�
			for (ToGdsSnglSub toGdsSub : toGdsSnglSub) {
				// ���ʵ��
//				InvtyTab invtyTab = new InvtyTab();
//				invtyTab.setWhsEncd(toGdsSub.getWhsEncd());
//				invtyTab.setInvtyEncd(toGdsSub.getInvtyEncd());
//				invtyTab.setBatNum(toGdsSub.getBatNum());
//				invtyTab = itd.selectInvtyTabsByTerm(invtyTab);
//				if (invtyTab == null) {
//					isSuccess = false;
//					message += "���ݺ�Ϊ:" + tGdsSngl.getToGdsSnglId() + "�ĵ������յ�����ʧ�ܣ�����û�иõ��ݶ�Ӧ�Ĵ����Ϣ���������еĴ���Ƿ���ڣ�\n";
//				} else {
//					invtyTab.setAvalQty(toGdsSub.getQty());
//					itd.updateInvtyTabAvalQtyJian(invtyTab);
//				}
				if (toGdsSub.getToOrdrNum() != null && toGdsSub.getToOrdrNum() != 0) {
					map.put("ordrNum", toGdsSub.getToOrdrNum());// ���
					BigDecimal returnQty = toGdsSnglSubDao.selectUnReturnQtyByOrdrNum(map);// �����ӱ���Ų�ѯδ��������
					if (returnQty != null) {
						map.put("returnQty", toGdsSub.getQty());
						toGdsSnglSubDao.updateToGdsSnglSubUnReturnQty(map);// ��������޸ĵ������е�δ��������
					} else {
						isSuccess = false;
						message += "���ݺ�Ϊ��" + tGdsSngl.getToGdsSnglId() + "�ĵ������յ���Ӧ�ĵ�������δ�������������ڣ��޷�����\n";
						throw new RuntimeException(message);
					}
				} else {
					isSuccess = false;
					message += "���ݺ�Ϊ��" + tGdsSngl.getToGdsSnglId() + "�ĵ������յ�û����Դ�ӱ���ţ��޷�����\n";
					throw new RuntimeException(message);
				}

			}
			int a = tgsd.updateToGdsSnglIsNtChk(tGdsSngl);
			if (a >= 1) {
				isSuccess = true;
				message += "���ݺ�Ϊ:" + tGdsSngl.getToGdsSnglId() + "�ĵ������յ�����ɹ���\n";
			} else {
				isSuccess = false;
				message += "���ݺ�Ϊ:" + tGdsSngl.getToGdsSnglId() + "�ĵ������յ�����ʧ�ܣ�\\n";
			}
		} else {
			isSuccess = false;
			message += "���ݺ�Ϊ:" + tGdsSngl.getToGdsSnglId() + "�ĵ������յ�δ��ˣ�������˸õ���\n";
		}
		map.put("isSuccess", isSuccess);
		map.put("message", message);
		return map;
	}

	// ��ӡ���������������Ϣ
	@Override
	public String printingToGdsSnglList(Map map) {
		String resp = "";
		List<String> toGdsSnglIdList = getList(((String)map.get("toGdsSnglId")));// ��������
		List<String> provrIdList = getList(((String) map.get("provrId")));// �ͻ�����
		List<String> accNumList = getList(((String) map.get("accNum")));// ҵ��Ա����
		List<String> invtyClsEncdList = getList(((String) map.get("invtyClsEncd")));// ����������
		List<String> invtyCdList = getList(((String) map.get("invtyCd")));// �������
		List<String> deptIdList = getList(((String) map.get("deptId")));// ���ű���
		List<String> provrOrdrNumList = getList(((String) map.get("provrOrdrNum")));// ��Ӧ�̶�����
		List<String> invtyEncdList = getList(((String) map.get("invtyEncd")));// �������
		List<String> whsEncdList = getList(((String) map.get("whsEncd")));// �ֿ����
		List<String> batNumList = getList(((String) map.get("batNum")));// ����
		List<String> intlBatList = getList(((String) map.get("intlBat")));// ��������
		List<String> pursOrdrIdList = getList(((String) map.get("pursOrdrId")));// �ɹ���������
		
		map.put("toGdsSnglIdList", toGdsSnglIdList);
		map.put("provrIdList", provrIdList);
		map.put("accNumList", accNumList);
		map.put("invtyClsEncdList", invtyClsEncdList);
		map.put("invtyCdList", invtyCdList);
		map.put("deptIdList", deptIdList);
		map.put("provrOrdrNumList", provrOrdrNumList);
		map.put("invtyEncdList", invtyEncdList);
		map.put("whsEncdList", whsEncdList);
		map.put("batNumList", batNumList);
		map.put("intlBatList", intlBatList);
		map.put("pursOrdrIdList", pursOrdrIdList);
		
		List<zizhu> toGdsSnglList = tgsd.printingToGdsSnglList(map);
		try {
			resp = BaseJson.returnRespObjListAnno("purc/ToGdsSngl/printingToGdsSnglList", true, "��ѯ�ɹ���", null,
					toGdsSnglList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
	}

	// ������ϸ��ѯ
	@Override
	public String queryToGdsSnglByInvtyEncd(Map map) {
		String resp = "";
		List<String> toGdsSnglIdList = getList(((String)map.get("toGdsSnglId")));// ��������
		List<String> provrIdList = getList(((String) map.get("provrId")));// ��Ӧ�̱���
		List<String> accNumList = getList(((String) map.get("accNum")));// ҵ��Ա����
		List<String> invtyClsEncdList = getList(((String) map.get("invtyClsEncd")));// ����������
		List<String> invtyCdList = getList(((String) map.get("invtyCd")));// �������
		List<String> deptIdList = getList(((String) map.get("deptId")));// ���ű���
		List<String> provrOrdrNumList = getList(((String) map.get("provrOrdrNum")));// ��Ӧ�̶�����
		List<String> invtyEncdList = getList(((String) map.get("invtyEncd")));// �������
		List<String> whsEncdList = getList(((String) map.get("whsEncd")));// �ֿ����
		List<String> batNumList = getList(((String) map.get("batNum")));// ����
		List<String> intlBatList = getList(((String) map.get("intlBat")));// ��������
		List<String> pursOrdrIdList = getList(((String) map.get("pursOrdrId")));// �ɹ���������
		
		map.put("toGdsSnglIdList", toGdsSnglIdList);
		map.put("provrIdList", provrIdList);
		map.put("accNumList", accNumList);
		map.put("invtyClsEncdList", invtyClsEncdList);
		map.put("invtyCdList", invtyCdList);
		map.put("deptIdList", deptIdList);
		map.put("provrOrdrNumList", provrOrdrNumList);
		map.put("invtyEncdList", invtyEncdList);
		map.put("whsEncdList", whsEncdList);
		map.put("batNumList", batNumList);
		map.put("intlBatList", intlBatList);
		map.put("pursOrdrIdList", pursOrdrIdList);
		if (map.containsKey("pageNo") && map.containsKey("pageSize")) {
			if (map.get("sort") == null||map.get("sort").equals("") ||
				map.get("sortOrder") == null||map.get("sortOrder").equals("")) {
				map.put("sort","toGdsSnglDt");
				map.put("sortOrder","desc");
			}
			List<Map> poList = tgsd.selectToGdsSnglByInvtyEncd(map);
			Map tableSums = tgsd.selectToGdsSnglListSums(map);
			if (null!=tableSums) {
				DecimalFormat df = new DecimalFormat("#,##0.00");
				for(Map.Entry entry : (Set<Entry>)tableSums.entrySet()) {
					String s = df.format((BigDecimal) entry.getValue()==null?new BigDecimal(0.00):entry.getValue());
					entry.setValue(s);
				}
			}
			int count = tgsd.selectToGdsSnglByInvtyEncdCount(map);

			int pageNo = Integer.parseInt(map.get("pageNo").toString());
			int pageSize = Integer.parseInt(map.get("pageSize").toString());
			int listNum = poList.size();
			int pages = count / pageSize + 1;

			try {
				resp = BaseJson.returnRespList("purc/ToGdsSngl/queryToGdsSnglByInvtyEncd", true, "��ѯ�ɹ���", count, pageNo,
						pageSize, listNum, pages, poList,tableSums);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			List<Map> poList = tgsd.selectToGdsSnglByInvtyEncd(map);
			Map tableSums = tgsd.selectToGdsSnglListSums(map);
			if (null!=tableSums) {
				DecimalFormat df = new DecimalFormat("#,##0.00");
				for(Map.Entry entry : (Set<Entry>)tableSums.entrySet()) {
					String s = df.format((BigDecimal) entry.getValue()==null?new BigDecimal(0.00):entry.getValue());
					entry.setValue(s);
				}
			}
			try {
				resp = BaseJson.returnRespList("purc/ToGdsSngl/queryToGdsSnglByInvtyEncd", true, "��ѯ�ɹ���", poList,tableSums);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return resp;
	}

	// ���뵽����
	public String uploadFileAddDb(MultipartFile file, int i) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";

		Map<String, ToGdsSngl> toGdsSnglMap = null;

		if (i == 0) {
			toGdsSnglMap = uploadScoreInfo(file);
		} else if (i == 1) {
			toGdsSnglMap = uploadScoreInfoU8(file);
		} else {
			isSuccess = false;
			message = "������쳣����";
			throw new RuntimeException(message);
		}
		
		//��MapתΪList��Ȼ���������븸������
		List<ToGdsSngl> toGdsSnglList = toGdsSnglMap.entrySet().stream().map(e -> e.getValue())
			.collect(Collectors.toList());
		List<List<ToGdsSngl>> toGdsSnglLists=Lists.partition(toGdsSnglList,1000);
		
		for(List<ToGdsSngl> toGdsSngl :toGdsSnglLists){
			tgsd.insertToGdsSnglUpload(toGdsSngl);
		}
		List<ToGdsSnglSub> toGdsSnglSubList = new ArrayList<>();
		int flag=0;
		for (ToGdsSngl entry : toGdsSnglList) {
			flag++;
			//���������ֱ�͸���Ĺ����ֶ�
			List<ToGdsSnglSub> tempList = entry.getToGdsSnglSub();
			tempList.forEach(s -> s.setToGdsSnglId(entry.getToGdsSnglId()));
			toGdsSnglSubList.addAll(tempList);
			//�������룬ÿ���ڵ���1000������һ��
			if(toGdsSnglSubList.size() >= 1000 || toGdsSnglMap.size()==flag){
				tgssd.insertToGdsSnglSub(toGdsSnglSubList);
				toGdsSnglSubList.clear();
			}
		}
		isSuccess = true;
		message = "����������ɹ���";
		try {
			if (i == 0) {
				resp = BaseJson.returnRespObj("purc/ToGdsSngl/uploadToGdsSnglFile", isSuccess, message, null);
			} else if (i == 1) {
				resp = BaseJson.returnRespObj("purc/ToGdsSngl/uploadToGdsSnglFileU8", isSuccess, message, null);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// ����excle
	private Map<String, ToGdsSngl> uploadScoreInfo(MultipartFile file) {
		Map<String, ToGdsSngl> temp = new HashMap<>();
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
			//����������
			getCellNames();
			// ��Sheet�е�ÿһ�н��е���
			for (int i = firstRowNum + 1; i <= lastRowNum; i++) {
				j++;
				Row r = sht0.getRow(i);
				// �����ǰ�е��кţ���0��ʼ��δ�ﵽ2�������У������ѭ��
				if (r.getRowNum() < 1) {
					continue;
				}
				String orderNo = GetCellData(r, "����������");
				// ����ʵ����
				ToGdsSngl toGdsSngl = new ToGdsSngl();
				if (temp.containsKey(orderNo)) {
					toGdsSngl = temp.get(orderNo);
				}
				// ȡ����ǰ�е�1����Ԫ�����ݣ�����װ��pursOrdrʵ��ĸ���������
				toGdsSngl.setPursTypId(GetCellData(r, "�ɹ����ͱ���"));// �ɹ����ͱ���
//				toGdsSngl.setPursTypNm(GetCellData(r,"�ɹ���������"));//�ɹ���������
				toGdsSngl.setToGdsSnglId(orderNo);// ����������
				if (GetCellData(r, "����������") == null || GetCellData(r, "����������").equals("")) {
					toGdsSngl.setToGdsSnglDt(null);
				} else {
					toGdsSngl.setToGdsSnglDt(GetCellData(r, "����������").replaceAll("[^0-9:-]", " "));// ����������
				}
				toGdsSngl.setProvrId(GetCellData(r, "��Ӧ�̱���"));// ��Ӧ�̱���
//				toGdsSngl.setProvrNm(GetCellData(r,"��Ӧ������"));//��Ӧ������
				toGdsSngl.setProvrOrdrNum(GetCellData(r, "��Ӧ�̶�����"));// ��Ӧ�̶�����
				toGdsSngl.setDeptId(GetCellData(r, "���ű���"));// ���ű���
//				toGdsSngl.setDepName(GetCellData(r,"��������"));//��������
				toGdsSngl.setAccNum(GetCellData(r, "ҵ��Ա����"));// �û�����
				toGdsSngl.setUserName(GetCellData(r, "ҵ��Ա����"));// �û�����
				toGdsSngl.setFormTypEncd(GetCellData(r, "�������ͱ���"));// ��������
				toGdsSngl.setPursOrdrId(GetCellData(r, "�ɹ���������"));// �ɹ���������
				toGdsSngl.setSetupPers(GetCellData(r, "������"));// ������
				if (GetCellData(r, "����ʱ��") == null || GetCellData(r, "����ʱ��").equals("")) {
					toGdsSngl.setSetupTm(null);
				} else {
					toGdsSngl.setSetupTm(GetCellData(r, "����ʱ��").replaceAll("[^0-9:-]", " "));// ����ʱ��
				}
				toGdsSngl.setIsNtChk(new Double(GetCellData(r, "�Ƿ����")).intValue());// �Ƿ����
				toGdsSngl.setChkr(GetCellData(r, "�����"));// �����
				if (GetCellData(r, "���ʱ��") == null || GetCellData(r, "���ʱ��").equals("")) {
					toGdsSngl.setChkTm(null);
				} else {
					toGdsSngl.setChkTm(GetCellData(r, "���ʱ��").replaceAll("[^0-9:-]", " "));// ���ʱ��
				}
				toGdsSngl.setMdfr(GetCellData(r, "�޸���")); // �޸���
				if (GetCellData(r, "�޸�ʱ��") == null || GetCellData(r, "�޸�ʱ��").equals("")) {
					toGdsSngl.setModiTm(null);
				} else {
					toGdsSngl.setModiTm(GetCellData(r, "�޸�ʱ��").replaceAll("[^0-9:-]", " "));// �޸�ʱ��
				}
				toGdsSngl.setMemo(GetCellData(r, "��ͷ��ע"));// ��ע
				toGdsSngl.setIsNtRtnGood(new Double(GetCellData(r, "�Ƿ��˻�")).intValue());// �Ƿ��˻�
				toGdsSngl.setToFormTypEncd(GetCellData(r, "��Դ�������ͱ���"));// ��Դ�������ͱ���

				List<ToGdsSnglSub> toGdsSnglSubList = toGdsSngl.getToGdsSnglSub();// �������ӱ�
				if (toGdsSnglSubList == null) {
					toGdsSnglSubList = new ArrayList<>();
				}
				ToGdsSnglSub toGdsSnglSub = new ToGdsSnglSub();
				toGdsSnglSub.setOrdrNum(Long.parseLong(GetCellData(r, "���")));
				toGdsSnglSub.setWhsEncd(GetCellData(r, "�ֿ����"));// �ֿ����
//				toGdsSnglSub.setWhsNm(GetCellData(r,"�ֿ�����"));//�ֿ�����
				toGdsSnglSub.setInvtyEncd(GetCellData(r, "�������"));// �������
				toGdsSnglSub.setQty(GetBigDecimal(GetCellData(r, "����"), 8));// 8��ʾС��λ�� //����
				toGdsSnglSub.setCntnTaxUprc(GetBigDecimal(GetCellData(r, "��˰����"), 8));// 8��ʾС��λ�� //��˰����
				toGdsSnglSub.setNoTaxUprc(GetBigDecimal(GetCellData(r, "��˰����"), 8));// 8��ʾС��λ�� //��˰����
				toGdsSnglSub.setNoTaxAmt(GetBigDecimal(GetCellData(r, "��˰���"), 8));// 8��ʾС��λ�� //��˰���
				toGdsSnglSub.setTaxAmt(GetBigDecimal(GetCellData(r, "˰��"), 8));// 8��ʾС��λ�� //˰��
				toGdsSnglSub.setPrcTaxSum(GetBigDecimal(GetCellData(r, "��˰�ϼ�"), 8));// 8��ʾС��λ�� //��˰�ϼ�
				toGdsSnglSub.setTaxRate(GetBigDecimal(GetCellData(r, "˰��"), 8));// 8��ʾС��λ�� //˰��
				toGdsSnglSub.setBxQty(GetBigDecimal(GetCellData(r, "����"), 8));// 8��ʾС��λ�� //����
				toGdsSnglSub.setBxRule(GetBigDecimal(GetCellData(r, "���"), 8));// 8��ʾС��λ�� //���
				toGdsSnglSub.setUnIntoWhsQty(GetBigDecimal(GetCellData(r, "δ�������"), 8));// 8��ʾС��λ�� //δ�������
				toGdsSnglSub.setBatNum(GetCellData(r, "����"));// ����
				if (GetCellData(r, "��������") == null || GetCellData(r, "��������").equals("")) {
					toGdsSnglSub.setPrdcDt(null);
				} else {
					toGdsSnglSub.setPrdcDt(GetCellData(r, "��������").replaceAll("[^0-9:-]", " "));// ��������
				}
				toGdsSnglSub.setBaoZhiQi(GetCellData(r, "������"));// ������
				if (GetCellData(r, "ʧЧ����") == null || GetCellData(r, "ʧЧ����").equals("")) {
					toGdsSnglSub.setInvldtnDt(null);
				} else {
					toGdsSnglSub.setInvldtnDt(GetCellData(r, "ʧЧ����").replaceAll("[^0-9:-]", " "));// ʧЧ����
				}
				toGdsSnglSub.setIntlBat(GetCellData(r, "��������"));// ��������
				toGdsSnglSub.setMemo(GetCellData(r, "���屸ע"));// �ӱ�ע
				toGdsSnglSub.setToOrdrNum(Long.parseLong(GetCellData(r, "��Դ�����ӱ����")));// ��Դ�����ӱ����
				toGdsSnglSub.setPursOrdrNum(GetCellData(r, "�ɹ���������"));// �ɹ���������

				toGdsSnglSubList.add(toGdsSnglSub);

				toGdsSngl.setToGdsSnglSub(toGdsSnglSubList);
				temp.put(orderNo, toGdsSngl);

			}
			fileIn.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("�ļ��ĵ�" + j + "�е����ʽ�����޷�����!" + e.getMessage());
		}
		return temp;
	}

	// ����excle
	private Map<String, ToGdsSngl> uploadScoreInfoU8(MultipartFile file) {
		Map<String, ToGdsSngl> temp = new HashMap<>();
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
			//����������
			getCellNames();
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
				ToGdsSngl toGdsSngl = new ToGdsSngl();
				if (temp.containsKey(orderNo)) {
					toGdsSngl = temp.get(orderNo);
				}
				// ȡ����ǰ�е�1����Ԫ�����ݣ�����װ��pursOrdrʵ��ĸ���������
				toGdsSngl.setPursTypId("1");// �ɹ����ͱ���
//					toGdsSngl.setPursTypNm(GetCellData(r,"�ɹ���������"));//�ɹ���������
				toGdsSngl.setToGdsSnglId(orderNo);// ����������
				if (GetCellData(r, "����") == null || GetCellData(r, "����").equals("")) {
					toGdsSngl.setToGdsSnglDt(null);
				} else {
					toGdsSngl.setToGdsSnglDt(GetCellData(r, "����").replaceAll("[^0-9:-]", " "));// ����������
				}
				toGdsSngl.setProvrId(GetCellData(r, "������λ����"));// ��Ӧ�̱���
//					toGdsSngl.setProvrNm(GetCellData(r,"��Ӧ������"));//��Ӧ������
				toGdsSngl.setProvrOrdrNum(GetCellData(r, "��Ӧ�̶�����"));// ��Ӧ�̶�����
				toGdsSngl.setDeptId(GetCellData(r, "���ű���"));// ���ű���
//					toGdsSngl.setDepName(GetCellData(r,"��������"));//��������
				toGdsSngl.setAccNum(GetCellData(r, "ְԱ����"));// �û�����
				toGdsSngl.setUserName(GetCellData(r, "ҵ �� Ա"));// �û�����

				toGdsSngl.setFormTypEncd("002");// ��������

//				������=�ɹ�ί�ⶩ����
				toGdsSngl.setPursOrdrId(GetCellData(r, "������"));// �ɹ���������
				toGdsSngl.setSetupPers(GetCellData(r, "�Ƶ���"));// ������
				if (GetCellData(r, "�Ƶ�ʱ��") == null || GetCellData(r, "�Ƶ�ʱ��").equals("")) {
					toGdsSngl.setSetupTm(null);
				} else {
					toGdsSngl.setSetupTm(GetCellData(r, "�Ƶ�ʱ��").replaceAll("[^0-9:-]", " "));// ����ʱ��
				}
				toGdsSngl.setIsNtChk(1);// �Ƿ����
				toGdsSngl.setChkr(GetCellData(r, "������"));// �����
				if (GetCellData(r, "��������") == null || GetCellData(r, "��������").equals("")) {
					toGdsSngl.setChkTm(null);
				} else {
					toGdsSngl.setChkTm(GetCellData(r, "��������").replaceAll("[^0-9:-]", " "));// ���ʱ��
				}
				toGdsSngl.setMdfr(GetCellData(r, "�޸���")); // �޸���
				if (GetCellData(r, "�޸�ʱ��") == null || GetCellData(r, "�޸�ʱ��").equals("")) {
					toGdsSngl.setModiTm(null);
				} else {
					toGdsSngl.setModiTm(GetCellData(r, "�޸�ʱ��").replaceAll("[^0-9:-]", " "));// �޸�ʱ��
				}
				toGdsSngl.setMemo(GetCellData(r, "��ע"));// ��ע
				toGdsSngl.setIsNtRtnGood(0);// �Ƿ��˻�
				toGdsSngl.setToFormTypEncd("001");// ��Դ�������ͱ���

				List<ToGdsSnglSub> toGdsSnglSubList = toGdsSngl.getToGdsSnglSub();// �������ӱ�
				if (toGdsSnglSubList == null) {
					toGdsSnglSubList = new ArrayList<>();
				}
				ToGdsSnglSub toGdsSnglSub = new ToGdsSnglSub();
//				toGdsSnglSub.setOrdrNum(Long.parseLong(GetCellData(r, "���")));
				toGdsSnglSub.setWhsEncd(GetCellData(r, "�ֿ����"));// �ֿ����
//					toGdsSnglSub.setWhsNm(GetCellData(r,"�ֿ�����"));//�ֿ�����
				toGdsSnglSub.setInvtyEncd(GetCellData(r, "�������"));// �������
				toGdsSnglSub.setQty(GetBigDecimal(GetCellData(r, "����"), 8));// 8��ʾС��λ�� //����
				toGdsSnglSub.setCntnTaxUprc(GetBigDecimal(GetCellData(r, "ԭ�Һ�˰����"), 8));// 8��ʾС��λ�� //��˰����
				toGdsSnglSub.setNoTaxUprc(GetBigDecimal(GetCellData(r, "���ҵ���"), 8));// 8��ʾС��λ�� //��˰����
				toGdsSnglSub.setNoTaxAmt(GetBigDecimal(GetCellData(r, "���ҽ��"), 8));// 8��ʾС��λ�� //��˰���
				toGdsSnglSub.setTaxAmt(GetBigDecimal(GetCellData(r, "����˰��"), 8));// 8��ʾС��λ�� //˰��
				toGdsSnglSub.setPrcTaxSum(GetBigDecimal(GetCellData(r, "���Ҽ�˰�ϼ�"), 8));// 8��ʾС��λ�� //��˰�ϼ�
				toGdsSnglSub.setTaxRate(GetBigDecimal(GetCellData(r, "����˰��"), 8));// 8��ʾС��λ�� //˰��
				toGdsSnglSub.setBxQty(GetBigDecimal(GetCellData(r, "����"), 8));// 8��ʾС��λ�� //����
				toGdsSnglSub.setBxRule(GetBigDecimal(GetCellData(r, "���"), 8));// 8��ʾС��λ�� //���
//				toGdsSnglSub.setUnIntoWhsQty(GetBigDecimal(GetCellData(r, "����"), 8));// 8��ʾС��λ�� //δ�������
				toGdsSnglSub.setReturnQty(GetBigDecimal(GetCellData(r, "����"), 8));// δ��������
//				toGdsSnglSub.setUnIntoWhsQty(GetBigDecimal(GetCellData(r, "δ�������"), 8));// 8��ʾС��λ�� //δ�������
				toGdsSnglSub.setBatNum(GetCellData(r, "����"));// ����
				if (GetCellData(r, "��������") == null || GetCellData(r, "��������").equals("")) {
					toGdsSnglSub.setPrdcDt(null);
				} else {
					toGdsSnglSub.setPrdcDt(GetCellData(r, "��������").replaceAll("[^0-9:-]", " "));// ��������
				}
				toGdsSnglSub.setBaoZhiQi(GetCellData(r, "������"));// ������
				if (GetCellData(r, "ʧЧ����") == null || GetCellData(r, "ʧЧ����").equals("")) {
					toGdsSnglSub.setInvldtnDt(null);
				} else {
					toGdsSnglSub.setInvldtnDt(GetCellData(r, "ʧЧ����").replaceAll("[^0-9:-]", " "));// ʧЧ����
				}
				toGdsSnglSub.setIntlBat(GetCellData(r, "��������"));// ��������
				toGdsSnglSub.setMemo(GetCellData(r, "�б�ע"));// �ӱ�ע
//				toGdsSnglSub.setToOrdrNum(Long.parseLong(GetCellData(r, "��Դ�����ӱ����")));// ��Դ�����ӱ����
				toGdsSnglSub.setPursOrdrNum(GetCellData(r, "������"));// �ɹ���������

				toGdsSnglSubList.add(toGdsSnglSub);

				toGdsSngl.setToGdsSnglSub(toGdsSnglSubList);
				temp.put(orderNo, toGdsSngl);

			}
			fileIn.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("�ļ��ĵ�" + j + "�е����ʽ�����޷�����!" + e.getMessage());
		}
		return temp;
	}

	// ����ʱ��ѯ����δ��������ĵ������ӱ���Ϣ
	@Override
	public String selectUnIntoWhsQtyByByToGdsSnglId(String pursOrdrId) {
		// TODO Auto-generated method stub
		String resp = "";
		List<String> lists = getList(pursOrdrId);
		List<ToGdsSnglSub> pursOrdrSubList = tgssd.selectUnIntoWhsQtyByByToGdsSnglId(lists);
		try {
			resp = BaseJson.returnRespObjList("purc/ToGdsSngl/selectUnIntoWhsQtyByByToGdsSnglId", true, "��ѯ�ɹ���", null,
					pursOrdrSubList);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// ����ʱ��ѯ����δ���������ĵ������ӱ���Ϣ
	@Override
	public String selectUnReturnQtyByToGdsSnglId(String pursOrdrId) {
		// TODO Auto-generated method stub
		String resp = "";
		List<String> lists = getList(pursOrdrId);
		List<ToGdsSnglSub> pursOrdrSubList = tgssd.selectUnReturnQtyByToGdsSnglId(lists);
		try {
			resp = BaseJson.returnRespObjList("purc/ToGdsSngl/selectUnReturnQtyByToGdsSnglId", true, "��ѯ�ɹ���", null,
					pursOrdrSubList);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// ����ʱ��ѯ������Ϣ
	@Override
	public String queryToGdsSnglLists(Map map) {
		String resp = "";
		List<String> toGdsSnglIdList = getList(((String)map.get("toGdsSnglId")));// ��������
		List<String> provrIdList = getList(((String) map.get("provrId")));// �ͻ�����
		List<String> accNumList = getList(((String) map.get("accNum")));// ҵ��Ա����
		List<String> invtyClsEncdList = getList(((String) map.get("invtyClsEncd")));// ����������
		List<String> invtyCdList = getList(((String) map.get("invtyCd")));// �������
		List<String> deptIdList = getList(((String) map.get("deptId")));// ���ű���
		List<String> provrOrdrNumList = getList(((String) map.get("provrOrdrNum")));// ��Ӧ�̶�����
		List<String> invtyEncdList = getList(((String) map.get("invtyEncd")));// �������
		List<String> whsEncdList = getList(((String) map.get("whsEncd")));// �ֿ����
		List<String> batNumList = getList(((String) map.get("batNum")));// ����
		List<String> intlBatList = getList(((String) map.get("intlBat")));// ��������
		List<String> pursOrdrIdList = getList(((String) map.get("pursOrdrId")));// �ɹ���������
		
		map.put("toGdsSnglIdList", toGdsSnglIdList);
		map.put("provrIdList", provrIdList);
		map.put("accNumList", accNumList);
		map.put("invtyClsEncdList", invtyClsEncdList);
		map.put("invtyCdList", invtyCdList);
		map.put("deptIdList", deptIdList);
		map.put("provrOrdrNumList", provrOrdrNumList);
		map.put("invtyEncdList", invtyEncdList);
		map.put("whsEncdList", whsEncdList);
		map.put("batNumList", batNumList);
		map.put("intlBatList", intlBatList);
		map.put("pursOrdrIdList", pursOrdrIdList);
		List<ToGdsSngl> poList = tgsd.selectToGdsSnglLists(map);
		int count = tgsd.selectToGdsSnglCounts(map);

		int pageNo = Integer.parseInt(map.get("pageNo").toString());
		int pageSize = Integer.parseInt(map.get("pageSize").toString());
		int listNum = poList.size();
		int pages = count / pageSize + 1;

		try {
			resp = BaseJson.returnRespList("purc/ToGdsSngl/queryToGdsSnglLists", true, "��ѯ�ɹ���", count, pageNo, pageSize,
					listNum, pages, poList);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}
	// ��ҳ��ѯ���е�������Ϣ
	@Override
	public String queryToGdsSnglListOrderBy(Map map) {
		String resp = "";
		List<String> toGdsSnglIdList = getList(((String)map.get("toGdsSnglId")));// ��������
		List<String> provrIdList = getList(((String) map.get("provrId")));// �ͻ�����
		List<String> accNumList = getList(((String) map.get("accNum")));// ҵ��Ա����
		List<String> invtyClsEncdList = getList(((String) map.get("invtyClsEncd")));// ����������
		List<String> invtyCdList = getList(((String) map.get("invtyCd")));// �������
		List<String> deptIdList = getList(((String) map.get("deptId")));// ���ű���
		List<String> provrOrdrNumList = getList(((String) map.get("provrOrdrNum")));// ��Ӧ�̶�����
		List<String> invtyEncdList = getList(((String) map.get("invtyEncd")));// �������
		List<String> whsEncdList = getList(((String) map.get("whsEncd")));// �ֿ����
		List<String> batNumList = getList(((String) map.get("batNum")));// ����
		List<String> intlBatList = getList(((String) map.get("intlBat")));// ��������
		List<String> pursOrdrIdList = getList(((String) map.get("pursOrdrId")));// �ɹ���������
		
		map.put("toGdsSnglIdList", toGdsSnglIdList);
		map.put("provrIdList", provrIdList);
		map.put("accNumList", accNumList);
		map.put("invtyClsEncdList", invtyClsEncdList);
		map.put("invtyCdList", invtyCdList);
		map.put("deptIdList", deptIdList);
		map.put("provrOrdrNumList", provrOrdrNumList);
		map.put("invtyEncdList", invtyEncdList);
		map.put("whsEncdList", whsEncdList);
		map.put("batNumList", batNumList);
		map.put("intlBatList", intlBatList);
		map.put("pursOrdrIdList", pursOrdrIdList);
		List<?> poList;
		if (map.get("sort") == null||map.get("sort").equals("") ||
			map.get("sortOrder") == null||map.get("sortOrder").equals("")) {
			map.put("sort","tgs.to_gds_sngl_dt");
			map.put("sortOrder","desc");
		}
		poList = tgsd.selectToGdsSnglListOrderBy(map);
		Map tableSums = tgsd.selectToGdsSnglListSums(map);
		if (null!=tableSums) {
			DecimalFormat df = new DecimalFormat("#,##0.00");
			for(Map.Entry entry : (Set<Entry>)tableSums.entrySet()) {
				String s = df.format((BigDecimal) entry.getValue()==null?new BigDecimal(0.00):entry.getValue());
				entry.setValue(s);
			}
		}
		int count = tgsd.selectToGdsSnglCount(map);

		int pageNo = Integer.parseInt(map.get("pageNo").toString());
		int pageSize = Integer.parseInt(map.get("pageSize").toString());
		int listNum = poList.size();
		int pages = count / pageSize + 1;

		try {
			resp = BaseJson.returnRespList("purc/ToGdsSngl/queryToGdsSnglListOrderBy", true, "��ѯ�ɹ���", count, pageNo, pageSize,
					listNum, pages, poList,tableSums);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	@Override
	public String updateToGdsSnglDealStat(String toGdsSnglId,int flat) {
		String message="";
		Boolean isSuccess=true;
		String resp="";
		try {
			if(flat==1){
				List<String> lists = getList(toGdsSnglId);
				for(String toGdsSngId: lists){
					ToGdsSngl toGdsSngl=tgsd.selectToGdsSnglById(toGdsSngId);
					if(toGdsSngl.getDealStat().equals("�Ѵ�")){
						int i=tgsd.updateToGdsSnglDealStatOK(toGdsSngId);
						if(i>=1){
							isSuccess=true;
							message+="���ݺ�Ϊ"+toGdsSngId+"�رճɹ�\n";
						}	
					}else{
						isSuccess=false;
						message+="���ݺ�Ϊ"+toGdsSngId+"�ĵ����ѹر�\n";
					}
				}
				resp=BaseJson.returnRespObj("purc/ToGdsSngl/updateToGdsSnglDealStatOK", isSuccess, message, null);
			}else if(flat==0){
				List<String> lists = getList(toGdsSnglId);
				for(String toGdsSngId: lists){
					ToGdsSngl toGdsSngl=tgsd.selectToGdsSnglById(toGdsSngId);
					if(toGdsSngl.getDealStat().equals("�ѹر�")){
						int i=tgsd.updateToGdsSnglDealStatNO(toGdsSngId);
						if(i>=1){
							isSuccess=true;
							message+="���ݺ�Ϊ"+toGdsSngId+"�򿪳ɹ�\n";
						}
					}else{
						isSuccess=false;
						message+="���ݺ�Ϊ"+toGdsSngId+"�ĵ����Ѵ�\n";
					}
				}
				resp=BaseJson.returnRespObj("purc/ToGdsSngl/updateToGdsSnglDealStatNO", isSuccess, message, null);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}
	
	public static class zizhu {
		@JsonProperty("����������")
		public String toGdsSnglId;//����������
		@JsonProperty("����������")
	    public String toGdsSnglDt;//��������
		@JsonProperty("�ɹ����ͱ���")
	    public String pursTypId;//�ɹ����ͱ���
		@JsonProperty("�ɹ���������")
	    public String pursTypNm;//�ɹ���������
		@JsonProperty("�������ͱ���")
	    public String formTypEncd;//�������ͱ���
		@JsonProperty("������������")
	    public String formTypName;//������������
		@JsonProperty("��Ӧ�̱���")
	    public String provrId;//��Ӧ�̱���
		@JsonProperty("��Ӧ������")
	    public String provrNm;//��Ӧ������
		@JsonProperty("���ű���")
	    public String deptId;//���ű���
		@JsonProperty("��������")
	    public String deptName;//��������
		@JsonProperty("�û�����")
	    public String accNum;//�û�����
		@JsonProperty("�û�����")
	    public String userName;//�û�����
		@JsonProperty("�ɹ���������")
	    public String pursOrdrId;//�ɹ���������
		@JsonProperty("��Ӧ�̶�����")
	    public String provrOrdrNum;//��Ӧ�̶�����
		@JsonProperty("�Ƿ����")
	    public Integer isNtChk;//�Ƿ����
		@JsonProperty("�����")
	    public String chkr;//�����
		@JsonProperty("���ʱ��")
	    public String chkTm;//���ʱ��
		@JsonProperty("������")
	    public String setupPers;//������
		@JsonProperty("����ʱ��")
	    public String setupTm;//����ʱ��
		@JsonProperty("�޸���")
	    public String mdfr;//�޸���
		@JsonProperty("�޸�ʱ��")
	    public String modiTm;//�޸�ʱ��
		@JsonProperty("��ͷ��ע")
	    public String memo;//��ע
		@JsonProperty("�Ƿ����˻�")
	    public Integer isNtRtnGood;//�Ƿ����˻�
		@JsonProperty("����״̬")
	    public String dealStat;//����״̬
		@JsonProperty("���ձ�ע")
	    public String returnMemo;//���ձ�ע
		
		@JsonProperty("��Դ�������ͱ���")
	    public String toFormTypEncd;//��Դ�������ͱ���
	    //�ӱ�
		@JsonProperty("���")
	    public Long ordrNum;//���
		@JsonProperty("�������")
		public String invtyEncd;//�������
		@JsonProperty("�ֿ����")
	    public String whsEncd;//�ֿ����
//		@JsonProperty("�ɹ�������")
//	    public String pursOrdrNum;//�ɹ�������
		@JsonProperty("����")
	    public BigDecimal qty;//����
		@JsonProperty("����")
	    public BigDecimal bxQty;//����
		@JsonProperty("˰��")
	    public BigDecimal taxRate;//˰��
		@JsonProperty("��˰����")
	    public BigDecimal noTaxUprc;//��˰����
		@JsonProperty("��˰���")
	    public BigDecimal noTaxAmt;//��˰���
		@JsonProperty("˰��")
	    public BigDecimal taxAmt;//˰��
		@JsonProperty("��˰����")
	    public BigDecimal cntnTaxUprc;//��˰����
		@JsonProperty("��˰�ϼ�")
	    public BigDecimal prcTaxSum;//��˰�ϼ�
		@JsonProperty("������")
	    public String baoZhiQi;//������
		@JsonProperty("��������")
	    public String intlBat;//��������
		@JsonProperty("����")
	    public String batNum;//����
		@JsonProperty("��������")
	    public String prdcDt;//��������
		@JsonProperty("ʧЧ����")
	    public String invldtnDt;//ʧЧ����
//		@JsonProperty("�Ƿ���Ʒ")
//	    public Integer isComplimentary;//�Ƿ���Ʒ
//		@JsonProperty("�Ƿ��˻�")
//	    public Integer isNtRtnGoods;//�Ƿ��˻���1:����  0���˻���
		@JsonProperty("δ��������")
	    public BigDecimal returnQty;//δ��������
//		@JsonProperty("��λ����")
//	    public String gdsBitEncd;//��λ����--------
		//�����������ֶΡ�������λ���ơ��ֿ�����
		@JsonProperty("�������")
	    public String invtyNm;//�������
		@JsonProperty("����ͺ�")
	    public String spcModel;//����ͺ�
		@JsonProperty("�������")
	    public String invtyCd;//�������
		@JsonProperty("���")
	    public BigDecimal bxRule;//���
		@JsonProperty("����˰��")
	    public BigDecimal iptaxRate;//����˰��
		@JsonProperty("����˰��")
	    public BigDecimal optaxRate;//����˰��
		@JsonProperty("��߽���")
	    public BigDecimal highestPurPrice;//��߽���
		@JsonProperty("����ۼ�")
	    public BigDecimal loSellPrc;//����ۼ�
		@JsonProperty("�ο��ɱ�")
	    public BigDecimal refCost;//�ο��ɱ�
		@JsonProperty("�ο��ۼ�")
	    public BigDecimal refSellPrc;//�ο��ۼ�
		@JsonProperty("���³ɱ�")
	    public BigDecimal ltstCost;//���³ɱ�
		@JsonProperty("������λ����")
	    public String measrCorpNm;//������λ����
		@JsonProperty("�ֿ�����")
	    public String whsNm;//�ֿ�����
		@JsonProperty("��Ӧ������")
	    public String crspdBarCd;//��Ӧ������
		@JsonProperty("δ�������")
	    public BigDecimal unIntoWhsQty;//δ�������
		@JsonProperty("��Դ�����ӱ����")
	    public Long toOrdrNum;//��Դ�����ӱ����
		@JsonProperty("���屸ע")
	    public String memos;//���屸ע
	}

}
