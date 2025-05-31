package capstone.view.donation;

import capstone.controller.DonationPostController;
import capstone.model.User;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class DonationPostWriteView extends JFrame {
    public DonationPostWriteView(User user, DonationPostController controller, Runnable onPostWritten) {

        setTitle("기부글 작성");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JTextField imageField = new JTextField("donationImg.jpg");
        JTextField titleField = new JTextField();
        JTextField goalPointField = new JTextField();
        JTextField endDateField = new JTextField("YYYY-MM-DD");
        JTextArea contentArea = new JTextArea(6, 20);
        contentArea.setLineWrap(true);
        JScrollPane contentScroll = new JScrollPane(contentArea);

        JButton submitBtn = new JButton("등록");
        JButton cancelBtn = new JButton("취소");

        // 🔽 폼 구성
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        gbc.gridx = 0; gbc.gridy = row; formPanel.add(new JLabel("이미지 경로:"), gbc);
        gbc.gridx = 1; formPanel.add(imageField, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row; formPanel.add(new JLabel("제목:"), gbc);
        gbc.gridx = 1; formPanel.add(titleField, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row; formPanel.add(new JLabel("목표 금액:"), gbc);
        gbc.gridx = 1; formPanel.add(goalPointField, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row; formPanel.add(new JLabel("마감일:"), gbc);
        gbc.gridx = 1; formPanel.add(endDateField, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row; gbc.anchor = GridBagConstraints.NORTH;
        formPanel.add(new JLabel("내용:"), gbc);
        gbc.gridx = 1; formPanel.add(contentScroll, gbc);
        row++;

        JPanel btnPanel = new JPanel();
        btnPanel.add(cancelBtn);
        btnPanel.add(submitBtn);

        add(formPanel, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);

        cancelBtn.addActionListener(e -> dispose());

        submitBtn.addActionListener(e -> {
            try {
                String title = titleField.getText();
                String content = contentArea.getText();
                int goal = Integer.parseInt(goalPointField.getText());
                String img = imageField.getText();
                LocalDate endAt = LocalDate.parse(endDateField.getText());
                controller.createPost(user, img, goal, endAt, title, content);
                JOptionPane.showMessageDialog(this, "기부글이 등록되었습니다.");
                if (onPostWritten != null) onPostWritten.run();
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "입력값을 확인해주세요: " + ex.getMessage());
            }
        });
    }

}
