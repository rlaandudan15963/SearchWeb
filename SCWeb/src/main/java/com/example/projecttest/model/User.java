package com.example.projecttest.model;

import java.util.List;

public class User {
    private String userId;
    private String password;
    private String email;
    private String name;
    private int age;
    private String phoneNum;
    private List<Friend> friends; // 친구 목록 추가

    // 기본 생성자
    public User() {}

    // 모든 필드를 사용하는 생성자
    public User(String userId, String password, String email, String name, int age, String phoneNum, List<Friend> friends) {
        this.userId = userId;
        this.password = password;
        this.email = email;
        this.name = name;
        this.age = age;
        this.phoneNum = phoneNum;
        this.friends = friends;
    }

    // Getter 및 Setter 메서드
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public List<Friend> getFriends() {
        return friends;
    }

    public void setFriends(List<Friend> friends) {
        this.friends = friends;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", phoneNum='" + phoneNum + '\'' +
                ", friends=" + friends +
                '}';
    }
}