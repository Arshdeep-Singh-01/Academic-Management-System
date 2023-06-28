package software;

import org.junit.FixMethodOrder;
import org.junit.Test;
import static org.junit.Assert.*;

import software.Acad.AcademicDashboard;
import software.Acad.acadVariables;
import software.Faculty.FacultyDashboard;
import software.database.Login;


import software.student.Constants;
import software.student.StudentDashboard;

import java.sql.Statement;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

@FixMethodOrder(org.junit.runners.MethodSorters.NAME_ASCENDING)
public class AcademicDashboardTest {
    @Test
    public void test0Acad_notDean() throws IOException {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/student", "postgres", "1234");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        AcademicDashboard acad = new AcademicDashboard();
        
        String input = "";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        Scanner sc = new Scanner(System.in);
        acad.main(conn, "2020csb1074",sc);
        sc.close();

        String output = outContent.toString();
        String expectedOutput = "You are not authorized";
        // assertEquals(output, expectedOutput);

        assertTrue(output.contains(expectedOutput));

        inputStream.close();
        outContent.close();
    }   

    @Test
    public void test0Acad_invalidChoice() throws IOException {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/student", "postgres", "1234");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        AcademicDashboard acad = new AcademicDashboard();
        
        String input = "9\n5\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        Scanner sc = new Scanner(System.in);
        acad.main(conn, "dean",sc);
        sc.close();

        String output = outContent.toString();
        String expectedOutput = "Invalid Choice";
        // assertEquals(output, expectedOutput);

        assertTrue(output.contains(expectedOutput));

        inputStream.close();
        outContent.close();
    }    

    @Test
    public void test0Acad_viewAcademicSessionDetails() throws IOException {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/student", "postgres", "1234");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        AcademicDashboard acad = new AcademicDashboard();
        
        String input = "0\n5\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        Scanner sc = new Scanner(System.in);
        acad.main(conn, "dean",sc);
        sc.close();

        String output = outContent.toString();
        String expectedOutput = "Academic Session Status";
        // assertEquals(output, expectedOutput);

        assertTrue(output.contains(expectedOutput));

        inputStream.close();
        outContent.close();
    }    

    @Test
    public void test1Acad_updateAcademicSession_status() throws IOException {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/student", "postgres", "1234");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        AcademicDashboard acad = new AcademicDashboard();
        acadVariables acadVar = new acadVariables(conn);
        String currentAcademicStatus = acadVar.getSEMESTERSTATUS();
        
        String input = "1\n1\nstart\n9\n4\n5\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        Scanner sc = new Scanner(System.in);
        acad.main(conn, "dean",sc);
        sc.close();

        String output = outContent.toString();
        String expectedOutput = "Academic Session Status Updated";
        // assertEquals(output, expectedOutput);

        try {
            Statement stmt = conn.createStatement();
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
    public void test1Acad_updateAcademicSession_year() throws IOException {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/student", "postgres", "1234");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        AcademicDashboard acad = new AcademicDashboard();
        acadVariables acadVar = new acadVariables(conn);
        String currentAcademicYear = acadVar.getCURRENTYEAR();
        
        String input = "1\n2\nstart\n4\n5\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        Scanner sc = new Scanner(System.in);
        acad.main(conn, "dean",sc);
        sc.close();

        String output = outContent.toString();
        String expectedOutput = "Academic Session Year Updated";
        // assertEquals(output, expectedOutput);

        try {
            Statement stmt = conn.createStatement();
            String query = String.format("UPDATE academic_variables SET current_year = '%s' WHERE user_id = 'dean';", currentAcademicYear);
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
    public void test1Acad_updateAcademicSession_semester() throws IOException {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/student", "postgres", "1234");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        AcademicDashboard acad = new AcademicDashboard();
        acadVariables acadVar = new acadVariables(conn);
        String currentAcademicSemester = acadVar.getCURRENTSEMESTER();
        
        String input = "1\n3\nstart\n4\n5\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        Scanner sc = new Scanner(System.in);
        acad.main(conn, "dean",sc);
        sc.close();

        String output = outContent.toString();
        String expectedOutput = "Academic Session Semester Updated";
        // assertEquals(output, expectedOutput);

        try {
            Statement stmt = conn.createStatement();
            String query = String.format("UPDATE academic_variables SET current_semester = '%s' WHERE user_id = 'dean';", currentAcademicSemester);
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
    public void test2_0Acad_editViewCourseCatalog_add_newCourse() throws IOException {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/student", "postgres", "1234");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        AcademicDashboard acad = new AcademicDashboard();
        
        String input = "2\n1\nge999\nDemoName\nDemoLTP\n0\nDemoType\nDemoDpt\n5\n5\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        Scanner sc = new Scanner(System.in);
        acad.main(conn, "dean",sc);
        sc.close();

        String output = outContent.toString();
        String expectedOutput = "Course Added";
        // assertEquals(output, expectedOutput);

        assertTrue(output.contains(expectedOutput));

        inputStream.close();
        outContent.close();
    }

    @Test
    public void test2_1Acad_editViewCourseCatalog_add_alreadyAdded() throws IOException {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/student", "postgres", "1234");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        AcademicDashboard acad = new AcademicDashboard();
        
        String input = "2\n1\nge999\n9\n5\n5\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        Scanner sc = new Scanner(System.in);
        acad.main(conn, "dean",sc);
        sc.close();

        String output = outContent.toString();
        String expectedOutput = "Course Already Added";
        // assertEquals(output, expectedOutput);

        assertTrue(output.contains(expectedOutput));

        inputStream.close();
        outContent.close();
    }

    @Test
    public void test2_2Acad_editViewCourseCatalog_edit_courseNotFound() throws IOException {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/student", "postgres", "1234");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        AcademicDashboard acad = new AcademicDashboard();
        
        String input = "2\n3\nzz999\n5\n5\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        Scanner sc = new Scanner(System.in);
        acad.main(conn, "dean",sc);
        sc.close();

        String output = outContent.toString();
        String expectedOutput = "Course Not Found";
        // assertEquals(output, expectedOutput);

        assertTrue(output.contains(expectedOutput));

        inputStream.close();
        outContent.close();
    }

    @Test
    public void test2_3Acad_editViewCourseCatalog_edit() throws IOException {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/student", "postgres", "1234");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        AcademicDashboard acad = new AcademicDashboard();
        
        String input = "2\n3\nge999\nnewLTP\n0\nnewType\nnewDpt\n5\n5\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        Scanner sc = new Scanner(System.in);
        acad.main(conn, "dean",sc);
        sc.close();

        String output = outContent.toString();
        String expectedOutput = "Course Updated";
        // assertEquals(output, expectedOutput);

        assertTrue(output.contains(expectedOutput));

        inputStream.close();
        outContent.close();
    }

    @Test
    public void test2_4Acad_editViewCourseCatalog_remove() throws IOException {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/student", "postgres", "1234");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        AcademicDashboard acad = new AcademicDashboard();
        
        String input = "2\n2\nge999\n5\n5\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        Scanner sc = new Scanner(System.in);
        acad.main(conn, "dean",sc);
        sc.close();

        String output = outContent.toString();
        String expectedOutput = "Course Removed";
        // assertEquals(output, expectedOutput);

        assertTrue(output.contains(expectedOutput));

        inputStream.close();
        outContent.close();
    }

    @Test
    public void test2_6Acad_editViewCourseCatalog_view() throws IOException {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/student", "postgres", "1234");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        AcademicDashboard acad = new AcademicDashboard();
        
        String input = "2\n4\n5\n5\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        Scanner sc = new Scanner(System.in);
        acad.main(conn, "dean",sc);
        sc.close();

        String output = outContent.toString();
        String expectedOutput = "Course Catalog";
        // assertEquals(output, expectedOutput);

        assertTrue(output.contains(expectedOutput));

        inputStream.close();
        outContent.close();
    }

    @Test
    public void test3_0Acad_viewGrades_studentNotfound() throws IOException {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/student", "postgres", "1234");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        AcademicDashboard acad = new AcademicDashboard();
        
        String input = "3\n999csb999\n5\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        Scanner sc = new Scanner(System.in);
        acad.main(conn, "dean",sc);
        sc.close();

        String output = outContent.toString();
        String expectedOutput = "Student Not Found";
        // assertEquals(output, expectedOutput);

        assertTrue(output.contains(expectedOutput));

        inputStream.close();
        outContent.close();
    }

    @Test
    public void test3_1Acad_viewGrades_view_noCourse() throws IOException {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/student", "postgres", "1234");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        AcademicDashboard acad = new AcademicDashboard();
        
        String input = "3\n2020csb1074\n1\nS\n2999\n9\n2\n5\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        Scanner sc = new Scanner(System.in);
        acad.main(conn, "dean",sc);
        sc.close();

        String output = outContent.toString();
        String expectedOutput = "No Courses Found";
        // assertEquals(output, expectedOutput);

        assertTrue(output.contains(expectedOutput));

        inputStream.close();
        outContent.close();
    }

    @Test
    public void test3_2Acad_viewGrades_view_noTranscript() throws IOException {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/student", "postgres", "1234");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        AcademicDashboard acad = new AcademicDashboard();
        
        String input = "3\n2020csb1074\n1\nS\n2023\nN\n2\n5\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        Scanner sc = new Scanner(System.in);
        acad.main(conn, "dean",sc);
        sc.close();

        String output = outContent.toString();
        String expectedOutput = "Do you want to Generate Transcript";
        // assertEquals(output, expectedOutput);

        assertTrue(output.contains(expectedOutput));

        inputStream.close();
        outContent.close();
    }

    @Test
    public void test3_3Acad_viewGrades_view_withTranscript() throws IOException {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/student", "postgres", "1234");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        AcademicDashboard acad = new AcademicDashboard();
        
        String input = "3\n2020csb1074\n1\nS\n2023\nY\n2\n5\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        Scanner sc = new Scanner(System.in);
        acad.main(conn, "dean",sc);
        sc.close();

        String output = outContent.toString();
        String expectedOutput = "Do you want to Generate Transcript";
        // assertEquals(output, expectedOutput);

        assertTrue(output.contains(expectedOutput));

        inputStream.close();
        outContent.close();
    }

    @Test
    public void test4_0Acad_graduationCheck() throws IOException {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/student", "postgres", "1234");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        AcademicDashboard acad = new AcademicDashboard();
        
        String input = "4\n2020csb1074\n5\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        Scanner sc = new Scanner(System.in);
        acad.main(conn, "dean",sc);
        sc.close();

        String output = outContent.toString();
        String expectedOutput = "eligible for Graduation";
        // assertEquals(output, expectedOutput);

        assertTrue(output.contains(expectedOutput));

        inputStream.close();
        outContent.close();
    }

    @Test
    public void test4_0Acad_graduationCheck_notFound() throws IOException {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/student", "postgres", "1234");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        AcademicDashboard acad = new AcademicDashboard();
        
        String input = "4\n9999zz9999\n5\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        Scanner sc = new Scanner(System.in);
        acad.main(conn, "dean",sc);
        sc.close();

        String output = outContent.toString();
        String expectedOutput = "Student Not Found";
        // assertEquals(output, expectedOutput);

        assertTrue(output.contains(expectedOutput));

        inputStream.close();
        outContent.close();
    }

}