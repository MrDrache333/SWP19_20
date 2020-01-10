package de.uol.swp.server.usermanagement;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import de.uol.swp.common.user.User;
import de.uol.swp.server.usermanagement.store.UserStore;

import java.util.List;
import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class UserManagement extends AbstractUserManagement {

    private final UserStore userStore;
    private final SortedMap<String, User> loggedInUsers = new TreeMap<>();

    @Inject
    public UserManagement(UserStore userStore) {
        this.userStore = userStore;
    }

    @Override
    public User login(String username, String password) {
        Optional<User> user = userStore.findUser(username, password);
        if (user.isPresent() && !loggedInUsers.containsKey(username)) {
            this.loggedInUsers.put(username, user.get());
            return user.get();
        } else {
            throw new SecurityException("Cannot auth user " + username);
        }
    }

    @Override
    public boolean isLoggedIn(User username) {
        return loggedInUsers.containsKey(username.getUsername());
    }

    @Override
    public User createUser(User userToCreate) {
        Optional<User> user = userStore.findUser(userToCreate.getUsername());
        if (user.isPresent()) {
            throw new UserManagementException("Username already used!");
        }
        return userStore.createUser(userToCreate.getUsername(), userToCreate.getPassword(), userToCreate.getEMail());
    }

    @Override
    public User updateUser(User userToUpdate, User oldUser) {
        Optional<User> user = userStore.findUser(oldUser.getUsername());
        if (user.isEmpty()) {
            throw new UserManagementException("Username unknown!");
        }
        //Überprüft ob der Username bereits vergeben ist
        user = userStore.findUser(userToUpdate.getUsername());
        if (user.isPresent() && !userToUpdate.getUsername().equals(oldUser.getUsername())) {
            throw new UserUpdateException("Dieser Name ist bereits vergeben!");
        }
        //Überprüft ob bereits ein Account mit der Email existiert
        List<String> mails = userStore.getAllUsers().stream().map(User::getEMail).collect(Collectors.toList());
        if (mails.contains(userToUpdate.getEMail()) && !userToUpdate.getEMail().equals(oldUser.getEMail())) {
            throw new UserUpdateException("Diese Email ist bereits vergeben!");
        }
       return userStore.updateUser(userToUpdate.getUsername(), userToUpdate.getPassword(), userToUpdate.getEMail(), oldUser.getUsername());
    }

    @Override
    public void dropUser(User userToDrop) {
        Optional<User> user = userStore.findUser(userToDrop.getUsername());
        if (user.isEmpty()) {
            throw new UserManagementException("Username unknown!");
        }
        logout(userToDrop);
        userStore.removeUser(userToDrop.getUsername());

    }

    private String firstNotNull(String firstValue, String secondValue) {
        return Strings.isNullOrEmpty(firstValue) ? secondValue : firstValue;
    }

    @Override
    public void logout(User user) {
        loggedInUsers.remove(user.getUsername());
    }

    @Override
    public List<User> retrieveAllUsers() {
        return userStore.getAllUsers();
    }
}
