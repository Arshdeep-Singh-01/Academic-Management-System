package software;

import org.junit.FixMethodOrder;
import org.junit.Test;
import static org.junit.Assert.*;


import software.Acad.acadVariables;
import software.Faculty.FacultyDashboard;
import software.student.Constants;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

@FixMethodOrder(org.junit.runners.MethodSorters.NAME_ASCENDING)
public class FacultyDashboardTest {
    Constants constant = new Constants();
    
    @Test
    public void test1FacultyProfile_notFaculty() throws IOException {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/student", "postgres", "1234");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        FacultyDashboard faculty = new FacultyDashboard();
        
        String input = "";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        Scanner sc = new Scanner(System.in);
        faculty.main(conn, "2020csb1074",sc);
        sc.close();

        String output = outContent.toString();
        String expectedOutput = "You are not a Faculty";
        // assertEquals(output, expectedOutput);

        assertTrue(output.contains(expectedOutput));

        inputStream.close();
        outContent.close();
    }   

    @Test
    public void test1FacultyProfileSuccess() throws IOException {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/student", "postgres", "1234");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        FacultyDashboard faculty = new FacultyDashboard();
        
        String input = "1\n6\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        Scanner sc = new Scanner(System.in);
        faculty.main(conn, "fac1",sc);
        sc.close();

        String output = outContent.toString();
        String expectedOutput = "Faculty PROFILE";
        // assertEquals(output, expectedOutput);

        assertTrue(output.contains(expectedOutput));

        inputStream.close();
        outContent.close();
    }    

    @Test
    public void test1Faculty_invalidChoice() throws IOException {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/student", "postgres", "1234");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        FacultyDashboard faculty = new FacultyDashboard();
        
        String input = "9\n6\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        Scanner sc = new Scanner(System.in);
        faculty.main(conn, "fac1",sc);
        sc.close();

        String output = outContent.toString();
        String expectedOutput = "Invalid Choice";
        // assertEquals(output, expectedOutput);

        assertTrue(output.contains(expectedOutput));

        inputStream.close();
        outContent.close();
    }    

    @Test
    public void test2FacultyViewCoursesCurrentSem() throws IOException {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/student", "postgres", "1234");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        FacultyDashboard faculty = new FacultyDashboard();
        
        String input = "2\n1\n3\n6\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        Scanner sc = new Scanner(System.in);
        faculty.main(conn, "fac1",sc);
        sc.close();

        String output = outContent.toString();
        String expectedOutput = "COURSES OFFERED";
        // assertEquals(output, expectedOutput);

        assertTrue(output.contains(expectedOutput));

        inputStream.close();
        outContent.close();
    }    

    @Test
    public void test2FacultyViewCoursesOtherSem() throws IOException {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/student", "postgres", "1234");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        FacultyDashboard faculty = new FacultyDashboard();
        
        String input = "2\n2\n2023\nS\n3\n6\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Scanner sc = new Scanner(System.in);
        faculty.main(conn, "fac1",sc);

        String output = outContent.toString();
        String expectedOutput = "COURSES OFFERED";
        // assertEquals(output, expectedOutput);

        assertTrue(output.contains(expectedOutput));

        inputStream.close();
        outContent.close();
        sc.close();
    }    

    @Test
    public void test3FacultyActionOnCourses_exit() throws IOException{
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/student", "postgres", "1234");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        FacultyDashboard faculty = new FacultyDashboard();
        
        acadVariables acadVar = new acadVariables(conn);
        String currentAcademicStatus = acadVar.getSEMESTERSTATUS();
        
        Statement stmt = null;
        try {
            String query = "UPDATE academic_variables SET semester_status = 'float' WHERE user_id = 'dean';";
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
        }catch (SQLException e) {
            e.printStackTrace();
        }
        
        String input = "3\n4\n6\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        Scanner sc = new Scanner(System.in);
        faculty.main(conn, "fac1",sc);

        String output = outContent.toString();
        String expectedOutput = "back to Main Menu";
        // assertEquals(output, expectedOutput);

        try {
            String query = String.format("UPDATE academic_variables SET semester_status = '%s' WHERE user_id = 'dean';", currentAcademicStatus);
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
        }catch (SQLException e) {
            e.printStackTrace();
        }
        assertTrue(output.contains(expectedOutput));

        inputStream.close();
        outContent.close();
        sc.close();
    }

    @Test
    public void test3FacultyActionOnCourses_invalid() throws IOException{
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/student", "postgres", "1234");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        FacultyDashboard faculty = new FacultyDashboard();
        
        acadVariables acadVar = new acadVariables(conn);
        String currentAcademicStatus = acadVar.getSEMESTERSTATUS();
        
        Statement stmt = null;
        try {
            String query = "UPDATE academic_variables SET semester_status = 'float' WHERE user_id = 'dean';";
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
        }catch (SQLException e) {
            e.printStackTrace();
        }
        
        String input = "3\n9\n4\n6\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        Scanner sc = new Scanner(System.in);
        faculty.main(conn, "fac1",sc);

        String output = outContent.toString();
        String expectedOutput = "Invalid Choice";
        // assertEquals(output, expectedOutput);

        try {
            String query = String.format("UPDATE academic_variables SET semester_status = '%s' WHERE user_id = 'dean';", currentAcademicStatus);
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
        }catch (SQLException e) {
            e.printStackTrace();
        }
        assertTrue(output.contains(expectedOutput));

        inputStream.close();
        outContent.close();
        sc.close();
    }

    @Test
    public void test3FacultyActionOnCourses_updateCourseConstraints() throws IOException{
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/student", "postgres", "1234");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        FacultyDashboard faculty = new FacultyDashboard();
        
        acadVariables acadVar = new acadVariables(conn);
        String currentAcademicStatus = acadVar.getSEMESTERSTATUS();
        
        Statement stmt = null;
        try {
            String query = "UPDATE academic_variables SET semester_status = 'float' WHERE user_id = 'dean';";
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
        }catch (SQLException e) {
            e.printStackTrace();
        }
        
        String input = "3\n3\ncs305\n1\n5\n4\n6\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        Scanner sc = new Scanner(System.in);
        faculty.main(conn, "fac1",sc);

        String output = outContent.toString();
        String expectedOutput = "Elegible Students";
        // assertEquals(output, expectedOutput);

        try {
            String query = String.format("UPDATE academic_variables SET semester_status = '%s' WHERE user_id = 'dean';", currentAcademicStatus);
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
        }catch (SQLException e) {
            e.printStackTrace();
        }
        assertTrue(output.contains(expectedOutput));

        inputStream.close();
        outContent.close();
        sc.close();
    }

    @Test
    public void test3_0FacultyActionOnCourses_updateCourseConstraints_notoffered() throws IOException{
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/student", "postgres", "1234");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        FacultyDashboard faculty = new FacultyDashboard();
        
        acadVariables acadVar = new acadVariables(conn);
        String currentAcademicStatus = acadVar.getSEMESTERSTATUS();
        
        Statement stmt = null;
        try {
            String query = "UPDATE academic_variables SET semester_status = 'float' WHERE user_id = 'dean';";
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
        }catch (SQLException e) {
            e.printStackTrace();
        }
        
        String input = "3\n3\ncs555\n4\n6\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        Scanner sc = new Scanner(System.in);
        faculty.main(conn, "fac1",sc);

        String output = outContent.toString();
        String expectedOutput = "No such course offered";
        // assertEquals(output, expectedOutput);

        try {
            String query = String.format("UPDATE academic_variables SET semester_status = '%s' WHERE user_id = 'dean';", currentAcademicStatus);
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
        }catch (SQLException e) {
            e.printStackTrace();
        }
        assertTrue(output.contains(expectedOutput));

        inputStream.close();
        outContent.close();
        sc.close();
    }

    @Test
    public void test3_0FacultyActionOnCourses_updateCourseConstraints_notCourse() throws IOException{
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/student", "postgres", "1234");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        FacultyDashboard faculty = new FacultyDashboard();
        
        acadVariables acadVar = new acadVariables(conn);
        String currentAcademicStatus = acadVar.getSEMESTERSTATUS();
        
        Statement stmt = null;
        try {
            String query = "UPDATE academic_variables SET semester_status = 'float' WHERE user_id = 'dean';";
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
        }catch (SQLException e) {
            e.printStackTrace();
        }
        
        String input = "3\n3\n4\n6\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        Scanner sc = new Scanner(System.in);
        faculty.main(conn, "fac10",sc);

        String output = outContent.toString();
        String expectedOutput = "No courses offered";
        // assertEquals(output, expectedOutput);

        try {
            String query = String.format("UPDATE academic_variables SET semester_status = '%s' WHERE user_id = 'dean';", currentAcademicStatus);
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
        }catch (SQLException e) {
            e.printStackTrace();
        }
        assertTrue(output.contains(expectedOutput));

        inputStream.close();
        outContent.close();
        sc.close();
    }

    @Test
    public void test3_1FacultyActionOnCourses_updateCourseConstraints_newConstraint() throws IOException{
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/student", "postgres", "1234");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        FacultyDashboard faculty = new FacultyDashboard();
        
        acadVariables acadVar = new acadVariables(conn);
        String currentAcademicStatus = acadVar.getSEMESTERSTATUS();
        
        Statement stmt = null;
        try {
            String query = "UPDATE academic_variables SET semester_status = 'float' WHERE user_id = 'dean';";
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
        }catch (SQLException e) {
            e.printStackTrace();
        }
        
        String input = "3\n3\ncs305\n2\nELE\n3\n0\n5\n4\n6\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        Scanner sc = new Scanner(System.in);
        faculty.main(conn, "fac1",sc);

        String output = outContent.toString();
        String expectedOutput = "New Constraints Set";
        try {
            String query = String.format("UPDATE academic_variables SET semester_status = '%s' WHERE user_id = 'dean';", currentAcademicStatus);
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
        }catch (SQLException e) {
            e.printStackTrace();
        }
        assertTrue(output.contains(expectedOutput));

        inputStream.close();
        outContent.close();
        sc.close();
    }

    @Test
    public void test3_2FacultyActionOnCourses_updateCourseConstraints_notCourse() throws IOException{
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/student", "postgres", "1234");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        FacultyDashboard faculty = new FacultyDashboard();
        
        acadVariables acadVar = new acadVariables(conn);
        String currentAcademicStatus = acadVar.getSEMESTERSTATUS();
        
        Statement stmt = null;
        try {
            String query = "UPDATE academic_variables SET semester_status = 'float' WHERE user_id = 'dean';";
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
        }catch (SQLException e) {
            e.printStackTrace();
        }
        
        String input = "3\n3\ncs305\n3\nELE\n3\n0\n5\n4\n6\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        Scanner sc = new Scanner(System.in);
        faculty.main(conn, "fac1",sc);

        String output = outContent.toString();
        String expectedOutput = "Constraint Updated";
        try {
            String query = String.format("UPDATE academic_variables SET semester_status = '%s' WHERE user_id = 'dean';", currentAcademicStatus);
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
        }catch (SQLException e) {
            e.printStackTrace();
        }
        assertTrue(output.contains(expectedOutput));

        inputStream.close();
        outContent.close();
        sc.close();
    }

    @Test
    public void test3_3FacultyActionOnCourses_updateCourseConstraints_notCourse() throws IOException{
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/student", "postgres", "1234");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        FacultyDashboard faculty = new FacultyDashboard();
        
        acadVariables acadVar = new acadVariables(conn);
        String currentAcademicStatus = acadVar.getSEMESTERSTATUS();
        
        Statement stmt = null;
        try {
            String query = "UPDATE academic_variables SET semester_status = 'float' WHERE user_id = 'dean';";
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
        }catch (SQLException e) {
            e.printStackTrace();
        }
        
        String input = "3\n3\ncs305\n4\nELE\n3\n5\n4\n6\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        Scanner sc = new Scanner(System.in);
        faculty.main(conn, "fac1",sc);

        String output = outContent.toString();
        String expectedOutput = "Constraint Removed";
        try {
            String query = String.format("UPDATE academic_variables SET semester_status = '%s' WHERE user_id = 'dean';", currentAcademicStatus);
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
        }catch (SQLException e) {
            e.printStackTrace();
        }
        assertTrue(output.contains(expectedOutput));

        inputStream.close();
        outContent.close();
        sc.close();
    }

    @Test
    public void test3FacultyActionOnCourses_outOfSession() throws IOException{
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/student", "postgres", "1234");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        FacultyDashboard faculty = new FacultyDashboard();
        
        acadVariables acadVar = new acadVariables(conn);
        String currentAcademicStatus = acadVar.getSEMESTERSTATUS();
        
        Statement stmt = null;
        try {
            String query = "UPDATE academic_variables SET semester_status = 'end' WHERE user_id = 'dean';";
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
        }catch (SQLException e) {
            e.printStackTrace();
        }
        
        String input = "3\n6\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        Scanner sc = new Scanner(System.in);
        faculty.main(conn, "fac1",sc);

        String output = outContent.toString();
        String expectedOutput = "not allowed at this time";
        // assertEquals(output, expectedOutput);

        try {
            String query = String.format("UPDATE academic_variables SET semester_status = '%s' WHERE user_id = 'dean';", currentAcademicStatus);
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
        }catch (SQLException e) {
            e.printStackTrace();
        }
        assertTrue(output.contains(expectedOutput));

        inputStream.close();
        outContent.close();
        sc.close();
    }

    @Test
    public void test4FacultyViewEnrollment_CoursesOffered() throws IOException {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/student", "postgres", "1234");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        FacultyDashboard faculty = new FacultyDashboard();
        
        String input = "4\n2023\nW\ncs305\n6\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        Scanner sc = new Scanner(System.in);
        faculty.main(conn, "fac1",sc);

        String output = outContent.toString();
        String expectedOutput = "ENROLLMENT IN COURSE";
        // assertEquals(output, expectedOutput);

        assertTrue(output.contains(expectedOutput));

        inputStream.close();
        outContent.close();
        sc.close();
    }    

    @Test
    public void test4FacultyViewEnrollment_CoursesNotOffered() throws IOException {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/student", "postgres", "1234");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        FacultyDashboard faculty = new FacultyDashboard();
        
        String input = "4\n1219\nW\n6\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        Scanner sc = new Scanner(System.in);
        faculty.main(conn, "fac1",sc);

        String output = outContent.toString();
        String expectedOutput = "No courses offered";
        // assertEquals(output, expectedOutput);

        assertTrue(output.contains(expectedOutput));

        inputStream.close();
        outContent.close();
        sc.close();
    }
    

    // fac1 has offered 305
    @Test
    public void test5Faculty_UpdateCourseGrades() throws IOException {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/student", "postgres", "1234");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        FacultyDashboard faculty = new FacultyDashboard();
        
        acadVariables acadVar = new acadVariables(conn);
        String currentAcademicStatus = acadVar.getSEMESTERSTATUS();
        
        Statement stmt = null;
        try {
            String query = "UPDATE academic_variables SET semester_status = 'grade_submission' WHERE user_id = 'dean';";
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
        }catch (SQLException e) {
            e.printStackTrace();
        }
        
        String input = "5\ncs305\n6\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        Scanner sc = new Scanner(System.in);
        faculty.main(conn, "fac1",sc);

        String output = outContent.toString();
        String expectedOutput = "Grade Updated for";
        // assertEquals(output, expectedOutput);

        try {
            String query = String.format("UPDATE academic_variables SET semester_status = '%s' WHERE user_id = 'dean';", currentAcademicStatus);
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
        }catch (SQLException e) {
            e.printStackTrace();
        }
        assertTrue(output.contains(expectedOutput));


        inputStream.close();
        outContent.close();
        sc.close();
    }

    @Test
    public void test5Faculty_UpdateCourseGrades_inValidCourse() throws IOException {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/student", "postgres", "1234");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        FacultyDashboard faculty = new FacultyDashboard();
        
        acadVariables acadVar = new acadVariables(conn);
        String currentAcademicStatus = acadVar.getSEMESTERSTATUS();
        
        Statement stmt = null;
        try {
            String query = "UPDATE academic_variables SET semester_status = 'grade_submission' WHERE user_id = 'dean';";
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
        }catch (SQLException e) {
            e.printStackTrace();
        }
        
        String input = "5\ncs355\n6\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        Scanner sc = new Scanner(System.in);
        faculty.main(conn, "fac1",sc);

        String output = outContent.toString();
        String expectedOutput = "Course ID not offered";
        // assertEquals(output, expectedOutput);

        try {
            String query = String.format("UPDATE academic_variables SET semester_status = '%s' WHERE user_id = 'dean';", currentAcademicStatus);
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
        }catch (SQLException e) {
            e.printStackTrace();
        }
        assertTrue(output.contains(expectedOutput));


        inputStream.close();
        outContent.close();
        sc.close();
    }

    @Test
    public void test5Faculty_UpdateCourseGrades_noCourseOffered() throws IOException {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/student", "postgres", "1234");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        FacultyDashboard faculty = new FacultyDashboard();
        
        acadVariables acadVar = new acadVariables(conn);
        String currentAcademicStatus = acadVar.getSEMESTERSTATUS();
        
        Statement stmt = null;
        try {
            String query = "UPDATE academic_variables SET semester_status = 'grade_submission' WHERE user_id = 'dean';";
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
            String query2 = "INSERT INTO faculty(faculty_id, name, contact, department, designation) VALUES ('fac99', 'test', 'test', 'CSE', 'test')";
            String query3 = "INSERT INTO users(user_id, password) VALUES('fac99',1234);";
            stmt.executeUpdate(query2);
            stmt.executeUpdate(query3);
        }catch (SQLException e) {
            e.printStackTrace();
        }
        
        String input = "5\n6\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        Scanner sc = new Scanner(System.in);
        faculty.main(conn, "fac99",sc);

        String output = outContent.toString();
        String expectedOutput = "You are not offering any course";
        // assertEquals(output, expectedOutput);

        try {
            String query = String.format("UPDATE academic_variables SET semester_status = '%s' WHERE user_id = 'dean';", currentAcademicStatus);
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
            String query2 = "DELETE FROM faculty WHERE faculty_id = 'fac99';";
            String query3 = "DELETE FROM users WHERE user_id = 'fac99';";
            stmt.executeUpdate(query2);
            stmt.executeUpdate(query3);
        }catch (SQLException e) {
            e.printStackTrace();
        }
        assertTrue(output.contains(expectedOutput));


        inputStream.close();
        outContent.close();
        sc.close();
    }

    @Test
    public void test5Faculty_UpdateCourseGrades_outOfSession() throws IOException {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/student", "postgres", "1234");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        FacultyDashboard faculty = new FacultyDashboard();
        acadVariables acadVar = new acadVariables(conn);
        String currentAcademicStatus = acadVar.getSEMESTERSTATUS();
        
        Statement stmt = null;
        try {
            String query = "UPDATE academic_variables SET semester_status = 'end' WHERE user_id = 'dean';";
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
        }catch (SQLException e) {
            e.printStackTrace();
        }
        
        String input = "5\n6\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        Scanner sc = new Scanner(System.in);
        faculty.main(conn, "fac1",sc);

        String output = outContent.toString();
        String expectedOutput = "cannot be updated at this time";
        // assertEquals(output, expectedOutput);

        try {
            String query = String.format("UPDATE academic_variables SET semester_status = '%s' WHERE user_id = 'dean';", currentAcademicStatus);
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
        }catch (SQLException e) {
            e.printStackTrace();
        }
        assertTrue(output.contains(expectedOutput));

        inputStream.close();
        outContent.close();
        sc.close();
    }

    @Test
    public void test6actionOnCourses_outOfSession() throws IOException {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/student", "postgres", "1234");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        FacultyDashboard faculty = new FacultyDashboard();

        acadVariables acadVar = new acadVariables(conn);
        String currentAcademicStatus = acadVar.getSEMESTERSTATUS();
        
        Statement stmt = null;
        try {
            String query = "UPDATE academic_variables SET semester_status = 'end' WHERE user_id = 'dean';";
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
        }catch (SQLException e) {
            e.printStackTrace();
        }
        
        String input = "3\n6\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        Scanner sc = new Scanner(System.in);
        faculty.main(conn, "fac1",sc);

        String output = outContent.toString();
        String expectedOutput = "Floating/updation is not allowed";
        // assertEquals(output, expectedOutput);

        try {
            String query = String.format("UPDATE academic_variables SET semester_status = '%s' WHERE user_id = 'dean';", currentAcademicStatus);
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
        }catch (SQLException e) {
            e.printStackTrace();
        }

        assertTrue(output.contains(expectedOutput));

        inputStream.close();
        outContent.close();
        sc.close();
    }   

    @Test
    public void test6actionOnCourses_inValidChoice() throws IOException {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/student", "postgres", "1234");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        FacultyDashboard faculty = new FacultyDashboard();

        acadVariables acadVar = new acadVariables(conn);
        String currentAcademicStatus = acadVar.getSEMESTERSTATUS();
        
        Statement stmt = null;
        try {
            String query = "UPDATE academic_variables SET semester_status = 'float' WHERE user_id = 'dean';";
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
        }catch (SQLException e) {
            e.printStackTrace();
        }
        
        String input = "3\n41\n4\n6\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        Scanner sc = new Scanner(System.in);
        faculty.main(conn, "fac1",sc);

        String output = outContent.toString();
        String expectedOutput = "Invalid Choice: enter";
        // assertEquals(output, expectedOutput);

        try {
            String query = String.format("UPDATE academic_variables SET semester_status = '%s' WHERE user_id = 'dean';", currentAcademicStatus);
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
        }catch (SQLException e) {
            e.printStackTrace();
        }

        assertTrue(output.contains(expectedOutput));

        inputStream.close();
        outContent.close();
        sc.close();
    }   

    @Test
    public void test6actionOnCourses_registerCourses_courseCatalog() throws IOException {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/student", "postgres", "1234");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        FacultyDashboard faculty = new FacultyDashboard();

        acadVariables acadVar = new acadVariables(conn);
        String currentAcademicStatus = acadVar.getSEMESTERSTATUS();
        
        Statement stmt = null;
        try {
            String query = "UPDATE academic_variables SET semester_status = 'float' WHERE user_id = 'dean';";
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
        }catch (SQLException e) {
            e.printStackTrace();
        }
        
        String input = "3\n1\n1\n3\n4\n6\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        Scanner sc = new Scanner(System.in);
        faculty.main(conn, "fac1",sc);

        String output = outContent.toString();
        String expectedOutput = "Courses offered by the university";
        // assertEquals(output, expectedOutput);

        try {
            String query = String.format("UPDATE academic_variables SET semester_status = '%s' WHERE user_id = 'dean';", currentAcademicStatus);
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
        }catch (SQLException e) {
            e.printStackTrace();
        }

        assertTrue(output.contains(expectedOutput));

        inputStream.close();
        outContent.close();
        sc.close();
    }   

    @Test
    public void test6_0actionOnCourses_registerCourses_register_offerAndRegister() throws IOException {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/student", "postgres", "1234");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        FacultyDashboard faculty = new FacultyDashboard();

        acadVariables acadVar = new acadVariables(conn);
        String currentAcademicStatus = acadVar.getSEMESTERSTATUS();
        
        Statement stmt = null;
        try {
            String query = "UPDATE academic_variables SET semester_status = 'float' WHERE user_id = 'dean';";
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
        }catch (SQLException e) {
            e.printStackTrace();
        }
        
        String input = "3\n1\n2\nge555\n5\n3\n4\n6\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        Scanner sc = new Scanner(System.in);
        faculty.main(conn, "fac1",sc);

        String output = outContent.toString();
        String expectedOutput = "You are now registered";
        // assertEquals(output, expectedOutput);

        try {
            String query = String.format("UPDATE academic_variables SET semester_status = '%s' WHERE user_id = 'dean';", currentAcademicStatus);
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
        }catch (SQLException e) {
            e.printStackTrace();
        }

        assertTrue(output.contains(expectedOutput));

        inputStream.close();
        outContent.close();
        sc.close();
    }   

    @Test
    public void test6_1actionOnCourses_registerCourses_register_alreadyOffered_alreadyRegistered() throws IOException {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/student", "postgres", "1234");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        FacultyDashboard faculty = new FacultyDashboard();

        acadVariables acadVar = new acadVariables(conn);
        String currentAcademicStatus = acadVar.getSEMESTERSTATUS();
        
        Statement stmt = null;
        try {
            String query = "UPDATE academic_variables SET semester_status = 'float' WHERE user_id = 'dean';";
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
        }catch (SQLException e) {
            e.printStackTrace();
        }
        
        String input = "3\n1\n2\nge555\n5\n3\n4\n6\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        Scanner sc = new Scanner(System.in);
        faculty.main(conn, "fac1",sc);

        String output = outContent.toString();
        String expectedOutput = "You are already registered";
        // assertEquals(output, expectedOutput);

        try {
            String query = String.format("UPDATE academic_variables SET semester_status = '%s' WHERE user_id = 'dean';", currentAcademicStatus);
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
        }catch (SQLException e) {
            e.printStackTrace();
        }

        assertTrue(output.contains(expectedOutput));

        inputStream.close();
        outContent.close();
        sc.close();
    }   

    @Test
    public void test6_2actionOnCourses_registerCourses_register_alreadyOffered_register() throws IOException {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/student", "postgres", "1234");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        FacultyDashboard faculty = new FacultyDashboard();

        acadVariables acadVar = new acadVariables(conn);
        String currentAcademicStatus = acadVar.getSEMESTERSTATUS();
        
        Statement stmt = null;
        try {
            String query = "UPDATE academic_variables SET semester_status = 'float' WHERE user_id = 'dean';";
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
        }catch (SQLException e) {
            e.printStackTrace();
        }
        
        String input = "3\n1\n2\nge555\n5\n3\n4\n6\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        Scanner sc = new Scanner(System.in);
        faculty.main(conn, "fac2",sc);

        String output = outContent.toString();
        String expectedOutput = "You are now registered";
        // assertEquals(output, expectedOutput);

        try {
            String query = String.format("UPDATE academic_variables SET semester_status = '%s' WHERE user_id = 'dean';", currentAcademicStatus);
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
        }catch (SQLException e) {
            e.printStackTrace();
        }

        assertTrue(output.contains(expectedOutput));

        inputStream.close();
        outContent.close();
        sc.close();
    }   

    //Note: Serial order of these function is important as, firstly course is offered and registered, then registered again by other fac, now deregistered by first fac and the by second fac
    // 6_0 to 7_2 together

    @Test
    public void test7_0actionOnCourses_deregisterCourses() throws IOException {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/student", "postgres", "1234");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        FacultyDashboard faculty = new FacultyDashboard();

        acadVariables acadVar = new acadVariables(conn);
        String currentAcademicStatus = acadVar.getSEMESTERSTATUS();
        
        Statement stmt = null;
        try {
            String query = "UPDATE academic_variables SET semester_status = 'float' WHERE user_id = 'dean';";
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
        }catch (SQLException e) {
            e.printStackTrace();
        }
        
        String input = "3\n2\nge555\n4\n6\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        Scanner sc = new Scanner(System.in);
        faculty.main(conn, "fac1",sc);

        String output = outContent.toString();
        String expectedOutput = "You are now deregistered";
        // assertEquals(output, expectedOutput);

        try {
            String query = String.format("UPDATE academic_variables SET semester_status = '%s' WHERE user_id = 'dean';", currentAcademicStatus);
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
        }catch (SQLException e) {
            e.printStackTrace();
        }

        assertTrue(output.contains(expectedOutput));

        inputStream.close();
        outContent.close();
        sc.close();
    }   


    @Test
    public void test7_1actionOnCourses_deregisterCourses_notRegistered() throws IOException {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/student", "postgres", "1234");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        FacultyDashboard faculty = new FacultyDashboard();

        acadVariables acadVar = new acadVariables(conn);
        String currentAcademicStatus = acadVar.getSEMESTERSTATUS();
        
        Statement stmt = null;
        try {
            String query = "UPDATE academic_variables SET semester_status = 'float' WHERE user_id = 'dean';";
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
        }catch (SQLException e) {
            e.printStackTrace();
        }
        
        String input = "3\n2\nge555\n4\n6\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        Scanner sc = new Scanner(System.in);
        faculty.main(conn, "fac1",sc);
        sc.close();

        String output = outContent.toString();
        String expectedOutput = "You are not registered";
        // assertEquals(output, expectedOutput);

        try {
            String query = String.format("UPDATE academic_variables SET semester_status = '%s' WHERE user_id = 'dean';", currentAcademicStatus);
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
        }catch (SQLException e) {
            e.printStackTrace();
        }

        assertTrue(output.contains(expectedOutput));

        inputStream.close();
        outContent.close();
        sc.close();
    }   

    
    @Test
    public void test7_2actionOnCourses_deregisterCourses() throws IOException {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/student", "postgres", "1234");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        FacultyDashboard faculty = new FacultyDashboard();

        acadVariables acadVar = new acadVariables(conn);
        String currentAcademicStatus = acadVar.getSEMESTERSTATUS();
        
        Statement stmt = null;
        try {
            String query = "UPDATE academic_variables SET semester_status = 'float' WHERE user_id = 'dean';";
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
        }catch (SQLException e) {
            e.printStackTrace();
        }
        
        String input = "3\n2\nge555\n4\n6\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        Scanner sc = new Scanner(System.in);
        faculty.main(conn, "fac2",sc);

        String output = outContent.toString();
        String expectedOutput = "You are now deregistered";
        String expectedOutput2 = "Course Offering Deleted";
        // assertEquals(output, expectedOutput);

        try {
            String query = String.format("UPDATE academic_variables SET semester_status = '%s' WHERE user_id = 'dean';", currentAcademicStatus);
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
        }catch (SQLException e) {
            e.printStackTrace();
        }

        assertTrue(output.contains(expectedOutput) || output.contains(expectedOutput2));

        inputStream.close();
        outContent.close();
        sc.close();
    }   


    @Test
    public void test6actionOnCourses_registerCourses_register_notElegible() throws IOException {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/student", "postgres", "1234");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        FacultyDashboard faculty = new FacultyDashboard();

        acadVariables acadVar = new acadVariables(conn);
        String currentAcademicStatus = acadVar.getSEMESTERSTATUS();
        
        Statement stmt = null;
        try {
            String query = "UPDATE academic_variables SET semester_status = 'float' WHERE user_id = 'dean';";
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
        }catch (SQLException e) {
            e.printStackTrace();
        }
        
        String input = "3\n1\n2\nee101\n3\n4\n6\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        Scanner sc = new Scanner(System.in);
        faculty.main(conn, "fac1",sc);

        String output = outContent.toString();
        String expectedOutput = "not elegible to register";
        // assertEquals(output, expectedOutput);

        try {
            String query = String.format("UPDATE academic_variables SET semester_status = '%s' WHERE user_id = 'dean';", currentAcademicStatus);
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
        }catch (SQLException e) {
            e.printStackTrace();
        }

        assertTrue(output.contains(expectedOutput));

        inputStream.close();
        outContent.close();
        sc.close();
    }   

    @Test
    public void test6actionOnCourses_registerCourses_register_courseNotFound() throws IOException {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/student", "postgres", "1234");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        FacultyDashboard faculty = new FacultyDashboard();

        acadVariables acadVar = new acadVariables(conn);
        String currentAcademicStatus = acadVar.getSEMESTERSTATUS();
        
        Statement stmt = null;
        try {
            String query = "UPDATE academic_variables SET semester_status = 'float' WHERE user_id = 'dean';";
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
        }catch (SQLException e) {
            e.printStackTrace();
        }
        
        String input = "3\n1\n2\nzz101\n3\n4\n6\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        Scanner sc = new Scanner(System.in);
        faculty.main(conn, "fac1",sc);

        String output = outContent.toString();
        String expectedOutput = "Course not found";
        // assertEquals(output, expectedOutput);

        try {
            String query = String.format("UPDATE academic_variables SET semester_status = '%s' WHERE user_id = 'dean';", currentAcademicStatus);
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
        }catch (SQLException e) {
            e.printStackTrace();
        }

        assertTrue(output.contains(expectedOutput));

        inputStream.close();
        outContent.close();
        sc.close();
    }   


    @Test
    public void test6actionOnCourses_registerCourses_register_notElegible1() throws IOException {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/student", "postgres", "1234");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        FacultyDashboard faculty = new FacultyDashboard();

        acadVariables acadVar = new acadVariables(conn);
        String currentAcademicStatus = acadVar.getSEMESTERSTATUS();
        
        Statement stmt = null;
        try {
            String query = "UPDATE academic_variables SET semester_status = 'float' WHERE user_id = 'dean';";
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
        }catch (SQLException e) {
            e.printStackTrace();
        }
        
        String input = "3\n1\n2\nee101\n3\n4\n6\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        Scanner sc = new Scanner(System.in);
        faculty.main(conn, "fac1",sc);

        String output = outContent.toString();
        String expectedOutput = "not elegible to register";
        // assertEquals(output, expectedOutput);

        try {
            String query = String.format("UPDATE academic_variables SET semester_status = '%s' WHERE user_id = 'dean';", currentAcademicStatus);
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
        }catch (SQLException e) {
            e.printStackTrace();
        }

        assertTrue(output.contains(expectedOutput));

        inputStream.close();
        outContent.close();
        sc.close();
    }   
}