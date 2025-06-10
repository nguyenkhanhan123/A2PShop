package com.dragnell.a2p_shop.view

import java.util.Objects

interface OnMainCallBack2 {
    fun showFragment(tag:String, data: Objects?, isBack:Boolean,viewID:Int)
}