package com.example.employeeattendanceapp.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @Copyright : Muhammad Junaid Raza
 * @Developer : Muhammad Junaid Raza
 */

public class LeaveStatusData implements Serializable {

    @SerializedName("UserId")
    public String userId;

    @SerializedName("Id")
    public String leaveId;

    @SerializedName("LeaveFrom")
    public String leaveStartDate;

    @SerializedName("LeaveTo")
    public String leaveEndDate;

    @SerializedName("Title")
    public String leaveTitle;

    @SerializedName("Description")
    public String leaveDescription;

    @SerializedName("ApplicationNo")
    public String leaveApplicationNo;

    @SerializedName("DesignationName")
    public String userDesignationName;

    @SerializedName("DepartmentName")
    public String userDepartmentName;

    @SerializedName("LeaveStatusvalue")
    public String leaveStatusvalue;

    @SerializedName("LeaveStatusId")
    public String leaveStatusId;

    @SerializedName("IsClosed")
    public String leaveIsClosed;

    @SerializedName("File")
    public String leaveFile;

    @SerializedName("FullName")
    public String userFullName;

    @SerializedName("LeaveType")
    public String leaveType;

    @SerializedName("LeaveStatus")
    public String leaveStatus;

    @SerializedName("ApplyDate")
    public String leaveApplyDate;

    @SerializedName("IsShortLeave")
    public String leaveIsShortLeave;

    @SerializedName("NumberOfDays")
    public String leaveNumberOfDays;

    @SerializedName("UpdateLeaveTypeId")
    public String updateLeaveTypeId;

    @SerializedName("ModifiedOn")
    public String leaveModifiedOn;

    @SerializedName("ApprovedByName")
    public String leaveApprovedByName;

    @SerializedName("LineManagerName")
    public String leaveLineManagerName;

    @SerializedName("Attachment")
    public String leaveAttachment;

    @SerializedName("Action")
    public String leaveAction;

}
