package com.example.myapplication.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.model.AllMember

class AdapterLoadMember(private val arrayList: ArrayList<AllMember>) : RecyclerView.Adapter<AdapterLoadMember.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtAdapterName: TextView = view.findViewById(R.id.txtAdapterName)
        val txtAdapterAge: TextView = view.findViewById(R.id.txtAdapterAge)
        val txtAdapterWeight: TextView = view.findViewById(R.id.txtAdapterWeight)
        val txtAdapterMobile: TextView = view.findViewById(R.id.txtAdapterMobile)
        val txtAdapterAddress: TextView = view.findViewById(R.id.txtAdapterAddress)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = LayoutInflater.from(parent.context).inflate(R.layout.all_member_list_res, parent, false)
        return MyViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        with(holder) {
            with(arrayList[position]) {
                txtAdapterName.text = "$firstName $lastName"
                txtAdapterAge.text = "Age: $age"
                txtAdapterWeight.text = "Weight: $weight"
                txtAdapterMobile.text = "Mobile: $mobile"
                txtAdapterAddress.text = address

                if (image.isNotEmpty()) {
                    Glide.with(itemView.context)
                        .load(image)
                        .into(itemView.findViewById(R.id.imgAdapterPic))
                }

                if (gender == "Homem") {
                    Glide.with(itemView.context)
                        .load(R.drawable.boy)
                        .into(itemView.findViewById(R.id.imgAdapterPic))
                } else {
                    Glide.with(itemView.context)
                        .load(R.drawable.girl)
                        .into(itemView.findViewById(R.id.imgAdapterPic))
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }
}