package com.px.mis.whs.service.impl;

import java.io.IOException;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.px.mis.purc.dao.IntoWhsDao;
import com.px.mis.purc.dao.IntoWhsSubDao;
import com.px.mis.purc.dao.SellOutWhsDao;
import com.px.mis.purc.dao.SellOutWhsSubDao;
import com.px.mis.purc.dao.ToGdsSnglDao;
import com.px.mis.purc.dao.ToGdsSnglSubDao;
import com.px.mis.purc.entity.IntoWhs;
import com.px.mis.purc.entity.IntoWhsSub;
import com.px.mis.purc.entity.SellOutWhs;
import com.px.mis.purc.entity.SellOutWhsSub;
import com.px.mis.purc.entity.ToGdsSngl;
import com.px.mis.purc.entity.ToGdsSnglSub;
import com.px.mis.purc.service.impl.PurcIntoWhsServiceImpl;
import com.px.mis.purc.service.impl.SellOutWhsServiceImpl;
import com.px.mis.purc.service.impl.ToGdsSnglServiceImpl;
import com.px.mis.purc.util.CalcAmt;
import com.px.mis.util.BaseJson;
import com.px.mis.util.GetOrderNo;
import com.px.mis.whs.dao.IntoWhsMapper;
import com.px.mis.whs.dao.InvtyGdsBitListMapper;
import com.px.mis.whs.dao.InvtyNumMapper;
import com.px.mis.whs.dao.OthOutIntoWhsMapper;
import com.px.mis.whs.entity.GdsBit;
import com.px.mis.whs.entity.InvtyTab;
import com.px.mis.whs.entity.MovBitTab;
import com.px.mis.whs.entity.RefuseReason;
import com.px.mis.whs.service.IntoWhsService;

@Service
@Transactional
public class IntoWhsServiceImpl implements IntoWhsService {

    @Autowired
    IntoWhsMapper intoWhsMapper;

    @Autowired
    OthOutIntoWhsMapper othOutIntoWhsMapper;

    @Autowired
    InvtyNumMapper invtyNumMapper;

    // ������
    @Autowired
    ToGdsSnglDao toGdsSnglDao;
    @Autowired
    ToGdsSnglSubDao toGdsSnglSubDao;
    // ��ⵥ
    @Autowired
    IntoWhsDao intoWhsDao;
    @Autowired
    IntoWhsSubDao intoWhsSubDao;
    // ������
    @Autowired
    GetOrderNo getOrderNo;
    // ���۳��ⵥ
    @Autowired
    SellOutWhsDao sellOutWhsDao;
    @Autowired
    SellOutWhsSubDao sellOutWhsSubDao;
    @Autowired
    InvtyGdsBitListMapper bitListMapper;
    @Autowired
    ToGdsSnglServiceImpl toGdsSnglServiceImpl;

    @Autowired
    PurcIntoWhsServiceImpl purcIntoWhsServiceImpl;
    @Autowired
    SellOutWhsServiceImpl sellOutWhsServiceImpl;

    // ��ѯ���е�����
    @Override
    public String selectToGdsSnglList(String whsEncd) {
        String resp = "";
        List<String> list = getList(whsEncd);

        List<ToGdsSngl> tGdsSngl = intoWhsMapper.selectToGdsSnglList(list);

        // **************�Ƽ�����λ*****************

        for (ToGdsSngl toGdsSngl : tGdsSngl) {
            List<ToGdsSnglSub> tSnglSubsList = toGdsSngl.getToGdsSnglSub();
            for (ToGdsSnglSub tSnglSub : tSnglSubsList) {

                MovBitTab movBitTab = new MovBitTab();
                movBitTab.setWhsEncd(tSnglSub.getWhsEncd());
                movBitTab.setInvtyEncd(tSnglSub.getInvtyEncd());
                movBitTab.setBatNum(tSnglSub.getBatNum());
                List<MovBitTab> mBitTab = intoWhsMapper.selectIntogBit(movBitTab);
                String s = "";
                for (MovBitTab mTab : mBitTab) {
                    s += mTab.getGdsBitEncd() + ",";
                }
                if (s.length() > 2) {
                    s.substring(s.length() - 2);
                }
                tSnglSub.setGdsBitEncd(s);
            }
        }
        // *******************************

        try {
            if (tGdsSngl.size() > 0) {
                resp = BaseJson.returnRespObjList("/whs/pda_into_whs/selectToGdsSnglList", true, "��ѯ�ɹ���", null,
                        tGdsSngl);
            } else {
                resp = BaseJson.returnRespObjList("/whs/pda_into_whs/selectToGdsSnglList", false, "��������", null,
                        tGdsSngl);
            }

        } catch (IOException e) {

        }
        return resp;
    }

    // �����������յ���Ϣ
    @Override
    public String insertToGdsSngl(String userId, ToGdsSngl toGdsSngl, List<ToGdsSnglSub> toGdsSnglSubList) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";

        String number = null;
        try {
            number = getOrderNo.getSeqNo("DHD", userId,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        } catch (Exception e1) {

            throw new RuntimeException("���Ż�ȡʧ��");
        } // ��ȡ������

        if (intoWhsMapper.selectToGdsSnglByToGdsSnglId(number) != null) {
            message = "���" + number + "�Ѵ��ڣ����������룡";
            isSuccess = false;
        } else {

            toGdsSngl.setToGdsSnglId(number);// ������
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String toGdsSnglDt = sdf.format(date);
            toGdsSngl.setToGdsSnglDt(toGdsSnglDt);// ���յ�����

            toGdsSnglDao.insertToGdsSngl(toGdsSngl);
            toGdsSnglSubDao.insertToGdsSnglSub(toGdsSnglSubList);

            message = "�����ɹ���";
            isSuccess = true;
        }

        try {
            resp = BaseJson.returnRespObj("/whs/pda_into_whs/addToGdsSngl", true, message, null);
        } catch (IOException e) {

        }
        return resp;
    }

    /*
     * //������ⵥ
     *
     * @Override public String addIntoWhs(String userId,String userName, IntoWhs
     * intoWhs, List<IntoWhsSub> intoWhsSubList, List<InvtyTab>
     * invtyTab,List<MovBitTab> movBitTab, ToGdsSngl toGdsSngl,List<ToGdsSnglSub>
     * tSnglSubList) throws Exception{
     *
     * String message=""; Boolean isSuccess=true; String resp="";
     *
     * String number = null; try { number = getOrderNo.getSeqNo("RK", userId); }
     * catch (Exception e) { //�˴���Ҫ�������׳��쳣�����ش������� throw new Exception("�޷���ȡ����ⵥ�ţ�"
     * + e.getMessage()); }//��ȡ������
     *
     * for(IntoWhsSub intoWhsSub:intoWhsSubList) {
     *
     * if(intoWhsSub.getReturnQty().compareTo(BigDecimal.ZERO) >0) {
     * //������ⵥ�;��յ�--------------------- //1.��ⵥ //������ⵥ
     * intoWhs.setIntoWhsSnglId(number);//���ݺ� Date date=new Date(); SimpleDateFormat
     * sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); String intoWhsDt =
     * sdf.format(date); intoWhs.setIntoWhsDt(intoWhsDt);//�������
     * intoWhs.setRecvSendCateId("1");//�շ������� �ɹ����
     * intoWhs.setOutIntoWhsTypId("9");//��������������� intoWhs.setAccNum(userId);//ҵ��Ա����
     * intoWhs.setUserName(userName);//������ intoWhs.setSetupPers(userName);//������
     *
     * //���������ӱ���Ϣ intoWhsDao.insertIntoWhs(intoWhs);
     * intoWhsSub.setIntoWhsSnglId(intoWhs.getIntoWhsSnglId());//��ⵥ�ݺ�
     * intoWhsSub.setIsNtRtnGoods(1); BigDecimal dQty=BigDecimal.ZERO; for(MovBitTab
     * mTab:movBitTab) { dQty = dQty.add(mTab.getQty()); } intoWhsSub.setQty(dQty);
     * intoWhsSubDao.insertIntoWhsSub(intoWhsSubList);
     *
     * //(����ʱ���������������������ڻ�λ�����ӣ����û����������)
     * if(invtyNumMapper.selectAllMbit(movBitTab)!=null) {
     *
     * invtyNumMapper.updateMovbitTabJia(movBitTab);//�޸���λ��(����) }else { //������λ�� Ѱ������
     * for(MovBitTab mTab:movBitTab) { List<GdsBit>
     * gList=invtyNumMapper.selectRegn(mTab.getGdsBitEncd()); for(GdsBit
     * gdsBit:gList) { mTab.setRegnEncd(gdsBit.getRegnEncd());//����
     * movBitTab.add(mTab); } } invtyNumMapper.insertMovBitTab(movBitTab);//������λ�� }
     *
     *
     * //2.���յ� String number1 = null; try { number1 = getOrderNo.getSeqNo("DHD",
     * userId); } catch (Exception e1) { }//��ȡ������
     *
     *
     * //�������յ��� toGdsSngl.setToGdsSnglId(number1);//������ Date date1=new Date();
     * SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); String
     * toGdsSnglDt = sdf1.format(date1);
     * toGdsSngl.setToGdsSnglDt(toGdsSnglDt);//���յ�����
     * toGdsSngl.setAccNum(userId);//ҵ��Ա��� toGdsSngl.setUserName(userName);//ҵ��Ա����
     * toGdsSngl.setSetupPers(userName);//������
     *
     * //�������յ��ӱ� for(ToGdsSnglSub tSnglSub:tSnglSubList) {
     * tSnglSub.setToGdsSnglId(toGdsSngl.getToGdsSnglId());
     * tSnglSub.setMemo(tSnglSub.getReturnMemo());//��ע BigDecimal
     * rQty=tSnglSub.getReturnQty(); BigDecimal rFu=new BigDecimal("-1"); double
     * cheng=rQty.multiply(rFu).doubleValue(); BigDecimal returnQty=new
     * BigDecimal(cheng); tSnglSub.setQty(returnQty);//�������� //δ˰���
     * tSnglSub.setCntnTaxUprc(CalcAmt.noTaxAmt(tSnglSub.getNoTaxUprc(),
     * tSnglSub.getReturnQty())); //��˰����
     * tSnglSub.setCntnTaxUprc(CalcAmt.cntnTaxUprc(tSnglSub.getNoTaxUprc(),
     * tSnglSub.getReturnQty(), tSnglSub.getTaxRate())); //��˰���(��˰�ϼ�)
     * tSnglSub.setPrcTaxSum(CalcAmt.prcTaxSum(tSnglSub.getNoTaxUprc(),
     * tSnglSub.getReturnQty(), tSnglSub.getTaxRate()));
     * tSnglSub.setIsNtRtnGoods(0); }
     *
     * toGdsSnglDao.insertToGdsSngl(toGdsSngl);
     * toGdsSnglSubDao.insertToGdsSnglSub(tSnglSubList);
     *
     *
     * message="�����ɹ���"; isSuccess=true;
     *
     * try { resp=BaseJson.returnRespObj("/whs/pda_into_whs/addIntoWhs", isSuccess,
     * message, null); } catch (IOException e) { throw new RuntimeException(); }
     *
     * }else { //��ⵥ-------------
     *
     * //��ⵥ���� intoWhs.setIntoWhsSnglId(number);//���ݺ� Date date=new Date();
     * SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); String
     * intoWhsDt = sdf.format(date); intoWhs.setIntoWhsDt(intoWhsDt);//�������
     * intoWhs.setRecvSendCateId("1");//�շ������� �ɹ����
     * intoWhs.setOutIntoWhsTypId("9");//���������������
     * intoWhs.setUserName(userName);//������ intoWhs.setSetupPers(userName);//������
     *
     * //��ⵥ�ӱ� intoWhsSub.setIntoWhsSnglId(intoWhs.getIntoWhsSnglId());//��ⵥ�ݺ�
     * intoWhsSub.setIsNtRtnGoods(1); double dQty=0; for(MovBitTab mTab:movBitTab) {
     * dQty += mTab.getQty().doubleValue(); } intoWhsSub.setQty(new
     * BigDecimal(dQty));
     *
     * //���������ӱ���Ϣ intoWhsDao.insertIntoWhs(intoWhs);
     * intoWhsSubDao.insertIntoWhsSub(intoWhsSubList);
     *
     * message="�����ɹ���"; isSuccess=true;
     *
     *
     * //(����ʱ���������������������ڻ�λ�����ӣ����û����������)
     * if(invtyNumMapper.selectAllMbit(movBitTab)!=null) {
     *
     * invtyNumMapper.updateMovbitTabJia(movBitTab);//�޸���λ��(����) }else { //������λ�� Ѱ������
     * for(MovBitTab mTab:movBitTab) { List<GdsBit>
     * gList=invtyNumMapper.selectRegn(mTab.getGdsBitEncd()); for(GdsBit
     * gdsBit:gList) { mTab.setRegnEncd(gdsBit.getRegnEncd());//����
     * movBitTab.add(mTab); } } invtyNumMapper.insertMovBitTab(movBitTab);//������λ�� }
     *
     *
     * try { resp=BaseJson.returnRespObj("/whs/pda_into_whs/addIntoWhs", isSuccess,
     * message, null); } catch (IOException e) { }
     *
     *
     * } }
     *
     *
     * return resp; }
     */

    /**
     * ������ⵥ δ�޸Ŀ��
     */
    @Override
    public String addIntoWhs(String userId, String userName, IntoWhs intoWhs, List<IntoWhsSub> intoWhsSubList,
                             List<InvtyTab> invtyTab, List<MovBitTab> movBitTab, ToGdsSngl toGdsSngl, List<ToGdsSnglSub> tSnglSubList)
            throws Exception {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        try {
            // ��������
            ToGdsSngl toGdsSngls = null;
            // ���յ���
            String number = null;
            // �����ӱ�
            List<ToGdsSnglSub> tSnglSubLists = new ArrayList<ToGdsSnglSub>();
            // �����
            List<IntoWhsSub> intoWhsSubs = new ArrayList<IntoWhsSub>();
            // �����
//			intoWhs=new IntoWhs();

            String numbers = null;
            toGdsSngl = toGdsSnglDao.selectToGdsSnglById(toGdsSngl.getToGdsSnglId());

            for (ToGdsSnglSub gdsSnglSubs : tSnglSubList) {
                ToGdsSnglSub snglSub = intoWhsMapper.selectToGdsByOrdrNumKey(gdsSnglSubs.getOrdrNum());
                if (snglSub != null) {
                    snglSub.setQty(gdsSnglSubs.getQty());//ʵ�ʵ�����

                    IntoWhsSub intoWhsSub = new IntoWhsSub();
                    BeanUtils.copyProperties(snglSub, intoWhsSub);
                    intoWhsSub.setOrdrNum(null);
                    intoWhsSub.setToOrdrNum(gdsSnglSubs.getOrdrNum());
                    intoWhsSub.setCrspdBarCd(gdsSnglSubs.getCrspdBarCd());
                    intoWhsSub.setPursOrdrNum(snglSub.getToOrdrNum());
                    if (gdsSnglSubs.getIntlBat() != null && !gdsSnglSubs.getIntlBat().equals("")) {
                        intoWhsSub.setIntlBat(gdsSnglSubs.getIntlBat());
                    }
                    intoWhsSub.setPursToGdsSnglSubTabInd(snglSub.getOrdrNum().toString());
                    intoWhsSub.setReturnMemo(null);
//                    if (gdsSnglSubs.getReturnQty() != null
//                            && gdsSnglSubs.getReturnQty().abs().compareTo(snglSub.getQty()) == 0) {
//                        if (number == null) {
//                            toGdsSngls = new ToGdsSngl();
//                            number = aInstToGds(toGdsSngls, userId, userName, toGdsSngl);
//                        }
//                        snglSub.setToGdsSnglId(number);
//                        snglSub.setQty(BigDecimal.ZERO.subtract(gdsSnglSubs.getReturnQty().abs()));// ��������
//                        snglSub.setMemo(gdsSnglSubs.getMemo());
//                        snglSub.setNoTaxAmt(snglSub.getNoTaxUprc().multiply(snglSub.getQty()));// ��˰���
//                        snglSub.setPrcTaxSum(snglSub.getCntnTaxUprc().multiply(snglSub.getQty()));// ��˰�ϼ�
//                        snglSub.setTaxAmt(snglSub.getNoTaxAmt().multiply(snglSub.getTaxRate()));// ˰��
//                        snglSub.setBxQty(BigDecimal.ZERO.subtract(snglSub.getBxQty()));// ����
//                        snglSub.setIsNtRtnGoods(0);
//                        snglSub.setOrdrNum(null);
//
//                        snglSub.setToOrdrNum(gdsSnglSubs.getOrdrNum());
//                        snglSub.setUnIntoWhsQty(BigDecimal.ZERO);
//                        snglSub.setReturnQty(BigDecimal.ZERO);
//
//                        tSnglSubLists.add(snglSub);
//                    } else

                        if (gdsSnglSubs.getReturnQty() != null
                            && gdsSnglSubs.getReturnQty().abs().compareTo(BigDecimal.ZERO) != 0) {
                        if (number == null) {
                            toGdsSngls = new ToGdsSngl();
                            number = aInstToGds(toGdsSngls, userId, userName, toGdsSngl);
                        }

                        // �о�����ϸ
                        intoWhsSub.setQty(snglSub.getQty());
                        intoWhsSub.setNoTaxAmt(snglSub.getNoTaxUprc().multiply(intoWhsSub.getQty()));// ��˰���
                        intoWhsSub.setPrcTaxSum(snglSub.getCntnTaxUprc().multiply(intoWhsSub.getQty()));// ��˰�ϼ�
                        intoWhsSub.setTaxAmt(snglSub.getNoTaxAmt().multiply(snglSub.getTaxRate()));// ˰��
                        intoWhsSub.setBxQty(intoWhsSub.getQty().divide(
                                (snglSub.getBxRule() == null || snglSub.getBxRule().compareTo(BigDecimal.ZERO) == 0)
                                        ? new BigDecimal(1)
                                        : snglSub.getBxRule(),
                                2, BigDecimal.ROUND_HALF_UP));// ����

                        intoWhsSub.setUnBllgAmt(intoWhsSub.getPrcTaxSum());
                        intoWhsSub.setUnBllgQty(intoWhsSub.getQty());
                        intoWhsSub.setUnBllgUprc(intoWhsSub.getCntnTaxUprc());
                        intoWhsSub.setReturnQty(intoWhsSub.getQty());
                        intoWhsSubs.add(intoWhsSub);

                        // ������
                        snglSub.setToGdsSnglId(number);
                        snglSub.setQty(BigDecimal.ZERO.subtract(gdsSnglSubs.getReturnQty().abs()));// ��������
                        snglSub.setMemo(gdsSnglSubs.getMemo());
                        snglSub.setNoTaxAmt(snglSub.getNoTaxUprc().multiply(snglSub.getQty()));// ��˰���
                        snglSub.setPrcTaxSum(snglSub.getCntnTaxUprc().multiply(snglSub.getQty()));// ��˰�ϼ�
                        snglSub.setTaxAmt(snglSub.getNoTaxAmt().multiply(snglSub.getTaxRate()));// ˰��
                        snglSub.setBxQty(snglSub.getQty().divide(
                                (snglSub.getBxRule() == null || snglSub.getBxRule().compareTo(BigDecimal.ZERO) == 0)
                                        ? new BigDecimal(1)
                                        : snglSub.getBxRule(),
                                2, BigDecimal.ROUND_HALF_UP));// ����
                        snglSub.setIsNtRtnGoods(0);
                        snglSub.setOrdrNum(null);
                        snglSub.setToOrdrNum(gdsSnglSubs.getOrdrNum());
                        snglSub.setUnIntoWhsQty(BigDecimal.ZERO);
                        snglSub.setReturnQty(BigDecimal.ZERO);

                        tSnglSubLists.add(snglSub);

                    } else {

                        // ʵ�ʵ�����
                        intoWhsSub.setQty(snglSub.getQty());
                        intoWhsSub.setNoTaxAmt(snglSub.getNoTaxUprc().multiply(intoWhsSub.getQty()));// ��˰���
                        intoWhsSub.setPrcTaxSum(snglSub.getCntnTaxUprc().multiply(intoWhsSub.getQty()));// ��˰�ϼ�
                        intoWhsSub.setTaxAmt(snglSub.getNoTaxAmt().multiply(snglSub.getTaxRate()));// ˰��
                        intoWhsSub.setBxQty(intoWhsSub.getQty().divide(
                                (snglSub.getBxRule() == null || snglSub.getBxRule().compareTo(BigDecimal.ZERO) == 0)
                                        ? new BigDecimal(1)
                                        : snglSub.getBxRule(),
                                2, BigDecimal.ROUND_HALF_UP));// ����

                        intoWhsSub.setUnBllgAmt(intoWhsSub.getPrcTaxSum());
                        intoWhsSub.setUnBllgQty(intoWhsSub.getQty());
                        intoWhsSub.setUnBllgUprc(intoWhsSub.getCntnTaxUprc());
                        intoWhsSub.setReturnQty(intoWhsSub.getQty());
                        intoWhsSubs.add(intoWhsSub);
                    }
                } else {
                    throw new RuntimeException("������������ϸ��Ŵ���");
                }

            }

            // �����
            List<String> lists = new ArrayList<String>();
            // �ֲֿ�
            Map<String, List<IntoWhsSub>> intoWhsMap = new HashMap<String, List<IntoWhsSub>>();

            for (IntoWhsSub into : intoWhsSubs) {
                String key = into.getWhsEncd();
                List<IntoWhsSub> intoWhslist;
                if (intoWhsMap.containsKey(key)) {
                    intoWhslist = intoWhsMap.get(key);
                } else {
                    intoWhslist = new ArrayList<IntoWhsSub>();
                }
                intoWhslist.add(into);
                intoWhsMap.put(key, intoWhslist);
            }

            List<List<IntoWhsSub>> intoLists = new ArrayList<List<IntoWhsSub>>(intoWhsMap.values());

            List<IntoWhs> IntoWhslist = new ArrayList<IntoWhs>();

            for (List<IntoWhsSub> intoList : intoLists) {
                if (intoList.size() > 0) {
                    // System.out.println("\t\t\t�ֿ�" + intoList.get(0).getWhsEncd());

                    // ��ⵥ��
                    intoWhs = new IntoWhs();
                    numbers = aInstIntoWhs(intoWhs, userId, userName, toGdsSngl);
                    IntoWhslist.add(intoWhs);

                    for (IntoWhsSub intoWhsSub : intoList) {
                        intoWhsSub.setIntoWhsSnglId(numbers);
                    }
                    // System.out.println("\t ����ǰ" + intoList.size());

                    intoWhsSubDao.insertIntoWhsSub(intoList);
                    // System.out.println("\t �����" + intoList.size());

                    for (IntoWhsSub intoWhsSub : intoList) {

                        // System.out.println("\t ���" + intoWhsSub.getOrdrNum());

                        for (MovBitTab mov : movBitTab) {
                            // System.out.println(mov.getSerialNum() + "" + intoWhsSub.getCrspdBarCd());
                            if (mov.getSerialNum().equals(intoWhsSub.getCrspdBarCd())) {
                                mov.setOrderNum(numbers);
                                mov.setSerialNum(intoWhsSub.getOrdrNum().toString());
                                lists.add(mov.getOrderNum());
                            }

                        }
                    }
                }

            }

            // �������յ���
            if (tSnglSubLists.size() > 0) {
                toGdsSnglSubDao.insertToGdsSnglSub(tSnglSubLists);
            }
            if (toGdsSngls != null) {
                toGdsSnglServiceImpl.updateReturnToGdsSnglIsNtChkOK(toGdsSngls);
            }

            if (movBitTab.size() > 0) {
                bitListMapper.deleteInvtyGdsBitList(lists);
                bitListMapper.insertInvtyGdsBitLists(movBitTab);
            }
            message = "�ɹ�";

            // ������-�������
//			ToGdsSngl toGdsSngla = new ToGdsSngl();
//			toGdsSngla.setToGdsSnglId(toGdsSngl.getToGdsSnglId());
//			toGdsSngla.setDealStat("�������");
            intoWhsMapper.updateTGdsGngl(tSnglSubList);

            for (IntoWhs whs : IntoWhslist) {
                whs.setChkr(userId);
                whs.setIsNtChk(1);
                purcIntoWhsServiceImpl.updateIntoWhsIsNtChk(whs);
            }

            if (true) {
                try {
                    resp = BaseJson.returnRespObj("whs/pda_into_whs/addIntoWhs", isSuccess, message, null);
                    return resp;
                } catch (IOException e) {


                }
            }

            // �����
            List<ToGdsSnglSub> toGdsSnglSubs = toGdsSnglSubDao
                    .selectToGdsSnglSubByToGdsSnglId(toGdsSngl.getToGdsSnglId());
            for (ToGdsSnglSub gdsSnglSub : toGdsSnglSubs) {
                IntoWhsSub intoWhsSub = new IntoWhsSub();
                BeanUtils.copyProperties(gdsSnglSub, intoWhsSub);
                intoWhsSub.setIntoWhsSnglId(numbers);
                intoWhsSub.setOrdrNum(null);

                for (ToGdsSnglSub gdsSnglSubs : tSnglSubList) {

                    ToGdsSnglSub snglSub = intoWhsMapper.selectToGdsByOrdrNumKey(gdsSnglSubs.getOrdrNum());
                    if (snglSub != null) {
                        if (number == null) {
                            toGdsSngls = new ToGdsSngl();
                            number = aInstToGds(toGdsSngls, userId, userName, toGdsSngl);
                        }

                        intoWhsSub.setQty(snglSub.getQty().subtract(gdsSnglSubs.getQty().abs()));
                        intoWhsSub.setNoTaxAmt(snglSub.getNoTaxUprc().multiply(intoWhsSub.getQty()));// ��˰���
                        intoWhsSub.setPrcTaxSum(snglSub.getCntnTaxUprc().multiply(intoWhsSub.getQty()));// ��˰�ϼ�
                        intoWhsSub.setTaxAmt(snglSub.getNoTaxAmt().multiply(snglSub.getTaxRate()));// ˰��

                        snglSub.setToGdsSnglId(number);
                        snglSub.setQty(BigDecimal.ZERO.subtract(gdsSnglSubs.getQty().abs()));// ��������
                        snglSub.setMemo(gdsSnglSubs.getMemo());
                        snglSub.setNoTaxAmt(snglSub.getNoTaxUprc().multiply(snglSub.getQty()));// ��˰���
                        snglSub.setPrcTaxSum(snglSub.getCntnTaxUprc().multiply(snglSub.getQty()));// ��˰�ϼ�
                        snglSub.setTaxAmt(snglSub.getNoTaxAmt().multiply(snglSub.getTaxRate()));// ˰��

                        snglSub.setIsNtRtnGoods(0);
                        snglSub.setOrdrNum(null);
                        tSnglSubLists.add(snglSub);
                        intoWhsSubs.add(intoWhsSub);
                    } else {
                        // System.out.println("���ݺ���Ų�����");
                    }
                }
            }

            if (intoWhsSubs.size() > 0) {
                intoWhsSubDao.insertIntoWhsSub(intoWhsSubs);
            }
            // �������յ���
            if (tSnglSubLists.size() > 0) {
                toGdsSnglSubDao.insertToGdsSnglSub(tSnglSubLists);
            }
            if (toGdsSngls != null) {
                toGdsSnglServiceImpl.updateReturnToGdsSnglIsNtChkOK(toGdsSngls);
            }

            String number21 = null;

            for (IntoWhsSub intoWhsSub : intoWhsSubList) {
                // ��ⵥ�ӱ�
                InstIntoWhs(number, intoWhsSub);
                if ((intoWhsSub.getReturnQty() == null ? BigDecimal.ZERO : intoWhsSub.getReturnQty())
                        .compareTo(BigDecimal.ZERO) > 0) {
                    // 3.���յ�
                    if (number21 == null) {
                        number21 = aInstToGds(toGdsSngl, userId, userName, toGdsSngl);
                    }
                }

            }
            InstMovBitTab(movBitTab);
            // System.out.println(number21);
            if (number21 != null) {
                InstToGds(toGdsSngl, number21, tSnglSubList);
            }

//				for (IntoWhsSub intoWhsSub : intoWhsSubList) {
//					if (intoWhsSub.getReturnQty().compareTo(BigDecimal.ZERO) > 0) {
//						// ������ⵥ�;��յ�---------------------
//						// ��ⵥ�ӱ�
//						InstIntoWhs(number, intoWhsSub);
//						// 2.��λ��
//						InstMovBitTab(movBitTab);
//						// 3.���յ�
//						InstToGds(, tSnglSubList, userId, userName);
//					} else {
//						// ��ⵥ-------------
//						// ��ⵥ����
//						InstIntoWhs(number, intoWhsSub);
//						// (����ʱ���������������������ڻ�λ�����ӣ����û����������)
//						InstMovBitTab(movBitTab);
//					}
//				}
        } catch (Exception e) {

            throw new RuntimeException(e.getMessage());
        }
        return userName;
    }

    private String aInstIntoWhs(IntoWhs intoWhs, String userId, String userName, ToGdsSngl toGdsSngl) throws Exception {
        String number = null;
        try {
            number = getOrderNo.getSeqNo("RK", userId,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        } catch (Exception e) {
            // �˴���Ҫ�������׳��쳣�����ش�������

            throw new Exception("�޷���ȡ����ⵥ�ţ�" + e.getMessage());
        } // ��ȡ������
        // ������ⵥ
        // System.out.println(number);
        intoWhs.setIntoWhsSnglId(number);// ���ݺ�
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String intoWhsDt = sdf.format(date);
        intoWhs.setIntoWhsDt(intoWhsDt);// �������
        intoWhs.setPursTypId(toGdsSngl.getPursTypId());// �ɹ����ͱ��
        intoWhs.setProvrId(toGdsSngl.getProvrId());// ��Ӧ�̱��
        intoWhs.setRecvSendCateId("1");// �շ������� �ɹ����
        intoWhs.setOutIntoWhsTypId("9");// ���������������
        intoWhs.setAccNum(userId);// ҵ��Ա����
        intoWhs.setUserName(userName);// ������
        intoWhs.setSetupPers(userName);// ������
        intoWhs.setPursOrdrId(toGdsSngl.getPursOrdrId());// �ɹ��������
        intoWhs.setToGdsSnglId(toGdsSngl.getToGdsSnglId());// ���������
        intoWhs.setProvrOrdrNum(toGdsSngl.getProvrOrdrNum());// ��Ӧ�̶�����
        intoWhs.setFormTypEncd("004");
        intoWhs.setDeptId(toGdsSngl.getDeptId());// ���ű��
        intoWhs.setIsNtRtnGood(0);// �Ƿ����˻�
        intoWhs.setToFormTypEncd(toGdsSngl.getFormTypEncd());
//		intoWhs.setExaRep(toGdsSngl.getexaRep);//������߱���
        // ����������Ϣ
        intoWhsDao.insertIntoWhs(intoWhs);

        return number;

    }

    private void InstIntoWhs(String number, IntoWhsSub intoWhsSub) throws Exception {

        intoWhsSub.setIntoWhsSnglId(number);// ��ⵥ�ݺ�
        intoWhsSub.setIsNtRtnGoods(1);
//			BigDecimal dQty = BigDecimal.ZERO;
//			for (MovBitTab mTab : movBitTab) {
//				dQty = dQty.add(mTab.getQty());
//			}
        // System.out.println("intoWhsSub.getQty(dQty);" + intoWhsSub.getQty());
        // intoWhsSub.setQty(dQty);
//			// System.out.println("intoWhsSub.setQty(dQty);"+dQty);

        List<IntoWhsSub> lst = new ArrayList<IntoWhsSub>();
        lst.add(intoWhsSub);
        // ������ⵥ�ӱ�
        intoWhsSubDao.insertIntoWhsSub(lst);
    }

    // �����λ������
    private void InstMovBitTab(List<MovBitTab> movBitTab) {
        for (MovBitTab mTab : movBitTab) {
            List<MovBitTab> lmbt = new ArrayList<>();
            // System.out.println("new.size()" + lmbt.size());
            lmbt.add(mTab);
            // System.out.println("add.size()" + lmbt.size());
            // (����ʱ���������������������ڻ�λ�����ӣ����û����������)
            if (invtyNumMapper.selectAllMbit(lmbt).size() > 0) {

                invtyNumMapper.updateMovbitTabJia(lmbt);// �޸���λ��(����)

            } else {
                List<GdsBit> gList = invtyNumMapper.selectRegn(mTab.getGdsBitEncd());
                if (gList.size() == 0) {
                    throw new RuntimeException("û������");
                }
                // System.out.println(gList.get(0).getRegnEncd());
                mTab.setRegnEncd(gList.get(0).getRegnEncd());// ����
                // System.out.println("else.size()" + lmbt.size());
                lmbt.clear();
                // System.out.println("clear.size()" + lmbt.size());
                lmbt.add(mTab);
                // System.out.println("add.size()" + lmbt.size());
                invtyNumMapper.insertMovBitTab(lmbt);// ������λ��
            }

        }

//			// (����ʱ���������������������ڻ�λ�����ӣ����û����������)
//			if (invtyNumMapper.selectAllMbit(movBitTab) != null) {
//
//				invtyNumMapper.updateMovbitTabJia(movBitTab);// �޸���λ��(����)
//			} else {
//				// ������λ�� Ѱ������
//				for (MovBitTab mTab : movBitTab) {
//					List<GdsBit> gList = invtyNumMapper.selectRegn(mTab.getGdsBitEncd());
//					for (GdsBit gdsBit : gList) {
//						mTab.setRegnEncd(gdsBit.getRegnEncd());// ����
//						movBitTab.add(mTab);
//					}
//				}
//				invtyNumMapper.insertMovBitTab(movBitTab);// ������λ��
//			}
    }

    // �������յ���
    private String aInstToGds(ToGdsSngl toGdsSngl, String userId, String userName, ToGdsSngl toGdsSngls)
            throws Exception {
        String number = null;
        try {
            number = getOrderNo.getSeqNo("DH", userId,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        } catch (Exception e) {

            throw new RuntimeException(e.getMessage());
        } // ��ȡ������
        BeanUtils.copyProperties(toGdsSngls, toGdsSngl);

        // �������յ���
        toGdsSngl.setToGdsSnglId(number);// ������
        Date date1 = new Date();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String toGdsSnglDt = sdf1.format(date1);
        toGdsSngl.setToGdsSnglDt(toGdsSnglDt);// ���յ�����
        toGdsSngl.setAccNum(userId);// ҵ��Ա���
        toGdsSngl.setUserName(userName);// ҵ��Ա����
        toGdsSngl.setSetupPers(userName);// ������
        toGdsSngl.setFormTypEncd("003");// ��������
        toGdsSngl.setMemo(toGdsSngls.getMemo());
        toGdsSngl.setIsNtRtnGood(1);
        toGdsSnglDao.insertToGdsSngl(toGdsSngl);
        // System.out.println(number);
        return number;

    }

    // �������յ���
    private void InstToGds(ToGdsSngl toGdsSngl, String number1, List<ToGdsSnglSub> tSnglSubList) throws Exception {
        // �������յ��ӱ�
        for (ToGdsSnglSub tSnglSub : tSnglSubList) {
            tSnglSub.setToGdsSnglId(number1);
            tSnglSub.setPursOrdrNum(toGdsSngl.getPursOrdrId());// �ɹ�������
            tSnglSub.setMemo(tSnglSub.getReturnMemo());// ��ע
            BigDecimal rQty = tSnglSub.getReturnQty();
//				BigDecimal rFu = new BigDecimal("-1");
//				double cheng = rQty.multiply(rFu).doubleValue();
//				BigDecimal returnQty = new BigDecimal(cheng);
//				tSnglSub.setQty(returnQty);// ��������
            tSnglSub.setQty(rQty);// ��������
            // δ˰���
            tSnglSub.setCntnTaxUprc(CalcAmt.noTaxAmt(tSnglSub.getNoTaxUprc(), tSnglSub.getReturnQty()));
            // ��˰����
            tSnglSub.setCntnTaxUprc(
                    CalcAmt.cntnTaxUprc(tSnglSub.getNoTaxUprc(), tSnglSub.getReturnQty(), tSnglSub.getTaxRate()));
            // ��˰���(��˰�ϼ�)
            tSnglSub.setPrcTaxSum(
                    CalcAmt.prcTaxSum(tSnglSub.getNoTaxUprc(), tSnglSub.getReturnQty(), tSnglSub.getTaxRate()));
            tSnglSub.setIsNtRtnGoods(0);
        }

        toGdsSnglSubDao.insertToGdsSnglSub(tSnglSubList);
    }

    // ��ѯ���۳��ⵥ
    @Override
    public String selectSellOutWhsList(String whsEncd) {
        String resp = "";
        List<String> list = getList(whsEncd);

        List<SellOutWhs> sellOutWhs = intoWhsMapper.selectSellOutWhsList(list);

        // **************�Ƽ������λ*****************

        for (SellOutWhs sOutWhs : sellOutWhs) {
            List<SellOutWhsSub> sList = sOutWhs.getsList();
            for (SellOutWhsSub sub : sList) {

                MovBitTab movBitTab = new MovBitTab();
                movBitTab.setWhsEncd(sub.getWhsEncd());
                movBitTab.setInvtyEncd(sub.getInvtyEncd());
                movBitTab.setBatNum(sub.getBatNum());
                List<MovBitTab> mBitTab = intoWhsMapper.selectOutgBit(movBitTab);
                String s = "";
                for (MovBitTab mTab : mBitTab) {
                    s += mTab.getGdsBitEncd() + ",";
                }
                if (s.length() > 2) {
                    s.substring(s.length() - 2);
                }
                sub.setGdsBitEncd(s);
            }
        }
        // *******************************

        try {
            if (sellOutWhs.size() > 0) {
                resp = BaseJson.returnRespObjList("/whs/pda_into_whs/selectSellOutWhsList", true, "��ѯ�ɹ���", null,
                        sellOutWhs);
            } else {
                resp = BaseJson.returnRespObjList("/whs/pda_into_whs/selectSellOutWhsList", false, "��������", null,
                        sellOutWhs);
            }

        } catch (IOException e) {

        }
        return resp;
    }

    /**
     * ���۳��� ���״̬����
     */
    @Override
    public String updatesOutWhs(SellOutWhs sellOutWhs, List<SellOutWhsSub> sellOutWhsSub, List<InvtyTab> invtyTab,
                                List<MovBitTab> movBitTab) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";

        List<MovBitTab> bitTabs = bitListMapper.selectInvtyGdsBitListf(sellOutWhs.getOutWhsId());
        if (bitTabs.size() > 0) {
            List<String> lists = new ArrayList<String>();
            lists.add(sellOutWhs.getOutWhsId());
            bitListMapper.deleteInvtyGdsBitList(lists);
        }

        bitListMapper.insertInvtyGdsBitLists(movBitTab);
        message = "�ɹ�";

        try {
            sellOutWhsServiceImpl.updateSellOutWhsIsNtChk(sellOutWhs);
        } catch (Exception e2) {

            throw new RuntimeException(e2.getMessage());
        }
        if (true) {
            try {
                resp = BaseJson.returnRespObj("whs/pda_into_whs/updatesOutWhs", isSuccess, message, null);
                return resp;
            } catch (IOException e) {


            }
        }

        // �޸����״̬
        int a = intoWhsMapper.updateSOutWhs(sellOutWhs);// �޸�״̬
        if (a >= 1) {
            Boolean enough = true;
            for (SellOutWhsSub sWhsSub : sellOutWhsSub) {
                for (InvtyTab iTab : invtyTab) {
                    InvtyTab inTab = invtyNumMapper.selectInvtyTabByTerm(iTab);

                    // System.out.println("inTab.getNowStok()" + inTab.getNowStok() + "\tsWhsSub.getQty()"
//                            + sWhsSub.getQty() + "\tinTab.getNowStok()" + inTab.getNowStok() + "\tsWhsSub.getQty()"
//                            + sWhsSub.getQty());
                    if (inTab.getNowStok().compareTo(sWhsSub.getQty()) == 1
                            || inTab.getNowStok().compareTo(sWhsSub.getQty()) == 0) {
                        isSuccess = true;
                        message = "����ɹ���";
//						enough=true;
                    }

                    if (inTab.getNowStok().compareTo(sWhsSub.getQty()) == -1) {
                        isSuccess = false;
                        message = "�������������";
                        enough = false;
                        try {
                            throw new RuntimeException("�������������(����)");
                        } catch (Exception e) {

                            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

                            try {
                                resp = BaseJson.returnRespObj("whs/pda_into_whs/updatesOutWhs", true, message, null);
                            } catch (IOException e1) {

                            }
                            return resp;
                        }

                    }
                }
            }

            if (enough) {
                // System.out.println("û�и��Ľ��       sql ûд����");
                invtyNumMapper.updateInvtyTab(invtyTab);// �ִ�������
                // �޸���λ����(����)
                invtyNumMapper.updateMovbitTab(movBitTab);// �޸���λ��(����)
            }

        } else {
            isSuccess = false;
            message = "����ʧ�ܣ�";
        }

        try {
            resp = BaseJson.returnRespObj("whs/pda_into_whs/updatesOutWhs", true, message, null);
        } catch (IOException e) {

        }
        return resp;
    }

    // ����ԭ��
    @Override
    public String selectReason() {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";

        List<RefuseReason> reasons = intoWhsMapper.selectReason();
        if (reasons.size() > 0) {
            isSuccess = true;
            message = "��ѯ����ԭ��ɹ�";
            try {
                resp = BaseJson.returnRespObjList("whs/pda_into_whs/selectReason", isSuccess, message, null, reasons);
            } catch (IOException e) {

            }

        } else {
            isSuccess = false;
            message = "��ѯ����ԭ��ʧ��";
            try {
                resp = BaseJson.returnRespObjList("whs/pda_into_whs/selectReason", isSuccess, message, null, reasons);
            } catch (IOException e) {

            }
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

    // ��ѯ���ۺ��ֳ��ⵥ
    @Override
    public String selectRedSellOutWhsList(String whsEncd) {
        String resp = "";
        List<String> list = getList(whsEncd);

        List<SellOutWhs> sellOutWhs = intoWhsMapper.selectRedSellOutWhsList(list);

        // **************�Ƽ�������룩���λ*****************

        for (SellOutWhs sOutWhs : sellOutWhs) {
            List<SellOutWhsSub> sList = sOutWhs.getsList();
            for (SellOutWhsSub sub : sList) {
                sub.setQty(sub.getQty().abs());
                MovBitTab movBitTab = new MovBitTab();
                movBitTab.setWhsEncd(sub.getWhsEncd());
                movBitTab.setInvtyEncd(sub.getInvtyEncd());
                movBitTab.setBatNum(sub.getBatNum());
                List<MovBitTab> mBitTab = intoWhsMapper.selectIntogBit(movBitTab);
                String s = "";
                for (MovBitTab mTab : mBitTab) {
                    s += mTab.getGdsBitEncd() + ",";
                }
                if (s.length() > 2) {
                    s.substring(s.length() - 2);
                }
                sub.setGdsBitEncd(s);
            }
        }
        // *******************************

        try {
            if (sellOutWhs.size() > 0) {
                resp = BaseJson.returnRespObjList("/whs/pda_into_whs/selectRedSellOutWhsList", true, "��ѯ�ɹ���", null,
                        sellOutWhs);
            } else {
                resp = BaseJson.returnRespObjList("/whs/pda_into_whs/selectRedSellOutWhsList", false, "��������", null,
                        sellOutWhs);
            }

        } catch (IOException e) {

        }
        return resp;
    }

    /**
     * �������۳��� ���״̬����
     */
    @Override
    public String updatesRedOutWhs(SellOutWhs sellOutWhs, List<MovBitTab> movBitTab) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";

        List<MovBitTab> bitTabs = bitListMapper.selectInvtyGdsBitListf(sellOutWhs.getOutWhsId());
        if (bitTabs.size() > 0) {
            List<String> lists = new ArrayList<String>();
            lists.add(sellOutWhs.getOutWhsId());
            bitListMapper.deleteInvtyGdsBitList(lists);
        }

        bitListMapper.insertInvtyGdsBitLists(movBitTab);
        message = "�ɹ�";

        try {
            sellOutWhsServiceImpl.updateSellOutWhsIsNtChk(sellOutWhs);
        } catch (Exception e2) {
            throw new RuntimeException(e2.getMessage());
        }

        try {
            resp = BaseJson.returnRespObj("whs/pda_into_whs/updatesRedOutWhsPda", isSuccess, message, null);
        } catch (IOException e1) {


        }

        return resp;
    }

    //		 ��ѯ������ⵥ
    @Override
    public String selectIntoWhsList(String whsEncd) {
        String resp = "";
        List<String> list = getList(whsEncd);

        List<IntoWhs> intoWhs = intoWhsMapper.selectIntoWhsList(list);

        // **************�Ƽ������λ*****************

        for (IntoWhs into : intoWhs) {
            List<IntoWhsSub> sList = into.getIntoWhsSub();
            for (IntoWhsSub sub : sList) {
                sub.setQty(sub.getQty().abs());
                MovBitTab movBitTab = new MovBitTab();
                movBitTab.setWhsEncd(sub.getWhsEncd());
                movBitTab.setInvtyEncd(sub.getInvtyEncd());
                movBitTab.setBatNum(sub.getBatNum());
                List<MovBitTab> mBitTab = intoWhsMapper.selectOutgBit(movBitTab);
                String s = "";
                for (MovBitTab mTab : mBitTab) {
                    s += mTab.getGdsBitEncd() + ",";
                }
                if (s.length() > 2) {
                    s.substring(s.length() - 2);
                }
                sub.setGdsBitEncd(s);
            }
        }
        // *******************************

        try {
            if (intoWhs.size() > 0) {
                resp = BaseJson.returnRespObjList("/whs/pda_into_whs/selectIntoWhsList", true, "��ѯ�ɹ���", null, intoWhs);
            } else {
                resp = BaseJson.returnRespObj("/whs/pda_into_whs/selectIntoWhsList", false, "��������", null);
            }

        } catch (IOException e) {

        }
        return resp;
    }

    @Override
    public String updatesRedIntoWhs(IntoWhs intoWhs, List<MovBitTab> list) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";

        List<MovBitTab> bitTabs = bitListMapper.selectInvtyGdsBitListf(intoWhs.getIntoWhsSnglId());
        if (bitTabs.size() > 0) {
//			List<String> lists = new ArrayList<String>();
//			lists.add(sellOutWhs.getOutWhsId());
            bitListMapper.deleteInvtyGdsBitList(Arrays.asList(intoWhs.getIntoWhsSnglId()));
        }

        bitListMapper.insertInvtyGdsBitLists(list);
        message = "�ɹ�";

        try {
            purcIntoWhsServiceImpl.updateIntoWhsIsNtChk(intoWhs);
        } catch (Exception e2) {

            throw new RuntimeException(e2.getMessage());
        }

        try {
            resp = BaseJson.returnRespObj("whs/pda_into_whs/updatesRedOutWhsPda", isSuccess, message, null);
        } catch (IOException e1) {

        }

        return resp;
    }
}
