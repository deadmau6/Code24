package pagani.joe.steganography;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.EditText;
import android.os.Handler;
import android.util.Log;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * Created by User on 10/8/2016.
 */

public class EncryptActivity extends Activity implements View.OnClickListener {
    private static final int READ_REQUEST_CODE = 42;
    private Handler mHandler = new Handler();
    public Matroschka mat=new Matroschka();
    public byte[] encrypted;
    public static Context mContext;
    private Button selectImg;
    private Button encryptBtn;
    private String selectedImg;
    private String data;
    private String key;
    private Bitmap bitImg;
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
                selectedImg = imgUri.toString();
                try {
                    Bitmap bitTemp = getBitmapFromUri(imgUri);
                    bitImg = bitTemp.copy(Bitmap.Config.ARGB_8888, true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    public void encryptActivity() {
        key = passwordTxt.getText().toString();
        data = messageTxt.getText().toString();
        new getImageTask().execute("named");
        mHandler.postDelayed(new Runnable() {
            public void run() {
                finish();
                //Intent startMainActivity = new Intent(mContext, MainActivity.class);
                //startActivity(startMainActivity);
            }
        }, 2000);

    }
    private class getImageTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            File dest = new File(selectedImg, params+".png");
            try {
                FileOutputStream out = new FileOutputStream(dest);
                encrypted = mat.encrypt(data, key);
                mat.hideMessage(bitImg,encrypted);
                bitImg.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();
                //MediaStore.Images.Media.insertImage(getContentResolver(), dest.getAbsolutePath(), params+".png", "your secret sir");
                return true;
            } catch (Exception e){
                mat.hideMessage(bitImg,encrypted);
                //bitImg.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(dest));
                Log.v("Catch","image sync fail");
                return false;
            }
        }
    }
}