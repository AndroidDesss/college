package com.desss.collegeproduct.module.studentSubModule.transport

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.desss.collegeproduct.R
import com.desss.collegeproduct.databinding.FragmentTransportBinding


class TransportFragment : Fragment() {

    private lateinit var fragmentTransportFragmentBinding: FragmentTransportBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentTransportFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_transport, container, false)
        return fragmentTransportFragmentBinding.root
    }

}