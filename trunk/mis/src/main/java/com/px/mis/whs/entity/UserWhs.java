package com.px.mis.whs.entity;

//�ֿ��û�
public class UserWhs {
    private Integer id;
    /**
     * �û�����
     */
    private String accNum;
    /**
     * �û�����
     */
    private String userName;

    /**
     * �ֿܲ����
     */

    private String realWhs;

    /**
     * �ֿܲ�����
     */

    private String realNm;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getRealWhs() {
        return realWhs;
    }

    public void setRealWhs(String realWhs) {
        this.realWhs = realWhs;
    }

    public String getRealNm() {
        return realNm;
    }

    public void setRealNm(String realNm) {
        this.realNm = realNm;
    }

}