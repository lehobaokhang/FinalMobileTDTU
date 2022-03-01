package lehobaokhang.tdtu.finalterm.model;

import java.io.Serializable;

public class Diary implements Serializable {
    private String id;
    private String weather;
    private String date;
    private String reaction;
    private String title;
    private String content;
    private String color;

    public Diary(){}

    public Diary(String id, String weather, String date, String reaction, String title, String content, String color) {
        this.id = id;
        this.weather = weather;
        this.date = date;
        this.reaction = reaction;
        this.title = title;
        this.content = content;
        this.color = color;
    }

    public String getWeather() {
        return weather;
    }

    public String getDate() {
        return date;
    }

    public String getReaction() {
        return reaction;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setReaction(String reaction) {
        this.reaction = reaction;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() { return id; }

    public void setId(String id) {
        this.id = id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
