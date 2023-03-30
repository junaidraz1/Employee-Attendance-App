package com.example.employeeattendanceapp.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @Copyright : Muhammad Junaid Raza
 * @Developer : Muhammad Junaid Raza
 */

public class GenderData implements Serializable {

    @SerializedName("Id")
    public String genderId;

    @SerializedName("Name")
    public String genderName;
}
