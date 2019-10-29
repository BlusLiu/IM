package com.example.factory.model.db;

import com.example.factory.model.Author;
import com.example.factory.utils.DiffUiDataCallback;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.Date;
import java.util.Objects;

/**
 * @Author: liuzhen
 * @Description:
 * @Date: Create in 13:47 2019/10/19
 */
@Table(database = AppDatabase.class)
public class User extends BaseDbModel<User> implements Author{


    @PrimaryKey
    private String id;
    @Column
    private String name;
    @Column
    private String phone;
    @Column
    private String portrait;
    @Column
    private String alias;
    @Column
    private String desc;
    // man = 1, woman = 0
    @Column
    private int sex = 0;
    @Column
    private Boolean isFollow = false;
    @Column
    private int follows;
    @Column
    private int following;
    @Column
    private Date modifyAt;

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

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public Boolean isFollow() {
        return isFollow;
    }

    public void setFollow(Boolean follow) {
        isFollow = follow;
    }

    public int getFollows() {
        return follows;
    }

    public void setFollows(int follows) {
        this.follows = follows;
    }

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }

    public Date getModifyAt() {
        return modifyAt;
    }

    public void setModifyAt(Date modifyAt) {
        this.modifyAt = modifyAt;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", portrait='" + portrait + '\'' +
                ", alias='" + alias + '\'' +
                ", desc='" + desc + '\'' +
                ", sex=" + sex +
                ", isFollow=" + isFollow +
                ", follows=" + follows +
                ", following=" + following +
                ", modifyAt=" + modifyAt +
                '}';
    }



    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return sex == user.sex &&
                follows == user.follows &&
                following == user.following &&
                Objects.equals(id, user.id) &&
                Objects.equals(name, user.name) &&
                Objects.equals(phone, user.phone) &&
                Objects.equals(portrait, user.portrait) &&
                Objects.equals(alias, user.alias) &&
                Objects.equals(desc, user.desc) &&
                Objects.equals(isFollow, user.isFollow) &&
                Objects.equals(modifyAt, user.modifyAt);
    }

    @Override
    public boolean isSame(User old) {
        User user = old;
        return this == old || Objects.equals(id, user.id);
    }

    @Override
    public boolean isUiContentSame(User old) {
        return this == old || this.hashCode() == old.hashCode();
    }

}
