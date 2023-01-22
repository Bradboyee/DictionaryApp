package com.thepparat.dictionaryapp.feature_dictionary.domain.model

data class Meaning(
    val definitions: List<Definition>,
    val partOfSpeech: String
)
