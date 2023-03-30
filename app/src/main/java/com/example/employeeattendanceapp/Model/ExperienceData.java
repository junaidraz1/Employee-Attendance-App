package com.example.employeeattendanceapp.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @Copyright : Muhammad Junaid Raza
 * @Developer : Muhammad Junaid Raza
 */

public class ExperienceData implements Serializable {

    @SerializedName("Id")
    public String expId;

    @SerializedName("Title")
    public String expTitle;

    @SerializedName("UserId")
    public String expUserId;

    @SerializedName("CompanyId")
    public String expCompanyId;

    @SerializedName("CompanyName")
    public String expCompanyName;

    @SerializedName("CompanyLocationId")
    public String expCompanyLocationId;

    @SerializedName("CompanyLocatioName")
    public String expCompanyLocatioName;

    @SerializedName("FromDate")
    public String expFromDate;

    @SerializedName("ToDate")
    public String expToDate;

    @SerializedName("IsCurrent")
    public String expIsCurrent;

    @SerializedName("Description")
    public String expDescription;

    @SerializedName("Path")
    public String expAttachmentPath;


}
