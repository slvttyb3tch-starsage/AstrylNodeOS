package org.astryl.coven.security

import androidx.exifinterface.media.ExifInterface
import java.io.File

class AstrylMediaSanitizer {

    // strips all gps and device metadata from an image before encryption
    fun stripExifData(imageFile: File) {
        if (!imageFile.exists()) return
        
        val exif = ExifInterface(imageFile.absolutePath)
        
        // nuke all gps and device tracking tags
        exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE, null)
        exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, null)
        exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, null)
        exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, null)
        exif.setAttribute(ExifInterface.TAG_GPS_TIMESTAMP, null)
        exif.setAttribute(ExifInterface.TAG_MAKE, null)
        exif.setAttribute(ExifInterface.TAG_MODEL, null)
        
        // save the sanitized image back to the file
        exif.saveAttributes()
    }
}