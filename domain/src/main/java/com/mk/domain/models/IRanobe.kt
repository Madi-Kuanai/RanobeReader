package com.mk.domain.models

import java.io.Serializable


open class IRanobe(
    var title: String,
    var description: String,
    var imageLink: String,
    var linkToRanobe: String
) : Serializable