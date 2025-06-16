package capstone.service;

import capstone.dto.DonatedPostInfo;
import capstone.model.DonationPost;
import capstone.model.User;
import capstone.model.DonationRecord;
import capstone.model.VirtualAccount;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class DonationPostService {
    private final List<DonationPost> posts = new ArrayList<>();
    private final List<DonationRecord> donationRecords = new ArrayList<>();

    // id인 특정 기부글 상세조회
    public DonationPost findById(int id) {
        for (DonationPost post : posts) {
            if (post.getId() == id) {
                return post;
            }
        }
        return null;
    }

    // 기부글 작성
    public DonationPost create(User writer, String donationImg, int goalPoint, LocalDate endAt, String title, String content) {
        DonationPost post = new DonationPost(writer, donationImg, goalPoint, endAt, title, content);

        String vaAccount = generateVirtualAccount(); // 가상계좌 생성
        VirtualAccount virtualAccount = new VirtualAccount(writer.getBankType(), vaAccount, writer, post);

        post.setVirtualAccount(virtualAccount);
        posts.add(post);

        return post;
    }

    // 랜덤 가상계좌 생성
    public String generateVirtualAccount() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        sb.append("VA-"); // prefix (예: 가상계좌의 의미)
        for (int i = 0; i < 12; i++) {
            sb.append(random.nextInt(10)); // 0~9 숫자 12자리
        }
        return sb.toString();
    }

    // 전체 기부글 조회
    public List<DonationPost> getAll() {
        return posts.stream()
                .sorted(Comparator.comparing(DonationPost::getUpFuncAt).reversed()) // 최신 up 순
                .collect(Collectors.toList());
    }

    // 기부글 수정
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

    // 기부글 삭제
    public void delete(int id) {
        posts.removeIf(p -> p.getId() == id);
    }

    // 기부글에 기부하기
    public boolean donateToPost(DonationPost post, User donor, int donatePpoint) {
        if (post.isCompleted()) return false; // 이미 종료된 기부글
        if (donor.getPoint() < donatePpoint) return false;

        donor.setPoint(donor.getPoint() - donatePpoint); // 포인트 차감
        post.donate(donatePpoint); // 포스트에 기부 반영

        // 사용자의 기부 기록 저장
        DonationRecord record = new DonationRecord(donor, post, donatePpoint, LocalDateTime.now());
        donationRecords.add(record);
        return true;
    }

    // 사용자의 기부한 내역 조회
    public List<DonatedPostInfo> getDonatedPostInfos(User user) {
        return donationRecords.stream()
                .filter(record -> record.getUser().equals(user))
                .map(record -> new DonatedPostInfo(
                        record.getDonationPost(),
                        record.getPoint(),
                        record.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    // 기부글 정산하기
    public boolean settlePost(DonationPost post) {
        if (post.isCompleted() && !post.isSettled()) {
            User writer = post.getWriter();
            VirtualAccount virtualAccount = post.getVirtualAccount();

            if (writer != null && virtualAccount != null) {
                int actualRaised = post.getRaisedPoint();
                virtualAccount.settleReceipt(actualRaised);
                post.settle();
                return true;
            }
        }
        return false;
    }

    // 기부글 up하기
    public boolean upPost(DonationPost post, User user) {
        int upCost = 300;

        if (user.getPoint() < upCost) {
            return false;
        }
        user.setPoint(user.getPoint() - upCost);
        post.setUpFuncAt(LocalDateTime.now());
        return true;
    }
}
