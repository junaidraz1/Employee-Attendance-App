package com.example.employeeattendanceapp.Model.Request;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UpdateProfileRequest implements Serializable {

    @SerializedName("UserId")
    public String UserId;

    @SerializedName("CNICNumber")
    public String CNICNumber;

    @SerializedName("UserName")
    public String UserName;

    @SerializedName("PersonTitleId")
    public String PersonTitleId;

    @SerializedName("Prefix")
    public String Prefix;

    @SerializedName("FirstName")
    public String FirstName;

    @SerializedName("MiddleName")
    public String MiddleName;

    @SerializedName("LastName")
    public String LastName;

    @SerializedName("GenderId")
    public String GenderId;

    @SerializedName("RelationshipTypeId")
    public String RelationshipTypeId;

    @SerializedName("GuardianName")
    public String GuardianName;

    @SerializedName("MaritalStatusId")
    public String MaritalStatusId;

    @SerializedName("DateOfBirth")
    public String DateOfBirth;

    @SerializedName("PicturePath")
    public String PicturePath;

    @SerializedName("CountryId")
    public String CountryId;

    @SerializedName("StateOrProvinceId")
    public String StateOrProvinceId;

    @SerializedName("City")
    public String City;

    @SerializedName("CityId")
    public String CityId;

    @SerializedName("Address")
    public String Address;

    @SerializedName("CellNumber")
    public String CellNumber;

    @SerializedName("TelephoneNumber")
    public String TelephoneNumber;

    @SerializedName("Email")
    public String Email;

    @SerializedName("NOKFirstName")
    public String NOKFirstName;

    @SerializedName("NOKLastName")
    public String NOKLastName;

    @SerializedName("NOKRelationshipTypeId")
    public String NOKRelationshipTypeId;

    @SerializedName("NOKCNICNumber")
    public String NOKCNICNumber;

    @SerializedName("NOKCellNumber")
    public String NOKCellNumber;

    @SerializedName("BloodGroupId")
    public String BloodGroupId;

    @SerializedName("Age")
    public String Age;

    @SerializedName("DepartmentId")
    public String DepartmentId;

}
