package com.ycompany.yelectronics.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.ycompany.yelectronics.injections.CustomViewModelFactory
import com.ycompany.yelectronics.network.dto.Product
import com.ycompany.yelectronics.ui.R
import com.ycompany.yelectronics.ui.base.BaseFragment
import com.ycompany.yelectronics.ui.databinding.FavoriteFragmentBinding
import com.ycompany.yelectronics.ui.home.OnProductClickListener
import com.ycompany.yelectronics.ui.home.ProductsAdapter
import com.ycompany.yelectronics.ui.productdetails.ProductDetailsFragment
import com.ycompany.yelectronics.utils.Extensions
import com.ycompany.yelectronics.utils.ProgressLoadingDialog
import com.ycompany.yelectronics.viewmodel.StateData
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FavoriteFragment : BaseFragment<FavoriteFragmentBinding>(), OnProductClickListener {
    private lateinit var favoritesViewModel: FavoritesViewModel

    @Inject
    lateinit var customViewModelFactory: CustomViewModelFactory

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
        binding = FavoriteFragmentBinding.inflate(inflater, container, false)
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoritesViewModel =
            ViewModelProvider(this, customViewModelFactory)[FavoritesViewModel::class.java]

        binding?.apply {
            favoritesRecyclerView.layoutManager =
                GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false)
            favoritesRecyclerView.setHasFixedSize(true)
            favoritesRecyclerView.isNestedScrollingEnabled = false
        }

        favoritesViewModel.favoritesListObservable().observe(
            viewLifecycleOwner, processFavoriteListData()
        )

        favoritesViewModel.getListOfFavorites()
    }

    private fun processFavoriteListData(): (t: StateData<List<Product>>?) -> Unit = {
        if (it != null) {
            when (it.getStatus()) {
                StateData.DataStatus.LOADING -> {
                    progressLoadingDialog.startLoadingDialog("Please wait!!")
                }

                StateData.DataStatus.SUCCESS -> {
                    progressLoadingDialog.dismissDialog()
                    setFavoriteProductsRecyclerView(it.getData())
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
                }
            }
        }
    }

    private fun setFavoriteProductsRecyclerView(data: List<Product>?) {
        data?.let {
            val productsAdapter = ProductsAdapter(this, data)
            binding?.favoritesRecyclerView?.adapter = productsAdapter
        }
    }

    override fun onProductClick(product: Product) {
        activity?.applicationContext?.let {
            val args = Bundle()
            args.putParcelable(ProductDetailsFragment.KEY_PRODUCT, product)
            findNavController().navigate(R.id.action_favoriteFragment_productDetails, args)
        }
    }
}