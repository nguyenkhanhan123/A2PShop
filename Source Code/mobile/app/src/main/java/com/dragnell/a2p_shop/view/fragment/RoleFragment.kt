package com.dragnell.a2p_shop.view.fragment

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.dragnell.a2p_shop.App
import com.dragnell.a2p_shop.CommonUtils
import com.dragnell.a2p_shop.databinding.AccountsBinding
import com.dragnell.a2p_shop.databinding.RoleBinding
import com.dragnell.a2p_shop.model.Account
import com.dragnell.a2p_shop.model.res_api.RoleRes
import com.dragnell.a2p_shop.view.adapter.AccountsAdapter
import com.dragnell.a2p_shop.view.adapter.RoleAdapter
import com.dragnell.a2p_shop.view.api.Api
import com.dragnell.a2p_shop.view.api.ApiInterface
import com.dragnell.a2p_shop.view.dialog.AddOrFixRoleDialog
import com.dragnell.a2p_shop.viewmodel.CommonViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RoleFragment : BaseFragment<RoleBinding, CommonViewModel>() {
    override fun getClassVM(): Class<CommonViewModel> {
        return CommonViewModel::class.java
    }

    override fun initView() {
        mbinding.add.setOnClickListener {
            val dialog = AddOrFixRoleDialog(requireContext(),null, onSucess = {
                callGetRole()
            }
            )
            dialog.behavior.isHideable = false
            dialog.behavior.setState(BottomSheetBehavior.STATE_EXPANDED)
            dialog.show()
        }
        callGetRole()
    }

    override fun initViewBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): RoleBinding {
        return RoleBinding.inflate(layoutInflater)
    }

    companion object {
        val TAG: String = RoleFragment::class.java.name
    }

    private fun callGetRole() {
        val apiInterface: ApiInterface = Api.client.create(ApiInterface::class.java)
        Log.e("KQ", "Bearer ${CommonUtils.getInstance().getPref("Token")}")
        apiInterface.getRole(
            "Bearer ${CommonUtils.getInstance().getPref("Token")}"
        ).enqueue(object : Callback<RoleRes> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<RoleRes>, response: Response<RoleRes>
            ) {
                Log.e("KQ", response.body().toString())
                if (response.body()!=null){
                    App.getInstance().storage.listRole= response.body()!!.records
                    mbinding.rv.adapter=RoleAdapter(response.body()!!.records,requireContext(), onClick = {
                        val dialog = AddOrFixRoleDialog(requireContext(),it, onSucess = {
                            callGetRole()
                        }
                        )
                        dialog.behavior.isHideable = false
                        dialog.behavior.setState(BottomSheetBehavior.STATE_EXPANDED)
                        dialog.show()
                    }  )
                }
                Log.e("KQ", response.body().toString())

            }

            override fun onFailure(call: Call<RoleRes>, t: Throwable) {
                Log.e("KQ", "onFailure: " + t.message)
            }

        })
    }
}