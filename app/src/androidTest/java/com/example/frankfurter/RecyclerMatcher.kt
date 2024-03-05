package com.example.frankfurter

import android.content.res.Resources
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

class RecyclerViewMatcher(
    private val position: Int,
    private val targetViewId: Int = -1,
    private val recyclerViewId: Int
) : TypeSafeMatcher<View>() {

    private var resources: Resources? = null
    private var childView: View? = null

    override fun describeTo(description: Description) {
        var idDescription = recyclerViewId.toString()
        if (this.resources != null) {
            idDescription = try {
                this.resources!!.getResourceName(recyclerViewId)
            } catch (e: Resources.NotFoundException) {
                String.format("%s (resource name not found)", recyclerViewId)
            }
        }

        description.appendText("RecyclerView with id: $idDescription at position: $position")
    }

    override fun matchesSafely(view: View): Boolean {

        this.resources = view.resources

        if (childView == null) {
            val recyclerView = view.rootView.findViewById<RecyclerView>(recyclerViewId)
            if (recyclerView.id == recyclerViewId) {
                val viewHolder = recyclerView.findViewHolderForAdapterPosition(position)
                if (viewHolder != null) {
                    childView = viewHolder.itemView
                }
            } else {
                return false
            }
        }

        return if (targetViewId == -1) {
            view === childView
        } else {
            val targetView = childView!!.findViewById<View>(targetViewId)
            view === targetView
        }
    }
}