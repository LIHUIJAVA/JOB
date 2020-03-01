package com.px.mis.purc.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;
import com.px.mis.purc.dao.InvtyTabDao;
import com.px.mis.purc.dao.SellOutWhsDao;
import com.px.mis.purc.dao.SellOutWhsSubDao;
import com.px.mis.purc.entity.SellOutWhs;
import com.px.mis.purc.entity.SellOutWhsSub;
import com.px.mis.purc.service.SellOutWhsService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.GetOrderNo;
import com.px.mis.util.poiTool;
import com.px.mis.whs.dao.InvtyGdsBitListMapper;
import com.px.mis.whs.dao.InvtyNumMapper;
import com.px.mis.whs.dao.WhsDocMapper;
import com.px.mis.whs.entity.InvtyTab;
import com.px.mis.whs.entity.MovBitTab;

/*���۳��ⵥ����*/
@Transactional
@Service
public class SellOutWhsServiceImpl extends poiTool implements SellOutWhsService {
	@Autowired
	private SellOutWhsDao sowd;// ���۳��ⵥ����
	@Autowired
	private SellOutWhsSubDao sowsd;// ���۳��ⵥ�ӱ�
	@Autowired
	private InvtyTabDao itd;// ����
	@Autowired
	InvtyGdsBitListMapper bitListMapper;// ��λ������Ŷ�Ӧ��
	@Autowired
	InvtyNumMapper invtyNumMapper;// ��λ
	@Autowired
	private WhsDocMapper whsDocMapper;// �ֿ⵵��
	
	private Logger logger=LoggerFactory.getLogger(SellOutWhsServiceImpl.class);

	// ������
	@Autowired
	private GetOrderNo getOrderNo;



	@Override
	public String addSellOutWhs(String userId, SellOutWhs sellOutWhs, List<SellOutWhsSub> sellOutWhsSubList,String loginTime) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
//			TreeSet<SellOutWhsSub> sellOutWhsSubSet = new TreeSet();
//			sellOutWhsSubSet.addAll(sellOutWhsSubList);
//			if(sellOutWhsSubSet.size() < sellOutWhsSubList.size()) {
//				resp = BaseJson.returnRespObj("purc/SellOutWhs/addSellOutWhs", false, "��������ϸ������ͬһ�ֿ��д���ͬһ�������ͬ���Σ�",null);
//				return resp;
//			}

			// ��ȡ������
			String number = getOrderNo.getSeqNo("CK", userId,loginTime);
			if (sowd.selectSellOutWhsById(number) != null) {
				message = "����" + number + "�Ѵ��ڣ����������룡";
				isSuccess = false;
			} else {
				sellOutWhs.setOutWhsId(number);
				if (sellOutWhs.getBizTypId().equals("1")) {
					sellOutWhs.setRecvSendCateId("7");// �շ����ͱ��룬��������
				}
				if (sellOutWhs.getBizTypId().equals("2")) {
					sellOutWhs.setRecvSendCateId("6");// �շ����ͱ��룬��������
				}
				for (SellOutWhsSub sellOutWhsSub : sellOutWhsSubList) {
					sellOutWhsSub.setOutWhsId(sellOutWhs.getOutWhsId());
					if (sellOutWhsSub.getPrdcDt() == "") {
						sellOutWhsSub.setPrdcDt(null);
					}
					if (sellOutWhsSub.getInvldtnDt() == "") {
						sellOutWhsSub.setInvldtnDt(null);
					}
				}
				sowd.insertSellOutWhs(sellOutWhs);
				sowsd.insertSellOutWhsSub(sellOutWhsSubList);
				message = "�����ɹ���";
				isSuccess = true;
			}
			resp = BaseJson.returnRespObj("purc/SellOutWhs/addSellOutWhs", isSuccess, message, sellOutWhs);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resp;
	}

	// �޸����۳��ⵥ
	@Override
	public String editSellOutWhs(SellOutWhs sellOutWhs, List<SellOutWhsSub> sellOutWhsSubList) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
//			TreeSet<SellOutWhsSub> sellOutWhsSubSet = new TreeSet();
//			sellOutWhsSubSet.addAll(sellOutWhsSubList);
//			if(sellOutWhsSubSet.size() < sellOutWhsSubList.size()) {
//				resp = BaseJson.returnRespObj("purc/SellOutWhs/editSellOutWhs", false, "��������ϸ������ͬһ�ֿ��д���ͬһ�������ͬ���Σ�",null);
//				return resp;
//			}
			if (sellOutWhs.getBizTypId().equals("1")) {
				sellOutWhs.setRecvSendCateId("7");// �շ����ͱ��룬��������
			}
			if (sellOutWhs.getBizTypId().equals("2")) {
				sellOutWhs.setRecvSendCateId("6");// �շ����ͱ��룬��������
			}
			for (SellOutWhsSub sellOutWhsSub : sellOutWhsSubList) {
				sellOutWhsSub.setOutWhsId(sellOutWhs.getOutWhsId());
				if (sellOutWhsSub.getPrdcDt() == "") {
					sellOutWhsSub.setPrdcDt(null);
				}
				if (sellOutWhsSub.getInvldtnDt() == "") {
					sellOutWhsSub.setInvldtnDt(null);
				}
			}
			sowsd.deleteSellOutWhsSubByOutWhsId(sellOutWhs.getOutWhsId());
			sowd.updateSellOutWhsByOutWhsId(sellOutWhs);
			sowsd.insertSellOutWhsSub(sellOutWhsSubList);
			message = "���³ɹ���";
			System.out.println(sellOutWhs);
			resp = BaseJson.returnRespObj("purc/SellOutWhs/editSellOutWhs", isSuccess, message, sellOutWhs);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	@Override
	public String deleteSellOutWhs(String outWhsId) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		if (sowd.selectSellOutWhsByOutWhsId(outWhsId) != null) {
			sowd.deleteSellOutWhsByOutWhsId(outWhsId);
			sowsd.deleteSellOutWhsSubByOutWhsId(outWhsId);
			isSuccess = true;
			message = "ɾ���ɹ���";
		} else {
			isSuccess = false;
			message = "����" + outWhsId + "�����ڣ�";
		}
		try {
			resp = BaseJson.returnRespObj("purc/SellOutWhs/deleteSellOutWhs", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	@Override
	public String querySellOutWhs(String outWhsId) {
		// TODO Auto-generated method stub
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		SellOutWhs sellOutWhs = sowd.selectSellOutWhsByOutWhsId(outWhsId);
		if (sellOutWhs != null) {
			message = "��ѯ�ɹ���";
		} else {
			isSuccess = false;
			message = "����" + outWhsId + "�����ڣ�";
		}

		try {
			resp = BaseJson.returnRespObj("purc/SellOutWhs/querySellOutWhs", isSuccess, message, sellOutWhs);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	@Override
	public String querySellOutWhsList(Map map) {
		String resp = "";
		List<String> outWhsIdList = getList((String) map.get("outWhsId"));// ���۳��ⵥ��
		List<String> custIdList = getList((String) map.get("custId"));// �ͻ�����
		List<String> accNumList = getList((String) map.get("accNum"));// ҵ��Ա����
		List<String> invtyEncdList = getList((String) map.get("invtyEncd"));// �������
		List<String> invtyClsEncdList = getList((String) map.get("invtyClsEncd"));// ����������
		List<String> invtyCdList = getList((String) map.get("invtyCd"));// �������
		List<String> deptIdList = getList((String) map.get("deptId"));// ���ű���
		List<String> whsEncdList = getList((String) map.get("whsEncd"));// �ֿ����
		List<String> custOrdrNumList = getList((String) map.get("custOrdrNum"));// �ͻ�������
		List<String> batNumList = getList((String) map.get("batNum"));// ����
		List<String> intlBatList = getList((String) map.get("intlBat"));// ��������

		map.put("outWhsIdList", outWhsIdList);
		map.put("custIdList", custIdList);
		map.put("accNumList", accNumList);
		map.put("invtyEncdList", invtyEncdList);
		map.put("invtyClsEncdList", invtyClsEncdList);
		map.put("invtyCdList", invtyCdList);
		map.put("deptIdList", deptIdList);
		map.put("whsEncdList", whsEncdList);
		map.put("custOrdrNumList", custOrdrNumList);
		map.put("batNumList", batNumList);
		map.put("intlBatList", intlBatList);
		
		//�ֿ�Ȩ�޿���
		List<String> whsIdList = getList((String) map.get("whsId"));//�ֿ����
		map.put("whsIdList", whsIdList);
		

		List<SellOutWhs> poList = sowd.selectSellOutWhsList(map);
		int count = sowd.selectSellOutWhsCount(map);
		
		int pageNo = Integer.parseInt(map.get("pageNo").toString());
		int pageSize = Integer.parseInt(map.get("pageSize").toString());
		int listNum = poList.size();
		int pages = count / pageSize + 1;

		try {
			resp = BaseJson.returnRespList("purc/SellOutWhs/querySellOutWhsList", true, "��ѯ�ɹ���", count, pageNo,
					pageSize, listNum, pages, poList);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// ����ɾ��
	@Override
	public String deleteSellOutWhsList(String outWhsId) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			List<String> lists = getList(outWhsId);
			List<String> lists2 = new ArrayList<>();
			List<String> lists3 = new ArrayList<>();
			for (String list : lists) {
				if (sowd.selectIsNtChkByOutWhsId(list) == 0) {
					lists2.add(list);
				} else {
					lists3.add(list);
				}
			}
			if (lists2.size() > 0) {
				int a = 0;
				try {
					a = deleteSellOutWhsList(lists2);
				} catch (Exception e) {
					isSuccess = false;
					message += "���ݺ�Ϊ��" + lists2.toString() + "�Ķ���ɾ��ʧ�ܣ�\n";
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
			resp = BaseJson.returnRespObj("purc/SellOutWhs/deleteSellOutWhsList", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	@Transactional
	private int deleteSellOutWhsList(List<String> lists2) {
		sowd.insertSellOutWhsDl(lists2);
		sowsd.insertSellOutWhsSubDl(lists2);
		int a = sowd.deleteSellOutWhsList(lists2);
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

	// ���۳��ⵥ���
	@Override
	public Map<String, Object> updateSellOutWhsIsNtChk(SellOutWhs selOutWhs) throws Exception {
		StringBuilder message = new StringBuilder();
		Boolean isSuccess = true;
		Map<String, Object> map = new HashMap<>();
		try {
//			int isNtRtnGood = sowd.selectIsNtRtnGoodByOutWhsId(selOutWhs.getOutWhsId());//���ݳ��ⵥ�Ų�ѯ���۳��ⵥ���˻�״̬
			SellOutWhs sellOutWhs = sowd.selectSellOutWhsAndSub(selOutWhs.getOutWhsId());
			if (sellOutWhs.getIsNtRtnGood() == 0) {
				if (selOutWhs.getIsNtChk() == 1) {
					message.append(updateSellOutWhsIsNtChkOK(sellOutWhs, selOutWhs).get("message"));
				} else if (selOutWhs.getIsNtChk() == 0) {
					message.append(updateSellOutWhsIsNtChkNO(sellOutWhs, selOutWhs).get("message"));
				} else {
					isSuccess = false;
					message.append("���״̬�����޷���ˣ�\n");
				}
			} else if (sellOutWhs.getIsNtRtnGood() == 1) {
				if (selOutWhs.getIsNtChk() == 1) {
					message.append(updateReturnSellWhsIsNtChkOK(sellOutWhs, selOutWhs).get("message"));
				} else if (selOutWhs.getIsNtChk() == 0) {
					message.append(updateReturnSellWhsIsNtChkNO(sellOutWhs, selOutWhs).get("message"));
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

	// ���۳��ⵥ���
	public Map<String, Object> updateSellOutWhsIsNtChkOK(SellOutWhs sellOutWhs, SellOutWhs selOutWhs) {
		String message = "";
		Boolean isSuccess = true;
		Map<String, Object> map = new HashMap<>();
		List<SellOutWhsSub> sellOutWhsSubs = new ArrayList<>();// ���ⵥ�ӱ�
		if (sellOutWhs.getIsNtChk() == 0) {
			Integer updateFlag = sowd.updateSellOutWhsIsNtChk(selOutWhs);//���Ч��,������ֱ�Ӵ��л�
			if (updateFlag==0) {
				throw new RuntimeException("��������ˣ�����Ҫ�ظ����");
			}
			sellOutWhsSubs = sellOutWhs.getSellOutWhsSub();
			InvtyTab invtyTab = new InvtyTab();// ����
			String whsId="";
			for (SellOutWhsSub seOutWhsSubs : sellOutWhsSubs) {
				invtyTab = new InvtyTab();// ����
				invtyTab.setWhsEncd(seOutWhsSubs.getWhsEncd());
				invtyTab.setInvtyEncd(seOutWhsSubs.getInvtyEncd());
				invtyTab.setBatNum(seOutWhsSubs.getBatNum());
				invtyTab = itd.selectInvtyTabsByTerm(invtyTab);//������
				if (invtyTab != null) {
					// a.compareTo(b) -1��ʾС�� 1 ��ʾ���� 0��ʾ����
					if (invtyTab.getNowStok().compareTo(seOutWhsSubs.getQty()) >= 0) {
						// ����ִ����㹻�����޸Ŀ����е��ִ����ͽ�ͨ���ִ����ͽ����������йؽ����ֶ�
						// invtyTab.setAvalQty(seOutWhsSubs.getQty());
						// itd.updateInvtyTabAvalQtyJian(invtyTab);
						itd.updateInvtyTabJianToSellOutWhs(seOutWhsSubs);
					} else {
						isSuccess = false;
						message += "����Ϊ��" + selOutWhs.getOutWhsId() + "�����۳��ⵥ��, �ֿ⣺" + seOutWhsSubs.getWhsEncd() + ",���:" + seOutWhsSubs.getInvtyEncd() + "������"
								+ invtyTab.getBatNum() + "�Ŀ�����������㣬�޷����\n";
						throw new RuntimeException(message);
					}
				} else {
					isSuccess = false;
					message += "����Ϊ��" + selOutWhs.getOutWhsId() + "�����۳��ⵥ��,�ֿ⣺" + seOutWhsSubs.getWhsEncd() + ",�����" + seOutWhsSubs.getInvtyEncd() + ",���Σ�"
							+ seOutWhsSubs.getBatNum() + "�Ŀ�治���ڣ��޷����\n";
					throw new RuntimeException(message);
				}
				whsId=seOutWhsSubs.getWhsEncd();//�ֿ����
			}
			Integer whs = whsDocMapper.selectisNtPrgrGdsBitMgmtWhs(whsId);
			if(whs != null && whs==1) {
				sellOutWhsHuoWeiJian(selOutWhs.getOutWhsId(),sellOutWhsSubs);
			}
			if (updateFlag==1) {
				isSuccess = true;
				message += selOutWhs.getOutWhsId() + "���۳��ⵥ��˳ɹ���\n";
			}
		} else {
			isSuccess = false;
			message += selOutWhs.getOutWhsId() + "��������ˣ�����Ҫ�ظ����\n";
		}
		map.put("isSuccess", isSuccess);
		map.put("message", message);
		return map;
	}

	// ���۳��ⵥ����
	public Map<String, Object> updateSellOutWhsIsNtChkNO(SellOutWhs sellOutWhs, SellOutWhs selOutWhs) {
		String message = "";
		Boolean isSuccess = true;
		Map<String, Object> map = new HashMap<>();

		List<SellOutWhsSub> sellOutWhsSubs = new ArrayList<>();// ���ⵥ�ӱ�
		// �ж����۳��ⵥ�Ƿ����
		if (sellOutWhs.getIsNtChk() == 1) {
			// �ж����۳��ⵥ�Ƿ�Ʊ
			if (sellOutWhs.getIsNtBllg() == 0) {
				// �ж����۳��ⵥ�Ƿ����
				if (sellOutWhs.getIsNtBookEntry() == 0) {
					sellOutWhsSubs = sellOutWhs.getSellOutWhsSub();
					String whsId="";
					for (SellOutWhsSub seOutWhsSubs : sellOutWhsSubs) {
						InvtyTab invtyTab = new InvtyTab();// ����
						invtyTab.setWhsEncd(seOutWhsSubs.getWhsEncd());
						invtyTab.setInvtyEncd(seOutWhsSubs.getInvtyEncd());
						invtyTab.setBatNum(seOutWhsSubs.getBatNum());

						invtyTab = itd.selectInvtyTabsByTerm(invtyTab);
						
						if (invtyTab != null) {
//							invtyTab.setAvalQty(seOutWhsSubs.getQty());
//							itd.updateInvtyTabAvalQtyJia(invtyTab);
							itd.updateInvtyTabJiaToSellOutWhs(seOutWhsSubs);
						} else {
							isSuccess = false;
							message += "����Ϊ��" + selOutWhs.getOutWhsId() + "�����۳��ⵥ��,�ֿ⣺" + seOutWhsSubs.getWhsEncd() + ",�����" + seOutWhsSubs.getInvtyEncd()
									+ ",���Σ�" + seOutWhsSubs.getBatNum() + "�Ŀ�治���ڣ��޷�����\n";
							throw new RuntimeException(message);
						}
						whsId=seOutWhsSubs.getWhsEncd();//�ֿ����
					}
					Integer updateFlag = sowd.updateSellOutWhsIsNtChk(selOutWhs);
					
					Integer whs = whsDocMapper.selectisNtPrgrGdsBitMgmtWhs(whsId);
					if(whs != null && whs==1) {
						sellOutWhsHuoWeiJia(selOutWhs.getOutWhsId(),sellOutWhsSubs);
					}
					if (updateFlag==1) {
						isSuccess = true;
						message += "����Ϊ��" + selOutWhs.getOutWhsId() + "�����۳��ⵥ����ɹ���\n";
					}
					else {
						isSuccess=false;
						message+="����Ϊ��" + selOutWhs.getOutWhsId() + "�����۳��ⵥ������,����Ҫ�ظ�����!\n";
						throw new RuntimeException(message);
					}
				} else {
					isSuccess = false;
					message += "���ݺ�Ϊ" + selOutWhs.getOutWhsId() + "�����۳��ⵥ�Ѽ��ˣ���������\n";
				}
			} else {
				isSuccess = false;
				message += "���ݺ�Ϊ" + selOutWhs.getOutWhsId() + "�����۳��ⵥ�ѿ���Ʊ����������\n";
			}
		} else {
			isSuccess = false;
			message += "����Ϊ��" + selOutWhs.getOutWhsId() + "�����۳��ⵥδ��ˣ�����Ҫ����\n";
		}
		map.put("isSuccess", isSuccess);
		map.put("message", message);
		return map;
	}

	// �������۳��ⵥ���
	public Map<String, Object> updateReturnSellWhsIsNtChkOK(SellOutWhs sellOutWhs, SellOutWhs selOutWhs) {
		String message = "";
		Boolean isSuccess = true;
		Map<String, Object> map = new HashMap<>();
		List<SellOutWhsSub> sellOutWhsSubs = new ArrayList<>();// ���ⵥ�ӱ�

		// �жϺ������۳��ⵥ�Ƿ����
		if (sellOutWhs.getIsNtChk() == 0) {
			sellOutWhsSubs = sellOutWhs.getSellOutWhsSub();
			String whsId="";
			for (SellOutWhsSub seOutWhsSubs : sellOutWhsSubs) {
				InvtyTab invtyTab = new InvtyTab();// ����
				invtyTab.setWhsEncd(seOutWhsSubs.getWhsEncd());
				invtyTab.setInvtyEncd(seOutWhsSubs.getInvtyEncd());
				invtyTab.setBatNum(seOutWhsSubs.getBatNum());
				invtyTab = itd.selectInvtyTabsByTerm(invtyTab);
				if (invtyTab != null) {
					itd.updateInvtyTabJianToSellOutWhs(seOutWhsSubs);
				} else {
					seOutWhsSubs.setQty(seOutWhsSubs.getQty().abs());
					itd.insertInvtyTabBySellOutWhsSub(seOutWhsSubs);
//					isSuccess = false;
//					message += "����Ϊ��" + selOutWhs.getOutWhsId() + "�ĺ������۳��ⵥ��,������룺" + seOutWhsSubs.getInvtyEncd()
//							+ ",���Σ�" + seOutWhsSubs.getBatNum() + "�Ŀ�治���ڣ��޷����\n";
//					throw new RuntimeException(message);
				}
				whsId=seOutWhsSubs.getWhsEncd();
			}
			Integer updateFlag = sowd.updateSellOutWhsIsNtChk(selOutWhs);
			Integer whs = whsDocMapper.selectisNtPrgrGdsBitMgmtWhs(whsId);
			if(whs != null && whs==1) {
				sellOutWhsHuoWeiJia(selOutWhs.getOutWhsId(),sellOutWhsSubs);
			}
			if (updateFlag==1) {
				isSuccess = true;
				message += "����Ϊ��" + selOutWhs.getOutWhsId() + "�ĺ������۳��ⵥ��˳ɹ���\n";
			}else {
				isSuccess = false;
				message += "����Ϊ��" + selOutWhs.getOutWhsId() + "�ĺ������۳��ⵥ����ˣ�����Ҫ�ظ����\n";
				throw new RuntimeException(message);
			}
			
		} else {
			isSuccess = false;
			message += "����Ϊ��" + selOutWhs.getOutWhsId() + "�ĺ������۳��ⵥ����ˣ�����Ҫ�ظ����\n";
			throw new RuntimeException(message);
		}
		map.put("isSuccess", isSuccess);
		map.put("message", message);
		return map;
	}
	
	// �������۳��ⵥ����
	public Map<String, Object> updateReturnSellWhsIsNtChkNO(SellOutWhs sellOutWhs, SellOutWhs selOutWhs) {
		String message = "";
		Boolean isSuccess = true;
		Map<String, Object> map = new HashMap<>();
		List<SellOutWhsSub> sellOutWhsSubs = new ArrayList<>();// ���ⵥ�ӱ�
		// �жϺ������۳��ⵥ�Ƿ����
		if (sellOutWhs.getIsNtChk() == 1) {
			// �ж����۳��ⵥ�Ƿ�Ʊ
			if (sellOutWhs.getIsNtBllg() == 0) {
				// �ж����۳��ⵥ�Ƿ����
				if (sellOutWhs.getIsNtBookEntry() == 0) {
					sellOutWhsSubs = sellOutWhs.getSellOutWhsSub();
					String whsId="";
					for (SellOutWhsSub seOutWhsSubs : sellOutWhsSubs) {
						InvtyTab invtyTab = new InvtyTab();// ����
						invtyTab.setWhsEncd(seOutWhsSubs.getWhsEncd());
						invtyTab.setInvtyEncd(seOutWhsSubs.getInvtyEncd());
						invtyTab.setBatNum(seOutWhsSubs.getBatNum());
						invtyTab = itd.selectInvtyTabsByTerm(invtyTab);
						if (invtyTab != null) {
							// a.compareTo(b) -1��ʾС�� 1 ��ʾ���� 0��ʾ����
							if (invtyTab.getNowStok().compareTo(seOutWhsSubs.getQty().abs()) >= 0) {
								// ����ִ����㹻�����޸Ŀ����е��ִ����ͽ�ͨ���ִ����ͽ����������йؽ����ֶ�
								itd.updateInvtyTabJiaToSellOutWhs(seOutWhsSubs);
							} else {
								isSuccess = false;
								message += "����Ϊ��" + selOutWhs.getOutWhsId() + "�ĺ������۳��ⵥ��,�ֿ⣺" + seOutWhsSubs.getWhsEncd() + 
										",���:" + seOutWhsSubs.getInvtyEncd() + ",����:" + seOutWhsSubs.getBatNum()
										+ "�Ŀ�����������㣬�޷�����\n";
								throw new RuntimeException(message);
							}
						} else {
							isSuccess = false;
							message += "����Ϊ��" + selOutWhs.getOutWhsId() + "�ĺ������۳��ⵥ��,�ֿ⣺" + seOutWhsSubs.getWhsEncd()+",�����"
									+ seOutWhsSubs.getInvtyEncd() + ",���Σ�" + seOutWhsSubs.getBatNum() + "�Ŀ�治���ڣ��޷�����\n";
							throw new RuntimeException(message);
						}
						whsId=seOutWhsSubs.getWhsEncd();
					}
					Integer whs = whsDocMapper.selectisNtPrgrGdsBitMgmtWhs(whsId);
					if(whs != null && whs==1) {
						sellOutWhsHuoWeiJian(selOutWhs.getOutWhsId(),sellOutWhsSubs);
					}
					int a = sowd.updateSellOutWhsIsNtChk(selOutWhs);
					if (a >= 1) {
						isSuccess = true;
						message += "����Ϊ��" + selOutWhs.getOutWhsId() + "�ĺ������۳��ⵥ����ɹ���\n";
					} else {
						isSuccess = false;
						message += "����Ϊ��" + selOutWhs.getOutWhsId() + "�ĺ������۳��ⵥ����ʧ�ܣ�\n";
						throw new RuntimeException(message);
					}
				} else {
					isSuccess = false;
					message += "���ݺ�Ϊ" + selOutWhs.getOutWhsId() + "�����۳��ⵥ�Ѽ��ˣ���������\n";
					throw new RuntimeException(message);
				}
			} else {
				isSuccess = false;
				message += "���ݺ�Ϊ" + selOutWhs.getOutWhsId() + "�����۳��ⵥ�ѿ���Ʊ����������\n";
				throw new RuntimeException(message);
			}
		} else {
			isSuccess = false;
			message += "����Ϊ��" + selOutWhs.getOutWhsId() + "�ĺ������۳��ⵥδ��ˣ�����Ҫ����\n";
			throw new RuntimeException(message);
		}
		map.put("isSuccess", isSuccess);
		map.put("message", message);
		return map;
	}

	private void sellOutWhsHuoWeiJia(String outWhsId, List<SellOutWhsSub> selOutWhsSubList) {
		/**
		 * ��λ�ж�
		 */
		List<MovBitTab> tabs = bitListMapper.selectInvtyGdsBitListf(outWhsId);// ��ѯ���ɵĲɹ���ⵥ���Ƿ���¼���λ��Ϣ
		if (tabs.size() == 0) {
			throw new RuntimeException("������ϸû��¼���λ��Ϣ,��˶Ժ��ٲ���");
		}
		for (SellOutWhsSub sellOutWhsSub : selOutWhsSubList) {
			MovBitTab bitTab = new MovBitTab();
			bitTab.setWhsEncd(sellOutWhsSub.getWhsEncd());
			bitTab.setInvtyEncd(sellOutWhsSub.getInvtyEncd());
			bitTab.setBatNum(sellOutWhsSub.getBatNum());
			bitTab.setSerialNum(sellOutWhsSub.getOrdrNum().toString());
			bitTab.setOrderNum(outWhsId);
			//��ѯ�õ��ݸô�����л�λ������
			MovBitTab bitTab2 = bitListMapper.selectInvtyGdsBitSum(bitTab);
			// �жϸõ��ݸô�����л�λ�������Ƿ񲻴��ڵ��ݴ��������һ��
			if (bitTab2 == null) {
				throw new RuntimeException(bitTab.getInvtyEncd() + 
						"���Σ�" + bitTab.getBatNum() + "û��¼���Ӧ�Ļ�λ��Ϣ");
			} else if (bitTab2.getQty().abs().compareTo(sellOutWhsSub.getQty().abs()) != 0) {
				throw new RuntimeException("�����" + bitTab.getInvtyEncd() + "���Σ�"
						+ bitTab.getBatNum() + " ¼��Ļ�λ���������������ƥ��,��˶Ի�λ�����ٲ���");
			}
		}
		/* ��λ��Ϣ */
		for (MovBitTab tab : tabs) {
			tab.setQty(tab.getQty().abs());
			MovBitTab whsTab = invtyNumMapper.selectMbit(tab);// ��ѯ�������Ƿ���ڸû�λ��Ϣ
			if (whsTab == null) {
				invtyNumMapper.insertMovBitTabJia(tab);// ������ʱ����
			} else {
				tab.setMovBitEncd(whsTab.getMovBitEncd());
				invtyNumMapper.updateJiaMbit(tab);// ����ʱ�޸�
			}
		}
	}
	
	private void sellOutWhsHuoWeiJian(String outWhsId, List<SellOutWhsSub> selOutWhsSubList) {
		/**
		 * ��λ�ж�
		 */
		List<MovBitTab> tabs = bitListMapper.selectInvtyGdsBitListf(outWhsId);// ��ѯ���ɵĲɹ���ⵥ���Ƿ���¼���λ��Ϣ
		if (tabs.size() == 0) {
			throw new RuntimeException("������ϸû��¼���λ��Ϣ,��˶Ժ��ٲ���");
		}
		for (SellOutWhsSub sellOutWhsSub : selOutWhsSubList) {
			MovBitTab bitTab = new MovBitTab();
			bitTab.setWhsEncd(sellOutWhsSub.getWhsEncd());
			bitTab.setInvtyEncd(sellOutWhsSub.getInvtyEncd());
			bitTab.setBatNum(sellOutWhsSub.getBatNum());
			bitTab.setSerialNum(sellOutWhsSub.getOrdrNum().toString());
			bitTab.setOrderNum(outWhsId);
			MovBitTab bitTab2 = bitListMapper.selectInvtyGdsBitSum(bitTab);// �жϸõ��ݸô�����л�λ�������Ƿ񲻴��ڵ��ݴ��������һ��
			if (bitTab2 == null) {
				throw new RuntimeException(bitTab.getInvtyEncd() + 
						"���Σ�" + bitTab.getBatNum() + "û��¼���Ӧ�Ļ�λ��Ϣ,��˶Ժ��ٲ���");
			} else if (bitTab2.getQty().abs().compareTo(sellOutWhsSub.getQty().abs()) != 0) {
				throw new RuntimeException("�����" + bitTab.getInvtyEncd() + "���Σ�"
						+ bitTab.getBatNum() + " ¼��Ļ�λ���������������ƥ��,��˶Ի�λ�����ٲ���");
			}
		}
		/* ��λ��Ϣ */
		for (MovBitTab tab : tabs) {
			tab.setQty(tab.getQty().abs());
			MovBitTab whsTab = invtyNumMapper.selectMbit(tab);// ��ѯ�������Ƿ���ڸû�λ��Ϣ
			if (whsTab == null) {
				throw new RuntimeException("�ֿ⣺" + tab.getWhsEncd() + ",�����"
						+ tab.getInvtyEncd() + ",���ţ�" + tab.getBatNum() + "�޻�λ����,��˶Ժ��ٲ���");
			} else if (whsTab.getQty().abs().compareTo(tab.getQty().abs()) == 1 || whsTab.getQty().abs().compareTo(tab.getQty().abs()) == 0) {
				tab.setMovBitEncd(whsTab.getMovBitEncd());
				invtyNumMapper.updateJianMbit(tab);// ����
			} else {
				throw new RuntimeException("�ֿ⣺" + tab.getWhsEncd() + ",�����"
						+ tab.getInvtyEncd() + ",���ţ�" + tab.getBatNum() + "��Ӧ�Ļ�λ��������");
			}
		}
	}

	// ��ӡ���۳��ⵥ
	@Override
	public String printingSellOutWhsList(Map map) {
		String resp = "";
		List<String> outWhsIdList = getList((String) map.get("outWhsId"));// ���۳��ⵥ��
		List<String> custIdList = getList((String) map.get("custId"));// �ͻ�����
		List<String> accNumList = getList((String) map.get("accNum"));// ҵ��Ա����
		List<String> invtyEncdList = getList((String) map.get("invtyEncd"));// �������
		List<String> invtyClsEncdList = getList((String) map.get("invtyClsEncd"));// ����������
		List<String> invtyCdList = getList((String) map.get("invtyCd"));// �������
		List<String> deptIdList = getList((String) map.get("deptId"));// ���ű���
		List<String> whsEncdList = getList((String) map.get("whsEncd"));// �ֿ����
		List<String> custOrdrNumList = getList((String) map.get("custOrdrNum"));// �ͻ�������
		List<String> batNumList = getList((String) map.get("batNum"));// ����
		List<String> intlBatList = getList((String) map.get("intlBat"));// ��������

		map.put("outWhsIdList", outWhsIdList);
		map.put("custIdList", custIdList);
		map.put("accNumList", accNumList);
		map.put("invtyEncdList", invtyEncdList);
		map.put("invtyClsEncdList", invtyClsEncdList);
		map.put("invtyCdList", invtyCdList);
		map.put("deptIdList", deptIdList);
		map.put("whsEncdList", whsEncdList);
		map.put("custOrdrNumList", custOrdrNumList);
		map.put("batNumList", batNumList);
		map.put("intlBatList", intlBatList);

		//�ֿ�Ȩ�޿���
		List<String> whsIdList = getList((String) map.get("whsId"));//�ֿ����
		map.put("whsIdList", whsIdList);
				
		List<zizhu> sellOutWhsList = sowd.printingSellOutWhsList(map);
		try {
//			
			resp = BaseJson.returnRespObjListAnno("purc/SellOutWhs/printingSellOutWhsList", true, "��ѯ�ɹ���", null,
					sellOutWhsList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
	}

	// ������ϸ��
	@Override
	public String querySellOutWhsByInvtyEncd(Map map) {
		String resp = "";
		List<String> outWhsIdList = getList((String) map.get("outWhsId"));// ���۳��ⵥ��
		List<String> custIdList = getList((String) map.get("custId"));// �ͻ�����
		List<String> accNumList = getList((String) map.get("accNum"));// ҵ��Ա����
		List<String> invtyEncdList = getList((String) map.get("invtyEncd"));// �������
		List<String> invtyClsEncdList = getList((String) map.get("invtyClsEncd"));// ����������
		List<String> invtyCdList = getList((String) map.get("invtyCd"));// �������
		List<String> deptIdList = getList((String) map.get("deptId"));// ���ű���
		List<String> whsEncdList = getList((String) map.get("whsEncd"));// �ֿ����
		List<String> custOrdrNumList = getList((String) map.get("custOrdrNum"));// �ͻ�������
		List<String> batNumList = getList((String) map.get("batNum"));// ����
		List<String> intlBatList = getList((String) map.get("intlBat"));// ��������

		map.put("outWhsIdList", outWhsIdList);
		map.put("custIdList", custIdList);
		map.put("accNumList", accNumList);
		map.put("invtyEncdList", invtyEncdList);
		map.put("invtyClsEncdList", invtyClsEncdList);
		map.put("invtyCdList", invtyCdList);
		map.put("deptIdList", deptIdList);
		map.put("whsEncdList", whsEncdList);
		map.put("custOrdrNumList", custOrdrNumList);
		map.put("batNumList", batNumList);
		map.put("intlBatList", intlBatList);
		List<?> poList;
		if (map.containsKey("pageNo") && map.containsKey("pageSize")) {
			if (map.get("sort") == null||map.get("sort").equals("") ||
				map.get("sortOrder") == null||map.get("sortOrder").equals("")) {
				map.put("sort","outWhsDt");
				map.put("sortOrder","desc");
			}
			poList = sowd.selectSellOutWhsByInvtyEncd(map);
			Map tableSums = sowd.selectSellOutWhsByInvtyEncdSums(map);
			if (null!=tableSums) {
				DecimalFormat df = new DecimalFormat("#,##0.00");
				for(Map.Entry entry : (Set<Entry>)tableSums.entrySet()) {
					String s = df.format((BigDecimal) entry.getValue()==null?new BigDecimal(0.00):entry.getValue());
					entry.setValue(s);
				}
			}
			int count = sowd.selectSellOutWhsByInvtyEncdCount(map);

			int pageNo = Integer.parseInt(map.get("pageNo").toString());
			int pageSize = Integer.parseInt(map.get("pageSize").toString());
			int listNum = poList.size();
			int pages = count / pageSize + 1;

			try {
				resp = BaseJson.returnRespList("purc/SellOutWhs/querySellOutWhsByInvtyEncd", true, "��ѯ�ɹ���", count,
						pageNo, pageSize, listNum, pages, poList,tableSums);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			poList = sowd.selectSellOutWhsByInvtyEncd(map);
			try {
				resp = BaseJson.returnRespList("purc/SellOutWhs/querySellOutWhsByInvtyEncd", true, "��ѯ�ɹ���", poList);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return resp;
	}
	// ������ϸ��--����
		@Override
		public String querySellOutWhsByInvtyEncdPrint(Map map) {
			
			String resp = "";
			List<String> outWhsIdList = getList((String) map.get("outWhsId"));// ���۳��ⵥ��
			List<String> custIdList = getList((String) map.get("custId"));// �ͻ�����
			List<String> accNumList = getList((String) map.get("accNum"));// ҵ��Ա����
			List<String> invtyEncdList = getList((String) map.get("invtyEncd"));// �������
			List<String> invtyClsEncdList = getList((String) map.get("invtyClsEncd"));// ����������
			List<String> invtyCdList = getList((String) map.get("invtyCd"));// �������
			List<String> deptIdList = getList((String) map.get("deptId"));// ���ű���
			List<String> whsEncdList = getList((String) map.get("whsEncd"));// �ֿ����
			List<String> custOrdrNumList = getList((String) map.get("custOrdrNum"));// �ͻ�������
			List<String> batNumList = getList((String) map.get("batNum"));// ����
			List<String> intlBatList = getList((String) map.get("intlBat"));// ��������

			map.put("outWhsIdList", outWhsIdList);
			map.put("custIdList", custIdList);
			map.put("accNumList", accNumList);
			map.put("invtyEncdList", invtyEncdList);
			map.put("invtyClsEncdList", invtyClsEncdList);
			map.put("invtyCdList", invtyCdList);
			map.put("deptIdList", deptIdList);
			map.put("whsEncdList", whsEncdList);
			map.put("custOrdrNumList", custOrdrNumList);
			map.put("batNumList", batNumList);
			map.put("intlBatList", intlBatList);
			List<?> poList;
				poList = sowd.selectSellOutWhsByInvtyEncdPrint(map);
				
				try {
					resp = BaseJson.returnRespList("purc/SellOutWhs/querySellOutWhsByInvtyEncdPrint", true, "��ѯ�ɹ���",poList);
				} catch (Exception e) {
				logger.error(e.getMessage());
				}
	
			return resp;
		}


	// �������۳��ⵥ
	public String uploadFileAddDb(MultipartFile file, int i) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		Map<String, SellOutWhs> sellOutWhsMap = null;
		if (i == 0) {
			sellOutWhsMap = uploadScoreInfo(file);
		} else if (i == 1) {
			sellOutWhsMap = uploadScoreInfoU8(file);
		} else {
			isSuccess = false;
			message = "������쳣����";
			throw new RuntimeException(message);
		}

		// ��MapתΪList��Ȼ���������븸������
		List<SellOutWhs> sellOutWhsList = sellOutWhsMap.entrySet().stream().map(e -> e.getValue())
				.collect(Collectors.toList());
		List<List<SellOutWhs>> sellOutWhsLists = Lists.partition(sellOutWhsList, 1000);

		for (List<SellOutWhs> sellOutWhs : sellOutWhsLists) {
			sowd.batchInsertSellOutWhs(sellOutWhs);
		}
		List<SellOutWhsSub> sellOutWhsSubList = new ArrayList<>();
		int flag = 0;
		for (SellOutWhs entry : sellOutWhsList) {
			flag++;
			// ���������ֱ�͸���Ĺ����ֶ�
			List<SellOutWhsSub> tempList = entry.getSellOutWhsSub();
			tempList.forEach(s -> s.setOutWhsId(entry.getOutWhsId()));
			sellOutWhsSubList.addAll(tempList);
			// �������룬ÿ���ڵ���1000������һ��
			if (sellOutWhsSubList.size() >= 1000 || sellOutWhsMap.size() == flag) {
				sowsd.insertSellOutWhsSubUpload(sellOutWhsSubList);
				sellOutWhsSubList.clear();
			}
		}

		isSuccess = true;
		message = "���۳��ⵥ����ɹ���";

		try {
			if (i == 0) {
				resp = BaseJson.returnRespObj("purc/SellOutWhs/uploadSellOutWhsFile", isSuccess, message, null);
			} else if (i == 1) {
				resp = BaseJson.returnRespObj("purc/SellOutWhs/uploadSellOutWhsFileU8", isSuccess, message, null);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// ����excle
	private Map<String, SellOutWhs> uploadScoreInfo(MultipartFile file) {
		Map<String, SellOutWhs> temp = new HashMap<>();
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

				String orderNo = GetCellData(r, "���۳��ⵥ����");
				// ����ʵ����
				SellOutWhs sellOutWhs = new SellOutWhs();
				if (temp.containsKey(orderNo)) {
					sellOutWhs = temp.get(orderNo);
				}
				// ȡ����ǰ�е�1����Ԫ�����ݣ�����װ��pursOrdrʵ��ĸ���������
				sellOutWhs.setSellTypId(GetCellData(r, "�������ͱ���"));// �������ͱ���
				sellOutWhs.setOutWhsId(orderNo); // ���۳��ⵥ����
				if (GetCellData(r, "���۳��ⵥ����") == null || GetCellData(r, "���۳��ⵥ����").equals("")) {
					sellOutWhs.setOutWhsDt(null);
				} else {
					sellOutWhs.setOutWhsDt(GetCellData(r, "���۳��ⵥ����"));// ���۳��ⵥ����
				}
				sellOutWhs.setCustId(GetCellData(r, "�ͻ�����"));// �ͻ�����
				sellOutWhs.setCustOrdrNum(GetCellData(r, "�ͻ�������"));// �ͻ�������
				sellOutWhs.setDeptId(GetCellData(r, "���ű���"));// ���ű���
				sellOutWhs.setAccNum(GetCellData(r, "ҵ��Ա����"));// �û�����
				sellOutWhs.setUserName(GetCellData(r, "ҵ��Ա����"));// �û�����
				sellOutWhs.setFormTypEncd(GetCellData(r, "�������ͱ���"));// �������ͱ���
				sellOutWhs.setBizTypId(GetCellData(r, "ҵ�����ͱ���"));// ҵ�����ͱ���
				sellOutWhs.setRecvSendCateId(GetCellData(r, "�շ�������"));// �շ�������
				sellOutWhs.setSellOrdrInd(GetCellData(r, "��Դ���ݺ�"));// ��Դ���ݺ�
//				sellOutWhs.setRtnGoodsId(GetCellData(r,"�˻�����"));//�˻�����
				sellOutWhs.setDealStat(GetCellData(r, "����״̬"));// ����״̬
				sellOutWhs.setSetupPers(GetCellData(r, "������"));// ������
				if (GetCellData(r, "����ʱ��") == null || GetCellData(r, "����ʱ��").equals("")) {
					sellOutWhs.setSetupTm(null);
				} else {
					sellOutWhs.setSetupTm(GetCellData(r, "����ʱ��").replaceAll("[^0-9:-]", " "));// ����ʱ��
				}
				sellOutWhs.setMdfr(GetCellData(r, "�޸���")); // �޸���
				if (GetCellData(r, "�޸�ʱ��") == null || GetCellData(r, "�޸�ʱ��").equals("")) {
					sellOutWhs.setModiTm(null);
				} else {
					sellOutWhs.setModiTm(GetCellData(r, "�޸�ʱ��").replaceAll("[^0-9:-]", " "));// �޸�ʱ��
				}
				sellOutWhs.setIsNtChk(new Double(GetCellData(r, "�Ƿ����")).intValue());// �Ƿ����
				sellOutWhs.setChkr(GetCellData(r, "�����"));// �����
				if (GetCellData(r, "���ʱ��") == null || GetCellData(r, "���ʱ��").equals("")) {
					sellOutWhs.setChkTm(null);
				} else {
					sellOutWhs.setChkTm(GetCellData(r, "���ʱ��").replaceAll("[^0-9:-]", " "));// ���ʱ��
				}
				sellOutWhs.setIsNtBookEntry(new Double(GetCellData(r, "�Ƿ����")).intValue());// �Ƿ����
				sellOutWhs.setBookEntryPers(GetCellData(r, "������"));// ������
				if (GetCellData(r, "����ʱ��") == null || GetCellData(r, "����ʱ��").equals("")) {
					sellOutWhs.setBookEntryTm(null);
				} else {
					sellOutWhs.setBookEntryTm(GetCellData(r, "����ʱ��").replaceAll("[^0-9:-]", " "));// ����ʱ��
				}
				sellOutWhs.setIsNtBllg(new Double(GetCellData(r, "�Ƿ�Ʊ")).intValue());// �Ƿ�Ʊ
				sellOutWhs.setIsNtStl(new Double(GetCellData(r, "�Ƿ����")).intValue());// �Ƿ����
				sellOutWhs.setIsNtRtnGood(new Double(GetCellData(r, "�Ƿ��˻�")).intValue());// �Ƿ��˻�
				sellOutWhs.setMemo(GetCellData(r, "����ע"));// ����ע
				sellOutWhs.setIsNtMakeVouch(new Double(GetCellData(r, "�Ƿ�����ƾ֤")).intValue());
				List<SellOutWhsSub> sellOutWhsSubList = sellOutWhs.getSellOutWhsSub();// ���۳��ⵥ�ӱ�
				if (sellOutWhsSubList == null) {
					sellOutWhsSubList = new ArrayList<>();
				}
				SellOutWhsSub sellOutWhsSub = new SellOutWhsSub();
				sellOutWhsSub.setWhsEncd(GetCellData(r, "�ֿ����"));// �ֿ����
//				sellOutWhsSub.setWhsNm(GetCellData(r, "�ֿ�����"));// �ֿ�����
				sellOutWhsSub.setInvtyEncd(GetCellData(r, "�������"));// �������
				sellOutWhsSub.setQty(GetBigDecimal(GetCellData(r, "����"), 8));// 8��ʾС��λ�� //����
				sellOutWhsSub.setCntnTaxUprc(GetBigDecimal(GetCellData(r, "��˰����"), 8));// 8��ʾС��λ�� //��˰����
				sellOutWhsSub.setNoTaxUprc(GetBigDecimal(GetCellData(r, "��˰����"), 8));// 8��ʾС��λ�� //��˰����
				sellOutWhsSub.setNoTaxAmt(GetBigDecimal(GetCellData(r, "��˰���"), 8));// 8��ʾС��λ�� //��˰���
				sellOutWhsSub.setTaxAmt(GetBigDecimal(GetCellData(r, "˰��"), 8));// 8��ʾС��λ�� //˰��
				sellOutWhsSub.setPrcTaxSum(GetBigDecimal(GetCellData(r, "��˰�ϼ�"), 8));// 8��ʾС��λ�� //��˰�ϼ�
				sellOutWhsSub.setTaxRate(GetBigDecimal(GetCellData(r, "˰��"), 8));// 8��ʾС��λ�� //˰��
				sellOutWhsSub.setBxQty(GetBigDecimal(GetCellData(r, "����"), 8));// 8��ʾС��λ�� //����
				sellOutWhsSub.setBxRule(GetBigDecimal(GetCellData(r, "���"), 8));// 8��ʾС��λ�� //���
				sellOutWhsSub.setBatNum(GetCellData(r, "����"));// ����
				sellOutWhsSub.setIntlBat(GetCellData(r, "��������"));// ��������
				if (GetCellData(r, "��������") == null || GetCellData(r, "��������").equals("")) {
					sellOutWhsSub.setPrdcDt(null);
				} else {
					sellOutWhsSub.setPrdcDt(GetCellData(r, "��������"));// ��������
				}
				sellOutWhsSub.setBaoZhiQi(new Double(GetCellData(r, "������")).intValue());// ������
				if (GetCellData(r, "ʧЧ����") == null || GetCellData(r, "ʧЧ����").equals("")) {
					sellOutWhsSub.setInvldtnDt(null);
				} else {
					sellOutWhsSub.setInvldtnDt(GetCellData(r, "ʧЧ����"));// ʧЧ����
				}
				sellOutWhsSub.setProjEncd(GetCellData(r, "��Ŀ����"));// ��Ŀ����
//				sellOutWhsSub.setProjNm(GetCellData(r, "��Ŀ����"));// ��Ŀ����
				sellOutWhsSub.setGdsBitEncd(GetCellData(r, "��λ����"));// ��λ����
				sellOutWhsSub.setRegnEncd(GetCellData(r, "�������"));// �������
				sellOutWhsSub.setMemo(GetCellData(r, "�ӱ�ע"));// �ӱ�ע

				sellOutWhsSubList.add(sellOutWhsSub);
				sellOutWhs.setSellOutWhsSub(sellOutWhsSubList);
				temp.put(orderNo, sellOutWhs);
			}
			fileIn.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e + ",�ļ��ĵ�" + j + "�е����ʽ�����޷�����!" + e.getMessage());
		}
		return temp;
	}

	// ����excle
	private Map<String, SellOutWhs> uploadScoreInfoU8(MultipartFile file) {
		Map<String, SellOutWhs> temp = new HashMap<>();
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
				String orderNo = GetCellData(r, "���ⵥ��");
				// ����ʵ����
				SellOutWhs sellOutWhs = new SellOutWhs();
				if (temp.containsKey(orderNo)) {
					sellOutWhs = temp.get(orderNo);
				}
				// ȡ����ǰ�е�1����Ԫ�����ݣ�����װ��pursOrdrʵ��ĸ���������
				if (GetCellData(r, "ҵ������") != null && GetCellData(r, "ҵ������").equals("��ͨ����")) {
					sellOutWhs.setSellTypId("1");// �������ͱ���

				} else if (GetCellData(r, "ҵ������") != null && GetCellData(r, "ҵ������").equals("ί�д���")) {
					sellOutWhs.setSellTypId("2");// �������ͱ���

				}
				sellOutWhs.setOutWhsId(orderNo); // ���۳��ⵥ����
				if (GetCellData(r, "��������") == null || GetCellData(r, "��������").equals("")) {
					sellOutWhs.setOutWhsDt(null);
				} else {
					sellOutWhs.setOutWhsDt(GetCellData(r, "��������"));// ���۳��ⵥ����
				}
				sellOutWhs.setCustId(GetCellData(r, "�ͻ�����"));// �ͻ�����
				sellOutWhs.setCustOrdrNum(GetCellData(r, "�ͻ�������"));// �ͻ�������
				sellOutWhs.setDeptId(GetCellData(r, "���ű���"));// ���ű���
				sellOutWhs.setAccNum(GetCellData(r, "ҵ��Ա����"));// �û�����
				sellOutWhs.setUserName(GetCellData(r, "ҵ��Ա"));// �û�����
				
				
				if (GetCellData(r, "��������") != null && GetCellData(r, "��������").equals("B2B")) {
					sellOutWhs.setBizTypId("1");
					sellOutWhs.setRecvSendCateId("7");// �շ�������
				} else if (GetCellData(r, "��������") != null && GetCellData(r, "��������").equals("B2C")) {
					sellOutWhs.setBizTypId("2");
					sellOutWhs.setRecvSendCateId("6");// �շ�������

				}
				// �Ƿ�ί�д��� //0��ʾ���۳��� 1��ʾί�д���
				if (GetCellData(r, "ҵ������") != null && GetCellData(r, "ҵ������").equals("��ͨ����")) {
					sellOutWhs.setIsNtEntrsAgn(0);

				} else if (GetCellData(r, "ҵ������") != null && GetCellData(r, "ҵ������").equals("ί�д���")) {
					sellOutWhs.setIsNtEntrsAgn(1);
				}
				sellOutWhs.setSellOrdrInd(GetCellData(r, "��������"));// ��Դ���ݺ�
//				sellOutWhs.setRtnGoodsId(GetCellData(r,"�˻�����"));//�˻�����
				sellOutWhs.setDealStat("������");// ����״̬
				sellOutWhs.setSetupPers(GetCellData(r, "�Ƶ���"));// ������
				if (GetCellData(r, "�Ƶ�ʱ��") == null || GetCellData(r, "�Ƶ�ʱ��").equals("")) {
					sellOutWhs.setSetupTm(null);
				} else {
					sellOutWhs.setSetupTm(GetCellData(r, "�Ƶ�ʱ��").replaceAll("[^0-9:-]", " "));// ����ʱ��
				}
				sellOutWhs.setMdfr(GetCellData(r, "�޸���")); // �޸���
				if (GetCellData(r, "�޸�ʱ��") == null || GetCellData(r, "�޸�ʱ��").equals("")) {
					sellOutWhs.setModiTm(null);
				} else {
					sellOutWhs.setModiTm(GetCellData(r, "�޸�ʱ��").replaceAll("[^0-9:-]", " "));// �޸�ʱ��
				}
				sellOutWhs.setIsNtChk(1);// �Ƿ����
				sellOutWhs.setChkr(GetCellData(r, "�����"));// �����
				if (GetCellData(r, "���ʱ��") == null || GetCellData(r, "���ʱ��").equals("")) {
					sellOutWhs.setChkTm(null);
				} else {
					sellOutWhs.setChkTm(GetCellData(r, "���ʱ��").replaceAll("[^0-9:-]", " "));// ���ʱ��
				}
				sellOutWhs.setIsNtBookEntry(0);// �Ƿ����
				sellOutWhs.setBookEntryPers(GetCellData(r, "������"));// ������
//				if (GetCellData(r, "����ʱ��") == null || GetCellData(r, "����ʱ��").equals("")) {
//					sellOutWhs.setBookEntryTm(null);
//				} else {
//					sellOutWhs.setBookEntryTm(GetCellData(r, "����ʱ��").replaceAll("[^0-9:-]", " "));// ����ʱ��
//				}
				sellOutWhs.setIsNtBllg(0);// �Ƿ�Ʊ
				sellOutWhs.setIsNtStl(0);// �Ƿ����
				sellOutWhs.setIsNtRtnGood(
						GetBigDecimal(GetCellData(r, "����"), 8).compareTo(BigDecimal.ZERO) == -1 ? 1 : 0);// �Ƿ��˻�
				if (sellOutWhs.getIsNtRtnGood()==0) {
					sellOutWhs.setFormTypEncd("009");// �������ͱ���

				} else if (sellOutWhs.getIsNtRtnGood()==1) {
					sellOutWhs.setFormTypEncd("010");// �������ͱ���
				}
				sellOutWhs.setMemo(GetCellData(r, "��ע"));// ����ע
				sellOutWhs.setIsNtMakeVouch(0);
				List<SellOutWhsSub> sellOutWhsSubList = sellOutWhs.getSellOutWhsSub();// ���۳��ⵥ�ӱ�
				if (sellOutWhsSubList == null) {
					sellOutWhsSubList = new ArrayList<>();
				}
				SellOutWhsSub sellOutWhsSub = new SellOutWhsSub();
//				sellOutWhsSub.setOrdrNum(Long.parseLong(GetCellData(r, "���")));
				sellOutWhsSub.setWhsEncd(GetCellData(r, "�ֿ����"));// �ֿ����
				sellOutWhsSub.setInvtyEncd(GetCellData(r, "�������"));// �������
				sellOutWhsSub.setQty(GetBigDecimal(GetCellData(r, "����"), 8));// 8��ʾС��λ�� //����
			

//				sellOutWhsSub.setCntnTaxUprc(GetBigDecimal(GetCellData(r, "����"), 8));// 8��ʾС��λ�� //��˰����
				sellOutWhsSub.setNoTaxUprc(GetBigDecimal(GetCellData(r, "����"), 8));// 8��ʾС��λ�� //��˰����
				sellOutWhsSub.setNoTaxAmt(GetBigDecimal(GetCellData(r, "���"), 8));// 8��ʾС��λ�� //��˰���
//				sellOutWhsSub.setTaxAmt(GetBigDecimal(GetCellData(r, "˰��"), 8));// 8��ʾС��λ�� //˰��
//				sellOutWhsSub.setPrcTaxSum(GetBigDecimal(GetCellData(r, "���"), 8));// 8��ʾС��λ�� //��˰�ϼ�
//				sellOutWhsSub.setTaxRate(GetBigDecimal(GetCellData(r, "˰��"), 8));// 8��ʾС��λ�� //˰��
				sellOutWhsSub.setIsNtRtnGoods(
						GetBigDecimal(GetCellData(r, "����"), 8).compareTo(BigDecimal.ZERO) == -1 ? 1 : 0);// �Ƿ��˻�
				sellOutWhsSub.setBxQty(GetBigDecimal(GetCellData(r, "����"), 8));// 8��ʾС��λ�� //����
				sellOutWhsSub.setBxRule(GetBigDecimal(GetCellData(r, "���"), 8));// 8��ʾС��λ�� //���
				sellOutWhsSub.setBatNum(GetCellData(r, "����"));// ����
				sellOutWhsSub.setIntlBat(GetCellData(r, "��������"));// ��������
				if (GetCellData(r, "��������") == null || GetCellData(r, "��������").equals("")) {
					sellOutWhsSub.setPrdcDt(null);
				} else {
					sellOutWhsSub.setPrdcDt(GetCellData(r, "��������"));// ��������
				}
				sellOutWhsSub.setBaoZhiQi(new Double(GetCellData(r, "������")).intValue());// ������
				if (GetCellData(r, "ʧЧ����") == null || GetCellData(r, "ʧЧ����").equals("")) {
					sellOutWhsSub.setInvldtnDt(null);
				} else {
					sellOutWhsSub.setInvldtnDt(GetCellData(r, "ʧЧ����"));// ʧЧ����
				}
				sellOutWhsSub.setProjEncd(GetCellData(r, "��Ŀ����"));// ��Ŀ����
				sellOutWhsSub.setProjNm(GetCellData(r, "��Ŀ"));// ��Ŀ����
//				sellOutWhsSub.setGdsBitEncd(GetCellData(r, "��λ����"));// ��λ����
//				sellOutWhsSub.setRegnEncd(GetCellData(r, "�������"));// �������
				sellOutWhsSub.setToOrdrNum(Long.parseLong(GetCellData(r, "�������ӱ�id")));
				sellOutWhsSub.setMemo(GetCellData(r, "���屸ע"));// �ӱ�ע

				sellOutWhsSubList.add(sellOutWhsSub);
				sellOutWhs.setSellOutWhsSub(sellOutWhsSubList);
				temp.put(orderNo, sellOutWhs);
			}
			fileIn.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e + ",�ļ��ĵ�" + j + "�е����ʽ�����޷�����!" + e.getMessage());
		}
		return temp;
	}
	//��ҳ+�����б�չʾ
	@Override
	public String querySellOutWhsListOrderBy(Map map) {
		String resp = "";
		List<String> outWhsIdList = getList((String) map.get("outWhsId"));// ���۳��ⵥ��
		List<String> custIdList = getList((String) map.get("custId"));// �ͻ�����
		List<String> accNumList = getList((String) map.get("accNum"));// ҵ��Ա����
		List<String> invtyEncdList = getList((String) map.get("invtyEncd"));// �������
		List<String> invtyClsEncdList = getList((String) map.get("invtyClsEncd"));// ����������
		List<String> invtyCdList = getList((String) map.get("invtyCd"));// �������
		List<String> deptIdList = getList((String) map.get("deptId"));// ���ű���
		List<String> whsEncdList = getList((String) map.get("whsEncd"));// �ֿ����
		List<String> custOrdrNumList = getList((String) map.get("custOrdrNum"));// �ͻ�������
		List<String> batNumList = getList((String) map.get("batNum"));// ����
		List<String> intlBatList = getList((String) map.get("intlBat"));// ��������
		List<String> expressNumList = getList((String)map.get("expressNum"));

		map.put("outWhsIdList", outWhsIdList);
		map.put("custIdList", custIdList);
		map.put("accNumList", accNumList);
		map.put("invtyEncdList", invtyEncdList);
		map.put("invtyClsEncdList", invtyClsEncdList);
		map.put("invtyCdList", invtyCdList);
		map.put("deptIdList", deptIdList);
		map.put("whsEncdList", whsEncdList);
		map.put("custOrdrNumList", custOrdrNumList);
		map.put("batNumList", batNumList);
		map.put("intlBatList", intlBatList);
		map.put("expressNums", expressNumList);
		
		//�ֿ�Ȩ�޿���
		List<String> whsIdList = getList((String) map.get("whsId"));//�ֿ����
		map.put("whsIdList", whsIdList);
		
		List<?> poList;
		if (map.get("sort") == null||map.get("sort").equals("") ||
				map.get("sortOrder") == null||map.get("sortOrder").equals("")) {
			map.put("sort","sow.out_whs_dt");
			map.put("sortOrder","desc");
		}
			poList = sowd.selectSellOutWhsListOrderBy(map);
			Map tableSums = sowd.selectSellOutWhsListSums(map);
			if (null!=tableSums) {
				DecimalFormat df = new DecimalFormat("#,##0.00");
				for(Map.Entry entry : (Set<Entry>)tableSums.entrySet()) {
					String s = df.format((BigDecimal) entry.getValue()==null?new BigDecimal(0.00):entry.getValue());
					entry.setValue(s);
				}
			}
		
		int count = sowd.selectSellOutWhsCount(map);
		int pageNo = Integer.parseInt(map.get("pageNo").toString());
		int pageSize = Integer.parseInt(map.get("pageSize").toString());
		int listNum = poList.size();
		int pages = count / pageSize + 1;

		try {
			resp = BaseJson.returnRespList("purc/SellOutWhs/querySellOutWhsListOrderBy", true, "��ѯ�ɹ���", count, pageNo,
					pageSize, listNum, pages, poList,tableSums);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}
	
	public static class zizhu {
		// ����
		@JsonProperty("�������")
		public String outWhsId;//�������
		@JsonProperty("��������")
	    public String outWhsDt;//��������
//		@JsonProperty("��������1")
//	    public String outWhsDt1;//��������
//		@JsonProperty("��������2")
//	    public String outWhsDt2;//��������
		@JsonProperty("�������ͱ���")
	    public String formTypEncd;//�������ͱ���
		@JsonProperty("������������")
	    public String formTypName;//������������
		@JsonProperty("�û�����")
	    public String accNum;//�û�����
		@JsonProperty("�û�����")
	    public String userName;//�û�����
		@JsonProperty("���ű���")
	    public String deptId;//���ű���
		@JsonProperty("��������")
	    public String deptName;//��������
		@JsonProperty("�ͻ�����")
	    public String custId;//�ͻ�����
		@JsonProperty("�ͻ�����")
	    public String custNm;//�ͻ�����
		@JsonProperty("ҵ�����ͱ���")
	    public String bizTypId;//ҵ�����ͱ���
		@JsonProperty("ҵ����������")
	    public String bizTypNm;//ҵ����������
		@JsonProperty("�������ͱ���")
	    public String sellTypId;//�������ͱ���
		@JsonProperty("������������")
	    public String sellTypNm;//������������
		@JsonProperty("�շ����ͱ���")
	    public String recvSendCateId;//�շ����ͱ���
		@JsonProperty("�շ��������")
	    public String recvSendCateNm;//�շ��������
		@JsonProperty("��������ͱ���")
	    public String outIntoWhsTypId;//��������ͱ���
		@JsonProperty("������������")
	    public String outIntoWhsTypNm;//������������
		@JsonProperty("���۶�����ʶ")
	    public String sellOrdrInd;//���۶�����ʶ
		@JsonProperty("�Ƿ����")
	    public Integer isNtChk;//�Ƿ����
		@JsonProperty("�����")
	    public String chkr;//�����
		@JsonProperty("���ʱ��")
	    public String chkTm;//���ʱ��
		@JsonProperty("�Ƿ����")
	    public Integer isNtBookEntry;//�Ƿ����
		@JsonProperty("������")
	    public String bookEntryPers;//������
		@JsonProperty("����ʱ��")
	    public String bookEntryTm;//����ʱ��
		@JsonProperty("������")
	    public String setupPers;//������
		@JsonProperty("����ʱ��")
	    public String setupTm;//����ʱ��
		@JsonProperty("�޸���")
	    public String mdfr;//�޸���
		@JsonProperty("�޸�ʱ��")
	    public String modiTm;//�޸�ʱ��
		@JsonProperty("�Ƿ�ί�д���")
	    public Integer isNtEntrsAgn;//�Ƿ�ί�д��� //0��ʾ���۳���  1��ʾί�д��� 
		@JsonProperty("����״̬")
	    public String dealStat;//����״̬��1.������� 0.�����У�
		@JsonProperty("��ͷ��ע")
	    public String memo;//��ע
		@JsonProperty("�˻�������")
	    public String rtnGoodsId;//�˻������루��ʱ���ã�����sellOrdrInd��
		@JsonProperty("�Ƿ�Ʊ")
	    public Integer isNtBllg;//�Ƿ�Ʊ
		@JsonProperty("�Ƿ����")
	    public Integer isNtStl;//�Ƿ����
		@JsonProperty("�ͻ�������")
	    public String custOrdrNum;//�ͻ�������
		@JsonProperty("�Ƿ����˻�")
	    public Integer isNtRtnGood;//�Ƿ����˻�
		@JsonProperty("���ձ�ע")
	    public String returnMemo;//���ձ�ע
		@JsonProperty("��Դ�������ͱ���")
	    public String toFormTypEncd;//��Դ�������ͱ���
		@JsonProperty("�Ƿ�����ƾ֤")
	    public Integer isNtMakeVouch;//�Ƿ�����ƾ֤
		@JsonProperty("��ƾ֤��")
	    public String makVouchPers;//��ƾ֤��
		@JsonProperty("��ƾ֤ʱ��")
	    public String makVouchTm;//��ƾ֤ʱ��
	    //�ӱ�
		@JsonProperty("���")
	    public Long ordrNum;//���
		@JsonProperty("�ֿ����")
	    public String whsEncd;//�ֿ����
		@JsonProperty("�ֿ�����")
	    public String whsNm;//�ֿ�����
		@JsonProperty("�������")
	    public String invtyEncd;//�������
		@JsonProperty("�������")
	    public String invtyNm;//�������
		@JsonProperty("����ͺ�")
	    public String spcModel;//����ͺ�
		@JsonProperty("�������")
	    public String invtyCd;//�������
		@JsonProperty("���")
	    public BigDecimal bxRule;//���
		@JsonProperty("����")
	    public BigDecimal qty;//����
		@JsonProperty("����")
	    public BigDecimal bxQty;//����
		@JsonProperty("��˰����")
	    public BigDecimal cntnTaxUprc;//��˰����
		@JsonProperty("��˰�ϼ�")
	    public BigDecimal prcTaxSum;//��˰�ϼ�
		@JsonProperty("��˰����")
	    public BigDecimal noTaxUprc;//��˰����
		@JsonProperty("��˰���")
	    public BigDecimal noTaxAmt;//��˰���
		@JsonProperty("˰��")
	    public BigDecimal taxAmt;//˰��
		@JsonProperty("˰��")
	    public BigDecimal taxRate;//˰��
		@JsonProperty("����")
	    public String batNum;//����
		@JsonProperty("��������")
	    public String intlBat;//��������
		@JsonProperty("������")
	    public Integer baoZhiQi;//������
		@JsonProperty("��������")
		public String prdcDt;//��������
		@JsonProperty("ʧЧ����")
	    public String invldtnDt;//ʧЧ����
		@JsonProperty("�Ƿ���Ʒ")
	    public Integer isComplimentary;//�Ƿ���Ʒ
		@JsonProperty("�Ƿ��˻�")
	    public Integer isNtRtnGoods;//�Ƿ��˻�
		@JsonProperty("��������")
	    public BigDecimal returnQty;//��������
//		@JsonProperty("��λ����")
//	    public String gdsBitEncd;//��λ����--------
		@JsonProperty("��Ŀ����")
	    public String projEncd;//��Ŀ����
		@JsonProperty("��Ŀ����")
	    public String projNm;//��Ŀ����
	   //�����������ֶΡ�������λ���ơ��ֿ�����
		
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
	    public String measrCorpId;//������λ����
		@JsonProperty("������λ����")
	    public String measrCorpNm;//������λ����
		@JsonProperty("��Ӧ������")
	    public String crspdBarCd;//��Ӧ������
//		@JsonProperty("����������")
//	    public String invtyClsEncd;//�����������
//		@JsonProperty("�����������")
//	    public String invtyClsNm;//�����������
//		@JsonProperty("�������")
//	    public String regnEncd;//�������--------
		@JsonProperty("��Դ�����ӱ����")
	    public Long toOrdrNum;//��Դ�����ӱ��ʶ
		@JsonProperty("��ת����")
	    public BigDecimal carrOvrUprc;//��ת����
		@JsonProperty("��ת���")
	    public BigDecimal carrOvrAmt;//��ת���
		@JsonProperty("��ת����reba")
	    public BigDecimal rebaUprc;//��ת����
		@JsonProperty("��ת���reba")
	    public BigDecimal rebaAmt;//��ת���
		@JsonProperty("���屸ע")
	    public String memos;//���屸ע
	}
}
