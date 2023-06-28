package software;

import org.junit.FixMethodOrder;
import org.junit.Test;
import static org.junit.Assert.*;

import software.database.Login;
import software.student.Constants;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@FixMethodOrder(org.junit.runners.MethodSorters.NAME_ASCENDING)
public class AppTest {
    Constants constant = new Constants();

    @Test
    public void testLoginSuccess() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/student", "postgres", "1234");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Login login = new Login();
        login.loginUser(conn, "2020csb1074", "1234");

        assertTrue(login.getLoginSuccess());
    }

    @Test
    public void testLoginFailure() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/student", "postgres", "1234");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Login login = new Login();
        login.loginUser(conn, "invalidUsername", "invalidPassword");

        assertFalse(login.getLoginSuccess());
    }

    @Test
    public void invalidLoginQuitChoice() {
        String input = "3\n2\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        App.main(null);

        String output = outContent.toString();
        String expectedOutput = "Invalid Choice";

        assertTrue(output.contains(expectedOutput));
    }

    @Test
    public void testStudentDashboard() { 
        String input = "1\n1\n2020csb1074\n1234\n8\n2\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        App.main(null);

        String output = outContent.toString();
        String expectedOutput = "Welcome to Student Dashboard";

        assertTrue(output.contains(expectedOutput));

    }

    @Test
    public void testAPP_invalidUser() {
        String input = "1\n1\n9999csb9999\n1234\n2\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        App.main(null);

        String output = outContent.toString();
        String expectedOutput = "Problem in logging-in";

        assertTrue(output.contains(expectedOutput));
    }

    @Test
    public void testAPP_invalidUserType() {
        String input = "1\n5\n2020csb1074\n1234\n2\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        App.main(null);

        String output = outContent.toString();
        String expectedOutput = "Invalid Choice of USER TYPE";

        assertTrue(output.contains(expectedOutput));
    }

    @Test
    public void testFacultyDashboard() {
        String input = "1\n2\nfac1\n1234\n1\n6\n2\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        App.main(null);

        String output = outContent.toString();
        String expectedOutput = "Welcome to Faculty Dashboard";

        assertTrue(output.contains(expectedOutput));
    }

    @Test
    public void testAcademicDashboard() {
        String input = "1\n3\ndean\n1234\n5\n2\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        App.main(null);

        String output = outContent.toString();
        String expectedOutput = "Welcome to Academic Dashboard";

        assertTrue(output.contains(expectedOutput));

    }


}
