package capstone.service;

import capstone.model.DonationPost;
import capstone.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        posts.add(post);
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

    // 진행중 기부글 (마감일이 오늘 이후)
    public List<DonationPost> getOngoingPosts() {
        List<DonationPost> result = new ArrayList<>();
        for (DonationPost post : posts) {
            if (post.getEndAt().isAfter(LocalDate.now())) {
                result.add(post);
            }
        }
        return result;
    }

    // 완료된 기부글 (마감일이 오늘이거나 이전)
    public List<DonationPost> getCompletedPosts() {
        List<DonationPost> result = new ArrayList<>();
        for (DonationPost post : posts) {
            if (!post.getEndAt().isAfter(LocalDate.now())) {
                result.add(post);
            }
        }
        return result;
    }
}
