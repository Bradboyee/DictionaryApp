package com.thepparat.dictionaryapp.feature_dictionary.domain.repository

import com.thepparat.dictionaryapp.core.util.Resource
import com.thepparat.dictionaryapp.feature_dictionary.domain.model.WordInfo
import kotlinx.coroutines.flow.Flow

interface WordInfoRepository {
    fun getWordInfo(word: String): Flow<Resource<List<WordInfo>>>
}