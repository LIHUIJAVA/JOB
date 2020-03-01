package com.px.mis.purc.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.px.mis.ec.dao.LogisticsTabDao;
import com.px.mis.ec.entity.LogisticsTab;
import com.px.mis.ec.entity.PlatOrder;
import com.px.mis.purc.dao.EntrsAgnDelvDao;
import com.px.mis.purc.dao.EntrsAgnDelvSubDao;
import com.px.mis.purc.dao.IntoWhsDao;
import com.px.mis.purc.dao.InvtyDocDao;
import com.px.mis.purc.dao.InvtyTabDao;
import com.px.mis.purc.dao.SellOutWhsDao;
import com.px.mis.purc.dao.SellOutWhsSubDao;
import com.px.mis.purc.dao.SellSnglDao;
import com.px.mis.purc.entity.EntrsAgnDelv;
import com.px.mis.purc.entity.EntrsAgnDelvSub;
import com.px.mis.purc.entity.InvtyDoc;
import com.px.mis.purc.entity.SellOutWhs;
import com.px.mis.purc.entity.SellOutWhsSub;
import com.px.mis.purc.service.EntrsAgnDelvService;
import com.px.mis.purc.util.CalcAmt;
import com.px.mis.util.BaseJson;
import com.px.mis.util.CommonUtil;
import com.px.mis.util.GetOrderNo;
import com.px.mis.util.poiTool;
import com.px.mis.whs.entity.InvtyTab;

/*ί�д���������*/
@Transactional
@Service
public class EntrsAgnDelvServiceImpl extends poiTool implements EntrsAgnDelvService {
	@Autowired
	private EntrsAgnDelvDao eadd;
	@Autowired
	private EntrsAgnDelvSubDao eadsd;
	@Autowired
	private SellOutWhsDao sowd; // ���۳�����
	@Autowired
	private SellOutWhsSubDao sowds; // ���۳�����
	@Autowired
	private SellSnglDao ssd; // ���۵���
	@Autowired
	private InvtyTabDao itd;// ����
	@Autowired
	private LogisticsTabDao ltd; // ������
	@Autowired
	private IntoWhsDao intoWhsDao;// �ɹ���ⵥ
	@Autowired
	private InvtyDocDao idd;// �������
	@Autowired
	private InvtyDocDao invtyDocDao;
	// ������
	@Autowired
	private GetOrderNo getOrderNo;	

	// ����ί�д���������
	@Override
	public String addEntrsAgnDelv(String userId, EntrsAgnDelv entrsAgnDelv, List<EntrsAgnDelvSub> entrsAgnDelvSubList,String loginTime) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
//			TreeSet<EntrsAgnDelvSub> entrsAgnDelvSubSet = new TreeSet();
//			entrsAgnDelvSubSet.addAll(entrsAgnDelvSubList);
//			if(entrsAgnDelvSubSet.size() < entrsAgnDelvSubList.size()) {
//				resp = BaseJson.returnRespObj("purc/EntrsAgnDelv/addEntrsAgnDelv", false, "��������ϸ������ͬһ�ֿ��д���ͬһ�������ͬ���Σ�",null);
//				return resp;
//			}
			// ��ȡ������
			String number = getOrderNo.getSeqNo("WF", userId,loginTime);
			if (eadd.selectEntrsAgnDelvById(number) != null) {
				message = "����" + number + "�Ѵ��ڣ����������룡";
				isSuccess = false;
			} else {
				entrsAgnDelv.setDelvSnglId(number);
				for (EntrsAgnDelvSub entrsAgnDelvSub : entrsAgnDelvSubList) {
					entrsAgnDelvSub.setDelvSnglId(entrsAgnDelv.getDelvSnglId());
					InvtyDoc invtyDoc = invtyDocDao.selectAllByInvtyEncd(entrsAgnDelvSub.getInvtyEncd());
					if (invtyDoc.getIsNtSell() == null) {
						isSuccess = false;
						message = "��ί�д�������Ӧ�Ĵ��:" + entrsAgnDelvSub.getInvtyEncd()
								+ "û�������Ƿ��������ԣ��޷����棡";
						throw new RuntimeException(message);
					} else if (invtyDoc.getIsNtSell().intValue() != 1) {
						isSuccess = false;
						message = "��ί�д�������Ӧ�Ĵ��:" + entrsAgnDelvSub.getInvtyEncd()+ "�ǿ����۴�����޷����棡";
						throw new RuntimeException(message);
					}
					if (invtyDoc.getIsQuaGuaPer() == null) {
						isSuccess = false;
						message = "��ί�д�������Ӧ�Ĵ��:" + entrsAgnDelvSub.getInvtyEncd()
								+ "û�������Ƿ����ڹ������ԣ��޷����棡";
						throw new RuntimeException(message);
					} else if(invtyDoc.getIsQuaGuaPer() == 1){
						if (entrsAgnDelvSub.getPrdcDt() == "" || entrsAgnDelvSub.getPrdcDt() == null) {
							isSuccess = false;
							message = "��ί�д�������Ӧ�Ĵ��:" + entrsAgnDelvSub.getInvtyEncd()
									+ "�Ǳ����ڹ����������������ڣ�";
							throw new RuntimeException(message);
						}
						if (entrsAgnDelvSub.getInvldtnDt() == ""|| entrsAgnDelvSub.getInvldtnDt() == null) {
							isSuccess = false;
							message = "��ί�д�������Ӧ�Ĵ��:" + entrsAgnDelvSub.getInvtyEncd()
									+ "�Ǳ����ڹ���������ʧЧ���ڣ�";
							throw new RuntimeException(message);
						}
					}
					if (entrsAgnDelvSub.getPrdcDt() == "") {
						entrsAgnDelvSub.setPrdcDt(null);
					}
					if (entrsAgnDelvSub.getInvldtnDt() == "") {
						entrsAgnDelvSub.setInvldtnDt(null);
					}
					entrsAgnDelvSub.setStlQty(new BigDecimal(0));// ��������
					entrsAgnDelvSub.setStlUprc(new BigDecimal(0));// ���㵥��
					entrsAgnDelvSub.setStlAmt(new BigDecimal(0));// ������
					entrsAgnDelvSub.setBllgQty(new BigDecimal(0));// ��Ʊ����
					if (entrsAgnDelv.getIsNtRtnGood() == 0) {
						entrsAgnDelvSub.setUnBllgRtnGoodsQty(entrsAgnDelvSub.getQty());// ��������
					}

				}
				eadd.insertEntrsAgnDelv(entrsAgnDelv);
				eadsd.insertEntrsAgnDelvSub(entrsAgnDelvSubList);
				message = "�����ɹ���";
				isSuccess = true;
			}
			resp = BaseJson.returnRespObj("purc/EntrsAgnDelv/addEntrsAgnDelv", isSuccess, message, entrsAgnDelv);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resp;
	}

	// �޸�ί�д���������
	@Override
	public String editEntrsAgnDelv(EntrsAgnDelv entrsAgnDelv, List<EntrsAgnDelvSub> entrsAgnDelvSubList) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			for (EntrsAgnDelvSub entrsAgnDelvSub : entrsAgnDelvSubList) {
				entrsAgnDelvSub.setDelvSnglId(entrsAgnDelv.getDelvSnglId());
				InvtyDoc invtyDoc = invtyDocDao.selectAllByInvtyEncd(entrsAgnDelvSub.getInvtyEncd());
				if (invtyDoc.getIsNtSell() == null) {
					isSuccess = false;
					message = "��ί�д�������Ӧ�Ĵ��:" + entrsAgnDelvSub.getInvtyEncd()
							+ "û�������Ƿ��������ԣ��޷����棡";
					throw new RuntimeException(message);
				} else if (invtyDoc.getIsNtSell().intValue() != 1) {
					isSuccess = false;
					message = "��ί�д�������Ӧ�Ĵ��:" + entrsAgnDelvSub.getInvtyEncd()+ "�ǿ����۴�����޷����棡";
					throw new RuntimeException(message);
				}
				if (invtyDoc.getIsQuaGuaPer() == null) {
					isSuccess = false;
					message = "��ί�д�������Ӧ�Ĵ��:" + entrsAgnDelvSub.getInvtyEncd()
							+ "û�������Ƿ����ڹ������ԣ��޷����棡";
					throw new RuntimeException(message);
				} else if(invtyDoc.getIsQuaGuaPer() == 1){
					if (entrsAgnDelvSub.getPrdcDt() == "" || entrsAgnDelvSub.getPrdcDt() == null) {
						isSuccess = false;
						message = "��ί�д�������Ӧ�Ĵ��:" + entrsAgnDelvSub.getInvtyEncd()
								+ "�Ǳ����ڹ����������������ڣ�";
						throw new RuntimeException(message);
					}
					if (entrsAgnDelvSub.getInvldtnDt() == ""|| entrsAgnDelvSub.getInvldtnDt() == null) {
						isSuccess = false;
						message = "��ί�д�������Ӧ�Ĵ��:" + entrsAgnDelvSub.getInvtyEncd()
								+ "�Ǳ����ڹ���������ʧЧ���ڣ�";
						throw new RuntimeException(message);
					}
				}
				if (entrsAgnDelvSub.getPrdcDt() == "") {
					entrsAgnDelvSub.setPrdcDt(null);
				}
				if (entrsAgnDelvSub.getInvldtnDt() == "") {
					entrsAgnDelvSub.setInvldtnDt(null);
				}
				entrsAgnDelvSub.setStlQty(new BigDecimal(0));// ��������
				entrsAgnDelvSub.setStlUprc(new BigDecimal(0));// ���㵥��
				entrsAgnDelvSub.setStlAmt(new BigDecimal(0));// ������
				entrsAgnDelvSub.setBllgQty(new BigDecimal(0));// ��Ʊ����
				if (entrsAgnDelv.getIsNtRtnGood() == 0) {
					entrsAgnDelvSub.setUnBllgRtnGoodsQty(entrsAgnDelvSub.getQty());// ��������
				}
			}
			eadsd.deleteEntrsAgnDelvSubByDelvSnglId(entrsAgnDelv.getDelvSnglId());
			eadd.updateEntrsAgnDelvByDelvSnglId(entrsAgnDelv);
			eadsd.insertEntrsAgnDelvSub(entrsAgnDelvSubList);
			message = "���³ɹ���";
			resp = BaseJson.returnRespObj("purc/EntrsAgnDelv/editEntrsAgnDelv", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// ����ɾ��ί�д���������
	@Override
	public String deleteEntrsAgnDelv(String delvSnglId) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		if (eadd.selectEntrsAgnDelvByDelvSnglId(delvSnglId) != null) {
			eadd.deleteEntrsAgnDelvByDelvSnglId(delvSnglId);
			eadsd.deleteEntrsAgnDelvSubByDelvSnglId(delvSnglId);
			isSuccess = true;
			message = "ɾ���ɹ���";
		} else {
			isSuccess = false;
			message = "����" + delvSnglId + "�����ڣ�";
		}

		try {
			resp = BaseJson.returnRespObj("purc/EntrsAgnDelv/deleteEntrsAgnDelv", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// ������ѯί�д���������
	@Override
	public String queryEntrsAgnDelv(String delvSnglId) {
		// TODO Auto-generated method stub
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		/* List<EntrsAgnDelvSub> entrsAgnDelvSub = new ArrayList<>(); */
		EntrsAgnDelv entrsAgnDelv = eadd.selectEntrsAgnDelvByDelvSnglId(delvSnglId);
		if (entrsAgnDelv != null) {
			/* entrsAgnDelvSub=eadsd.selectEntrsAgnDelvSubByDelvSnglId(delvSnglId); */
			message = "��ѯ�ɹ���";
		} else {
			isSuccess = false;
			message = "����" + delvSnglId + "�����ڣ�";
		}

		try {
			resp = BaseJson.returnRespObj("purc/EntrsAgnDelv/queryEntrsAgnDelv", isSuccess, message, entrsAgnDelv);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// ��ѯ����ί�д���������
	@Override
	public String queryEntrsAgnDelvList(Map map) {
		String resp = "";
		List<String> delvSnglIdList = getList((String) map.get("delvSnglId"));// ί�д�����������
		List<String> custIdList = getList((String) map.get("custId"));// �ͻ�����
		List<String> accNumList = getList((String) map.get("accNum"));// ҵ��Ա����
		List<String> invtyClsEncdList = getList((String) map.get("invtyClsEncd"));// ����������
		List<String> invtyCdList = getList((String) map.get("invtyCd"));// �������
		List<String> deptIdList = getList((String) map.get("deptId"));// ���ű���
		List<String> whsEncdList = getList((String) map.get("whsEncd"));// �ֿ����
		List<String> custOrdrNumList = getList((String) map.get("custOrdrNum"));// �ͻ�������
		List<String> invtyEncdList = getList((String) map.get("invtyEncd"));// �������
		List<String> batNumList = getList((String) map.get("batNum"));// ����

		map.put("delvSnglIdList", delvSnglIdList);
		map.put("custIdList", custIdList);
		map.put("accNumList", accNumList);
		map.put("invtyClsEncdList", invtyClsEncdList);
		map.put("invtyCdList", invtyCdList);
		map.put("deptIdList", deptIdList);
		map.put("whsEncdList", whsEncdList);
		map.put("custOrdrNumList", custOrdrNumList);
		map.put("invtyEncdList", invtyEncdList);
		map.put("batNumList", batNumList);
		
		List<EntrsAgnDelv> poList = eadd.selectEntrsAgnDelvList(map);
		int count = eadd.selectEntrsAgnDelvCount(map);

		int pageNo = Integer.parseInt(map.get("pageNo").toString());
		int pageSize = Integer.parseInt(map.get("pageSize").toString());
		int listNum = poList.size();
		int pages = count / pageSize + 1;

		try {
			resp = BaseJson.returnRespList("purc/EntrsAgnDelv/queryEntrsAgnDelvList", true, "��ѯ�ɹ���", count, pageNo,
					pageSize, listNum, pages, poList);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// ����ɾ��ί�д���������
	@Override
	public String deleteEntrsAgnDelvList(String delvSnglId) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			List<String> lists = getList(delvSnglId);
			List<String> lists2 = new ArrayList<>();
			List<String> lists3 = new ArrayList<>();
			for (String list : lists) {
				if (eadd.selectEntrsAgnDelvIsNtChk(list) == 0) {
					lists2.add(list);
				} else {
					lists3.add(list);
				}
			}
			if (lists2.size() > 0) {
				int a = 0;
				try {
					a = deleteEntrsAgnDelvList(lists2);
				} catch (Exception e) {
					isSuccess = false;
					message += "���ݺ�Ϊ��" + lists2.toString() + "�Ķ���ɾ��ʧ��!";
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				}
				if (a >= 1) {
					isSuccess = true;
					message += "���ݺ�Ϊ��" + lists2.toString() + "�Ķ���ɾ���ɹ�!\n";
				} else {
					isSuccess = false;
					message += "���ݺ�Ϊ��" + lists2.toString() + "�Ķ���ɾ��ʧ�ܣ�\n";
				}
			}
			if (lists3.size() > 0) {
				isSuccess = false;
				message += "���ݺ�Ϊ��" + lists3.toString() + "�Ķ����ѱ���ˣ��޷�ɾ����\n";
			}
			resp = BaseJson.returnRespObj("purc/EntrsAgnDelv/deleteEntrsAgnDelvList", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	private int deleteEntrsAgnDelvList(List<String> lists2) {
		eadd.insertEntrsAgnDelvDl(lists2);
		eadsd.insertEntrsAgnDelvSubDl(lists2);
		int a = eadd.deleteEntrsAgnDelvList(lists2);
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

	// ��ӡ �������ί�д���������
	@Override
	public String printingEntrsAgnDelvList(Map map) {
		String resp = "";
		List<String> delvSnglIdList = getList((String) map.get("delvSnglId"));// ί�д�����������
		List<String> custIdList = getList((String) map.get("custId"));// �ͻ�����
		List<String> accNumList = getList((String) map.get("accNum"));// ҵ��Ա����
		List<String> invtyClsEncdList = getList((String) map.get("invtyClsEncd"));// ����������
		List<String> invtyCdList = getList((String) map.get("invtyCd"));// �������
		List<String> deptIdList = getList((String) map.get("deptId"));// ���ű���
		List<String> whsEncdList = getList((String) map.get("whsEncd"));// �ֿ����
		List<String> custOrdrNumList = getList((String) map.get("custOrdrNum"));// �ͻ�������
		List<String> invtyEncdList = getList((String) map.get("invtyEncd"));// �������
		List<String> batNumList = getList((String) map.get("batNum"));// ����

		map.put("delvSnglIdList", delvSnglIdList);
		map.put("custIdList", custIdList);
		map.put("accNumList", accNumList);
		map.put("invtyClsEncdList", invtyClsEncdList);
		map.put("invtyCdList", invtyCdList);
		map.put("deptIdList", deptIdList);
		map.put("whsEncdList", whsEncdList);
		map.put("custOrdrNumList", custOrdrNumList);
		map.put("invtyEncdList", invtyEncdList);
		map.put("batNumList", batNumList);
		List<zizhu> entrsAgnDelvList = eadd.printingEntrsAgnDelvList(map);
		try {
//			
			resp = BaseJson.returnRespObjListAnno("purc/EntrsAgnDelv/printingEntrsAgnDelvList", true, "��ѯ�ɹ���", null,
					entrsAgnDelvList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
	}

	// �������ί�д�����������ί�д����˻���
	@Override
	public Map<String, Object> updateEntrsAgnDelvIsNtChkList(String userId, EntrsAgnDelv entrsAgnDelvs,String loginTime)
			throws Exception {
		StringBuilder message = new StringBuilder();
		Boolean isSuccess = true;
		Map<String, Object> map = new HashMap<>();
		try {
			EntrsAgnDelv entrsAgnDelv = eadd.selectEntrsAgnDelvByDelvSnglId(entrsAgnDelvs.getDelvSnglId());
			if (entrsAgnDelv.getIsNtRtnGood() == 0) {
				if (entrsAgnDelvs.getIsNtChk() == 1) {
					// ί�д������������
					message.append(updateEntrsAgnDelvIsNtChkOK(userId, entrsAgnDelvs,loginTime).get("message"));
				} else if (entrsAgnDelvs.getIsNtChk() == 0) {
					// ί�д�������������
					message.append(updateEntrsAgnDelvIsNtChkNO(entrsAgnDelvs).get("message"));
				} else {
					isSuccess = false;
					message.append("���״̬�����޷���ˣ�\n");
				}
			} else if (entrsAgnDelv.getIsNtRtnGood() == 1) {
				if (entrsAgnDelvs.getIsNtChk() == 1) {
					// ί�д����˻������
					message.append(updateReturnEntrsAgnDelvIsNtChkOK(userId, entrsAgnDelvs,loginTime).get("message"));
				} else if (entrsAgnDelvs.getIsNtChk() == 0) {
					// ί�д����˻�������
					message.append(updateReturnEntrsAgnDelvIsNtChkNO(entrsAgnDelvs).get("message"));
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

	// ί�д������������
	private Map<String, Object> updateEntrsAgnDelvIsNtChkOK(String userId, EntrsAgnDelv eAgnDelv,String loginTime) {
		String message = "";
		Boolean isSuccess = true;
		Map<String, Object> map = new HashMap<>();
		// ���۳��ⵥ�ӱ���
//		List<SellOutWhsSub> sellOutWhsSubList = new ArrayList<>();
		// ��������Ϣ
		LogisticsTab logisticsTab = new LogisticsTab();
		BigDecimal goodNum = new BigDecimal(0);// ��������
		// ͨ��ί�д��������������ȡί�д��������������ӱ���Ϣ
		EntrsAgnDelv entrsAgnDelv = eadd.selectEntrsAgnDelvByDelvSnglId(eAgnDelv.getDelvSnglId());
		// ί�д�����������������ί�д����������ӱ���Ϣ�����Բ���Ҫ�����ѯ�ӱ���Ϣ
		List<EntrsAgnDelvSub> entrsAgnDelvSubList = entrsAgnDelv.getEntrsAgnDelvSub();

		// ��ѯί�д��������������״̬
		if (entrsAgnDelv.getIsNtChk() == 0) {
			Map<String,List<EntrsAgnDelvSub>> handleMap = entrsAgnDelvSubList.stream().collect(Collectors.groupingBy(EntrsAgnDelvSub::getWhsEncd));
			for(Map.Entry<String, List<EntrsAgnDelvSub>> entry : handleMap.entrySet()){
				SellOutWhs  sellOutWhs= new SellOutWhs();//���۳��ⵥ����
				String number = getOrderNo.getSeqNo("WCK", userId,loginTime);
				try {
					BeanUtils.copyProperties(sellOutWhs, entrsAgnDelv);
					//��ί�д��������Ƹ�����ר�÷�Ʊ
					sellOutWhs.setOutWhsId(number);//���ⵥ����
					sellOutWhs.setOutWhsDt(CommonUtil.getLoginTime(loginTime));//���ⵥ����
					sellOutWhs.setSellOrdrInd(entrsAgnDelv.getDelvSnglId());//���۳��ⵥ�ж�Ӧ�����۵�����
					sellOutWhs.setFormTypEncd("009");//�������ͱ���
					sellOutWhs.setToFormTypEncd(entrsAgnDelv.getFormTypEncd());//��Դ��������
					// ͨ��ҵ�������ж��շ����
					if (entrsAgnDelv.getBizTypId().equals("1")) {
						sellOutWhs.setRecvSendCateId("7");// �շ����ͱ�ţ���������
					}
					if (entrsAgnDelv.getBizTypId().equals("2")) {
						sellOutWhs.setRecvSendCateId("6");// �շ����ͱ�ţ���������
					}
					sellOutWhs.setIsNtRtnGood(0);//�Ƿ��˻�
					sellOutWhs.setOutIntoWhsTypId("10");//��������ͱ���
					sellOutWhs.setSetupPers(eAgnDelv.getChkr());// ������,�����۵��������
					sellOutWhs.setSetupTm(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));// ����ʱ��=��ǰʱ��
					
					List<SellOutWhsSub> sellOutWhsSubList = new ArrayList<>();//���۳��ⵥ�ӱ���
					for(EntrsAgnDelvSub entrsAgnDelvSub:entry.getValue()) {
						if (entrsAgnDelvSub.getShdTaxLabour() == null || entrsAgnDelvSub.getIsNtDiscnt() == null) {
							isSuccess = false;
							message += "���Ϊ��" + entrsAgnDelvSub.getDelvSnglId() + "��ί�д�����������,�����ţ�" + entrsAgnDelvSub.getInvtyEncd()
							+ "û������Ӧ˰�������Ի��Ƿ��ۿ����ԣ��޷����\n";
					       throw new RuntimeException(message);
						}
						if (entrsAgnDelvSub.getShdTaxLabour().intValue() == 1 || entrsAgnDelvSub.getIsNtDiscnt().intValue() == 1) {
							continue;
						} else {
							goodNum = goodNum.add(entrsAgnDelvSub.getQty());
							// ����ʵ��
							InvtyTab invtyTab = new InvtyTab();
							invtyTab.setWhsEncd(entrsAgnDelvSub.getWhsEncd());
							invtyTab.setInvtyEncd(entrsAgnDelvSub.getInvtyEncd());
							invtyTab.setBatNum(entrsAgnDelvSub.getBatNum());
							invtyTab = itd.selectInvtyTabsByTerm(invtyTab);
							// ���۵����ʱ�жϿ������Ƿ��иô����Ϣ
							if (invtyTab != null) {
								// a.compareTo(b) -1��ʾС�� 1 ��ʾ���� 0��ʾ���� �Ƚ����۵��������Ϳ���Ŀ�����
								if(invtyTab.getAvalQty().compareTo(entrsAgnDelvSub.getQty()) >= 0) {
									SellOutWhsSub sellOutWhsSub = new SellOutWhsSub();//���۳��ⵥ�ӱ�
									BeanUtils.copyProperties(sellOutWhsSub, entrsAgnDelvSub);//�����۵��ӱ��Ƹ����۳��ⵥ
									sellOutWhsSub.setOutWhsId(sellOutWhs.getOutWhsId());//�����۳��ⵥ������븴�Ƹ����۳��ⵥ�ӱ����������
									// ���û���������ںͱ�����ʱ��ʧЧ����Ĭ��Ϊnull
									if (entrsAgnDelvSub.getPrdcDt() == null || entrsAgnDelvSub.getBaoZhiQi() == null) {
										sellOutWhsSub.setInvldtnDt(null);
									} else {
										// �������۳��ⵥ��ʧЧ����
										sellOutWhsSub.setPrdcDt(entrsAgnDelvSub.getPrdcDt());// ��������
										sellOutWhsSub.setBaoZhiQi(entrsAgnDelvSub.getBaoZhiQi());// ������
										sellOutWhsSub.setInvldtnDt(CalcAmt.getDate(sellOutWhsSub.getPrdcDt(), sellOutWhsSub.getBaoZhiQi()));
									}
									// ��ѯ��������˰����
									// ����ȡ����˰���۸������۳������˰����
									setSellOutWhsCB(invtyTab,entrsAgnDelvSub,sellOutWhsSub);
									sellOutWhsSub.setIsNtRtnGoods(0);// �Ƿ��˻�
									sellOutWhsSub.setToOrdrNum(entrsAgnDelvSub.getOrdrNum());//��Դ�����ӱ����
									sellOutWhsSubList.add(sellOutWhsSub);
									invtyTab.setAvalQty(entrsAgnDelvSub.getQty().abs());
									itd.updateInvtyTabAvalQtyJian(invtyTab);
								}else{
									isSuccess = false;
									message += "���Ϊ��" + eAgnDelv.getDelvSnglId()+ "��ί�д�����������,�����ţ�" + entrsAgnDelvSub.getInvtyEncd()
											+ ",����:" + entrsAgnDelvSub.getBatNum() + "������������㣬�޷����\n";
									throw new RuntimeException(message);
								}
						}else {
							isSuccess = false;
							message += "����Ϊ��" + eAgnDelv.getDelvSnglId() + "��ί�д�����������,������룺"
									+ entrsAgnDelvSub.getInvtyEncd() + ",���Σ�" + entrsAgnDelvSub.getBatNum() + "�Ŀ�治���ڣ��޷����\n";
							throw new RuntimeException(message);
						}
					}
			      }
				if(sellOutWhsSubList.size() > 0){
					// ��˺�������������Ϣ
					PlatOrder platOrder = new PlatOrder();
					logisticsTab.setSaleEncd(entrsAgnDelv.getDelvSnglId());// ί�д�������
//					logisticsTab.setOrderId(entrsAgnDelv.getTxId());// �������� ȥ���۵��еĽ��ױ���
					logisticsTab.setOutWhsId(sellOutWhs.getOutWhsId());// ���۳��ⵥ��
//					logisticsTab.setEcOrderId(entrsAgnDelv.getEcOrderId());// ���̶�����
					logisticsTab.setIsBackPlatform(0);
					logisticsTab.setIsShip(0);
					logisticsTab.setIsPick(0);
					// ������Ʒ����
//					logisticsTab.setBuyerNote(entrsAgnDelv.getBuyerNote());// �������
					logisticsTab.setRecAddress(entrsAgnDelv.getRecvrAddr());// �ջ�����ϸ��ַ
					logisticsTab.setRecName(entrsAgnDelv.getRecvr());// �ռ�������
					logisticsTab.setRecMobile(entrsAgnDelv.getRecvrTel());// �ռ����ֻ���
					logisticsTab.setBizTypId(entrsAgnDelv.getBizTypId());// ҵ�����ͱ���
					logisticsTab.setSellTypId(entrsAgnDelv.getSellTypId());// �������ͱ���
//					logisticsTab.setDeliverWhs(entrsAgnDelv.getDelvAddr());// �����ֿ�
					logisticsTab.setRecvSendCateId(entrsAgnDelv.getRecvSendCateId());// �շ�������
					
					logisticsTab.setGoodNum(goodNum);
					goodNum=new BigDecimal(0);
					ltd.insert(logisticsTab);
					int a = sowd.insertSellOutWhs(sellOutWhs);
					sowds.insertSellOutWhsSub(sellOutWhsSubList);
					sellOutWhsSubList.clear();
				}
			    }catch (IllegalAccessException | InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			int a = eadd.updateEntrsAgnDelvIsNtChk(eAgnDelv);
			if (a==1) {
				isSuccess = true;
				message += "����Ϊ��" + eAgnDelv.getDelvSnglId() + "��ί�д�����������˳ɹ�\n";
			}else {
				isSuccess = false;
				message += "����Ϊ��" + eAgnDelv.getDelvSnglId() + "��ί�д�������������ˣ������ظ����\n";
				throw new RuntimeException(message);
			}
			
		} else {
			isSuccess = false;
			message += "����Ϊ��" + eAgnDelv.getDelvSnglId() + "��ί�д�������������ˣ������ظ����\n";
			throw new RuntimeException(message);
		}
		map.put("isSuccess", isSuccess);
		map.put("message", message);
		return map;
	}
	
	private void setSellOutWhsCB(InvtyTab invtyTab,EntrsAgnDelvSub eAgnDelvSub,SellOutWhsSub sowSub) {
		Map<String, Object> map = new HashMap<>();
		// ��ѯ��������˰���� ����ȡ����˰���۸������۳������˰����
		if (invtyTab.getUnTaxUprc() == null) {
			map.put("invtyEncd", sowSub.getInvtyEncd());
			map.put("whsEncd", sowSub.getWhsEncd());
			map.put("batNum", sowSub.getBatNum());
			BigDecimal pursunTaxUprc = intoWhsDao.selectUnTaxUprc(map);// �ɹ���ⵥ���һ����ⵥ��
			if (pursunTaxUprc == null) {
				BigDecimal noTaxUprc = idd.selectRefCost(sowSub.getInvtyEncd());// ȡ������������һ�βο��ɱ�
				// ����ȡ����˰���۸���ί�д������۳������˰����
				sowSub.setNoTaxUprc(noTaxUprc);
				sowSub.setTaxRate((eAgnDelvSub.getTaxRate()));// ˰��
				// ����δ˰��� ���=δ˰����*δ˰����
				sowSub.setNoTaxAmt(
						CalcAmt.noTaxAmt(sowSub.getNoTaxUprc(), sowSub.getQty()));
				// ����˰�� ˰��=δ˰���*˰��
				sowSub.setTaxAmt(CalcAmt
						.taxAmt(sowSub.getNoTaxUprc(), sowSub.getQty(), sowSub.getTaxRate())
						.divide(new BigDecimal(100)));
				// ���㺬˰���� ��˰����=��˰����*˰��+��˰����
				sowSub.setCntnTaxUprc(CalcAmt.cntnTaxUprc(sowSub.getNoTaxUprc(),
						sowSub.getQty(), sowSub.getTaxRate().divide(new BigDecimal(100))));
				// �����˰�ϼ� ��˰�ϼ�=��˰���*˰��+��˰���=˰��+��˰���
				sowSub.setPrcTaxSum(CalcAmt.prcTaxSum(sowSub.getNoTaxUprc(),
						sowSub.getQty(), sowSub.getTaxRate().divide(new BigDecimal(100))));
				
			} else {
				sowSub.setNoTaxUprc(pursunTaxUprc);
				sowSub.setTaxRate((eAgnDelvSub.getTaxRate()));// ˰��
				// ����δ˰��� ���=δ˰����*δ˰����
				sowSub.setNoTaxAmt(CalcAmt.noTaxAmt(sowSub.getNoTaxUprc(), sowSub.getQty()));
				// ����˰�� ˰��=δ˰���*˰��
				sowSub.setTaxAmt(CalcAmt
						.taxAmt(sowSub.getNoTaxUprc(), sowSub.getQty(), sowSub.getTaxRate())
						.divide(new BigDecimal(100)));
				// ���㺬˰���� ��˰����=��˰����*˰��+��˰����
				sowSub.setCntnTaxUprc(CalcAmt.cntnTaxUprc(sowSub.getNoTaxUprc(),
						sowSub.getQty(), sowSub.getTaxRate().divide(new BigDecimal(100))));
				// �����˰�ϼ� ��˰�ϼ�=��˰���*˰��+��˰���=˰��+��˰���
				sowSub.setPrcTaxSum(CalcAmt.prcTaxSum(sowSub.getNoTaxUprc(), sowSub.getQty(),
						sowSub.getTaxRate().divide(new BigDecimal(100))));
			}
		} else {
			sowSub.setNoTaxUprc(invtyTab.getUnTaxUprc());
			sowSub.setTaxRate((eAgnDelvSub.getTaxRate()));// ˰��
			// ����δ˰��� ���=δ˰����*δ˰����
			sowSub.setNoTaxAmt(CalcAmt.noTaxAmt(sowSub.getNoTaxUprc(), sowSub.getQty()));
			// ����˰�� ˰��=δ˰���*˰��
			sowSub.setTaxAmt(
					CalcAmt.taxAmt(sowSub.getNoTaxUprc(), sowSub.getQty(), sowSub.getTaxRate())
							.divide(new BigDecimal(100)));
			// ���㺬˰���� ��˰����=��˰����*˰��+��˰����
			sowSub.setCntnTaxUprc(CalcAmt.cntnTaxUprc(sowSub.getNoTaxUprc(), sowSub.getQty(),
					sowSub.getTaxRate().divide(new BigDecimal(100))));
			// �����˰�ϼ� ��˰�ϼ�=��˰���*˰��+��˰���=˰��+��˰���
			sowSub.setPrcTaxSum(CalcAmt.prcTaxSum(sowSub.getNoTaxUprc(), sowSub.getQty(),
					sowSub.getTaxRate().divide(new BigDecimal(100))));
		}
	}
	
	// ί�д�������������
	private Map<String, Object> updateEntrsAgnDelvIsNtChkNO(EntrsAgnDelv eAgnDelv) {
		String message = "";
		Boolean isSuccess = true;
		Map<String, Object> map = new HashMap<>();
		// ͨ��ί�д��������������ȡί�д��������������ӱ���Ϣ
		EntrsAgnDelv entrsAgnDelv = eadd.selectEntrsAgnDelvByDelvSnglId(eAgnDelv.getDelvSnglId());
		
		// �����ж����۵������״̬�����״̬Ϊ1ʱ�ſ���ִ����������
		if (entrsAgnDelv.getIsNtChk() == 1) {
			// �������۱�ʶ��ѯ���۳��ⵥ�����״̬ �� �Ƿ����ɼ����
			if (entrsAgnDelv.getIsPick() == 0) {
				List<SellOutWhs> sellOutWhsList = sowd.selectOutWhsIdBySellOrdrInd(eAgnDelv.getDelvSnglId());
				int isNtChkQty=0;
				for(SellOutWhs sellOutWhs:sellOutWhsList){
					if(sellOutWhs.getIsNtChk()==1){
						isNtChkQty++;
					}
				}
				if (isNtChkQty > 0) {
					isSuccess = false;
					message += "����Ϊ��" + eAgnDelv.getDelvSnglId() + "��ί�д�����������Ӧ�������ε��ݡ����۳��ⵥ������ˣ��޷�����\n";
					throw new RuntimeException(message);
//					
				} else {
					// ί�д�����������������ί�д����������ӱ���Ϣ�����Բ���Ҫ�����ѯ�ӱ���Ϣ
					List<EntrsAgnDelvSub> entrsAgnDelvSubList = entrsAgnDelv.getEntrsAgnDelvSub();
					for (EntrsAgnDelvSub enAgnDeSubs : entrsAgnDelvSubList) {
						// ����ʵ��
						InvtyTab invtyTab = new InvtyTab();
						invtyTab.setWhsEncd(enAgnDeSubs.getWhsEncd());
						invtyTab.setInvtyEncd(enAgnDeSubs.getInvtyEncd());
						invtyTab.setBatNum(enAgnDeSubs.getBatNum());
						invtyTab = itd.selectInvtyTabsByTerm(invtyTab);
						if (invtyTab != null) {
							invtyTab.setAvalQty(enAgnDeSubs.getQty());
							itd.updateInvtyTabAvalQtyJia(invtyTab);
						} else {
							isSuccess = false;
							message += "����Ϊ��" + eAgnDelv.getDelvSnglId() + "��ί�д���������û�п�棬�޷�����\n";
							throw new RuntimeException(message);
						}
					}
					int updateFlag = eadd.updateEntrsAgnDelvIsNtChk(eAgnDelv);
					if (updateFlag>=1) {
						isSuccess = true;
						message += "����Ϊ��" + eAgnDelv.getDelvSnglId() + "��ί�д�������������ɹ�!\n";
					}else {
						isSuccess = false;
						message += "����Ϊ��" + eAgnDelv.getDelvSnglId() + "��ί�д���������������,�����ظ�����!\n";
						throw new RuntimeException(message);
					}
					sowd.deleteSellOutWhsBySellOrdrInd(eAgnDelv.getDelvSnglId());// ɾ�����۳��ⵥ
					ssd.deleteLogisticsTab(eAgnDelv.getDelvSnglId());// ɾ��������
				}
			} else {
				isSuccess = false;
				message += "����Ϊ��" + eAgnDelv.getDelvSnglId() + "��ί�д��������������ɶ�Ӧ�ļ������������������\n";
				throw new RuntimeException(message);
			}
		} else {
			message += "����Ϊ��" + eAgnDelv.getDelvSnglId() + "��ί�д��������������󣬲����ظ�����\n";
			throw new RuntimeException(message);
		}
		map.put("isSuccess", isSuccess);
		map.put("message", message);
		return map;
	}

	// ���ί�д����˻���
	private Map<String, Object> updateReturnEntrsAgnDelvIsNtChkOK(String userId, EntrsAgnDelv eAgnDelv,String loginTime) {
		String message = "";
		Boolean isSuccess = true;
		Map<String, Object> map = new HashMap<>();
		// ͨ��ί�д��������������ȡί�д��������������ӱ���Ϣ
		EntrsAgnDelv entrsAgnDelv = eadd.selectEntrsAgnDelvByDelvSnglId(eAgnDelv.getDelvSnglId());
		// ί�д�����������������ί�д����������ӱ���Ϣ�����Բ���Ҫ�����ѯ�ӱ���Ϣ
		List<EntrsAgnDelvSub> entrsAgnDelvSubList = entrsAgnDelv.getEntrsAgnDelvSub();
		if (entrsAgnDelv.getIsNtChk() == 0) {
			Map<String,List<EntrsAgnDelvSub>> handleMap = entrsAgnDelvSubList.stream().collect(Collectors.groupingBy(EntrsAgnDelvSub::getWhsEncd));
			for(Map.Entry<String, List<EntrsAgnDelvSub>> entry : handleMap.entrySet()){
				SellOutWhs  sellOutWhs= new SellOutWhs();//���۳��ⵥ����
				String number = getOrderNo.getSeqNo("WCK", userId,loginTime);
				try {
					BeanUtils.copyProperties(sellOutWhs, entrsAgnDelv);
					//��ί�д��������Ƹ�����ר�÷�Ʊ
					sellOutWhs.setOutWhsId(number);//���ⵥ����
					sellOutWhs.setOutWhsDt(loginTime);//���ⵥ����
					sellOutWhs.setSellOrdrInd(entrsAgnDelv.getDelvSnglId());//���۳��ⵥ�ж�Ӧ�����۵�����
					sellOutWhs.setFormTypEncd("010");//�������ͱ���
					sellOutWhs.setToFormTypEncd(entrsAgnDelv.getFormTypEncd());//��Դ��������
					// ͨ��ҵ�������ж��շ����
					if (entrsAgnDelv.getBizTypId().equals("1")) {
						sellOutWhs.setRecvSendCateId("7");// �շ����ͱ�ţ���������
					}
					if (entrsAgnDelv.getBizTypId().equals("2")) {
						sellOutWhs.setRecvSendCateId("6");// �շ����ͱ�ţ���������
					}
					sellOutWhs.setIsNtRtnGood(1);//�Ƿ��˻�
					sellOutWhs.setOutIntoWhsTypId("10");//��������ͱ���
					sellOutWhs.setSetupPers(eAgnDelv.getChkr());// ������,�����۵��������
					sellOutWhs.setSetupTm(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));// ����ʱ��=��ǰʱ��
					
					List<SellOutWhsSub> sellOutWhsSubList = new ArrayList<>();//���۳��ⵥ�ӱ���
					for(EntrsAgnDelvSub entrsAgnDelvSub:entry.getValue()) {
						BigDecimal qtyList = eadsd.selectEntrsAgnDelvSubQty(entrsAgnDelvSub);
						if (qtyList == null) {
							isSuccess = false;
							message += "����Ϊ��" + eAgnDelv.getDelvSnglId() + "���˻���������Ϊ�գ��޷����\n";
							throw new RuntimeException(message);
						} else if (qtyList.intValue() == 0) {
							continue;
						} else {
							// ����ʵ��
							InvtyTab invtyTab = new InvtyTab();
							invtyTab.setWhsEncd(entrsAgnDelvSub.getWhsEncd());
							invtyTab.setInvtyEncd(entrsAgnDelvSub.getInvtyEncd());
							invtyTab.setBatNum(entrsAgnDelvSub.getBatNum());
							invtyTab = itd.selectInvtyTabsByTerm(invtyTab);
							SellOutWhsSub sellOutWhsSub = new SellOutWhsSub();//���۳��ⵥ�ӱ�
							BeanUtils.copyProperties(sellOutWhsSub, entrsAgnDelvSub);//�����۵��ӱ��Ƹ����۳��ⵥ
							sellOutWhsSub.setOutWhsId(sellOutWhs.getOutWhsId());//�����۳��ⵥ������븴�Ƹ����۳��ⵥ�ӱ����������
							// ���û���������ںͱ�����ʱ��ʧЧ����Ĭ��Ϊnull
							if (entrsAgnDelvSub.getPrdcDt() == null || entrsAgnDelvSub.getBaoZhiQi() == null) {
								sellOutWhsSub.setInvldtnDt(null);
							} else {
								// �������۳��ⵥ��ʧЧ����
								sellOutWhsSub.setPrdcDt(entrsAgnDelvSub.getPrdcDt());// ��������
								sellOutWhsSub.setBaoZhiQi(entrsAgnDelvSub.getBaoZhiQi());// ������
								sellOutWhsSub.setInvldtnDt(CalcAmt.getDate(sellOutWhsSub.getPrdcDt(), sellOutWhsSub.getBaoZhiQi()));
							}
							// ��ѯ��������˰����
							// ����ȡ����˰���۸������۳������˰����
							setRtnSellOutWhsCB(invtyTab,entrsAgnDelv,entrsAgnDelvSub,sellOutWhsSub);
							sellOutWhsSub.setIsNtRtnGoods(1);// �Ƿ��˻�
							sellOutWhsSub.setToOrdrNum(entrsAgnDelvSub.getOrdrNum());//��Դ�����ӱ����
							sellOutWhsSubList.add(sellOutWhsSub);
							// ���۵����ʱ�жϿ������Ƿ��иô����Ϣ
							if (invtyTab != null) {
								invtyTab.setAvalQty(entrsAgnDelvSub.getQty().abs());
								// �ڿ����н���Ӧ�Ŀ���������
								itd.updateInvtyTabAvalQtyJia(invtyTab);
							} else {
								// �����û�иô��ʱ����Ҫ���������Ϣ����������˻���ʵ�壬�����Ҫ���˻����������ó���������������
								entrsAgnDelvSub.setQty(entrsAgnDelvSub.getQty().abs());
								itd.insertInvtyTabToEnAgDelSub(entrsAgnDelvSub);
							}
					}
					if (entrsAgnDelvSub.getToOrdrNum() != null && entrsAgnDelvSub.getToOrdrNum() != 0) {
						EntrsAgnDelvSub eAgnDelvSubs = eadsd.selectEntDeSubToOrdrNum(entrsAgnDelvSub.getToOrdrNum());// �����˻����ӱ���Ų�ѯ���۵��п�������							BigDecimal rtnblQty = eAgnDelvSubs.getUnBllgRtnGoodsQty();
						BigDecimal rtnblQty = eAgnDelvSubs.getUnBllgRtnGoodsQty();
						if (rtnblQty != null) {
							if (rtnblQty.compareTo(entrsAgnDelvSub.getQty().abs()) >0) {
								map.put("unBllgRtnGoodsQty", entrsAgnDelvSub.getQty().abs());// �޸Ŀ�������
								eadsd.updateEntDeSubUnBllgRtnGoodsQty(map);// �����˻����ӱ�����޸����۵��п�������
							} else {
								isSuccess = false;
								message += "���ݺ�Ϊ��" + eAgnDelv.getDelvSnglId() + "���˻������д����"
										+ entrsAgnDelvSub.getInvtyEncd() + "���ۼ��˻��������ڿ����������޷���ˣ�\n";
								throw new RuntimeException(message);
							}
						} else {
							isSuccess = false;
							message += "���ݺ�Ϊ��" + eAgnDelv.getDelvSnglId() + "���˻�����Ӧ�Ĳɹ������п������������ڣ��޷���ˣ�\n";
							throw new RuntimeException(message);
						}
					}
			     }
				if (sellOutWhsSubList.size() > 0) {
					sowd.insertSellOutWhs(sellOutWhs);
					sowds.insertSellOutWhsSub(sellOutWhsSubList);
					sellOutWhsSubList.clear();
				} 
			    }catch (IllegalAccessException | InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			int b = eadd.updateEntrsAgnDelvIsNtChk(eAgnDelv);
			if (b >= 1) {
				isSuccess = true;
				message += "����Ϊ��" + eAgnDelv.getDelvSnglId() + "��ί�д����˻�����˳ɹ���\n";
			} else {
			    isSuccess = false;
				message += "����Ϊ��" + eAgnDelv.getDelvSnglId() + "��ί�д����˻������ʧ�ܣ�\n";
				throw new RuntimeException(message);
			}
		} else {
			isSuccess = false;
			message += "����Ϊ��" + eAgnDelv.getDelvSnglId() + "��ί�д����˻�������ˣ������ظ����\n";
			throw new RuntimeException(message);
		}
		map.put("isSuccess", isSuccess);
		map.put("message", message);
		return map;
	}

	private void setRtnSellOutWhsCB( InvtyTab invtyTab,EntrsAgnDelv eAgnDelv,EntrsAgnDelvSub eAgnDelvSub,
			SellOutWhsSub sowSub) {
		Map<String, Object> map = new HashMap<>();
		if (eAgnDelvSub.getToOrdrNum() != null) {
			map.put("toOrdrNum", eAgnDelvSub.getToOrdrNum());
			map.put("sellSnglId", eAgnDelv.getDelvSnglId());//ί�д�����������
			//ԭ���۵���Ӧ�ĳ��ⵥ�ӱ����
			BigDecimal noTaxUprc1 = sowd.selectSellOutWhsSubByToOrdrNum(map);
			if (noTaxUprc1 != null) {
				sowSub.setNoTaxUprc(noTaxUprc1);
				// ����δ˰��� ���=δ˰����*δ˰����
				sowSub.setNoTaxAmt(CalcAmt.noTaxAmt(sowSub.getNoTaxUprc(), sowSub.getQty()));
				// ����˰�� ˰��=δ˰���*˰��
				sowSub.setTaxAmt(CalcAmt.taxAmt(sowSub.getNoTaxUprc(), sowSub.getQty(),
						sowSub.getTaxRate().divide(new BigDecimal(100))));
				// ���㺬˰���� ��˰����=��˰����*˰��+��˰����
				sowSub.setCntnTaxUprc(CalcAmt.cntnTaxUprc(sowSub.getNoTaxUprc(), sowSub.getQty(),
						sowSub.getTaxRate().divide(new BigDecimal(100))));
				// �����˰�ϼ� ��˰�ϼ�=��˰���*˰��+��˰���=˰��+��˰���
				sowSub.setPrcTaxSum(CalcAmt.prcTaxSum(sowSub.getNoTaxUprc(), sowSub.getQty(),
						sowSub.getTaxRate().divide(new BigDecimal(100))));
			} else {
				throw new RuntimeException("���Ϊ" + eAgnDelv.getDelvSnglId() + "��ί�д����˻������ֶ�Ӧ�ķ����������ڣ������ԭ�������\n");
			}
		} else {
			map.put("invtyEncd", eAgnDelvSub.getInvtyEncd());
			BigDecimal pursunTaxUprc = intoWhsDao.selectUnTaxUprc(map);// �ɹ���ⵥ���һ����ⵥ��
			if (pursunTaxUprc != null) {
				// ����ȡ����˰���۸���ί�д������۳������˰����
				sowSub.setNoTaxUprc(pursunTaxUprc);
				// ����δ˰��� ���=δ˰����*δ˰����
				sowSub.setNoTaxAmt(CalcAmt.noTaxAmt(sowSub.getNoTaxUprc(), sowSub.getQty()));
				// ����˰�� ˰��=δ˰���*˰��
				sowSub.setTaxAmt(CalcAmt.taxAmt(sowSub.getNoTaxUprc(), sowSub.getQty(),
						sowSub.getTaxRate().divide(new BigDecimal(100))));
				// ���㺬˰���� ��˰����=��˰����*˰��+��˰����
				sowSub.setCntnTaxUprc(CalcAmt.cntnTaxUprc(sowSub.getNoTaxUprc(), sowSub.getQty(),
						sowSub.getTaxRate().divide(new BigDecimal(100))));
				// �����˰�ϼ� ��˰�ϼ�=��˰���*˰��+��˰���=˰��+��˰���
				sowSub.setPrcTaxSum(CalcAmt.prcTaxSum(sowSub.getNoTaxUprc(), sowSub.getQty(),
						sowSub.getTaxRate().divide(new BigDecimal(100))));
			} else {
				// ��ѯ��������еĲο��ɱ�
				BigDecimal noTaxUprc2 = idd.selectRefCost(eAgnDelvSub.getInvtyEncd());
				if (noTaxUprc2 != null) {
					// ����ȡ����˰���۸���ί�д������۳������˰����
					sowSub.setNoTaxUprc(noTaxUprc2);
					// ����δ˰��� ���=δ˰����*δ˰����
					sowSub.setNoTaxAmt(CalcAmt.noTaxAmt(sowSub.getNoTaxUprc(), sowSub.getQty()));
					// ����˰�� ˰��=δ˰���*˰��
					sowSub.setTaxAmt(CalcAmt.taxAmt(sowSub.getNoTaxUprc(), sowSub.getQty(),
							sowSub.getTaxRate().divide(new BigDecimal(100))));
					// ���㺬˰���� ��˰����=��˰����*˰��+��˰����
					sowSub.setCntnTaxUprc(CalcAmt.cntnTaxUprc(sowSub.getNoTaxUprc(), sowSub.getQty(),
							sowSub.getTaxRate().divide(new BigDecimal(100))));
					// �����˰�ϼ� ��˰�ϼ�=��˰���*˰��+��˰���=˰��+��˰���
					sowSub.setPrcTaxSum(CalcAmt.prcTaxSum(sowSub.getNoTaxUprc(), sowSub.getQty(),
							sowSub.getTaxRate().divide(new BigDecimal(100))));
				} else {
					throw new RuntimeException(
							"����Ϊ" + eAgnDelv.getDelvSnglId() + "��ί�д����˻����޷����ɶ�Ӧ�������۳��ⵥ�����ʧ�ܣ������ԭ�������\n");
				}
			}
		}
	}

	// ����ί�д����˻���
	private Map<String, Object> updateReturnEntrsAgnDelvIsNtChkNO(EntrsAgnDelv eAgnDelv) {
		String message = "";
		Boolean isSuccess = true;
		Map<String, Object> map = new HashMap<>();
		// ͨ��ί�д��������������ȡί�д��������������ӱ���Ϣ
		EntrsAgnDelv entrsAgnDelv = eadd.selectEntrsAgnDelvByDelvSnglId(eAgnDelv.getDelvSnglId());
		
		// �����ж����۵������״̬�����״̬Ϊ1ʱ�ſ���ִ����������
		if (entrsAgnDelv.getIsNtChk() == 1) {
			// ����ί�д�����������ѯ��Ӧ�����۳��ⵥ��Ϣ
			List<SellOutWhs> sellOutWhsList = sowd.selectOutWhsIdBySellOrdrInd(eAgnDelv.getDelvSnglId());
			int isNtChkQty=0;
			for(SellOutWhs sellOutWhs:sellOutWhsList){
				if(sellOutWhs.getIsNtChk()==1){
					isNtChkQty++;
				}
			}
			if (isNtChkQty > 0) {
				isSuccess = false;
				message += "����Ϊ��" + eAgnDelv.getDelvSnglId() + "��ί�д�����������Ӧ�������ε��ݡ����۳��ⵥ������ˣ��޷�����\n";
				throw new RuntimeException(message);
			} else {
				// ί�д�����������������ί�д����������ӱ���Ϣ�����Բ���Ҫ�����ѯ�ӱ���Ϣ
				List<EntrsAgnDelvSub> entrsAgnDelvSubList = entrsAgnDelv.getEntrsAgnDelvSub();
				for (EntrsAgnDelvSub enAgnDeSubs : entrsAgnDelvSubList) {
					// ����ʵ��
					InvtyTab invtyTab = new InvtyTab();
					invtyTab.setWhsEncd(enAgnDeSubs.getWhsEncd());
					invtyTab.setInvtyEncd(enAgnDeSubs.getInvtyEncd());
					invtyTab.setBatNum(enAgnDeSubs.getBatNum());
					invtyTab = itd.selectInvtyTabsByTerm(invtyTab);
					if (invtyTab != null) {
						if (invtyTab.getAvalQty().compareTo(enAgnDeSubs.getQty().abs()) == 1
								|| invtyTab.getAvalQty().compareTo(enAgnDeSubs.getQty().abs()) == 0) {
							invtyTab.setAvalQty(enAgnDeSubs.getQty());
							itd.updateInvtyTabAvalQtyJia(invtyTab);
						} else if (invtyTab.getAvalQty().compareTo(enAgnDeSubs.getQty().abs()) == -1) {
							isSuccess = false;
							message += "����Ϊ��" + eAgnDelv.getDelvSnglId() + "��ί�д����˻����д�����룺" + enAgnDeSubs.getInvtyEncd()
									+ ",���Σ�" + enAgnDeSubs.getBatNum() + "�Ŀ�治�㣬�޷�����\n";
							throw new RuntimeException(message);
						}
					} else {
						isSuccess = false;
						message += "����Ϊ��" + eAgnDelv.getDelvSnglId() + "��ί�д����˻�����,������룺" + enAgnDeSubs.getInvtyEncd()
								+ ",���Σ�" + enAgnDeSubs.getBatNum() + "�Ŀ�治���ڣ��޷�����\n";
						throw new RuntimeException(message);
					}
				}
				int a = eadd.updateEntrsAgnDelvIsNtChk(eAgnDelv);
				sowd.deleteSellOutWhsBySellOrdrInd(eAgnDelv.getDelvSnglId());// ɾ�����۳��ⵥ
				if (a >= 1) {
					isSuccess = true;
					message += "����Ϊ" + eAgnDelv.getDelvSnglId() + "��ί�д����˻�������ɹ���\n";
				} else {
					isSuccess = false;
					message += "����Ϊ" + eAgnDelv.getDelvSnglId() + "��ί�д����˻�������ʧ�ܣ�\n";
					throw new RuntimeException(message);
				}
			}
		} else {
			isSuccess = false;
			message += "����Ϊ��" + eAgnDelv.getDelvSnglId() + "��ί�д����˻���δ��ˣ�������˸õ���\n";
			throw new RuntimeException(message);
		}
		map.put("isSuccess", isSuccess);
		map.put("message", message);
		return map;
	}

	// ����ί�д���������
	@Override
	public String uploadFileAddDb(MultipartFile file) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		Map<String, EntrsAgnDelv> entrsAgnDelvMap = uploadScoreInfo(file);

		for (Map.Entry<String, EntrsAgnDelv> entry : entrsAgnDelvMap.entrySet()) {
			if (eadd.selectEntrsAgnDelvById(entry.getValue().getDelvSnglId()) != null) {
				isSuccess = false;
				message = "ί�д����������в��ֶ��������Ѵ��ڣ��޷����룬������ٵ��룡";
				throw new RuntimeException(message);
			} else {
				eadd.insertEntrsAgnDelvUpload(entry.getValue());
				for (EntrsAgnDelvSub entrsAgnDelvSub : entry.getValue().getEntrsAgnDelvSub()) {
					entrsAgnDelvSub.setDelvSnglId(entry.getValue().getDelvSnglId());
				}
				eadsd.insertEntrsAgnDelvSub(entry.getValue().getEntrsAgnDelvSub());
			}
			isSuccess = true;
			message = "ί�д�������������ɹ���";
		}

		try {
			resp = BaseJson.returnRespObj("/purc/EntrsAgnDelv/uploadEntrsAgnDelvFile", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// ����excle
	private Map<String, EntrsAgnDelv> uploadScoreInfo(MultipartFile file) {
		Map<String, EntrsAgnDelv> temp = new HashMap<>();
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
				j++;
				Row r = sht0.getRow(i);
				// �����ǰ�е��кţ���0��ʼ��δ�ﵽ2�������У������ѭ��
				if (r.getRowNum() < 1) {
					continue;
				}
				String orderNo = GetCellData(r, "ί�д�������������");
				// ����ʵ����
				EntrsAgnDelv entrsAgnDelv = new EntrsAgnDelv();
				if (temp.containsKey(orderNo)) {
					entrsAgnDelv = temp.get(orderNo);
				}
				// ȡ����ǰ�е�1����Ԫ�����ݣ�����װ��pursOrdrʵ��ĸ���������
				entrsAgnDelv.setSellTypId(GetCellData(r, "�������ͱ���"));// �������ͱ���
				entrsAgnDelv.setDelvSnglId(orderNo); // ί�д�������������
				if (GetCellData(r, "ί�д�������������") == null || GetCellData(r, "ί�д�������������").equals("")) {
					entrsAgnDelv.setDelvSnglDt(null);
				} else {
					entrsAgnDelv.setDelvSnglDt(GetCellData(r, "ί�д�������������").replaceAll("[^0-9:-]", " "));// ���۳��ⵥ����
				}
				entrsAgnDelv.setCustId(GetCellData(r, "�ͻ�����"));// �ͻ�����
				entrsAgnDelv.setCustOrdrNum(GetCellData(r, "�ͻ�������"));// �ͻ�������
				entrsAgnDelv.setDeptId(GetCellData(r, "���ű���"));// ���ű���
//				entrsAgnDelv.setDepName(GetCellData(r, "��������"));//��������
				entrsAgnDelv.setAccNum(GetCellData(r, "ҵ��Ա����"));// �û�����
				entrsAgnDelv.setUserName(GetCellData(r, "ҵ��Ա����"));// �û�����
				entrsAgnDelv.setFormTypEncd(GetCellData(r, "�������ͱ���"));// ��������
				entrsAgnDelv.setBizTypId(GetCellData(r, "ҵ�����ͱ���"));// ҵ��������
//				entrsAgnDelv.setRecvSendCateId(GetCellData(r, "�û�����"));//�շ�������
				entrsAgnDelv.setSetupPers(GetCellData(r, "������"));// ������
				if (GetCellData(r, "����ʱ��") == null || GetCellData(r, "����ʱ��").equals("")) {
					entrsAgnDelv.setSetupTm(null);
				} else {
					entrsAgnDelv.setSetupTm(GetCellData(r, "����ʱ��").replaceAll("[^0-9:-]", " "));// ����ʱ��
				}
				entrsAgnDelv.setIsNtChk(new Double(GetCellData(r, "�Ƿ����")).intValue());// �Ƿ����
				entrsAgnDelv.setChkr(GetCellData(r, "�����"));// �����
				if (GetCellData(r, "���ʱ��") == null || GetCellData(r, "���ʱ��").equals("")) {
					entrsAgnDelv.setChkDt(null);
				} else {
					entrsAgnDelv.setChkDt(GetCellData(r, "���ʱ��").replaceAll("[^0-9:-]", " "));// ���ʱ��
				}
				entrsAgnDelv.setMdfr(GetCellData(r, "�޸���")); // �޸���
				if (GetCellData(r, "�޸�ʱ��") == null || GetCellData(r, "�޸�ʱ��").equals("")) {
					entrsAgnDelv.setModiTm(null);
				} else {
					entrsAgnDelv.setModiTm(GetCellData(r, "�޸�ʱ��").replaceAll("[^0-9:-]", " "));// �޸�ʱ��
				}
				entrsAgnDelv.setIsNtBookEntry(new Double(GetCellData(r, "�Ƿ����")).intValue());// �Ƿ����
				entrsAgnDelv.setBookEntryPers(GetCellData(r, "������"));// ������
				if (GetCellData(r, "����ʱ��") == null || GetCellData(r, "����ʱ��").equals("")) {
					entrsAgnDelv.setBookEntryTm(null);
				} else {
					entrsAgnDelv.setBookEntryTm(GetCellData(r, "����ʱ��").replaceAll("[^0-9:-]", " "));// ����ʱ��
				}
				entrsAgnDelv.setMemo(GetCellData(r, "��ͷ��ע"));// ����ע
				entrsAgnDelv.setIsNtRtnGood(new Double(GetCellData(r, "�˻���ʶ")).intValue());// �Ƿ��˻�
				// entrsAgnDelv.setIsPick(new Double(GetCellData(r,"�Ƿ���")).intValue());//�Ƿ���
				entrsAgnDelv.setIsNtStl(new Double(GetCellData(r, "�Ƿ����")).intValue());// �Ƿ����
				entrsAgnDelv.setRecvr(GetCellData(r, "�ռ���"));// �ռ���
				entrsAgnDelv.setRecvrTel(GetCellData(r, "�ռ��˵绰"));// �ռ��˵绰
				entrsAgnDelv.setRecvrAddr(GetCellData(r, "�ռ��˵�ַ"));// �ռ��˵�ַ
				entrsAgnDelv.setRecvrEml(GetCellData(r, "�ռ�������"));// �ռ�������
				entrsAgnDelv.setIsNtMakeVouch(new Double(GetCellData(r, "�Ƿ�����ƾ֤")).intValue());

				List<EntrsAgnDelvSub> entrsAgnDelvSubList = entrsAgnDelv.getEntrsAgnDelvSub();// ί�д����������ӱ�
				if (entrsAgnDelvSubList == null) {
					entrsAgnDelvSubList = new ArrayList<>();
				}
				EntrsAgnDelvSub entrsAgnDelvSub = new EntrsAgnDelvSub();
				entrsAgnDelvSub.setWhsEncd(GetCellData(r, "�ֿ����"));// �ֿ����
//				entrsAgnDelvSub.setWhsNm(GetCellData(r, "�ֿ�����"));//�ֿ�����
				entrsAgnDelvSub.setInvtyEncd(GetCellData(r, "�������"));// �������
				entrsAgnDelvSub.setQty(GetBigDecimal(GetCellData(r, "����"), 8));// 8��ʾС��λ�� //����
				entrsAgnDelvSub.setUnBllgQty(GetBigDecimal(GetCellData(r, "δ��Ʊ����"), 8).abs());// 8��ʾС��λ�� //δ��Ʊ����
				entrsAgnDelvSub.setCntnTaxUprc(GetBigDecimal(GetCellData(r, "��˰����"), 8)); // ��˰����
				entrsAgnDelvSub.setNoTaxUprc(GetBigDecimal(GetCellData(r, "��˰����"), 8)); // ��˰����
				entrsAgnDelvSub.setNoTaxAmt(GetBigDecimal(GetCellData(r, "��˰���"), 8)); // ��˰���
				entrsAgnDelvSub.setTaxAmt(GetBigDecimal(GetCellData(r, "˰��"), 8)); // ˰��
				entrsAgnDelvSub.setPrcTaxSum(GetBigDecimal(GetCellData(r, "��˰�ϼ�"), 8)); // ��˰�ϼ�
				entrsAgnDelvSub.setTaxRate(GetBigDecimal(GetCellData(r, "˰��"), 8)); // ˰��
				entrsAgnDelvSub.setBxQty(GetBigDecimal(GetCellData(r, "����"), 8)); // ����
				entrsAgnDelvSub.setBxRule(GetBigDecimal(GetCellData(r, "���"), 8)); // ���
				entrsAgnDelvSub.setBatNum(GetCellData(r, "����"));// ����
				entrsAgnDelvSub.setIntlBat(GetCellData(r, "��������"));// ��������
				if (GetCellData(r, "��������") == null || GetCellData(r, "��������").equals("")) {
					entrsAgnDelvSub.setPrdcDt(null);
				} else {
					entrsAgnDelvSub.setPrdcDt(GetCellData(r, "��������").replaceAll("[^0-9:-]", " "));// ��������
				}
				entrsAgnDelvSub.setBaoZhiQi(new Double(GetCellData(r, "������")).intValue());// ������
				if (GetCellData(r, "ʧЧ����") == null || GetCellData(r, "ʧЧ����").equals("")) {
					entrsAgnDelvSub.setInvldtnDt(null);
				} else {
					entrsAgnDelvSub.setInvldtnDt(GetCellData(r, "ʧЧ����").replaceAll("[^0-9:-]", " "));// ʧЧ����
				}
				entrsAgnDelvSub.setProjEncd(GetCellData(r, "��Ŀ����"));// ��Ŀ����
				entrsAgnDelvSub.setProjNm(GetCellData(r, "��Ŀ����"));// ��Ŀ����
//				entrsAgnDelvSub.setDiscntRatio(GetCellData(r, "�ۿ۱���"));//�ۿ۱���
				entrsAgnDelvSub.setStlQty(GetBigDecimal(GetCellData(r, "��������"), 8));// 8��ʾС��λ�� //��������
				entrsAgnDelvSub.setStlUprc(GetBigDecimal(GetCellData(r, "���㵥��"), 8));// 8��ʾС��λ�� //���㵥��
				entrsAgnDelvSub.setStlAmt(GetBigDecimal(GetCellData(r, "������"), 8));// 8��ʾС��λ�� //������

				entrsAgnDelvSub.setUnBllgRtnGoodsQty(GetBigDecimal(GetCellData(r, "��������"), 8)); // ��������
				entrsAgnDelvSub.setBllgQty(GetBigDecimal(GetCellData(r, "��Ʊ����"), 8));// 8��ʾС��λ�� //��Ʊ����
//				entrsAgnDelvSub.setFinalStlQty(GetBigDecimal(GetCellData(r, "����������"), 8));//8��ʾС��λ�� //����������
//				entrsAgnDelvSub.setFinalStlAmt(GetBigDecimal(GetCellData(r, "��������"), 8));//8��ʾС��λ�� //��������
//				entrsAgnDelvSub.setAccmStlAmt(GetBigDecimal(GetCellData(r, "�ۼƽ�����"), 8));//8��ʾС��λ�� //�ۼƽ�����
//				entrsAgnDelvSub.setAccmOutWhsQty(GetBigDecimal(GetCellData(r, "�ۼƳ�������"), 8));//8��ʾС��λ�� //�ۼƳ�������
				entrsAgnDelvSub.setMemo(GetCellData(r, "���屸ע"));// �ӱ�ע
				entrsAgnDelvSub.setIsComplimentary(new Double(GetCellData(r, "��Ʒ")).intValue());// �Ƿ���Ʒ
//				entrsAgnDelvSub.setOrdrNum(Long.parseLong(GetCellData(r, "�������ӱ�id")));

				entrsAgnDelvSubList.add(entrsAgnDelvSub);
				entrsAgnDelv.setEntrsAgnDelvSub(entrsAgnDelvSubList);
				temp.put(orderNo, entrsAgnDelv);
			}
			fileIn.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("�ļ��ĵ�" + j + "�е����ʽ�����޷�����!" + e.getMessage());
		}
		return temp;
	}

	// ����ʱ��ѯ����ί�д���������
	@Override
	public String queryEntrsAgnDelvLists(Map map) {
		String resp = "";
		List<String> delvSnglIdList = getList((String) map.get("delvSnglId"));// ί�д�����������
		List<String> custIdList = getList((String) map.get("custId"));// �ͻ�����
		List<String> accNumList = getList((String) map.get("accNum"));// ҵ��Ա����
		List<String> invtyClsEncdList = getList((String) map.get("invtyClsEncd"));// ����������
		List<String> invtyCdList = getList((String) map.get("invtyCd"));// �������
		List<String> deptIdList = getList((String) map.get("deptId"));// ���ű���
		List<String> whsEncdList = getList((String) map.get("whsEncd"));// �ֿ����
		List<String> custOrdrNumList = getList((String) map.get("custOrdrNum"));// �ͻ�������
		List<String> invtyEncdList = getList((String) map.get("invtyEncd"));// �������
		List<String> batNumList = getList((String) map.get("batNum"));// ����

		map.put("delvSnglIdList", delvSnglIdList);
		map.put("custIdList", custIdList);
		map.put("accNumList", accNumList);
		map.put("invtyClsEncdList", invtyClsEncdList);
		map.put("invtyCdList", invtyCdList);
		map.put("deptIdList", deptIdList);
		map.put("whsEncdList", whsEncdList);
		map.put("custOrdrNumList", custOrdrNumList);
		map.put("invtyEncdList", invtyEncdList);
		map.put("batNumList", batNumList);
		List<EntrsAgnDelv> poList = eadd.selectEntrsAgnDelvLists(map);
		int count = eadd.selectEntrsAgnDelvCounts(map);

		int pageNo = Integer.parseInt(map.get("pageNo").toString());
		int pageSize = Integer.parseInt(map.get("pageSize").toString());
		int listNum = poList.size();
		int pages = count / pageSize + 1;

		try {
			resp = BaseJson.returnRespList("purc/EntrsAgnDelv/queryEntrsAgnDelvLists", true, "��ѯ�ɹ���", count, pageNo,
					pageSize, listNum, pages, poList);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// ί�д����˻�������ʱ����ί�д����������ӱ���Ϣ
	@Override
	public String selectEntDeSubUnBllgRtnGoodsQty(String delvSnglId) {
		// TODO Auto-generated method stub
		String resp = "";
		List<String> lists = getList(delvSnglId);
		List<EntrsAgnDelvSub> entrsAgnDelvSubList = eadsd.selectEntDeSubUnBllgRtnGoodsQty(lists);
		try {
			resp = BaseJson.returnRespObjList("purc/EntrsAgnDelv/selectEntDeSubUnBllgRtnGoodsQty", true, "��ѯ�ɹ���", null,
					entrsAgnDelvSubList);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	@Override
	public String uploadFileAddDb(MultipartFile file, int i) {
		// TODO Auto-generated method stub
		return null;
	}
	// ��ҳ+����
		@Override
		public String queryEntrsAgnDelvListOrderBy(Map map) {
			String resp = "";
			List<String> delvSnglIdList = getList((String) map.get("delvSnglId"));// ί�д�����������
			List<String> custIdList = getList((String) map.get("custId"));// �ͻ�����
			List<String> accNumList = getList((String) map.get("accNum"));// ҵ��Ա����
			List<String> invtyClsEncdList = getList((String) map.get("invtyClsEncd"));// ����������
			List<String> invtyCdList = getList((String) map.get("invtyCd"));// �������
			List<String> deptIdList = getList((String) map.get("deptId"));// ���ű���
			List<String> whsEncdList = getList((String) map.get("whsEncd"));// �ֿ����
			List<String> custOrdrNumList = getList((String) map.get("custOrdrNum"));// �ͻ�������
			List<String> invtyEncdList = getList((String) map.get("invtyEncd"));// �������
			List<String> batNumList = getList((String) map.get("batNum"));// ����

			map.put("delvSnglIdList", delvSnglIdList);
			map.put("custIdList", custIdList);
			map.put("accNumList", accNumList);
			map.put("invtyClsEncdList", invtyClsEncdList);
			map.put("invtyCdList", invtyCdList);
			map.put("deptIdList", deptIdList);
			map.put("whsEncdList", whsEncdList);
			map.put("custOrdrNumList", custOrdrNumList);
			map.put("invtyEncdList", invtyEncdList);
			map.put("batNumList", batNumList);
			List<zizhu> poList;
			if (map.get("sort") == null||map.get("sort").equals("") ||
				map.get("sortOrder") == null||map.get("sortOrder").equals("")) {
				map.put("sort","ead.delv_sngl_dt");
				map.put("sortOrder","desc");
			}
				poList =eadd.selectEntrsAgnDelvListOrderBy(map);
				Map tableSums = eadd.selectEntrsAgnDelvListSums(map);
				if (null!=tableSums) {
					DecimalFormat df = new DecimalFormat("#,##0.00");
					for(Map.Entry entry : (Set<Entry>)tableSums.entrySet()) {
						String s = df.format((BigDecimal) entry.getValue()==null?new BigDecimal(0.00):entry.getValue());
						entry.setValue(s);
					}
				}
				
			int count = eadd.selectEntrsAgnDelvCount(map);

			int pageNo = Integer.parseInt(map.get("pageNo").toString());
			int pageSize = Integer.parseInt(map.get("pageSize").toString());
			int listNum = poList.size();
			int pages = count / pageSize + 1;

			try {
				resp = BaseJson.returnRespList("purc/EntrsAgnDelv/queryEntrsAgnDelvListOrderBy", true, "��ѯ�ɹ���", count, pageNo,
						pageSize, listNum, pages, poList,tableSums);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return resp;
		}
		
		public static class zizhu{
			@JsonProperty("��������")
			public String delvSnglId;//���������
			@JsonProperty("��������")
			public String delvSnglDt;//����������
			@JsonProperty("�������ͱ���")
			public String formTypEncd;//�������ͱ���
			@JsonProperty("�������ͱ��")
			public String sellTypId;//�������ͱ��
			@JsonProperty("ҵ�����ͱ��")
			public String bizTypId;//ҵ�����ͱ��
			@JsonProperty("�շ����ͱ��")
			public String recvSendCateId;//�շ����ͱ��
			@JsonProperty("�ͻ����")
			public String custId;//�ͻ����
			@JsonProperty("�û����")
			public String accNum;//�û����
			@JsonProperty("�û�����")
			public String userName;//�û�����
			@JsonProperty("���ű��")
			public String deptId;//���ű��
			@JsonProperty("��������")
			public String deptName;//��������
			@JsonProperty("������")
			public String setupPers;//������
			@JsonProperty("����ʱ��")
			public String setupTm;//����ʱ��
			@JsonProperty("�Ƿ����")
			public Integer isNtBookEntry;//�Ƿ����
			@JsonProperty("������")
			public String bookEntryPers;//������
			@JsonProperty("����ʱ��")
			public String bookEntryTm;//����ʱ��
			@JsonProperty("�Ƿ����")
			public Integer isNtChk;//�Ƿ����
			@JsonProperty("�����")
			public String chkr;//�����
			@JsonProperty("���ʱ��")
			public String chkDt;//���ʱ��
			@JsonProperty("�޸���")
			public String mdfr;//�޸���
			@JsonProperty("�޸�ʱ��")
			public String modiTm;//�޸�ʱ��
			@JsonProperty("������ַ���")
			public String delvAddrId;//������ַ���
			@JsonProperty("������ַ����")
			public String delvAddrNm;//������ַ����
			@JsonProperty("���۶�����")
			public String sellOrdrId;//���۶�����
			@JsonProperty("���۷�Ʊ��")
			public String sellInvId;//���۷�Ʊ��
			@JsonProperty("���۷�Ʊ�����ʶ")
			public String sellInvMasTabInd;//���۷�Ʊ�����ʶ
			@JsonProperty("�Ƿ����� ")
			public Integer isNtSellUse;//�Ƿ����� 
			@JsonProperty("�Ƿ���⿪Ʊ")
			public Integer isNtOutWhsBllg;//�Ƿ���⿪Ʊ
			@JsonProperty("�Ƿ���Ҫ��Ʊ")
			public Integer isNtNeedBllg;//�Ƿ���Ҫ��Ʊ
			@JsonProperty("�Ƿ����")
			public Integer isNtStl;//�Ƿ����
			@JsonProperty("�ռ���")
			public String recvr;//�ռ���
			@JsonProperty("�ռ��绰")
			public String recvrTel;//�ռ��绰
			@JsonProperty("�ռ��˵�ַ")
			public String recvrAddr;//�ռ��˵�ַ
			@JsonProperty("�ռ�������")
			public String recvrEml;//�ռ�������
			@JsonProperty("�Ƿ��� ")
			public Integer isPick;//�Ƿ��� 
			@JsonProperty("�Ƿ��˻�(����)")
			public Integer isNtRtnGood;//�Ƿ��˻�
			@JsonProperty("�ͻ�������")
		    public String custOrdrNum;//�ͻ�������
			@JsonProperty("���屸ע")
			public String memos;//��ע
			@JsonProperty("��ͷ��ע")
			public String memo;//��ע

		    //������ѯ�ͻ����ơ��û����ơ��������ơ������������ơ��շ�������ơ�ҵ����������
			@JsonProperty("�ͻ�����")
		    public String custNm;//�ͻ�����
			@JsonProperty("������������")
		    public String sellTypNm;//������������
			@JsonProperty("�շ��������")
		    public String recvSendCateNm;//�շ��������
			@JsonProperty("ҵ����������")
		    public String bizTypNm;//ҵ����������
			@JsonProperty("������������")
		    public String formTypName;//������������
			@JsonProperty("�Ƿ�����ƾ֤")
		    public Integer isNtMakeVouch;//�Ƿ�����ƾ֤
			@JsonProperty("��ƾ֤��")
		    public String makVouchPers;//��ƾ֤��
			@JsonProperty("��ƾ֤ʱ��")
		    public String makVouchTm;//��ƾ֤ʱ��
			@JsonProperty("���")
		    public Long ordrNum;//���
			@JsonProperty("�ֿ����")
		    public String whsEncd;//�ֿ����
			@JsonProperty("�������")
		    public String invtyEncd;//�������
			@JsonProperty("����")
		    public BigDecimal qty;//����
			@JsonProperty("����")
		    public BigDecimal quotn;//����
			@JsonProperty("��˰����")
		    public BigDecimal noTaxUprc;//��˰����
			@JsonProperty("��˰���")
		    public BigDecimal noTaxAmt;//��˰���
			@JsonProperty("˰��")
		    public BigDecimal taxRate;//˰��
			@JsonProperty("˰��")
		    public BigDecimal taxAmt;//˰��
			@JsonProperty("��˰����")
		    public BigDecimal cntnTaxUprc;//��˰����
			@JsonProperty("��˰�ϼ�")
		    public BigDecimal prcTaxSum;//��˰�ϼ�
			@JsonProperty("δ��Ʊ�˻�����")
		    public BigDecimal unBllgRtnGoodsQty;//��������
			@JsonProperty("��Ʊ����")
		    public BigDecimal bllgQty;//��Ʊ����
			@JsonProperty("�˻���ʶ")
		    public Integer rtnGoodsInd;//�˻���ʶ
			@JsonProperty("���㵥��")
		    public BigDecimal stlUprc;//���㵥��
			@JsonProperty("��������")
		    public BigDecimal stlQty;//��������
			@JsonProperty("������")
		    public BigDecimal stlAmt;//������
			@JsonProperty("�ۼƽ�����")
		    public BigDecimal accmStlAmt;//�ۼƽ�����
			@JsonProperty("����������")
		    public BigDecimal finalStlQty;//����������
			@JsonProperty("��������")
		    public BigDecimal finalStlAmt;//��������
			@JsonProperty("�ۼƳ�������")
		    public BigDecimal accmOutWhsQty;//�ۼƳ�������
			@JsonProperty("��������")
		    public String prdcDt;//��������
			@JsonProperty("������")
		    public Integer baoZhiQi;//������
			@JsonProperty("ʧЧ����")
		    public String invldtnDt;//ʧЧ����
			@JsonProperty("����")
		    public String batNum;//����
			@JsonProperty("��������")
		    public String intlBat;//��������
			@JsonProperty("�Ƿ���Ʒ")
		    public Integer isComplimentary;//�Ƿ���Ʒ
			@JsonProperty("�Ƿ��˻�(�ӱ�)")
		    public Integer isNtRtnGoods;//�Ƿ��˻�
			@JsonProperty("����")
		    public BigDecimal bxQty;//����
			@JsonProperty("��Ŀ���")
		    public String projEncd;//��Ŀ���
			@JsonProperty("��Ŀ����")
		    public String projNm;//��Ŀ����
			@JsonProperty("�ۿ۱���")
		    public String discntRatio;//�ۿ۱���
			@JsonProperty("�������")
		    //�����������ֶΡ�������λ���ơ��ֿ�����
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
			@JsonProperty("Ӧ˰����")
		    public Integer shdTaxLabour;//Ӧ˰����
			@JsonProperty("��Դ�ӱ����")
		    public Long toOrdrNum;//��Դ�ӱ����
			@JsonProperty("δ��Ʊ����")
		    public BigDecimal unBllgQty;//δ��Ʊ����
			@JsonProperty("�Ƿ��ۿ�")
		    public Integer isNtDiscnt;//�Ƿ��ۿ�
		}
}
