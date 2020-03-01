package com.px.mis.whs.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.px.mis.purc.entity.IntoWhs;
import com.px.mis.purc.entity.IntoWhsSub;
import com.px.mis.purc.entity.SellOutWhs;
import com.px.mis.purc.entity.SellOutWhsSub;
import com.px.mis.purc.entity.ToGdsSngl;
import com.px.mis.purc.entity.ToGdsSnglSub;
import com.px.mis.whs.entity.MovBitTab;
import com.px.mis.whs.entity.RefuseReason;

public interface IntoWhsMapper {

    // 1.�ɹ���
    public List<ToGdsSngl> selectToGdsSnglList(List<String> whsEncd);// ��ѯ���е�����
    // --�����������յ���Ϣ

    public int insertToGdsSngl(ToGdsSngl toGdsSngl);

    public int insertToGdsSnglSub(List<ToGdsSnglSub> toGdsSnglSubList);

    // ���ձ�Ų�ѯ��������Ϣ
    public ToGdsSngl selectToGdsSnglByToGdsSnglId(@Param("toGdsSnglId") String toGdsSnglId);

    // ��ѯ�Ƿ��е��������
    public ToGdsSngl selectToGdsSnglId(@Param("toGdsSnglId") String toGdsSnglId);

    // --������ⵥ
    public int insertIntoWhs(IntoWhs intoWhs);

    public int insertIntoWhsSub(List<IntoWhsSub> intoWhsSubList);

    // ��������һ������״̬��������-������ɣ� ���������������ⵥ
    public int updateTGdsGngl(List<ToGdsSnglSub> toGdsSngl);

    // ���ղɹ���ⵥ��Ų�ѯ�ɹ���ⵥ��Ϣ
    public IntoWhs selectIntoWhsByIntoWhsSnglId(@Param("intoWhsSnglId") String intoWhsSnglId);

    // ��ѯ�Ƿ��вɹ���ⵥ���
    public IntoWhs selectIntoWhsId(@Param("intoWhsSnglId") String intoWhsSnglId);

    // ��� �Ƽ���λ
    public List<MovBitTab> selectIntogBit(MovBitTab oBitTab);

    // ���� �Ƽ���λ
    public List<MovBitTab> selectOutgBit(MovBitTab oBitTab);

    // 2.���۳�
    public List<SellOutWhs> selectSellOutWhsList(List<String> whsEncd);// ��ѯ���۳��ⵥ
    // ������۳��ⵥ

    public int updateSOutWhs(SellOutWhs sellOutWhs);

    // ��ѯ����ԭ��
    public List<RefuseReason> selectReason();

    /**
     * ��Ų��ӱ� ������
     *
     * @param ordrNum ���
     * @return ToGdsSnglSub ��ϸ��
     */
    public ToGdsSnglSub selectToGdsByOrdrNumKey(@Param("ordrNum") Long ordrNum);

    /**
     * ��Ų��ӱ� ���۳��ⵥ
     *
     * @param ordrNum ���
     * @return ToGdsSnglSub ��ϸ��
     */
    public SellOutWhsSub selectSellByOrdrNumKey(@Param("ordrNum") Long ordrNum);

    /**
     * ���ֳ��ⵥ��ѯ
     */
    public List<SellOutWhs> selectRedSellOutWhsList(List<String> list);

    /**
     * �������
     */
    public List<IntoWhs> selectIntoWhsList(List<String> whsEncd);

}
