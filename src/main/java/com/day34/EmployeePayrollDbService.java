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

        try (Connection connection = this.getConnection();) {
            String sql = "update employee_payroll set salary = ? where name=?;";
            PreparedStatement preparestatement = connection.prepareStatement(sql);
            preparestatement.setDouble(1, salary);
            preparestatement.setString(2, name);
            int update = preparestatement.executeUpdate();
            return update;
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
        String sql = String.format("SELECT * FROM employee_payroll WHERE START BETWEEN '%s' AND '%s';",
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

    public EmployeePayrollData addEmployeeToPayroll(String name, double salary, LocalDate start, String gender) {
        int employeeId = -1;
        EmployeePayrollData employee_payroll_Data = null;
        String sql = String.format("INSERT INTO employee_payroll(name,gender,salary,start) values('%s','%s','%s','%s')",
                name, gender, salary, Date.valueOf(start));
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            int rowAffected = statement.executeUpdate(sql, statement.RETURN_GENERATED_KEYS);
            if (rowAffected == 1) {
                ResultSet resultSet = statement.getGeneratedKeys();
                if (resultSet.next())
                    employeeId = resultSet.getInt(1);
            }
            employee_payroll_Data = new EmployeePayrollData(employeeId, name, salary, start);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employee_payroll_Data;
    }

    public EmployeePayrollData addEmployeeToPayrollUC8(String name, double salary, LocalDate startDate,
                                                         String gender) {
        int employeeId = -1;
        Connection connection = null;
        EmployeePayrollData employee_payroll_Data = null;
        try {
            connection = this.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try (Statement statement = connection.createStatement()) {
            String sql = String.format(
                    "INSERT INTO employee_payroll(name,gender,salary,start) " + "values('%s','%s','%s','%s')", name,
                    gender, salary, Date.valueOf(startDate));
            int rowAffected = statement.executeUpdate(sql, statement.RETURN_GENERATED_KEYS);
            if (rowAffected == 1) {
                ResultSet resultSet = statement.getGeneratedKeys();
                if (resultSet.next())
                    employeeId = resultSet.getInt(1);
            }
            employee_payroll_Data = new EmployeePayrollData(employeeId, name, salary, startDate);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (Statement statement = connection.createStatement()) {
            double deductions = salary * 0.2;
            double taxablePay = salary - deductions;
            double tax = taxablePay * 0.1;
            double netPay = salary - tax;
            String sql = String.format(
                    "INSERT INTO payroll_details(id,basic_pay,deductions,taxable_pay,tax,net_pay) "
                            + "values(%s , %s , %s , %s , %s , %s)",
                    employeeId, salary, deductions, taxablePay, tax, netPay);
            int rowAffected = statement.executeUpdate(sql);
            if (rowAffected == 1) {

                employee_payroll_Data = new EmployeePayrollData(employeeId, name, salary, startDate);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return employee_payroll_Data;
    }
}