package com.company.service;

import com.company.database.Database;
import com.company.model.Employee;

import java.sql.*;
import java.util.Optional;

public class EmployeeService {


    public static Employee getEmployeeById(String id) {
        loadEmployeeList();

        Optional<Employee> optional = Database.EMPLOYEELIST.stream()
                .filter(employee -> employee.getId().equals(id))
                .findFirst();
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    public static Employee getSuplierById(String id) {
        loadSuplierList();

        Optional<Employee> optional = Database.SUPLIERLIST.stream()
                .filter(employee -> employee.getId().equals(id))
                .findFirst();
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }


    public static Employee getAllEmployeeById(String id) {

        loadAllEmployeeList();

        Optional<Employee> optional = Database.ALLEMPLOYEELIST.stream()
                .filter(employee -> employee.getId().equals(id))
                .findFirst();
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    public static Employee getAdminList(String id) {

        loadAdminList();

        Optional<Employee> optional = Database.ADMIN_LIST.stream()
                .filter(employee -> employee.getId().equals(id))
                .findFirst();
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    public static Employee getEmployeeList(String id) {

        loadEmployeeList();

        Optional<Employee> optional = Database.EMPLOYEELIST.stream()
                .filter(employee -> employee.getId().equals(id))
                .findFirst();
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    public static Employee getSuplierList(String id) {

        loadEmployeeList();

        Optional<Employee> optional = Database.SUPLIERLIST.stream()
                .filter(employee -> employee.getId().equals(id))
                .findFirst();
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }


    public static void loadAllEmployeeList() {

        Connection connection = Database.getConnection();

        if (connection != null) {

            try (Statement statement = connection.createStatement()) {

                Database.ALLEMPLOYEELIST.clear();

                String query = "SELECT * FROM employee WHERE is_admin=false and is_employee=false and is_customer=false;";

                ResultSet resultSet = statement.executeQuery(query);

                while (resultSet.next()) {
                    String id = resultSet.getString("id");
                    String firstName = resultSet.getString("first_name");
                    String lastName = resultSet.getString("last_name");
                    String phoneNumber = resultSet.getString("phone_number");
                    String employeeStatus = resultSet.getString("employee_status");
                    boolean isSuplier = resultSet.getBoolean("is_customer");
                    boolean isAdmin = resultSet.getBoolean("is_admin");
                    boolean isEmployee = resultSet.getBoolean("is_employee");
                    Employee employee = new Employee(id, firstName, lastName, phoneNumber, employeeStatus, isSuplier, isAdmin, isEmployee);

                    Database.ALLEMPLOYEELIST.add(employee);
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void loadSuplierList() {

        Connection connection = Database.getConnection();

        if (connection != null) {

            try (Statement statement = connection.createStatement()) {

                Database.SUPLIERLIST.clear();

                String query = "SELECT * FROM employee WHERE is_customer=true and is_employee=false;";

                ResultSet resultSet = statement.executeQuery(query);

                while (resultSet.next()) {
                    String id = resultSet.getString("id");
                    String firstName = resultSet.getString("first_name");
                    String lastName = resultSet.getString("last_name");
                    String phoneNumber = resultSet.getString("phone_number");
                    String employeeStatus = resultSet.getString("employee_status");
                    boolean isSuplier = resultSet.getBoolean("is_customer");
                    boolean isAdmin = resultSet.getBoolean("is_admin");
                    boolean isEmployee = resultSet.getBoolean("is_employee");
                    Employee employee = new Employee(id, firstName, lastName, phoneNumber, employeeStatus, isSuplier, isAdmin, isEmployee);

                    Database.SUPLIERLIST.add(employee);
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void loadEmployeeList() {

        Connection connection = Database.getConnection();

        if (connection != null) {

            try (Statement statement = connection.createStatement()) {

                Database.EMPLOYEELIST.clear();

                String query = "SELECT * FROM employee WHERE is_employee=true and is_customer=false;";

                ResultSet resultSet = statement.executeQuery(query);

                while (resultSet.next()) {
                    String id = resultSet.getString("id");
                    String firstName = resultSet.getString("first_name");
                    String lastName = resultSet.getString("last_name");
                    String phoneNumber = resultSet.getString("phone_number");
                    String employeeStatus = resultSet.getString("employee_status");
                    boolean isSuplier = resultSet.getBoolean("is_customer");
                    boolean isAdmin = resultSet.getBoolean("is_admin");
                    boolean isEmployee = resultSet.getBoolean("is_employee");
                    Employee employee = new Employee(id, firstName, lastName, phoneNumber, employeeStatus, isSuplier, isAdmin, isEmployee);

                    Database.EMPLOYEELIST.add(employee);
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void loadAdminList() {

        Connection connection = Database.getConnection();

        if (connection != null) {

            try (Statement statement = connection.createStatement()) {

                Database.ADMIN_LIST.clear();

                String query = "SELECT * FROM employee WHERE is_admin=true;";

                ResultSet resultSet = statement.executeQuery(query);

                while (resultSet.next()) {
                    String id = resultSet.getString("id");
                    String firstName = resultSet.getString("first_name");
                    String lastName = resultSet.getString("last_name");
                    String phoneNumber = resultSet.getString("phone_number");
                    String employeeStatus = resultSet.getString("employee_status");
                    boolean isSuplier = resultSet.getBoolean("is_customer");
                    boolean isAdmin = resultSet.getBoolean("is_admin");
                    boolean isEmployee = resultSet.getBoolean("is_employee");

                    Employee employee = new Employee(id, firstName, lastName, phoneNumber, employeeStatus, isSuplier, isAdmin, isEmployee);

                    Database.ADMIN_LIST.add(employee);


                }
                connection.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }





    public static void addEmployee(Employee employee) {
        Connection connection = Database.getConnection();
        if (connection != null) {

            String query = " INSERT INTO employee(id, first_name, last_name, phone_number, employee_status, is_customer, is_admin, is_employee)" +
                    " VALUES(?, ?, ?, ?, ?, ?, ?, ?); ";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setString(1, employee.getId());
                preparedStatement.setString(2, employee.getFirstName());
                preparedStatement.setString(3, employee.getLastName());
                preparedStatement.setString(4, employee.getPhoneNumber());
                preparedStatement.setString(5, String.valueOf(employee.getEmployeeStatus()));
                preparedStatement.setBoolean(6, false);
                preparedStatement.setBoolean(7, false);
                preparedStatement.setBoolean(8, false);


                int executeUpdate = preparedStatement.executeUpdate();
                System.out.println(executeUpdate);

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        loadEmployeeList();
    }

    public static void changeEmployee(String employeeId) {
        Connection connection = Database.getConnection();
        if (connection != null) {

            String query = String.format("UPDATE employee SET is_employee=true, is_customer=false WHERE id='%s';",employeeId);
            try (Statement statement = connection.createStatement()) {
                int i = statement.executeUpdate(query);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void changeEmployee2(String employeeId) {
        Connection connection = Database.getConnection();
        if (connection != null) {

            String query = String.format("UPDATE employee SET is_employee=false, is_customer=false WHERE id='%s';",employeeId);
            try (Statement statement = connection.createStatement()) {
                int i = statement.executeUpdate(query);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void changeEmployeeToSuplier(String employeeId) {
        Connection connection = Database.getConnection();
        if (connection != null) {

            String query = String.format("UPDATE employee SET is_employee=false, is_customer=true WHERE id='%s';",employeeId);
            try (Statement statement = connection.createStatement()) {
                int i = statement.executeUpdate(query);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public static void changeSuplier(String employeeId) {
        Connection connection = Database.getConnection();
        if (connection != null) {

            String query = String.format("UPDATE employee SET is_employee=false, is_customer=true WHERE id='%s';",employeeId);
            try (Statement statement = connection.createStatement()) {
                int i = statement.executeUpdate(query);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void changeSuplier2(String employeeId) {
        Connection connection = Database.getConnection();
        if (connection != null) {

            String query = String.format("UPDATE employee SET is_employee=false, is_customer=false WHERE id='%s';",employeeId);
            try (Statement statement = connection.createStatement()) {
                int i = statement.executeUpdate(query);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public static void changeSuplierToEmployee(String employeeId) {
        Connection connection = Database.getConnection();
        if (connection != null) {

            String query = String.format("UPDATE employee SET is_employee=true, is_customer=false WHERE id='%s';",employeeId);
            try (Statement statement = connection.createStatement()) {
                int i = statement.executeUpdate(query);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


}
