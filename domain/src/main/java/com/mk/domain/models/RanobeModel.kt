package com.mk.domain.models

import java.io.Serializable


open class RanobeModel(
    title: String, imageLink: String,
    linkToRanobe: String, var author: String, description: String,
    var numberOfChapters: String, var numberOfPage: String,
    var rating: String,
    var ratingOfTranslate: String, likes: String, var stateOfTranslation: String,
    var genres: Map<String, String>, var tags: Map<String, String>
) : IRanobe(
    title = title.split("/").toTypedArray()[title.split("/").toTypedArray().size - 1],
    description,
    imageLink = "https://tl.rulate.ru$imageLink",
    linkToRanobe = "https://tl.rulate.ru$linkToRanobe"
), Serializable {
    var likes: Int = likes.toInt()
    override fun toString(): String {
        return "RanobeModel{" +
                "name='" + title + '\'' +
                ", imageLink='" + imageLink + '\'' +
                ", linkToRanobe='" + linkToRanobe + '\'' +
                ", author='" + author + '\'' +
                ", description='" + description + '\'' +
                ", numberOfChapters='" + numberOfChapters + '\'' +
                ", numberOfPage='" + numberOfPage + '\'' +
                ", Rating='" + rating + '\'' +
                ", RatingOfTranslate='" + ratingOfTranslate + '\'' +
                ", likes=" + likes +
                ", stateOfTranslation='" + stateOfTranslation + '\'' +
                ", genres=" + genres +
                ", tags=" + tags +
                '}'
    }

}