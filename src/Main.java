import capstone.controller.DonationPostController;
import capstone.controller.UserController;
import capstone.model.BankType;
import capstone.model.User;
import capstone.service.DonationPostService;
import capstone.service.UserService;
import capstone.view.main.DonationPostListView;
import capstone.view.main.MainView;

import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {
    private User loginUser;
    private final UserController userController;
    private final DonationPostController donationPostController;
    private DonationPostListView donationPostListView;

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

    //이 부분 나중에 로그인 화면으로 넘어가게 로직 수정하기
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // 1. 서비스 인스턴스 초기화 (싱글톤 인스턴스 가져오기)
            //    모든 곳에서 동일한 서비스 인스턴스를 사용하게 됩니다.
            UserService userService = UserService.getInstance();
            DonationPostService donationService = DonationPostService.getInstance();

            // 2. 컨트롤러 인스턴스 초기화 (서비스 주입)
            UserController userController = new UserController(userService);
            DonationPostController donationPostController = new DonationPostController(donationService);

            // 3. 초기 사용자 등록 (테스트 용도)
            //    이 부분은 앱의 로그인/회원가입 로직으로 대체.
            //    현재 로그인할 유저가 필요합니다.
            //    만약 앱 시작 시 로그인 화면이 있다면, 로그인 후 user 객체를 받아와야 합니다.
            //    여기서는 임시로 미리 가입된 유저를 가져오거나 생성합니다.
            User initialLoginUser = userService.findById("userId"); // 또는 로그인 성공 후 반환되는 User 객체
             if (initialLoginUser == null) { // 유저가 없다면 생성 (테스트용)
                userService.signUp("1", "1", "테스트유저1", "url1", BankType.KAKAO, "123-456");
                initialLoginUser = userService.findById("1");
            }
            initialLoginUser.setPoint(100000); // 테스트용 포인트 설정 (기존 포인트와 합산될 수 있으니 주의)*/


            // 4. 애플리케이션의 시작 뷰 실행
            //   바로 DonationPostListView를 띄움
            new DonationPostListView(initialLoginUser, donationPostController, null); // previousView는 null
        });
    }

}