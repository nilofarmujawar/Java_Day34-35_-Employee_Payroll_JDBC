package com.day34;
/**
 * import all classes
 */

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

/**
 * create class EmployeePayrollDbService
 */
public class EmployeePayrollDbService {
    /**
     * An object that represents a precompiled SQL statement.
     * A SQL statement is precompiled and stored in a PreparedStatement object.
     * This object can then be used to efficiently execute this statement multiple times.
     */
    private PreparedStatement employeePayrollDataStatement;
    private static EmployeePayrollDbService employeePayrollDBService;

    /**
     * create default constructor name as EmployeePayrollDbService
     */
    public EmployeePayrollDbService() {
    }

    /**
     * create a method name as getInstance
     * @return employeePayrollDBService
     */
    public static EmployeePayrollDbService getInstance() {
        /**
         * employeePayrollDBService is empty
         */
        if (employeePayrollDBService == null)
            employeePayrollDBService = new EmployeePayrollDbService();
        return employeePayrollDBService;
    }

    /**
     *  create a list ,method name as readData
     * @return
     */
    public List<EmployeePayrollData> readData() {
        String sql = "SELECT * FROM employee_payroll;";
        /**
         * create list ,create object name as employeepayrolllist
         * all data stored in this object
         */
        List<EmployeePayrollData> employeePayrollList = new ArrayList<EmployeePayrollData>();
        /**
         * A connection (session) with a specific database.
         * SQL statements are executed and results are returned within the context of a connection.
         */
        try (Connection connection = this.getConnection();) {
            /**
             * Statement=
             * The object used for executing a static SQL statement and returning the results it produces.
             * createStatement=
             * Creates a Statement object for sending SQL statements to the database.
             */
            Statement statement = connection.createStatement();
            /**
             *  resultSet will be scrollable, will not show changes made by others,
             *   and will be updatable
             */
            ResultSet resultSet = statement.executeQuery(sql);
            employeePayrollList = this.getEmployeePayrollData(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        /**
         * return employeePayrollList
         */
        return employeePayrollList;
    }

    /**
     * create a method name as updateEmployeeData
     * this is parameterized method
     * @param name-employee name
     * @param salary-employee salary
     * @return udate employee name ,salary
     */
    public int updateEmployeeData(String name, Double salary) {
        return this.updateEmployeeDataUsingPreparedStatement(name, salary);
    }

    /**
     * create a method name as updateEmployeeDataUsingPreparedStatement
     * this is parameterized method
     * @param name of employee
     * @param salary -employee salary
     * @return update name and salary
     */
    private int updateEmployeeDataUsingPreparedStatement(String name, Double salary) {

        /**
         * A Connection object's database is able to provide information describing its tables,
         * its supported SQL grammar, its stored procedures, the capabilities of this connection, and so on.
         * This information is obtained with the getMetaData method.
         */
        try (Connection connection = this.getConnection();) {
            String sql = "update employee_payroll set salary = ? where name= ? ;";
            /**
             * An object that represents a precompiled SQL statement.
             * A SQL statement is precompiled and stored in a PreparedStatement object.
             * This object can then be used to efficiently execute this statement multiple times
             */
            PreparedStatement preparestatement = connection.prepareStatement(sql);
            /**
             * set salary
             * set name
             */
            preparestatement.setDouble(1, salary);
            preparestatement.setString(2, name);
            /**
             * Executes the SQL statement in this PreparedStatement object,
             * which must be an SQL Data Manipulation Language (DML) statement, such as UPDATE ;
             * or an SQL statement that returns nothing, such as a DDL statement.
             */
            int update = preparestatement.executeUpdate();
            return update;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * create a method name as preparedStatementForEmployeeData
     */
    private void preparedStatementForEmployeeData() {
        try {
            Connection connection = this.getConnection();
            String sql = "Select * from employee_payroll WHERE name = ?";
            /**
             * Creates a PreparedStatement object for sending parameterized SQL statements to the database.
             */
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
        /**
         * return employeePayrollList
         */
        return employeePayrollList;
    }

    /**
     * create method name as getEmployeePayrollData
     * @param resultSet-set all the data of employee
     * @return employeelist
     */
    private List<EmployeePayrollData> getEmployeePayrollData(ResultSet resultSet) {
        /**
         * create a list ,create object name as employeePayrollList
         */
        List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                /**
                 * set employee data
                 * ColumnLable = employee id,name,salary,start .
                 */
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                double salary = resultSet.getDouble("salary");
                LocalDate startDate = resultSet.getDate("start").toLocalDate();
                employeePayrollList.add(new EmployeePayrollData(id, name, salary, startDate));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        /**
         * return employeePayrollList
         */
        return employeePayrollList;
    }

    /**
     * create a method name as getEmployeeForDateRange
     * @param startDate of employee
     * @param endDate of employee
     * @return employee payroll start date and end date
     */
    public List<EmployeePayrollData> getEmployeeForDateRange(LocalDate startDate, LocalDate endDate) {
        String sql = String.format("SELECT * FROM employee_payroll WHERE START BETWEEN '%s' AND '%s';",
                Date.valueOf(startDate), Date.valueOf(endDate));
        return this.getEmployeePayrollDataUsingDB(sql);
    }

    /**
     * create a method name as getEmployeePayrollDataUsingDB
     * @param sql- sql data
     * @return employeePayrollList
     */
    private List<EmployeePayrollData> getEmployeePayrollDataUsingDB(String sql) {
        /**
         * A table of data representing a database result set,
         * which is usually generated by executing a statement that queries the database.
         */
        ResultSet resultSet;
        /**
         * now employeePayrollList is empty
         */
        List<EmployeePayrollData> employeePayrollList = null;
        /**
         * A Connection object's database is able to provide information describing its tables, its supported SQL grammar, its stored procedures,
         * the capabilities of this connection, and so on.
         * This information is obtained with the getMetaData method.
         */
        try (Connection connection = this.getConnection();) {
            /**
             * Creates a PreparedStatement object for sending parameterized SQL statements to the database
             */
            PreparedStatement prepareStatement = connection.prepareStatement(sql);
            /**
             * Executes the given SQL statement, which returns a single ResultSet object
             */
            resultSet = prepareStatement.executeQuery(sql);
            /**
             * result set
             */
            employeePayrollList = this.getEmployeePayrollData(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        /**
         * return employee payroll list
         */
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

    /**
     * create a map method name as get_AverageSalary_ByGender
     * in this method we calculate avrg salary by employee gender
     * @return genderToAverageSalaryMap
     */
    public Map<String, Double> get_AverageSalary_ByGender() {
        String sql = "SELECT gender,AVG(salary) as avg_salary FROM employee_payroll GROUP BY gender;";
        /**
         * map = An object that maps keys to values
         * create a map ,create a object name as genderToAverageSalaryMap.
         * data stored in this object
         */
        Map<String, Double> genderToAverageSalaryMap = new HashMap<>();
        /**
         * A connection (session) with a specific database.
         * SQL statements are executed and results are returned within the context of a connection.
         */
        try (Connection connection = this.getConnection();) {
            /**
             * Creates a PreparedStatement object for sending parameterized SQL statements to the database
             */
            PreparedStatement prepareStatement = connection.prepareStatement(sql);
            /**
             * a ResultSet object that contains the data produced by the given query
             */
            ResultSet resultSet = prepareStatement.executeQuery(sql);
            while (resultSet.next()) {
                /**
                 * result set
                 * columnLablel - gender ;
                 * columnLablel - avg_salary.
                 */
                String gender = resultSet.getString("gender");
                double salary = resultSet.getDouble("avg_salary");
                /**
                 * caliing put method from  genderToAverageSalaryMap object
                 */
                genderToAverageSalaryMap.put(gender, salary);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        /**
         * return avrg salary by gender
         */
        return genderToAverageSalaryMap;
    }

    /**
     * create a map,method name as get_SumOfSalary_ByGender
     * @return sum of salary by employee gender
     */
    public Map<String, Double> get_SumOfSalary_ByGender() {
        String sql = "SELECT gender,SUM(salary) as sum_salary FROM employee_payroll GROUP BY gender;";
        /**
         * Map = An object that maps keys to values.
         * create map ,create a object name as genderToSumOfSalaryMap
         * all data stored in this object
         */
        Map<String, Double> genderToSumOfSalaryMap = new HashMap<>();
        /**
         * A Connection object's database is able to provide information describing its tables, its supported SQL grammar,
         * its stored procedures, the capabilities of this connection, and so on.
         * This information is obtained with the getMetaData method.
         */
        try (Connection connection = this.getConnection()) {
            /**
             * Creates a PreparedStatement object for sending parameterized SQL statements to the database.
             */
            PreparedStatement prepareStatement = connection.prepareStatement(sql);
            /**
             * Executes the given SQL statement, which returns a single ResultSet object.
             */
            ResultSet resultSet = prepareStatement.executeQuery(sql);
            while (resultSet.next()) {
                /**
                 * result set
                 * columnLable = gender ;
                 * columnLable = sum_salary
                 */
                String gender = resultSet.getString("gender");
                double salary = resultSet.getDouble("sum_salary");
                /**
                 * calling put method from  genderToSumOfSalaryMap object
                 */
                genderToSumOfSalaryMap.put(gender, salary);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        /**
         * return sum of salary by employee gender
         */
        return genderToSumOfSalaryMap;
    }

    /**
     * create a method name as get_Min_Salary_ByGender
     * in this method get employee min salary by there gender
     * @return genderToMinSalaryMap
     */
    public Map<String, Double> get_Min_Salary_ByGender() {
        String sql = "SELECT gender,MIN(salary) as min_salary FROM employee_payroll GROUP BY gender;";
        /**
         * Map =
         * An object that maps keys to values.
         * A map cannot contain duplicate keys; each key can map to at most one value.
         *
         * create a map ,create a object name as genderToMinSalaryMap
         * all data stored in this object
         */
        Map<String, Double> genderToMinSalaryMap = new HashMap<>();
        /**
         * A Connection object's database is able to provide information describing its tables,
         * its supported SQL grammar, its stored procedures,the capabilities of this connection, and so on.
         * This information is obtained with the getMetaData method.
         */
        try (Connection connection = this.getConnection();) {
            /**
             * Creates a PreparedStatement object for sending parameterized SQL statements to the database.
             */
            PreparedStatement prepareStatement = connection.prepareStatement(sql);
            /**
             * Executes the given SQL statement, which returns a single ResultSet object.
             */
            ResultSet resultSet = prepareStatement.executeQuery(sql);
            while (resultSet.next()) {
                /**
                 * set result
                 * columnLable = gender;
                 * columnLable = min_salary
                 */
                String gender = resultSet.getString("gender");
                double salary = resultSet.getDouble("min_salary");
                /**
                 * calling put method from  genderToMinSalaryMap object
                 */
                genderToMinSalaryMap.put(gender, salary);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        /**
         * get min salary by employee gender
         */
        return genderToMinSalaryMap;
    }
}