package com.px.mis.whs.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import com.px.mis.whs.entity.InvtyTab;
import com.px.mis.whs.entity.MovBitTab;
import com.px.mis.whs.entity.OthOutIntoWhs;
import com.px.mis.whs.entity.OthOutIntoWhsSubTab;


public interface OthOutIntoWhsService {

    //新增其他出入库
    public String insertOthOutIntoWhs(String userId, OthOutIntoWhs outIntoWhs, List<OthOutIntoWhsSubTab> oList,String loginTime);

    //修改其他出入库
    public String updateOthOutIntoWhs(OthOutIntoWhs outIntoWhs, List<OthOutIntoWhsSubTab> oList);

    //删除其他出入库
    public String deleteAllOthOutIntoWhs(String formNum);

    //简单查 组装拆卸单
    public String query(String formNum);

    public String queryMovBitTab(String formNum, String eqwe, String qeq);

    public String queryList(Map map);

    //审核
    public String updateOutInWhsChk(String userId, String jsonBody,String loginTime);

    //弃审
    public String updateOutInWhsNoChk(String userId, String jsonBody);

    //打印
    public String queryListDaYin(Map map);

    //PDA 入库单  
    //(查询)  入库
    public String selectOINChkr(String whsEncd);

    //查询  出库
    public String selectOutChkr(String whsEncd);

    //回传数据  指定货位 入库单
    public String uInvMov(List<InvtyTab> invtyTab, List<OthOutIntoWhsSubTab> othOutIntoWhsSubTab,
                          List<MovBitTab> movBitTab, OthOutIntoWhs othOutIntoWhs);

    //PDA 出库单
    public String uOutvMov(List<InvtyTab> invtyTab, List<OthOutIntoWhsSubTab> othOutIntoWhsSubTab,
                           List<MovBitTab> movBitTab, OthOutIntoWhs othOutIntoWhs);

    public String updateMovbitTab(List<MovBitTab> movBitTab);//修改移位表数量

    public String uploadFileAddDb(MultipartFile file);

    public String uploadFileAddDbU8(MultipartFile file);

    //新增货位表
    public String insertInvtyGdsBitList(List<MovBitTab> oList, String orderNum,  String  serialNum);

    //删除
    public String deleteInvtyGdsBitList(String num, String denji, String list);

    //查询
    public String queryInvtyGdsBitList(String json);

    //更新
    public String uploadInvtyGdsBitList(List<MovBitTab> xinlist, List<MovBitTab> gailist, String orderNum,  String  serialNum);

    // 整单联查
    public String wholeSingleLianZha(String formNum, String formTypEncd);
    // 批量分配货位
    String allInvtyGdsBitList( String  formNum );
}
