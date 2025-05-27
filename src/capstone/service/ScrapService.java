package capstone.service;

import capstone.model.DonationPost;
import capstone.model.ScrapRecord;
import capstone.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ScrapService {
    private final List<ScrapRecord> scraps = new ArrayList<>();

    public void scrap(User user, DonationPost post) {
        if (!isScrapped(user, post)) {
            scraps.add(new ScrapRecord(user, post));
        }
    }

    public void unscrap(User user, DonationPost post) {
        scraps.removeIf(s -> s.getUser().equals(user) && s.getDonationPost().equals(post));
    }

    public boolean isScrapped(User user, DonationPost post) {
        return scraps.stream()
                .anyMatch(s -> s.getUser().equals(user) && s.getDonationPost().equals(post));
    }

    public List<DonationPost> getScrappedPosts(User user) {
        return scraps.stream()
                .filter(s -> s.getUser().equals(user))
                .map(ScrapRecord::getDonationPost)
                .collect(Collectors.toList());
    }
}
