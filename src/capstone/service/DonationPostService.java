package capstone.service;

import capstone.model.DonationPost;
import capstone.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DonationPostService {
    private final List<DonationPost> posts = new ArrayList<>();

    // 기부글 쓰기
    public void create(User writer, String donationImg, int goalPoint, LocalDate endAt, String title, String content) {
        posts.add(new DonationPost(writer, donationImg, goalPoint, endAt, title, content));
    }

    // 기부글 전체 조회
    public List<DonationPost> getAll() {
        return posts;
    }

    // id에 해당하는 기부글 하나 상세조회
    public Optional<DonationPost> findById(int id) {
        return posts.stream().filter(p -> p.getId() == id).findFirst();
    }

    // 기부글 삭제
    public void delete(int id) {
        posts.removeIf(p -> p.getId() == id);
    }
}
