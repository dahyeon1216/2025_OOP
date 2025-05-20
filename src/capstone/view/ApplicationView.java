package capstone.view;

import capstone.service.DonationPostService;
import capstone.service.UserService;

import javax.swing.*;
import java.awt.*;

public class ApplicationView extends JFrame {
    public ApplicationView(UserService userService, DonationPostService donationService) {
        setTitle("시작 창");
        setSize(300, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JButton openAppBtn = new JButton("애플리케이션 실행");

        openAppBtn.addActionListener(e -> {
            new LoginView(userService, donationService).setVisible(true);
        });

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(openAppBtn, BorderLayout.CENTER);
        add(panel);
    }
}