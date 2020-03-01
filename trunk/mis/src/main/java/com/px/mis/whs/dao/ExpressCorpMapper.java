package com.px.mis.whs.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.px.mis.ec.entity.ExpressCodeAndName;
import com.px.mis.whs.entity.ExpressCorp;
import com.px.mis.whs.entity.ExpressCorpMap;

public interface ExpressCorpMapper {

    // ������ݹ�˾
    public int insertExpressCorp(ExpressCorp record);

    // �޸Ŀ�ݹ�˾
    public int updateExpressCorp(ExpressCorp record);

    // ɾ����ݹ�˾
    public int deleteExpressCorp(String expressEncd);

    public int deleteECorpList(List<String> expressEncd);// ��ɾ
    // ����

    public List<ExpressCorp> selectECorpAllList(@Param("expressEncd") List<String> expressEncd);

    // �򵥲� ��ݹ�˾
    public ExpressCorp selectExpressCorp(@Param("expressEncd") String expressEncd);

    public List<ExpressCorp> selectExpressCorpList(@Param("expressEncd") String expressEncd);

    // ��ҳ��
    public List<ExpressCorp> selectList(Map map);

    public int selectCount(Map map);

    // ��ӡ
    public List<ExpressCorpMap> selectListDaYin(Map map);

    public List<ExpressCorp> selectExpressEncdName(@Param("expressEncd") String expressEncd);

    //��ݹ�˾��������ExpressCodeAndName
    public List<ExpressCodeAndName> queryExpressCodeAndNameList();

}