package ir.vasfa.vpush.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import ir.vasfa.vpush.R;

public class EmailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        Bundle exBundle = getIntent().getExtras();
        String[] emailAccoutTo = exBundle.getStringArray("emailAccountTo");
        String[] emailAccoutCC = exBundle.getStringArray("emailAccountCC");
        String emailTitle = exBundle.getString("emailTitle");
        String emailmessage = exBundle.getString("emailMessage");
        String emailChosserTitle = exBundle.getString("emailChooserTitle");

        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, emailAccoutTo);
        if (emailAccoutCC != null && emailAccoutCC.length > 0)
            emailIntent.putExtra(Intent.EXTRA_CC, emailAccoutCC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, emailTitle);
        emailIntent.putExtra(Intent.EXTRA_TEXT, emailmessage);
//        Intent.createChooser(emailIntent, emailChosserTitle);
        startActivity(Intent.createChooser(emailIntent, emailChosserTitle));
        finish();


    }
}
