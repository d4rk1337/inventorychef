package net.d4rk.inventorychef.inventory.expandablelistview.roomembedded.model;

public class PositionMapping {

    private int mGroupIndex;
    private int mChildIndex;

    public PositionMapping(int groupIndex, int childIndex) {
        mGroupIndex = groupIndex;
        mChildIndex = childIndex;
    }

    public int getGroupIndex() {
        return mGroupIndex;
    }

    public int getChildIndex() {
        return mChildIndex;
    }
}
