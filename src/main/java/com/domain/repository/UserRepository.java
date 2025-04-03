package com.domain.repository;

import com.domain.entity.User;
import java.util.List;

public interface UserRepository {
    void create(User user);

    List<User> recover();

    User recover(Long userId);
}
