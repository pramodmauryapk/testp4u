package com.p4u.parvarish;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class dataActivity extends AppCompatActivity {
    private WebView webviewthis;
    private RecyclerView webVieRes;
    private String mdataRef;
    private static final String TAG = dataActivity.class.getSimpleName();


    public dataActivity(){

    }
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);
        // FirebaseDatabase.getInstance().getReference().child("USERS");
        mdataRef ="https://google.com";
        webviewthis = (WebView)findViewById(R.id.webView_news);
        webviewthis.setWebViewClient(new WebViewClient());
        webviewthis.getSettings().getBuiltInZoomControls();
        webviewthis.getSettings().getJavaScriptCanOpenWindowsAutomatically();
        webviewthis.getSettings().getAllowContentAccess();
        webviewthis.getSettings().getCacheMode();
        webviewthis.getSettings().getMediaPlaybackRequiresUserGesture();
        webviewthis.getSettings().setJavaScriptEnabled(true);
        webviewthis.getSettings().setLoadsImagesAutomatically(true);

        webviewthis.loadUrl(String.valueOf(mdataRef));
        /*mdataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                post2web webView = dataSnapshot.getValue(post2web.class);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

    }
}