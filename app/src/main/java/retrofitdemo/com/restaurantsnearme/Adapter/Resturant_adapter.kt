package retrofitdemo.com.restaurantsnearme.Adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.squareup.picasso.Picasso
import retrofitdemo.com.restaurantsnearme.Activity.MainActivity
import retrofitdemo.com.restaurantsnearme.Activity.MapsActivity
import retrofitdemo.com.restaurantsnearme.R
import retrofitdemo.com.restaurantsnearme.R.id.distance
import retrofitdemo.com.restaurantsnearme.Responsc.PlacesDetails_Modal


import java.util.ArrayList


/**
 * Created by Kiran on 05/04/19.
 */

class Resturant_adapter(context: Context, storeModels: ArrayList<PlacesDetails_Modal>, current_address: String, current_lat: Double, current_lng: Double) : RecyclerView.Adapter<Resturant_adapter.MyViewHolder>() {
    override fun getItemCount(): Int {
        return storeModels.size + 1
    }

    private val storeModels: ArrayList<PlacesDetails_Modal>
    private val context: Context
    private val current_address: String
    private var currentlat: Double
    private var currentlng: Double

    init {
        this.context = context
        this.storeModels = storeModels
        this.current_address = current_address
        this.currentlat = current_lat
        this.currentlng = current_lng
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_header, parent, false)
        if (viewType == TYPE_LIST) {
            val itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recyclerview_listitem, parent, false)
            return MyViewHolder(itemView, viewType)
        } else if (viewType == TYPE_HEAD) {
            val itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recyclerview_header, parent, false)
            return MyViewHolder(itemView, viewType)
        }
        itemView.setOnClickListener {
            val intent = Intent(itemView.context, MainActivity::class.java)
            itemView.context.startActivity(intent);
        }
        return MyViewHolder(itemView, viewType)

    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0) {
            return TYPE_HEAD
        } else {
            return TYPE_LIST
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (holder.view_type == TYPE_LIST) {
            holder.res_name.setText(storeModels.get(holder.getAdapterPosition() - 1).name)
            Picasso.with(context).load(storeModels.get(holder.getAdapterPosition() - 1).photourl)
                    .placeholder(R.drawable.placeholder).resize(100, 100).into(holder.res_image)
            holder.res_address.setText(storeModels.get(holder.getAdapterPosition() - 1).address)
            holder.res_distance.setText(storeModels.get(holder.getAdapterPosition() - 1).distance)
            Log.i("details on adapter", (storeModels.get(holder.getAdapterPosition() - 1).name + " " +
                    storeModels.get(holder.getAdapterPosition() - 1).address +
                    " " + storeModels.get(holder.getAdapterPosition() - 1).distance))
            holder.card.setOnClickListener {
                val intent = Intent(context, MapsActivity::class.java)
                intent.putExtra("LAT", storeModels.get(holder.getAdapterPosition() - 1).lat)
                intent.putExtra("LNG", storeModels.get(holder.getAdapterPosition() - 1).lng)
                intent.putExtra("CURRENT_LAT", currentlat)
                intent.putExtra("CURRENT_LNG", currentlng)
                context.startActivity(intent);
            }
        } else if (holder.view_type == TYPE_HEAD) {
            if (current_address == null) {
                holder.current_location.setText("Unable to Detect Current Location")
            } else {
                holder.current_location.setText(current_address)
            }
        }
    }

    inner class MyViewHolder(itemView: View, viewType: Int) : RecyclerView.ViewHolder(itemView) {
        internal lateinit var res_name: TextView
        internal lateinit var res_rating: TextView
        internal lateinit var res_address: TextView
        internal lateinit var res_phone: TextView
        internal lateinit var res_distance: TextView
        internal lateinit var current_location: TextView
        internal lateinit var res_image: ImageView
        internal lateinit var card: LinearLayout
        internal var view_type: Int = 0
        init {
            if (viewType == TYPE_LIST) {
                view_type = 1
                this.res_name = itemView.findViewById(R.id.name) as TextView
                this.res_address = itemView.findViewById(R.id.address) as TextView
                this.res_distance = itemView.findViewById(distance) as TextView
                this.res_image = itemView.findViewById(R.id.res_image) as ImageView
                this.card = itemView.findViewById(R.id.card) as LinearLayout

            } else if (viewType == TYPE_HEAD) {
                view_type = 0
                this.current_location = itemView.findViewById(R.id.location_tv) as TextView
            }
        }
    }
    companion object {
        private val TYPE_HEAD = 0
        private val TYPE_LIST = 1
    }
}
