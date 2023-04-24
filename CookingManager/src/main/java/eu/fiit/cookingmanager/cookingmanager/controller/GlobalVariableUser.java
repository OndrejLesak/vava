package eu.fiit.cookingmanager.cookingmanager.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GlobalVariableUser {

    private static String name;
    private static String surname;
    private static String email;
    private static int user_type;

    public static void setUser(int id, Connection conn) throws SQLException {

        String query = "SELECT * FROM public.user WHERE id=(?)";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setInt(1, id);
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            name = rs.getString("name");
            surname = rs.getString("surname");
            email = rs.getString("email");
            user_type = rs.getInt("user_type_id");
        }


    }

    public static String getName() {
        return name;
    }
    public static int getType(){
        return user_type;
    }
    public static String getSurname() {
        return surname;
    }
    public static String getEmail() {
        return email;
    }
}
