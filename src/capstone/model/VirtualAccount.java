package capstone.model;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

public class VirtualAccount {
    private BankType bankType;
    private String bankAccount;
    private User ownerUser;
    private DonationPost donationPost;
    private int raisedPoint; // 초기 모금 포인트
    private int currentPoint; // 포인트 잔고
    private Map<Integer,ReceiptRecord> receipt; // 포인트 사용 내역

    // 생성자
    public VirtualAccount(BankType bankType, String bankAccount, User ownerUser, DonationPost donationPost) {
        this.bankType = bankType;
        this.bankAccount = bankAccount;
        this.ownerUser = ownerUser;
        this.donationPost = donationPost;
        this.receipt = new LinkedHashMap<>();
    }

    public VirtualAccount(BankType type, String bankAccount){
        this.bankType = type;
        this.bankAccount = bankAccount;
        this.receipt = new LinkedHashMap<>();
    }

    // getter setter
    public BankType getBankType() {
        return bankType;
    }

    public void setBankType(BankType bankType) {
        this.bankType = bankType;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public User getOwnerUser() {
        return ownerUser;
    }

    public int getRaisedPoint() {
        return raisedPoint;
    }

    public void settleReceipt(int point) {
        this.raisedPoint = point;
        this.currentPoint = point;
        int nextId = receipt.size() + 1;
        receipt.put(nextId, new ReceiptRecord(LocalDateTime.now(), "정산금", 0, point));
    }

    public boolean usePoint(int amount, String usageDetail) {
        if (currentPoint < amount) {
            return false;
        }
        currentPoint -= amount;
        int nextId = receipt.size() + 1;
        receipt.put(nextId, new ReceiptRecord(LocalDateTime.now(), usageDetail, amount, currentPoint));
        return true;
    }

    public Map<Integer, ReceiptRecord> getReceipt() {
        return receipt;
    }

}
