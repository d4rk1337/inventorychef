package net.d4rk.inventorychef.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import net.d4rk.inventorychef.R;
import net.d4rk.inventorychef.constants.Constants;
import net.d4rk.inventorychef.database.dao.Ingredient;
import net.d4rk.inventorychef.database.dao.IngredientViewModel;
import net.d4rk.inventorychef.database.dao.StoragePlace;
import net.d4rk.inventorychef.database.room.AppDatabase;
import net.d4rk.inventorychef.inventory.expandablelistview.storageplaceingredientlist.adapter.InventoryAdapter;
import net.d4rk.inventorychef.inventory.expandablelistview.storageplaceingredientlist.model.Inventory;
import net.d4rk.inventorychef.util.DatabaseInitializer;

import java.util.ArrayList;
import java.util.List;

public class ActivityMain extends AppCompatActivity {

    private static final String TAG = ActivityMain.class.getName();

    private IngredientViewModel mIngredientViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        try {
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            initList();

            initFloatingButton();

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    protected void onDestroy() {
        AppDatabase.destroyInstance();

        super.onDestroy();
    }

    private void initList() {
        List<Inventory> inventory = new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.recyclerview);

        InventoryAdapter inventoryAdapter = new InventoryAdapter(inventory);

        recyclerView.setAdapter(inventoryAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        StoragePlace storageItem = new StoragePlace();
        storageItem.setId(1);
        storageItem.setName("Küche");

        List<Ingredient> ingredientList = new ArrayList<Ingredient>();

        Ingredient ingredientItem = new Ingredient();
        ingredientItem.setId(2);
        ingredientItem.setName("Reis");
        ingredientItem.setStorageId(1);
        ingredientItem.setAmount(1);

        ingredientList.add(ingredientItem);

        Inventory inventoryItem = new Inventory(storageItem, ingredientList);

        inventory.add(inventoryItem);

//        InventoryAdapter inventoryAdapter = new InventoryAdapter(inventory);
//
//        recyclerView.setAdapter(inventoryAdapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        inventoryAdapter.notifyDataSetChanged();

//        final IngredientAdapter adapter = new IngredientAdapter(this);
//
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        // Get a new or existing ViewModel from the ViewModelProvider.
//        mIngredientViewModel = ViewModelProviders.of(this).get(IngredientViewModel.class);
//
//        // Add an observer on the LiveData returned by getAlphabetizedWords.
//        // The onChanged() method fires when the observed data changes and the activity is
//        // in the foreground.
//        mIngredientViewModel.getAllIngredients().observe(this, new Observer<List<Ingredient>>() {
//            @Override
//            public void onChanged(@Nullable final List<Ingredient> ingredients) {
//                // Update the cached copy of the words in the adapter.
//                adapter.setIngredients(ingredients);
//            }
//        });
    }

    private void initFloatingButton() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = ActivityMain.this.getLayoutInflater();

                final View dialogView = inflater.inflate(R.layout.dialog_newingredient, null);

                AutoCompleteTextView ingredientNameInput = dialogView.findViewById(R.id.dialog_newingredient_name);
                ArrayAdapter<String> ingredientNameAdapter = new ArrayAdapter<>(ActivityMain.this, android.R.layout.simple_list_item_1, Constants.INGREDIENT_SUGGESTIONS);
                ingredientNameInput.setAdapter(ingredientNameAdapter);

                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityMain.this, R.style.AppTheme_DialogAlert);
                builder.setTitle("Neue Zutaten erfassen");

                builder.setView(dialogView);

                // Set up the buttons
                builder.setPositiveButton("Speichern", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText nameInput = dialogView.findViewById(R.id.dialog_newingredient_name);
                        DatabaseInitializer.insertIngredientAsync(AppDatabase.getAppDatabase(ActivityMain.this), nameInput.getText().toString());
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                Dialog dialog = builder.create();
                dialog.show();
            }
        });
    }
}
