package capstone.service;

import capstone.model.BankType;
import capstone.model.User;

import java.util.HashMap;
import java.util.Map;

public class UserService {
    private final Map<String, User> userMap = new HashMap<>();

    public boolean SignUp(String userId, String password, String name, String nickName, BankType bankType, String bankAccount) {
        if (userMap.containsKey(userId)) return false;
        User user = new User(userId, password, name, nickName, bankType, bankAccount);
        userMap.put(userId, user);
        return true;
    }

    public User login(String userId, String password) {
        User user = userMap.get(userId);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }
}
