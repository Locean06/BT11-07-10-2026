package vn.edu.hcmute.service;

import vn.edu.hcmute.model.Rating_24110282;

import java.util.List;

public interface IRatingService_24110282 {
    List<Rating_24110282> getByBookId(int bookId);
    int countByBookId(int bookId);
    boolean saveOrUpdate(Rating_24110282 rating);
}
