package com.example.seknet

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment


class FragmentHome : Fragment(R.layout.fragment_home) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity?.title = "HOME";
    }
}
