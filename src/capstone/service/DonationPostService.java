package capstone.service;

import capstone.model.DonationPost;
import capstone.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// UserService 임포트 (더미 데이터 생성에 필요)
import capstone.service.UserService;
import capstone.model.Tier; // Tier 임포트 (더미 데이터 생성에 필요)

public class DonationPostService {
    private static DonationPostService instance;

    // 2. 모든 게시글을 저장하는 리스트 (인메모리 데이터베이스 역할)
    private final List<DonationPost> posts;
    private int sequence = 1; // 게시글 ID를 위한 시퀀스

    // 3. private 생성자로 외부에서 직접 생성 방지
    private DonationPostService() {
        posts = new ArrayList<>();
        // 서비스 생성 시 초기 데이터 로드
        initializeDummyData();
    }

    // 4. 싱글톤 인스턴스를 반환하는 public static 메소드
    public static DonationPostService getInstance() {
        if (instance == null) {
            instance = new DonationPostService();
        }
        return instance;
    }

    // 5. 더미 데이터 초기화 메소드 추가
    private void initializeDummyData() {
        // 더미 사용자 생성 (UserService의 싱글톤 인스턴스를 활용하거나 직접 생성)
        User dummyWriter = new User("writerId", "name", "글쓴이1", "images/profile.jpg", Tier.SILVER);
        dummyWriter.setPoint(100000); // 더미 유저에게 포인트 부여

        // 진행중 더미 글
        create(dummyWriter, "images/dog1.jpg", 10000000, LocalDate.now().plusDays(10), "아기 유기견들을 도와주세요", "차가운 길거리에서 발견된 아기 유기견들을 위해 따뜻한 보금자리와 사료를 기부해주세요.");
        create(dummyWriter, "images/cat1.jpg", 5000000, LocalDate.now().plusDays(20), "길고양이 사료 기부 프로젝트", "매일 배고픈 길고양이들에게 사료를 지원하고 싶습니다. 많은 참여 부탁드립니다.");

        // 완료된 더미 글 (마감일이 지난 글)
        DonationPost completedPost1 = new DonationPost(dummyWriter, "images/dog2.jpg", 3000000, LocalDate.now().minusDays(5), "성공적으로 완료된 수술비 모금", "아픈 강아지의 수술이 성공적으로 마무리되었습니다. 감사합니다!");
        completedPost1.setRaisedPoint(completedPost1.getGoalPoint()); // 목표 달성 상태로 설정
        posts.add(completedPost1);
        completedPost1.setId(sequence++); // 시퀀스 수동 증가
    }


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
        post.setId(sequence++);
        System.out.println("생성된 post ID = " + post.getId());
        System.out.println("새 게시물 생성: " + post.getTitle());
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
