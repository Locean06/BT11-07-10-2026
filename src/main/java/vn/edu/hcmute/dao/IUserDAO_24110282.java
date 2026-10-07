package vn.edu.hcmute.dao;

import vn.edu.hcmute.model.User_24110282;

public interface IUserDAO_24110282 {
    User_24110282 findByEmail(String email);
    boolean existsByEmail(String email);
    boolean insert(User_24110282 user);
    boolean updateLastLogin(int userId);
}
