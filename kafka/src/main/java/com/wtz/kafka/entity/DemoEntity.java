package com.wtz.kafka.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author tiezhu
 * Company dtstack
 * Date 2020/8/12 星期三
 */
public class DemoEntity {
    public String phoneName;
    public String os;
    public String city;
    public String loginTime;
    public String errorMessage;

    public DemoEntity() {
    }

    public DemoEntity(String phoneName, String os, String city, String loginTime) {
        this.phoneName = phoneName;
        this.os = os;
        this.city = city;
        this.loginTime = loginTime;
    }

    public DemoEntity(String phoneName, String os, String city, String loginTime, String errorMessage) {
        this.phoneName = phoneName;
        this.os = os;
        this.city = city;
        this.loginTime = loginTime;
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "Entity{" +
                "phoneName='" + phoneName + '\'' +
                ", os='" + os + '\'' +
                ", city='" + city + '\'' +
                ", loginTime='" + loginTime + '\'' +
                '}';
    }

    public String getPhoneName() {
        return phoneName;
    }

    public void setPhoneName(String phoneName) {
        this.phoneName = phoneName;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }

    public void setErrorMessage(String errorMessage) {this.errorMessage = errorMessage;}

    public static DemoEntity createEntity() {
        DemoEntity entity = new DemoEntity();
        //手机信号
        String[] phoneArray = {"iPhone", "HUAWEI", "xiaomi", "moto", "vivo"};
        //操作系统
        String[] osArray = {"Android 7.0", "Mac OS", "Apple Kernel", "Windows", "kylin OS", "chrome"};
        //城市
        String[] cityArray = {"北京", "上海", "杭州", "南京", "西藏", "西安", "合肥", "葫芦岛"};
        //随机产生一个手机型号
        int k = (int) (Math.random() * 5);
        String phoneName = phoneArray[k];
        //随机产生一个os
        int m = (int) (Math.random() * 6);
        String os = osArray[m];
        //随机产生一个城市地点
        int n = (int) (Math.random() * 8);
        String city = cityArray[n];
        //时间戳，存当前时间
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String loginTime = sf.format(new Date());
        //加载数据到实体中
        entity.setCity(city);
        entity.setLoginTime(loginTime);
        entity.setOs(os);
        entity.setPhoneName(phoneName);
        return entity;
    }

}
