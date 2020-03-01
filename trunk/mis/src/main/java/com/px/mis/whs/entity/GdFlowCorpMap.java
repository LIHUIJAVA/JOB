package com.px.mis.whs.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

//������˾
public class GdFlowCorpMap {
//	private String gdFlowEncd;// ������˾���
//
//	private String gdFlowNm;// ������˾����
//
//	private String traffMode;// ���䷽ʽ
//
//	private String traffVehic;// ���䳵��
//
//	private String traffFormNum;// ���䵥��
//
//	private Integer printCnt;// ��ӡ����
//
//	private String memo;// ��ע
//
//	private Integer isNtChk;// �Ƿ����
//
//	private String setupPers;// ������
//
//	private String setupTm;// ����ʱ��
//
//	private String mdfr;// �޸���
//
//	private String modiTm;// �޸�ʱ��
//
//	private String chkr;// �����
//
//	private String chkTm;// ���ʱ��

    @JsonProperty("������˾����")
    private String gdFlowEncd;// ������˾���
    @JsonProperty("������˾����")
    private String gdFlowNm;// ������˾����
    @JsonProperty("���䷽ʽ")
    private String traffMode;// ���䷽ʽ
    @JsonProperty("���䳵��")
    private String traffVehic;// ���䳵��
    //	@JsonProperty("���䵥��")
    @JsonIgnore
    private String traffFormNum;// ���䵥��
    //	@JsonProperty("��ӡ����")
    @JsonIgnore
    private Integer printCnt;// ��ӡ����
    @JsonProperty("��ע")
    private String memo;// ��ע
    //	@JsonProperty("�Ƿ����")
    @JsonIgnore
    private Integer isNtChk;// �Ƿ����
    @JsonProperty("������")
    private String setupPers;// ������
    @JsonProperty("����ʱ��")
    private String setupTm;// ����ʱ��
    @JsonProperty("�޸���")
    private String mdfr;// �޸���
    @JsonProperty("�޸�ʱ��")
    private String modiTm;// �޸�ʱ��
    //	@JsonProperty("�����")
    @JsonIgnore
    private String chkr;// �����
    //	@JsonProperty("���ʱ��")
    @JsonIgnore
    private String chkTm;// ���ʱ��

    public Integer getPrintCnt() {
        return printCnt;
    }

    public void setPrintCnt(Integer printCnt) {
        this.printCnt = printCnt;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Integer getIsNtChk() {
        return isNtChk;
    }

    public void setIsNtChk(Integer isNtChk) {
        this.isNtChk = isNtChk;
    }

    public String getGdFlowEncd() {
        return gdFlowEncd;
    }

    public void setGdFlowEncd(String gdFlowEncd) {
        this.gdFlowEncd = gdFlowEncd == null ? null : gdFlowEncd.trim();
    }

    public String getGdFlowNm() {
        return gdFlowNm;
    }

    public void setGdFlowNm(String gdFlowNm) {
        this.gdFlowNm = gdFlowNm == null ? null : gdFlowNm.trim();
    }

    public String getTraffMode() {
        return traffMode;
    }

    public void setTraffMode(String traffMode) {
        this.traffMode = traffMode == null ? null : traffMode.trim();
    }

    public String getTraffVehic() {
        return traffVehic;
    }

    public void setTraffVehic(String traffVehic) {
        this.traffVehic = traffVehic == null ? null : traffVehic.trim();
    }

    public String getTraffFormNum() {
        return traffFormNum;
    }

    public void setTraffFormNum(String traffFormNum) {
        this.traffFormNum = traffFormNum == null ? null : traffFormNum.trim();
    }

    public String getSetupPers() {
        return setupPers;
    }

    public void setSetupPers(String setupPers) {
        this.setupPers = setupPers == null ? null : setupPers.trim();
    }

    public String getMdfr() {
        return mdfr;
    }

    public void setMdfr(String mdfr) {
        this.mdfr = mdfr;
    }

    public String getChkr() {
        return chkr;
    }

    public void setChkr(String chkr) {
        this.chkr = chkr;
    }

    public String getSetupTm() {
        return setupTm;
    }

    public void setSetupTm(String setupTm) {
        this.setupTm = setupTm;
    }

    public String getModiTm() {
        return modiTm;
    }

    public void setModiTm(String modiTm) {
        this.modiTm = modiTm;
    }

    public String getChkTm() {
        return chkTm;
    }

    public void setChkTm(String chkTm) {
        this.chkTm = chkTm;
    }

}