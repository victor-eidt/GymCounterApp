package com.techplus.gymmanagement.fragment

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.FragmentAddMemberBinding
import com.example.myapplication.global.DB
import java.text.SimpleDateFormat
import java.util.*

class FragmentAddMember : Fragment() {

    var db: DB? = null
    var oneMonth:String?=""
    var threeMonths:String?=""
    var sixMonths:String?=""
    var oneYear:String?=""
    var threeYear:String?=""
    private lateinit var binding: FragmentAddMemberBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAddMemberBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = activity?.let { DB(it) }

        val cal = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { view1, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "dd/MM/yyyy" // specify the format
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            binding.edtJoining.setText(sdf.format(cal.time))
        }

        binding.imgDate.setOnClickListener {
            activity?.let { it1 ->
                DatePickerDialog(it1, dateSetListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
            }
        }

        binding.spMemberShip.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val value = parent.getItemAtPosition(position).toString().trim()

                if (value == "Select") {
                    binding.edtExpire.setText("")
                    calculateTotal(binding.spMemberShip,binding.edtDiscount,binding.edtAmount)
                } else {
                    if (binding.edtJoining.text.toString().trim().isNotEmpty()) {
                        when (value) {
                            "1 Month" -> calculateExpireDate(1, binding.edtExpire)
                            calculateTotal(binding.spMemberShip,binding.edtDiscount,binding.edtAmount)
                                "3 Months" -> calculateExpireDate(3, binding.edtExpire)
                            calculateTotal(binding.spMemberShip,binding.edtDiscount,binding.edtAmount)
                                "6 Months" -> calculateExpireDate(6, binding.edtExpire)
                            calculateTotal(binding.spMemberShip,binding.edtDiscount,binding.edtAmount)
                                "1 Year" -> calculateExpireDate(12, binding.edtExpire)
                            calculateTotal(binding.spMemberShip,binding.edtDiscount,binding.edtAmount)
                                "3 Years" -> calculateExpireDate(36, binding.edtExpire)
                            calculateTotal(binding.spMemberShip,binding.edtDiscount,binding.edtAmount)
                        }
                    } else {
                        showToast("Select Joining date first")
                        binding.spMemberShip.setSelection(0)
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Not yet implemented
            }
        }

        binding.edtDiscount.addTextChangedListener(object : TexWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int){

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Edittable?){
                if(p0!=null){
                    calculateTotal(binding.spMembemShip,binding.edtDiscount,binding.edtAmount)
                }
            }

        })

        binding.imgPicDate.setOnclickListener{
            activity?.Let{ it1 -> DatePickerDialog(it1,dateSetListener,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)).show()}
        }

        binding.imgTakeImage.setOnclickListener{

        }

        getFee()
    }
    private fun getFee(){
        try {
                val sqlQuery = "SELECT * FROM FEE WHERE ID = '1'"
                db?.fireQuery(sqlQuery)?.use{
                    oneMonth = MyFunction.getValue(it,"ONE_MONTH")
                    threeMonths = MyFunction.getValue(it,"THREE_MONTH")
                    sixMonths = MyFunction.getValue(it,"SIX_MONTH")
                    oneYear = MyFunction.getValue(it,"ONE_YEAR")
                    threeYear = MyFunction.getValue(it,"THREE_YEAR")
                }

        }catch (e:Expection){
            e.printStackTrace()
        }
    }
    private fun calculateTotal(spMember:Spinner,edtDis:EditText,edtAmt:EditText){
        val month = spMember.seLectedItem.tostring().trim()
        var discount = edtDis.text.toString().trim()
        if(edtDis.text.ToString().tostring().isEmpty()){
            discount = "0"
        }

        if(month =="Select"){
            edtAmt.setText("")
        }else if(month =="1 Month"){
            if(discount.trim().isEmpty()){
                discount = "0"
            }
            if(oneMonth!!.trim().isNotEmpty()){
                val discountAmount = ((oneMonth!!.toDouble() * discount.toDouble())/100) //finding out thediscount amount
                val total = oneMonth!!.toDouble() - discountAmount  // Minus discount amount from main amount
                edtAmt.setText(total.toString())

            }
            
        }else if(month =="3 Months"){

            if(discount.trim().isEmpty()){
                discount = "0"
            }
            if(ThreeeMonths!!.trim().isNotEmpty()){
                val discountAmount = ((ThreeeMonths!!.toDouble() * discount.toDouble())/100) //finding out thediscount amount
                val total = ThreeeMonths!!.toDouble() - discountAmount  // Minus discount amount from main amount
                edtAmt.setText(total.toString())

            }

        }else if(month =="6 Months"){

            if(discount.trim().isEmpty()){
                discount = "0"
            }
            if(SixMonths!!.trim().isNotEmpty()){
                val discountAmount = ((sixMonths!!.toDouble() * discount.toDouble())/100) //finding out thediscount amount
                val total = sixMonths!!.toDouble() - discountAmount  // Minus discount amount from main amount
                edtAmt.setText(total.toString())

            }

        }else if(month =="1 Year"){

            if(discount.trim().isEmpty()){
                discount = "0"
            }
            if(oneYear!!.trim().isNotEmpty()){
                val discountAmount = ((oneYear!!.toDouble() * discount.toDouble())/100) //finding out thediscount amount
                val total = oneYear!!.toDouble() - discountAmount  // Minus discount amount from main amount
                edtAmt.setText(total.toString())

            }

        }else if(month =="3 Years"){

            if(discount.trim().isEmpty()){
                discount = "0"
            }
            if(threeYears!!.trim().isNotEmpty()){
                val discountAmount = ((threeYears!!.toDouble() * discount.toDouble())/100) //finding out thediscount amount
                val total = threeYears!!.toDouble() - discountAmount  // Minus discount amount from main amount
                edtAmt.setText(total.toString())

            }

        }
    }


    private fun calculateExpireDate(month: Int, edtExpiry: EditText) {
        val dtStart = binding.edtJoining.text.toString().trim()
        if (dtStart.isNotEmpty()) {
            val format = SimpleDateFormat("dd/MM/yyyy", Locale.US)
            val date1 = format.parse(dtStart)
            val cal = Calendar.getInstance()
            cal.time = date1
            cal.add(Calendar.MONTH, month)

            val myFormat = "dd/MM/yyyy"
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            edtExpiry.setText(sdf.format(cal.time))
        }
    }

    private fun showToast(value: String) {
        Toast.makeText(activity, value, Toast.LENGTH_LONG).show()
    }

    private fun getImage(){
        val Items:Array<CharSequence>
        try {

            items = arrayOf("Take Photo", "Chosse Image", "Cancel")
            val builder = amdroid.app.AlertDialog.Builder(acticity)
            builder.setTitle("Select Image")
            builder.setItems(itens),DialogInterface.OnClickListener{dialogInterface. i ->

                if (items[i] == "Take Photo"){
                }


            }

        }catch (e:Exception){
            e.printStackTrace
        }
    }
}
