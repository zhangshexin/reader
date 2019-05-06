package com.redread.kaoshi.bean;

import java.util.Date;
import java.util.List;

public class Grade {
    private String phoneNum;

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }
    private List<Grade> topN;//排位前多少的成绩

    public List<Grade> getTopN() {
        return topN;
    }

    public void setTopN(List<Grade> topN) {
        this.topN = topN;
    }
    //---------------------
    private Integer idt;

    private Integer userId;

    private Integer grade;

    private Date createDate;

    private Date updataDate;

    private Integer status=1;

    private Integer specialId;//专题id

    public Integer getIdt() {
        return idt;
    }

    public void setIdt(Integer idt) {
        this.idt = idt;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdataDate() {
        return updataDate;
    }

    public void setUpdataDate(Date updataDate) {
        this.updataDate = updataDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getSpecialId() {
        return specialId;
    }

    public void setSpecialId(Integer specialId) {
        this.specialId = specialId;
    }

    private int rank;//排名

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}