package com.example.fyp

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp.Entity.Shop
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class ShopAdapter(private val shopList: ArrayList<Shop>) : RecyclerView.Adapter<ShopAdapter.ShopViewHolder>() {
    var onItemClick : ((Shop)->Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopViewHolder {
        val itemHolder = LayoutInflater.from(parent.context).inflate(R.layout.layout_shopitem, parent, false)
        return ShopViewHolder(itemHolder)
    }

    override fun getItemCount(): Int {
        return shopList.size
    }

    override fun onBindViewHolder(holder: ShopViewHolder, position: Int) {
        var shop: Shop = shopList[position]

        var imgName = shop.image.toString()
        val storageRef = FirebaseStorage.getInstance().reference.child("shopimg/$imgName")
        val localfile = File.createTempFile("tempImage", "png")

        storageRef.getFile(localfile).addOnSuccessListener {

            val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
            holder.imgItem.setImageBitmap(bitmap)
        }

        //holder.tvShopName.text = shop.name

        holder.tvItemName.text = shop.name

        /*holder.itemView.findViewById<Button>(R.id.btnEdit).setOnClickListener {
            onItemClick?.invoke(shop)
        }*/
    }

    public class ShopViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var imgItem = itemView.findViewById<ImageView>(R.id.imgItem)
        var tvItemName = itemView.findViewById<TextView>(R.id.tvItemName)
        //var tvShopName = itemView.findViewById<TextView>(R.id.tvShopName)
    }
}