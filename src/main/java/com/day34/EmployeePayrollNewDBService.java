package com.day34;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class EmployeePayrollNewDBService {
    private static EmployeePayrollNewDBService employeePayrollNewDBService;
    private PreparedStatement employeePayrollNewDataStatement;

    private EmployeePayrollNewDBService() {

    }

    public static EmployeePayrollNewDBService getInstance() {
        if (employeePayrollNewDBService == null)
            employeePayrollNewDBService = new EmployeePayrollNewDBService();
        return employeePayrollNewDBService;
    }

    public List<Employee_payroll_Data> readData() {
        String sql = "SELECT e.id,e.name,e.gender,e.start,d.dept_name,p.basic_pay"
                + " FROM employee_payroll e JOIN employee_department ed "
                + "ON e.id=ed.id JOIN department d ON d.dept_id=ed.dept_id JOIN payroll_details p ON e.id=p.id ";
        return this.getEmployeePayrollDataUsingSQLQuery(sql);
    }

    private List<Employee_payroll_Data> getEmployeePayrollDataUsingSQLQuery(String sql) {
        List<Employee_payroll_Data> employeePayrollList = null;
        try (Connection connection = this.getConnection();) {
            PreparedStatement prepareStatement = connection.prepareStatement(sql);
            ResultSet resultSet = prepareStatement.executeQuery(sql);
            employeePayrollList = this.getEmployeePayrollData(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeePayrollList;
    }

    private List<Employee_payroll_Data> getEmployeePayrollData(ResultSet resultSet) {
        List<Employee_payroll_Data> employeePayrollList = new ArrayList<>();
        List<String> department = new ArrayList<String>();
        try {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int companyId = resultSet.getInt("company_id");
                String name = resultSet.getString("name");
                String gender = resultSet.getString("gender");
                LocalDate startDate = resultSet.getDate("start").toLocalDate();
                String companyName = resultSet.getString("company_Name");
                String dept = resultSet.getString("dept_name");
                double salary = resultSet.getDouble("basic_pay");
                department.add(dept);
                String[] departmentArray = new String[department.size()];
                employeePayrollList.add(new Employee_payroll_Data(id, name, salary, startDate, gender, companyName,
                        companyId, departmentArray));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeePayrollList;
    }

    public List<Employee_payroll_Data> getEmployeePayrollData(String name) {
        List<Employee_payroll_D> employeePayrollList = null;
        if (this.employeePayrollNewDataStatement == null)
            this.preparedStatementForEmployeeData();
        try {
            employeePayrollNewDataStatement.setString(1, name);
            ResultSet resultSet = employeePayrollNewDataStatement.executeQuery();
            employeePayrollList = this.getEmployeePayrollData(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeePayrollList;
    }

    private void preparedStatementForEmployeeData() {
        try {
            Connection connection = this.getConnection();
            String sql = "  SELECT e.id,e.name,e.gender,e.start,d.dept_name,p.basic_pay"
                    + "    FROM employee_payroll e JOIN employee_department ed ON e.id=ed.id"
                    + "    JOIN department d ON d.dept_id=ed.dept_id" + "    JOIN payroll_details p ON e.id=p.id ;";
            employeePayrollNewDataStatement = connection.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int updateEmployeeData(String name, Double salary) {
        return this.updateEmployeeDataUsingPreparedStatement(name, salary);
    }

    private int updateEmployeeDataUsingPreparedStatement(String name, double salary) {
        try (Connection connection = this.getConnection();) {
            String sql = "UPDATE payroll_details SET basic_pay = ? WHERE id = "
                    + "(SELECT id from employee_payroll WHERE name = ? );";
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

    private Connection getConnection() throws SQLException {
        Connection connection;
        System.out.println("Connecting to database: ");
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/employee_payroll_service", "root",
                "Mujawar#1118");
        System.out.println("Connection successful: " + connection);
        return connection;
    }
}