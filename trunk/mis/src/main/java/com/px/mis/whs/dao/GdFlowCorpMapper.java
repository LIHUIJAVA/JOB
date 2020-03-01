package com.px.mis.whs.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.px.mis.whs.entity.GdFlowCorp;
import com.px.mis.whs.entity.GdFlowCorpMap;

public interface GdFlowCorpMapper {

    //����������˾
    public int insertGdFlowCorp(GdFlowCorp record);

    //�޸�������˾
    public int updateGdFlowCorp(GdFlowCorp record);

    //ɾ��������˾
    public int deleteGdFlowCorp(String gdFlowEncd);

    public int deleteGFlowCorpList(List<String> gdFlowEncd);//��ɾ

    //����
    public List<GdFlowCorp> selectGFlowCorpAllList(@Param("gdFlowEncd") List<String> gdFlowEncd);

    //�򵥲� ������˾
    public GdFlowCorp selectGdFlowCorp(@Param("gdFlowEncd") String gdFlowEncd);

    public List<GdFlowCorp> selectGdFlowCorpList(@Param("gdFlowEncd") String gdFlowEncd);

    //��ҳ��
    public List<GdFlowCorp> selectList(Map map);

    public int selectCount(Map map);

    //��ӡ
    public List<GdFlowCorpMap> selectListDaYin(Map map);

}