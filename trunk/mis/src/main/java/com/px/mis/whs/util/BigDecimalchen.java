package com.px.mis.whs.util;

import java.math.BigDecimal;

/**
 * @author zhangxiaoyu
 * @version ����ʱ�䣺2018��11��16�� ����10:09:45
 * @ClassName ������
 * @Description ������
 */
public class BigDecimalchen {

    /**
     * �ṩ��ȷ�˷������mul����
     *
     * @param value1 ������
     * @param value2 ����
     * @return ���������Ļ�
     */
    public static double decimalNum(BigDecimal value1, BigDecimal value2) {

        return value1.multiply(value2).doubleValue();
    }


    public static double mul(double value1, double value2) {

        BigDecimal b1 = new BigDecimal(Double.valueOf(value1));
        BigDecimal b2 = new BigDecimal(Double.valueOf(value2));
        return b1.multiply(b2).doubleValue();
    }


    /**
     * �ṩ��ȷ�ӷ������add����
     *
     * @param value1 ������
     * @param value2 ����
     * @return ���������ĺ�
     */

    public static double decimaljia(BigDecimal value1, BigDecimal value2) {

        return value1.add(value2).doubleValue();
    }


    public static double add(double value1, double value2) {
        BigDecimal b1 = new BigDecimal(Double.valueOf(value1));
        BigDecimal b2 = new BigDecimal(Double.valueOf(value2));
        return b1.add(b2).doubleValue();
    }

    /**
     * �ṩ��ȷ���������sub����
     *
     * @param value1 ������
     * @param value2 ����
     * @return ���������Ĳ�
     */

    public static double decimaljian(BigDecimal value1, BigDecimal value2) {

        return value1.subtract(value2).doubleValue();
    }

    public static double sub(double value1, double value2) {
        BigDecimal b1 = new BigDecimal(Double.valueOf(value1));
        BigDecimal b2 = new BigDecimal(Double.valueOf(value2));
        return b1.subtract(b2).doubleValue();
    }

    /**
     * �ṩ��ȷ�ĳ������㷽��div
     *
     * @param value1 ������
     * @param value2 ����
     * @param scale  ��ȷ��Χ
     * @return ������������
     * @throws IllegalAccessException
     */
    public static double div(double value1, double value2, int scale) throws IllegalAccessException {
        //�����ȷ��ΧС��0���׳��쳣��Ϣ
        if (scale < 0) {
            throw new IllegalAccessException("��ȷ�Ȳ���С��0");
        }
        BigDecimal b1 = new BigDecimal(Double.valueOf(value1));
        BigDecimal b2 = new BigDecimal(Double.valueOf(value2));
        return b1.divide(b2, scale).doubleValue();
    }

    public static double decimalchu(BigDecimal value1, BigDecimal value2) {

        return value1.divide(value2).doubleValue();
    }


}
