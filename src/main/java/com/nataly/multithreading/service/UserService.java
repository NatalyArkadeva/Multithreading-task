package com.nataly.multithreading.service;

import com.nataly.multithreading.entity.User;
import com.nataly.multithreading.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    @Async
    public CompletableFuture<List<User>> saveUsers(MultipartFile file) throws Exception {
        List<User> users = parseCVSFile(file);
        log.info("saving list of users of size {}", users.size(), "" + Thread.currentThread().getName());
        users = repository.saveAll(users);
        return CompletableFuture.completedFuture(users);
    }

    @Async
    public CompletableFuture<List<User>> getAllUsers() {
        log.info("get list of user by "+ Thread.currentThread().getName());
        List<User> users = repository.findAll();
        return CompletableFuture.completedFuture(users);
    }

    private List<User> parseCVSFile(final MultipartFile file) throws Exception {
        final List<User> users = new ArrayList<>();
        try(final BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                final String[] data = line.split(",");
                final User user = new User();
                user.setFirstName(data[1]);
                user.setLastName(data[2]);
                user.setEmail(data[3]);
                user.setGender(data[4]);
                users.add(user);
            }
            return users;
        } catch (IOException e) {
            log.error("Failed to parse CSV file " + e);
            throw new Exception("Failed to parse CSV file {}", e);
        }
    }
}
