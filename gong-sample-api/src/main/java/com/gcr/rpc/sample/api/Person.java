package com.gcr.rpc.sample.api;

/**
 * Created by gongchunru
 * Date：2017/9/1.
 * Time：14:59
 */
public class Person {

    private String firstName;

    private String lastName;

    public Person(){

    }

    public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
