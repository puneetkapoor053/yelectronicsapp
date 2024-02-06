package com.ycompany.yelectronics.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ycompany.yelectronics.injections.CustomViewModelFactory
import com.ycompany.yelectronics.network.database.OrdersListEntity
import com.ycompany.yelectronics.ui.base.BaseFragment
import com.ycompany.yelectronics.ui.databinding.OrdersDetailsFragmentBinding
import com.ycompany.yelectronics.utils.Extensions
import com.ycompany.yelectronics.utils.ProgressLoadingDialog
import com.ycompany.yelectronics.viewmodel.StateData
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OrderDetailsFragment : BaseFragment<OrdersDetailsFragmentBinding>() {

    private lateinit var cartViewModel: CartViewModel

    @Inject
    lateinit var customViewModelFactory: CustomViewModelFactory

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
        binding = OrdersDetailsFragmentBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cartViewModel =
            ViewModelProvider(this, customViewModelFactory)[CartViewModel::class.java]

        binding?.apply {
            orderRecyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            orderRecyclerView.setHasFixedSize(true)
            orderRecyclerView.isNestedScrollingEnabled = false
        }

        cartViewModel.ordersListObservable().observe(
            viewLifecycleOwner, processOrdersData()
        )
        cartViewModel.getAllOrders()
    }

    private fun processOrdersData(): (t: StateData<List<OrdersListEntity>>?) -> Unit = {
        if (it != null) {
            when (it.getStatus()) {
                StateData.DataStatus.LOADING -> {
                    progressLoadingDialog.startLoadingDialog("Please wait!!")
                }

                StateData.DataStatus.SUCCESS -> {
                    progressLoadingDialog.dismissDialog()
                    setOrdersRecyclerView(it.getData())
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

    private fun setOrdersRecyclerView(data: List<OrdersListEntity>?) {
        data?.let {
            if (data.isEmpty()) {
                binding?.emptyOrdersLayout?.visibility = View.VISIBLE
                binding?.orderRecyclerView?.visibility = View.GONE
                binding?.animationViewCartPage?.playAnimation()
                binding?.animationViewCartPage?.loop(true)
            } else {
                binding?.emptyOrdersLayout?.visibility = View.GONE
                binding?.orderRecyclerView?.visibility = View.VISIBLE
                binding?.animationViewCartPage?.pauseAnimation()
                val orderListAdapter = OrderListAdapter(data)
                binding?.orderRecyclerView?.adapter = orderListAdapter
            }
        }
    }
}