
public class Player {
    private String name;
    private int score, time;

    public Player(String name, int score, int time) {
        this.name = name;
        this.score = score;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public int getTime() {
        return time;
    }
}
