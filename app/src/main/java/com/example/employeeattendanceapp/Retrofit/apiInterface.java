package com.example.employeeattendanceapp.Retrofit;

import com.example.employeeattendanceapp.Model.Request.SubmitAttendanceRequest;
import com.example.employeeattendanceapp.Model.Request.SubmitLeaveRequest;
import com.example.employeeattendanceapp.Model.Request.UpdateProfileRequest;
import com.example.employeeattendanceapp.Model.Response.AttachFileResponse;
import com.example.employeeattendanceapp.Model.Response.BloodGroupResponse;
import com.example.employeeattendanceapp.Model.Response.EducationDetailsResponse;
import com.example.employeeattendanceapp.Model.Response.EmployeeProfileResponse;
import com.example.employeeattendanceapp.Model.Response.ExperienceDetailResponse;
import com.example.employeeattendanceapp.Model.Response.GenderResponse;
import com.example.employeeattendanceapp.Model.Response.GetAttendanceHistoryResponse;
import com.example.employeeattendanceapp.Model.Response.GetCityResponse;
import com.example.employeeattendanceapp.Model.Response.GetCountryResponse;
import com.example.employeeattendanceapp.Model.Response.GetDepartmentResponse;
import com.example.employeeattendanceapp.Model.Response.GetLeaveBalanceResponse;
import com.example.employeeattendanceapp.Model.Response.GetLeaveStatusResponse;
import com.example.employeeattendanceapp.Model.Response.GetLeaveTypeResponse;
import com.example.employeeattendanceapp.Model.Response.GetLineManagerResponse;
import com.example.employeeattendanceapp.Model.Response.GetStateResponse;
import com.example.employeeattendanceapp.Model.Response.GetSubDepartmentResponse;
import com.example.employeeattendanceapp.Model.Response.LoginResponse;
import com.example.employeeattendanceapp.Model.Response.LogoutResponse;
import com.example.employeeattendanceapp.Model.Response.MaritalStatusResponse;
import com.example.employeeattendanceapp.Model.Response.SubmitAttendanceResponse;
import com.example.employeeattendanceapp.Model.Response.SubmitLeaveResponse;
import com.example.employeeattendanceapp.Model.Response.TitleResponse;
import com.example.employeeattendanceapp.Model.Response.UpdateProfileResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * @Copyright : Muhammad Junaid Raza
 * @Developer : Muhammad Junaid Raza
 */

public interface apiInterface {

    @POST("api/account")
    Call<LoginResponse> loginEmployee(@Body RequestBody requestBody);

    @POST("api/account/Logoff")
    Call<LogoutResponse> logoutEmployee(@Body RequestBody requestBody);

    @POST("api/account/SubmitAttendance")
    Call<SubmitAttendanceResponse> submitAttendance(@Body SubmitAttendanceRequest submitAttendanceRequest);

    @POST("api/ddl/GetPersonTitle")
    Call<TitleResponse> getTitle();

    @GET("api/ddl/GetGenders")
    Call<GenderResponse> getGender();

    @GET("api/ddl/GetMaritalStatus")
    Call<MaritalStatusResponse> getMaritalStatus();

    @GET("api/ddl/GetBloodGroup")
    Call<BloodGroupResponse> getBloodGroup();

    @GET("api/ddl/GetCountries")
    Call<GetCountryResponse> getCountries();

    @POST("api/ddl/GetStateOrProvinces")
    Call<GetStateResponse> getStates(@Body RequestBody requestBody);

    @POST("api/ddl/GetCities")
    Call<GetCityResponse> getCities(@Body RequestBody requestBody);

    @POST("api/Leave/GetDepartments")
    Call<GetDepartmentResponse> getDepartments(@Body RequestBody requestBody);

    @POST("api/Leave/GetSubDepartments")
    Call<GetSubDepartmentResponse> getSubDepartments(@Body RequestBody requestBody);

    @POST("api/account/GetUpdateProfileDetail")
    Call<EmployeeProfileResponse> getEmployeeProfile(@Body RequestBody requestBody);

    @POST("api/account/UpdateProfile")
    Call<UpdateProfileResponse> updateEmployeeProfile(@Body RequestBody requestBody);

    @POST("api/ddl/GetEducationalDetail")
    Call<EducationDetailsResponse> getEducationDetails(@Body RequestBody requestBody);

    @POST("api/ddl/GetExpereinceDetail")
    Call<ExperienceDetailResponse> getExperienceDetails(@Body RequestBody requestBody);

    @POST("api/Leave/LeaveTypes")
    Call<GetLeaveTypeResponse> getLeaveType();

    @POST("api/Leave/GetLineManagers")
    Call<GetLineManagerResponse> getLineManager(@Body RequestBody requestBody);

    @Multipart
    @POST("api/Leave/AttachFile")
    Call<AttachFileResponse> attachFile(@Part MultipartBody.Part filePart);

    @POST("api/Leave/LeaveBalance")
    Call<GetLeaveBalanceResponse> getLeaveBalance(@Body RequestBody requestBody);

    @POST("api/Leave/SubmitLeave")
    Call<SubmitLeaveResponse> submitLeave(@Body SubmitLeaveRequest submitLeaveRequest);

    @POST("api/Leave/LeaveStatus")
    Call<GetLeaveStatusResponse> getLeaveStatus(@Body RequestBody requestBody);

    @POST("api/account/GetAttendanceHistory")
    Call<GetAttendanceHistoryResponse> getAttendanceHistory(@Body RequestBody requestBody);

}
