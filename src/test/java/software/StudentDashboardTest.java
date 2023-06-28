package software;

import org.junit.FixMethodOrder;
import org.junit.Test;
import static org.junit.Assert.*;


import software.Acad.acadVariables;
import software.student.Constants;
import software.student.StudentDashboard;


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
public class StudentDashboardTest {
    Constants constant = new Constants();

    @Test
    public void test1StudentProfileSuccess() throws IOException {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/student", "postgres", "1234");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        StudentDashboard student = new StudentDashboard();
        
        String input = "1\n8\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        Scanner sc = new Scanner(System.in);
        student.main(conn, "2020csb1074",sc);
        sc.close();

        String output = outContent.toString();
        String expectedOutput = "STUDENT PROFILE";

        assertTrue(output.contains(expectedOutput));
        System.out.println("tested");

        inputStream.close();
        outContent.close();
    }

    @Test
    public void test1StudentProfile_invalidChoice() throws IOException {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/student", "postgres", "1234");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        StudentDashboard student = new StudentDashboard();
        
        String input = "9\n8\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        Scanner sc = new Scanner(System.in);
        student.main(conn, "2020csb1074",sc);
        sc.close();

        String output = outContent.toString();
        String expectedOutput = "Invalid Choice";

        assertTrue(output.contains(expectedOutput));
        System.out.println("tested");

        inputStream.close();
        outContent.close();
    }

    @Test
    public void test2ViewCourses() throws IOException{
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/student", "postgres", "1234");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        StudentDashboard student = new StudentDashboard();
        
        String input = "2\n8\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        Scanner sc = new Scanner(System.in);
        student.main(conn, "2020csb1074",sc);
        sc.close();

        String output = outContent.toString();
        String expectedOutput = "COURSES";

        assertTrue(output.contains(expectedOutput));
        // System.out.println("tested");

        inputStream.close();
        outContent.close();
    }

    @Test
    public void test3ViewOfferedCourses() throws IOException{
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/student", "postgres", "1234");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        StudentDashboard student = new StudentDashboard();
        
        String input = "3\nCSE\n2023\nW\n8\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        Scanner sc = new Scanner(System.in);
        student.main(conn, "2020csb1074",sc);
        sc.close();

        String output = outContent.toString();
        String expectedOutput = "COURSE OFFERED";

        assertTrue(output.contains(expectedOutput));
        // assertEquals(output, expectedOutput);
        
        inputStream.close();
        outContent.close();
    }

    @Test
    public void test4calculateCGPA() throws IOException{
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/student", "postgres", "1234");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        StudentDashboard student = new StudentDashboard();
        
        String input = "6\n8\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        Scanner sc = new Scanner(System.in);
        student.main(conn, "2020csb1074",sc);
        sc.close();

        String output = outContent.toString();
        String expectedOutput = "Your CGPA is";

        assertTrue(output.contains(expectedOutput));

        inputStream.close();
        outContent.close();
    }

    @Test
    public void test5CalculateSGPA() throws IOException{
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/student", "postgres", "1234");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        StudentDashboard student = new StudentDashboard();
        
        String input = "7\n2023\nS\n8\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        Scanner sc = new Scanner(System.in);
        student.main(conn, "2020csb1074",sc);
        sc.close();

        String output = outContent.toString();
        String expectedOutput = "Your SGPA";

        assertTrue(output.contains(expectedOutput));

        inputStream.close();
        outContent.close();
    }

    @Test
    public void test6EnrollCourses_Success() throws IOException{
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/student", "postgres", "1234");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        StudentDashboard student = new StudentDashboard();
        acadVariables acadVar = new acadVariables(conn);
        String currentAcademicStatus = acadVar.getSEMESTERSTATUS();
        
        Statement stmt = null;
        try {
            String query = "UPDATE academic_variables SET semester_status = 'enroll' WHERE user_id = 'dean';";
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
        }catch (SQLException e) {
            e.printStackTrace();
        }
        
        String input = "4\nge108\n8\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        Scanner sc = new Scanner(System.in);
        student.main(conn, "2023csb1000",sc);
        sc.close();

        String output = outContent.toString();
        // String expectedOutput1 = "don't have sufficient credits";
        // String expectedOutput2 = "not elegible";
        // String expectedOutput3 = "do not have the pre-requisites";
        // String expectedOutput4 = "Already enrolled";
        String expectedOutput5 = "enrolled successfully";

        try {
            String query = String.format("UPDATE academic_variables SET semester_status = '%s' WHERE user_id = 'dean';", currentAcademicStatus);
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
        }catch (SQLException e) {
            e.printStackTrace();
        }

        assertTrue( output.contains(expectedOutput5));

        inputStream.close();
        outContent.close();
    }

    @Test
    public void test7EnrollCourses_alreadyTaken() throws IOException{
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/student", "postgres", "1234");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        StudentDashboard student = new StudentDashboard();
        acadVariables acadVar = new acadVariables(conn);
        String currentAcademicStatus = acadVar.getSEMESTERSTATUS();
        
        Statement stmt = null;
        try {
            String query = "UPDATE academic_variables SET semester_status = 'enroll' WHERE user_id = 'dean';";
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
        }catch (SQLException e) {
            e.printStackTrace();
        }
        
        String input = "4\nge104\n8\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
    
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        Scanner sc = new Scanner(System.in);
        student.main(conn, "2023csb1000",sc);
        sc.close();
    
        String output = outContent.toString();
        String expectedOutput = "already taken";

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
    }
    @Test
    public void test9EnrollCourses_outOfSession() throws IOException{
        // when academic session is not at enroll stage

        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/student", "postgres", "1234");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        StudentDashboard student = new StudentDashboard();
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
        
        String input = "4\nge108\n8\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        Scanner sc = new Scanner(System.in);
        student.main(conn, "2023csb1000",sc);
        sc.close();

        String output = outContent.toString();
        String expectedOutput1 = "cannot enroll";

        try {
            String query = String.format("UPDATE academic_variables SET semester_status = '%s' WHERE user_id = 'dean';", currentAcademicStatus);
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
        }catch (SQLException e) {
            e.printStackTrace();
        }
        assertTrue(output.contains(expectedOutput1) );


        inputStream.close();
        outContent.close();
    }

    @Test
    public void test9EnrollCourses_checkearnedcredits() throws IOException{
        // when academic session is not at enroll stage

        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/student", "postgres", "1234");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        StudentDashboard student = new StudentDashboard();
        acadVariables acadVar = new acadVariables(conn);
        String currentAcademicStatus = acadVar.getSEMESTERSTATUS();
        
        Statement stmt = null;
        try {
            String query = "UPDATE academic_variables SET semester_status = 'enroll' WHERE user_id = 'dean';";
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
        }catch (SQLException e) {
            e.printStackTrace();
        }
        
        String input = "4\ncs539\n8\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        Scanner sc = new Scanner(System.in);
        student.main(conn, "2021csb5000",sc);
        sc.close();

        String output = outContent.toString();
        String expectedOutput1 = "enrolled successfully";

        try {
            String query = String.format("UPDATE academic_variables SET semester_status = '%s' WHERE user_id = 'dean';", currentAcademicStatus);
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
        }catch (SQLException e) {
            e.printStackTrace();
        }

        assertTrue(output.contains(expectedOutput1) );

        inputStream.close();
        outContent.close();

    }
    @Test
    public void test9DropCourses_checkearnedcredits() throws IOException{
        // when academic session is not at enroll stage

        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/student", "postgres", "1234");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        StudentDashboard student = new StudentDashboard();
        acadVariables acadVar = new acadVariables(conn);
        String currentAcademicStatus = acadVar.getSEMESTERSTATUS();
        
        Statement stmt = null;
        try {
            String query = "UPDATE academic_variables SET semester_status = 'enroll' WHERE user_id = 'dean';";
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
        }catch (SQLException e) {
            e.printStackTrace();
        }
        
        String input = "5\ncs539\n8\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        Scanner sc = new Scanner(System.in);
        student.main(conn, "2021csb5000",sc);
        sc.close();

        String output = outContent.toString();
        String expectedOutput1 = "dropped successfully";
        String expectedOutput2 = "not enrolled";

        try {
            String query = String.format("UPDATE academic_variables SET semester_status = '%s' WHERE user_id = 'dean';", currentAcademicStatus);
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
        }catch (SQLException e) {
            e.printStackTrace();
        }

        assertTrue(output.contains(expectedOutput1) || output.contains(expectedOutput2));

        inputStream.close();
        outContent.close();
    }

    @Test
    public void test8DropCourses() throws IOException{
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/student", "postgres", "1234");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        StudentDashboard student = new StudentDashboard();
        acadVariables acadVar = new acadVariables(conn);
        String currentAcademicStatus = acadVar.getSEMESTERSTATUS();
        
        Statement stmt = null;
        try {
            String query = "UPDATE academic_variables SET semester_status = 'enroll' WHERE user_id = 'dean';";
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
        }catch (SQLException e) {
            e.printStackTrace();
        }
        
        String input = "5\nge108\n8\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        Scanner sc = new Scanner(System.in);
        student.main(conn, "2023csb1000",sc);
        sc.close();

        String output = outContent.toString();
        String expectedOutput1 = "dropped successfully";
        String expectedOutput2 = "not enrolled";

        try {
            String query = String.format("UPDATE academic_variables SET semester_status = '%s' WHERE user_id = 'dean';", currentAcademicStatus);
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
        }catch (SQLException e) {
            e.printStackTrace();
        }

        assertTrue(output.contains(expectedOutput1) || output.contains(expectedOutput2));

        inputStream.close();
        outContent.close();
    }

    @Test
    public void test10DropCourses_outOfSession() throws IOException{
        // when academic session is not at enroll stage

        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/student", "postgres", "1234");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        StudentDashboard student = new StudentDashboard();
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
        
        String input = "5\nge108\n8\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        Scanner sc = new Scanner(System.in);
        student.main(conn, "2023csb1000",sc);
        sc.close();

        String output = outContent.toString();
        String expectedOutput1 = "cannot enroll";

        try {
            String query = String.format("UPDATE academic_variables SET semester_status = '%s' WHERE user_id = 'dean';", currentAcademicStatus);
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
        }catch (SQLException e) {
            e.printStackTrace();
        }
        assertTrue(output.contains(expectedOutput1) );


        inputStream.close();
        outContent.close();
    }

}