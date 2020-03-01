package com.px.mis.whs.service;

import java.util.List;

import com.px.mis.purc.entity.IntoWhs;
import com.px.mis.purc.entity.IntoWhsSub;
import com.px.mis.purc.entity.SellOutWhs;
import com.px.mis.purc.entity.SellOutWhsSub;
import com.px.mis.purc.entity.ToGdsSngl;
import com.px.mis.purc.entity.ToGdsSnglSub;
import com.px.mis.whs.entity.InvtyTab;
import com.px.mis.whs.entity.MovBitTab;

public interface IntoWhsService {

    //1.�ɹ���
    public String selectToGdsSnglList(String whsEncd);//��ѯ���е�����

    //�����������յ���Ϣ
    public String insertToGdsSngl(String userId, ToGdsSngl toGdsSngl, List<ToGdsSnglSub> toGdsSnglSubList);

    //������ⵥ
    public String addIntoWhs(String userId, String userName,
                             IntoWhs intoWhs, List<IntoWhsSub> intoWhsSubList,
                             List<InvtyTab> invtyTab, List<MovBitTab> movBitTab,
                             ToGdsSngl toGdsSngl, List<ToGdsSnglSub> tSnglSubList) throws Exception;


    //2.���۳�
    public String selectSellOutWhsList(String whsEncd);//��ѯ���۳��ⵥ

    //�ش����۳��ⵥ
    public String updatesOutWhs(SellOutWhs sellOutWhs, List<SellOutWhsSub> sellOutWhsSub,
                                List<InvtyTab> invtyTab, List<MovBitTab> movBitTab);

    //����ԭ��
    public String selectReason();

    //���ֳ��ⵥ
    public String selectRedSellOutWhsList(String whsEncd);

    //���ֳ��ⵥ(�ش�)
    public String updatesRedOutWhs(SellOutWhs sOutWhs, List<MovBitTab> list);

    // ��ѯ������ⵥ
    String selectIntoWhsList(String whsEncd);

    //������ⵥ(�ش�)
    public String updatesRedIntoWhs(IntoWhs intoWhs, List<MovBitTab> list);
}
