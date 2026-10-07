package vn.edu.hcmute.service.impl;

import vn.edu.hcmute.dao.IRatingDAO_24110282;
import vn.edu.hcmute.dao.impl.RatingDAOImpl_24110282;
import vn.edu.hcmute.model.Rating_24110282;
import vn.edu.hcmute.service.IRatingService_24110282;

import java.util.List;

public class RatingServiceImpl_24110282 implements IRatingService_24110282 {
    private final IRatingDAO_24110282 ratingDAO = new RatingDAOImpl_24110282();

    @Override
    public List<Rating_24110282> getByBookId(int bookId) {
        return ratingDAO.findByBookId(bookId);
    }

    @Override
    public int countByBookId(int bookId) {
        return ratingDAO.countByBookId(bookId);
    }

    @Override
    public boolean saveOrUpdate(Rating_24110282 rating) {
        return ratingDAO.saveOrUpdate(rating);
    }
}
