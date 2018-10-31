package marno.jalan;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class navigasi extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ListView list;

    private final static String TERBARU_URL="http://58.145.168.181/fb/terbaru.php";
    private final static String REPORT_URL="http://58.145.168.181/fb/report.php";
    private final static String GET_FOTO="http://58.145.168.181/fb/getfoto.php";
    private final static String KEY_MAIL="email";
    protected List<Row> rows = new ArrayList<Row>();
    private String setatback=null;

    private GeoPoint curLoc = null;
    public double curLong;
    public double curLat;
    private MyLocationNewOverlay mlocationOverlay;

    LocationListener locationListener;
    LocationManager locationManager;

    boolean loaded = false;

private Button btn;




    @Override
    //dijalankan pertama
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigasi);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Highway Report");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);
        //Fetching email from shared preferences


        //Showing the current logged in email to textview
        //textView.setText("Current User: ");


/*View view=navigationView.inflateHeaderView(R.layout.nav_header_main);*/
        TextView eml = (TextView)header.findViewById(R.id.nama);
        //TextView eml = (TextView)header.findViewById(R.id.textView4);
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String emaill = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");
        //name.setText(personName);
        eml.setText(emaill);




        //menget lokasi
        locationListener = new navigasi.mLocationListener(this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        int i = this.getPackageManager().checkPermission("android.permission.ACCESS_FINE_LOCATION", this.getPackageName());
        int j = this.getPackageManager().checkPermission("android.permission.ACCESS_COARSE_LOCATION", this.getPackageName());
        if(i == PackageManager.PERMISSION_GRANTED && j == PackageManager.PERMISSION_GRANTED){
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        //mengeget lokasi dengan geopoint
        if(loc != null){
            curLoc = new GeoPoint(loc.getLongitude(), loc.getLatitude());
        }
        //membagi lokasi geopoint yang di dapat ke long dan lat
        if(curLoc != null){
            curLong = loc.getLongitude();
            curLat = loc.getLatitude();
        }
        //navigationView.setCheckedItem(R.id.nav_camera);

        android.app.FragmentManager fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.inii,new coba()).commit();
        sendReq();
        getFotoProfile();
        TextView tv=(TextView)findViewById(R.id.textView2);



        //navigasi.img = (ImageView) findViewById(R.id.imgview);

    }
    public void getFotoProfile(){
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String emaill = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");
         final String eml=emaill;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_FOTO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                            parse(response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        //Toast.makeText(navigasi.this,"Koneksi Bermasalah Pastikan Koneksi Data Internet Anda Menyala atau Tunggu Beberapa Saat Lagi!",Toast.LENGTH_LONG).show();
                        //finish();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_MAIL,eml);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }




    public void sendReq(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, TERBARU_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        parseRes(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(navigasi.this,"Koneksi Bermasalah Pastikan Koneksi Data Internet Anda Menyala atau Tunggu Beberapa Saat Lagi!",Toast.LENGTH_LONG).show();
                        //finish();
                        //Toast.makeText(navigasi.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

public void parse(String json){
    System.out.println("jsonnnnnnnnnnnnnnnnnn: "+json);
    JSONParser jpar = new JSONParser(json);
    jpar.parsefoto();
    final String [] fot =jpar.getparsefoto();
//    System.out.println(fot.length);
    ImageView imgv=(ImageView)findViewById(R.id.imgview);
   // TextView name = (TextView)findViewById(R.id.nama);
    UrlImageViewHelper.setUrlDrawable(imgv, fot[0]);
    //name.setText(fot[1]);

}
    //public static void setGambar(String foto){

        //UrlImageViewHelper.setUrlDrawable(img, foto);
    //}

    //private static ImageView img;

    //parse untuk listview
    public void parseRes(String json){
        System.out.println(json);
        JSONParser jp = new JSONParser(json);
        jp.parseJSONMany();
        String [][] places = jp.getJSONMany();
        rows.clear();
        for(int i = 0; i < places.length; i++){
            rows.add(new Row(places[i][1], places[i][3], places[i][2], places[i][0], places[i][4], places[i][5], places[i][6], places[i][7]));
        }



        CustomAdapter adapter = new CustomAdapter(this, rows);
        ListView listv = (ListView)findViewById(R.id.list_search);
        listv.setAdapter(adapter);


        //Button btn=(Button)findViewById(R.id.buttonRep);
        //btn.setOnClickListener(new View.OnClickListener() {
         //   @Override
         //   public void onClick(View v) {
         //       Intent intent = new Intent(navigasi.this,report.class);
         //       intent.putExtra("id_tempat", text.getText());
         //       intent.putExtra("caller", 1);
         //       startActivity(intent);
         //   }
        //});

        listv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView text = (TextView)view.findViewById(R.id.textView2);
                Intent intent = new Intent(navigasi.this,report.class);
                //based on item add info to intent
                intent.putExtra("id_tempat", text.getText());
                startActivity(intent);
                finish();
            }


        });


    }



    public void sendRep(){

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String emaill = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");
        final String eml=emaill;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, REPORT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        //Toast.makeText(navigasi.this,"Koneksi Bermasalah Pastikan Koneksi Data Internet Anda Menyala atau Tunggu Beberapa Saat Lagi!",Toast.LENGTH_LONG).show();
                        //finish();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_MAIL,eml);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);





    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

                super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigasi, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            logout();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        android.app.FragmentManager fm = getFragmentManager();
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            fm.beginTransaction().replace(R.id.inii, new coba()).commit();
            sendReq();
            getFotoProfile();
        } else if (id == R.id.nav_gallery) {
            Intent select = new Intent(navigasi.this, input_laporan.class);
            startActivity(select);
        } else if (id == R.id.nav_slideshow) {
            Intent select = new Intent(navigasi.this, Terdekat.class);
            startActivity(select);
        } else if (id == R.id.nav_manage) {
            Intent select = new Intent(navigasi.this, ubahprofil.class);
            startActivity(select);
        } else if (id == R.id.nav_share) {
            fm.beginTransaction().replace(R.id.inii,new frag_about()).commit();
            setatback="loop";
        }else if (id == R.id.nav_feed) {
            Intent select = new Intent(navigasi.this, feedback.class);
            startActivity(select);
        }
        else if (id==R.id.syaratket){
            fm.beginTransaction().replace(R.id.inii,new frag_syarket()).commit();
            setatback="loop";
        }
        else if (id == R.id.nav_send) {
            logout();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

            alertDialog.setTitle("Pengaturan GPS");
            alertDialog.setMessage("GPS Tidak Diaktifkan. Aplikasi Ini Membutuhkan GPS Yang Aktif.");
            alertDialog.setPositiveButton("Pengaturan", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    cont.startActivity(intent);
                }
            });

            alertDialog.setNegativeButton("Batal", new DialogInterface.OnClickListener(){
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

    //Logout function
    private void logout(){
        //Creating an alert dialog to confirm logout
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Anda Yakin Ingin logout?");
        alertDialogBuilder.setPositiveButton("Ya",
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
                        Intent intent = new Intent(navigasi.this, Login.class);
                        startActivity(intent);
                        finish();
                    }
                });

        alertDialogBuilder.setNegativeButton("Tidak",
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
