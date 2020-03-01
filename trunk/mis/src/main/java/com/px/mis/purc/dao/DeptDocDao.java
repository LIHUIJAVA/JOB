package com.px.mis.purc.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.purc.entity.DeptDoc;

public interface DeptDocDao {

    int insertDeptDoc(DeptDoc deptDoc);
    
    int updateDeptDocByDeptEncd(DeptDoc deptDoc);
    
    int updateDeptDocByDeptEncd(List<DeptDoc> deptDoc);
    
    int deleteDeptDocByDeptEncd(String deptEncd);

    int insertSelective(DeptDoc record);

    DeptDoc selectDeptDocByDeptEncd(String deptEncd);
    
    List<DeptDoc> selectDeptDocList(Map map);
    
    int selectDeptDocCount(Map map);
    
    List<DeptDoc> printingDeptDocList(Map map);
    
    String selectDeptId(String deptId);
    
    int deleteDeptDocList(List<String> deptId);
 
}