package com.elewa.nagwaandroidtask.ui.video

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.elewa.nagwaandroidtask.R
import com.elewa.nagwaandroidtask.data.model.ItemModel
import com.elewa.nagwaandroidtask.databinding.FragmentVideoBinding
import com.elewa.nagwaandroidtask.util.Constants
import com.elewa.nagwaandroidtask.util.Constants.DOWNLOADFLAG
import com.elewa.nagwaandroidtask.util.Status
import dagger.hilt.android.AndroidEntryPoint

import com.elewa.nagwaandroidtask.util.service.ForegroundService


@AndroidEntryPoint
class VideoFragment : Fragment(), VideoAdapter.OnVideoAdapterClickListener {

    private var _binding: FragmentVideoBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: VideoAdapter

    private val viewmodel: VideoViewModel by viewModels()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVideoBinding.inflate(inflater)

        init()
        observer()


        return binding.root
    }

    private fun init() {
        adapter = VideoAdapter(this)
        binding.recyclerVideos.adapter = adapter

        // SwipeRefresh
        binding.swipeRefresh.setProgressBackgroundColorSchemeColor(Color.parseColor("#FF313131"))
        binding.swipeRefresh.setColorSchemeColors(Color.parseColor("#ffeb3b"))
        binding.swipeRefresh.setOnRefreshListener {
            viewmodel.fetchVideos()
        }
    }

    private fun observer() {


        viewmodel.offlineVideos.observe(viewLifecycleOwner,{
            when (it.status) {
                Status.SUCCESS -> {
                    if (it.data.isNullOrEmpty()){
                        Toast.makeText(requireContext(), "No Internet Connection", Toast.LENGTH_LONG).show()
                    }else{
                        adapter.apply {
                            adapter.submitList(it.data)
                            binding.recyclerVideos.visibility = View.VISIBLE
                        }
                    }
                    binding.swipeRefresh.isRefreshing = false
                }
                Status.LOADING -> {
                    binding.swipeRefresh.isRefreshing = true
                    binding.recyclerVideos.visibility = View.GONE
                }
                Status.ERROR -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                    binding.swipeRefresh.isRefreshing = false
                }
                Status.INTERNET -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                    binding.swipeRefresh.isRefreshing = false
                }
            }
        })

    }

    override fun onVideoAdapterClick(item: ItemModel) {

    }

    override fun onDownloadClick(item: ItemModel) {

        if (Constants.verifyAvailableNetwork(requireActivity())){
            if (DOWNLOADFLAG) {
                Toast.makeText(
                    requireContext(),
                    "Wait till the last download finish",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val myDialog = Dialog(requireActivity())
                myDialog.setContentView(R.layout.popup_q)
                myDialog.setCancelable(false)
                myDialog.window
                    ?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                val txtTitle = myDialog.findViewById<TextView>(R.id.txtTitle)
                val txtYes = myDialog.findViewById<TextView>(R.id.txtYes)
                val txtNo = myDialog.findViewById<TextView>(R.id.txtNo)
                myDialog.show()

                txtTitle.text = "Are you sure to download " + item.name

                txtNo.setOnClickListener {
                    myDialog.dismiss()
                }

                txtYes.setOnClickListener {

                    //Fire service to download
                    ForegroundService.startService(requireContext(), item.name+" is downloading...",item.id)

                    myDialog.dismiss()

                }
            }
        }else{
            Toast.makeText(requireContext(), "Check your internet Connection!", Toast.LENGTH_SHORT).show()
        }


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}