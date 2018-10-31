package marno.jalan;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by marno on 4/11/2017.
 */

public class frag_syarket extends android.app.Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.activity_syaratket, container, false);
        WebView web = (WebView) v.findViewById(R.id.webv);
        web.loadUrl("http://58.145.168.181/Syarat.php");

        // Enable Javascript
        WebSettings webSettings = web.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Force links and redirects to open in the WebView instead of in a browser
        web.setWebViewClient(new WebViewClient());

        return v;
    }
}

