package capstone.model;

public enum Tier {
    BRONZE("🥉Bronze"),
    SILVER("🥈Silver"),
    GOLD("🏅Gold"),
    PLATINUM("🎖️Platinum");

    private final String msg;

    Tier(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
