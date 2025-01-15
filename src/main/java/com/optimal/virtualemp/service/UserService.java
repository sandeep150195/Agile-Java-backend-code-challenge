package com.optimal.virtualemp.service;

import com.optimal.virtualemp.exception.UserNotFoundException;
import com.optimal.virtualemp.model.User;
import com.optimal.virtualemp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Service
public class UserService {
    private final UserRepository userRepository;
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUser(String username) {
        return userRepository.findById(username);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(String username, User user) {
        if (userRepository.existsById(username)) {
            user.setUsername(username);
            return userRepository.save(user);
        } else {
            throw new UserNotFoundException("User with username " + username + " not found");
        }
    }

    public void deleteUser(String username) {
        if (!userRepository.existsById(username)) {
            throw new UserNotFoundException("User with username " + username + " not found");
        }
        userRepository.deleteById(username);
    }

    public List<User> generateRandomUsers(int number) {
        List<User> randomUsers = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            User user = User.builder()
                    .username("user" + i).name("Random User " + i)
                    .email("user" + i + "@example.com")
                    .gender(i % 2 == 0 ? "Male" : "Female")
                    .picture("https://randomuser.me/api/portraits/" + (i % 2 == 0 ? "men" : "women") + "/" + i + ".jpg")
                    .country("Country" + (i % 3)).
                    state("State" + (i % 5))
                    .city("City" + (i % 7)).build();
            userRepository.save(user);
            randomUsers.add(user);
        }
        return randomUsers;
    }

    public Map<String, Map<String, Map<String, List<User>>>> getUsersTreeStructure() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .collect(Collectors.groupingBy(User::getCountry,
                        Collectors.groupingBy(User::getState,
                                Collectors.groupingBy(User::getCity))));
    }
}