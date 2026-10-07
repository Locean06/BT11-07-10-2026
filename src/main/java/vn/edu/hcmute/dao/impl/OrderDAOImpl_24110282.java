package vn.edu.hcmute.dao.impl;

import vn.edu.hcmute.config.DBConnection_24110282;
import vn.edu.hcmute.dao.IOrderDAO_24110282;
import vn.edu.hcmute.model.CartItem_24110282;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;

public class OrderDAOImpl_24110282 implements IOrderDAO_24110282 {

    @Override
    public int createCodOrder(int userId, String customerName, String phone, String address, String note,
                              Map<Integer, CartItem_24110282> cart) throws Exception {
        if (cart == null || cart.isEmpty()) {
            throw new IllegalArgumentException("Giỏ hàng đang trống.");
        }

        String lockBookSql = "SELECT price, quantity FROM books WITH (UPDLOCK, ROWLOCK) WHERE bookid = ?";
        String insertOrderSql = """
                INSERT INTO orders(userid, customer_name, phone, shipping_address, note,
                                   payment_method, payment_status, order_status, total_amount, created_at)
                VALUES (?, ?, ?, ?, ?, 'COD', 'UNPAID', 'PENDING', ?, GETDATE())
                """;
        String insertItemSql = """
                INSERT INTO order_items(order_id, bookid, quantity, unit_price, subtotal)
                VALUES (?, ?, ?, ?, ?)
                """;
        String updateStockSql = "UPDATE books SET quantity = quantity - ? WHERE bookid = ?";

        try (Connection conn = DBConnection_24110282.getConnection()) {
            conn.setAutoCommit(false);
            try {
                BigDecimal total = BigDecimal.ZERO;

                // Re-check price and stock inside the same transaction.
                for (CartItem_24110282 item : cart.values()) {
                    try (PreparedStatement ps = conn.prepareStatement(lockBookSql)) {
                        ps.setInt(1, item.getBook().getBookId());
                        try (ResultSet rs = ps.executeQuery()) {
                            if (!rs.next()) {
                                throw new IllegalStateException("Sách không còn tồn tại: " + item.getBook().getTitle());
                            }
                            int stock = rs.getInt("quantity");
                            if (item.getQuantity() < 1 || item.getQuantity() > stock) {
                                throw new IllegalStateException(
                                        "Số lượng sách '" + item.getBook().getTitle() + "' không hợp lệ. Tồn kho hiện tại: " + stock);
                            }
                            BigDecimal currentPrice = rs.getBigDecimal("price");
                            item.getBook().setPrice(currentPrice);
                            item.getBook().setQuantity(stock);
                            total = total.add(currentPrice.multiply(BigDecimal.valueOf(item.getQuantity())));
                        }
                    }
                }

                int orderId;
                try (PreparedStatement ps = conn.prepareStatement(insertOrderSql, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setInt(1, userId);
                    ps.setString(2, customerName);
                    ps.setString(3, phone);
                    ps.setString(4, address);
                    ps.setString(5, note);
                    ps.setBigDecimal(6, total);
                    ps.executeUpdate();
                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        if (!keys.next()) throw new IllegalStateException("Không tạo được đơn hàng.");
                        orderId = keys.getInt(1);
                    }
                }

                for (CartItem_24110282 item : cart.values()) {
                    BigDecimal unitPrice = item.getBook().getPrice();
                    BigDecimal subtotal = unitPrice.multiply(BigDecimal.valueOf(item.getQuantity()));

                    try (PreparedStatement ps = conn.prepareStatement(insertItemSql)) {
                        ps.setInt(1, orderId);
                        ps.setInt(2, item.getBook().getBookId());
                        ps.setInt(3, item.getQuantity());
                        ps.setBigDecimal(4, unitPrice);
                        ps.setBigDecimal(5, subtotal);
                        ps.executeUpdate();
                    }

                    try (PreparedStatement ps = conn.prepareStatement(updateStockSql)) {
                        ps.setInt(1, item.getQuantity());
                        ps.setInt(2, item.getBook().getBookId());
                        ps.executeUpdate();
                    }
                }

                conn.commit();
                return orderId;
            } catch (Exception e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }
}
