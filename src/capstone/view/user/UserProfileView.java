package capstone.view.user;

import capstone.controller.UserController;
import capstone.model.User;

import javax.swing.*;
import java.awt.*;

public class UserProfileView extends JFrame {
    public UserProfileView(User loginUser, UserController profileController) {
        setTitle("회원 정보 조회");
        setSize(400, 300);
        setLocationRelativeTo(null);

        // controller를 통해 사용자 정보 조회
        User user = profileController.getUserProfile(loginUser);

        if (user == null) {
            JOptionPane.showMessageDialog(this, "사용자 정보를 불러올 수 없습니다.", "오류", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        JPanel panel = new JPanel(new GridLayout(9, 2, 5, 5));
        panel.add(new JLabel("아이디:"));
        panel.add(new JLabel(user.getUserId()));

        panel.add(new JLabel("이름:"));
        panel.add(new JLabel(user.getName()));

        panel.add(new JLabel("닉네임:"));
        panel.add(new JLabel(user.getNickName()));

        panel.add(new JLabel("은행명:"));
        panel.add(new JLabel(user.getBankType().getBankName()));

        panel.add(new JLabel("계좌번호:"));
        panel.add(new JLabel(user.getBankAccount()));

        panel.add(new JLabel("등급(Tier):"));
        panel.add(new JLabel(user.getTier()));

        panel.add(new JLabel("보유 포인트:"));
        panel.add(new JLabel(String.valueOf(user.getPoint())));

        JButton closeButton = new JButton("닫기");
        panel.add(new JLabel());
        panel.add(closeButton);

        closeButton.addActionListener(e -> dispose());

        add(panel);
    }
}
