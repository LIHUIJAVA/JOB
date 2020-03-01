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

//采购普通发票主表
@org.springframework.stereotype.Service
@Transactional
public class PursComnInvServiceImpl<E> extends poiTool implements PursComnInvService {

	@Autowired
	private PursComnInvDao pursComnInvDao;
	@Autowired
	private PursComnInvSubDao pursComnInvSubDao;
	@Autowired
	private IntoWhsDao intoWhsDao;// 采购入库单子表
	@Autowired
	private IntoWhsSubDao intoWhsSubDao;// 采购入库单子表
	@Autowired
	private ProvrDocDao provrDocDao;
	@Autowired
	private GetOrderNo getOrderNo;// 订单号

	// 新增采购普通发票
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
				message = "采购发票号不能为空，新增失败！";
				String number = getOrderNo.getSeqNo("CGFP", userId, loginTime);
				if (pursComnInvDao.selectPursComnInvByIds(pursInvNum) == null) {
					pursComnInv.setPursInvNum(number);
					if (provrDocDao.selectProvrDocByProvrId(pursComnInv.getProvrId()) != null) {
						int a = 0;
						int b = 0;
						a = pursComnInvDao.insertPursComnInv(pursComnInv);
						//根据发票金额区分红蓝,金额小于零为红字,大于零为蓝字
						BigDecimal totalAmt=BigDecimal.ZERO;
						for (PursComnInvSub pursComnInvSub : pursComnInvSubList) {
							pursComnInvSub.setPursInvNum(pursComnInv.getPursInvNum());
							totalAmt=totalAmt.add(pursComnInvSub.getNoTaxAmt());
							b = pursComnInvSubDao.insertPursComnInvSub(pursComnInvSub);
						}
						//先占用供方联系人字段
						if (totalAmt.compareTo(BigDecimal.ZERO)>0) {
							pursComnInv.setColor("蓝");
						}else  {
							pursComnInv.setColor("红");
						}
						
						pursComnInvDao.updatePursComnInvById(pursComnInv);//更改红蓝字段
						if (a >= 1 || b == 1) {
							isSuccess = true;
							message = "采购发票新增成功";

						} else {
							isSuccess = false;
							message = "采购发票新增失败";
						}
					} else {
						isSuccess = false;
						message = "供应商：" + pursComnInv.getProvrId() + "不存在，新增失败！";
					}
				} else {
					isSuccess = false;
					message = "采购发票号：" + pursInvNum + "已存在，新增失败！";
				}

			} else {
				if (pursComnInvDao.selectPursComnInvByIds(pursInvNum) == null) {
					if (provrDocDao.selectProvrDocByProvrId(pursComnInv.getProvrId()) != null) {
						int a = 0;
						int b = 0;
						a = pursComnInvDao.insertPursComnInv(pursComnInv);
						//根据发票金额区分红蓝,金额小于零为红字,大于零为蓝字
						BigDecimal totalAmt=BigDecimal.ZERO;
						for (PursComnInvSub pursComnInvSub : pursComnInvSubList) {
							pursComnInvSub.setPursInvNum(pursInvNum);
							totalAmt=totalAmt.add(Optional.ofNullable(pursComnInvSub.getNoTaxAmt()).orElse(BigDecimal.ZERO));
							b = pursComnInvSubDao.insertPursComnInvSub(pursComnInvSub);
						}
						//先占用供方联系人字段
						if (totalAmt.compareTo(BigDecimal.ZERO)>0) {
							pursComnInv.setColor("蓝");
						}else  {
							pursComnInv.setColor("红");
						}
				
						pursComnInvDao.updatePursComnInvById(pursComnInv);//更改红蓝字段
						if (a >= 1 || b == 1) {
							isSuccess = true;
							message = "采购发票新增成功";

						} else {
							isSuccess = false;
							message = "采购发票新增失败";
						}
					} else {
						isSuccess = false;
						message = "供应商：" + pursComnInv.getProvrId() + "不存在，新增失败！";
					}
				} else {
					isSuccess = false;
					message = "采购发票号：" + pursInvNum + "已存在，新增失败！";
				}
			}
			resp = BaseJson.returnRespObj("/account/PursComnInv/addPursComnInv", isSuccess, message, pursComnInv);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// 修改采购发票
	@Override
	public String updatePursComnInv(PursComnInv pursComnInv, List<PursComnInvSub> pursComnInvSubList) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			String pursInvNum = pursComnInv.getPursInvNum();
			if (pursInvNum == null || pursInvNum.equals("")) {
				isSuccess = false;
				message = "采购发票号不能为空，修改失败!";
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
						message = "采购发票修改成功";
					} else {
						isSuccess = false;
						message = "采购发票修改失败";
					}
				} else {
					isSuccess = false;
					message = "供应商编码：" + pursComnInv.getProvrId() + "不存在，修改失败！";
				}
			}
			resp = BaseJson.returnRespObj("/account/PursComnInv/updatePursComnInv", isSuccess, message, pursComnInv);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
	}

	// 单个删除采购发票
	@Override
	public String deletePursComnInv(String pursInvNum) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";

		int a = pursComnInvDao.deletePursComnInvById(pursInvNum);
		int b = pursComnInvSubDao.deletePursComnInvSubByOrdrNum(pursInvNum);
		if (a >= 1 && b >= 1) {
			isSuccess = true;
			message = "处理成功";
		} else if (a == 0 && b == 0) {
			isSuccess = true;
			message = "没有要删除的数据";
		} else {
			isSuccess = false;
			message = "处理失败";
		}
		try {
			resp = BaseJson.returnRespObj("/account/PursComnInv/deletePursComnInv", isSuccess, message, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
	}

	// 查询采购发票详情
	@Override
	public String queryPursComnInvByPursInvNum(String pursInvNum) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		PursComnInv pursComnInv = pursComnInvDao.selectPursComnInvById(pursInvNum);
		if (pursComnInv != null) {
			isSuccess = true;
			message = "查询成功！";
		} else {
			isSuccess = false;
			message = "编码" + pursInvNum + "不存在！";
		}

		try {
			resp = BaseJson.returnRespObj("/account/PursComnInv/queryPursComnInvByPursInvNum", isSuccess, message,
					pursComnInv);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// 批量删除
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
					message += "单据号为：" + lists2.toString() + "的订单删除成功!\n";
				} else {
					isSuccess = false;
					message += "单据号为：" + lists2.toString() + "的订单删除失败！\n";
				}
			}
			if (lists3.size() > 0) {
				isSuccess = false;
				message += "单据号为：" + lists3.toString() + "的订单已被审核，无法删除！\n";
			}
			resp = BaseJson.returnRespObj("/account/PursComnInv/deletePursComnInvList", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	/**
	 * id放入list
	 * 
	 * @param id id(多个已逗号分隔)
	 * @return List集合
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

	// 分页查询采购普通发票
	@Override
	public String queryPursComnInvList(Map map) {
		String resp = "";
		//List<String> invtyClsEncdList = getList((String) map.get("invtyClsEncd"));// 存货分类编码
		List<String> provrIdList = getList((String) map.get("provrId"));// 供应商编码
		//map.put("invtyClsEncdList", invtyClsEncdList);
		map.put("provrIdList", provrIdList);

		List<PursComnInv> poList = pursComnInvDao.selectPursComnInvList(map);
		int count = pursComnInvDao.selectPursComnInvCount(map);

		int pageNo = Integer.parseInt(map.get("pageNo").toString());
		int pageSize = Integer.parseInt(map.get("pageSize").toString());
		int listNum = poList.size();
		int pages = count / pageSize + 1;

		try {
			resp = BaseJson.returnRespList("/account/PursComnInv/queryPursComnInvList", true, "查询成功！", count, pageNo,
					pageSize, listNum, pages, poList);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// 审核采购发票
	@Override
	public Map<String, Object> updatePursComnInvIsNtChkList(PursComnInv pursComnInv) {
		StringBuilder message = new StringBuilder();
		Boolean isSuccess = true;
		Map<String, Object> map = new HashMap<>();
		try {
			if (pursComnInv.getIsNtChk() == 1) {
				System.out.println("采购发票审核 999999999999");
				message.append(updatePursComnInvIsNtChkOK(pursComnInv).get("message"));
			} else if (pursComnInv.getIsNtChk() == 0) {
				System.out.println("采购发票	弃审 999999999999");
				message.append(updatePursComnInvIsNtChkNO(pursComnInv).get("message"));
			} else {
				isSuccess = false;
				message.append("审核状态错误，无法审核！\n");
			}
			map.put("isSuccess", isSuccess);
			map.put("message", message.toString());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return map;
	}

	// 采购发票审核
	public Map<String, Object> updatePursComnInvIsNtChkOK(PursComnInv pursComnInv) {
		String message = "";
		Boolean isSuccess = true;
		Map<String, Object> map = new HashMap<>();
		if (pursComnInvDao.selectPursComnInvIsNtChk(pursComnInv.getPursInvNum()) == 0) {
			PursComnInv pursComnInvs = pursComnInvDao.selectPursComnInvById(pursComnInv.getPursInvNum());// 采购发票主表
			List<PursComnInvSub> pursComnInvSubList = pursComnInvs.getPursList();// 采购发票子表
			for (PursComnInvSub pursComInvSub : pursComnInvSubList) {
				map.put("unBllgQty", pursComInvSub.getQty().abs());// 未开票数量
				map.put("unBllgAmt", pursComInvSub.getPrcTaxSum().abs());// 未开票金额
				map.put("ordrNum", pursComInvSub.getIntoWhsSnglSubtabId());// 采购入库单子表序号
				if (pursComInvSub.getIntoWhsSnglSubtabId() != null && pursComInvSub.getIntoWhsSnglSubtabId() != 0) {
					BigDecimal unBllgQty = intoWhsSubDao.selectUnBllgQtyByOrdrNum(map);// 根据采购入库单序号查询未开票数量

					if (unBllgQty != null) {
						if (unBllgQty.compareTo(pursComInvSub.getQty().abs()) == 1
								|| unBllgQty.compareTo(pursComInvSub.getQty().abs()) == 0) {
							intoWhsSubDao.updateIntoWhsSubByInvWhsBat(map);// 根据采购入库单子表序号修改未开票数量和金额
						} else {
							isSuccess = false;
							message += "单据号为：" + pursComnInv.getPursInvNum() + "的采购发票中存货【"
									+ pursComInvSub.getInvtyEncd() + "】累计开票数量大于入库数量，无法审核！\n";
							throw new RuntimeException(message);
						}
					} // else {
//						isSuccess = false;
//						message += "单据号为：" + pursComnInv.getPursInvNum() + "的采购发票对应的采购入库单中未开票数量不存在，无法审核！\n";
//						throw new RuntimeException(message);
//					}
				}
			}
			// 查询采购入库单号
			String intoWhsSnglId = pursComnInvDao.selectIntoWhsSnglIdByPursComnInv(pursComnInv.getPursInvNum());
			if (intoWhsSnglId != null && !intoWhsSnglId.equals("")) {
				List<String> intoWhsIdList = getintoWhsList(intoWhsSnglId);// 获取多个入库单编码
				for (String intoWhsId : intoWhsIdList) {
					List<IntoWhsSub> intoWhsSubList = intoWhsSubDao.selectIntoWhsSubByIntoWhsSnglId(intoWhsSnglId);// 根据采购入库单单据号查询采购入库单子表明细
					if (intoWhsSubList.size() > 0) {
						int num = 0;
						for (IntoWhsSub intoWhsSub : intoWhsSubList) {
							if (intoWhsSub.getUnBllgQty().intValue() > 0) {
								num++;
							}
						}
						if (num == 0) {
							// 修改是否开票状态
							intoWhsDao.updateIntoWhsIsNtBllgOK(intoWhsId);
						}
					}

				}
			}

			// 修改审核状态
			int a = pursComnInvDao.updatePursComnInvIsNtChk(pursComnInv);
			if (a >= 1) {
				isSuccess = true;
				message += "单据号为" + pursComnInv.getPursInvNum() + "的采购普通发票审核成功！\n";
			} else {
				isSuccess = false;
				message += "单据号为" + pursComnInv.getPursInvNum() + "的采购普通发票审核失败！\n";
				throw new RuntimeException(message);
			}
		} else if (pursComnInvDao.selectPursComnInvIsNtChk(pursComnInv.getPursInvNum()) == 1) {
			isSuccess = false;
			message += "单据号为" + pursComnInv.getPursInvNum() + "的采购普通发票已审核，不需要重复审核\n";
			throw new RuntimeException(message);
		}
		map.put("isSuccess", isSuccess);
		map.put("message", message);
		return map;
	}

	// 采购发票弃审
	public Map<String, Object> updatePursComnInvIsNtChkNO(PursComnInv pursComnInv) {
		String message = "";
		Boolean isSuccess = true;
		Map<String, Object> map = new HashMap<>();
		// 判断是否记账
		if (pursComnInvDao.selectPursComnInvIsNtBookEntry(pursComnInv.getPursInvNum()) == 0) {
			if (pursComnInvDao.selectPursComnInvIsNtChk(pursComnInv.getPursInvNum()) == 1) {
				PursComnInv pursComnInvs = pursComnInvDao.selectPursComnInvById(pursComnInv.getPursInvNum());// 采购发票主表
				List<PursComnInvSub> pursComnInvSubList = pursComnInvs.getPursList();// 采购发票子表
				for (PursComnInvSub pursComInvSub : pursComnInvSubList) {
					map.put("ordrNum", pursComInvSub.getIntoWhsSnglSubtabId());// 采购入库单子表序号
					if (pursComInvSub.getIntoWhsSnglSubtabId() != null && pursComInvSub.getIntoWhsSnglSubtabId() != 0) {
						BigDecimal unBllgQty = intoWhsSubDao.selectUnBllgQtyByOrdrNum(map);// 根据采购入库单序号查询未开票数量
						if (unBllgQty != null) {
							map.put("unBllgQty", pursComInvSub.getQty().abs().multiply(new BigDecimal(-1)));// 未开票数量
							map.put("unBllgAmt", pursComInvSub.getPrcTaxSum().abs().multiply(new BigDecimal(-1)));// 未开票金额
							intoWhsSubDao.updateIntoWhsSubByInvWhsBat(map);
						} // else {
//							isSuccess = false;
//							message += "单据号为：" + pursComnInv.getPursInvNum() + "的采购发票对应的采购入库单中未开票数量不存在，无法审核！\n";
//							throw new RuntimeException(message);
//						}	
					}
				}
				// 查询采购入库单号
				String intoWhsSnglId = pursComnInvDao.selectIntoWhsSnglIdByPursComnInv(pursComnInv.getPursInvNum());
				if (intoWhsSnglId != null && intoWhsSnglId != "") {
					List<String> intoWhsIdList = getintoWhsList(intoWhsSnglId);// 获取入库单编码
					for (String intoWhsId : intoWhsIdList) {
						List<IntoWhsSub> intoWhsSubList = intoWhsSubDao.selectIntoWhsSubByIntoWhsSnglId(intoWhsSnglId);// 根据采购入库单单据号查询采购入库单子表明细
						int num = 0;
						for (IntoWhsSub intoWhsSub : intoWhsSubList) {
							if (intoWhsSub.getUnBllgQty().intValue() > 0) {
								num++;
							}
						}
						if (num > 0) {
							// 修改是否开票状态
							intoWhsDao.updateIntoWhsIsNtBllgNO(intoWhsId);
						}
					}
				}
				int a = pursComnInvDao.updatePursComnInvIsNtChk(pursComnInv);
				if (a >= 1) {
					isSuccess = true;
					message += "单据号为" + pursComnInv.getPursInvNum() + "的采购普通发票弃审成功！\n";
				} else {
					isSuccess = false;
					message += "单据号为" + pursComnInv.getPursInvNum() + "的采购普通发票弃审失败！\n";
					throw new RuntimeException(message);
				}

			} else {
				isSuccess = false;
				message += "单据号为" + pursComnInv.getPursInvNum() + "的采购普通发票未审核，请先审核该单据\n";
				throw new RuntimeException(message);
			}
		} else {
			isSuccess = false;
			message += "单据号为" + pursComnInv.getPursInvNum() + "的采购普通发票已记账无法弃审\n";
			throw new RuntimeException(message);
		}
		map.put("isSuccess", isSuccess);
		map.put("message", message);
		return map;
	}

	// 根据采购发票审核的时候修改对应的入库单的金额
	private void updateIntoWhsByPursComnInv(String intoWhsId) {
		IntoWhs intoWhs = intoWhsDao.selectIntoWhsByIntoWhsSnglId(intoWhsId);
		List<IntoWhsSub> intoWhsSub = intoWhs.getIntoWhsSub();

	}

	/**
	 * id放入list
	 * 
	 * @param id id(多个已逗号分隔)
	 * @return List集合
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

	// 合并采购入库单并入采购发票中
	@Override
	public String selectPursComnInvBingList(List<IntoWhs> intoWhsList) {
		String resp = "";
		String message = "";
		Boolean isSuccess = true;
		// 实例化一个新的对象 采购普通发票主表
		PursComnInv purCoInv = new PursComnInv();
		List<PursComnInvSub> pursComnInvSubList = new ArrayList<>();
		Map<String, Object> map = new HashMap<>();
		// 获取List集合中第一个对应，并放入map中
		IntoWhs intoWhs = intoWhsList.get(0);
		// 将供应商编码，部门编码，业务员编码拼接起来
		String str1 = intoWhs.getProvrId() + intoWhs.getDeptId() + intoWhs.getAccNum();
		map.put("uniqInd", str1);
		String intoWhsId = "";
		for (IntoWhs intoWh : intoWhsList) {
			intoWhsId += intoWh.getIntoWhsSnglId() + ",";
			String str2 = intoWh.getProvrId() + intoWh.getDeptId() + intoWh.getAccNum();
			if (str2.equals(map.get("uniqInd").toString()) == false) {
				isSuccess = false;
				message = "必须选择相同供应商、部门、业务员的单据进行生单，请重新选择！";
			} else if (str2.equals(map.get("uniqInd").toString()) == true) {
				try {
					BeanUtils.copyProperties(purCoInv, intoWh);// 将采购入库主表单拷贝给采购普通发票主表
					purCoInv.setIntoWhsSnglId(intoWhsId.substring(0, intoWhsId.length() - 1));
					// 根据主表主键查询相关的子表信息
					List<IntoWhsSub> intoWhsSubList = intoWhsSubDao
							.selectIntoWhsSubByIntoWhsSnglId(intoWh.getIntoWhsSnglId());
					for (IntoWhsSub intoWhsSub : intoWhsSubList) {
						PursComnInvSub pursComnInvSub = new PursComnInvSub();
						BeanUtils.copyProperties(pursComnInvSub, intoWhsSub);// 将采购入库单子表拷贝给采购普通发票子表
						pursComnInvSubList.add(pursComnInvSub);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					throw new RuntimeException("查询数据有误，请检查再进行生单！");
				}
				isSuccess = true;
				message = "生单成功！";
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

	// 导入
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
			message = "导入出异常啦！";
			throw new RuntimeException(message);
		}

		// 将Map转为List，然后批量插入父表数据
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
			// 批量设置字表和父表的关联字段
			List<PursComnInvSub> tempList = entry.getPursList();
			tempList.forEach(s -> s.setPursInvNum(entry.getPursInvNum()));
			pursComnInvSubList.addAll(tempList);
			// 批量插入，每大于等于1000条插入一次
			if (pursComnInvSubList.size() >= 1000 || pursComnInvMap.size() == flag) {
				pursComnInvSubDao.insertPursComnInvSubUpload(pursComnInvSubList);
				pursComnInvSubList.clear();
			}
		}

		isSuccess = true;
		message = "采购发票导入成功！";
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

	// 导入excle
	private Map<String, PursComnInv> uploadScoreInfo(MultipartFile file) {
		Map<String, PursComnInv> temp = new HashMap<>();
		int j = 0;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			InputStream fileIn = file.getInputStream();
			// 根据指定的文件输入流导入Excel从而产生Workbook对象
			Workbook wb0 = new HSSFWorkbook(fileIn);
			// 获取Excel文档中的第一个表单
			Sheet sht0 = wb0.getSheetAt(0);
			// 获得当前sheet的开始行
			int firstRowNum = sht0.getFirstRowNum();
			// 获取文件的最后一行
			int lastRowNum = sht0.getLastRowNum();
			// 设置中文字段和下标映射
			SetColIndex(sht0.getRow(firstRowNum));
			// 设置列名称
			getCellNames();
			// 对Sheet中的每一行进行迭代
			for (int i = firstRowNum + 1; i <= lastRowNum; i++) {
				j++;
				Row r = sht0.getRow(i);
				// 如果当前行的行号（从0开始）未达到2（第三行）则从新循环
				if (r.getRowNum() < 1) {
					continue;
				}
				String orderNo = GetCellData(r, "采购发票号");
				// 创建实体类
				PursComnInv pursComnInv = new PursComnInv();
				if (temp.containsKey(orderNo)) {
					pursComnInv = temp.get(orderNo);
				}
				// 取出当前行第1个单元格数据，并封装在pursOrdr实体的各个属性上
				pursComnInv.setPursInvNum(orderNo);// 采购发票编码
				if (GetCellData(r, "开票日期") == null || GetCellData(r, "开票日期").equals("")) {
					pursComnInv.setBllgDt(null);
				} else {
					pursComnInv.setBllgDt(GetCellData(r, "开票日期").replaceAll("[^0-9:-]", " "));// 开票日期
				}
				pursComnInv.setInvTyp(GetCellData(r, "发票类型")); // '发票类型'
				pursComnInv.setProvrNm(GetCellData(r, "供应商名称"));// 供应商名称
				pursComnInv.setAccNum(GetCellData(r, "业务员编码")); // 业务员编码'
				pursComnInv.setUserName(GetCellData(r, "业务员名称")); // '业务员名称'
				pursComnInv.setPursTypId(GetCellData(r, "采购类型编码"));// "采购类型编码"
				pursComnInv.setProvrId(GetCellData(r, "供应商编码")); // '供应商编码'
				pursComnInv.setInvTyp(GetCellData(r, "发票类型")); // '发票类型'
				pursComnInv.setFormTypEncd(GetCellData(r, "单据类型编码"));// 单据类型编码
//				pursComnInv.setToFormTypEncd(GetCellData(r, "来源单据类型编码"));//来源单据类型编码
				pursComnInv.setToFormTypEncd(
						GetBigDecimal(GetCellData(r, "数量"), 8).compareTo(BigDecimal.ZERO) == -1 ? "006" : "002");// 单据类型
				pursComnInv.setDeptId(GetCellData(r, "部门编码")); // '部门编码'

				pursComnInv.setIsNtChk(new Double(GetCellData(r, "是否审核")).intValue()); // '是否审核', int(11
				pursComnInv.setChkr(GetCellData(r, "审核人")); // '审核人', varchar(200
				if (GetCellData(r, "审核时间") == null || GetCellData(r, "审核时间").equals("")) {
					pursComnInv.setChkTm(null);
				} else {
					pursComnInv.setChkTm(GetCellData(r, "审核时间").replaceAll("[^0-9:-]", " "));// 审核时间
				}
				pursComnInv.setIsNtMakeVouch(new Double(GetCellData(r, "是否生成凭证")).intValue()); // '是否审核', int(11
				pursComnInv.setMakDocPers(GetCellData(r, "制凭证人")); // '审核人', varchar(200
				if (GetCellData(r, "制凭证时间") == null || GetCellData(r, "制凭证时间").equals("")) {
					pursComnInv.setMakDocTm(null);
				} else {
					pursComnInv.setMakDocTm(GetCellData(r, "制凭证时间").replaceAll("[^0-9:-]", " "));// 审核时间
				}

				pursComnInv.setIsNtBookEntry(new Double(GetCellData(r, "是否记账")).intValue()); // 是否记账
				pursComnInv.setBookEntryPers(GetCellData(r, "记账人")); // 记账人',
				if (GetCellData(r, "记账时间") == null || GetCellData(r, "记账时间").equals("")) {
					pursComnInv.setBookEntryTm(null);
				} else {
					pursComnInv.setBookEntryTm(GetCellData(r, "记账时间").replaceAll("[^0-9:-]", " "));// 记账时间
				}

				pursComnInv.setSetupPers(GetCellData(r, "创建人")); // '创建人', varchar(200
				if (GetCellData(r, "创建时间") == null || GetCellData(r, "创建时间").equals("")) {
					pursComnInv.setSetupTm(null);
				} else {
					pursComnInv.setSetupTm(GetCellData(r, "创建时间").replaceAll("[^0-9:-]", " "));// 记账时间
				}
				pursComnInv.setMdfr(GetCellData(r, "修改人")); // '修改人', varchar(200
				if (GetCellData(r, "修改时间") == null || GetCellData(r, "修改时间").equals("")) {
					pursComnInv.setModiTm(null);
				} else {
					pursComnInv.setModiTm(GetCellData(r, "修改时间").replaceAll("[^0-9:-]", " "));// 记账时间
				}
//				pursComnInv.setPursOrdrId(GetCellData(r, "采购订单编码"));
				pursComnInv.setIntoWhsSnglId(GetCellData(r, "采购入库单编码"));
				pursComnInv.setMemo(GetCellData(r, "表头备注")); // '备注', varchar(2000
				List<PursComnInvSub> pursComnInvSubList = pursComnInv.getPursList();// 采购发票子表
				if (pursComnInvSubList == null) {
					pursComnInvSubList = new ArrayList<>();
				}
				PursComnInvSub pursComnInvSub = new PursComnInvSub();

//				pursComnInvSub.setWhsEncd(GetCellData(r, "仓库编码")); // '仓库编码',
				pursComnInvSub.setInvtyEncd(GetCellData(r, "存货编码")); // '存货编码',
				pursComnInvSub.setQty(GetBigDecimal(GetCellData(r, "数量"), 8)); // '数量',
				pursComnInvSub.setBxQty(GetBigDecimal(GetCellData(r, "箱数"), 8)); // '箱数',
				pursComnInvSub.setBxRule(GetBigDecimal(GetCellData(r, "箱规"), 8));// 箱规
				pursComnInvSub.setCntnTaxUprc(GetBigDecimal(GetCellData(r, "含税单价"), 8)); // '含税单价',
				pursComnInvSub.setPrcTaxSum(GetBigDecimal(GetCellData(r, "价税合计"), 8)); // '价税合计',
				pursComnInvSub.setNoTaxUprc(GetBigDecimal(GetCellData(r, "无税单价"), 8)); // '无税单价',
				pursComnInvSub.setNoTaxAmt(GetBigDecimal(GetCellData(r, "无税金额"), 8)); // '无税金额',
				pursComnInvSub.setTaxAmt(GetBigDecimal(GetCellData(r, "税额"), 8)); // '税额',
				pursComnInvSub.setTaxRate(GetBigDecimal(GetCellData(r, "税率"), 8)); // '税率',
				pursComnInvSub.setIntlBat(GetCellData(r, "国际批次")); // 国际批次
				pursComnInvSub.setBatNum(GetCellData(r, "批次")); // '批次',
				pursComnInvSub.setMemo(GetCellData(r, "表体备注")); // '备注',
				pursComnInvSub.setCrspdIntoWhsSnglNum(GetCellData(r, "采购入库单编码"));// 子表中的采购入库单编码

//				pursComnInvSub.setIntoWhsSnglSubtabId(Long.parseLong(GetCellData(r,"来源单据子表序号")));//来源单据子表序号

				if (GetCellData(r, "来源单据子表序号") == null || GetCellData(r, "来源单据子表序号") == "") {
					pursComnInvSub.setIntoWhsSnglSubtabId(null);
				} else {
					pursComnInvSub.setIntoWhsSnglSubtabId(Long.parseLong(GetCellData(r, "来源单据子表序号")));
				}

				pursComnInvSubList.add(pursComnInvSub);

				pursComnInv.setPursList(pursComnInvSubList);
				temp.put(orderNo, pursComnInv);
			}
			fileIn.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("文件的第" + j + "行导入格式有误，无法导入!" + e.getMessage());
		}
		return temp;
	}

	// 导入excle
	private Map<String, PursComnInv> uploadScoreInfoU8(MultipartFile file) {
		Map<String, PursComnInv> temp = new HashMap<>();
		int j = 0;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			InputStream fileIn = file.getInputStream();
			// 根据指定的文件输入流导入Excel从而产生Workbook对象
			Workbook wb0 = new HSSFWorkbook(fileIn);
			// 获取Excel文档中的第一个表单
			Sheet sht0 = wb0.getSheetAt(0);
			// 获得当前sheet的开始行
			int firstRowNum = sht0.getFirstRowNum();
			// 获取文件的最后一行
			int lastRowNum = sht0.getLastRowNum();
			// 设置中文字段和下标映射
			SetColIndex(sht0.getRow(firstRowNum));
			// 设置列名称
			getCellNames();
			// 对Sheet中的每一行进行迭代
			for (int i = firstRowNum + 1; i <= lastRowNum; i++) {
				j++;
				Row r = sht0.getRow(i);
				// 如果当前行的行号（从0开始）未达到2（第三行）则从新循环
				if (r.getRowNum() < 1) {
					continue;
				}
				String orderNo = GetCellData(r, "发票号");
				// 创建实体类
				PursComnInv pursComnInv = new PursComnInv();
				if (temp.containsKey(orderNo)) {
					pursComnInv = temp.get(orderNo);
				}
				// 取出当前行第1个单元格数据，并封装在pursOrdr实体的各个属性上
				pursComnInv.setPursInvNum(orderNo);// 采购发票编码
				if (GetCellData(r, "开票日期") == null || GetCellData(r, "开票日期").equals("")) {
					pursComnInv.setBllgDt(null);
				} else {
					pursComnInv.setBllgDt(GetCellData(r, "开票日期").replaceAll("[^0-9:-]", " "));// 开票日期
				}
				pursComnInv.setAccNum(GetCellData(r, "业务员编码")); // 业务员编码'
				pursComnInv.setUserName(GetCellData(r, "业务员")); // '业务员名称'
				pursComnInv.setPursTypId("1");// "采购类型编码"
				pursComnInv.setProvrId(GetCellData(r, "供应商编码")); // '供应商编码'
				pursComnInv.setInvTyp(GetCellData(r, "发票类型")); // '发票类型'
				if (GetCellData(r, "发票类型") != null && GetCellData(r, "发票类型").equals("专用发票")) {
					pursComnInv.setFormTypEncd("019");// 单据类型编码

				} else if (GetCellData(r, "发票类型") != null && GetCellData(r, "发票类型").equals("普通发票")) {
					pursComnInv.setFormTypEncd("020");// 单据类型编码
				}
				pursComnInv.setToFormTypEncd(GetCellData(r, "来源单据类型编码"));// 来源单据类型编码

				pursComnInv.setDeptId(GetCellData(r, "部门编码")); // '部门编码'

				pursComnInv.setIsNtChk(0); // '是否审核', int(11
				pursComnInv.setIsNtMakeVouch(0); // '是否生成凭证',
				pursComnInv.setIsNtBookEntry(0); // 是否记账

				pursComnInv.setSetupPers(GetCellData(r, "制单人")); // '创建人', varchar(200
				if (GetCellData(r, "制单时间") == null || GetCellData(r, "制单时间").equals("")) {
					pursComnInv.setSetupTm(null);
				} else {
					pursComnInv.setSetupTm(GetCellData(r, "制单时间").replaceAll("[^0-9:-]", " "));// 记账时间
				}
				pursComnInv.setMdfr(GetCellData(r, "修改人")); // '修改人', varchar(200
				if (GetCellData(r, "修改时间") == null || GetCellData(r, "修改时间").equals("")) {
					pursComnInv.setModiTm(null);
				} else {
					pursComnInv.setModiTm(GetCellData(r, "修改时间").replaceAll("[^0-9:-]", " "));// 记账时间
				}
				pursComnInv.setIntoWhsSnglId(GetCellData(r, "入库单号"));
				pursComnInv.setMemo(GetCellData(r, "备注")); // '备注', varchar(2000
				List<PursComnInvSub> pursComnInvSubList = pursComnInv.getPursList();// 采购发票子表
				if (pursComnInvSubList == null) {
					pursComnInvSubList = new ArrayList<>();
				}
				PursComnInvSub pursComnInvSub = new PursComnInvSub();
				pursComnInvSub.setInvtyEncd(GetCellData(r, "存货编码")); // '存货编码',
				pursComnInvSub.setQty(GetBigDecimal(GetCellData(r, "数量"), 8)); // '数量',
				pursComnInvSub.setBxQty(GetBigDecimal(GetCellData(r, "箱数"), 8)); // '箱数',
				pursComnInvSub.setBxRule(GetBigDecimal(GetCellData(r, "箱规"), 8));// 箱规
				pursComnInvSub.setCntnTaxUprc(GetBigDecimal(GetCellData(r, "原币含税单价"), 8)); // '含税单价',
				pursComnInvSub.setPrcTaxSum(GetBigDecimal(GetCellData(r, "原币价税合计"), 8)); // '价税合计',
				pursComnInvSub.setNoTaxUprc(GetBigDecimal(GetCellData(r, "原币无税单价"), 8)); // '无税单价',
				pursComnInvSub.setNoTaxAmt(GetBigDecimal(GetCellData(r, "原币金额"), 8)); // '无税金额',
				pursComnInvSub.setTaxAmt(GetBigDecimal(GetCellData(r, "原币税额"), 8)); // '税额',
				pursComnInvSub.setTaxRate(GetBigDecimal(GetCellData(r, "税率"), 8)); // '税率',
				pursComnInvSub.setIntlBat(GetCellData(r, "国际批号")); // 国际批次
				pursComnInvSub.setBatNum(GetCellData(r, "批号")); // '批次',
				pursComnInvSub.setCrspdIntoWhsSnglNum(GetCellData(r, "入库单号"));// 子表中的采购入库单编码
				if (GetCellData(r, "来源单据子表序号") == null || GetCellData(r, "来源单据子表序号") == "") {
					pursComnInvSub.setIntoWhsSnglSubtabId(null);
				} else {
					pursComnInvSub.setIntoWhsSnglSubtabId(Long.parseLong(GetCellData(r, "来源单据子表序号")));
				}

				pursComnInvSubList.add(pursComnInvSub);

				pursComnInv.setPursList(pursComnInvSubList);
				temp.put(orderNo, pursComnInv);
			}
			fileIn.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("文件的第" + j + "行导入格式有误，无法导入!" + e.getMessage());
		}
		return temp;
	}

	// 原来的导出接口,不敢删
	@Override
	public String upLoadPursComnInvList(Map map) {
		String resp = "";
		List<String> invtyClsEncdList = getList((String) map.get("invtyClsEncd"));// 存货分类编码
		List<String> provrIdList = getList((String) map.get("provrId"));// 供应商编码
		map.put("invtyClsEncdList", invtyClsEncdList);
		map.put("provrIdList", provrIdList);
		List<PursComnInv> poList = pursComnInvDao.printingPursComnInvList(map);
		try {
			resp = BaseJson.returnRespObjList("purc/PursComnInv/printingPursComnInvList", true, "查询成功！", null, poList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
	}

	// 导出使用的接口
	@Override
	public String printPursComnInvList(Map map) {
		String resp = "";
		List<String> invtyClsEncdList = null;// 存货分类编码
		List<String> provrIdList = null;// 供应商编码
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
			resp = BaseJson.returnRespObjListAnno("purc/PursComnInv/printPursComnInvList", true, "查询成功！", null, poList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
	}

	// 子主表类
	public static class zizhu {
		@JsonProperty("采购发票号")
		public String pursInvNum;
		@JsonProperty("开票日期")
		public String bllgDt;
		@JsonProperty("发票类型")
		public String invTyp;
		@JsonProperty("供应商名称")
		public String provrNm;
		@JsonProperty("部门名称")
		public String deptName;
		@JsonProperty("对应入库单号")
		public String crspdIntoWhsSnglNum;
		@JsonProperty("表头备注")
		public String memo;
		@JsonProperty("是否审核")
		public Integer isNtChk;
		@JsonProperty("入库单号")
		public String intoWhsSnglId;
		@JsonProperty("序号")
		public Integer ordrNum;
		@JsonProperty("存货编码")
		public String invtyEncd;
		@JsonProperty("仓库编码")
		public String whsEncd;
		@JsonProperty("存货名称")
		public String invtyNm;
		@JsonProperty("项目编码")
		public String projEncd;
		@JsonProperty("规格型号")
		public String spcModel;
		@JsonProperty("主计量单位")
		public String measrCorpNm;
		@JsonProperty("数量")
		public BigDecimal qty;
		@JsonProperty("含税单价")
		public BigDecimal cntnTaxUprc;
		@JsonProperty("价税合计")
		public BigDecimal prcTaxSum;
		@JsonProperty("无税单价")
		public BigDecimal noTaxUprc;
		@JsonProperty("无税金额")
		public BigDecimal noTaxAmt;
		@JsonProperty("税率")
		public BigDecimal taxRate;
		@JsonProperty("税额")
		public BigDecimal taxAmt;
		@JsonProperty("箱规")
		public BigDecimal bxRule;
		@JsonProperty("箱数")
		public BigDecimal bxQty;
		@JsonProperty("对应条形码")
		public String crspdBarCd;
		@JsonProperty("批次")
		public String batNum;
		@JsonProperty("表体备注")
		public String memos;

		// 忽略

		@JsonIgnore
		@JsonProperty("表头税率")
		public String tabHeadTaxRate;

		@JsonProperty("审核人")
		@JsonIgnore
		public String chkr;

		@JsonProperty("审核时间")
		@JsonIgnore
		public String chkTm;

		@JsonProperty("供方联系人")
		@JsonIgnore
		public String provrContcr;

		@JsonProperty("供方银行名称")
		@JsonIgnore
		public String provrBankNm;

		@JsonProperty("创建人")
		@JsonIgnore
		public String setupPers;

		@JsonProperty("创建日期")
		@JsonIgnore
		public String setupTm;

		@JsonProperty("是否记账")
		@JsonIgnore
		public Integer isNtBookEntry;

		@JsonProperty("记账人")
		@JsonIgnore
		public String bookEntryPers;

		@JsonProperty("记账时间")
		@JsonIgnore
		public String bookEntryTm;

		@JsonProperty("修改人")
		@JsonIgnore
		public String mdfr;

		@JsonProperty("修改日期")
		@JsonIgnore
		public String modiTm;

		@JsonProperty("采购类型编码")
		@JsonIgnore
		public String pursTypId;

		@JsonProperty("供应商编码")
		@JsonIgnore
		public String provrId;

		@JsonProperty("部门编码")
		@JsonIgnore
		public String deptId;

		@JsonProperty("业务员编码")
		@JsonIgnore
		public String accNum;

		@JsonProperty("采购订单号 ")
		@JsonIgnore
		public String pursOrdrId;

		@JsonProperty("科目编码")
		@JsonIgnore
		public String subjId;

		@JsonProperty("制单人")
		@JsonIgnore
		public String makDocPers;

		@JsonProperty("制单时间")
		@JsonIgnore
		public String makDocTm;

		@JsonProperty("凭证号")
		@JsonIgnore
		public String vouchNum;

		@JsonProperty("采购类型名称")
		@JsonIgnore
		public String pursTypNm;

		@JsonProperty("用户名称")
		@JsonIgnore
		public String userName;

		@JsonProperty("单据类型编码")
		@JsonIgnore
		public String formTypEncd;

		@JsonProperty("来源单据类型编码")
		@JsonIgnore
		public String toFormTypEncd;

		@JsonProperty("是否生成凭证")
		@JsonIgnore
		public Integer isNtMakeVouch;

		@JsonProperty("制凭证人")
		@JsonIgnore
		public String makVouchPers;

		@JsonProperty("制凭证时间")
		@JsonIgnore
		public String makVouchTm;

		@JsonProperty("存货一级分类")
		@JsonIgnore
		public String invtyFstLvlCls;

		@JsonProperty("入库单子表id")
		@JsonIgnore
		public Long intoWhsSnglSubtabId;

		@JsonProperty("国际批次")
		@JsonIgnore
		public String intlBat;

		@JsonProperty("计量单位编码")
		@JsonIgnore
		public String measrCorpId;

		@JsonProperty("结算日期")
		@JsonIgnore
		public String stlDt;

		@JsonProperty("结算时间")
		@JsonIgnore
		public String stlTm;

		@JsonProperty("仓库")
		@JsonIgnore
		public String whsNm;

		@JsonProperty("存货代码")
		@JsonIgnore
		public String invtyCd;

		@JsonProperty("是否退货")
		@JsonIgnore
		public Integer isNtRtnGoods;

		@JsonProperty("保质期天数")
		@JsonIgnore
		public String baoZhiQiDt;

		@JsonProperty("赠品")
		@JsonIgnore
		public String gift;

		@JsonProperty("项目名称")
		@JsonIgnore
		public String projNm;

	}

	@Override
	public U8PursComnInv encapsulation(PursComnInv pursComnInv, List<PursComnInvSub> list) throws Exception {
		// 行对象-->单条发票
		U8PursComnInv dataRow = new U8PursComnInv();
		// 主表
		dataRow.setDscode(pursComnInv.getPursInvNum());
		dataRow.setVouttype(pursComnInv.getInvTyp().equals("采购专用发票") ? "01" : "02");// 发票类型 专票01 普票02
		dataRow.setCptcode(pursComnInv.getPursTypId().trim().length() < 1 ? "1" : pursComnInv.getPursTypId());// 采购类型编码

		dataRow.setDdate(pursComnInv.getBllgDt());// 单据日期
		dataRow.setCdepcode(pursComnInv.getDeptId());// 部门编码
		dataRow.setCvencode(pursComnInv.getProvrId());// 供应商
		dataRow.setItaxrate(pursComnInv.getTabHeadTaxRate() != null ? new BigDecimal(pursComnInv.getTabHeadTaxRate())
				: new BigDecimal(13));// 表头税率

		dataRow.setCpersoncode(pursComnInv.getAccNum());// 人员编码
		dataRow.setRemark(pursComnInv.getMemo());// 表头备注
		// 子表集合
		ArrayList<U8PursComnInvSub> detailsList = new ArrayList<U8PursComnInvSub>();
		// 循环添加子表
		list.forEach(item -> {
			U8PursComnInvSub details = new U8PursComnInvSub();
			details.setCinvcode(item.getInvtyEncd());// 存货编码item.getInvtyEncd()
			details.setQuantity(item.getQty());// 数量
			details.setIprice(item.getNoTaxUprc());// 不含税单价
			details.setItax(item.getTaxAmt());// 税额
			details.setCitemcode(item.getProjEncd());// 项目编码(可以为空)
			details.setItaxrate(item.getTaxRate());// 税率
			detailsList.add(details);
		});

		dataRow.setSubList(detailsList);
		return dataRow;
	}

	public String connectToU8(String methodName, String dataXmlStr, String ztCodeStr) {
		String resultStr = "";
		try {
			ServiceClient serviceClient = new ServiceClient();
			// 创建服务地址WebService的URL,注意不是WSDL的URL
			String url = "http://106.14.183.228:8081/YBService.asmx";
			EndpointReference targetEPR = new EndpointReference(url);
			Options options = serviceClient.getOptions();
			options.setTo(targetEPR);
			// 确定调用方法（wsdl 命名空间地址 (wsdl文档中的targetNamespace) 和 方法名称 的组合）
			options.setAction("http://tempuri.org/" + methodName);

			OMFactory fac = OMAbstractFactory.getOMFactory();
			/*
			 * 指定命名空间，参数： uri--即为wsdl文档的targetNamespace，命名空间 perfix--可不填
			 */
			OMNamespace omNs = fac.createOMNamespace("http://tempuri.org/", "");
			// 指定方法
			OMElement method = fac.createOMElement(methodName, omNs);
			// 指定方法的参数
			OMElement dataXml = fac.createOMElement("dataXml", omNs);
			OMElement ztCode = fac.createOMElement("ztCode", omNs);
			dataXml.setText(dataXmlStr);
			ztCode.setText(ztCodeStr);

			method.addChild(dataXml);
			method.addChild(ztCode);
			method.build();
			System.err.println("请求参数:::" + method.toString());
			// 远程调用web服务
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

		/********** 以下是接口对接************ */
		String methodName = "SetPuBill";
		String dataXmlStr = JacksonUtil.getXmlStr(table).replace(",", "");
		String ztCodeStr = "905";

		String resultStr = connectToU8(methodName, dataXmlStr, ztCodeStr);

		System.err.println("返回的XML结构::" + resultStr);

		/************* 接口结束 ***********/

		MappingIterator<U8PursComnInvResponse> iterator = JacksonUtil.getResponse("SetPuBillResult",
				U8PursComnInvResponse.class, resultStr);

		ArrayList<U8PursComnInvResponse> failedList = new ArrayList<U8PursComnInvResponse>();

		StringBuffer message = new StringBuffer();
		int count = 0;
		while (iterator.hasNext()) {
			U8PursComnInvResponse response = (U8PursComnInvResponse) iterator.next();

			if (response.getType() == 1 && response.getDscode() != null) {
				failedList.add(response);
				logger.info("单号: " + response.getDscode() + "的发票推送失败,原因为 :" + response.getInfor());

			} else if (response.getType() == 0) {
				pursComnInvDao.updatePushed(response.getDscode());
				count++;
			} else {
				failedList.add(response);
				logger.info("单号: " + "--未知单号--" + "的发票推送失败,原因为 :" + response.getInfor());
			}
		}
		message.append("共" + count + "张单据上传成功!" + '\n');
		if (failedList.size() > 0) {
			message.append("共" + failedList.size() + "张单据上传失败!" + "\n");
		}

		String resp = BaseJson.returnRespList("url://", true, message.toString(), null);

		return resp;
	}

}
