package marno.jalan;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class report extends AppCompatActivity {
private String lap;
    private Button btn;
    private EditText alasan;
    private Spinner alsn;
    private TextView id;
    public static final String URL = "http://58.145.168.181/fb/pengaduan.php";
    private static final String KEY_EMAIL="mail";
    private static final String KEY_ALS="als";
    private static final String KEY_ID="idnya";
    private static final String KEY_PENJELASAN="pen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        Toolbar topToolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(topToolbar);
        topToolbar.setTitle("Home Screen");
        topToolbar.setLogo(R.mipmap.ic_launcher);
        topToolbar.setLogoDescription("Pengaduan");
        lap = getIntent().getExtras().getString("id_tempat");
        id=(TextView)findViewById(R.id.textView4);
        id.setText(lap);
        alasan=(EditText)findViewById(R.id.input_alsan);
        alsn=(Spinner)findViewById(R.id.spinner3);
        btn=(Button)findViewById(R.id.btn_Kirm);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendReq();
            }
        });
    }
public void sendReq(){
    final ProgressDialog prog = ProgressDialog.show(this,"Meng-Upload Feedback Anda......","Mohon Tunggu.....",false,false);

    SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
    final String emaill = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");
    final String idnya=lap;
    final String jenis=alsn.getSelectedItem().toString().trim();
    final String alasaan=alasan.getText().toString().trim();

    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    prog.dismiss();
                    //Showing toast message of the response
                    Toast.makeText(report.this,response,Toast.LENGTH_LONG).show();
                    alasan.setText("");

                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    prog.dismiss();
                    Toast.makeText(report.this,error.toString(),Toast.LENGTH_LONG).show();
                }
            }){
        @Override
        protected Map<String,String> getParams(){
            Map<String,String> params = new HashMap<String, String>();
            params.put(KEY_EMAIL,emaill);
            params.put(KEY_ID,idnya);
            params.put(KEY_ALS,emaill);
            params.put(KEY_PENJELASAN,alasaan);
            return params;
        }

    };

    RequestQueue requestQueue = Volley.newRequestQueue(this);
    requestQueue.add(stringRequest);

}
    public void onBackPressed() {

            //super.onBackPressed();
        Intent intent = new Intent(report.this,navigasi.class);
        startActivity(intent);
        finish();


        }
    }




