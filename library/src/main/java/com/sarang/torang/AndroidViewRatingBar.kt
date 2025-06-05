package com.sarang.torang

import android.R
import android.content.res.ColorStateList
import android.util.Log
import android.widget.RatingBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView

@Composable
internal fun AndroidViewRatingBar(
    modifier: Modifier = Modifier,
    rating: Float,
    isSmall: Boolean = true,
    changable: Boolean = true,
    progressTintColor: Color? = null,
) {
    // Adds view to Compose
    AndroidView(
        modifier = modifier,
        factory = { context ->
            // Creates view
            if (isSmall) {
                RatingBar(context, null, R.attr.ratingBarStyleSmall).apply {
                    // Sets up listeners for View -> Compose communication
                    this.rating = rating
                    Log.d("AndroidViewRatingBar", "${rating}")
                    setIsIndicator(!changable)

                    progressTintColor?.let {
                        progressTintList = ColorStateList(
                            arrayOf(
                                intArrayOf(R.attr.state_enabled), // enabled
                            ), intArrayOf(
                                it.hashCode(),
                            )
                        )

                        secondaryProgressTintList = ColorStateList(
                            arrayOf(
                                intArrayOf(R.attr.state_enabled), // enabled
                            ), intArrayOf(
                                it.hashCode(),
                            )
                        )
                    }

                }
            } else {
                RatingBar(context).apply {
                    // Sets up listeners for View -> Compose communication
                    this.rating = rating
                    setIsIndicator(!changable)

                    progressTintColor?.let {
                        progressTintList = ColorStateList(
                            arrayOf(
                                intArrayOf(R.attr.state_enabled), // enabled
                            ), intArrayOf(
                                it.hashCode(),
                            )
                        )

                        secondaryProgressTintList = ColorStateList(
                            arrayOf(
                                intArrayOf(R.attr.state_enabled), // enabled
                            ), intArrayOf(
                                it.hashCode(),
                            )
                        )
                    }

                }
            }
        },
        update = { view ->
            view.rating = rating
            // View's been inflated or state read in this block has been updated
            // Add logic here if necessary

            // As selectedItem is read here, AndroidView will recompose
            // whenever the state changes
            // Example of Compose -> View communication
        }
    )
}
