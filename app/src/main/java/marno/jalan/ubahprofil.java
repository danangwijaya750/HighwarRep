package marno.jalan;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ubahprofil extends AppCompatActivity {
    private Bitmap bitmap;

    private int PICK_IMAGE_REQUEST = 1;
private Button btnpilih;
    private Button btnup;
    private ImageView imgv;

    public static final String UBAH_URL = "http://58.145.168.181/fb/ubah.php";
    private static final String KEY_FOTO="foto";
private static final String KEY_MAIL="mail";
    private static final String KEY_EMAIL="email";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubahprofil);
        btnpilih=(Button)findViewById(R.id.pilih);
        btnup=(Button)findViewById(R.id.btnUpld);
        btnup.setEnabled(false);
        imgv=(ImageView)findViewById(R.id.imgPreview2);
        Toolbar topToolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(topToolbar);
        topToolbar.setTitle("Home Screen");
        topToolbar.setLogo(R.mipmap.ic_launcher);
        topToolbar.setLogoDescription("Ubah Foto Profil");
        btnup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnup.setEnabled(false);
                sendfoto();
            }
        });
        btnpilih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pemilihan();
            }
        });




    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void pemilihan(){
        Intent i =new Intent();
        i.setType("image/*");
        i.setAction(i.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Pilih Foto Untuk Foto Profil Anda"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Setting the Bitmap to ImageView

                imgv.setImageBitmap(bitmap);
                btnup.setEnabled(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
private void sendfoto(){
    final ProgressDialog progres = ProgressDialog.show(this,"Meng-Upload Foto Anda......","Mohon Tunggu.....",true,true);

    SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
    final String emaill = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");
    final String mailcomb=emaill+timeStamp;
    final String fotow = getStringImage(bitmap);
    StringRequest stringRequest = new StringRequest(Request.Method.POST, UBAH_URL,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progres.dismiss();
                    //Showing toast message of the response
                    btnup.setEnabled(true);
                    Toast.makeText(ubahprofil.this,response,Toast.LENGTH_LONG).show();

                    //navigasi.setGambar(response);

                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progres.dismiss();
                    btnup.setEnabled(true);
                    Toast.makeText(ubahprofil.this,error.toString(),Toast.LENGTH_LONG).show();
                }
            }){
        @Override
        protected Map<String,String> getParams(){
            Map<String,String> params = new HashMap<String, String>();
            params.put(KEY_EMAIL,emaill);
            params.put(KEY_FOTO,fotow);
            params.put(KEY_MAIL,mailcomb);
            return params;
        }

    };

    RequestQueue requestQueue = Volley.newRequestQueue(this);
    requestQueue.add(stringRequest);
}



}
