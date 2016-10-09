package pagani.joe.steganography;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.os.Handler;
import android.util.Log;

import java.io.FileDescriptor;
import java.io.IOException;

/**
 * Created by User on 10/8/2016.
 */

public class EncryptActivity extends Activity implements View.OnClickListener {
    private static final int READ_REQUEST_CODE = 42;
    private Matroschka mat = new Matroschka();
    private Handler mHandler = new Handler();
    public static Context mContext;
    private Button selectImg;
    private Button encryptBtn;
    private Bitmap selectedImg;
    private EditText passwordtxt;
    private String key;
    private EditText messagetxt;
    private String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.encrypt);
        mContext = this;
        selectImg = (Button) findViewById(R.id.selectImgBtn);
        selectImg.setOnClickListener(this);
        encryptBtn = (Button) findViewById(R.id.encryptBtn);
        encryptBtn.setOnClickListener(this);
        passwordtxt = (EditText) findViewById(R.id.password);
        messagetxt = (EditText) findViewById(R.id.message);

    }
    public void onClick(View v){
        switch (v.getId()){
            case R.id.selectImgBtn:
                //go encrypt
                selectImage();
                break;
            case R.id.encryptBtn:
                key = passwordtxt.getText().toString();
                data = messagetxt.getText().toString();
                try {
                    encryptActivity();
                } catch (Exception e) {
                    Toast.makeText(mContext, "Catch", Toast.LENGTH_SHORT).show();
                }
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
                try {
                    selectedImg = getImg(imgUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Bitmap getImg(Uri uri) throws IOException{
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "rb");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }
    public void encryptActivity() throws Exception {
        if (key != null && data != null && selectedImg != null) {
            mat.hideMessage(selectedImg, mat.encrypt(data, key));
            Toast.makeText(mContext, "Success", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, "Error", Toast.LENGTH_SHORT).show();
        }
        mHandler.postDelayed(new Runnable() {
            public void run() {
                finish();
                //Intent startMainActivity = new Intent(mContext, MainActivity.class);
                //startActivity(startMainActivity);
            }
        }, 2000);

    }
}