package com.techplus.gymmanagement.fragment

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.FragmentAddMemberBinding
import com.example.myapplication.global.DB
import com.example.myapplication.global.MyFunction
import com.techplus.mypracticeapp.activity.CaptureImage
import java.text.SimpleDateFormat
import java.util.*

class FragmentAddMember : Fragment() {

    private var db: DB? = null
    private var oneMonth: String? = ""
    private var threeMonths: String? = ""
    private var sixMonths: String? = ""
    private var oneYear: String? = ""
    private var threeYear: String? = ""
    private lateinit var binding: FragmentAddMemberBinding
    private var captureImage:CaptureImage?=null
    private val REQUEST_CAMERA =1234
    private val REQUEST_GALLERY = 5464
    private var actualImagePath = ""
    private var gender = "Male"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddMemberBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = activity?.let { DB(it) }
        captureImage = CaptureImage(activity)

        val cal = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.US)
            binding.edtJoining.setText(sdf.format(cal.time))
        }

        binding.imgPicDate.setOnClickListener {
            activity?.let {
                DatePickerDialog(it, dateSetListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
            }
        }

        binding.spMembership.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val value = parent.getItemAtPosition(position).toString().trim()
                if (value == "Select") {
                    binding.edtExpire.setText("")
                    calculateTotal(binding.spMembership, binding.edtDiscount, binding.edtAmount)
                } else {
                    if (binding.edtJoining.text.toString().trim().isNotEmpty()) {
                        when (value) {
                            "1 Month" -> {
                                calculateExpireDate(1, binding.edtExpire)
                                calculateTotal(binding.spMembership, binding.edtDiscount, binding.edtAmount)
                            }
                            "3 Months" -> {
                                calculateExpireDate(3, binding.edtExpire)
                                calculateTotal(binding.spMembership, binding.edtDiscount, binding.edtAmount)
                            }
                            "6 Months" -> {
                                calculateExpireDate(6, binding.edtExpire)
                                calculateTotal(binding.spMembership, binding.edtDiscount, binding.edtAmount)
                            }
                            "1 Year" -> {
                                calculateExpireDate(12, binding.edtExpire)
                                calculateTotal(binding.spMembership, binding.edtDiscount, binding.edtAmount)
                            }
                            "3 Years" -> {
                                calculateExpireDate(36, binding.edtExpire)
                                calculateTotal(binding.spMembership, binding.edtDiscount, binding.edtAmount)
                            }
                        }
                    } else {
                        showToast("Select Joining date first")
                        binding.spMembership.setSelection(0)
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // No action needed
            }
        }

        binding.edtDiscount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                calculateTotal(binding.spMembership, binding.edtDiscount, binding.edtAmount)
            }
        })

        binding.radioGroup.setOnClickListener{radioGroup,i ->
            when (id){
                R.id.rdMale ->{
                    gender = "Malw"

                }
                R.id.rdFeMale ->{

            }
        }

        binding.imgTakeImage.setOnClickListener {
            getImage()
        }

        getFee()
    }

    private fun getFee() {
        try {
            val sqlQuery = "SELECT * FROM FEE WHERE ID = '1'"
            db?.fireQuery(sqlQuery)?.use {
                oneMonth = MyFunction.getValue(it, "ONE_MONTH")
                threeMonths = MyFunction.getValue(it, "THREE_MONTH")
                sixMonths = MyFunction.getValue(it, "SIX_MONTH")
                oneYear = MyFunction.getValue(it, "ONE_YEAR")
                threeYear = MyFunction.getValue(it, "THREE_YEAR")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun calculateTotal(spMember: Spinner, edtDis: EditText, edtAmt: EditText) {
        val month = spMember.selectedItem.toString().trim()
        var discount = edtDis.text.toString().trim().ifEmpty { "0" }
        val fee = when (month) {
            "1 Month" -> oneMonth
            "3 Months" -> threeMonths
            "6 Months" -> sixMonths
            "1 Year" -> oneYear
            "3 Years" -> threeYear
            else -> null
        }

        if (fee != null && fee.isNotEmpty()) {
            val discountAmount = (fee.toDouble() * discount.toDouble()) / 100
            val total = fee.toDouble() - discountAmount
            edtAmt.setText(total.toString())
        } else {
            edtAmt.setText("")
        }
    }

    private fun calculateExpireDate(month: Int, edtExpiry: EditText) {
        val dtStart = binding.edtJoining.text.toString().trim()
        if (dtStart.isNotEmpty()) {
            val format = SimpleDateFormat("dd/MM/yyyy", Locale.US)
            val date = format.parse(dtStart)
            val cal = Calendar.getInstance().apply { time = date }
            cal.add(Calendar.MONTH, month)
            edtExpiry.setText(SimpleDateFormat("dd/MM/yyyy", Locale.US).format(cal.time))
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }

    private fun getImage() {
        val items = arrayOf("Take Photo", "Choose Image", "Cancel")

        try {
            val builder = android.app.AlertDialog.Builder(activity)
            builder.setTitle("Select Image")
            builder.setItems(items) { dialogInterface, i ->
                when (items[i]) {
                    "Take Photo" -> {
                        RuntimePermission.askPermission(this)
                            .request(Manifest.permission.CAMERA)
                            .onAccepted {
                                takePicture()
                                // Capture image logic
                            }
                            .onDenied {
                                showPermissionAlert()
                            }
                    }
                    "Choose Image" -> {
                        RuntimePermission.askPermission(this)
                            .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            .onAccepted {
                                takeFromGallery()
                                // Choose image logic
                            }
                            .onDenied {
                                showPermissionAlert()
                            }
                    }
                    "Cancel" -> dialogInterface.dismiss()
                }
            }
            builder.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showPermissionAlert() {
        android.app.AlertDialog.Builder(activity)
            .setMessage("Please accept our permission to proceed")
            .setPositiveButton("Yes") { _, _ ->
                // Handle re-request of permission if necessary
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun takePicture(){
        val takePicIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        takePicIntent.putExtra(MediaStore.EXTRA_OUTPUT,captureImage?.setimgUri())
        takePicIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivityForResult(takePicIntent,REQUEST_CAMERA)
    }

    private fun takeFromGallery(){
        val intent = Intent()
        intent.type = "image/jpg"
        intent.action = Intent.ACTION_PICK
        startActivityForResult(intent,REQUEST_GALLERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK){
            captureImage(captureImage?.getRightAngleImage(captureImage?.imagePath).toString())

        }else if(resultCode == REQUEST_GALLERY && resultCode == Activity.RESULT_OK){
            captureImage(captureImage?.getRightAngleImage(captureImage?.getPath(data?.data,context)).toString())

        }
    }

    private fun captureImage(path:String){
        Log.d("FragmentAdd","imagePath : $path")
        getImagePath(captureImage?.decodeFile(Path))
    }

    private fun getImagePath(bitmap:Bitmap?){
        val tempUri:Uri? = captureImage?.getImageUri(activity,bitmap)
        actualImagePath = captureImage?.getRealPathFromURI(tempUri,activity).toString()
        Log.d("FragmentAdd","ActualImagePath : $ActualImagePath")

        activity?.let {
            Glide.with(it)
            .load(actualImagePath)
            .into(binding.imgPic)
        }
    }
}

