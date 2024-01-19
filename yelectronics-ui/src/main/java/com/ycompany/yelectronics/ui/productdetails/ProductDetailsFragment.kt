package com.ycompany.yelectronics.ui.productdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ycompany.yelectronics.injections.CustomViewModelFactory
import com.ycompany.yelectronics.network.database.CartEntity
import com.ycompany.yelectronics.network.dto.Product
import com.ycompany.yelectronics.ui.R
import com.ycompany.yelectronics.ui.base.BaseFragment
import com.ycompany.yelectronics.ui.cart.CartViewModel
import com.ycompany.yelectronics.ui.databinding.FragmentProductDetailsBinding
import com.ycompany.yelectronics.ui.favorite.FavoritesViewModel
import com.ycompany.yelectronics.ui.home.OnFavoriteClickListener
import com.ycompany.yelectronics.utils.Extensions
import com.ycompany.yelectronics.viewmodel.StateData
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProductDetailsFragment : BaseFragment<FragmentProductDetailsBinding>(),
    OnFavoriteClickListener {

    private lateinit var favoritesViewModel: FavoritesViewModel
    private lateinit var cartViewModel: CartViewModel

    @Inject
    lateinit var customViewModelFactory: CustomViewModelFactory

    var quantitySelected: Int = 1

    override fun onCreateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) {
        binding = FragmentProductDetailsBinding.inflate(inflater, container, false)
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoritesViewModel =
            ViewModelProvider(this, customViewModelFactory)[FavoritesViewModel::class.java]

        cartViewModel =
            ViewModelProvider(this, customViewModelFactory)[CartViewModel::class.java]


        val bundle: Bundle = arguments as Bundle
        val product = bundle.getParcelable<Product>(KEY_PRODUCT)

        binding?.apply {
            Glide.with(requireContext())
                .load(product?.productImage)
                .into(productImage)
            productName.text = product?.productName
            productDescription.text = product?.productDes
            product?.productRating?.let {
                productRating.rating = it
                ratingProductDetails.text = "$it Rating on this Product."
            }
            productBrandName.text = product?.productBrand
            productPrice.text = "₹ " + product?.productPrice
            addToCart.setOnClickListener {
                product?.let { addProductToCart(it) }
            }
            productAddToFav.setOnClickListener {
                handleFavoriteSelected(product)
            }
        }

        favoritesViewModel.favoritesObservable().observe(
            viewLifecycleOwner, processFavoritesData()
        )

        product?.productId?.let { favoritesViewModel.isProductFavorite(it) }

        val bottomNavigation = activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation?.visibility = View.GONE
    }

    private fun addProductToCart(product: Product) {
        val bottomSheetDialog = BottomSheetDialog(
            requireContext(), R.style.BottomSheetDialogTheme
        )

        val bottomSheetView = LayoutInflater.from(requireContext()).inflate(
            R.layout.fragment_bottom_add_to_cart,
            requireActivity().findViewById<ConstraintLayout>(R.id.bottomSheet)
        )

        bottomSheetView.findViewById<View>(R.id.addToCart_BottomSheet).setOnClickListener {
            addProductToCartAndDB(product)
            quantitySelected = 1
            bottomSheetDialog.dismiss()
        }

        bottomSheetView.findViewById<LinearLayout>(R.id.minusLayout).setOnClickListener {
            if (bottomSheetView.findViewById<EditText>(R.id.quantityEtBottom).text.toString()
                    .toInt() > 1
            ) {
                quantitySelected--
                bottomSheetView.findViewById<EditText>(R.id.quantityEtBottom)
                    .setText(quantitySelected.toString())
            }
        }

        bottomSheetView.findViewById<LinearLayout>(R.id.plusLayout).setOnClickListener {
            if (bottomSheetView.findViewById<EditText>(R.id.quantityEtBottom).text.toString()
                    .toInt() < 10
            ) {
                quantitySelected++
                bottomSheetView.findViewById<EditText>(R.id.quantityEtBottom)
                    .setText(quantitySelected.toString())
            }
        }

        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()
    }

    private fun addProductToCartAndDB(product: Product) {
        cartViewModel.insert(
            CartEntity(
                name = product.productName,
                quantity = quantitySelected,
                price = product.productPrice.toInt(),
                productId = product.productId,
                productImage = product.productImage
            )
        )
        activity?.applicationContext?.let {
            Extensions.toast(
                "Product Added to Cart Successfully!!",
                it
            )
        }
    }

    private fun handleFavoriteSelected(product: Product?) {
        if (binding?.productAddToFav?.tag == "UnFavorite") {
            binding?.productAddToFav?.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_fav_selected
                )
            )
            binding?.productAddToFav?.tag = "Favorite"
            product?.productId?.let { favoritesViewModel.addFavoriteProduct(it) }
        } else {
            binding?.productAddToFav?.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_fav
                )
            )
            binding?.productAddToFav?.tag = "UnFavorite"
            product?.productId?.let { favoritesViewModel.removeFavoriteProduct(it) }
        }
    }

    private fun processFavoritesData(): (t: StateData<Boolean>?) -> Unit = {
        if (it != null) {
            when (it.getStatus()) {

                StateData.DataStatus.SUCCESS -> {
                    if (it.getData() == true) {
                        binding?.productAddToFav?.setImageDrawable(
                            ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.ic_fav_selected
                            )
                        )
                        binding?.productAddToFav?.tag = "Favorite"
                    } else {
                        binding?.productAddToFav?.setImageDrawable(
                            ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.ic_fav
                            )
                        )
                        binding?.productAddToFav?.tag = "UnFavorite"
                    }
                }

                StateData.DataStatus.ERROR -> {
                    activity?.applicationContext?.let { context ->
                        Extensions.toast(
                            it.getError()?.message.toString(),
                            context
                        )
                    }
                }

                else -> {
                    activity?.applicationContext?.let { Extensions.toast("Sign in failed", it) }
                }
            }
        }
    }


    companion object {
        const val KEY_PRODUCT = "KEY_PRODUCT"
    }

    override fun onFavoriteClick(favoriteProductId: String, isChecked: Boolean) {
        TODO("Not yet implemented")
    }
}