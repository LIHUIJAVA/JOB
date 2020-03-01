package com.px.mis.account.entity;

import java.io.Serializable;
import java.util.List;

/**
 * �س嵥
 * @author 
 */
public class FormBackFlush implements Serializable {
    /**
     * �س嵥����
     */
    private String backNum;

    /**
     * ���ݺ�
     */
    private String formNum;

    /**
     * ��������
     */
    private String formDt;

    /**
     * �س嵥����
     */
    private String backDt;

    /**
     * �Ƿ����
     */
    private Integer isNtChk;

    /**
     * �����
     */
    private String chkr;

    /**
     * �������
     */
    private String chkTm;

    /**
     * ��������ͱ��
     */
    private String outIntoWhsTypId;

    /**
     * ���������
     */
    private String outIntoWhsTypNm;

    /**
     * ҵ�����ͱ��
     */
    private String bizTypId;

    /**
     * ҵ����������
     */
    private String bizTypNm;

    /**
     * �������ͱ��
     */
    private String sellTypId;

    /**
     * ������������
     */
    private String sellTypNm;

    /**
     * �ɹ����ͱ��
     */
    private String pursTypId;

    /**
     * �ɹ���������
     */
    private String pursTypNm;

    /**
     * ��Ӧ�̱���
     */
    private String provrId;

    /**
     * ��Ӧ������
     */
    private String provrNm;

    /**
     * �շ����ͱ���
     */
    private String recvSendCateId;

    /**
     * �շ���������
     */
    private String recvSendCateNm;

    /**
     * �ͻ����
     */
    private String custId;

    /**
     * �ͻ�����
     */
    private String custNm;

    /**
     * �û����
     */
    private String accNum;

    /**
     * �û�����
     */
    private String userName;

    /**
     * �Ƿ�������
     */
    private Integer isNtBookOk;

    /**
     * ������
     */
    private String bookOkAcc;

    /**
     * ��������
     */
    private String bookOkDt;

    /**
     * �Ƿ�Ʊ
     */
    private Integer isNtBllg;

    /**
     * �Ƿ����
     */
    private Integer isNtStl;

    /**
     * �Ƿ�����ƾ֤
     */
    private Integer isNtMakeVouch;

    /**
     * �Ƿ��ϴ�����
     */
    private Integer isNtUploadHead;

    /**
     * ƾ֤���
     */
    private String makeVouchId;

    /**
     * ƾ֤ժҪ
     */
    private String makeVouchAbst;

    /**
     * ��ע
     */
    private String memo;

    /**
     * �Ƿ���ĩ����
     */
    private Integer isMthEndStl;

    /**
     * �ϼ����ݺ�
     */
    private String sellOrdrInd;

    /**
     * �Ƿ���ֻس�
     */
    private Integer isRedBack;
    
    private String setupPers;//�Ƶ���

    private String setupTm;//����ʱ��
    
    private List<FormBackFlushSub> formBackFlushSub;
    
    //��������
    private String formTypEncd; 
    //������������ 
    private String formTypName;
     
    private static final long serialVersionUID = 1L;

    public String getBackNum() {
        return backNum;
    }

    public void setBackNum(String backNum) {
        this.backNum = backNum;
    }

    public String getFormNum() {
        return formNum;
    }

    public void setFormNum(String formNum) {
        this.formNum = formNum;
    }



    public Integer getIsNtChk() {
        return isNtChk;
    }

    public void setIsNtChk(Integer isNtChk) {
        this.isNtChk = isNtChk;
    }

    public String getChkr() {
        return chkr;
    }

    public void setChkr(String chkr) {
        this.chkr = chkr;
    }

    public String getChkTm() {
        return chkTm;
    }

    public void setChkTm(String chkTm) {
        this.chkTm = chkTm;
    }

    public String getOutIntoWhsTypId() {
        return outIntoWhsTypId;
    }

    public void setOutIntoWhsTypId(String outIntoWhsTypId) {
        this.outIntoWhsTypId = outIntoWhsTypId;
    }

    public String getOutIntoWhsTypNm() {
        return outIntoWhsTypNm;
    }

    public void setOutIntoWhsTypNm(String outIntoWhsTypNm) {
        this.outIntoWhsTypNm = outIntoWhsTypNm;
    }

    public String getBizTypId() {
        return bizTypId;
    }

    public void setBizTypId(String bizTypId) {
        this.bizTypId = bizTypId;
    }

    public String getBizTypNm() {
        return bizTypNm;
    }

    public void setBizTypNm(String bizTypNm) {
        this.bizTypNm = bizTypNm;
    }

    public String getSellTypId() {
        return sellTypId;
    }

    public void setSellTypId(String sellTypId) {
        this.sellTypId = sellTypId;
    }

    public String getSellTypNm() {
        return sellTypNm;
    }

    public void setSellTypNm(String sellTypNm) {
        this.sellTypNm = sellTypNm;
    }

    public String getPursTypId() {
        return pursTypId;
    }

    public void setPursTypId(String pursTypId) {
        this.pursTypId = pursTypId;
    }

    public String getPursTypNm() {
        return pursTypNm;
    }

    public void setPursTypNm(String pursTypNm) {
        this.pursTypNm = pursTypNm;
    }

    public String getProvrId() {
        return provrId;
    }

    public void setProvrId(String provrId) {
        this.provrId = provrId;
    }

    public String getProvrNm() {
        return provrNm;
    }

    public void setProvrNm(String provrNm) {
        this.provrNm = provrNm;
    }

    public String getRecvSendCateId() {
        return recvSendCateId;
    }

    public void setRecvSendCateId(String recvSendCateId) {
        this.recvSendCateId = recvSendCateId;
    }

    public String getRecvSendCateNm() {
        return recvSendCateNm;
    }

    public void setRecvSendCateNm(String recvSendCateNm) {
        this.recvSendCateNm = recvSendCateNm;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getCustNm() {
        return custNm;
    }

    public void setCustNm(String custNm) {
        this.custNm = custNm;
    }

    public String getAccNum() {
        return accNum;
    }

    public void setAccNum(String accNum) {
        this.accNum = accNum;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getIsNtBookOk() {
        return isNtBookOk;
    }

    public void setIsNtBookOk(Integer isNtBookOk) {
        this.isNtBookOk = isNtBookOk;
    }

    public String getBookOkAcc() {
        return bookOkAcc;
    }

    public void setBookOkAcc(String bookOkAcc) {
        this.bookOkAcc = bookOkAcc;
    }



    public Integer getIsNtBllg() {
        return isNtBllg;
    }

    public void setIsNtBllg(Integer isNtBllg) {
        this.isNtBllg = isNtBllg;
    }

    public Integer getIsNtStl() {
        return isNtStl;
    }

    public void setIsNtStl(Integer isNtStl) {
        this.isNtStl = isNtStl;
    }

    public Integer getIsNtMakeVouch() {
        return isNtMakeVouch;
    }

    public void setIsNtMakeVouch(Integer isNtMakeVouch) {
        this.isNtMakeVouch = isNtMakeVouch;
    }

    public Integer getIsNtUploadHead() {
        return isNtUploadHead;
    }

    public void setIsNtUploadHead(Integer isNtUploadHead) {
        this.isNtUploadHead = isNtUploadHead;
    }

    public String getMakeVouchId() {
        return makeVouchId;
    }

    public void setMakeVouchId(String makeVouchId) {
        this.makeVouchId = makeVouchId;
    }

    public String getMakeVouchAbst() {
        return makeVouchAbst;
    }

    public void setMakeVouchAbst(String makeVouchAbst) {
        this.makeVouchAbst = makeVouchAbst;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Integer getIsMthEndStl() {
        return isMthEndStl;
    }

    public void setIsMthEndStl(Integer isMthEndStl) {
        this.isMthEndStl = isMthEndStl;
    }

    public String getSellOrdrInd() {
        return sellOrdrInd;
    }

    public void setSellOrdrInd(String sellOrdrInd) {
        this.sellOrdrInd = sellOrdrInd;
    }

    public Integer getIsRedBack() {
        return isRedBack;
    }

    public void setIsRedBack(Integer isRedBack) {
        this.isRedBack = isRedBack;
    }
    
    public List<FormBackFlushSub> getFormBackFlushSub() {
		return formBackFlushSub;
	}

	public void setFormBackFlushSub(List<FormBackFlushSub> formBackFlushSub) {
		this.formBackFlushSub = formBackFlushSub;
	}

	public String getFormDt() {
		return formDt;
	}

	public void setFormDt(String formDt) {
		this.formDt = formDt;
	}

	public String getBackDt() {
		return backDt;
	}

	public void setBackDt(String backDt) {
		this.backDt = backDt;
	}

	public String getBookOkDt() {
		return bookOkDt;
	}

	public void setBookOkDt(String bookOkDt) {
		this.bookOkDt = bookOkDt;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getSetupPers() {
		return setupPers;
	}

	public void setSetupPers(String setupPers) {
		this.setupPers = setupPers;
	}

	public String getSetupTm() {
		return setupTm;
	}

	public void setSetupTm(String setupTm) {
		this.setupTm = setupTm;
	}

	public String getFormTypEncd() {
		return formTypEncd;
	}

	public void setFormTypEncd(String formTypEncd) {
		this.formTypEncd = formTypEncd;
	}

	public String getFormTypName() {
		return formTypName;
	}

	public void setFormTypName(String formTypName) {
		this.formTypName = formTypName;
	}
	
	
}