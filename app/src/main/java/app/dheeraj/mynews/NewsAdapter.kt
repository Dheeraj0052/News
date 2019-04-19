package app.dheeraj.mynews

import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso

//import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_row.view.*

class NewsAdapter(val newslist : List<News>) : RecyclerView.Adapter<NewsAdapter.Newsholder>()
{
    override fun onCreateViewHolder(viewgroup: ViewGroup, position: Int): Newsholder {
    return Newsholder(LayoutInflater.from(viewgroup.context).inflate(R.layout.item_row,viewgroup,false))
    }

    override fun getItemCount(): Int {
      return newslist.size
    }

    override fun onBindViewHolder(newsholder: Newsholder, position: Int) {
        val currentnews = newslist[position]
        with(newsholder.itemView){
            tvTitle.text = currentnews.title
            tvdesc.text= currentnews.description
          Picasso.get().load(currentnews.urlToImage).into(ivurl)


        }

    }
    inner class Newsholder(itemview : View) : RecyclerView.ViewHolder(itemview)

}