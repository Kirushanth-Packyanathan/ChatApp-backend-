package chatroom.chat.model;
// comment

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Message {
    @Id
    @GeneratedValue
    private Long messageId;
    private String senderName;
    private String receiverName;
    private String message;
    private String date;
    private Status status;
}
