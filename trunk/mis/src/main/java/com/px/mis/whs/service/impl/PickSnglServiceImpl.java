package com.px.mis.whs.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.px.mis.whs.entity.OthOutIntoWhsMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.px.mis.purc.entity.SellOutWhs;
import com.px.mis.purc.entity.SellOutWhsSub;
import com.px.mis.purc.entity.SellSngl;
import com.px.mis.purc.service.impl.SellOutWhsServiceImpl;
import com.px.mis.util.BaseJson;
import com.px.mis.util.GetOrderNo;
import com.px.mis.whs.dao.InvtyGdsBitListMapper;
import com.px.mis.whs.dao.PickSnglMapper;
import com.px.mis.whs.entity.MergePickSngl;
import com.px.mis.whs.entity.MovBitTab;
import com.px.mis.whs.entity.PickSnglSubTab;
import com.px.mis.whs.entity.PickSnglTab;
import com.px.mis.whs.entity.SellSnglWhs;
import com.px.mis.whs.service.PickSnglService;

@Service
@Transactional
public class PickSnglServiceImpl implements PickSnglService {

    @Autowired
    PickSnglMapper pickSnglMapper;

    @Autowired
    GetOrderNo getOrderNo;
    @Autowired
    InvtyGdsBitListMapper bitListMapper;
    @Autowired
    SellOutWhsServiceImpl sellOutWhsServiceImpl;

    // 查询销售单
    @Override
    public String selectSellById(Map map) throws Exception {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        try {

            // 查询销售单
//		List<String> list = getList(sellSnglId);
//		List<SellSngl> sList = pickSnglMapper.selectSellById(list);
//		// 修改销售订单的是否拣货标识
//		// 可以把List<SellSngl> sList的销售单号 提出 转换成 set 去重复id 应用处理变慢，减少一次查询
//		List<SellSngl> sellList = pickSnglMapper.selectDistinctId(list);
            List<SellSnglWhs> sList = pickSnglMapper.selectSellWhsIsPick(map);
            int count = pickSnglMapper.selectSellWhsIsPickCount(map);

            int pageNo = Integer.parseInt(map.get("pageNo").toString());
            int pageSize = Integer.parseInt(map.get("pageSize").toString());
            int listNum = sList.size();
            int pages = count / pageSize + 1;
            /**
             * List<SellSngl> sellList = pickSnglMapper.selectDistinctWhs(sellSnglWhs);
             * pickSnglMapper.updateSellSngl(sellList);
             *
             * // 添加拣货单 String Number = getOrderNo.getSeqNo("JH", userId);// 获取订单号
             *
             * if (pickSnglMapper.selectPick(Number) != null) { message = "编号" + Number +
             * "已存在，请重新输入！"; isSuccess = false; throw new RuntimeException(message); } else
             * { // 拣货主表 PickSnglTab pickTab = new PickSnglTab();
             * pickTab.setPickSnglNum(Number); pickTab.setSetupPers(userId);// 当前登录用户为创建人
             *
             * // 拣货子表 List<PickSnglSubTab> pickList = new ArrayList<PickSnglSubTab>(); for
             * (SellSngl sngl : sList) { PickSnglSubTab pSubTab = new PickSnglSubTab();
             * pSubTab.setPickSnglNum(Number); pSubTab.setSellSnglId(sngl.getSellSnglId());
             * pSubTab.setWhsEncd(sngl.getWhsEncd()); pSubTab.setWhsNm(sngl.getWhsNm());
             * pSubTab.setInvtyEncd(sngl.getInvtyEncd());
             * pSubTab.setInvtyNm(sngl.getInvtyNm());
             * pSubTab.setSpcModel(sngl.getSpcModel());
             * pSubTab.setMeasrCorpId(sngl.getMeasrCorpId());
             * pSubTab.setBarCd(sngl.getCrspdBarCd()); pSubTab.setBatNum(sngl.getBatNum());
             * pSubTab.setPrdcDt(sngl.getPrdcDt());
             * pSubTab.setBaoZhiQi(sngl.getBaoZhiQi());// 保质期
             * pSubTab.setInvldtnDt(sngl.getInvldtnDt());
             * pSubTab.setGdsBitEncd(sngl.getGdsBitEncd());
             * pSubTab.setSellTyp(sngl.getSellTypId());
             * pSubTab.setBizTyp(sngl.getBizTypId()); pSubTab.setQty(sngl.getQty());
             * pickList.add(pSubTab); }
             *
             * pickSnglMapper.insertPickSngl(pickTab);
             * pickSnglMapper.insertPickSnglSubTab(pickList);
             *
             * message = "新增成功！"; isSuccess = true; }
             */

            resp = BaseJson.returnRespList("whs/pick_sngl/selectSellById", isSuccess, message, count, pageNo, pageSize,
                    listNum, pages, sList);
        } catch (Exception e) {

        }

        return resp;
    }

    /**
     * id放入list
     *
     * @param id id(多个已逗号分隔)
     * @return List集合
     */
    public List<String> getList(String id) {
        if (id == null || id.equals("")) {
            return null;
        }
        List<String> list = new ArrayList<String>();
        String[] str = id.split(",");
        for (int i = 0; i < str.length; i++) {

            list.add(str[i]);
        }
        return list;
    }

    // 添加拣货单
    @Override
    public String insertPickSngl(String sellSnglId, String userId, String userName,String loginTime) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        try {
            List<String> list = getList(sellSnglId);
            if (list.size() == 0) {
                throw new RuntimeException("请输入销售单号");
            }
//			int asd = pickSnglMapper.selectSellSngl(list,"0");
//			// System.out.println(asd);
//			if (list.size() != asd) {
//				throw new RuntimeException("请检查销售单号是否有为符合要求");
//			}
            // 修改销售订单的是否拣货标识
            int li = pickSnglMapper.updateSellSngl(list, "1");
//			if (li != list.size()) {
//				throw new RuntimeException("请检查销售单号是否有为符合要求");
//			}

            // 添加拣货单
            String Number = getOrderNo.getSeqNo("JH", userId,loginTime);// 获取订单号
            if (pickSnglMapper.selectPick(Number) != null) {
                message = "编号" + Number + "已存在，请重新输入！";
                isSuccess = false;
                throw new RuntimeException(message);
            } else {
                pickSnglMapper.updateSellSnglWhs(list, Number);

                PickSnglTab pickTab = new PickSnglTab();
                pickTab.setPickSnglNum(Number);
                pickTab.setSetupPers(userName);// 当前登录用户为创建人
                pickTab.setSetupTm(loginTime);//
                pickTab.setPickSnglTm(loginTime);//
                List<PickSnglSubTab> pickSnglSubTab = pickSnglMapper.selectDistinctWhs(list);
                for (PickSnglSubTab tab : pickSnglSubTab) {
                    tab.setPickSnglNum(Number);

                    tab.setInvldtnDt(
                            (tab.getInvldtnDt() == null || tab.getInvldtnDt().equals("")) ? null : tab.getInvldtnDt());
                    tab.setPrdcDt((tab.getPrdcDt() == null || tab.getPrdcDt().equals("")) ? null : tab.getPrdcDt());
                }
                pickSnglMapper.insertPickSngl(pickTab);
                pickSnglMapper.insertPickSnglSubTab(pickSnglSubTab);

                message = "新增成功！";
                isSuccess = true;

            }

//		if (pickSnglMapper.selectPick(pickTab.getPickSnglNum()) != null) {
//			message = "编号" + pickTab.getPickSnglNum() + "已存在，请重新输入！";
//			isSuccess = false;
//		} else {
//
//			pickSnglMapper.insertPickSngl(pickTab);
//			pickSnglMapper.insertPickSnglSubTab(pickList);
//
//			message = "新增成功！";
//			isSuccess = true;
//		}
//

            resp = BaseJson.returnRespObj("whs/pick_sngl/insertPickSngl", isSuccess, message, null);
        } catch (IOException e) {

        }
        return resp;
    }

    // 拣货单主列表显示
    @Override
    public String queryAllList(Map map) {
        String resp = "";

        List<String> whsId = getList((String) map.get("whsId"));
        map.put("whsId", whsId);

        List<PickSnglTab> pList = pickSnglMapper.selectAllPickSngl(map);
        int count = pickSnglMapper.selectAllPickCount(map);

        int pageNo = Integer.parseInt(map.get("pageNo").toString());
        int pageSize = Integer.parseInt(map.get("pageSize").toString());
        int listNum = pList.size();
        int pages = count / pageSize + 1;
        try {
            resp = BaseJson.returnRespList("whs/pick_sngl/queryAllList", true, "查询成功！", count, pageNo, pageSize,
                    listNum, pages, pList);
        } catch (IOException e) {

        }
        return resp;
    }

    // 通过主表拣货单据号查询子表信息
    @Override
    public String selectPSubTabById(String pickSnglNum) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";

        List<PickSnglSubTab> pList = new ArrayList<>();
        PickSnglTab pTab = pickSnglMapper.selectPick(pickSnglNum);
        if (pTab != null) {
            pList = pickSnglMapper.selectPSubTabById(pickSnglNum);
            message = "查询成功！";
        } else {
            isSuccess = false;
            message = "编号" + pickSnglNum + "不存在！";
        }

        try {
            resp = BaseJson.returnRespObjList("whs/pick_sngl/selectPSubTabById", isSuccess, message, pTab, pList);
        } catch (IOException e) {

        }
        return resp;
    }

    // 拣货单主-子列表显示
    @Override
    public String queryList(Map map) {
        String resp = "";
        List<PickSnglTab> pList = pickSnglMapper.selectList(map);
        int count = pickSnglMapper.selectCount(map);

        int pageNo = Integer.parseInt(map.get("pageNo").toString());
        int pageSize = Integer.parseInt(map.get("pageSize").toString());
        int listNum = pList.size();
        int pages = count / pageSize;
        if (count % pageSize > 0) {
            pages += 1;
        }
        try {
            resp = BaseJson.returnRespList("whs/pick_sngl/queryList", true, "查询成功！", count, pageNo, pageSize, listNum,
                    pages, pList);
        } catch (IOException e) {

        }
        return resp;
    }

    // 合并拣货单
    @Override
    public String selectPSubTab(String userId, String pickSnglNum, String whsEncd) {
        String resp = "";
        try {
            // 查询出唯一数据
            List<PickSnglSubTab> pList = pickSnglMapper.selectPSubTab(pickSnglNum, whsEncd);

            // 将值放入合并表中
            List<MergePickSngl> mPickList = new ArrayList<>();
            for (PickSnglSubTab pSubTab : pList) {
                MergePickSngl mergePickSngl = new MergePickSngl();
                // mergePickSngl.setPickSnglNum(pSubTab.getPickSnglNum());
//                String Number = getOrderNo.getSeqNo("HB", userId);// 获取订单号
//                mergePickSngl.setPickSnglNum(Number);
                mergePickSngl.setPickNum(pSubTab.getPickSnglNum());
                mergePickSngl.setWhsEncd(pSubTab.getWhsEncd());
                mergePickSngl.setWhsNm(pSubTab.getWhsNm());
                mergePickSngl.setInvtyEncd(pSubTab.getInvtyEncd());
                mergePickSngl.setInvtyNm(pSubTab.getInvtyNm());
                mergePickSngl.setBarCd(pSubTab.getCrspdBarCd());
                mergePickSngl.setBatNum(pSubTab.getBatNum());
                mergePickSngl.setPrdcDt(pSubTab.getPrdcDt());
                mergePickSngl.setInvldtnDt(pSubTab.getInvldtnDt());
                mergePickSngl.setGdsBitEncd(pSubTab.getGdsBitEncd());
                mergePickSngl.setQty(pSubTab.getQty());
                mergePickSngl.setIsFinshPick(0);
                mPickList.add(mergePickSngl);
            }
            int mergePick = pickSnglMapper.insertmergePick(mPickList);

            if (mergePick >= 1) {

                for (PickSnglSubTab pSubTab : pList) {
                    PickSnglTab pTab = new PickSnglTab();
                    pTab.setPickSnglNum(pSubTab.getPickSnglNum());
                    pickSnglMapper.updatePTab(pTab);
                }

                resp = BaseJson.returnRespObj("whs/pick_sngl/mergePickSingl", true, "新增成功", null);
            } else {
                resp = BaseJson.returnRespObj("whs/pick_sngl/mergePickSingl", false, "新增失败", null);
            }
        } catch (Exception e) {

        }
        return resp;
    }

    // PDA 显示所有拣货单列表-
    @Override
    public String selectAllMerge(String whsEncd) {
        String resp = "";
        List<String> list = getList(whsEncd);

        List<PickSnglTab> pList = pickSnglMapper.selectAllMerge(list);

        try {
            if (pList.size() > 0) {

                resp = BaseJson.returnRespObjList("whs/pick_sngl/selectAllMerge", true, "查询成功！", null, pList);
            } else {
                resp = BaseJson.returnRespObjList("whs/pick_sngl/selectAllMerge", false, "查询失败！", null, pList);
            }
        } catch (IOException e) {

        }
        return resp;
    }

    @Override
    public String selectAllById(String pickSnglNum) {
        String resp = "";
        List<MergePickSngl> pList = pickSnglMapper.selectAllById(pickSnglNum);

        try {
            if (pList.size() > 0) {
                resp = BaseJson.returnRespObjList("whs/pick_sngl/selectAllById", true, "查询成功！", null, pList);
            } else {
                resp = BaseJson.returnRespObjList("whs/pick_sngl/selectAllById", false, "查询失败！", null, pList);
            }
        } catch (IOException e) {

        }
        return resp;
    }

    // PDA 回传接口 拣货完成标识 拣货完成时间
    @Override
    public String updatePTabPda(PickSnglTab pSnglTab, List<MovBitTab> list) {
        String resp = "";
        try {
            PickSnglTab mList = pickSnglMapper.selectPick(pSnglTab.getPickSnglNum());
            List<MovBitTab> movBitTabList = new ArrayList<MovBitTab>();
            HashSet<String> set = new HashSet<>();

            int mPick = pickSnglMapper.updatePTabPda(pSnglTab);
            if (mPick == 0) {
                throw new RuntimeException("已回传，无法重复回传");

            }
            List<SellOutWhsSub> outWhsSub = pickSnglMapper.selectPickSnglOutTabList(pSnglTab.getPickSnglNum());
            if (outWhsSub.size() == 0) {
                throw new RuntimeException("拣货单对应的销售已删除");
            }

            for (SellOutWhsSub sub : outWhsSub) {
                set.add(sub.getOutWhsId());
                for (MovBitTab tab : list) {
                    if (sub.getWhsEncd().equals(tab.getWhsEncd()) && sub.getInvtyEncd().equals(tab.getInvtyEncd())
                            && sub.getBatNum().equals(tab.getBatNum()) && tab.getQty().compareTo(BigDecimal.ZERO) == 1
                            && sub.getQty().compareTo(BigDecimal.ZERO) == 1) {
                        MovBitTab bitTab = new MovBitTab();
                        bitTab.setWhsEncd(tab.getWhsEncd());
                        bitTab.setInvtyEncd(tab.getInvtyEncd());
                        bitTab.setBatNum(tab.getBatNum());
                        bitTab.setRegnEncd(tab.getRegnEncd());
                        bitTab.setGdsBitEncd(tab.getGdsBitEncd());
                        bitTab.setOrderNum(sub.getOutWhsId());
                        bitTab.setSerialNum(sub.getOrdrNum().toString());
                        if (tab.getQty().compareTo(sub.getQty()) == 1) {
                            // 货位和-单据数

                            BigDecimal diff = tab.getQty().subtract(sub.getQty());
                            bitTab.setQty(sub.getQty());

                            sub.setQty(BigDecimal.ZERO);
                            tab.setQty(diff);
                        } else if (tab.getQty().compareTo(sub.getQty()) == -1) {
                            // 单据数-货位和

                            BigDecimal diff = sub.getQty().subtract(tab.getQty());
                            bitTab.setQty(tab.getQty());

                            sub.setQty(diff);
                            tab.setQty(BigDecimal.ZERO);
                        } else {

                            bitTab.setQty(tab.getQty());
                            sub.setQty(BigDecimal.ZERO);
                            tab.setQty(BigDecimal.ZERO);
                        }
                        movBitTabList.add(bitTab);
                    }
                }

            }

            for (MovBitTab tab : list) {

                if (tab.getQty().compareTo(BigDecimal.ZERO) != 0) {

                    throw new RuntimeException("拣货数量与出库单据数量存在差异,请检查销售与出库单");
                }
            }

            for (MovBitTab tab : movBitTabList) {
                // System.out.println("分配货位后movBitTabList\t" + tab.toString());
            }

            List<String> lists = new ArrayList<>(set);
            bitListMapper.deleteInvtyGdsBitList(lists);
            bitListMapper.insertInvtyGdsBitLists(movBitTabList);

//		for (String string : set) {
//			List<MovBitTab> bitTabs = bitListMapper.selectInvtyGdsBitListf(string);
//			if (bitTabs.size() > 0) {
//				List<String>  = new ArrayList<String>();
//				lists.add(pSnglTab.getPickSnglNum());
//			}
//		}
            for (String string : lists) {
                SellOutWhs outWhs = new SellOutWhs();
                outWhs.setOutWhsId(string);
                outWhs.setIsNtChk(1);
                outWhs.setChkr(pSnglTab.getPickPersId());
                // System.out.println(string);
                sellOutWhsServiceImpl.updateSellOutWhsIsNtChk(outWhs);

            }
            resp = BaseJson.returnRespObj("whs/pick_sngl/updatePTabPda", true, "回传成功！", null);

//		pSnglTab.getPickPers();//拣货人
//		pSnglTab.getPickPersId();//拣货人编码

//			if (mList!=null) {
//				if (mPick == 1) {
//					// 如果拣货完成则删除拣货合并表数据
//					// pickSnglMapper.deleteMergePick(pSnglTab.getPickNum());
//					if(pickSnglMapper.selectPickSnglSubTabCount(mergePickSngl)==0) {
//						int pick = pickSnglMapper.updatePTabPda(pSnglTab);
//					}
//					
//				} else {
//				}
//			} else {
//				resp = BaseJson.returnRespObj("whs/pick_sngl/updatePTabPda", false, "该拣货单已删除！", null);
//			}

        } catch (Exception e) {

            try {
                resp = BaseJson.returnRespObj("whs/pick_sngl/updatePTabPda", false, e.getMessage(), null);
            } catch (IOException e1) {


            }

            throw new RuntimeException(e.getMessage());

        }
        return resp;
    }

    // 删除合并拣货单
    @Override
    public String deleteMerPickSngl(List<MergePickSngl> mSnglList) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        try {

            for (MergePickSngl mergePickSngl : mSnglList) {
                // 删除合并拣货单
                pickSnglMapper.deleteMerPickSngl(mergePickSngl.getPickSnglNum());
                PickSnglSubTab pSnglSubTab = new PickSnglSubTab();// 拣货单子表
                pSnglSubTab.setPickSnglNum(mergePickSngl.getPickNum());
                pSnglSubTab.setWhsEncd(mergePickSngl.getWhsEncd());
                pSnglSubTab.setInvtyEncd(mergePickSngl.getInvtyEncd());
                pSnglSubTab.setBatNum(mergePickSngl.getBatNum());
                PickSnglSubTab pTab = pickSnglMapper.selectSellId(pSnglSubTab);
                SellSngl sellSngl = new SellSngl();// 销售单
//				sellSngl.setSellSnglId(pTab.getSellSnglId());
                // 删除拣货单子表
                pickSnglMapper.deletePickSnglTab(pSnglSubTab);

                int count = pickSnglMapper.selectPSubTabByIdCount(mergePickSngl.getPickNum());

                if (count != 0) {

                    int sellZero = pickSnglMapper.updateSellSnglZero(sellSngl);
                    if (sellZero >= 1) {
                        isSuccess = true;
                        message = "删除成功！";
                        resp = BaseJson.returnRespObj("whs/pick_sngl/deleteMerPickSngl", isSuccess, message, null);

                    } else {
                        isSuccess = false;
                        message = "删除失败！";
                        resp = BaseJson.returnRespObj("whs/pick_sngl/deleteMerPickSngl", isSuccess, message, null);

                    }

                } else {

                    int pickSngl = 0;// pickSnglMapper.deletePickSngl(mergePickSngl.getPickNum());
                    int sellZero = pickSnglMapper.updateSellSnglZero(sellSngl);
                    if (pickSngl >= 1 && sellZero >= 1) {
                        isSuccess = true;
                        message = "删除成功！";
                        resp = BaseJson.returnRespObj("whs/pick_sngl/deleteMerPickSngl", isSuccess, message, null);

                    } else {
                        isSuccess = false;
                        message = "删除失败！";
                        resp = BaseJson.returnRespObj("whs/pick_sngl/deleteMerPickSngl", isSuccess, message, null);

                    }

                }

            }

        } catch (Exception e) {

        }
        return resp;
    }

    @Override
    public String deletePickSngl(String pickSngllId) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";

        List<String> lists = getList(pickSngllId);
        if (lists.size() == 0) {
            throw new RuntimeException("请输入单号");
        }
        for (String danHao : lists) {
            PickSnglTab pickSnglTab = pickSnglMapper.selectPick(danHao);
            if (pickSnglTab == null) {
                throw new RuntimeException(danHao + "单号不存在");
            }
            if (pickSnglTab.getIsFinshPick() == 1) {
                throw new RuntimeException(danHao + "单号已拣货完成");
            } else {

//				int asd = pickSnglMapper.selectSellSngl(list,"1");
//				if (list.size() != asd) {
//					throw new RuntimeException("请检查销售单号是否有为符合要求");
//				}
                // 修改销售订单的是否拣货标识
                List<String> list = pickSnglMapper.selectSellSnglTab(danHao);
                List<String> dlList = new ArrayList<String>();
                dlList.add(danHao);
                pickSnglMapper.insertPickSnglDl(dlList);
                pickSnglMapper.insertPickSnglSubTabDl(dlList);

                if (list.size() == 0) {
                    pickSnglMapper.deletePickSngl(danHao);
//					throw new RuntimeException(danHao + );
                    isSuccess = true;
                    message += danHao + "单号不存在对应的销售单,删除成功！\n";
                } else {
                    pickSnglMapper.updateSellSngl(list, "0");
//					if (li != list.size()) {
//						throw new RuntimeException("请检查销售单号是否有为符合要求");
//					}
                    pickSnglMapper.updateSellSnglWhs(list, null);

                    pickSnglMapper.deletePickSngl(danHao);
                }

                try {
                    isSuccess = true;
                    message += danHao + "删除成功！\n";
                    resp = BaseJson.returnRespObj("whs/pick_sngl/deleteMerPickSngl", isSuccess, message, null);
                } catch (IOException e) {


                }

            }

        }

        return resp;
    }

    @Override
    public String updatePTabPC(PickSnglTab pSnglTab) {
        // TODO Auto-generated method stub
        String resp = "";

        try {
            PickSnglTab pickSnglTab = pickSnglMapper.selectPick(pSnglTab.getPickSnglNum());
            if (pickSnglTab == null) {
                return BaseJson.returnRespObj("whs/pick_sngl/updatePTabPda", false, "该拣货单不存在", null);
            }
            if (pickSnglTab.getIsFinshPick() == 1) {
                return BaseJson.returnRespObj("whs/pick_sngl/updatePTabPda", false, "该拣货单已完成", null);
            }
            int pick = pickSnglMapper.updatePTabPda(pSnglTab);

            if (pick == 1) {
                // 如果拣货完成则删除拣货合并表数据
                // pickSnglMapper.deleteMergePick(pSnglTab.getPickNum());
                resp = BaseJson.returnRespObj("whs/pick_sngl/updatePTabPda", true, "拣货完成回传成功！", null);
            } else {
                resp = BaseJson.returnRespObj("whs/pick_sngl/updatePTabPda", false, "拣货完成回传失败！", null);
            }

        } catch (IOException e) {

        }
        return resp;

    }

    @Override
    public String queryListPrint(Map map) {
        String resp = "";
        List<PickSnglTab> pList = pickSnglMapper.selectList(map);
//        int count = pickSnglMapper.selectCount(map);
        pList.add(new PickSnglTab());
        try {
            resp = BaseJson.returnRespObjList("whs/pick_sngl/queryListPrint", true, "查询成功！", null, pList);
        } catch (IOException e) {

        }
        return resp;
    }

}
