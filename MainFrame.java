package view;

import model.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {
    private User currentUser;
    private JTabbedPane tabbedPane;
    private MyProductsPanel myProductsPanel;

    public MainFrame(User user) {
        this.currentUser = user;
        setTitle("校园二手交易平台 - " + user.getUsername());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 创建菜单栏
        JMenuBar menuBar = new JMenuBar();

        JMenu userMenu = new JMenu("用户");
        JMenuItem profileItem = new JMenuItem("个人信息");
        JMenuItem logoutItem = new JMenuItem("退出登录");

        userMenu.add(profileItem);
        userMenu.addSeparator();
        userMenu.add(logoutItem);

        menuBar.add(userMenu);
        setJMenuBar(menuBar);

        // 创建选项卡面板
        tabbedPane = new JTabbedPane();

        // 添加商品列表选项卡
        JPanel productListPanel = new ProductListPanel(currentUser);
        tabbedPane.addTab("商品列表", productListPanel);

        // 添加我的商品选项卡
        myProductsPanel = new MyProductsPanel(currentUser);
        tabbedPane.addTab("我的商品", myProductsPanel);

        // 添加交易记录选项卡
        JPanel transactionPanel = new TransactionPanel(currentUser);
        tabbedPane.addTab("交易记录", transactionPanel);

        add(tabbedPane);

        // 添加选项卡选择事件监听器
//        tabbedPane.addChangeListener(e -> {
//            int selectedIndex = tabbedPane.getSelectedIndex();
//            // 检查是否切换到"我的商品"选项卡
//            if (selectedIndex == 1) { // 索引1对应"我的商品"选项卡
//                // 刷新我的商品列表
//                myProductsPanel.refreshProducts();
//            }
//            if (selectedIndex == 2) {
//                ((TransactionPanel) transactionPanel).refreshTransactions();
//            }
//        });

        // 添加个人信息菜单项事件
        profileItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ProfileFrame(currentUser).setVisible(true);
            }
        });

        // 添加退出登录菜单项事件
        logoutItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int option = JOptionPane.showConfirmDialog(MainFrame.this,
                        "确定要退出登录吗？", "确认", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    new LoginFrame().setVisible(true);
                    dispose();
                }
            }
        });
    }
}
