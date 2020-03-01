package com.px.mis.account.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;
import com.px.mis.account.dao.SellComnInvDao;
import com.px.mis.account.dao.SellComnInvSubDao;
import com.px.mis.account.entity.SellComnInv;
import com.px.mis.account.entity.SellComnInvSub;
import com.px.mis.account.entity.PursComnInvForU8.U8PursComnInvResponse;
import com.px.mis.account.entity.SellComnInvForU8.U8SellComnInv;
import com.px.mis.account.entity.SellComnInvForU8.U8SellComnInvResponse;
import com.px.mis.account.entity.SellComnInvForU8.U8SellComnInvSub;
import com.px.mis.account.entity.SellComnInvForU8.U8SellComnInvTable;
import com.px.mis.account.service.SellComnInvService;
import com.px.mis.purc.dao.CustDocDao;
import com.px.mis.purc.dao.EntrsAgnStlDao;
import com.px.mis.purc.dao.EntrsAgnStlSubDao;
import com.px.mis.purc.dao.RtnGoodsDao;
import com.px.mis.purc.dao.RtnGoodsSubDao;
import com.px.mis.purc.dao.SellSnglDao;
import com.px.mis.purc.dao.SellSnglSubDao;
import com.px.mis.purc.entity.CustCls;
import com.px.mis.purc.entity.CustDoc;
import com.px.mis.purc.entity.EntrsAgnStl;
import com.px.mis.purc.entity.EntrsAgnStlSub;
import com.px.mis.purc.entity.RtnGoodsSub;
import com.px.mis.purc.entity.SellSngl;
import com.px.mis.purc.entity.SellSnglSub;
import com.px.mis.util.BaseJson;
import com.px.mis.util.GetOrderNo;
import com.px.mis.util.JacksonUtil;
import com.px.mis.util.poiTool;

//������ͨ��Ʊ����
@Service
@Transactional
public class SellComnInvServiceImpl<E> extends poiTool implements SellComnInvService {
	@Autowired
	private SellComnInvDao sellComnInvDao;
	@Autowired
	private CustDocDao custDocDao;
	@Autowired
	private SellComnInvSubDao sellComnInvSubDao;
	@Autowired
	private SellSnglDao sellSnglDao;// ���۵�����
	@Autowired
	private SellSnglSubDao sellSnglSubDao;// ���۵��ӱ�
	@Autowired
	private RtnGoodsDao rtnGoodsDao;// �˻�������
	@Autowired
	private RtnGoodsSubDao rtnGoodsSubDao;// �˻����ӱ�
	@Autowired
	private EntrsAgnStlDao entrsAgnStlDao;// ί�д������㵥����
	@Autowired
	private EntrsAgnStlSubDao entrsAgnStlSubDao;// ί�д������㵥�ӱ�
	@Autowired
	private GetOrderNo getOrderNo;// ������

	// ����������ͨ��Ʊ
	@Override
	public ObjectNode addSellComnInv(SellComnInv sellComnInv, String userId, String loginTime) {
		ObjectNode on = null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sellInvNum = sellComnInv.getSellInvNum();
		if (sellInvNum == null || sellInvNum.equals("")) {
			String number = getOrderNo.getSeqNo("XSFP", userId, loginTime);
			if (sellComnInvDao.selectSellComnInvBySellInvNum(number) != null) {
				on.put("isSuccess", false);
				on.put("message", "���۷�Ʊ��" + sellInvNum + "�Ѵ��ڣ�����ʧ�ܣ�");
			} else {
				sellComnInv.setSellInvNum(number);
				int insertResult = sellComnInvDao.insertSellComnInv(sellComnInv);
				if (insertResult == 1) {
					List<SellComnInvSub> sellSubList = sellComnInv.getSellComnInvSubList();
					BigDecimal totalAmt=BigDecimal.ZERO;
					for (SellComnInvSub sellComnInvSub : sellSubList) {
						sellComnInvSub.setSellInvNum(sellComnInv.getSellInvNum());
						totalAmt=totalAmt.add(Optional.ofNullable(sellComnInvSub.getNoTaxAmt()).orElse(BigDecimal.ZERO));
					}
					if (totalAmt.compareTo(BigDecimal.ZERO)>0) {
						//�˴���ռ����ϵ���ֶ�,���������Ʊ,������0Ϊ����,���С��0Ϊ����
						sellComnInv.setColor("��");
					}else if (totalAmt.compareTo(BigDecimal.ZERO)<0) {
						sellComnInv.setColor("��");
					}
					sellComnInvDao.updateSellComnInvBySellInvNum(sellComnInv);
					sellComnInvSubDao.insertSellComnInvSubList(sellSubList);
					on.put("isSuccess", true);
					on.put("message", "���۷�Ʊ�����ɹ�");
				} else {
					on.put("isSuccess", false);
					on.put("message", "���۷�Ʊ����ʧ��");
				}
			}
		} else {
			if (sellComnInvDao.selectSellComnInvBySellInvNum(sellInvNum) != null) {
				on.put("isSuccess", false);
				on.put("message", "���۷�Ʊ��" + sellInvNum + "�Ѵ��ڣ�����ʧ�ܣ�");
			} else {
				int insertResult = sellComnInvDao.insertSellComnInv(sellComnInv);
				if (insertResult == 1) {
					List<SellComnInvSub> sellSubList = sellComnInv.getSellComnInvSubList();
					BigDecimal totalAmt=BigDecimal.ZERO;
					for (SellComnInvSub sellComnInvSub : sellSubList) {
						sellComnInvSub.setSellInvNum(sellInvNum);
						totalAmt=totalAmt.add(Optional.ofNullable(sellComnInvSub.getNoTaxAmt()).orElse(BigDecimal.ZERO));
					}
					if (totalAmt.compareTo(BigDecimal.ZERO)>0) {
						//�˴���ռ����ϵ���ֶ�,���������Ʊ,������0Ϊ����,���С��0Ϊ����
						sellComnInv.setColor("��");
					}else if (totalAmt.compareTo(BigDecimal.ZERO)<0) {
						sellComnInv.setColor("��");
					}
					sellComnInvDao.updateSellComnInvBySellInvNum(sellComnInv);
					sellComnInvSubDao.insertSellComnInvSubList(sellSubList);
					on.put("isSuccess", true);
					on.put("message", "���۷�Ʊ�����ɹ�");
				} else {
					on.put("isSuccess", false);
					on.put("message", "���۷�Ʊ����ʧ��");
				}
			}
		}
		return on;
	}

	@Override
	public ObjectNode editSellComnInv(SellComnInv sellComnInv, List<SellComnInvSub> sellComnInvSubList) {
		ObjectNode on = null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sellInvNum = sellComnInv.getSellInvNum();
		if (sellInvNum == null || sellInvNum.equals("")) {
			on.put("isSuccess", false);
			on.put("message", "���۷�Ʊ�Ų���Ϊ��,����ʧ��!");
		} else if (sellComnInvDao.selectSellComnInvBySellInvNum(sellInvNum) == null) {
			on.put("isSuccess", false);
			on.put("message", "���۷�Ʊ��" + sellInvNum + "�����ڣ�����ʧ�ܣ�");
		} else if (custDocDao.selectCustDocByCustId(sellComnInv.getCustId()) == null) {
			on.put("isSuccess", false);
			on.put("message", "�ͻ����" + sellComnInv.getCustId() + "�����ڣ�����ʧ�ܣ�");
		} else {
			int updateResult = sellComnInvDao.updateSellComnInvBySellInvNum(sellComnInv);
			int deleteResult = sellComnInvSubDao.deleteSellComnInvSubBySellInvNum(sellInvNum);
			if (updateResult == 1 && deleteResult >= 1) {
				for (SellComnInvSub sellComnInvSub : sellComnInvSubList) {
					sellComnInvSub.setSellInvNum(sellInvNum);
				}
				sellComnInvSubDao.insertSellComnInvSubList(sellComnInvSubList);
				on.put("isSuccess", true);
				on.put("message", "���۷�Ʊ���³ɹ�");
			} else {
				on.put("isSuccess", false);
				on.put("message", "���۷�Ʊ����ʧ��");
			}
		}
		return on;
	}

	@Override
	public ObjectNode deleteSellComnInvBySellInvNum(String sellInvNum) {
		ObjectNode on = null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		int a = sellComnInvDao.deleteSellComnInvBySellInvNum(sellInvNum);
		int b = sellComnInvSubDao.deleteSellComnInvSubBySellInvNum(sellInvNum);
		if (a >= 1 && b >= 1) {
			on.put("isSuccess", true);
			on.put("message", "ɾ���ɹ�");
		} else if (a == 0 || b == 0) {
			on.put("isSuccess", false);
			on.put("message", "û��Ҫɾ��������");
		} else {
			on.put("isSuccess", false);
			on.put("message", "ɾ��ʧ��");
		}
		return on;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public String selectSellComnInvList(Map map) {
		String resp = "";
		// List<String> invtyClsEncdList = getList((String) map.get("invtyClsEncd"));//
		// ����������
		List<String> custIdList = getList((String) map.get("custId"));// �ͻ�����
		// map.put("invtyClsEncdList", invtyClsEncdList);
		map.put("custIdList", custIdList);

		List<SellComnInv> list = sellComnInvDao.selectSellComnInvList(map);
		int count = sellComnInvDao.selectSellComnInvCount(map);
		int listNum = 0;
		if (list != null) {
			listNum = list.size();
		}
		try {
			int pageNo = (int) map.get("pageNo");
			int pageSize = (int) map.get("pageSize");
			int pages = (count + pageSize - 1) / pageSize;
			resp = BaseJson.returnRespList("/account/SellComnInv/selectSellComnInvList", true, "��ѯ�ɹ���", count, pageNo,
					pageSize, listNum, pages, list);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// ����ɾ��
	@Override
	public String deleteSellComnInvList(String sellInvNum) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			List<String> lists = getList(sellInvNum);
			List<String> lists2 = new ArrayList<>();
			List<String> lists3 = new ArrayList<>();
			for (String list : lists) {
				if (sellComnInvDao.selectSellComnInvIsNtChk(list) == 0) {
					lists2.add(list);
				} else {
					lists3.add(list);
				}
			}
			if (lists2.size() > 0) {
				int a = sellComnInvDao.deleteSellComnInvList(lists2);
				;
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
			resp = BaseJson.returnRespObj("/account/SellComnInv/deleteSellComnInvList", isSuccess, message, null);
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

	// ������ѯ������ͨ��Ʊ
	@Override
	public String selectSellComnInvBySellInvNum(String sellInvNum) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			SellComnInv sellComnInv = sellComnInvDao.selectSellComnInvBySellInvNum(sellInvNum);
			if (sellComnInv != null) {
				isSuccess = true;
				message = "��ѯ�ɹ���";
			} else {
				isSuccess = false;
				message = "���" + sellInvNum + "�����ڣ�";
			}
			resp = BaseJson.returnRespObj("/account/SellComnInv/selectSellComnInvById", isSuccess, message,
					sellComnInv);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// ��˲ɹ���Ʊ
	@Override
	public Map<String, Object> updateSellComnInvIsNtChkList(SellComnInv sellComnInv) {
		StringBuilder message = new StringBuilder();
		Boolean isSuccess = true;
		Map<String, Object> map = new HashMap<>();
		try {
			if (sellComnInv.getIsNtChk() == 1) {
				message.append(updateSellComnInvIsNtChkOK(sellComnInv).get("message"));
			} else if (sellComnInv.getIsNtChk() == 0) {
				message.append(updateSellComnInvIsNtChkNO(sellComnInv).get("message"));
			} else {
				isSuccess = false;
				message.append("���״̬�����޷���ˣ�\n");
			}
			map.put("isSuccess", isSuccess);
			map.put("message", message.toString());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return map;
	}

	// ���۷�Ʊ����
	public Map<String, Object> updateSellComnInvIsNtChkNO(SellComnInv sellComnInv) {
		String message = "";
		Boolean isSuccess = true;
		Map<String, Object> map = new HashMap<>();
		if (sellComnInvDao.selectSellComnInvIsNtBookEntry(sellComnInv.getSellInvNum()) == 0) {
			if (sellComnInvDao.selectSellComnInvIsNtChk(sellComnInv.getSellInvNum()) == 1) {
				SellComnInv selComInv = sellComnInvDao.selectSellComnInvBySellInvNum(sellComnInv.getSellInvNum());// �������۷�Ʊ�����ѯ���۷�Ʊ������Ϣ
				List<SellComnInvSub> sellComnInvSubList = selComInv.getSellComnInvSubList();// ����ר�÷�Ʊ�ӱ�
				String toFormTypEncd = selComInv.getToFormTypEncd();// ��ȡ��Ӧ��������
				if (toFormTypEncd != null && !toFormTypEncd.equals("")) {
					if (toFormTypEncd.equals("007")) {
						String sellSnglNum = selComInv.getSellSnglNum();// ��ȡ��Ӧ���۷�������
						updateSellComnInvBySellSnglIsNtChkNO(sellComnInv, sellComnInvSubList, sellSnglNum);// �޸����۵����ɵ����۷�Ʊ
					} else if (toFormTypEncd.equals("008")) {
						String rtnGoodsId = selComInv.getSellSnglNum();// ��ȡ�˻�������
						updateSellComnInvByRtnGoodIsNtChkNO(sellComnInv, sellComnInvSubList, rtnGoodsId);// �޸��˻������ɵķ�Ʊ
					} else if (toFormTypEncd.equals("025")) {
						String stlSnglId = selComInv.getSellSnglNum();// ��ȡί�д�����������
						updateSellComnInvByEntrsAgnStlIsNtChkNO(sellComnInv, sellComnInvSubList, stlSnglId);// �޸�ί�д������㵥���ɵķ�Ʊ
					}
					// �޸����״̬
					int a = sellComnInvDao.updateSellComnInvIsNtChk(sellComnInv);
					if (a >= 1) {
						isSuccess = true;
						message += "���ݺ�Ϊ" + sellComnInv.getSellInvNum() + "�����۷�Ʊ����ɹ���\n";
					} else {
						isSuccess = false;
						message += "���ݺ�Ϊ" + sellComnInv.getSellInvNum() + "�����۷�Ʊ����ʧ�ܣ�\n";
						throw new RuntimeException(message);
					}
				} else {
					// �޸����״̬
					int a = sellComnInvDao.updateSellComnInvIsNtChk(sellComnInv);
					if (a >= 1) {
						isSuccess = true;
						message += "���ݺ�Ϊ" + sellComnInv.getSellInvNum() + "�����۷�Ʊ����ɹ���\n";
					} else {
						isSuccess = false;
						message += "���ݺ�Ϊ" + sellComnInv.getSellInvNum() + "�����۷�Ʊ����ʧ�ܣ�\n";
						throw new RuntimeException(message);
					}
				}
			} else {
				isSuccess = false;
				message += "���ݺ�Ϊ" + sellComnInv.getSellInvNum() + "�����۷�Ʊδ��ˣ�������˸õ���\n";
				throw new RuntimeException(message);
			}
		} else {
			isSuccess = false;
			message += "���ݺ�Ϊ" + sellComnInv.getSellInvNum() + "�����۷�Ʊ�Ѽ��ˣ��޷�����\n";
			throw new RuntimeException(message);
		}
		map.put("isSuccess", isSuccess);
		map.put("message", message);
		return map;
	}

	// ���۵���Ӧ�����۷�Ʊ����
	private Map<String, Object> updateSellComnInvBySellSnglIsNtChkNO(SellComnInv sellComnInv,
			List<SellComnInvSub> sellComnInvSubList, String sellSnglNum) {
		String message = "";
		Boolean isSuccess = true;
		Map<String, Object> map = new HashMap<>();
		if (sellSnglNum != null && !sellSnglNum.equals("")) {
			for (SellComnInvSub sellComnInvSub : sellComnInvSubList) {
				map.put("ordrNum", sellComnInvSub.getSellSnglSubId());// ��Ʊ�����۵��ӱ����
				map.put("unBllgQty", sellComnInvSub.getQty().multiply(new BigDecimal(-1)));// δ��Ʊ����
				BigDecimal unBllgQty = sellSnglSubDao.selectSellSnglUnBllgQtyByOrdrNum(map);
				if (unBllgQty != null) {
					sellSnglSubDao.updateSellSnglUnBllgQtyByOrdrNum(map);// �������۷������ӱ�����޸�δ��Ʊ����
				} else {
					isSuccess = false;
					message += "���ݺ�Ϊ��" + sellComnInv.getSellInvNum() + "�Ĳɹ���Ʊ��Ӧ�����۵���δ��Ʊ���������ڣ��޷�����\n";
					throw new RuntimeException(message);
				}
			}
			List<String> sellSnglIdList = getSellSnglList(sellSnglNum);// ��ȡ���۵����
			for (String sellSnglId : sellSnglIdList) {

				List<SellSnglSub> sellSnglSubList = sellSnglSubDao.selectSellSnglSubBySellSnglId(sellSnglId);
				int num = 0;
				for (SellSnglSub sellSnglSub : sellSnglSubList) {
					if (sellSnglSub.getUnBllgQty().intValue() > 0) {
						num++;
					}
				}
				if (num > 0) {
					sellSnglDao.updateSellSnglIsNtBllgNO(sellSnglId);// �޸Ŀ�Ʊ״̬
				}
			}
		} else {
			isSuccess = false;
			message += "���ݺ�Ϊ" + sellComnInv.getSellInvNum() + "�ķ�Ʊ��Ӧҵ�񵥾ݵĵ��ݺŲ����ڣ��޷�����\n";
			throw new RuntimeException(message);
		}
		map.put("isSuccess", isSuccess);
		map.put("message", message);
		return map;
	}

	// �˻�����Ӧ�����۷�Ʊ����
	private Map<String, Object> updateSellComnInvByRtnGoodIsNtChkNO(SellComnInv sellComnInv,
			List<SellComnInvSub> sellComnInvSubList, String rtnGoodsId) {
		String message = "";
		Boolean isSuccess = true;
		Map<String, Object> map = new HashMap<>();
		if (rtnGoodsId != null && !rtnGoodsId.equals("")) {
			for (SellComnInvSub sellComnInvSub : sellComnInvSubList) {
				map.put("ordrNum", sellComnInvSub.getSellSnglSubId());// ��Ʊ���˻����ӱ����
				map.put("unBllgQty", sellComnInvSub.getQty().multiply(new BigDecimal(-1)));// δ��Ʊ����
				BigDecimal unBllgQty = rtnGoodsSubDao.selectRtnGoodsUnBllgQtyByOrdrNum(map);// ��ȡ�˻����е�δ��Ʊ����
				if (unBllgQty != null) {
					rtnGoodsSubDao.updateRtnGoodsUnBllgQtyByOrdrNum(map);// �����˻����ӱ�����޸�δ��Ʊ����

				} else {
					isSuccess = false;
					message += "���ݺ�Ϊ��" + sellComnInv.getSellInvNum() + "�Ĳɹ���Ʊ��Ӧ�����۵���δ��Ʊ���������ڣ��޷�����\n";
					throw new RuntimeException(message);
				}
			}
			List<String> rtnGoodsIdList = getSellSnglList(rtnGoodsId);// ��ȡ���۵����
			for (String rtnGoodId : rtnGoodsIdList) {
				List<RtnGoodsSub> rtnGoodsSubList = rtnGoodsSubDao.selectRtnGoodsSubByRtnGoodsId(rtnGoodId);
				int num = 0;
				for (RtnGoodsSub rtnGoodsSub : rtnGoodsSubList) {
					if (rtnGoodsSub.getUnBllgQty().intValue() > 0) {
						num++;
					}
				}
				if (num > 0) {
					rtnGoodsDao.updateRtnGoodsIsNtBllgNO(rtnGoodId);// �޸Ŀ�Ʊ״̬
				}
			}
		} else {
			isSuccess = false;
			message += "���ݺ�Ϊ" + sellComnInv.getSellInvNum() + "�ķ�Ʊ��Ӧҵ�񵥾ݵĵ��ݺŲ����ڣ��޷�����\n";
			throw new RuntimeException(message);
		}
		map.put("isSuccess", isSuccess);
		map.put("message", message);
		return map;
	}

	// ί�д�����������Ӧ�����۷�Ʊ����
	private Map<String, Object> updateSellComnInvByEntrsAgnStlIsNtChkNO(SellComnInv sellComnInv,
			List<SellComnInvSub> sellComnInvSubList, String stlSnglId) {
		String message = "";
		Boolean isSuccess = true;
		Map<String, Object> map = new HashMap<>();
		if (stlSnglId != null && !stlSnglId.equals("")) {
			for (SellComnInvSub sellComnInvSub : sellComnInvSubList) {
				map.put("ordrNum", sellComnInvSub.getSellSnglSubId());// ��Ʊ��ί�д������ӱ����
				map.put("unBllgQty", sellComnInvSub.getQty().multiply(new BigDecimal(-1)));// δ��Ʊ����
				BigDecimal unBllgQty = entrsAgnStlSubDao.selectEntrsAgnStlUnBllgQtyByOrdrNum(map);// ��ȡί�д���������δ��Ʊ����
				if (unBllgQty != null) {
					entrsAgnStlSubDao.updateEntrsAgnStlUnBllgQtyByOrdrNum(map);// ����ί�д����������ӱ�����޸�δ��Ʊ����
				} else {
					isSuccess = false;
					message += "���ݺ�Ϊ��" + sellComnInv.getSellInvNum() + "�Ĳɹ���Ʊ��Ӧ�����۵���δ��Ʊ���������ڣ��޷�����\n";
					throw new RuntimeException(message);
				}
			}
			List<String> stlSnglIdList = getSellSnglList(stlSnglId);// ��ȡί�д������������
			for (String stlSngId : stlSnglIdList) {
				List<EntrsAgnStlSub> entrsAgnStlSubList = entrsAgnStlSubDao.selectEntrsAgnStlSubByStlSnglId(stlSngId);
				int num = 0;
				for (EntrsAgnStlSub entrsAgnStlSub : entrsAgnStlSubList) {
					if (entrsAgnStlSub.getUnBllgQty().intValue() > 0) {
						num++;
					}
				}
				if (num > 0) {
					entrsAgnStlDao.updateEntrsAgnStlIsNtBllgNO(stlSngId);// �޸Ŀ�Ʊ״̬
				}
			}
		} else {
			isSuccess = false;
			message += "���ݺ�Ϊ" + sellComnInv.getSellInvNum() + "�ķ�Ʊ��Ӧҵ�񵥾ݵĵ��ݺŲ����ڣ��޷�����\n";
			throw new RuntimeException(message);
		}
		map.put("isSuccess", isSuccess);
		map.put("message", message);
		return map;
	}

	// ���۷�Ʊ���
	public Map<String, Object> updateSellComnInvIsNtChkOK(SellComnInv sellComnInv) {
		String message = "";
		Boolean isSuccess = true;
		Map<String, Object> map = new HashMap<>();
		SellComnInv selComInv = sellComnInvDao.selectSellComnInvBySellInvNum(sellComnInv.getSellInvNum());// �������۷�Ʊ�����ѯ���۷�Ʊ������Ϣ
		List<SellComnInvSub> sellComnInvSubList = selComInv.getSellComnInvSubList();// ����ר�÷�Ʊ�ӱ�
		if (selComInv.getIsNtChk() == 0) {
			String sellSnglNum = selComInv.getSellSnglNum();// ��ȡ��Ӧ���۷�������
			if (sellSnglNum != null && !sellSnglNum.equals("")) {
				if (selComInv.getToFormTypEncd() != null) {
					updateSellComnInvByEntrsAgnStlIsNtChkOK(sellComnInv, sellComnInvSubList, sellSnglNum);
					// �޸����״̬
					int a = sellComnInvDao.updateSellComnInvIsNtChk(sellComnInv);
					if (a >= 1) {
						isSuccess = true;
						message += "���ݺ�Ϊ" + sellComnInv.getSellInvNum() + "�����۷�Ʊ��˳ɹ���\n";
					} else {
						isSuccess = false;
						message += "���ݺ�Ϊ" + sellComnInv.getSellInvNum() + "�����۷�Ʊ���ʧ�ܣ�\n";
						throw new RuntimeException(message);
					}
				} else {
					updateSellComnInvBySellSnglIsNtChkOK(sellComnInv, sellComnInvSubList, sellSnglNum);// �޸����۵����ɵ����۷�Ʊ
					// �޸����״̬
					int a = sellComnInvDao.updateSellComnInvIsNtChk(sellComnInv);
					if (a >= 1) {
						isSuccess = true;
						message += "���ݺ�Ϊ" + sellComnInv.getSellInvNum() + "�����۷�Ʊ��˳ɹ���\n";
					} else {
						isSuccess = false;
						message += "���ݺ�Ϊ" + sellComnInv.getSellInvNum() + "�����۷�Ʊ���ʧ�ܣ�\n";
						throw new RuntimeException(message);
					}
				}
			} else {
				// �޸����״̬
				int a = sellComnInvDao.updateSellComnInvIsNtChk(sellComnInv);
				if (a >= 1) {
					isSuccess = true;
					message += "���ݺ�Ϊ" + sellComnInv.getSellInvNum() + "�����۷�Ʊ��˳ɹ���\n";
				} else {
					isSuccess = false;
					message += "���ݺ�Ϊ" + sellComnInv.getSellInvNum() + "�����۷�Ʊ���ʧ�ܣ�\n";
					throw new RuntimeException(message);
				}
			}
		} else {
			isSuccess = false;
			message += "���ݺ�Ϊ" + sellComnInv.getSellInvNum() + "�����۷�Ʊ����ˣ�����Ҫ�ظ����\n";
			throw new RuntimeException(message);
		}
		map.put("isSuccess", isSuccess);
		map.put("message", message);
		return map;
	}

	// ���۵���Ӧ�����۷�Ʊ���
	private Map<String, Object> updateSellComnInvBySellSnglIsNtChkOK(SellComnInv sellComnInv,
			List<SellComnInvSub> sellComnInvSubList, String sellSnglNum) {
		String message = "";
		Boolean isSuccess = true;
		Map<String, Object> map = new HashMap<>();
		for (SellComnInvSub sellComnInvSub : sellComnInvSubList) {
			if (sellComnInvSub.getIsNtRtnGoods() != null) {
				if (sellComnInvSub.getIsNtRtnGoods() == 0) {
					map.put("ordrNum", sellComnInvSub.getSellSnglSubId());// ��Ʊ�����۵��ӱ����
					map.put("unBllgQty", sellComnInvSub.getQty().abs());// δ��Ʊ����
					BigDecimal unBllgQty = sellSnglSubDao.selectSellSnglUnBllgQtyByOrdrNum(map);
					if (unBllgQty != null) {
						if (unBllgQty.compareTo(sellComnInvSub.getQty().abs()) >= 0) {
							sellSnglSubDao.updateSellSnglUnBllgQtyByOrdrNum(map);// �������۷������ӱ�����޸�δ��Ʊ����
						} else {
							isSuccess = false;
							message += "���ݺ�Ϊ��" + sellComnInv.getSellInvNum() + "�����۷�Ʊ�д����"
									+ sellComnInvSub.getInvtyEncd() + "�������Ρ�" + sellComnInvSub.getBatNum()
									+ "���ۼƿ�Ʊ�������������������޷���ˣ�\n";
							throw new RuntimeException(message);
						}
					}
				} else {
					map.put("ordrNum", sellComnInvSub.getSellSnglSubId());// ��Ʊ���˻����ӱ����
					map.put("unBllgQty", sellComnInvSub.getQty().abs());// δ��Ʊ����
					BigDecimal unBllgQty = rtnGoodsSubDao.selectRtnGoodsUnBllgQtyByOrdrNum(map);// ��ȡ�˻����е�δ��Ʊ����
					if (unBllgQty != null) {
						if (unBllgQty.compareTo(sellComnInvSub.getQty().abs()) >= 0) {
							rtnGoodsSubDao.updateRtnGoodsUnBllgQtyByOrdrNum(map);// �����˻����ӱ�����޸�δ��Ʊ����
						} else {
							isSuccess = false;
							message += "���ݺ�Ϊ��" + sellComnInv.getSellInvNum() + "�����۷�Ʊ�д����"
									+ sellComnInvSub.getInvtyEncd() + "�������Ρ�" + sellComnInvSub.getBatNum()
									+ "���ۼƿ�Ʊ���������˻��������޷���ˣ�\n";
							throw new RuntimeException(message);
						}
					}
				}
			}
		}
		map.put("isSuccess", isSuccess);
		map.put("message", message);
		return map;
	}

	// ί�д�����������Ӧ�����۷�Ʊ���
	private Map<String, Object> updateSellComnInvByEntrsAgnStlIsNtChkOK(SellComnInv sellComnInv,
			List<SellComnInvSub> sellComnInvSubList, String stlSnglId) {
		String message = "";
		Boolean isSuccess = true;
		Map<String, Object> map = new HashMap<>();
		for (SellComnInvSub sellComnInvSub : sellComnInvSubList) {
			map.put("ordrNum", sellComnInvSub.getSellSnglSubId());// ��Ʊ��ί�д������ӱ����
			map.put("unBllgQty", sellComnInvSub.getQty());// δ��Ʊ����
			BigDecimal unBllgQty = entrsAgnStlSubDao.selectEntrsAgnStlUnBllgQtyByOrdrNum(map);// ��ȡί�д���������δ��Ʊ����
			if (unBllgQty != null) {
				if (unBllgQty.compareTo(sellComnInvSub.getQty()) == 1
						|| unBllgQty.compareTo(sellComnInvSub.getQty()) == 0) {
					entrsAgnStlSubDao.updateEntrsAgnStlUnBllgQtyByOrdrNum(map);// ����ί�д����������ӱ�����޸�δ��Ʊ����
				} else {
					isSuccess = false;
					message += "���ݺ�Ϊ��" + sellComnInv.getSellInvNum() + "�����۷�Ʊ�д����" + sellComnInvSub.getInvtyEncd()
							+ "�������Ρ�" + sellComnInvSub.getBatNum() + "���ۼƿ�Ʊ��������ί�д��������������޷���ˣ�\n";
					throw new RuntimeException(message);
				}
			}
		}
		List<String> stlSnglIdList = getSellSnglList(stlSnglId);// ��ȡί�д������������
		for (String stlSngId : stlSnglIdList) {
			List<EntrsAgnStlSub> entrsAgnStlSubList = entrsAgnStlSubDao.selectEntrsAgnStlSubByStlSnglId(stlSngId);
			if (entrsAgnStlSubList.size() > 0) {
				int num = 0;
				for (EntrsAgnStlSub entrsAgnStlSub : entrsAgnStlSubList) {
					if (entrsAgnStlSub.getUnBllgQty().intValue() > 0) {
						num++;
					}
				}
				if (num == 0) {
					entrsAgnStlDao.updateEntrsAgnStlIsNtBllgOK(stlSngId);// �޸Ŀ�Ʊ״̬
				}
			}
		}
		map.put("isSuccess", isSuccess);
		map.put("message", message);
		return map;
	}

	// �˻�����Ӧ�����۷�Ʊ���
	private Map<String, Object> updateSellComnInvByRtnGoodIsNtChkOK(SellComnInv sellComnInv,
			List<SellComnInvSub> sellComnInvSubList, String rtnGoodsId) {
		String message = "";
		Boolean isSuccess = true;
		Map<String, Object> map = new HashMap<>();
		if (rtnGoodsId != null && !rtnGoodsId.equals("")) {
			for (SellComnInvSub sellComnInvSub : sellComnInvSubList) {
				map.put("ordrNum", sellComnInvSub.getSellSnglSubId());// ��Ʊ���˻����ӱ����
				map.put("unBllgQty", sellComnInvSub.getQty());// δ��Ʊ����
				BigDecimal unBllgQty = rtnGoodsSubDao.selectRtnGoodsUnBllgQtyByOrdrNum(map);// ��ȡ�˻����е�δ��Ʊ����
				if (unBllgQty != null) {
					if (unBllgQty.compareTo(sellComnInvSub.getQty()) == 1
							|| unBllgQty.compareTo(sellComnInvSub.getQty()) == 0) {
						rtnGoodsSubDao.updateRtnGoodsUnBllgQtyByOrdrNum(map);// �����˻����ӱ�����޸�δ��Ʊ����
					} else {
						isSuccess = false;
						message += "���ݺ�Ϊ��" + sellComnInv.getSellInvNum() + "�����۷�Ʊ�д����" + sellComnInvSub.getInvtyEncd()
								+ "�������Ρ�" + sellComnInvSub.getBatNum() + "���ۼƿ�Ʊ���������˻��������޷���ˣ�\n";
						throw new RuntimeException(message);
					}
				}
			}
			List<String> rtnGoodsIdList = getSellSnglList(rtnGoodsId);// ��ȡ���۵����
			for (String rtnGoodId : rtnGoodsIdList) {
				List<RtnGoodsSub> rtnGoodsSubList = rtnGoodsSubDao.selectRtnGoodsSubByRtnGoodsId(rtnGoodId);
				if (rtnGoodsSubList.size() > 0) {
					int num = 0;
					for (RtnGoodsSub rtnGoodsSub : rtnGoodsSubList) {
						if (rtnGoodsSub.getUnBllgQty().intValue() > 0) {
							num++;
						}
					}
					if (num == 0) {
						rtnGoodsDao.updateRtnGoodsIsNtBllgOK(rtnGoodId);// �޸Ŀ�Ʊ״̬
					}
				}

			}
		} else {
			isSuccess = false;
			message += "���ݺ�Ϊ" + sellComnInv.getSellInvNum() + "�ķ�Ʊ��Ӧҵ�񵥾ݵĵ��ݺŲ����ڣ��޷���ˣ�\n";
			throw new RuntimeException(message);
		}
		map.put("isSuccess", isSuccess);
		map.put("message", message);
		return map;
	}

	/**
	 * id����list
	 * 
	 * @param id id(����Ѷ��ŷָ�)
	 * @return List����
	 */
	public List<String> getSellSnglList(String id) {
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
	@Override
	public String uploadFileAddDb(MultipartFile file, int i) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		Map<String, SellComnInv> sellComnInvMap = null;

		if (i == 0) {
			sellComnInvMap = uploadScoreInfo(file);
		} else if (i == 1) {
			sellComnInvMap = uploadScoreInfoU8(file);
		} else {
			isSuccess = false;
			message = "������쳣����";
			throw new RuntimeException(message);
		}

		// ��MapתΪList��Ȼ���������븸������
		List<SellComnInv> sellComnInvList = sellComnInvMap.entrySet().stream().map(e -> e.getValue())
				.collect(Collectors.toList());
		List<List<SellComnInv>> sellComnInvLists = Lists.partition(sellComnInvList, 1000);

		for (List<SellComnInv> sellComnInv : sellComnInvLists) {
			sellComnInvDao.insertSellComnInvUpload(sellComnInv);
		}
		List<SellComnInvSub> sellComnInvSubList = new ArrayList<>();
		int flag = 0;
		for (SellComnInv entry : sellComnInvList) {
			flag++;
			// ���������ֱ�͸���Ĺ����ֶ�
			List<SellComnInvSub> tempList = entry.getSellComnInvSubList();
			tempList.forEach(s -> s.setSellInvNum(entry.getSellInvNum()));
			sellComnInvSubList.addAll(tempList);
			// �������룬ÿ���ڵ���1000������һ��
			if (sellComnInvSubList.size() >= 1000 || sellComnInvMap.size() == flag) {
				sellComnInvSubDao.insertSellComnInvSubList(sellComnInvSubList);
				sellComnInvSubList.clear();
			}
		}
		isSuccess = true;
		message = "���۷�Ʊ����ɹ���";
		try {
			if (i == 0) {
				resp = BaseJson.returnRespObj("/account/SellComnInv/uploadSellComnInvFile", isSuccess, message, null);
			} else if (i == 1) {
				resp = BaseJson.returnRespObj("/account/SellComnInv/uploadSellComnInvFileU8", isSuccess, message, null);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// ����excle
	private Map<String, SellComnInv> uploadScoreInfo(MultipartFile file) {
		Map<String, SellComnInv> temp = new HashMap<>();
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
				String orderNo = GetCellData(r, "���۷�Ʊ����");
				// ����ʵ����
				SellComnInv sellComnInv = new SellComnInv();
				if (temp.containsKey(orderNo)) {
					sellComnInv = temp.get(orderNo);
				}
				// ȡ����ǰ�е�1����Ԫ�����ݣ�����װ��pursOrdrʵ��ĸ���������
				sellComnInv.setSellInvNum(orderNo);// ���۷�Ʊ����
				if (GetCellData(r, "��Ʊ����") == null || GetCellData(r, "��Ʊ����").equals("")) {
					sellComnInv.setBllgDt(null);
				} else {
					sellComnInv.setBllgDt(GetCellData(r, "��Ʊ����").replaceAll("[^0-9:-]", " "));// ��Ʊ����
				}
				sellComnInv.setAccNum(GetCellData(r, "ҵ��Ա����")); // ҵ��Ա����', varchar(200
				sellComnInv.setUserName(GetCellData(r, "ҵ��Ա����")); // ҵ��Ա����', varchar(200
				sellComnInv.setCustId(GetCellData(r, "�ͻ�����"));// �ͻ�����
				sellComnInv.setBizTypId(GetCellData(r, "ҵ�����ͱ���"));// ҵ�����ͱ���
				sellComnInv.setSellTypId(GetCellData(r, "�������ͱ���")); // '�������ͱ���', varchar(200
				sellComnInv.setDeptId(GetCellData(r, "���ű���")); // '���ű���', varchar(200
				sellComnInv.setInvTyp(GetCellData(r, "��Ʊ����"));// ��Ʊ����
				sellComnInv.setFormTypEncd(GetCellData(r, "�������ͱ���"));// �������ͱ���
				sellComnInv.setToFormTypEncd(GetCellData(r, "��Ӧ�������ͱ���"));// ��Ӧ�������ͱ���
				sellComnInv.setIsNtChk(new Double(GetCellData(r, "�Ƿ����")).intValue()); // '�Ƿ����', int(11
				sellComnInv.setChkr(GetCellData(r, "�����")); // '�����', varchar(200
				if (GetCellData(r, "���ʱ��") == null || GetCellData(r, "���ʱ��").equals("")) {
					sellComnInv.setChkTm(null);
				} else {
					sellComnInv.setChkTm(GetCellData(r, "���ʱ��").replaceAll("[^0-9:-]", " "));// ���ʱ��
				}

				sellComnInv.setIsNtBookEntry(new Double(GetCellData(r, "�Ƿ����")).intValue()); // �Ƿ����
				sellComnInv.setBookEntryPers(GetCellData(r, "������")); // ������',
				if (GetCellData(r, "����ʱ��") == null || GetCellData(r, "����ʱ��").equals("")) {
					sellComnInv.setBookEntryTm(null);
				} else {
					sellComnInv.setBookEntryTm(GetCellData(r, "����ʱ��").replaceAll("[^0-9:-]", " "));// ����ʱ��
				}

				sellComnInv.setSetupPers(GetCellData(r, "������")); // '������', varchar(200
				if (GetCellData(r, "����ʱ��") == null || GetCellData(r, "����ʱ��").equals("")) {
					sellComnInv.setSetupTm(null);
				} else {
					sellComnInv.setSetupTm(GetCellData(r, "����ʱ��").replaceAll("[^0-9:-]", " "));// ����ʱ��
				}
				sellComnInv.setMdfr(GetCellData(r, "�޸���")); // '�޸���', varchar(200
				if (GetCellData(r, "�޸�ʱ��") == null || GetCellData(r, "�޸�ʱ��").equals("")) {
					sellComnInv.setModiTm(null);
				} else {
					sellComnInv.setModiTm(GetCellData(r, "�޸�ʱ��").replaceAll("[^0-9:-]", " "));// ����ʱ��
				}
				sellComnInv.setSellSnglNum(GetCellData(r, "��Դ���ݺ�"));
				sellComnInv.setMemo(GetCellData(r, "��ͷ��ע")); // '��ע', varchar(2000
				List<SellComnInvSub> sellComnInvSubList = sellComnInv.getSellComnInvSubList();// ���۷�Ʊ�ӱ�
				if (sellComnInvSubList == null) {
					sellComnInvSubList = new ArrayList<>();
				}
				SellComnInvSub sellComnInvSub = new SellComnInvSub();

				sellComnInvSub.setWhsEncd(GetCellData(r, "�ֿ����")); // '�ֿ����',
				sellComnInvSub.setInvtyEncd(GetCellData(r, "�������")); // '�������',
				sellComnInvSub.setQty(GetBigDecimal(GetCellData(r, "����"), 8)); // '����',
				sellComnInvSub.setBxQty(GetBigDecimal(GetCellData(r, "����"), 8)); // '����',
				sellComnInvSub.setBxRule(GetBigDecimal(GetCellData(r, "���"), 8));// ���
				sellComnInvSub.setCntnTaxUprc(GetBigDecimal(GetCellData(r, "��˰����"), 8)); // '��˰����',
				sellComnInvSub.setPrcTaxSum(GetBigDecimal(GetCellData(r, "��˰�ϼ�"), 8)); // '��˰�ϼ�',
				sellComnInvSub.setNoTaxUprc(GetBigDecimal(GetCellData(r, "��˰����"), 8)); // '��˰����',
				sellComnInvSub.setNoTaxAmt(GetBigDecimal(GetCellData(r, "��˰���"), 8)); // '��˰���',
				sellComnInvSub.setTaxAmt(GetBigDecimal(GetCellData(r, "˰��"), 8)); // '˰��',
				sellComnInvSub.setTaxRate(GetBigDecimal(GetCellData(r, "˰��"), 8)); // '˰��',
				sellComnInvSub.setIntlBat(GetCellData(r, "��������")); // ��������
				sellComnInvSub.setBatNum(GetCellData(r, "����")); // '����',
				sellComnInvSub.setMemo(GetCellData(r, "���屸ע")); // '��ע',
				sellComnInvSub.setIsNtRtnGoods(new Double(GetCellData(r, "�Ƿ��˻�")).intValue());
				sellComnInvSub.setSellSnglNums(GetCellData(r, "��Դ���ݺ�"));
				sellComnInvSub.setOutWhsId(GetCellData(r, "���ⵥ��"));
//				sellComnInvSub.setVouchNums(GetCellData(r, "ί�д������㵥��"));
				sellComnInvSub.setProjEncd(GetCellData(r, "��Ŀ����"));
				sellComnInvSubList.add(sellComnInvSub);
				sellComnInv.setSellComnInvSubList(sellComnInvSubList);
				temp.put(orderNo, sellComnInv);
			}
			fileIn.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("�ļ��ĵ�" + j + "�е����ʽ�����޷�����!" + e.getMessage());
		}
		return temp;
	}

	// ����excle
	private Map<String, SellComnInv> uploadScoreInfoU8(MultipartFile file) {
		Map<String, SellComnInv> temp = new HashMap<>();
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
				String orderNo = GetCellData(r, "��Ʊ��");
				// ����ʵ����
				SellComnInv sellComnInv = new SellComnInv();
				if (temp.containsKey(orderNo)) {
					sellComnInv = temp.get(orderNo);
				}
				// ȡ����ǰ�е�1����Ԫ�����ݣ�����װ��pursOrdrʵ��ĸ���������
				sellComnInv.setSellInvNum(orderNo);// ���۷�Ʊ����
				if (GetCellData(r, "��Ʊ����") == null || GetCellData(r, "��Ʊ����").equals("")) {
					sellComnInv.setBllgDt(null);
				} else {
					sellComnInv.setBllgDt(GetCellData(r, "��Ʊ����").replaceAll("[^0-9:-]", " "));// ��Ʊ����
				}
				sellComnInv.setAccNum(GetCellData(r, "ҵ��Ա����")); // ҵ��Ա����', varchar(200
				sellComnInv.setUserName(GetCellData(r, "ҵ �� Ա")); // ҵ��Ա����', varchar(200
				sellComnInv.setCustId(GetCellData(r, "�ͻ����"));// �ͻ�����

				if (GetCellData(r, "��������") != null && GetCellData(r, "��������").equals("B2B")) {
					sellComnInv.setBizTypId("1");// ҵ�����ͱ���
				} else if (GetCellData(r, "��������") != null && GetCellData(r, "��������").equals("B2C")) {
					sellComnInv.setBizTypId("2");// ҵ�����ͱ���
				}
				int sellTyp = 0;
				int rtnGoods = 0;
				if (GetCellData(r, "ҵ������") != null && GetCellData(r, "ҵ������").equals("��ͨ����")) {
					sellComnInv.setSellTypId("1"); // '�������ͱ���', varchar(200
					sellTyp = 0;
				} else if (GetCellData(r, "ҵ������") != null && GetCellData(r, "ҵ������").equals("ί��")) {
					sellComnInv.setSellTypId("2"); // '�������ͱ���', varchar(200
					sellTyp = 1;
				}

				sellComnInv.setDeptId(GetCellData(r, "���ű��")); // '���ű���', varchar(200

				if (GetCellData(r, "��Ʊ����") != null && GetCellData(r, "��Ʊ����").equals("����ר�÷�Ʊ")) {
					sellComnInv.setFormTypEncd("021");// �������ͱ���

				} else if (GetCellData(r, "��Ʊ����") != null && GetCellData(r, "��Ʊ����").equals("������ͨ��Ʊ")) {
					sellComnInv.setFormTypEncd("022");// �������ͱ���
				}
				sellComnInv.setInvTyp(GetCellData(r, "��Ʊ����"));// ��Ʊ����
				if (GetCellData(r, "�˻���־") != null && GetCellData(r, "�˻���־").equals("��")) {
					rtnGoods = 1;
				} else if (GetCellData(r, "�˻���־") != null && GetCellData(r, "�˻���־").equals("��")) {
					rtnGoods = 0;
				}
				// ��؛
				if (rtnGoods == 0) {
					// ί��
					if (sellTyp == 0) {
						sellComnInv.setToFormTypEncd("007");// ��Ӧ�������ͱ���

					} else if (sellTyp == 1) {

						sellComnInv.setToFormTypEncd("023");// ��Ӧ�������ͱ���
					}

				} else if (rtnGoods == 1) {
					// ί��
					if (sellTyp == 0) {
						sellComnInv.setToFormTypEncd("008");// ��Ӧ�������ͱ���

					} else if (sellTyp == 1) {
						sellComnInv.setToFormTypEncd("024");// ��Ӧ�������ͱ���

					}
				}

				sellComnInv.setIsNtChk(1); // '�Ƿ����', int(11
				sellComnInv.setChkr(GetCellData(r, "�����")); // '�����', varchar(200
//				if (GetCellData(r, "���ʱ��") == null || GetCellData(r, "���ʱ��").equals("")) {
//					sellComnInv.setChkTm(null);
//				} else {
//					sellComnInv.setChkTm(GetCellData(r, "���ʱ��").replaceAll("[^0-9:-]", " "));// ���ʱ��
//				}

				sellComnInv.setIsNtBookEntry(0); // �Ƿ����
				sellComnInv.setBookEntryPers(GetCellData(r, "������")); // ������',
//				if(GetCellData(r,"����ʱ��")==null || GetCellData(r,"����ʱ��").equals("")) {
//					sellComnInv.setBookEntryTm(null); 
//				}else {
//					sellComnInv.setBookEntryTm(GetCellData(r,"����ʱ��").replaceAll("[^0-9:-]"," "));//����ʱ�� 
//				}

				sellComnInv.setSetupPers(GetCellData(r, "�Ƶ���")); // '������', varchar(200
				if (GetCellData(r, "�Ƶ�ʱ��") == null || GetCellData(r, "�Ƶ�ʱ��").equals("")) {
					sellComnInv.setSetupTm(null);
				} else {
					sellComnInv.setSetupTm(GetCellData(r, "�Ƶ�ʱ��").replaceAll("[^0-9:-]", " "));// ����ʱ��
				}
				sellComnInv.setMdfr(GetCellData(r, "�޸���")); // '�޸���', varchar(200
				if (GetCellData(r, "�޸�ʱ��") == null || GetCellData(r, "�޸�ʱ��").equals("")) {
					sellComnInv.setModiTm(null);
				} else {
					sellComnInv.setModiTm(GetCellData(r, "�޸�ʱ��").replaceAll("[^0-9:-]", " "));// ����ʱ��
				}
				sellComnInv.setSellSnglNum(GetCellData(r, "��������"));// ������Դ���ݺ�
				sellComnInv.setMemo(GetCellData(r, "�� ע")); // '��ע', varchar(2000
				List<SellComnInvSub> sellComnInvSubList = sellComnInv.getSellComnInvSubList();// ���۷�Ʊ�ӱ�
				if (sellComnInvSubList == null) {
					sellComnInvSubList = new ArrayList<>();
				}
				SellComnInvSub sellComnInvSub = new SellComnInvSub();

				sellComnInvSub.setWhsEncd(GetCellData(r, "�ֿ���")); // '�ֿ����',
				sellComnInvSub.setInvtyEncd(GetCellData(r, "�������")); // '�������',
				sellComnInvSub.setQty(GetBigDecimal(GetCellData(r, "����"), 8)); // '����',
				sellComnInvSub.setBxQty(GetBigDecimal(GetCellData(r, "����"), 8)); // '����',
				sellComnInvSub.setBxRule(GetBigDecimal(GetCellData(r, "���"), 8));// ���
				sellComnInvSub.setCntnTaxUprc(GetBigDecimal(GetCellData(r, "��˰����"), 8)); // '��˰����',
				sellComnInvSub.setPrcTaxSum(GetBigDecimal(GetCellData(r, "��˰�ϼ�"), 8)); // '��˰�ϼ�',
				sellComnInvSub.setNoTaxUprc(GetBigDecimal(GetCellData(r, "��˰����"), 8)); // '��˰����',
				sellComnInvSub.setNoTaxAmt(GetBigDecimal(GetCellData(r, "��˰���"), 8)); // '��˰���',
				sellComnInvSub.setTaxAmt(GetBigDecimal(GetCellData(r, "˰��"), 8)); // '˰��',
				sellComnInvSub.setTaxRate(GetBigDecimal(GetCellData(r, "��ͷ˰��"), 8)); // '˰��',
				sellComnInvSub.setIntlBat(GetCellData(r, "��������")); // ��������
				sellComnInvSub.setBatNum(GetCellData(r, "����")); // '����',
				sellComnInvSub.setMemo(GetCellData(r, "���屸ע")); // '��ע',
				if (GetCellData(r, "�˻���־") != null && GetCellData(r, "�˻���־").equals("��")) {
					sellComnInvSub.setIsNtRtnGoods(1);
				} else if (GetCellData(r, "�˻���־") != null && GetCellData(r, "�˻���־").equals("��")) {
					sellComnInvSub.setIsNtRtnGoods(0);
				}
				sellComnInvSub.setSellSnglNums(GetCellData(r, "����������"));
				sellComnInvSub.setSellSnglSubId(Long.parseLong(GetCellData(r, "�������ӱ�id")));
				sellComnInvSub.setOutWhsId(GetCellData(r, "���ⵥ��"));
				sellComnInvSub.setProjEncd(GetCellData(r, "��Ŀ����"));
				sellComnInvSubList.add(sellComnInvSub);
				sellComnInv.setSellComnInvSubList(sellComnInvSubList);
				temp.put(orderNo, sellComnInv);
			}
			fileIn.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("�ļ��ĵ�" + j + "�е����ʽ�����޷�����!" + e.getMessage());
		}
		return temp;
	}

	// ������ҳ��ȫ��ӿ�
	@Override
	public String upLoadSellComnInvList(Map map) {
		String resp = "";
		List<String> invtyClsEncdList = getList((String) map.get("invtyClsEncd"));// ����������
		List<String> custIdList = getList((String) map.get("custId"));// �ͻ�����
		map.put("invtyClsEncdList", invtyClsEncdList);
		map.put("custIdList", custIdList);
		List<SellComnInv> poList = sellComnInvDao.printingSellComnInvList(map);
		try {
			resp = BaseJson.returnRespObjList("/account/SellComnInv/printingSellComnInvList", true, "��ѯ�ɹ���", null,
					poList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
	}

	// �����ӿ�
	@Override
	public String printSellComnInvList(Map map) {
		String resp = "";
		List<String> invtyClsEncdList = getList((String) map.get("invtyClsEncd"));// ����������
		List<String> custIdList = getList((String) map.get("custId"));// �ͻ�����
		map.put("invtyClsEncdList", invtyClsEncdList);
		map.put("custIdList", custIdList);
		List<zizhu> poList = sellComnInvDao.printSellComnInvList(map);
		try {
			resp = BaseJson.returnRespObjListAnno("/account/SellComnInv/printSellComnInvList", true, "��ѯ�ɹ���", null,
					poList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
	}

	// �������۵�����һ�����۷�Ʊ
//	@RequestMapping(value="selectSellComnInvBingList",method = RequestMethod.POST)
//	@ResponseBody
	@Override
	public String selectSellComnInvBingList(List<SellComnInv> sellSnglList) {
		String resp = "";
		String message = "";
		Boolean isSuccess = true;
		try {
			// ʵ����һ���µĶ��� ������ͨ��Ʊ����
			SellComnInv sellComnInv = new SellComnInv();
			List<SellComnInvSub> sellComnInvSubList = new ArrayList<>();
			String sellSnglId = "";// ���۷�������
			String rtnGoodsId = "";// �˻�����
			String stlSnglId = "";// ί�д�������
			for (SellComnInv selComInv : sellSnglList) {
				if (selComInv.getSellInvNum() != null && !selComInv.getSellInvNum().equals("")) {
					sellSnglId += selComInv.getSellInvNum() + ",";// ���۷�������
					sellComnInv.setSellSnglNum(sellSnglId.substring(0, sellSnglId.length() - 1));
					if (selComInv.getFormTypEncd() == null || selComInv.getFormTypEncd().equals("")) {
						isSuccess = false;
						throw new RuntimeException("����ѡ�����,�������������յĵ����޵������ͣ�");
					} else if (selComInv.getFormTypEncd().equals("007")) {// ���۵�
						BeanUtils.copyProperties(sellComnInv, selComInv);// �����۵��������������۷�Ʊ����
						sellComnInv.setToFormTypEncd(selComInv.getFormTypEncd());// ��Ӧ��Դ��������
						// ��������������ѯ��ص��ӱ���Ϣ
						List<SellComnInvSub> sellComInvSuList = selComInv.getSellComnInvSubList();// ����ҳ���е��ӱ���Ϣ
						Map<String, Object> map = new HashMap<>();
						for (SellComnInvSub sellComnInvSub : sellComInvSuList) {
							map.put("ordrNum", sellComnInvSub.getOrdrNum());
							SellSnglSub sellSnglSub = sellSnglSubDao.selectSellSnglSubBySelSnIdAndOrdrNum(map);
							SellComnInvSub selComnInSub = new SellComnInvSub();
							BeanUtils.copyProperties(selComnInSub, sellSnglSub);// �����۵��ӱ��������۷�Ʊ�ӱ�
							sellComnInvSubList.add(selComnInSub);
						}
					} else {
						isSuccess = false;
						throw new RuntimeException("���յĵ��ݶ�Ӧ�ĵ������ʹ����������������");
					}
				}
				if (selComInv.getSellSnglNum() != null && !selComInv.getSellSnglNum().equals("")) {
					rtnGoodsId += selComInv.getSellSnglNum() + ",";// �˻�����
					sellComnInv.setSellSnglNum(rtnGoodsId.substring(0, rtnGoodsId.length() - 1));
					if (selComInv.getFormTypEncd() == null || selComInv.getFormTypEncd().equals("")) {
						isSuccess = false;
						throw new RuntimeException("����ѡ�����,�������������յĵ����޵������ͣ�");
					} else if (selComInv.getFormTypEncd().equals("008")) {// �˻���
						BeanUtils.copyProperties(sellComnInv, selComInv);// �����۵��������������۷�Ʊ����
						sellComnInv.setToFormTypEncd(selComInv.getFormTypEncd());// ��Ӧ��Դ��������
						// ��������������ѯ��ص��ӱ���Ϣ
						List<SellComnInvSub> sellComInvSuList = selComInv.getSellComnInvSubList();// ����ҳ���е��ӱ���Ϣ
						Map<String, Object> map = new HashMap<>();
						for (SellComnInvSub sellComnInvSub : sellComInvSuList) {
							map.put("ordrNum", sellComnInvSub.getOrdrNum());
							RtnGoodsSub rtnGoodsSub = rtnGoodsSubDao.selectRtnGoodsSubByRtGoIdAndOrdrNum(map);
							SellComnInvSub selComnInSub = new SellComnInvSub();
							BeanUtils.copyProperties(selComnInSub, rtnGoodsSub);// ���˻����ӱ��������۷�Ʊ�ӱ�
							sellComnInvSubList.add(selComnInSub);
						}
					} else {
						isSuccess = false;
						throw new RuntimeException("���յĵ��ݶ�Ӧ�ĵ������ʹ����������������");
					}
				}
				if (selComInv.getSellSnglNum() != null && !selComInv.getSellSnglNum().equals("")) {
					stlSnglId += selComInv.getSellSnglNum() + ",";// ί�д�������
					sellComnInv.setSellSnglNum(stlSnglId.substring(0, stlSnglId.length() - 1));
					if (selComInv.getFormTypEncd() == null || selComInv.getFormTypEncd().equals("")) {
						isSuccess = false;
						throw new RuntimeException("����ѡ�����,�������������յĵ����޵������ͣ�");
					} else if (selComInv.getFormTypEncd().equals("025")) {// ���۵�
						BeanUtils.copyProperties(sellComnInv, selComInv);// �����۵��������������۷�Ʊ����
						sellComnInv.setToFormTypEncd(selComInv.getFormTypEncd());// ��Ӧ��Դ��������
						// ��������������ѯ��ص��ӱ���Ϣ
						List<SellComnInvSub> sellComInvSuList = selComInv.getSellComnInvSubList();// ����ҳ���е��ӱ���Ϣ
						Map<String, Object> map = new HashMap<>();
						for (SellComnInvSub sellComnInvSub : sellComInvSuList) {
							map.put("ordrNum", sellComnInvSub.getOrdrNum());
							EntrsAgnStlSub entrsAgnStlSub = entrsAgnStlSubDao.selectEntrsAgnStlByStlSnIdAndOrdrNum(map);
							SellComnInvSub selComnInSub = new SellComnInvSub();
							BeanUtils.copyProperties(selComnInSub, entrsAgnStlSub);// ��ί�д����������ӱ��������۷�Ʊ�ӱ�
							sellComnInvSubList.add(selComnInSub);
						}
					} else {
						isSuccess = false;
						throw new RuntimeException("���յĵ��ݶ�Ӧ�ĵ������ʹ����������������");
					}
				}
				isSuccess = true;
				message = "�����ɹ���";
			}
			sellComnInv.setSellComnInvSubList(sellComnInvSubList);
			resp = BaseJson.returnRespObj("/account/SellComnInv/selectSellComnInvBingList", isSuccess, message,
					sellComnInv);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return resp;
	}

	// ���۷�Ʊ���չ���
	@Override
	public String selectSellReturnEntrs(Map map) throws IOException {
		String resp = "";
		List<SellComnInv> sellComnInvList = new ArrayList<>();
		int count = 0;
		try {
			if (map.get("formTypEncd").equals("007")) {
				List<String> formIdList = getList((String) map.get("sellInvNum"));
				String formDate1 = (String) map.get("formDt1");
				String formDate2 = (String) map.get("formDt2");
				if (formDate1 != null && formDate1 != "") {
					formDate1 = formDate1 + " 00:00:00";
				}
				if (formDate2 != null && formDate2 != "") {
					formDate2 = formDate2 + " 23:59:59";
				}
				List<String> custIdList = getList((String) map.get("custId"));// �ͻ�����
				List<String> accNumList = getList((String) map.get("accNum"));// ҵ��Ա����
				List<String> deptIdList = getList((String) map.get("deptId"));// ���ű���
				List<String> whsEncdList = getList((String) map.get("whsEncd"));// �ֿ����
				List<String> custOrdrNumList = getList((String) map.get("custOrdrNum"));// �ͻ�������
				List<String> invtyEncdList = getList((String) map.get("invtyEncd"));// �������
				List<String> batNumList = getList((String) map.get("batNum"));// ����
				
				map.put("formIdList", formIdList);
				map.put("formDate1", formDate1);
				map.put("formDate2", formDate2);
				map.put("custIdList", custIdList);
				map.put("accNumList", accNumList);
				map.put("deptIdList", deptIdList);
				map.put("whsEncdList", whsEncdList);
				map.put("custOrdrNumList", custOrdrNumList);
				map.put("invtyEncdList", invtyEncdList);
				map.put("batNumList", batNumList);
				
				String[] flag = {null};
				if(custIdList.size()>0) {
					custIdList.forEach(i->{
						CustCls selectCustDocByCustId = custDocDao.selectCustDocByCustId(i);
						if (null==flag[0]) {
							flag[0] = selectCustDocByCustId.getCustDoc().get(0).getCustTotlCorpId();
						}else {
							if (flag[0]!=selectCustDocByCustId.getCustDoc().get(0).getCustTotlCorpId()) {
								throw new RuntimeException("ͬһ�ŵ��ݵĿͻ��ܹ�˾Ӧ��һ��,��˶Կͻ�!");
							}
						}
						
						
					});
				}
				
				if (map.containsKey("sort")) {
					map.put("sort",((String)map.get("sort")).replace("a.", ""));
				}
				List<SellSngl> sellSnglList = sellSnglDao.selectSellSnglCZLists(map);
				count = sellSnglDao.selectSellSnglCZListsCount(map);

				for (SellSngl sellSngl : sellSnglList) {
					SellComnInv sellComnInv = new SellComnInv();
					try {
						BeanUtils.copyProperties(sellComnInv, sellSngl);
						sellComnInv.setSellInvNum(sellSngl.getSellSnglId());// ���ݱ��
						sellComnInv.setBllgDt(sellSngl.getSellSnglDt());// ��������
						sellComnInvList.add(sellComnInv);
					} catch (IllegalAccessException | InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} else if (map.get("formTypEncd").equals("025")) {
				map.put("stlSnglId", map.get("sellInvNum"));
				String stlSnglDt1 = (String) map.get("formDt1");
				String stlSnglDt2 = (String) map.get("formDt2");
				if (stlSnglDt1 != null && stlSnglDt1 != "") {
					stlSnglDt1 = stlSnglDt1 + " 00:00:00";
				}
				if (stlSnglDt2 != null && stlSnglDt2 != "") {
					stlSnglDt2 = stlSnglDt2 + " 23:59:59";
				}
				map.put("stlSnglDt1", stlSnglDt1);
				map.put("stlSnglDt2", stlSnglDt2);
				List<String> custIdList = getList((String) map.get("custId"));// �ͻ�����
				List<String> accNumList = getList((String) map.get("accNum"));// ҵ��Ա����
				List<String> deptIdList = getList((String) map.get("deptId"));// ���ű���

				map.put("custIdList", custIdList);
				map.put("accNumList", accNumList);
				map.put("deptIdList", deptIdList);

				List<EntrsAgnStl> entrsAgnStlList = entrsAgnStlDao.selectEntrsAgnStlListToCZ(map);
				count = entrsAgnStlDao.selectEntrsAgnStlListToCZCount(map);

				for (EntrsAgnStl entrsAgnStl : entrsAgnStlList) {
					SellComnInv sellComnInv = new SellComnInv();
					try {
						BeanUtils.copyProperties(sellComnInv, entrsAgnStl);
						sellComnInv.setSellInvNum(entrsAgnStl.getStlSnglId());// ���ݱ��
						sellComnInv.setBllgDt(entrsAgnStl.getStlSnglDt());// ��������
						sellComnInvList.add(sellComnInv);
					} catch (IllegalAccessException | InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			int pageNo = Integer.parseInt(map.get("pageNo").toString());// �ڼ�ҳ
			int pageSize = Integer.parseInt(map.get("pageSize").toString());// ÿҳ����
			int listNum = sellComnInvList.size();
			int pages = count / pageSize + 1;// ��ҳ��\
			resp = BaseJson.returnRespList("account/SellComnInv/selectSellReturnEntrs", true, "��ѯ�ɹ���", count, pageNo,
					pageSize, listNum, pages, sellComnInvList);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		return resp;
	}

	// ����ʱ�������۵����˻�����ί�д��������������ѯ�ӱ���Ϣ
	@Override
	public String selectSellComnInvBySellRtnEntList(List<SellComnInv> sellComnInvList) {
		// TODO Auto-generated method stub
		String resp = "";
		try {
			List<SellComnInvSub> sellComnInvSubList = new ArrayList<>();
			for (SellComnInv sellComnInv : sellComnInvList) {
				if (sellComnInv.getFormTypEncd().equals("007")) {
					List<SellSnglSub> sellSnglSubList = sellSnglSubDao
							.selectSellSnglSubBySellSnglIdAndUnBllgQty(sellComnInv.getSellInvNum());
					for (SellSnglSub sellSnglSub : sellSnglSubList) {
						SellComnInvSub selComnInSub = new SellComnInvSub();
						try {
							BeanUtils.copyProperties(selComnInSub, sellSnglSub);
							selComnInSub.setSellInvNum(sellSnglSub.getSellSnglId());// ���۵��ӱ������۵���
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} // ���˻����ӱ��������۷�Ʊ�ӱ�
						sellComnInvSubList.add(selComnInSub);
					}
				}
				if (sellComnInv.getFormTypEncd().equals("008")) {
					List<RtnGoodsSub> rtnGoodsSubList = rtnGoodsSubDao
							.selectRtnGoodsSubByRtnGoodsIdAndUnBllgQty(sellComnInv.getSellInvNum());
					for (RtnGoodsSub rtnGoodsSub : rtnGoodsSubList) {
						SellComnInvSub selComnInSub = new SellComnInvSub();
						try {
							BeanUtils.copyProperties(selComnInSub, rtnGoodsSub);
							selComnInSub.setSellInvNum(rtnGoodsSub.getRtnGoodsId());// �˻����ӱ����˻�����
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} // ���˻����ӱ��������۷�Ʊ�ӱ�
						sellComnInvSubList.add(selComnInSub);
					}
				}
				if (sellComnInv.getFormTypEncd().equals("025")) {
					List<EntrsAgnStlSub> entrsAgnStlSubList = entrsAgnStlSubDao
							.selectEntAgStSubByStlIdAndUnBllgQty(sellComnInv.getSellInvNum());
					for (EntrsAgnStlSub entrsAgnStlSub : entrsAgnStlSubList) {
						SellComnInvSub selComnInSub = new SellComnInvSub();
						try {
							BeanUtils.copyProperties(selComnInSub, entrsAgnStlSub);
							selComnInSub.setSellInvNum(entrsAgnStlSub.getStlSnglId());// ί�д����������ӱ���ί�д�����������
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} // ���˻����ӱ��������۷�Ʊ�ӱ�
						sellComnInvSubList.add(selComnInSub);
					}
				}
			}

			resp = BaseJson.returnRespObjList("/account/SellComnInv/selectSellComnInvBySellRtnEntList", true, "��ѯ�ɹ���",
					null, sellComnInvSubList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
	}

	@Override
	public String pushToU8(String ids) throws IOException {
		Logger logger = LoggerFactory.getLogger(SellComnInvServiceImpl.class);

		List<String> idList = getList(ids);

		List<SellComnInv> sellComnInvList = sellComnInvDao.selectComnInvs(idList);
		// ������
		U8SellComnInvTable table = new U8SellComnInvTable();
		// �м���
		ArrayList<U8SellComnInv> rowList = new ArrayList<U8SellComnInv>();
		// ��װ����
		for (SellComnInv main : sellComnInvList) {
			rowList.add(encapsulation(main, main.getSellComnInvSubList()));
		}
		table.setDataRowList(rowList);

		/********** �����ǽӿڶԽ�************ */
		System.err.println("����ṹ::" + JacksonUtil.getXmlStr(table));

		String resultStr = connectToU8("SetSOBill", JacksonUtil.getXmlStr(table), "905");

		System.err.println("���ص�XML�ṹ::" + resultStr);

		/*** �����ͽ��� ****/

		MappingIterator<U8SellComnInvResponse> iterator = JacksonUtil.getResponse("SetSOBillResult",
				U8SellComnInvResponse.class, resultStr);

		ArrayList<U8SellComnInvResponse> failedList = new ArrayList<U8SellComnInvResponse>();

		StringBuffer message = new StringBuffer();
		int count = 0;
		while (iterator.hasNext()) {
			U8SellComnInvResponse response = (U8SellComnInvResponse) iterator.next();
			if (response.getType() == 1 && response.getDscode() != null) {
				failedList.add(response);
				logger.info("����: " + response.getDscode() + "�ķ�Ʊ����ʧ��,ԭ��Ϊ :" + response.getInfor());

			} else if (response.getType() == 0) {
				sellComnInvDao.updatePushed(response.getDscode());
				count++;
			} else {
				failedList.add(response);
				logger.info("����: " + "--δ֪����--" + "�ķ�Ʊ����ʧ��,ԭ��Ϊ :" + response.getInfor());
			}
		}
		message.append("��" + count + "�ŵ����ϴ��ɹ�!" + '\n');
		if (failedList.size() > 0) {
			message.append("��" + failedList.size() + "�ŵ����ϴ�ʧ��!" + "\n");
		}
		String resp = BaseJson.returnRespList("url://", true, message.toString(), null);

		return resp;

	}

	@Override
	public U8SellComnInv encapsulation(SellComnInv sellComnInv, List<SellComnInvSub> sellComnInvSubList) {

		// �ж���-->������Ʊ
		U8SellComnInv dataRow = new U8SellComnInv();
		// ����
		dataRow.setDscode(sellComnInv.getSellInvNum());
		dataRow.setVouttype(sellComnInv.getInvTyp().equals("����ר�÷�Ʊ") ? "26" : "27");// 26רƱ 27��Ʊ
		dataRow.setCstcode("01");
		dataRow.setDdate(sellComnInv.getBllgDt());
		dataRow.setCdepcode(sellComnInv.getDeptId());
		dataRow.setCcuscode(sellComnInv.getCustId());
		if (dataRow.getVouttype().equals("26")) {
			CustCls custCls = custDocDao.selectCustDocByCustId(sellComnInv.getCustId());
			CustDoc custDoc = custCls.getCustDoc().get(0);

			dataRow.setCcusbank(custDoc.getOpnBnk());// �ͻ����� רƱ����
			dataRow.setCcusaccount(custDoc.getBkatNum());// �ͻ��˻�
		}
		dataRow.setCpersoncode(sellComnInv.getAccNum());
		dataRow.setRemark(sellComnInv.getMemo());
		// �ӱ���
		ArrayList<U8SellComnInvSub> detailsList = new ArrayList<U8SellComnInvSub>();
		BigDecimal[] headTaxRate = new BigDecimal[] { BigDecimal.ZERO };
		// ѭ������ӱ�
		sellComnInvSubList.forEach(item -> {
			U8SellComnInvSub details = new U8SellComnInvSub();
			details.setCinvcode(item.getInvtyEncd());
			details.setQuantity(item.getQty());
			details.setIprice(item.getNoTaxUprc());
			details.setCitemcode(item.getProjEncd());
			details.setItaxrate(item.getTaxRate());
			headTaxRate[0] = item.getTaxRate();// ��ͷ˰��
			details.setCbatch(item.getBatNum());
			details.setCwhcode(item.getWhsEncd());
			detailsList.add(details);
		});
		// ��ͷ˰��ѭ����
		dataRow.setItaxrate(sellComnInv.getTabHeadTaxRate() == null ? headTaxRate[0]
				: new BigDecimal(sellComnInv.getTabHeadTaxRate()));
		dataRow.setSubList(detailsList);
		return dataRow;
	}

	public String connectToU8(String methodName, String dataXmlStr, String ztCodeStr) {
		String resultStr = "";
		try {
			ServiceClient serviceClient = new ServiceClient();
			// ���������ַWebService��URL,ע�ⲻ��WSDL��URL
			String url = "http://106.14.183.228:8081/YBService.asmx";
			EndpointReference targetEPR = new EndpointReference(url);
			Options options = serviceClient.getOptions();
			options.setTo(targetEPR);
			// ȷ�����÷�����wsdl �����ռ��ַ (wsdl�ĵ��е�targetNamespace) �� �������� ����ϣ�
			options.setAction("http://tempuri.org/" + methodName);

			OMFactory fac = OMAbstractFactory.getOMFactory();
			/*
			 * ָ�������ռ䣬������ uri--��Ϊwsdl�ĵ���targetNamespace�������ռ� perfix--�ɲ���
			 */
			OMNamespace omNs = fac.createOMNamespace("http://tempuri.org/", "");
			// ָ������
			OMElement method = fac.createOMElement(methodName, omNs);
			// ָ�������Ĳ���
			OMElement dataXml = fac.createOMElement("dataXml", omNs);
			OMElement ztCode = fac.createOMElement("ztCode", omNs);
			dataXml.setText(dataXmlStr);
			ztCode.setText(ztCodeStr);

			method.addChild(dataXml);
			method.addChild(ztCode);
			method.build();

			// Զ�̵���web����
			OMElement result = serviceClient.sendReceive(method);
			resultStr = result.toString();
		} catch (AxisFault axisFault) {
			axisFault.printStackTrace();

		}
		return resultStr;
	}

	// ������
	public static class zizhu {
		@JsonProperty("���۷�Ʊ��")
		public String sellInvNum;
		@JsonProperty("��Ʊ����")
		public String bllgDt;
		@JsonProperty("��Ʊ����")
		public String invTyp;
		@JsonProperty("��������")
		public String sellTypNm;
		@JsonProperty("ҵ������")
		public String bizTypNm;
		@JsonProperty("ҵ��Ա���")
		public String accNum;
		@JsonProperty("����")
		public String deptNm;
		@JsonProperty("�ͻ�")
		public String custNm;
		@JsonProperty("�Ƿ����")
		public Integer isNtChk;
		@JsonProperty("��ͷ��ע")
		public String memo;
		@JsonProperty("���")
		public Integer ordrNum;
		@JsonProperty("���۵���")
		public String sellSnglNum;
		@JsonProperty("���۵���(�����ӱ�)")
		public Long sellSnglSubId;
		@JsonProperty("�ֿ�����")
		public String whsNm;
		@JsonProperty("�ֿ����")
		public String whsEncd;
		@JsonProperty("�������")
		public String invtyEncd;
		@JsonProperty("�������")
		public String invtyNm;
		@JsonProperty("��Ŀ����")
		public String projEncd;
		@JsonProperty("����ͺ�")
		public String spcModel;
		@JsonProperty("������λ")
		public String measrCorpNm;
		@JsonProperty("����")
		public BigDecimal qty;
		@JsonProperty("��˰����")
		public BigDecimal cntnTaxUprc;
		@JsonProperty("��˰�ϼ�")
		public BigDecimal prcTaxSum;
		@JsonProperty("��˰����")
		public BigDecimal noTaxUprc;
		@JsonProperty("��˰���")
		public BigDecimal noTaxAmt;
		@JsonProperty("˰��")
		public BigDecimal taxAmt;
		@JsonProperty("˰��")
		public BigDecimal taxRate;
		@JsonProperty("���")
		public BigDecimal bxRule;
		@JsonProperty("����")
		public BigDecimal bxQty;
		@JsonProperty("����")
		public String batNum;
		@JsonProperty("��������")
		public String intlBat;
		@JsonProperty("���屸ע")
		public String memos;

		// ����
		@JsonIgnore
		@JsonProperty("ҵ�����ͱ��")
		public String bizTypId;
		@JsonIgnore
		@JsonProperty("�������ͱ��")
		public String sellTypId;
		@JsonIgnore
		@JsonProperty("��ͷ˰��")
		public String tabHeadTaxRate;
		@JsonIgnore
		@JsonProperty("�շ������")
		public String recvSendCateId;
		@JsonIgnore
		@JsonProperty("�ͻ����")
		public String custId;
		@JsonIgnore
		@JsonProperty("���ű��")
		public String deptId;
		@JsonIgnore
		@JsonProperty("������")
		public String setupPers;
		@JsonIgnore
		@JsonProperty("��������")
		public String setupTm;
		@JsonIgnore
		@JsonProperty("�޸���")
		public String mdfr;
		@JsonIgnore
		@JsonProperty("�޸�ʱ��")
		public String modiTm;
		@JsonIgnore
		@JsonProperty("�����")
		public String chkr;
		@JsonIgnore
		@JsonProperty("���ʱ��")
		public String chkTm;
		@JsonIgnore
		@JsonProperty("�Ƿ����")
		public Integer isNtBookEntry;
		@JsonIgnore
		@JsonProperty("������")
		public String bookEntryPers;
		@JsonIgnore
		@JsonProperty("����ʱ��")
		public String bookEntryTm;
		@JsonIgnore
		@JsonProperty("��Ŀ����")
		public String subjEncd;
		@JsonIgnore
		@JsonProperty("�ͻ���ϵ��")
		public String contcr;
		@JsonIgnore
		@JsonProperty("�ͻ�����")
		public String bank;
		@JsonIgnore
		@JsonProperty("�ͻ��˺�")
		public String acctNum;
		@JsonIgnore
		@JsonProperty("�Ƶ�ʱ��")
		public String makDocTm;
		@JsonIgnore
		@JsonProperty("�Ƶ���")
		public String makDocPers;
		@JsonIgnore
		@JsonProperty("�û�����")
		public String userName;
		@JsonIgnore
		@JsonProperty("�������ͱ���")
		public String formTypEncd;
		@JsonIgnore
		@JsonProperty("��Դ�������ͱ���")
		public String toFormTypEncd;
		@JsonIgnore
		@JsonProperty("�Ƿ�����ƾ֤")
		public Integer isNtMakeVouch;
		@JsonIgnore
		@JsonProperty("��ƾ֤��")
		public String makVouchPers;
		@JsonIgnore
		@JsonProperty("��ƾ֤ʱ��")
		public String makVouchTm;
		@JsonIgnore
		@JsonProperty("������λ���")
		public String measrCorpId;
		@JsonIgnore
		@JsonProperty("�Ƿ�����Ʒ")
		public Integer isComplimentary;
		@JsonIgnore
		@JsonProperty("��������")
		public String delvSnglNum;
		@JsonIgnore
		@JsonProperty("���һ������")
		public String invtyFstLvlCls;
		@JsonIgnore
		@JsonProperty("��������ʶ")
		public String sellSnglNums;
		@JsonIgnore
		@JsonProperty("�������")
		public String invtyCd;
		@JsonIgnore
		@JsonProperty("����������")
		public String baoZhiQiDt;
		@JsonIgnore
		@JsonProperty("��Ӧ������")
		public String crspdBarCd;
		@JsonIgnore
		@JsonProperty("�Ƿ��˻�")
		public Integer isNtRtnGoods;
		@JsonIgnore
		@JsonProperty("���۳��ⵥ����")
		public String outWhsId;
		@JsonIgnore
		@JsonProperty("���۳��ⵥ�ӱ��ʶ")
		public Long outWhsSubId;
		@JsonIgnore
		@JsonProperty("��Ŀ����")
		public String projNm;
	}
}
