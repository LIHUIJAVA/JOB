package com.px.mis.whs.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.whs.entity.City;
import com.px.mis.whs.entity.RealWhs;
import com.px.mis.whs.entity.RealWhsMap;
import com.px.mis.whs.entity.UserWhs;
import com.px.mis.whs.entity.WhsDoc;
import com.px.mis.whs.entity.WhsGds;


public interface WhsDocService {

    //��Ӳֿ⵵��
    public ObjectNode insertWhsDoc(WhsDoc record);

    //�޸Ĳֿ⵵��
    public ObjectNode updateWhsDoc(WhsDoc record);

    //ɾ���ֿ⵵��
    public ObjectNode deleteWhsDoc(String whsEncd);

    public String deleteWDocList(String whsEncd);

    //�򵥲� �ֿ⵵��
    public WhsDoc selectWhsDoc(String whsEncd) throws IOException;

    public String selectWhsDocList(String whsEncd, String userId) throws IOException;

    //��ҳ��
    public String queryList(Map map);

    //��ӡ
    public String queryListDaYin(Map map);

    //ʡ �� ��
    public String selectCity(City city);

    //ʡ 
    public String selectProvinces();

    //��
    public String selectCities(String superiorCode);

    //��
    public String selectCounties(String superiorCode);

    //�Ƽ۷�ʽ
    public String selectValtnMode();

    //�ֿ�����
    public String selectWhsAttr();

    //���������Ʒ�ʽ
    public String selectAMode();

    public String uploadFileAddDb(MultipartFile file);

    //�ֿ��û�
    public ObjectNode insertUserWhs(List<UserWhs> userWhs);

    public ObjectNode updateUserWhs(UserWhs wDoc);

    public String deleteUserWhsList(String id);

    public String selectUserWhsList(Map map);

    //��λ��ֿ�
    public String deleteWhsGds(List<String> id);

    public ObjectNode insertWhsGds(List<WhsGds> userWhs);

    public String selectWhsGdsList(Map map);

    //pda��ѯ
    public String selectWhsGdsRealList(Map list);

    //�ܲ�
    public ObjectNode insertRealWhs(RealWhs wDoc);

    public ObjectNode updateRealWhs(RealWhs wDoc);

    public String deleteRealWhsList(String whsEncd);

    public RealWhs selectRealWhs(String realWhs);

    public String queryRealWhsListDaYin(Map map);

    public String queryRealWhsList(Map map);

    //�ֺܲͲֿ�
    public ObjectNode insertRealWhsMap(RealWhsMap realWhsMap);

    public String selectRealWhsMap(Map map);

    public String deleteRealWhsMap(List<String> list);

}
