package com.vila.testmobileintive.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Result
{
    @SerializedName("results")
    @Expose
    private List<Person> personList;

    public Result(){}

    public List<Person> getPersonList()
    {
        return personList;
    }

    public void setPersonList(List<Person> personList)
    {
        this.personList = personList;
    }
}
