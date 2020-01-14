package net.d4rk.inventorychef.inventory.expandablelistview.roomembedded.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import net.d4rk.inventorychef.R;
import net.d4rk.inventorychef.database.room.AppDatabase;
import net.d4rk.inventorychef.inventory.expandablelistview.roomembedded.database.dao.StoragePlace;
import net.d4rk.inventorychef.util.DatabaseInitializer;

import java.util.ArrayList;
import java.util.List;

public class StoragePlaceAdapter extends ArrayAdapter<StoragePlace> {

    private static StoragePlaceAdapter mInstance;

    Context mContext = null;

    View mSnackbarViw = null;

    List<StoragePlace> mStoragePlaces;

    public StoragePlaceAdapter(Context context, int defaultResource, List<StoragePlace> storagePlaces, View snackbarView) {
        super(context, defaultResource, storagePlaces);

        mContext = context;
        mStoragePlaces = storagePlaces;

        mSnackbarViw = snackbarView;
    }

    public static StoragePlaceAdapter getInstance(Context context, View snackbarView) {
        if (mInstance != null) {
            return mInstance;
        }

        mInstance = new StoragePlaceAdapter(context, R.layout.spinner_item_simple, new ArrayList<StoragePlace>(), snackbarView);

        return mInstance;
    }

    @Override
    public View getDropDownView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.spinner_item_simple, parent, false);
        } else if (mStoragePlaces.get(position).getIngredientCount() == 0) {
//            ((TextView) defaultView).setTextColor(mContext.getResources().getColor(android.R.color.holo_red_dark));
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.spinner_item_delete, parent, false);

            ImageButton spinnerDeleteButton = view.findViewById(R.id.spinner_deletestorageplace);
            spinnerDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseInitializer.deleteStoragePlaceAsync(AppDatabase.getAppDatabase(mContext), mStoragePlaces.get(position));

                    // showing snack bar with Undo option
                    Snackbar snackbar = Snackbar.make(mSnackbarViw, mStoragePlaces.get(position).getName() + " removed!", Snackbar.LENGTH_INDEFINITE);

                    snackbar.setAction("UNDO", new IngredientReactivateListener(mContext, mStoragePlaces.get(position)));
                    snackbar.setActionTextColor(Color.YELLOW);

                    snackbar.show();
                }
            });
        }

        TextView spinnerItemText = view.findViewById(R.id.spinner_text);

        spinnerItemText.setText(mStoragePlaces.get(position).getName());

        return view;
    }

    public void setStoragePlaces(List<StoragePlace> storagePlaces) {
        mStoragePlaces.clear();

        mStoragePlaces.addAll(storagePlaces);

        notifyDataSetChanged();
    }

    private class IngredientReactivateListener implements View.OnClickListener {

        private Context mContext;
        private StoragePlace mDeletedStoragePlace;

        public IngredientReactivateListener(
                Context context,
                StoragePlace deletedStoragePlace
        ) {
            mContext = context;
            mDeletedStoragePlace = deletedStoragePlace;
        }

        @Override
        public void onClick(View view) {
            // reactivate the deleted ingredient
            DatabaseInitializer.reactivateStoragePlacesAsync(AppDatabase.getAppDatabase(mContext), mDeletedStoragePlace);
        }
    }

}
