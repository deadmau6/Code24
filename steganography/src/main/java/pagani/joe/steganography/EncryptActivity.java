package pagani.joe.steganography;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by User on 10/8/2016.
 */

public class EncryptActivity extends Activity implements View.OnClickListener {
    private static Context context;
    private Button selectImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.encrypt);
        context = this;
        selectImg = (Button) findViewById(R.id.selectImgBtn);

    }
    public void onClick(View v){
        switch (v.getId()){
            case R.id.selectImgBtn:
                //go encrypt
                Toast.makeText(context,"encrypt",Toast.LENGTH_LONG);
                break;
        }
    }
}