package fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.RetrofitClient
import com.example.myapplication.databinding.FragmentAllMemberBinding
import com.example.myapplication.global.DB
import com.example.myapplication.adapter.AdapterLoadMember
import com.example.myapplication.global.MyFunction
import com.example.myapplication.model.AllMember
import com.example.myapplication.model.RandomUserResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentAllMember : Fragment() {
    private var db: DB? = null
    private lateinit var binding: FragmentAllMemberBinding
    private lateinit var adapter: AdapterLoadMember
    private var arrayList: ArrayList<AllMember> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentAllMemberBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = activity?.let { DB(it) }

        binding.recyclerViewMember.layoutManager = LinearLayoutManager(context)
        adapter = AdapterLoadMember(arrayList)
        binding.recyclerViewMember.adapter = adapter

        loadData("A")
    }

    private fun loadData(memberStatus: String) {
        lifecycleScope.launch {
            val sqlQuery = "SELECT * FROM MEMBER WHERE STATUS= '$memberStatus'"
            db?.fireQuery(sqlQuery)?.use { cursor ->
                if (cursor.count > 0) {
                    cursor.moveToFirst()
                    do {
                        val member = AllMember(
                            id = MyFunction.getValue(cursor, "ID"),
                            firstName = MyFunction.getValue(cursor, "FIRST_NAME"),
                            lastName = MyFunction.getValue(cursor, "LAST_NAME"),
                            age = MyFunction.getValue(cursor, "AGE"),
                            gender = MyFunction.getValue(cursor, "GENDER"),
                            weight = MyFunction.getValue(cursor, "WEIGHT"),
                            mobile = MyFunction.getValue(cursor, "MOBILE"),
                            address = MyFunction.getValue(cursor, "ADDRESS"),
                            dateOfJoining = MyFunction.getValue(cursor, "DATE_OF_JOINING"),
                            expiryDate = MyFunction.getValue(cursor, "EXPIRE_ON"),
                            image = MyFunction.getValue(cursor, "IMAGE_PATH")
                        )
                        arrayList.add(member)
                    } while (cursor.moveToNext())
                }
            }

            RetrofitClient.apiService.getUsers().enqueue(object : Callback<RandomUserResponse> {
                override fun onResponse(call: Call<RandomUserResponse>, response: Response<RandomUserResponse>) {
                    if (response.isSuccessful) {
                        response.body()?.results?.let { users ->
                            arrayList.clear()
                            users.forEach { user ->
                                val member = AllMember(
                                    id = user.login.uuid,
                                    firstName = user.name.first,
                                    lastName = user.name.last,
                                    age = user.dob.date, // Aqui você pode formatar a data conforme necessário
                                    gender = user.gender,
                                    weight = "N/A", // Se não houver peso na API, você pode definir um valor padrão
                                    mobile = user.phone,
                                    address = "${user.location.city}, ${user.location.state}, ${user.location.country}",
                                    image = user.picture.large,
                                    dateOfJoining = user.dob.date, // Novamente, formate conforme necessário
                                    expiryDate = "N/A" // Defina um valor padrão
                                )
                                arrayList.add(member)
                            }
                            adapter.notifyDataSetChanged()
                        }
                    }
                }

                override fun onFailure(call: Call<RandomUserResponse>, t: Throwable) {
                    // Tratamento de falha na chamada da API
                }
            })
        }
    }
}
