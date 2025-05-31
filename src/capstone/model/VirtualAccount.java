package capstone.model;

import java.util.LinkedHashMap;
import java.util.Map;

public class VirtualAccount {
    private BankType bankType;
    private String bankAccount;
    private User ownerUser;
    private DonationPost donationPost;
    private int raisedPoint; // 초기 모금 포인트
    private int currentPoint; // 포인트 잔고
    private Map<Integer,String> receipt; // 포인트 사용 내역

    // 생성자
    public VirtualAccount(BankType bankType, String bankAccount, User ownerUser, DonationPost donationPost) {
        this.bankType = bankType;
        this.bankAccount = bankAccount;
        this.ownerUser = ownerUser;
        this.donationPost = donationPost;
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

    public void setOwnerUser(User ownerUser) {
        this.ownerUser = ownerUser;
    }

    public DonationPost getDonationPost() {
        return donationPost;
    }

    public void setDonationPost(DonationPost donationPost) {
        this.donationPost = donationPost;
    }

    public int getRaisedPoint() {
        return raisedPoint;
    }

    public void setRaisedPoint(int raisedPoint) {
        this.raisedPoint = raisedPoint;
    }

    public int getCurrentPoint() {
        return currentPoint;
    }

    public void setCurrentPoint(int point) {
        this.currentPoint = point;
    }

    public Map<Integer, String> getReceipt() {
        return receipt;
    }

    public boolean usePoint(int amount, String usageDetail) {
        if (currentPoint <= amount) {
            return false;
        }

        currentPoint -= amount;
        int nextId = receipt.size() + 1;
        receipt.put(nextId, usageDetail + " (잔여 포인트: " + currentPoint + "P)");
        return true;
    }
}
