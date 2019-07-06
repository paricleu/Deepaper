package com.hackathon.deepaper

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.transition.TransitionManager
import com.google.android.material.snackbar.Snackbar

fun <T : AppCompatActivity> Context.start(cls: Class<T>) {
    this.start(cls, null)
}

fun <T : AppCompatActivity> Context.start(cls: Class<T>, args: Bundle?) {
    val intent = Intent(this, cls)
    if (args != null)
        intent.putExtras(args)
    this.startActivity(intent)
}

fun <T : AppCompatActivity> Fragment.start(cls: Class<T>) {
    this.activity?.start(cls)
}

fun <T : AppCompatActivity> Fragment.start(cls: Class<T>, args: Bundle?) {
    this.activity?.start(cls, args)
}

fun <T : AppCompatActivity> Fragment.startForResult(cls: Class<T>, requestCode: Int) {
    this.startActivityForResult(Intent(this.activity, cls), requestCode)
}

fun <T : AppCompatActivity> AppCompatActivity.startForResult(cls: Class<T>, requestCode: Int) {
    this.startActivityForResult(Intent(this, cls), requestCode)
}

fun <T : AppCompatActivity> AppCompatActivity.startForResult(
    cls: Class<T>,
    requestCode: Int,
    args: Bundle?
) {
    val intent = Intent(this, cls)
    if (args != null)
        intent.putExtras(args)
    this.startActivityForResult(intent, requestCode, args)
}

fun ViewGroup.inflate(layoutRes: Int): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}

fun View.show() {
    if (visibility == View.GONE || visibility == View.INVISIBLE) {
        visibility = View.VISIBLE
    }
}

fun View.hide() {
    if (visibility == View.VISIBLE) {
        visibility = View.GONE
    }
}

fun View.invisible() {
    if (visibility == View.VISIBLE) {
        visibility = View.INVISIBLE
    }
}

fun View.disable() {
    isEnabled = false
    isClickable = false
    isFocusable = false
}

fun View.enable() {
    isEnabled = true
    isClickable = true
    isFocusable = true
}

fun TextView.setTextOrHide(s: String) {
    if (s.isBlank()) {
        hide()
    } else {
        text = s
        show()
    }
}

fun Context.toast(stringRes: Int, showLong: Boolean = false) {
    toast(getString(stringRes), showLong)
}

fun Context.toast(string: String, showLong: Boolean = false) {
    Toast.makeText(this, string, if (showLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT).show()
}

fun FragmentActivity.showToast(text: String) {
    runOnUiThread { Toast.makeText(this, text, Toast.LENGTH_SHORT).show() }
}

fun View.snackbar(msg: String) {
    Snackbar.make(this, Html.fromHtml("<font color=\"#000\">$msg</font>"), Snackbar.LENGTH_SHORT)
        .show()
}

fun AppCompatActivity.hideKeyboard() {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
}

fun Fragment.hideKeyboard() {
    val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
    activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
}

fun Context.color(id: Int): Int {
    return ContextCompat.getColor(this, id)
}

fun AppCompatActivity.defaultBackPressHandling() {
    if (supportFragmentManager.backStackEntryCount > 1) {
        supportFragmentManager.popBackStack()
    } else {
        finish()
    }
}

fun View.setBackground(res: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        this.background = ContextCompat.getDrawable(context, res)
    } else {
        this.setBackgroundDrawable(ContextCompat.getDrawable(context, res))
    }
}

fun TextView.setTextColorFromRes(res: Int) {
    setTextColor(ContextCompat.getColor(context, res))
}

fun TextView.hideIfBlank() {
    if (text.isBlank()) {
        slideOut()
    }
}

fun View.slideIn() {
    TransitionManager.beginDelayedTransition(parent as ViewGroup)
    visibility = View.VISIBLE
}

fun View.slideOut() {
    TransitionManager.beginDelayedTransition(parent as ViewGroup)
    visibility = View.GONE
}


// new

fun AppCompatActivity.popBackStack() {
    //Util.hideKeyboard(this)
    supportFragmentManager.popBackStack()
}

fun AppCompatActivity.popBackStackInclusive() {
    //Util.hideKeyboard(this)
    if (supportFragmentManager.backStackEntryCount > 0)
        supportFragmentManager.popBackStack(
            supportFragmentManager.getBackStackEntryAt(0).id,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
}

inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
    beginTransaction().func().commit()
}


fun AppCompatActivity.addFragment(fragment: Fragment, frameId: Int) {
    supportFragmentManager.inTransaction { add(frameId, fragment, fragment.javaClass.simpleName) }
}

fun AppCompatActivity.replaceFragment(
    fragment: Fragment,
    frameId: Int,
    addToStack: Boolean = false,
    clearBackStack: Boolean = false
) {
    supportFragmentManager.inTransaction {

        if (clearBackStack && supportFragmentManager.backStackEntryCount > 0) {
            val first = supportFragmentManager.getBackStackEntryAt(0)
            supportFragmentManager.popBackStack(first.id, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }

        if (addToStack) replace(frameId, fragment, fragment.javaClass.simpleName)
            .addToBackStack(fragment.javaClass.simpleName)
        else
            replace(frameId, fragment, fragment.javaClass.simpleName)
    }
}

fun AppCompatActivity.addFragment(fragment: Fragment, frameId: Int, addToStack: Boolean) {
    supportFragmentManager.inTransaction {
        if (addToStack) add(frameId, fragment, fragment.javaClass.simpleName)
            .addToBackStack(fragment.javaClass.simpleName)
        else add(frameId, fragment)
    }
}


fun AppCompatActivity.getCurrentFragment(): Fragment? {
    val fragmentManager = supportFragmentManager
    var fragmentTag: String? = ""

    if (fragmentManager.backStackEntryCount > 0)
        fragmentTag =
            fragmentManager.getBackStackEntryAt(fragmentManager.backStackEntryCount - 1).name

    return fragmentManager.findFragmentByTag(fragmentTag)
}

fun Bitmap.rotate(degree: Int): Bitmap {
    // Initialize a new matrix
    val matrix = Matrix()

    // Rotate the bitmap
    matrix.postRotate(degree.toFloat())

    // Resize the bitmap
    val scaledBitmap = Bitmap.createScaledBitmap(
        this,
        width,
        height,
        true
    )

    // Create and return the rotated bitmap
    return Bitmap.createBitmap(
        scaledBitmap,
        0,
        0,
        scaledBitmap.width,
        scaledBitmap.height,
        matrix,
        true
    )
}

inline fun <reified T : Any> Activity.launchActivity(
    requestCode: Int = -1,
    options: Bundle? = null,
    noinline init: Intent.() -> Unit = {}
) {
    val intent = newIntent<T>(this)
    intent.init()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        startActivityForResult(intent, requestCode, options)
    } else {
        startActivityForResult(intent, requestCode)
    }
}

inline fun <reified T : Any> Context.launchActivity(
    options: Bundle? = null,
    noinline init: Intent.() -> Unit = {}
) {
    val intent = newIntent<T>(this)
    intent.init()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        startActivity(intent, options)
    } else {
        startActivity(intent)
    }
}

inline fun <reified T : Any> newIntent(context: Context): Intent =
    Intent(context, T::class.java)