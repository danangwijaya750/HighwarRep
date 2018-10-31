package marno.jalan;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class feedback extends AppCompatActivity {
    private Button kirim;
    private EditText psn;
    public static final String URL = "http://58.145.168.181/fb/feedback.php";
    private static final String KEY_EMAIL="mail";
    private static final String KEY_USER="usr";
    private static final String KEY_ID="idnya";
    private static final String KEY_PESAN="psn";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        kirim=(Button)findViewById(R.id.btn_Kirim);
        psn=(EditText)findViewById(R.id.input_psn);
        kirim.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(psn.getText().toString().equals("")||psn.getText().toString().equals(null)){
                Toast.makeText(feedback.this,"Silahkan Isi Pesan Anda",Toast.LENGTH_LONG).show();
            }else {
            sendPesan();}
        }
    });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent mainIntent = new Intent(feedback.this, navigasi.class);
        startActivity(mainIntent);
        finish();
    }

    public void sendPesan(){
        final ProgressDialog prog = ProgressDialog.show(this,"Meng-Upload Feedback Anda......","Mohon Tunggu.....",false,false);

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        final String emaill = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");
        final String idnya=emaill+timeStamp;
        final String mailcomb=psn.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        prog.dismiss();
                        //Showing toast message of the response
                        Toast.makeText(feedback.this,response,Toast.LENGTH_LONG).show();
                        psn.setText("");

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        prog.dismiss();
                        Toast.makeText(feedback.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_EMAIL,emaill);
                params.put(KEY_ID,idnya);
                params.put(KEY_USER,emaill);
                params.put(KEY_PESAN,mailcomb);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}
