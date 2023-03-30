package com.example.employeeattendanceapp.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @Copyright : Muhammad Junaid Raza
 * @Developer : Muhammad Junaid Raza
 */

public class LineManagerData implements Serializable {

    @SerializedName("Id")
    public String lineManagerId;

    @SerializedName("Name")
    public String lineManagerName;

}
