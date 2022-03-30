package com.day34;

/**
 * import all sql pacakge classes
 * import Enumeration class
 */

import java.sql.*;
import java.util.Enumeration;

/**
 * create a class name as JDBCEmployeePayroll
 */
public class JDBCEmployeePayroll {
    /**
     * create a main method ,all program execute in main method
     * @param args no arguments, its defaults
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        /**
         * try catch block is used for exception handling
         */
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver is loaded");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("cannot find driver in classpath", e);
        }
        listDrivers();
        try {
            /**
             * get connection
             * url, username ,password
             */
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/employee_payroll_service?useSSL=false", "root",
                    "Mujawar#1118");
            System.out.println("Connection Done..!!!" + con);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * create a method name as list driver
     *
     */
    public static void listDrivers() {
        /**
         * Enumeration :-
         * An object that implements the Enumeration interface generates a series of elements, one at a time
         * Methods are provided to enumerate through the elements of a vector,
         * the keys of a hashtable, and the values in a hashtable
         */
        Enumeration<Driver> driverList = DriverManager.getDrivers();
        while (driverList.hasMoreElements()) {
            Driver driverClass = (Driver) driverList.nextElement();
            System.out.println(" " + driverClass.getClass().getName());
        }
    }
}