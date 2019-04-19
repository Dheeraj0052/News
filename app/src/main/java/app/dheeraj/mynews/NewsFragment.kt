package app.dheeraj.mynews

import android.arch.persistence.room.Room
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_layout.*
import okhttp3.*
import java.io.IOException

class NewsFragment : Fragment()
{
     private val newsdatabase by lazy {
            Room.databaseBuilder(requireContext(),NewsDatabase::class.java, "news.db")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build()
     }
    private val newsdao by lazy { newsdatabase.getNewsDoa() }
    var newslist = arrayListOf<News>()
     companion object {
        fun Newinstance(Endpiont :String) : NewsFragment
        {   val newfragment = NewsFragment()

            val arguments =Bundle()
            arguments.putString("Endpoint",Endpiont)
            newfragment.arguments=arguments
            return newfragment
        }
     }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return  inflater.inflate(R.layout.fragment_layout,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val endpoint = arguments?.getString("Endpoint") ?:""
        val url =
            "https://newsapi.org/v2/top-headlines?country=us&category=$endpoint&apiKey=201baf6282664af088ec46f6b95f6ecf"



        rcNews.layoutManager= LinearLayoutManager(requireContext())
        val newsadapter = NewsAdapter(newslist)
        rcNews.adapter=newsadapter

      if (newsdao.getNews(endpoint).isNullOrEmpty()) {
          val onetimequest = OneTimeWorkRequestBuilder<NewsFetchWorker>()
              .setInputData(Data.Builder().putString("Endpoint",endpoint).build())
              .build()
          WorkManager.getInstance().enqueue(onetimequest)

          WorkManager.getInstance().getWorkInfoByIdLiveData(onetimequest.id).observeForever()
          {
                if (it?.state==WorkInfo.State.SUCCEEDED)
                {  newslist.clear()
                    newslist.addAll(newsdao.getNews(endpoint))
                    newsadapter.notifyDataSetChanged()

                }
          }
      }
        else
      {
          newslist.clear()
          newslist.addAll(newsdao.getNews(endpoint))
          activity?.runOnUiThread {newsadapter.notifyDataSetChanged()  }

      }
    }
}