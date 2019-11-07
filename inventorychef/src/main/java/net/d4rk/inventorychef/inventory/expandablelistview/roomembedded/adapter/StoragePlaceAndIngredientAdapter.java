package net.d4rk.inventorychef.inventory.expandablelistview.roomembedded.adapter;

import net.d4rk.inventorychef.inventory.expandablelistview.roomembedded.database.dao.StoragePlaceAndAllIngredients;
import net.d4rk.inventorychef.inventory.expandablelistview.roomembedded.model.PositionMapping;

import java.util.List;

public class StoragePlaceAndIngredientAdapter
        extends ExpandableInventoryAdapter
        <
                ExpandableInventoryAdapter.StorageViewHolder,
                ExpandableInventoryAdapter.IngredientViewHolder
        > {

    public StoragePlaceAndIngredientAdapter() {
        super();
    }


    public void setStoragePlaceAndAllIngredients(
            List<StoragePlaceAndAllIngredients> storagePlaceAndAllIngredients
    ) {
        mStoragePlacesAndIngredients = storagePlaceAndAllIngredients;

        mGroupIndexList.clear();
        mElementIndexList.clear();

        int currentPositionIndex = 0;
        int currentGroupIndex = 0;

        for(StoragePlaceAndAllIngredients currentGroupItem : mStoragePlacesAndIngredients) {

            mGroupIndexList.put(currentPositionIndex++, currentGroupIndex);

            for (int currentChildIndex = 0; currentChildIndex < currentGroupItem.getIngredients().size(); currentChildIndex++) {
                mElementIndexList.put(currentPositionIndex++, new PositionMapping(currentGroupIndex, currentChildIndex));
            }

            currentGroupIndex++;

        }

        notifyDataSetChanged();
    }
}
