package fr.atraore.edl.ui


import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*
import java.util.Locale.FRANCE

//Gestion du clavier
fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}


//Dates
/**
 * Pattern: yyyy-MM-dd HH:mm:ss
 */
fun Date.formatToServerDateTimeDefaults(): String{
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", FRANCE)
    return sdf.format(this)
}

fun Date.formatToTruncatedDateTime(): String{
    val sdf = SimpleDateFormat("yyyyMMddHHmmss", FRANCE)
    return sdf.format(this)
}

/**
 * Pattern: yyyy-MM-dd
 */
fun Date.formatToServerDateDefaults(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd", FRANCE)
    return sdf.format(this)
}

/**
 * Pattern: HH:mm:ss
 */
fun Date.formatToServerTimeDefaults(): String {
    val sdf = SimpleDateFormat("HH:mm:ss", FRANCE)
    return sdf.format(this)
}

/**
 * Pattern: dd/MM/yyyy HH:mm:ss
 */
fun Date.formatToViewDateTimeDefaults(): String{
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", FRANCE)
    return sdf.format(this)
}

/**
 * Pattern: dd/MM/yyyy
 */
fun Date.formatToViewDateDefaults(): String{
    val sdf = SimpleDateFormat("dd/MM/yyyy", FRANCE)
    return sdf.format(this)
}

/**
 * Pattern: HH:mm:ss
 */
fun Date.formatToViewTimeDefaults(): String{
    val sdf = SimpleDateFormat("HH:mm:ss", FRANCE)
    return sdf.format(this)
}