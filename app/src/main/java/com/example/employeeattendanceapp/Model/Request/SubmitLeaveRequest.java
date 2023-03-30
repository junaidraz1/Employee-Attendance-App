package com.example.employeeattendanceapp.Model.Request;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SubmitLeaveRequest implements Serializable {

    @SerializedName("LeaveTypeId")
    public String leaveTypeId;

    @SerializedName("Title")
    public String leaveTitle;

    @SerializedName("Description")
    public String leaveDescription;

    @SerializedName("NumberOfDays")
    public String leaveNumberofDays;

    @SerializedName("StartDateTime")
    public String leaveStartDateTime;

    @SerializedName("EndDateTime")
    public String leaveEndDateTime;

    @SerializedName("IsShortLeave")
    public int isShortLeave;

    @SerializedName("LeaveAttachmentPath")
    public String leaveAttachmentPath;

    @SerializedName("UserId")
    public String leaveUserId;

    @SerializedName("IsActive")
    public String leaveIsActive;

    @SerializedName("CreatedById")
    public String leaveCreatedById;

    @SerializedName("ModifiedById")
    public String leaveModifiedById;

    @SerializedName("LineManagerId")
    public String lineManagerId;

    @SerializedName("Remarks")
    public String lineManagerRemarks;

}
