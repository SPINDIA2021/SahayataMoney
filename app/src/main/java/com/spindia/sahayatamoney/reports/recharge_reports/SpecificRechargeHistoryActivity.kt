package com.spindia.sahayatamoney.reports.recharge_reports

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.spindia.sahayatamoney.R
import com.spindia.sahayatamoney.adapters_recyclerview.RecentRechargesAdapter
import com.spindia.sahayatamoney.model.RecentRechargeHistoryModal
import com.spindia.sahayatamoney.model.UserModel
import com.spindia.sahayatamoney.network_calls.AppApiCalls
import com.spindia.sahayatamoney.utils.*
import kotlinx.android.synthetic.main.activity_specific_recharge_history.*
import kotlinx.android.synthetic.main.activity_specific_recharge_history.custToolbar
import kotlinx.android.synthetic.main.activity_specific_recharge_history.progress_bar
import kotlinx.android.synthetic.main.activity_specific_recharge_history.rvRechargeHistory
import kotlinx.android.synthetic.main.activity_specific_recharge_history.view.*
import org.json.JSONObject
import java.util.*

class SpecificRechargeHistoryActivity : AppCompatActivity(), AppApiCalls.OnAPICallCompleteListener {

    lateinit var recentRechargesAdapter: RecentRechargesAdapter
    var recentRechargeHistoryModalArrayList = ArrayList<RecentRechargeHistoryModal>()
    private val RECHARGE_HISTORY: String = "RECHARGE_HISTORY"
    lateinit var userModel: UserModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = resources.getColor(R.color.status_bar, this.theme)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        }
        setContentView(R.layout.activity_specific_recharge_history)


        //Toolbar
        custToolbar.ivBackBtn.setOnClickListener { onBackPressed() }
        val gson = Gson()
        val json = AppPrefs.getStringPref("userModel", this)
        userModel = gson.fromJson(json, UserModel::class.java)



        tvSearchBtn.setOnClickListener {
            if (!etSearch.getText().toString().isEmpty()
                && etDateSearch.getText().toString().isEmpty()
            ) {
                hideKeyboard()
                recentRechargeHistoryModalArrayList =
                    java.util.ArrayList()
                rechargeHistoryByMobile(
                    userModel.cus_id,
                    etSearch.getText().toString(),
                    AppPrefs.getStringPref("deviceId", this).toString(),
                    AppPrefs.getStringPref("deviceName", this).toString(),
                    userModel.cus_pin,
                    userModel.cus_pass,
                    userModel.cus_mobile, userModel.cus_type
                )
                //filterNumber(etSearch.getText().toString());
            }
            if (!etDateSearch.getText().toString().isEmpty()
                && etSearch.getText().toString().isEmpty()) {
                recentRechargeHistoryModalArrayList = java.util.ArrayList()
                etSearch.setText("")
                rechargeHistoryByDate(
                    userModel.cus_id,
                    AppCommonMethods.convertDateFormat("dd/MM/yyyy",
                        "yyyy-MM-dd", etDateSearch.text.toString()).toString(),
                    AppPrefs.getStringPref("deviceId", this).toString(),
                    AppPrefs.getStringPref("deviceName", this).toString(),
                    userModel.cus_pin,
                    userModel.cus_pass,
                    userModel.cus_mobile, userModel.cus_type
                )

                //filterDate(etDateSearch.getText().toString());
            }
        }

        etDateSearch.setOnClickListener {
            etSearch.setText("")
            TodatePicker() }

    }

    private fun rechargeHistoryByMobile(
        cus_id: String, mobile: String, deviceId: String, deviceName: String, pin: String,
        pass: String, cus_mobile: String, cus_type: String
    ) {
        etDateSearch.setText("")
        progress_bar.visibility = View.VISIBLE

        if (AppCommonMethods(this).isNetworkAvailable) {
            val mAPIcall =
                AppApiCalls(this, RECHARGE_HISTORY, this)
            mAPIcall.rechargeHistoryByMobile(
                cus_id, mobile, deviceId, deviceName, pin,
                pass, cus_mobile, cus_type
            )
        } else {

            Toast.makeText(this, "Internet Error", Toast.LENGTH_SHORT).show()
        }
    }

    private fun rechargeHistoryByDate(
        cus_id: String, date: String, deviceId: String, deviceName: String, pin: String,
        pass: String, cus_mobile: String, cus_type: String
    ) {
        etSearch.setText("")
        progress_bar.visibility = View.VISIBLE

        if (AppCommonMethods(this).isNetworkAvailable) {
            val mAPIcall =
                AppApiCalls(this, RECHARGE_HISTORY, this)
            mAPIcall.rechargeHistoryByDate(
                cus_id, date, deviceId, deviceName, pin,
                pass, cus_mobile, cus_type
            )
        } else {

            Toast.makeText(this, "Internet Error", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onAPICallCompleteListner(item: Any?, flag: String?, result: String) {
        if (flag.equals(RECHARGE_HISTORY)) {
            recentRechargeHistoryModalArrayList.clear()
            Log.e("RECHARGE_HISTORY", result)
            val jsonObject = JSONObject(result)
            val status = jsonObject.getString(AppConstants.STATUS)
            Log.e(AppConstants.STATUS, status)
            if (status.contains("true")) {

                progress_bar.visibility = View.INVISIBLE

                val cast = jsonObject.getJSONArray("result")

                for (i in 0 until cast.length()) {
                    val notifyObjJson = cast.getJSONObject(i)
                    val recid = notifyObjJson.getString("recid")
                    Log.e("recid ", recid)
                    val rechargeHistoryModal = Gson()
                        .fromJson(
                            notifyObjJson.toString(),
                            RecentRechargeHistoryModal::class.java
                        )


                    recentRechargeHistoryModalArrayList.add(rechargeHistoryModal)
                }

                rvRechargeHistory.apply {

                    layoutManager = LinearLayoutManager(this@SpecificRechargeHistoryActivity)
                    recentRechargesAdapter = RecentRechargesAdapter(
                        context, recentRechargeHistoryModalArrayList
                    )
                    rvRechargeHistory.adapter = recentRechargesAdapter
                }


            } else {
                recentRechargeHistoryModalArrayList.clear()
                rvRechargeHistory.apply {

                    layoutManager = LinearLayoutManager(this@SpecificRechargeHistoryActivity)
                    recentRechargesAdapter = RecentRechargesAdapter(
                        context, recentRechargeHistoryModalArrayList
                    )
                    rvRechargeHistory.adapter = recentRechargesAdapter
                }
                progress_bar.visibility = View.INVISIBLE


            }
        }

    }

    fun TodatePicker() {

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        var dpd =
            DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
                val mmMonth = mMonth + 1
                val date = "$mDay/$mmMonth/$mYear"
                etDateSearch.setText(date)

            }, year, month, day)
        dpd.show()
    }

}