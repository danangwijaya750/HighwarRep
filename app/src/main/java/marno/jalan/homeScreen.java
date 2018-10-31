package marno.jalan;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;
public class homeScreen extends AppCompatActivity {

    private GeoPoint curLoc = null;
    public double curLong;
    public double curLat;
    private MyLocationNewOverlay mlocationOverlay;

    LocationListener locationListener;
    LocationManager locationManager;

    boolean loaded = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Toolbar topToolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(topToolbar);
        topToolbar.setTitle("Home Screen");
        topToolbar.setLogo(R.mipmap.ic_launcher);
        topToolbar.setLogoDescription(getResources().getString(R.string.app_name));
        TextView textView = (TextView) findViewById(R.id.textView);

        //Fetching email from shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String email = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");

        //Showing the current logged in email to textview
        textView.setText("Current User: " + email);

        View input = findViewById(R.id.input);
        input.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    v.setBackgroundColor(Color.argb(70, 00, 00, 00));
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.setBackgroundColor(Color.argb(00, 00, 00, 00));
                    Intent select = new Intent(homeScreen.this, input_laporan.class);
                    startActivity(select);
                }
                return true;
            }
        });


        View terdekat = findViewById(R.id.terdekatbut);
        terdekat.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    v.setBackgroundColor(Color.argb(70, 00, 00, 00));
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.setBackgroundColor(Color.argb(00, 00, 00, 00));
                    Intent select = new Intent(homeScreen.this, Terdekat.class);
                    startActivity(select);
                }
                return true;
            }
        });

        View terbaru = findViewById(R.id.terbaru);
        terbaru.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    v.setBackgroundColor(Color.argb(70, 00, 00, 00));
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.setBackgroundColor(Color.argb(00, 00, 00, 00));
                    Intent select = new Intent(homeScreen.this, Terbaru.class);
                    startActivity(select);
                }
                return true;
            }
        });



        locationListener = new homeScreen.mLocationListener(this);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Adding our menu to toolbar
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.Logout) {
            //calling logout method when the logout button is clicked
            logout();
        }else if(id==R.id.about){
            Intent intent = new Intent(homeScreen.this, aboutus.class);
            startActivity(intent);
            finish();
        }else if(id==R.id.Syarat) {
            Intent intent = new Intent(homeScreen.this, syaratket.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


    //Logout function
    private void logout(){
        //Creating an alert dialog to confirm logout
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to logout?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        //Getting out sharedpreferences
                        SharedPreferences preferences = getSharedPreferences(Config.SHARED_PREF_NAME,Context.MODE_PRIVATE);
                        //Getting editor
                        SharedPreferences.Editor editor = preferences.edit();

                        //Puting the value false for loggedin
                        editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, false);

                        //Putting blank value to email
                        editor.putString(Config.EMAIL_SHARED_PREF, "");

                        //Saving the sharedpreferences
                        editor.commit();

                        //Starting login activity
                        Intent intent = new Intent(homeScreen.this, Login.class);
                        startActivity(intent);
                        finish();
                    }
                });

        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        //Showing the alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

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


}

