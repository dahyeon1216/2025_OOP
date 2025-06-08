package capstone.view.donation;

//기부글 수정하는 view

import capstone.controller.DonationPostController;
import capstone.model.DonationPost;
import capstone.model.Tier;
import capstone.model.User;
import capstone.service.DonationPostService;
import capstone.view.Roundborder.RoundedBorder;
import capstone.view.Roundborder.RoundedButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.stream.IntStream;

import static capstone.model.BankType.KB;

public class DonationPostEditView extends JFrame {
    public interface EditCallback {
        void onEdited();
    }

    //커스텀 폰트 로딩
    private static Font customFont;
    static {
        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/font1.ttf")).deriveFont(15f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
        } catch (Exception e) {
            customFont = new Font("SansSerif", Font.PLAIN, 15); // fallback
            e.printStackTrace();
        }
    }

    public DonationPostEditView(DonationPost post, User user, DonationPostController controller, EditCallback callback) {
        super("기부글 수정하기");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(393, 698); // 9:16 비율 적용
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);

        // Header
        JPanel header = new JPanel(null);
        header.setPreferredSize(new Dimension(393, 45)); // 높이 45px
        header.setBackground(new Color(120, 230, 170));

        //뒤로가기 버튼
        ImageIcon backIcon = new ImageIcon("icons/arrow-leftb.png");
        Image scaledImg = backIcon.getImage().getScaledInstance(26, 26, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(scaledImg);

        JButton backBtn = new JButton(resizedIcon);
        backBtn.setBorderPainted(false);
        backBtn.setContentAreaFilled(false);
        backBtn.setFocusPainted(false);
        backBtn.setBounds(5, 6, 40, 30);
        backBtn.addActionListener(e -> dispose());
        header.add(backBtn);

        //헤더 내 텍스트
        JLabel titleLbl = new JLabel("기부글 수정하기", SwingConstants.CENTER);
        titleLbl.setFont(customFont.deriveFont(Font.BOLD, 23f));
        titleLbl.setBounds(13, 7, 360, 30);
        header.add(titleLbl);

        // Body
        JPanel body = new JPanel(null);
        body.setPreferredSize(new Dimension(393, 653));
        body.setBackground(Color.WHITE);

        // 이미지 박스 (회색 사각형)
        JLabel imagePreviewLabel = new JLabel();
        imagePreviewLabel.setOpaque(true); //내부 채우기 활성화
        imagePreviewLabel.setBackground(new Color(240, 240, 240));
        imagePreviewLabel.setBorder(new RoundedBorder(15));
        imagePreviewLabel.setBounds(20, 20, 70, 70);

        imagePreviewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imagePreviewLabel.setVerticalAlignment(SwingConstants.CENTER);
        imagePreviewLabel.setLayout(null);

        // ➕ 버튼
        ImageIcon plusIcon = new ImageIcon("icons/plus-icon.png");
        Image scaledPlus = plusIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        JButton chooseImageBtn = new JButton(new ImageIcon(scaledPlus));
        chooseImageBtn.setBounds(75, 20, 80, 60);
        chooseImageBtn.setFocusPainted(false);
        chooseImageBtn.setContentAreaFilled(false);
        chooseImageBtn.setBorderPainted(false);

        final File[] selectedImageFile = {null}; // 선택된 이미지 파일 저장

        chooseImageBtn.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("이미지 선택");
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                selectedImageFile[0] = fileChooser.getSelectedFile();
                //imageField.setText(selectedImageFile[0].getName());

                // 미리보기
                ImageIcon icon = new ImageIcon(new ImageIcon(selectedImageFile[0].getAbsolutePath()).getImage().getScaledInstance(200, 150, Image.SCALE_SMOOTH));
                imagePreviewLabel.setIcon(icon);
            }
        });

        body.add(chooseImageBtn);
        body.add(imagePreviewLabel);

        // 제목 필드
        JLabel titleLabel = new JLabel("제목");
        titleLabel.setFont(customFont.deriveFont(Font.BOLD, 22f));
        titleLabel.setBounds(18, 100, 100, 22);
        body.add(titleLabel);

        JTextField titleField = new JTextField("제목을 입력하세요");
        titleField.setBackground(new Color(240, 240, 240)); // 텍스트 필드 배경: 연한 회색
        titleField.setForeground(Color.BLACK); // 텍스트 색
        titleField.setBorder(new RoundedBorder(15)); // 둥근 테두리
        titleField.addFocusListener(placeholderAdapter("제목을 입력하세요"));
        titleField.setBounds(18, 127, 325, 35);
        body.add(titleField);
        body.add(Box.createVerticalStrut(10));

        // 목표금액 필드
        JLabel goalLabel = new JLabel("목표금액");
        goalLabel.setFont(customFont.deriveFont(Font.BOLD, 22f));
        goalLabel.setBounds(18, 175, 100, 22);
        body.add(goalLabel);

        //목표금액 텍스트 필드
        JPanel moneyPanel = new JPanel(new BorderLayout());
        JTextField goalPointField = new JTextField("목표 금액을 입력하세요");
        goalPointField.setBackground(new Color(240, 240, 240)); //배경색
        goalPointField.setForeground(Color.BLACK); //텍스트 색
        goalPointField.setBorder(new RoundedBorder(15)); // 둥근 테두리
        goalPointField.addFocusListener(placeholderAdapter("목표 금액을 입력하세요"));
        goalPointField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));

        // P 텍스트
        JLabel currencyLbl = new JLabel(" P");
        currencyLbl.setBorder(new EmptyBorder(0, 5, 0, 0));
        currencyLbl.setOpaque(true);
        currencyLbl.setBackground(Color.WHITE);
        currencyLbl.setFont(new Font("맑은 고딕", Font.BOLD, 18));

        moneyPanel.add(goalPointField, BorderLayout.CENTER);
        moneyPanel.add(currencyLbl, BorderLayout.EAST);
        moneyPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        moneyPanel.setBounds(18, 205, 330, 35);
        body.add(moneyPanel);
        body.add(Box.createVerticalStrut(10));

        // 기한 필드
        JLabel dateLabel = new JLabel("기한");
        dateLabel.setFont(customFont.deriveFont(Font.BOLD, 22f));
        dateLabel.setBounds(18, 250, 100, 22);
        body.add(dateLabel);

        // 날짜 패널 (Null layout로 배치 수동 조정)
        JPanel datePanel = new JPanel(null);
        datePanel.setBounds(18, 278, 330, 40);
        datePanel.setBackground(Color.WHITE); // 배경 흰색
        body.add(datePanel);

        // 콤보박스 스타일용 폰트
        Font comboFont = customFont.deriveFont(Font.PLAIN, 18f);
        Color bgColor = new Color(240, 240, 240);

        // 년도 콤보박스
        JComboBox<Integer> yearCb = new JComboBox<>();
        IntStream.range(LocalDate.now().getYear(), LocalDate.now().getYear() + 6)
                .forEach(yearCb::addItem);
        yearCb.setRenderer(createSuffixRenderer("년")); // ← 렌더러 적용
        yearCb.setBounds(0, 0, 100, 30);
        yearCb.setBackground(bgColor);
        yearCb.setFont(comboFont);
        yearCb.setFocusable(false);
        datePanel.add(yearCb);

        // 월 콤보박스
        JComboBox<Integer> monthCb = new JComboBox<>();
        IntStream.rangeClosed(1, 12)
                .forEach(monthCb::addItem);
        monthCb.setRenderer(createSuffixRenderer("월")); // ← 렌더러 적용
        monthCb.setBounds(115, 0, 100, 30);
        monthCb.setBackground(bgColor);
        monthCb.setFont(comboFont);
        monthCb.setFocusable(false);
        datePanel.add(monthCb);

        // 일 콤보박스
        JComboBox<Integer> dayCb = new JComboBox<>();
        IntStream.rangeClosed(1, 31)
                .forEach(dayCb::addItem);
        dayCb.setRenderer(createSuffixRenderer("일")); // ← 렌더러 적용
        dayCb.setBounds(230, 0, 100, 30);
        dayCb.setBackground(bgColor);
        dayCb.setFont(comboFont);
        dayCb.setFocusable(false);
        datePanel.add(dayCb);

        // 설명 필드
        JLabel descLabel = new JLabel("자세한 설명");
        descLabel.setFont(customFont.deriveFont(Font.BOLD, 22f));
        descLabel.setBounds(18, 323, 100, 22);
        body.add(descLabel);

        JTextArea contentArea = new JTextArea(9, 20);
        contentArea.setBackground(new Color(240, 240, 240)); // 텍스트 area 배경: 연한 회색
        contentArea.setBorder(new RoundedBorder(15)); // 둥근 테두리
        JScrollPane scroll = new JScrollPane(contentArea);
        scroll.setBorder(null);
        scroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        scroll.setBounds(18, 357, 330, 200);
        body.add(scroll);

        // 저장완료버튼 생성 및 설정
        RoundedButton saveBtn = new RoundedButton("수정 완료", new Color(60, 60, 60), 30);
        saveBtn.setPreferredSize(new Dimension(0, 44));
        saveBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        saveBtn.setFont(customFont.deriveFont(Font.BOLD, 20f));
        saveBtn.setForeground(Color.WHITE);

        // footer 설정
        JPanel footer = new JPanel();
        footer.setBorder(new EmptyBorder(10, 20, 20, 20));
        footer.setLayout(new BorderLayout());
        footer.setOpaque(false);
        footer.add(saveBtn, BorderLayout.CENTER);

        // 전체 조립
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(header, BorderLayout.NORTH);
        getContentPane().add(body, BorderLayout.CENTER);
        getContentPane().add(footer, BorderLayout.SOUTH);


        //이 코드는 뒤로가기버튼 리스너를 통해서 구현 가능할 듯 합니다
        // cancelBtn.addActionListener(e -> dispose());

        saveBtn.addActionListener(e -> {
            try {
                String title = titleField.getText();
                String content = contentArea.getText();
                int goal = Integer.parseInt(goalPointField.getText());

                //기한을 콤보박스로 받아서 이 코드 필요함
                int year = (Integer) yearCb.getSelectedItem();
                int month = (Integer) monthCb.getSelectedItem();
                int day = (Integer) dayCb.getSelectedItem();

                LocalDate endAt = LocalDate.of(year, month, day);

                //이 부분은 제 view 구현 코드에 맞게 사진 받아올 수 있도록 수정했습니다
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

                controller.updatePost(post.getId(), title, content, savedFileName, goal, endAt);

                if (callback != null) callback.onEdited();
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "입력값을 확인하세요: " + ex.getMessage());
            }
        });


        // 스크롤 기능 추가
        JScrollPane scrollPane = new JScrollPane(
                body,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER // 가로 스크롤 끔
        );
        scrollPane.setBorder(null);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    //여기부터는 부가적으로 view에 필요한 코드입니다
    private FocusAdapter placeholderAdapter(String placeholder) {
        return new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                JTextField tf = (JTextField) e.getComponent();
                if (tf.getText().equals(placeholder)) tf.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {
                JTextField tf = (JTextField) e.getComponent();
                if (tf.getText().isBlank()) tf.setText(placeholder);
            }
        };
    }

    private ListCellRenderer<? super Integer> createSuffixRenderer(String suffix) {
        return new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                String display = value == null ? "" : value + suffix;
                return super.getListCellRendererComponent(list, display, index, isSelected, cellHasFocus);
            }
        };
    }

}
