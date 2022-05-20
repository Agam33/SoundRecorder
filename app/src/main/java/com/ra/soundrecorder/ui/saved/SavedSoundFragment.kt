package com.ra.soundrecorder.ui.saved

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.ra.soundrecorder.App
import com.ra.soundrecorder.R
import com.ra.soundrecorder.adapter.SavedSoundAdapter
import com.ra.soundrecorder.databinding.FragmentSavedSoundBinding

class SavedSoundFragment : Fragment() {

    private var _binding: FragmentSavedSoundBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity() as App).mainComponent.inject(this)
        setupRecyclerView()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSavedSoundBinding.inflate(inflater, container, false)
        return binding?.root
    }

    private fun setupRecyclerView() {
        binding?.let {
            it.rvSoundList.apply {
                LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = SavedSoundAdapter(
                    ArrayList() // TODO: Empty list
                ) {

                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}