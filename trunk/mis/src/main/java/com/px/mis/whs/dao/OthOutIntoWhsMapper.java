package com.px.mis.whs.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.whs.entity.MovBitTab;
import org.apache.ibatis.annotations.Param;

import com.px.mis.whs.entity.OthOutIntoWhs;
import com.px.mis.whs.entity.OthOutIntoWhsMap;
import com.px.mis.whs.entity.OthOutIntoWhsSubTab;

//其他出入库单
public interface OthOutIntoWhsMapper {
    /**
     * 查询未记账的其他出入库单
     */
    List<OthOutIntoWhs> selectOthOutIntoWhsIsInvty(Map map);

    /**
     * 新增个月期初出入库单
     */
    Integer selectInvtyTabTermInvty(String loginTime);

    /**
     * 新增总期初出入库单
     */
    Integer insertInvtyTabTermInvty(String loginTime);

    /**
     * 删除期总初出入库单
     */
    Integer deleteInvtyTabTerm(String loginTime);

    /**
     * 查询期初时间出入库单记录是否有记录
     */
    Integer selectInvtyTabTermInvtyCount(String loginTime);

    /**
     * 删除期初出入库单
     */
    Integer deleteInvtyTabTermInvty(String loginTime);

    // 新增其他出入库
    public int insertOthOutIntoWhs(OthOutIntoWhs record);

    public int insertOthOutIntoWhsSubTab(List<OthOutIntoWhsSubTab> record);// 批增子表

    public int exInsertOthOutIntoWhs(OthOutIntoWhs record);

    public int exInsertOthOutIntoWhsSubTab(List<OthOutIntoWhsSubTab> record);// 批增子表
    // 修改其他出入库

    public int updateOthOutIntoWhs(OthOutIntoWhs record);

    public int updateBat(List<OthOutIntoWhsSubTab> oList);// 返回国际批次
    // 删除其他出入库 批删

    public int deleteOthOutIntoWhsSubTab(String formNum); // 主

    public int deleteAllOthOutIntoWhs(List<String> formNum);// 主子

    // 简单查 其他出入库
    public OthOutIntoWhs selectOthOutIntoWhs(@Param("formNum") String formNum);

    public List<OthOutIntoWhsSubTab> selectOthOutIntoWhsSubTabList(@Param("formNum") String formNum);

    // 主子表
    public OthOutIntoWhs selectOthOutIntoWhsSubTab(@Param("formNum") String formNum);

    // 批查
    public List<OthOutIntoWhs> selectOthOutIntoWhsList(@Param("formNum") List<String> formNum);

    // 分页查
    public List<OthOutIntoWhsMap> selectList(Map map);

    public int selectCount(Map map);

    // 打印
    public List<OthOutIntoWhs> selectListDaYin(Map map);

    // 审核
    public int updateOutInWhsChk(List<OthOutIntoWhs> cList);

    public OthOutIntoWhs selectIsChk(@Param("formNum") String formNum);// 查询是否是已审核状态
    // 弃审

    public int updateOutInWhsNoChk(List<OthOutIntoWhs> cList);

    // PDA 入库单
    // (查询) 入库
    public List<OthOutIntoWhs> selectOINChkr(List<String> whsEncd);
    // 回传数据 指定货位 【库存表需要出库增加可用量 修改出入库单的入库状态[处理中--处理完成]】InvtyNum表

    // PDA 出库单
    // 查询 出库
    public List<OthOutIntoWhs> selectOutChkr(List<String> whsEncd);

    // 【与入库单调用的接口是一样的 库存表需要出库减少可用量 修改出入库单的出库状态[处理中--处理完成]】
    // 修改记账状态
    public int updateOutInWhsBookEntry(List<OthOutIntoWhs> cList);

    // 分页
    List<OthOutIntoWhs> selectOthOutIntoWhsStream(Map map);

    int countSelectOthOutIntoWhsStream(Map map);

    // 查来源单号
    public OthOutIntoWhs selectOthOutIntoWhsSrc(@Param("srcFormNum") String srcFormNum,
                                                @Param("outIntoWhsTypId") String outIntoWhsTypId);

    /**
     * 序号查子表 其他出入库
     *
     * @param ordrNum 序号
     * @return OthOutIntoWhsSubTab 明细表
     */
    public OthOutIntoWhsSubTab selectOthByOrdrNumKey(@Param("ordrNum") Long ordrNum);


    //		 新增其他出入库删除后数据
    public Integer insertOthOutIntoWhsDl(List<String> formNum);

    public Integer insertOthOutIntoWhsSubTabDl(List<String> formNum);

    List<MovBitTab> selectallInvtyGdsBitListInto(String formNum);


}