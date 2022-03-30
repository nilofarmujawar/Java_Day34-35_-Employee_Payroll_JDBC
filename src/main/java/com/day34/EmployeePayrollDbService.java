package com.day34;
/**
 * import all classes
 */

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeePayrollDbService {
    public List<EmployeePayrollData> readData() {
        String sql = "SELECT * FROM employee_payroll;";
        List<EmployeePayrollData> employeePayrollList = new ArrayList<EmployeePayrollData>();
        try (Connection connection = this.getConnection();) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                double salary = resultSet.getDouble("salary");
                LocalDate startDate = resultSet.getDate("start").toLocalDate();
                employeePayrollList.add(new EmployeePayrollData(id, name, salary, startDate));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeePayrollList;
    }
    /**
     *  create a method name as getConnection
     * @return connection
     * @throws SQLException
     */
    private Connection getConnection() throws SQLException {
        /**
         * A connection (session) with a specific database.
         * SQL statements are executed and results are returned within the context of a connection.
         */
        Connection connection;
        /**
         * Here DRiverManager is class
         * The basic service for managing a set of JDBC drivers.
         * get connection is url,username,and password
         */
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/employee_payroll_service", "root",
                "Mujawar#1118");
        /**
         * if connection is succsesful then show this result
         * result =Connection successful: com.mysql.cj.jdbc.ConnectionImpl@4009e306
         */
        System.out.println("Connection successful: " + connection);
        /**
         * return connection
         */
        return connection;
    }
}
