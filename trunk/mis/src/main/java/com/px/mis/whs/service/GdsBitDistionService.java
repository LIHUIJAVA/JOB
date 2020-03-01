package com.px.mis.whs.service;

import java.util.List;
import java.util.Map;

import com.px.mis.whs.entity.MovBitList;
import com.px.mis.whs.entity.MovBitTab;

//货位分配
public interface GdsBitDistionService {

    //查询仓库、区域、货位
    public String selectWDoc();

    public String selectRegn(String whsEncd);

    public String selectBit(String regnEncd);

    public String selectInvty(String gdsBitEncd);

    public String selectInvtyWhs(Map map);

    //PC进行移位
    public String updateMovbit(List<MovBitList> movBitList, List<MovBitTab> movBitTab);

    //不要了
    public String selectMTab(String whsEncd);

    //PAD 展示列表
    public String selectMTabList(String whsEncd);

    public String updateMBitList(MovBitList movBitList);//回传移位清单

    //	货位匹配1.0版
    public String selectMBitList(Map map);

    //PC进行移位新增
    public String insertMovbitPC(List<MovBitList> movBitList);

    //移位删除
    public String deleteMovbit(String ordrNum);

    //条件查询
    String selectMTabLists(Map map);

    //条件查询仓库的所有货位
    public String selectWhsgds(Map map);

}
