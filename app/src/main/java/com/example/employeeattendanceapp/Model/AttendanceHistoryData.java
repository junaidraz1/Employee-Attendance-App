package com.example.employeeattendanceapp.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @Copyright : Muhammad Junaid Raza
 * @Developer : Muhammad Junaid Raza
 */

public class AttendanceHistoryData implements Serializable {

    @SerializedName("ShiftId")
    public String atdShiftId;

    @SerializedName("Date")
    public String atdDate;

    @SerializedName("Status")
    public String atdStatus;

    @SerializedName("Remarks")
    public String atdRemarks;

    @SerializedName("DesignationName")
    public String atdDesignationName;

    @SerializedName("DesignationId")
    public String atdDesignationId;

    @SerializedName("DepartmentName")
    public String atdDepartmentName;

    @SerializedName("DepartmentId")
    public String atdDepartmentId;

    @SerializedName("HoursWorked")
    public String atdHoursWorked;

    @SerializedName("TimeCategory")
    public String atdTimeCategory;

    @SerializedName("ExpectedHours")
    public String atdExpectedHours;

    @SerializedName("TimeDiff")
    public String atdTimeDiff;

    @SerializedName("TimeShiftName")
    public String atdTimeShiftName;

    @SerializedName("EmployeeNumber")
    public String atdEmployeeNumber;

    @SerializedName("GenderId")
    public String atdGenderId;

    @SerializedName("GenderName")
    public String atdGenderName;

    @SerializedName("UserName")
    public String atdUserName;

    @SerializedName("CellNumber")
    public String atdCellNumber;

    @SerializedName("TimeIn")
    public String atdTimeIn;

    @SerializedName("TimeOut")
    public String atdTimeOut;

    @SerializedName("OverTimeIn")
    public String atdOverTimeIn;

    @SerializedName("OverTimeOut")
    public String atdOverTimeOut;

    @SerializedName("IsManual")
    public boolean atdIsManual;

    @SerializedName("ManualText")
    public String atdManualText;

    @SerializedName("SrNo")
    public String atdSrNo;

    @SerializedName("IsManualTimeIn")
    public boolean atdIsManualTimeIn;

    @SerializedName("IsManualTimeOut")
    public boolean atdIsManualTimeOut;

}
