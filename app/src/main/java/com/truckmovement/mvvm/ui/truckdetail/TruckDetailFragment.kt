package com.truckmovement.mvvm.ui.truckdetail

//import com.example.mvvm.ui.mailsender.Mailer

import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.ViewModelProviders
import com.truckmovement.mvvm.R
import com.truckmovement.mvvm.ui.Base.BaseFragment

import com.truckmovement.mvvm.ui.home.HomeActivity
import com.truckmovement.mvvm.ui.truckdetail.viewmodel.TruckDetailViewModel
import com.truckmovement.mvvm.ui.truckdetail.viewmodelfactory.TruckDetailViewModelFactory
import com.truckmovement.mvvm.utils.showToast
import kotlinx.android.synthetic.main.fragment_truck_detail.view.*
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.FileNotFoundException
import java.io.IOException


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class TruckDetailFragment : BaseFragment(), View.OnClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var Schichtbeginn = "No"
    var Schichtende = "No"
    var Pause = "No"
    var Pausenende = "No"
    lateinit var et_truck_number: EditText
    lateinit var dispo_email: EditText
    lateinit var customer_email: EditText
    lateinit var shift_start: CheckBox
    lateinit var shift_end: CheckBox
    lateinit var braekstart: CheckBox
    lateinit var break_end: CheckBox
    lateinit var viewModel: TruckDetailViewModel
    var departureList = ArrayList<String>()
    var arrivalList = ArrayList<String>()
    var departure = ""
    var arrival = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_truck_detail, container, false)
        viewModel = ViewModelProviders.of(this, TruckDetailViewModelFactory(this)).get(TruckDetailViewModel::class.java)
        observer()
        et_truck_number = view.findViewById<EditText>(R.id.et_truck_number)
        dispo_email = view.findViewById(R.id.dispo_email)
        customer_email = view.findViewById(R.id.customer_email)
        shift_start = view.findViewById(R.id.shift_start)
        shift_end = view.findViewById(R.id.shift_end)
        braekstart = view.findViewById(R.id.braekstart)
        break_end = view.findViewById(R.id.break_end)
        view.sendBtn.setOnClickListener(this)
        departureList = ArrayList()
        AsyncTaskExample(this, view).execute()

        view.departure_sp.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View,
                position: Int,
                id: Long
            ) {
                departure = parentView!!.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
            }
        })

        view.arrival_sp.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View,
                position: Int,
                id: Long
            ) {
                arrival = parentView!!.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
            }
        })
        return view
    }

    private fun observer() {
        viewModel.sendMailApiResponse.observeForever {
            if (it.status == "1") {
                showToast(this.requireContext(), this.getString(R.string.datasentsuccessfully))
                fragmentManager!!.popBackStack()
                (activity as HomeActivity).updateToolbaar()

            }
        }
    }

    fun readExceldata() {
        departureList.clear()
        arrivalList.clear()
        try {
            val am = activity!!.getAssets()
            val abs = am.open("Departure_Field_Values.xlsx")
            // Using XSSF for xlsx format, for xls use HSSF
            val workbook = XSSFWorkbook(abs)
            val numberOfSheets = workbook.getNumberOfSheets()
            //looping over each workbook sheet
            for (i in 0 until numberOfSheets) {
                val sheet = workbook.getSheetAt(i)
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
                            departureList.add(cell.getStringCellValue())
                            arrivalList.add(cell.getStringCellValue())
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

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TruckDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            view.sendBtn.id ->if(viewModel.validation())if(viewModel.termsAndCondtions())  viewModel.sendEmail()
        }
    }

    private class AsyncTaskExample(var frag: TruckDetailFragment, var view: View) :
        AsyncTask<String?, String?, Boolean>() {

        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun onPostExecute(bitmap: Boolean) {
            super.onPostExecute(bitmap)
            frag.departureList.sort()
            frag.arrivalList.sort()
            if(frag.departureList.size>0){
                frag.departureList.add(0,frag.getString(R.string.arrival))
                val arrayAdapter = ArrayAdapter(frag.requireContext(), android.R.layout.simple_spinner_item, frag.departureList)
                view!!.departure_sp.setAdapter(arrayAdapter)
            }
            if(frag.arrivalList.size>0){
                frag.arrivalList.add(0,this.frag.getString(R.string.departure))
                val arrayAdapter = ArrayAdapter(frag.context!!, android.R.layout.simple_spinner_item, frag.arrivalList)
                view!!.arrival_sp.setAdapter(arrayAdapter)
            }
        }

        override fun doInBackground(vararg params: String?): Boolean {
            frag.readExceldata()
            return true
        }
    }
}
