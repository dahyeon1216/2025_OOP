package capstone.model;

public enum BankType {
    // 상수("연관시킬 문자")
    KB("국민은행"),
    SHINHAN("신한은행"),
    KAKAO("카카오뱅크"),
    NH("농협은행");

    private final String bankName;
    public String getBankName() {
        return bankName;
    }
    private BankType(String bankName) {
        this.bankName = bankName;
    }
}
