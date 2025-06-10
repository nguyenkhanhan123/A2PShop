package com.dragnell.a2p_shop.view.fragment

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.dragnell.a2p_shop.App
import com.dragnell.a2p_shop.CommonUtils
import com.dragnell.a2p_shop.databinding.AccountsBinding
import com.dragnell.a2p_shop.model.Account
import com.dragnell.a2p_shop.view.adapter.AccountsAdapter
import com.dragnell.a2p_shop.view.api.Api
import com.dragnell.a2p_shop.view.api.ApiInterface
import com.dragnell.a2p_shop.view.dialog.AccountDialog
import com.dragnell.a2p_shop.view.dialog.AddOrFixRoleDialog
import com.dragnell.a2p_shop.viewmodel.CommonViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AccountsFragment : BaseFragment<AccountsBinding, CommonViewModel>() {
    override fun getClassVM(): Class<CommonViewModel> {
        return CommonViewModel::class.java
    }

    override fun initView() {
        mbinding.add.setOnClickListener {
            val dialog = AccountDialog(requireContext(),null, onSucess = {
                callGetAccounts()
            }
            )
            dialog.behavior.isHideable = false
            dialog.behavior.setState(BottomSheetBehavior.STATE_EXPANDED)
            dialog.show()
        }
        callGetAccounts()
    }

    override fun initViewBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): AccountsBinding {
        return AccountsBinding.inflate(layoutInflater)
    }

    companion object {
        val TAG: String = AccountsFragment::class.java.name
    }

    private fun callGetAccounts() {
        Log.e("KQ", CommonUtils.getInstance().getPref("Token").toString())
        val apiInterface: ApiInterface = Api.client.create(ApiInterface::class.java)
        apiInterface.getAccounts(
            "Bearer ${CommonUtils.getInstance().getPref("Token")}"
        ).enqueue(object : Callback<List<Account>> {
            override fun onResponse(
                call: Call<List<Account>>, response: Response<List<Account>>
            ) {
                if (response.body()!=null){
                    mbinding.rv.adapter =
                        AccountsAdapter(response.body()!!, requireContext(), getNameRole = { i ->
                            getNameRole(i)
                        },onClick = {
                            val dialog = AccountDialog(requireContext(),it, onSucess = {
                                callGetAccounts()
                            }
                            )
                            dialog.behavior.isHideable = false
                            dialog.behavior.setState(BottomSheetBehavior.STATE_EXPANDED)
                            dialog.show()
                        }  )
                }
                Log.e("KQ", response.body().toString())
            }

            override fun onFailure(call: Call<List<Account>>, t: Throwable) {
                Log.e("KQ", "onFailure: " + t.message)
            }

        })
    }

    private fun getNameRole(i: String): String {
        val listRole = App.getInstance().storage.listRole
        return listRole.find { it.id == i }?.title ?: "Chưa phân công vai trò"
    }
}