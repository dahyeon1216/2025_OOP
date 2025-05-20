package capstone.service;

import capstone.model.BankType;
import capstone.model.User;

import java.util.HashMap;
import java.util.Map;

public class UserService {
    private final Map<String, User> userMap = new HashMap<>();

    // 회원가입
    public boolean signUp(String userId, String password, String name, String nickName, BankType bankType, String bankAccount) {
        if (userMap.containsKey(userId)) return false;
        User user = new User(userId, password, name, nickName, bankType, bankAccount);
        userMap.put(userId, user);
        return true;
    }

    // 로그인
    public User login(String userId, String password) {
        User user = userMap.get(userId);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    public User findById(String userId) {
        return userMap.get(userId);
    }

    public Map<String, User> getAllUsers() {
        return userMap;
    }

    public boolean updateUser(String userId, String name, String nickName, capstone.model.BankType bankType, String bankAccount) {
        User user = userMap.get(userId);
        if (user == null) return false;

        user.setName(name);
        user.setNickName(nickName);
        user.setBankType(bankType);
        user.setBankAccount(bankAccount);
        return true;
    }
}
