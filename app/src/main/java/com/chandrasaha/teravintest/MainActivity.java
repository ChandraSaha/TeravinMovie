package com.chandrasaha.teravintest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.chandrasaha.teravintest.adapter.MovieAdapter;
import com.chandrasaha.teravintest.model.Movie;
import com.chandrasaha.teravintest.service.InternetService;
import com.chandrasaha.teravintest.util.DBAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private ListView lvMovie;
    private MovieAdapter movieAdapter;
    private List<Movie> movieList = new ArrayList<>();
    private DBAdapter db;
    private int index=0;
    private Snackbar snackbar;
    private ProgressDialog dialog;
    private int first =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        checkFirstTime();
        startService(new Intent(this, InternetService.class));
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("response"));
    }

    private void checkFirstTime() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if(!prefs.getBoolean("first_time", false))
        {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("first_time", true);
            editor.apply();
        }
        else{
            loadData();
        }
    }

    private void init() {
        db = new DBAdapter(this);
        db.open();
        dialog = ProgressDialog.show(this,"Loading","Please wait",true);
        dialog.show();
        snackbar = Snackbar.make(findViewById(android.R.id.content), "Penyimpanan lokal telah diperbarui", Snackbar.LENGTH_LONG)
                .setActionTextColor(Color.RED)
                .setAction("Tampilkan", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loadData();
                    }
                })
                ;
        TextView textView = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        lvMovie = (ListView) findViewById(R.id.lvMovie);
        movieAdapter = new MovieAdapter(MainActivity.this,movieList);
        lvMovie.setAdapter(movieAdapter);
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String currentSpeed = intent.getStringExtra("data");
            db.deleteAllMovie();
            index+=1;
            saveData(currentSpeed);
        }

        private void saveData(String currentSpeed) {
            try{
                JSONObject jsonObject = new JSONObject(currentSpeed);
                JSONArray result = jsonObject.getJSONArray("results");
                JSONObject item;
                int start;
                int end;
                if(index%2==1){
                    start=0;
                    end=10;
                }
                else{
                    start=10;
                    end=20;
                }
                for(int i=start;i<end;i++){
                    item = result.getJSONObject(i);
                    Movie movie = new Movie();
                    movie.setTitle(item.getString("title"));
                    movie.setDate(item.getString("release_date"));
                    db.createMovie(movie);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(first==0){
                loadData();
                first=1;
            }
            else{
                snackbar.show();
            }
        }

        private void loadData() {
            movieList.clear();
            List<Movie> movieList1 = db.getAllMovie();
            for (int i=0;i<movieList1.size();i++){
                Movie movie = movieList1.get(i);
                movieList.add(movie);
            }
            if(dialog.isShowing()){
                dialog.dismiss();
            }
            movieAdapter.notifyDataSetChanged();
        }
    };
    private void loadData() {
        movieList.clear();
        List<Movie> movieList1 = db.getAllMovie();
        for (int i=0;i<movieList1.size();i++){
            Movie movie = movieList1.get(i);
            movieList.add(movie);
        }
        if(dialog.isShowing()){
            dialog.dismiss();
        }
        movieAdapter.notifyDataSetChanged();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
