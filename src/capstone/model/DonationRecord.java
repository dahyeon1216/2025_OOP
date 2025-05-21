package capstone.model;

import java.time.LocalDateTime;

public class DonationRecord {
//    private Long id;
    private User user;
    private DonationPost donationPost;
    private int point;
    private LocalDateTime createdAt;

    // 기본 생성자
    public DonationRecord() {
    }

    // 전체 필드 생성자
    public DonationRecord(User user, DonationPost donationPost, int point, LocalDateTime createdAt) {
        this.user = user;
        this.donationPost = donationPost;
        this.point = point;
        this.createdAt = createdAt;
    }

    // Getter and Setter
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public DonationPost getDonationPost() { return donationPost; }
    public void setDonationPost(DonationPost donationPost) { this.donationPost = donationPost; }

    public int getPoint() { return point; }
    public void setPoint(int point) { this.point = point; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
