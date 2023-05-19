package com.mk.domain.models

class FullRanobeModel(
    title: String?,
    imageLink: String?,
    linkToRanobe: String?,
    author: String?,
    description: String?,
    numberOfChapters: String?,
    rating: List<Any>,
    ratingOfTranslate: String?,
    likes: String?,
    stateOfTranslation: String?,
    genres: Map<String, String?>,
    tags: Map<String, String?>,
    var chapters: Map<String, String>
) : RanobeModel(
    title,
    imageLink,
    linkToRanobe,
    author,
    description,
    numberOfChapters,
    rating,
    ratingOfTranslate,
    likes,
    stateOfTranslation,
    genres,
    tags
) {
}