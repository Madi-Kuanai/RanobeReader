package com.mk.ranobereader.models

import com.mk.ranobereader.Const

class RanobeModel(name: String, imageLink: String, linkToRanobe: String, author: String, description: String,
                  numberOfChapters: String, numberOfPage: String, rating: String,
                  ratingOfTranslate: String, likes: String, stateOfTranslation: String,
                  genres: Map<String, String>, tags: Map<String, String>) {
    var name: String
    var imageLink: String
    var linkToRanobe: String
    var author: String
    var description: String
    var numberOfChapters: String
    var numberOfPage: String
    var rating: String
    var ratingOfTranslate: String
    var likes: Int
    var stateOfTranslation: String
    var genres: Map<String, String>
    var tags: Map<String, String>
    override fun toString(): String {
        return "RanobeModel{" +
                "name='" + name + '\'' +
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

    init {
        this.imageLink = Const.baseURI + imageLink
        this.name = name.split("/").toTypedArray().get(name.split("/").toTypedArray().size - 1)
        this.linkToRanobe = linkToRanobe
        this.author = author
        this.description = description
        this.numberOfChapters = numberOfChapters
        this.numberOfPage = numberOfPage
        this.rating = rating
        this.ratingOfTranslate = ratingOfTranslate
        this.likes = likes.toInt()
        this.stateOfTranslation = stateOfTranslation
        this.genres = genres
        this.tags = tags
    }
}