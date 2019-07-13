package me.melijn.dumbhome.objects

import android.view.ViewGroup.MarginLayoutParams
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.databinding.BindingAdapter
import me.melijn.dumbhome.R
import me.melijn.dumbhome.components.Location
import me.melijn.dumbhome.ui.sync.ITEM_VIEW_TYPE_SWITCH
import me.melijn.dumbhome.utils.toPx


@BindingAdapter("locationImage")
fun ImageView.setLocationImage(location: Location?) {
    location?.let {
        setImageResource(
            when (location) {
                Location.BUITEN -> R.drawable.ic_local_florist_black_24dp
                Location.FABIAN_KAMER, Location.MERLIJN_KAMER -> R.drawable.ic_kid_bed
                Location.SPEELKAMER -> R.drawable.ic_supernintendo_controller
                Location.WOONKAMER -> R.drawable.ic_family_sofa
                Location.ANDERE -> R.drawable.ic_lightbulb_outline_black_24dp
            }
        )
    }
}

@BindingAdapter("deviceImage")
fun ImageView.setDeviceImage(id: Int?) {
    id?.let {
        setImageResource(
            when (id) {
                ITEM_VIEW_TYPE_SWITCH -> R.drawable.ic_switch
                else -> R.drawable.ic_lightbulb_outline_black_24dp
            }
        )
    }
}

@BindingAdapter("startMargin")
fun setStartMargin(view: CardView, id: Int) {
    val layoutParams = view.layoutParams as MarginLayoutParams
    layoutParams.marginStart = (if (id % 2 == 0) 16 else 8).toPx()
    view.layoutParams = layoutParams
}

@BindingAdapter("endMargin")
fun setEndMargin(view: CardView, id: Int) {
    val layoutParams = view.layoutParams as MarginLayoutParams
    layoutParams.marginEnd = (if (id % 2 == 0) 8 else 16).toPx()
    view.layoutParams = layoutParams
}