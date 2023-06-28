package software.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Login {
    // to show the status of login success
    private boolean loginSuccess = false;
    public boolean getLoginSuccess(){
        return this.loginSuccess;
    }

    // to keep track of error 
    public String loginError;

    //main function for user login
    public void loginUser(Connection conn,String userID, String password){
        try {
            // Construct the SQL query to fetch the user record
            String sql = String.format("SELECT * FROM users WHERE user_id = '%s';", userID);
            
            Statement statement = conn.createStatement();

            // Execute the query and get the result set
            ResultSet resultSet = statement.executeQuery(sql);

            // Check if a record with the given userID exists in the database
            if (resultSet.next()) {
                // Extract the password from the result set
                String storedPassword = resultSet.getString("password");

                // Check if the entered password matches the stored password
                if (password.equals(storedPassword)) {
                    this.loginSuccess = true;
                } else {
                    this.loginError = "invalid password";
                }
            } else {
                this.loginError = "User not found.";
            }

            // Close the result set, statement, and connection
            resultSet.close();
            statement.close();
        } catch (Exception e) {
            // handle exception
            System.out.println("Error occured in Login: "+e);
        }
    }
}
