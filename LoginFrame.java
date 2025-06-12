package view;

import model.User;
import dao.UserDAO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    
    public LoginFrame() {
        setTitle("校园二手交易平台 - 登录");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // 创建面板
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // 添加标题
        JLabel titleLabel = new JLabel("校园二手交易平台");
        titleLabel.setFont(new Font("宋体", Font.BOLD, 20));
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
        
        // 添加登录按钮
        loginButton = new JButton("登录");
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(loginButton, gbc);
        
        // 添加注册按钮
        registerButton = new JButton("注册");
        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(registerButton, gbc);
        
        add(panel);
        
        // 添加登录按钮事件
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                
                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(LoginFrame.this, "用户名和密码不能为空！", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                UserDAO userDAO = new UserDAO();
                User user = userDAO.login(username, password);
                
                if (user != null) {
                    JOptionPane.showMessageDialog(LoginFrame.this, "登录成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                    new MainFrame(user).setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(LoginFrame.this, "用户名或密码错误！", "错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        // 添加注册按钮事件
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RegisterFrame().setVisible(true);
                dispose();
            }
        });
    }
}
