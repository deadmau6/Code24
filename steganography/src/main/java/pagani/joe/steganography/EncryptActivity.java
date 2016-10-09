package pagani.joe.steganography;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.os.Handler;

import static pagani.joe.steganography.R.layout.encrypt;

/**
 * Created by User on 10/8/2016.
 */

public class EncryptActivity extends Activity implements View.OnClickListener {
    private Handler mHandler = new Handler();
    public static Context mContext;
    private Button selectImg;
    private Button encryptBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(encrypt);
        mContext = this;
        selectImg = (Button) findViewById(R.id.selectImgBtn);
        selectImg.setOnClickListener(this);
        encryptBtn = (Button) findViewById(R.id.encryptBtn);
        encryptBtn.setOnClickListener(this);

    }
    public void onClick(View v){
        switch (v.getId()){
            case R.id.selectImgBtn:
                //go encrypt
                selectImage();
                break;
            case R.id.encryptBtn:
                encryptActivity();
                break;
        }
    }
    public void selectImage(){
        Intent openImg = new Intent();
        openImg.setAction(Intent.ACTION_VIEW);
        openImg.setDataAndType(Uri.parse("content://media/external/images/ 1"), "image/*");
        startActivity(openImg);
    }
    public void encryptActivity() {
        Toast.makeText(mContext, "Success", Toast.LENGTH_SHORT).show();
        mHandler.postDelayed(new Runnable() {
            public void run() {
                finish();
                //Intent startMainActivity = new Intent(mContext, MainActivity.class);
                //startActivity(startMainActivity);
            }
        }, 2000);

    }
}