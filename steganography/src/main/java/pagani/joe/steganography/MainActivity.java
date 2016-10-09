package pagani.joe.steganography;

import android.content.Intent;
import android.widget.Toast;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.content.Context;
import android.widget.Button;

public class MainActivity extends Activity implements View.OnClickListener{
    public static Context context;
    private Button encrypt;
    private Button decrypt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        encrypt = (Button) findViewById(R.id.startEncryptBtn);
        encrypt.setOnClickListener(this);
        decrypt = (Button) findViewById(R.id.startDecryptBtn);
        decrypt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.startEncryptBtn:
                //go encrypt
                startEncryptActivity();
                break;
            case R.id.startDecryptBtn:
                //go decrypt
                startDecryptActivity();
                break;
        }
    }

    public void startEncryptActivity(){
        Intent startEncryptActivity = new Intent(this, EncryptActivity.class);
        startActivity(startEncryptActivity);
    }
    public void startDecryptActivity(){
        Intent startDecryptActivity = new Intent(this, DecryptActivity.class);
        startActivity(startDecryptActivity);
    }
}
