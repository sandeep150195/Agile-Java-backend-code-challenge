package com.optimal.virtualemp.services;

import com.optimal.virtualemp.exception.UserNotFoundException;
import com.optimal.virtualemp.model.User;
import com.optimal.virtualemp.repository.UserRepository;
import com.optimal.virtualemp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllUsers() {
        List<User> mockUsers = Arrays.asList(
                User.builder().username("user1").name("User One").email("user1@example.com").gender("Male").picture("url1").country("Country1").state("State1").city("City1").build(),
                User.builder().username("user2").name("User Two").email("user2@example.com").gender("Female").picture("url2").country("Country2").state("State2").city("City2").build()
        );

        when(userRepository.findAll()).thenReturn(mockUsers);

        List<User> users = userService.getAllUsers();

        assertEquals(2, users.size());
        assertEquals("user1", users.get(0).getUsername());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetUserFound() {
        String username = "user1";
        User mockUser = User.builder().username(username).name("User One").email("user1@example.com").gender("Male").picture("url1").country("Country1").state("State1").city("City1").build();

        when(userRepository.findById(username)).thenReturn(Optional.of(mockUser));

        Optional<User> user = userService.getUser(username);

        assertTrue(user.isPresent());
        assertEquals(username, user.get().getUsername());
        verify(userRepository, times(1)).findById(username);
    }

    @Test
    void testGetUserNotFound() {
        String username = "nonexistent";

        when(userRepository.findById(username)).thenReturn(Optional.empty());

        Optional<User> user = userService.getUser(username);

        assertFalse(user.isPresent());
        verify(userRepository, times(1)).findById(username);
    }

    @Test
    void testCreateUser() {
        User mockUser = User.builder().username("user1").name("User One").email("user1@example.com").gender("Male").picture("url1").country("Country1").state("State1").city("City1").build();

        when(userRepository.save(mockUser)).thenReturn(mockUser);

        User createdUser = userService.createUser(mockUser);

        assertNotNull(createdUser);
        assertEquals("user1", createdUser.getUsername());
        verify(userRepository, times(1)).save(mockUser);
    }

    @Test
    void testUpdateUserSuccess() {
        String username = "user1";
        User updatedUser = User.builder().username(username).name("Updated User").email("updated@example.com").gender("Male").picture("url1").country("Country1").state("State1").city("City1").build();

        when(userRepository.existsById(username)).thenReturn(true);
        when(userRepository.save(updatedUser)).thenReturn(updatedUser);

        User result = userService.updateUser(username, updatedUser);

        assertEquals("Updated User", result.getName());
        verify(userRepository, times(1)).existsById(username);
        verify(userRepository, times(1)).save(updatedUser);
    }

    @Test
    void testUpdateUserNotFound() {
        String username = "nonexistent";
        User updatedUser = User.builder().username(username).name("Updated User").email("updated@example.com").gender("Male").picture("url1").country("Country1").state("State1").city("City1").build();

        when(userRepository.existsById(username)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> userService.updateUser(username, updatedUser));
        verify(userRepository, times(1)).existsById(username);
    }

    @Test
    void testDeleteUserSuccess() {
        String username = "user1";

        when(userRepository.existsById(username)).thenReturn(true);
        doNothing().when(userRepository).deleteById(username);

        userService.deleteUser(username);

        verify(userRepository, times(1)).existsById(username);
        verify(userRepository, times(1)).deleteById(username);
    }

    @Test
    void testDeleteUserNotFound() {
        String username = "nonexistent";

        when(userRepository.existsById(username)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(username));
        verify(userRepository, times(1)).existsById(username);
    }

    @Test
    void testGenerateRandomUsers() {
        int number = 5;

        List<User> result = userService.generateRandomUsers(number);

        assertEquals(number, result.size());
        verify(userRepository, times(number)).save(any(User.class));
    }

    @Test
    void testGetUsersTreeStructure() {
        List<User> mockUsers = Arrays.asList(
                User.builder().username("user1").name("User One").email("user1@example.com").gender("Male").picture("url1").country("Country1").state("State1").city("City1").build(),
                User.builder().username("user2").name("User Two").email("user2@example.com").gender("Female").picture("url2").country("Country1").state("State1").city("City2").build(),
                User.builder().username("user3").name("User Three").email("user3@example.com").gender("Male").picture("url3").country("Country2").state("State2").city("City3").build()
        );

        when(userRepository.findAll()).thenReturn(mockUsers);

        Map<String, Map<String, Map<String, List<User>>>> tree = userService.getUsersTreeStructure();

        assertEquals(2, tree.size());
        assertTrue(tree.containsKey("Country1"));
        assertTrue(tree.containsKey("Country2"));
        verify(userRepository, times(1)).findAll();
    }
}
