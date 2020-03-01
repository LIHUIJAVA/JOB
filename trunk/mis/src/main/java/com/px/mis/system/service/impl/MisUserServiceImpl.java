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
				message = "当前账号不存在!";
				isSuccess = false;
			} else if (!misUser.getPassword().equals(password)) {
				misUser = null;
				message = "密码错误，请重新输入!";
				isSuccess = false;
			} else {
				Map<String, Object> map = new HashMap<>();
				map.put("accNum", accNum);
				String whsId = "";
				// 查询用户下的已有仓库
				List<WhsDoc> userWhsList = userWhsDao.selectUserWhsByMap(map);
				if (userWhsList.size() > 0) {
					List<String> delList = userWhsList.stream().map(whsDoc -> {
						return whsDoc.getWhsEncd();
					}).collect(Collectors.toList());
					whsId = StringUtils.join(delList, ",");
				}
				misUser.setWhsId(whsId);
				message = "登录成功!";
			}

			resp = BaseJson.returnRespObj("system/misUser/login", isSuccess, message, misUser);

		} catch (IOException e) {
			logger.error("URL:system/misUser/login 异常说明：", e);
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
				objectNode.put("message", "新增成功！");
			} else {
				objectNode.put("isSuccess", false);
				objectNode.put("message", "当前账号已存在！");
			}
		} catch (Exception e) {
			logger.error("URL:system/misUser/login 异常说明：", e);
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
					objectNode.put("message", "当前用户不存在!");
				} else {
					misUserDao.delete2(accNum);
					objectNode.put("isSuccess", true);
					objectNode.put("message", "删除成功！");
				}
			} catch (Exception e) {
				logger.error("URL:system/misUser/delete 异常说明：", e);
			}
		} else {
			try {
				objectNode = JacksonUtil.getObjectNode("");
				if (misUserDao.select(accNum) == null) {
					objectNode.put("isSuccess", false);
					objectNode.put("message", "当前用户不存在!");
				} else {
					misUserDao.delete(accNum);
					objectNode.put("isSuccess", true);
					objectNode.put("message", "删除成功！");
				}
			} catch (Exception e) {
				logger.error("URL:system/misUser/delete 异常说明：", e);
			}
		}
		return objectNode;
	}

	/**
	 * id放入list
	 * 
	 * @param id id(多个已逗号分隔)
	 * @return List集合
	 */
	public List<String> getList(String id) {
		List<String> list = new ArrayList<String>();
		String[] str = id.split(",");
		for (int i = 0; i < str.length; i++) {
			System.out.println(str[i] + "=====impl 的 str[i]");
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
				message = "角色编号不存在！";
				isSuccess = false;
			} else {
				MisUser user = misUserDao.select(misUser.getAccNum());
//				misUser.setPassword(user.getPassword());
				misUser.setRoleId(role.getName());
			}

			if (misUserDao.select(misUser.getAccNum()) == null) {
				message = "编号" + misUser.getAccNum() + "不存在，请重新输入！";
				isSuccess = false;
			} else {
				misUserDao.update(misUser);
				message = "修改成功！";
				isSuccess = true;
			}

			resp = BaseJson.returnRespObj("system/misUser/edit", isSuccess, message, null);
		} catch (IOException e) {
			logger.error("URL:system/misUser/edit 异常说明：", e);
			throw new RuntimeException();
		}
		return resp;
	}
	//用户档案查询接口
	@Override
	public String queryList(Map map) {
		String message = "查询成功！";
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
			logger.error("URL:system/misUser/queryList 异常说明：", e);
		}
		return resp;
	}
	//用户档案导出接口
		@Override
		public String queryListExport(Map map) {
			String message = "查询成功！";
			Boolean isSuccess = true;
			String resp = "";
			try {
				List<MisUser> mList = misUserDao.selectList(map);
				int count = misUserDao.selectCount(map);
				resp = BaseJson.returnRespList("system/misUser/queryListExport", isSuccess, message, mList);
			} catch (Exception e) {
				logger.error("URL:system/misUser/queryListExport 异常说明：", e);
			}
			return resp;
		}

	@Override
	public String query(String accNum) {
		String message = "查询成功！";
		Boolean isSuccess = true;
		String resp = "";
		try {
			MisUser misUser = misUserDao.select(accNum);
			if (misUser == null) {
				isSuccess = false;
				message = "该用户不存在！";
			}
			resp = BaseJson.returnRespObj("system/misUser/query", isSuccess, message, misUser);
		} catch (Exception e) {
			logger.error("URL:system/misUser/query 异常说明：", e);
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
			resp = BaseJson.returnResp("system/misUser/userTree", true, "查询成功！",
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

			resp = BaseJson.returnRespObj("system/user/permAss", true, "菜单权限分配成功！", null);
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
				objectNode.put("message", "删除成功！");
			} else {
				objectNode.put("isSuccess", false);
				objectNode.put("message", "删除失败！");
			}
		} catch (Exception e) {
			logger.error("URL:system/misUser/delMisUser 异常说明：", e);
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
				message = "批量修改成功！";
				isSuccess = true;
				misUserDao.updateBatch(misUserList);
			} else {
				message = "修改失败！";
				isSuccess = false;
			}
			resp = BaseJson.returnRespObj("system/misUser/updateBatch", isSuccess, message, null);
		} catch (Exception e) {
			logger.error("URL:system/misUser/updateBatch 异常说明：", e);
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
				throw new RuntimeException("插入重复用户" + entry.getValue().getAccNum() + " 请检查");

			}
			try {
				System.err.println(entry.getValue().getAccNum());
				misUserDao.insert(entry.getValue());
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				throw new RuntimeException("插入sql问题");
			}

		}

		isSuccess = true;
		message = "用户档案新增成功！";
		try {
			resp = BaseJson.returnRespObj("system/misUser/uploadFileAddDb", isSuccess, message, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return resp;

	}

	// 导入excle
	private Map<String, MisUser> uploadScoreInfo(MultipartFile file) {
		Map<String, MisUser> temp = new HashMap<>();
		int j = 1;
		try {
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
			// 对Sheet中的每一行进行迭代
			for (int i = firstRowNum + 1; i <= lastRowNum; i++) {
				j++;
				Row r = sht0.getRow(i);
				// 如果当前行的行号（从0开始）未达到2（第三行）则从新循环
				if (r.getRowNum() < 1) {
					continue;
				}
				String orderNo = GetCellData(r, "用户编码");
				if (orderNo == null || orderNo.length() == 0) {
					continue;
				}
				// 创建实体类
				MisUser misUser = new MisUser();
				if (temp.containsKey(orderNo)) {
					misUser = temp.get(orderNo);
				}
				// r.getCell(0).setCellType(Cell.CELL_TYPE_NUMERIC);
//						System.out.println(r.getCell(0));
				// 取出当前行第1个单元格数据，并封装在pursOrdr实体的各个属性上
				misUser.setAccNum(orderNo); // 账号
				misUser.setPassword(GetCellData(r, "密码") == null ? "123456" : GetCellData(r, "密码")); // 密码
				misUser.setAccSet(GetCellData(r, "账套")); // 账套
				misUser.setUserName(GetCellData(r, "用户名称")); // 用户姓名
				misUser.setDepId(GetCellData(r, "部门编码")); // 部门编号
				misUser.setDepName(GetCellData(r, "部门名称")); // 部门名称
				misUser.setRoleId(GetCellData(r, "角色编码")); // 角色编号
				misUser.setRoleName(GetCellData(r, "角色名称")); // 角色名称
				misUser.setPhoneNo(GetCellData(r, "联系电话")); // 联系电话
				misUser.setEmail(GetCellData(r, "企业邮箱")); // 企业邮箱
				misUser.setCreateDate(GetCellData(r, "创建时间")); // 用户创建日期
				misUser.setWhsId(GetCellData(r, "仓库编码")); // 仓库编号
				misUser.setWhsName(GetCellData(r, "仓库名称"));// 仓库名称

				temp.put(orderNo, misUser);

			}
			fileIn.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("解析格式问题第" + j + "行" + e.getMessage());
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
//				throw new RuntimeException("查询失败,没有分配仓库权限");
//			}
//			List<String> aList = whsDocMapper.selectRealWhsList(list);
//			if (aList.size() == 0) {
//				throw new RuntimeException("查询失败,未分配逻辑仓库权限");
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

		String isSelctWhs = (String) reqMap.get("isSelctWhs"); // 是否查询已有仓库
		String isSelctRealWhs = (String) reqMap.get("isSelctRealWhs"); // 是否查询大仓
		String accNum = (String) reqMap.get("accNum");
		String realWhs = (String) reqMap.get("realWhs");
		map.put("realWhs", realWhs);
		if (isSelctWhs.equals("1")) {
			map.put("accNum", accNum);
			// 查询用户下的已有仓库
			whsList = userWhsDao.selectUserWhsByMap(map);
			map.clear();
		} else {
			map.put("accNum", accNum);
			if (realWhs.equals("01")) {
				realWhs = "";
			}
			map.put("realWhs", realWhs);
			// 查询用户下的已有仓库
			List<WhsDoc> userWhsList = userWhsDao.selectUserWhsByMap(map);

			List<String> list = new ArrayList<>();
			list = userWhsList.stream().map(whsDoc -> {
				return whsDoc.getWhsEncd();
			}).collect(Collectors.toList());

			map.put("whsList", list);
			// 可选仓库
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
				resp = BaseJson.returnRespList("system/misUser/userWhsList", false, "您没有任何仓库权限，请在基本档案中进行配置!", realWhsList);
				return resp;
			}
			resp = BaseJson.returnRespList("system/misUser/userWhsList", true, "查询成功", realWhsList);

		} else {
			if (whsList.size()==0) {
				resp = BaseJson.returnRespList("system/misUser/userWhsList", true, "您没有任何仓库权限，请在基本档案中进行配置!", whsList);
				return resp;
			}
			resp = BaseJson.returnRespList("system/misUser/userWhsList", true, "查询成功", whsList);
		}

		return resp;
	}

	@Override
	public String userWhsAdd(String jsonBody) throws Exception {

		Map<String, Object> map = new HashMap<>();
		List<WhsDoc> whsList = new ArrayList<>();
		List<UserWhs> list = new ArrayList<>(); // 添加仓库-用户集合
		UserWhs uw = new UserWhs();
		Boolean isSuccess = true;
		String message = "";
		String resp = "";

		Map reqMap = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());

		String accNum = (String) reqMap.get("accNum"); // 用户账号
		String whsEncds = (String) reqMap.get("whsEncd"); // 添加仓库多个
		List<String> whsEncd = strToList(whsEncds);
		map.put("accNum", accNum);
		if (whsEncd.size() > 0) {
			for (String whs : whsEncd) {
				map.put("whsEncd", whs);
				whsList = userWhsDao.selectUserWhsByMap(map); // 查询是否存在仓库权限
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
			message = "操作成功";
			isSuccess = true;
		} else {
			message = "操作失败";
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
		String whsEncds = (String) reqMap.get("whsEncd"); // 删除仓库关系 多个

		List<String> whsEncd = strToList(whsEncds);

		List<String> delList = new ArrayList<>();

		map.put("accNum", accNum);

		if (whsEncd.size() > 0) {
			map.put("whsList", whsEncd);
			whsList = userWhsDao.selectUserWhsByMap(map); // 查询是否存在仓库权限
			if (whsList.size() > 0) {
				delList = whsList.stream().map(whsDoc -> {
					return whsDoc.getOrdrNum().toString();
				}).collect(Collectors.toList());
			}
		}

		if (delList.size() > 0) {
			userWhsDao.delList(delList);
			message = "操作成功";
			isSuccess = true;
		} else {
			message = "操作失败";
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
	 * 判断是否有仓库权限
	 * 
	 * @param jsonBody 请求body
	 * @param whsEncd  仓库编码
	 * @return true允许访问 false无权访问
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
