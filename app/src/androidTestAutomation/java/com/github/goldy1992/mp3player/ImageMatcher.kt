package com.github.goldy1992.mp3player

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

class ImageMatcher(@DrawableRes private val expectedId : Int) : TypeSafeMatcher<View>(View::class.java) {

    private var resourceName: String? = null
    val EMPTY = -1
    val ANY = -2

    override fun describeTo(description: Description) {
        description.appendText("with drawable from resource id: ")
        description.appendValue(expectedId)
        if (resourceName != null) {
            description.appendText("[")
            description.appendText(resourceName)
            description.appendText("]")
        }
    }

    override fun matchesSafely(target: View?): Boolean {

            if (!(target is ImageView)) {
                return false;
            }
            var imageView : ImageView =  target
            if (expectedId == EMPTY) {
                return imageView.getDrawable() == null
            }
            if (expectedId == ANY) {
                return imageView.getDrawable() != null
            }

            val resources : Resources = target.getContext().resources
            val theme : Resources.Theme = target.context.theme
            val expectedDrawable : Drawable = resources.getDrawable(expectedId, theme)
            resourceName = resources.getResourceEntryName(expectedId)

            if (expectedDrawable == null) {
                return false;
            }

            val bitmap : Bitmap = getBitmap(imageView.getDrawable());
            val otherBitmap : Bitmap = getBitmap(expectedDrawable);
            return bitmap.sameAs(otherBitmap);
        }

        private fun getBitmap(drawable : Drawable) : Bitmap {
            val bitmap : Bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888);
            val canvas = Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.width, canvas.height);
            drawable.draw(canvas);
            return bitmap;
        }

}