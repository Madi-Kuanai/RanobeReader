package com.mk.domain.models

data class UpdatedRanobeModel(
    val title: String,
    val description: String,
    var imageLink: String,
    var linkToRanobe: String,
    val titleOfLastUpdate: String,
    val linkToLastUpdate: String,
    val dateOfUpdate: String
) {
    init {
        imageLink = "https://tl.rulate.ru$imageLink"
        linkToRanobe = "https://tl.rulate.ru$linkToRanobe"
    }

    override fun toString(): String {
        return "UpdatedRanobeModel(title='$title', description='$description', imageLink='$imageLink', linkToRanobe='$linkToRanobe', dateOfUpdate='$titleOfLastUpdate')"
    }

}