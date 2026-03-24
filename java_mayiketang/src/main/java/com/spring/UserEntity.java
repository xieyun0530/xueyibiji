package com.spring;

/**
 * Created by vn0790t on 2020/6/2.
 */
public class UserEntity {
    private Integer userId;
    private String userName;

    public UserEntity(Integer userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                '}';
    }
}
