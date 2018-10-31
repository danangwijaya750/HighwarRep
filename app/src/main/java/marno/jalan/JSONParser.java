package marno.jalan;

/**
 * Created by marno on 2/24/2017.
 */

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONParser {
    private String json;
    private JSONArray jsonA = null;
    private String[][] res;
    private JSONObject one = null;
    private String[] resOne = null;

    public JSONParser(String json){
          this.json = json;
    }
    public void parsefoto(){
        try {
            one=new JSONObject(json);
            resOne=new String [one.length()];
            resOne[0]=one.getString("url_foto");
            //resOne[1]=one.getString("nama");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public String [] getparsefoto(){
        return resOne;
    }



    public void parseJSONMany(){
        try{
            this.jsonA = new JSONArray(json);
            System.out.println("Jumlah "+jsonA.length());
            res = new String[jsonA.length()][8];
            System.out.println("tempat"+jsonA.getJSONObject(0).getString("setatus"));
            for(int i = 0; i < jsonA.length(); i++){
                JSONObject obj = jsonA.getJSONObject(i);
                String[] arr = {obj.getString("setatus"), obj.getString("judul_laporan"), obj.getString("alamat"), obj.getString("url_gambar"), obj.getString("longitude"), obj.getString("latitude"), obj.getString("jenis_laporan"), obj.getString("id_laporan")};
                res[i] = arr;
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
    }


    public String[][] getJSONMany(){
        return res;
    }


}
