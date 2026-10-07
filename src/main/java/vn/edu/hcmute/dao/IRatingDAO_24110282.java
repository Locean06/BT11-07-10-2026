package vn.edu.hcmute.dao;

import vn.edu.hcmute.model.Rating_24110282;

import java.util.List;

public interface IRatingDAO_24110282 {
    List<Rating_24110282> findByBookId(int bookId);
    int countByBookId(int bookId);
    boolean saveOrUpdate(Rating_24110282 rating);
}
