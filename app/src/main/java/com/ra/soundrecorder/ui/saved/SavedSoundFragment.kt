package com.ra.soundrecorder.ui.saved

import android.content.Context
import android.os.Bundle
import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ra.soundrecorder.App
import com.ra.soundrecorder.R
import com.ra.soundrecorder.adapter.SavedSoundAdapter
import com.ra.soundrecorder.databinding.FragmentSavedSoundBinding
import com.ra.soundrecorder.model.SoundRecord
import com.ra.soundrecorder.ui.ViewModelFactory
import com.ra.soundrecorder.utils.DataDummy
import javax.inject.Inject

class SavedSoundFragment : Fragment() {

    @Inject
    lateinit var factory: ViewModelFactory

    private val viewModel: SavedSoundViewModel by viewModels { factory }

    private var _binding: FragmentSavedSoundBinding? = null
    private val binding get() = _binding

    private lateinit var savedSoundAdapter: SavedSoundAdapter


    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as App).mainComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSavedSoundBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(activity != null) {
            observer()
            setListSavedSound(DataDummy.getAllRecord())
        }
    }

    private fun observer() {
      //  viewModel.getAllRecord().observe(viewLifecycleOwner,::setListSavedSound)
    }

    private fun setListSavedSound(item: List<SoundRecord>) {
        savedSoundAdapter = SavedSoundAdapter(item) {
            Toast.makeText(context, "${it.name}", Toast.LENGTH_SHORT).show()
        }
        binding?.rvSoundList?.apply {
            adapter = savedSoundAdapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}