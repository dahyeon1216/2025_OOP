import capstone.model.BankType;
import capstone.service.DonationPostService;
import capstone.service.UserService;
import capstone.view.ApplicationView;

public class Main {
    public static void main(String[] args) {
        UserService userService = new UserService();
        DonationPostService donationService = new DonationPostService();

        // 초기 사용자 등록
        userService.signUp("1", "1", "1", "1", BankType.KAKAO, "1");
        userService.signUp("2", "2", "2", "2", BankType.SHINHAN, "2");

        // 시작 화면 실행
        new ApplicationView(userService, donationService).setVisible(true);
    }
}