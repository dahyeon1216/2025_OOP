package capstone.model;

import capstone.model.Tier;

import java.time.LocalDate;
import java.util.Objects;

public class User {
//    private Long id;
    private String userId; // 로그인할 때 쓰는 아이디
    private String password;
    private String name;
    private String nickName;
    private String profileImg;
    private BankType bankType;
    private String bankAccount;
    private int point;
    private Tier tier;
    private int donatedTotalPoint; // 누적 기부 포인트

    // 기본 생성자
    public User() {
    }

    // 전체 필드 생성자
    public User(String userId, String password, String name, String nickName,
                String profileImg, BankType bankType, String bankAccount,
                int point, Tier tier) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.nickName = nickName;
        this.profileImg = profileImg;
        this.bankType = bankType;
        this.bankAccount = bankAccount;
        this.point = point;
        this.tier = tier;
        this.donatedTotalPoint = 0;
    }

    // 회원가입 생성자
    public User(String profileImg, String userId, String password, String name, String nickName, BankType bankType, String bankAccount) {
        this.profileImg = profileImg;
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.nickName = nickName;
        this.bankType = bankType;
        this.bankAccount = bankAccount;
        this.tier = Tier.BRONZE; // default
    }

    public void addDonation(int amount) {
        this.donatedTotalPoint += amount;
        updateTierIfNeeded(); // ✅ 기부할 때마다 티어 자동 확인
    }

    public void updateTierIfNeeded() {
        if (donatedTotalPoint >= 50000) {
            this.tier = Tier.PLATINUM;
        } else if (donatedTotalPoint >= 30000) {
            this.tier = Tier.GOLD;
        } else if (donatedTotalPoint >= 10000) {
            this.tier = Tier.SILVER;
        } else {
            this.tier = Tier.BRONZE;
        }
    }

    // Getter and Setter
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getNickName() { return nickName; }
    public void setNickName(String nickName) { this.nickName = nickName; }

    public String getProfileImg() { return profileImg; }
    public void setProfileImg(String profileImag) { this.profileImg = profileImag; }

    public BankType getBankType() { return bankType; }
    public void setBankType(BankType bankType) { this.bankType = bankType; }

    public String getBankAccount() { return bankAccount; }
    public void setBankAccount(String bankAccount) { this.bankAccount = bankAccount; }

    public int getPoint() { return point; }
    public void setPoint(int point) { this.point = point; }

    public String getTier() { return tier.getMsg(); }
    public void setTier(Tier tier) { this.tier = tier; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userId, user.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }

}
