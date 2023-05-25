package com.mk.domain.models

import com.mk.domain.Const.TAG

class FullRanobeModel(
    title: String?,
    imageLink: String?,
    linkToRanobe: String?,
    author: String?,
    description: String?,
    numberOfChapters: String?,
    rating: List<Any>,
    ratingOfTranslate: List<Any>,
    likes: String?,
    stateOfTranslation: String?,
    genres: Map<String, String?>,
    tags: Map<String, String?>,
    var statusOfTitle: String,
    var chapters: Map<String, String>,
    var yearOfAnons: String
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
    override fun toString(): String {

        return "${super.toString()} FullRanobeModel(statusOfTitle='$statusOfTitle', chapters=$chapters, yearOfAnons='$yearOfAnons')"
    }
}