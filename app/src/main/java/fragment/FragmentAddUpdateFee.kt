package fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapplication.databinding.FragmentAddUpdateFeeBinding


class FragmentAddUpdateFee : Fragment() {

    private lateinit var binding: FragmentAddUpdateFeeBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddUpdateFeeBinding.inflate(inflater,container,false)
        return binding.root
    }
}