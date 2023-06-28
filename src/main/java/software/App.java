/*
 * Arshdeep Singh
 * 2020csb1074
 */

package software;



import java.sql.Connection;
import java.util.*;

import software.Acad.AcademicDashboard;
import software.Faculty.FacultyDashboard;
import software.database.DBfunctions;
import software.database.Login;
import software.student.Constants;
import software.student.StudentDashboard;

import java.io.Console;

public class App {
    public static void main(String[] args) {

        Constants constant = new Constants();

        // connecting to database
        DBfunctions db = new DBfunctions();
        Connection conn =  db.connect_to_db(constant.DB_NAME, constant.DB_username, constant.DB_password);

        int choice = 0;
        Scanner sc = new Scanner(System.in);
        while(true)
        {
            try {
                System.out.println("\nSelect: 1-Login, 2-Quit");
                System.out.print("Enter your Choice: ");

                choice = sc.nextInt();
                sc.nextLine();

                if(choice==constant.LOGIN){
                    String userID, password;

                    System.out.print("Whether you are a student (1) or a Faculty (2) or academics (3)?: ");
                    String userType = sc.nextLine();

                    System.out.println("\n----Welcome to the Academic Database Management System----\n");
                    System.out.print("Enter userID  : ");
                    
                    userID = sc.nextLine();

                    System.out.print("Enter Password: ");
                    
                    password = sc.nextLine();

                    // user login
                    Login login = new Login();

                    login.loginUser(conn,userID,password);

                    if(login.getLoginSuccess()){
                        System.out.println("\n[ Login Successful ]\n");

                        switch(userType){
                            case "1":
                                // Student Dashboard
                                StudentDashboard student = new StudentDashboard();
                                student.main(conn, userID,sc);
                                break;
                            case "2":
                                // Faculty Dashboard
                                FacultyDashboard Faculty = new FacultyDashboard();
                                Faculty.main(conn, userID,sc);
                                break;
                            case "3":
                                // Academics Dashboard
                                AcademicDashboard academics = new AcademicDashboard();
                                academics.main(conn, userID,sc);
                                break; 
                            default:
                                System.out.println("Invalid Choice of USER TYPE");
                                break;
                        }
                    }
                    else{
                        System.out.println("| Problem in logging-in. Please retry.");
                        System.out.println("| Error: "+ login.loginError);
                        System.out.println("------------------------------");
                    }
                    
                }
                else if(choice==constant.QUIT){
                    System.out.println("Thank you for visiting");
                    break;
                }
                else{
                    System.out.println("Invalid Choice");
                    continue;
                }
            } catch (Exception e) {
                // handling exception
                System.out.println("Error occured");
            }
        }
        sc.close();
    }
}


