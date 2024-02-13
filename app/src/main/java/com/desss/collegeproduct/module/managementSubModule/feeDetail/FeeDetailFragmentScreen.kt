package com.desss.collegeproduct.module.managementSubModule.feeDetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.desss.collegeproduct.R
import com.desss.collegeproduct.databinding.FragmentFeeDetailBinding


class FeeDetailFragmentScreen : Fragment() {

    private lateinit var fragmentFeeDetailBinding: FragmentFeeDetailBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentFeeDetailBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_fee_detail, container, false)

        return fragmentFeeDetailBinding.root
    }

}