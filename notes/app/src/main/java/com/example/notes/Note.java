package com.example.notes;

public class Note {
    private long id;
    private String heading;
    private String details;

    // Constructor mặc định (cần thiết cho ORM hoặc Firebase)
    public Note() {}

    // Constructor đầy đủ
    public Note(long id, String heading, String details) {
        this.id = id;
        this.heading = heading;
        this.details = details;
    }

    // Constructor không có ID (khi tạo mới)
    public Note(String heading, String details) {
        this.heading = heading;
        this.details = details;
    }

    // Getter và Setter
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    // Ghi đè toString() để debug dễ hơn
    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", heading='" + heading + '\'' +
                ", details='" + details + '\'' +
                '}';
    }
}
