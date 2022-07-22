package com.soulcode.Servicos.Controllers;

import com.soulcode.Servicos.Models.User;
import com.soulcode.Servicos.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("servicos")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/usuarios")
    public List<User> listarUsers() {
        return userService.listarUsers();
    }

    @PostMapping("/usuarios")
    public ResponseEntity<User> cadastrarUser(@RequestBody User user) {
        String senhaCodificada = passwordEncoder.encode(user.getPassword());
        user.setPassword(senhaCodificada);
        user = userService.cadastrarUser(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
}
