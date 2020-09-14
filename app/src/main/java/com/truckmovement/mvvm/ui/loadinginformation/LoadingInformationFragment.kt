package com.truckmovement.mvvm.ui.loadinginformation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.CheckBox
import androidx.lifecycle.ViewModelProviders
import com.truckmovement.mvvm.R
import com.truckmovement.mvvm.ui.Base.BaseFragment
import com.truckmovement.mvvm.ui.home.HomeActivity

import com.truckmovement.mvvm.ui.loadinginformation.viewmodel.LodingInfoVM
import com.truckmovement.mvvm.ui.loadinginformation.viewmodelfactory.LoadingInfoVMF
import com.truckmovement.mvvm.utils.from_spList
import com.truckmovement.mvvm.utils.showToast
import com.truckmovement.mvvm.utils.to_spList
import kotlinx.android.synthetic.main.fragment_loading_information.*
import kotlinx.android.synthetic.main.fragment_loading_information.view.*
import kotlinx.android.synthetic.main.fragment_loading_information.view.from_sp


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class LoadingInformationFragment : BaseFragment(), View.OnClickListener {
    private var param1: String? = null
    private var param2: String? = null
    var from_citylist = ArrayList<String>()
    var to_citylist = ArrayList<String>()
    var from = ""
    var to = ""
    lateinit var shift_start: CheckBox
    lateinit var shift_end: CheckBox
    lateinit var braekstart: CheckBox
    lateinit var break_end: CheckBox
    lateinit var comp_cargo: CheckBox
    lateinit var departure: CheckBox
    var Register = "No"
    var Cancellation = "No"
    var Braekstart = "No"
    var Braekend = "No"
    var Compcargo = "No"
    var Departure = "No"
    var fromsp_id: String = "0"
    lateinit var viewModel: LodingInfoVM
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

        var view = inflater.inflate(R.layout.fragment_loading_information, container, false)
        viewModel = ViewModelProviders.of(this, LoadingInfoVMF(this)).get(LodingInfoVM::class.java)
        shift_start = view.findViewById(R.id.shift_start)
        shift_end = view.findViewById(R.id.shift_end)
        braekstart = view.findViewById(R.id.braekstart)
        break_end = view.findViewById(R.id.break_end)
        comp_cargo = view.findViewById(R.id.comp_cargo)
        departure = view.findViewById(R.id.departure)
        viewModel.setFromSppinerData(view)
        observer()
        view.from_sp.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View,
                position: Int,
                id: Long
            ) {
                from = parentView!!.getItemAtPosition(position).toString()
                var fromsp_city = from_citylist[position]
                // here we are get City from sorted list and get actualy id of selected city
                for (i in from_spList.indices) {
                    if (from_spList[i].city == fromsp_city) {
                        fromsp_id = from_spList[i].id.toString()
                    }
                }
                to_citylist = ArrayList()
                to_citylist.clear()
                for (i in to_spList.indices) {
                    if (to_spList[i].id == fromsp_id?.toInt()) {
                        to_citylist.add(to_spList[i].city)
                    }
                }
                to_citylist.sort()
                to_citylist.add(0, getString(R.string.loading_place))
                val arrayAdapter1 = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    to_citylist
                )
                view.to_sp.setAdapter(arrayAdapter1)
                hideProgress()
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
            }
        })

        view.to_sp.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View,
                position: Int,
                id: Long
            ) {
                to = parentView!!.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
            }
        })

        view.sendBtn.setOnClickListener(this)
        view.openTermsCondition.setOnClickListener(this)

        return view
    }


    private fun observer() {
        viewModel.mailApiResponce.observeForever {
            if (it.status == "1") {
                showToast(this.requireContext(), this.getString(R.string.datasentsuccessfully))
                fragmentManager!!.popBackStack()
                (activity as HomeActivity).updateToolbaar()


            }
        }
    }

    override fun onClick(view: View?) {

        when (view!!.id) {
            sendBtn.id -> if (viewModel.termsAndCondition()) viewModel.sendMail()
        }
    }


    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoadingInformationFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


}


