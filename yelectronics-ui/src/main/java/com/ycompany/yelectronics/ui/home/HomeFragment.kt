package com.ycompany.yelectronics.ui.home

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ycompany.yelectronics.injections.CustomViewModelFactory
import com.ycompany.yelectronics.network.dto.Product
import com.ycompany.yelectronics.ui.R
import com.ycompany.yelectronics.ui.base.BaseFragment
import com.ycompany.yelectronics.ui.databinding.HomeFragmentBinding
import com.ycompany.yelectronics.utils.CirclePagerIndicatorDecoration
import com.ycompany.yelectronics.utils.Extensions
import com.ycompany.yelectronics.utils.ProgressLoadingDialog
import com.ycompany.yelectronics.viewmodel.StateData
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeFragmentBinding>(), OnProductClickListener {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    @Inject
    lateinit var customViewModelFactory: CustomViewModelFactory

    private lateinit var homeViewModel: HomeViewModel

    val progressLoadingDialog: ProgressLoadingDialog by lazy {
        ProgressLoadingDialog(
            requireActivity()
        )
    }

    override fun onCreateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) {
        binding = HomeFragmentBinding.inflate(inflater, container, false)
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel = ViewModelProvider(this, customViewModelFactory)[HomeViewModel::class.java]

        binding?.apply {
            highlightRecyclerView.layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL, false
            )
            highlightRecyclerView.setHasFixedSize(true)
            highlightRecyclerView.setScrollIndicators(View.SCROLL_INDICATOR_BOTTOM)
        }
        homeViewModel.productHighlightObservable().observe(
            viewLifecycleOwner, processProductHighlightData()
        )

        homeViewModel.getProductHighlightList(requireContext())
    }

    private fun processProductHighlightData(): (t: StateData<List<Product>>?) -> Unit = {
        if (it != null) {
            when (it.getStatus()) {
                StateData.DataStatus.LOADING -> {
                    progressLoadingDialog.startLoadingDialog("Please wait!!")
                }

                StateData.DataStatus.SUCCESS -> {
                    progressLoadingDialog.dismissDialog()
                    setProductHighlightsRecyclerView(it.getData())
                    activity?.applicationContext?.let { context ->
                        Extensions.toast(
                            "Highlight Product List Fetched successfully",
                            context
                        )
                    }
                }

                StateData.DataStatus.ERROR -> {
                    progressLoadingDialog.dismissDialog()
                    activity?.applicationContext?.let { context ->
                        Extensions.toast(
                            it.getError()?.message.toString(),
                            context
                        )
                    }
                }

                else -> {
                    progressLoadingDialog.dismissDialog()
                    activity?.applicationContext?.let { Extensions.toast("Sign in failed", it) }
                }
            }
        }
    }

    private fun setProductHighlightsRecyclerView(data: List<Product>?) {
        data?.let {
            val productHighlightAdapter = ProductHighlightAdapter(this, data)
            binding?.highlightRecyclerView?.adapter = productHighlightAdapter
            binding?.highlightRecyclerView?.addItemDecoration(
                CirclePagerIndicatorDecoration(ContextCompat.getColor(requireContext(), R.color.primary))
                )
        }
    }

    private fun setProductsRecyclerView(data: List<Product>?) {
        data?.let {
            val productsAdapter = ProductsAdapter(this, data)
            binding?.newProductsRecyclerView?.adapter = productsAdapter
            binding?.newProductsRecyclerView?.addItemDecoration(
                CirclePagerIndicatorDecoration(ContextCompat.getColor(requireContext(), R.color.primary))
            )
        }
    }

    companion object {
        fun getInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    override fun onProductClick(productHighlight: Product) {
        activity?.applicationContext?.let { context ->
            Extensions.toast(
                productHighlight.productName,
                context
            )
        }

    }
}