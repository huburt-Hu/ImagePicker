@file:JvmName("Extensions")

package com.huburt.picker.util

import java.util.*


/**
 * 判断两个Date是否在同一周
 */
fun Date.inSameWeek(other: Date): Boolean {
    val cal1 = Calendar.getInstance()
    val cal2 = Calendar.getInstance()
    cal1.firstDayOfWeek = Calendar.MONDAY//将周一设为一周的第一天，默认周日为一周的第一天
    cal2.firstDayOfWeek = Calendar.MONDAY
    cal1.time = this
    cal2.time = other
    val subYear = cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR)
    if (subYear == 0) {//同一年
        if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
            return true
    } else if ((subYear == 1 && cal2.get(Calendar.MONTH) == 11)
            || (subYear == -1 && cal1.get(Calendar.MONTH) == 11)) { //差一年，且小一年date在12月
        if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
            return true
    }
    return false
}