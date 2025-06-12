package view;

import dao.UserDAO;
import model.Product;
import model.Transaction;
import dao.TransactionDAO;
import dao.ProductDAO;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

public class BuyProductFrame extends JFrame {
    private User currentUser;
    private Product product;
    private JTextField priceField;
    private JTextArea addressArea;

    public BuyProductFrame(User user, Product product) {
        this.currentUser = user;
        this.product = product;
        setTitle("购买商品");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());

        // 商品信息面板
        JPanel infoPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        infoPanel.setBorder(BorderFactory.createTitledBorder("商品信息"));

        infoPanel.add(new JLabel("商品名称:"));
        infoPanel.add(new JLabel(product.getTitle()));

        infoPanel.add(new JLabel("商品价格:"));
        priceField = new JTextField(String.valueOf(product.getPrice()));
        priceField.setEditable(false);
        infoPanel.add(priceField);

        infoPanel.add(new JLabel("卖家:"));
        UserDAO userDAO = new UserDAO();
        User seller = userDAO.getUserById(product.getUserId());
        infoPanel.add(new JLabel(seller != null ? seller.getUsername() : "未知"));

        // 收货地址面板
        JPanel addressPanel = new JPanel(new BorderLayout());
        addressPanel.setBorder(BorderFactory.createTitledBorder("收货地址"));

        addressArea = new JTextArea(5, 20);
        addressArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(addressArea);
        addressPanel.add(scrollPane, BorderLayout.CENTER);

        // 按钮面板
        JPanel buttonPanel = new JPanel();
        JButton buyButton = new JButton("确认购买");
        JButton cancelButton = new JButton("取消");

        buttonPanel.add(buyButton);
        buttonPanel.add(cancelButton);

        panel.add(infoPanel, BorderLayout.NORTH);
        panel.add(addressPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        add(panel);

        // 购买按钮事件
        buyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String address = addressArea.getText().trim();
                if (address.isEmpty()) {
                    JOptionPane.showMessageDialog(BuyProductFrame.this, "请输入收货地址");
                    return;
                }

                // 创建交易记录
                Transaction transaction = new Transaction();
                transaction.setBuyerId(currentUser.getUserId());
                transaction.setSellerId(product.getUserId());
                transaction.setProductId(product.getProductId());
                transaction.setAmount(product.getPrice());
                transaction.setStatus("待付款");
                transaction.setTransactionTime(new Date());

                TransactionDAO transactionDAO = new TransactionDAO();
                if (transactionDAO.createTransaction(transaction)) {
                    // 更新商品状态
                    ProductDAO productDAO = new ProductDAO();
                    product.setStatus("已售出");
                    if (productDAO.updateProduct(product)) {
                        JOptionPane.showMessageDialog(BuyProductFrame.this, "购买成功，请尽快完成付款");
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(BuyProductFrame.this, "商品状态更新失败");
                    }
                } else {
                    JOptionPane.showMessageDialog(BuyProductFrame.this, "交易创建失败");
                }
            }
        });

        // 取消按钮事件
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
}
