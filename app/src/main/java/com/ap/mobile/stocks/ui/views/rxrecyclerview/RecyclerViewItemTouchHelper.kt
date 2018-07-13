package com.ap.mobile.stocks.ui.views.rxrecyclerview


import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper


/**
 * Recycler view on item actions listener
 * onDrag and onSwipe
 */
class RecyclerViewItemTouchHelper(
    private val adapter: ItemTouchHelperAdapter
): ItemTouchHelper.Callback() {


    /**
     * override movement flags for recycler view item (determine the direction of an event)
     * For Drag: User can drop up or down
     * For SwipeL: User can swipe left or right
     */
    override fun getMovementFlags(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?): Int {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
        return ItemTouchHelper.Callback.makeMovementFlags(dragFlags, swipeFlags)
    }

    /**
     * enable long press
     * (drag will be supported on long press only)
     */
    override fun isLongPressDragEnabled(): Boolean {
        return true
    }

    /**
     * enable swipe for recycler view item
     */
    override fun isItemViewSwipeEnabled(): Boolean {
        return true
    }

    /**
     * handle on move action for recycler view item
     */
    override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        adapter.onItemMove(
            viewHolder.adapterPosition,
            target.adapterPosition)
        return true
    }

    /**
     * handle swipe action for recycler view item
     */
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        adapter.onItemDismiss(viewHolder.adapterPosition)
    }
}