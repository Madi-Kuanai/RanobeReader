import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.widget.ImageView

object ImageUtils {
    fun blurImage(context: Context, imageView: ImageView, radius: Float) {
        val drawable = imageView.drawable as BitmapDrawable
        val imageBitmap = drawable.bitmap
        val blurredBitmap = blurBitmap(context, imageBitmap, radius)
        imageView.setImageBitmap(blurredBitmap)
    }

    public fun blurBitmap(context: Context, imageBitmap: Bitmap, radius: Float): Bitmap {
        // Create a new bitmap for the blurred image
        val blurredBitmap =
            Bitmap.createBitmap(imageBitmap.width, imageBitmap.height, Bitmap.Config.ARGB_8888)

        // Create RenderScript and ScriptIntrinsicBlur objects
        val renderScript = RenderScript.create(context)
        val blurScript = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript))

        // Create Allocation objects for input and output bitmap
        val inputAllocation = Allocation.createFromBitmap(renderScript, imageBitmap)
        val outputAllocation = Allocation.createFromBitmap(renderScript, blurredBitmap)

        // Set blur radius and blur the image
        blurScript.setRadius(radius)
        blurScript.setInput(inputAllocation)
        blurScript.forEach(outputAllocation)

        // Copy the output bitmap to the blurred bitmap
        outputAllocation.copyTo(blurredBitmap)

        // Clean up resources
        renderScript.destroy()
        inputAllocation.destroy()
        outputAllocation.destroy()
        return blurredBitmap
    }
}