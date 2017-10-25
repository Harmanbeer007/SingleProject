package sandhu.harman.singleproject.admin;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.CookieSyncManager;
import android.webkit.DownloadListener;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.net.MalformedURLException;
import java.net.URL;

import sandhu.harman.singleproject.R;

import static sandhu.harman.singleproject.admin.Loading.hideIt;
import static sandhu.harman.singleproject.admin.Loading.moveIt;
import static sandhu.harman.singleproject.admin.Loading.shakeIt;

public class WebsiteAdd extends AppCompatActivity {

    EditText mWebsEd;
    WebView myWebsite;
    private Button addWebsite;
    private static final int PERMISSION_REQUEST_CODE = 4277;
    public static final int REQUEST_SELECT_FILE = 1234;
    public ValueCallback<Uri[]> mUploadMessageArr;
    public static final int REQUEST_SELECT_FILE_LEGACY = 4277;
    private ValueCallback<Uri> mUploadMessage;
    private String myurl;
    private ProgressDialog pd;
    private AlertDialog.Builder myConnectionError;
    private FirebaseDatabase mfirebase_Database;
    private DatabaseReference mWebInstance;
    private String college;
    private AppBarLayout appbarLayout;
    SharedPreferences websiteShared;
    SharedPreferences.Editor webSiteEditor;
    private String website;
    private FloatingActionButton addWebsiteToShowCase;
    private String web;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_website_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        websiteShared = getSharedPreferences(getString(R.string.userLoggedPrefs), MODE_PRIVATE);
        website = websiteShared.getString(getString(R.string.website), "");

        mWebsEd = (EditText) findViewById(R.id.edWebsiteR);
        myWebsite = (WebView) findViewById(R.id.college_Web);
        addWebsite = (Button) findViewById(R.id.button2R);

        addWebsiteToShowCase = (FloatingActionButton) findViewById(R.id.btnAddBusToShowcase);


        mfirebase_Database = FirebaseDatabase.getInstance();

        mWebsEd.setText(website);

//        final Animation animShake = AnimationUtils.loadAnimation(this, R.anim.shakemyicon);
        shakeIt(WebsiteAdd.this, addWebsite);
//        addWebsite.startAnimation(animShake);

        appbarLayout = (AppBarLayout) findViewById(R.id.myapp_bar);

        appbarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                if (verticalOffset == 0) {
                    getSupportActionBar().setTitle("");

                } else {
                    getSupportActionBar().setTitle("Preview for " + mWebsEd.getText().toString());
                }


            }
        });


        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.adminPref), MODE_PRIVATE);
        college = sharedPreferences.getString(getString(R.string.college_id), "");
        mWebInstance = mfirebase_Database.getReference("ADMIN/" + college);


        myConnectionError = new AlertDialog.Builder(WebsiteAdd.this);

//        myConnectionError.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//                myWebsite.loadUrl(myurl);
//
//            }
//        });


//        myConnectionError.create();


        addWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                web = mWebsEd.getText().toString().trim();

                if (!web.contains("http://")) {
                    String add = "http://";
                    web = add.concat(web).trim();
                }
                loadUrl(web);

                appbarLayout.setExpanded(false);
                hideIt(WebsiteAdd.this, addWebsite);
                getSupportActionBar().setTitle("Preview" + web);

                webSiteEditor = websiteShared.edit();
                webSiteEditor.putString(getString(R.string.website), mWebsEd.getText().toString().trim());
                webSiteEditor.apply();

            }


        });

    }

    private void addWeb(String web) {

        mWebInstance.child("website").setValue(web).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    Toast.makeText(WebsiteAdd.this, "Website  Added", Toast.LENGTH_SHORT).show();
                    addWebsiteToShowCase.setImageResource(R.drawable.website_set_success);

                } else {
                    Toast.makeText(WebsiteAdd.this, "Website Failed To add to databse", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void loadUrl(final String web) {
        pd = new ProgressDialog(WebsiteAdd.this);
        pd.setMessage("Please wait Loading Preview...");
        pd.setCancelable(true);
        pd.show();
        myWebsite.setInitialScale(1);
        myWebsite.setVisibility(View.VISIBLE);
        myWebsite.getSettings().setJavaScriptEnabled(true);
        myWebsite.getSettings().setBuiltInZoomControls(true);
        myWebsite.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        myWebsite.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        myWebsite.getSettings().setDisplayZoomControls(false);

        myWebsite.getSettings().setAppCacheEnabled(true);
        myWebsite.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        myWebsite.getSettings().setDomStorageEnabled(true);
        myWebsite.getSettings().enableSmoothTransition();
        myWebsite.getSettings().setUseWideViewPort(true);
        myWebsite.getSettings().setSavePassword(true);
        myWebsite.getSettings().setJavaScriptCanOpenWindowsAutomatically(false);
        myWebsite.getSettings().setSupportMultipleWindows(false);
        myWebsite.getSettings().setAllowFileAccess(true);
        myWebsite.getSettings().setAllowFileAccessFromFileURLs(true);
        myWebsite.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        myWebsite.loadUrl(web);


        myWebsite.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // do your handling codes here, which url is the requested url
                // probably you need to open that url rather than redirect:
                view.loadUrl(url);
                myurl = url;
                if (!pd.isShowing()) {
                    pd.show();
                }
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (!pd.isShowing()) {
                    pd.show();
                }
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onPageFinished(WebView view, String url) {
                CookieSyncManager.getInstance().sync();

                System.out.println("on finish");
                if (pd.isShowing()) {
                    addWebsiteToShowCase.setVisibility(View.VISIBLE);
                    moveIt(WebsiteAdd.this, addWebsiteToShowCase);
                    addWebsiteToShowCase.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            addWeb(web);
                        }
                    });
                    pd.dismiss();
                }

                CookieSyncManager.getInstance().sync();
                URL url1 = null;
                try {
                    url1 = new URL(url);
                } catch (MalformedURLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


                if (url.endsWith("pdf")) {
                    myWebsite.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url=" + url);
                }

                //return false; // then it is not handled by default action

            }

        });
        myWebsite.setDownloadListener(new DownloadListener() {
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                //download file using web browser
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });


        myWebsite.setWebChromeClient(new WebChromeClient() {


            // For Android < 3.0
            @SuppressWarnings("unused")
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                mUploadMessage = uploadMsg;

                Intent i = new Intent(Intent.ACTION_GET_CONTENT);

                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");

                startActivityForResult(Intent.createChooser(i, getString(R.string.select_file)), REQUEST_SELECT_FILE_LEGACY);

            }

            // For Android 3.0+
            @SuppressWarnings("unused")
            public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
                mUploadMessage = uploadMsg;

                Intent i = new Intent(Intent.ACTION_GET_CONTENT);

                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType(acceptType);

                startActivityForResult(Intent.createChooser(i, getString(R.string.select_file)), REQUEST_SELECT_FILE_LEGACY);
            }

            // For Android 4.1+
            @SuppressWarnings("unused")
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                mUploadMessage = uploadMsg;

                Intent i = new Intent(Intent.ACTION_GET_CONTENT);

                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType(acceptType);

                startActivityForResult(Intent.createChooser(i, getString(R.string.select_file)), REQUEST_SELECT_FILE_LEGACY);
            }

            // For Android 5.0+
            @SuppressLint("NewApi")
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                if (mUploadMessageArr != null) {
                    mUploadMessageArr.onReceiveValue(null);
                    mUploadMessageArr = null;
                }

                mUploadMessageArr = filePathCallback;

                Intent intent = fileChooserParams.createIntent();

                try {
                    startActivityForResult(intent, REQUEST_SELECT_FILE);
                } catch (ActivityNotFoundException e) {
                    mUploadMessageArr = null;

                    Toast.makeText(WebsiteAdd.this, getString(R.string.file_error), Toast.LENGTH_LONG).show();

                    return false;
                }

                return true;
            }
        });

    }
}
