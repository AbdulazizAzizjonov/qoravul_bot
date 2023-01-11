package com.company.service;

import com.company.database.Database;
import com.company.model.Product;

import java.sql.*;
import java.util.Optional;

public class ProductService {

    public static Product getProductById(Integer id) {
        loadProductList();

        Optional<Product> optional = Database.PRODUCTLIST.stream()
                .filter(product -> product.getId().equals(id))
                .findFirst();
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }


    public static void loadProductList() {

        Connection connection = Database.getConnection();

        if (connection != null) {

            try (Statement statement = connection.createStatement()) {

                Database.PRODUCTLIST.clear();

                String query = " SELECT * FROM product WHERE NOT is_deleted; ";

                ResultSet resultSet = statement.executeQuery(query);

                while (resultSet.next()) {
                    Integer id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    double price = resultSet.getDouble("price");
                    String image = resultSet.getString("image");
                    boolean deleted = resultSet.getBoolean("is_deleted");

                    Product product = new Product(id, name, price, image, deleted);

                    Database.PRODUCTLIST.add(product);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void addProduct(Product product) {

        Connection connection = Database.getConnection();

        if (connection != null) {

            String query = " INSERT INTO product(name, price, image, is_deleted)" +
                    " VALUES(?, ?, ?, ?); ";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setString(1, product.getName());
                preparedStatement.setDouble(2, product.getPrice());
                preparedStatement.setString(3, product.getImage());
                preparedStatement.setBoolean(4, false);

                int executeUpdate = preparedStatement.executeUpdate();
                System.out.println(executeUpdate);

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        loadProductList();
    }


    public static void updateProductName(Integer id, String text) {
        Connection connection = Database.getConnection();
        if (connection != null) {


            String query = " UPDATE product SET name = ? WHERE id = ? ;";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {


                preparedStatement.setString(1, text);
                preparedStatement.setInt(2, id);
                int executeUpdate = preparedStatement.executeUpdate();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public static void updateProductPrice(Integer id, double price) {
        Connection connection = Database.getConnection();
        if (connection != null) {


            String query = " UPDATE product SET price = ? WHERE id = ? ;";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setDouble(1, price);
                preparedStatement.setInt(2, id);
                int executeUpdate = preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void updateProductImage(Integer id, String image) {
        Connection connection = Database.getConnection();
        if (connection != null) {


            String query = " UPDATE product SET image = ? WHERE id = ? ;";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setString(1, image);
                preparedStatement.setInt(2, id);
                int executeUpdate = preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public static void deleteProduct(Integer productId) {
        Connection connection = Database.getConnection();
        if (connection != null) {

            String query = String.format("UPDATE product SET is_deleted=true WHERE id='%s';", productId);
            try (Statement statement = connection.createStatement()) {
                int i = statement.executeUpdate(query);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


//    public static Product getProductById(Integer productId) {
//        ProductService.loadProductList();
//        Optional<Product> optional = Database.PRODUCTLIST.stream()
//                .filter(product -> product.getId().equals(productId))
//                .findFirst();
//        return optional.orElse(null);
//    }


}
