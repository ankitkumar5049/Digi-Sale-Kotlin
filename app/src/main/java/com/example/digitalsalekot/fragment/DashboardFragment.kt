package com.example.digitalsalekot.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.digitalsalekot.R
import com.example.digitalsalekot.adapter.DashboardRecyclerAdapter
import com.example.digitalsalekot.model.CreateTransaction
import org.json.JSONException
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class DashboardFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    var sal:Int =0
    var pur:Int = 0
    val list = arrayListOf<CreateTransaction>()
    lateinit var recyclerDashboard : RecyclerView
    lateinit var layoutManager : RecyclerView.LayoutManager
    lateinit var recyclerAdapter: DashboardRecyclerAdapter

    lateinit var  saleDash:TextView
    lateinit var sale:TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        val btnPurchase: Button = view.findViewById(R.id.btnPurchase)
        val btnSale: Button = view.findViewById(R.id.btnSale)
        val purchase: TextView = view.findViewById(R.id.txtTotalPurchase)
        sale = view.findViewById(R.id.txtTotalSale)
        saleDash = view.findViewById(R.id.txtSaleDash)
        val purDash :TextView = view.findViewById(R.id.txtPurDash)
        recyclerDashboard = view.findViewById(R.id.recycleDashboard)
        val date : TextView = view.findViewById(R.id.txtDateDash)

        date.text = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())

        layoutManager = LinearLayoutManager(activity)


        apiGetCallTran()
        //apiGetCallTotal()



        btnSale.setOnClickListener {
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            if(transaction!=null) {
                transaction.replace(R.id.frame, SaleFragment())
                transaction.isAddToBackStackAllowed
                transaction.commit()
            }

        }

        btnPurchase.setOnClickListener {
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            if(transaction!=null) {
                transaction.replace(R.id.frame, PurchaseFragment())
                transaction.isAddToBackStackAllowed
                transaction.commit()
            }

        }

        return view;
    }


    private fun apiGetCallTotal(){
        val queue = Volley.newRequestQueue(activity as Context)
        val url = "http://13.235.250.119/v1/book/get_book"


                try {

                    val jsonObjectRequest = object : JsonArrayRequest(Request.Method.GET, url , null, Response.Listener {
                        /*pur = it[0] as Int
                        sal = it[1] as Int*/
                    },Response.ErrorListener {

                        if(activity!=null) {
                            Toast.makeText(
                                activity as Context,
                                "Volley error occurred !",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }){

                    }
                    queue.add(jsonObjectRequest)

                }catch (e: JSONException){
                    Toast.makeText(activity as Context,"Some unexpected error occurred !",Toast.LENGTH_SHORT).show()
                }



    }
    private fun apiGetCallTran(){

        val queue = Volley.newRequestQueue(activity as Context)
        val url = "https://44e2-122-168-199-16.ngrok.io/api/getTransactionList"


        try {

            val jsonObjectRequest = object : JsonObjectRequest(Request.Method.GET, url , null, Response.Listener {
             // try {

                   val res = it.getJSONArray("response")

                   for (i in 0 until res.length()){
                       val jo = res.getJSONObject(i)
                       val transaction = CreateTransaction(
                           jo.getString("date"),
                           jo.getString("time"),
                           jo.getString("name"),
                           jo.getString("unit"),
                           jo.getInt("quantity"),
                           jo.getInt("totalPrice"),
                           jo.getString("typeOfTransaction")

                       )

                       sal+=jo.getInt("totalPrice")
                       list.add(transaction)
                       recyclerAdapter = DashboardRecyclerAdapter(activity as Context,list)

                       recyclerDashboard.adapter = recyclerAdapter

                       recyclerDashboard.layoutManager = layoutManager
                   }
                Log.i("sal",sal.toString())
                saleDash.text = sal.toString()
                sale.text = sal.toString()


               /*}catch (e:Exception){
                   Toast.makeText(activity as Context,"Some unexpected error occurred !",Toast.LENGTH_SHORT).show()
               }*/
            },Response.ErrorListener {

                if(activity!=null) {
                    Toast.makeText(
                        activity as Context,
                        "Volley error occurred !",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }){

            }
            queue.add(jsonObjectRequest)

        }catch (e: JSONException){
            Toast.makeText(activity as Context,"Some unexpected error occurred !",Toast.LENGTH_SHORT).show()
        }


    }
}