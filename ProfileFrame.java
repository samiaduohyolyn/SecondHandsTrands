package view;

import model.User;
import dao.UserDAO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class ProfileFrame extends JFrame {
    private User currentUser;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField emailField;
    private JTextField phoneField;
    private JTextField campusField;
    private JLabel avatarLabel;

    public ProfileFrame(User user) {
        this.currentUser = user;
        setTitle("个人资料");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());

        // 创建表单面板
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // 用户名
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("用户名:"), gbc);

        usernameField = new JTextField(20);
        usernameField.setText(currentUser.getUsername());
        usernameField.setEditable(false);
        gbc.gridx = 1;
        gbc.gridy = 0;
        formPanel.add(usernameField, gbc);

        // 密码
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("密码:"), gbc);

        passwordField = new JPasswordField(20);
        passwordField.setText(currentUser.getPassword());
        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(passwordField, gbc);

        // 邮箱
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("邮箱:"), gbc);

        emailField = new JTextField(20);
        emailField.setText(currentUser.getEmail());
        gbc.gridx = 1;
        gbc.gridy = 2;
        formPanel.add(emailField, gbc);

        // 电话
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("电话:"), gbc);

        phoneField = new JTextField(20);
        phoneField.setText(currentUser.getPhone());
        gbc.gridx = 1;
        gbc.gridy = 3;
        formPanel.add(phoneField, gbc);

        // 校区
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("校区:"), gbc);

        campusField = new JTextField(20);
        campusField.setText(currentUser.getCampus());
        gbc.gridx = 1;
        gbc.gridy = 4;
        formPanel.add(campusField, gbc);

        // 头像
        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("头像:"), gbc);

        avatarLabel = new JLabel("无头像");
        if (currentUser.getAvatar() != null && !currentUser.getAvatar().isEmpty()) {
            try {
                ImageIcon icon = new ImageIcon(currentUser.getAvatar());
                Image image = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                avatarLabel.setIcon(new ImageIcon(image));
                avatarLabel.setText("");
            } catch (Exception e) {
                avatarLabel.setText("头像加载失败");
            }
        }

        JButton changeAvatarButton = new JButton("更改头像");
        JPanel avatarPanel = new JPanel(new BorderLayout());
        avatarPanel.add(avatarLabel, BorderLayout.CENTER);
        avatarPanel.add(changeAvatarButton, BorderLayout.SOUTH);

        gbc.gridx = 1;
        gbc.gridy = 5;
        formPanel.add(avatarPanel, gbc);

        // 按钮面板
        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("保存");
        JButton cancelButton = new JButton("取消");

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        add(panel);

        // 保存按钮事件
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveProfile();
            }
        });

        // 取消按钮事件
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        // 更改头像按钮事件
        changeAvatarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
                    @Override
                    public boolean accept(File f) {
                        return f.isDirectory() || f.getName().toLowerCase().endsWith(".jpg") ||
                                f.getName().toLowerCase().endsWith(".jpeg") ||
                                f.getName().toLowerCase().endsWith(".png") ||
                                f.getName().toLowerCase().endsWith(".gif");
                    }

                    @Override
                    public String getDescription() {
                        return "图片文件 (*.jpg, *.jpeg, *.png, *.gif)";
                    }
                });

                int result = fileChooser.showOpenDialog(ProfileFrame.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    currentUser.setAvatar(selectedFile.getAbsolutePath());

                    // 更新头像显示
                    try {
                        ImageIcon icon = new ImageIcon(currentUser.getAvatar());
                        Image image = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                        avatarLabel.setIcon(new ImageIcon(image));
                        avatarLabel.setText("");
                    } catch (Exception ex) {
                        avatarLabel.setText("头像加载失败");
                    }
                }
            }
        });
    }

    // 保存个人资料
    private void saveProfile() {
        String password = new String(passwordField.getPassword());
        String email = emailField.getText();
        String phone = phoneField.getText();
        String campus = campusField.getText();

        if (password.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "密码和邮箱不能为空");
            return;
        }

        currentUser.setPassword(password);
        currentUser.setEmail(email);
        currentUser.setPhone(phone);
        currentUser.setCampus(campus);

        UserDAO userDAO = new UserDAO();
        if (userDAO.updateUser(currentUser)) {
            JOptionPane.showMessageDialog(this, "个人资料更新成功");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "个人资料更新失败");
        }
    }
}
