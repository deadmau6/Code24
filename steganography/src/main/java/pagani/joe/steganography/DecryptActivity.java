package pagani.joe.steganography;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileDescriptor;
import java.io.IOException;

/**
 * Created by Joe on 10/8/2016.
 */

public class DecryptActivity extends Activity implements View.OnClickListener {
    private static final int READ_REQUEST_CODE = 42;
    public static Context mContext;
    private Button selectImg;
    private Button decryptBtn;
    private Bitmap mapImg;
    private EditText passwordTxt;
    public String message="this shit";
    public String password;
    public Matroschka mat = new Matroschka();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.decrypt);
        mContext = this;
        selectImg = (Button) findViewById(R.id.selectImgBtn);
        selectImg.setOnClickListener(this);
        decryptBtn = (Button) findViewById(R.id.decryptBtn);
        decryptBtn.setOnClickListener(this);
        passwordTxt = (EditText) findViewById(R.id.passwordField);
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
                    mapImg=getBitmapFromUri(imgUri);
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
    public void decryptActivity() {
        password = passwordTxt.getText().toString();
        try {
            message=mat.decrypt(mat.getMessageFromImage(mapImg),password);
            //new getImageTask().execute("k");
            //message = temp[0];
        } catch (Exception e) {
            e.printStackTrace();
        }
        Intent startMessageActivity = new Intent(this, MessageActivity.class);
        startMessageActivity.putExtra("Imported Message", message);
        startActivity(startMessageActivity);
        finish();
    }
    private class getImageTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                message=mat.decrypt(mat.getMessageFromImage(mapImg),password);
            } catch (Exception e){
                //mat.hideMessage(bitImg,encrypted)
            }
            return true;
        }
    }
}

