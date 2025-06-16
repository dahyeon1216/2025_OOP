package capstone.model;

import java.time.LocalDateTime;

public class ReceiptRecord {
    private LocalDateTime timestamp;
    private String usageDetail;
    private int usedAmount;
    private int remainingBalance;

    public ReceiptRecord(LocalDateTime timestamp, String usageDetail, int usedAmount, int remainingBalance) {
        this.timestamp = timestamp;
        this.usageDetail = usageDetail;
        this.usedAmount = usedAmount;
        this.remainingBalance = remainingBalance;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getUsageDetail() {
        return usageDetail;
    }

    public int getUsedAmount() {
        return usedAmount;
    }

    public int getRemainingBalance() {
        return remainingBalance;
    }
}
