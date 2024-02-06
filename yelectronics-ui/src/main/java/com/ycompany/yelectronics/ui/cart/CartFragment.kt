package com.ycompany.yelectronics.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.ycompany.yelectronics.injections.CustomViewModelFactory
import com.ycompany.yelectronics.network.database.CartEntity
import com.ycompany.yelectronics.network.database.Orders
import com.ycompany.yelectronics.network.database.OrdersListEntity
import com.ycompany.yelectronics.ui.R
import com.ycompany.yelectronics.ui.base.BaseFragment
import com.ycompany.yelectronics.ui.databinding.CartFragmentBinding
import com.ycompany.yelectronics.ui.home.HomeActivity
import com.ycompany.yelectronics.utils.Extensions
import com.ycompany.yelectronics.utils.ProgressLoadingDialog
import com.ycompany.yelectronics.viewmodel.StateData
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject
import kotlin.math.roundToInt

@AndroidEntryPoint
class CartFragment : BaseFragment<CartFragmentBinding>(), CartItemClickListener,
    PaymentCompletedListener {

    private lateinit var cartViewModel: CartViewModel

    @Inject
    lateinit var customViewModelFactory: CustomViewModelFactory
    var productsAdapter: CartAdapter? = null

    var listOfCartProducts: ArrayList<CartEntity>? = arrayListOf()
    var sum: Int = 0
    var totalAmountToPay: Int = 0

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
            checkOutBagPage.setOnClickListener {
                initiatePayment()
            }
        }

        cartViewModel.cartListObservable().observe(
            viewLifecycleOwner, processCartData()
        )
        cartViewModel.getAllCartItems()

        (this.activity as HomeActivity).setPaymentListener(this)
    }

    private fun initiatePayment() {
        //Checkout.preload(requireContext())
        val checkout = Checkout()
        checkout.setKeyID("rzp_test_v04yDeAqTfuKrC")
        // set image
        checkout.setImage(R.drawable.yelectronics_logo)

        val amount = (totalAmountToPay.toFloat() * 100).roundToInt()
        checkout.open(
            this.activity as HomeActivity,
            cartViewModel.getPaymentPayload(amount)
        )
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
        totalAmountToPay = sum1
        binding?.totalPrice?.text = "${getString(R.string.rupees)}$totalAmountToPay"
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
        Toast.makeText(context, "Feature in progress", Toast.LENGTH_SHORT).show()
    }

    override fun onPaymentSuccess(s1: String?, pO2: PaymentData?) {
        Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
        val orderID = getString(R.string.order_id_prefix) + java.util.Random().nextInt()
        val ordersList = ArrayList<Orders>()

        listOfCartProducts?.forEach {
            ordersList.add(Orders.toOrderList(it))
        }

        val orders = OrdersListEntity(ordersList, orderID, pO2?.paymentId!!, Date().time)

        cartViewModel.insertOrders(orders)

        cartViewModel.deleteAllCart()
        listOfCartProducts?.clear()

        productsAdapter?.updateList()
        updateCartView(listOfCartProducts?.toList())

        showOrderSuccessView(orderID, pO2)
    }

    private fun showOrderSuccessView(orderID: String, pO2: PaymentData) {
        binding?.apply {
            emptyBagMsgLayout.visibility = View.GONE
            animationViewCartPage.pauseAnimation()
            animationViewCartPage.visibility = View.GONE

            emptyBagMsgLayout.visibility = View.VISIBLE
            animationViewOrderSuccess.playAnimation()
            animationViewOrderSuccess.loop(true)
            animationViewOrderSuccess.visibility = View.VISIBLE
            orderId.visibility = View.VISIBLE
            orderPaymentReference.visibility = View.VISIBLE
            orderMore.visibility = View.VISIBLE
            dateOfOrder.visibility = View.VISIBLE

            cartMessage.text = getString(R.string.order_success)
            orderPaymentReference.text = getString(R.string.payment_reference) + pO2.paymentId!!
            orderId.text = "${getString(R.string.orderid)}$orderID"
            dateOfOrder.text = "${context?.getString(R.string.date_of_order)}${
                SimpleDateFormat(getString(R.string.order_date_format)).format(Date().time)
            }"
            orderMore.setOnClickListener {
                findNavController().navigate(R.id.homeFragment)
            }
        }
    }

    override fun onPaymentError(i1: Int, s2: String?, pO3: PaymentData?) {
        Toast.makeText(context, "Payment Failed", Toast.LENGTH_SHORT).show()
    }
}

interface PaymentCompletedListener {
    fun onPaymentSuccess(s1: String?, pO2: PaymentData?)
    fun onPaymentError(i1: Int, s2: String?, pO3: PaymentData?)
}