package com.example.employeeattendanceapp.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @Copyright : Muhammad Junaid Raza
 * @Developer : Muhammad Junaid Raza
 */

public class EducationData implements Serializable {

    @SerializedName("Id")
    public String eduDetailId;

    @SerializedName("UserId")
    public String empId;

    @SerializedName("SchoolOrUniversityId")
    public String empSchoolOrUniversityId;

    @SerializedName("InstituteName")
    public String empInstituteName;

    @SerializedName("DegreeId")
    public String empDegreeId;

    @SerializedName("DegreeName")
    public String empDegreeName;

    @SerializedName("FieldOfStudyId")
    public String empFieldOfStudyId;

    @SerializedName("FieldName")
    public String empFieldName;

    @SerializedName("StartDate")
    public String empStartDate;

    @SerializedName("EndDate")
    public String empEndDate;

    @SerializedName("IssueDate")
    public String empIssueDate;

    @SerializedName("Grade")
    public String empGrade;

    @SerializedName("IsCurrent")
    public String empIsCurrent;

    @SerializedName("Path")
    public String empPath;

    @SerializedName("IsActive")
    public String empIsActive;

    @SerializedName("GradingSystem")
    public String empGradingSystem;

    @SerializedName("TotalMarks")
    public String empTotalMarks;

    @SerializedName("ObtainedMarks")
    public String empObtainedMarks;

    @SerializedName("ObtainedPercentage")
    public String empObtainedPercentage;

    @SerializedName("CountryId")
    public String empCountryId;

    @SerializedName("UserGrade")
    public String empUserGrade;
}
