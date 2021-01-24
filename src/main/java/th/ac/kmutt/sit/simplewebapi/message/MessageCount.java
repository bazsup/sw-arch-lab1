package th.ac.kmutt.sit.simplewebapi.message;

public class MessageCount {
    private String text;

    private int count;

    public MessageCount(String text, int count) {
        this.text = text;
        this.count = count;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
