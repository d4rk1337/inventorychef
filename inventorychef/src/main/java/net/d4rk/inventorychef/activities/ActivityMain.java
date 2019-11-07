package net.d4rk.inventorychef.activities;

import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import net.d4rk.inventorychef.R;
import net.d4rk.inventorychef.constants.Constants;
import net.d4rk.inventorychef.database.room.AppDatabase;
import net.d4rk.inventorychef.inventory.expandablelistview.roomembedded.RecyclerItemTouchHelper;
import net.d4rk.inventorychef.inventory.expandablelistview.roomembedded.adapter.ExpandableInventoryAdapter;
import net.d4rk.inventorychef.inventory.expandablelistview.roomembedded.adapter.StoragePlaceAndIngredientAdapter;
import net.d4rk.inventorychef.inventory.expandablelistview.roomembedded.database.dao.Ingredient;
import net.d4rk.inventorychef.inventory.expandablelistview.roomembedded.database.dao.StoragePlaceAndAllIngredients;
import net.d4rk.inventorychef.inventory.expandablelistview.roomembedded.database.dao.StoragePlaceAndAllIngredientsViewModel;
import net.d4rk.inventorychef.inventory.expandablelistview.storageplaceingredientlist.unused.IngredientViewModel;
import net.d4rk.inventorychef.util.DatabaseInitializer;

import java.util.List;

public class ActivityMain
        extends AppCompatActivity
        implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener{

    private static final String TAG = ActivityMain.class.getName();

    private IngredientViewModel mIngredientViewModel;
    private StoragePlaceAndAllIngredientsViewModel mStoragePlaceAndAllIngredientsViewModel;

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

        // ################################################################################
        // ListView (ExpandableAdapter) with mock data
        // ################################################################################

//        List<Inventory> inventory = new ArrayList<>();
//
//        RecyclerView recyclerView = findViewById(R.id.recyclerview);
//
//        InventoryAdapter inventoryAdapter = new InventoryAdapter(inventory);
//
//        recyclerView.setAdapter(inventoryAdapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//
//        StoragePlace storageItem = new StoragePlace();
//        storageItem.setId(1);
//        storageItem.setName("Küche");
//
//        List<Ingredient> ingredientList = new ArrayList<Ingredient>();
//
//        Ingredient ingredientItem = new Ingredient();
//        ingredientItem.setId(2);
//        ingredientItem.setName("Reis");
//        ingredientItem.setStorageId(1);
//        ingredientItem.setAmount(1);
//
//        ingredientList.add(ingredientItem);
//
//        Inventory inventoryItem = new Inventory(storageItem, ingredientList);
//
//        inventory.add(inventoryItem);
//
//        inventoryAdapter.notifyDataSetChanged();

        // ################################################################################
        // ListView (ExpandableAdapter) with LiveData
        // ################################################################################

        RecyclerView recyclerView = findViewById(R.id.recyclerview);

        final StoragePlaceAndIngredientAdapter adapter = new StoragePlaceAndIngredientAdapter();

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Swipe addons
        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);


        // Get a new or existing ViewModel from the ViewModelProvider.
        mStoragePlaceAndAllIngredientsViewModel = ViewModelProviders.of(this).get(StoragePlaceAndAllIngredientsViewModel.class);

        // Add an observer on the LiveData returned by getAlphabetizedWords.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        mStoragePlaceAndAllIngredientsViewModel.getAllStoragePlacesAndIngredients().observe(this, new Observer<List<StoragePlaceAndAllIngredients>>() {
            @Override
            public void onChanged(@Nullable final List<StoragePlaceAndAllIngredients> storagePlaceAndAllIngredients) {
                // Update the cached copy of the words in the adapter.
                adapter.setStoragePlaceAndAllIngredients(storagePlaceAndAllIngredients);
            }
        });

        // ################################################################################
        // ListView with LiveData
        // ################################################################################

//        RecyclerView recyclerView = findViewById(R.id.recyclerview);
//
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
                final View dialogView = LayoutInflater.from(ActivityMain.this).inflate(R.layout.dialog_newingredient, (ViewGroup) view.getParent(), false);

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

    @Override
    public void onSwiped(
            RecyclerView.ViewHolder viewHolder,
            int direction,
            int position
    ) {
        if (viewHolder instanceof ExpandableInventoryAdapter.IngredientViewHolder) {
            ExpandableInventoryAdapter.IngredientViewHolder convertedViewModel = (ExpandableInventoryAdapter.IngredientViewHolder) viewHolder;

            int viewHolderAdapterPosition = viewHolder.getAdapterPosition();

            final Ingredient swipedIngredient = (Ingredient) convertedViewModel.itemView.getTag();

            // get the removed item name to display it in snack bar
            String name = swipedIngredient.getName();

            // backup of removed item for undo purpose
            final int swipedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
//            mAdapter.removeItem(viewHolder.getAdapterPosition());

            DatabaseInitializer.deleteIngredientsAsync(AppDatabase.getAppDatabase(ActivityMain.this), swipedIngredient);

            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar
                    .make(findViewById(R.id.coordinator_layout), name + " removed from inventory!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // undo is selected, restore the deleted item
//                    mAdapter.restoreItem(deletedItem, deletedIndex);
                    DatabaseInitializer.reactivateIngredientsAsync(AppDatabase.getAppDatabase(ActivityMain.this), swipedIngredient);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }
}
