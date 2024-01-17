package com.desss.collegeproduct.module.commonDashBoardFragment.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.desss.collegeproduct.R
import com.desss.collegeproduct.databinding.FragmentProfileScreenBinding


class ProfileFragmentScreen : Fragment() {

    private lateinit var fragmentProfileFragmentScreen: FragmentProfileScreenBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View
    {
        fragmentProfileFragmentScreen = DataBindingUtil.inflate(inflater, R.layout.fragment_profile_screen, container, false)

        return fragmentProfileFragmentScreen.root
    }

}