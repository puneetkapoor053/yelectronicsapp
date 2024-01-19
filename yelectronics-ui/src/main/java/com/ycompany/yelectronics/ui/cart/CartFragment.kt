package com.ycompany.yelectronics.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ycompany.yelectronics.injections.CustomViewModelFactory
import com.ycompany.yelectronics.network.database.CartEntity
import com.ycompany.yelectronics.ui.base.BaseFragment
import com.ycompany.yelectronics.ui.databinding.CartFragmentBinding
import com.ycompany.yelectronics.utils.Extensions
import com.ycompany.yelectronics.utils.ProgressLoadingDialog
import com.ycompany.yelectronics.viewmodel.StateData
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CartFragment : BaseFragment<CartFragmentBinding>(), CartItemClickListener {

    private lateinit var cartViewModel: CartViewModel

    @Inject
    lateinit var customViewModelFactory: CustomViewModelFactory
    var productsAdapter: CartAdapter? = null

    var listOfCartProducts: ArrayList<CartEntity>? = arrayListOf()
    var sum: Int = 0

    private val progressLoadingDialog: ProgressLoadingDialog by lazy {
        ProgressLoadingDialog(
            requireActivity()
        )
    }

    override fun onCreateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) {
        binding = CartFragmentBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cartViewModel =
            ViewModelProvider(this, customViewModelFactory)[CartViewModel::class.java]

        binding?.apply {
            cartRecyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            cartRecyclerView.setHasFixedSize(true)
            cartRecyclerView.isNestedScrollingEnabled = false
            emptyCartUpdate()
        }

        cartViewModel.cartListObservable().observe(
            viewLifecycleOwner, processCartData()
        )
        cartViewModel.getAllCartItems()
    }

    private fun processCartData(): (t: StateData<List<CartEntity>>?) -> Unit = {
        if (it != null) {
            when (it.getStatus()) {
                StateData.DataStatus.LOADING -> {
                    progressLoadingDialog.startLoadingDialog("Please wait!!")
                }

                StateData.DataStatus.SUCCESS -> {
                    progressLoadingDialog.dismissDialog()
                    setCartListProductsRecyclerView(it.getData())
                    activity?.applicationContext?.let { context ->
                        Extensions.toast(
                            "Favorite List Fetched successfully",
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

    private fun setCartListProductsRecyclerView(data: List<CartEntity>?) {
        data?.let {
            listOfCartProducts = ArrayList(it)
            productsAdapter = CartAdapter(this, listOfCartProducts!!)

            binding?.cartRecyclerView?.adapter = productsAdapter
            updateCartView(data)
        }
    }

    private fun updateCartView(
        data: List<CartEntity>?
    ) {
        if (data?.isEmpty() == true) {
            emptyCartUpdate()
            sum = 0
        } else {
            updateCartWithAmount(data, sum)
        }
    }

    private fun updateCartWithAmount(
        it: List<CartEntity>?,
        sum: Int
    ) {
        var sum1 = sum
        binding?.emptyBagMsgLayout?.visibility = View.GONE
        binding?.bottomCartLayout?.visibility = View.VISIBLE
        binding?.mybagText?.visibility = View.VISIBLE
        binding?.animationViewCartPage?.pauseAnimation()
        it?.forEach {
            sum1 += (it.price) * (it.quantity)
        }
        binding?.totalPrice?.text = "₹ " + sum1
    }

    override fun onItemDeleteClick(product: CartEntity) {
        cartViewModel.deleteCart(product)
        listOfCartProducts?.remove(product)
        productsAdapter?.updateList()
        updateCartView(listOfCartProducts?.toList())
        Toast.makeText(context, "Removed From Bag", Toast.LENGTH_SHORT).show()
    }

    private fun emptyCartUpdate() {
        binding?.animationViewCartPage?.playAnimation()
        binding?.animationViewCartPage?.loop(true)
        binding?.bottomCartLayout?.visibility = View.GONE
        binding?.mybagText?.visibility = View.GONE
        binding?.emptyBagMsgLayout?.visibility = View.VISIBLE
    }

    override fun onItemUpdateClick(product: CartEntity) {
//        cartViewModel.deleteCart(product)
//        Toast.makeText(context,"Removed From Bag",Toast.LENGTH_SHORT).show()
    }
}