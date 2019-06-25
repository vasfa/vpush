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
public class FirebaseNotificationDTO {
    private int id;
    private String type;
    private boolean showActivity;
    private FirebaseNotificationActivityDTO activity;
    private String icon;
    private String image;
    private boolean turnScreenOn;
    private FirebaseNotificationNotifyDTO notify;
    private String title;
    private String summeryMessage;
    private String longMessage;
    private long date;
    private boolean show;

    private Boolean ButtonOne;
    private Boolean ButtonTwo;
    private Boolean ButtonThree;

    private String ButtonTitleOne;
    private String ButtonTitleTwo;
    private String ButtonTitleThree;
}
