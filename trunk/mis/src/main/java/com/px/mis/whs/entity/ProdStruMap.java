package com.px.mis.whs.entity;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

//��Ʒ�ṹ���ӱ�
public class ProdStruMap {
    ////	@JsonProperty("���")
//	@JsonIgnore
//	private String ordrNum;// ���
//	@JsonProperty("ĸ������")
//	private String momEncd;// ĸ������
//	@JsonProperty("ĸ������")
//	private String momNm;// ĸ������
//	@JsonProperty("�汾˵��")
//	private String edComnt;// �汾˵��
//	@JsonProperty("ĸ�����")
//	private String momSpc;// ĸ�����
//	@JsonProperty("ĸ����ע")
//	private String memo;// ��ע
////	@JsonProperty("�Ƿ���WMS�ϴ�")
//	@JsonIgnore
//	private Integer isNtWms;// �Ƿ���WMS�ϴ�
//	@JsonProperty("�Ƿ����")
//	private Integer isNtChk;// �Ƿ����
////	@JsonProperty("�Ƿ����")
//	@JsonIgnore
//	private Integer isNtCmplt;// �Ƿ����
////	@JsonProperty("�Ƿ�ر�")
//	@JsonIgnore
//	private Integer isNtClos;// �Ƿ�ر�
////	@JsonProperty("��ӡ����")
//	@JsonIgnore
//	private Integer printCnt;// ��ӡ����
//	@JsonProperty("������")
//	private String setupPers;// ������
//	@JsonProperty("����ʱ��")
//	private String setupTm;// ����ʱ��
//	@JsonProperty("�޸���")
//	private String mdfr;// �޸���
//	@JsonProperty("�޸�ʱ��")
//	private String modiTm;// �޸�ʱ��
//	@JsonProperty("�����")
//	private String chkr;// �����
//	@JsonProperty("���ʱ��")
//	private String chkTm;// ���ʱ��
//	@JsonProperty("ĸ��������λ����")
//	private String measrCorpNm;// ������λ����
//
//	@JsonProperty("�Ӽ�����")
//	private String subEncd;// �Ӽ�����
////	@JsonProperty("���")
//	@JsonIgnore
//	private String ordrNumSub;// ���
//	@JsonProperty("�Ӽ�����")
//	private String subNm;// �Ӽ�����
//	@JsonProperty("�Ӽ����")
//	private String subSpc;// �Ӽ����
//	@JsonProperty("�Ӽ�������λ")
//	private String measrCorp;// ������λ
////	@JsonProperty("���")
//	@JsonIgnore
//	private BigDecimal bxRule;// ���
//	@JsonProperty("�Ӽ�����")
//	private BigDecimal subQty;// �Ӽ�����
//	@JsonProperty("ĸ������")
//	private BigDecimal momQty;// ĸ������
//	@JsonProperty("�Ӽ���ע")
//	private String memos;// ��עs
//	@JsonProperty("�Ӽ�������λ����")
//	private String measrCorpNms;// ������λ����s
    @JsonProperty("ĸ������")
    private String momEncd;//ĸ������
    @JsonIgnore
    private String ordrNum;//���
    @JsonProperty("ĸ������")
    private String momNm;//ĸ������
    @JsonProperty("�汾˵��")
    private String edComnt;//�汾˵��
    @JsonProperty("ĸ�����")
    private String momSpc;//ĸ�����
    @JsonProperty("ĸ����ע")
    private String memo;//��ע
    @JsonIgnore
    private Integer isNtWms;//�Ƿ���WMS�ϴ�
    @JsonProperty("�Ƿ����")
    private Integer isNtChk;//�Ƿ����
    @JsonIgnore
    private Integer isNtCmplt;//�Ƿ����
    @JsonIgnore
    private Integer isNtClos;//�Ƿ�ر�
    @JsonIgnore
    private Integer printCnt;//��ӡ����
    @JsonProperty("������")
    private String setupPers;//������
    @JsonProperty("����ʱ��")
    private String setupTm;//����ʱ��
    @JsonProperty("�޸���")
    private String mdfr;//�޸���
    @JsonProperty("�޸�ʱ��")
    private String modiTm;//�޸�ʱ��
    @JsonProperty("�����")
    private String chkr;//�����
    @JsonProperty("���ʱ��")
    private String chkTm;//���ʱ��
    @JsonProperty("ĸ��������λ����")
    private String measrCorpNm;// ������λ����
    @JsonProperty("ĸ�����")
    private BigDecimal mbxRule;// ���
    @JsonProperty("ĸ��˰��")
    private BigDecimal moptaxRate;// ����˰��
    @JsonProperty("ĸ���ο��ɱ�")
    private BigDecimal mrefCost;// �ο��ɱ�
    @JsonProperty("ĸ����Ӧ������")
    private String mcrspdBarCd;// ��Ӧ������
    @JsonProperty("ĸ���������")
    private String minvtyCd;// �������
    @JsonProperty("ĸ������������")
    private String mbaoZhiQiDt;// ����������
    @JsonProperty("�Ӽ�����")
    private String subEncd;//�Ӽ�����
    @JsonProperty("���")
    private String ordrNumSub;//���
    @JsonProperty("�Ӽ�����")
    private String subNm;//�Ӽ�����
    @JsonProperty("�Ӽ����")
    private String subSpc;//�Ӽ����
//    @JsonIgnore
    @JsonProperty("�Ӽ�������λ����")
    private String measrCorp;//������λ
    @JsonProperty("�Ӽ����")
    private BigDecimal bxRule;//���
    @JsonProperty("�Ӽ�����")
    private BigDecimal subQty;//�Ӽ�����
    @JsonProperty("ĸ������")
    private BigDecimal momQty;//ĸ������
    @JsonProperty("�Ӽ���ע")
    private String smemo;//��ע
    @JsonProperty("�Ӽ�������λ����")
    private String smeasrCorpNm;//������λ����
    @JsonProperty("�Ӽ�����������")
    private String sbaoZhiQiDt;// ����������
    @JsonProperty("�Ӽ�˰��")
    private BigDecimal soptaxRate;// ����˰��
    @JsonProperty("�Ӽ��������")
    private String sinvtyCd;// �������
    @JsonProperty("�Ӽ��ο��ɱ�")
    private BigDecimal srefCost;// �ο��ɱ�
    @JsonProperty("�Ӽ���Ӧ������")
    private String scrspdBarCd;// ��Ӧ������

    public String getMomEncd() {
        return momEncd;
    }

    public void setMomEncd(String momEncd) {
        this.momEncd = momEncd;
    }

    public String getOrdrNum() {
        return ordrNum;
    }

    public void setOrdrNum(String ordrNum) {
        this.ordrNum = ordrNum;
    }

    public String getMomNm() {
        return momNm;
    }

    public void setMomNm(String momNm) {
        this.momNm = momNm;
    }

    public String getEdComnt() {
        return edComnt;
    }

    public void setEdComnt(String edComnt) {
        this.edComnt = edComnt;
    }

    public String getMomSpc() {
        return momSpc;
    }

    public void setMomSpc(String momSpc) {
        this.momSpc = momSpc;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Integer getIsNtWms() {
        return isNtWms;
    }

    public void setIsNtWms(Integer isNtWms) {
        this.isNtWms = isNtWms;
    }

    public Integer getIsNtChk() {
        return isNtChk;
    }

    public void setIsNtChk(Integer isNtChk) {
        this.isNtChk = isNtChk;
    }

    public Integer getIsNtCmplt() {
        return isNtCmplt;
    }

    public void setIsNtCmplt(Integer isNtCmplt) {
        this.isNtCmplt = isNtCmplt;
    }

    public Integer getIsNtClos() {
        return isNtClos;
    }

    public void setIsNtClos(Integer isNtClos) {
        this.isNtClos = isNtClos;
    }

    public Integer getPrintCnt() {
        return printCnt;
    }

    public void setPrintCnt(Integer printCnt) {
        this.printCnt = printCnt;
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

    public String getMdfr() {
        return mdfr;
    }

    public void setMdfr(String mdfr) {
        this.mdfr = mdfr;
    }

    public String getModiTm() {
        return modiTm;
    }

    public void setModiTm(String modiTm) {
        this.modiTm = modiTm;
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

    public String getMeasrCorpNm() {
        return measrCorpNm;
    }

    public void setMeasrCorpNm(String measrCorpNm) {
        this.measrCorpNm = measrCorpNm;
    }

    public BigDecimal getMbxRule() {
        return mbxRule;
    }

    public void setMbxRule(BigDecimal mbxRule) {
        this.mbxRule = mbxRule;
    }

    public BigDecimal getMoptaxRate() {
        return moptaxRate;
    }

    public void setMoptaxRate(BigDecimal moptaxRate) {
        this.moptaxRate = moptaxRate;
    }

    public BigDecimal getMrefCost() {
        return mrefCost;
    }

    public void setMrefCost(BigDecimal mrefCost) {
        this.mrefCost = mrefCost;
    }

    public String getMcrspdBarCd() {
        return mcrspdBarCd;
    }

    public void setMcrspdBarCd(String mcrspdBarCd) {
        this.mcrspdBarCd = mcrspdBarCd;
    }

    public String getMinvtyCd() {
        return minvtyCd;
    }

    public void setMinvtyCd(String minvtyCd) {
        this.minvtyCd = minvtyCd;
    }

    public String getMbaoZhiQiDt() {
        return mbaoZhiQiDt;
    }

    public void setMbaoZhiQiDt(String mbaoZhiQiDt) {
        this.mbaoZhiQiDt = mbaoZhiQiDt;
    }

    public String getSubEncd() {
        return subEncd;
    }

    public void setSubEncd(String subEncd) {
        this.subEncd = subEncd;
    }

    public String getOrdrNumSub() {
        return ordrNumSub;
    }

    public void setOrdrNumSub(String ordrNumSub) {
        this.ordrNumSub = ordrNumSub;
    }

    public String getSubNm() {
        return subNm;
    }

    public void setSubNm(String subNm) {
        this.subNm = subNm;
    }

    public String getSubSpc() {
        return subSpc;
    }

    public void setSubSpc(String subSpc) {
        this.subSpc = subSpc;
    }

    public String getMeasrCorp() {
        return measrCorp;
    }

    public void setMeasrCorp(String measrCorp) {
        this.measrCorp = measrCorp;
    }

    public BigDecimal getBxRule() {
        return bxRule;
    }

    public void setBxRule(BigDecimal bxRule) {
        this.bxRule = bxRule;
    }

    public BigDecimal getSubQty() {
        return subQty;
    }

    public void setSubQty(BigDecimal subQty) {
        this.subQty = subQty;
    }

    public BigDecimal getMomQty() {
        return momQty;
    }

    public void setMomQty(BigDecimal momQty) {
        this.momQty = momQty;
    }

    public String getSmemo() {
        return smemo;
    }

    public void setSmemo(String smemo) {
        this.smemo = smemo;
    }

    public String getSmeasrCorpNm() {
        return smeasrCorpNm;
    }

    public void setSmeasrCorpNm(String smeasrCorpNm) {
        this.smeasrCorpNm = smeasrCorpNm;
    }

    public String getSbaoZhiQiDt() {
        return sbaoZhiQiDt;
    }

    public void setSbaoZhiQiDt(String sbaoZhiQiDt) {
        this.sbaoZhiQiDt = sbaoZhiQiDt;
    }

    public BigDecimal getSoptaxRate() {
        return soptaxRate;
    }

    public void setSoptaxRate(BigDecimal soptaxRate) {
        this.soptaxRate = soptaxRate;
    }

    public String getSinvtyCd() {
        return sinvtyCd;
    }

    public void setSinvtyCd(String sinvtyCd) {
        this.sinvtyCd = sinvtyCd;
    }

    public BigDecimal getSrefCost() {
        return srefCost;
    }

    public void setSrefCost(BigDecimal srefCost) {
        this.srefCost = srefCost;
    }

    public String getScrspdBarCd() {
        return scrspdBarCd;
    }

    public void setScrspdBarCd(String scrspdBarCd) {
        this.scrspdBarCd = scrspdBarCd;
    }
}