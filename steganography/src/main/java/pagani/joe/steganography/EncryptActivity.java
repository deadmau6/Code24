package pagani.joe.steganography;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.EditText;
import android.os.Handler;
import android.util.Log;

/**
 * Created by User on 10/8/2016.
 */

public class EncryptActivity extends Activity implements View.OnClickListener {
    private static final int READ_REQUEST_CODE = 42;
    private Handler mHandler = new Handler();
    public static Context mContext;
    private Button selectImg;
    private Button encryptBtn;
    private String selectedImg;
    private EditText passwordTxt;
    private EditText messageTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.encrypt);
        mContext = this;
        selectImg = (Button) findViewById(R.id.selectImgBtn);
        selectImg.setOnClickListener(this);
        encryptBtn = (Button) findViewById(R.id.encryptBtn);
        encryptBtn.setOnClickListener(this);
        passwordTxt = (EditText) findViewById(R.id.passwordField);
        messageTxt = (EditText) findViewById(R.id.messageField);

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
        Intent openImg = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        openImg.addCategory(Intent.CATEGORY_OPENABLE);
        openImg.setType("image/*");
        startActivityForResult(openImg, READ_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode == Activity.RESULT_OK && requestCode == READ_REQUEST_CODE){
            Uri imgUri = null;
            if (data != null) {
                imgUri = data.getData();
                Log.d("Path: ", imgUri.toString());
                getSelectedImgPath(imgUri);
            }
        }
    }

    public void getSelectedImgPath(Uri uri){
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                selectedImg = cursor.getString(
                        cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                );
                Log.d("Display Name ", selectedImg);
            }
        } finally {
            cursor.close();
        }
    }
    public void encryptActivity() {
        String password = passwordTxt.getText().toString();
        String message = messageTxt.getText().toString();
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