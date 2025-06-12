package dao;

import database.DBConnection;
import model.Product;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    // 发布商品
    public boolean addProduct(Product product) {
        String sql = "INSERT INTO products (user_id, title, description, price, category) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, product.getUserId());
            pstmt.setString(2, product.getTitle());
            pstmt.setString(3, product.getDescription());
            pstmt.setDouble(4, product.getPrice());
            pstmt.setString(5, product.getCategory());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        product.setProductId(generatedKeys.getInt(1));
                        return true;
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // 添加商品图片
    public boolean addProductImage(int productId, String imagePath, boolean isMain) {
        String sql = "INSERT INTO product_images (product_id, image_path, is_main) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, productId);
            pstmt.setString(2, imagePath);
            pstmt.setInt(3, isMain ? 1 : 0);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 获取用户发布的商品列表
    public List<Product> getUserProducts(int userId) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE user_id = ? ORDER BY create_time DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Product product = new Product();
                    product.setProductId(rs.getInt("product_id"));
                    product.setUserId(rs.getInt("user_id"));
                    product.setTitle(rs.getString("title"));
                    product.setDescription(rs.getString("description"));
                    product.setPrice(rs.getDouble("price"));
                    product.setCategory(rs.getString("category"));
                    product.setStatus(rs.getString("status"));
                    product.setCreateTime(rs.getTimestamp("create_time"));
                    products.add(product);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }

    // 获取所有在售商品
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE status = '在售' ORDER BY create_time DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Product product = new Product();
                    product.setProductId(rs.getInt("product_id"));
                    product.setUserId(rs.getInt("user_id"));
                    product.setTitle(rs.getString("title"));
                    product.setDescription(rs.getString("description"));
                    product.setPrice(rs.getDouble("price"));
                    product.setCategory(rs.getString("category"));
                    product.setStatus(rs.getString("status"));
                    product.setCreateTime(rs.getTimestamp("create_time"));
                    products.add(product);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }

    // 更新商品信息
    public boolean updateProduct(Product product) {
        String sql = "UPDATE products SET title = ?, description = ?, price = ?, category = ?, status = ? WHERE product_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, product.getTitle());
            pstmt.setString(2, product.getDescription());
            pstmt.setDouble(3, product.getPrice());
            pstmt.setString(4, product.getCategory());
            pstmt.setString(5, product.getStatus());
            pstmt.setInt(6, product.getProductId());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 删除商品
    public boolean deleteProduct(int productId) {
        // 首先删除相关的商品图片
        String deleteImagesSql = "DELETE FROM product_images WHERE product_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmtImages = conn.prepareStatement(deleteImagesSql)) {

            pstmtImages.setInt(1, productId);
            pstmtImages.executeUpdate();

            // 然后删除商品
            String deleteProductSql = "DELETE FROM products WHERE product_id = ?";
            try (PreparedStatement pstmtProduct = conn.prepareStatement(deleteProductSql)) {
                pstmtProduct.setInt(1, productId);
                int affectedRows = pstmtProduct.executeUpdate();
                return affectedRows > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 根据ID获取商品
    public Product getProductById(int productId) {
        String sql = "SELECT * FROM products WHERE product_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, productId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Product product = new Product();
                    product.setProductId(rs.getInt("product_id"));
                    product.setUserId(rs.getInt("user_id"));
                    product.setTitle(rs.getString("title"));
                    product.setDescription(rs.getString("description"));
                    product.setPrice(rs.getDouble("price"));
                    product.setCategory(rs.getString("category"));
                    product.setStatus(rs.getString("status"));
                    product.setCreateTime(rs.getTimestamp("create_time"));
                    return product;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // 在ProductDAO类中添加此方法
    public Connection getConnection() throws SQLException {
        return DBConnection.getConnection();
    }
}
