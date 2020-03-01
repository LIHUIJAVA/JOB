package com.px.mis.account.service.impl;

import java.io.IOException;
import java.io.InputStream;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.MappingIterator;
import com.google.common.collect.Lists;
import com.px.mis.account.dao.PursComnInvDao;
import com.px.mis.account.dao.PursComnInvSubDao;
import com.px.mis.account.dao.SellComnInvDao;
import com.px.mis.account.dao.SellComnInvSubDao;
import com.px.mis.account.entity.PursComnInv;
import com.px.mis.account.entity.PursComnInvSub;
import com.px.mis.account.entity.PursComnInvForU8.U8PursComnInv;
import com.px.mis.account.entity.PursComnInvForU8.U8PursComnInvResponse;
import com.px.mis.account.entity.PursComnInvForU8.U8PursComnInvSub;
import com.px.mis.account.entity.PursComnInvForU8.U8PursComnInvTable;
import com.px.mis.account.service.PursComnInvService;
import com.px.mis.purc.dao.IntoWhsDao;
import com.px.mis.purc.dao.IntoWhsSubDao;
import com.px.mis.purc.dao.ProvrDocDao;
import com.px.mis.purc.entity.IntoWhs;
import com.px.mis.purc.entity.IntoWhsSub;
import com.px.mis.util.BaseJson;
import com.px.mis.util.GetOrderNo;
import com.px.mis.util.JacksonUtil;
import com.px.mis.util.poiTool;

//�ɹ���ͨ��Ʊ����
@org.springframework.stereotype.Service
@Transactional
public class PursComnInvServiceImpl<E> extends poiTool implements PursComnInvService {

	@Autowired
	private PursComnInvDao pursComnInvDao;
	@Autowired
	private PursComnInvSubDao pursComnInvSubDao;
	@Autowired
	private IntoWhsDao intoWhsDao;// �ɹ���ⵥ�ӱ�
	@Autowired
	private IntoWhsSubDao intoWhsSubDao;// �ɹ���ⵥ�ӱ�
	@Autowired
	private ProvrDocDao provrDocDao;
	@Autowired
	private GetOrderNo getOrderNo;// ������

	// �����ɹ���ͨ��Ʊ
	@Override
	public String addPursComnInv(PursComnInv pursComnInv, List<PursComnInvSub> pursComnInvSubList, String userId,
			String loginTime) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			String pursInvNum = pursComnInv.getPursInvNum();
			if (pursInvNum == null || pursInvNum.equals("")) {
				isSuccess = false;
				message = "�ɹ���Ʊ�Ų���Ϊ�գ�����ʧ�ܣ�";
				String number = getOrderNo.getSeqNo("CGFP", userId, loginTime);
				if (pursComnInvDao.selectPursComnInvByIds(pursInvNum) == null) {
					pursComnInv.setPursInvNum(number);
					if (provrDocDao.selectProvrDocByProvrId(pursComnInv.getProvrId()) != null) {
						int a = 0;
						int b = 0;
						a = pursComnInvDao.insertPursComnInv(pursComnInv);
						//���ݷ�Ʊ������ֺ���,���С����Ϊ����,������Ϊ����
						BigDecimal totalAmt=BigDecimal.ZERO;
						for (PursComnInvSub pursComnInvSub : pursComnInvSubList) {
							pursComnInvSub.setPursInvNum(pursComnInv.getPursInvNum());
							totalAmt=totalAmt.add(pursComnInvSub.getNoTaxAmt());
							b = pursComnInvSubDao.insertPursComnInvSub(pursComnInvSub);
						}
						//��ռ�ù�����ϵ���ֶ�
						if (totalAmt.compareTo(BigDecimal.ZERO)>0) {
							pursComnInv.setColor("��");
						}else  {
							pursComnInv.setColor("��");
						}
						
						pursComnInvDao.updatePursComnInvById(pursComnInv);//���ĺ����ֶ�
						if (a >= 1 || b == 1) {
							isSuccess = true;
							message = "�ɹ���Ʊ�����ɹ�";

						} else {
							isSuccess = false;
							message = "�ɹ���Ʊ����ʧ��";
						}
					} else {
						isSuccess = false;
						message = "��Ӧ�̣�" + pursComnInv.getProvrId() + "�����ڣ�����ʧ�ܣ�";
					}
				} else {
					isSuccess = false;
					message = "�ɹ���Ʊ�ţ�" + pursInvNum + "�Ѵ��ڣ�����ʧ�ܣ�";
				}

			} else {
				if (pursComnInvDao.selectPursComnInvByIds(pursInvNum) == null) {
					if (provrDocDao.selectProvrDocByProvrId(pursComnInv.getProvrId()) != null) {
						int a = 0;
						int b = 0;
						a = pursComnInvDao.insertPursComnInv(pursComnInv);
						//���ݷ�Ʊ������ֺ���,���С����Ϊ����,������Ϊ����
						BigDecimal totalAmt=BigDecimal.ZERO;
						for (PursComnInvSub pursComnInvSub : pursComnInvSubList) {
							pursComnInvSub.setPursInvNum(pursInvNum);
							totalAmt=totalAmt.add(Optional.ofNullable(pursComnInvSub.getNoTaxAmt()).orElse(BigDecimal.ZERO));
							b = pursComnInvSubDao.insertPursComnInvSub(pursComnInvSub);
						}
						//��ռ�ù�����ϵ���ֶ�
						if (totalAmt.compareTo(BigDecimal.ZERO)>0) {
							pursComnInv.setColor("��");
						}else  {
							pursComnInv.setColor("��");
						}
				
						pursComnInvDao.updatePursComnInvById(pursComnInv);//���ĺ����ֶ�
						if (a >= 1 || b == 1) {
							isSuccess = true;
							message = "�ɹ���Ʊ�����ɹ�";

						} else {
							isSuccess = false;
							message = "�ɹ���Ʊ����ʧ��";
						}
					} else {
						isSuccess = false;
						message = "��Ӧ�̣�" + pursComnInv.getProvrId() + "�����ڣ�����ʧ�ܣ�";
					}
				} else {
					isSuccess = false;
					message = "�ɹ���Ʊ�ţ�" + pursInvNum + "�Ѵ��ڣ�����ʧ�ܣ�";
				}
			}
			resp = BaseJson.returnRespObj("/account/PursComnInv/addPursComnInv", isSuccess, message, pursComnInv);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// �޸Ĳɹ���Ʊ
	@Override
	public String updatePursComnInv(PursComnInv pursComnInv, List<PursComnInvSub> pursComnInvSubList) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			String pursInvNum = pursComnInv.getPursInvNum();
			if (pursInvNum == null || pursInvNum.equals("")) {
				isSuccess = false;
				message = "�ɹ���Ʊ�Ų���Ϊ�գ��޸�ʧ��!";
			} else {
				if (provrDocDao.selectProvrDocByProvrId(pursComnInv.getProvrId()) != null) {
					int updateResult = pursComnInvDao.updatePursComnInvById(pursComnInv);

					int deleteResult = pursComnInvSubDao.deletePursComnInvSubByOrdrNum(pursInvNum);
					if (updateResult >= 1 && deleteResult >= 1) {
						for (PursComnInvSub pursComnInvSub : pursComnInvSubList) {
							pursComnInvSub.setPursInvNum(pursInvNum);
							pursComnInvSubDao.insertPursComnInvSub(pursComnInvSub);
						}
						isSuccess = true;
						message = "�ɹ���Ʊ�޸ĳɹ�";
					} else {
						isSuccess = false;
						message = "�ɹ���Ʊ�޸�ʧ��";
					}
				} else {
					isSuccess = false;
					message = "��Ӧ�̱��룺" + pursComnInv.getProvrId() + "�����ڣ��޸�ʧ�ܣ�";
				}
			}
			resp = BaseJson.returnRespObj("/account/PursComnInv/updatePursComnInv", isSuccess, message, pursComnInv);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
	}

	// ����ɾ���ɹ���Ʊ
	@Override
	public String deletePursComnInv(String pursInvNum) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";

		int a = pursComnInvDao.deletePursComnInvById(pursInvNum);
		int b = pursComnInvSubDao.deletePursComnInvSubByOrdrNum(pursInvNum);
		if (a >= 1 && b >= 1) {
			isSuccess = true;
			message = "����ɹ�";
		} else if (a == 0 && b == 0) {
			isSuccess = true;
			message = "û��Ҫɾ��������";
		} else {
			isSuccess = false;
			message = "����ʧ��";
		}
		try {
			resp = BaseJson.returnRespObj("/account/PursComnInv/deletePursComnInv", isSuccess, message, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
	}

	// ��ѯ�ɹ���Ʊ����
	@Override
	public String queryPursComnInvByPursInvNum(String pursInvNum) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		PursComnInv pursComnInv = pursComnInvDao.selectPursComnInvById(pursInvNum);
		if (pursComnInv != null) {
			isSuccess = true;
			message = "��ѯ�ɹ���";
		} else {
			isSuccess = false;
			message = "����" + pursInvNum + "�����ڣ�";
		}

		try {
			resp = BaseJson.returnRespObj("/account/PursComnInv/queryPursComnInvByPursInvNum", isSuccess, message,
					pursComnInv);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// ����ɾ��
	@Override
	public String deletePursComnInvList(String pursInvNum) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			List<String> lists = getList(pursInvNum);
			List<String> lists2 = new ArrayList<>();
			List<String> lists3 = new ArrayList<>();
			for (String purInvNum : lists) {
				if (pursComnInvDao.selectPursComnInvIsNtChk(purInvNum) == 0) {
					lists2.add(purInvNum);
				} else {
					lists3.add(purInvNum);
				}
			}
			if (lists2.size() > 0) {
				int a = pursComnInvDao.deletePursComnInvList(lists2);
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
			resp = BaseJson.returnRespObj("/account/PursComnInv/deletePursComnInvList", isSuccess, message, null);
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

	// ��ҳ��ѯ�ɹ���ͨ��Ʊ
	@Override
	public String queryPursComnInvList(Map map) {
		String resp = "";
		//List<String> invtyClsEncdList = getList((String) map.get("invtyClsEncd"));// ����������
		List<String> provrIdList = getList((String) map.get("provrId"));// ��Ӧ�̱���
		//map.put("invtyClsEncdList", invtyClsEncdList);
		map.put("provrIdList", provrIdList);

		List<PursComnInv> poList = pursComnInvDao.selectPursComnInvList(map);
		int count = pursComnInvDao.selectPursComnInvCount(map);

		int pageNo = Integer.parseInt(map.get("pageNo").toString());
		int pageSize = Integer.parseInt(map.get("pageSize").toString());
		int listNum = poList.size();
		int pages = count / pageSize + 1;

		try {
			resp = BaseJson.returnRespList("/account/PursComnInv/queryPursComnInvList", true, "��ѯ�ɹ���", count, pageNo,
					pageSize, listNum, pages, poList);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// ��˲ɹ���Ʊ
	@Override
	public Map<String, Object> updatePursComnInvIsNtChkList(PursComnInv pursComnInv) {
		StringBuilder message = new StringBuilder();
		Boolean isSuccess = true;
		Map<String, Object> map = new HashMap<>();
		try {
			if (pursComnInv.getIsNtChk() == 1) {
				System.out.println("�ɹ���Ʊ��� 999999999999");
				message.append(updatePursComnInvIsNtChkOK(pursComnInv).get("message"));
			} else if (pursComnInv.getIsNtChk() == 0) {
				System.out.println("�ɹ���Ʊ	���� 999999999999");
				message.append(updatePursComnInvIsNtChkNO(pursComnInv).get("message"));
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

	// �ɹ���Ʊ���
	public Map<String, Object> updatePursComnInvIsNtChkOK(PursComnInv pursComnInv) {
		String message = "";
		Boolean isSuccess = true;
		Map<String, Object> map = new HashMap<>();
		if (pursComnInvDao.selectPursComnInvIsNtChk(pursComnInv.getPursInvNum()) == 0) {
			PursComnInv pursComnInvs = pursComnInvDao.selectPursComnInvById(pursComnInv.getPursInvNum());// �ɹ���Ʊ����
			List<PursComnInvSub> pursComnInvSubList = pursComnInvs.getPursList();// �ɹ���Ʊ�ӱ�
			for (PursComnInvSub pursComInvSub : pursComnInvSubList) {
				map.put("unBllgQty", pursComInvSub.getQty().abs());// δ��Ʊ����
				map.put("unBllgAmt", pursComInvSub.getPrcTaxSum().abs());// δ��Ʊ���
				map.put("ordrNum", pursComInvSub.getIntoWhsSnglSubtabId());// �ɹ���ⵥ�ӱ����
				if (pursComInvSub.getIntoWhsSnglSubtabId() != null && pursComInvSub.getIntoWhsSnglSubtabId() != 0) {
					BigDecimal unBllgQty = intoWhsSubDao.selectUnBllgQtyByOrdrNum(map);// ���ݲɹ���ⵥ��Ų�ѯδ��Ʊ����

					if (unBllgQty != null) {
						if (unBllgQty.compareTo(pursComInvSub.getQty().abs()) == 1
								|| unBllgQty.compareTo(pursComInvSub.getQty().abs()) == 0) {
							intoWhsSubDao.updateIntoWhsSubByInvWhsBat(map);// ���ݲɹ���ⵥ�ӱ�����޸�δ��Ʊ�����ͽ��
						} else {
							isSuccess = false;
							message += "���ݺ�Ϊ��" + pursComnInv.getPursInvNum() + "�Ĳɹ���Ʊ�д����"
									+ pursComInvSub.getInvtyEncd() + "���ۼƿ�Ʊ������������������޷���ˣ�\n";
							throw new RuntimeException(message);
						}
					} // else {
//						isSuccess = false;
//						message += "���ݺ�Ϊ��" + pursComnInv.getPursInvNum() + "�Ĳɹ���Ʊ��Ӧ�Ĳɹ���ⵥ��δ��Ʊ���������ڣ��޷���ˣ�\n";
//						throw new RuntimeException(message);
//					}
				}
			}
			// ��ѯ�ɹ���ⵥ��
			String intoWhsSnglId = pursComnInvDao.selectIntoWhsSnglIdByPursComnInv(pursComnInv.getPursInvNum());
			if (intoWhsSnglId != null && !intoWhsSnglId.equals("")) {
				List<String> intoWhsIdList = getintoWhsList(intoWhsSnglId);// ��ȡ�����ⵥ����
				for (String intoWhsId : intoWhsIdList) {
					List<IntoWhsSub> intoWhsSubList = intoWhsSubDao.selectIntoWhsSubByIntoWhsSnglId(intoWhsSnglId);// ���ݲɹ���ⵥ���ݺŲ�ѯ�ɹ���ⵥ�ӱ���ϸ
					if (intoWhsSubList.size() > 0) {
						int num = 0;
						for (IntoWhsSub intoWhsSub : intoWhsSubList) {
							if (intoWhsSub.getUnBllgQty().intValue() > 0) {
								num++;
							}
						}
						if (num == 0) {
							// �޸��Ƿ�Ʊ״̬
							intoWhsDao.updateIntoWhsIsNtBllgOK(intoWhsId);
						}
					}

				}
			}

			// �޸����״̬
			int a = pursComnInvDao.updatePursComnInvIsNtChk(pursComnInv);
			if (a >= 1) {
				isSuccess = true;
				message += "���ݺ�Ϊ" + pursComnInv.getPursInvNum() + "�Ĳɹ���ͨ��Ʊ��˳ɹ���\n";
			} else {
				isSuccess = false;
				message += "���ݺ�Ϊ" + pursComnInv.getPursInvNum() + "�Ĳɹ���ͨ��Ʊ���ʧ�ܣ�\n";
				throw new RuntimeException(message);
			}
		} else if (pursComnInvDao.selectPursComnInvIsNtChk(pursComnInv.getPursInvNum()) == 1) {
			isSuccess = false;
			message += "���ݺ�Ϊ" + pursComnInv.getPursInvNum() + "�Ĳɹ���ͨ��Ʊ����ˣ�����Ҫ�ظ����\n";
			throw new RuntimeException(message);
		}
		map.put("isSuccess", isSuccess);
		map.put("message", message);
		return map;
	}

	// �ɹ���Ʊ����
	public Map<String, Object> updatePursComnInvIsNtChkNO(PursComnInv pursComnInv) {
		String message = "";
		Boolean isSuccess = true;
		Map<String, Object> map = new HashMap<>();
		// �ж��Ƿ����
		if (pursComnInvDao.selectPursComnInvIsNtBookEntry(pursComnInv.getPursInvNum()) == 0) {
			if (pursComnInvDao.selectPursComnInvIsNtChk(pursComnInv.getPursInvNum()) == 1) {
				PursComnInv pursComnInvs = pursComnInvDao.selectPursComnInvById(pursComnInv.getPursInvNum());// �ɹ���Ʊ����
				List<PursComnInvSub> pursComnInvSubList = pursComnInvs.getPursList();// �ɹ���Ʊ�ӱ�
				for (PursComnInvSub pursComInvSub : pursComnInvSubList) {
					map.put("ordrNum", pursComInvSub.getIntoWhsSnglSubtabId());// �ɹ���ⵥ�ӱ����
					if (pursComInvSub.getIntoWhsSnglSubtabId() != null && pursComInvSub.getIntoWhsSnglSubtabId() != 0) {
						BigDecimal unBllgQty = intoWhsSubDao.selectUnBllgQtyByOrdrNum(map);// ���ݲɹ���ⵥ��Ų�ѯδ��Ʊ����
						if (unBllgQty != null) {
							map.put("unBllgQty", pursComInvSub.getQty().abs().multiply(new BigDecimal(-1)));// δ��Ʊ����
							map.put("unBllgAmt", pursComInvSub.getPrcTaxSum().abs().multiply(new BigDecimal(-1)));// δ��Ʊ���
							intoWhsSubDao.updateIntoWhsSubByInvWhsBat(map);
						} // else {
//							isSuccess = false;
//							message += "���ݺ�Ϊ��" + pursComnInv.getPursInvNum() + "�Ĳɹ���Ʊ��Ӧ�Ĳɹ���ⵥ��δ��Ʊ���������ڣ��޷���ˣ�\n";
//							throw new RuntimeException(message);
//						}	
					}
				}
				// ��ѯ�ɹ���ⵥ��
				String intoWhsSnglId = pursComnInvDao.selectIntoWhsSnglIdByPursComnInv(pursComnInv.getPursInvNum());
				if (intoWhsSnglId != null && intoWhsSnglId != "") {
					List<String> intoWhsIdList = getintoWhsList(intoWhsSnglId);// ��ȡ��ⵥ����
					for (String intoWhsId : intoWhsIdList) {
						List<IntoWhsSub> intoWhsSubList = intoWhsSubDao.selectIntoWhsSubByIntoWhsSnglId(intoWhsSnglId);// ���ݲɹ���ⵥ���ݺŲ�ѯ�ɹ���ⵥ�ӱ���ϸ
						int num = 0;
						for (IntoWhsSub intoWhsSub : intoWhsSubList) {
							if (intoWhsSub.getUnBllgQty().intValue() > 0) {
								num++;
							}
						}
						if (num > 0) {
							// �޸��Ƿ�Ʊ״̬
							intoWhsDao.updateIntoWhsIsNtBllgNO(intoWhsId);
						}
					}
				}
				int a = pursComnInvDao.updatePursComnInvIsNtChk(pursComnInv);
				if (a >= 1) {
					isSuccess = true;
					message += "���ݺ�Ϊ" + pursComnInv.getPursInvNum() + "�Ĳɹ���ͨ��Ʊ����ɹ���\n";
				} else {
					isSuccess = false;
					message += "���ݺ�Ϊ" + pursComnInv.getPursInvNum() + "�Ĳɹ���ͨ��Ʊ����ʧ�ܣ�\n";
					throw new RuntimeException(message);
				}

			} else {
				isSuccess = false;
				message += "���ݺ�Ϊ" + pursComnInv.getPursInvNum() + "�Ĳɹ���ͨ��Ʊδ��ˣ�������˸õ���\n";
				throw new RuntimeException(message);
			}
		} else {
			isSuccess = false;
			message += "���ݺ�Ϊ" + pursComnInv.getPursInvNum() + "�Ĳɹ���ͨ��Ʊ�Ѽ����޷�����\n";
			throw new RuntimeException(message);
		}
		map.put("isSuccess", isSuccess);
		map.put("message", message);
		return map;
	}

	// ���ݲɹ���Ʊ��˵�ʱ���޸Ķ�Ӧ����ⵥ�Ľ��
	private void updateIntoWhsByPursComnInv(String intoWhsId) {
		IntoWhs intoWhs = intoWhsDao.selectIntoWhsByIntoWhsSnglId(intoWhsId);
		List<IntoWhsSub> intoWhsSub = intoWhs.getIntoWhsSub();

	}

	/**
	 * id����list
	 * 
	 * @param id id(����Ѷ��ŷָ�)
	 * @return List����
	 */

	public List<String> getintoWhsList(String id) {
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

	// �ϲ��ɹ���ⵥ����ɹ���Ʊ��
	@Override
	public String selectPursComnInvBingList(List<IntoWhs> intoWhsList) {
		String resp = "";
		String message = "";
		Boolean isSuccess = true;
		// ʵ����һ���µĶ��� �ɹ���ͨ��Ʊ����
		PursComnInv purCoInv = new PursComnInv();
		List<PursComnInvSub> pursComnInvSubList = new ArrayList<>();
		Map<String, Object> map = new HashMap<>();
		// ��ȡList�����е�һ����Ӧ��������map��
		IntoWhs intoWhs = intoWhsList.get(0);
		// ����Ӧ�̱��룬���ű��룬ҵ��Ա����ƴ������
		String str1 = intoWhs.getProvrId() + intoWhs.getDeptId() + intoWhs.getAccNum();
		map.put("uniqInd", str1);
		String intoWhsId = "";
		for (IntoWhs intoWh : intoWhsList) {
			intoWhsId += intoWh.getIntoWhsSnglId() + ",";
			String str2 = intoWh.getProvrId() + intoWh.getDeptId() + intoWh.getAccNum();
			if (str2.equals(map.get("uniqInd").toString()) == false) {
				isSuccess = false;
				message = "����ѡ����ͬ��Ӧ�̡����š�ҵ��Ա�ĵ��ݽ���������������ѡ��";
			} else if (str2.equals(map.get("uniqInd").toString()) == true) {
				try {
					BeanUtils.copyProperties(purCoInv, intoWh);// ���ɹ���������������ɹ���ͨ��Ʊ����
					purCoInv.setIntoWhsSnglId(intoWhsId.substring(0, intoWhsId.length() - 1));
					// ��������������ѯ��ص��ӱ���Ϣ
					List<IntoWhsSub> intoWhsSubList = intoWhsSubDao
							.selectIntoWhsSubByIntoWhsSnglId(intoWh.getIntoWhsSnglId());
					for (IntoWhsSub intoWhsSub : intoWhsSubList) {
						PursComnInvSub pursComnInvSub = new PursComnInvSub();
						BeanUtils.copyProperties(pursComnInvSub, intoWhsSub);// ���ɹ���ⵥ�ӱ������ɹ���ͨ��Ʊ�ӱ�
						pursComnInvSubList.add(pursComnInvSub);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					throw new RuntimeException("��ѯ�������������ٽ���������");
				}
				isSuccess = true;
				message = "�����ɹ���";
			}

		}
		purCoInv.setPursList(pursComnInvSubList);
		try {
			resp = BaseJson.returnRespObj("/account/PursComnInv/selectPursComnInvBingList", isSuccess, message,
					purCoInv);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// ����
	@Override
	public String uploadFileAddDb(MultipartFile file, int i) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		Map<String, PursComnInv> pursComnInvMap = null;

		if (i == 0) {
			pursComnInvMap = uploadScoreInfo(file);
		} else if (i == 1) {
			pursComnInvMap = uploadScoreInfoU8(file);
		} else {
			isSuccess = false;
			message = "������쳣����";
			throw new RuntimeException(message);
		}

		// ��MapתΪList��Ȼ���������븸������
		List<PursComnInv> pursComnInvList = pursComnInvMap.entrySet().stream().map(e -> e.getValue())
				.collect(Collectors.toList());
		List<List<PursComnInv>> pursComnInvLists = Lists.partition(pursComnInvList, 1000);
		for (List<PursComnInv> pursComnInv : pursComnInvLists) {
			pursComnInvDao.insertPursComnInvUpload(pursComnInv);
		}
		List<PursComnInvSub> pursComnInvSubList = new ArrayList<>();
		int flag = 0;
		for (PursComnInv entry : pursComnInvList) {
			flag++;
			// ���������ֱ�͸���Ĺ����ֶ�
			List<PursComnInvSub> tempList = entry.getPursList();
			tempList.forEach(s -> s.setPursInvNum(entry.getPursInvNum()));
			pursComnInvSubList.addAll(tempList);
			// �������룬ÿ���ڵ���1000������һ��
			if (pursComnInvSubList.size() >= 1000 || pursComnInvMap.size() == flag) {
				pursComnInvSubDao.insertPursComnInvSubUpload(pursComnInvSubList);
				pursComnInvSubList.clear();
			}
		}

		isSuccess = true;
		message = "�ɹ���Ʊ����ɹ���";
		try {
			if (i == 0) {
				resp = BaseJson.returnRespObj("/account/PursComnInv/uploadPursComnInvFile", isSuccess, message, null);
			} else if (i == 1) {
				resp = BaseJson.returnRespObj("/account/PursComnInv/uploadPursComnInvFileU8", isSuccess, message, null);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// ����excle
	private Map<String, PursComnInv> uploadScoreInfo(MultipartFile file) {
		Map<String, PursComnInv> temp = new HashMap<>();
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
				String orderNo = GetCellData(r, "�ɹ���Ʊ��");
				// ����ʵ����
				PursComnInv pursComnInv = new PursComnInv();
				if (temp.containsKey(orderNo)) {
					pursComnInv = temp.get(orderNo);
				}
				// ȡ����ǰ�е�1����Ԫ�����ݣ�����װ��pursOrdrʵ��ĸ���������
				pursComnInv.setPursInvNum(orderNo);// �ɹ���Ʊ����
				if (GetCellData(r, "��Ʊ����") == null || GetCellData(r, "��Ʊ����").equals("")) {
					pursComnInv.setBllgDt(null);
				} else {
					pursComnInv.setBllgDt(GetCellData(r, "��Ʊ����").replaceAll("[^0-9:-]", " "));// ��Ʊ����
				}
				pursComnInv.setInvTyp(GetCellData(r, "��Ʊ����")); // '��Ʊ����'
				pursComnInv.setProvrNm(GetCellData(r, "��Ӧ������"));// ��Ӧ������
				pursComnInv.setAccNum(GetCellData(r, "ҵ��Ա����")); // ҵ��Ա����'
				pursComnInv.setUserName(GetCellData(r, "ҵ��Ա����")); // 'ҵ��Ա����'
				pursComnInv.setPursTypId(GetCellData(r, "�ɹ����ͱ���"));// "�ɹ����ͱ���"
				pursComnInv.setProvrId(GetCellData(r, "��Ӧ�̱���")); // '��Ӧ�̱���'
				pursComnInv.setInvTyp(GetCellData(r, "��Ʊ����")); // '��Ʊ����'
				pursComnInv.setFormTypEncd(GetCellData(r, "�������ͱ���"));// �������ͱ���
//				pursComnInv.setToFormTypEncd(GetCellData(r, "��Դ�������ͱ���"));//��Դ�������ͱ���
				pursComnInv.setToFormTypEncd(
						GetBigDecimal(GetCellData(r, "����"), 8).compareTo(BigDecimal.ZERO) == -1 ? "006" : "002");// ��������
				pursComnInv.setDeptId(GetCellData(r, "���ű���")); // '���ű���'

				pursComnInv.setIsNtChk(new Double(GetCellData(r, "�Ƿ����")).intValue()); // '�Ƿ����', int(11
				pursComnInv.setChkr(GetCellData(r, "�����")); // '�����', varchar(200
				if (GetCellData(r, "���ʱ��") == null || GetCellData(r, "���ʱ��").equals("")) {
					pursComnInv.setChkTm(null);
				} else {
					pursComnInv.setChkTm(GetCellData(r, "���ʱ��").replaceAll("[^0-9:-]", " "));// ���ʱ��
				}
				pursComnInv.setIsNtMakeVouch(new Double(GetCellData(r, "�Ƿ�����ƾ֤")).intValue()); // '�Ƿ����', int(11
				pursComnInv.setMakDocPers(GetCellData(r, "��ƾ֤��")); // '�����', varchar(200
				if (GetCellData(r, "��ƾ֤ʱ��") == null || GetCellData(r, "��ƾ֤ʱ��").equals("")) {
					pursComnInv.setMakDocTm(null);
				} else {
					pursComnInv.setMakDocTm(GetCellData(r, "��ƾ֤ʱ��").replaceAll("[^0-9:-]", " "));// ���ʱ��
				}

				pursComnInv.setIsNtBookEntry(new Double(GetCellData(r, "�Ƿ����")).intValue()); // �Ƿ����
				pursComnInv.setBookEntryPers(GetCellData(r, "������")); // ������',
				if (GetCellData(r, "����ʱ��") == null || GetCellData(r, "����ʱ��").equals("")) {
					pursComnInv.setBookEntryTm(null);
				} else {
					pursComnInv.setBookEntryTm(GetCellData(r, "����ʱ��").replaceAll("[^0-9:-]", " "));// ����ʱ��
				}

				pursComnInv.setSetupPers(GetCellData(r, "������")); // '������', varchar(200
				if (GetCellData(r, "����ʱ��") == null || GetCellData(r, "����ʱ��").equals("")) {
					pursComnInv.setSetupTm(null);
				} else {
					pursComnInv.setSetupTm(GetCellData(r, "����ʱ��").replaceAll("[^0-9:-]", " "));// ����ʱ��
				}
				pursComnInv.setMdfr(GetCellData(r, "�޸���")); // '�޸���', varchar(200
				if (GetCellData(r, "�޸�ʱ��") == null || GetCellData(r, "�޸�ʱ��").equals("")) {
					pursComnInv.setModiTm(null);
				} else {
					pursComnInv.setModiTm(GetCellData(r, "�޸�ʱ��").replaceAll("[^0-9:-]", " "));// ����ʱ��
				}
//				pursComnInv.setPursOrdrId(GetCellData(r, "�ɹ���������"));
				pursComnInv.setIntoWhsSnglId(GetCellData(r, "�ɹ���ⵥ����"));
				pursComnInv.setMemo(GetCellData(r, "��ͷ��ע")); // '��ע', varchar(2000
				List<PursComnInvSub> pursComnInvSubList = pursComnInv.getPursList();// �ɹ���Ʊ�ӱ�
				if (pursComnInvSubList == null) {
					pursComnInvSubList = new ArrayList<>();
				}
				PursComnInvSub pursComnInvSub = new PursComnInvSub();

//				pursComnInvSub.setWhsEncd(GetCellData(r, "�ֿ����")); // '�ֿ����',
				pursComnInvSub.setInvtyEncd(GetCellData(r, "�������")); // '�������',
				pursComnInvSub.setQty(GetBigDecimal(GetCellData(r, "����"), 8)); // '����',
				pursComnInvSub.setBxQty(GetBigDecimal(GetCellData(r, "����"), 8)); // '����',
				pursComnInvSub.setBxRule(GetBigDecimal(GetCellData(r, "���"), 8));// ���
				pursComnInvSub.setCntnTaxUprc(GetBigDecimal(GetCellData(r, "��˰����"), 8)); // '��˰����',
				pursComnInvSub.setPrcTaxSum(GetBigDecimal(GetCellData(r, "��˰�ϼ�"), 8)); // '��˰�ϼ�',
				pursComnInvSub.setNoTaxUprc(GetBigDecimal(GetCellData(r, "��˰����"), 8)); // '��˰����',
				pursComnInvSub.setNoTaxAmt(GetBigDecimal(GetCellData(r, "��˰���"), 8)); // '��˰���',
				pursComnInvSub.setTaxAmt(GetBigDecimal(GetCellData(r, "˰��"), 8)); // '˰��',
				pursComnInvSub.setTaxRate(GetBigDecimal(GetCellData(r, "˰��"), 8)); // '˰��',
				pursComnInvSub.setIntlBat(GetCellData(r, "��������")); // ��������
				pursComnInvSub.setBatNum(GetCellData(r, "����")); // '����',
				pursComnInvSub.setMemo(GetCellData(r, "���屸ע")); // '��ע',
				pursComnInvSub.setCrspdIntoWhsSnglNum(GetCellData(r, "�ɹ���ⵥ����"));// �ӱ��еĲɹ���ⵥ����

//				pursComnInvSub.setIntoWhsSnglSubtabId(Long.parseLong(GetCellData(r,"��Դ�����ӱ����")));//��Դ�����ӱ����

				if (GetCellData(r, "��Դ�����ӱ����") == null || GetCellData(r, "��Դ�����ӱ����") == "") {
					pursComnInvSub.setIntoWhsSnglSubtabId(null);
				} else {
					pursComnInvSub.setIntoWhsSnglSubtabId(Long.parseLong(GetCellData(r, "��Դ�����ӱ����")));
				}

				pursComnInvSubList.add(pursComnInvSub);

				pursComnInv.setPursList(pursComnInvSubList);
				temp.put(orderNo, pursComnInv);
			}
			fileIn.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("�ļ��ĵ�" + j + "�е����ʽ�����޷�����!" + e.getMessage());
		}
		return temp;
	}

	// ����excle
	private Map<String, PursComnInv> uploadScoreInfoU8(MultipartFile file) {
		Map<String, PursComnInv> temp = new HashMap<>();
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
				PursComnInv pursComnInv = new PursComnInv();
				if (temp.containsKey(orderNo)) {
					pursComnInv = temp.get(orderNo);
				}
				// ȡ����ǰ�е�1����Ԫ�����ݣ�����װ��pursOrdrʵ��ĸ���������
				pursComnInv.setPursInvNum(orderNo);// �ɹ���Ʊ����
				if (GetCellData(r, "��Ʊ����") == null || GetCellData(r, "��Ʊ����").equals("")) {
					pursComnInv.setBllgDt(null);
				} else {
					pursComnInv.setBllgDt(GetCellData(r, "��Ʊ����").replaceAll("[^0-9:-]", " "));// ��Ʊ����
				}
				pursComnInv.setAccNum(GetCellData(r, "ҵ��Ա����")); // ҵ��Ա����'
				pursComnInv.setUserName(GetCellData(r, "ҵ��Ա")); // 'ҵ��Ա����'
				pursComnInv.setPursTypId("1");// "�ɹ����ͱ���"
				pursComnInv.setProvrId(GetCellData(r, "��Ӧ�̱���")); // '��Ӧ�̱���'
				pursComnInv.setInvTyp(GetCellData(r, "��Ʊ����")); // '��Ʊ����'
				if (GetCellData(r, "��Ʊ����") != null && GetCellData(r, "��Ʊ����").equals("ר�÷�Ʊ")) {
					pursComnInv.setFormTypEncd("019");// �������ͱ���

				} else if (GetCellData(r, "��Ʊ����") != null && GetCellData(r, "��Ʊ����").equals("��ͨ��Ʊ")) {
					pursComnInv.setFormTypEncd("020");// �������ͱ���
				}
				pursComnInv.setToFormTypEncd(GetCellData(r, "��Դ�������ͱ���"));// ��Դ�������ͱ���

				pursComnInv.setDeptId(GetCellData(r, "���ű���")); // '���ű���'

				pursComnInv.setIsNtChk(0); // '�Ƿ����', int(11
				pursComnInv.setIsNtMakeVouch(0); // '�Ƿ�����ƾ֤',
				pursComnInv.setIsNtBookEntry(0); // �Ƿ����

				pursComnInv.setSetupPers(GetCellData(r, "�Ƶ���")); // '������', varchar(200
				if (GetCellData(r, "�Ƶ�ʱ��") == null || GetCellData(r, "�Ƶ�ʱ��").equals("")) {
					pursComnInv.setSetupTm(null);
				} else {
					pursComnInv.setSetupTm(GetCellData(r, "�Ƶ�ʱ��").replaceAll("[^0-9:-]", " "));// ����ʱ��
				}
				pursComnInv.setMdfr(GetCellData(r, "�޸���")); // '�޸���', varchar(200
				if (GetCellData(r, "�޸�ʱ��") == null || GetCellData(r, "�޸�ʱ��").equals("")) {
					pursComnInv.setModiTm(null);
				} else {
					pursComnInv.setModiTm(GetCellData(r, "�޸�ʱ��").replaceAll("[^0-9:-]", " "));// ����ʱ��
				}
				pursComnInv.setIntoWhsSnglId(GetCellData(r, "��ⵥ��"));
				pursComnInv.setMemo(GetCellData(r, "��ע")); // '��ע', varchar(2000
				List<PursComnInvSub> pursComnInvSubList = pursComnInv.getPursList();// �ɹ���Ʊ�ӱ�
				if (pursComnInvSubList == null) {
					pursComnInvSubList = new ArrayList<>();
				}
				PursComnInvSub pursComnInvSub = new PursComnInvSub();
				pursComnInvSub.setInvtyEncd(GetCellData(r, "�������")); // '�������',
				pursComnInvSub.setQty(GetBigDecimal(GetCellData(r, "����"), 8)); // '����',
				pursComnInvSub.setBxQty(GetBigDecimal(GetCellData(r, "����"), 8)); // '����',
				pursComnInvSub.setBxRule(GetBigDecimal(GetCellData(r, "���"), 8));// ���
				pursComnInvSub.setCntnTaxUprc(GetBigDecimal(GetCellData(r, "ԭ�Һ�˰����"), 8)); // '��˰����',
				pursComnInvSub.setPrcTaxSum(GetBigDecimal(GetCellData(r, "ԭ�Ҽ�˰�ϼ�"), 8)); // '��˰�ϼ�',
				pursComnInvSub.setNoTaxUprc(GetBigDecimal(GetCellData(r, "ԭ����˰����"), 8)); // '��˰����',
				pursComnInvSub.setNoTaxAmt(GetBigDecimal(GetCellData(r, "ԭ�ҽ��"), 8)); // '��˰���',
				pursComnInvSub.setTaxAmt(GetBigDecimal(GetCellData(r, "ԭ��˰��"), 8)); // '˰��',
				pursComnInvSub.setTaxRate(GetBigDecimal(GetCellData(r, "˰��"), 8)); // '˰��',
				pursComnInvSub.setIntlBat(GetCellData(r, "��������")); // ��������
				pursComnInvSub.setBatNum(GetCellData(r, "����")); // '����',
				pursComnInvSub.setCrspdIntoWhsSnglNum(GetCellData(r, "��ⵥ��"));// �ӱ��еĲɹ���ⵥ����
				if (GetCellData(r, "��Դ�����ӱ����") == null || GetCellData(r, "��Դ�����ӱ����") == "") {
					pursComnInvSub.setIntoWhsSnglSubtabId(null);
				} else {
					pursComnInvSub.setIntoWhsSnglSubtabId(Long.parseLong(GetCellData(r, "��Դ�����ӱ����")));
				}

				pursComnInvSubList.add(pursComnInvSub);

				pursComnInv.setPursList(pursComnInvSubList);
				temp.put(orderNo, pursComnInv);
			}
			fileIn.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("�ļ��ĵ�" + j + "�е����ʽ�����޷�����!" + e.getMessage());
		}
		return temp;
	}

	// ԭ���ĵ����ӿ�,����ɾ
	@Override
	public String upLoadPursComnInvList(Map map) {
		String resp = "";
		List<String> invtyClsEncdList = getList((String) map.get("invtyClsEncd"));// ����������
		List<String> provrIdList = getList((String) map.get("provrId"));// ��Ӧ�̱���
		map.put("invtyClsEncdList", invtyClsEncdList);
		map.put("provrIdList", provrIdList);
		List<PursComnInv> poList = pursComnInvDao.printingPursComnInvList(map);
		try {
			resp = BaseJson.returnRespObjList("purc/PursComnInv/printingPursComnInvList", true, "��ѯ�ɹ���", null, poList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
	}

	// ����ʹ�õĽӿ�
	@Override
	public String printPursComnInvList(Map map) {
		String resp = "";
		List<String> invtyClsEncdList = null;// ����������
		List<String> provrIdList = null;// ��Ӧ�̱���
		if (map.get("invtyClsEncd") != null) {
			invtyClsEncdList = getList((String) map.get("invtyClsEncd"));

		}
		if (map.get("provrId") != null) {
			provrIdList = getList((String) map.get("provrId"));
		}
		map.put("invtyClsEncdList", invtyClsEncdList);
		map.put("provrIdList", provrIdList);
		List<zizhu> poList = pursComnInvDao.printPursComnInvList(map);

		try {
			resp = BaseJson.returnRespObjListAnno("purc/PursComnInv/printPursComnInvList", true, "��ѯ�ɹ���", null, poList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
	}

	// ��������
	public static class zizhu {
		@JsonProperty("�ɹ���Ʊ��")
		public String pursInvNum;
		@JsonProperty("��Ʊ����")
		public String bllgDt;
		@JsonProperty("��Ʊ����")
		public String invTyp;
		@JsonProperty("��Ӧ������")
		public String provrNm;
		@JsonProperty("��������")
		public String deptName;
		@JsonProperty("��Ӧ��ⵥ��")
		public String crspdIntoWhsSnglNum;
		@JsonProperty("��ͷ��ע")
		public String memo;
		@JsonProperty("�Ƿ����")
		public Integer isNtChk;
		@JsonProperty("��ⵥ��")
		public String intoWhsSnglId;
		@JsonProperty("���")
		public Integer ordrNum;
		@JsonProperty("�������")
		public String invtyEncd;
		@JsonProperty("�ֿ����")
		public String whsEncd;
		@JsonProperty("�������")
		public String invtyNm;
		@JsonProperty("��Ŀ����")
		public String projEncd;
		@JsonProperty("����ͺ�")
		public String spcModel;
		@JsonProperty("��������λ")
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
		public BigDecimal taxRate;
		@JsonProperty("˰��")
		public BigDecimal taxAmt;
		@JsonProperty("���")
		public BigDecimal bxRule;
		@JsonProperty("����")
		public BigDecimal bxQty;
		@JsonProperty("��Ӧ������")
		public String crspdBarCd;
		@JsonProperty("����")
		public String batNum;
		@JsonProperty("���屸ע")
		public String memos;

		// ����

		@JsonIgnore
		@JsonProperty("��ͷ˰��")
		public String tabHeadTaxRate;

		@JsonProperty("�����")
		@JsonIgnore
		public String chkr;

		@JsonProperty("���ʱ��")
		@JsonIgnore
		public String chkTm;

		@JsonProperty("������ϵ��")
		@JsonIgnore
		public String provrContcr;

		@JsonProperty("������������")
		@JsonIgnore
		public String provrBankNm;

		@JsonProperty("������")
		@JsonIgnore
		public String setupPers;

		@JsonProperty("��������")
		@JsonIgnore
		public String setupTm;

		@JsonProperty("�Ƿ����")
		@JsonIgnore
		public Integer isNtBookEntry;

		@JsonProperty("������")
		@JsonIgnore
		public String bookEntryPers;

		@JsonProperty("����ʱ��")
		@JsonIgnore
		public String bookEntryTm;

		@JsonProperty("�޸���")
		@JsonIgnore
		public String mdfr;

		@JsonProperty("�޸�����")
		@JsonIgnore
		public String modiTm;

		@JsonProperty("�ɹ����ͱ���")
		@JsonIgnore
		public String pursTypId;

		@JsonProperty("��Ӧ�̱���")
		@JsonIgnore
		public String provrId;

		@JsonProperty("���ű���")
		@JsonIgnore
		public String deptId;

		@JsonProperty("ҵ��Ա����")
		@JsonIgnore
		public String accNum;

		@JsonProperty("�ɹ������� ")
		@JsonIgnore
		public String pursOrdrId;

		@JsonProperty("��Ŀ����")
		@JsonIgnore
		public String subjId;

		@JsonProperty("�Ƶ���")
		@JsonIgnore
		public String makDocPers;

		@JsonProperty("�Ƶ�ʱ��")
		@JsonIgnore
		public String makDocTm;

		@JsonProperty("ƾ֤��")
		@JsonIgnore
		public String vouchNum;

		@JsonProperty("�ɹ���������")
		@JsonIgnore
		public String pursTypNm;

		@JsonProperty("�û�����")
		@JsonIgnore
		public String userName;

		@JsonProperty("�������ͱ���")
		@JsonIgnore
		public String formTypEncd;

		@JsonProperty("��Դ�������ͱ���")
		@JsonIgnore
		public String toFormTypEncd;

		@JsonProperty("�Ƿ�����ƾ֤")
		@JsonIgnore
		public Integer isNtMakeVouch;

		@JsonProperty("��ƾ֤��")
		@JsonIgnore
		public String makVouchPers;

		@JsonProperty("��ƾ֤ʱ��")
		@JsonIgnore
		public String makVouchTm;

		@JsonProperty("���һ������")
		@JsonIgnore
		public String invtyFstLvlCls;

		@JsonProperty("��ⵥ�ӱ�id")
		@JsonIgnore
		public Long intoWhsSnglSubtabId;

		@JsonProperty("��������")
		@JsonIgnore
		public String intlBat;

		@JsonProperty("������λ����")
		@JsonIgnore
		public String measrCorpId;

		@JsonProperty("��������")
		@JsonIgnore
		public String stlDt;

		@JsonProperty("����ʱ��")
		@JsonIgnore
		public String stlTm;

		@JsonProperty("�ֿ�")
		@JsonIgnore
		public String whsNm;

		@JsonProperty("�������")
		@JsonIgnore
		public String invtyCd;

		@JsonProperty("�Ƿ��˻�")
		@JsonIgnore
		public Integer isNtRtnGoods;

		@JsonProperty("����������")
		@JsonIgnore
		public String baoZhiQiDt;

		@JsonProperty("��Ʒ")
		@JsonIgnore
		public String gift;

		@JsonProperty("��Ŀ����")
		@JsonIgnore
		public String projNm;

	}

	@Override
	public U8PursComnInv encapsulation(PursComnInv pursComnInv, List<PursComnInvSub> list) throws Exception {
		// �ж���-->������Ʊ
		U8PursComnInv dataRow = new U8PursComnInv();
		// ����
		dataRow.setDscode(pursComnInv.getPursInvNum());
		dataRow.setVouttype(pursComnInv.getInvTyp().equals("�ɹ�ר�÷�Ʊ") ? "01" : "02");// ��Ʊ���� רƱ01 ��Ʊ02
		dataRow.setCptcode(pursComnInv.getPursTypId().trim().length() < 1 ? "1" : pursComnInv.getPursTypId());// �ɹ����ͱ���

		dataRow.setDdate(pursComnInv.getBllgDt());// ��������
		dataRow.setCdepcode(pursComnInv.getDeptId());// ���ű���
		dataRow.setCvencode(pursComnInv.getProvrId());// ��Ӧ��
		dataRow.setItaxrate(pursComnInv.getTabHeadTaxRate() != null ? new BigDecimal(pursComnInv.getTabHeadTaxRate())
				: new BigDecimal(13));// ��ͷ˰��

		dataRow.setCpersoncode(pursComnInv.getAccNum());// ��Ա����
		dataRow.setRemark(pursComnInv.getMemo());// ��ͷ��ע
		// �ӱ���
		ArrayList<U8PursComnInvSub> detailsList = new ArrayList<U8PursComnInvSub>();
		// ѭ������ӱ�
		list.forEach(item -> {
			U8PursComnInvSub details = new U8PursComnInvSub();
			details.setCinvcode(item.getInvtyEncd());// �������item.getInvtyEncd()
			details.setQuantity(item.getQty());// ����
			details.setIprice(item.getNoTaxUprc());// ����˰����
			details.setItax(item.getTaxAmt());// ˰��
			details.setCitemcode(item.getProjEncd());// ��Ŀ����(����Ϊ��)
			details.setItaxrate(item.getTaxRate());// ˰��
			detailsList.add(details);
		});

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
			System.err.println("�������:::" + method.toString());
			// Զ�̵���web����
			OMElement result = serviceClient.sendReceive(method);
			resultStr = result.toString();
		} catch (AxisFault axisFault) {
			axisFault.printStackTrace();

		}
		return resultStr;
	}

	@Override

	public String pushToU8(String ids) throws Exception {
		Logger logger = LoggerFactory.getLogger(PursComnInvServiceImpl.class);

		List<String> idList = getList(ids);
		List<PursComnInv> pursComnInvList = pursComnInvDao.selectComnInvs(idList);

		ArrayList<U8PursComnInv> rowList = new ArrayList<U8PursComnInv>();

		for (PursComnInv item : pursComnInvList) {

			rowList.add(encapsulation(item, item.getPursList()));
		}
		U8PursComnInvTable table = new U8PursComnInvTable();

		table.setRowList(rowList);

		/********** �����ǽӿڶԽ�************ */
		String methodName = "SetPuBill";
		String dataXmlStr = JacksonUtil.getXmlStr(table).replace(",", "");
		String ztCodeStr = "905";

		String resultStr = connectToU8(methodName, dataXmlStr, ztCodeStr);

		System.err.println("���ص�XML�ṹ::" + resultStr);

		/************* �ӿڽ��� ***********/

		MappingIterator<U8PursComnInvResponse> iterator = JacksonUtil.getResponse("SetPuBillResult",
				U8PursComnInvResponse.class, resultStr);

		ArrayList<U8PursComnInvResponse> failedList = new ArrayList<U8PursComnInvResponse>();

		StringBuffer message = new StringBuffer();
		int count = 0;
		while (iterator.hasNext()) {
			U8PursComnInvResponse response = (U8PursComnInvResponse) iterator.next();

			if (response.getType() == 1 && response.getDscode() != null) {
				failedList.add(response);
				logger.info("����: " + response.getDscode() + "�ķ�Ʊ����ʧ��,ԭ��Ϊ :" + response.getInfor());

			} else if (response.getType() == 0) {
				pursComnInvDao.updatePushed(response.getDscode());
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

}
