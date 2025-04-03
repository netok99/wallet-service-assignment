package com.domain.usecase;

import com.domain.entity.User;
import com.domain.repository.UserRepository;
import com.domain.repository.WalletRepository;
import java.util.List;

public class UserUseCase {

    private final UserRepository userRepository;

    public UserUseCase(WalletRepository walletRepository, UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void create(User user) {
        userRepository.create(user);
    }

    public List<User> recover() {
        return userRepository.recover();
    }

    public User recover(Long userId) {
        return userRepository.recover(userId);
    }
}
