package com.example.digitalsalekot.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.digitalsalekot.R
import org.json.JSONObject
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SaleFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SaleFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var txtUnit: TextView
    lateinit var txtDate: TextView
    lateinit var txtTime: TextView
    lateinit var txtProductName: TextView
    lateinit var txtPrice: TextView
    lateinit var txtUnitOfPrice: TextView
    lateinit var txtTotalQuantity: TextView
    lateinit var txtTotalSum: TextView
    lateinit var btnSave: Button
    lateinit var date : String
    lateinit var time : String


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
        val view = inflater.inflate(R.layout.fragment_sale, container, false)

        txtUnit = view.findViewById(R.id.txtAddUnit)
        txtDate = view.findViewById(R.id.txtDate)
        txtTime = view.findViewById(R.id.txtTime)
        txtProductName = view.findViewById(R.id.txtProductName)
        txtPrice = view.findViewById(R.id.txtPrice)
        txtUnitOfPrice = view.findViewById(R.id.txtUnitOfPrice)
        txtTotalQuantity = view.findViewById(R.id.txtTotQuantity)
        txtTotalSum = view.findViewById(R.id.txtTotalSum)
        btnSave = view.findViewById(R.id.btnSave)

         date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
         time = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())


        txtDate.text = date
        txtTime.text = time

        val bottomSheetFragment = BottomSheetFragment()
        txtUnit.setOnClickListener {
            activity?.let { it1 -> bottomSheetFragment.show(it1.supportFragmentManager,"BottomSheetDialog") }
        }


        btnSave.setOnClickListener {

            apiPostCall()
        }

        return view
    }

    private fun apiPostCall(){

        val queue = Volley.newRequestQueue(activity as Context)
        val url = "https://44e2-122-168-199-16.ngrok.io/api/createTransaction"

        val name = txtProductName.text.toString()

        var pricePerItem = 0;
        if(txtPrice.text.toString()!="") {
            pricePerItem = txtPrice.text.toString().toInt()
        }
            val unit = txtUnit.text.toString()

            var quantity = 0
            if (txtTotalQuantity.text.toString() != "") {
                quantity = txtTotalQuantity.text.toString().toInt()
            }
            val totalPrice = (pricePerItem*quantity).toString()
            val typeOfTransaction = "IN"

            val jsonParams = JSONObject()
            jsonParams.put("date", date)
            jsonParams.put("time", time)
            jsonParams.put("name", name)
            jsonParams.put("pricePerItem", pricePerItem)
            jsonParams.put("unit", unit)
            jsonParams.put("quantity", quantity)
            jsonParams.put("totalPrice", totalPrice)
            jsonParams.put("typeOfTransaction", typeOfTransaction)

            val jsonRequest =
                object : JsonObjectRequest(Method.POST, url, jsonParams, Response.Listener {

                    try {

                        //val success = it.getBoolean("response")
                        val success = true
                        if (success) {
                            Toast.makeText(activity as Context, "Data Added", Toast.LENGTH_SHORT)
                                .show()
                            val transaction = activity?.supportFragmentManager?.beginTransaction()
                            if (transaction != null) {
                                transaction.replace(R.id.frame, DashboardFragment())
                                transaction.disallowAddToBackStack()
                                transaction.commit()
                            }
                        }

                    } catch (e: java.lang.Exception) {
                        Log.i("Error","Some error")
                    }

                }, Response.ErrorListener {
                    Toast.makeText(activity as Context, "Volley error $it", Toast.LENGTH_SHORT)
                        .show()
                }) {
                    /*override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String,String>()
                    headers["Content-type"] = "application/json"
                    headers["token"] = "a087f3bb8710df"
                    return headers
                }*/
                }
            queue.add(jsonRequest)

    }


}