package com.px.mis.whs.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.px.mis.whs.entity.GdsBit;
import com.px.mis.whs.entity.MovBitTab;
import com.px.mis.whs.entity.Regn;
import com.px.mis.whs.entity.WhsDoc;
import com.px.mis.whs.entity.MovBitList;

//货位分配
public interface GdsBitDistionMapper {

    //查询仓库、区域、货位、存货信息
    public List<WhsDoc> selectWDoc();

    public List<Regn> selectRegn(@Param("whsEncd") String whsEncd);

    public List<GdsBit> selectBit(@Param("regnEncd") String regnEncd);

    public List<MovBitTab> selectInvty(@Param("gdsBitEncd") String gdsBitEncd);

    public List<Map> selectInvtyWhs(Map map);

    public int selectInvtyWhsCount(Map map);

    //插入移位清单
    public int insertMovBitList(List<MovBitList> movBitList);

    //不要了
    public List<MovBitTab> selectMTab(@Param("whsEncd") String whsEncd);

    //PAD 展示列表
    public List<MovBitList> selectMTabList(List<String> list);

    public int updateMBitList(MovBitList movBitList);//回传移位清单

    //PC端移位
    public int updateMovbit(MovBitTab movBitTab);

    public int updateMovbitTab(List<MovBitTab> movBitTab);//修改移位表数量(减法)

    public int updateMovbitTabJia(List<MovBitTab> movBitTab);//修改移位表数量(加法)

    //查询
    public List<MovBitList> selectMTabListId(List<String> list);

    //删除
    public void deleteMTabList(List<String> list);

    //条件
    public List<MovBitList> selectMTabLists(Map<?, ?> map);

    public int selectMTabListsCount(Map<?, ?> map);

    //条件查询仓库的所有货位
    public List<Map<String, Object>> selectWhsgds(Map map);


}
