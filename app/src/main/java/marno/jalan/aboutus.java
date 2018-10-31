package marno.jalan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class aboutus extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);
    }
    public void onBackPressed(){
        Intent intent = new Intent(aboutus.this, homeScreen.class);
        startActivity(intent);
        finish();
    }
}
