package com.snehatilak.helloimages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String URL = "https://mnktechnology.com/tech/mytester/web/web-service/dev-get-image-list";

    //will be used to show any type of error: network issues, empty data sets
    private LinearLayout errorView;
    private ImageView errorImageView;
    private TextView errorTextView;

    private RecyclerView imagesRecView;
    private ImagesRecyclerViewAdapter adapter;

    private ArrayList<Image> imageList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imagesRecView = findViewById(R.id.recView_images);
        errorView = findViewById(R.id.error_view);
        errorImageView = findViewById(R.id.error_image);
        errorTextView = findViewById(R.id.error_message);

        adapter = new ImagesRecyclerViewAdapter(this);
        adapter.setImageList(imageList);

        imagesRecView.setAdapter(adapter);
        imagesRecView.setLayoutManager(new LinearLayoutManager(this));

    }

    public void reloadImageList(View view) {
        imageList.clear();
        RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
        JsonArrayRequest imageRequest = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if(response.length() <= 0){
                    //no data
                    errorImageView.setImageResource(R.drawable.ic_no_data);
                    errorTextView.setText(R.string.no_data);
                    errorView.setVisibility(View.VISIBLE);
                    imagesRecView.setVisibility(View.GONE);
                } else{
                    errorView.setVisibility(View.GONE);
                    imagesRecView.setVisibility(View.VISIBLE);
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject o = null;
                        try {
                            o = response.getJSONObject(i);
                            imageList.add(new Image(o.getString("caption"), o.getString("imageUrl")));
                        } catch (JSONException e) {
                            Toast.makeText(view.getContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                        }

                    }
                }

                adapter.setImageList(imageList);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error instanceof NetworkError || error instanceof NoConnectionError || error instanceof TimeoutError){
                    errorImageView.setImageResource(R.drawable.ic_no_network);
                    errorTextView.setText(R.string.no_network);
                    errorView.setVisibility(View.VISIBLE);
                    imagesRecView.setVisibility(View.GONE);
                } else{
                    Toast.makeText(view.getContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                }

            }
        }

        );

        requestQueue.add(imageRequest);


    }
}