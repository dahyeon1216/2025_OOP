package capstone.model;

public class ScrapRecord {
//    private String id;
    private User user;
    private DonationPost donationPost;

    // 기본 생성자
    public ScrapRecord() {
    }

    // 전체 필드 생성자
    public ScrapRecord(User user, DonationPost donationPost) {
        this.user = user;
        this.donationPost = donationPost;
    }

    // Getter and Setter
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public DonationPost getDonationPost() { return donationPost; }
    public void setDonationPost(DonationPost donationPost) { this.donationPost = donationPost; }
}
