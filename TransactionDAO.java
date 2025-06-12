package dao;

import database.DBConnection;
import model.Transaction;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {
    // 创建交易记录
    public boolean createTransaction(Transaction transaction) {
        String sql = "INSERT INTO transactions (buyer_id, seller_id, product_id, amount) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, transaction.getBuyerId());
            pstmt.setInt(2, transaction.getSellerId());
            pstmt.setInt(3, transaction.getProductId());
            pstmt.setDouble(4, transaction.getAmount());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        transaction.setTransactionId(generatedKeys.getInt(1));
                        return true;
                    }
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    // 更新交易状态
    public boolean updateTransactionStatus(int transactionId, String status) {
        String sql = "UPDATE transactions SET status = ? WHERE transaction_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status);
            pstmt.setInt(2, transactionId);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // 获取用户的购买记录
    public List<Transaction> getUserPurchaseHistory(int userId) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE buyer_id = ? ORDER BY transaction_time DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Transaction transaction = new Transaction();
                    transaction.setTransactionId(rs.getInt("transaction_id"));
                    transaction.setBuyerId(rs.getInt("buyer_id"));
                    transaction.setSellerId(rs.getInt("seller_id"));
                    transaction.setProductId(rs.getInt("product_id"));
                    transaction.setTransactionTime(rs.getTimestamp("transaction_time"));
                    transaction.setAmount(rs.getDouble("amount"));
                    transaction.setStatus(rs.getString("status"));
                    transactions.add(transaction);
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return transactions;
    }
    
    // 获取用户的销售记录
    public List<Transaction> getUserSalesHistory(int userId) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE seller_id = ? ORDER BY transaction_time DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Transaction transaction = new Transaction();
                    transaction.setTransactionId(rs.getInt("transaction_id"));
                    transaction.setBuyerId(rs.getInt("buyer_id"));
                    transaction.setSellerId(rs.getInt("seller_id"));
                    transaction.setProductId(rs.getInt("product_id"));
                    transaction.setTransactionTime(rs.getTimestamp("transaction_time"));
                    transaction.setAmount(rs.getDouble("amount"));
                    transaction.setStatus(rs.getString("status"));
                    transactions.add(transaction);
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return transactions;
    }
}
