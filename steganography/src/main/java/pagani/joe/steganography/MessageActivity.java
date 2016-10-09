package pagani.joe.steganography;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by User on 10/8/2016.
 */

public class MessageActivity extends Activity implements View.OnClickListener {
    public static Context mContext;
    private Button backBtn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message);
        mContext = this;
        backBtn = (Button) findViewById(R.id.backBtn);
        backBtn.setOnClickListener(this);
        TextView box = (TextView) findViewById(R.id.messageBox);
        String message="hey";
        box.setText(message);
    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtn:
                finish();
                break;
        }
    }
}
