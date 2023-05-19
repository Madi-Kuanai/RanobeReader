package com.mk.domain.models

import java.io.Serializable


open class RanobeModel(
    title: String?, imageLink: String?,
    linkToRanobe: String?, var author: String?, description: String?,
    var numberOfChapters: String?,
    var rating: List<Any>,
    var ratingOfTranslate: String?, likes: String?, var stateOfTranslation: String?,
    var genres: Map<String, String?>, var tags: Map<String, String?>
) : IRanobe(
    title = title?.split("/")?.toTypedArray()?.get(title.split("/").toTypedArray().size - 1),
    description,
    imageLink = "https://tl.rulate.ru$imageLink",
    linkToRanobe = "https://tl.rulate.ru$linkToRanobe"
), Serializable {
    var likes: Int = likes?.toInt() ?: 0
    override fun toString(): String {
        return "RanobeModel{" +
                "name='" + title + '\'' +
                ", imageLink='" + imageLink + '\'' +
                ", linkToRanobe='" + linkToRanobe + '\'' +
                ", author='" + author + '\'' +
                ", description='" + description + '\'' +
                ", numberOfChapters='" + numberOfChapters + '\'' +
                ", Rating='" + rating + '\'' +
                ", RatingOfTranslate='" + ratingOfTranslate + '\'' +
                ", likes=" + likes +
                ", stateOfTranslation='" + stateOfTranslation + '\'' +
                ", genres=" + genres +
                ", tags=" + tags +
                '}'
    }
}