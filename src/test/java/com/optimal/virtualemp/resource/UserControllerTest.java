package com.optimal.virtualemp.resource;

import com.optimal.virtualemp.model.User;
import com.optimal.virtualemp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllUsers() {
        List<User> users = Arrays.asList(
                User.builder().username("user1").name("User One").email("user1@example.com").gender("Male").picture("url1").country("Country1").state("State1").city("City1").build(),
                User.builder().username("user2").name("User Two").email("user2@example.com").gender("Female").picture("url2").country("Country2").state("State2").city("City2").build()
        );

        when(userService.getAllUsers()).thenReturn(users);

        List<User> result = userController.getAllUsers();

        assertEquals(2, result.size());
        assertEquals("user1", result.get(0).getUsername());
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void testGetUserFound() {
        String username = "user1";
        User user = User.builder().username(username).name("User One").email("user1@example.com").gender("Male").picture("url1").country("Country1").state("State1").city("City1").build();

        when(userService.getUser(username)).thenReturn(Optional.of(user));

        User result = userController.getUser(username);

        assertNotNull(result);
        assertEquals(username, result.getUsername());
        verify(userService, times(1)).getUser(username);
    }

    @Test
    void testGetUserNotFound() {
        String username = "nonexistent";

        when(userService.getUser(username)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> userController.getUser(username));
        assertTrue(exception.getMessage().contains("not found"));
        verify(userService, times(1)).getUser(username);
    }

    @Test
    void testCreateUser() {
        User user = User.builder().username("user1").name("User One").email("user1@example.com").gender("Male").picture("url1").country("Country1").state("State1").city("City1").build();

        when(userService.createUser(user)).thenReturn(user);

        User result = userController.createUser(user);

        assertNotNull(result);
        assertEquals("user1", result.getUsername());
        verify(userService, times(1)).createUser(user);
    }

    @Test
    void testUpdateUser() {
        String username = "user1";
        User updatedUser = User.builder().username(username).name("Updated User").email("updated@example.com").gender("Male").picture("url1").country("Country1").state("State1").city("City1").build();

        when(userService.updateUser(eq(username), eq(updatedUser))).thenReturn(updatedUser);

        User result = userController.updateUser(username, updatedUser);

        assertNotNull(result);
        assertEquals("Updated User", result.getName());
        verify(userService, times(1)).updateUser(eq(username), eq(updatedUser));
    }

    @Test
    void testDeleteUser() {
        String username = "user1";

        doNothing().when(userService).deleteUser(username);

        String result = userController.deleteUser(username);

        assertEquals("User deleted successfully", result);
        verify(userService, times(1)).deleteUser(username);
    }

    @Test
    void testGenerateRandomUsers() {
        int number = 5;
        List<User> users = Arrays.asList(
                User.builder().username("user1").name("User One").email("user1@example.com").gender("Male").picture("url1").country("Country1").state("State1").city("City1").build()
        );

        when(userService.generateRandomUsers(number)).thenReturn(users);

        List<User> result = userController.generateRandomUsers(number);

        assertEquals(1, result.size());
        assertEquals("user1", result.get(0).getUsername());
        verify(userService, times(1)).generateRandomUsers(number);
    }

    @Test
    void testGetUsersTreeStructure() {
        Map<String, Map<String, Map<String, List<User>>>> treeStructure = Map.of(
                "Country1", Map.of("State1", Map.of("City1", List.of(
                        User.builder().username("user1").name("User One").email("user1@example.com").gender("Male").picture("url1").country("Country1").state("State1").city("City1").build()
                )))
        );

        when(userService.getUsersTreeStructure()).thenReturn(treeStructure);

        Map<String, Map<String, Map<String, List<User>>>> result = userController.getUsersTreeStructure();

        assertNotNull(result);
        assertTrue(result.containsKey("Country1"));
        verify(userService, times(1)).getUsersTreeStructure();
    }
}
