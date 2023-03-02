package com.mk.domain.models


open class RanobeModel(
    title: String, imageLink: String,
    var linkToRanobe: String, var author: String, var description: String,
    var numberOfChapters: String, var numberOfPage: String,
    var rating: String,
    var ratingOfTranslate: String, likes: String, var stateOfTranslation: String,
    var genres: Map<String, String>, var tags: Map<String, String>
) {
    var title: String =
        title.split("/").toTypedArray()[title.split("/").toTypedArray().size - 1]
    var imageLink: String = "https://tl.rulate.ru$imageLink"
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