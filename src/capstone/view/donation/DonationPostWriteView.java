package capstone.view.donation;

import capstone.controller.DonationPostController;
import capstone.model.User;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.time.LocalDate;

public class DonationPostWriteView extends JFrame {
    public DonationPostWriteView(User user, DonationPostController controller, Runnable onPostWritten) {

        setTitle("기부글 작성");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JTextField imageField = new JTextField();
        imageField.setEditable(false); // 사용자가 직접 입력하지 못하게

        JButton chooseImageBtn = new JButton("이미지 선택");
        JLabel imagePreviewLabel = new JLabel();

        JTextField titleField = new JTextField();
        JTextField goalPointField = new JTextField();
        JTextField endDateField = new JTextField("YYYY-MM-DD");
        JTextArea contentArea = new JTextArea(6, 20);
        contentArea.setLineWrap(true);
        JScrollPane contentScroll = new JScrollPane(contentArea);

        JButton submitBtn = new JButton("등록");
        JButton cancelBtn = new JButton("취소");

        final File[] selectedImageFile = {null}; // 선택된 이미지 파일 저장

        chooseImageBtn.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("이미지 선택");
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                selectedImageFile[0] = fileChooser.getSelectedFile();
                imageField.setText(selectedImageFile[0].getName());

                // 미리보기
                ImageIcon icon = new ImageIcon(new ImageIcon(selectedImageFile[0].getAbsolutePath()).getImage().getScaledInstance(200, 150, Image.SCALE_SMOOTH));
                imagePreviewLabel.setIcon(icon);
            }
        });

        // 🔽 폼 구성
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        gbc.gridx = 0; gbc.gridy = row; formPanel.add(new JLabel("이미지:"), gbc);
        gbc.gridx = 1; formPanel.add(imageField, gbc);
        gbc.gridx = 2; formPanel.add(chooseImageBtn, gbc);
        row++;

        gbc.gridx = 1; gbc.gridy = row; formPanel.add(imagePreviewLabel, gbc);
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
                LocalDate endAt = LocalDate.parse(endDateField.getText());

                String savedFileName = null;
                if (selectedImageFile[0] != null) {
                    String uploadDir = "resources/images";
                    File targetDir = new File(uploadDir);
                    if (!targetDir.exists()) targetDir.mkdirs();

                    // 파일명 중복 방지 (타임스탬프 등으로)
                    String fileName = System.currentTimeMillis() + "_" + selectedImageFile[0].getName();
                    File destFile = new File(targetDir, fileName);
                    Files.copy(selectedImageFile[0].toPath(), destFile.toPath());
                    savedFileName = fileName;
                }

                controller.createPost(user, savedFileName, goal, endAt, title, content);
                JOptionPane.showMessageDialog(this, "기부글이 등록되었습니다.");
                if (onPostWritten != null) onPostWritten.run();
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "입력값을 확인해주세요: " + ex.getMessage());
            }
        });
    }
}
