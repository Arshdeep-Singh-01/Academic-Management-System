package software.student;

import java.sql.Array;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

import org.checkerframework.common.util.report.qual.ReportUnqualified;

import java.util.Dictionary;
import java.util.Hashtable;

import software.Dashboard;
import software.Acad.acadVariables;

/*
 * Table in Database: 
 * 
 * faculty
 *  faculty_id - varchar - Primary Key
 *  name - varchar
 *  contact - varchar
 *  department - varchar
 *  designation - varchar
 * 
 * courses
 *  course_id - varchar - Primary Key
 *  entry_number - varchar
 *  status - varchar
 *  grade - varchar
 *  year - varchar
 *  semester - varchar
 * 
 * course_offerings
 *  course_id - varchar
 *  course_name - varchar
 *  instructor - varchar[]
 *  year - varchar
 *  semester - varchar
 * 
 * course_catalog
 *  course_id - varchar - Primary Key
 *  name - varchar
 *  ltp - varchar
 *  credits - varchar
 *  type - varchar
 *  department - varchar
 *  
 * pre_requisites
 *  course_id - varchar - Primary Key
 *  course_required - varchar
 *  grade - double
 * 
 * students
 *  entry_number - varchar - Primary Key
 *  name - varchar
 *  program - varchar
 *  department - varchar
 *  year_of_joining - integer
 *
 * users
 *  user_id - varchar - Primary Key
 *  password - varchar
 * 
 */

public class StudentDashboard implements Dashboard {
    private String currentSemester;
    private int currentYear;
    private int currentAcademicYear;
    public String semesterStatus;

    @Override
    public void main(Connection conn ,String userID, Scanner sc){
        acadVariables acadVar = new acadVariables(conn);
        currentAcademicYear = Integer.parseInt(acadVar.getCURRENTYEAR());
        currentSemester = acadVar.getCURRENTSEMESTER();
        semesterStatus = acadVar.getSEMESTERSTATUS();

        Constants constant = new Constants();

        System.out.println();
        int choice = 0;

        // initializing the year and semester
        this.currentYear = getYear(conn, userID); // [1,2,3,4]
        if(this.currentYear < 0){
            return;
        }

        while(true){
            try {
                System.out.println("\n-----------Welcome to Student Dashboard-----------");
                System.out.println("Select: 1-Show Student Profile, 2-View Your Courses, 3-View Offered Courses,4-Enroll Courses, 5-Drop Courses, 6-Calculate CGPA, 7-Calculate SGPA, 8-Quit");
                System.out.print("Enter your Choice: ");
                choice = sc.nextInt();
                sc.nextLine();
                choice += 100; // adding 100 for ease
                if(choice == constant.SHOW_STUDENT_PROFILE){
                    this.studentProfile(conn,userID);
                }
                else if(choice == constant.VIEW_COURSES){
                    this.viewCourses(conn,userID); // shows the courses that student is doing or have done
                }
                else if(choice == constant.VIEW_OFFERED_COURSES){
                    this.viewOfferedCourses(conn,userID,sc);
                }
                else if(choice == constant.ENROLL_COURSES){
                    this.enrollCourses(conn,userID,sc);
                }
                else if(choice == constant.DROP_COURSES){
                    this.dropCourses(conn,userID,sc);
                }
                else if(choice == constant.CALCULATE_CGPA){
                    this.calculateCGPA(conn,userID);
                }
                else if(choice == constant.CALCULATE_SGPA){
                    this.calculateSGPA(conn, userID,sc);
                }
                else if(choice == constant.LOGOUT_STUDENT){
                    System.out.println("Successfully Logged Out\n");
                    break;
                }
                else{
                    System.out.println("Invalid Choice: Enter 1, 2, 3, 4, 5, 6, 7 or 8");
                }
            } catch (Exception e) {
                // handle exception
                break;
            }
        }
        // sc.close();
    }

    private void studentProfile(Connection conn, String userID){
        Statement statement;
        try {
            String query = String.format("SELECT * FROM students WHERE entry_number = '%s';", userID);
            statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if(resultSet.next()){
                String entryNumber = resultSet.getString("entry_number");
                String name = resultSet.getString("name");
                String department = resultSet.getString("department");
                String program = resultSet.getString("program");
                String YoJ = resultSet.getString("year_of_joining");

                System.out.println("\n---------STUDENT PROFILE---------");
                System.out.println("Student Name    : " + name);
                System.out.println("Entry Number    : " + entryNumber);
                System.out.println("Department      : " + department);
                System.out.println("Program         : " + program);
                System.out.println("Year of Joining : " + YoJ);
                System.out.println("Current Year    : " + currentYear);
                System.out.println("Current Semester: " + currentSemester);
                System.out.println("---------------------------------\n");
            }
        } catch (Exception e) {
            // handle exception
            System.out.println("Error at Student Profile: "+ e);
        }
    }

    private void viewCourses(Connection conn, String userID){
        Statement statement;
        try {
            String query = String.format("SELECT * FROM courses WHERE entry_number = '%s'", userID);
            //query results sorted in increasing order of year and semester
            query = query + " ORDER BY year ASC, semester ASC;";
            statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            String SEM = "0";
            String YEAR = "0";
            System.out.println("\n---------COURSES---------");
            while(resultSet.next()){

                // query on course catalog to get the course name, credit, type, structure
                String sql = String.format("SELECT * FROM course_catalog WHERE course_id = '%s';", resultSet.getString("course_id"));
                Statement statement2 = conn.createStatement(); 
                ResultSet resultSet2 = statement2.executeQuery(sql);
                String courseName = "";
                String courseCredit = "";
                String courseType = "";
                String courseStructure = "";
                if(resultSet2.next()){
                    courseName = resultSet2.getString("name");
                    courseCredit = resultSet2.getString("credits");
                    courseType = resultSet2.getString("type");
                    courseStructure = resultSet2.getString("ltp");
                }
                else{
                    System.out.println("Error: Course not found in catalog");
                }


                String courseCode = resultSet.getString("course_id");
                String courseGrade = resultSet.getString("grade");
                String courseSemester = resultSet.getString("semester");
                String courseYear = resultSet.getString("year");
                String courseStatus = resultSet.getString("status");

               if(SEM.equals(courseSemester) == false || YEAR.equals(courseYear) == false){
                    System.out.println("\n--------- Semester: " + courseSemester + " Year: " + courseYear + "----------");
                    SEM = courseSemester;
                    YEAR = courseYear;
                }
                System.out.println();
                System.out.println("Course Code  : " + courseCode);
                System.out.println("Course Name  : " + courseName);
                System.out.println("Course Status: " + courseStatus);
                System.out.println("Course Credit: " + courseCredit);
                System.out.println("Course Type  : " + courseType);
                System.out.println("Course LTP   : " + courseStructure);
                System.out.println("Course Grade : " + courseGrade);
    
                System.out.println("-----------------------------------");

            }
        } catch (Exception e) {
            System.out.println("Error at View Courses: "+ e );
        }
        
    }

    private void viewOfferedCourses(Connection conn, String userID,Scanner sc){
        Statement statement;
            try {
                // summer - August - November: S
                // winter - January - May: W
                {
                    System.out.print("Enter the department (CSE, EE, ME, HS, GEN) or (ALL): ");
                    String department = sc.nextLine();
                    System.out.print("Enter the year (20XX): ");
                    String yearChosen = sc.nextLine();
    
                    System.out.print("Enter the semester (Summer - S, Winter - W): ");
                    String semesterChosen = sc.nextLine();
    
                    String query;
                    if(department.equals("ALL")){
                        query = String.format("SELECT cc.course_id course_id, cc.name name, cc.credits credits, cc.ltp ltp, cc.type type, cc.department department, co.instructor instructor FROM course_offerings co, course_catalog cc  WHERE co.course_id = cc.course_id and co.year = '%s' and co.semester = '%s';", yearChosen, semesterChosen);
                    }
                    else{
                        query = String.format("SELECT cc.course_id course_id, cc.name name, cc.credits credits, cc.ltp ltp, cc.type type, cc.department department, co.instructor instructor  FROM course_offerings co, course_catalog cc WHERE co.course_id = cc.course_id and co.year = '%s' and co.semester = '%s' and cc.department = '%s';", yearChosen, semesterChosen, department);
                    }
                    statement = conn.createStatement();
                    ResultSet resultSet = statement.executeQuery(query);
                    
                    System.out.println("\n---------COURSE OFFERED---------");
                    while(resultSet.next()){
                        
                        
                        //--------------- Data from course catalog--------------------------
                        String courseName = resultSet.getString("name");
                        String courseCode = resultSet.getString("course_id");
                        String courseCredit = resultSet.getString("credits");
                        String courseStructure = resultSet.getString("ltp");
                        String courseDepartment = resultSet.getString("department");
                        String courseType = resultSet.getString("type");
                        //--------------- Data from course offerings--------------------------
                        Array instructors = resultSet.getArray("instructor");
                        
                        
                        //--------------- Data from Pre-requisites--------------------------
                        String preReqQuery = String.format("SELECT * FROM pre_requisites WHERE course_id = '%s';", resultSet.getString("course_id"));
                        Statement preReqStatement = conn.createStatement();
                        ResultSet preReqResultSet = preReqStatement.executeQuery(preReqQuery);
                        ArrayList<String> preReqList = new ArrayList<String>();
                        while(preReqResultSet.next()){
                            preReqList.add(preReqResultSet.getString("course_required"));
                        }
                        
                        
                        //--------------- Data from Constraints--------------------------
                        String constraintsQuery = String.format("SELECT * FROM constraints WHERE course_id = '%s' and year_offered = '%s' and semester_offered = '%s';", resultSet.getString("course_id"),yearChosen,semesterChosen);
                        Statement constraintsStatement = conn.createStatement();
                        ResultSet constraintsResultSet = constraintsStatement.executeQuery(constraintsQuery);
                
                        ArrayList<String> constraintBranch = new ArrayList<String>();
                        ArrayList<Integer> constraintYear = new ArrayList<Integer>();
                        ArrayList<Double> constraintCGPA = new ArrayList<Double>();
                        while(constraintsResultSet.next()){
                            constraintBranch.add(constraintsResultSet.getString("branch"));
                            constraintYear.add(constraintsResultSet.getInt("year"));
                            constraintCGPA.add(constraintsResultSet.getDouble("cgpa"));
                        }
                            
                            
    
                        // print all the data
                        System.out.println("\n------------Course---------------");
                        System.out.println("Course Code: " + courseName);
                        System.out.println("Course Name: " + courseCode);
                        System.out.println("Department : " + courseDepartment);
                        System.out.println("Instructors: " + instructors);
                        System.out.println("Credits    : " + courseCredit);
                        System.out.println("LTP        : " + courseStructure);
                        System.out.println("Type       : " + courseType);
    
                        if(preReqList.size() > 0){
                            System.out.println("Pre-requisites: ");
                            for(int i = 0; i < preReqList.size(); i++){
                                System.out.println(" | " +preReqList.get(i));
                            }
                        }
                        else{
                            System.out.println("No pre-requisites");
                        }
    
                        if(constraintBranch.size() > 0){
                            System.out.println("Constraints: ");
                            for(int i = 0; i < constraintBranch.size(); i++){
                                System.out.println(" | Branch: " + constraintBranch.get(i) + " , Year: " + constraintYear.get(i) + " , CGPA: " + constraintCGPA.get(i));
                            }
                        }
                        else{
                            System.out.println("Elegibility: No");
                        }
                        System.out.println("---------------------------------\n");
                    }
                }
                // sc.close();
    
            } catch (Exception e) {
                System.out.println("Error at View Course Offered: "+ e );
            }
        }
    
    private boolean preRequisitesCheck(Connection conn, String userID, String courseID){
        Statement statement;
        try {
            String query = String.format("SELECT * FROM pre_requisites WHERE course_id = '%s';", courseID);
            statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            ArrayList<String> preRequisites = new ArrayList<String>();

            while(resultSet.next()){
                preRequisites.add(resultSet.getString("course_required"));
            }

            // check if the student has taken the pre-requisites
            for(int i = 0; i < preRequisites.size(); i++){
                String query2 = String.format("SELECT * FROM courses WHERE entry_number = '%s' and course_id = '%s' and status = 'completed';", userID, preRequisites.get(i));
                statement = conn.createStatement();
                ResultSet resultSet2 = statement.executeQuery(query2);
                if(resultSet2.next()==false){
                    System.out.println("Error: Pre-requisite not taken");
                    return false;
                }
            }
            return true;

        } catch (Exception e) {
            System.out.println("Error at Prerequisites Check: "+ e );
            return false;
        }
    }

    private Integer getGradePoint(String grade){
        Dictionary<String, Integer> dict = new Hashtable<>();
        dict.put("A", 10);
        dict.put("A-", 9);
        dict.put("B", 8);
        dict.put("B-", 7);
        dict.put("C", 6);
        dict.put("C-", 5);
        dict.put("D", 4);
        dict.put("D-", 3);
        dict.put("E", 2);
        dict.put("F", 1);
        return dict.get(grade);
    }

    public Double getCGPA(Connection conn, String userID){
        Statement statement;
        try {
            String query = String.format("SELECT * FROM courses WHERE entry_number = '%s' and status = 'completed';", userID);
            statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            Double totalCredits = 0.0;
            Double totalGradePoints = 0.0;
            while(resultSet.next()){
                String courseID = resultSet.getString("course_id");
                String courseGrade = resultSet.getString("grade");
                String query2 = String.format("SELECT * FROM course_catalog WHERE course_id = '%s';", courseID);
                statement = conn.createStatement();
                ResultSet resultSet2 = statement.executeQuery(query2);
                if(resultSet2.next()){
                    Integer credit = resultSet2.getInt("credits");
                    totalCredits += credit;
                    totalGradePoints += credit * getGradePoint(courseGrade);
                }
            }
            if(totalCredits == 0.0)
                return 0.0;
            else
                return totalGradePoints/totalCredits;
        } catch (Exception e) {
            System.out.println("Error at CGPA: "+ e );
            return 0.0;
        }
    }

    private Integer getYear(Connection conn, String userID){
        try {
            // get year of joining
            Integer YOJ = 0;
            Statement statement;
            try {
                String query = String.format("SELECT * FROM students WHERE entry_number = '%s';", userID);
                statement = conn.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                if(resultSet.next()){
                    YOJ = resultSet.getInt("year_of_joining");
                }
                else{
                    System.out.println("Error: Student not found");
                    return -1;
                }
            } catch (Exception e) {
                System.out.println("SQL: Error at Year: "+ e );
                return -2;
            }

            Integer currentYear = (currentAcademicYear - YOJ);
            if(currentSemester.equals("S") || currentYear == 0){
                currentYear += 1;
            }
            
            return currentYear;
        } catch (Exception e) {
            System.out.println("Error at Year: "+ e );
        }
        return 3;
    }

    private boolean elegiblilityCheck(Connection conn, String userID, String courseID){
        Statement statement;
        try {
            String query = String.format("SELECT * FROM courses WHERE entry_number = '%s' and course_id = '%s';", userID, courseID);
            statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if(resultSet.next()){
                System.out.println("Error: Course already taken");
                return false;
            }
            else{

                Integer studentCurrentYear = this.getYear(conn, userID);

                String query2 = String.format("SELECT * from students WHERE entry_number = '%s';", userID);
                statement = conn.createStatement();
                ResultSet resultSet2 = statement.executeQuery(query2);
                if(resultSet2.next()){
                    String department = resultSet2.getString("department");
                    Double currentCGPA = getCGPA(conn, userID);
                    // check if the student is eligible for the course
                    String queery3 = String.format("SELECT * from constraints WHERE branch = '%s' and course_id = '%s' and semester_offered = '%s' and year_offered = '%s' and year = '%d';", department, courseID,currentSemester,currentAcademicYear + "", studentCurrentYear);
                    statement = conn.createStatement();
                    ResultSet resultSet3 = statement.executeQuery(queery3);
                    if(resultSet3.next()){
                        Double requiredCGPA = resultSet3.getDouble("cgpa");
                        if(currentCGPA < requiredCGPA){
                            System.out.println("Error: CGPA not met");
                            return false;
                        }
                        else{
                            // Elegibility check passed
                            return true;
                        }
                    }
                    else{
                        //department / year not allowed
                        return false;
                    }
                }
                else{
                    System.out.println("Error: Student not found");
                    return false;
                }
            }
        } catch (Exception e) {
            System.out.println("Error at Elegibility Check: "+ e );
            return false;
        }
    }

    private Integer getEarnedCreditsInSemester(Connection conn, String userID, String semester, String year){
        Statement statement;
        try {
            String query = String.format("SELECT course_id FROM courses WHERE entry_number = '%s' and status = 'completed' and semester = '%s' and year = '%s';", userID, semester, year);
            statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            Integer totalCredits = 0;
            while(resultSet.next()){
                String courseID = resultSet.getString("course_id");
                String query2 = String.format("SELECT * FROM course_catalog WHERE course_id = '%s';", courseID);
                statement = conn.createStatement();
                ResultSet resultSet2 = statement.executeQuery(query2);
                if(resultSet2.next()){
                    Integer credit = resultSet2.getInt("credits");
                    totalCredits += credit;
                }
            }
            return totalCredits;
        } catch (Exception e) {
            System.out.println("Error at Credits: "+ e );
            return 0;
        }
    }

    private Integer getElegibleCredits(Connection conn, String userID){
        if(currentYear==1){
            return 24;
        }
        else{
            Double totalCredits = 0.0;
            totalCredits += getEarnedCreditsInSemester(conn, userID, "W", currentAcademicYear - 1 + "");
            if(currentSemester=="S"){
                totalCredits += getEarnedCreditsInSemester(conn, userID, "S", currentAcademicYear - 1 + "");
            }
            else{
                totalCredits += getEarnedCreditsInSemester(conn, userID, "S", currentAcademicYear + "");
            }
            // return ceiling of totalCredits/2
            return (int) Math.ceil((totalCredits/2)*1.25);
        }
    }

    private boolean checkCreditLimit(Connection conn, String userID, String courseID){
        Statement statement;
        Integer givenCourseCredits = 100;
        try{
            String query = String.format("SELECT * FROM course_catalog WHERE course_id = '%s';", courseID);
            statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if(resultSet.next()){
                givenCourseCredits = resultSet.getInt("credits");
            }

            Integer elegibleCredits = getElegibleCredits(conn, userID);
            
            String queryCourses = String.format("SELECT * FROM courses WHERE entry_number = '%s' and status = 'enrolled';", userID);
            statement = conn.createStatement();
            ResultSet resultSetCourses = statement.executeQuery(queryCourses);
            Integer totalCreditsEnrolledThisSem = 0;
            while(resultSetCourses.next()){
                String courseID2 = resultSetCourses.getString("course_id");
                String query2 = String.format("SELECT * FROM course_catalog WHERE course_id = '%s';", courseID2);
                statement = conn.createStatement();
                ResultSet resultSet2 = statement.executeQuery(query2);
                if(resultSet2.next()){
                    Integer credit = resultSet2.getInt("credits");
                    totalCreditsEnrolledThisSem += credit;
                }
            }
            if(totalCreditsEnrolledThisSem + givenCourseCredits > elegibleCredits){
                System.out.println("Error: Credits limit exceeded");
                return false;
            }
            else{
                return true;
            }

        } catch (Exception e) {
            System.out.println("Error at Credits Check: "+ e );
            return false;
        }
    }

    private void enrollThisCourse(Connection conn, String userID, String courseCode){
        Statement statement;
        try {
            String query2 = String.format("SELECT * FROM course_catalog WHERE course_id = '%s';", courseCode);
            statement = conn.createStatement();
            ResultSet resultSet2 = statement.executeQuery(query2);
            if(resultSet2.next()){
                // course is offered in the current semester
                String sql = String.format("INSERT INTO courses (entry_number, course_id, status, semester, year, grade) VALUES ('%s', '%s', 'enrolled', '%s', '%s', 'NA');", userID, courseCode,currentSemester , currentAcademicYear + "");
                statement = conn.createStatement();
                statement.executeUpdate(sql);
                System.out.println("Course enrolled successfully");
            }
        } catch (Exception e) {
            System.out.println("Error at Enroll Courses: "+ e );
        }
    }

    private void enrollCourses(Connection conn, String userID,Scanner sc){
        try{
            if(semesterStatus.equals("enroll")==false){
                System.out.println("You cannot enroll in courses at this time");
                return;
            }
            System.out.print("Enter the course code: ");
            String courseCode = sc.nextLine();
            if(checkCreditLimit(conn,userID,courseCode)==false){
                System.out.println("You don't have sufficient credits to enroll in this course");
            }
            else if(elegiblilityCheck(conn, userID, courseCode) == false){
                System.out.println("You are not elegible for this course");
            }
            else if(preRequisitesCheck(conn, userID, courseCode) == false){
                System.out.println("You do not have the pre-requisites for this course");
            }
            else{
                // elegible for enrollment, so let's enroll
                enrollThisCourse(conn,userID,courseCode);
            }
        } catch (Exception e) {
            System.out.println("Error at Enroll Courses: "+ e);
        }
    }

    private void dropCourses(Connection conn, String userID,Scanner sc){
        try {
            if(semesterStatus.equals("enroll")==false){
                System.out.println("You cannot enroll/drop in courses at this time");
                return;
            }
            Statement statement = null;
            statement = conn.createStatement();
            System.out.println("Enter the course code to drop: ");
            String courseCode = sc.nextLine();
            String query = String.format("SELECT * FROM courses WHERE entry_number = '%s' and course_id = '%s' and status = 'enrolled' and year = '%s' and semester = '%s';", userID, courseCode, currentAcademicYear + "", currentSemester);
            ResultSet resultSet = statement.executeQuery(query);
            if(resultSet.next()){
                // remove that course for that userID from the courses table
                String sql = String.format("DELETE FROM courses WHERE entry_number = '%s' and course_id = '%s' and status = 'enrolled' and year = '%s' and semester = '%s';", userID, courseCode, currentAcademicYear + "", currentSemester);
                statement = conn.createStatement();
                if(statement.executeUpdate(sql)>0){
                    System.out.println("Course dropped successfully");
                }
                else{
                    System.out.println("Error: Course not found (Deletion failed)");
                }
            }
            else{
                System.out.println("Error: You are not enrolled in this course");
                return;
            }

        } catch (Exception e) {
            System.out.println("Error at Drop Courses: "+ e);
        }
    }

    private void calculateCGPA(Connection conn, String userID){
        try {
            Double CGPA = this.getCGPA(conn, userID);
            System.out.println("\n| Your CGPA is: " + CGPA + "\n");
        } catch (Exception e) {
            System.out.println("Error at Student> calculateCGPA: "+ e);
        }
    }

    public Double getSGPA(Connection conn, String userID, String year, String sem){
        try {
            Statement statement = null;
            String query = String.format("SELECT * FROM courses WHERE entry_number = '%s' and status = 'completed' and year = '%s' and semester = '%s';", userID, year, sem);
            statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            Double totalCredits = 0.0;
            Double totalGradePoints = 0.0;
            while(resultSet.next()){
                String courseID = resultSet.getString("course_id");
                String courseGrade = resultSet.getString("grade");
                String query2 = String.format("SELECT * FROM course_catalog WHERE course_id = '%s';", courseID);
                statement = conn.createStatement();
                ResultSet resultSet2 = statement.executeQuery(query2);
                if(resultSet2.next()){
                    Integer credit = resultSet2.getInt("credits");
                    totalCredits += credit;
                    totalGradePoints += credit * getGradePoint(courseGrade);
                }
                else{
                    System.out.println("Error: Course " + courseID +" not found in course_catalog");
                }
            }
            Double SGPA = totalGradePoints / totalCredits;
            if(SGPA.isNaN()){
                SGPA = 0.0;
            }
            return SGPA;    
        } catch (Exception e) {
            System.out.println("Error at Student> calculateSGPA: "+ e);
        }
        return 0.0;
    }

    private void calculateSGPA(Connection conn, String userID,Scanner sc){
        try {
            System.out.print("Enter the year: ");
            String year = sc.nextLine();
            System.out.print("Enter the semester: ");
            String sem = sc.nextLine();
            Double SGPA = getSGPA(conn, userID, year, sem);
            System.out.println("\n| Your SGPA for (" + year + ", " + sem + ") is: " + SGPA + "\n");
        } catch (Exception e) {
            System.out.println("Error at Student> calculateSGPA: "+ e);
        }
    }
}
