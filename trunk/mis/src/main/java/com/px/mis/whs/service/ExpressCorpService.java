package com.px.mis.whs.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.whs.entity.ExpressCorp;

public interface ExpressCorpService {

    //新增快递公司
    public ObjectNode insertExpressCorp(ExpressCorp record);

    //修改快递公司
    public ObjectNode updateExpressCorp(ExpressCorp record);

    //删除快递公司
    public ObjectNode deleteExpressCorp(String expressEncd);

    public String deleteECorpList(String expressEncd);

    //简单查  快递公司
    public ExpressCorp selectExpressCorp(String expressEncd);

    public List<ExpressCorp> selectExpressCorpList(String expressEncd);

    //分页查
    public String queryList(Map map);

    //打印
    public String queryListDaYin(Map map);

    public String uploadFileAddDb(MultipartFile file);

    // 查快递express_code_and_name
    public String queryExpressCodeAndNameList();

}
