package com.px.mis.purc.service.impl;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;
import com.px.mis.purc.dao.*;
import com.px.mis.purc.entity.RtnGoods;
import com.px.mis.purc.entity.RtnGoodsSub;
import com.px.mis.purc.entity.SellOutWhs;
import com.px.mis.purc.entity.SellOutWhsSub;
import com.px.mis.purc.service.RtnGoodsService;
import com.px.mis.purc.util.CalcAmt;
import com.px.mis.util.BaseJson;
import com.px.mis.util.CommonUtil;
import com.px.mis.util.GetOrderNo;
import com.px.mis.util.poiTool;
import com.px.mis.whs.entity.InvtyTab;
import org.apache.commons.beanutils.BeanUtils;
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

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.management.RuntimeErrorException;

/*�˻�������*/
@Transactional
@Service
public class RtnGoodsServiceImpl extends poiTool implements RtnGoodsService {
	@Autowired
	private RtnGoodsDao rgd;
	@Autowired
	private RtnGoodsSubDao rgsd;
	@Autowired
	private SellSnglSubDao sssd;// ���۵��ӱ�
	@Autowired
	private SellOutWhsDao sowd;// ���۳��ⵥ����
	@Autowired
	private SellOutWhsSubDao sowds;// ���۳����ӱ�
	@Autowired
	private InvtyTabDao itd;// ����
	@Autowired
	private InvtyDocDao idd;// �������
	@Autowired
	private IntoWhsDao intoWhsDao;// �ɹ���ⵥ
	// ������
	@Autowired
	private GetOrderNo getOrderNo;

	@Override
	public String addRtnGoods(String userId, RtnGoods rtnGoods, List<RtnGoodsSub> rtnGoodsSubList, String loginTime) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";

		// ��ȡ������
		String number = getOrderNo.getSeqNo("TH", userId, loginTime);
		if (rgd.selectRtnGoodsById(number) != null) {
			message = "����" + number + "�Ѵ��ڣ����������룡";
			isSuccess = false;
		} else {
			rtnGoods.setRtnGoodsId(number);
			for (RtnGoodsSub rtnGoodsSub : rtnGoodsSubList) {
				rtnGoodsSub.setRtnGoodsId(rtnGoods.getRtnGoodsId());
				if (rtnGoodsSub.getPrdcDt() == "") {
					rtnGoodsSub.setPrdcDt(null);
				}
				if (rtnGoodsSub.getInvldtnDt() == "") {
					rtnGoodsSub.setInvldtnDt(null);
				}
				rtnGoodsSub.setUnBllgQty(rtnGoodsSub.getQty().abs());// δ��Ʊ����
			}
			rgd.insertRtnGoods(rtnGoods);
			rgsd.insertRtnGoodsSub(rtnGoodsSubList);
			message = "�����ɹ���";
			isSuccess = true;
		}
		try {
			resp = BaseJson.returnRespObj("purc/RtnGoods/addRtnGoods", isSuccess, message, rtnGoods);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resp;
	}

	// �޸��˻���
	@Override
	public String editRtnGoods(RtnGoods rtnGoods, List<RtnGoodsSub> rtnGoodsSubList) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";

		rgsd.deleteRtnGoodsSubByRtnGoodsId(rtnGoods.getRtnGoodsId());
		rgd.updateRtnGoodsByRtnGoodsId(rtnGoods);
		for (RtnGoodsSub rtnGoodsSub : rtnGoodsSubList) {
			rtnGoodsSub.setRtnGoodsId(rtnGoods.getRtnGoodsId());
			if (rtnGoodsSub.getPrdcDt() == "") {
				rtnGoodsSub.setPrdcDt(null);
			}
			if (rtnGoodsSub.getInvldtnDt() == "") {
				rtnGoodsSub.setInvldtnDt(null);
			}
			rtnGoodsSub.setUnBllgQty(rtnGoodsSub.getQty().abs());// δ��Ʊ����
		}
		rgsd.insertRtnGoodsSub(rtnGoodsSubList);
		isSuccess = true;
		message = "���³ɹ���";
		try {
			resp = BaseJson.returnRespObj("purc/RtnGoods/editRtnGoods", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	@Override
	public String deleteRtnGoods(String rtnGoodsId) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		RtnGoods rtn = rgd.selectRtnGoodsByRtnGoodsId(rtnGoodsId);
		
		if (rtn != null) {
			rgd.deleteRtnGoodsByRtnGoodsId(rtnGoodsId);
			rgsd.deleteRtnGoodsSubByRtnGoodsId(rtnGoodsId);
			if (rtn.getBizTypId().equals("2")) {
				
				throw new RuntimeException("�����ֶ�ɾ��B2C���˻���!");
			}
			isSuccess = true;
			message = "ɾ���ɹ���";
		} else {
			isSuccess = false;
			message = "����" + rtnGoodsId + "�����ڣ�";
		}

		try {
			resp = BaseJson.returnRespObj("purc/RtnGoods/deleteRtnGoods", isSuccess, message, null);
		} catch (IOException e) {
			throw new RuntimeException(message);
		}
		return resp;
	}

	@Override
	public String queryRtnGoods(String rtnGoodsId) {
		// TODO Auto-generated method stub
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		/* List<RtnGoodsSub> rtnGoodsSub = new ArrayList<>(); */
		RtnGoods rtnGoods = rgd.selectRtnGoodsByRtnGoodsId(rtnGoodsId);
		if (rtnGoods != null) {
			/* rtnGoodsSub=rgsd.selectRtnGoodsSubByRtnGoodsId(rtnGoodsId); */
			message = "��ѯ�ɹ���";
		} else {
			isSuccess = false;
			message = "����" + rtnGoodsId + "�����ڣ�";
		}
		try {
			resp = BaseJson.returnRespObj("purc/RtnGoods/queryRtnGoods", isSuccess, message, rtnGoods);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	@Override
	public String queryRtnGoodsList(Map map) {
		String resp = "";
		List<String> rtnGoodsIdList = getList((String) map.get("rtnGoodsId"));// �˻�������
		List<String> custIdList = getList((String) map.get("custId"));// �ͻ�����
		List<String> accNumList = getList((String) map.get("accNum"));// ҵ��Ա����
		List<String> invtyClsEncdList = getList((String) map.get("invtyClsEncd"));// ����������
		List<String> invtyCdList = getList((String) map.get("invtyCd"));// �������
		List<String> deptIdList = getList((String) map.get("deptId"));// ���ű���
		List<String> whsEncdList = getList((String) map.get("whsEncd"));// �ֿ����
		List<String> custOrdrNumList = getList((String) map.get("custOrdrNum"));// �ͻ�������
		List<String> invtyEncdList = getList((String) map.get("invtyEncd"));// �������
		List<String> batNumList = getList((String) map.get("batNum"));// ����

		map.put("rtnGoodsIdList", rtnGoodsIdList);
		map.put("custIdList", custIdList);
		map.put("accNumList", accNumList);
		map.put("invtyClsEncdList", invtyClsEncdList);
		map.put("invtyCdList", invtyCdList);
		map.put("deptIdList", deptIdList);
		map.put("whsEncdList", whsEncdList);
		map.put("custOrdrNumList", custOrdrNumList);
		map.put("invtyEncdList", invtyEncdList);
		map.put("batNumList", batNumList);
		List<RtnGoods> poList = rgd.selectRtnGoodsList(map);

		int count = rgd.selectRtnGoodsCount(map);

		int pageNo = Integer.parseInt(map.get("pageNo").toString());
		int pageSize = Integer.parseInt(map.get("pageSize").toString());
		int listNum = poList.size();
		int pages = count / pageSize + 1;

		try {
			resp = BaseJson.returnRespList("purc/RtnGoods/queryRtnGoodsList", true, "��ѯ�ɹ���", count, pageNo, pageSize,
					listNum, pages, poList);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// ����ɾ��
	@Override
	public String deleteRtnGoodsList(String rtnGoodsId) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		if (rgd.selectRtnGoodsById(rtnGoodsId).getBizTypId().equals("2")) {
			throw new RuntimeException("�����ֶ�ɾ��B2C���˻���!");
		}
		try {
			List<String> lists = getList(rtnGoodsId);
			List<String> lists2 = new ArrayList<>();
			List<String> lists3 = new ArrayList<>();
			for (String list : lists) {
				if (rgd.selectRtnGoodsIsNtChk(list) == 0) {
					lists2.add(list);
				} else {
					lists3.add(list);
				}
			}
			if (lists2.size() > 0) {
				int a = 0;
				try {
					a = deleteRtnGoodsList(lists2);
				} catch (Exception e) {
					isSuccess = false;
					message += "���ݺ�Ϊ��" + lists2.toString() + "�Ķ���ɾ��ʧ�ܣ�\n";
					throw new RuntimeException(message);
				}

				if (a >= 1) {
					isSuccess = true;
					message += "���ݺ�Ϊ��" + lists2.toString() + "�Ķ���ɾ���ɹ�!\n";
				} else {
					isSuccess = false;
					message += "���ݺ�Ϊ��" + lists2.toString() + "�Ķ���ɾ��ʧ�ܣ�\n";
					throw new RuntimeException(message);
				}
			}
			if (lists3.size() > 0) {
				isSuccess = false;
				message += "���ݺ�Ϊ��" + lists3.toString() + "�Ķ����ѱ���ˣ��޷�ɾ����\n";
				throw new RuntimeException(message);
			}
			resp = BaseJson.returnRespObj("purc/RtnGoods/deleteRtnGoodsList", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	@Transactional
	private int deleteRtnGoodsList(List<String> lists2) {
		rgd.insertRtnGoodsDl(lists2);
		rgsd.insertRtnGoodsSubDl(lists2);
		int a = rgd.deleteRtnGoodsList(lists2);
		return a;
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

	@Override
	public String printingRtnGoodsList(Map map) {
		String resp = "";
		List<String> rtnGoodsIdList = getList((String) map.get("rtnGoodsId"));// �˻�������
		List<String> custIdList = getList((String) map.get("custId"));// �ͻ�����
		List<String> accNumList = getList((String) map.get("accNum"));// ҵ��Ա����
		List<String> invtyClsEncdList = getList((String) map.get("invtyClsEncd"));// ����������
		List<String> invtyCdList = getList((String) map.get("invtyCd"));// �������
		List<String> deptIdList = getList((String) map.get("deptId"));// ���ű���
		List<String> whsEncdList = getList((String) map.get("whsEncd"));// �ֿ����
		List<String> custOrdrNumList = getList((String) map.get("custOrdrNum"));// �ͻ�������
		List<String> invtyEncdList = getList((String) map.get("invtyEncd"));// �������
		List<String> batNumList = getList((String) map.get("batNum"));// ����

		map.put("rtnGoodsIdList", rtnGoodsIdList);
		map.put("custIdList", custIdList);
		map.put("accNumList", accNumList);
		map.put("invtyClsEncdList", invtyClsEncdList);
		map.put("invtyCdList", invtyCdList);
		map.put("deptIdList", deptIdList);
		map.put("whsEncdList", whsEncdList);
		map.put("custOrdrNumList", custOrdrNumList);
		map.put("invtyEncdList", invtyEncdList);
		map.put("batNumList", batNumList);
		List<zizhu> rtnGoodsList = rgd.printingRtnGoodsList(map);
		try {
//			
			resp = BaseJson.returnRespObjListAnno("purc/RtnGoods/printingRtnGoodsList", true, "��ѯ�ɹ���", null,
					rtnGoodsList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
	}

	// �˻�����˺�����
	@Override
	public Map<String, Object> updateRtnGoodsIsNtChksList(String userId, RtnGoods rtnGoods, String loginTime)
			throws Exception {
		StringBuilder message = new StringBuilder();
		Boolean isSuccess = true;
		Map<String, Object> map = new HashMap<>();
		try {
			if (rtnGoods.getIsNtChk() == 1) {
				message.append(updateRtnGoodsIsNtChkOK(userId, rtnGoods, loginTime).get("message"));
			} else if (rtnGoods.getIsNtChk() == 0) {
				message.append(updateRtnGoodsIsNtChkNO(rtnGoods).get("message"));
			}
			map.put("isSuccess", isSuccess);
			map.put("message", message.toString());

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return map;
	}

	// �˻������
	public Map<String, Object> updateRtnGoodsIsNtChkOK(String userId, RtnGoods rtnGoods, String loginTime) {
		String message = "";
		Boolean isSuccess = true;
		Map<String, Object> map = new HashMap<>();
		RtnGoods rtnGods = rgd.selectRtnGoodsByRtnGoodsId(rtnGoods.getRtnGoodsId());// �����ݿ�
		List<RtnGoodsSub> rtnGoodsSubList = rtnGods.getRtnGoodsSub();// ����˻����ӱ�

		// �����ж��˻��������״̬�����״̬Ϊ0ʱ�ſ���ִ����˲���
		if (rtnGods.getIsNtChk() == 0) {
			Map<String, List<RtnGoodsSub>> handleMap = rtnGoodsSubList.stream()
					.collect(Collectors.groupingBy(RtnGoodsSub::getWhsEncd));
			for (Map.Entry<String, List<RtnGoodsSub>> entry : handleMap.entrySet()) {
				SellOutWhs sellOutWhs = new SellOutWhs();// ���۳��ⵥ����
				String number = getOrderNo.getSeqNo("CK", userId, loginTime);
				try {
					BeanUtils.copyProperties(sellOutWhs, rtnGods);
					// ��ί�д��������Ƹ�����ר�÷�Ʊ
					sellOutWhs.setOutWhsId(number);// ���ⵥ����
					sellOutWhs.setOutWhsDt(CommonUtil.getLoginTime(loginTime));// ���ⵥ����
					sellOutWhs.setSellOrdrInd(rtnGods.getRtnGoodsId());// ���۳��ⵥ�ж�Ӧ�����۵�����
					sellOutWhs.setFormTypEncd("010");// �������ͱ���
					sellOutWhs.setToFormTypEncd(rtnGods.getFormTypEncd());// ��Դ��������
					// ͨ��ҵ�������ж��շ����
					if (rtnGods.getBizTypId().equals("1")) {
						sellOutWhs.setRecvSendCateId("7");// �շ����ͱ��룬��������
					}
					if (rtnGods.getBizTypId().equals("2")) {
						sellOutWhs.setRecvSendCateId("6");// �շ����ͱ��룬��������
					}
					sellOutWhs.setIsNtRtnGood(1);// �Ƿ��˻�
					sellOutWhs.setOutIntoWhsTypId("10");// ��������ͱ���
					sellOutWhs.setSetupPers(rtnGoods.getChkr());// ������,�����۵��������
					sellOutWhs.setSetupTm(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));// ����ʱ��=��ǰʱ��

					List<SellOutWhsSub> sellOutWhsSubList = new ArrayList<>();// ���۳��ⵥ�ӱ���
					for (RtnGoodsSub rtnGoodsSub : entry.getValue()) {
						BigDecimal qtyList = rgsd.selectRtnGoodsSubQty(rtnGoodsSub);
						if (qtyList == null) {
							isSuccess = false;
							message += "����Ϊ��" + rtnGoods.getRtnGoodsId() + "���˻���������Ϊ�գ��޷����\n";
							throw new RuntimeException(message);
						} else if (qtyList.intValue() == 0) {
							continue;
						} else {
							// ����ʵ��
							InvtyTab invtyTab = new InvtyTab();
							invtyTab.setWhsEncd(rtnGoodsSub.getWhsEncd());
							invtyTab.setInvtyEncd(rtnGoodsSub.getInvtyEncd());
							invtyTab.setBatNum(rtnGoodsSub.getBatNum());
							invtyTab = itd.selectInvtyTabsByTerm(invtyTab);// ��ֹ�����޸�,FOR UPDATE
							SellOutWhsSub sellOutWhsSub = new SellOutWhsSub();// ���۳��ⵥ�ӱ�
							BeanUtils.copyProperties(sellOutWhsSub, rtnGoodsSub);// �����۵��ӱ��Ƹ����۳��ⵥ
							sellOutWhsSub.setOutWhsId(sellOutWhs.getOutWhsId());// �����۳��ⵥ������븴�Ƹ����۳��ⵥ�ӱ����������
							// ���û���������ںͱ�����ʱ��ʧЧ����Ĭ��Ϊnull
							if (rtnGoodsSub.getPrdcDt() == null || rtnGoodsSub.getBaoZhiQi() == null) {
								sellOutWhsSub.setInvldtnDt(null);
							} else {
								// �������۳��ⵥ��ʧЧ����
								sellOutWhsSub.setPrdcDt(rtnGoodsSub.getPrdcDt());// ��������
								sellOutWhsSub.setBaoZhiQi(rtnGoodsSub.getBaoZhiQi());// ������
								sellOutWhsSub.setInvldtnDt(
										CalcAmt.getDate(sellOutWhsSub.getPrdcDt(), sellOutWhsSub.getBaoZhiQi()));
							}
							// ��ѯ��������˰����
							// ����ȡ����˰���۸������۳������˰����
							setRtnSellOutWhsCB(invtyTab, rtnGods, rtnGoodsSub, sellOutWhsSub);
							sellOutWhsSub.setIsNtRtnGoods(1);// �Ƿ��˻�
							sellOutWhsSub.setToOrdrNum(rtnGoodsSub.getOrdrNum());// ��Դ�����ӱ����
							sellOutWhsSubList.add(sellOutWhsSub);
							// ���۵����ʱ�жϿ������Ƿ��иô����Ϣ
							if (invtyTab != null) {
								invtyTab.setAvalQty(rtnGoodsSub.getQty());
								// �ڿ����н���Ӧ�Ŀ��������ӣ������˻����е������Ǹ��������Կ�����Ҫ��������,����������
								itd.updateInvtyTabAvalQtyJian(invtyTab);
							} else {
								// �����û�иô��ʱ����Ҫ���������Ϣ����������˻���ʵ�壬�����Ҫ���˻����������ó���������������
								rtnGoodsSub.setQty(rtnGoodsSub.getQty().abs());
								itd.insertInvtyTabToRtnGoods(rtnGoodsSub);
							}
						}
						if (rtnGoodsSub.getToOrdrNum() != null && rtnGoodsSub.getToOrdrNum() != 0) {
							map.put("ordrNum", rtnGoodsSub.getToOrdrNum());// ���
							map.put("sellSnglId", rtnGods.getSellOrdrId());// ���۵���
							BigDecimal rtnblQty = sssd.selectRtnblQtyByOrdrNum(map);// �����˻����ӱ���Ų�ѯ���۵��п�������
							if (rtnblQty != null) {
								System.out.println("�˻�������" + rtnGoodsSub.getQty().abs());
								if (rtnGoodsSub.getQty().abs().compareTo(rtnblQty) <= 0) {
									map.put("rtnblQty", rtnGoodsSub.getQty().abs());// �޸Ŀ�������
									sssd.updateSellSnglRtnblQtyByOrdrNum(map);// �����˻����ӱ�����޸����۵��п�������
								} else {
									isSuccess = false;
									message += "���ݺ�Ϊ��" + rtnGoods.getRtnGoodsId() + "���˻������д����"
											+ rtnGoodsSub.getInvtyEncd() + "���ۼ��˻��������ڿ����������޷���ˣ�\n";
									throw new RuntimeException(message);
								}
							} else {
								isSuccess = false;
								message += "���ݺ�Ϊ��" + rtnGoods.getRtnGoodsId() + "���˻�����Ӧ�����۵��п������������ڣ��޷���ˣ�\n";
								throw new RuntimeException(message);
							}
						}
					}
					if (sellOutWhsSubList.size() > 0) {
						sowd.insertSellOutWhs(sellOutWhs);
						sowds.insertSellOutWhsSub(sellOutWhsSubList);
						sellOutWhsSubList.clear();
					}
				} catch (IllegalAccessException | InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			int a = rgd.updateRtnGoodsIsNtChk(rtnGoods);
			if (a >= 1) {
				isSuccess = true;
				message += "����Ϊ" + rtnGoods.getRtnGoodsId() + "���˻�����˳ɹ�\n";
			} else {
				isSuccess = false;
				message += "����Ϊ" + rtnGoods.getRtnGoodsId() + "���˻������ʧ��\n";
				throw new RuntimeException(message);
			}
		} else {
			isSuccess = false;
			message += "����Ϊ" + rtnGoods.getRtnGoodsId() + "�ĵ�������ˣ�����Ҫ�ظ����\n";
			throw new RuntimeException(message);
		}
		map.put("isSuccess", isSuccess);
		map.put("message", message);
		return map;
	}

	// �˻�������
	public Map<String, Object> updateRtnGoodsIsNtChkNO(RtnGoods rtnGoods) {
		String message = "";
		Boolean isSuccess = true;
		Map<String, Object> map = new HashMap<>();
		RtnGoods rtnGods = rgd.selectRtnGoodsByRtnGoodsId(rtnGoods.getRtnGoodsId());
		// �����ж����۵������״̬�����״̬Ϊ1ʱ�ſ���ִ����������
		if (rtnGods.getIsNtChk() == 1) {
			// �����˻�����ȡ���۳��ⵥ��������Ϣ
			List<SellOutWhs> sellOutWhsList = sowd.selectOutWhsIdBySellOrdrInd(rtnGoods.getRtnGoodsId());
			int isNtChkQty = 0;
			if (sellOutWhsList.size() > 0) {
				for (SellOutWhs sellOutWhs : sellOutWhsList) {
					if (sellOutWhs.getIsNtChk() == 1) {
						isNtChkQty++;
					}
				}
			}
			if (isNtChkQty > 0) {
				isSuccess = false;
				message += "����Ϊ" + rtnGoods.getRtnGoodsId() + "���˻�����Ӧ�����ε��ݡ����۳��ⵥ������ˣ��޷�����\n";
				throw new RuntimeException(message);
			} else {
				// ͨ���˻�����ȡ�˻����ӱ���Ϣ
				List<RtnGoodsSub> rtnGoodsSubList = rtnGods.getRtnGoodsSub();// ����˻����ӱ�
				for (RtnGoodsSub rGSub : rtnGoodsSubList) {
					InvtyTab invtyTab = new InvtyTab();// ���ʵ��
					invtyTab.setWhsEncd(rGSub.getWhsEncd());
					invtyTab.setInvtyEncd(rGSub.getInvtyEncd());
					invtyTab.setBatNum(rGSub.getBatNum());
					invtyTab = itd.selectInvtyTabsByTerm(invtyTab);
					if (invtyTab != null) {
						if (invtyTab.getAvalQty().compareTo(rGSub.getQty().abs()) >= 0) {
							invtyTab.setAvalQty(rGSub.getQty());
							itd.updateInvtyTabAvalQtyJia(invtyTab);
						} else if (invtyTab.getAvalQty().compareTo(rGSub.getQty().abs()) == -1) {
							isSuccess = false;
							message += "����Ϊ��" + rtnGoods.getRtnGoodsId() + "���˻����д�����룺" + rGSub.getInvtyEncd() + ",���Σ�"
									+ rGSub.getBatNum() + "�Ŀ�治�㣬�޷�����\n";
							throw new RuntimeException(message);
						}
					} else {
						isSuccess = false;
						message += "����Ϊ��" + rtnGoods.getRtnGoodsId() + "���˻�����,������룺" + rGSub.getInvtyEncd() + ",���Σ�"
								+ rGSub.getBatNum() + "�Ŀ�治���ڣ��޷�	����\n";
						throw new RuntimeException(message);
					}
					if (rGSub.getToOrdrNum() != null && rGSub.getToOrdrNum() != 0) {
						map.put("ordrNum", rGSub.getToOrdrNum());// ���
						map.put("sellSnglId", rtnGods.getSellOrdrId());
						BigDecimal rtnblQty = sssd.selectRtnblQtyByOrdrNum(map);// �����˻����ӱ���Ų�ѯ���۵��п�������
						if (rtnblQty != null) {
							map.put("rtnblQty", rGSub.getQty());// �޸Ŀ�������
							sssd.updateSellSnglRtnblQtyByOrdrNum(map);// �����˻����ӱ�����޸����۵��п�������
						} else {
							isSuccess = false;
							message += "���ݺ�Ϊ��" + rtnGoods.getRtnGoodsId() + "���˻�����Ӧ�����۵��п������������ڣ��޷�����\n";
							throw new RuntimeException(message);
						}
					}
				}
				int a = rgd.updateRtnGoodsIsNtChk(rtnGoods);
				if (sellOutWhsList.size() > 0) {
					sowd.deleteSellOutWhsBySellOrdrInd(rtnGoods.getRtnGoodsId());// ɾ�����۳��ⵥ
				}
				if (a >= 1) {
					isSuccess = true;
					message += "����Ϊ" + rtnGoods.getRtnGoodsId() + "���˻�������ɹ�\n";
				} else {
					isSuccess = false;
					message += "����Ϊ" + rtnGoods.getRtnGoodsId() + "���˻�������ʧ��\n";
					throw new RuntimeException(message);
				}
			}
		} else {
			isSuccess = false;
			message = "����Ϊ" + rtnGoods.getRtnGoodsId() + "���˻���δ��ˣ�������˸õ���\n";
			throw new RuntimeException(message);
		}
		map.put("isSuccess", isSuccess);
		map.put("message", message);
		return map;
	}

	private void setRtnSellOutWhsCB(InvtyTab invtyTab, RtnGoods rtnGoods, RtnGoodsSub rGSub, SellOutWhsSub sOutWhsSub) {
		Map<String, Object> map = new HashMap<>();
		if (rGSub.getToOrdrNum() != null && rGSub.getToOrdrNum() != 0) {
			// ԭ���۵���Ӧ�ĳ��ⵥ�ӱ����
			map.put("toOrdrNum", rGSub.getToOrdrNum());
			map.put("sellSnglId", rtnGoods.getSellOrdrId());// ���۵���
			BigDecimal noTaxUprc1 = sowd.selectSellOutWhsSubByToOrdrNum(map);
			if (noTaxUprc1 != null) {
				sOutWhsSub.setNoTaxUprc(noTaxUprc1);
				// ����δ˰��� ���=δ˰����*δ˰����
				sOutWhsSub.setNoTaxAmt(CalcAmt.noTaxAmt(sOutWhsSub.getNoTaxUprc(), sOutWhsSub.getQty()));
				// ����˰�� ˰��=δ˰���*˰��
				sOutWhsSub.setTaxAmt(CalcAmt.taxAmt(sOutWhsSub.getNoTaxUprc(), sOutWhsSub.getQty(),
						sOutWhsSub.getTaxRate().divide(new BigDecimal(100))));
				// ���㺬˰���� ��˰����=��˰����*˰��+��˰����
				sOutWhsSub.setCntnTaxUprc(CalcAmt.cntnTaxUprc(sOutWhsSub.getNoTaxUprc(), sOutWhsSub.getQty(),
						sOutWhsSub.getTaxRate().divide(new BigDecimal(100))));
				// �����˰�ϼ� ��˰�ϼ�=��˰���*˰��+��˰���=˰��+��˰���
				sOutWhsSub.setPrcTaxSum(CalcAmt.prcTaxSum(sOutWhsSub.getNoTaxUprc(), sOutWhsSub.getQty(),
						sOutWhsSub.getTaxRate().divide(new BigDecimal(100))));
			} else {
				throw new RuntimeException("����Ϊ" + rtnGoods.getRtnGoodsId() + "���˻������ִ��δ�ҵ���Ӧ�ĳ���ɱ��������ԭ�������\n");
			}
		} else {
			map.put("invtyEncd", rGSub.getInvtyEncd());
			BigDecimal pursunTaxUprc = intoWhsDao.selectUnTaxUprc(map);// �ɹ���ⵥ���һ����ⵥ��
			if (pursunTaxUprc != null) {
				// ��ȡ��˰����,ȡ���ĵ���Ϊ��������Ҫת�ɸ���
				sOutWhsSub.setNoTaxUprc(pursunTaxUprc);
				// ����δ˰��� ���=δ˰����*δ˰����
				sOutWhsSub.setNoTaxAmt(CalcAmt.noTaxAmt(sOutWhsSub.getNoTaxUprc(), sOutWhsSub.getQty()));
				// ����˰�� ˰��=δ˰���*˰��
				sOutWhsSub.setTaxAmt(CalcAmt.taxAmt(sOutWhsSub.getNoTaxUprc(), sOutWhsSub.getQty(),
						sOutWhsSub.getTaxRate().divide(new BigDecimal(100))));
				// ���㺬˰���� ��˰����=��˰����*˰��+��˰����
				sOutWhsSub.setCntnTaxUprc(CalcAmt.cntnTaxUprc(sOutWhsSub.getNoTaxUprc(), sOutWhsSub.getQty(),
						sOutWhsSub.getTaxRate().divide(new BigDecimal(100))));
				// �����˰�ϼ� ��˰�ϼ�=��˰���*˰��+��˰���=˰��+��˰���
				sOutWhsSub.setPrcTaxSum(CalcAmt.prcTaxSum(sOutWhsSub.getNoTaxUprc(), sOutWhsSub.getQty(),
						sOutWhsSub.getTaxRate().divide(new BigDecimal(100))));
			} else {
				// ��ѯ��������еĲο��ɱ�
				BigDecimal noTaxUprc2 = idd.selectRefCost(rGSub.getInvtyEncd());
				if (noTaxUprc2 != null) {
					// ��ȡ��˰����,ȡ���ĵ���Ϊ��������Ҫת�ɸ���
					sOutWhsSub.setNoTaxUprc(noTaxUprc2);
					// ����δ˰��� ���=δ˰����*δ˰����
					sOutWhsSub.setNoTaxAmt(CalcAmt.noTaxAmt(sOutWhsSub.getNoTaxUprc(), sOutWhsSub.getQty()));
					// ����˰�� ˰��=δ˰���*˰��
					sOutWhsSub.setTaxAmt(CalcAmt.taxAmt(sOutWhsSub.getNoTaxUprc(), sOutWhsSub.getQty(),
							sOutWhsSub.getTaxRate().divide(new BigDecimal(100))));
					// ���㺬˰���� ��˰����=��˰����*˰��+��˰����
					sOutWhsSub.setCntnTaxUprc(CalcAmt.cntnTaxUprc(sOutWhsSub.getNoTaxUprc(), sOutWhsSub.getQty(),
							sOutWhsSub.getTaxRate().divide(new BigDecimal(100))));
					// �����˰�ϼ� ��˰�ϼ�=��˰���*˰��+��˰���=˰��+��˰���
					sOutWhsSub.setPrcTaxSum(CalcAmt.prcTaxSum(sOutWhsSub.getNoTaxUprc(), sOutWhsSub.getQty(),
							sOutWhsSub.getTaxRate().divide(new BigDecimal(100))));
					System.out.println("�ο��ۼ�");
				} else {
					throw new RuntimeException("����Ϊ" + rtnGoods.getRtnGoodsId() + "���˻���ȡ������Ӧ�ĳɱ����޷����ɶ�Ӧ���۳��ⵥ�����ʧ�ܣ�\n");
				}
			}
		}
	}

	// �����˻���
	@Override
	public String uploadFileAddDb(MultipartFile file, int i) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		Map<String, RtnGoods> rtnGoodsMap = null;
		if (i == 0) {
			rtnGoodsMap = uploadScoreInfo(file);
		} else if (i == 1) {
			rtnGoodsMap = uploadScoreInfoU8(file);
		} else {
			isSuccess = false;
			message = "������쳣����";
			throw new RuntimeException(message);
		}

		// ��MapתΪList��Ȼ���������븸������
		List<RtnGoods> rtnGoodsList = rtnGoodsMap.entrySet().stream().map(e -> e.getValue())
				.collect(Collectors.toList());
		List<List<RtnGoods>> rtnGoodsLists = Lists.partition(rtnGoodsList, 1000);

		for (List<RtnGoods> rtnGoods : rtnGoodsLists) {
			rgd.insertRtnGoodsbyUpload(rtnGoods);
		}
		List<RtnGoodsSub> rtnGoodsSubList = new ArrayList<>();
		int flag = 0;
		for (RtnGoods entry : rtnGoodsList) {
			flag++;
			// ���������ֱ�͸���Ĺ����ֶ�
			List<RtnGoodsSub> tempList = entry.getRtnGoodsSub();
			tempList.forEach(s -> s.setRtnGoodsId(entry.getRtnGoodsId()));
			rtnGoodsSubList.addAll(tempList);
			// �������룬ÿ���ڵ���1000������һ��
			if (rtnGoodsSubList.size() >= 1000 || rtnGoodsMap.size() == flag) {
				rgsd.insertRtnGoodsSub(rtnGoodsSubList);
				rtnGoodsSubList.clear();
			}
		}
		isSuccess = true;
		message = "�˻�������ɹ���";
		try {
			if (i == 0) {
				resp = BaseJson.returnRespObj("purc/RtnGoods/uploadRtnGoodsFile", isSuccess, message, null);
			} else if (i == 1) {
				resp = BaseJson.returnRespObj("purc/RtnGoods/uploadRtnGoodsFileU8", isSuccess, message, null);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// ����excle
	private Map<String, RtnGoods> uploadScoreInfo(MultipartFile file) {
		Map<String, RtnGoods> temp = new HashMap<>();
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
			// ����������
			getCellNames();
			// ��Sheet�е�ÿһ�н��е���
			for (int i = firstRowNum + 1; i <= lastRowNum; i++) {
				j++;
				Row r = sht0.getRow(i);
				// �����ǰ�е��кţ���0��ʼ��δ�ﵽ2�������У������ѭ��
				if (r.getRowNum() < 1) {
					continue;
				}
				String orderNo = GetCellData(r, "�˻�������");
				// ����ʵ����
				RtnGoods rtnGoods = new RtnGoods();
				if (temp.containsKey(orderNo)) {
					rtnGoods = temp.get(orderNo);
				}
				// ȡ����ǰ�е�1����Ԫ�����ݣ�����װ��pursOrdrʵ��ĸ���������
				rtnGoods.setSellTypId(GetCellData(r, "�������ͱ���"));// �������ͱ���
				rtnGoods.setRtnGoodsId(orderNo); // �˻�������
				if (GetCellData(r, "�˻�������") == null || GetCellData(r, "�˻�������").equals("")) {
					rtnGoods.setRtnGoodsDt(null);
				} else {
					rtnGoods.setRtnGoodsDt(GetCellData(r, "�˻�������").replaceAll("[^0-9:-]", " "));// �˻�������
				}
				rtnGoods.setCustId(GetCellData(r, "�ͻ�����"));// �ͻ�����
//				rtnGoods.setCustNm(GetCellData(r, "�ͻ�����"));// �ͻ�����
				rtnGoods.setCustOrdrNum(GetCellData(r, "�ͻ�������"));// �ͻ�������
				rtnGoods.setDeptId(GetCellData(r, "���ű���"));// ���ű���
//				rtnGoods.setDepName(GetCellData(r, "��������"));// ��������
				rtnGoods.setAccNum(GetCellData(r, "ҵ��Ա����"));// ҵ��Ա����
				rtnGoods.setUserName(GetCellData(r, "ҵ��Ա����"));// ҵ��Ա����
				rtnGoods.setFormTypEncd(GetCellData(r, "�������ͱ���"));// �������ͱ���
				rtnGoods.setBizTypId(GetCellData(r, "ҵ�����ͱ���"));// ҵ�����ͱ���
//				rtnGoods.setRecvSendCateId(GetCellData(r,"�շ�������"));//�շ�������
				rtnGoods.setSellOrdrId(GetCellData(r, "���۵�����"));// ���۵�����
//				rtnGoods.setRefId(GetCellData(r,"�˿��"));//�˿�� 
				rtnGoods.setTxId(GetCellData(r, "���ױ���"));// ���ױ���
				rtnGoods.setRecvr(GetCellData(r, "�ռ���")); // '�ռ���'
				rtnGoods.setRecvrTel(GetCellData(r, "�ռ��˵绰")); // '�ռ��˵绰'
				rtnGoods.setRecvrAddr(GetCellData(r, "�ռ��˵�ַ")); // '�ռ��˵�ַ'
				rtnGoods.setRecvrEml(GetCellData(r, "�ռ�������")); // '�ռ�������'
				rtnGoods.setBuyerNote(GetCellData(r, "�������")); // '�������'
				rtnGoods.setSetupPers(GetCellData(r, "������"));// ������
				if (GetCellData(r, "����ʱ��") == null || GetCellData(r, "����ʱ��").equals("")) {
					rtnGoods.setSetupTm(null);
				} else {
					rtnGoods.setSetupTm(GetCellData(r, "����ʱ��").replaceAll("[^0-9:-]", " "));// ����ʱ��
				}
				rtnGoods.setMdfr(GetCellData(r, "�޸���")); // �޸���
				if (GetCellData(r, "�޸�ʱ��") == null || GetCellData(r, "�޸�ʱ��").equals("")) {
					rtnGoods.setModiTm(null);
				} else {
					rtnGoods.setModiTm(GetCellData(r, "�޸�ʱ��").replaceAll("[^0-9:-]", " "));// �޸�ʱ��
				}
				rtnGoods.setIsNtChk(new Double(GetCellData(r, "�Ƿ����")).intValue());// �Ƿ����
				rtnGoods.setChkr(GetCellData(r, "�����"));// �����
				if (GetCellData(r, "���ʱ��") == null || GetCellData(r, "���ʱ��").equals("")) {
					rtnGoods.setChkTm(null);
				} else {
					rtnGoods.setChkTm(GetCellData(r, "���ʱ��").replaceAll("[^0-9:-]", " "));// ���ʱ��
				}
				rtnGoods.setIsNtBllg(new Double(GetCellData(r, "�Ƿ�Ʊ")).intValue());// �Ƿ�Ʊ
				rtnGoods.setMemo(GetCellData(r, "��ͷ��ע"));// ����ע
				rtnGoods.setIsNtMakeVouch(new Double(GetCellData(r, "�Ƿ�����ƾ֤")).intValue());
				List<RtnGoodsSub> rtnGoodsSubList = rtnGoods.getRtnGoodsSub();// �˻����ӱ�
				if (rtnGoodsSubList == null) {
					rtnGoodsSubList = new ArrayList<>();
				}
				RtnGoodsSub rtnGoodsSub = new RtnGoodsSub();
				rtnGoodsSub.setWhsEncd(GetCellData(r, "�ֿ����"));// �ֿ����
//				rtnGoodsSub.setWhsNm(GetCellData(r, "�ֿ�����"));// �ֿ�����
				rtnGoodsSub.setInvtyEncd(GetCellData(r, "�������"));// �������
				rtnGoodsSub.setQty(GetBigDecimal(GetCellData(r, "����"), 8));// 8��ʾС��λ�� //����
				rtnGoodsSub.setCntnTaxUprc(GetBigDecimal(GetCellData(r, "��˰����"), 8));// 8��ʾС��λ�� //��˰����
				rtnGoodsSub.setNoTaxUprc(GetBigDecimal(GetCellData(r, "��˰����"), 8));// 8��ʾС��λ�� //��˰����
				rtnGoodsSub.setNoTaxAmt(GetBigDecimal(GetCellData(r, "��˰���"), 8));// 8��ʾС��λ�� //��˰���
				rtnGoodsSub.setTaxAmt(GetBigDecimal(GetCellData(r, "˰��"), 8));// 8��ʾС��λ�� //˰��
				rtnGoodsSub.setPrcTaxSum(GetBigDecimal(GetCellData(r, "��˰�ϼ�"), 8));// 8��ʾС��λ�� //��˰�ϼ�
				rtnGoodsSub.setTaxRate(GetBigDecimal(GetCellData(r, "˰��"), 8));// 8��ʾС��λ�� //˰��
				rtnGoodsSub.setBxQty(GetBigDecimal(GetCellData(r, "����"), 8));// 8��ʾС��λ�� //����
				rtnGoodsSub.setBxRule(GetBigDecimal(GetCellData(r, "���"), 8));// 8��ʾС��λ�� //���
				rtnGoodsSub.setBatNum(GetCellData(r, "����"));// ����
				rtnGoodsSub.setIntlBat(GetCellData(r, "��������"));// ��������
				if (GetCellData(r, "��������") == null || GetCellData(r, "��������").equals("")) {
					rtnGoodsSub.setPrdcDt(null);
				} else {
					rtnGoodsSub.setPrdcDt(GetCellData(r, "��������").replaceAll("[^0-9:-]", " "));// ��������
				}
				rtnGoodsSub.setBaoZhiQi(new Double(GetCellData(r, "������")).intValue());// ������
				if (GetCellData(r, "ʧЧ����") == null || GetCellData(r, "ʧЧ����").equals("")) {
					rtnGoodsSub.setInvldtnDt(null);
				} else {
					rtnGoodsSub.setInvldtnDt(GetCellData(r, "ʧЧ����").replaceAll("[^0-9:-]", " "));// ʧЧ����
				}
				rtnGoodsSub.setProjEncd(GetCellData(r, "��Ŀ����"));// ��Ŀ����
				rtnGoodsSub.setProjNm(GetCellData(r, "��Ŀ����"));// ��Ŀ����
				rtnGoodsSub.setDiscntRatio(GetCellData(r, "�ۿ۱���"));// �ۿ۱���
				rtnGoodsSub.setUnBllgQty(GetBigDecimal(GetCellData(r, "δ��Ʊ����"), 8));
				rtnGoodsSub.setMemo(GetCellData(r, "���屸ע"));// �ӱ�ע

				rtnGoodsSubList.add(rtnGoodsSub);
				rtnGoods.setRtnGoodsSub(rtnGoodsSubList);
				temp.put(orderNo, rtnGoods);
			}
			fileIn.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("�ļ��ĵ�" + j + "�е����ʽ�����޷�����!" + e.getMessage());
		}
		return temp;
	}

	// ����excle
	private Map<String, RtnGoods> uploadScoreInfoU8(MultipartFile file) {
		Map<String, RtnGoods> temp = new HashMap<>();
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
			// ����������
			getCellNames();
			// ��Sheet�е�ÿһ�н��е���
			for (int i = firstRowNum + 1; i <= lastRowNum; i++) {
				j++;
				Row r = sht0.getRow(i);
				// �����ǰ�е��кţ���0��ʼ��δ�ﵽ2�������У������ѭ��
				if (r.getRowNum() < 1) {
					continue;
				}
				String orderNo = GetCellData(r, "��������");
				// ����ʵ����
				RtnGoods sellSngl = new RtnGoods();
				if (temp.containsKey(orderNo)) {
					sellSngl = temp.get(orderNo);
				}

				// ȡ����ǰ�е�1����Ԫ�����ݣ�����װ��pursOrdrʵ��ĸ���������
				sellSngl.setRtnGoodsId(orderNo); // '���۵�����', varchar(200
				if (GetCellData(r, "��������") == null || GetCellData(r, "��������").equals("")) {
					sellSngl.setRtnGoodsDt(null);
				} else {
					sellSngl.setRtnGoodsDt(GetCellData(r, "��������").replaceAll("[^0-9:-]", " "));// ���۵�����
				}

				sellSngl.setAccNum(GetCellData(r, "ҵ��Ա����")); // ҵ��Ա����', varchar(200
				sellSngl.setUserName(GetCellData(r, "ҵ �� Ա")); // ҵ��Ա���� varchar(200
				sellSngl.setCustId(GetCellData(r, "�ͻ�����")); // '�ͻ�����', varchar(200
				sellSngl.setDeptId(GetCellData(r, "���ű���")); // '���ű���', varchar(200
				sellSngl.setSellTypId("1"); // �������ͱ���
				String bizTypId = null;

				if (GetCellData(r, "��������") != null && GetCellData(r, "��������").equals("B2B")) {
					bizTypId = "1";
				} else if (GetCellData(r, "��������") != null && GetCellData(r, "��������").equals("B2C")) {
					bizTypId = "2";
				}
				sellSngl.setFormTypEncd("008");// �������ͱ���

				sellSngl.setBizTypId(bizTypId); // ҵ�����ͱ���
//				007	���۵�
//				008	�˻���

//				if (GetCellData(r, "�˻���ʶ") != null && GetCellData(r, "�˻���ʶ").equals("��")) {
//					sellSngl.setFormTypEncd("007");// �������ͱ���
//				} else if (GetCellData(r, "�˻���ʶ") != null && GetCellData(r, "�˻���ʶ").equals("��")) {
//					sellSngl.setFormTypEncd("008");// �������ͱ���
//				}

//					sellSngl.setRecvSendCateId(GetCellData(r,"�շ�������")); // '�շ�������', varchar(200

//				sellSngl.setDelvAddr(GetCellData(r,"������ַ")); // '������ַ', varchar(200
				sellSngl.setTxId(GetCellData(r, "���ױ��")); // '���ױ���(��������', varchar(200
				sellSngl.setIsNtBllg(0); // '�Ƿ�Ʊ', int(11
				sellSngl.setIsNtChk(1); // '�Ƿ����', int(11
				sellSngl.setChkr(GetCellData(r, "�����")); // '�����', varchar(200
				if (GetCellData(r, "���ʱ��") == null || GetCellData(r, "���ʱ��").equals("")) {
					sellSngl.setChkTm(null);
				} else {
					sellSngl.setChkTm(GetCellData(r, "���ʱ��").replaceAll("[^0-9:-]", " "));// ���ʱ��
				}
//				sellSngl.setIsNtBookEntry(0); // '�Ƿ����', int(11
//					sellSngl.setChkr(GetCellData(r, "������")); // '�����', varchar(200
//					if (GetCellData(r, "����ʱ��") == null || GetCellData(r, "����ʱ��").equals("")) {
//						sellSngl.setChkTm(null);
//					} else {
//						sellSngl.setChkTm(GetCellData(r, "����ʱ��").replaceAll("[^0-9:-]", " "));// ���ʱ��
//					}

				sellSngl.setSetupPers(GetCellData(r, "�Ƶ���")); // '������', varchar(200
				if (GetCellData(r, "�Ƶ�ʱ��") == null || GetCellData(r, "�Ƶ�ʱ��").equals("")) {
					sellSngl.setSetupTm(null);
				} else {
					sellSngl.setSetupTm(GetCellData(r, "�Ƶ�ʱ��").replaceAll("[^0-9:-]", " "));// ����ʱ��
				}
				sellSngl.setMdfr(GetCellData(r, "�޸���")); // '�޸���', varchar(200
				if (GetCellData(r, "�޸�ʱ��") == null || GetCellData(r, "�޸�ʱ��").equals("")) {
					sellSngl.setModiTm(null);
				} else {
					sellSngl.setModiTm(GetCellData(r, "�޸�ʱ��").replaceAll("[^0-9:-]", " "));// ����ʱ��
				}
				sellSngl.setMemo(GetCellData(r, "�� ע")); // '��ע', varchar(2000
//				sellSngl.setIsPick(0); // '�Ƿ���', int(2
//				sellSngl.setEcOrderId(GetCellData(r, "�ͻ�������")); // '���̶�����', varchar(200)
				sellSngl.setRecvr(GetCellData(r, "�ջ���ϵ��")); // '�ռ���', varchar(200)
				sellSngl.setRecvrTel(GetCellData(r, "�ջ���ϵ���ֻ�")); // '�ռ��˵绰'
				sellSngl.setRecvrAddr(GetCellData(r, "������ַ")); // '�ռ��˵�ַ'
//				sellSngl.setRecvrEml(GetCellData(r, "�ռ�������")); // '�ռ�������'
				sellSngl.setBuyerNote(GetCellData(r, "�������")); // '�������'
//				sellSngl.setDeliverSelf(1); // '�Ƿ��Է���'
				sellSngl.setCustOrdrNum(GetCellData(r, "�ͻ�������")); // '�ͻ�������'
				sellSngl.setIsNtMakeVouch(0);

				List<RtnGoodsSub> sellSnglSubist = sellSngl.getRtnGoodsSub();// ���۶����ӱ�
				if (sellSnglSubist == null) {
					sellSnglSubist = new ArrayList<>();
				}
				RtnGoodsSub sellSnglSub = new RtnGoodsSub();
//				sellSnglSub.setOrdrNum(Long.parseLong(GetCellData(r, "�������ӱ�ID")));
				sellSnglSub.setWhsEncd(GetCellData(r, "�ֿ���")); // '�ֿ����',
				sellSnglSub.setInvtyEncd(GetCellData(r, "�������")); // '�������',
				sellSnglSub.setRtnGoodsId(orderNo); // '���۵�����',
//				if (GetCellData(r, "��������") == null || GetCellData(r, "��������").equals("")) {
//					sellSnglSub.setExpctDelvDt(null);
//				} else {
//					sellSnglSub.setExpctDelvDt(GetCellData(r, "��������").replaceAll("[^0-9:-]", " "));// Ԥ�Ʒ�������
//				}

				sellSnglSub.setQty(GetBigDecimal(GetCellData(r, "����"), 8)); // '����',
				sellSnglSub.setUnBllgQty(GetBigDecimal(GetCellData(r, "δ��Ʊ����"), 8).abs());// 'δ��Ʊ����',
				sellSnglSub.setBxQty(GetBigDecimal(GetCellData(r, "����"), 8)); // '����',
				sellSnglSub.setCntnTaxUprc(GetBigDecimal(GetCellData(r, "��˰����"), 8)); // '��˰����',
				sellSnglSub.setPrcTaxSum(GetBigDecimal(GetCellData(r, "��˰�ϼ�"), 8)); // '��˰�ϼ�',
				sellSnglSub.setNoTaxUprc(GetBigDecimal(GetCellData(r, "��˰����"), 8)); // '��˰����',
				sellSnglSub.setNoTaxAmt(GetBigDecimal(GetCellData(r, "��˰���"), 8)); // '��˰���',
				sellSnglSub.setTaxAmt(GetBigDecimal(GetCellData(r, "˰��"), 8)); // '˰��',
				sellSnglSub.setTaxRate(GetBigDecimal(GetCellData(r, "��ͷ˰��"), 8)); // '˰��',
				sellSnglSub.setIntlBat(GetCellData(r, "��������")); // ��������
				sellSnglSub.setBatNum(GetCellData(r, "����")); // '����',
				if (GetCellData(r, "��������") == null || GetCellData(r, "��������").equals("")) {
					sellSnglSub.setPrdcDt(null);
				} else {
					sellSnglSub.setPrdcDt(GetCellData(r, "��������").replaceAll("[^0-9:-]", " "));// �ƻ�����ʱ��
				}
				if (GetCellData(r, "ʧЧ����") == null || GetCellData(r, "ʧЧ����").equals("")) {
					sellSnglSub.setInvldtnDt(null);
				} else {
					sellSnglSub.setInvldtnDt(GetCellData(r, "ʧЧ����").replaceAll("[^0-9:-]", " "));// �ƻ�����ʱ��
				}

				sellSnglSub.setBaoZhiQi(new Double(GetCellData(r, "������")).intValue()); // '������',

				if (GetCellData(r, "�˻���ʶ") != null && GetCellData(r, "�˻���ʶ").equals("��")) {
					sellSnglSub.setIsNtRtnGoods(0); // '�˻���ʶ',
				} else if (GetCellData(r, "�˻���ʶ") != null && GetCellData(r, "�˻���ʶ").equals("��")) {
					sellSnglSub.setIsNtRtnGoods(1); // '�˻���ʶ',
				}
				if (GetCellData(r, "��Ʒ") != null && GetCellData(r, "��Ʒ").equals("��")) {
					sellSnglSub.setIsComplimentary(0); // '�Ƿ���Ʒ',
				} else if (GetCellData(r, "��Ʒ") != null && GetCellData(r, "��Ʒ").equals("��")) {
					sellSnglSub.setIsComplimentary(1); // '�Ƿ���Ʒ',
				}

				sellSnglSub.setMemo(GetCellData(r, "���屸ע")); // '��ע',
//				sellSnglSub.setRtnblQty(GetBigDecimal(GetCellData(r, "����"), 8)); // '��������'
//				sellSnglSub.setHadrtnQty(BigDecimal.ZERO); // '��������'
				sellSnglSub.setProjEncd(GetCellData(r, "��Ŀ����")); // '��Ŀ����'
				sellSnglSub.setProjNm(GetCellData(r, "��Ŀ����")); // '��Ŀ����'
				sellSnglSub.setDiscntRatio("0"); // '�ۿ۱���'
				sellSnglSubist.add(sellSnglSub);

				sellSngl.setRtnGoodsSub(sellSnglSubist);
				temp.put(orderNo, sellSngl);
			}
			fileIn.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("�ļ��ĵ�" + j + "�е����ʽ�����޷�����!" + e.getMessage());
		}
		return temp;
	}

	// ��ҳ+����
	@Override
	public String queryRtnGoodsListOrderBy(Map map) {
		String resp = "";
		List<String> rtnGoodsIdList = getList((String) map.get("rtnGoodsId"));// �˻�������
		List<String> custIdList = getList((String) map.get("custId"));// �ͻ�����
		List<String> accNumList = getList((String) map.get("accNum"));// ҵ��Ա����
		List<String> invtyClsEncdList = getList((String) map.get("invtyClsEncd"));// ����������
		List<String> invtyCdList = getList((String) map.get("invtyCd"));// �������
		List<String> deptIdList = getList((String) map.get("deptId"));// ���ű���
		List<String> whsEncdList = getList((String) map.get("whsEncd"));// �ֿ����
		List<String> custOrdrNumList = getList((String) map.get("custOrdrNum"));// �ͻ�������
		List<String> invtyEncdList = getList((String) map.get("invtyEncd"));// �������
		List<String> batNumList = getList((String) map.get("batNum"));// ����

		map.put("rtnGoodsIdList", rtnGoodsIdList);
		map.put("custIdList", custIdList);
		map.put("accNumList", accNumList);
		map.put("invtyClsEncdList", invtyClsEncdList);
		map.put("invtyCdList", invtyCdList);
		map.put("deptIdList", deptIdList);
		map.put("whsEncdList", whsEncdList);
		map.put("custOrdrNumList", custOrdrNumList);
		map.put("invtyEncdList", invtyEncdList);
		map.put("batNumList", batNumList);
		List<?> poList;
		if (map.get("sort") == null || map.get("sort").equals("") || map.get("sortOrder") == null
				|| map.get("sortOrder").equals("")) {
			map.put("sort", "rg.rtn_goods_dt");
			map.put("sortOrder", "desc");
		}
		poList = rgd.selectRtnGoodsListOrderBy(map);
		Map tableSums = rgd.selectRtnGoodsListSums(map);
		if (null != tableSums) {
			DecimalFormat df = new DecimalFormat("#,##0.00");
			for (Map.Entry entry : (Set<Entry>) tableSums.entrySet()) {
				String s = df.format((BigDecimal) entry.getValue() == null ? new BigDecimal(0.00) : entry.getValue());
				entry.setValue(s);
			}
		}
		int count = rgd.selectRtnGoodsCount(map);

		int pageNo = Integer.parseInt(map.get("pageNo").toString());
		int pageSize = Integer.parseInt(map.get("pageSize").toString());
		int listNum = poList.size();
		int pages = count / pageSize + 1;

		try {
			resp = BaseJson.returnRespList("purc/RtnGoods/queryRtnGoodsListOrderBy", true, "��ѯ�ɹ���", count, pageNo,
					pageSize, listNum, pages, poList, tableSums);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	public static class zizhu {
		// ����

		@JsonProperty("�˻�������")
		public String rtnGoodsId;// �˻����������
		@JsonProperty("�˻�������")
		public String rtnGoodsDt;// �˻�����
		@JsonProperty("�������ͱ���")
		public String sellTypId;// �������ͱ���
		@JsonProperty("������������")
		public String sellTypNm;// ������������
		@JsonProperty("ҵ�����ͱ���")
		public String bizTypId;// ҵ�����ͱ���
		@JsonProperty("ҵ����������")
		public String bizTypNm;// ҵ����������
		@JsonProperty("�շ�������")
		public String recvSendCateId;// �շ�������
		@JsonProperty("�շ��������")
		public String recvSendCateNm;// �շ��������
		@JsonProperty("�������ͱ���")
		public String formTypEncd;// �������ͱ���
		@JsonProperty("������������")
		public String formTypName;// ������������
		@JsonProperty("�ͻ�������")
		public String custOrdrNum;// �ͻ�������
		@JsonProperty("�ͻ�����")
		public String custId;// �ͻ�����
		@JsonProperty("�ͻ�����")
		public String custNm;// �ͻ�����
		@JsonProperty("�û�����")
		public String accNum;// �û�����
		@JsonProperty("�û�����")
		public String userName;// �û�����
		@JsonProperty("���ű���")
		public String deptId;// ���ű���
		@JsonProperty("��������")
		public String deptName;// ��������
		@JsonProperty("������")
		public String setupPers;// ������
		@JsonProperty("����ʱ��")
		public String setupTm;// ����ʱ��
		@JsonProperty("�Ƿ����")
		public Integer isNtChk;// �Ƿ����
		@JsonProperty("�����")
		public String chkr;// �����
		@JsonProperty("���ʱ��")
		public String chkTm;// ���ʱ��
		@JsonProperty("�޸���")
		public String mdfr;// �޸���
		@JsonProperty("�޸�ʱ��")
		public String modiTm;// �޸�ʱ��
		@JsonProperty("��ͷ��ע")
		public String memo;// ��ע
		@JsonProperty("������ַ����")
		public String delvAddrNm;// ������ַ����
		@JsonProperty("���۵���")
		public String sellOrdrId;// ���۵���
		@JsonProperty("�Ƿ�Ʊ")
		public Integer isNtBllg;// �Ƿ�Ʊ
		@JsonProperty("�ռ���")
		public String recvr;// �ռ���
		@JsonProperty("�ռ��绰")
		public String recvrTel;// �ռ��绰
		@JsonProperty("�ռ��˵�ַ")
		public String recvrAddr;// �ռ��˵�ַ
		@JsonProperty("�ռ�������")
		public String recvrEml;// �ռ�������
		@JsonProperty("�������")
		public String buyerNote;// �������
		@JsonProperty("�˿����")
		public String refId;// �˿����
		@JsonProperty("���ױ���")
		public String txId;// ���ױ���
		@JsonProperty("��Դ�������ͱ���")
		public String toFormTypEncd;// ��Դ�������ͱ���
		@JsonProperty("�Ƿ�����ƾ֤")
		public Integer isNtMakeVouch;// �Ƿ�����ƾ֤
		@JsonProperty("��ƾ֤��")
		public String makVouchPers;// ��ƾ֤��
		@JsonProperty("��ƾ֤ʱ��")
		public String makVouchTm;// ��ƾ֤ʱ��
		@JsonProperty("���")
		public Long ordrNum;// ���
		@JsonProperty("�������")
		public String invtyEncd;// �������
		@JsonProperty("�������")
		public String invtyNm;// �������
		@JsonProperty("����ͺ�")
		public String spcModel;// ����ͺ�
		@JsonProperty("�������")
		public String invtyCd;// �������
		@JsonProperty("���")
		public BigDecimal bxRule;// ���
		@JsonProperty("�ֿ����")
		public String whsEncd;// �ֿ����
		@JsonProperty("�ֿ�����")
		public String whsNm;// �ֿ�����
		@JsonProperty("����")
		public BigDecimal qty;// ����
		@JsonProperty("����")
		public BigDecimal bxQty;// ����
		@JsonProperty("��˰����")
		public BigDecimal noTaxUprc;// ��˰����
		@JsonProperty("��˰���")
		public BigDecimal noTaxAmt;// ��˰���
		@JsonProperty("˰��")
		public BigDecimal taxRate;// ˰��
		@JsonProperty("˰��")
		public BigDecimal taxAmt;// ˰��
		@JsonProperty("��˰����")
		public BigDecimal cntnTaxUprc;// ��˰����
		@JsonProperty("��˰�ϼ�")
		public BigDecimal prcTaxSum;// ��˰�ϼ�
		@JsonProperty("��������")
		public String prdcDt;// ��������
		@JsonProperty("������")
		public Integer baoZhiQi;// ������
		@JsonProperty("ʧЧ����")
		public String invldtnDt;// ʧЧ����
		@JsonProperty("����")
		public String batNum;// ����
		@JsonProperty("��������")
		public String intlBat;// ��������
		@JsonProperty("�Ƿ��˻�")
		public Integer isNtRtnGoods;// �Ƿ��˻�
//		@JsonProperty("�Ƿ���Ʒ")
//		public Integer isComplimentary;//�Ƿ���Ʒ
		@JsonProperty("��Ŀ����")
		public String projEncd;// ��Ŀ����
		@JsonProperty("��Ŀ����")
		public String projNm;// ��Ŀ����
		@JsonProperty("��Ӧ������")
		public String crspdBarCd;// ��Ӧ������
		@JsonProperty("δ��Ʊ����")
		public BigDecimal unBllgQty;// δ��Ʊ����
		@JsonProperty("�ۿ۱���")
		public String discntRatio;// �ۿ۱���
		@JsonProperty("����˰��")
		public BigDecimal iptaxRate;// ����˰��
		@JsonProperty("����˰��")
		public BigDecimal optaxRate;// ����˰��
		@JsonProperty("��߽���")
		public BigDecimal highestPurPrice;// ��߽���
		@JsonProperty("����ۼ�")
		public BigDecimal loSellPrc;// ����ۼ�
		@JsonProperty("�ο��ɱ�")
		public BigDecimal refCost;// �ο��ɱ�
		@JsonProperty("�ο��ۼ�")
		public BigDecimal refSellPrc;// �ο��ۼ�
		@JsonProperty("���³ɱ�")
		public BigDecimal ltstCost;// ���³ɱ�
		@JsonProperty("������λ����")
		public String measrCorpNm;// ������λ����
		@JsonProperty("��Դ�����ӱ��ʶ")
		public Long toOrdrNum;// ��Դ�����ӱ��ʶ
		@JsonProperty("���屸ע")
		public String memos;// ���屸ע
		@JsonProperty("�˻���ݵ���")
		public String expressNum;// �˻���ݵ���
	}
}
