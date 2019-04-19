package app.dheeraj.mynews

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import androidx.work.Data
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewPager.adapter=Pageradapter(supportFragmentManager)

        val periodicworkforsports = PeriodicWorkRequestBuilder<NewsFetchWorker>(1,TimeUnit.DAYS)
                .setInputData(Data.Builder().putString("Endpoint","sports").build())
                .build()
        val periodicworkfortechnology = PeriodicWorkRequestBuilder<NewsFetchWorker>(1,TimeUnit.DAYS)
                .setInputData(Data.Builder().putString("Endpoint","technology").build())
                .build()
        val periodicworkforentertainment = PeriodicWorkRequestBuilder<NewsFetchWorker>(1,TimeUnit.DAYS)
                .setInputData(Data.Builder().putString("Endpoint","entertainment").build())
                .build()
        WorkManager.getInstance().enqueue(listOf(

                periodicworkforsports,
                periodicworkfortechnology,
                periodicworkforentertainment
        ))
    }




 class Pageradapter (fm :FragmentManager?): FragmentPagerAdapter(fm) {
     override fun getItem(positon: Int): Fragment? {
         when(positon){
             0 ->  {return NewsFragment.Newinstance("sports")

             }
             1 -> {return  NewsFragment.Newinstance("technology")}
             2 -> {return NewsFragment.Newinstance("entertainment")}

         }
            return  null

     }

     override fun getPageTitle(position: Int): CharSequence? {
         when(position)
         {
             0->return "Sports"
             1->return "Technology"
             2->return "Enterainment"

             else->return ""
         }
     }

     override fun getCount() =3
 }
}
