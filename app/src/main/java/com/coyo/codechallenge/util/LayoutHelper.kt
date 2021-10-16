package com.coyo.codechallenge.util

import android.content.Context
import android.widget.LinearLayout

/**
 * Helper class to create different layout params
 */
class LayoutHelper {
    companion object {
        const val MATCH_PARENT = -1
        const val WRAP_CONTENT = -2

        private fun getSize(context: Context, size: Float): Int {
            return (if (size < 0) size else AndroidUtils.dp(context, size)).toInt()
        }

        fun createLinear(
            context: Context,
            width: Int,
            height: Int,
            gravity: Int,
            leftMargin: Int,
            topMargin: Int,
            rightMargin: Int,
            bottomMargin: Int
        ): LinearLayout.LayoutParams {
            val layoutParams = LinearLayout.LayoutParams(
                getSize(context, width.toFloat()),
                getSize(context, height.toFloat()),
            )
            layoutParams.setMargins(
                getSize(context, leftMargin.toFloat()),
                getSize(context, topMargin.toFloat()),
                getSize(context, rightMargin.toFloat()),
                getSize(context, bottomMargin.toFloat())
            )
            layoutParams.gravity = gravity
            return layoutParams
        }

        fun createLinear(
            context: Context,
            width: Int,
            height: Int,
            gravity: Int
        ): LinearLayout.LayoutParams {
            val layoutParams = LinearLayout.LayoutParams(
                getSize(context, width.toFloat()),
                getSize(context, height.toFloat())
            )
            layoutParams.gravity = gravity
            return layoutParams
        }

        fun createLinear(
            context: Context,
            width: Int,
            height: Int,
            weight: Float,
            gravity: Int
        ): LinearLayout.LayoutParams {
            val layoutParams = LinearLayout.LayoutParams(
                getSize(context, width.toFloat()),
                getSize(context, height.toFloat()), weight
            )
            layoutParams.gravity = gravity
            return layoutParams
        }
    }
}