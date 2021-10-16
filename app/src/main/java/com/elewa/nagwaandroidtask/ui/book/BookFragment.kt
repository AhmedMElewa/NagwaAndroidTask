package com.elewa.nagwaandroidtask.ui.book

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
import com.elewa.nagwaandroidtask.databinding.FragmentBookBinding
import com.elewa.nagwaandroidtask.util.Constants
import com.elewa.nagwaandroidtask.util.Status
import com.elewa.nagwaandroidtask.util.service.ForegroundService
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookFragment : Fragment(), BookAdapter.OnBookClickListener {

    private var _binding: FragmentBookBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: BookAdapter

    private val viewmodel: BookViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBookBinding.inflate(inflater)

        init()
        observer()

        return binding.root
    }

    private fun init() {
        adapter = BookAdapter(this)
        binding.recyclerBook.adapter = adapter
        // SwipeRefresh
        binding.swipeRefresh.setProgressBackgroundColorSchemeColor(Color.parseColor("#FF313131"))
        binding.swipeRefresh.setColorSchemeColors(Color.parseColor("#ffeb3b"))
        binding.swipeRefresh.setOnRefreshListener {
            viewmodel.fetchBooks()
        }
    }

    private fun observer() {


        viewmodel.offlineBooks.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {
                    if (it.data.isNullOrEmpty()) {
                        Toast.makeText(
                            requireContext(),
                            "No Internet Connection",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        adapter.apply {
                            adapter.submitList(it.data)
                            binding.recyclerBook.visibility = View.VISIBLE
                        }
                    }
                    binding.swipeRefresh.isRefreshing = false
                }
                Status.LOADING -> {
                    binding.swipeRefresh.isRefreshing = true
                    binding.recyclerBook.visibility = View.GONE
                }
                Status.ERROR -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    binding.swipeRefresh.isRefreshing = false
                }
                Status.INTERNET -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    binding.swipeRefresh.isRefreshing = false
                }
            }
        })

    }

    override fun onDownloadClick(book: ItemModel) {
        if (Constants.verifyAvailableNetwork(requireActivity())) {
            if (Constants.DOWNLOADFLAG) {
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

                txtTitle.text = "Are you sure to download " + book.name

                txtNo.setOnClickListener {
                    myDialog.dismiss()
                }

                txtYes.setOnClickListener {

                    //Fire service to download
                    ForegroundService.startService(
                        requireContext(),
                        book.name + " is donloading...",
                        book.id
                    )

                    myDialog.dismiss()

                }
            }
        } else {
            Toast.makeText(requireContext(), "Check your internet Connection!", Toast.LENGTH_SHORT)
                .show()
        }
    }


}