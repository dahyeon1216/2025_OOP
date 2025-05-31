import capstone.controller.DonationPostController;
import capstone.controller.ScrapController;
import capstone.controller.UserController;
import capstone.model.BankType;
import capstone.service.DonationPostService;
import capstone.service.ScrapService;
import capstone.service.UserService;
import capstone.view.main.MainView;

import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {
    private final UserController userController;
    private final DonationPostController donationPostController;
    private final ScrapController scrapController;

    public Main(UserController userController, DonationPostController donationPostController, ScrapController scrapController) {
        this.userController = userController;
        this.donationPostController = donationPostController;
        this.scrapController = scrapController;

        setTitle("Application Launcher");
        setSize(300, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JButton openMainViewBtn = new JButton("메인으로 이동");

        openMainViewBtn.addActionListener(e -> {
            new MainView(null, userController, donationPostController, scrapController).setVisible(true);
        });

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(openMainViewBtn, BorderLayout.CENTER);
        add(panel);
    }

    public static void main(String[] args) {
        UserService userService = new UserService();
        UserController userController = new UserController(userService);
        DonationPostService donationService = new DonationPostService();
        DonationPostController donationPostController = new DonationPostController(donationService);
        ScrapService scrapService = new ScrapService();
        ScrapController scrapController = new ScrapController(scrapService);

        // 초기 사용자 등록
        userService.signUp("1", "1", "1", "1", BankType.KAKAO, "1");
        userService.signUp("2", "2", "2", "2", BankType.SHINHAN, "2");

        // 시작 화면 실행
        new Main(userController, donationPostController, scrapController).setVisible(true);
    }
}