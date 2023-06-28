package software.student;

public class Constants {
    //--------------------app.java-----------------
    public int LOGIN = 1;
    public int QUIT = 2;

    // ----------------student Dashboard-----------
    public int SHOW_STUDENT_PROFILE = 101;
    public int VIEW_COURSES = 102;
    public int VIEW_OFFERED_COURSES = 103;
    public int ENROLL_COURSES = 104;
    public int DROP_COURSES = 105;
    public int CALCULATE_CGPA = 106;
    public int CALCULATE_SGPA = 107;
    public int LOGOUT_STUDENT = 108;


    // ----------------faculity Dashboard-----------
    public String EXIT_CHOICE = "6";

    // ----------------admin Dashboard-----------
    public Integer MIN_ELECTIVE_CREDITS = 3;
    public Integer MIN_CORE_CREDITS = 3;
    public Integer MIN_BTP_CREDITS = 3;
    public String transcriptFolderPath = "E:/IITRopar/6th_semester/se/app/src/main/java/software/Acad/Transcripts/";
    public String gradeFilePath = "E:/IITRopar/6th_semester/code_2020csb1074/app/src/main/java/software/Faculty/grades.csv";


    // env variables
    public String DB_URL = "jdbc:postgresql://localhost:5432/";
    public String DB_NAME = "student";
    public String DB_username = "postgres";
    public String DB_password = "1234";

}
