package app.dheeraj.mynews

import android.arch.persistence.room.Room

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request

class NewsFetchWorker (context : Context, workerparams : WorkerParameters): Worker(context,workerparams) {
   private val client : OkHttpClient = OkHttpClient()
   private val gson = Gson()
    private val newsdatabase by lazy {

        Room.databaseBuilder(applicationContext,NewsDatabase::class.java,"news.db")
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }
    private val newsdao by lazy {
        newsdatabase.getNewsDoa()
    }

    override fun doWork(): Result {



        val endpoint = inputData.getString("Endpoint") ?:""
        val url =
            "https://newsapi.org/v2/top-headlines?country=us&category=$endpoint&apiKey=201baf6282664af088ec46f6b95f6ecf"


        val request = Request.Builder().url(url).build()
       val response =client.newCall(request).execute()
        val responseString = response.body()?.string()
        //  val requestbody = request.body()?.toString()
        val parsedobject= gson.fromJson(responseString,Newsresponse::class.java)

            parsedobject.articles.forEach {
                it.type =endpoint
            }
        newsdao.insertMutipleNews(parsedobject.articles)
    return Result.success()
    }

}