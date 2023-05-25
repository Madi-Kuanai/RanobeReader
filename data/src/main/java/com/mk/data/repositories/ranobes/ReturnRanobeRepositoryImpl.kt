package com.mk.data.repositories.ranobes

import android.util.Log
import com.mk.domain.Const.TAG
import com.mk.domain.models.FullRanobeModel
import com.mk.domain.useCase.IReturnRanobeRepository
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import kotlin.math.log

class ReturnRanobeRepositoryImpl : IReturnRanobeRepository {
    override suspend fun fetchRanobe(url: String): FullRanobeModel {
        try {

            val doc = Jsoup.connect(url).get().body()
            val fullNameElement = doc.selectFirst(".span8")?.selectFirst("h1")
            val fullName = fullNameElement?.text().toString()

            val imageLinkElement = doc.select(".slick").first()?.select("img")?.first()
            val imageLink = imageLinkElement?.attr("src").toString()

            val ratingElements = doc.select(".span5").select(".rating-block")
            val rating = if (ratingElements.size >= 1) {
                ratingElements[0].text().split(" / ")
            } else {
                listOf("", "")
            }

            val ratingOfTranslateElement =
                doc.select(".span5").select(".rating-block").getOrNull(1)?.text()
            val ratingOfTranslate = ratingOfTranslateElement ?: ""

            val authorElement = doc.select(".span5").select("p")
                .firstOrNull { element -> element.text().toString().contains("Автор:") }
            val author = authorElement?.select("em")?.text() ?: ""

            val genres: MutableMap<String, String> = HashMap()
            val genresElement = doc.select(".span5").first()?.select("p")
                ?.firstOrNull { element: Element -> element.text().toString().contains("Жанры:") }
            genresElement?.select("em")?.select("a")?.forEach { elem ->
                genres[elem.text().toString()] = elem.attr("href").toString()
            }

            val tags: MutableMap<String, String> = HashMap()
            val tagsElement = doc.select(".span5").select("p")
                .firstOrNull { element: Element -> element.text().toString().contains("Тэги:") }
            tagsElement?.select("em")?.select("a")?.forEach { elem ->
                tags[elem.text().toString()] = elem.attr("href").toString()
            }

            val descriptionElement = doc.selectFirst("div[style=margin: 20px 0 0 0]")
            val description = descriptionElement?.text() ?: ""

            val stateOfTranslationElement =
                doc.select("div.tools")[1].select("dl.info").first()?.select("dd")?.first()
            val stateOfTranslation = stateOfTranslationElement?.text() ?: "Неизвестно"

            val numberOfChaptersElement = doc.select(".span4.sr").select("dl").select("dt")
                .find { element -> element.text().contains("Размер перевода:") }
            val numberOfChapters = numberOfChaptersElement?.let {
                val index = doc.select(".span4.sr").select("dl").select("dt").indexOf(it)
                val infoElements = doc.select("div.tools")[1]?.select("dl.info")?.select("dd")
                if (infoElements != null && index < infoElements.size) {
                    infoElements[index].text()
                } else {
                    // Handle the case when the index is out of bounds or the list is null
                    ""
                }
            } ?: ""


            val likesElement = doc.select("#liker").select("span").first()
            val likes = likesElement?.text() ?: ""

            val chapters: MutableMap<String, String> = HashMap()
            val chaptersElements = doc.select("#Chapters").first()?.select(".chapter_row")
            chaptersElements?.forEach { elem ->
                val name = elem.select(".t").first()?.select("a")?.text().toString()
                val href = elem.select(".t").first()?.select("a")?.attr("href").toString()
                chapters[name] = href
            }
            val statusOfTitleElement = doc.select(".span5").select("p")
                .firstOrNull { element -> element.text().toString().contains("Выпуск:") }
            val statusOfTitle = statusOfTitleElement?.select("em")?.text() ?: "Неизвестно"
            val yearOfAnonsElement = doc.select(".span5").select("p")
                .firstOrNull { element -> element.text().toString().contains("Год выпуска:") }
            val yearOfAnons = yearOfAnonsElement?.select("em")?.text() ?: "Неизвестно"

            return FullRanobeModel(
                fullName,
                imageLink,
                url,
                author,
                description,
                numberOfChapters,
                rating,
                ratingOfTranslate.split(" / "),
                likes,
                stateOfTranslation,
                genres,
                tags,
                statusOfTitle,
                chapters,
                yearOfAnons

            )

        } catch (e: Exception) {
            Log.d(TAG, "Error: " + e.stackTraceToString())
            throw e
        }

    }

}/*
             -title: String,
             -imageLink: String,
             -linkToRanobe: String,
             -author: String,
             -description: String,
             -numberOfChapters: String,
             -rating: String,
             -ratingOfTranslate: String,
             likes: String,
             -stateOfTranslation: String,
             -genres: Map<String, String>,
             -tags: Map<String, String>
*/