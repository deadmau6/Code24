package pagani.joe.steganography;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.EditText;
import android.os.Handler;
import android.util.Log;


import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;


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
                try {
                    bitImg=getBitmapFromUri(imgUri);
                } catch (IOException e) {
                    Log.d("file path: ", "Oh SHHHHHHHHHHHHHIIIIIIIIIIIITTTTTTT");
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
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {


            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);

            }
        }
        String password = passwordTxt.getText().toString();
        String message = messageTxt.getText().toString();
        byte[] encrypted;
        Matroschka mat=new Matroschka();
        try {

            encrypted = mat.encrypt(message, password);

            Bitmap bitEncryptImg=mat.hideMessage(bitImg,encrypted);
            String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                    "/Pictures/PicCrypt";
            File dir = new File(file_path);
            if(!dir.exists()) {
                Log.d("file path: ", "created");
                dir.mkdirs();
            }
            File file = new File(dir, "secret" + ".png");
            FileOutputStream fOut = new FileOutputStream(file);
            bitEncryptImg.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();

        } catch (Exception e) {
            Log.d("file path: ", "exception");
            e.printStackTrace();
        }
        Toast.makeText(mContext, "Success", Toast.LENGTH_SHORT).show();
        mHandler.postDelayed(new Runnable() {
            public void run() {
                finish();
            }
        }, 2000);

    }


}