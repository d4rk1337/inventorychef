package net.d4rk.inventorychef.inventory.expandablelistview.storageplaceingredientlist.adapter;

import net.d4rk.inventorychef.inventory.expandablelistview.storageplaceingredientlist.model.Inventory;

import java.util.List;

public class InventoryAdapter
        extends ExpandableInventoryAdapter
        <
                        ExpandableInventoryAdapter.StorageViewHolder,
                        ExpandableInventoryAdapter.IngredientViewHolder
                        > {

//    public InventoryAdapter(LiveData<List<Inventory>> inventory) {
    public InventoryAdapter(List<Inventory> inventory) {
        super(inventory);
    }
}
