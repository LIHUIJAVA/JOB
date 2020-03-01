package com.px.mis.purc.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.javassist.expr.NewArray;
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
import com.mysql.cj.log.Log;
import com.px.mis.account.entity.InvtyDetail;
import com.px.mis.ec.dao.LogisticsTabDao;
import com.px.mis.ec.dao.PlatOrderDao;
import com.px.mis.ec.dao.StoreRecordDao;
import com.px.mis.ec.entity.LogisticsTab;
import com.px.mis.ec.entity.PlatOrder;
import com.px.mis.purc.dao.IntoWhsDao;
import com.px.mis.purc.dao.InvtyDocDao;
import com.px.mis.purc.dao.InvtyTabDao;
import com.px.mis.purc.dao.SellOutWhsDao;
import com.px.mis.purc.dao.SellOutWhsSubDao;
import com.px.mis.purc.dao.SellSnglDao;
import com.px.mis.purc.dao.SellSnglSubDao;
import com.px.mis.purc.entity.BeiYong;
import com.px.mis.purc.entity.InvtyDoc;
import com.px.mis.purc.entity.SellOutWhs;
import com.px.mis.purc.entity.SellOutWhsSub;
import com.px.mis.purc.entity.SellSngl;
import com.px.mis.purc.entity.SellSnglSub;
import com.px.mis.purc.service.SellSnglService;
import com.px.mis.purc.util.CalcAmt;
import com.px.mis.util.BaseJson;
import com.px.mis.util.CommonUtil;
import com.px.mis.util.GetOrderNo;
import com.px.mis.util.poiTool;
import com.px.mis.whs.entity.InvtyTab;

/*���۵�����*/
@Transactional
@Service
public class SellSnglServiceImpl extends poiTool implements SellSnglService {
	@Autowired
	private SellSnglDao ssd; // ���۵���
	@Autowired
	private SellSnglSubDao sssd; // ���۵���
	@Autowired
	private SellOutWhsDao sowd; // ���۳�����
	@Autowired
	private SellOutWhsSubDao sowds; // ���۳�����
	@Autowired
	private InvtyTabDao itd; // ����
	@Autowired
	private LogisticsTabDao ltd; // ������
	@Autowired
	private InvtyDocDao idd;// �������
	@Autowired
	private PlatOrderDao platOrderDao;
	@Autowired
	private StoreRecordDao storeRecordDao;
	@Autowired
	private IntoWhsDao intoWhsDao;// �ɹ���ⵥ
	@Autowired
	private InvtyDocDao invtyDocDao;
	// ������
	@Autowired
	private GetOrderNo getOrderNo;

	// �������۵�
	@Override
	public String addSellSngl(String userId, SellSngl sellSngl, List<SellSnglSub> sellSnglSubList, String loginTime) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
//			TreeSet<SellSnglSub> sellSnglSubSet = new TreeSet();
//			sellSnglSubSet.addAll(sellSnglSubList);
//			if (sellSnglSubSet.size() < sellSnglSubList.size()) {
//				resp = BaseJson.returnRespObj("purc/SellSngl/addSellSngl", false, "��������ϸ������ͬһ�ֿ��д���ͬһ�������ͬ���Σ�", null);
//				return resp;
//			}

			// ��ȡ������
			String number = getOrderNo.getSeqNo("XS", userId, loginTime);
			if (ssd.selectSellSnglById(number) != null) {
				message = "���" + number + "�Ѵ��ڣ����������룡";
				isSuccess = false;
			} else {
				sellSngl.setSellSnglId(number);
				if (sellSngl.getDeliverSelf() == null) {
					sellSngl.setDeliverSelf(0);
				}

				for (SellSnglSub sellSnglSub : sellSnglSubList) {
					sellSnglSub.setSellSnglId(sellSngl.getSellSnglId());
					sellSnglSub.setRtnblQty(sellSnglSub.getQty());// ��������
					sellSnglSub.setUnBllgQty(sellSnglSub.getQty());// δ��Ʊ����
					InvtyDoc invtyDoc = invtyDocDao.selectAllByInvtyEncd(sellSnglSub.getInvtyEncd());
					if (invtyDoc.getIsNtSell() == null) {
						isSuccess = false;
						message = "�����۵���Ӧ�Ĵ��:" + sellSnglSub.getInvtyEncd()
								+ "û�������Ƿ��������ԣ��޷����棡";
						throw new RuntimeException(message);
					} else if (invtyDoc.getIsNtSell().intValue() != 1) {
						isSuccess = false;
						message = "�����۵���Ӧ�Ĵ��:" + sellSnglSub.getInvtyEncd()+ "�ǿ����۴�����޷����棡";
						throw new RuntimeException(message);
					}
					if (invtyDoc.getIsQuaGuaPer() == null) {
						isSuccess = false;
						message = "�����۵���Ӧ�Ĵ��:" + sellSnglSub.getInvtyEncd()
								+ "û�������Ƿ����ڹ������ԣ��޷����棡";
						throw new RuntimeException(message);
					} else if(invtyDoc.getIsQuaGuaPer() == 1){
						if (sellSnglSub.getPrdcDt() == "" || sellSnglSub.getPrdcDt() == null) {
							isSuccess = false;
							message = "�����۵���Ӧ�Ĵ��:" + sellSnglSub.getInvtyEncd()
									+ "�Ǳ����ڹ����������������ڣ�";
							throw new RuntimeException(message);
						}
						if (sellSnglSub.getInvldtnDt() == ""|| sellSnglSub.getInvldtnDt() == null) {
							isSuccess = false;
							message = "�����۵���Ӧ�Ĵ��:" + sellSnglSub.getInvtyEncd()
									+ "�Ǳ����ڹ���������ʧЧ���ڣ�";
							throw new RuntimeException(message);
						}
					}
					if (sellSnglSub.getPrdcDt() == "") {
						sellSnglSub.setPrdcDt(null);
					}
					if (sellSnglSub.getInvldtnDt() == "") {
						sellSnglSub.setInvldtnDt(null);
					}
				}
				ssd.insertSellSngl(sellSngl);
				sssd.insertSellSnglSub(sellSnglSubList);
				message = "�����ɹ���";
				isSuccess = true;
			}
			resp = BaseJson.returnRespObj("purc/SellSngl/addSellSngl", isSuccess, message, sellSngl);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		return resp;
	}

	@Override
	public String editSellSngl(SellSngl sellSngl, List<SellSnglSub> sellSnglSubList) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
//			TreeSet<SellSnglSub> sellSnglSubSet = new TreeSet();
//			sellSnglSubSet.addAll(sellSnglSubList);
//			if (sellSnglSubSet.size() < sellSnglSubList.size()) {
//				resp = BaseJson.returnRespObj("purc/SellSngl/editSellSngl", false, "��������ϸ������ͬһ�ֿ��д���ͬһ�������ͬ���Σ�", null);
//				return resp;
//			}
			sssd.deleteSellSnglSubBySellSnglId(sellSngl.getSellSnglId());
			ssd.updateSellSnglBySellSnglId(sellSngl);
			for (SellSnglSub sellSnglSub : sellSnglSubList) {
				sellSnglSub.setSellSnglId(sellSngl.getSellSnglId());
				sellSnglSub.setRtnblQty(sellSnglSub.getQty()); // ��������
				sellSnglSub.setUnBllgQty(sellSnglSub.getQty());// δ��Ʊ����
				InvtyDoc invtyDoc = invtyDocDao.selectAllByInvtyEncd(sellSnglSub.getInvtyEncd());
				if (invtyDoc.getIsNtSell() == null) {
					isSuccess = false;
					message = "�����۵���Ӧ�Ĵ��:" + sellSnglSub.getInvtyEncd()
							+ "û�������Ƿ��������ԣ��޷����棡";
					throw new RuntimeException(message);
				} else if (invtyDoc.getIsNtSell().intValue() != 1) {
					isSuccess = false;
					message = "�����۵���Ӧ�Ĵ��:" + sellSnglSub.getInvtyEncd()+ "�ǿ����۴�����޷����棡";
					throw new RuntimeException(message);
				}
				if (invtyDoc.getIsQuaGuaPer() == null) {
					isSuccess = false;
					message = "�����۵���Ӧ�Ĵ��:" + sellSnglSub.getInvtyEncd()
							+ "û�������Ƿ����ڹ������ԣ��޷����棡";
					throw new RuntimeException(message);
				} else if(invtyDoc.getIsQuaGuaPer() == 1){
					if (sellSnglSub.getPrdcDt() == "" || sellSnglSub.getPrdcDt() == null) {
						isSuccess = false;
						message = "�����۵���Ӧ�Ĵ��:" + sellSnglSub.getInvtyEncd()
								+ "�Ǳ����ڹ����������������ڣ�";
						throw new RuntimeException(message);
					}
					if (sellSnglSub.getInvldtnDt() == ""|| sellSnglSub.getInvldtnDt() == null) {
						isSuccess = false;
						message = "�����۵���Ӧ�Ĵ��:" + sellSnglSub.getInvtyEncd()
								+ "�Ǳ����ڹ���������ʧЧ���ڣ�";
						throw new RuntimeException(message);
					}
				}
				if (sellSnglSub.getPrdcDt() == "") {
					sellSnglSub.setPrdcDt(null);
				}
				if (sellSnglSub.getInvldtnDt() == "") {
					sellSnglSub.setInvldtnDt(null);
				}
			}
			sssd.insertSellSnglSub(sellSnglSubList);
			message = "���³ɹ���";
			resp = BaseJson.returnRespObj("purc/SellSngl/editSellSngl", isSuccess, message, null);
		} catch (IOException e) {
			////e.printStackTrace();
		}
		return resp;
	}

	@Override
	public String deleteSellSngl(String sellSnglId) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		SellSngl sngl = ssd.selectSellSnglBySellSnglId(sellSnglId);
		if (sngl != null && !sngl.getBizTypId().equals("2")) {
			ssd.deleteSellSnglBySellSnglId(sellSnglId);
			isSuccess = true;
			message = "ɾ���ɹ���";
		} else if (sngl.getBizTypId().equals("2")) {
			throw new RuntimeException("�����ֶ�ɾ��B2C�����۵�!");
		} else {
			isSuccess = false;
			message = "���" + sellSnglId + "�����ڣ�";
		}

		try {
			resp = BaseJson.returnRespObj("purc/SellSngl/deleteSellSngl", isSuccess, message, null);
		} catch (IOException e) {
			//e.printStackTrace();
		}
		return resp;
	}

	@Override
	public String querySellSngl(String sellSnglId) {
		// TODO Auto-generated method stub
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		SellSngl sellSngl = ssd.selectSellSnglBySellSnglId(sellSnglId);
		if (sellSngl != null) {
			isSuccess = true;
			message = "��ѯ�ɹ���";
		} else {
			isSuccess = false;
			message = "���" + sellSnglId + "�����ڣ�";
		}
		try {
			resp = BaseJson.returnRespObj("purc/SellSngl/querySellSngl", isSuccess, message, sellSngl);
		} catch (IOException e) {
			//e.printStackTrace();
		}
		return resp;
	}

	@Override
	public String querySellSnglList(Map map) {
		String resp = "";
		List<String> sellSnglIdList = getList(((String) map.get("sellSnglId")));// ���۵���
		List<String> custIdList = getList(((String) map.get("custId")));// �ͻ�����
		List<String> accNumList = getList(((String) map.get("accNum")));// ҵ��Ա����
		List<String> invtyClsEncdList = getList(((String) map.get("invtyClsEncd")));// �������
		List<String> invtyCdList = getList(((String) map.get("invtyCd")));// �������
		List<String> deptIdList = getList(((String) map.get("deptId")));// ���ű���
		List<String> whsEncdList = getList(((String) map.get("whsEncd")));// �ֿ����
		List<String> custOrdrNumList = getList(((String) map.get("custOrdrNum")));// �ͻ�������
		List<String> invtyEncdList = getList(((String) map.get("invtyEncd")));// �������
		List<String> batNumList = getList(((String) map.get("batNum")));// ����
		List<String> intlBatList = getList(((String) map.get("intlBat")));// ��������

		map.put("sellSnglIdList", sellSnglIdList);
		map.put("sellSnglIdList", sellSnglIdList);
		map.put("custIdList", custIdList);
		map.put("accNumList", accNumList);
		map.put("invtyClsEncdList", invtyClsEncdList);
		map.put("invtyCdList", invtyCdList);
		map.put("deptIdList", deptIdList);
		map.put("whsEncdList", whsEncdList);
		map.put("custOrdrNumList", custOrdrNumList);
		map.put("invtyEncdList", invtyEncdList);
		map.put("batNumList", batNumList);
		map.put("intlBatList", intlBatList);
		List<?> poList;
		if (map.containsKey("sort") && map.containsKey("sortOrder")) {
			poList = ssd.selectSellSnglListOrderBy(map);
		} else {
//			poList = ssd.selectSellSnglListOrderBy(map);
			poList = ssd.selectSellSnglList(map);
		}
		int count = ssd.selectSellSnglCount(map);

		int pageNo = Integer.parseInt(map.get("pageNo").toString());// �ڼ�ҳ
		int pageSize = Integer.parseInt(map.get("pageSize").toString());// ÿҳ����
		int listNum = poList.size();
		int pages = count / pageSize + 1;// ��ҳ��

		try {
			resp = BaseJson.returnRespList("purc/SellSngl/querySellSnglList", true, "��ѯ�ɹ���", count, pageNo, pageSize,
					listNum, pages, poList);
		} catch (IOException e) {
			//e.printStackTrace();
		}
		return resp;
	}

	// ����ɾ�����۵�
	@Override
	public String deleteSellSnglList(String sellSnglId) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";

		try {
			SellSngl sngl = ssd.selectSellSnglById(sellSnglId);
			if (sngl.getBizTypId().equals("2")) {
				throw new RuntimeException("�����ֶ�ɾ��B2C�����۵�!");
			}

			List<String> lists = getList(sellSnglId);
			List<String> lists2 = new ArrayList<>();
			List<String> lists3 = new ArrayList<>();

			for (String list : lists) {
				if (ssd.selectSellSnglIsNtChk(list) == 0) {
					lists2.add(list);
				} else {
					lists3.add(list);
				}
			}
			if (lists2.size() > 0) {
				int a = 0;
				try {
					a = deleteSellSnglList(lists2);
				} catch (Exception e) {
					isSuccess = false;
					message += "���ݺ�Ϊ��" + lists2.toString() + "ɾ��ʧ�ܣ�";
					throw new RuntimeException("���ݺ�Ϊ��" + lists2.toString() + "ɾ��ʧ�ܣ�");
				}
				if (a >= 1) {
					isSuccess = true;
					message += "���ݺ�Ϊ��" + lists2.toString() + "�Ķ���ɾ���ɹ�!\n";
				}
			}
			if (lists3.size() > 0) {
				isSuccess = false;
				message += "���ݺ�Ϊ��" + lists3.toString() + "�Ķ����ѱ���ˣ��޷�ɾ����\n";
				throw new RuntimeException(message);
			}
			resp = BaseJson.returnRespObj("purc/SellSngl/deleteSellSnglList", isSuccess, message, null);
		} catch (Exception e) {
			throw new RuntimeException(message);
		}
		return resp;
	}

	@Transactional
	private int deleteSellSnglList(List<String> lists2) {
		ssd.insertSellSnglDl(lists2);
		sssd.insertSellSnglSubDl(lists2);
		int a = ssd.deleteSellSnglList(lists2);
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

	// ������۵�
	@Override
	public Map<String, Object> updateSellSnglIsNtChkList(String userId, SellSngl sellSngl, String loginTime)
			throws Exception {
		StringBuilder message = new StringBuilder();
		Boolean isSuccess = true;
		Map<String, Object> map = new HashMap<>();
		try {

			// ��������Ϣ
			LogisticsTab logisticsTab = new LogisticsTab();
			BigDecimal goodNum = new BigDecimal(0);// ��������
			if (sellSngl.getIsNtChk() == 1) {
//				isSuccess = (Boolean) updateSellSnglIsNtChkOK(logisticsTab,goodNum,userId,sellSngl).get("isSuccess");
				message.append(
						updateSellSnglIsNtChkOK(logisticsTab, goodNum, userId, sellSngl, loginTime).get("message"));
			} else if (sellSngl.getIsNtChk() == 0) {
//				isSuccess = (Boolean) updateSellSnglIsNtChkNO(sellSngl).get("isSuccess");
				message.append(updateSellSnglIsNtChkNO(sellSngl).get("message"));
			} else {
				isSuccess = false;
				message.append("���״̬�����޷���ˣ�\n");
			}
			map.put("isSuccess", isSuccess);
			map.put("message", message.toString());
		} catch (Exception e) {
			//e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return map;
	}

	// ���۵����
	public Map<String, Object> updateSellSnglIsNtChkOK(LogisticsTab logisticsTab, BigDecimal goodNum, String userId,
			SellSngl sngls, String loginTime) throws RuntimeException {
		String message = "";
		Boolean isSuccess = true;
		Map<String, Object> map = new HashMap<>();
		// ͨ�����۵���Ż�ȡ���۵�������Ϣ
		SellSngl sellSngl = ssd.selectSellSnglAndSubBySellSnglId(sngls.getSellSnglId());
		// ͨ�����۵���Ż�ȡ���۵��ӱ���Ϣ
		List<SellSnglSub> sellSnglSubList = sellSngl.getSellSnglSub();
		String isJianInvtyTab = "";
		if (sellSngl.getBizTypId().equals("1")) {
			isJianInvtyTab = "1";
		}
		if (sellSngl.getBizTypId().equals("2")) {
			isJianInvtyTab = "2";
		}

		// �ж����۵������״̬
		if (sellSngl.getIsNtChk() == 0) {
			Map<String, List<SellSnglSub>> handleMap = sellSnglSubList.stream()
					.collect(Collectors.groupingBy(SellSnglSub::getWhsEncd));
			for (Map.Entry<String, List<SellSnglSub>> entry : handleMap.entrySet()) {
				SellOutWhs sellOutWhs = new SellOutWhs();// ���۳��ⵥ����

				String number = getOrderNo.getSeqNo("CK", userId, loginTime);
				try {
					BeanUtils.copyProperties(sellOutWhs, sellSngl);
					// ��ί�д��������Ƹ�����ר�÷�Ʊ
					sellOutWhs.setOutWhsId(number);// ���ⵥ����
//					CommonUtil.getLoginTime(loginTime);
					sellOutWhs.setOutWhsDt(CommonUtil.getLoginTime(loginTime));// ���ⵥ����
					sellOutWhs.setSellOrdrInd(sellSngl.getSellSnglId());// ���۳��ⵥ�ж�Ӧ�����۵�����
					sellOutWhs.setFormTypEncd("009");// �������ͱ���
					sellOutWhs.setToFormTypEncd(sellSngl.getFormTypEncd());// ��Դ��������
					// ͨ��ҵ�������ж��շ����
					if (sellSngl.getBizTypId().equals("1")) {
						sellOutWhs.setRecvSendCateId("7");// �շ����ͱ�ţ���������
					}
					if (sellSngl.getBizTypId().equals("2")) {
						sellOutWhs.setRecvSendCateId("6");// �շ����ͱ�ţ���������
					}
					sellOutWhs.setIsNtRtnGood(0);// �Ƿ��˻�
					sellOutWhs.setOutIntoWhsTypId("10");// ��������ͱ���
					sellOutWhs.setSetupPers(sngls.getChkr());// ������,�����۵��������

					// Date dt = new Date();//����������+ʱ��
					// "2019-11-30"
					Calendar cal = Calendar.getInstance();
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//					SimpleDateFormat.
					// ת����������Ϊ��������
					// �������������ʱ�䲿��
					// ����������󣬸�ʽ������+ʱ��

					sellOutWhs.setSetupTm(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));// ����ʱ��=��ǰʱ��

					List<SellOutWhsSub> sellOutWhsSubList = new ArrayList<>();// ���۳��ⵥ�ӱ���
					for (SellSnglSub sellSnglSub : entry.getValue()) {
						if (sellSnglSub.getShdTaxLabour() == null || sellSnglSub.getIsNtDiscnt() == null) {
							isSuccess = false;
							message += "���Ϊ��" + sellSnglSub.getSellSnglId() + "�����۵���,�����ţ�" + sellSnglSub.getInvtyEncd()
									+ "û������Ӧ˰�������Ի��Ƿ��ۿ����ԣ��޷����\n";
							throw new RuntimeException(message);
						}
						if (sellSnglSub.getShdTaxLabour().intValue() == 1
								|| sellSnglSub.getIsNtDiscnt().intValue() == 1) {
							continue;
						} else {
							goodNum = goodNum.add(sellSnglSub.getQty());
							// ����ʵ��
							if (isJianInvtyTab.equals("1")) {
								InvtyTab invtyTab = new InvtyTab();
								invtyTab.setWhsEncd(sellSnglSub.getWhsEncd());
								invtyTab.setInvtyEncd(sellSnglSub.getInvtyEncd());
								invtyTab.setBatNum(sellSnglSub.getBatNum());
								invtyTab = itd.selectInvtyTabsByTerm(invtyTab);// ����ߵĿ���Ǽ�����
								// ���۵����ʱ�жϿ������Ƿ��иô����Ϣ
								if (invtyTab != null) {
									// a.compareTo(b) -1��ʾС�� 1 ��ʾ���� 0��ʾ���� �Ƚ����۵��������Ϳ���Ŀ�����
									if (invtyTab.getAvalQty().compareTo(sellSnglSub.getQty()) >= 0) {
										SellOutWhsSub sellOutWhsSub = new SellOutWhsSub();// ���۳��ⵥ�ӱ�
										BeanUtils.copyProperties(sellOutWhsSub, sellSnglSub);// �����۵��ӱ��Ƹ����۳��ⵥ
										sellOutWhsSub.setOutWhsId(sellOutWhs.getOutWhsId());// �����۳��ⵥ������븴�Ƹ����۳��ⵥ�ӱ����������
										// ���û���������ںͱ�����ʱ��ʧЧ����Ĭ��Ϊnull
										if (sellSnglSub.getPrdcDt() == null || sellSnglSub.getBaoZhiQi() == null) {
											sellOutWhsSub.setInvldtnDt(null);
										} else {
											// �������۳��ⵥ��ʧЧ����
											sellOutWhsSub.setPrdcDt(sellSnglSub.getPrdcDt());// ��������
											sellOutWhsSub.setBaoZhiQi(sellSnglSub.getBaoZhiQi());// ������
											sellOutWhsSub.setInvldtnDt(CalcAmt.getDate(sellOutWhsSub.getPrdcDt(),
													sellOutWhsSub.getBaoZhiQi()));
										}
										// ��ѯ��������˰����
										// ����ȡ����˰���۸������۳������˰����
										setSellOutWhsCB(invtyTab, sellSnglSub, sellOutWhsSub);
										sellOutWhsSub.setIsNtRtnGoods(0);// �Ƿ��˻�
										sellOutWhsSub.setToOrdrNum(sellSnglSub.getOrdrNum());// ��Դ�����ӱ����
										sellOutWhsSubList.add(sellOutWhsSub);
										invtyTab.setAvalQty(sellSnglSub.getQty());
										itd.updateInvtyTabAvalQtyJian(invtyTab);

									} else {
										isSuccess = false;
										message += "���Ϊ��" + sngls.getSellSnglId() + "�����۵���,�ֿ���룺"
												+ sellSnglSub.getWhsEncd() + ",�����ţ�" + sellSnglSub.getInvtyEncd()
												+ ",����:" + sellSnglSub.getBatNum() + "������������㣬�޷����\n";
										throw new RuntimeException(message);
									}
								} else {
									isSuccess = false;
									message += "���Ϊ��" + sngls.getSellSnglId() + "�����۵���,�ֿ���룺" + sellSnglSub.getWhsEncd()
											+ ",�����ţ�" + sellSnglSub.getInvtyEncd() + ",���Σ�" + sellSnglSub.getBatNum()
											+ "�Ŀ�治���ڣ��޷����\n";
									throw new RuntimeException(message);
								}
							} else {
								InvtyTab invtyTab = new InvtyTab();
								invtyTab.setWhsEncd(sellSnglSub.getWhsEncd());
								invtyTab.setInvtyEncd(sellSnglSub.getInvtyEncd());
								invtyTab.setBatNum(sellSnglSub.getBatNum());
								invtyTab = itd.selectInvtyTabsByTerm(invtyTab);// ��ֹ�����޸�,������
								SellOutWhsSub sellOutWhsSub = new SellOutWhsSub();// ���۳��ⵥ�ӱ�
								BeanUtils.copyProperties(sellOutWhsSub, sellSnglSub);// �����۵��ӱ��Ƹ����۳��ⵥ
								sellOutWhsSub.setOutWhsId(sellOutWhs.getOutWhsId());// �����۳��ⵥ������븴�Ƹ����۳��ⵥ�ӱ����������
								// ���û���������ںͱ�����ʱ��ʧЧ����Ĭ��Ϊnull
								if (sellSnglSub.getPrdcDt() == null || sellSnglSub.getBaoZhiQi() == null) {
									sellOutWhsSub.setInvldtnDt(null);
								} else {
									// �������۳��ⵥ��ʧЧ����
									sellOutWhsSub.setPrdcDt(sellSnglSub.getPrdcDt());// ��������
									sellOutWhsSub.setBaoZhiQi(sellSnglSub.getBaoZhiQi());// ������
									sellOutWhsSub.setInvldtnDt(
											CalcAmt.getDate(sellOutWhsSub.getPrdcDt(), sellOutWhsSub.getBaoZhiQi()));
								}
								// ��ѯ��������˰����
								// ����ȡ����˰���۸������۳������˰����
								setSellOutWhsCB(invtyTab, sellSnglSub, sellOutWhsSub);
								sellOutWhsSub.setIsNtRtnGoods(0);// �Ƿ��˻�
								sellOutWhsSub.setToOrdrNum(sellSnglSub.getOrdrNum());// ��Դ�����ӱ����
								sellOutWhsSubList.add(sellOutWhsSub);
							}
						}
					}
					if (sellOutWhsSubList.size() > 0) {
						logisticsTab.setIsShip(0);// �Ƿ񷢻�
						logisticsTab.setIsBackPlatform(0);// �Ƿ�ش�ƽ̨
						// ��˺�������������Ϣ
						PlatOrder platOrder = new PlatOrder();
						if (sellSngl.getTxId() != null && !sellSngl.getTxId().equals("")) {
							platOrder = platOrderDao.select(sellSngl.getTxId());
							logisticsTab.setDeliverWhs(platOrder.getDeliverWhs());// �����ֿ�
							if (platOrder.getIsShip() == 1) {
								logisticsTab.setExpressCode(platOrder.getExpressNo());// ��ݵ���
								logisticsTab.setIsShip(1);// ����״̬
								logisticsTab.setIsBackPlatform(1);// �Ƿ�ش�ƽ̨
							}
						}
						if (sellSngl.getDeliverSelf() == null) {
							logisticsTab.setDeliverSelf(0);// �Ƿ��Է���
						} else {
							logisticsTab.setDeliverSelf(sellSngl.getDeliverSelf());// �Ƿ��Է���
						}
						if (!sellSngl.getTxId().equals("") && sellSngl.getTxId() != null
								&& sellSngl.getDeliverSelf() == 1) {// ��Ҫ�Է���
							logisticsTab.setExpressEncd(platOrder.getExpressCode());// ��ݹ�˾����
							logisticsTab.setTemplateId(platOrder.getExpressTemplate());// ��ӡģ��
							logisticsTab.setStoreId(platOrder.getStoreId());// ���̱���
							logisticsTab.setPlatId(storeRecordDao.select(platOrder.getStoreId()).getEcId());// ƽ̨����
						}
						logisticsTab.setSaleEncd(sellSngl.getSellSnglId());// ���۵���
						logisticsTab.setOrderId(sellSngl.getTxId());// ������� ȥ���۵��еĽ��ױ��
						logisticsTab.setOutWhsId(sellOutWhs.getOutWhsId());// ���۳��ⵥ��
						logisticsTab.setEcOrderId(sellSngl.getEcOrderId());// ���̶�����

						logisticsTab.setIsPick(0);// �Ƿ������
						// ������Ʒ����
						logisticsTab.setBuyerNote(sellSngl.getBuyerNote());// �������
						logisticsTab.setRecAddress(sellSngl.getRecvrAddr());// �ջ�����ϸ��ַ
						logisticsTab.setRecName(sellSngl.getRecvr());// �ռ�������
						logisticsTab.setRecMobile(sellSngl.getRecvrTel());// �ռ����ֻ���
						logisticsTab.setBizTypId(sellSngl.getBizTypId());// ҵ�����ͱ��
						logisticsTab.setSellTypId(sellSngl.getSellTypId());// �������ͱ��
						logisticsTab.setRecvSendCateId(sellSngl.getRecvSendCateId());// �շ������
						logisticsTab.setGoodNum(goodNum);
						goodNum = new BigDecimal(0);
						ltd.insert(logisticsTab);
						sowd.insertSellOutWhs(sellOutWhs);
						sowds.insertSellOutWhsSub(sellOutWhsSubList);
						sellOutWhsSubList.clear();
					}
				} catch (IllegalAccessException | InvocationTargetException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
			}
			int updateFlag = ssd.updateSellSnglIsNtChk(sngls);
			if (updateFlag == 1) {
				isSuccess = true;
				message += "���Ϊ��" + sngls.getSellSnglId() + "�����۵���˳ɹ���\n";
			} else {
				isSuccess = false;
				message += "���Ϊ��" + sngls.getSellSnglId() + "�����۵�����ˣ������ظ����\n";
				throw new RuntimeException(message);
			}
		} else {
			isSuccess = false;
			message += "���Ϊ��" + sngls.getSellSnglId() + "�����۵�����ˣ������ظ����\n";
			throw new RuntimeException(message);
		}
		map.put("isSuccess", isSuccess);
		map.put("message", message);
		return map;
	}

	private void setSellOutWhsCB(InvtyTab invtyTab, SellSnglSub sellSnglSub, SellOutWhsSub sellOutWhsSub) {
		Map<String, Object> map = new HashMap<>();
		if (invtyTab.getUnTaxUprc() == null) {
			map.put("invtyEncd", sellOutWhsSub.getInvtyEncd());
			map.put("whsEncd", sellOutWhsSub.getWhsEncd());
			map.put("batNum", sellOutWhsSub.getBatNum());
			BigDecimal pursunTaxUprc = intoWhsDao.selectUnTaxUprc(map);// �ɹ���ⵥ���һ����ⵥ��
			if (pursunTaxUprc == null) {
				BigDecimal noTaxUprc = idd.selectRefCost(sellOutWhsSub.getInvtyEncd());// ȡ������������һ�βο��ɱ�
				sellOutWhsSub.setNoTaxUprc(noTaxUprc);
				sellOutWhsSub.setTaxRate((sellSnglSub.getTaxRate()));// ˰��
				// ����δ˰��� ���=δ˰����*δ˰����
				sellOutWhsSub.setNoTaxAmt(CalcAmt.noTaxAmt(sellOutWhsSub.getNoTaxUprc(), sellOutWhsSub.getQty()));
				// ����˰�� ˰��=δ˰���*˰��
				sellOutWhsSub.setTaxAmt(
						CalcAmt.taxAmt(sellOutWhsSub.getNoTaxUprc(), sellOutWhsSub.getQty(), sellOutWhsSub.getTaxRate())
								.divide(new BigDecimal(100)));
				// ���㺬˰���� ��˰����=��˰����*˰��+��˰����
				sellOutWhsSub.setCntnTaxUprc(CalcAmt.cntnTaxUprc(sellOutWhsSub.getNoTaxUprc(), sellOutWhsSub.getQty(),
						sellOutWhsSub.getTaxRate().divide(new BigDecimal(100))));
				// �����˰�ϼ� ��˰�ϼ�=��˰���*˰��+��˰���=˰��+��˰���
				sellOutWhsSub.setPrcTaxSum(CalcAmt.prcTaxSum(sellOutWhsSub.getNoTaxUprc(), sellOutWhsSub.getQty(),
						sellOutWhsSub.getTaxRate().divide(new BigDecimal(100))));
			} else {
				sellOutWhsSub.setNoTaxUprc(pursunTaxUprc);
				sellOutWhsSub.setTaxRate((sellSnglSub.getTaxRate()));// ˰��
				// ����δ˰��� ���=δ˰����*δ˰����
				sellOutWhsSub.setNoTaxAmt(CalcAmt.noTaxAmt(sellOutWhsSub.getNoTaxUprc(), sellOutWhsSub.getQty()));
				// ����˰�� ˰��=δ˰���*˰��
				sellOutWhsSub.setTaxAmt(
						CalcAmt.taxAmt(sellOutWhsSub.getNoTaxUprc(), sellOutWhsSub.getQty(), sellOutWhsSub.getTaxRate())
								.divide(new BigDecimal(100)));
				// ���㺬˰���� ��˰����=��˰����*˰��+��˰����
				sellOutWhsSub.setCntnTaxUprc(CalcAmt.cntnTaxUprc(sellOutWhsSub.getNoTaxUprc(), sellOutWhsSub.getQty(),
						sellOutWhsSub.getTaxRate().divide(new BigDecimal(100))));
				// �����˰�ϼ� ��˰�ϼ�=��˰���*˰��+��˰���=˰��+��˰���
				sellOutWhsSub.setPrcTaxSum(CalcAmt.prcTaxSum(sellOutWhsSub.getNoTaxUprc(), sellOutWhsSub.getQty(),
						sellOutWhsSub.getTaxRate().divide(new BigDecimal(100))));
			}
		} else {
			sellOutWhsSub.setNoTaxUprc(invtyTab.getUnTaxUprc());
			sellOutWhsSub.setTaxRate((sellSnglSub.getTaxRate()));// ˰��
			// ����δ˰��� ���=δ˰����*δ˰����
			sellOutWhsSub.setNoTaxAmt(CalcAmt.noTaxAmt(sellOutWhsSub.getNoTaxUprc(), sellOutWhsSub.getQty()));
			// ����˰�� ˰��=δ˰���*˰��
			sellOutWhsSub.setTaxAmt(
					CalcAmt.taxAmt(sellOutWhsSub.getNoTaxUprc(), sellOutWhsSub.getQty(), sellOutWhsSub.getTaxRate())
							.divide(new BigDecimal(100)));
			// ���㺬˰���� ��˰����=��˰����*˰��+��˰����
			sellOutWhsSub.setCntnTaxUprc(CalcAmt.cntnTaxUprc(sellOutWhsSub.getNoTaxUprc(), sellOutWhsSub.getQty(),
					sellOutWhsSub.getTaxRate().divide(new BigDecimal(100))));
			// �����˰�ϼ� ��˰�ϼ�=��˰���*˰��+��˰���=˰��+��˰���
			sellOutWhsSub.setPrcTaxSum(CalcAmt.prcTaxSum(sellOutWhsSub.getNoTaxUprc(), sellOutWhsSub.getQty(),
					sellOutWhsSub.getTaxRate().divide(new BigDecimal(100))));
		}

	}

	// ���۵�����
	@SuppressWarnings("deprecation")
	private Map<String, Object> updateSellSnglIsNtChkNO(SellSngl sngls) {
		String message = "";
		Boolean isSuccess = true;
		Map<String, Object> map = new HashMap<>();
		// ͨ�����۵���Ż�ȡ���۵�������Ϣ
		SellSngl sellSngl = ssd.selectSellSnglAndSubBySellSnglId(sngls.getSellSnglId());
		String isJiaInvtyTab = "";
		if (sellSngl.getBizTypId().equals("1")) {
			isJiaInvtyTab = "1";
		}
		if (sellSngl.getBizTypId().equals("2")) {
			isJiaInvtyTab = "2";
		}
		// �����ж����۵������״̬�����״̬Ϊ1ʱ�ſ���ִ����������
		if (sellSngl.getIsNtChk() == 1) {
			// �������۱�ʶ��ѯ���۳��ⵥ�����״̬ �� �Ƿ���
			if (sellSngl.getIsPick() == 0) {
				List<SellOutWhs> sellOutWhsList = sowd.selectOutWhsIdBySellOrdrInd(sngls.getSellSnglId());
				int isNtChkQty = 0;
				for (SellOutWhs sellOutWhs : sellOutWhsList) {
					if (sellOutWhs.getIsNtChk() == 1) {
						isNtChkQty++;
					}
				}
				if (isNtChkQty > 0) {
					isSuccess = false;
					message += "���Ϊ��" + sngls.getSellSnglId() + "�����۵���Ӧ�����ε��ݡ����۳��ⵥ������ˣ��޷�����\n";
					throw new RuntimeException(message);

				} else {

					// ͨ�����۵���Ż�ȡ���۵��ӱ���Ϣ
					List<SellSnglSub> sellSnglSubList = sellSngl.getSellSnglSub();

					if (isJiaInvtyTab.equals("1")) {
						for (SellSnglSub selSnSubs : sellSnglSubList) {
							if (selSnSubs.getShdTaxLabour() == null || selSnSubs.getIsNtDiscnt() == null) {
								isSuccess = false;
								message += "���Ϊ��" + selSnSubs.getSellSnglId() + "�����۵���,�����ţ�" + selSnSubs.getInvtyEncd()
										+ "û������Ӧ˰�������Ի��Ƿ��ۿ����ԣ��޷�����\n";
								throw new RuntimeException(message);
							}
							if (selSnSubs.getShdTaxLabour().intValue() == 1
									|| selSnSubs.getIsNtDiscnt().intValue() == 1) {
								continue;
							} else {
								// ����ʵ��
								InvtyTab invtyTab = new InvtyTab();
								invtyTab.setWhsEncd(selSnSubs.getWhsEncd());
								invtyTab.setInvtyEncd(selSnSubs.getInvtyEncd());
								invtyTab.setBatNum(selSnSubs.getBatNum());
								invtyTab = itd.selectInvtyTabsByTerm(invtyTab);// ��ֹ�����޸�,������
								if (invtyTab != null) {
									invtyTab.setAvalQty(selSnSubs.getQty());
									itd.updateInvtyTabAvalQtyJia(invtyTab);
								} else {
									isSuccess = false;
									message += "���Ϊ��" + sngls.getSellSnglId() + "�����۵���,�ֿ���룺" + selSnSubs.getWhsEncd()
											+ ",�����ţ�" + selSnSubs.getInvtyEncd() + ",���Σ�" + selSnSubs.getBatNum()
											+ "�Ŀ�治���ڣ��޷�����\n";
									throw new RuntimeException(message);
								}
							}
						}
					}
					int updateFlag = ssd.updateSellSnglIsNtChk(sngls);
					if (updateFlag == 1) {
						isSuccess = true;
						message += "���Ϊ��" + sngls.getSellSnglId() + "�����۵�����ɹ�\n";
					} else {
						isSuccess = false;
						message += "���Ϊ��" + sngls.getSellSnglId() + "�����۵������󣬲����ظ�����\n";
						throw new RuntimeException(message);
					}
					sowd.deleteSellOutWhsBySellOrdrInd(sngls.getSellSnglId());// ɾ�����۳��ⵥ
					ssd.deleteLogisticsTab(sngls.getSellSnglId());// ɾ��������
				}
			} else {
				isSuccess = false;
				message += "���Ϊ��" + sngls.getSellSnglId() + "�����۵��Ѽ�����޷�����\n";
				throw new RuntimeException(message);
			}
		} else {
			isSuccess = false;
			message += "���Ϊ��" + sngls.getSellSnglId() + "�����۵������󣬲����ظ�����\n";
			throw new RuntimeException(message);
		}
		map.put("isSuccess", isSuccess);
		map.put("message", message);
		return map;
	}

	// ������ϸ���ѯ����
	@Override
	public String querySellSnglByInvtyEncd(Map map) {
		String resp = "";
		List<String> sellSnglIdList = getList(((String) map.get("sellSnglId")));// ���۵���
		List<String> custIdList = getList(((String) map.get("custId")));// �ͻ�����
		List<String> accNumList = getList(((String) map.get("accNum")));// ҵ��Ա����
		List<String> invtyClsEncdList = getList(((String) map.get("invtyClsEncd")));// �������
		List<String> invtyCdList = getList(((String) map.get("invtyCd")));// �������
		List<String> deptIdList = getList(((String) map.get("deptId")));// ���ű���
		List<String> whsEncdList = getList(((String) map.get("whsEncd")));// �ֿ����
		List<String> custOrdrNumList = getList(((String) map.get("custOrdrNum")));// �ͻ�������
		List<String> invtyEncdList = getList(((String) map.get("invtyEncd")));// �������
		List<String> batNumList = getList(((String) map.get("batNum")));// ����
		List<String> intlBatList = getList(((String) map.get("intlBat")));// ��������

		map.put("sellSnglIdList", sellSnglIdList);
		map.put("custIdList", custIdList);
		map.put("accNumList", accNumList);
		map.put("invtyClsEncdList", invtyClsEncdList);
		map.put("invtyCdList", invtyCdList);
		map.put("deptIdList", deptIdList);
		map.put("whsEncdList", whsEncdList);
		map.put("custOrdrNumList", custOrdrNumList);
		map.put("invtyEncdList", invtyEncdList);
		map.put("batNumList", batNumList);
		map.put("intlBatList", intlBatList);

		if (map.containsKey("pageNo") && map.containsKey("pageSize")) {

			List<BeiYong> poList = ssd.selectSellSnglInvtyEncd(map);// Ĭ�ϰ�ʱ�䵹��,�ɴ���
			Map tableSums = ssd.selectSellSnglInvtyEncdSums(map);
			if (null != tableSums) {
				DecimalFormat df = new DecimalFormat("#,##0.00");
				for (Map.Entry entry : (Set<Entry>) tableSums.entrySet()) {
					String s = df
							.format((BigDecimal) entry.getValue() == null ? new BigDecimal(0.00) : entry.getValue());
					entry.setValue(s);
				}
			}
			int count = ssd.selectSellSnglInvtyEncdCount(map);

			int pageNo = Integer.parseInt(map.get("pageNo").toString());
			int pageSize = Integer.parseInt(map.get("pageSize").toString());
			int listNum = poList.size();
			int pages = count / pageSize + 1;
			try {
				resp = BaseJson.returnRespList("purc/SellSngl/querySellSnglByInvtyEncd", true, "��ѯ�ɹ���", count, pageNo,
						pageSize, listNum, pages, poList, tableSums);
			} catch (IOException e) {
				//e.printStackTrace();
			}
		} else {
			List<BeiYong> poList = ssd.selectSellSnglInvtyEncd(map);
			try {
				resp = BaseJson.returnRespListAnno("purc/SellSngl/querySellSnglByInvtyEncd", true, "�ɹ���", poList);
			} catch (IOException e) {
				//e.printStackTrace();
			}
		}

		return resp;
	}

	// ����
	@Override
	public String uploadFileAddDb(MultipartFile file, int i) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		Map<String, SellSngl> sellSnglMap = null;
		if (i == 0) {
			sellSnglMap = uploadScoreInfo(file);
		} else if (i == 1) {
			sellSnglMap = uploadScoreInfoU8(file);
		} else {
			isSuccess = false;
			message = "������쳣����";
			throw new RuntimeException(message);
		}

		// ��MapתΪList��Ȼ���������븸������
		List<SellSngl> sellSnglList = sellSnglMap.entrySet().stream().map(e -> e.getValue())
				.collect(Collectors.toList());
		List<List<SellSngl>> sellSnglLists = Lists.partition(sellSnglList, 1000);

		for (List<SellSngl> sellSngl : sellSnglLists) {
			ssd.insertSellSnglUpload(sellSngl);
		}
		List<SellSnglSub> sellSnglSubList = new ArrayList<>();
		int flag = 0;
		for (SellSngl entry : sellSnglList) {
			flag++;
			// ���������ֱ�͸���Ĺ����ֶ�
			List<SellSnglSub> tempList = entry.getSellSnglSub();
			tempList.forEach(s -> s.setSellSnglId(entry.getSellSnglId()));
			sellSnglSubList.addAll(tempList);
			// �������룬ÿ���ڵ���1000������һ��
			if (sellSnglSubList.size() >= 1000 || sellSnglMap.size() == flag) {
				sssd.insertSellSnglSubUpload(sellSnglSubList);
				sellSnglSubList.clear();
			}
		}
		isSuccess = true;
		message = "���۵�����ɹ���";
		try {
			if (i == 0) {
				resp = BaseJson.returnRespObj("purc/SellSngl/uploadSellSnglFile", isSuccess, message, null);
			} else if (i == 1) {
				resp = BaseJson.returnRespObj("purc/SellSngl/uploadSellSnglFileU8", isSuccess, message, null);
			}
		} catch (IOException e) {
			//e.printStackTrace();
		}
		return resp;
	}

	// ����excle
	private Map<String, SellSngl> uploadScoreInfo(MultipartFile file) {
		Map<String, SellSngl> temp = new HashMap<>();
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
				String orderNo = GetCellData(r, "���۵�����");
				// ����ʵ����
				SellSngl sellSngl = new SellSngl();
				if (temp.containsKey(orderNo)) {
					sellSngl = temp.get(orderNo);
				}
				// ȡ����ǰ�е�1����Ԫ�����ݣ�����װ��pursOrdrʵ��ĸ���������
				sellSngl.setSellSnglId(orderNo); // '���۵�����', varchar(200
				if (GetCellData(r, "���۵�����") == null || GetCellData(r, "���۵�����").equals("")) {
					sellSngl.setSellSnglDt(null);
				} else {
					sellSngl.setSellSnglDt(GetCellData(r, "���۵�����").replaceAll("[^0-9:-]", " "));// ���۵�����
				}
				sellSngl.setAccNum(GetCellData(r, "ҵ��Ա����")); // ҵ��Ա����', varchar(200
				sellSngl.setUserName(GetCellData(r, "ҵ��Ա����")); // ҵ��Ա���� varchar(200
				sellSngl.setCustId(GetCellData(r, "�ͻ�����")); // '�ͻ�����', varchar(200
				sellSngl.setDeptId(GetCellData(r, "���ű���")); // '���ű���', varchar(200
				sellSngl.setBizTypId(GetCellData(r, "ҵ�����ͱ���")); // 'ҵ�����ͱ���', varchar(200
				sellSngl.setSellTypId(GetCellData(r, "�������ͱ���")); // '�������ͱ���', varchar(200
				sellSngl.setFormTypEncd(GetCellData(r, "�������ͱ���"));// �������ͱ���
//				sellSngl.setRecvSendCateId(GetCellData(r,"�շ�������")); // '�շ�������', varchar(200

//				sellSngl.setDelvAddr(GetCellData(r,"������ַ")); // '������ַ', varchar(200
				sellSngl.setTxId(GetCellData(r, "���ױ���")); // '���ױ���(��������', varchar(200
				sellSngl.setIsNtBllg(new Double(GetCellData(r, "�Ƿ�Ʊ")).intValue()); // '�Ƿ�Ʊ', int(11
				sellSngl.setIsNtChk(new Double(GetCellData(r, "�Ƿ����")).intValue()); // '�Ƿ����', int(11
				sellSngl.setChkr(GetCellData(r, "�����")); // '�����', varchar(200
				if (GetCellData(r, "���ʱ��") == null || GetCellData(r, "���ʱ��").equals("")) {
					sellSngl.setChkTm(null);
				} else {
					sellSngl.setChkTm(GetCellData(r, "���ʱ��").replaceAll("[^0-9:-]", " "));// ���ʱ��
				}
//				sellSngl.setIsNtBookEntry(new Double(GetCellData(r, "�Ƿ����")).intValue()); // '�Ƿ����', int(11
//				sellSngl.setChkr(GetCellData(r, "������")); // '�����', varchar(200
//				if (GetCellData(r, "����ʱ��") == null || GetCellData(r, "����ʱ��").equals("")) {
//					sellSngl.setChkTm(null);
//				} else {
//					sellSngl.setChkTm(GetCellData(r, "����ʱ��").replaceAll("[^0-9:-]", " "));// ���ʱ��
//				}
				sellSngl.setSetupPers(GetCellData(r, "������")); // '������', varchar(200
				if (GetCellData(r, "����ʱ��") == null || GetCellData(r, "����ʱ��").equals("")) {
					sellSngl.setSetupTm(null);
				} else {
					sellSngl.setSetupTm(GetCellData(r, "����ʱ��").replaceAll("[^0-9:-]", " "));// ����ʱ��
				}
				sellSngl.setMdfr(GetCellData(r, "�޸���")); // '�޸���', varchar(200
				if (GetCellData(r, "�޸�ʱ��") == null || GetCellData(r, "�޸�ʱ��").equals("")) {
					sellSngl.setModiTm(null);
				} else {
					sellSngl.setModiTm(GetCellData(r, "�޸�ʱ��").replaceAll("[^0-9:-]", " "));// ����ʱ��
				}
				sellSngl.setMemo(GetCellData(r, "��ͷ��ע")); // '��ע', varchar(2000
				sellSngl.setIsPick(new Double(GetCellData(r, "�Ƿ���")).intValue()); // '�Ƿ���', int(2
				sellSngl.setEcOrderId(GetCellData(r, "���̶�����")); // '���̶�����', varchar(200)
				sellSngl.setRecvr(GetCellData(r, "�ռ���")); // '�ռ���', varchar(200)
				sellSngl.setRecvrTel(GetCellData(r, "�ռ��˵绰")); // '�ռ��˵绰'
				sellSngl.setRecvrAddr(GetCellData(r, "�ռ��˵�ַ")); // '�ռ��˵�ַ'
				sellSngl.setRecvrEml(GetCellData(r, "�ռ�������")); // '�ռ�������'
				sellSngl.setBuyerNote(GetCellData(r, "�������")); // '�������'
				sellSngl.setDeliverSelf(new Double(GetCellData(r, "�Ƿ��Է���")).intValue()); // '�Ƿ��Է���'
				sellSngl.setCustOrdrNum(GetCellData(r, "�ͻ�������")); // '�ͻ�������'
				sellSngl.setIsNtMakeVouch(new Double(GetCellData(r, "�Ƿ�����ƾ֤")).intValue());

				List<SellSnglSub> sellSnglSubist = sellSngl.getSellSnglSub();// ���۶����ӱ�
				if (sellSnglSubist == null) {
					sellSnglSubist = new ArrayList<>();
				}
				SellSnglSub sellSnglSub = new SellSnglSub();
				sellSnglSub.setOrdrNum(Long.parseLong(GetCellData(r, "���")));
				sellSnglSub.setWhsEncd(GetCellData(r, "�ֿ����")); // '�ֿ����',
				sellSnglSub.setInvtyEncd(GetCellData(r, "�������")); // '�������',
//				sellSnglSub.setSellSnglId(GetCellData(r,"���۵�����")); // '���۵�����', 
				if (GetCellData(r, "Ԥ�Ʒ�������") == null || GetCellData(r, "Ԥ�Ʒ�������").equals("")) {
					sellSnglSub.setExpctDelvDt(null);
				} else {
					sellSnglSub.setExpctDelvDt(GetCellData(r, "Ԥ�Ʒ�������").replaceAll("[^0-9:-]", " "));// Ԥ�Ʒ�������
				}

				sellSnglSub.setQty(GetBigDecimal(GetCellData(r, "����"), 8)); // '����',
				sellSnglSub.setUnBllgQty(GetBigDecimal(GetCellData(r, "δ��Ʊ����"), 8)); // 'δ��Ʊ����',
				sellSnglSub.setBxQty(GetBigDecimal(GetCellData(r, "����"), 8)); // '����',
				sellSnglSub.setCntnTaxUprc(GetBigDecimal(GetCellData(r, "��˰����"), 8)); // '��˰����',
				sellSnglSub.setPrcTaxSum(GetBigDecimal(GetCellData(r, "��˰�ϼ�"), 8)); // '��˰�ϼ�',
				sellSnglSub.setNoTaxUprc(GetBigDecimal(GetCellData(r, "��˰����"), 8)); // '��˰����',
				sellSnglSub.setNoTaxAmt(GetBigDecimal(GetCellData(r, "��˰���"), 8)); // '��˰���',
				sellSnglSub.setTaxAmt(GetBigDecimal(GetCellData(r, "˰��"), 8)); // '˰��',
				sellSnglSub.setTaxRate(GetBigDecimal(GetCellData(r, "˰��"), 8)); // '˰��',
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

				sellSnglSub.setIsNtRtnGoods(new Double(GetCellData(r, "�˻���ʶ")).intValue()); // '�˻���ʶ',
//				sellSnglSub.setIsComplimentary(new Double(GetCellData(r,"�Ƿ���Ʒ")).intValue()); // '�Ƿ���Ʒ', 
				sellSnglSub.setMemo(GetCellData(r, "���屸ע")); // '��ע',
				sellSnglSub.setRtnblQty(GetBigDecimal(GetCellData(r, "��������"), 8)); // '��������'
				sellSnglSub.setHadrtnQty(GetBigDecimal(GetCellData(r, "��������"), 8)); // '��������'
				sellSnglSub.setProjEncd(GetCellData(r, "��Ŀ����")); // '��Ŀ����'
				sellSnglSub.setProjNm(GetCellData(r, "��Ŀ����")); // '��Ŀ����'
				sellSnglSub.setDiscntRatio(GetCellData(r, "�ۿ۱���")); // '�ۿ۱���'
				sellSnglSubist.add(sellSnglSub);

				sellSngl.setSellSnglSub(sellSnglSubist);
				temp.put(orderNo, sellSngl);
			}
			fileIn.close();
		} catch (Exception e) {
			//e.printStackTrace();
			throw new RuntimeException("�ļ��ĵ�" + j + "�е����ʽ�����޷�����!" + e.getMessage());
		}
		return temp;
	}

	// ����excle
	private Map<String, SellSngl> uploadScoreInfoU8(MultipartFile file) {
		Map<String, SellSngl> temp = new HashMap<>();
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
				SellSngl sellSngl = new SellSngl();
				if (temp.containsKey(orderNo)) {
					sellSngl = temp.get(orderNo);
				}

				// ȡ����ǰ�е�1����Ԫ�����ݣ�����װ��pursOrdrʵ��ĸ���������
				sellSngl.setSellSnglId(orderNo); // '���۵�����', varchar(200
				if (GetCellData(r, "��������") == null || GetCellData(r, "��������").equals("")) {
					sellSngl.setSellSnglDt(null);
				} else {
					sellSngl.setSellSnglDt(GetCellData(r, "��������").replaceAll("[^0-9:-]", " "));// ���۵�����
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
				sellSngl.setIsNtBookEntry(0); // '�Ƿ����', int(11
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
				sellSngl.setIsPick(0); // '�Ƿ���', int(2
//				sellSngl.setEcOrderId(GetCellData(r, "�ͻ�������")); // '���̶�����', varchar(200)
				sellSngl.setRecvr(GetCellData(r, "�ջ���ϵ��")); // '�ռ���', varchar(200)
				sellSngl.setRecvrTel(GetCellData(r, "�ջ���ϵ���ֻ�")); // '�ռ��˵绰'
				sellSngl.setRecvrAddr(GetCellData(r, "������ַ")); // '�ռ��˵�ַ'
//				sellSngl.setRecvrEml(GetCellData(r, "�ռ�������")); // '�ռ�������'
				sellSngl.setBuyerNote(GetCellData(r, "�������")); // '�������'
				sellSngl.setDeliverSelf(1); // '�Ƿ��Է���'
				sellSngl.setCustOrdrNum(GetCellData(r, "�ͻ�������")); // '�ͻ�������'
				sellSngl.setIsNtMakeVouch(0);

				List<SellSnglSub> sellSnglSubist = sellSngl.getSellSnglSub();// ���۶����ӱ�
				if (sellSnglSubist == null) {
					sellSnglSubist = new ArrayList<>();
				}
				SellSnglSub sellSnglSub = new SellSnglSub();
//				sellSnglSub.setOrdrNum(Long.parseLong(GetCellData(r, "�������ӱ�ID")));
				sellSnglSub.setWhsEncd(GetCellData(r, "�ֿ���")); // '�ֿ����',
				sellSnglSub.setInvtyEncd(GetCellData(r, "�������")); // '�������',
				sellSnglSub.setSellSnglId(orderNo); // '���۵�����',
				if (GetCellData(r, "��������") == null || GetCellData(r, "��������").equals("")) {
					sellSnglSub.setExpctDelvDt(null);
				} else {
					sellSnglSub.setExpctDelvDt(GetCellData(r, "��������").replaceAll("[^0-9:-]", " "));// Ԥ�Ʒ�������
				}

				sellSnglSub.setQty(GetBigDecimal(GetCellData(r, "����"), 8)); // '����',
				sellSnglSub.setUnBllgQty(GetBigDecimal(GetCellData(r, "δ��Ʊ����"), 8).abs()); // 'δ��Ʊ����',
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
				sellSnglSub.setRtnblQty(GetBigDecimal(GetCellData(r, "��������"), 8).abs()); // '��������'
				sellSnglSub.setHadrtnQty(GetBigDecimal(GetCellData(r, "��������"), 8).abs()); // '��������'
				sellSnglSub.setProjEncd(GetCellData(r, "��Ŀ����")); // '��Ŀ����'
				sellSnglSub.setProjNm(GetCellData(r, "��Ŀ����")); // '��Ŀ����'
				sellSnglSub.setDiscntRatio("0"); // '�ۿ۱���'
				sellSnglSubist.add(sellSnglSub);

				sellSngl.setSellSnglSub(sellSnglSubist);
				temp.put(orderNo, sellSngl);
			}
			fileIn.close();
		} catch (Exception e) {
			//e.printStackTrace();
			throw new RuntimeException("�ļ��ĵ�" + j + "�е����ʽ�����޷�����!" + e.getMessage());
		}
		return temp;
	}

	// ��ӡ��ѯ�ӿ�
	@Override
	public String printingSellSnglList(Map map) {
		String resp = "";
		List<String> sellSnglIdList = getList(((String) map.get("sellSnglId")));// ���۵���
		List<String> custIdList = getList(((String) map.get("custId")));// �ͻ�����
		List<String> accNumList = getList(((String) map.get("accNum")));// ҵ��Ա����
		List<String> invtyClsEncdList = getList(((String) map.get("invtyClsEncd")));// �������
		List<String> invtyCdList = getList(((String) map.get("invtyCd")));// �������
		List<String> deptIdList = getList(((String) map.get("deptId")));// ���ű���
		List<String> whsEncdList = getList(((String) map.get("whsEncd")));// �ֿ����
		List<String> custOrdrNumList = getList(((String) map.get("custOrdrNum")));// �ͻ�������
		List<String> invtyEncdList = getList(((String) map.get("invtyEncd")));// �������
		List<String> batNumList = getList(((String) map.get("batNum")));// ����
		List<String> intlBatList = getList(((String) map.get("intlBat")));// ��������

		map.put("sellSnglIdList", sellSnglIdList);
		map.put("custIdList", custIdList);
		map.put("accNumList", accNumList);
		map.put("invtyClsEncdList", invtyClsEncdList);
		map.put("invtyCdList", invtyCdList);
		map.put("deptIdList", deptIdList);
		map.put("whsEncdList", whsEncdList);
		map.put("custOrdrNumList", custOrdrNumList);
		map.put("invtyEncdList", invtyEncdList);
		map.put("batNumList", batNumList);
		map.put("intlBatList", intlBatList);
		List<zizhu> sellSnglList = ssd.printingSellSnglList(map);
		try {
//			
			resp = BaseJson.returnRespObjListAnno("purc/SellSngl/printingSellSnglList", true, "��ѯ�ɹ���", null,
					sellSnglList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		return resp;
	}

	// �������۳��ⵥ�������۵�
	@Override
	public String updateA(Map map) {
		Boolean isSuccess = true;
		String resp = "";
		List<String> sellSnglIdList = ssd.selectB(map);
		for (String selSngId : sellSnglIdList) {
			System.out.println("���۵���" + selSngId);
			int a = ssd.updateA(selSngId);
		}
		try {
			resp = BaseJson.returnRespObj("purc/SellSngl/updateSellBySellOut", isSuccess, "����ɹ�", null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		return resp;
	}

	// �˻�������ʱ��ѯ���۵��ӱ���Ϣ
	@Override
	public String selectSellSnglSubByRtnblQty(String sellSnglId) {
		// TODO Auto-generated method stub
		String resp = "";
		List<String> lists = getList(sellSnglId);
		List<SellSnglSub> sellSnglSubList = sssd.selectSellSnglSubByRtnblQty(lists);
		try {
			resp = BaseJson.returnRespObjList("purc/SellSngl/selectSellSnglSubByRtnblQty", true, "��ѯ�ɹ���", null,
					sellSnglSubList);
		} catch (IOException e) {
			//e.printStackTrace();
		}
		return resp;
	}

	// ����ʱ��ѯ������Ϣ
	@Override
	public String querySellSnglLists(Map map) {
		String resp = "";
		List<String> sellSnglIdList = getList(((String) map.get("sellSnglId")));// ���۵���
		List<String> custIdList = getList(((String) map.get("custId")));// �ͻ�����
		List<String> accNumList = getList(((String) map.get("accNum")));// ҵ��Ա����
		List<String> invtyClsEncdList = getList(((String) map.get("invtyClsEncd")));// �������
		List<String> invtyCdList = getList(((String) map.get("invtyCd")));// �������
		List<String> deptIdList = getList(((String) map.get("deptId")));// ���ű���
		List<String> whsEncdList = getList(((String) map.get("whsEncd")));// �ֿ����
		List<String> custOrdrNumList = getList(((String) map.get("custOrdrNum")));// �ͻ�������
		List<String> invtyEncdList = getList(((String) map.get("invtyEncd")));// �������
		List<String> batNumList = getList(((String) map.get("batNum")));// ����
		List<String> intlBatList = getList(((String) map.get("intlBat")));// ��������

		map.put("sellSnglIdList", sellSnglIdList);
		map.put("custIdList", custIdList);
		map.put("accNumList", accNumList);
		map.put("invtyClsEncdList", invtyClsEncdList);
		map.put("invtyCdList", invtyCdList);
		map.put("deptIdList", deptIdList);
		map.put("whsEncdList", whsEncdList);
		map.put("custOrdrNumList", custOrdrNumList);
		map.put("invtyEncdList", invtyEncdList);
		map.put("batNumList", batNumList);
		map.put("intlBatList", intlBatList);
		if (map.containsKey("sort")) {
			map.put("sort",((String)map.get("sort")).replace("a.", ""));
			//ʹ��union all ���ʱ��,�����Դ������,��������
		}
	
		List<SellSngl> poList = ssd.selectSellSnglListToCZ(map);
		int count = ssd.selectSellSnglListToCZCount(map);

		int pageNo = Integer.parseInt(map.get("pageNo").toString());// �ڼ�ҳ
		int pageSize = Integer.parseInt(map.get("pageSize").toString());// ÿҳ����
		int listNum = poList.size();
		int pages = count / pageSize + 1;// ��ҳ��
		
		try {
			resp = BaseJson.returnRespList("purc/SellSngl/querySellSnglLists", true, "��ѯ�ɹ���", count, pageNo, pageSize,
					listNum, pages, poList);
		} catch (IOException e) {
			//e.printStackTrace();
		}
		return resp;
	}

	// ��ѯ����ͳ�Ʊ�
	@Override
	public String selectXSTJList(Map map) {
		return selectSalesCountTable(map);
	}

	/**
	 * ����ͳ�Ʊ�
	 */
	private String selectSalesCountTable(Map map) {
		// ��ѯ���۷�Ʊ,���ղ��Ŵ���Ϳͻ�����
		map.put("isAll", "111");
		List<String> invtyEncdList = getList(((String) map.get("invtyEncd")));// �������
		List<String> custIdList = getList(((String) map.get("custId")));// �ͻ�����
		map.put("custIdList", custIdList);
		map.put("invtyEncdList", invtyEncdList);
		List<InvtyDetail> dataList = ssd.selectSalesCountList(map);

		int count = ssd.countSalesCountList(map);
		int pageNo = Integer.parseInt(map.get("pageNo").toString());
		int pageSize = Integer.parseInt(map.get("pageSize").toString());
		int listNum = dataList.size();
		int pages = count / pageSize + 1;

		String resp = "";
		if (dataList.size() > 0) {
			BigDecimal cost = new BigDecimal(0);
			for (InvtyDetail it : dataList) {
				it.setQty(Optional.ofNullable(it.getQty()).orElse(new BigDecimal(0.00000000)));
				it.setAmt(Optional.ofNullable(it.getAmt()).orElse(new BigDecimal(0.00000000)));

				if (it.getQty().compareTo(BigDecimal.ZERO) != 0) {
					it.setNoTaxUprc(it.getAmt().divide(it.getQty(), 8, BigDecimal.ROUND_HALF_UP));
					it.setCntnTaxUprc(it.getSellInAmt().divide(it.getQty(), 8, BigDecimal.ROUND_HALF_UP));
				}
				map.put("invtyEncd", it.getInvtyEncd());
				map.put("custId", it.getCustId());
				map.put("deptId", it.getDeptId());
				// ��ѯ���۳��ⵥ�����Ϊ�ɱ�
				it.setSellCost(ssd.selectSalesCost(map));
				it.setSellCost(Optional.ofNullable(it.getSellCost()).orElse(new BigDecimal(0.00000000)));
				cost = cost.add(it.getSellCost());
			}
			map.put("isAll", "");
			// �ܼ�
			List<InvtyDetail> allList = ssd.selectSalesCountList(map);
			BigDecimal sellCost = new BigDecimal(0);
			if (allList.size() > 0) {
				InvtyDetail all = allList.get(0);
				all.setDeptName("�ܼ�");
				all.setAccNum("");
				all.setCustNm("");
				all.setInvtyNm("");
				all.setSellCost(cost);

				dataList.add(dataList.size(), all);
			}
			for (InvtyDetail it : dataList) {
				// ����ë�� = ����-�ɱ�
				// ë����=ë��? ���������100%
				it.setSellCost(Optional.ofNullable(it.getSellCost()).orElse(new BigDecimal(0.00000000)));
				it.setGross(it.getAmt().subtract(it.getSellCost()));
				it.setGross(Optional.ofNullable(it.getGross()).orElse(new BigDecimal(0.00000000)));
				sellCost.add(it.getSellCost());
				if (it.getAmt().compareTo(BigDecimal.ZERO) != 0) {
					String ra = it.getGross().divide(it.getAmt(), 8, BigDecimal.ROUND_HALF_UP)
							.multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
					it.setGrossRate(ra + "%");
				}
			}
		}
		try {
			resp = BaseJson.returnRespList("purc/SellSngl/selectXSTJList", true, "��ѯ�ɹ���", count, pageNo, pageSize,
					listNum, pages, dataList);
		} catch (IOException e) {
			//e.printStackTrace();
		}
		return resp;

	}

	// ��ҳ+����
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public String querySellSnglListOrderBy(Map map) {
		String resp = "";
		List<String> sellSnglIdList = getList(((String) map.get("sellSnglId")));// ���۵���
		List<String> custIdList = getList(((String) map.get("custId")));// �ͻ�����
		List<String> accNumList = getList(((String) map.get("accNum")));// ҵ��Ա����
		List<String> invtyClsEncdList = getList(((String) map.get("invtyClsEncd")));// �������
		List<String> invtyCdList = getList(((String) map.get("invtyCd")));// �������
		List<String> deptIdList = getList(((String) map.get("deptId")));// ���ű���
		List<String> whsEncdList = getList(((String) map.get("whsEncd")));// �ֿ����
		List<String> custOrdrNumList = getList(((String) map.get("custOrdrNum")));// �ͻ�������
		List<String> invtyEncdList = getList(((String) map.get("invtyEncd")));// �������
		List<String> batNumList = getList(((String) map.get("batNum")));// ����
		List<String> intlBatList = getList(((String) map.get("intlBat")));// ��������
//		String memo = ((String)map.get("memo"));//��ע

//		map.put("memo", memo);
		map.put("sellSnglIdList", sellSnglIdList);
		map.put("custIdList", custIdList);
		map.put("accNumList", accNumList);
		map.put("invtyClsEncdList", invtyClsEncdList);
		map.put("invtyCdList", invtyCdList);
		map.put("deptIdList", deptIdList);
		map.put("whsEncdList", whsEncdList);
		map.put("custOrdrNumList", custOrdrNumList);
		map.put("invtyEncdList", invtyEncdList);
		map.put("batNumList", batNumList);
		map.put("intlBatList", intlBatList);
		List<?> poList;
		if (map.get("sort") == null || map.get("sort").equals("") || map.get("sortOrder") == null
				|| map.get("sortOrder").equals("")) {
			map.put("sort", "ss.sell_sngl_dt");
			map.put("sortOrder", "desc");
		}
		poList = ssd.selectSellSnglListOrderBy(map);// ��ѯ
		zizhu summary = (zizhu) poList.remove(poList.size() - 1);// ���һ�����ܼ�+����
		int count = Integer.parseInt(summary.sellSnglId);// ���ռ���
		Map tableSums = new HashMap();
		try {
			tableSums.put("qty", summary.qty);
			tableSums.put("bxQty", summary.bxQty);
			tableSums.put("prcTaxSum", summary.prcTaxSum);
			tableSums.put("noTaxAmt", summary.noTaxAmt);
			tableSums.put("taxAmt", summary.taxAmt);
		} catch (Exception e) {
			tableSums = null;
			System.err.println("תmap����");
		}

//			Map tableSums = ssd.selectSellSnglListSums(map);//�ܼ�
//			if (null!=tableSums) {
//				DecimalFormat df = new DecimalFormat("#,##0.00");
//				for(Map.Entry entry : (Set<Entry>)tableSums.entrySet()) {
//					String s = df.format((BigDecimal) entry.getValue()==null?new BigDecimal(0.00):entry.getValue());
//					entry.setValue(s);
//				}
//			}

//		int count = ssd.selectSellSnglCount(map);//����

		int pageNo = Integer.parseInt(map.get("pageNo").toString());// �ڼ�ҳ
		int pageSize = Integer.parseInt(map.get("pageSize").toString());// ÿҳ����
		int listNum = poList.size();
		int pages = count / pageSize + 1;// ��ҳ��

		try {
			resp = BaseJson.returnRespList("purc/SellSngl/querySellSnglListOrderBy", true, "��ѯ�ɹ���", count, pageNo,
					pageSize, listNum, pages, poList, tableSums);
		} catch (IOException e) {
			//e.printStackTrace();
		}
		return resp;
	}

	public static class zizhu {

		@JsonProperty("���۵����")
		public String sellSnglId;// ���۵����
		@JsonProperty("���۵�����")
		public String sellSnglDt;// ���۵�����
		@JsonProperty("�������ͱ���")
		public String formTypEncd;// �������ͱ���
		@JsonProperty("������������")
		public String formTypName;// ������������
		@JsonProperty("�û���� ")
		public String accNum;// �û����
		@JsonProperty("�û����� ")
		public String userName;// �û�����
		@JsonProperty("���ű�� ")
		public String deptId;// ���ű��
		@JsonProperty("��������")
		public String deptName;// ��������
		@JsonProperty("�ͻ����")
		public String custId;// �ͻ����
		@JsonProperty("�ͻ�����")
		public String custNm;// �ͻ�����
		@JsonProperty("ҵ�����ͱ��")
		public String bizTypId;// ҵ�����ͱ��
		@JsonProperty("ҵ����������")
		public String bizTypNm;// ҵ����������
		@JsonProperty("�������ͱ��")
		public String sellTypId;// �������ͱ��
		@JsonProperty("������������")
		public String sellTypNm;// ������������
//		@JsonProperty("�շ������")
//		public String recvSendCateId;// �շ������
//		@JsonProperty("�շ��������")
//		public String recvSendCateNm;// �շ��������
//		@JsonProperty("������ַ")
//		public String delvAddr;// ������ַ
		@JsonProperty("���ױ�� ")
		public String txId;// ���ױ��
		@JsonProperty("���̶����� ")
		public String ecOrderId;// ���̶�����
		@JsonProperty("�ͻ�������")
		public String custOrdrNum;// �ͻ�������
		@JsonProperty("�Ƿ�Ʊ")
		public Integer isNtBllg;// �Ƿ�Ʊ
		@JsonProperty("�Ƿ����")
		public Integer isNtChk;// �Ƿ����
		@JsonProperty("�����")
		public String chkr;// �����
		@JsonProperty("���ʱ��")
		public String chkTm;// ���ʱ��
		@JsonProperty("�Ƿ����")
		public Integer isNtBookEntry;// �Ƿ����
		@JsonProperty("������")
		public String bookEntryPers;// ������
		@JsonProperty("����ʱ��")
		public String bookEntryTm;// ����ʱ��
		@JsonProperty("������")
		public String setupPers;// ������
		@JsonProperty("����ʱ��")
		public String setupTm;// ����ʱ��
		@JsonProperty("�޸���")
		public String mdfr;// �޸���
		@JsonProperty("�޸�ʱ��")
		public String modiTm;// �޸�ʱ��
		@JsonProperty("��ͷ��ע ")
		public String memo;// ��ע
		@JsonProperty("�ռ��� ")
		public String recvr;// �ռ���
		@JsonProperty("�ռ��˵绰 ")
		public String recvrTel;// �ռ��˵绰
		@JsonProperty("�ռ��˵�ַ ")
		public String recvrAddr;// �ռ��˵�ַ
		@JsonProperty("�ռ�������")
		public String recvrEml;// �ռ�������
		@JsonProperty("�������")
		public String buyerNote;// �������
		@JsonProperty("�Ƿ������Ƿ����ɼ������ʶ�� ")
		public Integer isPick;// �Ƿ������Ƿ����ɼ������ʶ��
		@JsonProperty("�Ƿ��Է�����0�� 1�ǣ�")
		public Integer deliverSelf;// �Ƿ��Է�����0�� 1�ǣ�
		@JsonProperty("�Ƿ�����ƾ֤")
		public Integer isNtMakeVouch;// �Ƿ�����ƾ֤
		@JsonProperty("��ƾ֤��")
		public String makVouchPers;// ��ƾ֤��
		@JsonProperty("��ƾ֤ʱ��")
		public String makVouchTm;// ��ƾ֤ʱ��
		@JsonProperty("���")
		public Long ordrNum;// ���
		@JsonProperty("�ֿ����")
		public String whsEncd;// �ֿ����
		@JsonProperty("�������")
		public String invtyEncd;// �������
		@JsonProperty("Ԥ�Ʒ�������")
		public String expctDelvDt;// Ԥ�Ʒ�������
		@JsonProperty("����")
		public BigDecimal qty;// ����
		@JsonProperty("����")
		public BigDecimal bxQty;// ����
		@JsonProperty("��˰����")
		public BigDecimal cntnTaxUprc;// ��˰����
		@JsonProperty("��˰�ϼ�")
		public BigDecimal prcTaxSum;// ��˰�ϼ�
		@JsonProperty("��˰����")
		public BigDecimal noTaxUprc;// ��˰����
		@JsonProperty("��˰���")
		public BigDecimal noTaxAmt;// ��˰���
		@JsonProperty("˰��")
		public BigDecimal taxAmt;// ˰��
		@JsonProperty("˰��")
		public BigDecimal taxRate;// ˰��
		@JsonProperty("����")
		public String batNum;// ����
		@JsonProperty("��������")
		public String intlBat;// ��������
		@JsonProperty("������")
		public Integer baoZhiQi;// ������
		@JsonProperty("��������")
		public String prdcDt;// ��������
		@JsonProperty("ʧЧ����")
		public String invldtnDt;// ʧЧ����
		@JsonProperty("�Ƿ��˻�")
		public Integer isNtRtnGoods;// �Ƿ��˻�
		@JsonProperty("�Ƿ���Ʒ")
		public Integer isComplimentary;// �Ƿ���Ʒ
		@JsonProperty("��������")
		public BigDecimal rtnblQty;// ��������
		@JsonProperty("��������")
		public BigDecimal hadrtnQty;// ��������
		@JsonProperty("��Ŀ����")
		public String projEncd;// ��Ŀ����
		@JsonProperty("��Ŀ����")
		public String projNm;// ��Ŀ����
		@JsonProperty("�ۿ۱���")
		public String discntRatio;// �ۿ۱���
		@JsonProperty("�������")
		public String invtyNm;// �������
		@JsonProperty("����ͺ�")
		public String spcModel;// ����ͺ�
		@JsonProperty("�������")
		public String invtyCd;// �������
		@JsonProperty("���")
		public BigDecimal bxRule;// ���
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
		@JsonProperty("�ֿ�����")
		public String whsNm;// �ֿ�����
		@JsonProperty("��Ӧ������")
		public String crspdBarCd;// ��Ӧ������
		@JsonProperty("Ӧ˰����")
		public Integer shdTaxLabour;// Ӧ˰����
		@JsonProperty("�Ƿ��ۿ�")
		public Integer isNtDiscnt;// �Ƿ��ۿ�
		@JsonProperty("δ��Ʊ����")
		public BigDecimal unBllgQty;// δ��Ʊ����
		@JsonProperty("���屸ע")
		public String memos;// ���屸ע

	}
}
