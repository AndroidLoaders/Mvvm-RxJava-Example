package com.example.mvvm_example.utility

import android.graphics.*
import android.media.ExifInterface
import android.util.Base64
import android.widget.ImageView
import com.app.tagglifedatingapp.utility.FileIO
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.mvvm_example.R
import java.io.*
import java.net.HttpURLConnection
import java.net.URL

object ImageProcessor {

    private val TAG = ImageProcessor::class.java.simpleName

    private const val URL_PREFIX: String = "http://"

    private val requestOptions: RequestOptions by lazy {
        RequestOptions().diskCacheStrategy(DiskCacheStrategy.DATA)
            .fallback(R.drawable.ic_place_holder).placeholder(R.drawable.ic_place_holder)
            .error(R.drawable.ic_place_holder)
    }

    private fun calculateInSampleSize(
        options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int
    ): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val heightRatio = Math.round(height.toFloat() / reqHeight.toFloat())
            val widthRatio = Math.round(width.toFloat() / reqWidth.toFloat())
            inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
        }
        val totalPixels = (width * height).toFloat()
        val totalReqPixelsCap = (reqWidth * reqHeight * 2).toFloat()
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++
        }
        return inSampleSize
    }

    private fun validateImageUrl(imageUrl: String): String =
        if (imageUrl.startsWith("http")) imageUrl else "$URL_PREFIX$imageUrl"

    @Throws(Exception::class)
    fun getImageFromUrl(imageUrl: String): Bitmap? {
        val url = URL(imageUrl)
        val connection = url.openConnection() as HttpURLConnection
        connection.doInput = true
        connection.connect()
        val input = connection.inputStream
        return BitmapFactory.decodeStream(input)
    }

    fun loadProfilePic(imageView: ImageView, imageUrl: String = "") =
        Glide.with(imageView.context).load(validateImageUrl(imageUrl))
            .transition(GenericTransitionOptions.with(android.R.anim.fade_in))
            .apply(requestOptions).into(imageView)

    fun loadResizedProfilePic(imageView: ImageView, imageUrl: String = "", size: Int) =
        Glide.with(imageView.context).load(validateImageUrl(imageUrl))
            .transition(GenericTransitionOptions.with(android.R.anim.fade_in))
            .apply(requestOptions.override(size, size))
            .into(imageView)

    fun loadAdvertisement(advertisementUrl: String, imageView: ImageView) =
        Glide.with(imageView.context).load(validateImageUrl(advertisementUrl))
            .thumbnail(0.1F).transition(GenericTransitionOptions.with(android.R.anim.fade_in))
            .apply(requestOptions).into(imageView)

    fun loadThumbnailFromUrl(imageUrl: String, imageView: ImageView) =
        Glide.with(imageView.context).load(validateImageUrl(imageUrl)).thumbnail(0.1F)
            .transition(GenericTransitionOptions.with(android.R.anim.fade_in))
            .apply(requestOptions).into(imageView)

    fun loadThumbnailFromFile(imagePath: String, imageView: ImageView) {
        Glide.with(imageView.context)
            .load(File(FileIO.SentFolderAbsolutePath.plus(imagePath)))
            .thumbnail(0.1F)
            .transition(GenericTransitionOptions.with(android.R.anim.fade_in))
            .apply(requestOptions)
            .into(imageView)
    }

    fun loadThumbnailFromFile(imagePath: File, imageView: ImageView) {
        Glide.with(imageView.context).load(imagePath)
            .transition(GenericTransitionOptions.with(android.R.anim.fade_in))
            .apply(requestOptions)
            .into(imageView)
    }

    @Throws(Exception::class)
    fun getBase64String(bitmap: Bitmap?): String {
        if (bitmap != null) {
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
            val b = baos.toByteArray()
            return Base64.encodeToString(b, Base64.DEFAULT) ?: ""
        }
        return ""
    }

    // URL : https://android--examples.blogspot.com/2015/11/android-circular-bitmap-with-border-and.html
    fun getResizedBitmap(bm: Bitmap, newHeight: Int, newWidth: Int): Bitmap {
        val width = bm.width
        val height = bm.height
        val scaleWidth = newWidth.toFloat() / width
        val scaleHeight = newHeight.toFloat() / height

        // create a matrix for the manipulation
        val matrix = Matrix()

        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight)

        // recreate the new Bitmap
        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true)
    }

    fun getCircularBitmap(srcBitmap: Bitmap): Bitmap {
        val squareBitmapWidth = Math.min(srcBitmap.width, srcBitmap.height)
        val dstBitmap =
            Bitmap.createBitmap(squareBitmapWidth, squareBitmapWidth, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(dstBitmap)

        val paint = Paint()
        paint.isAntiAlias = true

        val rect = Rect(0, 0, squareBitmapWidth, squareBitmapWidth)
        val rectF = RectF(rect)

        canvas.drawOval(rectF, paint)

        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)

        val left = ((squareBitmapWidth - srcBitmap.width) / 2).toFloat()
        val top = ((squareBitmapWidth - srcBitmap.height) / 2).toFloat()
        canvas.drawBitmap(srcBitmap, left, top, paint)
        srcBitmap.recycle()

        return dstBitmap
    }

    /*private fun addBorderToCircularBitmap(
        srcBitmap: Bitmap, borderWidth: Int, borderColor: Int
    ): Bitmap {
        // Calculate the circular bitmap width with border
        val dstBitmapWidth = srcBitmap.width + borderWidth * 2

        // Initialize a new Bitmap to make it bordered circular bitmap
        val dstBitmap =
            Bitmap.createBitmap(dstBitmapWidth, dstBitmapWidth, Bitmap.Config.ARGB_8888)

        // Initialize a new Canvas instance
        val canvas = Canvas(dstBitmap)
        // Draw source bitmap to canvas
        canvas.drawBitmap(srcBitmap, borderWidth.toFloat(), borderWidth.toFloat(), null)

        // Initialize a new Paint instance to draw border
        val paint = Paint()
        paint.color = borderColor
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = borderWidth.toFloat()
        paint.isAntiAlias = true

        // Draw the circular border around circular bitmap
        canvas.drawCircle(
            (canvas.width / 2).toFloat()*//*cx*//*, (canvas.width / 2).toFloat()*//*cy*//*,
                (canvas.width / 2 - borderWidth / 2).toFloat() *//*Radius*//*, paint // Paint
            )

            // Free the native object associated with this bitmap.
            srcBitmap.recycle()

            // Return the bordered circular bitmap
            return dstBitmap
        }*/

    /*private fun addShadowToCircularBitmap(
        srcBitmap: Bitmap, shadowWidth: Int, shadowColor: Int
    ): Bitmap {
        // Calculate the circular bitmap width with shadow
        val dstBitmapWidth = srcBitmap.width + shadowWidth * 2
        val dstBitmap =
            Bitmap.createBitmap(dstBitmapWidth, dstBitmapWidth, Bitmap.Config.ARGB_8888)

        // Initialize a new Canvas instance
        val canvas = Canvas(dstBitmap)
        canvas.drawBitmap(srcBitmap, shadowWidth.toFloat(), shadowWidth.toFloat(), null)

        // Paint to draw circular bitmap shadow
        val paint = Paint()
        paint.color = shadowColor
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = shadowWidth.toFloat()
        paint.isAntiAlias = true

        // Draw the shadow around circular bitmap
        canvas.drawCircle(
            (dstBitmapWidth / 2).toFloat() *//*cx*//*, (dstBitmapWidth / 2).toFloat() *//*cy*//*,
                (dstBitmapWidth / 2 - shadowWidth / 2).toFloat() *//*Radius*//*, paint // Paint
            )
            srcBitmap.recycle()

            return dstBitmap
        }*/

    fun compressImage(
        imagePath: String, destinationPath: String = FileIO.getTempFilename()
    ): String {

        var scaledBitmap: Bitmap? = null

        val options = BitmapFactory.Options()

        /*By setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
        you try the use the bitmap here, you will get null.*/
        options.inJustDecodeBounds = true
        var bmp = BitmapFactory.decodeFile(imagePath, options)

        var actualHeight = options.outHeight
        var actualWidth = options.outWidth

        //TODO : Max Height and width values of the compressed image is taken as 816x612
        val maxHeight = 600.0f
        val maxWidth = 400.0f
        var imgRatio = (actualWidth / actualHeight).toFloat()
        val maxRatio = maxWidth / maxHeight

        //TODO : Width and height values are set maintaining the aspect ratio of the image
        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            when {
                imgRatio < maxRatio -> {
                    imgRatio = maxHeight / actualHeight
                    actualWidth = (imgRatio * actualWidth).toInt()
                    actualHeight = maxHeight.toInt()
                }
                imgRatio > maxRatio -> {
                    imgRatio = maxWidth / actualWidth
                    actualHeight = (imgRatio * actualHeight).toInt()
                    actualWidth = maxWidth.toInt()
                }
                else -> {
                    actualHeight = maxHeight.toInt()
                    actualWidth = maxWidth.toInt()
                }
            }
        }

        //TODO : Setting inSampleSize value allows to load a scaled down version of the original image
        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight)

        //TODO : InJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false

        //TODO : This options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true
        options.inInputShareable = true
        options.inTempStorage = ByteArray(16 * 1024)

        try {
            //TODO : Load the bitmap from its path
            bmp = BitmapFactory.decodeFile(imagePath, options)
        } catch (exception: OutOfMemoryError) {
            LogPrinter.printMessage(TAG, exception.message ?: "")
        }

        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888)
        } catch (exception: OutOfMemoryError) {
            LogPrinter.printMessage(TAG, exception.message ?: "")
        }

        val ratioX = actualWidth / options.outWidth.toFloat()
        val ratioY = actualHeight / options.outHeight.toFloat()
        val middleX = actualWidth / 2.0f
        val middleY = actualHeight / 2.0f

        val scaleMatrix = Matrix()
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY)

        val canvas = Canvas(scaledBitmap!!)
        canvas.setMatrix(scaleMatrix)
        canvas.drawBitmap(
            bmp, middleX - bmp.width / 2, middleY - bmp.height / 2, Paint(Paint.FILTER_BITMAP_FLAG)
        )

        //TODO : Check the rotation of the image and display it properly
        val exif: ExifInterface
        try {
            exif = ExifInterface(imagePath)
            val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0)
            val matrix = Matrix()
            when (orientation) {
                6 -> matrix.postRotate(90f)
                3 -> matrix.postRotate(180f)
                8 -> matrix.postRotate(270f)
            }
            scaledBitmap = Bitmap.createBitmap(
                scaledBitmap, 0, 0, scaledBitmap.width, scaledBitmap.height, matrix, true
            )
        } catch (e: IOException) {
            LogPrinter.printMessage(TAG, e.message ?: "")
        }

        val out: FileOutputStream
        try {
            out = FileOutputStream(destinationPath)
            scaledBitmap!!.compress(Bitmap.CompressFormat.JPEG, 95, out)
            //TODO : Write the compressed bitmap at the destination specified by filename.
        } catch (e: FileNotFoundException) {
            LogPrinter.printMessage(TAG, e.message ?: "")
        }
        return destinationPath
    }
}