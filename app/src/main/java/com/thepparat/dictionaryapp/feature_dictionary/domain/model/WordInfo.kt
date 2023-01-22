package com.thepparat.dictionaryapp.feature_dictionary.domain.model

data class WordInfo(
    val meanings: List<Meaning>,
    val origin: String?,
    val phonetic: String?,
    val word: String,
    val sourceUrls: List<String>
)
