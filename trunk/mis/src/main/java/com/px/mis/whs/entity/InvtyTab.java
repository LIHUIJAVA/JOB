package com.px.mis.whs.entity;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.px.mis.purc.entity.IntoWhs;
import com.px.mis.purc.entity.InvtyDoc;
import com.px.mis.purc.entity.SellOutWhs;

//����
public class InvtyTab {
    @JsonIgnore
    private Long ordrNum;// ���
    @JsonProperty("�ֿ����")
    private String whsEncd;// �ֿ����
    // ���������١����ӿ������ִ�������
    @JsonIgnore
    private String tranOutWhsEncd;// ת���ֿ����
    @JsonIgnore
    private String tranInWhsEncd;// ת��ֿ����
    @JsonProperty("�������")
    private String invtyEncd;// �����
    @JsonProperty("����")
    private String batNum;// ����
    @JsonProperty("�ִ�����")
    private BigDecimal nowStok;// �ִ���
    @JsonProperty("��������")
    private BigDecimal avalQty;// ������

    // ����������������
    @JsonIgnore
    private BigDecimal cannibQty;// ��������
    @JsonIgnore
    private BigDecimal recvQty;// ʵ������
    @JsonProperty("��������")
    private String prdcDt;// ��������
    @JsonProperty("������")
    private String baoZhiQi;// ������
    @JsonProperty("ʧЧ����")
    private String invldtnDt;// ʧЧ����
    @JsonProperty("��˰����")
    private BigDecimal cntnTaxUprc;// ��˰����
    @JsonProperty("δ˰����")
    private BigDecimal unTaxUprc;// δ˰����
    //	@JsonProperty("�������")
    @JsonIgnore
    private BigDecimal bookEntryUprc;// ���˵���
    @JsonProperty("˰��")
    private BigDecimal taxRate;// ˰��
    @JsonProperty("��˰���")
    private BigDecimal cntnTaxAmt;// ��˰���
    @JsonProperty("δ˰���")
    private BigDecimal unTaxAmt;// δ˰���
    @JsonIgnore
    private String regnEncd;// �������
    @JsonIgnore
    private String gdsBitEncd;// ��λ����
    @JsonIgnore
    private String gdsBitNm;// ��λ����
    @JsonIgnore
    private List<InvtyDoc> iDocList;

    // ������Ԥ��
    @JsonIgnore
    private InvtyDoc invtyDoc;
    @JsonIgnore
    private WhsDoc whsDoc;

    // �������ˮ��
    @JsonIgnore
    private IntoWhs intoWhs;// �ɹ���
    @JsonIgnore
    private SellOutWhs sellOutWhs;// ���۳�
    @JsonIgnore
    private OthOutIntoWhs outIntoWhs;// ���������

    // hj
    @JsonProperty("�������")
    private String invtyNm; // �������
    @JsonProperty("�ֿ�����")
    private String whsNm; // �ֿ�����

    //	@JsonProperty("������λ����")
    @JsonIgnore
    private String measrCorpNm;// ������λ����
    @JsonIgnore
    private String setupTm;
    //	@JsonProperty("����ͺ�")
    @JsonIgnore
    private String spcModel;// ����ͺ�
    @JsonIgnore
    private BigDecimal bookQty;// ������

    // ��λ��λʵ��
    @JsonIgnore
    private List<MovBitTab> movBitTab;
    @JsonIgnore
    private String barCd;// �̵��λ����

    public String getBarCd() {
        return barCd;
    }

    public void setBarCd(String barCd) {
        this.barCd = barCd;
    }

    public String getGdsBitNm() {
        return gdsBitNm;
    }

    public void setGdsBitNm(String gdsBitNm) {
        this.gdsBitNm = gdsBitNm;
    }

    public final List<MovBitTab> getMovBitTab() {
        return movBitTab;
    }

    public final void setMovBitTab(List<MovBitTab> movBitTab) {
        this.movBitTab = movBitTab;
    }

    public BigDecimal getBookQty() {
        return bookQty;
    }

    public void setBookQty(BigDecimal bookQty) {
        this.bookQty = bookQty;
    }

    public String getSpcModel() {
        return spcModel;
    }

    public void setSpcModel(String spcModel) {
        this.spcModel = spcModel;
    }

    public String getSetupTm() {
        return setupTm;
    }

    public void setSetupTm(String setupTm) {
        this.setupTm = setupTm;
    }

    public String getMeasrCorpNm() {
        return measrCorpNm;
    }

    public void setMeasrCorpNm(String measrCorpNm) {
        this.measrCorpNm = measrCorpNm;
    }

    public String getInvtyNm() {
        return invtyNm;
    }

    public void setInvtyNm(String invtyNm) {
        this.invtyNm = invtyNm;
    }

    public String getWhsNm() {
        return whsNm;
    }

    public void setWhsNm(String whsNm) {
        this.whsNm = whsNm;
    }

    public BigDecimal getCannibQty() {
        return cannibQty;
    }

    public void setCannibQty(BigDecimal cannibQty) {
        this.cannibQty = cannibQty;
    }

    public BigDecimal getRecvQty() {
        return recvQty;
    }

    public void setRecvQty(BigDecimal recvQty) {
        this.recvQty = recvQty;
    }

    public String getTranOutWhsEncd() {
        return tranOutWhsEncd;
    }

    public void setTranOutWhsEncd(String tranOutWhsEncd) {
        this.tranOutWhsEncd = tranOutWhsEncd;
    }

    public String getTranInWhsEncd() {
        return tranInWhsEncd;
    }

    public void setTranInWhsEncd(String tranInWhsEncd) {
        this.tranInWhsEncd = tranInWhsEncd;
    }

    public OthOutIntoWhs getOutIntoWhs() {
        return outIntoWhs;
    }

    public void setOutIntoWhs(OthOutIntoWhs outIntoWhs) {
        this.outIntoWhs = outIntoWhs;
    }

    public IntoWhs getIntoWhs() {
        return intoWhs;
    }

    public void setIntoWhs(IntoWhs intoWhs) {
        this.intoWhs = intoWhs;
    }

    public SellOutWhs getSellOutWhs() {
        return sellOutWhs;
    }

    public void setSellOutWhs(SellOutWhs sellOutWhs) {
        this.sellOutWhs = sellOutWhs;
    }

    public InvtyDoc getInvtyDoc() {
        return invtyDoc;
    }

    public void setInvtyDoc(InvtyDoc invtyDoc) {
        this.invtyDoc = invtyDoc;
    }

    public WhsDoc getWhsDoc() {
        return whsDoc;
    }

    public void setWhsDoc(WhsDoc whsDoc) {
        this.whsDoc = whsDoc;
    }

    public List<InvtyDoc> getiDocList() {
        return iDocList;
    }

    public void setiDocList(List<InvtyDoc> iDocList) {
        this.iDocList = iDocList;
    }

    public BigDecimal getCntnTaxUprc() {
        return cntnTaxUprc;
    }

    public void setCntnTaxUprc(BigDecimal cntnTaxUprc) {
        this.cntnTaxUprc = cntnTaxUprc;
    }

    public BigDecimal getUnTaxUprc() {
        return unTaxUprc;
    }

    public void setUnTaxUprc(BigDecimal unTaxUprc) {
        this.unTaxUprc = unTaxUprc;
    }

    public BigDecimal getBookEntryUprc() {
        return bookEntryUprc;
    }

    public void setBookEntryUprc(BigDecimal bookEntryUprc) {
        this.bookEntryUprc = bookEntryUprc;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    public BigDecimal getCntnTaxAmt() {
        return cntnTaxAmt;
    }

    public void setCntnTaxAmt(BigDecimal cntnTaxAmt) {
        this.cntnTaxAmt = cntnTaxAmt;
    }

    public String getRegnEncd() {
        return regnEncd;
    }

    public void setRegnEncd(String regnEncd) {
        this.regnEncd = regnEncd;
    }

    public String getGdsBitEncd() {
        return gdsBitEncd;
    }

    public void setGdsBitEncd(String gdsBitEncd) {
        this.gdsBitEncd = gdsBitEncd;
    }

    public Long getOrdrNum() {
        return ordrNum;
    }

    public void setOrdrNum(Long ordrNum) {
        this.ordrNum = ordrNum;
    }

    public String getWhsEncd() {
        return whsEncd;
    }

    public void setWhsEncd(String whsEncd) {
        this.whsEncd = whsEncd == null ? null : whsEncd.trim();
    }

    public String getInvtyEncd() {
        return invtyEncd;
    }

    public void setInvtyEncd(String invtyEncd) {
        this.invtyEncd = invtyEncd == null ? null : invtyEncd.trim();
    }

    public String getBatNum() {
        return batNum;
    }

    public void setBatNum(String batNum) {
        this.batNum = batNum == null ? null : batNum.trim();
    }

    public BigDecimal getNowStok() {
        return nowStok;
    }

    public void setNowStok(BigDecimal nowStok) {
        this.nowStok = nowStok;
    }

    public BigDecimal getAvalQty() {
        return avalQty;
    }

    public void setAvalQty(BigDecimal avalQty) {
        this.avalQty = avalQty;
    }

    public String getBaoZhiQi() {
        return baoZhiQi;
    }

    public void setBaoZhiQi(String baoZhiQi) {
        this.baoZhiQi = baoZhiQi;
    }

    public String getPrdcDt() {
        return prdcDt;
    }

    public void setPrdcDt(String prdcDt) {
        this.prdcDt = prdcDt;
    }

    public String getInvldtnDt() {
        return invldtnDt;
    }

    public void setInvldtnDt(String invldtnDt) {
        this.invldtnDt = invldtnDt;
    }

    public BigDecimal getUnTaxAmt() {
        return unTaxAmt;
    }

    public void setUnTaxAmt(BigDecimal unTaxAmt) {
        this.unTaxAmt = unTaxAmt;
    }

}