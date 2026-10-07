package vn.edu.hcmute.model;

import java.math.BigDecimal;

public class CartItem_24110282 {
    private Book_24110282 book;
    private int quantity;

    public CartItem_24110282() {}

    public CartItem_24110282(Book_24110282 book, int quantity) {
        this.book = book;
        this.quantity = quantity;
    }

    public Book_24110282 getBook() { return book; }
    public void setBook(Book_24110282 book) { this.book = book; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public BigDecimal getSubtotal() {
        if (book == null || book.getPrice() == null) return BigDecimal.ZERO;
        return book.getPrice().multiply(BigDecimal.valueOf(quantity));
    }
}
