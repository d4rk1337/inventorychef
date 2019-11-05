package net.d4rk.inventorychef.inventory.expandablelistview.storageplaceingredientlist.unused;

import android.view.View;
import android.widget.TextView;

//import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
//import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import net.d4rk.inventorychef.R;

public class InventoryViewHolder {
//        extends GroupViewHolder {

    public TextView inventoryName;

    public InventoryViewHolder(
            View itemView
    ) {
//        super(itemView);

        inventoryName = itemView.findViewById(R.id.storage_name);
    }

    public void setInventoryGroupName(
//            ExpandableGroup inventory
    ) {
//        inventoryName.setText(inventory.getTitle());
    }
}
