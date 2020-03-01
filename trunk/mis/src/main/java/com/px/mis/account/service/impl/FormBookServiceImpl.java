package com.px.mis.account.service.impl;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.BigDecimalConverter;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.px.mis.account.dao.AcctItmDocDao;
import com.px.mis.account.dao.FormBackFlushDao;
import com.px.mis.account.dao.FormBackFlushSubDao;
import com.px.mis.account.dao.FormBookEntryDao;
import com.px.mis.account.dao.FormBookEntrySubDao;
import com.px.mis.account.dao.InvtyDetailDao;
import com.px.mis.account.dao.InvtyMthTermBgnTabDao;
import com.px.mis.account.dao.InvtySendMthTermBgnTabDao;
import com.px.mis.account.dao.MthEndStlDao;
import com.px.mis.account.dao.MthTermBgnTabDao;
import com.px.mis.account.dao.OutIntoWhsAdjSnglDao;
import com.px.mis.account.dao.OutIntoWhsAdjSnglSubTabDao;
import com.px.mis.account.dao.PursInvMasTabDao;
import com.px.mis.account.dao.SellComnInvDao;
import com.px.mis.account.dao.SellInvMasTabDao;
import com.px.mis.account.dao.TermBgnBalDao;
import com.px.mis.account.entity.AcctItmDoc;
import com.px.mis.account.entity.FormBackFlush;
import com.px.mis.account.entity.FormBackFlushSub;
import com.px.mis.account.entity.FormBookEntry;
import com.px.mis.account.entity.FormBookEntrySub;
import com.px.mis.account.entity.InvtyDetail;
import com.px.mis.account.entity.InvtyDetails;
import com.px.mis.account.entity.InvtyMthTermBgnTab;
import com.px.mis.account.entity.MthEndStl;
import com.px.mis.account.entity.OutIntoWhsAdjSngl;
import com.px.mis.account.entity.OutIntoWhsAdjSnglSubTab;
import com.px.mis.account.entity.PursInvSubTab;
import com.px.mis.account.entity.SellComnInvSub;
import com.px.mis.account.entity.SubjectCredit;
import com.px.mis.account.entity.SubjectDebit;
import com.px.mis.account.entity.TermBgnBal;
import com.px.mis.account.service.FormBookService;
import com.px.mis.account.thread.Task;
import com.px.mis.account.thread.TaskDistributor;
import com.px.mis.account.thread.WorkThread;
import com.px.mis.purc.dao.IntoWhsDao;
import com.px.mis.purc.dao.InvtyClsDao;
import com.px.mis.purc.dao.InvtyDocDao;
import com.px.mis.purc.dao.SellOutWhsDao;
import com.px.mis.purc.dao.SellSnglDao;
import com.px.mis.purc.entity.IntoWhs;
import com.px.mis.purc.entity.IntoWhsSub;
import com.px.mis.purc.entity.InvtyDoc;
import com.px.mis.purc.entity.SellOutWhs;
import com.px.mis.purc.entity.SellOutWhsSub;
import com.px.mis.util.BaseJson;
import com.px.mis.util.CommonUtil;
import com.px.mis.util.GetOrderNo;
import com.px.mis.whs.dao.OthOutIntoWhsMapper;
import com.px.mis.whs.entity.OthOutIntoWhs;
import com.px.mis.whs.entity.OthOutIntoWhsSubTab;
import com.suning.api.entity.retailer.CmmdtydistributorGetResponse.RespBean;

/**
 * ���ݼ��˷����ʵ����
 */
@Service
public class FormBookServiceImpl implements FormBookService {

	private Logger logger = LoggerFactory.getLogger(FormBookServiceImpl.class);

	@Autowired
	private IntoWhsDao intoWhsDao; // �ɹ���ⵥ
	@Autowired
	private OthOutIntoWhsMapper othOutDao; // ��������ⵥ
	@Autowired
	private SellOutWhsDao sellOutWhsDao; // ���۳��ⵥ
	@Autowired
	private AcctItmDocDao accItemDao; // ��ƿ�Ŀ
	@Autowired
	private MthTermBgnTabDao mthTermBgnTabDao; // �ڳ����
	@Autowired
	private TermBgnBalDao termBgnBalDao; // �ڳ�
	@Autowired
	private PursInvMasTabDao pursInvMasTabDao; // �ɹ���Ʊ
	@Autowired
	private SellInvMasTabDao sellInvMasTabDao; // ���۷�Ʊ
	@Autowired
	private InvtyDetailDao invtyDetailDao; // �����ϸ��
	@Autowired
	private InvtyMthTermBgnTabDao invtyMthTermBgnTabDao; // ��������ڳ�
	@Autowired
	private GetOrderNo getOrderNo; // ���ɵ���
	@Autowired
	private OutIntoWhsAdjSnglDao outIntoWhsAdjSnglDao; // �����������
	@Autowired
	private OutIntoWhsAdjSnglSubTabDao outIntoWhsAdjSnglSubTabDao; // ��������ӵ�
	@Autowired
	private FormBookEntryDao formBookEntryDao; // ��������
	@Autowired
	private FormBookEntrySubDao formBookEntrySubDao; // �����ӱ�
	@Autowired
	private MthEndStlDao mthEndStlDao; // ��ĩ����
	@Autowired
	private InvtyDocDao invtyDocDao; // �������
	@Autowired
	private InvtySendMthTermBgnTabDao invtySendMthTermBgnTabDao; // ������Ʒ�����ڳ�
	@Autowired
	private SellSnglDao sellSnglDao; // ���۵�
	@Autowired
	private SellComnInvDao sellComnInvDao; // ��Ʊ
	@Autowired
	private FormBackFlushDao formBackFlushDao; // �س嵥
	@Autowired
	private FormBackFlushSubDao formBackFlushSubDao; // �س嵥�ӱ�
	@Autowired
	private InvtyClsDao invtyClsDao; // �������
	@Autowired
	private FormBookEntryUtil formBookEntryUtil;

	@Override
	@Transactional
	public String selectNormalInvtyAccount(Map map) throws Exception {
		// ���ݼ��˲�ѯ����
		// �ֿ���� �������ͱ�����, ���������, ��ǰ��¼����
		String message = "";
		String resp = "";
		boolean isSuccess = true;
		int count = 0;
		int pageNo = Integer.parseInt(map.get("pageNo").toString());
		int pageSize = Integer.parseInt(map.get("pageSize").toString());

		String whsCode = (String) map.get("whsEncd");
		String formCodes = (String) map.get("formType");
		String invIds = (String) map.get("invtyEncds");

		String loginTime = (String) map.get("loginTime");

		List<String> invIdList = CommonUtil.strToList(invIds);
		List<String> formCodeList = CommonUtil.strToList(formCodes);
		List<FormBookEntry> dataList = new ArrayList<>();
		List<FormBookEntrySub> subList = new ArrayList<>();

		map.put("isNtBookOk", "0"); // �Ƿ����
		map.put("invIdList", invIdList);
		map.put("formCodeList", formCodeList);
		map.put("isPage", "1");
		if (StringUtils.isEmpty(whsCode) && StringUtils.isEmpty(formCodes) && StringUtils.isEmpty(invIds)
				&& StringUtils.isEmpty(loginTime)) {

			message = "��ѡ������һ����ѯ����";
			isSuccess = false;
		} else {

			dataList = formBookEntryDao.selectStreamALLList(map);
			count = formBookEntryDao.selectStreamALLCount(map);

			/*
			 * try {
			 * 
			 * dataList = selectFormBook(map,pageSize);
			 * 
			 * count += intoWhsDao.countSelectIntoWhsStream(map);
			 * logger.info("��ѯ�������˵����б��ɹ���ⵥ{}��",count); count +=
			 * sellOutWhsDao.countSellOutWhsIsInvty(map); count +=
			 * othOutDao.countSelectOthOutIntoWhsStream(map);
			 * 
			 * } catch (ParseException e) {
			 * 
			 * e.printStackTrace(); }
			 */

		}

		resp = BaseJson.returnRespList("account/form/normal/list", isSuccess, message, count, pageNo, pageSize, 0, 0,
				dataList);

		return resp;

	}

	/**
	 * selectδ���˵ĵ����б�-����ҳ
	 * 
	 * @throws Exception
	 * 
	 */
	private List<FormBookEntry> selectFormBook(Map map, int pageSize) throws Exception {

		List<FormBookEntry> dataList = new ArrayList<>();

		// �ɹ����
		dataList = selectIntoWhsAndPage(map, dataList);
		if (dataList.size() < pageSize) {
			pageSize = pageSize - dataList.size();
			map.put("num", pageSize);
			// ���۳���
			dataList = selectSellOutWhsAndPage(map, dataList);
			if (dataList.size() < pageSize) {
				pageSize = pageSize - dataList.size();
				map.put("num", pageSize);
				// ������� ����,��װ,�̵�,��ж,����
				dataList = selectOthOutWhsAndPage(map, dataList);
			}
		}

		return dataList;

	}

	/**
	 * select ��������ⵥ����ҳ
	 */
	private List<FormBookEntry> selectOthOutWhsAndPage(Map map, List<FormBookEntry> dataList) {

		// ��������ⵥ
		List<OthOutIntoWhs> othOutIntoWhsList = othOutDao.selectOthOutIntoWhsStream(map);

		if (othOutIntoWhsList.size() > 0) {

			for (OthOutIntoWhs into : othOutIntoWhsList) {

				FormBookEntry form = new FormBookEntry(into.getFormNum(), into.getFormDt(), into.getIsNtChk(),
						into.getChkr(), into.getChkTm(), into.getOutIntoWhsTypId(), into.getOutIntoWhsTypNm(), "", "",
						"", "", "", "", "", "", "", "", "", "", into.getIsNtBookEntry(), 0, into.getMemo(), null, null,
						0, into.getSrcFormNum(), into.getSetupPers(), into.getSetupTm(), into.getFormTypEncd(),
						into.getFormTypName(), "", "", "", "");
				List<FormBookEntrySub> subList = new ArrayList<>();
				if (into.getOutIntoSubList().size() > 0) {

					for (OthOutIntoWhsSubTab sub : into.getOutIntoSubList()) {

						FormBookEntrySub forms = new FormBookEntrySub(sub.getFormNum(), null, sub.getInvtyEncd(),
								sub.getInvtyNm(), sub.getInvtyCd(), sub.getSpcModel(), sub.getMeasrCorpId(),
								sub.getMeasrCorpNm(), sub.getBxRule(), sub.getInvtyClsEncd(), sub.getInvtyClsNm(),
								into.getWhsEncd(), sub.getWhsNm(), sub.getUnTaxUprc(), sub.getUnTaxAmt(),
								new BigDecimal(0.00), sub.getCntnTaxUprc(), new BigDecimal(0.00), sub.getTaxRate(),
								sub.getQty(), new BigDecimal(0), sub.getBatNum(), sub.getIntlBat(), sub.getPrdcDt(),
								sub.getInvldtnDt(), sub.getBaoZhiQiDt(), new BigDecimal(0.00), new BigDecimal(0.00),
								new BigDecimal(0.00), sub.getOrdrNum().intValue(), sub.getProjEncd(), sub.getProjNm());

						subList.add(forms);
					}
				}
				form.setBookEntrySub(subList);
				dataList.add(form);

			}

		}

		return dataList;
	}

	/**
	 * select ���۳��ⵥ����ҳ
	 */
	private List<FormBookEntry> selectSellOutWhsAndPage(Map map, List<FormBookEntry> dataList) {

		List<SellOutWhs> sellOutList = sellOutWhsDao.selectSellOutWhsStream(map);

		if (sellOutList.size() > 0) {

			for (SellOutWhs into : sellOutList) {

				FormBookEntry form = new FormBookEntry(into.getOutWhsId(), into.getOutWhsDt(), into.getIsNtChk(),
						into.getChkr(), into.getChkTm(), into.getOutIntoWhsTypId(), into.getOutIntoWhsTypNm(),
						into.getBizTypId(), into.getBizTypNm(), into.getSellTypId(), into.getSellTypNm(), "", "", "",
						"", into.getCustId(), into.getCustNm(), into.getAccNum(), into.getUserName(),
						into.getIsNtBookEntry(), 0, into.getMemo(), into.getIsNtBllg(), into.getIsNtStl(), 0,
						into.getSellOrdrInd(), into.getSetupPers(), into.getSetupTm(), into.getFormTypEncd(),
						into.getFormTypName(), into.getRecvSendCateId(), into.getRecvSendCateNm(), into.getDeptId(),
						into.getDeptName());

				List<FormBookEntrySub> subList = new ArrayList<>();
				if (into.getSellOutWhsSub().size() > 0) {

					for (SellOutWhsSub sub : into.getSellOutWhsSub()) {

						FormBookEntrySub forms = new FormBookEntrySub(sub.getOutWhsId(), sub.getToOrdrNum(),
								sub.getInvtyEncd(), sub.getInvtyNm(), sub.getInvtyCd(), sub.getSpcModel(),
								sub.getMeasrCorpId(), sub.getMeasrCorpNm(), sub.getBxRule(), sub.getInvtyClsEncd(),
								sub.getInvtyClsNm(), sub.getWhsEncd(), sub.getWhsNm(), sub.getNoTaxUprc(),
								sub.getNoTaxAmt(), sub.getTaxAmt(), sub.getCntnTaxUprc(), sub.getPrcTaxSum(),
								sub.getTaxRate(), sub.getQty(), sub.getBxQty(), sub.getBatNum(), sub.getIntlBat(),
								sub.getPrdcDt(), sub.getInvldtnDt(), sub.getBaoZhiQi().toString(), new BigDecimal(0.00),
								new BigDecimal(0.00), new BigDecimal(0.00), sub.getOrdrNum().intValue(),
								sub.getProjEncd(), sub.getProjNm());
						subList.add(forms);
					}
				}
				form.setBookEntrySub(subList);
				dataList.add(form);

			}

		}
		return dataList;

	}

	/**
	 * select �ɹ���ⵥ����ҳ
	 */
	private List<FormBookEntry> selectIntoWhsAndPage(Map map, List<FormBookEntry> dataList) {

		List<IntoWhs> intoWhsList = intoWhsDao.selectIntoWhsStream(map);

		if (intoWhsList.size() > 0) {

			for (IntoWhs into : intoWhsList) {

				FormBookEntry form = new FormBookEntry(into.getIntoWhsSnglId(), into.getIntoWhsDt(), into.getIsNtChk(),
						into.getChkr(), into.getChkTm(), into.getOutIntoWhsTypId(), into.getOutIntoWhsTypNm(), "", "",
						"", "", into.getPursTypId(), into.getPursTypNm(), into.getProvrId(), into.getProvrNm(), "", "",
						into.getAccNum(), into.getUserName(), into.getIsNtBookEntry(), 0, into.getMemo(),
						into.getIsNtBllg(), into.getIsNtStl(), 0, into.getPursOrdrId(), into.getSetupPers(),
						into.getSetupTm(), into.getFormTypEncd(), into.getFormTypName(), into.getRecvSendCateId(),
						into.getRecvSendCateNm(), into.getDeptId(), into.getDeptName());

				List<FormBookEntrySub> subList = new ArrayList<>();
				if (into.getIntoWhsSub().size() > 0) {

					for (IntoWhsSub sub : into.getIntoWhsSub()) {

						FormBookEntrySub forms = new FormBookEntrySub(sub.getIntoWhsSnglId(), sub.getToOrdrNum(),
								sub.getInvtyEncd(), sub.getInvtyNm(), sub.getInvtyCd(), sub.getSpcModel(),
								sub.getMeasrCorpId(), sub.getMeasrCorpNm(), sub.getBxRule(), sub.getInvtyClsEncd(),
								sub.getInvtyClsNm(), sub.getWhsEncd(), sub.getWhsNm(), sub.getNoTaxUprc(),
								sub.getNoTaxAmt(), sub.getTaxAmt(), sub.getCntnTaxUprc(), sub.getPrcTaxSum(),
								sub.getTaxRate(), sub.getQty(), sub.getBxQty(), sub.getBatNum(), sub.getIntlBat(),
								sub.getPrdcDt(), sub.getInvldtnDt(), sub.getBaoZhiQi(), sub.getUnBllgQty(),
								sub.getUnBllgUprc(), sub.getUnBllgAmt(), sub.getOrdrNum().intValue(), sub.getProjEncd(),
								sub.getProjNm());

						subList.add(forms);

					}
				}
				form.setBookEntrySub(subList);
				dataList.add(form);
			}

		}
		return dataList;

	}

	@Override
	public Object selectDoument(Map map) {
		String formType = (String) map.get("outIntoWhsTypId");
		String formCode = (String) map.get("formNum");
		Object data = new Object();
		if (StringUtils.isEmpty(formType) || StringUtils.isEmpty(formCode)) {
			return data;
		} else {
			if (Integer.valueOf(formType) == 9) {
				// �ɹ���ⵥ
				return intoWhsDao.selectIntoWhsByIntoWhsSnglId(formCode);

			} else if (Integer.valueOf(formType) == 10) {
				// ���۳��ⵥ
				return sellOutWhsDao.selectSellOutWhsByOutWhsId(formCode);
			} else {
				// ���������
				return othOutDao.selectOthOutIntoWhs(formCode);
			}

		}
	}

	@Override
	@Transactional
	public String formBook(List<FormBookEntry> dataList, String userName, String loginTime) throws Exception {
		Map<String, Object> parmMap = new HashMap<>();

		String resp = "";
		String message = "";
		Boolean isSuccess = true;
		int count = 0;
		isSuccess = checkOutIsEndOfMonth(loginTime);
		
		if (!isSuccess) {
			message = "��ǰ��¼���ڣ�" + loginTime + "���ڽ�������";
			isSuccess = false;
			resp = BaseJson.returnRespObj("account/form/back/book", isSuccess, message, null);
			return resp;
		}
		Long beginTime = new Date().getTime(); // �ӿڿ�ʼʱ��
		// ���ݼ��˹���ҵ��
		if (dataList.size() != 0) {
			parmMap.put("formNumList", dataList);
			parmMap.put("isPage", "");
			parmMap.put("isNtBookOk", "0");
			dataList = formBookEntryDao.selectStreamALLList(parmMap);
			if (dataList.size() > 0) {
				// count = formBookEntryUtil.formBookEntryByStored(dataList, userName,
				// loginTime, count);
				count = formBookEntryUtil.formBookEntryByOld(dataList, userName, loginTime, count);

				Long opetime = new Date().getTime() - beginTime;
				long hour = opetime / (60 * 60 * 1000);
				long minute = (opetime - hour * 60 * 60 * 1000) / (60 * 1000);
				long second = (opetime - hour * 60 * 60 * 1000 - minute * 60 * 1000) / 1000;
				isSuccess = true;
				message = "���ݼ��˽ӿڵ�����ɣ����ι��ƣ�" + count + "�����ݣ���ʱ��" + hour + "ʱ" + minute + "�� " + second + "��";
			} else {
				message = "����������ݷ����쳣";
				isSuccess = false;
			}
		} else {
			message = "����������ݷ����쳣";
			isSuccess = false;

		}

		resp = BaseJson.returnRespObj("account/form/normal/book", isSuccess, message, null);
		return resp;
	}

	private Map<String, Object> returnCost(String userName, String loginTime, List<FormBookEntry> formList,
			List<IntoWhs> intoWhsList, List<SellOutWhs> sellOutWhsList, List<OthOutIntoWhs> othOutList) {
		Map<String, Object> map = new HashMap<>();
		List<SellOutWhsSub> sellSubList = new ArrayList<>();

		if (formList.size() > 0) {
			for (FormBookEntry form : formList) {

				if (StringUtils.isNotEmpty(form.getOutIntoWhsTypId())) {

					form.setBookOkAcc(userName);
					form.setBookOkDt(loginTime);
					form.setIsNtBookOk(1);
					// �ɹ���
					if (Integer.valueOf(form.getOutIntoWhsTypId()) == 9) {

						IntoWhs into = new IntoWhs();
						into.setIntoWhsSnglId(form.getFormNum());
						into.setIsNtBookEntry(1);
						into.setBookEntryPers(userName);

						into.setBookEntryTm(loginTime);
						intoWhsList.add(into);

					} else if (Integer.valueOf(form.getOutIntoWhsTypId()) == 10) {
						// ����
						// �������� =2 ȡί�д��� | =1 ��ͨ����
						SellOutWhs sellOuths = new SellOutWhs();
						sellOuths.setOutWhsId(form.getFormNum());
						sellOuths.setIsNtBookEntry(1);

						sellOuths.setBookEntryPers(userName);
						sellOuths.setBookEntryTm(loginTime);
						sellOutWhsList.add(sellOuths);

						for (FormBookEntrySub sub : form.getBookEntrySub()) {
							SellOutWhsSub sell = new SellOutWhsSub();
							sell.setCarrOvrAmt(sub.getNoTaxAmt());
							sell.setCarrOvrUprc(sub.getNoTaxUprc());
							sell.setOutWhsId(sub.getFormNum());
							sell.setOrdrNum(Long.valueOf(sub.getOrdrNum()));
							sellSubList.add(sell);
						}

					} else {
						// ���������
						OthOutIntoWhs othOut = new OthOutIntoWhs();
						othOut.setFormNum(form.getFormNum());
						othOut.setIsNtBookEntry(1);
						othOut.setBookEntryPers(userName);
						othOut.setBookEntryTm(loginTime);
						othOutList.add(othOut);

					}

				}

			}

		}

		map.put("sellSubList", sellSubList);
		map.put("formList", formList);
		map.put("intoWhsList", intoWhsList); // �ɹ���⼯��
		map.put("sellOutWhsList", sellOutWhsList); // ���۳��⼯��
		map.put("othOutList", othOutList); // ��������⼯��

		return map;
	}

	@Override
	@Transactional
	public String backFormBook(String formNum, String userName, String loginTime) throws IOException {
		// ������ƾ֤�޷��ָ�����
		// �޸Ĳɹ�/����/���������Ϊδ����
		String resp = "";
		String message = "";
		Boolean isSuccess = true;
		Long beginTime = new Date().getTime(); // �ӿڿ�ʼʱ��
		int count = 0;

		List<String> formNumList = CommonUtil.strToList(formNum);
		isSuccess = checkOutIsEndOfMonth(loginTime);
		if (!isSuccess) {
			message = "��ǰ��¼���ڣ�" + loginTime + "���ڽ�������";
			isSuccess = false;
			resp = BaseJson.returnRespObj("account/form/back/book", isSuccess, message, null);
			return resp;
		}
		if (formNumList.size() != 0) {

			Map<String, Object> map = new HashMap<String, Object>();

			map.put("formNumList", formNumList);
			map.put("isNtMakeVouch", "0");
			map.put("isMthEndStl", "0");
			map.put("nowTime", loginTime);
			List<FormBookEntry> formList = formBookEntryDao.selectMap(map); // �Ƿ�Ϊδ����ƾ֤
			if (formList.size() == 0) {
				message = "����������ƾ֤,�޷��ָ�����";
				isSuccess = false;
			} else {

				List<InvtyDetails> invtyDetailsList = new ArrayList<>(); // ��ϸ���ӱ���
				List<FormBookEntrySub> subList = new ArrayList<>(); // ��ϸ�˼���
				List<FormBookEntry> selList = new ArrayList<>();
				List<FormBookEntry> intList = new ArrayList<>();
				List<FormBookEntry> outList = new ArrayList<>();
				List<FormBookEntrySub> selsList = new ArrayList<>();

				for (FormBookEntry form : formList) {
					// ���ݵ��ݲ�ѯ��ϸ��
					// ���ݵ��ݲ�ѯ��ϸ���
					for (FormBookEntrySub sub : form.getBookEntrySub()) {
						count++;
						int year = Integer.valueOf(form.getBookOkDt().substring(0, 4));
						int month = Integer.valueOf(form.getBookOkDt().substring(5, 7));

						List<InvtyDetail> invtyList = getInvtyDetailList(sub, form.getBookOkDt());
						if (invtyList.size() > 0) {

							for (InvtyDetail it : invtyList) {
								if (it.getInvtyDetailsList().size() > 0) {

									for (InvtyDetails its : it.getInvtyDetailsList()) {
										invtyDetailsList.add(its);

									}
								}
							}

						}

						// �޸ĸ����ڳ��� Ϊ0ʱɾ��
						List<InvtyMthTermBgnTab> mthNowList = getMthList(sub, Integer.valueOf(year),
								Integer.valueOf(month));
						List<InvtyMthTermBgnTab> mthSendList = getSendMthList(sub, Integer.valueOf(year),
								Integer.valueOf(month), form.getCustId());
						if (mthSendList.size() > 0) {

							InvtyMthTermBgnTab mthSendNow = mthSendList.get(0);
							mthSendNow = backMthSend(mthSendNow, form, sub);

							// ���
							mthSendNow.setOthQty(
									mthSendNow.getQty().add(mthSendNow.getInQty()).subtract(mthSendNow.getSendQty()));
							mthSendNow.setOthMoeny(mthSendNow.getAmt().add(mthSendNow.getInMoeny())
									.subtract(mthSendNow.getSendMoeny()));
							if (mthSendNow.getOthQty().compareTo(BigDecimal.ZERO) == 0) {
								mthSendNow.setOthUnitPrice(new BigDecimal(0.00000000));
							} else {
								mthSendNow.setOthUnitPrice(mthSendNow.getOthMoeny().divide(mthSendNow.getOthQty(), 8,
										BigDecimal.ROUND_HALF_UP));
							}

							if (mthSendNow.getQty().compareTo(BigDecimal.ZERO) == 0
									&& mthSendNow.getAmt().compareTo(BigDecimal.ZERO) == 0
									&& mthSendNow.getInQty().compareTo(BigDecimal.ZERO) == 0
									&& mthSendNow.getInMoeny().compareTo(BigDecimal.ZERO) == 0
									&& mthSendNow.getSendQty().compareTo(BigDecimal.ZERO) == 0
									&& mthSendNow.getSendMoeny().compareTo(BigDecimal.ZERO) == 0
									&& mthSendNow.getOthQty().compareTo(BigDecimal.ZERO) == 0
									&& mthSendNow.getOthMoeny().compareTo(BigDecimal.ZERO) == 0) {

								invtySendMthTermBgnTabDao.deleteByordrNum(mthSendNow.getOrdrNum());

							} else {
								int s = invtySendMthTermBgnTabDao.updateSendMth(mthSendNow);
							}
						}
						if (mthNowList.size() == 0) {
							message = "�ڳ����Ϊ��";
						} else {
							// ��
							InvtyMthTermBgnTab mthNow = mthNowList.get(0); // �޸ı������뷢�����

							mthNow.setInQty(Optional.ofNullable(mthNow.getInQty()).orElse(new BigDecimal(0.00000000)));
							mthNow.setInMoeny(
									Optional.ofNullable(mthNow.getInMoeny()).orElse(new BigDecimal(0.00000000)));
							mthNow.setSendQty(
									Optional.ofNullable(mthNow.getSendQty()).orElse(new BigDecimal(0.00000000)));
							mthNow.setSendMoeny(
									Optional.ofNullable(mthNow.getSendMoeny()).orElse(new BigDecimal(0.00000000)));
							mthNow.setOthQty(
									Optional.ofNullable(mthNow.getOthQty()).orElse(new BigDecimal(0.00000000)));
							mthNow.setOthMoeny(
									Optional.ofNullable(mthNow.getOthMoeny()).orElse(new BigDecimal(0.00000000)));

							if (CommonUtil.strToList("1,3,5,8,9,12").contains(form.getOutIntoWhsTypId())) {
								// ����
								mthNow.setInQty(mthNow.getInQty().subtract(sub.getQty()));
								mthNow.setInMoeny(mthNow.getInMoeny().subtract(sub.getNoTaxAmt())); // �������

							}
							if (mthNow.getInQty().compareTo(BigDecimal.ZERO) == 0) {
								mthNow.setInUnitPrice(new BigDecimal(0.00000000));
							} else {
								mthNow.setInUnitPrice(
										mthNow.getInMoeny().divide(mthNow.getInQty(), 8, BigDecimal.ROUND_HALF_UP));
							}

							// ��
							if (CommonUtil.strToList("2,4,6,7,10,11").contains(form.getOutIntoWhsTypId())) {
								// ����
								mthNow.setSendQty(mthNow.getSendQty().subtract(sub.getQty()));
								mthNow.setSendMoeny(mthNow.getSendMoeny().subtract(sub.getNoTaxAmt())); // �������

							}
							if (mthNow.getSendQty().compareTo(BigDecimal.ZERO) == 0) {
								mthNow.setSendUnitPrice(new BigDecimal(0.00000000));
							} else {
								mthNow.setSendUnitPrice(
										mthNow.getSendMoeny().divide(mthNow.getSendQty(), 8, BigDecimal.ROUND_HALF_UP));
							}
							// ���
							mthNow.setOthQty(mthNow.getQty().add(mthNow.getInQty()).subtract(mthNow.getSendQty()));
							mthNow.setOthMoeny(
									mthNow.getAmt().add(mthNow.getInMoeny()).subtract(mthNow.getSendMoeny()));
							if (mthNow.getOthQty().compareTo(BigDecimal.ZERO) == 0) {
								mthNow.setOthUnitPrice(new BigDecimal(0.00000000));
							} else {
								mthNow.setOthUnitPrice(
										mthNow.getOthMoeny().divide(mthNow.getOthQty(), 8, BigDecimal.ROUND_HALF_UP));
							}

							if (mthNow.getQty().compareTo(BigDecimal.ZERO) == 0
									&& mthNow.getAmt().compareTo(BigDecimal.ZERO) == 0
									&& mthNow.getInQty().compareTo(BigDecimal.ZERO) == 0
									&& mthNow.getInMoeny().compareTo(BigDecimal.ZERO) == 0
									&& mthNow.getSendQty().compareTo(BigDecimal.ZERO) == 0
									&& mthNow.getSendMoeny().compareTo(BigDecimal.ZERO) == 0
									&& mthNow.getOthQty().compareTo(BigDecimal.ZERO) == 0
									&& mthNow.getOthMoeny().compareTo(BigDecimal.ZERO) == 0) {

								invtyMthTermBgnTabDao.deleteByordrNum(mthNow.getOrdrNum());

							} else {
								int n = invtyMthTermBgnTabDao.updateMth(mthNow);
							}

							logger.info("�޸�{}��{}��{}����ڳ����", year, month, sub.getInvtyEncd());

						}

						subList.add(sub);
					}
					form.setIsNtBookOk(0);
					form.setBookOkDt("0000-00-00 00:00:00");
					form.setBookOkAcc(" ");
					// �ɹ���
					if (Integer.valueOf(form.getOutIntoWhsTypId()) == 9) {
						intList.add(form);
					} else if (Integer.valueOf(form.getOutIntoWhsTypId()) == 10) {
						selList.add(form);

					} else {
						outList.add(form);
					}
				}
				// ɾ����ϸ��
				if (invtyDetailsList.size() > 0) {
					int ids = invtyDetailDao.delectInvtyList(invtyDetailsList);
					logger.info("����ɾ����ϸ�ӱ�,��Ӱ�����{}", ids);
				}
				if (selList.size() > 0) {
					formBookEntryDao.updateSellOutWhsBookEntryList(selList);
				}
				if (intList.size() > 0) {
					formBookEntryDao.updateIntoWhsBookEntryList(intList);
				}
				if (outList.size() > 0) {
					formBookEntryDao.updateOutInWhsBookEntry(outList);
				}

				int f = formBookEntryDao.delectFormBookList(formList);
				logger.info("ɾ����������,��Ӱ�����{}", f);

				Long opetime = new Date().getTime() - beginTime;
				long hour = opetime / (60 * 60 * 1000);
				long minute = (opetime - hour * 60 * 60 * 1000) / (60 * 1000);
				long second = (opetime - hour * 60 * 60 * 1000 - minute * 60 * 1000) / 1000;

				message = "���ݼ��˽ӿڵ�����ɣ����ι��ƣ�" + count + "�����ݣ���ʱ��" + hour + "ʱ" + minute + "�� " + second + "��";
				isSuccess = true;
			}
		} else {
			message = "�ָ�" + loginTime.substring(0, 4) + "��-" + loginTime.substring(5, 7) + "�µĵ��ݷ�������";
			isSuccess = false;
		}

		resp = BaseJson.returnRespObj("account/form/back/book ", isSuccess, message, null);
		return resp;
	}

	/**
	 * �ָ�������Ʒ�����ڳ�
	 */
	private InvtyMthTermBgnTab backMthSend(InvtyMthTermBgnTab mthSendNow, FormBookEntry form, FormBookEntrySub sub) {

		mthSendNow.setInQty(Optional.ofNullable(mthSendNow.getInQty()).orElse(new BigDecimal(0.00000000)));
		mthSendNow.setInMoeny(Optional.ofNullable(mthSendNow.getInMoeny()).orElse(new BigDecimal(0.00000000)));
		mthSendNow.setSendQty(Optional.ofNullable(mthSendNow.getSendQty()).orElse(new BigDecimal(0.00000000)));
		mthSendNow.setSendMoeny(Optional.ofNullable(mthSendNow.getSendMoeny()).orElse(new BigDecimal(0.00000000)));
		mthSendNow.setOthQty(Optional.ofNullable(mthSendNow.getOthQty()).orElse(new BigDecimal(0.00000000)));
		mthSendNow.setOthMoeny(Optional.ofNullable(mthSendNow.getOthMoeny()).orElse(new BigDecimal(0.00000000)));
		// ����
		if (10 == Integer.valueOf(form.getOutIntoWhsTypId())) {
			if (form.getIsNtBllg() == 1) {
				mthSendNow.setSendMoeny(mthSendNow.getSendMoeny().subtract(sub.getNoTaxAmt()));
				mthSendNow.setSendQty(mthSendNow.getSendQty().subtract(sub.getQty()));
			}
			if (mthSendNow.getSendQty().compareTo(BigDecimal.ZERO) == 0) {
				mthSendNow.setSendUnitPrice(new BigDecimal(0.00000000));
			} else {
				mthSendNow.setSendUnitPrice(
						mthSendNow.getSendMoeny().divide(mthSendNow.getSendQty(), 8, BigDecimal.ROUND_HALF_UP));
			}
		}

		// ����
		mthSendNow.setInQty(mthSendNow.getInQty().subtract(sub.getQty()));
		mthSendNow.setInMoeny(mthSendNow.getInMoeny().subtract(sub.getNoTaxAmt())); // �������

		if (mthSendNow.getInQty().compareTo(BigDecimal.ZERO) == 0) {
			mthSendNow.setInUnitPrice(new BigDecimal(0.00000000));
		} else {
			mthSendNow
					.setInUnitPrice(mthSendNow.getInMoeny().divide(mthSendNow.getInQty(), 8, BigDecimal.ROUND_HALF_UP));
		}
		return mthSendNow;
	}

	@Override
	public String selectNoFinalDealList(Map map, String loginTime) throws IOException {
		boolean isSuccess = true;
		isSuccess = checkOutIsEndOfMonth(loginTime);
		if (!isSuccess) {
			String message = "��ǰ��¼���ڣ�" + loginTime + "���ڽ�������";
			isSuccess = false;
			return BaseJson.returnRespObj("account/form/final/backDeal", isSuccess, message, null);

		}

		// δ|����ĩ������
		int year = Integer.valueOf(loginTime.substring(0, 4));
		int month = Integer.valueOf(loginTime.substring(5, 7));

		map.put("year", year);
		map.put("month", month);

		List<InvtyMthTermBgnTab> list = invtyMthTermBgnTabDao.selectIsFinalDeal(map);
		return BaseJson.returnRespObjList("account/form/final/noDeal ", true, "��ѯ�ɹ�", null, list);
	}

	@Override
	@Transactional
	public String finalDealForm(Map map, String loginTime, String accNum, String userName) throws Exception {
		String message = "";
		Boolean isSuccess = true;
		String resp;
		int year = Integer.valueOf(loginTime.substring(0, 4));
		int month = Integer.valueOf(loginTime.substring(5, 7));

		map.put("year", year);
		map.put("month", month);
		map.put("isFinalDeal", "0");
		map.put("isMthEndStl", "0");
		map.put("invtyEncdList", CommonUtil.strToList((String) map.get("invtyEncd")));
		List<String> invtyList = CommonUtil.strToList((String) map.get("invtyEncd"));
		map.put("invtyEncd", "");
		Map<String, Object> paramMap = new HashMap<String, Object>();
		isSuccess = checkOutIsEndOfMonth(loginTime);
		if (!isSuccess) {
			message = "��ǰ��¼���ڣ�" + loginTime + "���ڽ�������";
			isSuccess = false;
			resp = BaseJson.returnRespObj("account/form/final/deal", isSuccess, message, null);
			return resp;
		}
		List<InvtyMthTermBgnTab> mths = invtyMthTermBgnTabDao.selectByMthTerm(map);
		Integer isAdj = (Integer) map.get("isCreateAdj");

		if (mths.size() != 0) {

			for (InvtyMthTermBgnTab tab : mths) {
				tab.setUprc(Optional.ofNullable(tab.getUprc()).orElse(new BigDecimal(0.00000000)));
				tab.setOthQty(Optional.ofNullable(tab.getOthQty()).orElse(new BigDecimal(0.00000000)));
				tab.setOthMoeny(Optional.ofNullable(tab.getOthMoeny()).orElse(new BigDecimal(0.00000000)));

				// �жϽ�������Ƿ�Ϊ0,����Ϊ0
				if (isAdj == 1) {
					if (BigDecimal.ZERO.compareTo(tab.getOthQty()) == 0
							&& BigDecimal.ZERO.compareTo(tab.getOthMoeny()) != 0) {
						int im = dealAdj(loginTime, accNum, userName, tab, paramMap);

					} else {
						tab.setIsFinalDeal(1);
						tab.setAccNum(accNum);
						int im = invtyMthTermBgnTabDao.updateMth(tab);
					}
					message = "����ɹ�";
					isSuccess = true;
				} else {
					// ����Ҫ�Զ����ɳ��������
					tab.setIsFinalDeal(1);
					tab.setAccNum(accNum);
					int im = invtyMthTermBgnTabDao.updateMth(tab);
					if (im > 0) {
						message = "����ɹ�";
						isSuccess = true;
					}
				}
				// ������Ϊ������ʱ
				if (tab.getOthUnitPrice() != null) {
					if (new BigDecimal(0).compareTo(tab.getUprc()) == 1) {
						// ���ɳ��������
						message = "������Ϊ������";
						isSuccess = false;
						logger.error("������Ϊ������");
					}
				}

			}
			// ���ݴ�������µĺ��ֻس嵥
			dealFormBackFlush(loginTime, accNum, invtyList);
			// �����µ��ݹ����ݲ����ɺ��ֻس嵥
			getTempIntoWhs(loginTime, accNum, invtyList);

		} else {
			message = "����ʧ��";
			isSuccess = false;
		}

		return BaseJson.returnRespObj("account/form/final/deal ", isSuccess, message, null);
	}

	/**
	 * ���ɵ�����
	 * 
	 * @param loginTime
	 * @param accNum
	 * @param tab
	 * @param paramMap
	 * @return
	 */
	private int dealAdj(String loginTime, String accNum, String userName, InvtyMthTermBgnTab tab,
			Map<String, Object> paramMap) {

		// ���ɳ��������,���Ϊ�Ѽ���,
		// ͬһ����/���/�ֿ�/����

		OutIntoWhsAdjSngl outInto = new OutIntoWhsAdjSngl();

		String orderId = getOrderNo.getSeqNo("OTAD", accNum, loginTime);
		outInto.setFormNum(orderId); // ���ݺ�
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		outInto.setIsFifoAdjBan(0); // �Ƿ��Ƚ��ȳ��������
		outInto.setIsCrspdOutWhsSngl(0);// �Ƿ��Ӧ���ⵥ
		outInto.setOutIntoWhsInd(0); // ����
		outInto.setIsNtBookEntry(1); // �Ƿ����
		outInto.setBookEntryPers(userName);
		outInto.setUserName(userName);
		outInto.setFormTm(loginTime); // ��������
		outInto.setBookEntryTm(loginTime);

		outInto.setSetupPers(accNum);// �Ƶ���
		outInto.setSetupTm(sf.format(new Date()));
		outInto.setFormTypEncd("031");
		outInto.setFormTypName("���������");

		OutIntoWhsAdjSnglSubTab outIntos = new OutIntoWhsAdjSnglSubTab();
		try {
			BeanUtils.copyProperties(outIntos, tab);
			outIntos.setAmt(tab.getOthMoeny());
		} catch (IllegalAccessException | InvocationTargetException e) {

			e.printStackTrace();
		}
		outIntos.setFormNum(outInto.getFormNum());
		outIntos.setWhsEncd(tab.getWhsEncd());
		outIntos.setInvtyEncd(tab.getInvtyEncd());
		outIntos.setInvtyNm(tab.getInvtyNm());

		// �������� ���������031
		int it = outIntoWhsAdjSnglDao.insertOutIntoWhsAdjSngl(outInto);// ��
		int its = outIntoWhsAdjSnglSubTabDao.insertOutIntoWhsAdjSnglSubTab(outIntos);// ��
		// ���ݼ���
		FormBookEntry form = new FormBookEntry();
		form.setFormNum(outInto.getFormNum());
		form.setFormDt(outInto.getFormTm());
		form.setIsNtChk(1);
		form.setChkr(accNum);
		form.setChkTm(outInto.getFormTm());
		form.setIsNtMakeVouch(0); // ����ƾ֤
		form.setIsNtBllg(0); // �Ƿ�Ʊ
		form.setIsNtStl(0);// �Ƿ����
		form.setIsMthEndStl(0); // �Ƿ���ĩ����
		form.setIsNtUploadHead(0);// �Ƿ��ϴ�����
		form.setIsNtBookOk(1);
		form.setBookOkAcc(accNum);
		form.setBookOkDt(loginTime);// ����ʱ��
		form.setOutIntoWhsTypId("13");
		form.setOutIntoWhsTypNm("���������");
		form.setFormTypEncd("031");
		form.setFormTypName("���������");

		FormBookEntrySub formSub = new FormBookEntrySub();
		formSub.setFormNum(outInto.getFormNum());
		try {
			BeanUtils.copyProperties(formSub, outIntos);

		} catch (IllegalAccessException | InvocationTargetException e) {

			e.printStackTrace();
		}

		int ir = formBookEntryDao.insertForm(form);
		int irs = formBookEntrySubDao.insertFormSub(formSub);

		// ��ϸ��
		// paramMap.put("year", tab.getAcctYr());
		// paramMap.put("month", tab.getAcctiMth());
		// paramMap.put("formNum",orderId);
		paramMap.put("invtyEncd", tab.getInvtyEncd());
		paramMap.put("whsEncd", tab.getWhsEncd());
		paramMap.put("batNum", tab.getBatNum());
		List<InvtyDetail> ints = invtyDetailDao.selectByInvty(paramMap);
		int i = 0;
		if (ints.size() != 0) {

			InvtyDetail invty = ints.get(0);
			InvtyDetails invtys = new InvtyDetails(loginTime, null, null, null, "���������", null, null, null,
					tab.getSendUnitPrice(), new BigDecimal(0), tab.getOthMoeny(), tab.getOthUnitPrice(),
					tab.getOthQty(), tab.getOthMoeny().subtract(tab.getOthMoeny()), outInto.getFormNum(),
					invty.getDetailId(), 1);

			i = invtyDetailDao.insertInvtyDetails(invtys);

		}

		// �Ľ��
		tab.setOthMoeny(tab.getOthMoeny().subtract(tab.getOthMoeny()));
		tab.setIsFinalDeal(1);
		tab.setAccNum(accNum);
		int im = invtyMthTermBgnTabDao.updateMth(tab);
		return im;
	}

	/**
	 * ����������
	 */
	private void bookFormAdj() {

	}

	@Override
	@Transactional
	public String finalBackDealForm(Map map, String loginTime, String accNum) throws IOException {
		// �ָ���ĩ����
		int year = Integer.valueOf(loginTime.substring(0, 4));
		int month = Integer.valueOf(loginTime.substring(5, 7));
		String message = "";
		Boolean isSuccess = true;

		map.put("year", year);
		map.put("month", month);
		map.put("isFinalDeal", 1);
		map.put("isMthEndStl", 0);
		if (!isSuccess) {
			message = "��ǰ��¼���ڣ�" + loginTime + "���ڽ�������";
			isSuccess = false;
			return BaseJson.returnRespObj("account/form/final/backDeal", isSuccess, message, null);

		}
		if (StringUtils.isEmpty((String) map.get("invtyEncd"))) {
			message = "��ѡ��Ҫ�ָ��Ĵ������";
			isSuccess = false;
		} else {
			// ���ҳ��������
			map.put("bookOkDt", loginTime);
			List<String> invList = CommonUtil.strToList((String) map.get("invtyEncd"));
			map.put("invtyEncdList", invList);
			map.put("invtyEncd", "");

			// �ָ��س嵥
			recoverBackFlush(loginTime, accNum, invList);

			List<OutIntoWhsAdjSngl> outList = outIntoWhsAdjSnglDao.selectOutIntoWhsAdjSnglFinalDealList(map);
			if (outList.size() == 0) {

				List<InvtyMthTermBgnTab> mths = invtyMthTermBgnTabDao.selectByMthTerm(map);
				if (mths.size() != 0) {
					int m = 0;
					for (InvtyMthTermBgnTab mtht : mths) {
						InvtyMthTermBgnTab mth = mtht;
						mth.setIsFinalDeal(0);
						mth.setAccNum(accNum);
						m = invtyMthTermBgnTabDao.updateMth(mth) + m;
					}
					message = "�ָ��ɹ�";
					isSuccess = true;
					logger.info("������:{},�޸�{}��{}�»ָ���ĩ����ɹ�,��Ӱ�����{}", accNum, year, month, m);
				}

			} else {
				List<FormBookEntry> formList = new ArrayList<>();
				List<String> outFormList = new ArrayList<>();

				for (OutIntoWhsAdjSngl out : outList) {
					outFormList.add(out.getFormNum());
					for (OutIntoWhsAdjSnglSubTab sub : out.getOutIntoList()) {
						// �޸��ڳ����
						map.put("invtyEncd", sub.getInvtyEncd());
						map.put("whsEncd", sub.getWhsEncd());
						map.put("batNum", sub.getBatNum());
						List<InvtyMthTermBgnTab> mths = invtyMthTermBgnTabDao.selectByMthTerm(map);
						if (mths.size() != 0) {
							InvtyMthTermBgnTab mth = mths.get(0);
							mth.setOthMoeny(mth.getOthMoeny().add(sub.getAmt()));
							mth.setIsFinalDeal(0);
							mth.setAccNum(accNum);
							int m = invtyMthTermBgnTabDao.updateMth(mth);
							isSuccess = true;
							logger.info("������:{},�޸�{}��{}���ڳ����ɹ�,��Ӱ�����{}", accNum, year, month, m);
						}
					}
					// ���Ҷ�Ӧ���˱�
					FormBookEntry form = formBookEntryDao.selectByFormNum(out.getFormNum());
					if (form != null) {
						formList.add(form);
					}

					// ���Ҷ�Ӧ�����ϸ��
					map.put("formNum", out.getFormNum());
					List<InvtyDetail> invtyList = invtyDetailDao.selectByInvty(map);
					if (invtyList.size() != 0) {

						List<InvtyDetails> details = new ArrayList<>();
						for (InvtyDetail its : invtyList) {
							details.addAll(its.getInvtyDetailsList());
						}
						// ����ɾ����ϸ
						int ids = invtyDetailDao.delectInvtyList(details);
						logger.info("����ɾ����ϸ�ӱ�,��Ӱ�����{}", ids);
					}

				}
				// ɾ�����˱�
				int im = formBookEntryDao.delectFormBookList(formList);
				logger.info("����ɾ�����˱�,��Ӱ�����{}", im);

				// ɾ�����������
				int io = outIntoWhsAdjSnglDao.deleteOutIntoWhsAdjSnglList(outFormList);
				logger.info("����ɾ�����������,��Ӱ�����{}", io);

				if (im > 0 && io > 0) {
					message = "�ָ��ɹ�";
					isSuccess = true;
				} else {
					message = "�ָ��쳣";
					isSuccess = false;
				}
			}
		}

		return BaseJson.returnRespObj("account/form/final/backDeal ", isSuccess, message, null);
	}

	@Override
	@Transactional
	public String finalMonthDeal(Map map, String accNum, String loginTime) throws Exception {
		// ��ĩ����
		String message = "";
		Boolean isSuccess = true;
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		isSuccess = checkOutIsEndOfMonth(loginTime);
		if (!isSuccess) {
			message = "��ǰ��¼���ڣ�" + loginTime + "���ڽ�������";
			isSuccess = false;
			return BaseJson.returnRespObj("account/form/final/dealMonth", isSuccess, message, null);

		}

		List<MthEndStl> mthList = mthEndStlDao.selectByMap(map);
		if (mthList.size() != 0) {

			MthEndStl mth = mthList.get(0);
			if (mth.getIsMthEndStl() == 1) {
				message = "����������ִ��";
				isSuccess = false;
				return BaseJson.returnRespObj("account/form/final/dealMonth", isSuccess, message, null);
			}
			// ��ѯ�����Ѿ����˵ĵ���
			Map<String, Object> dataMap = new HashMap<>();
			dataMap.put("isNtBookOk", 1);
			dataMap.put("bookOkDt", mth.getAcctYr() + "-" + mth.getAcctMth() + "-01");

			List<FormBookEntry> formList = formBookEntryDao.selectMap(dataMap); // �����Ѿ����˵ĵ���

			if (formList.size() > 0) {
				// �޸ı��¼��˵���Ϊ����ĩ����
				for (FormBookEntry form : formList) {
					form.setIsMthEndStl(1);
				}
				formBookEntryDao.updateIsNtBook(formList);

				// �޸Ľ��,���ܽ��лָ�����
				dataMap.put("year", mth.getAcctYr());
				dataMap.put("month", mth.getAcctMth());
				dataMap.put("isMthEndStl", 0);

				List<InvtyMthTermBgnTab> termList = invtyMthTermBgnTabDao.selectByMthTerm(dataMap);
				List<InvtyMthTermBgnTab> nextMthList = new ArrayList<>();

				List<InvtyMthTermBgnTab> sendMthList = invtySendMthTermBgnTabDao.selectSendMthByMthTerm(dataMap);
				List<InvtyMthTermBgnTab> sendNextMthList = new ArrayList<>();

				if (termList.size() > 0) {

					for (InvtyMthTermBgnTab tab : termList) {
						if (BigDecimal.ZERO.compareTo(tab.getOthQty()) != 0
								|| BigDecimal.ZERO.compareTo(tab.getOthMoeny()) != 0) {
							InvtyMthTermBgnTab next = new InvtyMthTermBgnTab();
							next = generateNextMth(next, tab);
							nextMthList.add(next);
						}
						tab.setIsMthEndStl(1);
					}
					invtyMthTermBgnTabDao.updateMthList(termList);
					if (nextMthList.size() > 0) {
						invtyMthTermBgnTabDao.insertMthList(nextMthList);
					}

				}

				if (sendMthList.size() > 0) {
					// �����ڱ��µķ�Ʊ�����ͽ��
					dataMap.put("loginTime", loginTime);
					dataMap.put("bookDt", mth.getAcctYr() + "-" + mth.getAcctMth() + "-01");
					List<SellComnInvSub> sellList = sellComnInvDao.selectUnBllgQtyAndAmt(dataMap);
					Map<String, SellComnInvSub> sellMap = new HashMap<>();
					if (sellList.size() > 0) {
						for (SellComnInvSub sub : sellList) {
							String key = sub.getOutWhsId() + "-" + sub.getInvtyEncd() + "-" + sub.getWhsEncd() + "-"
									+ sub.getBatNum();

							if (!sellMap.containsKey(key)) {
								sellMap.put(key, sub);
							}
						}
					}

					for (InvtyMthTermBgnTab tab : sendMthList) {

						String key = tab.getCustId() + "-" + tab.getInvtyEncd() + "-" + tab.getWhsEncd() + "-"
								+ tab.getBatNum();

						if (sellMap.containsKey(key)) {

							SellComnInvSub sell = sellMap.get(key);

							if (sell != null) {
								tab.setSendMoeny(tab.getSendMoeny().add(sell.getNoTaxAmt()));
								tab.setSendQty(tab.getSendQty().add(sell.getQty()));
								if (tab.getSendQty().compareTo(BigDecimal.ZERO) == 0) {
									tab.setSendUnitPrice(new BigDecimal(0.00000000));
								} else {
									tab.setSendUnitPrice(tab.getSendMoeny()
											.divide(tab.getSendQty(), 8, BigDecimal.ROUND_HALF_UP).abs());
								}
								BigDecimal othSendQty = tab.getQty().add(tab.getInQty()).subtract(tab.getSendQty());
								BigDecimal othSendMoeny = tab.getAmt().add(tab.getInMoeny())
										.subtract(tab.getSendMoeny());
								BigDecimal uprc = new BigDecimal(0);
								if (othSendQty.compareTo(BigDecimal.ZERO) == 0) {
									uprc = new BigDecimal(0.00000000);
								} else {
									uprc = othSendMoeny.divide(othSendQty, 8, BigDecimal.ROUND_HALF_UP).abs();
								}
								tab.setOthMoeny(othSendMoeny);
								tab.setOthQty(othSendQty);
								tab.setOthUnitPrice(uprc);
							}
						}

						// �������µķ�����Ʒ
						if (BigDecimal.ZERO.compareTo(tab.getOthQty()) != 0
								|| BigDecimal.ZERO.compareTo(tab.getOthMoeny()) != 0) {
							InvtyMthTermBgnTab next = new InvtyMthTermBgnTab();
							next = generateNextMth(next, tab);
							sendNextMthList.add(next);
						}
						tab.setIsMthEndStl(1);
					}
					invtySendMthTermBgnTabDao.updateSendMthList(sendMthList);
					if (sendNextMthList.size() > 0) {
						invtySendMthTermBgnTabDao.insertMthList(sendNextMthList);
					}

				}
			}

			// ����ڳ�����
			int count = othOutDao.selectInvtyTabTermInvtyCount(loginTime);
			if (count == 0) {
				othOutDao.selectInvtyTabTermInvty(loginTime);
			}

			mth.setIsMthEndStl(1);
			mth.setIsMthSeal(1);
			mth.setAccNum(accNum);
			mth.setOprrDt(sf.format(new Date()));
			mth.setStlDt(loginTime);
			mthEndStlDao.updateIsMthEndStl(mth);

			message = "����ɹ�";
			isSuccess = true;
		}

		return BaseJson.returnRespObj("account/form/final/dealMonth ", isSuccess, message, null);
	}

	/**
	 * ��ĩ���� - �������µ��ڳ�����
	 */
	public InvtyMthTermBgnTab generateNextMth(InvtyMthTermBgnTab next, InvtyMthTermBgnTab tab) throws Exception {
		BeanUtils.copyProperties(next, tab);

		if (Integer.valueOf(tab.getAcctiMth()) == 12) {
			next.setAcctYr(String.valueOf(Integer.valueOf(tab.getAcctYr()) + 1));
			next.setAcctiMth("01");
		} else {
			next.setAcctYr(tab.getAcctYr());
			if (Integer.valueOf(String.valueOf(Integer.valueOf(tab.getAcctiMth()) + 1)) < 9) {
				next.setAcctiMth("0" + String.valueOf(Integer.valueOf(tab.getAcctiMth()) + 1));
			} else {
				next.setAcctiMth(String.valueOf(Integer.valueOf(tab.getAcctiMth()) + 1));
			}
		}
		next.setInMoeny(BigDecimal.ZERO);
		next.setInQty(BigDecimal.ZERO);
		next.setInUnitPrice(BigDecimal.ZERO);

		next.setSendMoeny(BigDecimal.ZERO);
		next.setSendQty(BigDecimal.ZERO);
		next.setSendUnitPrice(BigDecimal.ZERO);

		next.setQty(tab.getOthQty());
		next.setAmt(tab.getOthMoeny());
		next.setUprc(tab.getOthUnitPrice());
		next.setOthMoeny(tab.getOthMoeny());
		next.setOthQty(tab.getOthQty());
		next.setOthUnitPrice(tab.getOthUnitPrice());
		next.setIsFinalDeal(0);
		next.setIsMthEndStl(0);
		return next;
	}

	@Override
	@Transactional
	public String finalMonthDealBack(Map map, String accNum, String loginTime) throws IOException {
		String message = "";
		Boolean isSuccess = true;
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		map.put("loginTime", loginTime);
		List<MthEndStl> mthList = mthEndStlDao.selectByMap(map);

		// ��ѯ�¸����Ƿ��Ѿ�����
		String nextTime = getFirstDayOfNextMonth(loginTime, "yyyy-MM-dd HH:mm:ss");
		map.put("bookOkDt", nextTime);
		map.put("isNtBookOk", "1");

		if (mthList.size() == 0 || formBookEntryDao.selectMap(map).size() != 0) {
			message = "ȡ������ʧ��";
			isSuccess = false;
			logger.error("�����·�Ϊ��");

		} else {
			MthEndStl mth = mthList.get(0);
			// ��ѯ�����Ѿ����˵ĵ���
			Map<String, Object> dataMap = new HashMap<>();
			dataMap.put("isMthEndStl", 1); // ��ĩ����
			dataMap.put("bookOkDt", mth.getAcctYr() + "-" + mth.getAcctMth() + "-01");
			List<FormBookEntry> formList = formBookEntryDao.selectMap(dataMap); // ���¼��˵ĵ���

			if (formList.size() > 0) {
				// �޸ı��¼��˵���Ϊδ��ĩ����
				for (FormBookEntry form : formList) {
					form.setIsMthEndStl(0);
				}

				// �Ѿ���ĩ���˵��ڳ�����
				dataMap.put("year", mth.getAcctYr());
				dataMap.put("month", mth.getAcctMth());
				dataMap.put("isMthEndStl", 0);
				List<InvtyMthTermBgnTab> termList = invtyMthTermBgnTabDao.selectByMthTerm(dataMap);
				List<InvtyMthTermBgnTab> sendMthList = invtySendMthTermBgnTabDao.selectSendMthByMthTerm(dataMap);
				if (12 == Integer.valueOf(mth.getAcctMth())) {
					dataMap.put("year", Integer.valueOf(mth.getAcctYr()) + 1);
					dataMap.put("month", 01);
				} else {
					dataMap.put("month", Integer.valueOf(mth.getAcctMth()) + 1);
				}
				List<InvtyMthTermBgnTab> termNextList = invtyMthTermBgnTabDao.selectByMthTerm(dataMap);
				List<InvtyMthTermBgnTab> sendMthNextList = invtySendMthTermBgnTabDao.selectSendMthByMthTerm(dataMap);

				if (termList.size() > 0) {
					for (InvtyMthTermBgnTab tab : termList) {
						tab.setIsMthEndStl(0);
					}
					invtyMthTermBgnTabDao.updateMthList(termList);
					if (termNextList.size() > 0) {
						invtyMthTermBgnTabDao.deleteMthList(termNextList);
					}
				}
				if (sendMthList.size() > 0) {
					// �����ڱ��µķ�Ʊ�����ͽ��
					dataMap.put("loginTime", loginTime);
					dataMap.put("bookDt", mth.getAcctYr() + "-" + mth.getAcctMth() + "-01");
					List<SellComnInvSub> sellList = sellComnInvDao.selectUnBllgQtyAndAmt(dataMap);
					Map<String, SellComnInvSub> sellMap = new HashMap<>();
					if (sellList.size() > 0) {
						for (SellComnInvSub sub : sellList) {
							String key = sub.getOutWhsId() + "-" + sub.getInvtyEncd() + "-" + sub.getWhsEncd() + "-"
									+ sub.getBatNum();

							if (!sellMap.containsKey(key)) {
								sellMap.put(key, sub);
							}

						}
					}

					for (InvtyMthTermBgnTab tab : sendMthList) {
						// �����ڱ��µķ�Ʊ�����ͽ��

						String key = tab.getCustId() + "-" + tab.getInvtyEncd() + "-" + tab.getWhsEncd() + "-"
								+ tab.getBatNum();
						if (sellMap.containsKey(key)) {
							SellComnInvSub sell = sellMap.get(key);
							if (sell != null) {
								tab.setSendMoeny(tab.getSendMoeny().subtract(sell.getNoTaxAmt()));
								tab.setSendQty(tab.getSendQty().subtract(sell.getQty()));
								if (tab.getSendQty().compareTo(BigDecimal.ZERO) == 0) {
									tab.setSendUnitPrice(new BigDecimal(0.00000000));
								} else {
									tab.setSendUnitPrice(tab.getSendMoeny()
											.divide(tab.getSendQty(), 8, BigDecimal.ROUND_HALF_UP).abs());
								}
								BigDecimal othSendQty = tab.getQty().add(tab.getInQty()).subtract(tab.getSendQty());
								BigDecimal othSendMoeny = tab.getAmt().add(tab.getInMoeny())
										.subtract(tab.getSendMoeny());
								BigDecimal uprc = new BigDecimal(0);
								if (othSendQty.compareTo(BigDecimal.ZERO) == 0) {
									uprc = new BigDecimal(0.00000000);
								} else {
									uprc = othSendMoeny.divide(othSendQty, 8, BigDecimal.ROUND_HALF_UP).abs();
								}
								tab.setOthMoeny(othSendMoeny);
								tab.setOthQty(othSendQty);
								tab.setOthUnitPrice(uprc);
							}

						}

						tab.setIsMthEndStl(0);
					}
					invtySendMthTermBgnTabDao.updateSendMthList(sendMthList);
					if (sendMthNextList.size() > 0) {
						invtySendMthTermBgnTabDao.deleteSendMthList(sendMthNextList);
					}
				}

				formBookEntryDao.updateIsNtBook(formList);

			}

			// �ָ����
			othOutDao.deleteInvtyTabTermInvty(loginTime);

			mth.setIsMthEndStl(0);
			mth.setAccNum(" ");
			mth.setOprrDt(sf.format(new Date()));
			mth.setStlDt(null);
			int ie = mthEndStlDao.updateIsMthEndStl(mth);
			message = "ȡ���ɹ�";
			isSuccess = true;
		}
		return BaseJson.returnRespObj("account/form/final/backDealMonth ", isSuccess, message, null);
	}

	/**
	 * �س��ݹ�
	 */
	private List<IntoWhs> backTmepForm(List<FormBookEntry> dataList, Boolean isBack, String accNum, String loginTime) {

		List<IntoWhs> intoWhsList = new ArrayList<>();

		for (FormBookEntry form : dataList) {
			if (form.getIsNtBllg() != null && form.getOutIntoWhsTypId() != null) {
				if (form.getIsNtBllg() == 0 && Integer.valueOf(form.getOutIntoWhsTypId()) == 9) {
					if (isBack) {
						IntoWhs into = new IntoWhs();
						into.setFormNum(form.getFormNum());
						into.setIsNtBookEntry(0);
						into.setBookEntryPers(" ");
						into.setBookEntryTm(" ");
						intoWhsList.add(into);
					} else {
						IntoWhs into = new IntoWhs();
						into.setFormNum(form.getFormNum());
						into.setIsNtBookEntry(1);
						into.setBookEntryPers(accNum);
						into.setBookEntryTm(loginTime);
						intoWhsList.add(into);
					}

				}
			}

		}

		return intoWhsList;

	}

	@Override
	public String selectFinalMonthDealList(Map map, String loginTime) throws IOException {
		// ��ѯ��ĩ����
		// ��ǰ��¼�� �����·�
		String year = loginTime.substring(0, 4);
		map.put("year", year);
		List<MthEndStl> stlList = mthEndStlDao.selectByMap(map);
		if (stlList.size() != 0) {
			for (MthEndStl mth : stlList) {
				mth.setMthBgn(mth.getMthBgn().substring(0, 10));
				mth.setMthEnd(mth.getMthEnd().substring(0, 10));
			}
		}
		return BaseJson.returnRespObjList("account/form/final/dealMonthList ", true, "��ѯ�ɹ�", null, stlList);
	}

	@Override
	public String selectNovoucherList(Map map, String isNtMakeVouch) {
		// δ����ƾ֤�����б�
		String resp = "";
		try {
			map.put("isNtChk", "1"); // �����
			map.put("isNtBookOk", "1"); // �Ѽ���
			if (isNtMakeVouch.equals("111")) {
				map.put("isNtMakeVouch", "");
			} else {
				map.put("isNtMakeVouch", isNtMakeVouch); // δ����ƾ֤
			}

			String formNums = (String) map.get("formNum");// ������ݺ�
			String formType = (String) map.get("formType"); // ��������
			String whsCode = (String) map.get("whsEncd");

			List<String> whsCodeList = CommonUtil.strToList(whsCode);
			List<String> formNumList = CommonUtil.strToList(formNums);
			List<String> formTypeList = CommonUtil.strToList(formType);
			int pageNo = (int) map.get("pageNo");
			int pageSize = (int) map.get("pageSize");
			int count = 0;
			map.put("formNum", "");
			map.put("formTypeList", formTypeList);
			map.put("whsCodeList", whsCodeList);
			map.put("formNumList", formNumList);

			List<FormBookEntry> dataList = new ArrayList<>();

			// �ɹ�-����-���������
			if (formTypeList.contains("004") || formTypeList.contains("009") || formTypeList.contains("014")
					|| formTypeList.contains("015")) {
				// ����ⵥ��

				dataList = formBookEntryDao.selectFormNoVoucherGeneratedList(map);
				count = formBookEntryDao.countFormNoVoucherGeneratedList(map);
			}

			// ����-��װ-��ж
			if (formTypeList.contains("011") || formTypeList.contains("012") || formTypeList.contains("013")) {
				if (dataList.size() < pageSize) {
					pageSize = pageSize - dataList.size();
					map.put("num", pageSize);
					// ����ⵥ��
					dataList.addAll(formBookEntryDao.selectMergeMapAndPage(map));
					count += formBookEntryDao.countFormNoVoucherGeneratedList(map);
				}
			}
			// ���۵�
			if (formTypeList.contains("007")) {
				if (dataList.size() < pageSize) {
					pageSize = pageSize - dataList.size();
					map.put("num", pageSize);
					dataList.addAll(formBookEntryDao.selectSellSnglNoVoucherGeneratedList(map));
					count += formBookEntryDao.countSellSnglNoVoucherGeneratedList(map);
				}

			}

			// �ɹ���Ʊ
			if (formTypeList.contains("019") || formTypeList.contains("20")) {
				if (dataList.size() < pageSize) {
					pageSize = pageSize - dataList.size();
					map.put("num", pageSize);
					dataList.addAll(formBookEntryDao.selectPursInvMasTabNoVoucherGeneratedList(map));
					count += formBookEntryDao.countPursInvMasTabNoVoucherGeneratedList(map);
				}
			}

			// ���۷�Ʊ(ר��/��ͨ)
			if (formTypeList.contains("021")) {
				if (dataList.size() < pageSize) {
					pageSize = pageSize - dataList.size();
					map.put("num", pageSize);
					map.put("invTyp", "����ר�÷�Ʊ");
					dataList.addAll(formBookEntryDao.selectSellInvMasTabNoVoucherGeneratedList(map));
					count += formBookEntryDao.countSellInvMasTabNoVoucherGeneratedList(map);
				}
			}
			if (formTypeList.contains("022")) {
				if (dataList.size() < pageSize) {
					pageSize = pageSize - dataList.size();
					map.put("num", pageSize);
					map.put("invTyp", "������ͨ��Ʊ");
					dataList.addAll(formBookEntryDao.selectSellInvMasTabNoVoucherGeneratedList(map));
					count += formBookEntryDao.countSellInvMasTabNoVoucherGeneratedList(map);
				}
			}
			// ��������
			if (formTypeList.contains("030")) {
				if (dataList.size() < pageSize) {
					pageSize = pageSize - dataList.size();
					map.put("num", pageSize);

					dataList.addAll(formBookEntryDao.selectOutIntoWhsAdjSnglNoVoucherGeneratedList(map));
					count += formBookEntryDao.countOutIntoWhsAdjSnglNoVoucherGeneratedList(map);

				}
			}
			// ���������
			if (formTypeList.contains("031")) {
				if (dataList.size() < pageSize) {
					pageSize = pageSize - dataList.size();
					map.put("num", pageSize);
					dataList.addAll(formBookEntryDao.selectOutIntoWhsAdjSnglNoVoucherGeneratedList(map));
					count += formBookEntryDao.countOutIntoWhsAdjSnglNoVoucherGeneratedList(map);

				}
			}

			// ���ֻس嵥
			if (formTypeList.contains("017")) {
				if (dataList.size() < pageSize) {
					pageSize = pageSize - dataList.size();
					map.put("num", pageSize);
					map.put("isRedBack", "0");
					dataList.addAll(formBookEntryDao.selectFormBackFlushNoVoucherGeneratedList(map));
					count += formBookEntryDao.countFormBackFlushNoVoucherGeneratedList(map);

				}
			}
			// ���ֻس嵥
			if (formTypeList.contains("016")) {
				if (dataList.size() < pageSize) {
					pageSize = pageSize - dataList.size();
					map.put("num", pageSize);
					map.put("isRedBack", "1");
					dataList.addAll(formBookEntryDao.selectFormBackFlushNoVoucherGeneratedList(map));
					count += formBookEntryDao.countFormBackFlushNoVoucherGeneratedList(map);

				}
			}
			// �˻���rtn_goods
			if (formTypeList.contains("008")) {
				if (dataList.size() < pageSize) {
					pageSize = pageSize - dataList.size();
					map.put("num", pageSize);
					dataList.addAll(formBookEntryDao.selectRtnGoodsNoVoucherGeneratedList(map));
					count += formBookEntryDao.countRtnGoodsNoVoucherGeneratedList(map);

				}
			}
			// ί�д���������entrs_agn_delv
			if (formTypeList.contains("023")) {
				if (dataList.size() < pageSize) {
					pageSize = pageSize - dataList.size();
					map.put("num", pageSize);
					dataList.addAll(formBookEntryDao.selectEntrsAgnDelvNoVoucherGeneratedList(map));
					count += formBookEntryDao.countEntrsAgnDelvNoVoucherGeneratedList(map);

				}
			}
			// ί�д����˻���
			if (formTypeList.contains("024")) {
				if (dataList.size() < pageSize) {
					pageSize = pageSize - dataList.size();
					map.put("num", pageSize);
					dataList.addAll(formBookEntryDao.selectEntrsAgnDelvNoVoucherGeneratedList(map));
					count += formBookEntryDao.countEntrsAgnDelvNoVoucherGeneratedList(map);

				}
			}

			// ��Ŀ�Ƶ�
			if (dataList.size() > 0) {

				if ((String) map.get("isSelect") != null && (String) map.get("isSelect") == "") {
					for (FormBookEntry form : dataList) {

						for (FormBookEntrySub sub : form.getBookEntrySub()) {

							form = billingRulesByFormTypEncd(form, sub);
							sub.setSubjectDebit(null);
							sub.setSubjectCredit(null);
						}
					}
				}

				// dataList = mergedFormSubject(dataList);

			}
			resp = BaseJson.returnRespList("mis/account/invty/novoucher/list ", true, "��ѯ�ɹ�", count, pageNo, pageSize,
					0, 0, dataList);

			logger.info("��ѯδ����ƾ֤�����б�{}��", count);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	/**
	 * �ϲ���Ŀ
	 * 
	 * @param dataList
	 * @return
	 */
	private List<FormBookEntry> mergedFormSubject(List<FormBookEntry> dataList) {

		for (FormBookEntry form : dataList) {
			List<SubjectDebit> deList = null;// form.getSubjectDebitList();
			List<SubjectCredit> creList = null;// form.getSubjectCreditList();
			if (deList == null) {
				deList = new ArrayList<>();
			}
			if (creList == null) {
				creList = new ArrayList<>();
			}

			Map<String, FormBookEntrySub> deMap = new HashMap<>();
			Map<String, FormBookEntrySub> crMap = new HashMap<>();
			for (FormBookEntrySub sub : form.getBookEntrySub()) {

				if (deMap.containsKey(sub.getSubDebitId())) {
					FormBookEntrySub sub1 = (FormBookEntrySub) deMap.get(sub.getSubDebitId());
					sub1.setSubDebitMoney(sub1.getSubDebitMoney().add(sub.getSubDebitMoney()));
					sub1.setSubDebitNum(sub1.getSubDebitNum().add(sub.getSubDebitNum()));

					deMap.put(sub.getSubDebitId(), sub1);

				} else {
					deMap.put(sub.getSubDebitId(), sub);
				}

				if (crMap.containsKey(sub.getSubCreditId())) {
					FormBookEntrySub sub1 = (FormBookEntrySub) crMap.get(sub.getSubCreditId());
					sub1.setSubCreditMoney(sub1.getSubCreditMoney().add(sub.getSubCreditMoney()));
					sub1.setSubCreditNum(sub1.getSubCreditNum().add(sub.getSubCreditNum()));

					crMap.put(sub.getSubCreditId(), sub1);
				} else {
					crMap.put(sub.getSubCreditId(), sub);
				}
			}

			if (!deMap.isEmpty()) {
				for (Map.Entry<String, FormBookEntrySub> key : deMap.entrySet()) {
					FormBookEntrySub forms = (FormBookEntrySub) key.getValue();
					SubjectDebit de = new SubjectDebit();
					de.setSubDebitMoney(forms.getSubDebitMoney());
					de.setSubDebitNum(forms.getSubDebitNum());
					de.setSubDebitType(forms.getSubDebitType());
					de.setSubDebitId(forms.getSubDebitId());
					de.setSubDebitPath(forms.getSubDebitPath());
					de.setSubDebitNm(forms.getSubDebitNm());
					deList.add(de);
					// form.setSubjectDebitList(deList);
				}
			}
			if (!crMap.isEmpty()) {
				for (Map.Entry<String, FormBookEntrySub> key : crMap.entrySet()) {
					FormBookEntrySub forms = (FormBookEntrySub) key.getValue();
					SubjectCredit cr = new SubjectCredit();

					cr.setSubCreditMoney(forms.getSubCreditMoney());
					cr.setSubCreditNum(forms.getSubCreditNum());
					cr.setSubCreditType(forms.getSubCreditType());
					cr.setSubCreditId(forms.getSubCreditId());
					cr.setSubCreditPath(forms.getSubCreditPath());
					cr.setSubCreditNm(forms.getSubCreditNm());
					creList.add(cr);
					// form.setSubjectCreditList(creList);
				}
			}
		}
		return dataList;
	}

	@Override
	public String selectBackFormList(Map map, String loginTime) throws IOException {

		String resp = "";
		boolean isSuccess = true;
		int pageNo = Integer.parseInt(map.get("pageNo").toString());
		int pageSize = Integer.parseInt(map.get("pageSize").toString());
		int count = 0;
		List<FormBookEntry> list = new ArrayList<>();

		if (!isSuccess) {
			String message = "��ǰ��¼���ڣ�" + loginTime + "���ڽ�������";
			isSuccess = false;
			resp = BaseJson.returnRespObj("mis/account/form/backForm/list", isSuccess, message, null);
			return resp;
		}
		try {

			map.put("isNtChk", "1"); // �����
			map.put("isNtBookOk", "1"); // �Ѽ���
			map.put("isNtMakeVouch", "0"); // δ����ƾ֤
			map.put("bookOkDt", loginTime);
			String formCodes = (String) map.get("formCode");
			String formNums = (String) map.get("formNum");
			String whsCode = (String) map.get("whsEncd");
			String formType = (String) map.get("formType");
			List<String> formCodeList = CommonUtil.strToList(formCodes);
			List<String> whsCodeList = CommonUtil.strToList(whsCode);
			List<String> formNumList = CommonUtil.strToList(formNums);
			List<String> formTypeList = CommonUtil.strToList(formType);
			map.put("formCodeList", formCodeList);
			map.put("whsCodeList", whsCodeList);
			map.put("formNumList", formNumList);
			map.put("formTypeList", formTypeList);
			list = formBookEntryDao.selectMapAndPage(map);
			count = formBookEntryDao.countSelectMapAndPage(map);

		} catch (Exception e) {
			e.printStackTrace();
		}
		resp = BaseJson.returnRespList("mis/account/form/backForm/list", true, "��ѯ�ɹ�", count, pageNo, pageSize, 0, 0,
				list);
		return resp;

	}

	@Override
	public String settingDealMonth(Map map, String accNum, String loginTime) throws IOException {
		String message = "";
		Boolean isSuccess = true;
		// ���ҵ�ǰ����Ƿ����,�����Ƿ����
		String year = loginTime.substring(0, 4);
		int month = Integer.valueOf(loginTime.substring(5, 7));
		map.put("year", year);
		map.put("isMthEndStl", "1");
		List<MthEndStl> stlList = mthEndStlDao.selectByMap(map);
		List<MthEndStl> stlLists = new ArrayList<>();
		if (stlList.size() != 0) {
			message = "������·�������ĩ����,��������";
			isSuccess = false;
		} else {
			map.put("isMthEndStl", "0");
			stlList = mthEndStlDao.selectByMap(map);
			if (stlList.size() != 0) {
				int y = mthEndStlDao.delectByYear(year);
			}

			for (int i = 0; i < 12; i++) {

				MthEndStl stl = new MthEndStl();
				stl.setAccNum(accNum);
				stl.setAcctYr(year);
				if (i + 1 < 10) {
					stl.setAcctMth("0" + Integer.valueOf(i + 1));
				} else {
					stl.setAcctMth(String.valueOf(i + 1));
				}
				if (month > i + 1) {
					stl.setIsMthSeal(1);
					stl.setIsMthEndStl(1);
				} else {
					stl.setIsMthSeal(0);
					stl.setIsMthEndStl(0);
				}

				stl.setMthBgn(getFirstDayOfMonth1(Integer.valueOf(year), i + 1));
				stl.setMthEnd(getLastDayOfMonth1(Integer.valueOf(year), i + 1));
				stlLists.add(stl);

			}
			mthEndStlDao.insertListAllYear(stlLists);
			message = "���óɹ�";
			isSuccess = true;
		}
		return BaseJson.returnRespObj("account/form/final/settingDealMonth ", isSuccess, message, null);
	}

	/**
	 * ��ȡָ�����µĵ�һ��
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public static String getFirstDayOfMonth1(int year, int month) {
		Calendar cal = Calendar.getInstance();
		// �������
		cal.set(Calendar.YEAR, year);
		// �����·�
		cal.set(Calendar.MONTH, month - 1);
		// ��ȡĳ����С����
		int firstDay = cal.getMinimum(Calendar.DATE);
		// �����������·ݵ���С����
		cal.set(Calendar.DAY_OF_MONTH, firstDay);
		// ��ʽ������
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(cal.getTime());
	}

	/**
	 * get��ȡָ�����µ����һ��
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public static String getLastDayOfMonth1(int year, int month) {
		Calendar cal = Calendar.getInstance();
		// �������
		cal.set(Calendar.YEAR, year);
		// �����·�
		cal.set(Calendar.MONTH, month - 1);
		// ��ȡĳ���������
		int lastDay = cal.getActualMaximum(Calendar.DATE);
		// �����������·ݵ��������
		cal.set(Calendar.DAY_OF_MONTH, lastDay);
		// ��ʽ������
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(cal.getTime());
	}

	/**
	 * get���ָ�����ڵ�ǰһ��
	 * 
	 * @param specifiedDay
	 * @return
	 */
	public static String getSpecifiedDayBefore(String specifiedDay) {

		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c.setTime(date);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day - 1);

		String dayBefore = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
		return dayBefore;
	}

	/**
	 * ��ȡָ�������¸��µĵ�һ��
	 * 
	 * @param dateStr
	 * @param format
	 * @return
	 */
	public static String getFirstDayOfNextMonth(String dateStr, String format) {

		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Calendar calendar = Calendar.getInstance();
		Date date = null;
		try {
			date = new SimpleDateFormat("yy-MM-dd").parse(dateStr);
			calendar.setTime(date);
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			calendar.add(Calendar.MONTH, 1);
			return sdf.format(calendar.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ��������ⵥ
	 */
	public List<FormBookEntry> selectOthOutWhs(Map map, Boolean isBook) {
		List<FormBookEntry> othOutFormList = new ArrayList<>();

		// ��������ⵥ
		List<OthOutIntoWhs> othOutIntoWhsList = new ArrayList<>();
		if (isBook) {
			othOutIntoWhsList = othOutDao.selectOthOutIntoWhsIsInvty(map);
		} else {
			othOutIntoWhsList = othOutDao.selectOthOutIntoWhsStream(map);
		}
		logger.info("��ѯ�������˵����б���������ⵥ{}��", othOutIntoWhsList.size());
		if (othOutIntoWhsList.size() > 0) {

			for (OthOutIntoWhs into : othOutIntoWhsList) {

				FormBookEntry form = new FormBookEntry(into.getFormNum(), into.getFormDt(), into.getIsNtChk(),
						into.getChkr(), into.getChkTm(), into.getOutIntoWhsTypId(), into.getOutIntoWhsTypNm(), "", "",
						"", "", "", "", "", "", "", "", "", "", into.getIsNtBookEntry(), 0, into.getMemo(), null, null,
						0, into.getSrcFormNum(), into.getSetupPers(), into.getSetupTm(), into.getFormTypEncd(),
						into.getFormTypName(), "", "", "", "");
				List<FormBookEntrySub> subList = new ArrayList<>();
				if (into.getOutIntoSubList().size() > 0) {

					for (OthOutIntoWhsSubTab sub : into.getOutIntoSubList()) {

						FormBookEntrySub forms = new FormBookEntrySub(sub.getFormNum(), null, sub.getInvtyEncd(),
								sub.getInvtyNm(), sub.getInvtyCd(), sub.getSpcModel(), sub.getMeasrCorpId(),
								sub.getMeasrCorpNm(), sub.getBxRule(), sub.getInvtyClsEncd(), sub.getInvtyClsNm(),
								into.getWhsEncd(), sub.getWhsNm(), sub.getUnTaxUprc(), sub.getUnTaxAmt(),
								new BigDecimal(0.00), sub.getCntnTaxUprc(), new BigDecimal(0.00), sub.getTaxRate(),
								sub.getQty(), new BigDecimal(0), sub.getBatNum(), sub.getIntlBat(), sub.getPrdcDt(),
								sub.getInvldtnDt(), sub.getBaoZhiQiDt(), new BigDecimal(0.00), new BigDecimal(0.00),
								new BigDecimal(0.00), sub.getOrdrNum().intValue(), sub.getProjEncd(), sub.getProjNm());

						subList.add(forms);
					}
				}
				form.setBookEntrySub(subList);
				if (isBook) {
					if (formBookEntryDao.selectByFormNum(form.getFormNum()) == null) {
						othOutFormList.add(form);
					}
				} else {
					othOutFormList.add(form);
				}

			}

		}

		return othOutFormList;
	}

	/**
	 * ���۳��ⵥ
	 */
	public List<FormBookEntry> selectSellOutWhs(Map map, Boolean isBook) {
		List<FormBookEntry> sellOutFormList = new ArrayList<>();

		// ���۳��ⵥ
		List<SellOutWhs> sellOutList = new ArrayList<>();
		if (isBook) {
			sellOutList = sellOutWhsDao.selectSellOutWhsIsInvty(map);
		} else {
			sellOutList = sellOutWhsDao.selectSellOutWhsStream(map);
		}
		logger.info("��ѯ�������˵����б����۳��ⵥ{}��", sellOutList.size());

		if (sellOutList.size() > 0) {

			for (SellOutWhs into : sellOutList) {

				FormBookEntry form = new FormBookEntry(into.getOutWhsId(), into.getOutWhsDt(), into.getIsNtChk(),
						into.getChkr(), into.getChkTm(), into.getOutIntoWhsTypId(), into.getOutIntoWhsTypNm(),
						into.getBizTypId(), into.getBizTypNm(), into.getSellTypId(), into.getSellTypNm(), "", "", "",
						"", into.getCustId(), into.getCustNm(), into.getAccNum(), into.getUserName(),
						into.getIsNtBookEntry(), 0, into.getMemo(), into.getIsNtBllg(), into.getIsNtStl(), 0,
						into.getSellOrdrInd(), into.getSetupPers(), into.getSetupTm(), into.getFormTypEncd(),
						into.getFormTypName(), into.getRecvSendCateId(), into.getRecvSendCateNm(), into.getDeptId(),
						into.getDeptName());

				List<FormBookEntrySub> subList = new ArrayList<>();
				if (into.getSellOutWhsSub().size() > 0) {

					for (SellOutWhsSub sub : into.getSellOutWhsSub()) {

						FormBookEntrySub forms = new FormBookEntrySub(sub.getOutWhsId(), sub.getToOrdrNum(),
								sub.getInvtyEncd(), sub.getInvtyNm(), sub.getInvtyCd(), sub.getSpcModel(),
								sub.getMeasrCorpId(), sub.getMeasrCorpNm(), sub.getBxRule(), sub.getInvtyClsEncd(),
								sub.getInvtyClsNm(), sub.getWhsEncd(), sub.getWhsNm(), sub.getNoTaxUprc(),
								sub.getNoTaxAmt(), sub.getTaxAmt(), sub.getCntnTaxUprc(), sub.getPrcTaxSum(),
								sub.getTaxRate(), sub.getQty(), sub.getBxQty(), sub.getBatNum(), sub.getIntlBat(),
								sub.getPrdcDt(), sub.getInvldtnDt(), sub.getBaoZhiQi().toString(), new BigDecimal(0.00),
								new BigDecimal(0.00), new BigDecimal(0.00), sub.getOrdrNum().intValue(),
								sub.getProjEncd(), sub.getProjNm());
						subList.add(forms);
					}
				}
				form.setBookEntrySub(subList);
				if (isBook) {
					if (formBookEntryDao.selectByFormNum(form.getFormNum()) == null) {
						sellOutFormList.add(form);
					}
				} else {
					sellOutFormList.add(form);
				}

			}

		}
		return sellOutFormList;
	}

	/**
	 * �ɹ���ⵥ
	 * 
	 * @throws ParseException
	 */
	public List<FormBookEntry> selectIntoWhs(Map map, Boolean isBook) throws ParseException {
		List<FormBookEntry> intoWhsFormList = new ArrayList<>();

		// �ɹ����

		List<IntoWhs> intoWhsList = new ArrayList<>();
		if (isBook) {
			intoWhsList = intoWhsDao.selectIntoWhsIsInvty(map);
		} else {
			intoWhsList = intoWhsDao.selectIntoWhsStream(map);
		}
		logger.info("��ѯ�������˵����б��ɹ���ⵥ{}��", intoWhsList.size());

		if (intoWhsList.size() > 0) {

			for (IntoWhs into : intoWhsList) {

				FormBookEntry form = new FormBookEntry(into.getIntoWhsSnglId(), into.getIntoWhsDt(), into.getIsNtChk(),
						into.getChkr(), into.getChkTm(), into.getOutIntoWhsTypId(), into.getOutIntoWhsTypNm(), "", "",
						"", "", into.getPursTypId(), into.getPursTypNm(), into.getProvrId(), into.getProvrNm(), "", "",
						into.getAccNum(), into.getUserName(), into.getIsNtBookEntry(), 0, into.getMemo(),
						into.getIsNtBllg(), into.getIsNtStl(), 0, into.getPursOrdrId(), into.getSetupPers(),
						into.getSetupTm(), into.getFormTypEncd(), into.getFormTypName(), into.getRecvSendCateId(),
						into.getRecvSendCateNm(), into.getDeptId(), into.getDeptName());

				List<FormBookEntrySub> subList = new ArrayList<>();
				if (into.getIntoWhsSub().size() > 0) {

					for (IntoWhsSub sub : into.getIntoWhsSub()) {

						FormBookEntrySub forms = new FormBookEntrySub(sub.getIntoWhsSnglId(), sub.getToOrdrNum(),
								sub.getInvtyEncd(), sub.getInvtyNm(), sub.getInvtyCd(), sub.getSpcModel(),
								sub.getMeasrCorpId(), sub.getMeasrCorpNm(), sub.getBxRule(), sub.getInvtyClsEncd(),
								sub.getInvtyClsNm(), sub.getWhsEncd(), sub.getWhsNm(), sub.getNoTaxUprc(),
								sub.getNoTaxAmt(), sub.getTaxAmt(), sub.getCntnTaxUprc(), sub.getPrcTaxSum(),
								sub.getTaxRate(), sub.getQty(), sub.getBxQty(), sub.getBatNum(), sub.getIntlBat(),
								sub.getPrdcDt(), sub.getInvldtnDt(), sub.getBaoZhiQi(), sub.getUnBllgQty(),
								sub.getUnBllgUprc(), sub.getUnBllgAmt(), sub.getOrdrNum().intValue(), sub.getProjEncd(),
								sub.getProjNm());

						subList.add(forms);

					}

				}
				form.setBookEntrySub(subList);
				if (isBook) {
					if (formBookEntryDao.selectByFormNum(form.getFormNum()) == null) {
						intoWhsFormList.add(form);
					}

				} else {
					intoWhsFormList.add(form);
				}

			}

		}

		return intoWhsFormList;
	}

	/**
	 * ��Ŀ�Ƶ�����
	 * 
	 * @param otherList
	 * @param stockList
	 * @param formBookEntry
	 * @param outIntoWhsTypId ��������
	 * @param isNtBllg        �Ƿ�Ʊ
	 * @param sellTypId       �������ͱ��
	 * @return
	 */
	private FormBookEntrySub subMakeForm(FormBookEntry form, FormBookEntrySub subs) {

		// �����Ŀ
		Map<String, Object> map = new HashMap<>();
		map.put("clsEncd", subs.getInvtyClsEncd()); // ����������
		map.put("sellTypId", form.getSellTypId()); // �������� 1-��ͨ���� 2-ί�д���

		List<AcctItmDoc> stockList = accItemDao.selectStockByClsEncd(map);
		if (stockList.size() == 0) {
			AcctItmDoc acc = new AcctItmDoc();
			acc.setSubjId("");
			acc.setSubjNm("");
			stockList.add(acc);
		}

		// �Է���Ŀ
		if (StringUtils.isEmpty(form.getRecvSendCateId())) {
			map.put("recvSendCateId", ""); // �շ�������
		} else {
			map.put("recvSendCateId", form.getRecvSendCateId()); // �շ�������
		}
		// map.put("invtyEncd",subs.getInvtyEncd()); //�������
		if (StringUtils.isNotEmpty(String.valueOf(form.getIsNtBllg()))) {
			map.put("isNtBllg", form.getIsNtBllg()); // �Ƿ�Ʊ 0-ȡ�ݹ���Ŀ 1-ȡ�Է���Ŀ
		}
		List<AcctItmDoc> otherList = accItemDao.selectOtherByClsEncd(map);

		if (otherList.size() == 0) {
			AcctItmDoc acc = new AcctItmDoc();
			acc.setSubjId("");
			acc.setSubjNm("");
			otherList.add(acc);
		}

		// 1 ������ⵥ ��/�� ��/�� �������
		if (Integer.valueOf(form.getOutIntoWhsTypId()) == 1) {
			// ��ȡ��
			subs.setSubjectDebit(new SubjectDebit(stockList.get(0).getSubjId(), "���", stockList.get(0).getSubjNm(),
					subs.getNoTaxAmt(), new BigDecimal(0), subs.getQty(), new BigDecimal(0), 1));

		}

		// 2 �������ⵥ ��/�� ��/��
		if (Integer.valueOf(form.getOutIntoWhsTypId()) == 2) {
			// ��ȡ��
			subs.setSubjectCredit(new SubjectCredit(stockList.get(0).getSubjId(), "���", stockList.get(0).getSubjNm(),
					subs.getNoTaxAmt(), new BigDecimal(0), subs.getQty(), new BigDecimal(0), 2));

		}

		// 3 ��װ��ⵥ ��/�� ��/��
		if (Integer.valueOf(form.getOutIntoWhsTypId()) == 3) {
			// ��ȡ��
			subs.setSubjectDebit(new SubjectDebit(stockList.get(0).getSubjId(), "���", stockList.get(0).getSubjNm(),
					subs.getNoTaxAmt(), new BigDecimal(0), subs.getQty(), new BigDecimal(0), 1));

		}
		// 4 ��װ���ⵥ ��/�� ��/��
		if (Integer.valueOf(form.getOutIntoWhsTypId()) == 4) {
			// ����->ȡ��װ��������
			// ����-->ȡ��װ�������
			// ��ȡ��
			subs.setSubjectCredit(new SubjectCredit(stockList.get(0).getSubjId(), "���", stockList.get(0).getSubjNm(),
					subs.getNoTaxAmt(), new BigDecimal(0), subs.getQty(), new BigDecimal(0), 2));

		}

		// 5 ��ж��ⵥ ��/�� ��/��
		if (Integer.valueOf(form.getOutIntoWhsTypId()) == 5) {
			// ��ȡ��
			subs.setSubjectDebit(new SubjectDebit(stockList.get(0).getSubjId(), "���", stockList.get(0).getSubjNm(),
					subs.getNoTaxAmt(), new BigDecimal(0), subs.getQty(), new BigDecimal(0), 1));

		}
		// 6 ��ж���ⵥ ��/�� ��/��
		if (Integer.valueOf(form.getOutIntoWhsTypId()) == 6) {

			// ��ȡ��
			subs.setSubjectCredit(new SubjectCredit(stockList.get(0).getSubjId(), "���", stockList.get(0).getSubjNm(),
					subs.getNoTaxAmt(), new BigDecimal(0), subs.getQty(), new BigDecimal(0), 2));

		}
		// 7 �̿����ⵥ ��/�� ��/��
		if (Integer.valueOf(form.getOutIntoWhsTypId()) == 7) {
			// ��ȡ��
			subs.setSubjectDebit(new SubjectDebit(otherList.get(0).getSubjId(), "�Է�", otherList.get(0).getSubjNm(),
					subs.getNoTaxAmt(), new BigDecimal(0), subs.getQty(), new BigDecimal(0), 1));
			// ��ȡ��
			subs.setSubjectCredit(new SubjectCredit(stockList.get(0).getSubjId(), "���", stockList.get(0).getSubjNm(),
					subs.getNoTaxAmt(), new BigDecimal(0), subs.getQty(), new BigDecimal(0), 2));
		}
		// 8 ��ӯ��ⵥ ��/�� ��/��
		if (Integer.valueOf(form.getOutIntoWhsTypId()) == 8) {
			// ��ȡ��
			subs.setSubjectDebit(new SubjectDebit(stockList.get(0).getSubjId(), "���", stockList.get(0).getSubjNm(),
					subs.getNoTaxAmt(), new BigDecimal(0), subs.getQty(), new BigDecimal(0), 1));
			// ��ȡ��
			subs.setSubjectCredit(new SubjectCredit(otherList.get(0).getSubjId(), "�Է�", otherList.get(0).getSubjNm(),
					subs.getNoTaxAmt(), new BigDecimal(0), subs.getQty(), new BigDecimal(0), 2));
		}

		// 9 �ɹ���ⵥ ��/�� ��/��/�շ����Ŀ�Ŀ
		if (Integer.valueOf(form.getOutIntoWhsTypId()) == 9) {
			// ��ȡ��
			subs.setSubjectDebit(new SubjectDebit(stockList.get(0).getSubjId(), "���", stockList.get(0).getSubjNm(),
					subs.getNoTaxAmt(), new BigDecimal(0), subs.getQty(), new BigDecimal(0), 1));

			if (form.getIsNtBllg() == 1) {
				// �ѿ�Ʊ
				// ��ȡ��
				subs.setSubjectCredit(
						new SubjectCredit(otherList.get(0).getSubjId(), "�Է�", otherList.get(0).getSubjNm(),
								subs.getNoTaxAmt(), new BigDecimal(0), subs.getQty(), new BigDecimal(0), 2));
			} else {
				// δ��Ʊ
				subs.setSubjectCredit(
						new SubjectCredit(otherList.get(0).getSubjId(), "Ӧ���ݹ�", otherList.get(0).getSubjNm(),
								subs.getNoTaxAmt(), new BigDecimal(0), subs.getQty(), new BigDecimal(0), 2));
			}

		}
		// 10 ���۳��ⵥ ��/��/�շ����Ŀ�Ŀ ��/��
		if (Integer.valueOf(form.getOutIntoWhsTypId()) == 10) {
			// ��ȡ��
			subs.setSubjectCredit(new SubjectCredit(stockList.get(0).getSubjId(), "���", stockList.get(0).getSubjNm(),
					subs.getNoTaxAmt(), new BigDecimal(0), subs.getQty(), new BigDecimal(0), 2));

			if (StringUtils.isNotEmpty(form.getSellTypId()) && Integer.valueOf(form.getSellTypId()) == 2) {

				// ��ȡ��
				subs.setSubjectDebit(
						new SubjectDebit(otherList.get(0).getSubjId(), "ί�д���", otherList.get(0).getSubjNm(),
								subs.getNoTaxAmt(), new BigDecimal(0), subs.getQty(), new BigDecimal(0), 1));

			} else if (StringUtils.isNotEmpty(form.getSellTypId()) && Integer.valueOf(form.getSellTypId()) == 1) {

				// ��������=1������Ŀ
				// ��ȡ��/�շ����Ŀ�Ŀ
				subs.setSubjectDebit(
						new SubjectDebit(otherList.get(0).getSubjId(), "������Ʒ", otherList.get(0).getSubjNm(),
								subs.getNoTaxAmt(), new BigDecimal(0), subs.getQty(), new BigDecimal(0), 1));
			}

		}
		// 11 �������ⵥ ��/�� ��/��
		if (Integer.valueOf(form.getOutIntoWhsTypId()) == 11) {

			// ��ȡ��
			subs.setSubjectDebit(new SubjectDebit(otherList.get(0).getSubjId(), "�Է�", otherList.get(0).getSubjNm(),
					subs.getNoTaxAmt(), new BigDecimal(0), subs.getQty(), new BigDecimal(0), 1));
			// ��ȡ��
			subs.setSubjectCredit(new SubjectCredit(stockList.get(0).getSubjId(), "���", stockList.get(0).getSubjNm(),
					subs.getNoTaxAmt(), new BigDecimal(0), subs.getQty(), new BigDecimal(0), 2));

		}
		// 12 ������ⵥ ��/�� ��/��
		if (Integer.valueOf(form.getOutIntoWhsTypId()) == 12) {

			// ��ȡ��
			subs.setSubjectDebit(new SubjectDebit(stockList.get(0).getSubjId(), "���", stockList.get(0).getSubjNm(),
					subs.getNoTaxAmt(), new BigDecimal(0), subs.getQty(), new BigDecimal(0), 1));
			// ��ȡ��
			subs.setSubjectCredit(new SubjectCredit(otherList.get(0).getSubjId(), "�Է�", otherList.get(0).getSubjNm(),
					subs.getNoTaxAmt(), new BigDecimal(0), subs.getQty(), new BigDecimal(0), 2));

		}
		return subs;
	}

	public List<FormBookEntry> countAvgUprc2(List<FormBookEntry> formList, Map<String, Object> dataMap, String accNum,
			String loginTime) {

		List<FormBookEntrySub> pursIntoList = new ArrayList<>();
		List<FormBookEntrySub> othList = new ArrayList<>();
		List<FormBookEntrySub> selList = new ArrayList<>();
		Map<String, FormBookEntry> formMap = new HashMap<>();
		dataMap.put("year", Integer.valueOf(loginTime.substring(0, 4)));
		dataMap.put("month", Integer.valueOf(loginTime.substring(5, 7)));
		dataMap.put("bookDt", loginTime);
		formList.forEach(form -> {

			form.getBookEntrySub().forEach(ss -> ss.setProjEncd(form.getCustId()));

			if (Integer.valueOf(form.getOutIntoWhsTypId()) == 9) {

				pursIntoList.addAll(form.getBookEntrySub());
			} else {
				othList.addAll(form.getBookEntrySub());
			}
			form.getBookEntrySub().clear();
			form.setIsNtBookOk(1);
			form.setBookOkAcc(accNum);
			form.setBookOkDt(loginTime);
			form.setIsNtMakeVouch(0);
			form.setIsMthEndStl(0);
			form.setIsNtUploadHead(0);
			if (!formMap.containsKey(form.getFormNum())) {
				formMap.put(form.getFormNum(), form);
			}

		});

		if (pursIntoList.size() > 0) {
			// �ɹ��ɱ�
			dataMap.put("pursIntoList", pursIntoList);
			List<PursInvSubTab> pursInvList = pursInvMasTabDao.countPursNoTaxAmtAndQtyList(dataMap);
			Map<String, PursInvSubTab> piMap = new HashMap<>();
			if (pursInvList.size() > 0) {

				pursInvList.forEach(pi -> {
					String key = pi.getInvtyEncd() + pi.getBatNum() + pi.getCrspdIntoWhsSnglNum();
					if (!piMap.containsKey(key)) {
						piMap.put(key, pi);
					}
				});
			}

			pursIntoList.forEach(sub -> {
				String key = sub.getInvtyEncd() + sub.getBatNum() + sub.getFormNum();
				if (piMap.containsKey(key)) {
					if (piMap.get(key).getNoTaxUprc() != null) {
						sub.setNoTaxUprc(piMap.get(key).getNoTaxUprc());
						sub.setNoTaxAmt(piMap.get(key).getNoTaxUprc().multiply(sub.getQty()));
					}

				}
				if (formMap.containsKey(sub.getFormNum())) {
					FormBookEntry form = formMap.get(sub.getFormNum());
					List<FormBookEntrySub> sls = form.getBookEntrySub();
					if (sls == null || sls.size() == 0) {
						sls = new ArrayList<>();
					}
					sls.add(sub);
					form.setBookEntrySub(sls);
					formMap.put(sub.getFormNum(), form);
				}
			});

		}

		if (othList.size() > 0) {
			// ����ɱ�
			dataMap.put("othList", othList);
			List<PursInvSubTab> othInvList = pursInvMasTabDao.countSellAndOthAmtList(dataMap);
			Map<String, PursInvSubTab> outMap = new HashMap<>();
			if (othInvList.size() > 0) {
				othInvList.forEach(pi -> {
					String key = pi.getWhsEncd() + pi.getInvtyEncd() + pi.getBatNum();
					if (!outMap.containsKey(key)) {
						outMap.put(key, pi);
					}
				});
			}

			othList.forEach(sub -> {
				String key = sub.getWhsEncd() + sub.getInvtyEncd() + sub.getBatNum();

				if (formMap.containsKey(sub.getFormNum())) {
					FormBookEntry form = formMap.get(sub.getFormNum());

					if (!CommonUtil.strToList("9,10").contains(form.getOutIntoWhsTypId())) {

						if (sub.getQty().compareTo(BigDecimal.ZERO) != 0
								&& sub.getNoTaxAmt().compareTo(BigDecimal.ZERO) != 0) {
							if (outMap.containsKey(key)) {
								if (outMap.get(key).getNoTaxUprc() != null) {
									sub.setNoTaxUprc(outMap.get(key).getNoTaxUprc());
									sub.setNoTaxAmt(outMap.get(key).getNoTaxUprc().multiply(sub.getQty()));
								}

							}
						}
					} else {
						if (outMap.containsKey(key)) {
							if (outMap.get(key).getNoTaxUprc() != null) {
								sub.setNoTaxUprc(outMap.get(key).getNoTaxUprc());
								sub.setNoTaxAmt(outMap.get(key).getNoTaxUprc().multiply(sub.getQty()));
							}

						}
					}

					List<FormBookEntrySub> sls = form.getBookEntrySub();
					if (sls == null || sls.size() == 0) {
						sls = new ArrayList<>();
					}
					sls.add(sub);
					form.setBookEntrySub(sls);
					formMap.put(sub.getFormNum(), form);
					System.out.println("---->" + sls.size());
					if (Integer.valueOf(form.getOutIntoWhsTypId()) == 10) {
						selList.add(sub);

					}

				}

			});
			if (selList.size() > 0) {
				List<List<FormBookEntrySub>> lists = Lists.partition(selList, 100);
				for (List<FormBookEntrySub> iList : lists) {
					sellOutWhsDao.updateCost(iList);
				}
			}
		}

		if (!formMap.isEmpty()) {

			formList = new ArrayList<>();
			for (Map.Entry<String, FormBookEntry> key : formMap.entrySet()) {
				FormBookEntry form = (FormBookEntry) key.getValue();
				formList.add(form);
			}
		}

		return formList;

	}

	@Transactional
	public Boolean addInvtyDetailBook1(List<FormBookEntry> formList, String loginTime, String accNum) throws Exception {

		Boolean isSuccess = true;
		String year = loginTime.substring(0, 4);
		String month = loginTime.substring(loginTime.indexOf("-") + 1, loginTime.lastIndexOf("-"));

		if (formList.size() != 0) {

			for (FormBookEntry form : formList) {

				if (form.getIsNtBookOk() == 1) {
					List<InvtyDetails> itsList = new ArrayList<>();

					for (FormBookEntrySub sub : form.getBookEntrySub()) {
						// ���Ҷ�Ӧ�Ĵ����ϸ
						Map<String, Object> paramMap = new HashMap<String, Object>();
						paramMap.put("invtyEncd", sub.getInvtyEncd());
						paramMap.put("whsEncd", sub.getWhsEncd());
						paramMap.put("batNum", sub.getBatNum());
						paramMap.put("invtyClsEncd", sub.getInvtyClsEncd());

						List<InvtyDetail> itList = invtyDetailDao.selectByInvtyDeatil(paramMap);

						InvtyDetail invty = new InvtyDetail();
						// ��������ڳ�
						InvtyMthTermBgnTab im = new InvtyMthTermBgnTab();

						Boolean isIt = true;
						Boolean isIs = true;

						if (itList.size() > 0) {
							invty = itList.get(0);
						} else {

							BeanUtils.copyProperties(invty, sub);

							int i = invtyDetailDao.insertInvtyDetail(invty);

						}

						InvtyDetails its = new InvtyDetails();

						// ��ѯ���º����ڳ�����
						List<InvtyMthTermBgnTab> termList = getMthList(sub, Integer.valueOf(year),
								Integer.valueOf(month));

						if (termList.size() > 0) {

							isIt = true;
							im = termList.get(0);
						} else {

							isIt = false;
							im = setMthValue(year, month, accNum, loginTime, BigDecimal.ZERO, BigDecimal.ZERO,
									BigDecimal.ZERO, form, sub);

						}

						BigDecimal othQty = new BigDecimal(0); // �������
						BigDecimal othMoeny = new BigDecimal(0.00); // �����
						BigDecimal taxUprc = new BigDecimal(0.00);// ��浥��

						im.setSendQty(Optional.ofNullable(im.getSendQty()).orElse(BigDecimal.ZERO));
						im.setSendMoeny(Optional.ofNullable(im.getSendMoeny()).orElse(BigDecimal.ZERO));
						im.setInQty(Optional.ofNullable(im.getInQty()).orElse(BigDecimal.ZERO));
						im.setInMoeny(Optional.ofNullable(im.getInMoeny()).orElse(BigDecimal.ZERO));
						im.setQty(Optional.ofNullable(im.getQty()).orElse(BigDecimal.ZERO));
						im.setAmt(Optional.ofNullable(im.getAmt()).orElse(BigDecimal.ZERO));

						if (CommonUtil.strToList("1,3,5,8,9,12").contains(form.getOutIntoWhsTypId())) {
							// ����
							its = new InvtyDetails(loginTime, null, null, form.getOutIntoWhsTypId(),
									form.getOutIntoWhsTypNm(), sub.getNoTaxUprc(), sub.getQty(), sub.getNoTaxAmt(),
									null, null, null, taxUprc, othQty, othMoeny, form.getFormNum(), invty.getDetailId(),
									sub.getOrdrNum());

							im.setInMoeny(im.getInMoeny().add(sub.getNoTaxAmt()));
							im.setInQty(im.getInQty().add(sub.getQty()));
							if (im.getInQty().compareTo(BigDecimal.ZERO) == 0) {
								im.setInUnitPrice(new BigDecimal(0.00000000));
							} else {

								im.setInUnitPrice(
										im.getInMoeny().divide(im.getInQty(), 8, BigDecimal.ROUND_HALF_UP).abs());
							}
						}

						if (CommonUtil.strToList("2,4,6,7,10,11").contains(form.getOutIntoWhsTypId())) {
							// ����

							its = new InvtyDetails(loginTime, null, null, form.getOutIntoWhsTypId(),
									form.getOutIntoWhsTypNm(), null, null, null, sub.getNoTaxUprc(), sub.getQty(),
									sub.getNoTaxAmt(), taxUprc, othQty, othMoeny, form.getFormNum(),
									invty.getDetailId(), sub.getOrdrNum());
							im.setSendMoeny(im.getSendMoeny().add(sub.getNoTaxAmt()));
							im.setSendQty(im.getSendQty().add(sub.getQty()));
							if (im.getSendQty().compareTo(BigDecimal.ZERO) == 0) {
								im.setSendUnitPrice(new BigDecimal(0.00000000));
							} else {
								im.setSendUnitPrice(
										im.getSendMoeny().divide(im.getSendQty(), 8, BigDecimal.ROUND_HALF_UP).abs());
							}
						}

						// ��浥�� = (�����ڳ����+����������-���µķ���)/(�����ڳ�����+������������-����)
						othQty = im.getQty().add(im.getInQty()).subtract(im.getSendQty());
						othMoeny = im.getAmt().add(im.getInMoeny()).subtract(im.getSendMoeny());
						if (othQty.compareTo(BigDecimal.ZERO) == 0) {
							taxUprc = new BigDecimal(0.00000000);
						} else {
							taxUprc = othMoeny.divide(othQty, 8, BigDecimal.ROUND_HALF_UP).abs();
						}
						its.setOthMoeny(othMoeny);
						its.setOthQty(othQty);
						its.setOthUnitPrice(taxUprc);
						itsList.add(its);

						// ���½������
						im.setOthMoeny(othMoeny);
						im.setOthUnitPrice(taxUprc);
						im.setOthQty(othQty);

						// ��������ڳ� ������Ʒ�����ڳ�
						if (!isIt) {
							// insert �����ڳ�
							invtyMthTermBgnTabDao.insertMth(im);
						} else {
							invtyMthTermBgnTabDao.updateMth(im);

						}

					}
					// insert ��ϸ
					if (itsList.size() != 0) {

						invtyDetailDao.insertInvtyDetailsList(itsList);
						isSuccess = true;
					}
				} else {
					isSuccess = false;
					logger.info("���ݺ�:{}δ����,�޷�������ϸ�˴���", form.getFormNum());
				}
			}
		}
		return isSuccess;
	}

	public boolean dealSendProductMth(List<FormBookEntry> formList, String loginTime, String accNum) {

		String year = loginTime.substring(0, 4);
		String month = loginTime.substring(5, 7);
		boolean isSuccess = false;
		if (formList.size() > 0) {
			for (FormBookEntry form : formList) {
				if (10 == Integer.valueOf(form.getOutIntoWhsTypId())) {
					for (FormBookEntrySub sub : form.getBookEntrySub()) {
						// ������Ʒ�ڳ�����
						List<InvtyMthTermBgnTab> termSendList = getSendMthList(sub, Integer.valueOf(year),
								Integer.valueOf(month), form.getCustId());
						// ������Ʒ�����ڳ�
						InvtyMthTermBgnTab imSend = new InvtyMthTermBgnTab();

						Boolean isIt = true;
						Boolean isIs = true;

						if (termSendList.size() > 0) {
							isIs = true;
							imSend = termSendList.get(0);
						} else {
							isIs = false;
							imSend = setMthValue(year, month, accNum, loginTime, BigDecimal.ZERO, BigDecimal.ZERO,
									BigDecimal.ZERO, form, sub);
						}

						BigDecimal othQty = new BigDecimal(0); // �������
						BigDecimal othMoeny = new BigDecimal(0.00); // �����
						BigDecimal taxUprc = new BigDecimal(0.00);// ��浥��

						// ������Ʒ���뷢������
						imSend = setSendMth(imSend, form, sub);

						// ���
						BigDecimal othSendQty = imSend.getQty().add(imSend.getInQty()).subtract(imSend.getSendQty());
						BigDecimal othSendMoeny = imSend.getAmt().add(imSend.getInMoeny())
								.subtract(imSend.getSendMoeny());
						BigDecimal uprc = new BigDecimal(0);
						if (othSendQty.compareTo(BigDecimal.ZERO) == 0) {
							uprc = new BigDecimal(0.00000000);
						} else {
							uprc = othSendMoeny.divide(othSendQty, 8, BigDecimal.ROUND_HALF_UP).abs();
						}
						imSend.setOthMoeny(othSendMoeny);
						imSend.setOthQty(othSendQty);
						imSend.setOthUnitPrice(uprc);
						if (!isIs) {
							invtySendMthTermBgnTabDao.insertSendMth(imSend);
							isSuccess = true;
						} else {
							invtySendMthTermBgnTabDao.updateSendMth(imSend);
							isSuccess = true;
						}
					}
				}
			}
		}
		return isSuccess;
	}

	/**
	 * ������Ʒ�����ڳ�
	 */
	private InvtyMthTermBgnTab setSendMth(InvtyMthTermBgnTab imSend, FormBookEntry form, FormBookEntrySub sub) {
		imSend.setSendQty(Optional.ofNullable(imSend.getSendQty()).orElse(new BigDecimal(0.00000000)));
		imSend.setSendMoeny(Optional.ofNullable(imSend.getSendMoeny()).orElse(new BigDecimal(0.00000000)));
		imSend.setInQty(Optional.ofNullable(imSend.getInQty()).orElse(new BigDecimal(0.00000000)));
		imSend.setInMoeny(Optional.ofNullable(imSend.getInMoeny()).orElse(new BigDecimal(0.00000000)));
		Map map = new HashMap<>();
		map.put("invtyEncd", sub.getInvtyEncd());
		map.put("whsEncd", sub.getWhsEncd());
		map.put("batNum", sub.getBatNum());
		map.put("outWhsId", form.getFormNum());
		// map.put("custId", form.getCustId());
		map.put("bookDt", form.getFormDt());
		map.put("toOrdrNum", sub.getToOrdrNum());

		List<SellComnInvSub> sList = sellComnInvDao.selectSellComnInvToQty(map);
		BigDecimal openQty = BigDecimal.ZERO;
		if (sList.size() > 0) {
			openQty = sList.get(0).getQty();
		}
		if (openQty != null) {
			if (openQty.compareTo(sub.getQty()) == 0) {
				imSend.setSendMoeny(imSend.getSendMoeny().add(sub.getNoTaxAmt()));
				imSend.setSendQty(imSend.getSendQty().add(sub.getQty()));
			} else {
				imSend.setSendMoeny(imSend.getSendMoeny().add(sub.getNoTaxUprc().multiply(openQty)));
				imSend.setSendQty(imSend.getSendQty().add(openQty));
			}

			if (imSend.getSendQty().compareTo(BigDecimal.ZERO) == 0) {
				imSend.setSendUnitPrice(new BigDecimal(0.00000000));
			} else {

				imSend.setSendUnitPrice(
						imSend.getSendMoeny().divide(imSend.getSendQty(), 8, BigDecimal.ROUND_HALF_UP).abs());
			}
		}

		// ����
		imSend.setInMoeny(imSend.getInMoeny().add(sub.getNoTaxAmt()));
		imSend.setInQty(imSend.getInQty().add(sub.getQty()));
		if (imSend.getInQty().compareTo(BigDecimal.ZERO) == 0) {
			imSend.setInUnitPrice(new BigDecimal(0.00000000));
		} else {
			imSend.setInUnitPrice(imSend.getInMoeny().divide(imSend.getInQty(), 8, BigDecimal.ROUND_HALF_UP).abs());
		}

		imSend.setCustId(form.getCustId());

		return imSend;
	}

	/**
	 * ��ѯ������Ʒ�����ڳ�
	 */
	private List<InvtyMthTermBgnTab> getSendMthList(FormBookEntrySub sub, Integer year, Integer month, String custId) {
		Map<String, Object> map = putParamMap(sub, year, month);
		map.put("custId", custId);
		List<InvtyMthTermBgnTab> tabList = invtySendMthTermBgnTabDao.selectSendMthByMthTerm(map);
		return tabList;

	}

	/**
	 * ��ѯ���´����ϸ
	 */
	private List<InvtyDetail> getInvtyDetailList(FormBookEntrySub sub, String bookDt) {

		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("invtyClsEncd", sub.getInvtyClsEncd());
		paramMap.put("invtyEncd", sub.getInvtyEncd());
		paramMap.put("whsEncd", sub.getWhsEncd());
		paramMap.put("batNum", sub.getBatNum());
		paramMap.put("bookOkDt", bookDt);
		paramMap.put("formNum", sub.getFormNum());
		return invtyDetailDao.selectByInvty(paramMap);
	}

	/**
	 * ��ѯ�����ڳ�
	 */
	private List<InvtyMthTermBgnTab> getMthList(FormBookEntrySub sub, int year, int month) {

		List<InvtyMthTermBgnTab> tabList = invtyMthTermBgnTabDao.selectByMthTerm(putParamMap(sub, year, month));

		return tabList;
	}

	/**
	 * ��װmapֵ
	 */
	private Map<String, Object> putParamMap(FormBookEntrySub sub, int year, int month) {

		Map<String, Object> paramMap = new HashMap<>();

		paramMap.put("invtyClsEncd", sub.getInvtyClsEncd());
		paramMap.put("invtyEncd", sub.getInvtyEncd());
		paramMap.put("whsEncd", sub.getWhsEncd());
		paramMap.put("batNum", sub.getBatNum());
		paramMap.put("year", year);
		paramMap.put("month", month);
		return paramMap;
	}

	/**
	 * �洢�����ڳ�ֵ
	 */
	private InvtyMthTermBgnTab setMthValue(String acctYr, String acctiMth, String accNum, String logintTime,
			BigDecimal qty, BigDecimal unit, BigDecimal money, FormBookEntry form, FormBookEntrySub sub) {
		InvtyMthTermBgnTab mth = new InvtyMthTermBgnTab();
		try {
			BigDecimalConverter bd = new BigDecimalConverter(BigDecimal.ZERO);
			ConvertUtils.register(bd, java.math.BigDecimal.class);
			BeanUtils.copyProperties(mth, sub);
		} catch (Exception e) {
			e.printStackTrace();
		}
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		mth.setAccNum(accNum);
		mth.setCtime(sf.format(new Date()).toString());
		mth.setAccTime(logintTime);
		mth.setMakeVouchAbst("�ڳ����");
		mth.setAcctYr(acctYr);
		mth.setAcctiMth(acctiMth);
		mth.setQty(qty);
		mth.setUprc(unit);
		mth.setAmt(money);
		mth.setIsFinalDeal(0); // �Ƿ���ĩ����
		mth.setIsMthEndStl(0); // �Ƿ���ĩ����
		mth.setCustId(form.getCustId());
		return mth;
	}

	@Override
	@Transactional
	public String finalTermBgnBook(Map map, String accNum, String loginTime) throws Exception {
		String message = "";
		String resp = "";
		Boolean isSuccess = true;
		map.put("isBookOk", "0");
		String year = loginTime.substring(0, 4);
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String month = loginTime.substring(loginTime.indexOf("-") + 1, loginTime.lastIndexOf("-"));
		List<TermBgnBal> termBgnBalList = termBgnBalDao.selectBgnBalList(map);
		List<TermBgnBal> balList = new ArrayList<>();

		if (termBgnBalList.size() == 0) {
			message = "��ѯδ�����ڳ�����Ϊ��,��ѯ����:" + loginTime;
			isSuccess = false;
		} else {
			List<InvtyMthTermBgnTab> mthList = new ArrayList<>();
			for (TermBgnBal bal : termBgnBalList) {
				InvtyMthTermBgnTab mth = new InvtyMthTermBgnTab();
				FormBookEntrySub sub = new FormBookEntrySub();

				BeanUtils.copyProperties(mth, bal);
				// mth.setWhsEncd(bal.getWhsEncd());
				// mth.setInvtyEncd(bal.getInvtyEncd());
				// ��ѯ�����Ϣ
				InvtyDoc invtyDoc = invtyDocDao.selectInvtyDocByInvtyDocEncd(mth.getInvtyEncd());
				mth.setInvtyNm(invtyDoc.getInvtyNm());// �������
				mth.setBxRule(invtyDoc.getBxRule());// ���
				mth.setMeasrCorpNm(invtyDoc.getMeasrCorpNm());// ������λ
				mth.setInvtyClsEncd(invtyDoc.getInvtyClsEncd()); // �������
				mth.setCtime(sf.format(new Date()).toString());
				mth.setAccTime(loginTime);
				mth.setMakeVouchAbst("�ڳ����");
				mth.setAcctYr(year);
				mth.setAcctiMth(month);
				mth.setQty(bal.getQty());
				mth.setUprc(bal.getNoTaxUprc());
				mth.setAmt(bal.getNoTaxAmt());
				mth.setOthQty(bal.getQty());
				mth.setOthUnitPrice(bal.getNoTaxUprc());
				mth.setOthMoeny(bal.getNoTaxAmt());
				mth.setIsFinalDeal(0); // �Ƿ���ĩ����
				mth.setIsMthEndStl(0); // �Ƿ���ĩ����

				mthList.add(mth);

				bal.setIsNtBookEntry(1);
				bal.setBookEntryPers(accNum);
				bal.setBookEntryTm(sf.format(new Date()));
				balList.add(bal);

			}

			if (mthList.size() > 0) {
				othOutDao.insertInvtyTabTermInvty(loginTime);

				int m = invtyMthTermBgnTabDao.insertMthList(mthList);
				int t = termBgnBalDao.updateTermIsBookOk(balList);

				message = "���˳ɹ�";
				isSuccess = true;
				logger.info("�ڳ������˳ɹ�,������:{},��¼ʱ��:{},����ʱ��:{}", accNum, loginTime, sf.format(new Date()));

			}

		}
		resp = BaseJson.returnRespObj("account/form/final/termBgnBook", isSuccess, message, null);
		return resp;
	}

	@Override
	@Transactional
	public String finalTermBgnBackBook(Map map, String accNum, String loginTime) throws Exception {
		String message = "";
		String resp = "";
		Boolean isSuccess = true;
		map.put("isBookOk", "1");
		List<TermBgnBal> termList = new ArrayList<>();
		List<InvtyMthTermBgnTab> mthList = new ArrayList<>();

		String year = loginTime.substring(0, 4);
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String month = loginTime.substring(loginTime.indexOf("-") + 1, loginTime.lastIndexOf("-"));
		List<TermBgnBal> termBgnBalList = termBgnBalDao.selectBgnBalList(map);

		if (termBgnBalList.size() == 0) {
			message = "��ѯ�Ѽ����ڳ�����Ϊ��,��ѯ����:" + loginTime;
			isSuccess = false;
		} else {

			for (TermBgnBal bal : termBgnBalList) {
				map.put("invtyEncd", bal.getInvtyEncd());
				map.put("whsEncd", bal.getWhsEncd());
				map.put("year", year);
				map.put("month", month);
				map.put("isFinalDeal", "0");
				map.put("isMthEndStl", "0");

				mthList.addAll(0, invtyMthTermBgnTabDao.selectMthTermList(map));
				bal.setIsNtBookEntry(0);
				bal.setBookEntryPers(" ");
				bal.setBookEntryTm("0000-00-00 00:00:00");

				termList.add(bal);

			}

			if (mthList.size() == 0) {
				message = "�����ڳ�����Ѵ���,�޷��ָ�����";
				isSuccess = false;
			} else {
				invtyMthTermBgnTabDao.deleteMthList(mthList);
				termBgnBalDao.updateTermIsBookOk(termList);
				othOutDao.deleteInvtyTabTerm(loginTime);
				message = "�ָ��ɹ�";
				isSuccess = true;
				logger.info("�ڳ������˳ɹ�,������:{},��¼ʱ��:{},����ʱ��:{}", accNum, loginTime, sf.format(new Date()));

			}

		}
		resp = BaseJson.returnRespObj("account/form/final/termBgnBackBook", isSuccess, message, null);
		return resp;
	}

	/**
	 * ����Ƿ��ڽ�������
	 */
	private boolean checkOutIsEndOfMonth(String loginTime) {

		boolean isIn = false;

		Map map = new HashMap<>();

		if (loginTime.length() > 7) {
			int year = Integer.valueOf(loginTime.substring(0, 4));
			int month = Integer.valueOf(loginTime.substring(5, 7));

			// �ж��ϸ����Ƿ��ѽ���
			if (month == 1) {
				map.put("year", year - 1);
				map.put("month", 12);
			} else {
				map.put("year", year);
				map.put("month", month - 1);
			}
			List<MthEndStl> mthList = mthEndStlDao.selectByMap(map);
			if (mthList.size() > 0) {
				MthEndStl mth = mthList.get(0);
				if (mth.getIsMthEndStl() == 1) {
					isIn = checkIsInNowMonth(map, year, month);
				}

			} else {
				isIn = checkIsInNowMonth(map, year, month);
			}

		}
		return isIn;
	}

	private boolean checkIsInNowMonth(Map map, int year, int month) {
		// �жϱ����Ƿ����
		map.put("year", year);
		map.put("month", month);
		map.put("isMthEndStl", "0");
		List<MthEndStl> mthList = mthEndStlDao.selectByMap(map);
		if (mthList.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * ���ݴ����ѯ�����ݹ����ĵ���
	 * 
	 * @param loginTime
	 * @param accNum
	 * @throws Exception
	 */
	private void getTempIntoWhs(String loginTime, String accNum, List<String> invtyEncdList) throws Exception {
		Map<String, Object> map = new HashMap<>();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		map.put("isNtBookOk", "1");
		map.put("nowTime", loginTime);
		map.put("outIntoWhsTypId", "9"); // �ɹ���ⵥ
		map.put("ntBllg", "0");// δ��Ʊ
		map.put("invIdList", invtyEncdList);
		List<FormBookEntry> formList = formBookEntryDao.selectMap(map);
		FormBackFlush backFlush = new FormBackFlush();
		List<FormBackFlush> formBackList = new ArrayList<>();
		List<FormBackFlushSub> subList = new ArrayList<>();
		List<InvtyDetails> itsList = new ArrayList<>();
		int year = Integer.valueOf(loginTime.substring(0, 4));
		int month = Integer.valueOf(loginTime.substring(5, 7));
		if (formList.size() > 0) {
			for (FormBookEntry form : formList) {
				map.put("formNum", form.getFormNum());
				map.put("isRedBack", "1");
				// �жϱ����Ƿ��Ѿ����ɶ�Ӧ���ֻس嵥
				List<FormBackFlush> backList = formBackFlushDao.selectIntoWhsByMap(map);

				if (backList.size() == 0) {
					backFlush = new FormBackFlush();
					BeanUtils.copyProperties(backFlush, form);
					String backNum = getOrderNo.getSeqNo("BFRED", accNum, loginTime);
					backFlush.setBackNum(backNum); // ���ֻس嵥����
					backFlush.setBookOkDt(getFirstDayOfNextMonth(loginTime, "yyyy-MM-dd HH:mm:ss")); // �¸���1��

					backFlush.setBackDt(loginTime);
					backFlush.setBookOkAcc(accNum);
					backFlush.setIsRedBack(1);
					for (FormBookEntrySub sub : form.getBookEntrySub()) {
						FormBackFlushSub backs = new FormBackFlushSub();
						// ��Ŀ�Ƶ� �� �����Ŀ (����) �� Ӧ���˿�-�ݹ�Ӧ���� (����)
						List<AcctItmDoc> accList = getInvtySub(form.getSellTypId(), sub.getInvtyClsEncd());
						List<AcctItmDoc> tempList = getTempSub();
						sub.setQty(sub.getQty().negate());
						sub.setNoTaxAmt(sub.getNoTaxAmt().negate());
						// ��ȡ��
						sub.setSubjectDebit(
								new SubjectDebit(accList.get(0).getSubjId(), "���", accList.get(0).getSubjNm(),
										sub.getNoTaxAmt(), new BigDecimal(0), sub.getQty(), new BigDecimal(0), 1));
						sub.setSubjectCredit(
								new SubjectCredit(tempList.get(0).getSubjId(), "Ӧ���ݹ�", tempList.get(0).getSubjNm(),
										sub.getNoTaxAmt(), new BigDecimal(0), sub.getQty(), new BigDecimal(0), 2));

						BeanUtils.copyProperties(backs, sub);
						backs.setBackNum(backNum);
						// ���Ҷ�Ӧ�Ĵ����ϸ
						Map<String, Object> paramMap = new HashMap<String, Object>();
						paramMap.put("invtyEncd", sub.getInvtyEncd());
						paramMap.put("whsEncd", sub.getWhsEncd());
						paramMap.put("batNum", sub.getBatNum());
						paramMap.put("invtyClsEncd", sub.getInvtyClsEncd());

						List<InvtyDetail> itList = invtyDetailDao.selectByInvtyDeatil(paramMap);
						List<InvtyMthTermBgnTab> tabList = getMthList(sub, year, month);
						InvtyMthTermBgnTab tab = new InvtyMthTermBgnTab();
						if (tabList.size() > 0) {
							tab = tabList.get(0);
						} else {
							tab.setQty(BigDecimal.ZERO);
							tab.setAmt(BigDecimal.ZERO);

						}
						InvtyDetail invty = new InvtyDetail();
						if (itList.size() > 0) {
							invty = itList.get(0);
						}
						BigDecimal othQty = tab.getQty().add(sub.getQty());
						BigDecimal othMoeny = tab.getAmt().add(sub.getNoTaxAmt());
						BigDecimal taxUprc = new BigDecimal(0.000000);
						if (othQty.compareTo(BigDecimal.ZERO) != 0) {
							taxUprc = othMoeny.divide(othQty, 8, BigDecimal.ROUND_HALF_UP);
						}
						InvtyDetails its = new InvtyDetails();
						// ����
						its = new InvtyDetails(loginTime, null, null, "13", "���ֻس嵥", sub.getNoTaxUprc(), sub.getQty(),
								sub.getNoTaxAmt(), null, null, null, taxUprc, othQty, othMoeny, backNum,
								invty.getDetailId(), sub.getOrdrNum());
						itsList.add(its);
						subList.add(backs);
					}
					backFlush.setFormTypEncd("016");
					backFlush.setFormTypName("���ֻس嵥");
					backFlush.setFormBackFlushSub(subList);
					formBackList.add(backFlush);

				}
			}
			if (formBackList.size() > 0) {
				// ���ɱ��º��ֻس嵥
				formBackFlushDao.insertRedFormBackFlushList(formBackList);
				formBackFlushSubDao.insertRedFormBackFlushSubList(subList);
				// ������ϸ��
				invtyDetailDao.insertInvtyDetailsList(itsList);
			}
		}
	}

	/**
	 * �����µĺ��ֻس嵥
	 * 
	 * @throws Exception
	 */
	private void dealFormBackFlush(String loginTime, String accNum, List<String> invtyEncdList) throws Exception {
		Map<String, Object> map = new HashMap<>();
		map.put("loginTime", loginTime);
		map.put("isRedBack", "1"); // �Ƿ����
		map.put("invIdList", invtyEncdList);
		int year = Integer.valueOf(loginTime.substring(0, 4));
		int month = Integer.valueOf(loginTime.substring(5, 7));
		// ��ѯ���µĺ��ֻس嵥
		List<FormBackFlush> backRedList = formBackFlushDao.selectIntoWhsByMap(map);

		if (backRedList.size() != 0) {
			FormBackFlush blueFlush = new FormBackFlush();
			FormBackFlush redFlush = new FormBackFlush();
			List<FormBackFlush> blueList = new ArrayList<>();
			List<FormBackFlush> redList = new ArrayList<>();
			List<FormBackFlushSub> subList = new ArrayList<>();
			List<FormBackFlushSub> redSubList = new ArrayList<>();
			List<InvtyDetails> itsList = new ArrayList<>();

			for (FormBackFlush back : backRedList) {
				// ��ѯ��Ʊ�������߽��
				map.put("formNum", back.getFormNum());
				map.put("bookDt", loginTime);
				blueFlush = new FormBackFlush();
				redFlush = new FormBackFlush();
				String backBlueNum = getOrderNo.getSeqNo("BFBLUE", accNum, loginTime);
				String redBackNum = getOrderNo.getSeqNo("BFRED", accNum, loginTime);

				for (FormBackFlushSub sub : back.getFormBackFlushSub()) {
					map.put("invtyEncd", sub.getInvtyEncd());
					map.put("batNum", sub.getBatNum());
					map.put("whsEncd", sub.getWhsEncd());
					map.put("toOrdrNum", sub.getToOrdrNum());
					// ��ѯ�ɹ���ⵥԭ���Ŀ�Ʊ����
					List<IntoWhs> intoList = intoWhsDao.selectIntoWhsByInvty(map);
					FormBackFlushSub backBlue = new FormBackFlushSub();
					FormBackFlushSub backRed = new FormBackFlushSub();
					if (intoList.size() != 0) {
						IntoWhs into = intoList.get(0);
						if (into.getIntoWhsSub().size() != 0) {
							IntoWhsSub whsSub = into.getIntoWhsSub().get(0);
							if (whsSub.getUnBllgQty().compareTo(BigDecimal.ZERO) != 0) {
								// ���ֿ�Ʊ
								// δ��Ʊ
								BeanUtils.copyProperties(backBlue, sub);
								// ���ɱ������ֻس嵥
								backBlue.setQty(whsSub.getUnBllgQty());
								backBlue.setNoTaxAmt(whsSub.getNoTaxAmt());
								backBlue.setBackNum(backBlueNum);

								sub.setQty(whsSub.getUnBllgQty().negate());
								sub.setNoTaxAmt(whsSub.getNoTaxAmt().negate());

								sub.setBackNum(redBackNum); // �����¸��µĺ��ֻس嵥
								itsList = setBackFlushInvtyDetail(sub, year, month, loginTime, itsList, redBackNum,
										"13", "���ֻس嵥");
								itsList = setBackFlushInvtyDetail(backBlue, year, month, loginTime, itsList,
										backBlueNum, "14", "���ֻس嵥");
								redSubList.add(sub);
								subList.add(backBlue);

							} // ȫ����Ʊ
						} else {
							// δ��Ʊ
							BeanUtils.copyProperties(backBlue, sub);
							// ���ɱ������ֻس嵥
							backBlue.setQty(sub.getQty().negate());
							backBlue.setNoTaxAmt(sub.getNoTaxAmt().negate());
							backBlue.setBackNum(backBlueNum);

							sub.setBackNum(redBackNum); // �����¸��µĺ��ֻس嵥
							itsList = setBackFlushInvtyDetail(sub, year, month, loginTime, itsList, redBackNum, "13",
									"���ֻس嵥");
							itsList = setBackFlushInvtyDetail(backBlue, year, month, loginTime, itsList, backBlueNum,
									"14", "���ֻس嵥");
							redSubList.add(sub);
							subList.add(backBlue);
						}
					} else {
						// δ��Ʊ
						BeanUtils.copyProperties(backBlue, sub);
						// ���ɱ������ֻس嵥
						backBlue.setQty(sub.getQty().negate());
						backBlue.setNoTaxAmt(sub.getNoTaxAmt().negate());
						backBlue.setBackNum(backBlueNum);

						sub.setBackNum(redBackNum); // �����¸��µĺ��ֻس嵥
						itsList = setBackFlushInvtyDetail(sub, year, month, loginTime, itsList, redBackNum, "13",
								"���ֻس嵥");
						itsList = setBackFlushInvtyDetail(backBlue, year, month, loginTime, itsList, backBlueNum, "14",
								"���ֻس嵥");
						redSubList.add(sub);
						subList.add(backBlue);
					}

				}
				if (subList.size() > 0) {
					BeanUtils.copyProperties(blueFlush, back);
					blueFlush.setBackNum(backBlueNum); // ���ֻس嵥����
					blueFlush.setBookOkDt(loginTime);
					blueFlush.setBookOkAcc(accNum);
					blueFlush.setIsRedBack(0);
					blueFlush.setFormBackFlushSub(subList);
					blueFlush.setFormTypEncd("017");
					blueFlush.setFormTypName("���ֻس嵥");
					blueList.add(blueFlush);

				}
				if (redSubList.size() > 0) {
					BeanUtils.copyProperties(redFlush, back);
					redFlush.setBackNum(redBackNum); // ���ֻس嵥����
					redFlush.setBookOkDt(getFirstDayOfNextMonth(loginTime, "yyyy-MM-dd HH:mm:ss")); // �¸���1��
					redFlush.setBookOkAcc(accNum);
					redFlush.setIsRedBack(1);
					redFlush.setFormBackFlushSub(redSubList);
					redFlush.setFormTypEncd("016");
					redFlush.setFormTypName("���ֻس嵥");
					redList.add(redFlush);
				}

			}
			if (redList.size() > 0) {

				formBackFlushDao.insertRedFormBackFlushList(redList);
				formBackFlushSubDao.insertRedFormBackFlushSubList(redSubList);

			}
			if (itsList.size() > 0) {
				invtyDetailDao.insertInvtyDetailsList(itsList);
			}
			if (blueList.size() > 0) {
				formBackFlushDao.insertRedFormBackFlushList(blueList);
				formBackFlushSubDao.insertRedFormBackFlushSubList(subList);
			}
		}
	}

	/**
	 * ������ϸ��
	 * 
	 * @param sub       �����ϸ�ӱ�
	 * @param year      ��¼��
	 * @param month     ��¼��
	 * @param loginTime ��¼ʱ��
	 * @param itsList   �����ϸ�˼���
	 * @param backNum   �س嵥��
	 */
	private List<InvtyDetails> setBackFlushInvtyDetail(FormBackFlushSub sub, int year, int month, String loginTime,
			List<InvtyDetails> itsList, String backNum, String formTypeId, String formTypeName) {
		// ���Ҷ�Ӧ�Ĵ����ϸ
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("invtyEncd", sub.getInvtyEncd());
		paramMap.put("whsEncd", sub.getWhsEncd());
		paramMap.put("batNum", sub.getBatNum());
		paramMap.put("invtyClsEncd", sub.getInvtyClsEncd());

		List<InvtyDetail> itList = invtyDetailDao.selectByInvtyDeatil(paramMap);
		List<InvtyMthTermBgnTab> tabList = getBakcMthList(sub, year, month);
		InvtyMthTermBgnTab tab = new InvtyMthTermBgnTab();
		if (tabList.size() > 0) {
			tab = tabList.get(0);
		} else {
			tab.setQty(BigDecimal.ZERO);
			tab.setAmt(BigDecimal.ZERO);
		}
		InvtyDetail invty = new InvtyDetail();
		if (itList.size() > 0) {
			invty = itList.get(0);
		}
		BigDecimal othQty = tab.getQty().add(sub.getQty());
		BigDecimal othMoeny = tab.getAmt().add(sub.getNoTaxAmt());
		BigDecimal taxUprc = new BigDecimal(0.000000);
		if (othQty.compareTo(BigDecimal.ZERO) != 0) {
			taxUprc = othMoeny.divide(othQty, 8, BigDecimal.ROUND_HALF_UP);
		}
		InvtyDetails its = new InvtyDetails();
		// ����
		its = new InvtyDetails(loginTime, null, null, formTypeId, formTypeName, sub.getNoTaxUprc(), sub.getQty(),
				sub.getNoTaxAmt(), null, null, null, taxUprc, othQty, othMoeny, backNum, invty.getDetailId(),
				sub.getOrdrNum());
		itsList.add(its);

		return itsList;
	}

	/**
	 * �ָ�����-�ָ������ɺ��ֻس嵥�Ĳɹ���ⵥ
	 */
	private boolean recoverBackFlushRed(String loginTime, String accNum, List<String> invtyEncdList) {
		Map<String, Object> map = new HashMap<>();
		map.put("isNtBookOk", "1");
		map.put("nowTime", loginTime);
		map.put("outIntoWhsTypId", "9"); // �ɹ���ⵥ
		map.put("ntBllg", "0");// δ��Ʊ����
		map.put("invIdList", invtyEncdList);
		int year = Integer.valueOf(loginTime.substring(0, 4));
		int month = Integer.valueOf(loginTime.substring(5, 7));
		boolean isSuccess = false;

		List<FormBookEntry> formList = formBookEntryDao.selectMap(map); // ���µĲɹ���ⵥ-�ݹ�
		if (formList.size() > 0) {
			List<String> formNumList = new ArrayList<>();
			List<InvtyDetails> itsList = new ArrayList<>();

			for (FormBookEntry book : formList) {
				formNumList.add(book.getFormNum());
			}
			map.clear();
			map.put("backNumList", formNumList);
			map.put("isRedBack", "1");
			map.put("bookOkDt", getFirstDayOfNextMonth(loginTime, "yyyy-MM-dd HH:mm:ss"));
			// ��ѯ��ɾ�����ɵ����º��ֻس嵥
			List<FormBackFlush> backRedList = formBackFlushDao.selectIntoWhsByMap(map);

			if (backRedList.size() > 0) {
				formNumList.clear();
				map.clear();
				for (FormBackFlush back : backRedList) {
					formNumList.add(back.getBackNum());
				}
				map.put("backNumList", formNumList);
				// ��ѯ��ɾ����Ӧ����ϸ��
				List<InvtyDetail> itList = invtyDetailDao.selectByInvtyDeatilsList(map);
				for (InvtyDetail it : itList) {
					if (it.getInvtyDetailsList().size() > 0) {

						for (InvtyDetails its : it.getInvtyDetailsList()) {
							itsList.add(its);
						}
					}
				}
				if (itsList.size() > 0) {
					invtyDetailDao.delectInvtyList(itsList);
				}
				formBackFlushDao.delectFormBackFlushAndSub(backRedList);
				isSuccess = true;
			}
		}
		return isSuccess;
	}

	/**
	 * �ָ�����-�ָ��ѱ����Ļس嵥
	 */
	private void recoverBackFlush(String loginTime, String accNum, List<String> invtyEncdList) {

		boolean isSuccess = recoverBackFlushRed(loginTime, accNum, invtyEncdList);

		int year = Integer.valueOf(loginTime.substring(0, 4));
		int month = Integer.valueOf(loginTime.substring(5, 7));
		List<String> formNumList = new ArrayList<>();
		Map<String, Object> map = new HashMap<>();
		List<InvtyDetails> itsList = new ArrayList<>();

		map.put("isRedBack", "0");
		map.put("bookOkDt", loginTime);
		map.put("invtyEncd", "");
		map.put("invIdList", invtyEncdList);
		// ��ѯ��ɾ���������ɵ����ֻس嵥
		List<FormBackFlush> backBlueList = formBackFlushDao.selectIntoWhsByMap(map);
		if (backBlueList.size() > 0) {

			// ��ѯ��ɾ����Ӧ����ϸ��
			for (FormBackFlush back : backBlueList) {
				formNumList.add(back.getBackNum());
			}
			formBackFlushDao.delectFormBackFlushAndSub(backBlueList);
		}
		map.put("bookOkDt", getFirstDayOfNextMonth(loginTime, "yyyy-MM-dd HH:mm:ss"));
		map.put("isRedBack", "1");
		// ��ѯ��ɾ�����µĺ��ֻس嵥
		List<FormBackFlush> backRedList = formBackFlushDao.selectIntoWhsByMap(map);
		if (backRedList.size() > 0) {
			for (FormBackFlush back : backRedList) {
				formNumList.add(back.getBackNum());
			}

			formBackFlushDao.delectFormBackFlushAndSub(backRedList);
		}
		map.clear();
		map.put("backNumList", formNumList);
		// ��ѯ��ɾ����Ӧ����ϸ��
		List<InvtyDetail> itList = invtyDetailDao.selectByInvtyDeatilsList(map);
		for (InvtyDetail it : itList) {
			if (it.getInvtyDetailsList().size() > 0) {

				for (InvtyDetails its : it.getInvtyDetailsList()) {
					itsList.add(its);
				}
			}
		}
		// ɾ����Ӧ����ϸ��
		if (itsList.size() > 0) {
			invtyDetailDao.delectInvtyList(itsList);
		}

	}

	/**
	 * ��ѯ�ڳ�����
	 */
	private List<InvtyMthTermBgnTab> getBakcMthList(FormBackFlushSub sub, int year, int month) {
		Map<String, Object> paramMap = new HashMap<>();

		paramMap.put("invtyClsEncd", sub.getInvtyClsEncd());
		paramMap.put("invtyEncd", sub.getInvtyEncd());
		paramMap.put("whsEncd", sub.getWhsEncd());
		paramMap.put("batNum", sub.getBatNum());
		paramMap.put("year", year);
		paramMap.put("month", month);
		List<InvtyMthTermBgnTab> tabList = invtyMthTermBgnTabDao.selectByMthTerm(paramMap);
		return tabList;
	}

	/**
	 * ��ȡ�����Ŀ
	 */
	private List<AcctItmDoc> getInvtySub(String sellTypId, String clsEncd) {
		// �����Ŀ
		Map<String, Object> map = new HashMap<>();
		map.put("clsEncd", clsEncd); // ����������
		map.put("sellTypId", sellTypId); // ��������

		List<AcctItmDoc> stockList = accItemDao.selectStockByClsEncd(map);
		if (stockList.size() == 0) {
			AcctItmDoc acc = new AcctItmDoc();
			acc.setSubjId("");
			acc.setSubjNm("");
			stockList.add(acc);
		}
		return stockList;
	}

	/**
	 * ��ȡӦ���ݹ���Ŀ
	 * 
	 */
	private List<AcctItmDoc> getTempSub() {
		Map<String, Object> map = new HashMap<>();
		// �Է���Ŀ
		map.put("isNtBllg", "0"); // �Ƿ�Ʊ
		List<AcctItmDoc> otherList = accItemDao.selectOtherByClsEncd(map);

		if (otherList.size() == 0) {
			AcctItmDoc acc = new AcctItmDoc();
			acc.setSubjId("");
			acc.setSubjNm("");
			otherList.add(acc);
		}
		return otherList;
	}

	/**
	 * ��ȡӦ����Ŀ
	 * 
	 * @return
	 */
	private List<AcctItmDoc> getPaySub() {
		Map<String, Object> map = new HashMap<>();
		// Ӧ����Ŀ
		List<AcctItmDoc> otherList = accItemDao.selectPaySub(map);
		if (otherList.size() == 0) {
			AcctItmDoc acc = new AcctItmDoc();
			acc.setSubjId("");
			acc.setSubjNm("");
			otherList.add(acc);
		}
		return otherList;
	}

	@Override
	public String backFlushFormList(Map map, String url) throws Exception {
		// �س嵥
		Integer isRedBack = (Integer) map.get("isRedBack");
		boolean isSuccess = true;
		String resp = "";
		int pageNo = Integer.parseInt(map.get("pageNo").toString());
		int pageSize = Integer.parseInt(map.get("pageSize").toString());
		int count = 0;
		List<FormBackFlush> formFlushList = formBackFlushDao.selectBackFlushByMap(map);
		count = formBackFlushDao.countSelectBackFlushByMap(map);
		resp = BaseJson.returnRespList(url, isSuccess, "��ѯ�ɹ�", count, pageNo, pageSize, 0, 0, formFlushList);
		return resp;
	}

	@Override
	public String mthSealBook(Map map, String url, String loginTime, String accNum) throws Exception {
		// ����-����
		int year = Integer.valueOf(loginTime.substring(0, 4));
		int month = Integer.valueOf(loginTime.substring(5, 7));
		boolean isSuccess = false;
		String message = "";
		int isSealBook = (int) map.get("isSealBook"); // �Ƿ����
		map.put("year", year);
		map.put("month", month);
		// ��ѯ
		if (isSealBook == 2) {
			map.put("isSealBook", "");
			List<MthEndStl> stlList = mthEndStlDao.selectByMap(map);
			// message = "��ѯ�ɹ�";
			isSuccess = true;
			return BaseJson.returnRespList(url, isSuccess, message, stlList);
		} else {
			map.put("isSealBook", isSealBook);
			List<MthEndStl> stlList = mthEndStlDao.selectByMap(map);
			if (stlList.size() > 0) {
				MthEndStl mth = stlList.get(0);
				if (isSealBook == 0) {
					mth.setIsMthSeal(1);
				} else {
					mth.setIsMthSeal(0);
				}

				mthEndStlDao.updateIsMthEndStl(mth);
				map.put("isSealBook", "");
				stlList = mthEndStlDao.selectByMap(map);
				isSuccess = true;
				message = "�����ɹ�";
			} else {
				message = "����ʧ��";
			}
			return BaseJson.returnRespList(url, isSuccess, message, stlList);
		}

	}

	/**
	 * ��ǰ�·��Ƿ����
	 * 
	 * @param loginTime ��¼����
	 * @return true-�ѷ��� false-δ����
	 */
	@Override
	public boolean isMthSeal(String loginTime) {
		int year = Integer.valueOf(loginTime.substring(0, 4));
		int month = Integer.valueOf(loginTime.substring(5, 7));
		boolean isMthSeal = true;
		Map map = new HashMap<>();
		map.put("isSealBook", 1);
		map.put("year", year);
		map.put("month", month);

		List<MthEndStl> stlList = mthEndStlDao.selectByMap(map);
		if (stlList.size() > 0) {
			isMthSeal = true;
		} else {
			isMthSeal = false;
		}
		return isMthSeal;
	}

	/**
	 * �Ƶ�����
	 * 
	 * @param form �����ӱ�
	 * @param subs ��������
	 */
	public FormBookEntry billingRulesByFormTypEncd(FormBookEntry form, FormBookEntrySub subs) {

		String formTypEncd = form.getFormTypEncd();
		String outInoTypeId = form.getOutIntoWhsTypId();
		String invtyClsEncd = subs.getInvtyClsEncd();
		String sellTypId = form.getSellTypId();
		String recvSendCateId = form.getRecvSendCateId();

		// ����������
		// InvtyCls cls = invtyClsDao.selectInvtyClsSuper(invtyClsEncd);
		// �����Ŀ
		Map<String, Object> map = new HashMap<>();
		if (invtyClsEncd != null && invtyClsEncd != "") {
			map.put("clsEncd", invtyClsDao.selectInvtyClsSuper(invtyClsEncd).getInvtyClsEncd()); // ����������
		}
		map.put("sellTypId", sellTypId); // �������� 1-��ͨ���� 2-ί�д���

		List<AcctItmDoc> stockList = accItemDao.selectStockByClsEncd(map);
		if (stockList.size() == 0) {
			AcctItmDoc acc = new AcctItmDoc();
			acc.setSubjId("");
			acc.setSubjNm("");
			stockList.add(acc);
		}

		// �Է���Ŀ
		if (StringUtils.isEmpty(recvSendCateId)) {
			map.put("recvSendCateId", ""); // �շ�������
		} else {
			map.put("recvSendCateId", recvSendCateId); // �շ�������
		}

		List<AcctItmDoc> otherList = accItemDao.selectOtherByClsEncd(map);

		if (otherList.size() == 0) {
			AcctItmDoc acc = new AcctItmDoc();
			acc.setSubjId("");
			acc.setSubjNm("");
			otherList.add(acc);
		}
		SubjectDebit tabSub = subs.getSubjectDebit(); // ��
		SubjectCredit creSub = subs.getSubjectCredit(); // ��
		if (tabSub == null) {
			tabSub = new SubjectDebit();
		}
		if (creSub == null) {
			creSub = new SubjectCredit();
		}
		tabSub.setSubDebitMoney(Optional.ofNullable(tabSub.getSubDebitMoney()).orElse(new BigDecimal(0.00000000)));
		tabSub.setSubDebitNum(Optional.ofNullable(tabSub.getSubDebitNum()).orElse(new BigDecimal(0.00000000)));
		creSub.setSubCreditMoney(Optional.ofNullable(creSub.getSubCreditMoney()).orElse(new BigDecimal(0.00000000)));
		creSub.setSubCreditNum(Optional.ofNullable(creSub.getSubCreditNum()).orElse(new BigDecimal(0.00000000)));

		tabSub.setSubDebitMoney(subs.getNoTaxAmt());
		tabSub.setSubDebitNum(subs.getQty());
		tabSub.setSubDebitPath(1);

		creSub.setSubCreditMoney(subs.getNoTaxAmt());
		creSub.setSubCreditNum(subs.getQty());
		creSub.setSubCreditPath(2);

		// �����ⵥ ��/�� ��/��
		if (formTypEncd.equals("011")) {
			// �ϲ��Ƶ�
			if (outInoTypeId.equals("1")) {

				tabSub = setTabSubDebit(tabSub, stockList.get(0).getSubjId(), "���", stockList.get(0).getSubjNm());
				subs = setFormSubDebit(subs, stockList.get(0).getSubjId(), "���", stockList.get(0).getSubjNm());

			}
			if (outInoTypeId.equals("2")) {

				creSub = setTabSubCredit(creSub, otherList.get(0).getSubjId(), "�Է�", otherList.get(0).getSubjNm());
				subs = setFormSubCredit(subs, otherList.get(0).getSubjId(), "�Է�", otherList.get(0).getSubjNm());
			}
		}
		// ��װ�ⵥ ��/�� ��/��
		if (formTypEncd.equals("012")) {
			// �ϲ��Ƶ�
			if (outInoTypeId.equals("3")) {

				tabSub = setTabSubDebit(tabSub, stockList.get(0).getSubjId(), "���", stockList.get(0).getSubjNm());
				subs = setFormSubDebit(subs, stockList.get(0).getSubjId(), "���", stockList.get(0).getSubjNm());

			}
			if (outInoTypeId.equals("4")) {

				creSub = setTabSubCredit(creSub, stockList.get(0).getSubjId(), "���", stockList.get(0).getSubjNm());
				subs = setFormSubCredit(subs, stockList.get(0).getSubjId(), "���", stockList.get(0).getSubjNm());
			}
		}
		// ��ж�ⵥ ��/�� ��/��
		if (formTypEncd.equals("013")) {
			// �ϲ��Ƶ�
			if (outInoTypeId.equals("5")) {

				tabSub = setTabSubDebit(tabSub, stockList.get(0).getSubjId(), "���", stockList.get(0).getSubjNm());
				subs = setFormSubDebit(subs, stockList.get(0).getSubjId(), "���", stockList.get(0).getSubjNm());

			}
			if (outInoTypeId.equals("6")) {

				creSub = setTabSubCredit(creSub, stockList.get(0).getSubjId(), "���", stockList.get(0).getSubjNm());
				subs = setFormSubCredit(subs, stockList.get(0).getSubjId(), "���", stockList.get(0).getSubjNm());

			}
		}
		// ��ӯ��ⵥ ��/�� ��/��
		if (formTypEncd.equals("028") && outInoTypeId.equals("7")) {
			// ��ȡ��

			tabSub = setTabSubDebit(tabSub, stockList.get(0).getSubjId(), "���", stockList.get(0).getSubjNm());
			subs = setFormSubDebit(subs, stockList.get(0).getSubjId(), "���", stockList.get(0).getSubjNm());

			// ��ȡ��

			creSub = setTabSubCredit(creSub, otherList.get(0).getSubjId(), "�Է�", otherList.get(0).getSubjNm());
			subs = setFormSubCredit(subs, otherList.get(0).getSubjId(), "�Է�", otherList.get(0).getSubjNm());

		}
		// �̿����ⵥ ��/�� ��/��
		if (formTypEncd.equals("028") && outInoTypeId.equals("8")) {
			// ��ȡ��
			tabSub = setTabSubDebit(tabSub, otherList.get(0).getSubjId(), "�Է�", otherList.get(0).getSubjNm());
			subs = setFormSubDebit(subs, otherList.get(0).getSubjId(), "�Է�", otherList.get(0).getSubjNm());

			// ��ȡ��
			creSub = setTabSubCredit(creSub, stockList.get(0).getSubjId(), "���", stockList.get(0).getSubjNm());
			subs = setFormSubCredit(subs, stockList.get(0).getSubjId(), "���", stockList.get(0).getSubjNm());

		}
		// �ɹ���ⵥ ��/�� ��/��/�շ����Ŀ�Ŀ
		if (formTypEncd.equals("004")) {
			// ��ȡ��
			tabSub = setTabSubDebit(tabSub, stockList.get(0).getSubjId(), "���", stockList.get(0).getSubjNm());
			subs = setFormSubDebit(subs, stockList.get(0).getSubjId(), "���", stockList.get(0).getSubjNm());

			if (subs.getUnBllgQty() != null) {
				Integer isNtBllg = 0;
				if (subs.getUnBllgQty().compareTo(BigDecimal.ZERO) != 0) {
					isNtBllg = 0;
				} else {
					isNtBllg = 1;
				}
				map.put("isNtBllg", isNtBllg); // �Ƿ�Ʊ 0-ȡ�ݹ���Ŀ
				List<AcctItmDoc> other = accItemDao.selectOtherByClsEncd(map);

				if (other.size() > 0) {
					if (isNtBllg == 0) {

						creSub = setTabSubCredit(creSub, other.get(0).getSubjId(), "Ӧ���ݹ�", other.get(0).getSubjNm());
						subs = setFormSubCredit(subs, other.get(0).getSubjId(), "Ӧ���ݹ�", other.get(0).getSubjNm());
					} else {
						creSub = setTabSubCredit(creSub, other.get(0).getSubjId(), "�Է�", other.get(0).getSubjNm());
						subs = setFormSubCredit(subs, other.get(0).getSubjId(), "�Է�", other.get(0).getSubjNm());
					}
				} else {
					creSub = setTabSubCredit(creSub, otherList.get(0).getSubjId(), "�Է�", otherList.get(0).getSubjNm());
					subs = setFormSubCredit(subs, otherList.get(0).getSubjId(), "�Է�", otherList.get(0).getSubjNm());

				}

			}

		}
		// ���۳��ⵥ ��/��/�շ����Ŀ�Ŀ ��/��
		if (formTypEncd.equals("009")) {
			// �跢����Ʒ �������Ʒ

			map.put("clsEncd", invtyClsDao.selectInvtyClsSuper(invtyClsEncd).getInvtyClsEncd()); // ����������
			map.put("sellTypId", sellTypId); // �������� 1-��ͨ���� 2-ί�д���

			otherList = accItemDao.selectStockByClsEncd(map);
			otherList = getAccItem(otherList); // �������Ʒ

			map.put("sellTypId", 2);
			stockList = accItemDao.selectStockByClsEncd(map); // �跢����Ʒ
			stockList = getAccItem(stockList);

			creSub = setTabSubCredit(creSub, otherList.get(0).getSubjId(), "���", otherList.get(0).getSubjNm());
			subs = setFormSubCredit(subs, otherList.get(0).getSubjId(), "���", otherList.get(0).getSubjNm());

			tabSub = setTabSubDebit(tabSub, stockList.get(0).getSubjId(), "������Ʒ", stockList.get(0).getSubjNm());
			subs = setFormSubDebit(subs, stockList.get(0).getSubjId(), "������Ʒ", stockList.get(0).getSubjNm());

			// if(StringUtils.isNotEmpty(form.getSellTypId()) &&
			// Integer.valueOf(form.getSellTypId()) == 2) {

			// ��ȡ��
			// tabSub =
			// setTabSubDebit(tabSub,otherList.get(0).getSubjId(),"ί�д���",otherList.get(0).getSubjNm());
			// subs =
			// setFormSubDebit(subs,otherList.get(0).getSubjId(),"ί�д���",otherList.get(0).getSubjNm());

			// } else if(StringUtils.isNotEmpty(form.getSellTypId()) &&
			// Integer.valueOf(form.getSellTypId()) == 1) {

			// ��������=1������Ŀ
			// ��ȡ��/�շ����Ŀ�Ŀ

			// tabSub =
			// setTabSubDebit(tabSub,otherList.get(0).getSubjId(),"������Ʒ",otherList.get(0).getSubjNm());
			// subs =
			// setFormSubDebit(subs,otherList.get(0).getSubjId(),"������Ʒ",otherList.get(0).getSubjNm());
			// }
		}
		// ������ⵥ ��/�� ��/��
		if (formTypEncd.equals("014") && outInoTypeId.equals("12")) {
			// ��ȡ��
			subs = setFormSubDebit(subs, stockList.get(0).getSubjId(), "���", stockList.get(0).getSubjNm());
			subs = setFormSubCredit(subs, otherList.get(0).getSubjId(), "�Է�", otherList.get(0).getSubjNm());

			tabSub = setTabSubDebit(tabSub, stockList.get(0).getSubjId(), "���", stockList.get(0).getSubjNm());
			creSub = setTabSubCredit(creSub, otherList.get(0).getSubjId(), "�Է�", otherList.get(0).getSubjNm());

		}
		// �������ⵥ ��/�� ��/��
		if (formTypEncd.equals("015") && outInoTypeId.equals("11")) {

			subs = setFormSubCredit(subs, stockList.get(0).getSubjId(), "���", stockList.get(0).getSubjNm());
			subs = setFormSubDebit(subs, otherList.get(0).getSubjId(), "�Է�", otherList.get(0).getSubjNm());

			creSub = setTabSubCredit(creSub, stockList.get(0).getSubjId(), "���", stockList.get(0).getSubjNm());
			tabSub = setTabSubDebit(tabSub, otherList.get(0).getSubjId(), "�Է�", otherList.get(0).getSubjNm());
		}

		// ���۵� �跢����Ʒ �����
		if (formTypEncd.equals("007")) {

			subs = setFormSubCredit(subs, stockList.get(0).getSubjId(), "���", stockList.get(0).getSubjNm());
			subs = setFormSubDebit(subs, otherList.get(0).getSubjId(), "������Ʒ", otherList.get(0).getSubjNm());

			creSub = setTabSubCredit(creSub, stockList.get(0).getSubjId(), "���", stockList.get(0).getSubjNm());
			tabSub = setTabSubDebit(tabSub, otherList.get(0).getSubjId(), "������Ʒ", otherList.get(0).getSubjNm());
		}
		// �ɹ���Ʊ ��ֱ�˿�Ŀ,˰���Ŀ ��Ӧ����Ŀ�Է���Ŀ���շ�����Ӧ�Ŀ�Ŀ
		if (formTypEncd.equals("019") || formTypEncd.equals("020")) {

		}
		// ���۷�Ʊ ��Է���Ŀ���շ�����Ӧ�Ŀ�Ŀ ��ֱ�˿�Ŀ
		if (formTypEncd.equals("021") || formTypEncd.equals("022")) {
			// ����Ӫҵ��ɱ� ��������Ʒ
			map.put("clsEncd", invtyClsDao.selectInvtyClsSuper(invtyClsEncd).getInvtyClsEncd()); // ����������
			map.put("isNtBllg", 1);
			if (StringUtils.isEmpty(recvSendCateId)) {
				map.put("recvSendCateId", ""); // �շ�������
			} else {
				map.put("recvSendCateId", recvSendCateId); // �շ�������
			}
			stockList = accItemDao.selectOtherByClsEncd(map);
			stockList = getAccItem(stockList); // ����Ӫҵ��ɱ�

			map.put("sellTypId", 2);
			otherList = accItemDao.selectStockByClsEncd(map);
			otherList = getAccItem(otherList); // ��������Ʒ

			subs = setFormSubCredit(subs, otherList.get(0).getSubjId(), "������Ʒ", otherList.get(0).getSubjNm());
			subs = setFormSubDebit(subs, stockList.get(0).getSubjId(), "�Է�", stockList.get(0).getSubjNm());

			creSub = setTabSubCredit(creSub, otherList.get(0).getSubjId(), "������Ʒ", otherList.get(0).getSubjNm());
			tabSub = setTabSubDebit(tabSub, stockList.get(0).getSubjId(), "�Է�", stockList.get(0).getSubjNm());

		}
		// �������� ���� ���Է�
		if (formTypEncd.equals("030")) {
			// ��ȡ��
			// ��ȡ��
			subs = setFormSubDebit(subs, stockList.get(0).getSubjId(), "���", stockList.get(0).getSubjNm());
			subs = setFormSubCredit(subs, otherList.get(0).getSubjId(), "�Է�", otherList.get(0).getSubjNm());

			tabSub = setTabSubDebit(tabSub, stockList.get(0).getSubjId(), "���", stockList.get(0).getSubjNm());
			creSub = setTabSubCredit(creSub, otherList.get(0).getSubjId(), "�Է�", otherList.get(0).getSubjNm());
		}
		// ��������� ��Է� �����
		if (formTypEncd.equals("031")) {
			subs = setFormSubCredit(subs, stockList.get(0).getSubjId(), "���", stockList.get(0).getSubjNm());
			subs = setFormSubDebit(subs, otherList.get(0).getSubjId(), "�Է�", otherList.get(0).getSubjNm());

			creSub = setTabSubCredit(creSub, stockList.get(0).getSubjId(), "���", stockList.get(0).getSubjNm());
			tabSub = setTabSubDebit(tabSub, otherList.get(0).getSubjId(), "�Է�", otherList.get(0).getSubjNm());
		}
		// ���ֻس嵥 ���� ��Ӧ���ʿ�
		if (formTypEncd.equals("016")) {
			// ��ȡ��
			subs = setFormSubDebit(subs, stockList.get(0).getSubjId(), "���", stockList.get(0).getSubjNm());
			subs = setFormSubCredit(subs, otherList.get(0).getSubjId(), "Ӧ���ʿ�", otherList.get(0).getSubjNm());

			tabSub = setTabSubDebit(tabSub, stockList.get(0).getSubjId(), "���", stockList.get(0).getSubjNm());
			creSub = setTabSubCredit(creSub, otherList.get(0).getSubjId(), "Ӧ���ʿ�", otherList.get(0).getSubjNm());

		}
		// ���ֻس嵥 ���� ��Ӧ���˿�-�ݹ�Ӧ��
		if (formTypEncd.equals("017")) {
			// ��ȡ��

			subs = setFormSubDebit(subs, stockList.get(0).getSubjId(), "���", stockList.get(0).getSubjNm());
			subs = setFormSubCredit(subs, otherList.get(0).getSubjId(), "Ӧ���˿�-�ݹ�Ӧ��", otherList.get(0).getSubjNm());

			tabSub = setTabSubDebit(tabSub, stockList.get(0).getSubjId(), "���", stockList.get(0).getSubjNm());
			creSub = setTabSubCredit(creSub, otherList.get(0).getSubjId(), "Ӧ���˿�-�ݹ�Ӧ��", otherList.get(0).getSubjNm());

		}
		// �˻���
		if (formTypEncd.equals("008")) {

		}
		// ί�д���������
		if (formTypEncd.equals("023")) {

		}
		// ί�д����˻���
		if (formTypEncd.equals("024")) {

		}

		subs.setSubjectDebit(tabSub);
		subs.setSubjectCredit(creSub);
		return form;
	}

	private List<AcctItmDoc> getAccItem(List<AcctItmDoc> stockList) {
		if (stockList.size() == 0) {
			AcctItmDoc acc = new AcctItmDoc();
			acc.setSubjId("");
			acc.setSubjNm("");
			stockList.add(acc);
		} else {
			return stockList;
		}

		return stockList;
	}

	private SubjectCredit setTabSubCredit(SubjectCredit creSub, String subjId, String type, String subjNm) {
		creSub.setSubCreditId(subjId);
		creSub.setSubCreditType(type);
		creSub.setSubCreditNm(subjNm);

		return creSub;
	}

	private SubjectDebit setTabSubDebit(SubjectDebit tabSub, String subjId, String type, String subjNm) {
		tabSub.setSubDebitId(subjId);
		tabSub.setSubDebitType(type);
		tabSub.setSubDebitNm(subjNm);
		return tabSub;
	}

	private FormBookEntrySub setFormSubCredit(FormBookEntrySub subs, String subjId, String type, String subjNm) {
		subs.setSubCreditId(subjId);
		subs.setSubCreditType(type);
		subs.setSubCreditNm(subjNm);
		subs.setSubCreditPath(2);
		subs.setSubCreditMoney(subs.getNoTaxAmt());
		subs.setSubCreditNum(subs.getQty());
		return subs;
	}

	private FormBookEntrySub setFormSubDebit(FormBookEntrySub subs, String subjId, String type, String subjNm) {
		subs.setSubDebitId(subjId);
		subs.setSubDebitType(type);
		subs.setSubDebitNm(subjNm);
		subs.setSubDebitPath(1);
		subs.setSubDebitMoney(subs.getNoTaxAmt());
		subs.setSubDebitNum(subs.getQty());
		return subs;
	}

	@Override
	@Transactional
	public String outIntoAdjFormBook(Map map, String url, String loginTime, String accNum) throws Exception {
		String message = "";
		String resp = "";
		boolean isSuccess = true;
		//��ѯ�����Ƿ��Ѿ�����
		isSuccess = checkOutIsEndOfMonth(loginTime);
		
		if (!isSuccess) {
			message = "��ǰ��¼���ڣ�" + loginTime + "���ڽ�������";
			isSuccess = false;
			resp = BaseJson.returnRespObj("account/form/back/book", isSuccess, message, null);
			return resp;
		}
		List<String> adjFormNumList = CommonUtil.strToList((String) map.get("adjFormNum")); // ���������Ŷ��
		map.put("isBookOk", "0");
		map.put("adjFormNumList", adjFormNumList);
		map.put("loginTime", loginTime);
		List<OutIntoWhsAdjSngl> adjList = outIntoWhsAdjSnglDao.selectOutIntoWhsAdjSnglList(map);
		if (adjList.size() > 0) {
			List<InvtyDetails> itsList = new ArrayList<>();
			for (OutIntoWhsAdjSngl adj : adjList) {
				adj.setIsNtBookEntry(1);
				adj.setBookEntryTm(loginTime);
				adj.setBookEntryPers(accNum);

				for (OutIntoWhsAdjSnglSubTab s : adj.getOutIntoList()) {
					map.put("whsEncd", s.getWhsEncd());
					map.put("invtyEncd", s.getInvtyEncd());
					map.put("batNum", s.getBatNum());

					List<InvtyDetail> itList = invtyDetailDao.selectByInvtyDeatil(map);
					InvtyDetail invty = new InvtyDetail();
					if (itList.size() > 0) {
						invty = itList.get(0);
					} else {

						BeanUtils.copyProperties(invty, s);
						int i = invtyDetailDao.insertInvtyDetail(invty);

					}
					
					itsList = getInvtyMthAndDeal(s, adj.getOutIntoWhsInd(), map,accNum,
							loginTime,invty.getDetailId(),itsList);

				}
			}
			
			if(itsList.size() > 0) {
				invtyDetailDao.insertInvtyDetailsList(itsList);			
			}
			outIntoWhsAdjSnglDao.updateOutIntoAdjList(adjList);
			message = "���˳ɹ�";
			isSuccess = true;
		} else {
			message = "��������쳣";
			isSuccess = false;
		}
		resp = BaseJson.returnRespObj("account/form/outIntoAdj/formBook", isSuccess, message, null);
		return resp;
	}

	@Override
	@Transactional
	public String outIntoAdjBackFormBook(Map map, String url, String loginTime, String userName) throws Exception {
		String message = "";
		String resp = "";
		boolean isSuccess = true;
		//��ѯ�����Ƿ��Ѿ�����
        isSuccess = checkOutIsEndOfMonth(loginTime);
		
		if (!isSuccess) {
			message = "��ǰ��¼���ڣ�" + loginTime + "���ڽ�������";
			isSuccess = false;
			resp = BaseJson.returnRespObj("account/form/back/book", isSuccess, message, null);
			return resp;
		}
		List<String> adjFormNumList = CommonUtil.strToList((String) map.get("adjFormNum")); // ���������Ŷ��
		map.put("isBookOk", "1");
		map.put("adjFormNumList", adjFormNumList);
		map.put("backNumList", adjFormNumList);
		map.put("year", Integer.valueOf(loginTime.substring(0, 4)));
		map.put("month", Integer.valueOf(loginTime.substring(5, 7)));
		map.put("loginTime", loginTime);
		List<OutIntoWhsAdjSngl> adjList = outIntoWhsAdjSnglDao.selectOutIntoWhsAdjSnglList(map);
		if (adjList.size() > 0) {
			
			for (OutIntoWhsAdjSngl adj : adjList) {
				adj.setIsNtBookEntry(1);
				adj.setBookEntryTm("0000-00-00 00:00:00");
				adj.setBookEntryPers("");
				
				for (OutIntoWhsAdjSnglSubTab s : adj.getOutIntoList()) {
					map.put("whsEncd", s.getWhsEncd());
					map.put("invtyEncd", s.getInvtyEncd());
					map.put("batNum", s.getBatNum());
					getInvtyMthAndBackDeal(s, adj.getOutIntoWhsInd(), map,loginTime);
				}
			}
			List<InvtyDetails> itsList = invtyDetailDao.selectByInvtyDeatilsList(map);
			if(itsList.size() > 0) {
				invtyDetailDao.delectInvtyList(itsList);
			}
			outIntoWhsAdjSnglDao.updateOutIntoAdjList(adjList);
			message = "���˳ɹ�";
			isSuccess = true;
		} else {
			message = "��������쳣";
			isSuccess = false;
		}
		
		resp = BaseJson.returnRespObj("account/form/outIntoAdj/backFormBook", isSuccess, message, null);
		return resp;
	}

	private void getInvtyMthAndBackDeal(OutIntoWhsAdjSnglSubTab s, Integer outIntoWhsInd, Map map, String loginTime) {
		// ��ѯ���º����ڳ�����
		List<InvtyMthTermBgnTab> termList = invtyMthTermBgnTabDao.selectByMthTerm(map);
		
		InvtyDetails its = new InvtyDetails();
		if (termList.size() > 0) {
			InvtyMthTermBgnTab im = termList.get(0);
			
			if(outIntoWhsInd == 0) {
				//����
				im.setSendMoeny(im.getSendMoeny().subtract(s.getAmt()));
				if (im.getSendQty().compareTo(BigDecimal.ZERO) == 0) {
					im.setSendUnitPrice(new BigDecimal(0.00000000));
				} else {
					im.setSendUnitPrice(
							im.getSendMoeny().divide(im.getSendQty(), 8, BigDecimal.ROUND_HALF_UP).abs());
				}
			} else {
				im.setInMoeny(im.getInMoeny().subtract(s.getAmt()));
				if (im.getInQty().compareTo(BigDecimal.ZERO) == 0) {
					im.setInUnitPrice(new BigDecimal(0.00000000));
				} else {
					im.setInUnitPrice(
							im.getInMoeny().divide(im.getInQty(), 8, BigDecimal.ROUND_HALF_UP).abs());
				}
			}
			im.setOthMoeny(im.getAmt().add(im.getInMoeny()).subtract(im.getSendMoeny()));
			if (im.getOthQty().compareTo(BigDecimal.ZERO) == 0) {
				im.setOthUnitPrice(new BigDecimal(0.00000000));
			} else {
				im.setOthUnitPrice(im.getOthMoeny().divide(im.getOthQty(), 8, BigDecimal.ROUND_HALF_UP).abs());
			}
			
			if (im.getQty().compareTo(BigDecimal.ZERO) == 0
					&& im.getAmt().compareTo(BigDecimal.ZERO) == 0
					&& im.getInQty().compareTo(BigDecimal.ZERO) == 0
					&& im.getInMoeny().compareTo(BigDecimal.ZERO) == 0
					&& im.getSendQty().compareTo(BigDecimal.ZERO) == 0
					&& im.getSendMoeny().compareTo(BigDecimal.ZERO) == 0
					&& im.getOthQty().compareTo(BigDecimal.ZERO) == 0
					&& im.getOthMoeny().compareTo(BigDecimal.ZERO) == 0) {

				invtySendMthTermBgnTabDao.deleteByordrNum(im.getOrdrNum());
			} else {
				 invtySendMthTermBgnTabDao.updateSendMth(im);
			}
		} 
		
		
	}

	private List<InvtyDetails> getInvtyMthAndDeal(OutIntoWhsAdjSnglSubTab sub, Integer outIntoWhsInd, Map map,
			String accNum, String loginTime,int detailId,List<InvtyDetails> itsList) {
		
		// ��ѯ���º����ڳ�����
		List<InvtyMthTermBgnTab> termList = invtyMthTermBgnTabDao.selectByMthTerm(map);
		InvtyMthTermBgnTab im = new InvtyMthTermBgnTab();
		InvtyDetails its = new InvtyDetails();
		if (termList.size() > 0) {
			im = termList.get(0);
		} else {
			im = setMthValue(accNum, loginTime, sub);
		}
		im.setSendQty(Optional.ofNullable(im.getSendQty()).orElse(BigDecimal.ZERO));
		im.setSendMoeny(Optional.ofNullable(im.getSendMoeny()).orElse(BigDecimal.ZERO));
		im.setInQty(Optional.ofNullable(im.getInQty()).orElse(BigDecimal.ZERO));
		im.setInMoeny(Optional.ofNullable(im.getInMoeny()).orElse(BigDecimal.ZERO));
		
		if(outIntoWhsInd == 0) {
			//����
			its = new InvtyDetails(loginTime, null, null,"031",
					"���������", null, null, null, BigDecimal.ZERO, BigDecimal.ZERO,
					sub.getAmt(), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, sub.getFormNum(),
					detailId, sub.getOrdrNum());
			im.setSendMoeny(im.getSendMoeny().add(sub.getAmt()));
			if (im.getSendQty().compareTo(BigDecimal.ZERO) == 0) {
				im.setSendUnitPrice(new BigDecimal(0.00000000));
			} else {
				im.setSendUnitPrice(
						im.getSendMoeny().divide(im.getSendQty(), 8, BigDecimal.ROUND_HALF_UP).abs());
			}
		} else {
					
			its = new InvtyDetails(loginTime, null, null, "030",
					"��������", sub.getAmt(), BigDecimal.ZERO, BigDecimal.ZERO,
					null, null, null, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, sub.getFormNum(), detailId,
					sub.getOrdrNum());
					
			//���
			im.setInMoeny(im.getInMoeny().add(sub.getAmt()));
			if (im.getInQty().compareTo(BigDecimal.ZERO) == 0) {
				im.setInUnitPrice(new BigDecimal(0.00000000));
			} else {
				im.setInUnitPrice(
						im.getInMoeny().divide(im.getInQty(), 8, BigDecimal.ROUND_HALF_UP).abs());
			}
				
		}
		
		im.setOthMoeny(im.getAmt().add(im.getInMoeny()).subtract(im.getSendMoeny()));
		if (im.getOthQty().compareTo(BigDecimal.ZERO) == 0) {
			im.setOthUnitPrice(new BigDecimal(0.00000000));
		} else {
			im.setOthUnitPrice(im.getOthMoeny().divide(im.getOthQty(), 8, BigDecimal.ROUND_HALF_UP).abs());
		}
		its.setOthMoeny(im.getOthMoeny());
		its.setOthQty(im.getOthQty());
		its.setOthUnitPrice(im.getOthUnitPrice());
		
		itsList.add(its);
		if(im.getOrdrNum() == -1) {
			invtyMthTermBgnTabDao.insertMth(im);
		
		} else {
			invtyMthTermBgnTabDao.updateMth(im);
		}
		
		return itsList;
	}


	private InvtyMthTermBgnTab setMthValue(String accNum, String loginTime, OutIntoWhsAdjSnglSubTab sub) {
		InvtyMthTermBgnTab mth = new InvtyMthTermBgnTab();
		try {
			BigDecimalConverter bd = new BigDecimalConverter(BigDecimal.ZERO);
			ConvertUtils.register(bd, java.math.BigDecimal.class);
			BeanUtils.copyProperties(mth, sub);
		} catch (IllegalAccessException e) {

			e.printStackTrace();
		} catch (InvocationTargetException e) {

			e.printStackTrace();
		}

		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		mth.setOrdrNum(-1);
		mth.setAccNum(accNum);
		mth.setCtime(sf.format(new Date()).toString());
		mth.setAccTime(loginTime);
		mth.setMakeVouchAbst("�ڳ����");
		mth.setAcctYr(loginTime.substring(0, 4));
		mth.setAcctiMth(loginTime.substring(5, 7));
		mth.setQty(BigDecimal.ZERO);
		mth.setUprc(BigDecimal.ZERO);
		mth.setAmt(BigDecimal.ZERO);
		mth.setInQty(BigDecimal.ZERO);
		mth.setInUnitPrice(BigDecimal.ZERO);
		mth.setInMoeny(BigDecimal.ZERO);
		mth.setSendQty(BigDecimal.ZERO);
		mth.setSendUnitPrice(BigDecimal.ZERO);
		mth.setSendMoeny(BigDecimal.ZERO);
		mth.setOthQty(BigDecimal.ZERO);
		mth.setOthUnitPrice(BigDecimal.ZERO);
		mth.setOthMoeny(BigDecimal.ZERO);
		mth.setIsFinalDeal(0); // �Ƿ���ĩ����
		mth.setIsMthEndStl(0); // �Ƿ���ĩ����

		return mth;
	}

}
