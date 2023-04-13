package eu.fiit.cookingmanager.cookingmanager.controller;

import java.sql.*;

import io.github.cdimascio.dotenv.Dotenv;

public class DBController {

    private final String driver = "org.postgresql.Driver";
    private Connection conn = null;
    private String connURL = null;
    private Dotenv env = null;

    public DBController() {
        this.env = Dotenv.configure().directory("src/main/java/eu/fiit/cookingmanager/cookingmanager/").filename(".env").load();
        this.connURL = String.format("jdbc:postgresql://%s/%s", this.env.get("DB_HOST"), this.env.get("DB_NAME"));

    }

    private void dbConnect() {
        try {
            Class.forName(this.driver);
        }
        catch(ClassNotFoundException classNotFoundException) {
            System.err.println("Could not set database driver");
        }

        try {
            this.conn = DriverManager.getConnection(this.connURL, this.env.get("DB_USER"), this.env.get("DB_PASS"));
        }
        catch (SQLException sqlException) {
            System.err.println("Could not establish database connection");
        }
    }

    private void dbDisconnect() {
        try {
            if (this.conn != null && !this.conn.isClosed()) {
                this.conn.close();
            }
        }
        catch(SQLException sqlException) {
            System.err.println("Error while trying to disconnect database");
        }
    }

}
