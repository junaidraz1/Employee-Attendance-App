package com.example.employeeattendanceapp.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @Copyright : Muhammad Junaid Raza
 * @Developer : Muhammad Junaid Raza
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    String TAG = "DatabaseHelper";
    public static DatabaseHelper mInstance = null;

    // Database Information
    public static final String DB_NAME = "EmployeeDetails.DB";

    // database version
    static final int DB_VERSION = 5;

    /**
     * Employee Table
     */
    public static final String TABLE_LOGIN = "employeeLogin";

    public static final String USER_ID = "UserId";
    public static final String CNIC_NUM = "CNIC";
    public static final String EMAIL = "Email";
    public static final String USERNAME = "UserName";
    public static final String FULL_NAME = "FullName";
    public static final String FIRST_NAME = "FirstName";
    public static final String MIDDLE_NAME = "MiddleName";
    public static final String LAST_NAME = "LastName";
    public static final String BRANCH_ID = "BranchId";
    public static final String COUNTRY = "Country";
    public static final String COUNTRY_ID = "CountryId";
    public static final String STATE = "State";
    public static final String STATE_ID = "StateId";
    public static final String CITY = "City";
    public static final String CITY_ID = "CityId";
    public static final String ORGANIZATION_ID = "OrganisationId";
    public static final String CELL_NUMBER = "CellNumber";
    public static final String TELEPHONE_NUMBER = "TelephoneNumber";
    public static final String USER_ADDRESS = "UserAddress";
    public static final String IMAGE_PATH = "ImagePath";
    public static final String DESIGNATION = "Designation";
    public static final String EMPLOYEE_NUMBER = "EmployeeNumber";
    /**
     * Employee Table Ends here
     */

    /**
     * Employee Profile Table
     */
    public static final String TABLE_PROFILE = "employeeProfile";

    public static final String TITLE_ID = "titleId";
    public static final String TITLE = "title";
    public static final String PREFIX = "prefix";
    public static final String GENDER = "gender";
    public static final String GENDER_ID = "genderId";
    public static final String RELATIONSHIP_TYPE_ID = "RelationshipTypeId";
    public static final String RELATIONSHIP_TYPE_NAME = "RelationshipTypeName";
    public static final String GUARDIAN_NAME = "GuardianName";
    public static final String MARITAL_STATUS_ID = "MaritalStatusId";
    public static final String MARITAL_STATUS = "MaritalStatus";
    public static final String DATE_OF_BIRTH = "DateOfBirth";
    public static final String NOK_FIRST_NAME = "NOKFirstName";
    public static final String NOK_LAST_NAME = "NOKLastName";
    public static final String NOK_RELATION = "NOKRelation";
    public static final String NOK_RELATIONSHIP_TYPE_ID = "NOKRelationshipTypeId";
    public static final String NOK_CNIC_NUMBER = "NOKCNICNumber";
    public static final String NOK_CELL_NUMBER = "NOKCellNumber";
    public static final String BLOOD_GROUP = "BloodGroup";
    public static final String BLOOD_GROUP_ID = "BloodGroupId";
    public static final String AGE = "Age";
    public static final String DEPARTMENT_ID = "DepartmentId";
    public static final String DEPARTMENT = "Department";

    /**
     * Employee Profile Table Ends here
     */

    /**
     * Employee Education Detail Table
     */
    public static final String TABLE_EDUCATION = "empEducationDetails";

    public static final String EDU_DETAILS_ID = "educationDetailId";
    public static final String INSTITUTE_ID = "instituteId";
    public static final String INSTITUTE_NAME = "instituteName";
    public static final String DEGREE_ID = "DegreeId";
    public static final String DEGREE_NAME = "DegreeName";
    public static final String FIELD_ID = "FieldOfStudyId";
    public static final String FIELD_NAME = "FieldName";
    public static final String START_DATE = "StartDate";
    public static final String END_DATE = "EndDate";
    public static final String ISSUE_DATE = "IssueDate";
    public static final String GRADE = "Grade";
    public static final String DEGREE_ACTIVE = "IsCurrent";
    public static final String DEGREE_ATTACHMENT = "Path";
    public static final String DEGREE_STATUS = "IsActive";
    public static final String GRADING_SYSTEM = "GradingSystem";
    public static final String TOTAL_MARKS = "TotalMarks";
    public static final String OBTAINED_MARKS = "ObtainedMarks";
    public static final String OBTAINED_PERCENTAGE = "ObtainedPercentage";
    public static final String INSTITUE_COUNTRY_ID = "CountryId";

    /**
     * Employee Education Detail Table Ends here
     */

    /**
     * Employee Experience Detail Table
     */
    public static final String TABLE_EXPERIENCE = "empExperienceDetails";

    public static final String EXP_DETAIL_ID = "exp_DetailId";
    public static final String EXP_TITLE = "Title";
    public static final String EXP_COMPANY_ID = "CompanyId";
    public static final String EXP_COMPANY_NAME = "CompanyName";
    public static final String EXP_COMPANY_LOC_ID = "CompanyLocationId";
    public static final String EXP_COMPANY_LOC_NAME = "CompanyLocatioName";
    public static final String EXP_START_DATE = "FromDate";
    public static final String EXP_END_DATE = "ToDate";
    public static final String EXP_STATUS = "IsCurrent";
    public static final String EXP_DESCRIPTION = "Description";
    public static final String EXP_ATTACHMENT_PATH = "Path";

    /**
     * Employee Experience Detail Table Ends here
     */

    /**
     * Employee Leave Status Detail Table
     */
    public static final String TABLE_LEAVE_STATUS = "empLeaveStatus";

    public static final String LEAVE_ID = "Id";
    public static final String LEAVE_START_DATE = "LeaveFrom";
    public static final String LEAVE_END_DATE = "LeaveTo";
    public static final String LEAVE_TITLE = "Title";
    public static final String LEAVE_DESCRIPTION = "Description";
    public static final String LEAVE_APPLICATION_NUM = "ApplicationNo";
    public static final String LEAVE_DESIGNATION_NAME = "DesignationName";
    public static final String LEAVE_DEPARTMENT_NAME = "DepartmentName";
    public static final String LEAVE_STATUS_VALUE = "LeaveStatusvalue";
    public static final String LEAVE_STATUS_ID = "LeaveStatusId";
    public static final String LEAVE_IS_CLOSED = "IsClosed";
    public static final String LEAVE_FILE = "File";
    public static final String LEAVE_FULL_NAME = "FullName";
    public static final String LEAVE_TYPE = "LeaveType";
    public static final String LEAVE_STATUS = "LeaveStatus";
    public static final String LEAVE_APPLY_DATE = "ApplyDate";
    public static final String LEAVE_IS_SHORT = "IsShortLeave";
    public static final String LEAVE_NUM_OF_DAYS = "NumberOfDays";
    public static final String LEAVE_UPDATED_ID = "UpdateLeaveTypeId";
    public static final String LEAVE_MODIFIED_ON = "ModifiedOn";
    public static final String LEAVE_APPROVED_BY = "ApprovedByName";
    public static final String LEAVE_LINE_MANAGER_NAME = "LineManagerName";
    public static final String LEAVE_ATTACHMENT = "Attachment";
    public static final String LEAVE_ACT_TEXT = "leaveAction";

    /**
     * Employee Leave Status Table Ends here
     */


    /**
     * Create Table Query Strings
     */
    private static final String CREATE_TABLE_LOGIN = "create table " + TABLE_LOGIN + "(" + USER_ID + " TEXT, " + USERNAME + " TEXT, "
            + FULL_NAME + " TEXT, " + FIRST_NAME + " TEXT, " + MIDDLE_NAME + " TEXT, " + LAST_NAME + " TEXT, "
            + CNIC_NUM + " TEXT, " + EMAIL + " TEXT, " + BRANCH_ID + " TEXT, " + COUNTRY_ID + " TEXT, "
            + CITY_ID + " TEXT, " + STATE_ID + " TEXT, " + ORGANIZATION_ID + " TEXT, " + CELL_NUMBER + " TEXT, " + TELEPHONE_NUMBER + " TEXT, "
            + USER_ADDRESS + " TEXT, " + IMAGE_PATH + " TEXT, " + DESIGNATION + " TEXT, " + EMPLOYEE_NUMBER + " TEXT);";

    private static final String CREATE_TABLE_PROFILE = "create table " + TABLE_PROFILE + "(" + USER_ID + " TEXT, " + CNIC_NUM + " TEXT, "
            + TITLE_ID + " TEXT, " + TITLE + " TEXT, " + PREFIX + " TEXT, " + FIRST_NAME + " TEXT, " + MIDDLE_NAME + " TEXT, " + LAST_NAME + " TEXT, "
            + GENDER + " TEXT, " + GENDER_ID + " TEXT, " + RELATIONSHIP_TYPE_ID + " TEXT, " + RELATIONSHIP_TYPE_NAME + " TEXT, "
            + GUARDIAN_NAME + " TEXT, " + MARITAL_STATUS_ID + " TEXT, " + MARITAL_STATUS + " TEXT, " + DATE_OF_BIRTH + " TEXT, " + IMAGE_PATH + " TEXT, "
            + COUNTRY + " TEXT, " + COUNTRY_ID + " TEXT, " + STATE + " TEXT, " + STATE_ID + " TEXT, " + CITY + " TEXT, " + CITY_ID + " TEXT, "
            + USER_ADDRESS + " TEXT, " + CELL_NUMBER + " TEXT, " + TELEPHONE_NUMBER + " TEXT, " + EMAIL + " TEXT, " + NOK_FIRST_NAME + " TEXT, "
            + NOK_LAST_NAME + " TEXT, " + NOK_RELATION + " TEXT, " + NOK_RELATIONSHIP_TYPE_ID + " TEXT, " + NOK_CNIC_NUMBER + " TEXT, "
            + NOK_CELL_NUMBER + " TEXT, " + BLOOD_GROUP + " TEXT, " + BLOOD_GROUP_ID + " TEXT, " + AGE + " TEXT, " + DEPARTMENT_ID + " TEXT, "
            + DEPARTMENT + " TEXT);";

    private static final String CREATE_TABLE_EDUCATION = " create table " + TABLE_EDUCATION + "(" + EDU_DETAILS_ID + " TEXT, " + INSTITUTE_ID + " TEXT, " + INSTITUTE_NAME + " TEXT, "
            + DEGREE_ID + " TEXT, " + DEGREE_NAME + " TEXT, " + FIELD_ID + " TEXT, " + FIELD_NAME + " TEXT, "
            + START_DATE + " TEXT, " + END_DATE + " TEXT, " + ISSUE_DATE + " TEXT, " + GRADE + " TEXT, "
            + DEGREE_ACTIVE + " TEXT, " + DEGREE_ATTACHMENT + " TEXT, " + DEGREE_STATUS + " TEXT, " + GRADING_SYSTEM + " TEXT, " + TOTAL_MARKS + " TEXT, "
            + OBTAINED_MARKS + " TEXT, " + OBTAINED_PERCENTAGE + " TEXT, " + INSTITUE_COUNTRY_ID + " TEXT);";

    private static final String CREATE_TABLE_EXPERIENCE = " create table " + TABLE_EXPERIENCE + "(" + EXP_DETAIL_ID + " TEXT, " + EXP_TITLE + " TEXT, " + EXP_COMPANY_ID + " TEXT, "
            + EXP_COMPANY_NAME + " TEXT, " + EXP_COMPANY_LOC_ID + " TEXT, " + EXP_COMPANY_LOC_NAME + " TEXT, " + EXP_START_DATE + " TEXT, "
            + EXP_END_DATE + " TEXT, " + EXP_STATUS + " TEXT, " + EXP_DESCRIPTION + " TEXT, " + EXP_ATTACHMENT_PATH + " TEXT);";

    private static final String CREATE_TABLE_LEAVE_STATUS = "create table " + TABLE_LEAVE_STATUS + "(" + LEAVE_ID + " TEXT, " + LEAVE_START_DATE + " TEXT, "
            + LEAVE_END_DATE + " TEXT, " + LEAVE_TITLE + " TEXT, " + LEAVE_DESCRIPTION + " TEXT, " + LEAVE_APPLICATION_NUM + " TEXT, " + LEAVE_DESIGNATION_NAME + " TEXT, "
            + LEAVE_DEPARTMENT_NAME + " TEXT, " + LEAVE_STATUS_VALUE + " TEXT, " + LEAVE_STATUS_ID + " TEXT, " + LEAVE_IS_CLOSED + " TEXT, " + LEAVE_FILE + " TEXT, "
            + LEAVE_FULL_NAME + " TEXT, " + LEAVE_TYPE + " TEXT, " + LEAVE_STATUS + " TEXT, " + LEAVE_APPLY_DATE + " TEXT, " + LEAVE_IS_SHORT + " TEXT, "
            + LEAVE_NUM_OF_DAYS + " TEXT, " + LEAVE_UPDATED_ID + " TEXT, " + LEAVE_MODIFIED_ON + " TEXT, " + LEAVE_APPROVED_BY + " TEXT, " + LEAVE_LINE_MANAGER_NAME + " TEXT, "
            + LEAVE_ATTACHMENT + " TEXT, " + LEAVE_ACT_TEXT + " TEXT);";

    /**
     * Constructor
     *
     * @param context
     */
    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    /**
     * Method to prevent sql object leak
     *
     * @param ctx
     */
    public static DatabaseHelper getInstance(Context ctx) {

        if (mInstance == null) {
            mInstance = new DatabaseHelper(ctx.getApplicationContext());
        }
        return mInstance;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_LOGIN);
        db.execSQL(CREATE_TABLE_PROFILE);
        db.execSQL(CREATE_TABLE_EDUCATION);
        db.execSQL(CREATE_TABLE_EXPERIENCE);
        db.execSQL(CREATE_TABLE_LEAVE_STATUS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFILE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EDUCATION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPERIENCE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LEAVE_STATUS);
    }


}
