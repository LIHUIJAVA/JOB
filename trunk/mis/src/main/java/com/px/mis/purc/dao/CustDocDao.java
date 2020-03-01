package com.px.mis.purc.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.purc.entity.CustCls;
import com.px.mis.purc.entity.CustDoc;

public interface CustDocDao {

	// 新增客户档案
	int insertCustDoc(CustDoc custDoc);

	// 导入时新增客户档案
	int insertCustDocUpload(CustDoc custDoc);

	// 修改科目档案
	int updateCustDocByCustId(CustDoc custDoc);

	// 删除单个客户档案
	int deleteCustDocByCustId(String custId);

	// 查询客户档案详情
	CustCls selectCustDocByCustId(String custId);

	// 分页查询客户档案
	List<CustCls> selectCustDocList(Map map);

	// 查询客户档案总条数
	int selectCustDocCount(Map map);

	// 不带分页的查询所有客户档案
	List<CustCls> printingCustDocList(Map map);

	// 按照客户分类编码查询关联的客户档案信息
	String selectClsId(String custId);

	// 批量删除客户档案
	int deleteCustDocList(List<String> custId);

	// 查询上级客户
	CustDoc selectCustTotalByCustId(String custId);

	List<CustDocDao> selectHaving(Map map);

	//区间选择客户
	List<CustCls> selectCustDocListByItv(Map map);
	
	

}