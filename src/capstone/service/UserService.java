package capstone.service;

import capstone.model.BankType;
import capstone.model.User;
import capstone.model.Tier;

import java.util.HashMap;
import java.util.Map;

public class UserService {
    // 1. 싱글톤 인스턴스 변수
    private static UserService instance;

    // 2. 사용자 데이터를 저장할 맵
    private final Map<String, User> userMap;

    // 3. private 생성자로 외부에서 직접 생성 방지
    private UserService() {
        userMap = new HashMap<>();
        // 서비스 생성 시 초기 사용자 데이터 로드
        initializeDummyData();
    }

    // 4. 싱글톤 인스턴스를 반환하는 public static 메소드
    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    // 5. 더미 데이터 초기화 메소드 추가
    private void initializeDummyData() {
        // 테스트용 사용자 생성
        signUp("user1", "pass1", "사용자1", "닉네임1", BankType.KB, "123-456-7890");
        signUp("user2", "pass2", "사용자2", "닉네임2", BankType.SHINHAN, "987-654-3210");
        signUp("user3", "pass3", "사용자3", "닉네임3", BankType.KAKAO, "1023-4567");

        // 추가적으로, 필요하다면 사용자에게 초기 포인트 부여
        User user1 = userMap.get("user1");
        if (user1 != null) {
            user1.setPoint(50000); // 테스트용 포인트
            user1.setTier(Tier.SILVER); // 티어 설정
        }
        User sally = userMap.get("sally1023");
        if (sally != null) {
            sally.setPoint(120000); // 테스트용 포인트
            sally.setTier(Tier.GOLD); // 티어 설정
        }

        System.out.println("UserService 더미 데이터 초기화 완료. 등록된 사용자 수: " + userMap.size());
    }

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
