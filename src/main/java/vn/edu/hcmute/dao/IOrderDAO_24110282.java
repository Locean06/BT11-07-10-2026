package vn.edu.hcmute.dao;

import vn.edu.hcmute.model.CartItem_24110282;
import java.util.Map;

public interface IOrderDAO_24110282 {
    int createCodOrder(int userId, String customerName, String phone, String address, String note,
                       Map<Integer, CartItem_24110282> cart) throws Exception;
}
