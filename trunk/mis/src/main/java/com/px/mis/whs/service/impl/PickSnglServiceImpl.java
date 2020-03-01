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

    // ��ѯ���۵�
    @Override
    public String selectSellById(Map map) throws Exception {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        try {

            // ��ѯ���۵�
//		List<String> list = getList(sellSnglId);
//		List<SellSngl> sList = pickSnglMapper.selectSellById(list);
//		// �޸����۶������Ƿ�����ʶ
//		// ���԰�List<SellSngl> sList�����۵��� ��� ת���� set ȥ�ظ�id Ӧ�ô������������һ�β�ѯ
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
             * // ��Ӽ���� String Number = getOrderNo.getSeqNo("JH", userId);// ��ȡ������
             *
             * if (pickSnglMapper.selectPick(Number) != null) { message = "���" + Number +
             * "�Ѵ��ڣ����������룡"; isSuccess = false; throw new RuntimeException(message); } else
             * { // ������� PickSnglTab pickTab = new PickSnglTab();
             * pickTab.setPickSnglNum(Number); pickTab.setSetupPers(userId);// ��ǰ��¼�û�Ϊ������
             *
             * // ����ӱ� List<PickSnglSubTab> pickList = new ArrayList<PickSnglSubTab>(); for
             * (SellSngl sngl : sList) { PickSnglSubTab pSubTab = new PickSnglSubTab();
             * pSubTab.setPickSnglNum(Number); pSubTab.setSellSnglId(sngl.getSellSnglId());
             * pSubTab.setWhsEncd(sngl.getWhsEncd()); pSubTab.setWhsNm(sngl.getWhsNm());
             * pSubTab.setInvtyEncd(sngl.getInvtyEncd());
             * pSubTab.setInvtyNm(sngl.getInvtyNm());
             * pSubTab.setSpcModel(sngl.getSpcModel());
             * pSubTab.setMeasrCorpId(sngl.getMeasrCorpId());
             * pSubTab.setBarCd(sngl.getCrspdBarCd()); pSubTab.setBatNum(sngl.getBatNum());
             * pSubTab.setPrdcDt(sngl.getPrdcDt());
             * pSubTab.setBaoZhiQi(sngl.getBaoZhiQi());// ������
             * pSubTab.setInvldtnDt(sngl.getInvldtnDt());
             * pSubTab.setGdsBitEncd(sngl.getGdsBitEncd());
             * pSubTab.setSellTyp(sngl.getSellTypId());
             * pSubTab.setBizTyp(sngl.getBizTypId()); pSubTab.setQty(sngl.getQty());
             * pickList.add(pSubTab); }
             *
             * pickSnglMapper.insertPickSngl(pickTab);
             * pickSnglMapper.insertPickSnglSubTab(pickList);
             *
             * message = "�����ɹ���"; isSuccess = true; }
             */

            resp = BaseJson.returnRespList("whs/pick_sngl/selectSellById", isSuccess, message, count, pageNo, pageSize,
                    listNum, pages, sList);
        } catch (Exception e) {

        }

        return resp;
    }

    /**
     * id����list
     *
     * @param id id(����Ѷ��ŷָ�)
     * @return List����
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

    // ��Ӽ����
    @Override
    public String insertPickSngl(String sellSnglId, String userId, String userName,String loginTime) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        try {
            List<String> list = getList(sellSnglId);
            if (list.size() == 0) {
                throw new RuntimeException("���������۵���");
            }
//			int asd = pickSnglMapper.selectSellSngl(list,"0");
//			// System.out.println(asd);
//			if (list.size() != asd) {
//				throw new RuntimeException("�������۵����Ƿ���Ϊ����Ҫ��");
//			}
            // �޸����۶������Ƿ�����ʶ
            int li = pickSnglMapper.updateSellSngl(list, "1");
//			if (li != list.size()) {
//				throw new RuntimeException("�������۵����Ƿ���Ϊ����Ҫ��");
//			}

            // ��Ӽ����
            String Number = getOrderNo.getSeqNo("JH", userId,loginTime);// ��ȡ������
            if (pickSnglMapper.selectPick(Number) != null) {
                message = "���" + Number + "�Ѵ��ڣ����������룡";
                isSuccess = false;
                throw new RuntimeException(message);
            } else {
                pickSnglMapper.updateSellSnglWhs(list, Number);

                PickSnglTab pickTab = new PickSnglTab();
                pickTab.setPickSnglNum(Number);
                pickTab.setSetupPers(userName);// ��ǰ��¼�û�Ϊ������
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

                message = "�����ɹ���";
                isSuccess = true;

            }

//		if (pickSnglMapper.selectPick(pickTab.getPickSnglNum()) != null) {
//			message = "���" + pickTab.getPickSnglNum() + "�Ѵ��ڣ����������룡";
//			isSuccess = false;
//		} else {
//
//			pickSnglMapper.insertPickSngl(pickTab);
//			pickSnglMapper.insertPickSnglSubTab(pickList);
//
//			message = "�����ɹ���";
//			isSuccess = true;
//		}
//

            resp = BaseJson.returnRespObj("whs/pick_sngl/insertPickSngl", isSuccess, message, null);
        } catch (IOException e) {

        }
        return resp;
    }

    // ��������б���ʾ
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
            resp = BaseJson.returnRespList("whs/pick_sngl/queryAllList", true, "��ѯ�ɹ���", count, pageNo, pageSize,
                    listNum, pages, pList);
        } catch (IOException e) {

        }
        return resp;
    }

    // ͨ�����������ݺŲ�ѯ�ӱ���Ϣ
    @Override
    public String selectPSubTabById(String pickSnglNum) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";

        List<PickSnglSubTab> pList = new ArrayList<>();
        PickSnglTab pTab = pickSnglMapper.selectPick(pickSnglNum);
        if (pTab != null) {
            pList = pickSnglMapper.selectPSubTabById(pickSnglNum);
            message = "��ѯ�ɹ���";
        } else {
            isSuccess = false;
            message = "���" + pickSnglNum + "�����ڣ�";
        }

        try {
            resp = BaseJson.returnRespObjList("whs/pick_sngl/selectPSubTabById", isSuccess, message, pTab, pList);
        } catch (IOException e) {

        }
        return resp;
    }

    // �������-���б���ʾ
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
            resp = BaseJson.returnRespList("whs/pick_sngl/queryList", true, "��ѯ�ɹ���", count, pageNo, pageSize, listNum,
                    pages, pList);
        } catch (IOException e) {

        }
        return resp;
    }

    // �ϲ������
    @Override
    public String selectPSubTab(String userId, String pickSnglNum, String whsEncd) {
        String resp = "";
        try {
            // ��ѯ��Ψһ����
            List<PickSnglSubTab> pList = pickSnglMapper.selectPSubTab(pickSnglNum, whsEncd);

            // ��ֵ����ϲ�����
            List<MergePickSngl> mPickList = new ArrayList<>();
            for (PickSnglSubTab pSubTab : pList) {
                MergePickSngl mergePickSngl = new MergePickSngl();
                // mergePickSngl.setPickSnglNum(pSubTab.getPickSnglNum());
//                String Number = getOrderNo.getSeqNo("HB", userId);// ��ȡ������
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

                resp = BaseJson.returnRespObj("whs/pick_sngl/mergePickSingl", true, "�����ɹ�", null);
            } else {
                resp = BaseJson.returnRespObj("whs/pick_sngl/mergePickSingl", false, "����ʧ��", null);
            }
        } catch (Exception e) {

        }
        return resp;
    }

    // PDA ��ʾ���м�����б�-
    @Override
    public String selectAllMerge(String whsEncd) {
        String resp = "";
        List<String> list = getList(whsEncd);

        List<PickSnglTab> pList = pickSnglMapper.selectAllMerge(list);

        try {
            if (pList.size() > 0) {

                resp = BaseJson.returnRespObjList("whs/pick_sngl/selectAllMerge", true, "��ѯ�ɹ���", null, pList);
            } else {
                resp = BaseJson.returnRespObjList("whs/pick_sngl/selectAllMerge", false, "��ѯʧ�ܣ�", null, pList);
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
                resp = BaseJson.returnRespObjList("whs/pick_sngl/selectAllById", true, "��ѯ�ɹ���", null, pList);
            } else {
                resp = BaseJson.returnRespObjList("whs/pick_sngl/selectAllById", false, "��ѯʧ�ܣ�", null, pList);
            }
        } catch (IOException e) {

        }
        return resp;
    }

    // PDA �ش��ӿ� �����ɱ�ʶ ������ʱ��
    @Override
    public String updatePTabPda(PickSnglTab pSnglTab, List<MovBitTab> list) {
        String resp = "";
        try {
            PickSnglTab mList = pickSnglMapper.selectPick(pSnglTab.getPickSnglNum());
            List<MovBitTab> movBitTabList = new ArrayList<MovBitTab>();
            HashSet<String> set = new HashSet<>();

            int mPick = pickSnglMapper.updatePTabPda(pSnglTab);
            if (mPick == 0) {
                throw new RuntimeException("�ѻش����޷��ظ��ش�");

            }
            List<SellOutWhsSub> outWhsSub = pickSnglMapper.selectPickSnglOutTabList(pSnglTab.getPickSnglNum());
            if (outWhsSub.size() == 0) {
                throw new RuntimeException("�������Ӧ��������ɾ��");
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
                            // ��λ��-������

                            BigDecimal diff = tab.getQty().subtract(sub.getQty());
                            bitTab.setQty(sub.getQty());

                            sub.setQty(BigDecimal.ZERO);
                            tab.setQty(diff);
                        } else if (tab.getQty().compareTo(sub.getQty()) == -1) {
                            // ������-��λ��

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

                    throw new RuntimeException("�����������ⵥ���������ڲ���,������������ⵥ");
                }
            }

            for (MovBitTab tab : movBitTabList) {
                // System.out.println("�����λ��movBitTabList\t" + tab.toString());
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
            resp = BaseJson.returnRespObj("whs/pick_sngl/updatePTabPda", true, "�ش��ɹ���", null);

//		pSnglTab.getPickPers();//�����
//		pSnglTab.getPickPersId();//����˱���

//			if (mList!=null) {
//				if (mPick == 1) {
//					// �����������ɾ������ϲ�������
//					// pickSnglMapper.deleteMergePick(pSnglTab.getPickNum());
//					if(pickSnglMapper.selectPickSnglSubTabCount(mergePickSngl)==0) {
//						int pick = pickSnglMapper.updatePTabPda(pSnglTab);
//					}
//					
//				} else {
//				}
//			} else {
//				resp = BaseJson.returnRespObj("whs/pick_sngl/updatePTabPda", false, "�ü������ɾ����", null);
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

    // ɾ���ϲ������
    @Override
    public String deleteMerPickSngl(List<MergePickSngl> mSnglList) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        try {

            for (MergePickSngl mergePickSngl : mSnglList) {
                // ɾ���ϲ������
                pickSnglMapper.deleteMerPickSngl(mergePickSngl.getPickSnglNum());
                PickSnglSubTab pSnglSubTab = new PickSnglSubTab();// ������ӱ�
                pSnglSubTab.setPickSnglNum(mergePickSngl.getPickNum());
                pSnglSubTab.setWhsEncd(mergePickSngl.getWhsEncd());
                pSnglSubTab.setInvtyEncd(mergePickSngl.getInvtyEncd());
                pSnglSubTab.setBatNum(mergePickSngl.getBatNum());
                PickSnglSubTab pTab = pickSnglMapper.selectSellId(pSnglSubTab);
                SellSngl sellSngl = new SellSngl();// ���۵�
//				sellSngl.setSellSnglId(pTab.getSellSnglId());
                // ɾ��������ӱ�
                pickSnglMapper.deletePickSnglTab(pSnglSubTab);

                int count = pickSnglMapper.selectPSubTabByIdCount(mergePickSngl.getPickNum());

                if (count != 0) {

                    int sellZero = pickSnglMapper.updateSellSnglZero(sellSngl);
                    if (sellZero >= 1) {
                        isSuccess = true;
                        message = "ɾ���ɹ���";
                        resp = BaseJson.returnRespObj("whs/pick_sngl/deleteMerPickSngl", isSuccess, message, null);

                    } else {
                        isSuccess = false;
                        message = "ɾ��ʧ�ܣ�";
                        resp = BaseJson.returnRespObj("whs/pick_sngl/deleteMerPickSngl", isSuccess, message, null);

                    }

                } else {

                    int pickSngl = 0;// pickSnglMapper.deletePickSngl(mergePickSngl.getPickNum());
                    int sellZero = pickSnglMapper.updateSellSnglZero(sellSngl);
                    if (pickSngl >= 1 && sellZero >= 1) {
                        isSuccess = true;
                        message = "ɾ���ɹ���";
                        resp = BaseJson.returnRespObj("whs/pick_sngl/deleteMerPickSngl", isSuccess, message, null);

                    } else {
                        isSuccess = false;
                        message = "ɾ��ʧ�ܣ�";
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
            throw new RuntimeException("�����뵥��");
        }
        for (String danHao : lists) {
            PickSnglTab pickSnglTab = pickSnglMapper.selectPick(danHao);
            if (pickSnglTab == null) {
                throw new RuntimeException(danHao + "���Ų�����");
            }
            if (pickSnglTab.getIsFinshPick() == 1) {
                throw new RuntimeException(danHao + "�����Ѽ�����");
            } else {

//				int asd = pickSnglMapper.selectSellSngl(list,"1");
//				if (list.size() != asd) {
//					throw new RuntimeException("�������۵����Ƿ���Ϊ����Ҫ��");
//				}
                // �޸����۶������Ƿ�����ʶ
                List<String> list = pickSnglMapper.selectSellSnglTab(danHao);
                List<String> dlList = new ArrayList<String>();
                dlList.add(danHao);
                pickSnglMapper.insertPickSnglDl(dlList);
                pickSnglMapper.insertPickSnglSubTabDl(dlList);

                if (list.size() == 0) {
                    pickSnglMapper.deletePickSngl(danHao);
//					throw new RuntimeException(danHao + );
                    isSuccess = true;
                    message += danHao + "���Ų����ڶ�Ӧ�����۵�,ɾ���ɹ���\n";
                } else {
                    pickSnglMapper.updateSellSngl(list, "0");
//					if (li != list.size()) {
//						throw new RuntimeException("�������۵����Ƿ���Ϊ����Ҫ��");
//					}
                    pickSnglMapper.updateSellSnglWhs(list, null);

                    pickSnglMapper.deletePickSngl(danHao);
                }

                try {
                    isSuccess = true;
                    message += danHao + "ɾ���ɹ���\n";
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
                return BaseJson.returnRespObj("whs/pick_sngl/updatePTabPda", false, "�ü����������", null);
            }
            if (pickSnglTab.getIsFinshPick() == 1) {
                return BaseJson.returnRespObj("whs/pick_sngl/updatePTabPda", false, "�ü���������", null);
            }
            int pick = pickSnglMapper.updatePTabPda(pSnglTab);

            if (pick == 1) {
                // �����������ɾ������ϲ�������
                // pickSnglMapper.deleteMergePick(pSnglTab.getPickNum());
                resp = BaseJson.returnRespObj("whs/pick_sngl/updatePTabPda", true, "�����ɻش��ɹ���", null);
            } else {
                resp = BaseJson.returnRespObj("whs/pick_sngl/updatePTabPda", false, "�����ɻش�ʧ�ܣ�", null);
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
            resp = BaseJson.returnRespObjList("whs/pick_sngl/queryListPrint", true, "��ѯ�ɹ���", null, pList);
        } catch (IOException e) {

        }
        return resp;
    }

}
