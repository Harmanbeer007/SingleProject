package sandhu.harman.singleproject.parent;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.MalformedURLException;
import java.net.URL;

import sandhu.harman.singleproject.R;
import sandhu.harman.singleproject.myInterface;

public class viewCollegeWebsite extends AppCompatActivity {

    WebView myWebsite;
    private Button addWebsite;
    private static final int PERMISSION_REQUEST_CODE = 4277;
    public static final int REQUEST_SELECT_FILE = 1234;
    public ValueCallback<Uri[]> mUploadMessageArr;
    public static final int REQUEST_SELECT_FILE_LEGACY = 4277;
    private ValueCallback<Uri> mUploadMessage;
    private ProgressDialog pd;
    private DatabaseReference mWebsiteInstance;
    private SharedPreferences mPrefsUser;
    private SharedPreferences.Editor mEditor;
    private String myurl;
    private AlertDialog.Builder myConnectionError;
    private FirebaseDatabase mFirebaseInstance;
    private Context context;
    private StringBuilder collegeID;
    private FirebaseAuth mAuth;
    private myInterface mListener;
    private SharedPreferences mparentPrefs;
    private SharedPreferences userCheckLoginPreferences;
    private SharedPreferences.Editor mUserEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_college_website);
        mparentPrefs = getSharedPreferences(getString(R.string.parentPref), MODE_PRIVATE);
        userCheckLoginPreferences = getSharedPreferences(getString(R.string.userLoggedPrefs), MODE_PRIVATE);
        mUserEditor = userCheckLoginPreferences.edit();
        myWebsite = (WebView) findViewById(R.id.wv_College_Website);


        mEditor = mparentPrefs.edit();
        pd = new ProgressDialog(viewCollegeWebsite.this);
        pd.setMessage("Please wait Loading Preview...");
        pd.setCancelable(true);
        pd.show();
        getWebsite();
        context = viewCollegeWebsite.this;
        mAuth = FirebaseAuth.getInstance();
    }

    private void getWebsite() {

        mPrefsUser = getSharedPreferences(getString(R.string.userLoggedPrefs), MODE_PRIVATE);

        collegeID = new StringBuilder(mparentPrefs.getString(getString(R.string.college_Id), ""));

        if (collegeID.toString().isEmpty() && collegeID.equals(null)) {
            collegeID = collegeID.append("Gtb");
        }
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mWebsiteInstance = mFirebaseInstance.getReference("ADMIN/" + collegeID + "/website");


        mWebsiteInstance.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    loadUrl(dataSnapshot.getValue().toString());

                } else {
                    loadUrl("file:///android_asset/default.html");
                    if (pd.isShowing()) {
                        pd.dismiss();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                ToastClass.ToastMessage(context, databaseError);
            }
        });
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && this.myWebsite.canGoBack()) {
            this.myWebsite.goBack();
            if (pd.isShowing()) {
                pd.dismiss();
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    private void loadUrl(final String web) {

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
        myWebsite.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        myWebsite.loadUrl(web);


        myWebsite.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
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

                    Toast.makeText(viewCollegeWebsite.this, getString(R.string.file_error), Toast.LENGTH_LONG).show();

                    return false;
                }

                return true;
            }
        });


    }
}
