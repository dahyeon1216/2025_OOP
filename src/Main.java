import capstone.controller.UserController;
import capstone.service.DonationPostService;
import capstone.service.UserService;
import capstone.view.LoginView;
import capstone.view.SignupView;

public class Main {
    public static void main(String[] args) {
        UserService userService = new UserService();
        DonationPostService donationService = new DonationPostService(); // 하나만 생성

        new LoginView(userService, donationService).setVisible(true);
        new LoginView(userService, donationService).setVisible(true); // 같은 서비스 공유
    }
}