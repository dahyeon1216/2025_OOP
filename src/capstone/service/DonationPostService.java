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

    public boolean update(int postId, String newTitle, String newContent, User editor) {
        Optional<DonationPost> optionalPost = posts.stream()
                .filter(p -> p.getId() == postId)
                .findFirst();

        if (optionalPost.isPresent()) {
            DonationPost post = optionalPost.get();
            if (post.getWriter().getUserId().equals(editor.getUserId())) {
                post.setTitle(newTitle);
                post.setContent(newContent);
                post.setUpFuncAt(LocalDateTime.now());
                return true;
            }
        }
        return false;
    }

    public boolean delete(int postId, User requester) {
        Optional<DonationPost> optionalPost = posts.stream()
                .filter(p -> p.getId() == postId)
                .findFirst();

        if (optionalPost.isPresent()) {
            DonationPost post = optionalPost.get();
            if (post.getWriter().getUserId().equals(requester.getUserId())) {
                posts.remove(post);
                return true;
            }
        }
        return false;
    }
}
