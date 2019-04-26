package com.redread.kaoshi.bean;

import android.databinding.BaseObservable;

import java.io.Serializable;
import java.util.Date;

public class Special extends BaseObservable implements Serializable{
    private Integer id=0;

    private String specialName;

    private String specialDes;

    private String testTime;

    private String count;

    private String status="1";

    private Date createDate;

    private Date updateDate;

    private String questions;

    private boolean type;

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

    public String getTestTime() {
        return testTime;
    }

    public void setTestTime(String testTime) {
        this.testTime = testTime;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
        if(createDate==null)
            this.createDate=new Date();
    }

    public Date getUpdateDate() {

        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
        if(updateDate==null)
            this.updateDate=new Date();
    }

    public String getQuestions() {
        return questions;
    }

    public void setQuestions(String questions) {
        this.questions = questions == null ? null : questions.trim();
    }

    public boolean getType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }


    @Override
    public String toString() {
        return "id="+getId()+"&specialName="+getSpecialName()+"&specialDes="+getSpecialDes()+"&testTime="+getTestTime()+"&count="+getCount()+"&status="+getStatus()+"&questions="+getQuestions()+"&type="+(type?1:0)+"&status="+status+"&createDate="+createDate+"&updateDate="+updateDate;
    }
}