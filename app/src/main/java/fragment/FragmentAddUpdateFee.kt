package fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.FragmentAddUpdateFeeBinding
import com.example.myapplication.global.DB
import com.example.myapplication.global.MyFunction

class FragmentAddUpdateFee : Fragment() {

    private lateinit var binding: FragmentAddUpdateFeeBinding
    var db: DB? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddUpdateFeeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = activity?.let { DB(it) }

        binding.btnAddMembership.setOnClickListener {
            if (validate()) {
                saveData()
            }
        }
        fillData()
    }

    private fun validate(): Boolean {
        return when {
            binding.edtOneMonth.text.toString().trim().isEmpty() -> {
                showToast("Enter one month fee")
                return false
            }
            binding.edtThreeMonth.text.toString().trim().isEmpty() -> {
                showToast("Enter three months fee")
                return false
            }
            binding.edtSixMonth.text.toString().trim().isEmpty() -> {
                showToast("Enter six months fee")
                return false
            }
            binding.edtOneYear.text.toString().trim().isEmpty() -> {
                showToast("Enter one year fee")
                return false
            }
            binding.edtThreeYear.text.toString().trim().isEmpty() -> {
                showToast("Enter three years fee")
                return false
            }
            else -> true
        }
    }

    private fun saveData() {
        val sqlQuery = "INSERT OR REPLACE INTO FEE(ID, ONE_MONTH, THREE_MONTH, SIX_MONTH, ONE_YEAR, THREE_YEAR) VALUES(" +
                "1, '${binding.edtOneMonth.text.toString().trim()}', '${binding.edtThreeMonth.text.toString().trim()}', " +
                "'${binding.edtSixMonth.text.toString().trim()}', '${binding.edtOneYear.text.toString().trim()}', " +
                "'${binding.edtThreeYear.text.toString().trim()}')"

        try {
            db?.executeQuery(sqlQuery)
            showToast("Membership data saved successfully")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun fillData() {
        val sqlQuery = "SELECT * FROM FEE WHERE ID = 1"
        try {
            db?.fireQuery(sqlQuery)?.use { cursor ->
                if (cursor.count > 0) {
                    cursor.moveToFirst()
                    binding.edtOneMonth.setText(MyFunction.getValue(cursor, "ONE_MONTH"))
                    binding.edtThreeMonth.setText(MyFunction.getValue(cursor, "THREE_MONTH"))
                    binding.edtSixMonth.setText(MyFunction.getValue(cursor, "SIX_MONTH"))
                    binding.edtOneYear.setText(MyFunction.getValue(cursor, "ONE_YEAR"))
                    binding.edtThreeYear.setText(MyFunction.getValue(cursor, "THREE_YEAR"))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showToast(value: String) {
        Toast.makeText(activity, value, Toast.LENGTH_LONG).show()
    }
}
