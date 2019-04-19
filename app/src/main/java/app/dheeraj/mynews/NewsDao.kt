package app.dheeraj.mynews

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

@Dao
interface NewsDao {

     @Insert
      fun insertNews(news :News)

    @Insert
    fun insertMutipleNews(news: List<News>)

    @Query("SELECT * FROM news where type=:typeofnews")
    fun getNews(typeofnews : String): List<News>


    @Query("DELETE FROM news WHERE type = :typeofnews")
    fun delNews(typeofnews: String)
}