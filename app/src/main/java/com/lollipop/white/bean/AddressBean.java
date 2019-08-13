package com.lollipop.white.bean;

import java.io.Serializable;

/**
 * Created by lollipop on 2019/8/6.
 */

public class AddressBean implements Serializable {

    private String id;
    private String name;        // 姓名
    private String phone;       // 电话
    private String address0;    // 省市区
    private String address1;    // 详细地址
    private int proId;          // 省ID
    private int cityId;         // 城市ID
    private int araeId;         // 区域ID
    private String proName;          // 省Name
    private String cityName;         // 城市Name
    private String araeName;         // 区域Name

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress0() {
        return address0;
    }

    public void setAddress0(String address0) {
        this.address0 = address0;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public int getProId() {
        return proId;
    }

    public void setProId(int proId) {
        this.proId = proId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getAraeId() {
        return araeId;
    }

    public void setAraeId(int araeId) {
        this.araeId = araeId;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getAraeName() {
        return araeName;
    }

    public void setAraeName(String araeName) {
        this.araeName = araeName;
    }
}
