package marno.jalan;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Terdekat extends AppCompatActivity {
    private static final String TERDEKAT_URL = "http://58.145.168.181/fb/terdekat.php";
    private static final String KEY_RAD="radius";
    public static final String KEY_LONG = "long";
    public static final String KEY_LAT= "lat";
    private GeoPoint curLoc = null;
    private double curLong;
    private double curLat;
    private MyLocationNewOverlay mlocationOverlay;
    private MapView map;
    private MapController mapCon;

    private EditText radi;
    private  ListView list;
    Double longi;
    Double lati;
    private Integer radiusse;
    private Button btne;
    boolean loaded = false;
    LocationListener locationListener;
    LocationManager locationManager;
    private boolean centered;

    String [][] places;

    protected List<Row> rows = new ArrayList<Row>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terdekat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Terdekat");

        map = (MapView) findViewById(R.id.maps);
        map.setBuiltInZoomControls(true);
        this.mapCon = (MapController) map.getController();

        //radi=(EditText)findViewById(R.id.input_rad);

        mapCon.setZoom(13);
        map.setMultiTouchControls(true);
        map.setTileSource(TileSourceFactory.MAPNIK);
        this.mlocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(map.getContext()), map);
        this.mlocationOverlay.enableMyLocation();
        map.getOverlays().add(mlocationOverlay);
        locationListener = new mLocationListener(this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        int i = this.getPackageManager().checkPermission("android.permission.ACCESS_FINE_LOCATION", this.getPackageName());
        int j = this.getPackageManager().checkPermission("android.permission.ACCESS_COARSE_LOCATION", this.getPackageName());
        if(i == PackageManager.PERMISSION_GRANTED && j == PackageManager.PERMISSION_GRANTED){
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(loc != null){
            curLoc = new GeoPoint(loc.getLongitude(), loc.getLatitude());
            if(!centered) {
                setMapCenter();
            }
        }

        if(curLoc != null){

            sendReq();
        }


    }
    public void sendReq(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, TERDEKAT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseRes(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Terdekat.this,"Koneksi Bermasalah Pastikan Koneksi Data Internet Anda Menyala atau Tunggu Beberapa Saat Lagi!",Toast.LENGTH_LONG).show();
                        finish();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();

                params.put(KEY_LONG,String.valueOf(curLoc.getLongitude()));
                params.put(KEY_LAT,String.valueOf(curLoc.getLatitude()));


                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void parseRes(String json){
        System.out.println(json);
        JSONParser jp = new JSONParser(json);
        jp.parseJSONMany();
        places = jp.getJSONMany();
        rows.clear();
        for(int i = 0; i < places.length; i++){
            rows.add(new Row(places[i][1], places[i][3], places[i][2], places[i][0], places[i][4], places[i][5], places[i][6], places[i][7]));

            ArrayList col = new ArrayList();
            lati=Double.parseDouble(places[i][4]);
            longi=Double.parseDouble(places[i][5]);
            //String judlap=places[i][1];
            String Setatuss=places[i][0];
            String alamatt=places[i][2];

            Marker marker = new Marker(map);
            marker.setPosition(new GeoPoint(lati, longi));
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            map.getOverlays().removeAll(col);
            col.add(marker);
            map.getOverlays().add(marker);
            marker.setTitle(alamatt);
            marker.setSubDescription(Setatuss);

        }

        list = (ListView) findViewById(R.id.listNear);
        CustomAdapter adapter = new CustomAdapter(this, rows);
        list.setAdapter(adapter);
        setMapCenter();
        this.loaded = true;

        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView text = (TextView)view.findViewById(R.id.textView2);
                Intent intent = new Intent(Terdekat.this,report.class);
                //based on item add info to intent
                intent.putExtra("id_tempat", text.getText());
                startActivity(intent);
                finish();
            }


        });

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
            if(!centered) {
                setMapCenter();
            }
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

    public void setMapCenter(){
        mapCon.setCenter(new GeoPoint(curLoc.getLongitude(), curLoc.getLatitude()));
        this.centered = true;
    }

    public void onBackPressed(){
        //Intent mainIntent = new Intent(Terdekat.this, homeScreen.class);
        //startActivity(mainIntent);
        finish();
    }

    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;

        }
        return false;
    }


    public void onResume(){
        super.onResume();
        mlocationOverlay.enableMyLocation();

    }
    public void loadData(){
        if(loaded == false){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            int i = this.getPackageManager().checkPermission("android.permission.ACCESS_FINE_LOCATION", this.getPackageName());
            int j = this.getPackageManager().checkPermission("android.permission.ACCESS_COARSE_LOCATION", this.getPackageName());
            if(loc != null){
                curLoc = new GeoPoint(loc.getLongitude(), loc.getLatitude());
                setMapCenter();
                System.out.println("curLoc harusnya aktif");
                sendReq();
            }

        }

    }
    }









