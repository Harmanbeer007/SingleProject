package sandhu.harman.singleproject.parent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import sandhu.harman.singleproject.R;

public class StudyOnline extends AppCompatActivity {
    RequestQueue requestQueue;
    String url = "https://bareheaded-signatur.000webhostapp.com/study_material/getpoducts.php";
    private JSONObject book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_online);
        getBooks();
    }

    public void getBooks() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(this);
            JsonObjectRequest job = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                public JSONArray books;

                @Override
                public void onResponse(JSONObject jsonObject) {
                    Toast.makeText(StudyOnline.this, jsonObject.toString(), Toast.LENGTH_SHORT).show();
                    try {
                        books = jsonObject.getJSONArray("books");
                        for (int i = 0; i < books.length(); i++) {
                            book = books.getJSONObject(i);
                            Toast.makeText(StudyOnline.this, book.getString("chapter_name"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(StudyOnline.this, volleyError.toString(), Toast.LENGTH_SHORT).show();

                }
            });
            requestQueue.add(job);
        }
    }
}
