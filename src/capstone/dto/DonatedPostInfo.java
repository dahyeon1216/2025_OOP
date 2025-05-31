package capstone.dto;

import capstone.model.DonationPost;

import java.time.LocalDateTime;

public class DonatedPostInfo {
    private DonationPost post;
    private int totalDonatedPoint;
    private final LocalDateTime donatedAt;

    public DonatedPostInfo(DonationPost post, int totalDonatedPoint, LocalDateTime donatedAt) {
        this.post = post;
        this.totalDonatedPoint = totalDonatedPoint;
        this.donatedAt = donatedAt;
    }

    public DonationPost getPost() {
        return post;
    }

    public int getTotalDonatedPoint() {
        return totalDonatedPoint;
    }

    public LocalDateTime getDonatedAt() {
        return donatedAt;
    }
}
