package com.mk.domain.models

import java.io.Serializable

class UpdatedRanobeModel(
    title: String,
    description: String,
    imageLink: String,
    linkToRanobe: String,
    val titleOfLastUpdate: String,
    val linkToLastUpdate: String,
    val dateOfUpdate: String
) : IRanobe(
    title = title,
    description = description,
    imageLink = imageLink,
    linkToRanobe = linkToRanobe
), Serializable