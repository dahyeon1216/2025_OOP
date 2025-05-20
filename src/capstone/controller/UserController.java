package capstone.controller;

import capstone.model.BankType;
import capstone.model.User;
import capstone.service.UserService;

public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 사용자 ID로 사용자 정보를 조회
    public User getUserProfile(String userId) {
        return userService.findById(userId);
    }

    // 현재 로그인된 사용자 객체를 그대로 넘겨받는 경우
    public User getUserProfile(User user) {
        if (user == null || user.getUserId() == null) return null;
        return userService.findById(user.getUserId());
    }

    // 사용자 정보 수정
    public boolean updateUserProfile(User user, String name, String nickName, BankType bankType, String bankAccount) {
        if (user == null || user.getUserId() == null) return false;
        return userService.updateUser(user.getUserId(), name, nickName, bankType, bankAccount);
    }
}
