package pagani.joe.steganography;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by Joe on 10/8/2016.
 */

public class DecryptActivity extends Activity implements View.OnClickListener {
    public static Context mContext;
    private Button selectImg;
    private Button decryptBtn;
    //private Intent openImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.decrypt);
        mContext = this;
        selectImg = (Button) findViewById(R.id.selectImgBtn);
        selectImg.setOnClickListener(this);
        decryptBtn = (Button) findViewById(R.id.decryptBtn);
        decryptBtn.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.selectImgBtn:
                //go encrypt
                selectImage();
                break;
            case R.id.decryptBtn:
                decryptActivity();
                break;
        }
    }

    public void selectImage() {
        Intent openImg = new Intent();
        openImg.setAction(Intent.ACTION_VIEW);
        openImg.setDataAndType(Uri.parse("content://media/external/images/ 1"), "image/*");
        startActivity(openImg);
    }

    public void decryptActivity() {
        Intent startMessageActivity = new Intent(this, MessageActivity.class);
        startActivity(startMessageActivity);
        finish();

    }

}

