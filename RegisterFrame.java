package view;

import model.User;
import dao.UserDAO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField emailField;
    private JTextField phoneField;
    private JTextField campusField;
    private JButton registerButton;
    private JButton cancelButton;
    
    public RegisterFrame() {
        setTitle("校园二手交易平台 - 注册");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // 创建面板
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // 添加标题
        JLabel titleLabel = new JLabel("用户注册");
        titleLabel.setFont(new Font("宋体", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);
        
        // 添加用户名输入框
        JLabel usernameLabel = new JLabel("用户名:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(usernameLabel, gbc);
        
        usernameField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(usernameField, gbc);
        
        // 添加密码输入框
        JLabel passwordLabel = new JLabel("密码:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(passwordLabel, gbc);
        
        passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(passwordField, gbc);
        
        // 添加确认密码输入框
        JLabel confirmPasswordLabel = new JLabel("确认密码:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(confirmPasswordLabel, gbc);
        
        confirmPasswordField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(confirmPasswordField, gbc);
        
        // 添加邮箱输入框
        JLabel emailLabel = new JLabel("邮箱:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(emailLabel, gbc);
        
        emailField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 4;
        panel.add(emailField, gbc);
        
        // 添加电话输入框
        JLabel phoneLabel = new JLabel("电话:");
        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(phoneLabel, gbc);
        
        phoneField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 5;
        panel.add(phoneField, gbc);
        
        // 添加校区输入框
        JLabel campusLabel = new JLabel("校区:");
        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(campusLabel, gbc);
        
        campusField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 6;
        panel.add(campusField, gbc);
        
        // 添加注册按钮
        registerButton = new JButton("注册");
        gbc.gridx = 0;
        gbc.gridy = 7;
        panel.add(registerButton, gbc);
        
        // 添加取消按钮
        cancelButton = new JButton("取消");
        gbc.gridx = 1;
        gbc.gridy = 7;
        panel.add(cancelButton, gbc);
        
        add(panel);
        
        // 添加注册按钮事件
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String confirmPassword = new String(confirmPasswordField.getPassword());
                String email = emailField.getText();
                String phone = phoneField.getText();
                String campus = campusField.getText();
                
                // 验证输入
                if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
                    JOptionPane.showMessageDialog(RegisterFrame.this, "用户名、密码和邮箱不能为空！", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (!password.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(RegisterFrame.this, "两次输入的密码不一致！", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                User user = new User(username, password, email);
                user.setPhone(phone);
                user.setCampus(campus);
                
                UserDAO userDAO = new UserDAO();
                boolean success = userDAO.registerUser(user);
                
                if (success) {
                    JOptionPane.showMessageDialog(RegisterFrame.this, "注册成功！请登录", "成功", JOptionPane.INFORMATION_MESSAGE);
                    new LoginFrame().setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(RegisterFrame.this, "注册失败，请重试", "错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        // 添加取消按钮事件
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LoginFrame().setVisible(true);
                dispose();
            }
        });
    }
}
