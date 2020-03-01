package com.px.mis.whs.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author zhangxiaoyu
 * @version 创建时间：2018年11月15日 下午7:33:53
 * @ClassName 类名称
 * @Description 失效日期=生产日期+保质期
 */
public class InvldtnDt {

    /*releaseDate  生产日期
      day          保质期*/
    public String getDate(String releaseDate, int day) {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();//获取当前时间

        Date d = new Date();
        try {
            calendar.setTime(df.parse(releaseDate));//字符串转日期,再设置calendar的时间
        } catch (ParseException e) {

        }
        //calendar.add(Calendar.DATE, day);//这句话可以替代下面的两行
        int day1 = calendar.get(Calendar.DAY_OF_YEAR);//返回给定日历字段的值
        calendar.set(Calendar.DAY_OF_YEAR, day1 + day);//将给定的日历字段设置为给定值
        d = calendar.getTime();//返回表示calendar的时间值的 Date 对象
        String date = df.format(d);//日期转字符串

        return date;//失效日期
    }

}
