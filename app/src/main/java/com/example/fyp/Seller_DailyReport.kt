package com.example.fyp

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
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
import java.text.SimpleDateFormat
import java.util.*

class Seller_DailyReport : AppCompatActivity() {
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
        setContentView(R.layout.activity_seller_view_daily_report)

        getCFoodStallID()

        recyclerView = findViewById(R.id.rvReport)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        reportArrayList = arrayListOf()

        reportAdapter = ReportAdapter(reportArrayList)
        recyclerView.adapter = reportAdapter

        val formatter = SimpleDateFormat("dd-MM-yyyy")
        val date = Date()
        val current = formatter.format(date)

        // Change the argument for demo purpose
        // put the variable -> current into the function below
        // It will always display today's sales
        generateDailyReport("05-12-2022")
    }

    private fun getCFoodStallID() {
        val intent = intent
        userObj = intent.getStringExtra("foodStall")
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun generateDailyReport(filterContent: String) {
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

                orderList = orderList.filter{ it.date?.contains(filterContent) == true } as ArrayList<Order>

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
                                                val reportData = Report(food.foodID, food.name, filterContent, qty.toInt(), subtotal)

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

}