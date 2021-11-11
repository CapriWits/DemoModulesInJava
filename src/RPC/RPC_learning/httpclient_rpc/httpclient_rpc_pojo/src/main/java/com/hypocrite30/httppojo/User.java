package com.hypocrite30.httppojo;


import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * @Author: Hypocrite30
 * @Date: 2021/11/11 22:01
 */
public class User implements Serializable {
    private String name;
    private String password;
    private Date birth;

    public int getAge() {
        if (birth == null) {
            return -1;
        }
        int birthYear = birth.getYear();
        int currentYear = new Date().getYear();
        return currentYear - birthYear;
    }

    public void setAge(int age) {
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    @Override
    public String toString() {
        return "{\"name\":\"" + name + "\", \"password\":\"" + password + "\"}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(name, user.name) &&
                Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, password);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
