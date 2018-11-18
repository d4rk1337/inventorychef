package net.d4rk.inventorychef.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import net.d4rk.inventorychef.R;
import net.d4rk.inventorychef.adapter.IngredientAdapter;
import net.d4rk.inventorychef.database.dao.Ingredient;
import net.d4rk.inventorychef.database.dao.IngredientViewModel;
import net.d4rk.inventorychef.database.room.AppDatabase;
import net.d4rk.inventorychef.util.DatabaseInitializer;

import java.util.List;

public class ActivityMain extends AppCompatActivity {

    private static final String TAG = ActivityMain.class.getName();

    private IngredientViewModel mIngredientViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {

//        ActivitySplashBinding activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash);

//        activityMainBinding.clickHereBtn.setOnClickListener(view ->
//                DatabaseInitializer.populateAsync(AppDatabase.getAppDatabase(this))
//        );

//        Log.d(TAG, "(onCreate): initialize database");
//
//        DatabaseInitializer.populateAsync(AppDatabase.getAppDatabase(this));

            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            RecyclerView recyclerView = findViewById(R.id.recyclerview);
            final IngredientAdapter adapter = new IngredientAdapter(this);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            // Get a new or existing ViewModel from the ViewModelProvider.
            mIngredientViewModel = ViewModelProviders.of(this).get(IngredientViewModel.class);

            // Add an observer on the LiveData returned by getAlphabetizedWords.
            // The onChanged() method fires when the observed data changes and the activity is
            // in the foreground.
            mIngredientViewModel.getAllIngredients().observe(this, new Observer<List<Ingredient>>() {
                @Override
                public void onChanged(@Nullable final List<Ingredient> ingredients) {
                    // Update the cached copy of the words in the adapter.
                    adapter.setIngredients(ingredients);
                }
            });

            FloatingActionButton fab = findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseInitializer.populateAsync(AppDatabase.getAppDatabase(ActivityMain.this));
                }
            });

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    protected void onDestroy() {
        AppDatabase.destroyInstance();
        super.onDestroy();
    }
}
