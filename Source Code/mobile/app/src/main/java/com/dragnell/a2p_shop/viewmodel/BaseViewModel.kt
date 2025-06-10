package com.dragnell.a2p_shop.viewmodel

import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {
    companion object {
        val TAG: String = BaseViewModel::class.java.name
    }

}
