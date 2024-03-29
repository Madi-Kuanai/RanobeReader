package com.mk.data.repositories.ranobes

import android.util.Log
import com.mk.domain.Const
import com.mk.domain.Const.TAG
import com.mk.domain.models.UpdatedRanobeModel
import com.mk.domain.useCase.IUpdateRanobeRepository
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class UpdateRanobeRepositoryImp : IUpdateRanobeRepository {
    override suspend fun fetchRanobeList(
        url: String,
    ): MutableList<UpdatedRanobeModel> {
        val doc: Document? = Jsoup.connect(url).get()
        val ulElement = doc?.select("ul.imged")?.first()
        val liElements = ulElement?.select("li")
        val backgroundImageRegex = Regex("background-image:url\\('(.+)'\\);")
        val ranobeModel: MutableList<UpdatedRanobeModel> = ArrayList()
        if (liElements != null) {
            for (liElement in liElements) {
                val linkToRanobe = liElement.select("a").first()?.attr("href").toString()
                var imageLink: String
                val div =
                    liElement.select("div").select("div").first()?.select("div")?.get(1).toString()
                val backgroundImageMatch = backgroundImageRegex.find(div)
                imageLink = if (backgroundImageMatch != null) {
                    backgroundImageMatch.groupValues[1]
                } else {
                    ""
                }
                val description = liElement.select("p.title").first()?.select("a")?.first()
                    ?.attr("data-original-title").toString()
                val title =
                    liElement.select("p.title").first()?.select("a")?.first()?.text().toString()
                val titleOfLastUpdate =
                    liElement.select("span").first()?.select("a")?.text().toString()
                val linkToLastUpdate =
                    liElement.select("span").first()?.select("a")?.attr("href").toString()
                val dateOfUpdate =
                    liElement.select("p")[1].select("span").first()?.text().toString()
                ranobeModel.add(
                    UpdatedRanobeModel(
                        title = title,
                        description = description,
                        imageLink = Const.BASE_URI + imageLink.split("-").first() + ".jpg",
                        linkToRanobe = Const.BASE_URI + linkToRanobe,
                        titleOfLastUpdate = titleOfLastUpdate,
                        linkToLastUpdate = linkToLastUpdate,
                        dateOfUpdate = dateOfUpdate
                    )
                )
            }
        }
        return ranobeModel
    }
}