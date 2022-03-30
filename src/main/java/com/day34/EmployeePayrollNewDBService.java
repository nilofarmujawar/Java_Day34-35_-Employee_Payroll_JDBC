package com.day34;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

import com.day34.EmployeePayrollException.Exception;

public class EmployeePayrollNewDBService {
    private static EmployeePayrollNewDBService employeePayrollNewDBService;
    private PreparedStatement employeePayrollNewDataStatement;

    public EmployeePayrollNewDBService() {
    }

    public static EmployeePayrollNewDBService getInstance() {
        if (employeePayrollNewDBService == null)
            employeePayrollNewDBService = new EmployeePayrollNewDBService();
        return employeePayrollNewDBService;
    }

    public List<EmployeePayrollData> readData() {
        String sql = "SELECT e.id,e.name,e.gender,e.start,d.dept_name,p.basic_pay"
                + " FROM employee_payroll e JOIN employee_department ed "
                + "ON e.id=ed.id JOIN department d ON d.dept_id=ed.dept_id JOIN payroll_details p ON e.id=p.id ";
        return this.getEmployeePayrollDataUsingSQLQuery(sql);
    }

    private List<EmployeePayrollData> getEmployeePayrollDataUsingSQLQuery(String sql) {
        List<EmployeePayrollData> employeePayrollList = null;
        try (Connection connection = this.getConnection();) {
            PreparedStatement prepareStatement = connection.prepareStatement(sql);
            ResultSet resultSet = prepareStatement.executeQuery(sql);
            employeePayrollList = this.getEmployeePayrollData(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeePayrollList;
    }

    private List<EmployeePayrollData> getEmployeePayrollData(ResultSet resultSet) {
        List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
        List<String> department = new ArrayList<String>();
        try {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String gender = resultSet.getString("gender");
                LocalDate startDate = resultSet.getDate("start").toLocalDate();
                String dept = resultSet.getString("dept_name");
                double salary = resultSet.getDouble("basic_pay");
                department.add(dept);
                String[] departmentArray = new String[department.size()];
                employeePayrollList
                        .add(new EmployeePayrollData(id, name, salary, startDate, gender, departmentArray));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeePayrollList;
    }

    public List<EmployeePayrollData> getEmployeePayrollData(String name) {
        List<EmployeePayrollData> employeePayrollList = null;
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

    public EmployeePayrollData addEmployeeToPayrollUC8(String name, String gender, double salary, LocalDate start)
            throws EmployeePayrollException {
        int id = -1;
        Connection connection = null;
        EmployeePayrollData employeePayrollData = null;
        try {
            connection = this.getConnection();
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try (Statement statement = connection.createStatement()) {
            String sql = String.format(
                    "INSERT INTO employee_payroll(name,gender,salary,start) VALUES ('%s','%s','%s','%s');", name,
                    gender, salary, Date.valueOf(start));
            int rowAffected = statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            if (rowAffected == 1) {
                ResultSet resultSet = statement.getGeneratedKeys();
                if (resultSet.next())
                    id = resultSet.getInt(1);
            }
            employeePayrollData = new EmployeePayrollData(id, name, salary, start, gender);
            if (rowAffected == 0)
                throw new EmployeePayrollException(Exception.INSERTION_FAILED, "Not added into database");
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return employeePayrollData;
        }
        try (Statement statement = connection.createStatement()) {
            double deductions = salary * 0.2;
            double taxablePay = salary - deductions;
            double tax = taxablePay * 0.1;
            double netPay = salary - tax;
            String sql = String.format(
                    "INSERT INTO payroll_details(id,basic_pay,deductions,taxable_pay,tax ,net_pay)VALUES (%s,%s,%s,%s,%s,%s)",
                    id, salary, deductions, taxablePay, tax, netPay);
            int rowAffected = statement.executeUpdate(sql);
            if (rowAffected == 1) {
                employeePayrollData = new EmployeePayrollData(id, name, salary, start);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return employeePayrollData;
        }
        try (Statement statement = connection.createStatement()) {
            int departmentId = 104;
            String departmentName = "Technology";
            String sql = String.format("INSERT INTO department(Dept_id,Dept_name) VALUES ('%s','%s')", departmentId,
                    departmentName);
            int rowAffected = statement.executeUpdate(sql);
            if (rowAffected == 1) {
                employeePayrollData = new EmployeePayrollData(departmentId, departmentName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return employeePayrollData;
        }
        try (Statement statement = connection.createStatement()) {
            int departmentId = 104;
            String sql = String.format("INSERT INTO employee_department(id,Dept_id) VALUES (%s,%s)", id, departmentId);
            int rowAffected = statement.executeUpdate(sql);
            if (rowAffected == 1) {
                employeePayrollData = new EmployeePayrollData(id, departmentId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return employeePayrollData;
        }
        try {
            connection.commit();
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
        return employeePayrollData;
    }

    public EmployeePayrollData addEmployeeToPayroll(String name, double salary, LocalDate start, String gender) {
        int employeeId = -1;
        EmployeePayrollData employee_payroll_Data = null;
        String sql = String.format("INSERT INTO employee_payroll(name,gender,salary,start) values('%s','%s','%s','%s')",
                name, gender, salary, Date.valueOf(start));
        try (Connection connection = this.getConnection()) {
            PreparedStatement preparedstatement = connection.prepareStatement(sql);
            int rowAffected = preparedstatement.executeUpdate(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            if (rowAffected == 1) {
                ResultSet resultSet = preparedstatement.getGeneratedKeys();
                if (resultSet.next())
                    employeeId = resultSet.getInt(1);
            }
            employee_payroll_Data = new EmployeePayrollData(employeeId, name, salary, start);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employee_payroll_Data;
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