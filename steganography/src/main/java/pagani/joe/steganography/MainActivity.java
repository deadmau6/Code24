package pagani.joe.steganography;

import android.support.v7.app.AppCompatActivity;
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
        encrypt = (Button) findViewById(R.id.encryptBtn);
        encrypt.setOnClickListener(this);
        decrypt = (Button) findViewById(R.id.decryptBtn);
        decrypt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.encryptBtn:
                //go encrypt
                encryptActivity();
                break;
            case R.id.decryptBtn:
                //go decrypt
                Toast.makeText(context, "Decrypt Btn", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void encryptActivity(){
        Intent startEncryptActivity = new Intent(this, EncryptActivity.class);
        startActivity(startEncryptActivity);
    }
}
