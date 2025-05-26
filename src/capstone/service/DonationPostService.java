package capstone.service;

import capstone.model.DonationPost;
import capstone.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class DonationPostService {
    private final List<DonationPost> posts = new ArrayList<>();

    public DonationPost findById(int id) {
        for (DonationPost post : posts) {
            if (post.getId() == id) {
                return post;
            }
        }
        return null;
    }

    public void create(User writer, String donationImg, int goalPoint, LocalDate endAt, String title, String content) {
        DonationPost post = new DonationPost(writer, donationImg, goalPoint, endAt, title, content);
        String virtualAccount = generateVirtualAccount(); // 가상계좌 생성
        post.setVirtualAccount(virtualAccount); // 가상계좌 설정
        posts.add(post);
    }

    // 랜덤 가상계좌 생성
    private String generateVirtualAccount() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        sb.append("VA-"); // prefix (예: 가상계좌의 의미)
        for (int i = 0; i < 12; i++) {
            sb.append(random.nextInt(10)); // 0~9 숫자 12자리
        }
        return sb.toString();
    }

    public List<DonationPost> getAll() {
        return new ArrayList<>(posts);
    }

    public List<DonationPost> getByUser(User user) {
        List<DonationPost> result = new ArrayList<>();
        for (DonationPost post : posts) {
            if (post.getWriter().getUserId().equals(user.getUserId())) {
                result.add(post);
            }
        }
        return result;
    }

    public void update(int id, String title, String content, String donationImg, int goalPoint, LocalDate endAt) {
        DonationPost post = findById(id);
        if (post != null) {
            post.setTitle(title);
            post.setContent(content);
            post.setDonationImg(donationImg);
            post.setGoalPoint(goalPoint);
            post.setEndAt(endAt);
        }
    }

    public void delete(int id) {
        posts.removeIf(p -> p.getId() == id);
    }
}
