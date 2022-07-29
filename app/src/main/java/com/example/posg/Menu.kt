package com.example.posg

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_menu.*
import kotlinx.android.synthetic.main.activity_menu.view.*
import kotlinx.android.synthetic.main.fragment_soup.*
import kotlinx.android.synthetic.main.fragment_soup.view.*
import kotlinx.android.synthetic.main.pay_dialog.view.*
import kotlinx.android.synthetic.main.sushiitem_dialog.*
import kotlinx.android.synthetic.main.sushiitem_dialog.view.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.*
import java.util.zip.Inflater
import kotlin.coroutines.coroutineContext

class Menu : AppCompatActivity() {

    var arr = arrayListOf<String>()
    var price = 0
    var sushiarr = JSONArray()
    var adpt: ArrayAdapter<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val bundle = intent.extras
        val name = bundle?.getString("employee")
        ID.text = "員工：" + name

        val file = File(filesDir,"sushi.json")

        pay.setOnClickListener(msg)
        sushiList.setOnItemClickListener(sushiListOnClick)
        sushiList.setOnItemLongClickListener(itemLongListener)

        viewpager.adapter = SamplePagerAdapter(file,object : onNotify {
            override fun sushiOnClick() {
                sushiItem()
                howmuch()
                sushiPrice.text = "金額:" + price
            }
        })

        viewpager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(viewpager))
    }

    private fun sushiItem(){
        val file = File(filesDir,"sushi.json")

        var inputStream: InputStream = file.inputStream()
        var json = inputStream.bufferedReader().use { it.readText() }
        sushiarr = JSONArray(json)
        arr.clear()

        for(i in 0..sushiarr.length()-1)
        {
            var jsonobj = sushiarr.getJSONObject(i)
            arr.add(jsonobj.getString("sushi") + ":$" + jsonobj.getString("price")+ "*"  + jsonobj.getString("num"))
        }

        adpt = ArrayAdapter(this,android.R.layout.simple_list_item_1,arr)
        sushiList.adapter = adpt
    }

    val sushiListOnClick = AdapterView.OnItemClickListener { parent, view, position, id ->
        var jsonobj = sushiarr.getJSONObject(position)
        val myDialog = LayoutInflater.from(this).inflate(R.layout.sushiitem_dialog,null)
        myDialog.myNum.setText(jsonobj.getString("num"))
        val d = AlertDialog.Builder(this)
            .setView(myDialog)
            .setTitle(jsonobj.getString("sushi") + "數量")
        val myAlertDialog = d.show()

        myDialog.myAdd.setOnClickListener{
            val n = myDialog.myNum.text.toString().toInt()+1
            myDialog.myNum.setText(""+n)
        }
        myDialog.myMinus.setOnClickListener{
            val n = myDialog.myNum.text.toString().toInt()-1
            if(n != 0){
                myDialog.myNum.setText(""+n)
            }

        }
        myDialog.myOK.setOnClickListener{
            myAlertDialog.dismiss()

            val n = myDialog.myNum.text.toString()
            var obj = JSONObject()
            obj.put("sushi",jsonobj.getString("sushi"))
            obj.put("price",jsonobj.getString("price"))
            obj.put("num",n)
            sushiarr.put(position,obj)
            saveJson(sushiarr.toString())
            sushiItem()
            howmuch()
            sushiPrice.text = "金額:" + price
        }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    val itemLongListener = AdapterView.OnItemLongClickListener { parent, view, position, id ->
        val d = AlertDialog.Builder(this)
        d.setTitle(R.string.app_name)
            .setMessage("確認")
            .setPositiveButton("confirm"){ dialog, which ->
                dialog.cancel()
                sushiarr.remove(position)
                saveJson(sushiarr.toString())
                sushiItem()
                howmuch()
                sushiPrice.text = "金額:" + price
            }
            .setNegativeButton("canael",null)
            .show()
        true
    }

    private fun howmuch(){
        price = 0
        if(sushiarr.length() > 0){
            for(i in 0..sushiarr.length()-1)
            {
                var jsonobj = sushiarr.getJSONObject(i)
                price += jsonobj.getString("price").toInt() * jsonobj.getString("num").toInt()
            }
        }

    }

    private fun saveJson(s: String){
        val file = File(filesDir,"sushi.json")
        val output: Writer
        if (!file.exists()) {
            file.createNewFile()
        }
        output= BufferedWriter(FileWriter(file))
        output.write(s)
        output.close()
    }

    val msg = View.OnClickListener {
        val discount = myDiscount.text.toString()
        //val d = AlertDialog.Builder(this)

        val myDialog = LayoutInflater.from(this).inflate(R.layout.pay_dialog,null)
        var M = 0
        if(discount.equals("")){
            myDialog.totalDiscount.setText("金額:" + price)
            M = price
        }else if(discount.toInt() > 10){
            myDialog.total.setText("原始金額:" + price)
            M = price * discount.toInt() / 100
            myDialog.totalDiscount.setText("折扣後金額:" + M)
        }else{
            myDialog.total.setText("原始金額:" + price)
            M = price * discount.toInt() / 10
            myDialog.totalDiscount.setText("折扣後金額:" + M)
        }


        val d = AlertDialog.Builder(this)
            .setView(myDialog)
            .setTitle("付款")
        val myAlertDialog = d.show()

        myDialog.myConfirm.setOnClickListener{
            var R = ""
            R = myDialog.receive.text.toString()
            if(R.isNullOrEmpty()){
                Toast.makeText(this, "未輸入金額", Toast.LENGTH_LONG).show()
            }else if(R.toInt() < M){
                Toast.makeText(this, "金額不夠", Toast.LENGTH_LONG).show()
            }else if(R.toInt() >= M){
                myAlertDialog.dismiss()
                val intent = Intent(this,Bill::class.java)
                var bundle = Bundle()
                bundle.putString("M",M.toString())
                bundle.putString("R",R)
                intent.putExtras(bundle)
                startActivityForResult(intent,0)
            }

        }
        myDialog.myCancel.setOnClickListener{
            myAlertDialog.dismiss()
        }

    }

}
