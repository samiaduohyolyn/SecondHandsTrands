package model;

import java.util.Date;

public class Transaction {
    private int transactionId;
    private int buyerId;
    private int sellerId;
    private int productId;
    private Date transactionTime;
    private double amount;
    private String status;
    
    // 构造方法、getter和setter方法
    public Transaction() {}
    
    // Getter和Setter方法
    public int getTransactionId() { return transactionId; }
    public void setTransactionId(int transactionId) { this.transactionId = transactionId; }
    
    public int getBuyerId() { return buyerId; }
    public void setBuyerId(int buyerId) { this.buyerId = buyerId; }
    
    public int getSellerId() { return sellerId; }
    public void setSellerId(int sellerId) { this.sellerId = sellerId; }
    
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
    
    public Date getTransactionTime() { return transactionTime; }
    public void setTransactionTime(Date transactionTime) { this.transactionTime = transactionTime; }
    
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
