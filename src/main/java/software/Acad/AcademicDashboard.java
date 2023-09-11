package software.Acad;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.io.File;
import java.io.FileWriter;

import software.Dashboard;
import software.student.Constants;
import software.student.StudentDashboard;

import java.util.Date;
import java.util.Scanner;


public class AcademicDashboard implements Dashboard {
    private String currentACADYear;
    private String currentACADSemester;
    
    @Override
    public void main(Connection conn, String userID, Scanner sc) {
        if(userID.equals("dean")==false){
            System.out.println("You are not authorized to access this page");
            return;
        }
        acadVariables acadVar = new acadVariables(conn);
        currentACADSemester = acadVar.getCURRENTSEMESTER();
        currentACADYear = acadVar.getCURRENTYEAR();

        System.out.println();
        String choice = "x";

        while(true){
            System.out.println("\n-----------Welcome to Academic Dashboard-----------");
            System.out.println("\nSelect: 0-To view Academic Session Details, 1-To Update Academic Session, 2-To Edit/View Course Catalog, 3-View Student Grades,4-Graduation Check, 5-Quit");
            System.out.print("Enter your Choice: ");
            choice = sc.nextLine();

            switch(choice){
                case "0":
                    System.out.println("\n----Academic Session Details");
                    getAcademicSessionDetails(conn,sc);
                    break;
                case "1":
                    System.out.println("\n----Update Academic Session");
                    updateAcademicSession(conn,sc); // update in th db - academic section
                    break;
                case "2":
                    System.out.println("\n----Edit/View Course Catalog");
                    editViewCourseCatalog(conn,sc);
                    break;
                case "3":
                    System.out.println("\n----View Student Grades");
                    viewStudentGrades(conn,sc);
                    break;
                case "4":
                    System.out.println("\n----Check Graduation");
                    checkGraduation(conn,sc);
                    break;
                case "5":
                    System.out.println("\n----Quit");
                    return;
                default:
                    System.out.println("Invalid Choice");
                    break;
            }
        }
    }

    private void getAcademicSessionDetails(Connection conn, Scanner sc) {
        try {
            String query = "SELECT * FROM academic_variables";
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            resultSet.next();
            System.out.println("Academic Session Status  : " + resultSet.getString("semester_status"));
            System.out.println("Academic Session Year    : " + resultSet.getString("current_year"));
            System.out.println("Academic Session Semester: " + resultSet.getString("current_semester"));
        } catch (Exception e) {
            System.out.println("Error in getAcademicSessionDetails: " + e);
        }
    }

    public void updateAcademicSession(Connection conn, Scanner sc){
        try {
            String choice = "0";
            while(true){
                System.out.println("\nSELECT:1-To update Academic Session Status, 2-To update Current Academic Year, 3-To update Current Academic Semester, 4-Exit\n");
                System.out.print("Enter your Choice: ");
                choice = sc.nextLine();
                switch(choice){
                    case "1":
                        updateAcademicSessionStatus(conn,sc);
                        break;
                    case "2":
                        updateAcademicSessionYear(conn,sc);
                        break;
                    case "3":
                        updateAcademicSessionSemester(conn,sc);
                        break;
                    case "4":
                        System.out.println("Exit");
                        return;
                    default:
                        System.out.println("Invalid Choice");
                        break;
                }
                if(choice.equals("4")){
                    break;
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Error at Acad>updateAcademicSession: "+e);
        }
    }

    /*
     * Update Academic Session Status
     * Status: 
     * start: Starting of the new Semester
     * float: floating of the courses (by the Faculty)
     * enroll: enrollment/drop of the courses (by the Students)
     * running: (enrollemnt closed) courses are running
     * grade_submission: (by the Faculty)
     * 
     */
    public void updateAcademicSessionStatus(Connection conn, Scanner sc){
        try {
            System.out.println("Academic Session Status: start, float, enroll, running, grade submission");
            System.out.print("Enter the new Academic Session Status: ");
            String newStatus = sc.nextLine();
            Statement stmt = conn.createStatement();
            String query = "UPDATE academic_variables SET semester_status = '"+newStatus+"'";
            stmt.executeUpdate(query);
            System.out.println("Academic Session Status Updated: "+newStatus);

            
        } catch (Exception e) {
            System.out.println("Error at Acad>updateAcademicSessionStatus: "+e);
        }
    }

    public void updateAcademicSessionYear(Connection conn, Scanner sc){
        try {
            System.out.print("Enter the new Academic Session Year: ");
            String newYear = sc.nextLine();
            Statement stmt = conn.createStatement();
            String query = "UPDATE academic_variables SET current_year = '"+newYear+"'";
            stmt.executeUpdate(query);
            System.out.println("Academic Session Year Updated");
            currentACADYear = newYear;
        } catch (Exception e) {
            System.out.println("Error at Acad>updateAcademicSessionYear: "+e);
        }
    }

    public void updateAcademicSessionSemester(Connection conn,Scanner sc){
        try {
            System.out.print("Enter the new Academic Session Semester: ");
            String newSemester = sc.nextLine();
            Statement stmt = conn.createStatement();
            String query = "UPDATE academic_variables SET current_semester = '"+newSemester+"'";
            stmt.executeUpdate(query);
            System.out.println("Academic Session Semester Updated");
            currentACADSemester = newSemester;
        } catch (Exception e) {
            System.out.println("Error at Acad>updateAcademicSessionSemester: "+e);
        }
    }

    public void editViewCourseCatalog(Connection conn,Scanner sc){
        try {
            String choice = "0";
            while(true){
                System.out.println("\nSELECT:1-To Add Course, 2-To Remove Course, 3-To Edit Course, 4-To View Course Catalog, 5-Exit\n");
                System.out.print("Enter your Choice: ");
                choice = sc.nextLine();
                switch(choice){
                    case "1":
                        addCourse(conn,sc);
                        break;
                    case "2":
                        removeCourse(conn,sc);
                        break;
                    case "3":
                        editCourse(conn,sc);
                        break;
                    case "4":
                        viewCourseCatalog(conn);
                        break;
                    case "5":
                        System.out.println("Exit");
                        break;
                    default:
                        System.out.println("Invalid Choice");
                        break;
                }
                if(choice.equals("5")){
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Error at Acad>editViewCourseCatalog: "+e);
        }
    }

    public void addCourse(Connection conn,Scanner sc){
        try {
            System.out.print("Enter the Course ID: ");
            String courseID = sc.nextLine();
            if(alreadyAddedCourse(conn, courseID)){
                System.out.println("Course Already Added\n");
                return;
            }
            else{
                System.out.print("Enter the Course Name: ");
                String courseName = sc.nextLine();
                System.out.print("Enter the Course LTP: ");
                String courseLTP = sc.nextLine();
                System.out.print("Enter the Course Credits: ");
                Integer courseCredits = sc.nextInt();
                sc.nextLine();
                System.out.print("Enter the Course Type: ");
                String courseType = sc.nextLine();
                System.out.print("Enter the Course Department: ");
                String courseDepartment = sc.nextLine();
                Statement stmt = conn.createStatement();
                String query = "INSERT INTO course_catalog(course_id, name, ltp, credits, type, department) VALUES('"+courseID+"', '"+courseName+"', '"+courseLTP+"', "+courseCredits+", '"+courseType+"', '"+courseDepartment+"');";
                stmt.executeUpdate(query);
                System.out.println("Course Added");
            }
        } catch (Exception e) {
            System.out.println("Error at Acad>addCourse: "+e);
        }
    }

    public void removeCourse(Connection conn,Scanner sc){
        try {
            System.out.print("Enter the Course ID: ");
            String courseID = sc.nextLine();
            if(alreadyAddedCourse(conn, courseID)){
                Statement stmt = conn.createStatement();
                String query = "DELETE FROM course_catalog WHERE course_id = '"+courseID+"';";
                stmt.executeUpdate(query);
                System.out.println("Course Removed");
            }
            else{
                System.out.println("Course Not Found");
            }
        } catch (Exception e) {
            System.out.println("Error at Acad>removeCourse: "+e);
        }
    }

    public void editCourse(Connection conn,Scanner sc){
        try {
            System.out.print("Enter the Course ID: ");
            String courseID = sc.nextLine();
            if(alreadyAddedCourse(conn, courseID)){
                System.out.print("Enter the new Course LTP       : ");
                String courseLTP = sc.nextLine();
                System.out.print("Enter the new Course Credits   : ");
                Integer courseCredits = sc.nextInt();
                sc.nextLine();
                System.out.print("Enter the new Course Type      : ");
                String courseType = sc.nextLine();
                System.out.print("Enter the new Course Department: ");
                String courseDepartment = sc.nextLine();
                Statement stmt = conn.createStatement();
                String query = "UPDATE course_catalog SET  ltp = '"+courseLTP+"', credits = "+courseCredits+", type = '"+courseType+"', department = '"+courseDepartment+"' WHERE course_id = '"+courseID+"';";
                stmt.executeUpdate(query);
                System.out.println("Course Updated");
            }
            else{
                System.out.println("Course Not Found");
            }
        } catch (Exception e) {
            System.out.println("Error at Acad>editCourse: "+e);
        }
    }

    public boolean alreadyAddedCourse(Connection conn, String courseID){
        try {
            Statement stmt = conn.createStatement();
            String query = "SELECT count(*) FROM course_catalog WHERE course_id = '"+courseID+"';";
            ResultSet rs = stmt.executeQuery(query);
            if(rs.next() && rs.getInt(1) > 0){
                return true;
            }
            else{
                return false;
            }
        } catch (Exception e) {
            System.out.println("Error at Acad>alreadyAddedCourse: "+e);
        }
        return false;
    }

    public void viewCourseCatalog(Connection conn){
        try {
            Statement stmt = conn.createStatement();
            String query = "SELECT * FROM course_catalog;";
            ResultSet rs = stmt.executeQuery(query);
            System.out.println("\nCourse Catalog:");
            while(rs.next()){
                String courseID = rs.getString("course_id");
                String courseName = rs.getString("name");
                String courseLTP = rs.getString("ltp");
                String courseCredits = rs.getString("credits");
                String courseType = rs.getString("type");
                String courseDepartment = rs.getString("department");

                System.out.println("Course ID: "+courseID);
                System.out.println("Course Name: "+courseName);
                System.out.println("Course LTP: "+courseLTP);
                System.out.println("Course Credits: "+courseCredits);
                System.out.println("Course Type: "+courseType);
                System.out.println("Course Department: "+courseDepartment);
                System.out.println("-----------------------------\n");
            }
        } catch (Exception e) {
            System.out.println("Error at Acad>viewCourseCatalog: "+e);
        }
    }

    private void viewStudentGrades(Connection conn,Scanner sc){
        try {
            System.out.print("Enter the Student ID: ");
            String studentID = sc.nextLine();
            Statement stmt = conn.createStatement();
            String query = "SELECT count(*) from students where entry_number = '"+studentID+"';";
            ResultSet rs = stmt.executeQuery(query);
            if(rs.next() && rs.getInt(1) == 0){
                System.out.println("Student Not Found");
                return;
            }
            String choice = "0";
            while(true){
                System.out.println("SELECT: 1. View Grades 2. Go Back");
                System.out.print("Enter your choice: ");
                choice = sc.nextLine();
                switch(choice){
                    case "1":
                        viewSpecificSemesterGrades(conn,studentID,sc);
                        break;
                    case "2":
                        return;
                    default:
                        System.out.println("Invalid Choice");
                        System.out.println("Enter 1 or 2");
                }
            }
        } catch (Exception e) {
            System.out.println("Error at Acad>viewStudentGrades: "+e);
        }
    }

    private void viewSpecificSemesterGrades(Connection conn,String studentID, Scanner sc){
        try {
            System.out.print("Enter the Semester [S/W]: ");
            String semester = sc.nextLine();
            System.out.print("Enter the Year [202X]   : ");
            String year = sc.nextLine();

            // fetching Course Details:
            Statement stmt = conn.createStatement();
            String query = String.format("SELECT * FROM courses where entry_number = '%s' and semester = '%s' and year = '%s';", studentID, semester, year);
            ResultSet rsCourses = stmt.executeQuery(query);


            // fetching Student Details:
            Statement stmt2 = conn.createStatement();
            String queryStudent = "SELECT * from students where entry_number = '"+studentID+"';";
            ResultSet rsStudent = stmt2.executeQuery(queryStudent);
            rsStudent.next();

            String studentName = rsStudent.getString("name");
            String studentDepartment = rsStudent.getString("department");
            System.out.println("\nStudent Name      : "+studentName);
            System.out.println("Student ID        : "+studentID);
            System.out.println("Student Department: "+studentDepartment);
            System.out.println("Course of Semester: "+semester+"  Year: "+year+":\n");
            int count = 0;

            Statement stmt3 = conn.createStatement();
            while(rsCourses.next()){
                String courseID = rsCourses.getString("course_id");
                String grade = rsCourses.getString("grade");

                String queryCourseCatalog = String.format("SELECT * FROM course_catalog WHERE course_id = '%s';", courseID);
                ResultSet rsCourseCatalog = stmt3.executeQuery(queryCourseCatalog);
                rsCourseCatalog.next();
                String courseType = rsCourseCatalog.getString("type");
                String courseLTP = rsCourseCatalog.getString("ltp");
                String courseCredits = rsCourseCatalog.getString("credits");
                String courseName = rsCourseCatalog.getString("name");

                System.out.println("Course Name   : "+courseName);
                System.out.println("Course ID     : "+courseID);
                System.out.println("Course Type   : "+courseType);
                System.out.println("Course LTP    : "+courseLTP);
                System.out.println("Course Credits: "+courseCredits);
                System.out.println("Grade         : "+grade);
                System.out.println();
                count++;
            }
            if(count==0){
                System.out.println("No Courses Found");
            }
            else{
                System.out.print("Do you want to Generate Transcript? (Y/N): ");
                String choice = sc.nextLine();
                if(choice.equals("Y") || choice.equals("y")){
                    generateTranscript(conn,studentID,semester,year);
                }
            }
        } catch (Exception e) {
            System.out.println("Error at Acad>viewSpecificSemesterGrades: "+e);
        }
    }

    private void generateTranscript(Connection conn,String studentID, String semester, String year){
        try {
            Constants constants = new Constants();
            Statement stmt = conn.createStatement();
            String queryStudent = "SELECT * from students where entry_number = '"+studentID+"';";
            ResultSet rsStudent = stmt.executeQuery(queryStudent);
            rsStudent.next();
            String studentName = rsStudent.getString("name");
            String studentDepartment = rsStudent.getString("department");
            
            //File handling
            String fileName = String.format("transcript_'%s'_'%s'_'%s'",studentID,semester,year);
            String filePath = constants.transcriptFolderPath +fileName+".txt";
            //creating file if not exists
            File file = new File(filePath);
            if(!file.exists()){
                file.createNewFile();
            }
            //writing to file 
            FileWriter fw = new FileWriter(filePath);
            fw.write("Student Name      : "+studentName  + "\n");
            fw.write("Student ID        : "+studentID  + "\n");
            fw.write("Student Department: "+studentDepartment  + "\n");
            fw.write("          Transcript of Semester "+semester+" and Year "+year+"\n"  + "\n");
            
            Statement stmt2 = conn.createStatement();
            String query = "SELECT * from courses where entry_number = '"+studentID+"' and semester = '"+semester+"' and year = '"+year+"';";
            ResultSet rs = stmt2.executeQuery(query);

            Statement stmt3 = conn.createStatement();
            while(rs.next()){
                String courseID = rs.getString("course_id");
                String grade = rs.getString("grade");

                String queryCourseCatalog = String.format("SELECT * FROM course_catalog WHERE course_id = '%s';", courseID);
                ResultSet rsCourseCatalog = stmt3.executeQuery(queryCourseCatalog);
                rsCourseCatalog.next();
                String courseType = rsCourseCatalog.getString("type");
                String courseLTP = rsCourseCatalog.getString("ltp");
                String courseCredits = rsCourseCatalog.getString("credits");
                String courseName = rsCourseCatalog.getString("name");

                fw.write("Course ID     : "+courseID + "\n");
                fw.write("Course Name   : "+courseName  + "\n");
                fw.write("Course LTP    : "+courseLTP  + "\n");
                fw.write("Course Credits: "+courseCredits  + "\n");
                fw.write("Course Type   : "+courseType  + "\n");
                fw.write("Grade         : "+grade  + "\n");
                fw.write("\n");
            }
            StudentDashboard sd = new StudentDashboard();
            Double sgpa = sd.getSGPA(conn, studentID, year, semester);
            Double cgpa = sd.getCGPA(conn, studentID);

            fw.write("\n");
            fw.write("SGPA: " + sgpa  + "\n");
            fw.write("CGPA: " + cgpa  + "\n");

            fw.write("\n");
            fw.write("Generated on: "+new Date());
            fw.close();
        } catch (Exception e) {
            System.out.println("Error at Acad>generateTranscript: "+e);
        }
    }

    private void checkGraduation(Connection conn, Scanner sc){
        try {
            Constants constants = new Constants();
            Integer YOJ = 0;
            Integer yearsSpent = 0;

            System.out.print("Enter the Student ID: ");
            String studentID = sc.nextLine();
            Statement stmt = conn.createStatement();
            String query = String.format("SELECT * FROM students WHERE entry_number = '%s';", studentID);
            ResultSet rs = stmt.executeQuery(query);
            if(rs.next()==false){
                System.out.println("Student Not Found");
                return;
            }
            else{
                YOJ = Integer.parseInt(rs.getString("year_of_joining"));
                yearsSpent = Integer.parseInt(currentACADYear) - YOJ;
            }

            Statement stmt2 = conn.createStatement();
            String queryCourses = String.format("SELECT * FROM courses WHERE entry_number = '%s' and status = 'completed';", studentID);
            ResultSet rsCourses = stmt2.executeQuery(queryCourses);
            int electiveCount = 0;
            int coreCount = 0;
            int btpcount = 0;
            Statement stmt3 = conn.createStatement();
            while(rsCourses.next()){
                String queryCourseCatalog = String.format("SELECT type,credits FROM course_catalog WHERE course_id = '%s';", rsCourses.getString("course_id"));
                ResultSet rsCourseCatalog = stmt3.executeQuery(queryCourseCatalog);
                rsCourseCatalog.next();
                String courseType = rsCourseCatalog.getString("type");
                if(courseType.equals("Elective")){
                    electiveCount += Integer.parseInt(rsCourseCatalog.getString("credits"));
                }
                else if(courseType.equals("BTP")){
                    btpcount += Integer.parseInt(rsCourseCatalog.getString("credits"));
                }
                else{
                    coreCount += Integer.parseInt(rsCourseCatalog.getString("credits"));
                }
            }
            System.out.println("Elective Credits: "+electiveCount);
            System.out.println("Core Credits    : "+coreCount);
            System.out.println("BTP Credits     : "+btpcount);
            System.out.println("Years Spent     : "+yearsSpent);
            if(electiveCount>=constants.MIN_ELECTIVE_CREDITS && coreCount>= constants.MIN_CORE_CREDITS && btpcount>=constants.MIN_BTP_CREDITS && ((yearsSpent==4 && currentACADSemester == "W") || yearsSpent>4) ){
                System.out.println("Student is eligible for Graduation");
            }
            else{
                System.out.println("Student is not eligible for Graduation");
            }
        } catch (Exception e) {
            System.out.println("Error at Acad>checkGraduation: "+e);
        }
    }
}
