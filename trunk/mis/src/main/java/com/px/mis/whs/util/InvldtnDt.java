package com.px.mis.whs.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author zhangxiaoyu
 * @version ����ʱ�䣺2018��11��15�� ����7:33:53
 * @ClassName ������
 * @Description ʧЧ����=��������+������
 */
public class InvldtnDt {

    /*releaseDate  ��������
      day          ������*/
    public String getDate(String releaseDate, int day) {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();//��ȡ��ǰʱ��

        Date d = new Date();
        try {
            calendar.setTime(df.parse(releaseDate));//�ַ���ת����,������calendar��ʱ��
        } catch (ParseException e) {

        }
        //calendar.add(Calendar.DATE, day);//��仰����������������
        int day1 = calendar.get(Calendar.DAY_OF_YEAR);//���ظ��������ֶε�ֵ
        calendar.set(Calendar.DAY_OF_YEAR, day1 + day);//�������������ֶ�����Ϊ����ֵ
        d = calendar.getTime();//���ر�ʾcalendar��ʱ��ֵ�� Date ����
        String date = df.format(d);//����ת�ַ���

        return date;//ʧЧ����
    }

}
