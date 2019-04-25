package com.redread.kaoshi.bean;

import java.io.Serializable;
import java.util.Date;

public class Special implements Serializable{
    private Integer id;

    private String specialName;

    private String specialDes;

    private Integer testTime;

    private Integer count;

    private Integer status;

    private Date createDate;

    private Date updateDate;

    private String questions;

    private Integer type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSpecialName() {
        return specialName;
    }

    public void setSpecialName(String specialName) {
        this.specialName = specialName == null ? null : specialName.trim();
    }

    public String getSpecialDes() {
        return specialDes;
    }

    public void setSpecialDes(String specialDes) {
        this.specialDes = specialDes == null ? null : specialDes.trim();
    }

    public Integer getTestTime() {
        return testTime;
    }

    public void setTestTime(Integer testTime) {
        this.testTime = testTime;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getQuestions() {
        return questions;
    }

    public void setQuestions(String questions) {
        this.questions = questions == null ? null : questions.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}