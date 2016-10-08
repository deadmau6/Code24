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
    private static Context context;
    private Button encrypt;
    private Button decrypt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        encrypt = (Button) findViewById(R.id.encryptBtn);
        decrypt = (Button) findViewById(R.id.decryptBtn);

    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.encryptBtn:
                //go encrypt
                System.out.println("hey");
                Toast.makeText(this, "Ecrypt Btn", Toast.LENGTH_SHORT).show();
                break;
            case R.id.decryptBtn:
                //go decrypt
                Toast.makeText(this, "Decrypt Btn", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
