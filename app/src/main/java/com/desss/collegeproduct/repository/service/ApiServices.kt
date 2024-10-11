package com.desss.collegeproduct.repository.service

import com.desss.collegeproduct.commonfunctions.CommonResponseModel
import com.desss.collegeproduct.module.admission.model.AdmissionModel
import com.desss.collegeproduct.module.admission.model.CourseModel
import com.desss.collegeproduct.module.admission.model.DegreeModel
import com.desss.collegeproduct.module.auth.model.ChangePasswordModel
import com.desss.collegeproduct.module.auth.model.ForgotModel
import com.desss.collegeproduct.module.auth.model.LoginModel
import com.desss.collegeproduct.module.commonDashBoardFragment.notification.model.NotificationModel
import com.desss.collegeproduct.module.commonDashBoardFragment.profile.model.ProfileModel
import com.desss.collegeproduct.module.managementSubModule.professorDetail.model.TotalProfessorModel
import com.desss.collegeproduct.module.managementSubModule.studentDetail.model.DegreeDepartmentSectionModel
import com.desss.collegeproduct.module.managementSubModule.studentDetail.model.StudentListManagementBasedModel
import com.desss.collegeproduct.module.professorSubModule.department.model.DepartmentModel
import com.desss.collegeproduct.module.professorSubModule.notes.model.UploadSuccessModel
import com.desss.collegeproduct.module.professorSubModule.professorAttendance.model.CheckProfessorAttendanceModel
import com.desss.collegeproduct.module.professorSubModule.professorAttendance.model.ProfessorCountModel
import com.desss.collegeproduct.module.professorSubModule.report.model.AddReportModel
import com.desss.collegeproduct.module.professorSubModule.report.model.ProfessorStudentReportModel
import com.desss.collegeproduct.module.professorSubModule.report.model.StudentListBasedModel
import com.desss.collegeproduct.module.professorSubModule.report.model.ViewReportsModel
import com.desss.collegeproduct.module.professorSubModule.schedule.model.PostScheduleModel
import com.desss.collegeproduct.module.professorSubModule.schedule.model.UpdateScheduleModel
import com.desss.collegeproduct.module.professorSubModule.schedule.model.ViewScheduleModel
import com.desss.collegeproduct.module.professorSubModule.studentAttendance.model.AddStudentAttendanceModel
import com.desss.collegeproduct.module.professorSubModule.studentAttendance.model.StudentCountModel
import com.desss.collegeproduct.module.studentSubModule.Lms.model.LmsDurationModel
import com.desss.collegeproduct.module.studentSubModule.Lms.model.LmsModel
import com.desss.collegeproduct.module.studentSubModule.Lms.model.PostLmsDurationModel
import com.desss.collegeproduct.module.studentSubModule.Lms.model.UpdateLmsExamModel
import com.desss.collegeproduct.module.studentSubModule.attendance.model.MonthlyHolidaysModel
import com.desss.collegeproduct.module.studentSubModule.attendance.model.StudentAttendanceModel
import com.desss.collegeproduct.module.studentSubModule.examTimeTable.model.ExamTimeTableModel
import com.desss.collegeproduct.module.studentSubModule.feePay.model.FeePayModel
import com.desss.collegeproduct.module.studentSubModule.notes.model.NotesModel
import com.desss.collegeproduct.module.studentSubModule.remarks.model.RemarksModel
import com.desss.collegeproduct.module.studentSubModule.results.model.ResultsModel
import com.desss.collegeproduct.module.studentSubModule.syllabus.model.SyllabusModel
import com.desss.collegeproduct.module.studentSubModule.transport.model.TransportModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


interface ApiServices {
    //Login Api
    @Multipart
    @POST("dynamic/dynamicapi.php")
    fun loginSubmit(
        @Part("action") action: RequestBody?,
        @Part("email") email: RequestBody?,
        @Part("password") password: RequestBody?
    ): Call<CommonResponseModel<LoginModel>?>?


    //Otp Api
    @Headers(*["Content-Type: application/json;charset=UTF-8"])
    @GET("dynamic/dynamicapi.php")
    fun getOtp(
        @Query("action") action: String?,
        @Query("email") userId: String?
    ): Call<CommonResponseModel<ForgotModel>?>?


    //Change Password Api
    @Headers(*["Content-Type: application/json;charset=UTF-8"])
    @GET("dynamic/dynamicapi.php")
    fun getChangePassword(
        @Query("action") action: String?,
        @Query("email") userId: String?,
        @Query("confirm_password") confirmPassword: String?
    ): Call<CommonResponseModel<ChangePasswordModel>?>?

    //Notification Api
    @Headers(*["Content-Type: application/json;charset=UTF-8"])
    @GET("dynamic/dynamicapi.php")
    fun notification(
        @Query("action") action: String?,
        @Query("table") tableName: String?
    ): Call<CommonResponseModel<NotificationModel>?>?


    //Profile Api
    @Headers(*["Content-Type: application/json;charset=UTF-8"])
    @GET("dynamic/dynamicapi.php")
    fun profile(
        @Query("action") action: String?,
        @Query("table") tableName: String?,
        @Query("id") id: String?
    ): Call<CommonResponseModel<ProfileModel>?>?


    //StudentAttendance Api
    @Headers(*["Content-Type: application/json;charset=UTF-8"])
    @GET("dynamic/dynamicapi.php")
    fun studentAttendance(
        @Query("action") action: String?,
        @Query("user_id") id: String?
    ): Call<CommonResponseModel<StudentAttendanceModel>?>?

    //StudentSyllabus Api
    @Headers(*["Content-Type: application/json;charset=UTF-8"])
    @GET("dynamic/dynamicapi.php")
    fun studentSyllabus(
        @Query("action") action: String?,
        @Query("table") table: String?,
        @Query("degree") degree: String?,
        @Query("course") course: String?,
        @Query("semester") semester: String?
    ): Call<CommonResponseModel<SyllabusModel>?>?

    //StudentNotes Api
    @Headers(*["Content-Type: application/json;charset=UTF-8"])
    @GET("dynamic/dynamicapi.php")
    fun studentNotes(
        @Query("action") table: String?,
        @Query("degree") degree: String?,
        @Query("course") course: String?,
        @Query("semester") semester: String?,
        @Query("section") section: String?
    ): Call<CommonResponseModel<NotesModel>?>?

    //StudentRemarks Api
    @Headers(*["Content-Type: application/json;charset=UTF-8"])
    @GET("dynamic/dynamicapi.php")
    fun studentRemarks(
        @Query("action") action: String?,
        @Query("table") table: String?,
        @Query("user_id") userId: String?
    ): Call<CommonResponseModel<RemarksModel>?>?

    //StudentExamTimeTable Api
    @Headers(*["Content-Type: application/json;charset=UTF-8"])
    @GET("dynamic/dynamicapi.php")
    fun studentExamTimeTable(
        @Query("action") action: String?,
        @Query("table") table: String?,
        @Query("degree") degree: String?,
        @Query("course") course: String?,
        @Query("semester") semester: String?
    ): Call<CommonResponseModel<ExamTimeTableModel>?>?

    //StudentFeePay Api
    @Headers(*["Content-Type: application/json;charset=UTF-8"])
    @GET("dynamic/dynamicapi.php")
    fun studentFeePay(
        @Query("action") action: String?,
        @Query("table") table: String?,
        @Query("user_id") userId: String?
    ): Call<CommonResponseModel<FeePayModel>?>?

    //Professor DepartmentFragment Api
    @Headers(*["Content-Type: application/json;charset=UTF-8"])
    @GET("dynamic/dynamicapi.php")
    fun professorDepartment(
        @Query("action") action: String?,
        @Query("table") table: String?,
        @Query("user_id") userId: String?
    ): Call<CommonResponseModel<DepartmentModel>?>?

    //Professor based Department Api
    @Headers(*["Content-Type: application/json;charset=UTF-8"])
    @GET("dynamic/dynamicapi.php")
    fun getProfessorDepartment(
        @Query("action") action: String?,
        @Query("table") table: String?,
        @Query("user_id") userId: String?
    ): Call<CommonResponseModel<ProfessorStudentReportModel>?>?

    //Professor based Department Api
    @Headers(*["Content-Type: application/json;charset=UTF-8"])
    @GET("dynamic/dynamicapi.php")
    fun getStudentList(
        @Query("action") action: String?,
        @Query("degree") degree: String?,
        @Query("departement") department: String?,
        @Query("semester") semester: String?,
        @Query("section") section: String?
    ): Call<CommonResponseModel<StudentListBasedModel>?>?


    //Reports List by professor
    @Headers(*["Content-Type: application/json;charset=UTF-8"])
    @GET("dynamic/dynamicapi.php")
    fun getReportsListByProfessor(
        @Query("action") action: String?,
        @Query("table") table: String?,
        @Query("pro_user_id") userId: String?,
        @Query("status") status: String?
    ): Call<CommonResponseModel<ViewReportsModel>?>?


    //Post Report for Students
    @Headers(*["Content-Type: application/json;charset=UTF-8"])
    @GET("dynamic/dynamicapi.php")
    fun postAddReport(
        @Query("action") action: String?,
        @Query("pro_user_id") professorId: String?,
        @Query("user_id") studentId: String?,
        @Query("content") content: String?,
        @Query("reg_no") regNo: String?,
        @Query("name") name: String?
    ): Call<CommonResponseModel<AddReportModel>?>?

    //Upload Notes Data
    @Multipart
    @POST("dynamic/dynamicapi.php")
    fun uploadPdfValuesWithBodyMultiPart(
        @Part("action") action: RequestBody,
        @Part("user_id") professorId: RequestBody,
        @Part("degree") degree: RequestBody,
        @Part("department") department: RequestBody,
        @Part("section") section: RequestBody,
        @Part("semester") semester: RequestBody,
        @Part("name") name: RequestBody,
        @Part pdfFile: MultipartBody.Part
    ): Call<CommonResponseModel<UploadSuccessModel>?>?

    //students count list
    @Headers(*["Content-Type: application/json;charset=UTF-8"])
    @GET("dynamic/dynamicapi.php")
    fun getStudentCountList(
        @Query("action") action: String?,
        @Query("department") department: String?,
        @Query("section") section: String?,
        @Query("semester") semester: String?,
        @Query("degree") degree: String?
    ): Call<CommonResponseModel<StudentCountModel>?>?

    //mark students attendance
    @Headers(*["Content-Type: application/json;charset=UTF-8"])
    @GET("dynamic/dynamicapi.php")
    fun getStudentAttendanceList(
        @Query("action") action: String?,
        @Query("professor_ids") professorId: String?,
        @Query("user_ids") totalStudentId: String?,
        @Query("statuses") selectedStudentId: String?
    ): Call<CommonResponseModel<AddStudentAttendanceModel>?>?


    //check professor today attendance
    @Headers(*["Content-Type: application/json;charset=UTF-8"])
    @GET("dynamic/dynamicapi.php")
    fun getProfessorAttendance(
        @Query("action") action: String?,
        @Query("user_id") userId: String?
    ): Call<CommonResponseModel<CheckProfessorAttendanceModel>?>?

    //check professor attendance count
    @Headers(*["Content-Type: application/json;charset=UTF-8"])
    @GET("dynamic/dynamicapi.php")
    fun getProfessorAttendanceCount(
        @Query("action") action: String?,
        @Query("user_id") userId: String?,
        @Query("month") month: String?,
        @Query("year") year: String?
    ): Call<CommonResponseModel<ProfessorCountModel>?>?

    //mark professor attendance
    @Headers(*["Content-Type: application/json;charset=UTF-8"])
    @GET("dynamic/dynamicapi.php")
    fun getMarkProfessorAttendanceCount(
        @Query("action") action: String?,
        @Query("user_id") userId: String?
    ): Call<CommonResponseModel<CheckProfessorAttendanceModel>?>?

    //post Schedule Message
    @Headers(*["Content-Type: application/json;charset=UTF-8"])
    @GET("dynamic/dynamicapi.php")
    fun getPostSchedule(
        @Query("action") action: String?,
        @Query("user_id") userId: String?,
        @Query("degree") degree: String?,
        @Query("department") department: String?,
        @Query("section") section: String?,
        @Query("semester") semester: String?,
        @Query("date") date: String?,
        @Query("from_time") fromTime: String?,
        @Query("to_time") toTime: String?,
        @Query("notes") scheduleMessage: String?
    ): Call<CommonResponseModel<PostScheduleModel>?>?


    //Professor View Schedule Api
    @Headers(*["Content-Type: application/json;charset=UTF-8"])
    @GET("dynamic/dynamicapi.php")
    fun getViewSchedule(
        @Query("action") action: String?,
        @Query("table") table: String?,
        @Query("user_id") userId: String?
    ): Call<CommonResponseModel<ViewScheduleModel>?>?

    //Professor View Schedule By Date Api
    @Headers(*["Content-Type: application/json;charset=UTF-8"])
    @GET("dynamic/dynamicapi.php")
    fun getViewScheduleByDate(
        @Query("action") action: String?,
        @Query("table") table: String?,
        @Query("user_id") userId: String?,
        @Query("date") date: String?
    ): Call<CommonResponseModel<ViewScheduleModel>?>?


    //Professor Update Schedule Api
    @Headers(*["Content-Type: application/json;charset=UTF-8"])
    @GET("dynamic/dynamicapi.php")
    fun getUpdateSchedule(
        @Query("action") action: String?,
        @Query("table") table: String?,
        @Query("id") scheduleId: String?,
        @Query("from_time") fromTime: String?,
        @Query("to_time") toTime: String?,
        @Query("notes") notes: String?
    ): Call<CommonResponseModel<UpdateScheduleModel>?>?

    //management side based Department Api
    @Headers(*["Content-Type: application/json;charset=UTF-8"])
    @GET("dynamic/dynamicapi.php")
    fun getStudentSideDepartmentMangement(
        @Query("action") action: String?,
        @Query("table") table: String?,
    ): Call<CommonResponseModel<DegreeDepartmentSectionModel>?>?

    //Professor based Department Api
    @Headers(*["Content-Type: application/json;charset=UTF-8"])
    @GET("dynamic/dynamicapi.php")
    fun getStudentListByManagement(
        @Query("action") action: String?,
        @Query("degree") degree: String?,
        @Query("departement") department: String?,
        @Query("semester") semester: String?,
        @Query("section") section: String?
    ): Call<CommonResponseModel<StudentListManagementBasedModel>?>?


    //Total Professor List Api
    @Headers(*["Content-Type: application/json;charset=UTF-8"])
    @GET("dynamic/dynamicapi.php")
    fun professorList(
        @Query("action") action: String?,
        @Query("table") table: String?,
        @Query("roll_id") rollId: String?
    ): Call<CommonResponseModel<TotalProfessorModel>?>?

    //Management Degree Api
    @Headers(*["Content-Type: application/json;charset=UTF-8"])
    @GET("dynamic/dynamicapi.php")
    fun managementDegrees(
        @Query("action") action: String?,
        @Query("table") table: String?,
        @Query("is_status") status: String?,
        @Query("is_deleted") deleted: String?
    ): Call<CommonResponseModel<DegreeModel>?>?

    //Management Course Api
    @Multipart
    @POST("dynamic/dynamicapi.php")
    fun managementCourses(
        @Part("action") action: RequestBody?,
        @Part("degree_id") degreeId: RequestBody?
    ): Call<CommonResponseModel<CourseModel>?>?

    //Management Admission Api
    @Multipart
    @POST("dynamic/dynamicapi.php")
    fun postAdmissionDetails(
        @Part("action") action: RequestBody?,
        @Part("ams_id") amsId: RequestBody?,
        @Part("first_name") firstName: RequestBody?,
        @Part("last_name") lastName: RequestBody?,
        @Part("email") email: RequestBody?,
        @Part("phone") phoneNumber: RequestBody?,
        @Part("alter_phone") alterPhoneNumber: RequestBody?
    ): Call<CommonResponseModel<AdmissionModel>?>?

    //Student Results Api
    @Headers(*["Content-Type: application/json;charset=UTF-8"])
    @GET("dynamic/dynamicapi.php")
    fun studentResults(
        @Query("action") action: String?,
        @Query("table") table: String?,
        @Query("timetable_id") timeTableId: String?,
        @Query("user_id") userId: String?
    ): Call<CommonResponseModel<ResultsModel>?>?

    //LmsLessonApi
    @Headers(*["Content-Type: application/json;charset=UTF-8"])
    @GET("dynamic/dynamicapi.php")
    fun lmsLessonData(
        @Query("action") action: String?,
        @Query("degree") degree: String?,
        @Query("department") department: String?,
        @Query("semester") semester: String?
    ): Call<CommonResponseModel<LmsModel>?>?

    @Headers(*["Content-Type: application/json;charset=UTF-8"])
    @GET("dynamic/dynamicapi.php")
    fun lmsSingleLessonData(
        @Query("action") action: String?,
        @Query("degree") degree: String?,
        @Query("department") department: String?,
        @Query("semester") semester: String?,
        @Query("id") lessonId: String?
    ): Call<CommonResponseModel<LmsModel>?>?

    @Headers(*["Content-Type: application/json;charset=UTF-8"])
    @GET("dynamic/dynamicapi.php")
    fun lmsDurationData(
        @Query("action") action: String?,
        @Query("user_id") userId: String?,
        @Query("lms_id") lmsId: String?
    ): Call<CommonResponseModel<LmsDurationModel>?>?

    @Multipart
    @POST("dynamic/dynamicapi.php")
    fun postDuration(
        @Part("action") action: RequestBody?,
        @Part("user_id") userId: RequestBody?,
        @Part("lms_id") lmsId: RequestBody?,
        @Part("last_watched_duration") lastWatchedDuration: RequestBody?,
        @Part("custom_duration") customDuration: RequestBody?
    ): Call<CommonResponseModel<PostLmsDurationModel>?>?

    @Multipart
    @POST("dynamic/dynamicapi.php")
    fun postExamAnswers(
        @Part("action") action: RequestBody?,
        @Part("user_id") userId: RequestBody?,
        @Part("lms_id") lmsId: RequestBody?,
        @Part("question_count") questionCount: RequestBody?,
        @Part("answer_count") answerCount: RequestBody?,
        @Part("qus_ans") questionAnswer: RequestBody?
    ): Call<CommonResponseModel<UpdateLmsExamModel>?>?


    @Headers(*["Content-Type: application/json;charset=UTF-8"])
    @GET("dynamic/dynamicapi.php")
    fun transportData(
        @Query("action") action: String?,
        @Query("bus_no") degree: String?
    ): Call<CommonResponseModel<TransportModel>?>?

    //StudentSyllabus Api
    @Headers(*["Content-Type: application/json;charset=UTF-8"])
    @GET("dynamic/dynamicapi.php")
    fun montlhyHolidays(
        @Query("action") action: String?,
        @Query("table") table: String?,
        @Query("month") month: String?,
        @Query("year") year: String?
    ): Call<CommonResponseModel<MonthlyHolidaysModel>?>?

}