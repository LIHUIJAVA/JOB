package com.px.mis.ec.service;

public interface OrderDownloadService {
    //�жϵ����Ƿ����ڲ���
    Boolean isOperation(String storeId);

    //��ӵ�ǰ����״̬
    void addState(String storeId);

    //ɾ����ǰ����״̬
    void rmState(String storeId);
}
