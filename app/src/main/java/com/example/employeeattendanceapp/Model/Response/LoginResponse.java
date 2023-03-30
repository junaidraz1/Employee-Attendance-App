package com.example.employeeattendanceapp.Model.Response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @Copyright : Muhammad Junaid Raza
 * @Developer : Muhammad Junaid Raza
 */

public class LoginResponse implements Serializable {

    @SerializedName("Id")
    public String empId;

    @SerializedName("CNICNumber")
    public String empCnic;

    @SerializedName("Email")
    public String empEmail;

    @SerializedName("Username")
    public String empUsername;

    @SerializedName("FullName")
    public String empFullName;

    @SerializedName("FirstName")
    public String empFirstName;

    @SerializedName("MiddleName")
    public String empMiddleName;

    @SerializedName("LastName")
    public String empLastName;

    @SerializedName("BranchId")
    public String branchId;

    @SerializedName("Status")
    public Integer status;

    @SerializedName("ErrorMessage")
    public String errorMessage;

    @SerializedName("WorkingSessionId")
    public String sessionId;

    @SerializedName("CountryId")
    public String empCountryId;

    @SerializedName("CityId")
    public String empCityId;

    @SerializedName("StateOrProvinceId")
    public String empStateOrProvinceId;

    @SerializedName("DistrictId")
    public String empDistrictIdd;

    @SerializedName("OrganizationId")
    public String empOrganizationId;

    @SerializedName("CellNumber")
    public String empCellNumber;

    @SerializedName("TelephoneNumber")
    public String empTelephoneNumber;

    @SerializedName("UserAddress")
    public String empAddress;

    @SerializedName("ImagePath")
    public String empImagePath;

    @SerializedName("Designations")
    public String empDesignation;

    @SerializedName("EmployeeNumber")
    public String empNumber;


}
