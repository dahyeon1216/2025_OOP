import capstone.controller.DonationPostController;
import capstone.controller.UserController;
import capstone.model.BankType;
import capstone.model.User;
import capstone.service.DonationPostService;
import capstone.service.UserService;
import capstone.view.MainView;

import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {
    private final UserController userController;
    private final DonationPostController donationPostController;

    public Main(UserController userController, DonationPostController donationPostController) {
        this.userController = userController;
        this.donationPostController = donationPostController;

        setTitle("Application Launcher");
        setSize(300, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JButton openMainViewBtn = new JButton("메인으로 이동");

        openMainViewBtn.addActionListener(e -> {
            new MainView(null, userController, donationPostController).setVisible(true);
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

        // 초기 사용자 등록
        userService.signUp("1", "1", "1", "1", BankType.KAKAO, "1");
        userService.signUp("2", "2", "2", "2", BankType.SHINHAN, "2");

        // 시작 화면 실행
        new Main(userController, donationPostController).setVisible(true);
    }
}