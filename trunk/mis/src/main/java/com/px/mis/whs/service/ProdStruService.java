package com.px.mis.whs.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.px.mis.whs.entity.ProdStru;
import com.px.mis.whs.entity.ProdStruSubTab;

public interface ProdStruService {


    //������Ʒ�ṹ
    public String insertProdStru(ProdStru prodStru, List<ProdStruSubTab> pList);

    //�޸Ĳ�Ʒ�ṹ
    public String updateProdStru(ProdStru prodStru, List<ProdStruSubTab> pList);

    //ɾ����Ʒ�ṹ
    //����ɾ
    public String deleteAllProdStru(String ordrNum);

    //�򵥲�  ��Ʒ�ṹ
    public String query(String ordrNum);//����������Ϣ������ʵ�壻�ӣ����ϣ�

    public String queryList(Map map);//��ҳ��ѯ

    //��ѯ ��ȡ��ĸ���Ĳ�Ʒ�ṹ��Ϣ
    public String selectProdStruByMom(String ordrNum);

    //���
    public String updatePStruChk(List<ProdStru> cList);

    //����
    public String updatePStruNoChk(List<ProdStru> cList);

    public String uploadFileAddDb(MultipartFile file);

    //��ӡ
    public String queryListPrint(Map map);

    public String queryAmbDisambSngl(Map map);//��������װ��������Ϣ������ʵ�壻�ӣ����ϣ�

}
