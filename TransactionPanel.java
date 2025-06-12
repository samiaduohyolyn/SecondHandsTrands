package view;

import model.User;
import model.Transaction;
import dao.TransactionDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class TransactionPanel extends JPanel {
    private User currentUser;
    private JTabbedPane transactionTabbedPane;
    private JTable purchaseTable;
    private JTable salesTable;
    private DefaultTableModel purchaseTableModel;
    private DefaultTableModel salesTableModel;

    public TransactionPanel(User user) {
        this.currentUser = user;
        setLayout(new BorderLayout());

        // 创建选项卡面板
        transactionTabbedPane = new JTabbedPane();

        // 创建购买记录表格模型
        String[] purchaseColumnNames = {"商品ID", "金额", "状态", "交易时间"};
        purchaseTableModel = new DefaultTableModel(purchaseColumnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        purchaseTable = new JTable(purchaseTableModel);
        JScrollPane purchaseScrollPane = new JScrollPane(purchaseTable);

        // 创建销售记录表格模型
        String[] salesColumnNames = {"商品ID", "金额", "状态", "交易时间"};
        salesTableModel = new DefaultTableModel(salesColumnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        salesTable = new JTable(salesTableModel);
        JScrollPane salesScrollPane = new JScrollPane(salesTable);

        // 添加选项卡
        transactionTabbedPane.addTab("购买记录", purchaseScrollPane);
        transactionTabbedPane.addTab("销售记录", salesScrollPane);

        add(transactionTabbedPane, BorderLayout.CENTER);

        // 加载交易记录
        loadPurchaseHistory();
        loadSalesHistory();

        // 添加刷新按钮
        JButton refreshButton = new JButton("刷新");
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadPurchaseHistory();
                loadSalesHistory();
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(refreshButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // 加载购买记录
    private void loadPurchaseHistory() {
        purchaseTableModel.setRowCount(0);
        TransactionDAO transactionDAO = new TransactionDAO();
        List<Transaction> transactions = transactionDAO.getUserPurchaseHistory(currentUser.getUserId());

        for (Transaction transaction : transactions) {
            purchaseTableModel.addRow(new Object[]{
                    transaction.getProductId(),
                    "¥" + transaction.getAmount(),
                    transaction.getStatus(),
                    transaction.getTransactionTime()
            });
        }
    }

    // 加载销售记录
    private void loadSalesHistory() {
        salesTableModel.setRowCount(0);
        TransactionDAO transactionDAO = new TransactionDAO();
        List<Transaction> transactions = transactionDAO.getUserSalesHistory(currentUser.getUserId());

        for (Transaction transaction : transactions) {
            salesTableModel.addRow(new Object[]{
                    transaction.getProductId(),
                    "¥" + transaction.getAmount(),
                    transaction.getStatus(),
                    transaction.getTransactionTime()
            });
        }
    }
}