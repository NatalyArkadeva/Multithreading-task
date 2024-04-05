package com.nataly.multithreading.controller;

import com.nataly.multithreading.entity.User;
import com.nataly.multithreading.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PostMapping(value = "/users", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity saveUsers(@RequestParam(value = "files") MultipartFile[] files) throws Exception {
        for (MultipartFile file : files) {
            service.saveUsers(file);
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping(value = "/users")
    public CompletableFuture<ResponseEntity> findAllUsers() {
        return  service.getAllUsers().thenApply(ResponseEntity::ok);
    }

    @GetMapping(value = "/getUsersByThread")
    public  ResponseEntity getUsers(){
        CompletableFuture<List<User>> users1=service.getAllUsers();
        CompletableFuture<List<User>> users2=service.getAllUsers();
        CompletableFuture<List<User>> users3=service.getAllUsers();
        CompletableFuture.allOf(users1,users2,users3).join();
        return  ResponseEntity.status(HttpStatus.OK).build();
    }
}
