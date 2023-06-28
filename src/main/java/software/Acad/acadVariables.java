package software.Acad;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class acadVariables {
    private String CURRENTYEAR = "2023";
    private String CURRENTSEMESTER = "W";
    private String SEMESTERSTATUS = "running";

    public acadVariables(Connection conn) {
        try {
            Statement statement = conn.createStatement();
            String query = "SELECT * FROM academic_variables";
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                CURRENTYEAR = resultSet.getString("current_year");
                CURRENTSEMESTER = resultSet.getString("current_semester");
                SEMESTERSTATUS = resultSet.getString("semester_status");
            }
        } catch (Exception e) {
            System.out.println("Error at acadVariables: " + e.getMessage());
        }
    }
    
    public String getSEMESTERSTATUS() {
        return SEMESTERSTATUS;
    }
    public String getCURRENTYEAR() {
        return CURRENTYEAR;
    }
    public String getCURRENTSEMESTER() {
        return CURRENTSEMESTER;
    }
}
