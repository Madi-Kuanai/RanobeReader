package com.mk.data.repositories.ranobes

import android.util.Log
import com.mk.domain.Const
import com.mk.domain.Const.TAG
import com.mk.domain.useCase.IRanobePageTextRepository
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

class RanobePageTextRepositoryImpl : IRanobePageTextRepository {
    override suspend fun fetchRanobePageText(url: String): MutableList<String>? {
        try {
            val doc = Jsoup.connect(url).get().body()
            val text = ArrayList<String>()
            doc.select(".content-text").forEach { elem ->
                elem.select("div").forEach {
                    text.add("${Const.IMG_TAG}${it.select("img").attr("src")}")
                }
                elem.select("p").forEach { element: Element? ->
                    element?.text()?.let { text.add(it) }
                }
            }
            return text
        } catch (e: Exception) {
            Log.d(TAG, "fetchRanobePageText: $e")
            return null
        }
    }
}