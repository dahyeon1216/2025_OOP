package capstone.controller;

import capstone.model.BankType;
import capstone.model.User;
import capstone.service.UserService;

public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 회원가입
    public boolean signUp(String profileImg, String userId, String password, String name, String nickName, BankType bankType, String bankAccount) {
        return userService.signUp(profileImg, userId, password, name, nickName, bankType, bankAccount);
    }

    // 로그인
    public User login(String userId, String password) {
        return userService.login(userId, password);
    }

    // 비밀번호 변경
    public boolean changePassword(String userId, String currentPassword, String newPassword) {
        User user = userService.findById(userId);
        if (user == null || !user.getPassword().equals(currentPassword)) {
            return false; // 현재 비밀번호 불일치 또는 사용자 없음
        }
        user.setPassword(newPassword);
        return true;
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
