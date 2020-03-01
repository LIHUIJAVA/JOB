package com.px.mis.whs.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.whs.entity.ExpressCorp;

public interface ExpressCorpService {

    //������ݹ�˾
    public ObjectNode insertExpressCorp(ExpressCorp record);

    //�޸Ŀ�ݹ�˾
    public ObjectNode updateExpressCorp(ExpressCorp record);

    //ɾ����ݹ�˾
    public ObjectNode deleteExpressCorp(String expressEncd);

    public String deleteECorpList(String expressEncd);

    //�򵥲�  ��ݹ�˾
    public ExpressCorp selectExpressCorp(String expressEncd);

    public List<ExpressCorp> selectExpressCorpList(String expressEncd);

    //��ҳ��
    public String queryList(Map map);

    //��ӡ
    public String queryListDaYin(Map map);

    public String uploadFileAddDb(MultipartFile file);

    // ����express_code_and_name
    public String queryExpressCodeAndNameList();

}
