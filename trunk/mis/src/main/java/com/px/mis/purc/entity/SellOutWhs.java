package com.px.mis.purc.entity;

import java.util.List;

public class SellOutWhs {
	
    private String outWhsId;//������
    private String outWhsDt;//��������
    private String outWhsDt1;//��������
    private String outWhsDt2;//��������
    private String formTypEncd;//�������ͱ���
    private String accNum;//�û����
    private String userName;//�û�����
    private String deptId;//���ű��
    private String deptName;//��������
    private String custId;//�ͻ����
    private String bizTypId;//ҵ�����ͱ��
    private String sellTypId;//�������ͱ��
    private String recvSendCateId;//�շ����ͱ��
    private String outIntoWhsTypId;//��������ͱ��
    private String sellOrdrInd;//���۶�����ʶ
    private Integer isNtChk;//�Ƿ����
    private String chkr;//�����
    private String chkTm;//���ʱ��
    private Integer isNtBookEntry;//�Ƿ����
    private String bookEntryPers;//������
    private String bookEntryTm;//����ʱ��
    private String setupPers;//������
    private String setupTm;//����ʱ��
    private String mdfr;//�޸���
    private String modiTm;//�޸�ʱ��
    private Integer isNtEntrsAgn;//�Ƿ�ί�д��� //0��ʾ���۳���  1��ʾί�д��� 
    private String dealStat;//����״̬��1.������� 0.�����У�
    private String memo;//��ע
    private String rtnGoodsId;//�˻�����ţ���ʱ���ã�����sellOrdrInd��
    private Integer isNtBllg;//�Ƿ�Ʊ
    private Integer isNtStl;//�Ƿ����
    private String custOrdrNum;//�ͻ�������
    private Integer isNtRtnGood;//�Ƿ����˻�
    private String returnMemo;//���ձ�ע
    
    private List<SellOutWhsSub> sellOutWhsSub;//���۳��ⵥ�ӱ�
    private List<SellOutWhsSub> sList;
  //������ѯ�ͻ����ơ��û����ơ��������ơ������������ơ��շ�������ơ�ҵ����������
    private String custNm;//�ͻ�����
    private String sellTypNm;//������������
    private String recvSendCateNm;//�շ��������
    private String outIntoWhsTypNm;//������������
    private String bizTypNm;//ҵ����������
    
    //�������ˮ��
    private String formDt1;//����������
    private String formDt2;//����������
    private String formNum;//���ݺ�
    private String chkTm1;//���������
    private String chkTm2;//���������
    private String formTypName;//������������
    private String toFormTypEncd;//��Դ�������ͱ���
    private Integer isNtMakeVouch;//�Ƿ�����ƾ֤
    private String makVouchPers;//��ƾ֤��
    private String makVouchTm;//��ƾ֤ʱ��
    private String custDelv;//�ͻ���ַ
    private String custTel;//�ͻ��绰
    private String expressNum;//���۳��ⵥ
    

    
    public String getExpressNum() {
		return expressNum;
	}

	public void setExpressNum(String expressNum) {
		this.expressNum = expressNum;
	}

	public String getCustTel() {
		return custTel;
	}

	public void setCustTel(String custTel) {
		this.custTel = custTel;
	}

	public String getCustDelv() {
		return custDelv;
	}

	public void setCustDelv(String custDelv) {
		this.custDelv = custDelv;
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

	public Integer getIsNtMakeVouch() {
		return isNtMakeVouch;
	}

	public void setIsNtMakeVouch(Integer isNtMakeVouch) {
		this.isNtMakeVouch = isNtMakeVouch;
	}

	public String getToFormTypEncd() {
		return toFormTypEncd;
	}

	public void setToFormTypEncd(String toFormTypEncd) {
		this.toFormTypEncd = toFormTypEncd;
	}

	public String getFormTypName() {
		return formTypName;
	}

	public void setFormTypName(String formTypName) {
		this.formTypName = formTypName;
	}

	public String getFormTypEncd() {
		return formTypEncd;
	}

	public void setFormTypEncd(String formTypEncd) {
		this.formTypEncd = formTypEncd;
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

	public Integer getIsNtRtnGood() {
		return isNtRtnGood;
	}

	public void setIsNtRtnGood(Integer isNtRtnGood) {
		this.isNtRtnGood = isNtRtnGood;
	}

	public Integer getIsNtBllg() {
		return isNtBllg;
	}

	public void setIsNtBllg(Integer isNtBllg) {
		this.isNtBllg = isNtBllg;
	}

	public String getCustOrdrNum() {
		return custOrdrNum;
	}

	public void setCustOrdrNum(String custOrdrNum) {
		this.custOrdrNum = custOrdrNum;
	}

	public Integer getIsNtStl() {
		return isNtStl;
	}

	public void setIsNtStl(Integer isNtStl) {
		this.isNtStl = isNtStl;
	}

	public String getOutWhsDt1() {
		return outWhsDt1;
	}

	public void setOutWhsDt1(String outWhsDt1) {
		this.outWhsDt1 = outWhsDt1;
	}

	public String getOutWhsDt2() {
		return outWhsDt2;
	}

	public void setOutWhsDt2(String outWhsDt2) {
		this.outWhsDt2 = outWhsDt2;
	}

	public String getRtnGoodsId() {
		return rtnGoodsId;
	}

	public void setRtnGoodsId(String rtnGoodsId) {
		this.rtnGoodsId = rtnGoodsId;
	}

	public String getReturnMemo() {
		return returnMemo;
	}

	public void setReturnMemo(String returnMemo) {
		this.returnMemo = returnMemo;
	}

	public List<SellOutWhsSub> getSellOutWhsSub() {
		return sellOutWhsSub;
	}

	public String getDealStat() {
		return dealStat;
	}

	public void setDealStat(String dealStat) {
		this.dealStat = dealStat;
	}

	public void setSellOutWhsSub(List<SellOutWhsSub> sellOutWhsSub) {
		this.sellOutWhsSub = sellOutWhsSub;
	}

	public String getOutWhsId() {
        return outWhsId;
    }

    public void setOutWhsId(String outWhsId) {
        this.outWhsId = outWhsId == null ? null : outWhsId.trim();
    }

    public String getOutIntoWhsTypId() {
		return outIntoWhsTypId;
	}

	public void setOutIntoWhsTypId(String outIntoWhsTypId) {
		this.outIntoWhsTypId = outIntoWhsTypId;
	}

	public String getOutWhsDt() {
        return outWhsDt;
    }

    public void setOutWhsDt(String outWhsDt) {
        this.outWhsDt = outWhsDt;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId == null ? null : custId.trim();
    }

    public String getBizTypId() {
        return bizTypId;
    }

    public void setBizTypId(String bizTypId) {
        this.bizTypId = bizTypId == null ? null : bizTypId.trim();
    }

    public String getRecvSendCateId() {
        return recvSendCateId;
    }

    public void setRecvSendCateId(String recvSendCateId) {
        this.recvSendCateId = recvSendCateId == null ? null : recvSendCateId.trim();
    }

    public String getSellOrdrInd() {
        return sellOrdrInd;
    }

    public void setSellOrdrInd(String sellOrdrInd) {
        this.sellOrdrInd = sellOrdrInd == null ? null : sellOrdrInd.trim();
    }

    public String getChkr() {
        return chkr;
    }

    public void setChkr(String chkr) {
        this.chkr = chkr == null ? null : chkr.trim();
    }

    public String getChkTm() {
        return chkTm;
    }

    public void setChkTm(String chkTm) {
        this.chkTm = chkTm;
    }

    public Integer getIsNtChk() {
		return isNtChk;
	}

	public void setIsNtChk(Integer isNtChk) {
		this.isNtChk = isNtChk;
	}

	public Integer getIsNtBookEntry() {
		return isNtBookEntry;
	}

	public void setIsNtBookEntry(Integer isNtBookEntry) {
		this.isNtBookEntry = isNtBookEntry;
	}

	public String getBookEntryPers() {
        return bookEntryPers;
    }

    public void setBookEntryPers(String bookEntryPers) {
        this.bookEntryPers = bookEntryPers == null ? null : bookEntryPers.trim();
    }

    public String getBookEntryTm() {
		return bookEntryTm;
	}

	public void setBookEntryTm(String bookEntryTm) {
		this.bookEntryTm = bookEntryTm;
	}

	public String getSetupPers() {
        return setupPers;
    }

    public void setSetupPers(String setupPers) {
        this.setupPers = setupPers == null ? null : setupPers.trim();
    }

    public String getSetupTm() {
        return setupTm;
    }

    public void setSetupTm(String setupTm) {
        this.setupTm = setupTm;
    }

    public String getMdfr() {
        return mdfr;
    }

    public void setMdfr(String mdfr) {
        this.mdfr = mdfr == null ? null : mdfr.trim();
    }

    public String getModiTm() {
        return modiTm;
    }

    public void setModiTm(String modiTm) {
        this.modiTm = modiTm;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }

	public String getAccNum() {
		return accNum;
	}

	public void setAccNum(String accNum) {
		this.accNum = accNum;
	}

	public String getSellTypId() {
		return sellTypId;
	}

	public void setSellTypId(String sellTypId) {
		this.sellTypId = sellTypId;
	}
	
	public Integer getIsNtEntrsAgn() {
		return isNtEntrsAgn;
	}

	public void setIsNtEntrsAgn(Integer isNtEntrsAgn) {
		this.isNtEntrsAgn = isNtEntrsAgn;
	}

	public List<SellOutWhsSub> getsList() {
		return sList;
	}

	public void setsList(List<SellOutWhsSub> sList) {
		this.sList = sList;
	}

	public String getFormDt1() {
		return formDt1;
	}

	public void setFormDt1(String formDt1) {
		this.formDt1 = formDt1;
	}

	public String getFormDt2() {
		return formDt2;
	}

	public void setFormDt2(String formDt2) {
		this.formDt2 = formDt2;
	}

	public String getFormNum() {
		return formNum;
	}

	public void setFormNum(String formNum) {
		this.formNum = formNum;
	}

	public String getChkTm1() {
		return chkTm1;
	}

	public void setChkTm1(String chkTm1) {
		this.chkTm1 = chkTm1;
	}

	public String getChkTm2() {
		return chkTm2;
	}

	public void setChkTm2(String chkTm2) {
		this.chkTm2 = chkTm2;
	}

	public String getOutIntoWhsTypNm() {
		return outIntoWhsTypNm;
	}

	public void setOutIntoWhsTypNm(String outIntoWhsTypNm) {
		this.outIntoWhsTypNm = outIntoWhsTypNm;
	}

	public String getCustNm() {
		return custNm;
	}

	public void setCustNm(String custNm) {
		this.custNm = custNm;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getSellTypNm() {
		return sellTypNm;
	}

	public void setSellTypNm(String sellTypNm) {
		this.sellTypNm = sellTypNm;
	}

	public String getRecvSendCateNm() {
		return recvSendCateNm;
	}

	public void setRecvSendCateNm(String recvSendCateNm) {
		this.recvSendCateNm = recvSendCateNm;
	}

	public String getBizTypNm() {
		return bizTypNm;
	}

	public void setBizTypNm(String bizTypNm) {
		this.bizTypNm = bizTypNm;
	}

    
}