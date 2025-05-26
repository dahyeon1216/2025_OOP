package capstone.view.donation;

import capstone.controller.DonationPostController;
import capstone.model.User;
import capstone.view.Roundborder.RoundedBorder;
import capstone.view.Roundborder.RoundedButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
import java.time.LocalDate;
import java.util.stream.IntStream;


public class DonationPostWriteView extends JFrame {

    private JFrame previousView; //전 화면
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

    public DonationPostWriteView(JFrame previousView,User user, DonationPostController controller) {
        super("기부글 쓰기");
        this.previousView = previousView;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(393, 698); // 9:16 비율 적용
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);

        // Header
        JPanel header = new JPanel(null);
        header.setPreferredSize(new Dimension(393, 45)); // 높이 45px
        header.setBackground(new Color(120, 230, 170));


        //뒤로가기 버튼

        ImageIcon backIcon = new ImageIcon("icons/arrow-left.png");
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
        JLabel titleLbl = new JLabel("기부글 쓰기",SwingConstants.CENTER);
        titleLbl.setFont(customFont.deriveFont(Font.BOLD, 23f));
        titleLbl.setBounds(13, 7, 360, 30);
        header.add(titleLbl);
        //header.add(Box.createHorizontalGlue());  // 오른쪽 여백

        // Body
        JPanel body = new JPanel(null);
        body.setPreferredSize(new Dimension(393, 653)); // 필요 시 조정
        body.setBackground(Color.WHITE);

        // 이미지 박스 (회색 사각형)
        JLabel imageLabel = new JLabel();
        imageLabel.setOpaque(true); //내부 채우기 활성화
        imageLabel.setBackground(new Color(240, 240, 240));
        imageLabel.setBorder(new RoundedBorder(15));
        imageLabel.setBounds(20,20,70,70);

        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        imageLabel.setLayout(null); // 내부 버튼을 위해 유지

// ➕ 버튼
        ImageIcon plusIcon = new ImageIcon("icons/plus-icon.png");
        Image scaledPlus = plusIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        JButton addImgBtn = new JButton(new ImageIcon(scaledPlus));
        addImgBtn.setBounds(75, 20, 80, 60);
        addImgBtn.setFocusPainted(false);
        addImgBtn.setContentAreaFilled(false);
        addImgBtn.setBorderPainted(false);

        addImgBtn.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            int result = chooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = chooser.getSelectedFile();
                ImageIcon img = new ImageIcon(selectedFile.getAbsolutePath());
                Image scaled = img.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(scaled));
                imageLabel.setText(null);
            }
        });

        body.add(addImgBtn);
        body.add(imageLabel); // 위치 조정된 라벨 추가

        // 제목 필드
        JLabel titleLabel = new JLabel("제목");
        titleLabel.setFont(customFont.deriveFont(Font.BOLD, 22f));
        titleLabel.setBounds(18,100,100,22 );
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
        JTextField goalField = new JTextField("목표 금액을 입력하세요");
        goalField.setBackground(new Color(240, 240, 240)); //배경색
        goalField.setForeground(Color.BLACK); //텍스트 색
        goalField.setBorder(new RoundedBorder(15)); // 둥근 테두리
        goalField.addFocusListener(placeholderAdapter("목표 금액을 입력하세요"));
        goalField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));

        // P 텍스트
        JLabel currencyLbl = new JLabel(" P");
        currencyLbl.setBorder(new EmptyBorder(0, 5, 0, 0));
        currencyLbl.setOpaque(true);
        currencyLbl.setBackground(Color.WHITE);
        currencyLbl.setFont(new Font("맑은 고딕", Font.BOLD, 18));

        moneyPanel.add(goalField, BorderLayout.CENTER);
        moneyPanel.add(currencyLbl, BorderLayout.EAST);
        moneyPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        moneyPanel.setBounds(18,205, 330, 35);
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
        JComboBox<String> yearCb = new JComboBox<>();
        IntStream.range(LocalDate.now().getYear(), LocalDate.now().getYear() + 6)
                .mapToObj(y -> y + "년")
                .forEach(yearCb::addItem);
        yearCb.setBounds(0, 0, 100, 30);
        yearCb.setBackground(bgColor);
        yearCb.setFont(comboFont);
        yearCb.setFocusable(false);
        datePanel.add(yearCb);

// 월 콤보박스
        JComboBox<String> monthCb = new JComboBox<>();
        IntStream.rangeClosed(1, 12)
                .mapToObj(m -> m + "월")
                .forEach(monthCb::addItem);
        monthCb.setBounds(115, 0, 100, 30);
        monthCb.setBackground(bgColor);
        monthCb.setFont(comboFont);
        monthCb.setFocusable(false);
        datePanel.add(monthCb);

// 일 콤보박스
        JComboBox<String> dayCb = new JComboBox<>();
        IntStream.rangeClosed(1, 31)
                .mapToObj(d -> d + "일")
                .forEach(dayCb::addItem);
        dayCb.setBounds(230, 0, 100, 30);
        dayCb.setBackground(bgColor);
        dayCb.setFont(comboFont);
        dayCb.setFocusable(false);
        datePanel.add(dayCb);


        // 설명 필드
        JLabel descLabel = new JLabel("자세한 설명");
        descLabel.setFont(customFont.deriveFont(Font.BOLD, 22f));
        descLabel.setBounds(18,323,100,22);
        body.add(descLabel);

        JTextArea contentArea = new JTextArea(9, 20);
        contentArea.setBackground(new Color(240, 240, 240)); // 텍스트 area 배경: 연한 회색
        contentArea.setBorder(new RoundedBorder(15)); // 둥근 테두리
        JScrollPane scroll = new JScrollPane(contentArea);
        scroll.setBorder(null);
        scroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        scroll.setBounds(18, 357,330,200);
        body.add(scroll);

        // 저장완료버튼 생성 및 설정
        RoundedButton submitBtn = new RoundedButton("저장완료", new Color(60, 60, 60), 30);
        submitBtn.setPreferredSize(new Dimension(0, 44));
        submitBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        submitBtn.setFont(customFont.deriveFont(Font.BOLD, 20f));
        submitBtn.setForeground(Color.WHITE);

        // footer 설정
        JPanel footer = new JPanel();
        footer.setBorder(new EmptyBorder(10, 20, 20, 20));
        footer.setLayout(new BorderLayout());
        footer.setOpaque(false);
        footer.add(submitBtn, BorderLayout.CENTER);

        // 전체 조립
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(header, BorderLayout.NORTH);
        getContentPane().add(body, BorderLayout.CENTER);
        getContentPane().add(footer, BorderLayout.SOUTH);

        backBtn.addActionListener(e -> {
            previousView.setVisible(true); // 이전 화면 다시 보여주기
            dispose(); // 현재 화면 닫기
        });

        submitBtn.addActionListener(e -> {
            try {
                String imgPath = "donationImg.jpg";
                String title = titleField.getText();
                String goalText = goalField.getText().replaceAll("[^0-9]", "");
                int goal = Integer.parseInt(goalText);
                LocalDate endAt = LocalDate.of(
                        (Integer) yearCb.getSelectedItem(),
                        (Integer) monthCb.getSelectedItem(),
                        (Integer) dayCb.getSelectedItem()
                );
                String content = contentArea.getText();
                controller.createPost(user, imgPath, goal, endAt, title, content);
                JOptionPane.showMessageDialog(this, "기부글이 등록되었습니다.");
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "입력값을 확인해주세요: " + ex.getMessage());
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

    //UI 테스트용 main
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new DonationPostWriteView(null,null, null);
        });
    }
}
