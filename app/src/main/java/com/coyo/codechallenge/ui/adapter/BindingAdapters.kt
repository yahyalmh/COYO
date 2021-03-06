package com.coyo.codechallenge.ui.adapter

import androidx.databinding.BindingAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 * @author yaya (@yahyalmh)
 * @since 14th October 2021
 */

object BindingAdapters {

    /**
     * binding adapter to set fab visibility base on received condition
     */
    @BindingAdapter("isFabGone")
    @JvmStatic
    fun bindIsFabGone(view: FloatingActionButton, isGone: Boolean?) {
        if (isGone == null || isGone) {
            view.hide()
        } else {
            view.show()
        }
    }
}