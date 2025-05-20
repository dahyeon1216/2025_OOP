package capstone.controller;

import capstone.model.DonationPost;
import capstone.model.User;
import capstone.service.DonationPostService;

import java.time.LocalDate;
import java.util.List;

public class DonationPostController {
    private final DonationPostService donationPostService;

    public DonationPostController(DonationPostService donationPostService) {
        this.donationPostService = donationPostService;
    }

    // 기부글 작성
    public void createPost(User writer, String donationImg, int goalPoint, LocalDate endAt, String title, String content) {
        donationPostService.create(writer, donationImg, goalPoint, endAt, title, content);
    }

    // 전체 기부글 조회
    public List<DonationPost> getAllPosts() {
        return donationPostService.getAll();
    }

    // 사용자 ID로 필터링된 기부글 조회 (추후 확장 가능)
    public List<DonationPost> getPostsByUser(User user) {
        return donationPostService.getByUser(user);
    }

    // 기부글 수정
    public boolean updatePost(int postId, String newTitle, String newContent, User editor) {
        return donationPostService.update(postId, newTitle, newContent, editor);
    }

    //  기부글 삭제
    public boolean deletePost(int postId, User requester) {
        return donationPostService.delete(postId, requester);
    }
}
