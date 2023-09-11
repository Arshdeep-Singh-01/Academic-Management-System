package software.Faculty;

import software.Dashboard;
import software.Acad.acadVariables;
import software.student.Constants;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import java.io.FileReader;
import java.io.IOException;


public class FacultyDashboard implements Dashboard {
    // Academic Variables
    String year = "";
    String semester = "";
    String semesterStatus = "";
    
    @Override
    public void main(Connection conn, String userID,Scanner sc){
        if(isFaculty(conn,userID)==false){
            System.out.println("You are not a Faculty");
            return;
        }

        acadVariables acadVar = new acadVariables(conn);
        year = acadVar.getCURRENTYEAR();
        semester = acadVar.getCURRENTSEMESTER();
        semesterStatus = acadVar.getSEMESTERSTATUS();
        

        Constants constants = new Constants();
        while(true){
            try {
                System.out.println("\n-----------Welcome to Faculty Dashboard-----------");
                System.out.println("\nSelect: 1-View Profile, 2-View Your Courses, 3-Register/ Deregister Courses, 4-View Enrollment in Course, 5-Update Course Grades, 6- Exit");
                System.out.print("Enter your choice: ");

                String choice = sc.nextLine();
                switch(choice){
                    case "1":
                        // View Profile
                        viewfacultyProfile(conn,userID);
                        break;
                    case "2":
                        // View Your Courses
                        viewCourses(conn, userID,sc);
                        break;
                    case "3":
                        // Register/ Deregister Courses
                        actionOnCourses(conn, userID,sc);
                        break;
                    case "4":
                        // view Enrollment in Course
                        viewEnrollmentInCourse(conn, userID,sc);
                        break;
                    case "5":
                        // update course grades
                        updateCourseGrades(conn,userID,sc);
                        break;
                    case "6":
                        // Exit
                        System.out.println("Exiting Faculty Dashboard");
                        break;
                    default:
                        System.out.println("Invalid Choice");
                        break;
                }

                if(choice.equals(constants.EXIT_CHOICE)){
                    break;
                }
            } catch (Exception e) {
                System.out.println("Error at FacultyDashboard: "+e);
            }
        }
        
    }

    private boolean isFaculty(Connection conn,String userID) {
        try {
            Statement statement = conn.createStatement();
            String query = String.format("SELECT * FROM faculty WHERE faculty_id = '%s';",userID);
            ResultSet resultSet = statement.executeQuery(query);
            if(resultSet.next()){
                return true;
            }
            return false;
        } catch (Exception e) {
            System.out.println("Error at notFaculty: "+e);
        }
        return false;
    }

    private ArrayList<String> getOfferedCourseID(Connection conn, String userID, String requiredSemester, String requiredYear){
        ArrayList<String> courseIDList = new ArrayList<String>();
        try {
            Statement statement = conn.createStatement();
            statement = conn.createStatement();
            String query = String.format("SELECT * FROM course_offerings WHERE year = '%s' and semester = '%s';",requiredYear,requiredSemester);
            ResultSet resultSet = statement.executeQuery(query);
            while(resultSet.next()){
                Array instructors = resultSet.getArray("instructor");
                if(instructors.toString().contains(userID)){
                    String courseID = resultSet.getString("course_id");
                    courseIDList.add(courseID);
                }
            }
        } catch (Exception e) {
            System.out.println("Error at showOfferedCoursesAndGetCourse"+e);
        }
        return courseIDList;
    }

    private void updateCourseGrades(Connection conn, String userID, Scanner sc) {
        try {
            if(semesterStatus.equals("grade_submission")==false){
                System.out.println("Grades cannot be updated at this time.");
                return;
            }
            else{
                Constants constants = new Constants();
                String requiredYear = year;
                String requiredSemester = semester;
                ArrayList<String> courseIDList = getOfferedCourseID(conn, userID, requiredSemester, requiredYear);
                
                System.out.print("Courses Offered by You: ");
                for(int i=0; i<courseIDList.size(); i++){
                    System.out.print(courseIDList.get(i) + " , ");
                }

                if(courseIDList.size()==0){
                    System.out.println("You are not offering any course this semester");
                    return;
                }

                System.out.print("\nEnter Course ID: ");
                String courseID = sc.nextLine();

                // check if courseID is offered by the faculty
                if(courseIDList.contains(courseID)==false){
                    System.out.println("Course ID not offered by you");
                    return;
                }
                CSVReader csvReader = null;
                csvReader = new CSVReader(new FileReader(constants.gradeFilePath));
                String[] line;
                while ((line = csvReader.readNext()) != null) {
                    String entryNumber = line[0];
                    String courseIDFile = line[1];
                    String grade = line[2];
                    Statement statement = conn.createStatement();
                    if(courseID.equals(courseIDFile)){
                        String query = String.format("UPDATE courses SET grade = '%s',status = 'completed' WHERE entry_number = '%s' and course_id = '%s' and semester = '%s' and year = '%s';",grade,entryNumber,courseID,requiredSemester,requiredYear);
                        statement.executeUpdate(query);
                        System.out.println("Grade Updated for "+entryNumber+" in "+courseID);
                    }
                }     
            }   
        } catch (Exception e) {
            System.out.println("Error at Faculty > updateCourseGrades: "+e);
        }
    }

    private void viewEnrollmentInCourse(Connection conn, String userID,Scanner sc) {
        try {
            System.out.print("Enter year   [202X] : ");
            String requiredYear = sc.nextLine();
            System.out.print("Enter semester [S/W]: ");
            String requiredSemester = sc.nextLine();
            ArrayList<String> courseIDList = getOfferedCourseID(conn, userID, requiredSemester, requiredYear);
            if(courseIDList.size() == 0){
                System.out.println("No courses offered by you in this semester");
                return;
            }
            else{
                System.out.print("Courses Offered by You: ");
                for(int i=0; i<courseIDList.size(); i++){
                    System.out.print(courseIDList.get(i) + " , ");
                }
                System.out.print("\nEnter Course ID: ");
                String courseID = sc.nextLine();
                Statement statement = conn.createStatement();
                String query = String.format("SELECT * FROM courses WHERE course_id = '%s' and year = '%s' and semester = '%s';",courseID,requiredYear,requiredSemester);
                ResultSet resultSet = statement.executeQuery(query);
                System.out.println("\n-----------ENROLLMENT IN COURSE-----------");
                while(resultSet.next()){
                    System.out.println("Entry Number: "+resultSet.getString("entry_number"));
                    System.out.println("Status: "+resultSet.getString("status"));
                    System.out.println("Grade: "+resultSet.getString("grade"));
                    System.out.println(" ");
                }
                System.out.println("------------------------------------------\n");
            }
        } catch (Exception e) {
            System.out.println("Error at Faculty > viewEnrollmentInCourse: "+e);
        }
    }

    private void viewfacultyProfile(Connection conn, String userID){
        
        Statement statement;
        try {
            statement = (Statement) conn.createStatement();
            String query = String.format("SELECT * FROM faculty WHERE faculty_id = '%s';",userID );
            ResultSet resultSet = statement.executeQuery(query);
            System.out.println("\n---------Faculty PROFILE---------");
            if(resultSet.next()){
                System.out.println("Name       : " + resultSet.getString("name"));
                System.out.println("Contact    : " + resultSet.getString("contact"));
                System.out.println("Department : " + resultSet.getString("department"));
                System.out.println("Designation: " + resultSet.getString("designation"));
                System.out.println("---------------------------------\n");
            }
        } catch (Exception e) {
            System.out.println("Error at Faculty Profile"+e);
        }

    }

    private void viewCoursesOfYearSem(Connection conn, String userID, String requiredYear, String requiredSemester){
        Statement statement;
        try {
            statement = conn.createStatement();
            String query = String.format("SELECT * FROM course_offerings WHERE year = '%s' and semester = '%s';",requiredYear,requiredSemester );
            ResultSet resultSet = statement.executeQuery(query);
            System.out.println("\n---------COURSES OFFERED---------");
            System.out.println("Courses offered by you in "+ requiredYear+" "+requiredSemester+" are:");
            int count = 0;
            while(resultSet.next()){
                Array instructors = resultSet.getArray("instructor");
                if(instructors.toString().contains(userID)){
                    count++;
                    System.out.println(count + ": ");
                    System.out.println("Course ID  : " + resultSet.getString("course_id"));
                    System.out.println("Course Name: " + resultSet.getString("course_name"));
                    System.out.println("---------------------------------\n");
                }
            }
            if(count == 0){
                System.out.println("No Courses Offered by you in "+requiredYear+" - "+requiredSemester);
            }
        } catch (Exception e) {
            System.out.println("Error at Faculty View Courses of Year Sem"+e);
        }
    }

    private void viewCourses(Connection conn, String userID,Scanner sc ){
        try {
            while(true){
                System.out.println("\nSelect: 1-View Courses of Current Year and Semester, 2-View Courses of Other Year and Semester, 3- Exit");
                System.out.print("Enter your choice: ");
                String choice = sc.nextLine();
                switch(choice){
                    case "1":
                        // View Courses of Current Year and Semester
                        viewCoursesOfYearSem(conn, userID, year, semester);
                        break;
                    case "2":
                        // View Courses of Other Year and Semester
                        System.out.print("Enter Year   [202X] : ");
                        String requiredYear = sc.nextLine();
                        System.out.print("Enter Semester [S/W]: ");
                        String requiredSemester = sc.nextLine();
                        viewCoursesOfYearSem(conn, userID, requiredYear, requiredSemester);
                        break;
                    case "3":
                        // Exit
                        System.out.println("Going Back to Main Menu (Faculty Dashboard)");
                        break;
                    default:
                        System.out.println("Invalid Choice");
                        break;
                }
                if(choice.equals("3")){
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Error at Faculty View Courses"+e);
        }
    }

    private void actionOnCourses(Connection conn, String userID,Scanner sc){
        if(semesterStatus.equals("float")){
            while(true){
                System.out.println("\nSelect: 1-Register Courses, 2-Deregister Courses,3-Update Course Constraints, 4- Exit");
                System.out.print("Enter your choice: ");
                String choice = sc.nextLine();
                switch(choice){
                    case "1":
                        // Register Courses
                        registerCourses(conn, userID,sc);
                        break;
                    case "2":
                        // Deregister Courses
                        deregisterCourses(conn, userID,sc);
                        break;
                    case "3":
                        // Update Course Constraints
                        updateCourseConstraints(conn, userID,sc);
                        break;

                    case "4":
                        // Exit
                        System.out.println("Going back to Main Menu (Faculty Dashboard)");
                        break;
                    default:
                        System.out.println("Invalid Choice: enter 1, 2, 3 or 4");
                        break;
                }
                if(choice.equals("4")){
                    break;
                }
            }
        }
        else{
            System.out.println("Course Floating/updation is not allowed at this time");
        }
    }

    private void updateCourseConstraints(Connection conn, String userID,Scanner sc){
        try {
            String requiredYear = year;
            String requiredSemester = semester;
            ArrayList<String> courseIDList = getOfferedCourseID(conn, userID, requiredSemester, requiredYear);
            if(courseIDList.size() == 0){
                System.out.println("No courses offered by you in this semester");
                return;
            }
            else{
                System.out.print("Courses Offered by You: ");
                for(int i=0; i<courseIDList.size(); i++){
                    System.out.print(courseIDList.get(i) + " , ");
                }
                System.out.print("\nEnter Course ID: ");
                String courseID = sc.nextLine();
                if(courseIDList.contains(courseID)){
                    setConstraints(conn, userID, courseID,sc);
                }
                else{
                    System.out.println("No such course offered by you in this semester");
                    return;
                }
            }
        } catch (Exception e) {
            System.out.println("Error at Faculty Update Course Constraints"+e);
        }
    }

    private void registerCourses(Connection conn, String userID,Scanner sc){
        try {
            while(true){
                System.out.println("\nSelect: 1-View Course Catalog, 2-Register a Course, 3- Exit");
                System.out.print("Enter your choice: ");
                String choice = sc.nextLine();
                switch(choice){
                    case "1":
                        // view course catalog:
                        courseCatalog(conn, userID);
                        break;
                    case "2":
                        // Register Courses
                        System.out.print("Enter Course ID: ");
                        String courseID = sc.nextLine();
                        register(conn, userID, courseID,sc);
                        break;
                    case "3":
                        // Exit
                        System.out.println("Going Back to Course Action Menu");
                        break;
                    default:
                        System.out.println("Invalid Choice");
                        break;
                }
                if(choice.equals("3")){
                    break;
                }
            }
        } catch (Exception e){
            System.out.println("Error at Faculty Register Courses"+e);
        }
    }

    private void courseCatalog(Connection conn, String userID){
        try {
            Statement statement = conn.createStatement();
            String department = getDepartment(conn, userID);
            String query = String.format("SELECT * FROM course_catalog where department = '%s' OR department = 'GEN';",department);
            ResultSet resultSet = statement.executeQuery(query);
            System.out.println("\n---------COURSE CATALOG---------");
            System.out.println("Courses offered by the university are:");
            int count = 0;
            while(resultSet.next()){
                count++;
                System.out.println(count + ": ");
                System.out.println("Course ID  : " + resultSet.getString("course_id"));
                System.out.println("Course Name: " + resultSet.getString("name"));
                System.out.println("Credits    : " + resultSet.getString("credits"));
                System.out.println("Department : " + resultSet.getString("department"));
                System.out.println("LTP        : " + resultSet.getString("ltp"));
                System.out.println("Credits    : " + resultSet.getString("credits"));
                System.out.println("---------------------------------\n");
            }
            if(count == 0){
                System.out.println("No Courses Offered by the university");
            }
        } catch (Exception e) {
            System.out.println("Error at Faculty View Course Catalog"+e);
        }
    }

    private String getDepartment(Connection conn, String userID){
        try {
            Statement statement = conn.createStatement();
            String query = String.format("SELECT department FROM faculty WHERE faculty_id = '%s';",userID);
            ResultSet resultSet = statement.executeQuery(query);
            resultSet.next();
            return resultSet.getString("department");
        } catch (Exception e) {
            System.out.println("Error at Faculty Get Department"+e);
        }
        return null;
    }

    private void register(Connection conn, String userID, String courseID,Scanner sc ){
        try {
            Statement statement = conn.createStatement();
            String query = String.format("SELECT * FROM course_catalog WHERE course_id = '%s';",courseID);
            ResultSet resultSet = statement.executeQuery(query);
            if(resultSet.next()){
                // Course Found in Course Catalog
                if(elegibleForCourse(conn, userID, courseID)){
                    // Faculty is elegible to register for this course
                    if(courseAlreadyOffered(conn, courseID)){
                        // Course is already offered by some other faculty
                        if(alreadyRegistered(conn, userID, courseID)){
                            // Faculty is already registered for this course
                            System.out.println("You are already registered for this course");
                            setConstraints(conn, userID, courseID,sc);
                        }
                        else{
                            // Faculty is not registered for this course, so append his name to the instructor array
                            registerForCourse(conn, userID, courseID);
                            setConstraints(conn, userID, courseID,sc);
                        }
                    }
                    else{
                        // Course is not offered by any faculty, so add it to the course_offerings table
                        offerAndRegister(conn, userID, courseID);
                        setConstraints(conn, userID, courseID,sc);
                    }
                }
                else{
                    System.out.println("You are not elegible to register this course");
                }
            }
            else{
                System.out.println("Course not found");
            }
        } catch (Exception e) {
            System.out.println("Error at Faculty Register"+e);
        }
    }

    private boolean elegibleForCourse(Connection conn, String userID, String courseID){
        try {
            Statement statement = conn.createStatement();
            String query = String.format("SELECT * FROM course_catalog WHERE course_id = '%s';",courseID);
            ResultSet resultSet = statement.executeQuery(query);
            resultSet.next();
            String department = resultSet.getString("department");
            String query2 = String.format("SELECT * FROM faculty WHERE faculty_id = '%s';",userID);
            ResultSet resultSet2 = statement.executeQuery(query2);
            resultSet2.next();
            String facultyDepartment = resultSet2.getString("department");
            if(facultyDepartment.equals(department) || department.equals("GEN")){
                return true;
            }
            else{
                return false;
            }
        } catch (Exception e) {
            System.out.println("Error at Faculty Elegible for Course"+e);
        }
        return false;
    }

    private boolean courseAlreadyOffered(Connection conn, String courseID){
        try {
            Statement statement = conn.createStatement();
            String query = String.format("SELECT * FROM course_offerings where course_id = '%s' and year = '%s' and semester = '%s';",courseID, year, semester);
            ResultSet resultSet = statement.executeQuery(query);
            if(resultSet.next()){
                return true;
            }
            else{
                return false;
            }
        } catch (Exception e) {
            System.out.println("Error at Faculty: Course Already Offered : "+e);
        }
        return false;
    }

    private boolean alreadyRegistered(Connection conn, String userID, String courseID){
        try {
            Statement statement = conn.createStatement();
            String query = String.format("SELECT * FROM course_offerings where course_id = '%s' and year = '%s' and semester = '%s';",courseID, year, semester);
            ResultSet resultSet = statement.executeQuery(query);
            if(resultSet.next()){
                Array instructor = resultSet.getArray("instructor");
                if(instructor.toString().contains(userID)){
                    return true;
                }
                else{
                    return false;
                }
            }
            else{
                return false;
            }
        } catch (Exception e) {
            System.out.println("Error at Faculty: Already Registered: "+e);
        }
        return false;
    }

    private void registerForCourse(Connection conn, String userID, String courseID){
        try {
            Statement statement = conn.createStatement();
            String query = String.format("UPDATE course_offerings SET instructor = array_append(instructor, '%s') WHERE course_id = '%s' and year = '%s' and semester = '%s';",userID, courseID, year, semester);
            int count = statement.executeUpdate(query);
            System.out.println("You are now registered for this course");
        } catch (Exception e) {
            System.out.println("Error at Faculty Register For Course: "+e);
        }
    }

    private void offerAndRegister(Connection conn, String userID, String courseID){
        try {
            Statement statement = conn.createStatement();
            String courseName = getCourseName(conn, courseID);
            String query = String.format("INSERT INTO course_offerings (course_id, course_name, instructor, year, semester) VALUES ('%s', '%s', '{%s}', '%s', '%s');",courseID,courseName, userID, year, semester);
            statement.executeUpdate(query);
            System.out.println("You are now registered for this course");
        } catch (Exception e) {
            System.out.println("Error at Faculty Offer and Register"+e);
        }
    }

    private void viewCurrentConstraints(Connection conn, String courseID){
        try {
            Statement statement = conn.createStatement();
            System.out.println("Elegible Students (Branch, Year, CGPA)-------------------");
            String query = String.format("SELECT * FROM constraints where course_id = '%s' and year_offered = '%s' and semester_offered = '%s';",courseID, year, semester);
            ResultSet resultSet = statement.executeQuery(query);
            int count = 0;
            while(resultSet.next()){
                count++;
                String branchElegible = resultSet.getString("branch");
                Integer yearElegible = resultSet.getInt("year");
                Double cgpaElegible = resultSet.getDouble("cgpa");

                System.out.println(" Branch: "+branchElegible+" Year: "+yearElegible+" CGPA: "+cgpaElegible);
            }
            if(count == 0){
                System.out.println("No Elegiblility Constraints (No students are elegible for this course)");
            }
        } catch (Exception e) {
            System.out.println("Error at Faculty>View Current Constraints: "+e);
        }
    }

    private void setNewConstraints(Connection conn, String courseID,Scanner sc ){
        try {
            Statement statement = conn.createStatement();
            System.out.print("Enter Branch (CSE, ELE, MEH, ALL): ");
            String constraintBranch = sc.nextLine();
            System.out.print("Enter Year [1/2/3/4]: ");
            String constraintYear = sc.nextLine();
            System.out.print("Enter CGPA [0.0 - 10.0]: ");
            String constraintCGPA = sc.nextLine();
            String query = String.format("INSERT INTO constraints (course_id, branch, year, cgpa, year_offered, semester_offered) VALUES ('%s', '%s', '%s', '%s', '%s', '%s');",courseID, constraintBranch, constraintYear, constraintCGPA, year, semester);
            statement.executeUpdate(query);
            System.out.println("New Constraints Set");
        } catch (Exception e) {
            System.out.println("Error at Faculty>Set New Constraints: "+e);
        }
    }

    private void updateCGPAofExistingConstraint(Connection conn, String courseID,Scanner sc ){
        try {
            Statement statement = conn.createStatement();
            System.out.print("Enter Branch (CSE, ELE, MEH, ALL): ");
            String constraintBranch = sc.nextLine();
            System.out.print("Enter Year [1/2/3/4]: ");
            String constraintYear = sc.nextLine();
            System.out.print("Enter new CGPA [0.0 - 10.0]: ");
            String constraintCGPA = sc.nextLine();
            String query = String.format("UPDATE constraints SET cgpa = '%s' WHERE course_id = '%s' and branch = '%s' and year = '%s' and year_offered = '%s' and semester_offered = '%s';",constraintCGPA, courseID, constraintBranch, constraintYear, year, semester);
            statement.executeUpdate(query);
            System.out.println("CGPA of Existing Constraint Updated");
        } catch (Exception e) {
            System.out.println("Error at Faculty>Update CGPA of Existing Constraint: "+e);
        }
    }

    private void removeConstraint(Connection conn, String courseID,Scanner sc ){
        try {
            Statement statement = conn.createStatement();
            System.out.print("Enter Branch (CSE, ELE, MEH, ALL): ");
            String constraintBranch = sc.nextLine();
            System.out.print("Enter Year [1/2/3/4]: ");
            String constraintYear = sc.nextLine();
            String query = String.format("DELETE FROM constraints WHERE course_id = '%s' and branch = '%s' and year = '%s' and year_offered = '%s' and semester_offered = '%s';",courseID, constraintBranch, constraintYear, year, semester);
            statement.executeUpdate(query);
            System.out.println("Constraint Removed");
        } catch (Exception e) {
            System.out.println("Error at Faculty>Remove Constraint: "+e);
        }
    }

    private void setConstraints(Connection conn, String userID, String courseID,Scanner sc ){
        try {
            while(true){
                System.out.println("SELECT 1: To view Current Constraints, 2: To set new Constraints, 3: To update CGPA criteria of Existing Constraint, 4: To remove a Constraint, 5: To exit");
                String choice = "0";
                System.out.print("Enter Choice: ");
                choice = sc.nextLine();
                switch(choice){
                    case "1":
                        viewCurrentConstraints(conn, courseID);
                        break;
                    case "2":
                        setNewConstraints(conn, courseID,sc);
                        break;
                    case "3":
                        updateCGPAofExistingConstraint(conn, courseID,sc);
                        break;
                    case "4":
                        removeConstraint(conn, courseID,sc);
                        break;
                    case "5":
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
            System.out.println("Error at Faculty Set Constraints: "+e);
        }
    }

    public String getCourseName(Connection conn, String courseID){
        try {
            Statement statement = conn.createStatement();
            String query = String.format("SELECT * FROM course_catalog WHERE course_id = '%s';",courseID);
            ResultSet resultSet = statement.executeQuery(query);
            resultSet.next();
            return resultSet.getString("name");
        } catch (Exception e) {
            System.out.println("Error at Faculty Get Course Name: "+e);
        }
        return null;
    }

    public void deregisterCourses(Connection conn, String userID,Scanner sc ){
        try {
            String courseID;
            System.out.print("Enter the course ID of the course you want to deregister from: ");
            courseID = sc.nextLine();
            if(alreadyRegistered(conn, userID, courseID)){
                deregister(conn,userID,courseID);
            }
            else{
                System.out.println("You are not registered for this course/Invalid Course ID");
            }
        } catch (Exception e) {
            System.out.println("Error at Faculty Deregister Courses"+e);
        }
    }

    public void deregister(Connection conn, String userID, String courseID){
        try {
            Statement statement = conn.createStatement();
            String query = String.format("UPDATE course_offerings SET instructor = array_remove(instructor, '%s') WHERE course_id = '%s' and year = '%s' and semester = '%s';",userID, courseID, year, semester);
            statement.executeUpdate(query);
            System.out.println("You are now deregistered for this course");
            if(noInstructor(conn, courseID)){
                deleteCourseOffering(conn, courseID);
            }
        } catch (Exception e) {
            System.out.println("Error at Faculty Deregister"+e);
        }
    }

    public boolean noInstructor(Connection conn, String courseID){
        try {
            Statement statement = conn.createStatement();
            String query = String.format("SELECT * FROM course_offerings WHERE course_id = '%s' and year = '%s' and semester = '%s';",courseID, year, semester);
            ResultSet resultSet = statement.executeQuery(query);
            resultSet.next();
            String[] instructors = (String[]) resultSet.getArray("instructor").getArray();
            if(instructors.length == 0){
                return true;
            }
            else{
                return false;
            }
        } catch (Exception e) {
            System.out.println("Error at Faculty: No Instructor: "+e);
        }
        return false;
    }
    
    public void deleteCourseOffering(Connection conn, String courseID){
        try {
            Statement statement = conn.createStatement();
            String query = String.format("DELETE FROM course_offerings WHERE course_id = '%s' and year = '%s' and semester = '%s';",courseID, year, semester);
            statement.executeUpdate(query);
            System.out.println("Course Offering Deleted");
        } catch (Exception e) {
            System.out.println("Error at Faculty Delete Course Offering"+e);
        }
    }
}
