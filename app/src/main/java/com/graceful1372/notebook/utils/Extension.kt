package com.graceful1372.notebook.utils

import android.view.View


//Change number english to persian
fun String.toPersian():String{
    var newsString = toString()
    newsString = newsString.replace("1", "۱")
    newsString = newsString.replace("2", "۲")
    newsString = newsString.replace("3", "۳")
    newsString = newsString.replace("4", "۴")
    newsString = newsString.replace("5", "۵")
    newsString = newsString.replace("6", "۶")
    newsString = newsString.replace("7", "۷")
    newsString = newsString.replace("8", "۸")
    newsString = newsString.replace("9", "۹")
    newsString = newsString.replace("0", "۰")
    return newsString
}

fun View.isVisible(b:Boolean){
    if (b){
        this.visibility = View.VISIBLE
    }else{
        this.visibility = View.INVISIBLE
    }

}