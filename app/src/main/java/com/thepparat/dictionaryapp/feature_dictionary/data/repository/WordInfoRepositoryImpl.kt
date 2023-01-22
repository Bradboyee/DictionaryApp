package com.thepparat.dictionaryapp.feature_dictionary.data.repository

import android.util.Log
import com.thepparat.dictionaryapp.core.util.Resource
import com.thepparat.dictionaryapp.feature_dictionary.data.local.WordInfoDao
import com.thepparat.dictionaryapp.feature_dictionary.data.remote.dto.DictionaryApi
import com.thepparat.dictionaryapp.feature_dictionary.domain.model.WordInfo
import com.thepparat.dictionaryapp.feature_dictionary.domain.repository.WordInfoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class WordInfoRepositoryImpl(
    private val api: DictionaryApi,
    private val dao: WordInfoDao
) : WordInfoRepository {

    override fun getWordInfo(word: String): Flow<Resource<List<WordInfo>>> = flow {
        emit(Resource.Loading())

        val wordInfos = dao.getWordInfos(word).map { it.toWordInfo() }
        emit(Resource.Loading(data = wordInfos))

        try {
            val remoteWordInfos = api.getWordInfo(word)
            Log.i("remoteWordInfos",remoteWordInfos.toString())
            dao.deleteWordInfos(remoteWordInfos.map { it.word })
            dao.insertWordInfos(remoteWordInfos.map { it.toWordInfoEntity() })
        } catch (e: HttpException) {
            emit(
                Resource.Error(
                    message = "Oops, something went wrong!",
                    data = wordInfos
                )
            )
        } catch (e: IOException) {
            emit(
                Resource.Error(
                    message = "Couldn't reach server, check your internet connection.",
                    data = wordInfos
                )
            )
        }

        val newWordInfos = dao.getWordInfos(word).map { it.toWordInfo() }
        emit(Resource.Success(newWordInfos))
    }
}
