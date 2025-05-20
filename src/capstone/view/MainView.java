package capstone.view;

import capstone.controller.DonationPostController;
import capstone.controller.UserController;
import capstone.model.User;
import capstone.service.UserService;

import javax.swing.*;
import java.awt.*;

public class MainView extends JFrame {
    private User loginUser;
    private final UserController userController;
    private final DonationPostController donationPostController;
    private JMenu sessionMenu;

    private JMenuItem profileView, profileEdit, pointCharge;
    private JMenuItem myPosts, myScraps, myDonations;
    private JMenuItem writePost, listPosts;

    private JPanel centerPanel;

    public MainView(User loginUser, UserController userController, DonationPostController donationPostController) {
        this.loginUser = loginUser;
        this.userController = userController;
        this.donationPostController = donationPostController;

        setTitle("메인 메뉴" + (loginUser != null ? " - " + loginUser.getUserId() : " (비로그인)"));
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JMenuBar menuBar = new JMenuBar();

        sessionMenu = new JMenu(loginUser != null ? loginUser.getUserId() : "로그인/회원가입");
        JMenuItem loginItem = new JMenuItem("로그인");
        JMenuItem joinItem = new JMenuItem("회원가입");
        JMenuItem logoutItem = new JMenuItem("로그아웃");
        sessionMenu.add(loginItem);
        sessionMenu.add(joinItem);
        sessionMenu.add(logoutItem);

        loginItem.addActionListener(e -> {
            new LoginView(userController, user -> {
                this.loginUser = user;
                setTitle("메인 메뉴 - " + user.getUserId());
                sessionMenu.setText(user.getUserId());
                JOptionPane.showMessageDialog(this, "로그인 성공");
                updateMenuAccess();
            }).setVisible(true);
        });

        joinItem.addActionListener(e -> {
            new SignupView(userController).setVisible(true);
        });

        logoutItem.addActionListener(e -> {
            if (this.loginUser != null) {
                this.loginUser = null;
                setTitle("메인 메뉴 (비로그인)");
                sessionMenu.setText("로그인/회원가입");
                JOptionPane.showMessageDialog(this, "로그아웃 되었습니다.");
                updateMenuAccess();
            } else {
                JOptionPane.showMessageDialog(this, "현재 로그인 상태가 아닙니다.");
            }
        });

        JMenu userInfoMenu = new JMenu("사용자 정보");
        profileView = new JMenuItem("회원 정보 조회");
        profileEdit = new JMenuItem("회원 정보 수정");
        pointCharge = new JMenuItem("포인트 충전");
        userInfoMenu.add(profileView);
        userInfoMenu.add(profileEdit);
        userInfoMenu.add(pointCharge);

        profileView.addActionListener(e -> {
            if (this.loginUser != null)
                new UserProfileView(this.loginUser, userController).setVisible(true);
            else
                JOptionPane.showMessageDialog(this, "로그인 후 이용 가능합니다.");
        });
        profileEdit.addActionListener(e -> {
            if (this.loginUser != null)
                new UserProfileEditView(this.loginUser, userController).setVisible(true);
            else
                JOptionPane.showMessageDialog(this, "로그인 후 이용 가능합니다.");
        });
        pointCharge.addActionListener(e -> {
            if (this.loginUser != null)
                new PointChargeView(this.loginUser).setVisible(true);
            else
                JOptionPane.showMessageDialog(this, "로그인 후 이용 가능합니다.");
        });

        JMenu myDonationMenu = new JMenu("나의 기부글");
        myPosts = new JMenuItem("내가 쓴 기부글 조회");
        myScraps = new JMenuItem("스크랩한 기부글 조회 (구현 예정)");
        myDonations = new JMenuItem("기부 내역 조회 (구현 예정)");
        myDonationMenu.add(myPosts);
        myDonationMenu.add(myScraps);
        myDonationMenu.add(myDonations);

        myPosts.addActionListener(e -> {
            if (this.loginUser != null) {
                swapCenterPanel(new MyDonationPostListPanel(donationPostController, this.loginUser).getContentPane());
            } else {
                JOptionPane.showMessageDialog(this, "로그인 후 이용 가능합니다.");
            }
        });

        JMenu writeMenu = new JMenu("기부글 쓰기");
        writePost = new JMenuItem("기부글 작성하기");
        writeMenu.add(writePost);

        writePost.addActionListener(e -> {
            if (this.loginUser != null) {
                new DonationPostWriteView(this.loginUser, donationPostController).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "로그인 후 이용 가능합니다.");
            }
        });

        JMenu detailMenu = new JMenu("기부글 상세보기 (구현 예정)");
        JMenuItem allPostsItem = new JMenuItem("기부글 전체보기");
        detailMenu.add(allPostsItem);

        allPostsItem.addActionListener(e -> {
            swapCenterPanel(new DonationPostListPanel(donationPostController));
        });

        menuBar.add(sessionMenu);
        menuBar.add(userInfoMenu);
        menuBar.add(myDonationMenu);
        menuBar.add(writeMenu);
        menuBar.add(detailMenu);

        setJMenuBar(menuBar);

        centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(new DonationPostListPanel(donationPostController), BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        updateMenuAccess();
    }

    private void updateMenuAccess() {
        boolean loggedIn = (loginUser != null);
        profileView.setEnabled(loggedIn);
        profileEdit.setEnabled(loggedIn);
        pointCharge.setEnabled(loggedIn);

        myPosts.setEnabled(loggedIn);
        myScraps.setEnabled(loggedIn);
        myDonations.setEnabled(loggedIn);

        writePost.setEnabled(loggedIn);
    }

    private void swapCenterPanel(Component newPanel) {
        centerPanel.removeAll();
        centerPanel.add(newPanel, BorderLayout.CENTER);
        centerPanel.revalidate();
        centerPanel.repaint();
    }
}