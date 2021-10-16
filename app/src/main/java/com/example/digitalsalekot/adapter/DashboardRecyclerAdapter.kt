package com.example.digitalsalekot.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.digitalsalekot.R
import com.example.digitalsalekot.model.CreateTransaction


class DashboardRecyclerAdapter(val context : Context, private val itemList : ArrayList<CreateTransaction>) : RecyclerView.Adapter<DashboardRecyclerAdapter.DashboardViewHolder>(){

    class DashboardViewHolder(view:View) : RecyclerView.ViewHolder(view) {

         val dateAndTime: TextView = view.findViewById(R.id.txtTimeAndDate)
         val productname: TextView = view.findViewById(R.id.txtProductName)
         val quantity: TextView = view.findViewById(R.id.txtUnitAndQuantity)
         val totalPur: TextView = view.findViewById(R.id.txtPurTotal)
         val totSal: TextView = view.findViewById(R.id.txtTotalSale)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_card_detail,parent,false)
        return DashboardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {

        val tran = itemList[position]
        holder.dateAndTime.text = tran.date+" "+tran.time
        holder.productname.text = tran.name
        holder.quantity.text = tran.quantity.toString()
        holder.totalPur.text = "0"
        holder.totSal.text = tran.totalPrice.toString()

    }
}