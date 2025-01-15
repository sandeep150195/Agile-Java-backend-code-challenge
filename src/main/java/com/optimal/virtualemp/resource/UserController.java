package com.optimal.virtualemp.resource;

import com.optimal.virtualemp.model.User;
import com.optimal.virtualemp.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.optimal.virtualemp.exception.UserNotFoundException;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        log.info("Fetching all users");
        return userService.getAllUsers();
    }

    @GetMapping("/{username}")
    public User getUser(@PathVariable String username) {
        log.info("Fetching user with username: {}", username);
        return userService.getUser(username).orElseThrow(() -> {
            log.error("User with username {} not found", username);
            return new UserNotFoundException("User with username " + username + " not found");
        });
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        log.info("Creating new user: {}", user.getUsername());
        return userService.createUser(user);
    }

    @PutMapping("/{username}")
    public User updateUser(@PathVariable String username, @RequestBody User user) {
        log.info("Updating user with username: {}", username);
        return userService.updateUser(username, user);
    }

    @DeleteMapping("/{username}")
    public String deleteUser(@PathVariable String username) {
        log.info("Deleting user with username: {}", username);
        userService.deleteUser(username);
        log.info("User with username {} deleted successfully", username);
        return "User deleted successfully";
    }

    @GetMapping("/generate/{number}")
    public List<User> generateRandomUsers(@PathVariable int number) {
        log.info("Generating {} random users", number);
        return userService.generateRandomUsers(number);
    }


    @GetMapping("/tree")
    public Map<String, Map<String, Map<String, List<User>>>> getUsersTreeStructure() {
        log.info("Fetching tree structure of users");
        return userService.getUsersTreeStructure();
    }
}