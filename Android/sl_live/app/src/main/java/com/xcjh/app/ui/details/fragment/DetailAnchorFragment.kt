package com.xcjh.app.ui.details.fragment

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.webkit.WebView
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.xcjh.app.R
import com.xcjh.app.appViewModel
import com.xcjh.app.base.BaseVpFragment
import com.xcjh.app.databinding.FragmentDetailTabAnchorBinding
import com.xcjh.app.ui.chat.ChatActivity
import com.xcjh.app.ui.details.DetailVm
import com.xcjh.app.utils.clearWebView
import com.xcjh.app.utils.judgeLogin
import com.xcjh.app.utils.setH5Data
import com.xcjh.app.utils.setWeb
import com.xcjh.base_lib.Constants
import com.xcjh.base_lib.base.BaseViewModel
import com.xcjh.base_lib.utils.loadImage
import com.xcjh.base_lib.utils.toHtml
import com.xcjh.base_lib.utils.view.textString


/**
 * 主播
 */

class DetailAnchorFragment(
    var anchorId: String,
    override val typeId: Long = 2,
) : BaseVpFragment<BaseViewModel, FragmentDetailTabAnchorBinding>() {
    private val vm by lazy {
        ViewModelProvider(requireActivity())[DetailVm::class.java]
    }
    private var mNoticeWeb: WebView? = null
    override fun initView(savedInstanceState: Bundle?) {
        mNoticeWeb = mDatabind.agentWeb
        setWeb(mNoticeWeb!!)
    }

    override fun lazyLoadData() {

    }
    override fun createObserver() {
        //主播详情接口返回监听处理
        vm.anchor.observe(this) {
            if (it != null) {
                this.anchorId = it.id
                mDatabind.tvTabAnchorNick.text = it.nickName  //主播昵称
                mDatabind.tvDetailTabAnchorFans.text = it.fansCount //主播粉丝数量
             /*   mDatabind.tvTabAnchorNotice.movementMethod = LinkMovementMethod.getInstance()
              *//*  it.notice?.toHtml {
                    Handler(Looper.getMainLooper()).post {
                        mDatabind.tvTabAnchorNotice.text = it
                    }
                }*//*
                mDatabind.tvTabAnchorNotice.text = it.notice?.toHtml() //主播公告*/
                val bb = "<html><head><style>body { font-size:14px; color: #94999f; margin: 0; }</style></head><body>${(it.notice)}</body></html>"
                //mNoticeWeb?.loadDataWithBaseURL(null, bb, "text/html", "UTF-8", null)
                setH5Data(mNoticeWeb,it.notice?:"", tvColor ="#94999f", maxLine = 200 )
                loadImage(requireContext(),
                    it.head,
                    mDatabind.ivTabAnchorAvatar,
                    R.drawable.default_anchor_icon
                )
            }
        }
        vm.isfocus.observe(this) {
            if (it) {
                mDatabind.tvDetailTabAnchorFans.text = mDatabind.tvDetailTabAnchorFans.text.toString().toInt().plus(1).toString() //主播粉丝数量
            }
        }
        vm.isUnFocus.observe(this) {
            if (it) {
                mDatabind.tvDetailTabAnchorFans.text = mDatabind.tvDetailTabAnchorFans.text.toString().toInt().minus(1).toString()
            }
        }
        vm.anchorInfo.observe(this) {
            updateInfo(it.userId)
        }
    }


    private fun updateInfo(anchorId: String?) {
        if (anchorId.isNullOrEmpty()) {
            return
        } else {
            this.anchorId = anchorId
        }
        //  lazyLoadData()
    }

    override fun onDestroy() {
        super.onDestroy()
        clearWebView(mNoticeWeb)
    }

}