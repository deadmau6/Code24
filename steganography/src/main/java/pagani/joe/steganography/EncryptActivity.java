package pagani.joe.steganography;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.util.Log;
/**
 * Created by User on 10/8/2016.
 */

public class EncryptActivity extends Activity implements View.OnClickListener {
    public static Context mContext;
    private Button selectImg;
    private String selectedImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.encrypt);
        mContext = this;
        selectImg = (Button) findViewById(R.id.selectImgBtn);
        selectImg.setOnClickListener(this);

    }
    public void onClick(View v){
        switch (v.getId()){
            case R.id.selectImgBtn:
                selectImage();
                break;
        }
    }
    public void selectImage(){
        Intent openImg = new Intent();
        openImg.setType("image/*");
        openImg.setAction(Intent.ACTION_OPEN_DOCUMENT);
        startActivityForResult(Intent.createChooser(openImg, "Select Image"), 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                Uri imgUri = data.getData();
                selectedImg = getSelectedImgPath(imgUri);
                Log.d("Path: ", selectedImg + "blank");
            }
        }
    }

    public String getSelectedImgPath(Uri uri){
        if (uri == null) {
            return null;
        }
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        cursor.moveToFirst();
        if (cursor != null) {
            int index = cursor.getColumnIndexOrThrow(projection[0]);
            String path = cursor.getString(index);
            cursor.close();
            return path;
        }
        cursor.close();
        return uri.getPath();
    }
}