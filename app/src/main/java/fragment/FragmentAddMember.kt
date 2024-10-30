package fragment

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.database.DatabaseUtils
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentAddMemberBinding
import com.example.myapplication.global.CaptureImage
import com.example.myapplication.global.DB
import com.example.myapplication.global.MyFunction
import java.text.SimpleDateFormat
import java.util.*

class FragmentAddMember : Fragment() {

    private var db: DB? = null
    private lateinit var binding: FragmentAddMemberBinding
    private var captureImage: CaptureImage? = null
    private val REQUEST_CAMERA_PERMISSION = 1234
    private val REQUEST_CAMERA = 1234
    private val REQUEST_GALLERY = 5464
    private var actualImagePath = ""
    private var gender = "Male"
    private var oneMonth: String? = ""
    private var threeMonths: String? = ""
    private var sixMonths: String? = ""
    private var oneYear: String? = ""
    private var threeYear: String? = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddMemberBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
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
                    // Remover qualquer lógica relacionada ao "Expire On"
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
                // Remover lógica de cálculo de total
            }
        })

        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rdMale -> {
                    gender = "Male"
                }
                R.id.rdFemale -> {
                    gender = "Female"
                }
            }
        }

        binding.imgTakeImage.setOnClickListener {
            getImage() // Abre o diálogo para tirar foto ou escolher da galeria
        }

        binding.btnAddMemberSave.setOnClickListener { // Botão para salvar os dados
            if (validate()) {
                saveData()
            }
        }

        getFee()
    }

    private fun requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
        } else {
            takePicture()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_GALLERY) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takeFromGallery() // Chama a galeria se a permissão foi concedida
            } else {
                showPermissionAlert() // Alerta se a permissão não foi concedida
            }
        }
    }

    private fun takePicture() {
        val takePicIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        takePicIntent.putExtra(MediaStore.EXTRA_OUTPUT, captureImage?.setImageUri())
        takePicIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivityForResult(takePicIntent, REQUEST_CAMERA)
    }

    private fun getFee() {
        try {
            val sqlQuery = "SELECT * FROM FEE WHERE ID = '1'"
            db?.fireQuery(sqlQuery)?.use { cursor ->
                if (cursor.count > 0) {
                    cursor.moveToFirst()
                    oneMonth = MyFunction.getValue(cursor, "ONE_MONTH")
                    threeMonths = MyFunction.getValue(cursor, "THREE_MONTH")
                    sixMonths = MyFunction.getValue(cursor, "SIX_MONTH")
                    oneYear = MyFunction.getValue(cursor, "ONE_YEAR")
                    threeYear = MyFunction.getValue(cursor, "THREE_YEAR")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun showToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun getImage() {
        val items = arrayOf("Take Photo", "Choose Image", "Cancel")

        try {
            val builder = android.app.AlertDialog.Builder(activity)
            builder.setTitle("Select Image")
            builder.setItems(items) { dialogInterface, i ->
                when (items[i]) {
                    "Take Photo" -> {
                        requestCameraPermission()
                    }
                    "Choose Image" -> {
                        requestGalleryPermission() // Implemente esta função para solicitar permissão para acessar a galeria
                    }
                    "Cancel" -> dialogInterface.dismiss()
                }
            }
            builder.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestGalleryPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_MEDIA_IMAGES), REQUEST_GALLERY)
        } else {
            takeFromGallery()
        }
    }

    private fun takeFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_GALLERY)
    }


    private fun showPermissionAlert() {
        android.app.AlertDialog.Builder(activity)
            .setMessage("Please accept our permission to proceed")
            .setPositiveButton("Yes") { _, _ -> }
            .setNegativeButton("No") { dialogInterface, _ -> dialogInterface.dismiss() }
            .show()
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK) {
            captureImage(captureImage?.getRightAngleImage(captureImage?.getCurrentImagePath()).toString())
        } else if (requestCode == REQUEST_GALLERY && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri = data.data
            if (selectedImageUri != null) {
                val imagePath = captureImage?.getRealPathFromURI(selectedImageUri, requireContext())
                captureImage(imagePath ?: "")
            } else {
                showToast("No image selected")
            }
        }
    }

    private fun captureImage(path: String) {
        Log.d("FragmentAdd", "imagePath : $path")
        getImagePath(captureImage?.decodeFile(path))
    }

    private fun getImagePath(bitmap: Bitmap?) {
        val tempUri: Uri? = captureImage?.getImageUri(activity, bitmap)
        actualImagePath = captureImage?.getRealPathFromURI(tempUri, activity).toString()
        Log.d("FragmentAdd", "ActualImagePath : $actualImagePath")

        activity?.let {
            Glide.with(it)
                .load(actualImagePath)
                .into(binding.imgPic)
        }
    }

    private fun validate(): Boolean {
        if (binding.edtFirstName.text.toString().trim().isEmpty()) {
            showToast("Enter first name")
            return false
        } else if (binding.edtLastName.text.toString().trim().isEmpty()) {
            showToast("Enter last name")
            return false
        } else if (binding.edtAge.text.toString().trim().isEmpty()) {
            showToast("Enter age")
            return false
        } else if (binding.edtMobile.text.toString().trim().isEmpty()) {
            showToast("Enter mobile")
            return false
        }
        return true
    }

    private fun saveData() {
        try {
            val sqlQuery = "INSERT OR REPLACE INTO MEMBER (ID, FIRST_NAME, LAST_NAME, GENDER, AGE, " +
                    "WEIGHT, MOBILE, ADDRESS, DATE_OF_JOINING, MEMBERSHIP, IMAGE_PATH, STATUS) VALUES " +
                    "(${getIncrementedId()}, " +
                    "${DatabaseUtils.sqlEscapeString(binding.edtFirstName.text.toString().trim())}, " +
                    "${DatabaseUtils.sqlEscapeString(binding.edtLastName.text.toString().trim())}, " +
                    "'$gender', " +
                    "${binding.edtAge.text.toString().trim()}, " +
                    "${binding.edtWeight.text.toString().trim()}, " +
                    "${DatabaseUtils.sqlEscapeString(binding.edtMobile.text.toString().trim())}, " +
                    "${DatabaseUtils.sqlEscapeString(binding.edtAddress.text.toString().trim())}, " +
                    "${MyFunction.returnSQLDateFormat(binding.edtJoining.text.toString().trim())}, " +
                    "'${binding.spMembership.selectedItem.toString().trim()}', " +
                    "'$actualImagePath', 'A')"

            db?.executeQuery(sqlQuery)
            showToast("Data saved successfully")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getIncrementedId(): String {
        var incrementId = ""
        try {
            val sqlQuery = "SELECT IFNULL(MAX(ID)+1, '1') AS ID FROM MEMBER"
            db?.fireQuery(sqlQuery)?.use { cursor ->
                if (cursor.count > 0) {
                    incrementId = MyFunction.getValue(cursor, columnName = "ID")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return incrementId
    }

}
