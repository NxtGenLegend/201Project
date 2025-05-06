package com.study_group_matcher.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCUtil {
	private static final String URL = "jdbc:mysql://localhost/study_group_matcher";
    private static final String USER = "root";
    private static final String PASSWORD = "1lliciuM1031";

    // load MySQL driver before asking for connections to the database
    static {
		try {
            Class.forName("com.mysql.cj.jdbc.Driver");
		}
		catch (ClassNotFoundException e) {
			System.out.println("JDBC Driver not found: " + e.getMessage());
		}
	}

    /*
     * I: None
     * P: Connect to MySQL database with the above credentials
     * O: Connection to the database
     */
	public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        catch (SQLException sqle) {
			System.out.println ("SQLException: " + sqle.getMessage());
		} 
        return conn;
    }
	
	/*
	 * I: Resources to be closed (result sets, statements, connections)
	 * P: Closes the given resources
	 * O: None
	 */
	public static void close(AutoCloseable ac) {
		try {
			if (ac != null) {
				ac.close();
			}
		}
		catch (SQLException sqle) {
			System.out.println("sqle: " + sqle.getMessage());
		} catch (Exception e) {
			System.out.println("Resource couldn't be closed");
		}
	}
}
