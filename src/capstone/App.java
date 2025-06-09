package capstone;

import capstone.controller.DonationPostController;
import capstone.controller.ScrapController;
import capstone.controller.UserController;
import capstone.model.BankType;
import capstone.service.DonationPostService;
import capstone.service.ScrapService;
import capstone.service.UserService;
import capstone.view.user.LoginView;
import capstone.view.user.SignupView;

import javax.swing.*;
import java.awt.*;

public class App extends JFrame {

    public App(UserController userController,
               DonationPostController donationPostController,
               ScrapController scrapController) {

        setTitle("시작 화면");
        setSize(400, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JButton loginBtn = new JButton("로그인");
        JButton signupBtn = new JButton("회원가입");

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1, 10, 10));
        panel.add(loginBtn);
        panel.add(signupBtn);

        add(panel);
        setVisible(true);

        loginBtn.addActionListener(e -> {
            dispose(); // App 창 닫기
            new LoginView(userController,null);
        });

        signupBtn.addActionListener(e -> {
            dispose(); // App 창 닫기
            new SignupView(userController);
        });
    }

    public static void main(String[] args) {
        // 서비스 및 컨트롤러 생성
        UserService userService = new UserService();
        UserController userController = new UserController(userService);

        DonationPostService donationService = new DonationPostService();
        DonationPostController donationPostController = new DonationPostController(donationService);

        ScrapService scrapService = new ScrapService();
        ScrapController scrapController = new ScrapController(scrapService);

        // 테스트용 사용자 등록
        userService.signUp("1", "1", "1", "1", BankType.KAKAO, "1");
        userService.signUp("2", "2", "2", "2", BankType.SHINHAN, "2");

        // App 실행 (시작 메뉴 열기)
        SwingUtilities.invokeLater(() ->
                new App(userController, donationPostController, scrapController));
    }
}
