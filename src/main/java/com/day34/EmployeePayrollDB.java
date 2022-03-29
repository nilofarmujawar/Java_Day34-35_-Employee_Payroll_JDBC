package com.day34;

/**
 * UC1 :- Ability to create a payroll service database and have java program connect to database
 *
 */

/**
 * import connection class
 * import driver class
 * import drivermanager class
 * import enumeration class
 */

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Enumeration;

public class EmployeePayrollDB {

    public static void main(String[] args) {

        String jdbcURL = "jdbc:mysql://localhost:3306/payroll_service?useSSL=false";
        String userName = "root";
        String password = "Mujawar#1118";
        Connection connection;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver Loaded Successfully");
        } catch (ClassNotFoundException exception) {
            throw new IllegalStateException("Cannot find driver in the classpath", exception);
        }
        listDrivers();
        try {
            System.out.println("Connecting to Database: " + jdbcURL);
            connection = DriverManager.getConnection(jdbcURL, userName, password);
            System.out.println("Connection is successful !!1: " + connection);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private static void listDrivers() {
        Enumeration<Driver> driverList = DriverManager.getDrivers();
        while (driverList.hasMoreElements()) {
            Driver driverClass = (Driver) driverList.nextElement();
            System.out.println("  " + driverClass.getClass().getName());
        }
    }
}