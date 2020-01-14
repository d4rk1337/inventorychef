package net.d4rk.inventorychef.activities;

import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import net.d4rk.inventorychef.R;
import net.d4rk.inventorychef.constants.Constants;
import net.d4rk.inventorychef.database.room.AppDatabase;
import net.d4rk.inventorychef.inventory.expandablelistview.roomembedded.RecyclerItemTouchHelper;
import net.d4rk.inventorychef.inventory.expandablelistview.roomembedded.adapter.ExpandableInventoryAdapter;
import net.d4rk.inventorychef.inventory.expandablelistview.roomembedded.adapter.StoragePlaceAdapter;
import net.d4rk.inventorychef.inventory.expandablelistview.roomembedded.adapter.StoragePlaceAndIngredientAdapter;
import net.d4rk.inventorychef.inventory.expandablelistview.roomembedded.database.dao.Ingredient;
import net.d4rk.inventorychef.inventory.expandablelistview.roomembedded.database.dao.StoragePlace;
import net.d4rk.inventorychef.inventory.expandablelistview.roomembedded.database.dao.StoragePlaceAndAllIngredients;
import net.d4rk.inventorychef.inventory.expandablelistview.roomembedded.database.dao.StoragePlaceAndAllIngredientsViewModel;
import net.d4rk.inventorychef.inventory.expandablelistview.roomembedded.database.dao.StoragePlaceViewModel;
import net.d4rk.inventorychef.inventory.expandablelistview.storageplaceingredientlist.unused.IngredientViewModel;
import net.d4rk.inventorychef.util.DatabaseInitializer;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Locale;

public class ActivityMain
        extends AppCompatActivity
        implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener{

    private static final String TAG = ActivityMain.class.getName();

    private IngredientViewModel mIngredientViewModel;
    private StoragePlaceAndAllIngredientsViewModel mStoragePlaceAndAllIngredientsViewModel;

    private StoragePlaceViewModel mStoragePlaceViewModel;

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
                Dialog dialog = null;

                final View dialogView = LayoutInflater.from(ActivityMain.this).inflate(R.layout.dialog_newingredient, (ViewGroup) view.getParent(), false);

                AutoCompleteTextView ingredientNameInput = dialogView.findViewById(R.id.dialog_newingredient_name);
                AutoCompleteTextView storageNameInput = dialogView.findViewById(R.id.dialog_newingredient_newstorage);

                Spinner storages = dialogView.findViewById(R.id.dialog_newingredient_storages);

                ImageButton addStorageName = dialogView.findViewById(R.id.dialog_newingredient_addstorage);

                // set adapter and observer
                storages.setAdapter(StoragePlaceAdapter.getInstance(ActivityMain.this, dialogView));

                // Get a new or existing ViewModel from the ViewModelProvider.
                mStoragePlaceViewModel = ViewModelProviders.of(ActivityMain.this).get(StoragePlaceViewModel.class);

                // Add an observer on the LiveData returned by getAlphabetizedWords.
                // The onChanged() method fires when the observed data changes and the activity is
                // in the foreground.
                mStoragePlaceViewModel.getAllActiveStoragePlaces().observe(ActivityMain.this, new Observer<List<StoragePlace>>() {
                    @Override
                    public void onChanged(@Nullable final List<StoragePlace> activeStoragePlaces) {
                        // Update the cached copy of the words in the adapter.
                        StoragePlaceAdapter.getInstance(ActivityMain.this, dialogView).setStoragePlaces(activeStoragePlaces);
                    }
                });


                ArrayAdapter<String> ingredientNameAdapter = new ArrayAdapter<>(ActivityMain.this, android.R.layout.simple_list_item_1, Constants.INGREDIENT_SUGGESTIONS);
                ingredientNameInput.setAdapter(ingredientNameAdapter);

                ArrayAdapter<String> storageNameAdapter = new ArrayAdapter<>(ActivityMain.this, android.R.layout.simple_list_item_1, Constants.LOCATION_SUGGESTIONS);
                storageNameInput.setAdapter(storageNameAdapter);

                // set actions
                addStorageName.setOnClickListener(new StoragePlaceAddListener(storageNameInput));

                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityMain.this, R.style.AppTheme_DialogAlert);
                builder.setTitle("Neue Zutaten erfassen");

                builder.setView(dialogView);

                // Set up the buttons
                builder.setPositiveButton("Speichern", null);
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                dialog = builder.create();

                dialog.setOnShowListener(new DialogInterface.OnShowListener() {

                    @Override
                    public void onShow(final DialogInterface dialog) {

                        Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                        button.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                EditText nameInput = dialogView.findViewById(R.id.dialog_newingredient_name);
                                EditText storagePlaceInput = dialogView.findViewById(R.id.dialog_newingredient_newstorage);
                                Spinner storages = dialogView.findViewById(R.id.dialog_newingredient_storages);

                                if(StringUtils.isEmpty(nameInput.getText().toString())) {
                                    nameInput.setError(getString(R.string.dialog_newingredient_error_name));

                                    return;
                                } else {
                                    nameInput.setError(null);
                                }

                                if(storages.getSelectedItem() == null) {
                                    nameInput.setError(getString(R.string.dialog_newingredient_error_storageplace));

                                    return;
                                } else {
                                    nameInput.setError(null);
                                }


//                        ingredient.setName(nameInput.getText().toString());
//                        ingredient.setStorageId();
//
//                        DatabaseInitializer.insertIngredientAsync(AppDatabase.getAppDatabase(ActivityMain.this), ingredient);

                                dialog.dismiss();
                            }
                        });
                    }
                });

                dialog.show();
            }
        });
    }

    private class StoragePlaceAddListener implements View.OnClickListener {

        EditText mStoragePlaceInput = null;

        public StoragePlaceAddListener(EditText storagePlaceInput) {
            mStoragePlaceInput = storagePlaceInput;
        }

        @Override
        public void onClick(View view) {
            if (StringUtils.isEmpty(mStoragePlaceInput.getText().toString())) {
                mStoragePlaceInput.setError(getString(R.string.dialog_newingredient_error_newstorageplace));

                return;
            } else {
                mStoragePlaceInput.setError(null);
            }

            if (mStoragePlaceViewModel.isStoragePlaceNameTaken(mStoragePlaceInput.getText().toString().toLowerCase(Locale.getDefault()))) {
                mStoragePlaceInput.setError(getString(R.string.dialog_newingredient_error_newstorageplace_taken));

                return;
            } else {
                mStoragePlaceInput.setError(null);
            }

            StoragePlace storagePlace = new StoragePlace();

            storagePlace.setName(mStoragePlaceInput.getText().toString());

            mStoragePlaceViewModel.insertNewStoragePlace(storagePlace);

            mStoragePlaceInput.setText("");
        }
    }

    @Override
    public void onSwiped(
            RecyclerView.ViewHolder viewHolder,
            int direction,
            int position
    )
    {
        // only delete ingredients
        if (viewHolder instanceof ExpandableInventoryAdapter.IngredientViewHolder) {
            // get the Ingredient object from holder tag
            ExpandableInventoryAdapter.IngredientViewHolder convertedViewModel = (ExpandableInventoryAdapter.IngredientViewHolder) viewHolder;
            Ingredient swipedIngredient = (Ingredient) convertedViewModel.itemView.getTag();

            // set the deleted flag for selected ingredient (list will be refreshed automatically through observer)
            DatabaseInitializer.deleteIngredientsAsync(AppDatabase.getAppDatabase(ActivityMain.this), swipedIngredient);

            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar.make(findViewById(R.id.coordinator_layout), swipedIngredient.getName() + " removed from inventory!", Snackbar.LENGTH_INDEFINITE);

            snackbar.setAction("UNDO", new IngredientReactivateListener(ActivityMain.this, swipedIngredient));
            snackbar.setActionTextColor(Color.YELLOW);

            snackbar.show();
        }
    }

    private class IngredientReactivateListener implements View.OnClickListener {

        private Context mContext;
        private Ingredient mSwipedIngredient;

        public IngredientReactivateListener(
                Context context,
                Ingredient swipedIngredient
        ) {
            mContext = context;
            mSwipedIngredient = swipedIngredient;
        }

        @Override
        public void onClick(View view) {
            // reactivate the deleted ingredient
            DatabaseInitializer.reactivateIngredientsAsync(AppDatabase.getAppDatabase(mContext), mSwipedIngredient);
        }
    }
}
