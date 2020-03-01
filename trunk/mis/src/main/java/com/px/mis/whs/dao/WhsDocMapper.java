package com.px.mis.whs.dao;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.px.mis.whs.entity.AvalQtyCtrlMode;
import com.px.mis.whs.entity.City;
import com.px.mis.whs.entity.RealWhs;
import com.px.mis.whs.entity.RealWhsMap;
import com.px.mis.whs.entity.UserWhs;
import com.px.mis.whs.entity.ValtnMode;
import com.px.mis.whs.entity.WhsAttr;
import com.px.mis.whs.entity.WhsDoc;
import com.px.mis.whs.entity.WhsGds;

public interface WhsDocMapper {

    //��Ӳֿ⵵��
    public int insertWhsDoc(WhsDoc record);

    public int selectWDoc(@Param("whsEncd") String whsEncd);

    public int exInsertWhsDoc(WhsDoc record);

    //�޸Ĳֿ⵵��
    public int updateWhsDoc(WhsDoc record);

    //ɾ���ֿ⵵��
    public int deleteWhsDoc(String whsEncd);

    public int deleteWDocList(List<String> whsEncd);

    //�򵥲� �ֿ⵵��
    public WhsDoc selectWhsDoc(@Param("whsEncd") String whsEncd);

    public List<WhsDoc> selectWhsDocList(@Param("whsEncd") String whsEncd);

    //������ѯ
    public List<WhsDoc> selectAllWDocList(@Param("whsEncd") List<String> whsEncd);

    //��ҳ��
    public List<WhsDoc> selectList(Map map);

    public int selectCount(Map map);

    //��ӡ
    public List<WhsDoc> selectListDaYin(Map map);

    //ʡ ��  ��
    public List<City> selectCity(City city);

    //ʡ 
    public List<City> selectProvinces();

    //��
    public List<City> selectCities(@Param("superiorCode") String superiorCode);

    //��
    public List<City> selectCounties(@Param("superiorCode") String superiorCode);

    //�Ƽ۷�ʽ
    public List<ValtnMode> selectValtnMode();

    //�ֿ�����
    public List<WhsAttr> selectWhsAttr();

    //���������Ʒ�ʽ
    public List<AvalQtyCtrlMode> selectAMode();

    //�û��ֿ�
    public int insertUserWhs(List<UserWhs> userWhs);

    public int updateUserWhs(UserWhs wDoc);

    public List<UserWhs> selectUserWhsList(Map map);

    public int selectUserWhsCount(Map map);

    public int deleteUserWhsList(List<String> list);

    //��λ�ֿ��Ӧ
    public int insertWhsGds(List<WhsGds> userWhs);

    public int deleteWhsGds(List<String> list);

    public List<WhsGds> selectWhsGdsList(Map map);

    public int selectWhsGdsListCount(Map map);

    public WhsGds selectWhsGds(WhsGds userWhs);

    public String selectWhsGdsReal(@Param(value = "whsEncd") String whsEncd, @Param(value = "gdsBitEncd") String gdsBitEncd);

    //pc��λ��ѯ
    public List<WhsGds> selectWhsGdsRealList(Map map);

    //�����ֲܲ�ѯҵ��ֿ�
    public List<String> selectRealWhsList(List<String> list);

    //
    public Integer selectisNtPrgrGdsBitMgmtWhs(@Param(value = "whsEncd") String whsEncd);

    //��
    public int insertRealWhs(RealWhs wDoc);

    public RealWhs selectRealWhs(@Param("realWhs") String realWhs);

    public int updateRealWhs(RealWhs record);

    public List<RealWhs> queryRealWhsList(Map map);

    public int deleteRealWhsList(List<String> list);

    public int queryRealWhsCount(Map map);

    public List<RealWhs> queryRealWhsListDaYin(Map map);

    //�ֿ���ܲ�
    public RealWhsMap selectRealWhsMap(RealWhsMap gds);

    public int insertRealWhsMap(RealWhsMap realWhsMap);

    public List<RealWhsMap> selectRealWhsMapList(Map map);

    public int deleteRealWhsMap(List<String> list);
}