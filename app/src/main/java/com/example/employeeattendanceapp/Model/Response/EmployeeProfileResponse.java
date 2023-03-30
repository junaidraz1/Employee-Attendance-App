package com.example.employeeattendanceapp.Model.Response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @Copyright : Muhammad Junaid Raza
 * @Developer : Muhammad Junaid Raza
 */

public class EmployeeProfileResponse implements Serializable {

    @SerializedName("Status")
    public Integer status;

    @SerializedName("Id")
    public String id;

    @SerializedName("CNICNumber")
    public String cNICNumber;

    @SerializedName("UserName")
    public String userName;

    @SerializedName("PersonTitleId")
    public String personTitleId;

    @SerializedName("Title")
    public String title;

    @SerializedName("Prefix")
    public String prefix;

    @SerializedName("FirstName")
    public String firstName;

    @SerializedName("MiddleName")
    public String middleName;

    @SerializedName("LastName")
    public String lastName;

    @SerializedName("Gender")
    public String gender;

    @SerializedName("GenderId")
    public String genderId;

    @SerializedName("RelationshipTypeId")
    public String relationshipTypeId;

    @SerializedName("RelationshipTypeName")
    public String relationshipTypeName;

    @SerializedName("GuardianName")
    public String guardianName;

    @SerializedName("MaritalStatusId")
    public String maritalStatusId;

    @SerializedName("MaritalStatus")
    public String maritalStatus;

    @SerializedName("DateOfBirth")
    public String dateOfBirth;

    @SerializedName("PicturePath")
    public String picturePath;

    @SerializedName("Country")
    public String country;

    @SerializedName("CountryId")
    public String countryId;

    @SerializedName("StateOrProvince")
    public String stateOrProvince;

    @SerializedName("StateOrProvinceId")
    public String stateOrProvinceId;

    @SerializedName("City")
    public String city;

    @SerializedName("CityId")
    public String cityId;

    @SerializedName("Address")
    public String address;

    @SerializedName("CellNumber")
    public String cellNumber;

    @SerializedName("TelephoneNumber")
    public String telephoneNumber;

    @SerializedName("Email")
    public String email;

    @SerializedName("NOKFirstName")
    public String nOKFirstName;

    @SerializedName("NOKLastName")
    public String nOKLastName;

    @SerializedName("NOKRelation")
    public String nOKRelation;

    @SerializedName("NOKRelationshipTypeId")
    public String nOKRelationshipTypeId;

    @SerializedName("NOKCNICNumber")
    public String nOKCNICNumber;

    @SerializedName("NOKCellNumber")
    public String nOKCellNumber;

    @SerializedName("BloodGroup")
    public String bloodGroup;

    @SerializedName("BloodGroupId")
    public String bloodGroupId;

    @SerializedName("Age")
    public String age;

    @SerializedName("DepartmentId")
    public String departmentId;

    @SerializedName("Department")
    public String department;

}
