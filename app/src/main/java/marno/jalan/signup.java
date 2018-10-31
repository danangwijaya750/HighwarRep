package marno.jalan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class signup extends AppCompatActivity {
    private static final String REGISTER_URL = "http://58.145.168.181/fb/register.php";

    public static final String KEY_NOTELP = "no_telp";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_NAMA = "nama";
    public static final String KEY_ALAMAT = "alamat";
    public static final String KEY_EMAIL = "email";

    private EditText notelp;
    private EditText eml;
    private EditText passwd;
    private EditText nama;
    private EditText alm;
    private Button btnregiter;
    private TextView login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        btnregiter=(Button)findViewById(R.id.btn_signup);
        notelp = (EditText) findViewById(R.id.notlp);
        eml = (EditText) findViewById(R.id.input_email);
        passwd = (EditText) findViewById(R.id.input_password);
        nama = (EditText) findViewById(R.id.input_name);
        alm = (EditText) findViewById(R.id.alamat);
        login=(TextView)findViewById(R.id.linklogin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(signup.this, Login.class);
                startActivity(intent);
            }
        });
        btnregiter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                btnregiter.setEnabled(false);
                if(eml.getText().toString().equals("")&&passwd.getText().toString().equals("")&&nama.getText().toString().equals("")&&notelp.getText().toString().equals("")&&alm.getText().toString().equals("")){
                    Toast.makeText(signup.this,"Data Belum Lengkap!",Toast.LENGTH_LONG).show();
                    btnregiter.setEnabled(true);
                }else {
                    btnregiter.setEnabled(false);
                    btnregiter.setActivated(false);
                    userReg();
                }

            }
        });

    }
    public void userReg(){

        final String email = eml.getText().toString().trim();
        final String password = passwd.getText().toString().trim();
        final String name = nama.getText().toString().trim();
        final String nomer = notelp.getText().toString().trim();
        final String alamt = alm.getText().toString().trim();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if("failure".equals(response)){
                            Toast.makeText(signup.this,"Maaf Email Yang Anda Masukan Sudah Digunakan Atau Sudah Terdaftar",Toast.LENGTH_LONG).show();
                        btnregiter.setEnabled(true);
                            btnregiter.setActivated(true);
                        }else{
                            Toast.makeText(signup.this,"SignUp Success",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(signup.this, Login.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(signup.this,"Kesalahan Pastikan Ada Yang Anda Masukan Sudah Lengkap Jika Tidak, Koneksi Internet Anda Sedang Bermasalah Silahkan Tunggu Beberapa Saat lagi!",Toast.LENGTH_LONG).show();
                        btnregiter.setEnabled(true);
                        btnregiter.setActivated(true);
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_NAMA,name);
                params.put(KEY_NOTELP,nomer);
                params.put(KEY_EMAIL,email);
                params.put(KEY_ALAMAT,alamt);
                params.put(KEY_PASSWORD,password);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(signup.this, Login.class);
        startActivity(intent);
        finish();
    }
}
