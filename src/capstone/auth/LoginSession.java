package capstone.auth;

import capstone.model.User;

public class LoginSession {
    private static User currentUser;

    public static void login(User user) {
        currentUser = user;
    }

    public static void logout() {
        currentUser = null;
    }

    // 사용X -> 로컬 환경에선 애플리케이션 전체에서 1개의 로그인 사용자 정보만 저장
//    public static User getCurrentUser() {
//        return currentUser;
//    }

    public static boolean isLoggedIn() {
        return currentUser != null;
    }
}
