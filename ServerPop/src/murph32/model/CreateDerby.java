package murph32.model;

import java.sql.*;

public class CreateDerby {

    private static final String DERBY_DRIVER_CLASS = "org.apache.derby.jdbc.EmbeddedDriver";
    private static final String DERBY_CREATE_CONNECTION = "jdbc:derby:POPDB;create=true";

    private static final String CREATE_USERS_TABLE =
            "CREATE TABLE USERS (USERNAME VARCHAR(20), PASSWORD VARCHAR(200), ACCESSLEVEL VARCHAR(30))";

    private static final String CREATE_INVENTORY_TABLE =
            "CREATE TABLE INVENTORY (UPC VARCHAR(12), DESCRIPTION VARCHAR(30), PRICE DECIMAL(7,2), NUM_STOCK INTEGER)";
    private static final String INSERT_USERS_TABLE =
            "INSERT INTO USERS (USERNAME,PASSWORD,ACCESSLEVEL) VALUES(?,?,?)";
    private static final String VIEW_USERS_TABLE =
            "SELECT username, password, accesslevel FROM users";

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        System.out.println("Seeking connection...");

        Class.forName(DERBY_DRIVER_CLASS);
        Connection con = DriverManager.getConnection(DERBY_CREATE_CONNECTION);
        System.out.println("Connection opened.");

        try {
            PreparedStatement createUserTable = con.prepareStatement(CREATE_USERS_TABLE);
            createUserTable.execute();
            createUserTable.close();
            System.out.println("Users table created.");
        } catch (SQLException e) {
            System.out.println("User table already exists.");
        }
        try {
            PreparedStatement createInvTable = con.prepareStatement(CREATE_INVENTORY_TABLE);
            createInvTable.execute();
            createInvTable.close();
            System.out.println("Inventory table created.");
        } catch (SQLException e) {
            System.out.println("Inventory table already exists.");
        }

        try {
            PreparedStatement insertUsersTable = con.prepareStatement(INSERT_USERS_TABLE);
            insertUsersTable.setString(1, "ADMIN");
            insertUsersTable.setString(2, "Ansh0u");
            insertUsersTable.setString(3, "Manager");
            insertUsersTable.execute();
            insertUsersTable.close();
            System.out.println("Inserted values into Users.");
        } catch (SQLException e) {
            System.out.println("Cannot insert into Users.");
        }

        try {
            PreparedStatement viewUsersTable = con.prepareStatement(VIEW_USERS_TABLE);
            ResultSet rs = viewUsersTable.executeQuery();
            if (rs.next()) {
                String username = rs.getString(1);
                String password = rs.getString(2);
                String accessLevel = rs.getString(3);

                System.out.println("Username: " + username + " Password: " + password + " AccessLevel: " + accessLevel);
            }
        } catch (SQLException e) {
            System.out.println("Cannot complete query.");
        }
        //close down connection
        con.close();
        System.out.println("Connection closed.");
    }
}