package com.mk.data.repositories.ranobes

import android.util.Log
import com.mk.domain.Const
import com.mk.domain.Const.TAG
import com.mk.domain.models.RanobeModel
import com.mk.domain.useCase.IRanobeFromListRepository
import org.json.JSONObject
import org.jsoup.Jsoup
import org.jsoup.nodes.Document


class RanobeFromListRepositoryImpl : IRanobeFromListRepository {
    override suspend fun fetchRanobeList(
        url: String,
        method: String,

        payload: JSONObject?
    ): MutableList<RanobeModel> {
        var doc: Document? = null
        // URL of the webpage to parse
        try {
            if (method == Const.GET) {
                // Connect to the URL and parse the HTML
                doc = Jsoup.connect(url).get()
            } else if (method == Const.POST) {
                // Parse the response with Jsoup
                val header: MutableMap<String, String> = HashMap()
                val it = payload?.keys()
                if (it != null) {
                    while (it.hasNext()) {
                        val key = it.next()
                        header[key] = payload.getString(key)
                    }
                }

                doc = Jsoup.connect(url).data(header).post()
            }

            val ulElement = doc!!.select("ul.search-results").first()

            val liElements = ulElement!!.select("li")

            // Create a list to store the parsed elements
            val ranobeList: MutableList<RanobeModel> = ArrayList()

            // Add the text content of each <li> element to the list
            for (liElement in liElements) {
                val imageLink = liElement.select(".th").first()!!.select("img").attr("src")
                val name =
                    liElement.getElementsByClass("book-tooltip").first()!!.getElementsByTag("a")
                        .text()
                val linkToRanobe =
                    liElement.getElementsByClass("book-tooltip").first()!!.getElementsByTag("a")
                        .attr("href")
                val author = liElement.getElementsByClass("meta").first()!!
                    .getElementsByClass("user user-inactive").first()!!.text()
                val numberOfChapter = liElement.getElementsByAttributeValue(
                    "title",
                    "Кол-во глав всего / Кол-во платных глав"
                ).text()
                val rating =
                    liElement.getElementsByAttributeValue("title", "Рейтинг").text().split(" / ")
                val ratingOfTranslate =
                    liElement.getElementsByAttributeValue("title", "Качество перевода").text()
                        .split(" / ")
                val like: String = liElement.getElementsByAttributeValue("title", "Лайки").text()
                val description = liElement.getElementsByClass("tooltip_templates").first()!!.text()
                val stateOfTranslate = liElement.getElementsByTag("em")[1].text()
                val genres: MutableMap<String, String> = HashMap()
                try {
                    for (aElement in liElement.getElementsByClass("meta").first()!!
                        .getElementsByTag("p")[3].getElementsByTag("a")) {
                        genres[aElement.text()] = aElement.attr("href")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                val tags: MutableMap<String, String> = HashMap()
                try {
                    for (aElement in liElement.getElementsByClass("meta").first()!!
                        .getElementsByTag("p")[4].getElementsByTag("a")) {
                        tags.put(aElement.text(), aElement.attr("href"))
                    }
                } catch (e: Exception) {
//                e.printStackTrace()
                }

                val model = RanobeModel(
                    name,
                    Const.BASE_URI + imageLink,
                    Const.BASE_URI + linkToRanobe,
                    author,
                    description,
                    numberOfChapter,
                    rating,
                    ratingOfTranslate,
                    like.replace(" ", "").toInt().toString(),
                    stateOfTranslate,
                    genres,
                    tags
                )
                ranobeList.add(model)
            }
            return ranobeList
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }
}