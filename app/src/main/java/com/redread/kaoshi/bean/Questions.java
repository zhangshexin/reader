package com.redread.kaoshi.bean;

import java.io.Serializable;
import java.util.Date;

public class Questions implements Serializable{
    //--------------新填本地用的字段-------start-----
    private boolean did=false;//是否作过了
    private int rightOrWrong=0;//如果打对了是1，答错了是0，不答默认为0

    public boolean isDid() {
        return did;
    }

    public void setDid(boolean did) {
        this.did = did;
    }

    public int getRightOrWrong() {
        return rightOrWrong;
    }

    public void setRightOrWrong(int rightOrWrong) {
        this.rightOrWrong = rightOrWrong;
    }

    private boolean checked;

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
    //-------end-----
    private Integer id=0;

    private Integer specialId;

    private String question;

    private String answer;//答案，json形式,例：{“all”:”a,bbbb,ddd,ddd”,”right”:”a”},all对应所有给出的答案，right对应正确答案（可有多个），都以逗号隔开

    private Integer status=1;

    private Date createDate=new Date();

    private Date updateDate=new Date();

    private Integer type=1;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSpecialId() {
        return specialId;
    }

    public void setSpecialId(Integer specialId) {
        this.specialId = specialId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question == null ? null : question.trim();
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer == null ? null : answer.trim();
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}