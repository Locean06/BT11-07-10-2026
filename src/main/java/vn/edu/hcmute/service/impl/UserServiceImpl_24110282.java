package vn.edu.hcmute.service.impl;

import vn.edu.hcmute.dao.IUserDAO_24110282;
import vn.edu.hcmute.dao.impl.UserDAOImpl_24110282;
import vn.edu.hcmute.model.User_24110282;
import vn.edu.hcmute.service.IUserService_24110282;

public class UserServiceImpl_24110282 implements IUserService_24110282 {
    private final IUserDAO_24110282 userDAO = new UserDAOImpl_24110282();

    @Override
    public User_24110282 login(String email, String password) {
        User_24110282 user = userDAO.findByEmail(email);

        if (user != null && user.getPasswd().equals(password)) {
            userDAO.updateLastLogin(user.getId());
            return userDAO.findByEmail(email);
        }

        return null;
    }

    @Override
    public boolean register(User_24110282 user) {
        if (existsByEmail(user.getEmail())) {
            return false;
        }

        return userDAO.insert(user);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userDAO.existsByEmail(email);
    }
}
