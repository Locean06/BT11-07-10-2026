package vn.edu.hcmute.service;

import vn.edu.hcmute.model.User_24110282;

public interface IUserService_24110282 {
    User_24110282 login(String email, String password);
    boolean register(User_24110282 user);
    boolean existsByEmail(String email);
}
