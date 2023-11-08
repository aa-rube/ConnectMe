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
    
}