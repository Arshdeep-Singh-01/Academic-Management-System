package software;

import java.sql.Connection;
import java.util.Scanner;

public interface Dashboard {
    void main(Connection conn, String userID, Scanner sc);
}
