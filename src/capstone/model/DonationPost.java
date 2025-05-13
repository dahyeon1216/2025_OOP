package capstone.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DonationPost {
    private static int seq = 1;
    private int id;
    private User writer;
    private String title;
    private String content;
    private String donationImg;
    private LocalDateTime createdAt;
    private LocalDateTime upFuncAt;
    private LocalDate endAt;
    private int goalPoint;
    private int raisedPoint;
    private String billImg;

    // 기본 생성자
    public DonationPost() {
    }

    // 전체 필드 생성자
    public DonationPost(User writer, String title, String content, String donationImg,
                        LocalDateTime createdAt, LocalDateTime upFuncAt, LocalDate endAt,
                        int goalPoint, int raisedPoint, String billImg) {
        this.writer = writer;
        this.title = title;
        this.content = content;
        this.donationImg = donationImg;
        this.createdAt = createdAt;
        this.upFuncAt = upFuncAt;
        this.endAt = endAt;
        this.goalPoint = goalPoint;
        this.raisedPoint = raisedPoint;
        this.billImg = billImg;
    }

    // 기부글 쓰기 생성자
    public DonationPost(User writer, String donationImg, int goalPoint, LocalDate endAt, String title, String content){
        this.id = seq++;
        this.writer = writer;
        this.title = title;
        this.content = content;
        this.donationImg = donationImg;
        this.goalPoint = goalPoint;
        this.createdAt = LocalDateTime.now();
        this.endAt = endAt;
    }

    // Getter and Setter
    public int getId() { return id; }

    public User getWriter() {
        return writer;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getDonationImg() { return donationImg; }
    public void setDonationImg(String donationImg) { this.donationImg = donationImg; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpFuncAt() { return upFuncAt; }
    public void setUpFuncAt(LocalDateTime upFuncAt) { this.upFuncAt = upFuncAt; }

    public LocalDate getEndAt() { return endAt; }
    public void setEndAt(LocalDate endAt) { this.endAt = endAt; }

    public int getGoalPoint() { return goalPoint; }
    public void setGoalPoint(int goalPoint) { this.goalPoint = goalPoint; }

    public int getRaisedPoint() { return raisedPoint; }
    public void setRaisedPoint(int raisedPoint) { this.raisedPoint = raisedPoint; }

    public String getBillImg() { return billImg; }
    public void setBillImg(String billImg) { this.billImg = billImg; }

    public void setWriter(User writer) {
        this.writer = writer;
    }
}
