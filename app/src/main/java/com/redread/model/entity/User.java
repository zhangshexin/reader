package com.redread.model.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by zhangshexin on 2019/4/19.
 * 用户信息
 */
@Entity
public class User {
    @Id(autoincrement = true)
    private long id;
    private String role;
    private String phone;
    private String name;
    @Generated(hash = 265189822)
    public User(long id, String role, String phone, String name) {
        this.id = id;
        this.role = role;
        this.phone = phone;
        this.name = name;
    }
    @Generated(hash = 586692638)
    public User() {
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getRole() {
        return this.role;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public String getPhone() {
        return this.phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
