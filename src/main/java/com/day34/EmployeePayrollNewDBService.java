package com.day34;

/**
 * import all class
 */

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

import com.day34.EmployeePayrollException.Exception;

/**
 * create a class name as EmployeePayrollNewDBService
 */
public class EmployeePayrollNewDBService {
    private static EmployeePayrollNewDBService employeePayrollNewDBService;
    private PreparedStatement employeePayrollNewDataStatement;

    /**
     * create default constructor name as EmployeePayrollNewDBService
     */
    public EmployeePayrollNewDBService() {
    }

    /**
     * create a method name as getInstance
     * here EmployeePayrollNewDBService class name
     * @return employeePayrollNewDBService
     */
    public static EmployeePayrollNewDBService getInstance() {
        if (employeePayrollNewDBService == null)
            employeePayrollNewDBService = new EmployeePayrollNewDBService();
        return employeePayrollNewDBService;
    }

    /**
     * create a method name as readdata
     * use for reading the data
     * @return
     */
    public List<EmployeePayrollData> readData() {
        String sql = "SELECT e.id,e.name,e.gender,e.start,d.dept_name,p.basic_pay"
                + " FROM employee_payroll e JOIN employee_department ed "
                + "ON e.id=ed.id JOIN department d ON d.dept_id=ed.dept_id JOIN payroll_details p ON e.id=p.id ";
        return this.getEmployeePayrollDataUsingSQLQuery(sql);
    }

    /**
     * create  a method name as getEmployeePayrollDataUsingSQLQuery
     * @param sql get query
     * @return employeePayrollList
     */
    private List<EmployeePayrollData> getEmployeePayrollDataUsingSQLQuery(String sql) {
        /**
         * create a list ,list is empty
         */
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

    /**
     * create a method name as  getEmployeePayrollData
     * @param resultSet employee name, id, gender, salary dep_name ,Basic_pay
     * @return
     */
    private List<EmployeePayrollData> getEmployeePayrollData(ResultSet resultSet) {
        /**
         * create a list ,create object name as employeePayrollList
         * all data store is in this object
         */
        List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
        /**
         * create a list object name as department
         */
        List<String> department = new ArrayList<String>();
        try {
            while (resultSet.next()) {
                /**
                 * columnlable=id,name,gender,start,dep_name,basic_pay
                 */
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

    /**
     * create a method name as getEmployeePayrollData
     * @param name employee name
     * @return employeePayrollList
     */
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

    /**
     * create a method name as preparedStatementForEmployeeData
     */
    private void preparedStatementForEmployeeData() {
        try {
            /**
             * A Connection object's database is able to provide information describing its tables,
             * its supported SQL grammar, its stored procedures,
             * the capabilities of this connection, and so on.
             * This information is obtained with the getMetaData method.
             */
            Connection connection = this.getConnection();
            String sql = "  SELECT e.id,e.name,e.gender,e.start,d.dept_name,p.basic_pay"
                    + "    FROM employee_payroll e JOIN employee_department ed ON e.id=ed.id"
                    + "    JOIN department d ON d.dept_id=ed.dept_id" + "    JOIN payroll_details p ON e.id=p.id ;";
            employeePayrollNewDataStatement = connection.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * create a method name as getEmployeeForDateRang
     * @param startDate employee start date
     * @param endDate employee end date
     * @return getEmployeePayrollDataUsingDB(sql)
     */
    public List<EmployeePayrollData> getEmployeeForDateRange(LocalDate startDate, LocalDate endDate) {
        String sql = String.format("SELECT * FROM employee_payroll WHERE START BETWEEN '%s' AND '%s';",
                Date.valueOf(startDate), Date.valueOf(endDate));
        return this.getEmployeePayrollDataUsingDB(sql);
    }

    /**
     * create a method name as getEmployeePayrollDataUsingDB
     * @param sql execute query
     * @return update data
     */
    private List<EmployeePayrollData> getEmployeePayrollDataUsingDB(String sql) {
        /**
         * A table of data representing a database result set,
         * which is usually generated by executing a statement that queries the database.
         */
        ResultSet resultSet;
        /**
         * create a list employeePayrollList is null
         */
        List<EmployeePayrollData> employeePayrollList = null;
        /**
         * A connection (session) with a specific database.
         * SQL statements are executed and results are returned within the context of a connection.
         */
        try (Connection connection = this.getConnection();) {
            /**
             * An object that represents a precompiled SQL statement.
             * A SQL statement is precompiled and stored in a PreparedStatement object.
             * This object can then be used to efficiently execute this statement multiple times.
             */
            PreparedStatement prepareStatement = connection.prepareStatement(sql);
            /**
             * Executes the given SQL statement, which returns a single ResultSet object.
             */
            resultSet = prepareStatement.executeQuery(sql);
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
     * create a method name as updateEmployeeData,this is parameterized method
     * @param name of employee
     * @param salary employee salary
     * @return name and salary updated
     */
    public int updateEmployeeData(String name, Double salary) {
        return this.updateEmployeeDataUsingPreparedStatement(name, salary);
    }

    /**
     * create a method name as updateEmployeeDataUsingPreparedStatement,
     * this method is parameterized method
     * @param name of employee
     * @param salary employee salary
     * @return update name and salary
     */
    private int updateEmployeeDataUsingPreparedStatement(String name, double salary) {
        try (Connection connection = this.getConnection();) {
            String sql = "UPDATE payroll_details SET basic_pay = ? WHERE id = "
                    + "(SELECT id from employee_payroll WHERE name = ? );";
            PreparedStatement preparestatement = connection.prepareStatement(sql);
            /**
             * set salary and name
             * parameterIndex 1, salary
             * parameterIndex 2, name
             */
            preparestatement.setDouble(1, salary);
            preparestatement.setString(2, name);
            int update = preparestatement.executeUpdate();
            return update;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * create a map, method name as get_Max_Salary_ByGender
     * @return genderToMaxSalaryMap
     */
    public Map<String, Double> get_Max_Salary_ByGender() {
        String sql = "SELECT gender,MAX(salary) as max_salary FROM employee_payroll GROUP BY gender;";
        /**
         * create a map,object name as genderToMaxSalaryMap
         */
        Map<String, Double> genderToMaxSalaryMap = new HashMap<>();
        try (Connection connection = this.getConnection();) {
            PreparedStatement prepareStatement = connection.prepareStatement(sql);
            ResultSet resultSet = prepareStatement.executeQuery(sql);
            while (resultSet.next()) {
                /**
                 * getString=
                 * the column value; if the value is SQL NULL, the value returned is null
                 *
                 * columanLable - gender
                 * columanLable - max_salary
                 */
                String gender = resultSet.getString("gender");
                double salary = resultSet.getDouble("max_salary");
                /**
                 * put =
                 * the previous value associated with key, or null if there was no mapping for key.
                 *
                 * here calling put method from genderToMaxSalaryMap object
                 */
                genderToMaxSalaryMap.put(gender, salary);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        /**
         * return gender to max salary
         */
        return genderToMaxSalaryMap;
    }

    /**
     * create a map,method name is get_CountOfEmployee_ByGender
     * in this method count the employee by gender
     * @return genderToCountMap
     */
    public Map<String, Double> get_CountOfEmployee_ByGender() {
        String sql = "SELECT gender,COUNT(salary) as emp_count FROM employee_payroll GROUP BY gender;";
        /**
         * Map = An object that maps keys to values
         * create a map,create a object name as genderToCountMap
         * data store in genderToCountMap
         */
        Map<String, Double> genderToCountMap = new HashMap<>();
        try (Connection connection = this.getConnection();) {
            PreparedStatement prepareStatement = connection.prepareStatement(sql);
            ResultSet resultSet = prepareStatement.executeQuery(sql);
            while (resultSet.next()) {
                /**
                 * columnLable = gender;
                 * columnLable = emp_count
                 */
                String gender = resultSet.getString("gender");
                double salary = resultSet.getDouble("emp_count");
                /**
                 * calling put method from  genderToCountMap object
                 */
                genderToCountMap.put(gender, salary);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        /**
         * return count employee by gender
         */
        return genderToCountMap;
    }

    /**
     * create a method name as addEmployeeToPayrollUC8
     * in this method we add employee in payroll
     * @param name of employee
     * @param gender- employee gender
     * @param salary- employee salary
     * @param start - start date
     * @return employeePayrollData
     * @throws EmployeePayrollException
     */
    public EmployeePayrollData addEmployeeToPayrollUC8(String name, String gender, double salary, LocalDate start)
            throws EmployeePayrollException {
        /**
         * variable
         */
        int id = -1;
        /**
         * A connection (session) with a specific database.
         * SQL statements are executed and results are returned within the context of a connection.
         */
        Connection connection = null;
        /**
         *  employeePayeollData is empty
         */
        EmployeePayrollData employeePayrollData = null;
        try {
            connection = this.getConnection();
            /**
             * Sets this connection's auto-commit mode to the given state. If a connection is in auto-commit mode,
             * then all its SQL statements will be executed and committed as individual transactions
             */
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        /**
         * The object used for executing a static SQL statement and returning the results it produces.
         */
        try (Statement statement = connection.createStatement()) {
            /**
             *
             A formatted string
             */
            String sql = String.format(
                    "INSERT INTO employee_payroll(name,gender,salary,start) VALUES ('%s','%s','%s','%s');", name,
                    gender, salary, Date.valueOf(start));
            /**
             * Executes the given SQL statement and signals the driver with the given flag about whether
             * the auto-generated keys produced by this Statement object should be made available for retrieval.
             */
            int rowAffected = statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            if (rowAffected == 1) {
                /**
                 * A table of data representing a database result set,
                 * which is usually generated by executing a statement that queries the database.
                 */
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
                /**
                 * calling rollback method
                 * rollback =
                 * Undoes all changes made in the current transaction and releases any database locks
                 * currently held by this Connection object.
                 * This method should be used only when auto-commit mode has been disabled.
                 */
                connection.rollback();
            } catch (SQLException ex) {
                /**
                 * Prints this throwable and its backtrace to the standard error stream
                 */
                ex.printStackTrace();
            }
            /**
             * all data of eemployeepayrolldaa
             */
            return employeePayrollData;
        }
        /**
         * Creates a Statement object for sending SQL statements to the database
         */
        try (Statement statement = connection.createStatement()) {
            /**
             * variables
             */
            double deductions = salary * 0.2;
            double taxablePay = salary - deductions;
            double tax = taxablePay * 0.1;
            double netPay = salary - tax;
            String sql = String.format(
                    "INSERT INTO payroll_details(id,basic_pay,deductions,taxable_pay,tax ,net_pay)VALUES (%s,%s,%s,%s,%s,%s)",
                    id, salary, deductions, taxablePay, tax, netPay);
            /**
             * Executes the given SQL statement, which may be an INSERT statement
             */
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
            /**
             * variable
             */
            int departmentId = 104;
            String departmentName = "Technology";
            String sql = String.format("INSERT INTO department(Dept_id,Dept_name) VALUES ('%s','%s')", departmentId,
                    departmentName);
            /**
             * Executes the given SQL statement, which may be an INSERT, department is and department name
             */
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
            /**
             * variable
             */
            int departmentId = 104;
            String sql = String.format("INSERT INTO employee_department(id,Dept_id) VALUES (%s,%s)", id, departmentId);
            /**
             * Executes the given SQL statement, which may be an INSERT, department id
             */
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
                    /**
                     * Calling the method close on a Connection object that is already closed is a no-op.
                     */
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        /**
         * employee payroll data
         */
        return employeePayrollData;
    }

    /**
     * create a method namea as addEmployeeToPayroll
     * @param name of employee
     * @param salary -employee salary
     * @param start -employee start date
     * @param gender - employee gender
     * @return employee payroll data
     */
    public EmployeePayrollData addEmployeeToPayroll(String name, double salary, LocalDate start, String gender) {
        /**
         * variable
         */
        int employeeId = -1;
        /**
         * now employee_payroll_Data is empty
         */
        EmployeePayrollData employee_payroll_Data = null;
        String sql = String.format("INSERT INTO employee_payroll(name,gender,salary,start) values('%s','%s','%s','%s')",
                name, gender, salary, Date.valueOf(start));
        try (Connection connection = this.getConnection()) {
            PreparedStatement preparedstatement = connection.prepareStatement(sql);
            /**
             * Executes the given SQL statement, which may be an INSERT employee_payroll,name gender salary start date
             */
            int rowAffected = preparedstatement.executeUpdate(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            if (rowAffected == 1) {
                ResultSet resultSet = preparedstatement.getGeneratedKeys();
                if (resultSet.next())
                /**
                 * get int = the column value;
                 * columnIndex = 1
                 */
                    employeeId = resultSet.getInt(1);
            }
            employee_payroll_Data = new EmployeePayrollData(employeeId, name, salary, start);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        /**
         * return employee paryroll data
         */
        return employee_payroll_Data;
    }

    /**
     * create a getActiveEmployees method
     * in this method check the active employee
     * @return active employee
     */
    public List<EmployeePayrollData> getActiveEmployees() {
        String sql = "select * from employee_payroll where active=1;";
        return this.getEmployeePayrollDataUsingDBActive(sql);
    }

    /**
     * create a method name as  getEmployeePayrollDataNormalisedActive
     * this method is parameterized method
     * @param resultSet- set data
     * @return -  employeePayrollList
     */
    private List<EmployeePayrollData> getEmployeePayrollDataNormalisedActive(ResultSet resultSet) {
        /**
         * create a list ,create a object name as employeePayrollList
         * all the data stored in this object
         */
        List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                /**
                 * ColumnLable = id,name,gender,start date, salary ,activity
                 */
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String gender = resultSet.getString("gender");
                LocalDate start = resultSet.getDate("start").toLocalDate();
                double salary = resultSet.getDouble("salary");
                boolean active = resultSet.getBoolean("active");
                /**
                 * calling add method from employeePayrollList object
                 */
                employeePayrollList.add(new EmployeePayrollData(id, name, salary, start, gender, active));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        /**
         * return list
         */
        return employeePayrollList;
    }

    /**
     * create a method name as getEmployeePayrollDataUsingDBActive
     * @param sql in sql
     * @return
     */
    private List<EmployeePayrollData> getEmployeePayrollDataUsingDBActive(String sql) {
        /**
         * A table of data representing a database result set, which is usually generated by executing
         *  a statement that queries the database.
         */
        ResultSet resultSet;
        /**
         * now list is empty
         */
        List<EmployeePayrollData> employeePayrollList = null;
        try (Connection connection = this.getConnection();) {
            PreparedStatement prepareStatement = connection.prepareStatement(sql);
            /**
             * execute sql query
             */
            resultSet = prepareStatement.executeQuery(sql);
            employeePayrollList = this.getEmployeePayrollDataNormalisedActive(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        /**
         * return employee payroll list
         */
        return employeePayrollList;
    }


    /**
     * create a method name as getConnection
     * @return connection
     * @throws SQLException
     */
    private Connection getConnection() throws SQLException {
        /**
         * A Connection object's database is able to provide information describing its tables,
         * its supported SQL grammar, its stored procedures, the capabilities of this connection, and so on.
         * This information is obtained with the getMetaData method.
         */
        Connection connection;
        System.out.println("Connecting to database: ");
        /**
         * DriverManager
         * The basic service for managing a set of JDBC drivers.
         *
         * getConnection=
         * Attempts to establish a connection to the given database URL.
         * The DriverManager attempts to select an appropriate driver from the set of registered JDBC drivers
         */
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/employee_payroll_service", "root",
                "Mujawar#1118");
        /**
         * if url password and username then display this data
         */
        System.out.println("Connection successful: " + connection);
        return connection;
    }
}