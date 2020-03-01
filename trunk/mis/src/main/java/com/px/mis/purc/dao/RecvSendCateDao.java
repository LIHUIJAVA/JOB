package com.px.mis.purc.dao;

import java.util.List;

import com.px.mis.purc.entity.RecvSendCate;

public interface RecvSendCateDao {
	
    int deleteRecvSendCateByRecvSendCateId(String recvSendCateId);

    int insertRecvSendCate(RecvSendCate recvSendCate);
    
    RecvSendCate selectRecvSendCateByRecvSendCateId(String recvSendCateId);
    
    int updateRecvSendCateByRecvSendCateId(RecvSendCate recvSendCate);
    
    List<RecvSendCate>  selectRecvSendCate();


}