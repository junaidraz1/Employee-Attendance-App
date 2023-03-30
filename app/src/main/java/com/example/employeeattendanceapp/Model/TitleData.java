package com.example.employeeattendanceapp.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @Copyright : Muhammad Junaid Raza
 * @Developer : Muhammad Junaid Raza
 */

public class TitleData implements Serializable {

    @SerializedName("Id")
    public String titleId;

    @SerializedName("Name")
    public String titleName;
}
