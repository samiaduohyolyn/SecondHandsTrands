package view;

import model.User;
import model.Product;
import dao.ProductDAO;
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

public class ProductListPanel extends JPanel {
    private User currentUser;
    private JTable productTable;
    private DefaultTableModel tableModel;
    private JButton buyButton;

    // 定义列索引常量，提高代码可读性
    private static final int COLUMN_IMAGE = 0; // 图片列索引
    private static final int COLUMN_TITLE = 1; // 标题列索引
    private static final int COLUMN_PRICE = 2; // 价格列索引
    private static final int COLUMN_STATUS = 3; // 状态列索引
    private static final int COLUMN_TIME = 4; // 时间列索引
    private static final int COLUMN_ID = 5; // ID列索引

    public ProductListPanel(User user) {
        this.currentUser = user;
        setLayout(new BorderLayout());

        // 创建顶部面板
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton refreshButton = new JButton("刷新");
        JButton addProductButton = new JButton("发布商品");

        topPanel.add(refreshButton);
        topPanel.add(addProductButton);

        add(topPanel, BorderLayout.NORTH);

        // 创建表格模型
        String[] columnNames = {"图片", "标题", "价格", "状态", "创建时间", "ID"};
        tableModel = new DefaultTableModel(columnNames, 0) {
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

        // 创建底部面板
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buyButton = new JButton("购买");
        buyButton.setEnabled(false);

        bottomPanel.add(buyButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // 加载商品列表
        loadProducts();

        // 添加刷新按钮事件
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadProducts();
            }
        });

        // 添加发布商品按钮事件
        addProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddProductFrame(currentUser).setVisible(true);
            }
        });

        // 添加商品列表选择事件
        productTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                buyButton.setEnabled(productTable.getSelectedRow() != -1);
            }
        });

        // 添加购买按钮事件
        buyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = productTable.getSelectedRow();
                if (selectedRow != -1) {
                    int productId = (int) tableModel.getValueAt(selectedRow, COLUMN_ID);
                    Product product = new ProductDAO().getProductById(productId);
                    if (product != null) {
                        // 检查是否是自己的商品
                        if (product.getUserId() == currentUser.getUserId()) {
                            JOptionPane.showMessageDialog(ProductListPanel.this,
                                    "不能购买自己的商品！", "错误", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        new BuyProductFrame(currentUser, product).setVisible(true);
                    }
                }
            }
        });
    }

    // 加载商品列表
    private void loadProducts() {
        tableModel.setRowCount(0);
        ProductDAO productDAO = new ProductDAO();
        List<Product> products = productDAO.getAllProducts();

        for (Product product : products) {
            // 获取商品的第一张图片
            String imagePath = getFirstProductImage(product.getProductId());

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
        ProductDAO productDAO = new ProductDAO();

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
}