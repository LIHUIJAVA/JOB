package com.px.mis.whs.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.px.mis.whs.entity.MovBitTab;

public interface InvtyGdsBitListMapper {
    // 增/
    public MovBitTab insertInvtyGdsBitList(MovBitTab record);

    // list增
    public int insertInvtyGdsBitLists(List<MovBitTab> record);

    // 查/
    public List<MovBitTab> selectInvtyGdsBitList(MovBitTab record);

    // 不分
    public List<MovBitTab> selectInvtyGdsBitListf(String orderNum);

    // 数量sum单号查
    public List<MovBitTab> selectInvtyGdsBitListOrderNum(String orderNum);

    // 对象sum查
    public MovBitTab selectInvtyGdsBitSum(MovBitTab record);

    // 主单 加 子单
    public List<MovBitTab> selectInvtyGdsBitListNum(@Param("list") List<String> list, @Param("orderNum") String orderNum);

    // 改/
    public int updateInvtyGdsBitList(MovBitTab record);

    // 改 list
    public int updateInvtyGdsBitLists(List<MovBitTab> record);

    // 删/
    public int deleteInvtyGdsBitList(List<String> orderNum);

    // 不循环的1，12，12，213 serial
    public int deleteInvtyGdsBitSerial(List<String> serial);

    // 不循环的1，12，12，213 id
    public int deleteInvtyGdsBitId(List<String> id);

    // 加数
    public int updateIntoinvtyGdsBitList(MovBitTab record);

    // 减数
    public int updateOutInvtyGdsBitList(MovBitTab record);

}
