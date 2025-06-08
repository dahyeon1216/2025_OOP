package capstone.controller;

import capstone.model.DonationPost;
import capstone.model.User;
import capstone.service.ScrapService;

import java.util.List;

public class ScrapController {
    private final ScrapService scrapService;

    public ScrapController(ScrapService scrapService) {
        this.scrapService = scrapService;
    }

    public void toggleScrap(User user, DonationPost post) {
        if (scrapService.isScrapped(user, post)) {
            scrapService.unscrap(user, post);
        } else {
            scrapService.scrap(user, post);
        }
    }

    public boolean isScrapped(User user, DonationPost post) {
        return scrapService.isScrapped(user, post);
    }

    public List<DonationPost> getScrappedPosts(User user) {
        return scrapService.getScrappedPosts(user);
    }
}
