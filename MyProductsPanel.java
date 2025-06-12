package view;

import model.Product;
import dao.ProductDAO;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class MyProductsPanel extends JPanel {
    private User currentUser;
    private JTable productTable;
    private DefaultTableModel tableModel;
    private ProductDAO productDAO;

    // 定义列索引常量，提高代码可读性
    private static final int COLUMN_ID = 5; // ID列索引
    private static final int COLUMN_IMAGE = 0; // 图片列索引
    private static final int COLUMN_TITLE = 1; // 标题列索引
    private static final int COLUMN_PRICE = 2; // 价格列索引
    private static final int COLUMN_STATUS = 3; // 状态列索引
    private static final int COLUMN_TIME = 4; // 时间列索引

    public MyProductsPanel(User user) {
        this.currentUser = user;
        this.productDAO = new ProductDAO();
        setLayout(new BorderLayout());

        // 创建表格模型，明确列名
        String[] columnNames = {"图片", "标题", "价格", "状态", "创建时间", "ID"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            // 使表格不可编辑
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // 创建表格
        productTable = new JTable(tableModel);
        productTable.setRowHeight(80); // 调整行高以适应图片

        // 设置列宽
        productTable.getColumnModel().getColumn(COLUMN_IMAGE).setPreferredWidth(100); // 图片列
        productTable.getColumnModel().getColumn(COLUMN_TITLE).setPreferredWidth(200); // 标题列
        productTable.getColumnModel().getColumn(COLUMN_PRICE).setPreferredWidth(80);  // 价格列
        productTable.getColumnModel().getColumn(COLUMN_STATUS).setPreferredWidth(80);  // 状态列
        productTable.getColumnModel().getColumn(COLUMN_TIME).setPreferredWidth(150);  // 时间列

        // 设置图片列的单元格渲染器
        productTable.getColumn("图片").setCellRenderer(new ImageCellRenderer());

        JScrollPane scrollPane = new JScrollPane(productTable);
        add(scrollPane, BorderLayout.CENTER);

        // 创建按钮面板
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("添加商品");
        JButton editButton = new JButton("编辑商品");
        JButton deleteButton = new JButton("删除商品");
        JButton refreshButton = new JButton("刷新");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // 加载用户商品
        loadUserProducts();

        // 添加按钮事件
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddProductFrame(currentUser).setVisible(true);
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = productTable.getSelectedRow();
                if (selectedRow != -1) {
                    try {
                        // 获取商品ID（现在明确ID在第5列）
                        int productId = (int) tableModel.getValueAt(selectedRow, COLUMN_ID);
                        Product product = productDAO.getProductById(productId);
                        if (product != null) {
                            new EditProductFrame(currentUser, product).setVisible(true);
                        } else {
                            JOptionPane.showMessageDialog(MyProductsPanel.this, "未找到该商品，请刷新列表", "错误", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (ClassCastException ex) {
                        JOptionPane.showMessageDialog(MyProductsPanel.this, "获取商品ID时出现错误，请刷新列表", "错误", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(MyProductsPanel.this, "请先选择一个商品", "提示", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = productTable.getSelectedRow();
                if (selectedRow != -1) {
                    try {
                        // 获取商品ID（现在明确ID在第5列）
                        int productId = (int) tableModel.getValueAt(selectedRow, COLUMN_ID);
                        Product product = productDAO.getProductById(productId);
                        if (product != null) {
                            int confirm = JOptionPane.showConfirmDialog(MyProductsPanel.this,
                                    "确定要删除此商品吗？", "确认删除", JOptionPane.YES_NO_OPTION);
                            if (confirm == JOptionPane.YES_OPTION) {
                                if (productDAO.deleteProduct(productId)) {
                                    JOptionPane.showMessageDialog(MyProductsPanel.this, "商品删除成功", "成功", JOptionPane.INFORMATION_MESSAGE);
                                    loadUserProducts();
                                } else {
                                    JOptionPane.showMessageDialog(MyProductsPanel.this, "商品删除失败，请重试", "错误", JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        } else {
                            JOptionPane.showMessageDialog(MyProductsPanel.this, "未找到该商品，请刷新列表", "错误", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (ClassCastException ex) {
                        JOptionPane.showMessageDialog(MyProductsPanel.this, "获取商品ID时出现错误，请刷新列表", "错误", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(MyProductsPanel.this, "请先选择一个商品", "提示", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        // 刷新按钮事件
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadUserProducts();
                JOptionPane.showMessageDialog(MyProductsPanel.this, "商品列表已刷新", "提示", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    // 加载用户商品
    private void loadUserProducts() {
        // 清空表格
        tableModel.setRowCount(0);

        List<Product> products = productDAO.getUserProducts(currentUser.getUserId());
        for (Product product : products) {
            // 获取商品的第一张图片
            String imagePath = getFirstProductImage(product.getProductId());

            // 添加到表格，注意ID列是第6个元素（索引5）
            tableModel.addRow(new Object[]{
                    imagePath,              // 图片列（索引0）
                    product.getTitle(),     // 标题列（索引1）
                    "¥" + product.getPrice(), // 价格列（索引2）
                    product.getStatus(),    // 状态列（索引3）
                    product.getCreateTime(),// 时间列（索引4）
                    product.getProductId()  // ID列（索引5）
            });
        }
    }

    // 获取商品的第一张图片路径
    private String getFirstProductImage(int productId) {
        String imagePath = "";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = productDAO.getConnection();
            String sql = "SELECT image_path FROM product_images WHERE product_id = ? AND is_main = 1 LIMIT 1";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, productId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                imagePath = rs.getString("image_path");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (pstmt != null) pstmt.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }

        return imagePath;
    }

    // 图片单元格渲染器
    private class ImageCellRenderer extends JLabel implements TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            setText("");
            setOpaque(true);
            setHorizontalAlignment(CENTER);
            setVerticalAlignment(CENTER);

            if (value != null && !value.toString().isEmpty()) {
                try {
                    ImageIcon icon = new ImageIcon(value.toString());
                    if (icon.getIconWidth() > 80) {
                        // 调整图片大小以适应单元格
                        Image scaledImage = icon.getImage().getScaledInstance(
                                80, -1, Image.SCALE_SMOOTH);
                        setIcon(new ImageIcon(scaledImage));
                    } else {
                        setIcon(icon);
                    }
                } catch (Exception e) {
                    setText("图片加载失败");
                }
            } else {
                setText("无图片");
            }

            if (isSelected) {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            } else {
                setBackground(table.getBackground());
                setForeground(table.getForeground());
            }

            return this;
        }
    }

    // 刷新方法
    public void refreshProducts() {
        loadUserProducts();
        JOptionPane.showMessageDialog(this, "商品列表已刷新", "提示", JOptionPane.INFORMATION_MESSAGE);
    }
}