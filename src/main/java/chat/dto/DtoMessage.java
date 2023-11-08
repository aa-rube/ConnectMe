package chat.dto;
public class DtoMessage {
    private String text;
    private String dateTime;
    private String userName;
    private String status;

    public DtoMessage(String text, String dateTime, String userName, String status) {
        this.text = text;
        this.dateTime = dateTime;
        this.userName = userName;
        this.status = status;
    }

    public DtoMessage() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}