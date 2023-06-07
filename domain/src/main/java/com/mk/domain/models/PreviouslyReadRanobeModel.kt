package com.mk.domain.models

import java.io.Serializable

class PreviouslyReadRanobeModel(
    title: String?,
    imageLink: String?,
    linkToRanobe: String?,
    author: String?,
    description: String?,
    numberOfChapters: String?,
    rating: List<Any>,
    ratingOfTranslate: List<Any>,
    likes: Int,
    stateOfTranslation: String?,
    genres: Map<String, String?>,
    tags: Map<String, String?>,
    statusOfTitle: String,
    chapters: Map<String, String>,
    yearOfAnons: String,
    var lastChapter: String,
    var lastPosition: String
) : FullRanobeModel(
    title,
    imageLink?.replaceFirst("https://tl.rulate.ru", ""),
    linkToRanobe,
    author,
    description,
    numberOfChapters,
    rating,
    ratingOfTranslate,
    likes.toString(),
    stateOfTranslation,
    genres,
    tags,
    statusOfTitle,
    chapters,
    yearOfAnons
), Serializable {
    constructor(fullRanobeModel: FullRanobeModel, lastChapter: String, lastPosition: String) : this(
        fullRanobeModel.title,
        fullRanobeModel.imageLink.replaceFirst("https://tl.rulate.ru", ""),
        fullRanobeModel.linkToRanobe,
        fullRanobeModel.author,
        fullRanobeModel.description,
        fullRanobeModel.numberOfChapters,
        fullRanobeModel.rating,
        fullRanobeModel.ratingOfTranslate,
        fullRanobeModel.likes,
        fullRanobeModel.stateOfTranslation,
        fullRanobeModel.genres,
        fullRanobeModel.tags,
        fullRanobeModel.statusOfTitle,
        fullRanobeModel.chapters,
        fullRanobeModel.yearOfAnons, lastChapter, lastPosition
    )
}