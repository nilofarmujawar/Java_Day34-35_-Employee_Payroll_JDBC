package com.day34;
/**
 * import all classes
 */

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * create a class name as EmployeePayrollDbService
 */
public class EmployeePayrollDbService {

    private PreparedStatement employeePayrollDataStatement;
    private static EmployeePayrollDbService employeePayrollDBService;

    public EmployeePayrollDbService() {
    }

    public static EmployeePayrollDbService getInstance() {
        if (employeePayrollDBService == null)
            employeePayrollDBService = new EmployeePayrollDbService();
        return employeePayrollDBService;
    }

    public List<EmployeePayrollData> readData() {
        String sql = "SELECT * FROM employee_payroll;";
        List<EmployeePayrollData> employeePayrollList = new ArrayList<EmployeePayrollData>();
        try (Connection connection = this.getConnection();) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            employeePayrollList = this.getEmployeePayrollData(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeePayrollList;
    }

    public int updateEmployeeData(String name, Double salary) {
        return this.updateEmployeeDataUsingPreparedStatement(name, salary);
    }

    private int updateEmployeeDataUsingPreparedStatement(String name, Double salary) {
        String sql = String.format("update employee_payroll set salary = %.2f where name='%s';", salary, name);
        try (Connection connection = this.getConnection();) {
            PreparedStatement prepareStatement = connection.prepareStatement(sql);
            return prepareStatement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private void preparedStatementForEmployeeData() {
        try {
            Connection connection = this.getConnection();
            String sql = "Select * from employee_payroll WHERE name = ?";
            employeePayrollDataStatement = connection.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<EmployeePayrollData> getEmployeePayrollData(String name) {
        List<EmployeePayrollData> employeePayrollList = null;
        if (this.employeePayrollDataStatement == null)
            this.preparedStatementForEmployeeData();
        try {
            employeePayrollDataStatement.setString(1, name);
            ResultSet resultSet = employeePayrollDataStatement.executeQuery();
            employeePayrollList = this.getEmployeePayrollData(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeePayrollList;
    }

    private List<EmployeePayrollData> getEmployeePayrollData(ResultSet resultSet) {
        List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
        try {
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

    public List<EmployeePayrollData> getEmployeeForDateRange(LocalDate startDate, LocalDate endDate) {
        String sql = String.format("SELECT * FROM employee_payroll2 WHERE START BETWEEN '%s' AND '%s';",
                Date.valueOf(startDate), Date.valueOf(endDate));
        return this.getEmployeePayrollDataUsingDB(sql);
    }

    private List<EmployeePayrollData> getEmployeePayrollDataUsingDB(String sql) {
        ResultSet resultSet;
        List<EmployeePayrollData> employeePayrollList = null;
        try (Connection connection = this.getConnection();) {
            PreparedStatement prepareStatement = connection.prepareStatement(sql);
            resultSet = prepareStatement.executeQuery(sql);
            employeePayrollList = this.getEmployeePayrollData(resultSet);
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
    public Map<String, Double> get_AverageSalary_ByGender() {
        String sql = "SELECT gender,AVG(salary) as avg_salary FROM employee_payroll GROUP BY gender;";
        Map<String, Double> genderToAverageSalaryMap = new HashMap<>();
        try (Connection connection = this.getConnection();) {
            PreparedStatement prepareStatement = connection.prepareStatement(sql);
            ResultSet resultSet = prepareStatement.executeQuery(sql);
            while (resultSet.next()) {
                String gender = resultSet.getString("gender");
                double salary = resultSet.getDouble("avg_salary");
                genderToAverageSalaryMap.put(gender, salary);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return genderToAverageSalaryMap;
    }

    public Map<String, Double> get_SumOfSalary_ByGender() {
        String sql = "SELECT gender,SUM(salary) as sum_salary FROM employee_payroll GROUP BY gender;";
        Map<String, Double> genderToSumOfSalaryMap = new HashMap<>();
        try (Connection connection = this.getConnection();) {
            PreparedStatement prepareStatement = connection.prepareStatement(sql);
            ResultSet resultSet = prepareStatement.executeQuery(sql);
            while (resultSet.next()) {
                String gender = resultSet.getString("gender");
                double salary = resultSet.getDouble("sum_salary");
                genderToSumOfSalaryMap.put(gender, salary);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return genderToSumOfSalaryMap;
    }

    public Map<String, Double> get_Max_Salary_ByGender() {
        String sql = "SELECT gender,MAX(salary) as max_salary FROM employee_payroll GROUP BY gender;";
        Map<String, Double> genderToMaxSalaryMap = new HashMap<>();
        try (Connection connection = this.getConnection();) {
            PreparedStatement prepareStatement = connection.prepareStatement(sql);
            ResultSet resultSet = prepareStatement.executeQuery(sql);
            while (resultSet.next()) {
                String gender = resultSet.getString("gender");
                double salary = resultSet.getDouble("max_salary");
                genderToMaxSalaryMap.put(gender, salary);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return genderToMaxSalaryMap;
    }

    public Map<String, Double> get_Min_Salary_ByGender() {
        String sql = "SELECT gender,MIN(salary) as min_salary FROM employee_payroll GROUP BY gender;";
        Map<String, Double> genderToMinSalaryMap = new HashMap<>();
        try (Connection connection = this.getConnection();) {
            PreparedStatement prepareStatement = connection.prepareStatement(sql);
            ResultSet resultSet = prepareStatement.executeQuery(sql);
            while (resultSet.next()) {
                String gender = resultSet.getString("gender");
                double salary = resultSet.getDouble("min_salary");
                genderToMinSalaryMap.put(gender, salary);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return genderToMinSalaryMap;
    }

    public Map<String, Double> get_CountOfEmployee_ByGender() {
        String sql = "SELECT gender,COUNT(salary) as emp_count FROM employee_payroll GROUP BY gender;";
        Map<String, Double> genderToCountMap = new HashMap<>();
        try (Connection connection = this.getConnection();) {
            PreparedStatement prepareStatement = connection.prepareStatement(sql);
            ResultSet resultSet = prepareStatement.executeQuery(sql);
            while (resultSet.next()) {
                String gender = resultSet.getString("gender");
                double salary = resultSet.getDouble("emp_count");
                genderToCountMap.put(gender, salary);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return genderToCountMap;
    }

}
