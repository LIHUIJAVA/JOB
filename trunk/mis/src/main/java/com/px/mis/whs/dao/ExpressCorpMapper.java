package com.px.mis.whs.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.px.mis.ec.entity.ExpressCodeAndName;
import com.px.mis.whs.entity.ExpressCorp;
import com.px.mis.whs.entity.ExpressCorpMap;

public interface ExpressCorpMapper {

    // 新增快递公司
    public int insertExpressCorp(ExpressCorp record);

    // 修改快递公司
    public int updateExpressCorp(ExpressCorp record);

    // 删除快递公司
    public int deleteExpressCorp(String expressEncd);

    public int deleteECorpList(List<String> expressEncd);// 批删
    // 批查

    public List<ExpressCorp> selectECorpAllList(@Param("expressEncd") List<String> expressEncd);

    // 简单查 快递公司
    public ExpressCorp selectExpressCorp(@Param("expressEncd") String expressEncd);

    public List<ExpressCorp> selectExpressCorpList(@Param("expressEncd") String expressEncd);

    // 分页查
    public List<ExpressCorp> selectList(Map map);

    public int selectCount(Map map);

    // 打印
    public List<ExpressCorpMap> selectListDaYin(Map map);

    public List<ExpressCorp> selectExpressEncdName(@Param("expressEncd") String expressEncd);

    //快递公司名称类型ExpressCodeAndName
    public List<ExpressCodeAndName> queryExpressCodeAndNameList();

}