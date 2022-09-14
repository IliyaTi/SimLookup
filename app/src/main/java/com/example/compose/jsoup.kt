package com.example.compose

import android.util.Log
import kotlinx.coroutines.coroutineScope
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.lang.IllegalStateException


suspend fun fetchExistence(prefix: String = "", input: String): ArrayList<NumberItem> {
    val phoneNo: StringBuilder = StringBuilder()

//    if (input.length != 7){
//        throw IllegalStateException("length does not match!")
//    }

    for (index in input) {
        when (index.lowercaseChar()) {
            'a', 'b', 'c' -> phoneNo.append("2")
            'd', 'e', 'f' -> phoneNo.append("3")
            'g', 'h', 'i' -> phoneNo.append("4")
            'j', 'k', 'l' -> phoneNo.append("5")
            'm', 'n', 'o' -> phoneNo.append("6")
            'p', 'q', 'r', 's' -> phoneNo.append("7")
            't', 'u', 'v' -> phoneNo.append("8")
            'w', 'x', 'y', 'z' -> phoneNo.append("9")
        }
    }
    /******************************* TILL HERE WORKS PERFECTLY **********************************/

    // URL BUILDER GOES HERE >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    val url = urlBuilder(prefix.toString(), phoneNo.toString())

    return coroutineScope {
        val resultList = arrayListOf<NumberItem>()

        val doc: Document = Jsoup.connect(url.toString()).get()
        val e: Element? = doc.select("table.empty-table").first()

        if (e != null) throw IllegalStateException("Not found")

        val items: Elements = doc.select("tr.odTr, tr.evendTr")

        for ((index, currentItem) in items.withIndex()) {
            val number = currentItem.getElementsByClass("t-link").first()!!.text().replace("\\s".toRegex(), "")

            val item = NumberItem(
                id = index,
                phoneNo = number,
                price = currentItem.select("span").first()!!.text(),
            )

            resultList.add(item)
        }
        return@coroutineScope resultList
    }

}

fun urlBuilder(prefix: String, phoneNo: String): String {
    val url = java.lang.StringBuilder("https://www.rond.ir/SearchSim?SimOrderBy=&ItemPerPage=200&")
    val params = listOf("PreCode=", "Code=", "FirstDigit=", "SecondDigit=", "ThirdDigit=", "FourthDigit=", "FifthDigit=", "SixthDigit=", "SimOperatorE=1&StateId=&SimType=&CityId=&SimStatus=&SimCoPrice=&SimQuality=&PriceFrom=&SimQualityTypeId=&PriceTo=&VendorType=")
    url.append(params[0] + prefix).append("&")
    for (i in 1..phoneNo.length){
        Log.e("FUCK", "index: $i, param: ${params[i]}, char: ${phoneNo[i - 1]}")
        url.append(params[i] + phoneNo[i-1]).append("&")
    }
    url.append(params[params.size - 1])
    Log.e("FUCK", url.toString())
    return url.toString()
}



//    url.append("SixthDigit=").append(phoneNo[6]).append("&")
//    url.append("FifthDigit=").append(phoneNo[5]).append("&")
//    url.append("ForthDigit=").append(phoneNo[4]).append("&")
//    url.append("ThirdDigit=").append(phoneNo[3]).append("&")
//    url.append("SecondDigit=").append(phoneNo[2]).append("&")
//    url.append("FirstDigit=").append(phoneNo[1]).append("&")
//    url.append("Code=").append(phoneNo[0]).append("&")
//    url.append("PreCode=").append(prefix).append("&")
//    url.append("SimOperatorE=1&StateId=&SimType=&CityId=&SimStatus=&SimCoPrice=&SimQuality=&PriceFrom=&SimQualityTypeId=&PriceTo=&VendorType=")
