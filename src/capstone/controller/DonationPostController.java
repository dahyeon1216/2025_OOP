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
    public void updatePost(int id, String title, String content, String img, int goal, LocalDate endAt) {
        donationPostService.update(id, title, content, img, goal, endAt);
    }

    //  기부글 삭제
    public void deletePost(int postId) {
        donationPostService.delete(postId);
    }

    // 기부글에 기부하기
    public boolean donate(DonationPost post, User user, int donatePpoint) {
        return donationPostService.donateToPost(post, user, donatePpoint);
    }

    // 기부글 정산하기
    public boolean settlePost(DonationPost post) {
        return donationPostService.settlePost(post);
    }

}
