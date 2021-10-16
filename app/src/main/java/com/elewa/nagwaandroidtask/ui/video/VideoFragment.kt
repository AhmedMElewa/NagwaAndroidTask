package com.elewa.nagwaandroidtask.ui.video

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
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
import com.elewa.nagwaandroidtask.util.LoadingView
import com.elewa.nagwaandroidtask.util.Status
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

import com.elewa.nagwaandroidtask.util.service.ForegroundService


@AndroidEntryPoint
class VideoFragment : Fragment(), VideoAdapter.OnVideoAdapterClickListener {

    private var _binding: FragmentVideoBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: VideoAdapter

    private val viewmodel: VideoViewModel by viewModels()

    @Inject
    lateinit var loadingView: LoadingView




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
    }

    private fun observer() {

//        viewmodel.videos.observe(viewLifecycleOwner, {
//            when (it.status) {
//                Status.SUCCESS -> {
//                    it.data?.let { videoData ->
//                        adapter.apply {
//                            adapter.submitList(videoData)
//                            Toast.makeText(requireContext(), "Done", Toast.LENGTH_LONG).show()
//                        }
//                    }
//                    binding.recyclerVideos.visibility = View.VISIBLE
//                    loadingView.dismissLoading()
//                }
//                Status.LOADING -> {
//                    loadingView.showLoading()
//                    binding.recyclerVideos.visibility = View.GONE
//                }
//                Status.ERROR -> {
//                    //Handle Error
//                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
//                    loadingView.dismissLoading()
//                }
//                Status.INTERNET -> {
//                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
//                    loadingView.dismissLoading()
//                }
//            }
//        })


        viewmodel.offlineVideos.observe(viewLifecycleOwner,{
            when (it.status) {
                Status.SUCCESS -> {
                    if (it.data.isNullOrEmpty()){
                        Toast.makeText(requireContext(), "Check your internet Connection!", Toast.LENGTH_SHORT).show()
                        loadingView.dismissLoading()
                    }else{
                        it.data?.let { videoData ->
                            adapter.apply {
                                adapter.submitList(videoData)
                            }
                        }
                        binding.recyclerVideos.visibility = View.VISIBLE
                        loadingView.dismissLoading()
                    }
                }
                Status.LOADING -> {
                    loadingView.showLoading()
                    binding.recyclerVideos.visibility = View.GONE
                }
                Status.ERROR -> {
                    //Handle Error
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                    loadingView.dismissLoading()
                }
                Status.INTERNET -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                    loadingView.dismissLoading()
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
                    ForegroundService.startService(requireContext(), item.name+" is donloading...",item.id)

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