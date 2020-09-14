package com.truckmovement.mvvm.ui

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ProgressBar
import com.truckmovement.mvvm.R
import com.truckmovement.mvvm.ui.Base.BaseActivity
import com.truckmovement.mvvm.ui.LogIn.LoginActivity
import com.truckmovement.mvvm.ui.home.HomeActivity
import com.truckmovement.mvvm.ui.loadinginformation.model.FromTypeModel
import com.truckmovement.mvvm.ui.loadinginformation.model.ToTypeModel
import com.truckmovement.mvvm.utils.from_spList
import com.truckmovement.mvvm.utils.to_spList
import kotlinx.android.synthetic.main.activity_splash.*
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.FileNotFoundException
import java.io.IOException


class SplashActivity : BaseActivity() {

    var idnumber = ""
     var p: ProgressDialog ?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
            p = ProgressDialog(this)

        AsyncTaskExample(this).execute()
    }

    fun readExceldata() {
        to_spList.clear()
        from_spList.clear()
        try {
            val am = getAssets()
            val abs = am.open("STE.xlsx")
            // Using XSSF for xlsx format, for xls use HSSF
            val workbook = XSSFWorkbook(abs)
            val numberOfSheets = workbook.getNumberOfSheets()
            //looping over each workbook sheet
            for (i in 0 until numberOfSheets) {
                val sheet = workbook.getSheetAt(i)
                from_spList.add(FromTypeModel(workbook.getSheetAt(i).sheetName, i))
                idnumber = i.toString()
                val rowIterator = sheet.iterator()
                //iterating over each row
                while (rowIterator.hasNext()) {
                    val row = rowIterator.next()
                    val cellIterator = row.cellIterator()
                    //Iterating over each cell (column wise) in a particular row.
                    while (cellIterator.hasNext()) {
                        val cell = cellIterator.next()
                        //The Cell Containing String will is name.
                        if (Cell.CELL_TYPE_STRING === cell.getCellType()) {
                            to_spList.add(ToTypeModel(cell.getStringCellValue(), idnumber.toInt()))

                        }
                    }
                }
            }


        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }
}

private class AsyncTaskExample(var activity: SplashActivity) :
    AsyncTask<String?, String?, Boolean>() {

    override fun onPreExecute() {
        super.onPreExecute()
        activity.p!!.setMessage(activity.getString(R.string.data_loading))
        activity.p!!.setIndeterminate(false)
        activity.p!!.setCancelable(false)
        activity.p!!.show()
    }


    override fun onPostExecute(bitmap: Boolean) {
        super.onPostExecute(bitmap)

        activity.p!!.hide()
        if (activity.sessionManager.getValue(activity.sessionManager.username, "") != "" &&
            activity.sessionManager.getValue(activity.sessionManager.password, "") != ""
        ) {
            val i = Intent(activity, HomeActivity::class.java)
            activity.startActivity(i)
            activity.overridePendingTransition(R.anim.fade_in_animation, R.anim.fade_out_animation);
            activity.finish()
        } else {
            val i = Intent(activity, LoginActivity::class.java)
            activity.startActivity(i)
            activity.overridePendingTransition(R.anim.fade_in_animation, R.anim.fade_out_animation);
            activity.finish()
        }

    }

    override fun doInBackground(vararg params: String?): Boolean {
        activity.readExceldata()
        return true
    }
}