package com.example.employeeattendanceapp.DB;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.employeeattendanceapp.Model.EducationData;
import com.example.employeeattendanceapp.Model.ExperienceData;
import com.example.employeeattendanceapp.Model.LeaveStatusData;
import com.example.employeeattendanceapp.Model.Response.EmployeeProfileResponse;
import com.example.employeeattendanceapp.Model.Response.LoginResponse;

import java.util.ArrayList;

/**
 * @Copyright : Muhammad Junaid Raza
 * @Developer : Muhammad Junaid Raza
 */

public class DBManager {

    String TAG = "DBManager";

    DatabaseHelper dbHelper;
    Context context;
    SQLiteDatabase database;


    public DBManager(Context c) {
        context = c;
    }

    public DBManager open() throws SQLException {
        dbHelper = DatabaseHelper.getInstance(context);
//        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    /**
     * insert profile
     *
     * @param loginResponse
     */
    public void insertLoginProfile(LoginResponse loginResponse) {
        if (!isDuplicateLoginProfile(loginResponse)) {  // insert
            ContentValues contentValue = new ContentValues();
            contentValue.put(DatabaseHelper.USER_ID, loginResponse.empId);
            contentValue.put(DatabaseHelper.CNIC_NUM, loginResponse.empCnic);
            contentValue.put(DatabaseHelper.EMAIL, loginResponse.empEmail);
            contentValue.put(DatabaseHelper.USERNAME, loginResponse.empUsername);
            contentValue.put(DatabaseHelper.FULL_NAME, loginResponse.empFullName);
            contentValue.put(DatabaseHelper.FIRST_NAME, loginResponse.empFirstName);
            contentValue.put(DatabaseHelper.MIDDLE_NAME, loginResponse.empMiddleName);
            contentValue.put(DatabaseHelper.LAST_NAME, loginResponse.empLastName);
            contentValue.put(DatabaseHelper.BRANCH_ID, loginResponse.branchId);
            contentValue.put(DatabaseHelper.COUNTRY_ID, loginResponse.empCountryId);
            contentValue.put(DatabaseHelper.STATE_ID, loginResponse.empStateOrProvinceId);
            contentValue.put(DatabaseHelper.CITY_ID, loginResponse.empCityId);
            contentValue.put(DatabaseHelper.ORGANIZATION_ID, loginResponse.empCityId);
            contentValue.put(DatabaseHelper.CELL_NUMBER, loginResponse.empCellNumber);
            contentValue.put(DatabaseHelper.TELEPHONE_NUMBER, loginResponse.empTelephoneNumber);
            contentValue.put(DatabaseHelper.USER_ADDRESS, loginResponse.empAddress);
            contentValue.put(DatabaseHelper.IMAGE_PATH, loginResponse.empImagePath);
            contentValue.put(DatabaseHelper.DESIGNATION, loginResponse.empDesignation);
            contentValue.put(DatabaseHelper.EMPLOYEE_NUMBER, loginResponse.empNumber);

            long db = database.insert(DatabaseHelper.TABLE_LOGIN, null, contentValue);

            //Log.e(TAG, "insert Profile Done");

        } else { // update
            //Log.e(TAG, "update Profile Done");
            updateLoginProfile(loginResponse);
        }
    }

    /**
     * is duplicate profile model
     *
     * @param loginResponse
     * @return boolean
     */
    public boolean isDuplicateLoginProfile(LoginResponse loginResponse) {
        boolean found = false;
        String[] params = new String[]{loginResponse.empId};
        String query = "select * from " + DatabaseHelper.TABLE_LOGIN + " where " + DatabaseHelper.USER_ID + "=?";
        Cursor cursor = database.rawQuery(query, params);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                found = true;
            }
            cursor.close();
        }
        return found;
    }

    /**
     * Update profile
     *
     * @param loginResponse
     */
    public void updateLoginProfile(LoginResponse loginResponse) {
        String[] args = new String[]{loginResponse.empId};
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.USER_ID, loginResponse.empId);
        contentValue.put(DatabaseHelper.CNIC_NUM, loginResponse.empCnic);
        contentValue.put(DatabaseHelper.EMAIL, loginResponse.empEmail);
        contentValue.put(DatabaseHelper.USERNAME, loginResponse.empUsername);
        contentValue.put(DatabaseHelper.FULL_NAME, loginResponse.empFullName);
        contentValue.put(DatabaseHelper.FIRST_NAME, loginResponse.empFirstName);
        contentValue.put(DatabaseHelper.MIDDLE_NAME, loginResponse.empMiddleName);
        contentValue.put(DatabaseHelper.LAST_NAME, loginResponse.empLastName);
        contentValue.put(DatabaseHelper.BRANCH_ID, loginResponse.branchId);
        contentValue.put(DatabaseHelper.COUNTRY_ID, loginResponse.empCountryId);
        contentValue.put(DatabaseHelper.STATE_ID, loginResponse.empStateOrProvinceId);
        contentValue.put(DatabaseHelper.CITY_ID, loginResponse.empCityId);
        contentValue.put(DatabaseHelper.ORGANIZATION_ID, loginResponse.empCityId);
        contentValue.put(DatabaseHelper.CELL_NUMBER, loginResponse.empCellNumber);
        contentValue.put(DatabaseHelper.TELEPHONE_NUMBER, loginResponse.empTelephoneNumber);
        contentValue.put(DatabaseHelper.USER_ADDRESS, loginResponse.empAddress);
        contentValue.put(DatabaseHelper.IMAGE_PATH, loginResponse.empImagePath);
        contentValue.put(DatabaseHelper.DESIGNATION, loginResponse.empDesignation);
        contentValue.put(DatabaseHelper.EMPLOYEE_NUMBER, loginResponse.empNumber);

        String whereClause = DatabaseHelper.USER_ID + "=?";

        long db = database.update(DatabaseHelper.TABLE_LOGIN, contentValue, whereClause, args);

        if (db != -1) {
            Log.d(TAG, "updateProfile inserted");
        } else {
            Log.d(TAG, "updateProfile something went wrong");
        }
    }

    /**
     * Get Profile
     *
     * @return profileModel
     */
    @SuppressLint("Range")
    public LoginResponse getLoginProfile() {
        LoginResponse loginResponse = new LoginResponse();
        String query = "select * from " + DatabaseHelper.TABLE_LOGIN;
        Cursor cursor = database.rawQuery(query, null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                loginResponse.empId = cursor.getString(cursor.getColumnIndex(DatabaseHelper.USER_ID));
                loginResponse.empCnic = cursor.getString(cursor.getColumnIndex(DatabaseHelper.CNIC_NUM));
                loginResponse.empEmail = cursor.getString(cursor.getColumnIndex(DatabaseHelper.EMAIL));
                loginResponse.empUsername = cursor.getString(cursor.getColumnIndex(DatabaseHelper.USERNAME));
                loginResponse.empFullName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.FULL_NAME));
                loginResponse.empFirstName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.FIRST_NAME));
                loginResponse.empMiddleName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.MIDDLE_NAME));
                loginResponse.empLastName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.LAST_NAME));
                loginResponse.branchId = cursor.getString(cursor.getColumnIndex(DatabaseHelper.BRANCH_ID));
                loginResponse.empCountryId = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COUNTRY_ID));
                loginResponse.empStateOrProvinceId = cursor.getString(cursor.getColumnIndex(DatabaseHelper.STATE_ID));
                loginResponse.empCityId = cursor.getString(cursor.getColumnIndex(DatabaseHelper.CITY_ID));
                loginResponse.empOrganizationId = cursor.getString(cursor.getColumnIndex(DatabaseHelper.ORGANIZATION_ID));
                loginResponse.empCellNumber = cursor.getString(cursor.getColumnIndex(DatabaseHelper.CELL_NUMBER));
                loginResponse.empTelephoneNumber = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TELEPHONE_NUMBER));
                loginResponse.empAddress = cursor.getString(cursor.getColumnIndex(DatabaseHelper.USER_ADDRESS));
                loginResponse.empImagePath = cursor.getString(cursor.getColumnIndex(DatabaseHelper.IMAGE_PATH));
                loginResponse.empDesignation = cursor.getString(cursor.getColumnIndex(DatabaseHelper.DESIGNATION));
                loginResponse.empNumber = cursor.getString(cursor.getColumnIndex(DatabaseHelper.EMPLOYEE_NUMBER));

                cursor.moveToNext();
            }
            cursor.close();
        }
        return loginResponse;
    }

    public Cursor fetchProfileParams() {
        String[] columns = new String[]{DatabaseHelper.USER_ID, DatabaseHelper.BRANCH_ID, DatabaseHelper.FULL_NAME,
                DatabaseHelper.FIRST_NAME, DatabaseHelper.EMPLOYEE_NUMBER, DatabaseHelper.ORGANIZATION_ID, DatabaseHelper.DESIGNATION, DatabaseHelper.IMAGE_PATH};
        Cursor cursor = database.query(DatabaseHelper.TABLE_LOGIN, columns, null, null, null,
                null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    /**
     * insert profile
     *
     * @param profileModel
     */
    public void insertProfile(EmployeeProfileResponse profileModel) {
        if (!isDuplicateProfileModel(profileModel)) {  // insert
            ContentValues contentValue = new ContentValues();
            contentValue.put(DatabaseHelper.USER_ID, profileModel.id);
            contentValue.put(DatabaseHelper.CNIC_NUM, profileModel.cNICNumber);
            contentValue.put(DatabaseHelper.TITLE_ID, profileModel.personTitleId);
            contentValue.put(DatabaseHelper.TITLE, profileModel.title);
            contentValue.put(DatabaseHelper.PREFIX, profileModel.prefix);
            contentValue.put(DatabaseHelper.FIRST_NAME, profileModel.firstName);
            contentValue.put(DatabaseHelper.MIDDLE_NAME, profileModel.middleName);
            contentValue.put(DatabaseHelper.LAST_NAME, profileModel.lastName);
            contentValue.put(DatabaseHelper.GENDER, profileModel.gender);
            contentValue.put(DatabaseHelper.GENDER_ID, profileModel.genderId);
            contentValue.put(DatabaseHelper.RELATIONSHIP_TYPE_ID, profileModel.relationshipTypeId);
            contentValue.put(DatabaseHelper.RELATIONSHIP_TYPE_NAME, profileModel.relationshipTypeName);
            contentValue.put(DatabaseHelper.GUARDIAN_NAME, profileModel.guardianName);
            contentValue.put(DatabaseHelper.MARITAL_STATUS_ID, profileModel.maritalStatusId);
            contentValue.put(DatabaseHelper.MARITAL_STATUS, profileModel.maritalStatus);
            contentValue.put(DatabaseHelper.DATE_OF_BIRTH, profileModel.dateOfBirth);
            contentValue.put(DatabaseHelper.COUNTRY, profileModel.country);
            contentValue.put(DatabaseHelper.COUNTRY_ID, profileModel.countryId);
            contentValue.put(DatabaseHelper.STATE, profileModel.stateOrProvince);
            contentValue.put(DatabaseHelper.STATE_ID, profileModel.stateOrProvinceId);
            contentValue.put(DatabaseHelper.CITY, profileModel.city);
            contentValue.put(DatabaseHelper.CITY_ID, profileModel.cityId);
            contentValue.put(DatabaseHelper.USER_ADDRESS, profileModel.address);
            contentValue.put(DatabaseHelper.CELL_NUMBER, profileModel.cellNumber);
            contentValue.put(DatabaseHelper.TELEPHONE_NUMBER, profileModel.telephoneNumber);
            contentValue.put(DatabaseHelper.EMAIL, profileModel.email);
            contentValue.put(DatabaseHelper.NOK_FIRST_NAME, profileModel.nOKFirstName);
            contentValue.put(DatabaseHelper.NOK_LAST_NAME, profileModel.nOKLastName);
            contentValue.put(DatabaseHelper.NOK_RELATION, profileModel.nOKRelation);
            contentValue.put(DatabaseHelper.NOK_CNIC_NUMBER, profileModel.nOKCNICNumber);
            contentValue.put(DatabaseHelper.NOK_CELL_NUMBER, profileModel.nOKCellNumber);
            contentValue.put(DatabaseHelper.BLOOD_GROUP, profileModel.bloodGroup);
            contentValue.put(DatabaseHelper.BLOOD_GROUP_ID, profileModel.bloodGroupId);
            contentValue.put(DatabaseHelper.AGE, profileModel.age);
            contentValue.put(DatabaseHelper.DEPARTMENT_ID, profileModel.departmentId);
            contentValue.put(DatabaseHelper.DEPARTMENT, profileModel.department);

            long db = database.insert(DatabaseHelper.TABLE_PROFILE, null, contentValue);

            //Log.e(TAG, "insert Profile Done");

        } else { // update
            //Log.e(TAG, "update Profile Done");
            updateProfile(profileModel);
        }
    }


    /**
     * is duplicate profile model
     *
     * @param profileModel
     * @return boolean
     */
    public boolean isDuplicateProfileModel(EmployeeProfileResponse profileModel) {
        boolean found = false;
        String[] params = new String[]{profileModel.id};
        String query = "select * from " + DatabaseHelper.TABLE_PROFILE + " where " + DatabaseHelper.USER_ID + "=?";
        Cursor cursor = database.rawQuery(query, params);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                found = true;
            }
            cursor.close();
        }
        return found;
    }

    /**
     * Update profile
     *
     * @param profileModel
     */
    public void updateProfile(EmployeeProfileResponse profileModel) {
        String[] args = new String[]{profileModel.id};
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.USER_ID, profileModel.id);
        contentValue.put(DatabaseHelper.CNIC_NUM, profileModel.cNICNumber);
        contentValue.put(DatabaseHelper.TITLE_ID, profileModel.personTitleId);
        contentValue.put(DatabaseHelper.TITLE, profileModel.title);
        contentValue.put(DatabaseHelper.PREFIX, profileModel.prefix);
        contentValue.put(DatabaseHelper.FIRST_NAME, profileModel.firstName);
        contentValue.put(DatabaseHelper.MIDDLE_NAME, profileModel.middleName);
        contentValue.put(DatabaseHelper.LAST_NAME, profileModel.lastName);
        contentValue.put(DatabaseHelper.GENDER, profileModel.gender);
        contentValue.put(DatabaseHelper.GENDER_ID, profileModel.genderId);
        contentValue.put(DatabaseHelper.RELATIONSHIP_TYPE_ID, profileModel.relationshipTypeId);
        contentValue.put(DatabaseHelper.RELATIONSHIP_TYPE_NAME, profileModel.relationshipTypeName);
        contentValue.put(DatabaseHelper.GUARDIAN_NAME, profileModel.guardianName);
        contentValue.put(DatabaseHelper.MARITAL_STATUS_ID, profileModel.maritalStatusId);
        contentValue.put(DatabaseHelper.MARITAL_STATUS, profileModel.maritalStatus);
        contentValue.put(DatabaseHelper.DATE_OF_BIRTH, profileModel.dateOfBirth);
        contentValue.put(DatabaseHelper.COUNTRY, profileModel.country);
        contentValue.put(DatabaseHelper.COUNTRY_ID, profileModel.countryId);
        contentValue.put(DatabaseHelper.STATE, profileModel.stateOrProvince);
        contentValue.put(DatabaseHelper.STATE_ID, profileModel.stateOrProvinceId);
        contentValue.put(DatabaseHelper.CITY, profileModel.city);
        contentValue.put(DatabaseHelper.CITY_ID, profileModel.cityId);
        contentValue.put(DatabaseHelper.USER_ADDRESS, profileModel.address);
        contentValue.put(DatabaseHelper.CELL_NUMBER, profileModel.cellNumber);
        contentValue.put(DatabaseHelper.TELEPHONE_NUMBER, profileModel.telephoneNumber);
        contentValue.put(DatabaseHelper.EMAIL, profileModel.email);
        contentValue.put(DatabaseHelper.NOK_FIRST_NAME, profileModel.nOKFirstName);
        contentValue.put(DatabaseHelper.NOK_LAST_NAME, profileModel.nOKLastName);
        contentValue.put(DatabaseHelper.NOK_RELATION, profileModel.nOKRelation);
        contentValue.put(DatabaseHelper.NOK_CNIC_NUMBER, profileModel.nOKCNICNumber);
        contentValue.put(DatabaseHelper.NOK_CELL_NUMBER, profileModel.nOKCellNumber);
        contentValue.put(DatabaseHelper.BLOOD_GROUP, profileModel.bloodGroup);
        contentValue.put(DatabaseHelper.BLOOD_GROUP_ID, profileModel.bloodGroupId);
        contentValue.put(DatabaseHelper.AGE, profileModel.age);
        contentValue.put(DatabaseHelper.DEPARTMENT_ID, profileModel.departmentId);
        contentValue.put(DatabaseHelper.DEPARTMENT, profileModel.department);

        String whereClause = DatabaseHelper.USER_ID + "=?";

        long db = database.update(DatabaseHelper.TABLE_PROFILE, contentValue, whereClause, args);

        if (db != -1) {
            Log.d(TAG, "updateProfile inserted");
        } else {
            Log.d(TAG, "updateProfile something went wrong");
        }
    }


    /**
     * Get Profile
     *
     * @return profileModel
     */
    @SuppressLint("Range")
    public EmployeeProfileResponse getProfile() {
        EmployeeProfileResponse profileModel = new EmployeeProfileResponse();
        String query = "select * from " + DatabaseHelper.TABLE_PROFILE;
        Cursor cursor = database.rawQuery(query, null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                profileModel.id = cursor.getString(cursor.getColumnIndex(DatabaseHelper.USER_ID));
                profileModel.cNICNumber = cursor.getString(cursor.getColumnIndex(DatabaseHelper.CNIC_NUM));
                profileModel.personTitleId = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TITLE_ID));
                profileModel.title = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TITLE));
                profileModel.prefix = cursor.getString(cursor.getColumnIndex(DatabaseHelper.PREFIX));
                profileModel.firstName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.FIRST_NAME));
                profileModel.middleName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.MIDDLE_NAME));
                profileModel.lastName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.LAST_NAME));
                profileModel.gender = cursor.getString(cursor.getColumnIndex(DatabaseHelper.GENDER));
                profileModel.genderId = cursor.getString(cursor.getColumnIndex(DatabaseHelper.GENDER_ID));
                profileModel.relationshipTypeId = cursor.getString(cursor.getColumnIndex(DatabaseHelper.RELATIONSHIP_TYPE_ID));
                profileModel.relationshipTypeName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.RELATIONSHIP_TYPE_NAME));
                profileModel.guardianName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.GUARDIAN_NAME));
                profileModel.maritalStatusId = cursor.getString(cursor.getColumnIndex(DatabaseHelper.MARITAL_STATUS_ID));
                profileModel.maritalStatus = cursor.getString(cursor.getColumnIndex(DatabaseHelper.MARITAL_STATUS));
                profileModel.dateOfBirth = cursor.getString(cursor.getColumnIndex(DatabaseHelper.DATE_OF_BIRTH));
                profileModel.country = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COUNTRY));
                profileModel.countryId = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COUNTRY_ID));
                profileModel.stateOrProvince = cursor.getString(cursor.getColumnIndex(DatabaseHelper.STATE));
                profileModel.stateOrProvinceId = cursor.getString(cursor.getColumnIndex(DatabaseHelper.STATE_ID));
                profileModel.city = cursor.getString(cursor.getColumnIndex(DatabaseHelper.CITY));
                profileModel.cityId = cursor.getString(cursor.getColumnIndex(DatabaseHelper.CITY_ID));
                profileModel.address = cursor.getString(cursor.getColumnIndex(DatabaseHelper.USER_ADDRESS));
                profileModel.cellNumber = cursor.getString(cursor.getColumnIndex(DatabaseHelper.CELL_NUMBER));
                profileModel.telephoneNumber = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TELEPHONE_NUMBER));
                profileModel.email = cursor.getString(cursor.getColumnIndex(DatabaseHelper.EMAIL));
                profileModel.nOKFirstName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.NOK_FIRST_NAME));
                profileModel.nOKLastName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.NOK_LAST_NAME));
                profileModel.nOKRelation = cursor.getString(cursor.getColumnIndex(DatabaseHelper.NOK_RELATION));
                profileModel.nOKCNICNumber = cursor.getString(cursor.getColumnIndex(DatabaseHelper.NOK_CNIC_NUMBER));
                profileModel.nOKCellNumber = cursor.getString(cursor.getColumnIndex(DatabaseHelper.NOK_CELL_NUMBER));
                profileModel.bloodGroup = cursor.getString(cursor.getColumnIndex(DatabaseHelper.BLOOD_GROUP));
                profileModel.bloodGroupId = cursor.getString(cursor.getColumnIndex(DatabaseHelper.BLOOD_GROUP_ID));
                profileModel.age = cursor.getString(cursor.getColumnIndex(DatabaseHelper.AGE));
                profileModel.departmentId = cursor.getString(cursor.getColumnIndex(DatabaseHelper.DEPARTMENT_ID));
                profileModel.department = cursor.getString(cursor.getColumnIndex(DatabaseHelper.DEPARTMENT));

                cursor.moveToNext();
            }
            cursor.close();
        }
        return profileModel;
    }

    /**
     * insert education details
     *
     * @param educationDataList
     */
    public void insertEducationDetails(ArrayList<EducationData> educationDataList) {
        int counter = 0, updated = 0;
        for (int i = 0; i < educationDataList.size(); i++) {
            EducationData educationData = educationDataList.get(i);
            if (!isDuplicateEducationData(educationData)) {  // insert
                counter++;
                ContentValues contentValue = new ContentValues();
                contentValue.put(DatabaseHelper.EDU_DETAILS_ID, educationData.eduDetailId);
                contentValue.put(DatabaseHelper.INSTITUTE_ID, educationData.empSchoolOrUniversityId);
                contentValue.put(DatabaseHelper.INSTITUTE_NAME, educationData.empInstituteName);
                contentValue.put(DatabaseHelper.DEGREE_ID, educationData.empDegreeId);
                contentValue.put(DatabaseHelper.DEGREE_NAME, educationData.empDegreeName);
                contentValue.put(DatabaseHelper.FIELD_ID, educationData.empFieldOfStudyId);
                contentValue.put(DatabaseHelper.FIELD_NAME, educationData.empFieldName);
                contentValue.put(DatabaseHelper.START_DATE, educationData.empStartDate);
                contentValue.put(DatabaseHelper.END_DATE, educationData.empEndDate);
                contentValue.put(DatabaseHelper.ISSUE_DATE, educationData.empIssueDate);
                contentValue.put(DatabaseHelper.GRADE, educationData.empGrade);
                contentValue.put(DatabaseHelper.DEGREE_ACTIVE, educationData.empIsActive);
                contentValue.put(DatabaseHelper.DEGREE_ATTACHMENT, educationData.empPath);
                contentValue.put(DatabaseHelper.DEGREE_STATUS, educationData.empIsActive);
                contentValue.put(DatabaseHelper.GRADING_SYSTEM, educationData.empGradingSystem);
                contentValue.put(DatabaseHelper.TOTAL_MARKS, educationData.empTotalMarks);
                contentValue.put(DatabaseHelper.OBTAINED_MARKS, educationData.empObtainedMarks);
                contentValue.put(DatabaseHelper.OBTAINED_PERCENTAGE, educationData.empObtainedPercentage);
                contentValue.put(DatabaseHelper.INSTITUE_COUNTRY_ID, educationData.empCountryId);

                long db = database.insert(DatabaseHelper.TABLE_EDUCATION, null, contentValue);

                //Log.e(TAG, "insert Profile Done");

            } else { // update
                //Log.e(TAG, "update Profile Done");
                updated++;
                updateEducationDetails(educationData);
            }
        }
    }


    /**
     * is duplicate education details model
     *
     * @param educationData
     * @return boolean
     */
    public boolean isDuplicateEducationData(EducationData educationData) {
        boolean found = false;
        String[] params = new String[]{educationData.eduDetailId};
        String query = "select * from " + DatabaseHelper.TABLE_EDUCATION + " where " + DatabaseHelper.EDU_DETAILS_ID + "=?";
        Cursor cursor = database.rawQuery(query, params);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                found = true;
            }
            cursor.close();
        }
        return found;
    }

    /**
     * Update education details model
     *
     * @param educationData
     */
    public void updateEducationDetails(EducationData educationData) {
        String[] args = new String[]{educationData.eduDetailId};
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.EDU_DETAILS_ID, educationData.eduDetailId);
        contentValue.put(DatabaseHelper.INSTITUTE_ID, educationData.empSchoolOrUniversityId);
        contentValue.put(DatabaseHelper.INSTITUTE_NAME, educationData.empInstituteName);
        contentValue.put(DatabaseHelper.DEGREE_ID, educationData.empDegreeId);
        contentValue.put(DatabaseHelper.DEGREE_NAME, educationData.empDegreeName);
        contentValue.put(DatabaseHelper.FIELD_ID, educationData.empFieldOfStudyId);
        contentValue.put(DatabaseHelper.FIELD_NAME, educationData.empFieldName);
        contentValue.put(DatabaseHelper.START_DATE, educationData.empStartDate);
        contentValue.put(DatabaseHelper.END_DATE, educationData.empEndDate);
        contentValue.put(DatabaseHelper.ISSUE_DATE, educationData.empIssueDate);
        contentValue.put(DatabaseHelper.GRADE, educationData.empGrade);
        contentValue.put(DatabaseHelper.DEGREE_ACTIVE, educationData.empIsActive);
        contentValue.put(DatabaseHelper.DEGREE_ATTACHMENT, educationData.empPath);
        contentValue.put(DatabaseHelper.DEGREE_STATUS, educationData.empIsActive);
        contentValue.put(DatabaseHelper.GRADING_SYSTEM, educationData.empGradingSystem);
        contentValue.put(DatabaseHelper.TOTAL_MARKS, educationData.empTotalMarks);
        contentValue.put(DatabaseHelper.OBTAINED_MARKS, educationData.empObtainedMarks);
        contentValue.put(DatabaseHelper.OBTAINED_PERCENTAGE, educationData.empObtainedPercentage);
        contentValue.put(DatabaseHelper.INSTITUE_COUNTRY_ID, educationData.empCountryId);

        String whereClause = DatabaseHelper.EDU_DETAILS_ID + "=?";

        long db = database.update(DatabaseHelper.TABLE_EDUCATION, contentValue, whereClause, args);

        if (db != -1) {
            Log.d(TAG, "updateProfile inserted");
        } else {
            Log.d(TAG, "updateProfile something went wrong");
        }
    }


    /**
     * Get Education Data
     *
     * @return ArrayList<EducationData>
     */
    @SuppressLint("Range")
    public ArrayList<EducationData> getEducationData() {
        ArrayList<EducationData> educationDataArrayList = new ArrayList<>();
        int counter = 0;
        String query = "select * from " + DatabaseHelper.TABLE_EDUCATION;
        Cursor cursor = database.rawQuery(query, null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                EducationData educationData = new EducationData();
                educationData.eduDetailId = cursor.getString(cursor.getColumnIndex(DatabaseHelper.EDU_DETAILS_ID));
                educationData.empSchoolOrUniversityId = cursor.getString(cursor.getColumnIndex(DatabaseHelper.INSTITUTE_ID));
                educationData.empInstituteName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.INSTITUTE_NAME));
                educationData.empDegreeId = cursor.getString(cursor.getColumnIndex(DatabaseHelper.DEGREE_ID));
                educationData.empDegreeName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.DEGREE_NAME));
                educationData.empFieldOfStudyId = cursor.getString(cursor.getColumnIndex(DatabaseHelper.FIELD_ID));
                educationData.empFieldName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.FIELD_NAME));
                educationData.empStartDate = cursor.getString(cursor.getColumnIndex(DatabaseHelper.START_DATE));
                educationData.empEndDate = cursor.getString(cursor.getColumnIndex(DatabaseHelper.END_DATE));
                educationData.empIssueDate = cursor.getString(cursor.getColumnIndex(DatabaseHelper.ISSUE_DATE));
                educationData.empGrade = cursor.getString(cursor.getColumnIndex(DatabaseHelper.GRADE));
                educationData.empIsActive = cursor.getString(cursor.getColumnIndex(DatabaseHelper.DEGREE_ACTIVE));
                educationData.empPath = cursor.getString(cursor.getColumnIndex(DatabaseHelper.DEGREE_ATTACHMENT));
                educationData.empIsActive = cursor.getString(cursor.getColumnIndex(DatabaseHelper.DEGREE_STATUS));
                educationData.empGradingSystem = cursor.getString(cursor.getColumnIndex(DatabaseHelper.GRADING_SYSTEM));
                educationData.empTotalMarks = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TOTAL_MARKS));
                educationData.empObtainedMarks = cursor.getString(cursor.getColumnIndex(DatabaseHelper.OBTAINED_MARKS));
                educationData.empObtainedPercentage = cursor.getString(cursor.getColumnIndex(DatabaseHelper.OBTAINED_PERCENTAGE));
                educationData.empCountryId = cursor.getString(cursor.getColumnIndex(DatabaseHelper.INSTITUE_COUNTRY_ID));

                counter++;
                educationDataArrayList.add(educationData);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return educationDataArrayList;
    }

    /**
     * insert experience details
     *
     * @param experienceDataList
     */
    public void insertExperienceDetails(ArrayList<ExperienceData> experienceDataList) {
        int counter = 0, updated = 0;
        for (int i = 0; i < experienceDataList.size(); i++) {
            ExperienceData experienceData = experienceDataList.get(i);
            if (!isDuplicateExperienceData(experienceData)) {  // insert
                counter++;
                ContentValues contentValue = new ContentValues();
                contentValue.put(DatabaseHelper.EXP_DETAIL_ID, experienceData.expId);
                contentValue.put(DatabaseHelper.EXP_TITLE, experienceData.expTitle);
                contentValue.put(DatabaseHelper.EXP_COMPANY_ID, experienceData.expCompanyId);
                contentValue.put(DatabaseHelper.EXP_COMPANY_NAME, experienceData.expCompanyName);
                contentValue.put(DatabaseHelper.EXP_COMPANY_LOC_ID, experienceData.expCompanyLocationId);
                contentValue.put(DatabaseHelper.EXP_COMPANY_LOC_NAME, experienceData.expCompanyLocatioName);
                contentValue.put(DatabaseHelper.EXP_START_DATE, experienceData.expFromDate);
                contentValue.put(DatabaseHelper.EXP_END_DATE, experienceData.expToDate);
                contentValue.put(DatabaseHelper.EXP_STATUS, experienceData.expIsCurrent);
                contentValue.put(DatabaseHelper.EXP_DESCRIPTION, experienceData.expDescription);
                contentValue.put(DatabaseHelper.EXP_ATTACHMENT_PATH, experienceData.expAttachmentPath);

                long db = database.insert(DatabaseHelper.TABLE_EXPERIENCE, null, contentValue);

                //Log.e(TAG, "insert Profile Done");

            } else { // update
                //Log.e(TAG, "update Profile Done");
                updated++;
                updateExperienceDetails(experienceData);
            }
        }
    }


    /**
     * is duplicate experience details model
     *
     * @param experienceData
     * @return boolean
     */
    public boolean isDuplicateExperienceData(ExperienceData experienceData) {
        boolean found = false;
        String[] params = new String[]{experienceData.expId};
        String query = "select * from " + DatabaseHelper.TABLE_EXPERIENCE + " where " + DatabaseHelper.EXP_DETAIL_ID + "=?";
        Cursor cursor = database.rawQuery(query, params);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                found = true;
            }
            cursor.close();
        }
        return found;
    }

    /**
     * Update education details model
     *
     * @param experienceData
     */
    public void updateExperienceDetails(ExperienceData experienceData) {
        String[] args = new String[]{experienceData.expId};
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.EXP_DETAIL_ID, experienceData.expId);
        contentValue.put(DatabaseHelper.EXP_TITLE, experienceData.expTitle);
        contentValue.put(DatabaseHelper.EXP_COMPANY_ID, experienceData.expCompanyId);
        contentValue.put(DatabaseHelper.EXP_COMPANY_NAME, experienceData.expCompanyName);
        contentValue.put(DatabaseHelper.EXP_COMPANY_LOC_ID, experienceData.expCompanyLocationId);
        contentValue.put(DatabaseHelper.EXP_COMPANY_LOC_NAME, experienceData.expCompanyLocatioName);
        contentValue.put(DatabaseHelper.EXP_START_DATE, experienceData.expFromDate);
        contentValue.put(DatabaseHelper.EXP_END_DATE, experienceData.expToDate);
        contentValue.put(DatabaseHelper.EXP_STATUS, experienceData.expIsCurrent);
        contentValue.put(DatabaseHelper.EXP_DESCRIPTION, experienceData.expDescription);
        contentValue.put(DatabaseHelper.EXP_ATTACHMENT_PATH, experienceData.expAttachmentPath);

        String whereClause = DatabaseHelper.EXP_DETAIL_ID + "=?";

        long db = database.update(DatabaseHelper.TABLE_EXPERIENCE, contentValue, whereClause, args);

        if (db != -1) {
            Log.d(TAG, "updateProfile inserted");
        } else {
            Log.d(TAG, "updateProfile something went wrong");
        }
    }


    /**
     * Get Experience Detail
     *
     * @return ArrayList<ExperienceData>
     */
    @SuppressLint("Range")
    public ArrayList<ExperienceData> getExperienceData() {
        ArrayList<ExperienceData> experienceDataArrayList = new ArrayList<>();
        int counter = 0;
        String query = "select * from " + DatabaseHelper.TABLE_EXPERIENCE;
        Cursor cursor = database.rawQuery(query, null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                ExperienceData experienceData = new ExperienceData();
                experienceData.expId = cursor.getString(cursor.getColumnIndex(DatabaseHelper.EXP_DETAIL_ID));
                experienceData.expTitle = cursor.getString(cursor.getColumnIndex(DatabaseHelper.EXP_TITLE));
                experienceData.expCompanyId = cursor.getString(cursor.getColumnIndex(DatabaseHelper.EXP_COMPANY_ID));
                experienceData.expCompanyName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.EXP_COMPANY_NAME));
                experienceData.expCompanyLocationId = cursor.getString(cursor.getColumnIndex(DatabaseHelper.EXP_COMPANY_LOC_ID));
                experienceData.expCompanyLocatioName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.EXP_COMPANY_LOC_NAME));
                experienceData.expFromDate = cursor.getString(cursor.getColumnIndex(DatabaseHelper.EXP_START_DATE));
                experienceData.expToDate = cursor.getString(cursor.getColumnIndex(DatabaseHelper.EXP_END_DATE));
                experienceData.expIsCurrent = cursor.getString(cursor.getColumnIndex(DatabaseHelper.EXP_STATUS));
                experienceData.expDescription = cursor.getString(cursor.getColumnIndex(DatabaseHelper.EXP_DESCRIPTION));
                experienceData.expAttachmentPath = cursor.getString(cursor.getColumnIndex(DatabaseHelper.EXP_ATTACHMENT_PATH));

                counter++;
                experienceDataArrayList.add(experienceData);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return experienceDataArrayList;
    }


    /**
     * insert leave status details
     *
     * @param leaveStatusData
     */
    public void insertLeaveStatusDetails(ArrayList<LeaveStatusData> leaveStatusData) {
        int counter = 0, updated = 0;
        for (int i = 0; i < leaveStatusData.size(); i++) {
            LeaveStatusData leaveData = leaveStatusData.get(i);
            if (!isDuplicateLeaveStatusData(leaveData)) {  // insert
                counter++;
                ContentValues contentValue = new ContentValues();
                contentValue.put(DatabaseHelper.LEAVE_ID, leaveData.leaveId);
                contentValue.put(DatabaseHelper.LEAVE_START_DATE, leaveData.leaveStartDate);
                contentValue.put(DatabaseHelper.LEAVE_END_DATE, leaveData.leaveEndDate);
                contentValue.put(DatabaseHelper.LEAVE_TITLE, leaveData.leaveTitle);
                contentValue.put(DatabaseHelper.LEAVE_DESCRIPTION, leaveData.leaveDescription);
                contentValue.put(DatabaseHelper.LEAVE_APPLICATION_NUM, leaveData.leaveApplicationNo);
                contentValue.put(DatabaseHelper.LEAVE_DESIGNATION_NAME, leaveData.userDesignationName);
                contentValue.put(DatabaseHelper.LEAVE_DEPARTMENT_NAME, leaveData.userDepartmentName);
                contentValue.put(DatabaseHelper.LEAVE_STATUS_VALUE, leaveData.leaveStatusvalue);
                contentValue.put(DatabaseHelper.LEAVE_STATUS_ID, leaveData.leaveStatusId);
                contentValue.put(DatabaseHelper.LEAVE_IS_CLOSED, leaveData.leaveIsClosed);
                contentValue.put(DatabaseHelper.LEAVE_FILE, leaveData.leaveFile);
                contentValue.put(DatabaseHelper.LEAVE_FULL_NAME, leaveData.userFullName);
                contentValue.put(DatabaseHelper.LEAVE_TYPE, leaveData.leaveType);
                contentValue.put(DatabaseHelper.LEAVE_STATUS, leaveData.leaveStatus);
                contentValue.put(DatabaseHelper.LEAVE_APPLY_DATE, leaveData.leaveApplyDate);
                contentValue.put(DatabaseHelper.LEAVE_IS_SHORT, leaveData.leaveIsShortLeave);
                contentValue.put(DatabaseHelper.LEAVE_NUM_OF_DAYS, leaveData.leaveNumberOfDays);
                contentValue.put(DatabaseHelper.LEAVE_UPDATED_ID, leaveData.updateLeaveTypeId);
                contentValue.put(DatabaseHelper.LEAVE_MODIFIED_ON, leaveData.leaveModifiedOn);
                contentValue.put(DatabaseHelper.LEAVE_APPROVED_BY, leaveData.leaveApprovedByName);
                contentValue.put(DatabaseHelper.LEAVE_LINE_MANAGER_NAME, leaveData.leaveLineManagerName);
                contentValue.put(DatabaseHelper.LEAVE_ATTACHMENT, leaveData.leaveAttachment);
                contentValue.put(DatabaseHelper.LEAVE_ACT_TEXT, leaveData.leaveAction);

                long db = database.insert(DatabaseHelper.TABLE_LEAVE_STATUS, null, contentValue);

                //Log.e(TAG, "insert Profile Done");

            } else { // update
                //Log.e(TAG, "update Profile Done");
                updated++;
                updateLeaveStatusDetails(leaveData);
            }
        }
    }


    /**
     * is duplicate leave Status details model
     *
     * @param leaveStatusData
     * @return boolean
     */
    public boolean isDuplicateLeaveStatusData(LeaveStatusData leaveStatusData) {
        boolean found = false;
        String[] params = new String[]{leaveStatusData.leaveId};
        String query = "select * from " + DatabaseHelper.TABLE_LEAVE_STATUS + " where " + DatabaseHelper.LEAVE_ID + "=?";
        Cursor cursor = database.rawQuery(query, params);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                found = true;
            }
            cursor.close();
        }
        return found;
    }

    /**
     * Update leave Status details model
     *
     * @param leaveData
     */
    public void updateLeaveStatusDetails(LeaveStatusData leaveData) {
        String[] args = new String[]{leaveData.leaveId};
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.LEAVE_ID, leaveData.leaveId);
        contentValue.put(DatabaseHelper.LEAVE_START_DATE, leaveData.leaveStartDate);
        contentValue.put(DatabaseHelper.LEAVE_END_DATE, leaveData.leaveEndDate);
        contentValue.put(DatabaseHelper.LEAVE_TITLE, leaveData.leaveTitle);
        contentValue.put(DatabaseHelper.LEAVE_DESCRIPTION, leaveData.leaveDescription);
        contentValue.put(DatabaseHelper.LEAVE_APPLICATION_NUM, leaveData.leaveApplicationNo);
        contentValue.put(DatabaseHelper.LEAVE_DESIGNATION_NAME, leaveData.userDesignationName);
        contentValue.put(DatabaseHelper.LEAVE_DEPARTMENT_NAME, leaveData.userDepartmentName);
        contentValue.put(DatabaseHelper.LEAVE_STATUS_VALUE, leaveData.leaveStatusvalue);
        contentValue.put(DatabaseHelper.LEAVE_STATUS_ID, leaveData.leaveStatusId);
        contentValue.put(DatabaseHelper.LEAVE_IS_CLOSED, leaveData.leaveIsClosed);
        contentValue.put(DatabaseHelper.LEAVE_FILE, leaveData.leaveFile);
        contentValue.put(DatabaseHelper.LEAVE_FULL_NAME, leaveData.userFullName);
        contentValue.put(DatabaseHelper.LEAVE_TYPE, leaveData.leaveType);
        contentValue.put(DatabaseHelper.LEAVE_STATUS, leaveData.leaveStatus);
        contentValue.put(DatabaseHelper.LEAVE_APPLY_DATE, leaveData.leaveApplyDate);
        contentValue.put(DatabaseHelper.LEAVE_IS_SHORT, leaveData.leaveIsShortLeave);
        contentValue.put(DatabaseHelper.LEAVE_NUM_OF_DAYS, leaveData.leaveNumberOfDays);
        contentValue.put(DatabaseHelper.LEAVE_UPDATED_ID, leaveData.updateLeaveTypeId);
        contentValue.put(DatabaseHelper.LEAVE_MODIFIED_ON, leaveData.leaveModifiedOn);
        contentValue.put(DatabaseHelper.LEAVE_APPROVED_BY, leaveData.leaveApprovedByName);
        contentValue.put(DatabaseHelper.LEAVE_LINE_MANAGER_NAME, leaveData.leaveLineManagerName);
        contentValue.put(DatabaseHelper.LEAVE_ATTACHMENT, leaveData.leaveAttachment);
        contentValue.put(DatabaseHelper.LEAVE_ACT_TEXT, leaveData.leaveAction);

        String whereClause = DatabaseHelper.LEAVE_ID + "=?";

        long db = database.update(DatabaseHelper.TABLE_LEAVE_STATUS, contentValue, whereClause, args);

        if (db != -1) {
            Log.d(TAG, "updateProfile inserted");
        } else {
            Log.d(TAG, "updateProfile something went wrong");
        }
    }


    /**
     * Get Leave Status Detail
     *
     * @return ArrayList<LeaveStatusData>
     */
    @SuppressLint("Range")
    public ArrayList<LeaveStatusData> getLeaveStatusData() {
        ArrayList<LeaveStatusData> leaveStatusDataArrayList = new ArrayList<>();
        int counter = 0;
        String query = "select * from " + DatabaseHelper.TABLE_LEAVE_STATUS;
        Cursor cursor = database.rawQuery(query, null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                LeaveStatusData leaveStatusData = new LeaveStatusData();
                leaveStatusData.leaveId = cursor.getString(cursor.getColumnIndex(DatabaseHelper.LEAVE_ID));
                leaveStatusData.leaveStartDate = cursor.getString(cursor.getColumnIndex(DatabaseHelper.LEAVE_START_DATE));
                leaveStatusData.leaveEndDate = cursor.getString(cursor.getColumnIndex(DatabaseHelper.LEAVE_END_DATE));
                leaveStatusData.leaveTitle = cursor.getString(cursor.getColumnIndex(DatabaseHelper.LEAVE_TITLE));
                leaveStatusData.leaveDescription = cursor.getString(cursor.getColumnIndex(DatabaseHelper.LEAVE_DESCRIPTION));
                leaveStatusData.leaveApplicationNo = cursor.getString(cursor.getColumnIndex(DatabaseHelper.LEAVE_APPLICATION_NUM));
                leaveStatusData.userDesignationName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.LEAVE_DESIGNATION_NAME));
                leaveStatusData.userDepartmentName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.LEAVE_DEPARTMENT_NAME));
                leaveStatusData.leaveStatusvalue = cursor.getString(cursor.getColumnIndex(DatabaseHelper.LEAVE_STATUS_VALUE));
                leaveStatusData.leaveStatusId = cursor.getString(cursor.getColumnIndex(DatabaseHelper.LEAVE_STATUS_ID));
                leaveStatusData.leaveIsClosed = cursor.getString(cursor.getColumnIndex(DatabaseHelper.LEAVE_IS_CLOSED));
                leaveStatusData.leaveFile = cursor.getString(cursor.getColumnIndex(DatabaseHelper.LEAVE_FILE));
                leaveStatusData.userFullName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.LEAVE_FULL_NAME));
                leaveStatusData.leaveType = cursor.getString(cursor.getColumnIndex(DatabaseHelper.LEAVE_TYPE));
                leaveStatusData.leaveStatus = cursor.getString(cursor.getColumnIndex(DatabaseHelper.LEAVE_STATUS));
                leaveStatusData.leaveApplyDate = cursor.getString(cursor.getColumnIndex(DatabaseHelper.LEAVE_APPLY_DATE));
                leaveStatusData.leaveIsShortLeave = cursor.getString(cursor.getColumnIndex(DatabaseHelper.LEAVE_IS_SHORT));
                leaveStatusData.leaveNumberOfDays = cursor.getString(cursor.getColumnIndex(DatabaseHelper.LEAVE_NUM_OF_DAYS));
                leaveStatusData.updateLeaveTypeId = cursor.getString(cursor.getColumnIndex(DatabaseHelper.LEAVE_UPDATED_ID));
                leaveStatusData.leaveModifiedOn = cursor.getString(cursor.getColumnIndex(DatabaseHelper.LEAVE_MODIFIED_ON));
                leaveStatusData.leaveApprovedByName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.LEAVE_APPROVED_BY));
                leaveStatusData.leaveLineManagerName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.LEAVE_LINE_MANAGER_NAME));
                leaveStatusData.leaveAttachment = cursor.getString(cursor.getColumnIndex(DatabaseHelper.LEAVE_ATTACHMENT));
                leaveStatusData.leaveAction = cursor.getString(cursor.getColumnIndex(DatabaseHelper.LEAVE_ACT_TEXT));

                counter++;
                leaveStatusDataArrayList.add(leaveStatusData);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return leaveStatusDataArrayList;
    }


    /**
     * Delete Table Login
     */
    public void deleteTables() {
        database.execSQL("delete from " + DatabaseHelper.TABLE_LOGIN);
        database.execSQL("delete from " + DatabaseHelper.TABLE_PROFILE);
        database.execSQL("delete from " + DatabaseHelper.TABLE_EDUCATION);
        database.execSQL("delete from " + DatabaseHelper.TABLE_EXPERIENCE);
        database.execSQL("delete from " + DatabaseHelper.TABLE_LEAVE_STATUS);
    }
}
