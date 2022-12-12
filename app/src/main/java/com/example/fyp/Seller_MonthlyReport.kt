package com.example.fyp

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp.Entity.Food
import com.example.fyp.Entity.Order
import com.example.fyp.Entity.OrderDetail
import com.example.fyp.Entity.Report
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.*

class Seller_MonthlyReport : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private var userObj: String? = ""

    private lateinit var recyclerView: RecyclerView
    private lateinit var reportArrayList: ArrayList<Report>
    private lateinit var reportAdapter: ReportAdapter

    // For retrieving accessible
    private var orderList: ArrayList<Order> = arrayListOf()
    private var orderDetailList: ArrayList<OrderDetail> = arrayListOf()

    private var totalPrice = 0.0

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller_view_monthly_report)

        getCFoodStallID()

        recyclerView = findViewById(R.id.rvReport)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        reportArrayList = arrayListOf()

        reportAdapter = ReportAdapter(reportArrayList)
        recyclerView.adapter = reportAdapter

        val formatter = SimpleDateFormat("MM")
        val date = Date()
        val current = formatter.format(date)

        var filterData = ""

        val spinner = findViewById<Spinner>(R.id.spinner)
        spinner.setSelection(current.toInt() - 1)

        spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                filterData = "${p2+1}"

                if(filterData.toInt() < 10)
                    filterData = addLeadingZero(filterData)

                generateDailyReport(filterData)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    private fun getCFoodStallID() {
        val intent = intent
        userObj = intent.getStringExtra("foodStall")
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun generateDailyReport(filterContent: String) {
        reset()

        val tfoot = findViewById<TableRow>(R.id.trTotal)
        val tfNoRecord = findViewById<TableRow>(R.id.trNoRecord)

        db = FirebaseFirestore.getInstance()
        db.collection("order")
            .whereEqualTo("foodstallID", userObj)
            .get()
            .addOnSuccessListener { it ->
                it?.forEach{ data ->
                    val order = data.toObject<Order>()
                    orderList.add(order)
                }

                orderList.forEach { od ->
                    od.date = od.date?.substring(3,5)
                }

                orderList = orderList.filter{ it.date?.equals(filterContent) == true } as ArrayList<Order>

                orderList = orderList.filter{ it.status?.equals("Order Completed", ignoreCase = true) == true } as ArrayList<Order>

                if(orderList.size > 0){
                    db.collection("orderDetail")
                        .whereIn("orderDetailID", orderList.map{ it.orderDetailID })
                        .get()
                        .addOnSuccessListener {
                            it?.forEach{ data ->
                                val orderContent = data.get("OrderContent") as HashMap<String,Int>
                                val orderDetail = data.toObject<OrderDetail>()
                                orderDetail.orderContent = orderContent
                                orderDetailList.add(orderDetail)
                            }

                            val orderedFood: HashMap<String, Long> = HashMap()
                            orderDetailList.forEach { od ->
                                od.orderContent?.forEach { oc ->
                                    if(orderedFood.containsKey(oc.key)){
                                        orderedFood[oc.key] = orderedFood.getValue(oc.key) + oc.value.toLong()
                                    }else{
                                        orderedFood[oc.key] = oc.value.toLong()
                                    }
                                }
                            }

                            if(orderedFood.size > 0){
                                db.collection("food")
                                    .whereIn("foodID", orderedFood.map{ of -> of.key})
                                    .get()
                                    .addOnSuccessListener {  f ->
                                        f?.forEach { data ->
                                            val food = data.toObject<Food>()

                                            if(orderedFood.containsKey(food.foodID)){
                                                val qty = orderedFood[food.foodID]!!
                                                val subtotal: Double = qty * food.price?.toDouble()!!

                                                val month = DateFormatSymbols().months[filterContent.toInt()-1]

                                                val reportData = Report(food.foodID, food.name, month, qty.toInt(), subtotal)

                                                totalPrice += subtotal
                                                reportArrayList.add(reportData)
                                            }

                                        }

                                        if(reportArrayList.size > 0){
                                            val tvTotalPrice = findViewById<TextView>(R.id.colTotalPrice)
                                            tvTotalPrice.text = "RM ${totalPrice}0"

                                            tfoot.visibility = View.VISIBLE
                                            tfNoRecord.visibility = View.GONE
                                        }else{
                                            tfoot.visibility = View.GONE
                                            tfNoRecord.visibility = View.VISIBLE
                                        }

                                        reportAdapter.notifyDataSetChanged()
                                    }
                            }else{
                                tfoot.visibility = View.GONE
                                tfNoRecord.visibility = View.VISIBLE
                            }
                        }
                }else{
                    tfoot.visibility = View.GONE
                    tfNoRecord.visibility = View.VISIBLE
                }
            }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun reset(){
        totalPrice = 0.0
        val tvTotalPrice = findViewById<TextView>(R.id.colTotalPrice)
        tvTotalPrice.text = "RM ${totalPrice}0"
        orderList.clear()
        orderDetailList.clear()
        reportArrayList.clear()
        reportAdapter.notifyDataSetChanged()
    }

    private fun addLeadingZero(month: String): String{
        return "0${month}"
    }

}