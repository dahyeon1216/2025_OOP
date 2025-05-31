package capstone.view.donation;

import capstone.controller.DonationPostController;
import capstone.model.DonationPost;
import capstone.model.User;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class DonationPostEditView extends JFrame {
    public interface EditCallback {
        void onEdited();
    }

    public DonationPostEditView(DonationPost post, User user, DonationPostController controller, EditCallback callback) {
        setTitle("기부글 수정");
        setSize(500, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JTextField titleField = new JTextField(post.getTitle());
        JTextArea contentArea = new JTextArea(post.getContent());
        JTextField goalField = new JTextField(String.valueOf(post.getGoalPoint()));
        JTextField imageField = new JTextField(post.getDonationImg());
        JTextField endDateField = new JTextField(post.getEndAt() != null ? post.getEndAt().toString() : "");

        JButton saveBtn = new JButton("저장");
        JButton cancelBtn = new JButton("취소");

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        formPanel.add(new JLabel("제목:"));
        formPanel.add(titleField);
        formPanel.add(new JLabel("내용:"));
        formPanel.add(new JScrollPane(contentArea));
        formPanel.add(new JLabel("목표 금액:"));
        formPanel.add(goalField);
        formPanel.add(new JLabel("이미지 경로:"));
        formPanel.add(imageField);
        formPanel.add(new JLabel("마감일 (YYYY-MM-DD):"));
        formPanel.add(endDateField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(cancelBtn);
        buttonPanel.add(saveBtn);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        cancelBtn.addActionListener(e -> dispose());

        saveBtn.addActionListener(e -> {
            try {
                String title = titleField.getText();
                String content = contentArea.getText();
                int goal = Integer.parseInt(goalField.getText());
                String image = imageField.getText();
                LocalDate endAt = LocalDate.parse(endDateField.getText());

                controller.updatePost(post.getId(), title, content, image, goal, endAt);

                if (callback != null) callback.onEdited();
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "입력값을 확인하세요: " + ex.getMessage());
            }
        });
    }
}
