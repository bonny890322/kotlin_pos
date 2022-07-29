package com.example.posg

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_bill.*
import org.json.JSONArray
import java.io.File
import java.io.InputStream

class Bill : AppCompatActivity() {
    var sushiarr = JSONArray()
    var arr = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bill)

        val file = File(filesDir,"sushi.json")

        var inputStream: InputStream = file.inputStream()
        var json = inputStream.bufferedReader().use { it.readText() }
        sushiarr = JSONArray(json)
        arr.clear()

        for(i in 0..sushiarr.length()-1)
        {
            var jsonobj = sushiarr.getJSONObject(i)
            myItem.text = myItem.text.toString() + jsonobj.getString("sushi") + " * "  + jsonobj.getString("num") + " $" + (jsonobj.getString("price").toInt()*jsonobj.getString("num").toInt()) + "\n"
        }

        val bundle = intent.extras
        val MM = bundle!!.getString("M")
        val RR = bundle!!.getString("R")

        //加原本金額多少跟折扣多少
        myM.text = "金額:" + MM
        myR.text = "實收:" + RR
        val mmm = MM!!.toInt()
        val rrr = RR!!.toInt()
        myChange.setText("找零:" + (rrr - mmm))
    }
}
