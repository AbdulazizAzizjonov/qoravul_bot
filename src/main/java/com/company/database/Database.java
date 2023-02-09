package com.company.database;

import com.company.model.Employee;
import com.company.model.Product;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;

public interface Database {
    List<Employee> ALLEMPLOYEELIST = new ArrayList<>();
    List<Employee> EMPLOYEELIST = new ArrayList<>();
    List<Employee> SUPLIERLIST = new ArrayList<>();
    List<Product> PRODUCTLIST = new ArrayList<>();
    List<Employee> ADMIN_LIST = new ArrayList<>();

    List<Employee> REPLAY_MESSAGE = new ArrayList<>();



    static Connection getConnection() {

        final String DB_USERNAME = "";
        final String DB_PASSWORD = "";
        final String DB_URL = "jdbc:postgresql:;

        Connection conn = null;
        try {

            Class.forName("org.postgresql.Driver");

            conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            if (conn != null) {
                System.out.println("Connection worked");
            } else {
                System.out.println("Connection failed");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return conn;
    }
}
