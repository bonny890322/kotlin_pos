package com.example.posg

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.viewpager.widget.PagerAdapter
import org.json.JSONArray
import org.json.JSONObject
import java.io.*

interface onNotify{
    fun sushiOnClick()
}

class SamplePagerAdapter(file: File,listener: onNotify): PagerAdapter() {

        val file = file
        var jsonarr = JSONArray()
        var arr = arrayListOf<String>()
        var adpt: ArrayAdapter<String>? = null
        lateinit var soupView:View //延遲初始化的意思
        lateinit var fryView:View //延遲初始化的意思
        lateinit var fireView:View //延遲初始化的意思
        lateinit var sushiView:View //延遲初始化的意思
        lateinit var boatView:View //延遲初始化的意思
        lateinit var snackView:View //延遲初始化的意思

    val notifyInterface: onNotify = listener

        override fun getCount(): Int {
            return 6
        }

        override fun isViewFromObject(view: View, o: Any): Boolean {
            return view == o
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            //var v:View
            if(position == 0){
                soupView = LayoutInflater.from(container.context).inflate(R.layout.fragment_soup, container, false)

                val soup = soupView.findViewById<Button>(R.id.mySoup)
                soup.setOnClickListener(soup40)
                soup.setOnLongClickListener(soup_40)

                val egg = soupView.findViewById<Button>(R.id.myEgg)
                egg.setOnClickListener(soup40)
                egg.setOnLongClickListener(soup_40)

                container.addView(soupView)
                return soupView
            }else if (position == 1){
                fryView = LayoutInflater.from(container.context).inflate(R.layout.fragment_fry, container, false)

                //val b = fryView.findViewById<TextView>(R.id.bub)
                //b.text = jsonarr.toString()

                val shrimp = fryView.findViewById<Button>(R.id.myShrimp)
                shrimp.setOnClickListener(fry60)
                shrimp.setOnLongClickListener(fry_60)

                val oil = fryView.findViewById<Button>(R.id.myOil)
                oil.setOnClickListener(fry40)
                oil.setOnLongClickListener(fry_40)

                container.addView(fryView)
                return fryView
            }else if (position == 2){
                fireView = LayoutInflater.from(container.context).inflate(R.layout.fragment_fire, container, false)
                container.addView(fireView)
                return fireView
            }else if (position == 3){
                sushiView = LayoutInflater.from(container.context).inflate(R.layout.fragment_sushi, container, false)
                container.addView(sushiView)
                return sushiView
            }else if (position == 4){
                boatView = LayoutInflater.from(container.context).inflate(R.layout.fragment_boat, container, false)
                container.addView(boatView)
                return boatView
            }else{
                snackView = LayoutInflater.from(container.context).inflate(R.layout.fragment_snack, container, false)
                container.addView(snackView)
                return snackView
            }

        }

        override fun destroyItem(container: ViewGroup, position: Int, any: Any) {
            container.removeView(any as View)
        }

        private val soup40 = View.OnClickListener { view->
            var btnName = soupView.findViewById<Button>(view.id).text.toString()
            sushi40(btnName,file)
        }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private val soup_40 = View.OnLongClickListener { view->
        var btnName = soupView.findViewById<Button>(view.id).text.toString()
        sushi_40(btnName,file)
        true
    }

        private val soup60 = View.OnClickListener { view->
            var btnName = soupView.findViewById<Button>(view.id).text.toString()
            sushi60(btnName,file)
        }

        private val fry40 = View.OnClickListener { view->
            var btnName = fryView.findViewById<Button>(view.id).text.toString()
            sushi40(btnName,file)
        }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private val fry_40 = View.OnLongClickListener { view->
        var btnName = fryView.findViewById<Button>(view.id).text.toString()
        sushi_40(btnName,file)
        true
    }

        private val fry60 = View.OnClickListener { view->
            var btnName = fryView.findViewById<Button>(view.id).text.toString()
            sushi60(btnName,file)
        }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private val fry_60 = View.OnLongClickListener { view->
        var btnName = fryView.findViewById<Button>(view.id).text.toString()
        sushi_60(btnName,file)
        true
    }

        private fun sushi40(item:String,file: File){

            var temp = ""
            var n = 1
            var p = -1
            var obj = JSONObject()

                for(i in 0..jsonarr.length()-1)
                {
                    var jsonobj = jsonarr.getJSONObject(i)
                    temp = jsonobj.getString("sushi").toString()
                    if(temp.equals(item)){
                        p = i
                        n = jsonobj.getString("num").toInt() + 1
                    }
                }

            if(n == 1){
                obj.put("sushi",item)
                obj.put("price",40)
                obj.put("num",n)
                jsonarr.put(obj)
            }else{
                obj.put("sushi",item)
                obj.put("price",40)
                obj.put("num",n)
                jsonarr.put(p,obj)
            }

            saveJson(jsonarr.toString(),file)
            notifyInterface.sushiOnClick()
        }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun sushi_40(item:String, file: File){

        var temp = ""
        var n = 1
        var p = -1
        var obj = JSONObject()

        for(i in 0..jsonarr.length()-1)
        {
            var jsonobj = jsonarr.getJSONObject(i)
            temp = jsonobj.getString("sushi").toString()
            if(temp.equals(item)){
                p = i
                n = jsonobj.getString("num").toInt() - 1
            }
        }

        if(n == 0){
            jsonarr.remove(p)
        }else if(n > 0){
            obj.put("sushi",item)
            obj.put("price",40)
            obj.put("num",n)
            jsonarr.put(p,obj)
        }

        saveJson(jsonarr.toString(),file)
        notifyInterface.sushiOnClick()
    }

        private fun sushi60(item:String,file: File){
            //var inputStream: InputStream = file.inputStream()
            //var json = inputStream.bufferedReader().use { it.readText() }
            //jsonarr = JSONArray(json)

            var temp = ""
            var n = 1
            var p = -1
            var obj = JSONObject()

            for(i in 0..jsonarr.length()-1)
            {
                var jsonobj = jsonarr.getJSONObject(i)
                temp = jsonobj.getString("sushi").toString()
                if(temp.equals(item)){
                    p = i
                    n = jsonobj.getString("num").toInt() + 1
                }
            }

            if(n == 1){
                obj.put("sushi",item)
                obj.put("price",60)
                obj.put("num",n)
                jsonarr.put(obj)
            }else if(n > 0){
                obj.put("sushi",item)
                obj.put("price",60)
                obj.put("num",n)
                jsonarr.put(p,obj)
            }

            saveJson(jsonarr.toString(),file)
            notifyInterface.sushiOnClick()
        }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun sushi_60(item:String, file: File){

        var temp = ""
        var n = 1
        var p = -1
        var obj = JSONObject()

        for(i in 0..jsonarr.length()-1)
        {
            var jsonobj = jsonarr.getJSONObject(i)
            temp = jsonobj.getString("sushi").toString()
            if(temp.equals(item)){
                p = i
                n = jsonobj.getString("num").toInt() - 1
            }
        }

        if(n == 0){
            jsonarr.remove(p)
        }else if(n > 0){
            obj.put("sushi",item)
            obj.put("price",60)
            obj.put("num",n)
            jsonarr.put(p,obj)
        }

        saveJson(jsonarr.toString(),file)
        notifyInterface.sushiOnClick()
    }

        private fun saveJson(s: String,file: File){
            val output: Writer
            if (!file.exists()) {
                file.createNewFile()
            }
            output= BufferedWriter(FileWriter(file))
            output.write(s)
            output.close()
        }

    }