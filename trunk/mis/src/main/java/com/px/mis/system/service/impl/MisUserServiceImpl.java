package com.px.mis.system.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.purc.dao.DeptDocDao;
import com.px.mis.purc.entity.DeptDoc;
import com.px.mis.system.dao.MisUserDao;
import com.px.mis.system.dao.RoleDao;
import com.px.mis.system.dao.UserMenuDao;
import com.px.mis.system.dao.UserWhsDao;
import com.px.mis.system.entity.MisUser;
import com.px.mis.system.entity.Role;
import com.px.mis.system.entity.UserMenu;
import com.px.mis.system.service.MisUserService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;
import com.px.mis.util.poiTool;
import com.px.mis.whs.dao.WhsDocMapper;
import com.px.mis.whs.entity.RealWhs;
import com.px.mis.whs.entity.UserWhs;
import com.px.mis.whs.entity.WhsDoc;

@Transactional
@Service
public class MisUserServiceImpl extends poiTool implements MisUserService {

	private Logger logger = LoggerFactory.getLogger(MisUserServiceImpl.class);

	@Autowired
	private MisUserDao misUserDao;

	@Autowired
	private RoleDao roleDao;

	@Autowired
	private DeptDocDao deptDocDao;

	@Autowired
	private UserMenuDao userMenuDao;
	@Autowired
	private UserWhsDao userWhsDao;
	@Autowired
	private WhsDocMapper whsDocMapper;

	@Override
	public String login(String accNum, String password, String accSet) {
		String message = "";
		boolean isSuccess = true;
		String resp = "";
		try {
			MisUser misUser = misUserDao.select(accNum);
			if (misUser == null) {
				message = "��ǰ�˺Ų�����!";
				isSuccess = false;
			} else if (!misUser.getPassword().equals(password)) {
				misUser = null;
				message = "�����������������!";
				isSuccess = false;
			} else {
				Map<String, Object> map = new HashMap<>();
				map.put("accNum", accNum);
				String whsId = "";
				// ��ѯ�û��µ����вֿ�
				List<WhsDoc> userWhsList = userWhsDao.selectUserWhsByMap(map);
				if (userWhsList.size() > 0) {
					List<String> delList = userWhsList.stream().map(whsDoc -> {
						return whsDoc.getWhsEncd();
					}).collect(Collectors.toList());
					whsId = StringUtils.join(delList, ",");
				}
				misUser.setWhsId(whsId);
				message = "��¼�ɹ�!";
			}

			resp = BaseJson.returnRespObj("system/misUser/login", isSuccess, message, misUser);

		} catch (IOException e) {
			logger.error("URL:system/misUser/login �쳣˵����", e);
		}
		return resp;
	}

	@Override
	public ObjectNode insert(MisUser misUser) {
		ObjectNode objectNode = null;
		try {
			objectNode = JacksonUtil.getObjectNode("");

			if (misUserDao.select(misUser.getAccNum()) == null) {
				misUserDao.insert(misUser);
				objectNode.put("isSuccess", true);
				objectNode.put("message", "�����ɹ���");
			} else {
				objectNode.put("isSuccess", false);
				objectNode.put("message", "��ǰ�˺��Ѵ��ڣ�");
			}
		} catch (Exception e) {
			logger.error("URL:system/misUser/login �쳣˵����", e);
		}
		return objectNode;
	}

	@Override
	public ObjectNode delete(String accNum) {
		ObjectNode objectNode = null;
		if (accNum.contains(",")) {
			accNum = "\'" + accNum.replace(",", "\',\'") + "\'";
			try {
				objectNode = JacksonUtil.getObjectNode("");
				if (misUserDao.select2(accNum) == null) {
					objectNode.put("isSuccess", false);
					objectNode.put("message", "��ǰ�û�������!");
				} else {
					misUserDao.delete2(accNum);
					objectNode.put("isSuccess", true);
					objectNode.put("message", "ɾ���ɹ���");
				}
			} catch (Exception e) {
				logger.error("URL:system/misUser/delete �쳣˵����", e);
			}
		} else {
			try {
				objectNode = JacksonUtil.getObjectNode("");
				if (misUserDao.select(accNum) == null) {
					objectNode.put("isSuccess", false);
					objectNode.put("message", "��ǰ�û�������!");
				} else {
					misUserDao.delete(accNum);
					objectNode.put("isSuccess", true);
					objectNode.put("message", "ɾ���ɹ���");
				}
			} catch (Exception e) {
				logger.error("URL:system/misUser/delete �쳣˵����", e);
			}
		}
		return objectNode;
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
			System.out.println(str[i] + "=====impl �� str[i]");
			list.add(str[i]);
		}
		return list;
	}

	@Override
	public String edit(String jsonReq) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		MisUser misUser = new MisUser();
		try {
			misUser = BaseJson.getPOJO(jsonReq, MisUser.class);
			Role role = roleDao.select(misUser.getRoleId());
			if (role == null) {
				message = "��ɫ��Ų����ڣ�";
				isSuccess = false;
			} else {
				MisUser user = misUserDao.select(misUser.getAccNum());
//				misUser.setPassword(user.getPassword());
				misUser.setRoleId(role.getName());
			}

			if (misUserDao.select(misUser.getAccNum()) == null) {
				message = "���" + misUser.getAccNum() + "�����ڣ����������룡";
				isSuccess = false;
			} else {
				misUserDao.update(misUser);
				message = "�޸ĳɹ���";
				isSuccess = true;
			}

			resp = BaseJson.returnRespObj("system/misUser/edit", isSuccess, message, null);
		} catch (IOException e) {
			logger.error("URL:system/misUser/edit �쳣˵����", e);
			throw new RuntimeException();
		}
		return resp;
	}
	//�û�������ѯ�ӿ�
	@Override
	public String queryList(Map map) {
		String message = "��ѯ�ɹ���";
		Boolean isSuccess = true;
		String resp = "";
		try {
			int pageNo = Integer.parseInt(map.get("pageNo").toString());
			int pageSize = Integer.parseInt(map.get("pageSize").toString());
			map.put("index", (pageNo - 1) * pageSize);
			map.put("num", pageSize);
			List<MisUser> mList = misUserDao.selectList(map);
			int count = misUserDao.selectCount(map);

			resp = BaseJson.returnRespList("system/misUser/queryList", isSuccess, message, count, pageNo, pageSize, 0,
					0, mList);
		} catch (Exception e) {
			logger.error("URL:system/misUser/queryList �쳣˵����", e);
		}
		return resp;
	}
	//�û����������ӿ�
		@Override
		public String queryListExport(Map map) {
			String message = "��ѯ�ɹ���";
			Boolean isSuccess = true;
			String resp = "";
			try {
				List<MisUser> mList = misUserDao.selectList(map);
				int count = misUserDao.selectCount(map);
				resp = BaseJson.returnRespList("system/misUser/queryListExport", isSuccess, message, mList);
			} catch (Exception e) {
				logger.error("URL:system/misUser/queryListExport �쳣˵����", e);
			}
			return resp;
		}

	@Override
	public String query(String accNum) {
		String message = "��ѯ�ɹ���";
		Boolean isSuccess = true;
		String resp = "";
		try {
			MisUser misUser = misUserDao.select(accNum);
			if (misUser == null) {
				isSuccess = false;
				message = "���û������ڣ�";
			}
			resp = BaseJson.returnRespObj("system/misUser/query", isSuccess, message, misUser);
		} catch (Exception e) {
			logger.error("URL:system/misUser/query �쳣˵����", e);
			e.printStackTrace();
		}
		return resp;
	}

	@Override
	public String userTree() {
		String resp = "";
//		List<MisUser> userList = misUserDao.selectAll(null);
		List<Role> roleList = roleDao.selectAll();
		List<DeptDoc> deptDocList = deptDocDao.printingDeptDocList(null);
		try {
			resp = BaseJson.returnResp("system/misUser/userTree", true, "��ѯ�ɹ���",
					getUserTree(null, roleList, deptDocList));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	private ObjectNode getUserTree(List<MisUser> userList, List<Role> roleList, List<DeptDoc> deptDocList) {
		ObjectMapper mapper = new ObjectMapper();

		ArrayNode firstNode = mapper.createArrayNode();
		for (DeptDoc deptDoc : deptDocList) {
			ObjectNode deptNode = mapper.createObjectNode();
			deptNode.put("id", deptDoc.getDeptId());
			deptNode.put("name", deptDoc.getDeptName());
			deptNode.put("pid", "");
			ArrayNode secondNode = mapper.createArrayNode();
			for (Role role : roleList) {
				if (deptDoc.getDeptId().equals(role.getDepId())) {
					ObjectNode roleNode = mapper.createObjectNode();
					roleNode.put("id", role.getId());
					roleNode.put("name", role.getName());
					roleNode.put("pid", deptDoc.getDeptId());
//					ArrayNode thirdNode = mapper.createArrayNode();
//					for (MisUser user : userList) {
//						if (role.getId().equals(user.getRoleId())) {
//							ObjectNode userNode = mapper.createObjectNode();
//							userNode.put("id", user.getAccNum());
//							userNode.put("name", user.getUserName());
//							userNode.put("pid", role.getId());
//							thirdNode.add(userNode);
//						}
//					}
//					roleNode.put("children", thirdNode);
					secondNode.add(roleNode);
				}
			}
			deptNode.put("children", secondNode);
			firstNode.add(deptNode);
		}

		ObjectNode objectNode = mapper.createObjectNode();
		objectNode.put("tree", firstNode);
		return objectNode;
	}

	@Override
	public String permAss(String jsonBody) {
		String resp = "";
		try {
			String userId = BaseJson.getReqBody(jsonBody).get("userId").asText();
			String menuIdStr = BaseJson.getReqBody(jsonBody).get("menuId").asText();
			String[] menuIds = menuIdStr.split(",");
			List<String> menuIdList = Arrays.asList(menuIds);

			List<UserMenu> userMenuList = new ArrayList<>();
			for (String menuId : menuIdList) {

				UserMenu userMenu = new UserMenu();
				userMenu.setUserId(userId);
				userMenu.setMenuId(menuId);
				userMenuList.add(userMenu);
			}

			userMenuDao.delete(userId);
			if (userMenuList.size() > 0) {
				userMenuDao.insert(userMenuList);
			}

			resp = BaseJson.returnRespObj("system/user/permAss", true, "�˵�Ȩ�޷���ɹ���", null);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		return resp;
	}

	@Override
	public ObjectNode delMisUser(String jsonBody) {
		ObjectNode objectNode = null;
		try {
			objectNode = JacksonUtil.getObjectNode("");
			String list = BaseJson.getReqBody(jsonBody).get("list").asText();
			System.err.println(list);
//			List<String> accNumList = new ObjectMapper().readValue(BaseJson.getReqBody(jsonBody).get("accNum").toString(), ArrayList.class);
			List<String> accNumList = getList(list);
			if (accNumList != null && accNumList.size() > 0) {
				misUserDao.delMisUser(accNumList);
				objectNode.put("isSuccess", true);
				objectNode.put("message", "ɾ���ɹ���");
			} else {
				objectNode.put("isSuccess", false);
				objectNode.put("message", "ɾ��ʧ�ܣ�");
			}
		} catch (Exception e) {
			logger.error("URL:system/misUser/delMisUser �쳣˵����", e);
		}
		return objectNode;
	}

	@Override
	public String updateBatch(String jsonBody) {
		String message = "";
		Boolean isSuccess = false;
		String resp = "";
		try {
			List<MisUser> misUserList = new ObjectMapper().readValue(
					BaseJson.getReqBody(jsonBody).get("list").toString(), new TypeReference<List<MisUser>>() {
					});
			if (misUserList != null && misUserList.size() > 0) {
				message = "�����޸ĳɹ���";
				isSuccess = true;
				misUserDao.updateBatch(misUserList);
			} else {
				message = "�޸�ʧ�ܣ�";
				isSuccess = false;
			}
			resp = BaseJson.returnRespObj("system/misUser/updateBatch", isSuccess, message, null);
		} catch (Exception e) {
			logger.error("URL:system/misUser/updateBatch �쳣˵����", e);
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return resp;
	}

	@Override
	public String uploadFileAddDb(MultipartFile file) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		Map<String, MisUser> pusOrderMap = uploadScoreInfo(file);

		for (Map.Entry<String, MisUser> entry : pusOrderMap.entrySet()) {

			if (misUserDao.select(entry.getValue().getAccNum()) != null) {
				throw new RuntimeException("�����ظ��û�" + entry.getValue().getAccNum() + " ����");

			}
			try {
				System.err.println(entry.getValue().getAccNum());
				misUserDao.insert(entry.getValue());
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				throw new RuntimeException("����sql����");
			}

		}

		isSuccess = true;
		message = "�û����������ɹ���";
		try {
			resp = BaseJson.returnRespObj("system/misUser/uploadFileAddDb", isSuccess, message, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return resp;

	}

	// ����excle
	private Map<String, MisUser> uploadScoreInfo(MultipartFile file) {
		Map<String, MisUser> temp = new HashMap<>();
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
				String orderNo = GetCellData(r, "�û�����");
				if (orderNo == null || orderNo.length() == 0) {
					continue;
				}
				// ����ʵ����
				MisUser misUser = new MisUser();
				if (temp.containsKey(orderNo)) {
					misUser = temp.get(orderNo);
				}
				// r.getCell(0).setCellType(Cell.CELL_TYPE_NUMERIC);
//						System.out.println(r.getCell(0));
				// ȡ����ǰ�е�1����Ԫ�����ݣ�����װ��pursOrdrʵ��ĸ���������
				misUser.setAccNum(orderNo); // �˺�
				misUser.setPassword(GetCellData(r, "����") == null ? "123456" : GetCellData(r, "����")); // ����
				misUser.setAccSet(GetCellData(r, "����")); // ����
				misUser.setUserName(GetCellData(r, "�û�����")); // �û�����
				misUser.setDepId(GetCellData(r, "���ű���")); // ���ű��
				misUser.setDepName(GetCellData(r, "��������")); // ��������
				misUser.setRoleId(GetCellData(r, "��ɫ����")); // ��ɫ���
				misUser.setRoleName(GetCellData(r, "��ɫ����")); // ��ɫ����
				misUser.setPhoneNo(GetCellData(r, "��ϵ�绰")); // ��ϵ�绰
				misUser.setEmail(GetCellData(r, "��ҵ����")); // ��ҵ����
				misUser.setCreateDate(GetCellData(r, "����ʱ��")); // �û���������
				misUser.setWhsId(GetCellData(r, "�ֿ����")); // �ֿ���
				misUser.setWhsName(GetCellData(r, "�ֿ�����"));// �ֿ�����

				temp.put(orderNo, misUser);

			}
			fileIn.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("������ʽ�����" + j + "��" + e.getMessage());
		}
		return temp;
	}

	@Override
	public void selectUserWhs(String jsonBody, Map map) {

//		String accNum = JSON.parseObject(jsonBody).getJSONObject("reqHead").getString("accNum");
//		MisUser misUser = misUserDao.select(accNum);
//		if (misUser.getDepId().equals("009")) {
//			List<String> list = new ArrayList<String>();
//			Map maps = new HashMap();
//			maps.put("accNum", accNum);
//			List<UserWhs> userWhsList = whsDocMapper.selectUserWhsList(maps);
//
//			for (UserWhs userWhs : userWhsList) {
//				list.add(userWhs.getRealWhs());
//			}
//			if (list.size() == 0) {
//				throw new RuntimeException("��ѯʧ��,û�з���ֿ�Ȩ��");
//			}
//			List<String> aList = whsDocMapper.selectRealWhsList(list);
//			if (aList.size() == 0) {
//				throw new RuntimeException("��ѯʧ��,δ�����߼��ֿ�Ȩ��");
//			}
//			map.put("whs", aList);
//			return;
//		}
	}

	@Override
	public String selectUserLogicWhs(String jsonBody) throws Exception {

		String resp = "";
		List<WhsDoc> whsList = new ArrayList<>();
		Map<String, Object> map = new HashMap<>();
		Map reqMap = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());

		String isSelctWhs = (String) reqMap.get("isSelctWhs"); // �Ƿ��ѯ���вֿ�
		String isSelctRealWhs = (String) reqMap.get("isSelctRealWhs"); // �Ƿ��ѯ���
		String accNum = (String) reqMap.get("accNum");
		String realWhs = (String) reqMap.get("realWhs");
		map.put("realWhs", realWhs);
		if (isSelctWhs.equals("1")) {
			map.put("accNum", accNum);
			// ��ѯ�û��µ����вֿ�
			whsList = userWhsDao.selectUserWhsByMap(map);
			map.clear();
		} else {
			map.put("accNum", accNum);
			if (realWhs.equals("01")) {
				realWhs = "";
			}
			map.put("realWhs", realWhs);
			// ��ѯ�û��µ����вֿ�
			List<WhsDoc> userWhsList = userWhsDao.selectUserWhsByMap(map);

			List<String> list = new ArrayList<>();
			list = userWhsList.stream().map(whsDoc -> {
				return whsDoc.getWhsEncd();
			}).collect(Collectors.toList());

			map.put("whsList", list);
			// ��ѡ�ֿ�
			whsList = userWhsDao.selectWhsByMap(map);

		}

		if (isSelctRealWhs.equals("1")) {

			List<RealWhs> realWhsList = userWhsDao.selectRealWhs(map);
			List<RealWhs> realList = new ArrayList<>();
			RealWhs whss = new RealWhs();
			for (RealWhs whs : realWhsList) {
				if (!whs.getRealWhs().equals("01")) {
					realList.add(whs);
				} else {
					whss.setRealNm(whs.getRealNm());
					whss.setRealWhs(whs.getRealWhs());

				}
			}
			whss.setRealWhsList(realList);
			realWhsList.clear();
			realWhsList.add(whss);
			if (realWhsList.size()==0) {
				resp = BaseJson.returnRespList("system/misUser/userWhsList", false, "��û���κβֿ�Ȩ�ޣ����ڻ��������н�������!", realWhsList);
				return resp;
			}
			resp = BaseJson.returnRespList("system/misUser/userWhsList", true, "��ѯ�ɹ�", realWhsList);

		} else {
			if (whsList.size()==0) {
				resp = BaseJson.returnRespList("system/misUser/userWhsList", true, "��û���κβֿ�Ȩ�ޣ����ڻ��������н�������!", whsList);
				return resp;
			}
			resp = BaseJson.returnRespList("system/misUser/userWhsList", true, "��ѯ�ɹ�", whsList);
		}

		return resp;
	}

	@Override
	public String userWhsAdd(String jsonBody) throws Exception {

		Map<String, Object> map = new HashMap<>();
		List<WhsDoc> whsList = new ArrayList<>();
		List<UserWhs> list = new ArrayList<>(); // ��Ӳֿ�-�û�����
		UserWhs uw = new UserWhs();
		Boolean isSuccess = true;
		String message = "";
		String resp = "";

		Map reqMap = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());

		String accNum = (String) reqMap.get("accNum"); // �û��˺�
		String whsEncds = (String) reqMap.get("whsEncd"); // ��Ӳֿ���
		List<String> whsEncd = strToList(whsEncds);
		map.put("accNum", accNum);
		if (whsEncd.size() > 0) {
			for (String whs : whsEncd) {
				map.put("whsEncd", whs);
				whsList = userWhsDao.selectUserWhsByMap(map); // ��ѯ�Ƿ���ڲֿ�Ȩ��
				if (whsList.size() == 0) {
					uw = new UserWhs();
					uw.setAccNum(accNum);
					uw.setRealWhs(whs);
					list.add(uw);
				}
			}
		}
		if (list.size() > 0) {
			userWhsDao.insertList(list);
			message = "�����ɹ�";
			isSuccess = true;
		} else {
			message = "����ʧ��";
			isSuccess = false;
		}
		resp = BaseJson.returnRespObj("system/misUser/userWhsAdd", isSuccess, message, null);
		return resp;
	}

	@Override
	public String userWhsUpdate(String jsonBody) throws Exception {
		Boolean isSuccess = true;
		String message = "";
		String resp = "";

		Map<String, Object> map = new HashMap<>();
		List<WhsDoc> whsList = new ArrayList<>();

		Map reqMap = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
		String accNum = (String) reqMap.get("accNum");
		String whsEncds = (String) reqMap.get("whsEncd"); // ɾ���ֿ��ϵ ���

		List<String> whsEncd = strToList(whsEncds);

		List<String> delList = new ArrayList<>();

		map.put("accNum", accNum);

		if (whsEncd.size() > 0) {
			map.put("whsList", whsEncd);
			whsList = userWhsDao.selectUserWhsByMap(map); // ��ѯ�Ƿ���ڲֿ�Ȩ��
			if (whsList.size() > 0) {
				delList = whsList.stream().map(whsDoc -> {
					return whsDoc.getOrdrNum().toString();
				}).collect(Collectors.toList());
			}
		}

		if (delList.size() > 0) {
			userWhsDao.delList(delList);
			message = "�����ɹ�";
			isSuccess = true;
		} else {
			message = "����ʧ��";
			isSuccess = false;
		}

		resp = BaseJson.returnRespObj("system/misUser/userWhsUpdate", isSuccess, message, null);
		return resp;
	}

	private List<String> strToList(String param) {

		List<String> list = new ArrayList<>();
		if (StringUtils.isNotEmpty(param)) {
			if (param.contains(",")) {
				String[] str = param.split(",");
				for (int i = 0; i < str.length; i++) {

					list.add(str[i]);

				}
			} else {
				if (StringUtils.isNotEmpty(param)) {
					list.add(param);
				}
			}
		}

		return list;

	}

	/**
	 * �ж��Ƿ��вֿ�Ȩ��
	 * 
	 * @param jsonBody ����body
	 * @param whsEncd  �ֿ����
	 * @return true������� false��Ȩ����
	 */
	public boolean isWhsPer(String jsonBody, String whsEncd) {
		Boolean isPer = true;
		try {
			String whsId = BaseJson.getReqHead(jsonBody).get("whsId").asText();
			List<String> list = strToList(whsId);
			if (list.size() > 0) {
				isPer =	list.contains(whsEncd);
			} else {
				isPer = false;
			}
		} catch (IOException e) {

			e.printStackTrace();
		}
		return isPer;
	}
}
