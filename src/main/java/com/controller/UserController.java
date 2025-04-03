package com.controller;

import com.controller.model.UserModel;
import com.domain.entity.User;
import com.domain.repository.UserRepository;
import com.domain.repository.WalletRepository;
import com.domain.usecase.UserUseCase;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserController {

    private final UserUseCase userUseCase;

    public UserController(UserRepository userRepository, WalletRepository walletRepository) {
        userUseCase = new UserUseCase(walletRepository, userRepository);
    }

    @PostMapping("/")
    public ResponseEntity<Void> create(@RequestBody UserModel userModel) {
        userUseCase.create(new User(0L, userModel.name(), userModel.email()));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<User>> recover() {
        return ResponseEntity.status(HttpStatus.OK).body(userUseCase.recover());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> recover(@PathVariable("userId") Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(userUseCase.recover(1L));
    }
}
