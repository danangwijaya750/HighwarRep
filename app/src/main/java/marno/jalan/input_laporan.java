package marno.jalan;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class input_laporan extends AppCompatActivity {
    private GeoPoint curLoc = null;


    public double curLong;
    public double curLat;
    private MyLocationNewOverlay mlocationOverlay;
    private static final String TAG = input_laporan.class.getSimpleName();
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private Button btn_upload;
    private Uri fileUri;
    LocationListener locationListener;
    LocationManager locationManager;

    boolean loaded = false;


    private ProgressBar progressBar;
    private String filePath ;
    private TextView txtPercentage;
    private ImageView imgPreview;
    private Button btnUpload;
    long totalSize = 0;
    private Bitmap bitmap;

    private EditText judul_lap;
    private EditText deskripsi;
    private Spinner kategori;
    private Spinner jeniss;
    private EditText alm;

    public static final String REGISTER_URL = "http://58.145.168.181/fb/input_laporan.php";
    private static final String KEY_JUDUL="judul";
    private static final String KEY_FOTO="foto";
    private static final String KEY_DESK="diskripsi";
    private static final String KEY_EMAIL="email";
    private static final String KEY_KAT="kategori";
    private static final String KEY_ALAMAT="alamat";
    private static final String KEY_TGL="tanggal";
    private static final String KEY_LONG="long";
    private static final String KEY_JEN="jenis";
    private static final String KEY_LAT="lat";
    private static final String KEY_ID="id_laporan";
    private static final String KEY_FILENAM="file_name";
    private boolean isImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_input_laporan);

        btnUpload = (Button) findViewById(R.id.btnUpld);
        judul_lap = (EditText) findViewById(R.id.judul_lapo);
        deskripsi = (EditText) findViewById(R.id.diskripsi);
        kategori= (Spinner) findViewById(R.id.spinner2);
        alm = (EditText) findViewById(R.id.alamat);

        jeniss=(Spinner)findViewById(R.id.spinner3);
        btn_upload=(Button)findViewById(R.id.btn_upld);
        if (filePath != null) {
            // Displaying the image or video on the screen
            //btnUpload.setClickable(true);
            btnUpload.setEnabled(true);

        }else if(filePath==null)
        {
            //btnUpload.setClickable(false);
            btnUpload.setEnabled(false);
            judul_lap.setFocusable(true);
        }





        String timeStamp = new SimpleDateFormat("yyyyMMdd_HH", Locale.getDefault()).format(new Date());

        Toolbar topToolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(topToolbar);

        topToolbar.setLogo(R.mipmap.ic_launcher);
        topToolbar.setLogoDescription("Input Laporan");

        txtPercentage = (TextView) findViewById(R.id.txtPercentage);

       // progressBar = (ProgressBar) findViewById(R.id.progressBar);
        imgPreview = (ImageView) findViewById(R.id.imgPreview);
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(judul_lap.getText().toString().equals("")||alm.getText().toString().equals("")||deskripsi.getText().toString().equals("")){
                    Toast.makeText(input_laporan.this,"Mohon Isi Data Laporan Dengan Lengkap",Toast.LENGTH_LONG).show();
                }else if(judul_lap.getText().toString().equals(null)||alm.getText().toString().equals(null)||deskripsi.getText().toString().equals(null)){
                    Toast.makeText(input_laporan.this,"Mohon Isi Data Laporan Dengan Lengkap",Toast.LENGTH_LONG).show();
                }
                else{
                    syarat();
                }
            }
        });





        btn_upload.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //syarat();
                captureImage();
            }
        });
        locationListener = new input_laporan.mLocationListener(this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        int i = this.getPackageManager().checkPermission("android.permission.ACCESS_FINE_LOCATION", this.getPackageName());
        int j = this.getPackageManager().checkPermission("android.permission.ACCESS_COARSE_LOCATION", this.getPackageName());
        if(i == PackageManager.PERMISSION_GRANTED && j == PackageManager.PERMISSION_GRANTED){
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(loc != null){
            curLoc = new GeoPoint(loc.getLongitude(), loc.getLatitude());
        }

        if(curLoc != null){
            curLong = loc.getLongitude();
            curLat = loc.getLatitude();
        }





    }
    public void onBackPressed(){
        Intent mainIntent = new Intent(input_laporan.this, navigasi.class);
        startActivity(mainIntent);
        finish();
    }

    private void syarat(){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle("Syarat Dan Ketentuan");
        alertDialog.setMessage("Jika Anda Memilih 'Ya' Maka Anda Sudah Menyetujui Syarat dan Kebijakan (Baca Syarat Dan Kebijakan pada Menu Item Pada Home) Kami Dan Apakah Informasi yang akan Anda kirim tersebut Sudah Valid dan Sesuai?");
        alertDialog.setPositiveButton("Ya", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                //captureImage();
                input_lap();
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                finish();
            }
        });

        alertDialog.show();

    }

    public void input_lap(){
         final ProgressDialog prog = ProgressDialog.show(this,"Meng-Upload Laporan Anda","Silahkan Tunggu",false,false);
        final String image = getStringImage(bitmap);

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String timeStamp2 = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String time = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        final String email = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");


        final String judul = judul_lap.getText().toString().trim();
        final String eml = email;
        final String diskripsi = deskripsi.getText().toString().trim();
        final String alamat = alm.getText().toString().trim();
        final String kat = kategori.getSelectedItem().toString();
        final String tagl = time.toString().trim();
        final String idlap=email+timeStamp2.toString().trim();
        final String filnam="IMG"+idlap+".jpg";
        final String jen=jeniss.getSelectedItem().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        prog.dismiss();
                        //Showing toast message of the response
                        Toast.makeText(input_laporan.this,response,Toast.LENGTH_LONG).show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        prog.dismiss();
                        Toast.makeText(input_laporan.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_JUDUL,judul);
                params.put(KEY_DESK,kat);
                params.put(KEY_EMAIL,eml);
                params.put(KEY_KAT,diskripsi);
                params.put(KEY_ALAMAT,alamat);
                params.put(KEY_TGL,tagl);
                params.put(KEY_FOTO,image);
                params.put(KEY_LONG,String.valueOf(curLoc.getLongitude()));
                params.put(KEY_LAT,String.valueOf(curLoc.getLatitude()));
                params.put(KEY_ID,idlap);
                params.put(KEY_FILENAM,filnam);
                params.put(KEY_JEN,jen);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encode= Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encode;
    }


    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on screen orientation
        // changes
        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    public File getOutputMediaFile(int type) {


        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                Config.IMAGE_DIRECTORY_NAME);

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "Oops! Failed create "
                        + Config.IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String email = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        String filen="IMG"+timeStamp+email+".jpg";
        File mediaFile;

        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator,
                    filen);
        } else {
            return null;
        }

        return mediaFile;
    }
    public class mLocationListener implements LocationListener {

        private Context cont;
        public mLocationListener(Context con){
            this.cont = con;
        }
        public void onLocationChanged(Location loc){
            curLoc = new GeoPoint(loc.getLongitude(), loc.getLatitude());
            curLong = loc.getLongitude();
            curLat = loc.getLatitude();
            loadData();
        }

        public void onProviderDisabled(String Prov){
            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(cont);

            alertDialog.setTitle("GPS Settings");
            alertDialog.setMessage("GPS Not Enabled. This app require GPS activated.");
            alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    cont.startActivity(intent);
                }
            });

            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    finish();
                }
            });

            alertDialog.show();
        }

        public void onProviderEnabled(String Prov){
        }

        public void onStatusChanged(String Prov, int stat, Bundle extras){

        }
    }

    public void loadData(){
        if(loaded == false){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            int i = this.getPackageManager().checkPermission("android.permission.ACCESS_FINE_LOCATION", this.getPackageName());
            int j = this.getPackageManager().checkPermission("android.permission.ACCESS_COARSE_LOCATION", this.getPackageName());
            if(loc != null){
                curLoc = new GeoPoint(loc.getLongitude(), loc.getLatitude());
                System.out.println("curLoc harusnya aktif");

            }
        }
    }



    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                btnUpload.setEnabled(true);
                // successfully captured the image
                // launching upload activity
                launchUploadActivity(true);


            } else if (resultCode == RESULT_CANCELED) {

                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();

            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }}}

    private void launchUploadActivity(boolean isImage){

       filePath= fileUri.getPath();
        if (filePath != null) {
            // Displaying the image or video on the screen
            previewMedia(isImage);
        } else {
            Toast.makeText(getApplicationContext(),
                    "Sorry, file path is missing!", Toast.LENGTH_LONG).show();
        }
    }


    private void previewMedia(boolean isImage) {
        // Checking whether captured media is image or video
        if (isImage) {
            imgPreview.setVisibility(View.VISIBLE);
            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();

            // down sizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 8;

            bitmap = BitmapFactory.decodeFile(filePath, options);

            imgPreview.setImageBitmap(bitmap);

        } }
    private void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message).setTitle("Response from Servers")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // do nothing
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }




    }
