package com.vincent.psm;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.vincent.psm.adapter.ProductDisplayAdapter;
import com.vincent.psm.data.Tile;
import com.vincent.psm.network_helper.MyOkHttp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.vincent.psm.data.DataHelper.KEY_ID;
import static com.vincent.psm.data.DataHelper.KEY_LENGTH;
import static com.vincent.psm.data.DataHelper.KEY_NAME;
import static com.vincent.psm.data.DataHelper.KEY_PHOTO;
import static com.vincent.psm.data.DataHelper.KEY_PRICE;
import static com.vincent.psm.data.DataHelper.KEY_PRODUCTS;
import static com.vincent.psm.data.DataHelper.KEY_STATUS;
import static com.vincent.psm.data.DataHelper.KEY_SUCCESS;
import static com.vincent.psm.data.DataHelper.KEY_THICK;
import static com.vincent.psm.data.DataHelper.KEY_WIDTH;

public class ProductHomeActivity extends AppCompatActivity {
    private Context context;
    private RecyclerView recyProduct;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar prgBar;

    private MyOkHttp conn;

    private ArrayList<Tile> tiles;
    private ProductDisplayAdapter adapter;

    private boolean isShown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_home);
        context = this;

        recyProduct = findViewById(R.id.recyclerView);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        prgBar = findViewById(R.id.prgBar);
        FloatingActionButton fabSearch = findViewById(R.id.fab_search);


        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });

        fabSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isShown)
            loadData();
    }

    private void loadData() {
        isShown = false;
        conn = new MyOkHttp(ProductHomeActivity.this, new MyOkHttp.TaskListener() {
            @Override
            public void onFinished(String result) {
                if (result == null) {
                    Toast.makeText(context, "沒有網路連線", Toast.LENGTH_SHORT).show();
                    prgBar.setVisibility(View.GONE);
                    return;
                }
                try {
                    JSONObject resObj = new JSONObject(result);
                    if (resObj.getBoolean(KEY_STATUS)) {
                        if(resObj.getBoolean(KEY_SUCCESS)) {
                            tiles = new ArrayList<>();
                            JSONArray ary = resObj.getJSONArray(KEY_PRODUCTS);
                            for (int i=0; i<ary.length(); i++) {
                                JSONObject obj = ary.getJSONObject(i);
                                tiles.add(new Tile(
                                        obj.getString(KEY_ID),
                                        obj.getString(KEY_PHOTO),
                                        obj.getString(KEY_NAME),
                                        obj.getString(KEY_LENGTH),
                                        obj.getString(KEY_WIDTH),
                                        obj.getString(KEY_THICK),
                                        obj.getString(KEY_PRICE)
                                ));
                            }
                        }
                        showData();
                    }else {
                        Toast.makeText(context, "伺服器發生例外", Toast.LENGTH_SHORT).show();
                    }
                }catch (JSONException e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                }
                prgBar.setVisibility(View.GONE);
            }
        });
        conn.execute(getString(R.string.link_list_products));
    }

    private void showData() {
        recyProduct.setHasFixedSize(true);
        recyProduct.setLayoutManager(new LinearLayoutManager(context));

        adapter = new ProductDisplayAdapter(getResources(), context, tiles, 10);
        adapter.setBackgroundColor(getResources(), R.color.card_product);
        recyProduct.setAdapter(adapter);
        swipeRefreshLayout.setEnabled(true);
        swipeRefreshLayout.setRefreshing(false);
        recyProduct.setVisibility(View.VISIBLE);

        tiles = null;
        isShown = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        conn.cancel();
    }

    @Override
    public void onDestroy() {
        adapter.destroy(true);
        adapter = null;
        super.onDestroy();
    }
}