package marno.jalan;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Terbaru extends AppCompatActivity {
    private final static String TERBARU_URL="http://58.145.168.181/fb/terbaru.php";
    protected List<Row> rows = new ArrayList<Row>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terbaru);


        sendReq();
    }
    public void sendReq(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, TERBARU_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseRes(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Terbaru.this,"Koneksi Bermasalah Pastikan Koneksi Data Internet Anda Menyala atau Tunggu Beberapa Saat Lagi!",Toast.LENGTH_LONG).show();
                        //finish();
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



    public void parseRes(String json){
        System.out.println(json);
        JSONParser jp = new JSONParser(json);
        jp.parseJSONMany();
        String [][] places = jp.getJSONMany();
        rows.clear();
        for(int i = 0; i < places.length; i++){
            rows.add(new Row(places[i][1], places[i][3], places[i][2], places[i][0], places[i][4], places[i][5], places[i][6], places[i][7]));
        }

        ListView list = (ListView)findViewById(R.id.list_search);

        CustomAdapter adapter = new CustomAdapter(this, rows);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //startActivity(intent);
                //finish();
            }
        });
    }
}
