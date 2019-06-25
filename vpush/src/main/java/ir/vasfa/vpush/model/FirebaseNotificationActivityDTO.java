package ir.vasfa.vpush.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FirebaseNotificationActivityDTO {
    private int type;
    private String content;
    private String imageURL;
    private String linkToShowSite;
    private String responderID;
    private String questionID;
    private String responderName;
    private String questionStatus;
}
