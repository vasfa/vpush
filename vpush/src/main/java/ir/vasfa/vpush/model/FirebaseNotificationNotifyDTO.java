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
public class FirebaseNotificationNotifyDTO {
    private int type;
    private String content;
    private String packageName;
    private String url;
    private String activityName;
    private String number;
    private String ussdCode;
    private String appName;

    private String emailAccountTo;
    private String emailAccountCC;
    private String emailTitle;
    private String emailMessage;
    private String emailChooserTitle;
}
