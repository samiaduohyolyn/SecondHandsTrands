package view;

import model.User;
import model.Product;
import dao.ProductDAO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AddProductFrame extends JFrame {
    private User currentUser;
    private JTextField titleField;
    private JTextArea descriptionArea;
    private JTextField priceField;
    private JTextField categoryField;
    private JButton addImageButton;
    private JButton submitButton;
    private JButton cancelButton;
    private JPanel imagePanel;
    private List<String> imagePaths;
    
    public AddProductFrame(User user) {
        this.currentUser = user;
        this.imagePaths = new ArrayList<>();
        
        setTitle("发布商品");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // 创建面板
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // 添加标题
        JLabel titleLabel = new JLabel("商品标题:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(titleLabel, gbc);
        
        titleField = new JTextField(30);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleField, gbc);
        
        // 添加描述
        JLabel descriptionLabel = new JLabel("商品描述:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(descriptionLabel, gbc);
        
        descriptionArea = new JTextArea(5, 30);
        JScrollPane descriptionScrollPane = new JScrollPane(descriptionArea);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        panel.add(descriptionScrollPane, gbc);
        
        // 添加价格
        JLabel priceLabel = new JLabel("价格(元):");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        panel.add(priceLabel, gbc);
        
        priceField = new JTextField(10);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        panel.add(priceField, gbc);
        
        // 添加分类
        JLabel categoryLabel = new JLabel("分类:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        panel.add(categoryLabel, gbc);
        
        categoryField = new JTextField(10);
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        panel.add(categoryField, gbc);
        
        // 添加图片
        JLabel imageLabel = new JLabel("商品图片:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        panel.add(imageLabel, gbc);
        
        imagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(imagePanel, gbc);
        
        // 添加图片按钮
        addImageButton = new JButton("添加图片");
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(addImageButton, gbc);
        
        // 添加提交按钮
        submitButton = new JButton("提交");
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        panel.add(submitButton, gbc);
        
        // 添加取消按钮
        cancelButton = new JButton("取消");
        gbc.gridx = 2;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        panel.add(cancelButton, gbc);
        
        add(panel);
        
        // 添加图片按钮事件
        addImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setMultiSelectionEnabled(true);
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
                
                int result = fileChooser.showOpenDialog(AddProductFrame.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File[] selectedFiles = fileChooser.getSelectedFiles();
                    for (File file : selectedFiles) {
                        String path = file.getAbsolutePath();
                        imagePaths.add(path);
                        
                        // 添加图片预览
                        ImageIcon icon = new ImageIcon(path);
                        if (icon.getIconWidth() > 100) {
                            icon = new ImageIcon(icon.getImage().getScaledInstance(
                                    100, -1, Image.SCALE_SMOOTH));
                        }
                        
                        JLabel imageLabel = new JLabel(icon);
                        imagePanel.add(imageLabel);
                    }
                    
                    imagePanel.revalidate();
                    imagePanel.repaint();
                }
            }
        });
        
        // 添加提交按钮事件
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = titleField.getText();
                String description = descriptionArea.getText();
                String priceText = priceField.getText();
                String category = categoryField.getText();
                
                // 验证输入
                if (title.isEmpty() || description.isEmpty() || priceText.isEmpty() || category.isEmpty()) {
                    JOptionPane.showMessageDialog(AddProductFrame.this, "所有字段都必须填写！", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                double price;
                try {
                    price = Double.parseDouble(priceText);
                    if (price <= 0) {
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(AddProductFrame.this, "请输入有效的价格！", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (imagePaths.isEmpty()) {
                    JOptionPane.showMessageDialog(AddProductFrame.this, "请至少上传一张商品图片！", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // 创建商品
                Product product = new Product();
                product.setUserId(currentUser.getUserId());
                product.setTitle(title);
                product.setDescription(description);
                product.setPrice(price);
                product.setCategory(category);
                product.setStatus("在售");
                
                // 保存商品
                ProductDAO productDAO = new ProductDAO();
                boolean success = productDAO.addProduct(product);
                
                if (success) {
                    // 保存图片
                    boolean isMain = true;
                    for (String imagePath : imagePaths) {
                        // 这里应该实现图片上传逻辑，将图片保存到服务器或本地目录
                        // 简化处理，直接保存路径
                        productDAO.addProductImage(product.getProductId(), imagePath, isMain);
                        isMain = false;
                    }
                    
                    JOptionPane.showMessageDialog(AddProductFrame.this, "商品发布成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(AddProductFrame.this, "商品发布失败，请重试！", "错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        // 添加取消按钮事件
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
}
