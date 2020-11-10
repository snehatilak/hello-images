package com.snehatilak.helloimages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
    private FloatingActionButton fab;

    private RecyclerView imagesRecView;
    private ImagesRecyclerViewAdapter adapter;

    boolean isGrid;
    private LinearLayoutManager linearLayoutManager;

    private ArrayList<Image> imageList = new ArrayList<>();
    private StaggeredGridLayoutManager staggeredGridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imagesRecView = findViewById(R.id.recView_images);
        fab = findViewById(R.id.btn_fab);
        errorView = findViewById(R.id.error_view);
        errorImageView = findViewById(R.id.error_image);
        errorTextView = findViewById(R.id.error_message);

        linearLayoutManager = new LinearLayoutManager(this);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        adapter = new ImagesRecyclerViewAdapter(this);
        adapter.setImageList(imageList);

        imagesRecView.setAdapter(adapter);
        imagesRecView.setLayoutManager(linearLayoutManager);

        imagesRecView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    fab.hide();
                } else {
                    fab.show();
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.layout_options, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_layout) {
            if (isGrid) {
                isGrid = false;
                imagesRecView.setLayoutManager(linearLayoutManager);
                item.setIcon(R.drawable.ic_menu_grid);
                item.setTitle("Show as Grid");
            } else {
                isGrid = true;
                imagesRecView.setLayoutManager(staggeredGridLayoutManager);
                item.setIcon(R.drawable.ic_menu_list);
                item.setTitle("Show as List");
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void reloadImageList(View view) {
        imageList.clear();
        RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
        JsonArrayRequest imageRequest = new JsonArrayRequest(Request.Method.GET, URL, null, (JSONArray response) -> {
            if (response.length() <= 0) {
                //no data
                errorImageView.setImageResource(R.drawable.ic_no_data);
                errorTextView.setText(R.string.no_data);
                errorView.setVisibility(View.VISIBLE);
                imagesRecView.setVisibility(View.GONE);
            } else {
                errorView.setVisibility(View.GONE);
                imagesRecView.setVisibility(View.VISIBLE);
                for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject o = response.getJSONObject(i);
                            imageList.add(new Image(o.getString("caption"), o.getString("imageUrl")));
                        } catch (JSONException e) {
                            Toast.makeText(view.getContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                        }

                }
            }

            adapter.setImageList(imageList);

        }, (VolleyError error) -> {
            if (error instanceof NetworkError || error instanceof TimeoutError) {
                errorImageView.setImageResource(R.drawable.ic_no_network);
                errorTextView.setText(R.string.no_network);
                errorView.setVisibility(View.VISIBLE);
                imagesRecView.setVisibility(View.GONE);
            } else {
                Toast.makeText(view.getContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
            }

        }
        );

        requestQueue.add(imageRequest);


    }
}