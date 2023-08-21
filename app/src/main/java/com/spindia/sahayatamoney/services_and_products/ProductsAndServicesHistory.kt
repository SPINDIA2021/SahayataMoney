package com.spindia.sahayatamoney.services_and_products

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.spindia.sahayatamoney.R
import com.spindia.sahayatamoney.dmt.SelectDmtAdapter
import com.spindia.sahayatamoney.dmt.SelectDmtModel
import com.spindia.sahayatamoney.model.UserModel
import com.spindia.sahayatamoney.network_calls.AppApiCalls
import com.spindia.sahayatamoney.utils.AppCommonMethods
import com.spindia.sahayatamoney.utils.AppConstants
import com.spindia.sahayatamoney.utils.AppPrefs
import com.spindia.sahayatamoney.utils.toast
import kotlinx.android.synthetic.main.activity_add_beneficiary.*
import kotlinx.android.synthetic.main.activity_add_beneficiary.progress_bar
import kotlinx.android.synthetic.main.activity_products_and_services_history.*
import kotlinx.android.synthetic.main.activity_select_dmt.*
import org.json.JSONObject

class ProductsAndServicesHistory : AppCompatActivity(), AppApiCalls.OnAPICallCompleteListener,
    ProductsHistoryAdapter.ListAdapterListener {

    lateinit var userModel: UserModel
    private val PRODUCT_HISTORY: String = "PRODUCT_HISTORY"
    var productsHistoryArrayList = ArrayList<ProductsHistoryModel>()
    lateinit var productsHistoryAdapter: ProductsHistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products_and_services_history)

        val gson = Gson()
        val json = AppPrefs.getStringPref(AppConstants.USER_MODEL, this)
        userModel = gson.fromJson(json, UserModel::class.java)

        productServicesHistory(userModel.cus_id)

        rvProductsHistory.apply {
            layoutManager = LinearLayoutManager(this@ProductsAndServicesHistory)
            productsHistoryAdapter = ProductsHistoryAdapter(context, productsHistoryArrayList, this@ProductsAndServicesHistory)
            rvProductsHistory.adapter = productsHistoryAdapter
        }
    }


    fun productServicesHistory(
        cus_id: String
    ) {
        progress_bar.visibility = View.VISIBLE

        if (AppCommonMethods(this).isNetworkAvailable) {
            val mAPIcall =
                AppApiCalls(this, PRODUCT_HISTORY, this)
            mAPIcall.productServicesHistory(cus_id)
        } else {

            Toast.makeText(this, "Internet Error", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onAPICallCompleteListner(item: Any?, flag: String?, result: String) {
        if (flag.equals(PRODUCT_HISTORY)) {
            productsHistoryArrayList.clear()

            Log.e("PRODUCT_HISTORY", result)
            val jsonObject = JSONObject(result)
            val status = jsonObject.getString(AppConstants.STATUS)
            val message = jsonObject.getString("message")
            Log.e(AppConstants.STATUS, status)
            if (status.contains("true")) {

                progress_bar.visibility = View.INVISIBLE

                val cast = jsonObject.getJSONArray("result")

                for (i in 0 until cast.length()) {
                    val notifyObjJson = cast.getJSONObject(i)
                    Log.e("Name",notifyObjJson.getString("txn_comment"))
                    val productsHistoryModel = Gson()
                        .fromJson(
                            notifyObjJson.toString(),
                            ProductsHistoryModel::class.java
                        )
                    productsHistoryArrayList.add(productsHistoryModel)
                }

                rvProductsHistory.adapter!!.notifyDataSetChanged()
                toast(message)
            } else {
                progress_bar.visibility = View.INVISIBLE
                toast(message)
            }
        }
    }

    override fun onClickAtOKButton(productsHistoryModel: ProductsHistoryModel?) {

    }
}