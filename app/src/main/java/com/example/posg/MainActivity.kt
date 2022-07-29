package com.example.posg

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import java.io.File
import java.io.InputStream

class MainActivity : AppCompatActivity() {

    var employeeID = arrayListOf<String>()
    var employee = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        readJson()
        confirm.setOnClickListener(signIn)
    }

    private fun readJson(){
        var inputStream: InputStream = assets.open("num.json")
        var json = inputStream.bufferedReader().use { it.readText() }
        var jsonarr = JSONArray(json)


        for(i in 0..jsonarr.length()-1)
        {
            var jsonobj = jsonarr.getJSONObject(i)
            employeeID.add(jsonobj.getString("employeeID"))
            employee.add(jsonobj.getString("employee"))
        }
    }

    val signIn = View.OnClickListener {
        val id = num.text.toString()
        var sign = false

        for(i in 0..employeeID.size-1)
        {
            if( id.equals(employeeID[i]) ){
                val intent = Intent(this,Menu::class.java)

                var bundle = Bundle()
                bundle.putString("employee",employee[i])
                intent.putExtras(bundle)
                sign = true

                startActivityForResult(intent,0)
            }
        }

        if( sign.equals(false) ){
             Toast.makeText(this, "請輸入員工代碼或員工編號有誤", Toast.LENGTH_LONG).show()
        }


    }
}
