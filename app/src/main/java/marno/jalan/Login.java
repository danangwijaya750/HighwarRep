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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;


public class Login extends AppCompatActivity {
    private EditText editTextEmail;
    private EditText editTextPassword;
    private boolean loggedIn = false;
    private Button btnlogin;
    private TextView signuptxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
         signuptxt=(TextView)findViewById(R.id.linksignup);

        signuptxt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Login.this, signup.class);
                startActivity(intent);
                finish();
            }
        });
        btnlogin=(Button)findViewById(R.id.btn_login);
        editTextEmail = (EditText) findViewById(R.id.input_email);
        editTextPassword = (EditText) findViewById(R.id.input_password);
     btnlogin.setOnClickListener(new View.OnClickListener(){

         @Override
         public void onClick(View v) {
             btnlogin.setEnabled(false);
             if(editTextEmail.getText().toString().equals("")&&editTextPassword.getText().toString().equals("")){
                 Toast.makeText(Login.this,"Data Belum Lengkap!",Toast.LENGTH_LONG).show();
                btnlogin.setEnabled(true);
             }else {
                 //Intent newintent = new Intent(Login.this, homeScreen.class);
                 //startActivity(newintent);
                 login();
                 btnlogin.setEnabled(false);
             }


         }
     });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //In onresume fetching value from sharedpreference
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        //Fetching the boolean value form sharedpreferences
        loggedIn = sharedPreferences.getBoolean(Config.LOGGEDIN_SHARED_PREF, false);

        //If we will get true
        if(loggedIn){
            //We will start the Profile Activity
            Intent intent = new Intent(Login.this, navigasi.class);
            startActivity(intent);
            finish();
        }
    }


    private void login(){
        final ProgressDialog prog = ProgressDialog.show(this,"Sedang Memproses....","Mohon Tunggu...",false,false);
        //Getting values from edit texts
        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();

        //Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //If we are getting success from server
                        if(response.equalsIgnoreCase(Config.LOGIN_SUCCESS)){
                            //Creating a shared preference
                            SharedPreferences sharedPreferences = Login.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                            //Creating editor to store values to shared preferences
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            //Adding values to editor
                            editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, true);
                            editor.putString(Config.EMAIL_SHARED_PREF, email);

                            //Saving values to editor
                            editor.commit();
                            prog.dismiss();
                            //Starting profile activity
                            Toast.makeText(Login.this,"Login Success Selamat Datang "+email+"!",Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(Login.this, navigasi.class);
                            startActivity(intent);
                            finish();
                        }else{
                            //If the server response is not success
                            //Displaying an error message on toast
                            prog.dismiss();
                            Toast.makeText(Login.this, "Username atau Passwor Salah", Toast.LENGTH_LONG).show();
                            btnlogin.setEnabled(true);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        prog.dismiss();
                        Toast.makeText(Login.this,"Tidak dapat terhubung ke Server",Toast.LENGTH_LONG).show();
                        //You can handle error here if you want
                        btnlogin.setEnabled(true);
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                //Adding parameters to request
                params.put(Config.KEY_EMAIL, email);
                params.put(Config.KEY_PASSWORD, password);

                //returning parameter
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(this,Login.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        finish();
        Intent newi=new Intent(this,homeScreen.class);
        newi.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        newi.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        finish();
    }


}
