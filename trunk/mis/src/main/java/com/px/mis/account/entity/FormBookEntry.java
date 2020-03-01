package com.px.mis.account.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

/**
 * form_book_entry
 * @author 
 */
public class FormBookEntry implements Serializable {
	
	public FormBookEntry() {}
	
	public FormBookEntry(String formNum, String formDt,Integer isNtChk,String chkr,String chkTm,String outIntoWhsTypId,String outIntoWhsTypNm, 
			String bizTypId, String bizTypNm, String sellTypId, String sellTypNm,String pursTypId, String pursTypNm, String provrId,
			String provrNm, String custId, String custNm,String accNum, String userName, Integer isNtBookOk, Integer isNtMakeVouch, String memo,
			Integer isNtBllg,Integer isNtStl,Integer isMthEndStl,String sellOrdrInd,String setupPers,String setupTm,String formTypEncd,
			String formTypName,String recvSendCateId,String recvSendCateNm,String deptId,String deptName) {
		super();
		this.formNum = formNum;
		this.formDt = formDt;
		this.isNtChk = isNtChk;
		this.chkr = chkr;
		this.chkTm = chkTm;
		this.outIntoWhsTypId = outIntoWhsTypId;
		this.outIntoWhsTypNm = outIntoWhsTypNm;
		this.bizTypId = bizTypId;
		this.bizTypNm = bizTypNm;
		this.sellTypId = sellTypId;
		this.sellTypNm = sellTypNm;
		this.pursTypId = pursTypId;
		this.pursTypNm = pursTypNm;
		this.provrId = provrId;
		this.provrNm = provrNm;
		this.custId = custId;
		this.custNm = custNm;
		this.accNum = accNum;
		this.userName = userName;
		this.isNtBookOk = isNtBookOk;
		this.isNtMakeVouch = isNtMakeVouch;
		this.memo = memo;
		this.isNtBllg = isNtBllg;
		this.isNtStl = isNtStl;
		this.isMthEndStl = isMthEndStl;
		this.sellOrdrInd = sellOrdrInd;
		this.setupPers = setupPers;
		this.setupTm = setupTm;
		this.formTypEncd = formTypEncd;
		this.formTypName = formTypName;
		this.recvSendCateId = recvSendCateId;
		this.recvSendCateNm = recvSendCateNm;
		this.deptId = deptId;
		this.deptName = deptName;
	}
	
	public FormBookEntry(String formNum, String formDt, Integer isNtChk, String chkr, String chkTm1, String outIntoWhsTypId,
			String outIntoWhsTypNm, String bizTypId, String bizTypNm, String sellTypId, String sellTypNm,
			String pursTypId, String pursTypNm, String provrId, String provrNm, String custId, String custNm,
			String accNum, String userName, Integer isNtBookOk, Integer isNtMakeVouch, String memo,Integer isNtBllg,
			Integer isNtStl, Integer isNtUploadHead, String bookName, String bookOkDt, String recvSendCateId,
			String recvSendCateNm,Integer isMthEndStl) {
		super();
		this.formNum = formNum;
		this.isNtChk = isNtChk;
		this.chkr = chkr;
		this.outIntoWhsTypId = outIntoWhsTypId;
		this.outIntoWhsTypNm = outIntoWhsTypNm;
		this.bizTypId = bizTypId;
		this.bizTypNm = bizTypNm;
		this.sellTypId = sellTypId;
		this.sellTypNm = sellTypNm;
		this.pursTypId = pursTypId;
		this.pursTypNm = pursTypNm;
		this.provrId = provrId;
		this.provrNm = provrNm;
		this.custId = custId;
		this.custNm = custNm;
		this.accNum = accNum;
		this.userName = userName;
		this.isNtBookOk = isNtBookOk;
		this.isNtMakeVouch = isNtMakeVouch;
		this.memo = memo;
		this.formDt = formDt;
		this.chkTm = chkTm1;
		this.isNtBllg = isNtBllg;
		this.isNtStl = isNtStl;
		this.isNtUploadHead = isNtUploadHead;
		this.bookOkAcc = bookName;
		this.bookOkDt = bookOkDt;
		this.recvSendCateId = recvSendCateId;
		this.recvSendCateNm = recvSendCateNm;
		this.isMthEndStl = isMthEndStl;
	}

	/**
     * ���ݺ�
     */
	@JsonProperty("���ݺ�")
    private String formNum;

    /**
     * ��������
     */
	@JsonProperty("��������")
    private String formDt;

    /**
     * �Ƿ����
     */
	@JsonProperty("�Ƿ����")
    private Integer isNtChk;

    /**
     * �����
     */
	@JsonProperty("�����")
    private String chkr;

    /**
     * �������
     */
	@JsonProperty("�������")
    private String chkTm;

    /**
     * ��������ͱ���
     */
	@JsonProperty("��������ͱ���")
    private String outIntoWhsTypId;

    /**
     * ���������
     */
	@JsonProperty("���������")
    private String outIntoWhsTypNm;

    /**
     * ҵ�����ͱ���
     */
	@JsonProperty("ҵ�����ͱ���")
    private String bizTypId;

    /**
     * ҵ����������
     */
	@JsonProperty("ҵ����������")
    private String bizTypNm;

    /**
     * �������ͱ���
     */
	@JsonProperty("�������ͱ���")
    private String sellTypId;

    /**
     * ������������
     */
	@JsonProperty("������������")
    private String sellTypNm;

    /**
     * �ɹ����ͱ���
     */
	@JsonProperty("�ɹ����ͱ���")
    private String pursTypId;

    /**
     * �ɹ���������
     */
	@JsonProperty("�ɹ���������")
    private String pursTypNm;

    /**
     * ��Ӧ�̱���
     */
	@JsonProperty("��Ӧ�̱���")
    private String provrId;

    /**
     * ��Ӧ������
     */
	@JsonProperty("��Ӧ������")
    private String provrNm;

    /**
     * �ͻ�����
     */
	@JsonProperty("�ͻ�����")
    private String custId;

    /**
     * �ͻ�����
     */
	@JsonProperty("�ͻ�����")
    private String custNm;

    /**
     * �û�����
     */
	@JsonProperty("�û�����")
    private String accNum;

    /**
     * �û�����
     */
	@JsonProperty("�û�����")
    private String userName;

    /**
     * �Ƿ�������
     */
	@JsonProperty("�Ƿ�������")
    private Integer isNtBookOk;
    
    /**
     * �Ƿ�����ƾ֤
     */
	@JsonProperty("�Ƿ�����ƾ֤")
    private Integer isNtMakeVouch;

    /**
     * ��ע
     */
	@JsonProperty("��ͷ��ע")
    private String memo;
	
	@JsonInclude(Include.NON_NULL)
    private List<FormBookEntrySub> bookEntrySub;
	@JsonIgnore
    private FormBookEntrySub formBookEntrySub;
    
	@JsonProperty("�Ƿ�Ʊ")
    private Integer isNtBllg;//�Ƿ�Ʊ
	@JsonProperty("�Ƿ����")
    private Integer isNtStl;//�Ƿ����
	@JsonProperty("�Ƿ��ϴ�����")
    private Integer isNtUploadHead; //�Ƿ��ϴ�����
	@JsonProperty("������")
    private String  bookOkAcc; //������
	@JsonProperty("����ʱ��")
    private String bookOkDt; //����ʱ��
	@JsonProperty("�շ����ͱ���")
    private String recvSendCateId;//�շ����ͱ���
	@JsonProperty("�շ���������")
    private String recvSendCateNm;//�շ���������
	@JsonProperty("ƾ֤����")
    private String makeVouchId;//ƾ֤����
	@JsonProperty("ƾ֤ժҪ")
    private String makeVouchAbst;//ƾ֤ժҪ
	@JsonProperty("�Ƿ���ĩ����")
    private Integer isMthEndStl; //�Ƿ���ĩ����
	@JsonProperty("���۶�����")
    private String sellOrdrInd;//���۶�����
	@JsonProperty("�Ƶ���")
    private String setupPers;//�Ƶ���
	@JsonProperty("����ʱ��")
    private String setupTm;//����ʱ��
	@JsonProperty("�������ͱ���")
    private String formTypEncd;//�������ͱ���
	@JsonProperty("������������")
    private String formTypName;//������������
	@JsonProperty("��ƾ֤��")
    private String makVouchPers;//��ƾ֤��
	@JsonProperty("��ƾ֤ʱ��")
    private String makVouchTm;//��ƾ֤ʱ��
	@JsonProperty("���ű���")
    private String deptId;//���ű���
	@JsonProperty("��������")
    private String deptName;//��������
    
    private String prdcDt;//��������
    
    
	public String getPrdcDt() {
		return prdcDt;
	}

	public void setPrdcDt(String prdcDt) {
		this.prdcDt = prdcDt;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getMakVouchPers() {
		return makVouchPers;
	}

	public void setMakVouchPers(String makVouchPers) {
		this.makVouchPers = makVouchPers;
	}

	public String getMakVouchTm() {
		return makVouchTm;
	}

	public void setMakVouchTm(String makVouchTm) {
		this.makVouchTm = makVouchTm;
	}
    private static final long serialVersionUID = 1L;

    public String getFormNum() {
        return formNum;
    }

    public void setFormNum(String formNum) {
        this.formNum = formNum;
    }

    public String getFormDt() {
		return formDt;
	}

	public void setFormDt(String formDt) {
		this.formDt = formDt;
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

    public Integer getIsNtMakeVouch() {
        return isNtMakeVouch;
    }

    public void setIsNtMakeVouch(Integer isNtMakeVouch) {
        this.isNtMakeVouch = isNtMakeVouch;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

	public List<FormBookEntrySub> getBookEntrySub() {
		return bookEntrySub;
	}

	public void setBookEntrySub(List<FormBookEntrySub> bookEntrySub) {
		this.bookEntrySub = bookEntrySub;
	}
	
	public FormBookEntrySub getFormBookEntrySub() {
		return formBookEntrySub;
	}

	public void setFormBookEntrySub(FormBookEntrySub formBookEntrySub) {
		this.formBookEntrySub = formBookEntrySub;
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
	
	public Integer getIsNtUploadHead() {
		return isNtUploadHead;
	}

	public void setIsNtUploadHead(Integer isNtUploadHead) {
		this.isNtUploadHead = isNtUploadHead;
	}


	public String getBookOkAcc() {
		return bookOkAcc;
	}

	public void setBookOkAcc(String bookOkAcc) {
		this.bookOkAcc = bookOkAcc;
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

	@Override
	public String toString() {
		return "FormBookEntry [formNum=" + formNum + ", formDt=" + formDt + ", isNtChk=" + isNtChk + ", chkr=" + chkr
				+ ", chkTm=" + chkTm + ", outIntoWhsTypId=" + outIntoWhsTypId + ", outIntoWhsTypNm=" + outIntoWhsTypNm
				+ ", bizTypId=" + bizTypId + ", bizTypNm=" + bizTypNm + ", sellTypId=" + sellTypId + ", sellTypNm="
				+ sellTypNm + ", pursTypId=" + pursTypId + ", pursTypNm=" + pursTypNm + ", provrId=" + provrId
				+ ", provrNm=" + provrNm + ", custId=" + custId + ", custNm=" + custNm + ", accNum=" + accNum
				+ ", userName=" + userName + ", isNtBookOk=" + isNtBookOk + ", isNtMakeVouch=" + isNtMakeVouch
				+ ", memo=" + memo + "]";
	}
    
}