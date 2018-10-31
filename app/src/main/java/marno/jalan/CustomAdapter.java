package marno.jalan;

/**
 * Created by marno on 2/24/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.List;

public class CustomAdapter extends BaseAdapter {
    Context context;
    List<Row> rows;
    CustomAdapter(Context context, List<Row> rows){
        this.context = context;
        this.rows = rows;
    }

    public int getCount(){
        return rows.size();
    }

    public long getItemId(int pos){
        return rows.indexOf(getItem(pos));
    }

    public Object getItem(int pos){
        return rows.get(pos);
    }

    private class ViewHolder{
        ImageView thumbnail;
        TextView namaTempat;
        TextView alamat;
        TextView idTempat;
        TextView jenies;
        TextView latitud;
    }

    public View getView(int pos, View conv, ViewGroup parent){
        ViewHolder hold = null;
        LayoutInflater mInfalter = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if(conv == null){
            conv = mInfalter.inflate(R.layout.list_item, null);

            hold = new ViewHolder();
            hold.namaTempat = (TextView) conv.findViewById(R.id.titlePlace);
            hold.alamat = (TextView) conv.findViewById(R.id.addressPlace);
            hold.thumbnail = (ImageView) conv.findViewById(R.id.imageList);
            hold.idTempat = (TextView) conv.findViewById(R.id.id_laporan);
            hold.jenies=(TextView)conv.findViewById(R.id.jeniess);
            hold.latitud=(TextView)conv.findViewById(R.id.textView2);
            conv.setTag(hold);
        }else{
            hold = (ViewHolder) conv.getTag();
        }
        Row row = rows.get(pos);
        //System.out.println("Id adalah"+row.getSetatus());
        hold.idTempat.setText(row.getSetatus());
        hold.namaTempat.setText(row.getJudulLaporan());
        hold.alamat.setText(row.getAlamat());
        hold.jenies.setText(row.getJen());
        hold.latitud.setText(row.getId());

        //ImageLoaderConfiguration conf = new ImageLoaderConfiguration.Builder(context).build();
        //ImageLoader.getInstance().init(conf);

        UrlImageViewHelper.setUrlDrawable(hold.thumbnail, row.getUrlGambar());
        //new setImageResource(hold.thumbnail).execute(row.getUrlGambar());
        return conv;
    }


    public class setImageResource extends AsyncTask<String, String, Bitmap> {
        protected ImageView img;
        private final WeakReference<ImageView> imgRef;
        protected setImageResource(ImageView img){
        imgRef = new WeakReference<ImageView>(img);
        }
        protected void onPreExecute(){
            super.onPreExecute();
        }

        protected Bitmap doInBackground(String... args){
            Bitmap bitmap = null;
            try{
                System.out.println(args[0]);
                bitmap = BitmapFactory.decodeStream((InputStream) new URL(args[0]).getContent());
            }catch (Exception e){
                e.printStackTrace();
            }

            return bitmap;
        }

        protected void onPostExecute(Bitmap image){
            super.onPostExecute(image);
            img = imgRef.get();
            img.setImageBitmap(image);
            img = null;
        }

    }

}
