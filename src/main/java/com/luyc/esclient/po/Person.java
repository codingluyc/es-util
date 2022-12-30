package com.luyc.esclient.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.luyc.esclient.common.BaseEntity;
import com.luyc.esclient.common.Index;

import java.time.LocalDate;

/**
 * @author luyc
 * @since 2022/10/12 15:10
 */
public class Person extends BaseEntity {


    @JsonProperty(value = "name")
    private String name;

    @JsonProperty(value = "num")
    private String num;

    @JsonProperty(value = "moto")
    private String moto;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty(value = "birthday")
    private LocalDate birthday;

    @JsonProperty(value = "height")
    private double height;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getMoto() {
        return moto;
    }

    public void setMoto(String motto) {
        this.moto = motto;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", moto='" + moto + '\'' +
                ", birthday=" + birthday +
                ", height=" + height +
                '}';
    }
}
