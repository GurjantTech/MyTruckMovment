package com.truckmovement.mvvm.utils

import android.app.*
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.wifi.WifiManager
import android.os.Build
import android.text.format.Formatter
import android.util.Base64
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.truckmovement.mvvm.R
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun String.upperCaseFirstLetter(): String {
    return this.substring(0, 1).toUpperCase().plus(this.substring(1))
}

fun Double.formatdicimal(): String {
    return String.format("%.2f", this)
}


fun showLoadingDialog(context: Context?): ProgressDialog {
    val progressDialog = ProgressDialog(context)
    progressDialog.let {
        it.show()
        it.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        it.setContentView(R.layout.progress_dialog)
        it.isIndeterminate = true
        it.setCancelable(false)
        it.setCanceledOnTouchOutside(false)

        return it
    }
}

fun isValidEmail(email: String): Boolean {

    return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
            + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
            + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
            + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
            + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
            + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches()
}

fun showToast(c: Context, msg: String) {
    Toast.makeText(c, msg, Toast.LENGTH_SHORT).show()
}

fun convertdateformat(value: String): String {
    val fromformat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
    val toformat = SimpleDateFormat("dd-MMM")
    var newforamat: String = ""
    try {
        val date = fromformat.parse(value)
        newforamat = toformat.format(date);
        System.out.println(date)
    } catch (e: ParseException) {
        e.printStackTrace()
    }

    return newforamat
}

fun convertTime(time: String): String {
    val serverFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val newFormat = SimpleDateFormat("hh:mm a")
    val dt: Date
    var convertedTime = ""
    try {
        dt = serverFormat.parse(time)
        println("Time Display: " + newFormat.format(dt)) // <-- I got result here
        convertedTime = newFormat.format(dt)
    } catch (e: ParseException) {
        e.printStackTrace()
    }

    return convertedTime
}

/**
 * Method checks if the app is in background or not
 */
fun isAppInBackground(context: Context): Boolean {
    var isInBackground = true
    val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
        val runningProcesses = activityManager.runningAppProcesses
        for (processInfo in runningProcesses) {
            if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                for (activeProcess in processInfo.pkgList) {
                    if (activeProcess == context.packageName) {
                        isInBackground = false
                    }
                }
            }
        }
    } else {
        val taskInfo = activityManager.getRunningTasks(1)
        val componentInfo = taskInfo[0].topActivity
        if (componentInfo!!.packageName == context.packageName) {
            isInBackground = false
        }
    }

    return isInBackground
}

// Clears notification tray messages
fun clearNotifications(context: Context) {
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.cancelAll()
}

fun hideKeyboard(activity: Activity) {
    val inputMethodManager = activity
        .getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    if (inputMethodManager.isAcceptingText && activity.currentFocus != null) {
        inputMethodManager.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
        Log.e("", "Software Keyboard was shown")
    } else {
        Log.e("", "Software Keyboard was not shown")
    }
}




fun getRandomNo(min: Int, max: Int): Int {
//        val min = 65
//        val max = 80

    val r = Random()
    val i1 = r.nextInt(max - min + 1) + min

    return i1
}


fun twoDigitInteger(dp: String): String {
    return String.format("%02d", Integer.valueOf(dp))
}

fun getWidth(activity: Activity): Int {
    val display = activity.windowManager.defaultDisplay
    val size = Point()
    display.getSize(size)
    return size.x

}

fun getHeight(activity: Activity): Int {
    val displaymetrics = DisplayMetrics()
    activity.windowManager.defaultDisplay.getMetrics(displaymetrics)
    return displaymetrics.heightPixels
}


fun getCurrentDateTime(pattern: String): String {
    val mcurrentTime = Calendar.getInstance()
    val df = SimpleDateFormat(pattern)
    val currentDate = df.format(mcurrentTime.time)

    println("currentDate = $currentDate")
    return currentDate
}

fun isNumberContain(string: String): Boolean {
    val p = Pattern.compile("(([0-9]))")
    val m = p.matcher(string)
    return m.find()
}

fun getWeeksBetween(a: Date, b: Date): Int {
    var a = a
    var b = b

    if (b.before(a)) {
        return -getWeeksBetween(b, a)
    }
    a = resetTime(a)
    b = resetTime(b)

    val cal = GregorianCalendar()
    cal.time = a
    var weeks = 0
    while (cal.time.before(b)) {
        // add another week
        cal.add(Calendar.WEEK_OF_YEAR, 1)
        weeks++
    }
    return weeks
}

fun resetTime(d: Date): Date {
    val cal = GregorianCalendar()
    cal.time = d
    cal.set(Calendar.HOUR_OF_DAY, 0)
    cal.set(Calendar.MINUTE, 0)
    cal.set(Calendar.SECOND, 0)
    cal.set(Calendar.MILLISECOND, 0)
    return cal.time
}

fun getIpAddress(context: Context): String {
    val wm = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
    val ip = Formatter.formatIpAddress(wm.connectionInfo.ipAddress)
    return ip
}

fun getKeyHashes(context: Context) {
    try {
        val info = context.packageManager.getPackageInfo(
            context.packageName,
            PackageManager.GET_SIGNATURES)
        for (signature in info.signatures) {
            val md = MessageDigest.getInstance("SHA")
            md.update(signature.toByteArray())
            println("keyhash=----" + Base64.encodeToString(md.digest(), Base64.NO_WRAP))
        }
    } catch (e: PackageManager.NameNotFoundException) {
        println(e.message)
    } catch (e: NoSuchAlgorithmException) {
        println(e.message)
    }
}


fun getFullAddress(context: Context, location: Location) :String {

    var fullAddress=""

    val addresses: List<Address>
    val geocoder = Geocoder(context, Locale.getDefault())

    addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1) // Here 1 represent max location result to returned, by documents it recommended 1 to 5
    val address = addresses[0].getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
    val city = addresses[0].locality
    val state = addresses[0].adminArea
    val country = addresses[0].countryName
    val postalCode = addresses[0].postalCode
    val knownName = addresses[0].featureName

    fullAddress = address

    return fullAddress
}

fun removeStatusBar(activity: Activity){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        activity.window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        //activity.window.s
    }
}




