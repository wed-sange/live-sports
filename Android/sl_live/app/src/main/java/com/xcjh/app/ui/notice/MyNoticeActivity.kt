package com.xcjh.app.ui.notice

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.drake.brv.listener.ItemDifferCallback
import com.drake.brv.utils.addModels
import com.drake.brv.utils.bindingAdapter
import com.drake.brv.utils.linear
import com.drake.brv.utils.models
import com.drake.brv.utils.mutable
import com.drake.brv.utils.setup
import com.drake.statelayout.StateConfig
import com.gyf.immersionbar.ImmersionBar
import com.xcjh.app.R
import com.xcjh.app.adapter.SchtitleAdapter
import com.xcjh.app.appViewModel
import com.xcjh.app.base.BaseActivity
import com.xcjh.app.bean.AnchorBean
import com.xcjh.app.bean.MatchBean
import com.xcjh.app.databinding.ActivityMynoticeBinding
import com.xcjh.app.databinding.ItemJsBinding
import com.xcjh.app.databinding.ItemSchAllBinding
import com.xcjh.app.ui.details.MatchDetailActivity
import com.xcjh.app.utils.judgeLogin
import com.xcjh.app.view.CustomHeader
import com.xcjh.base_lib.Constants.Companion.BASE_PAGE_SIZE
import com.xcjh.base_lib.utils.LogUtils
import com.xcjh.base_lib.utils.TimeUtil
import com.xcjh.base_lib.utils.distance
import com.xcjh.base_lib.utils.grid
import com.xcjh.base_lib.utils.horizontal
import com.xcjh.base_lib.utils.vertical
import com.xcjh.base_lib.utils.view.clickNoRepeat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/***
 * 我的关注比赛
 */

class MyNoticeActivity : BaseActivity<MyNoticeVm, ActivityMynoticeBinding>() {

    var mview: LottieAnimationView? = null
    var mview1: ImageView? = null
    var listdata: MutableList<MatchBean> = ArrayList<MatchBean>()
    var page = 1
    var pageSize = 10
    var index: Int = 0
    val animatorSet = AnimatorSet()
    var strTimeZu: MutableList<String> = ArrayList<String>()
    override fun initView(savedInstanceState: Bundle?) {
        try {

            ImmersionBar.with(this)
                .statusBarDarkFont(true)
                .navigationBarColor(R.color.c_ffffff)
                .navigationBarDarkIcon(true)
                .titleBar(mDatabind.titleTop.root)
                .init()
            mDatabind.state.apply {
                StateConfig.setRetryIds(R.id.ivEmptyIcon, R.id.txtEmptyName)
                onEmpty {
                    this.findViewById<TextView>(R.id.txtEmptyName).text =
                        resources.getString(R.string.no_data_hint)
                    this.findViewById<ImageView>(R.id.ivEmptyIcon)
                        .setImageDrawable(resources.getDrawable(R.drawable.ic_empet_all))
                    this.findViewById<ImageView>(R.id.ivEmptyIcon).setOnClickListener { }
                }
            }
            mDatabind.smartCommon.setRefreshHeader(CustomHeader(this))
            mDatabind.titleTop.tvTitle.text = resources.getString(R.string.my_txt_subscribe)
            mDatabind.rec.run {
                vertical()
                // adapter=mAdapter
                distance(0, 0, 0, 0)
            }
            mDatabind.rec.linear().setup {
                addType<MatchBean>(R.layout.item_sch_all)

                onBind {

                    // findView<TextView>(R.id.tvname).text = getModel<MatchBean>().competitionName
                    var binding = getBinding<ItemSchAllBinding>()
                    var item = _data as MatchBean
                    // 设置item数据

                    var time = TimeUtil.getDayOfWeek(item!!.matchTime.toLong(), context)
                    var time1 = TimeUtil.timeStamp2Date(item!!.matchTime.toLong(), null)
                    if (item.focus) {
                        binding.ivsc.setBackgroundResource(R.drawable.sc_shoucang_icon2)
                    } else {
                        binding.ivsc.setBackgroundResource(R.drawable.sc_shoucang_icon1)
                    }
                    binding.tvmiddletime.text = time
                    when (item.visbleTime) {
                        0 -> {
                            if (time1!!.substring(0, 10) == TimeUtil.gettimenowYear()) {
                                if (strTimeZu.size == 0) {
                                    item.visbleTime = 2
                                    binding.tvmiddletime.visibility = View.GONE
                                } else {
                                    if (strTimeZu[strTimeZu.size - 1] == time) {
                                        binding.tvmiddletime.visibility = View.GONE
                                        item.visbleTime = 2
                                    } else {
                                        binding.tvmiddletime.visibility = View.GONE

                                        item.visbleTime = 1
                                        strTimeZu.add(time)
                                    }
                                }
                            } else {
                                if (strTimeZu.size == 0) {
                                    binding.tvmiddletime.visibility = View.GONE

                                    item.visbleTime = 1
                                    strTimeZu.add(time)
                                } else {
                                    if (strTimeZu[strTimeZu.size - 1] == time) {
                                        binding.tvmiddletime.visibility = View.GONE
                                        item.visbleTime = 2
                                    } else {
                                        binding.tvmiddletime.visibility = View.GONE

                                        item.visbleTime = 1
                                        strTimeZu.add(time)
                                    }


                                }

                            }
                        }

                        1 -> {//显示
                            binding.tvmiddletime.visibility = View.GONE

                        }

                        2 -> {//不显示
                            binding.tvmiddletime.visibility = View.GONE

                        }
                    }

                    LogUtils.d("直播数据" + item.anchorList)
                    binding.txtMatchAnimation.visibility = View.VISIBLE
                    if (item.matchType == "1") {//比赛类型：1：足球；2：篮球,可用值:1,2
                        binding.tvhafl.text =
                            context.resources.getString(R.string.hafl_rices) + "" + item.homeHalfScore + "-" + item.awayHalfScore
                        binding.tvtime.text = time1!!.substring(11, 16)
                        binding.tvname.text = item.competitionName
                        binding.tvnameLeft.text = item.homeName
                        binding.tvnameRight.text = item.awayName
                        Glide.with(context).load(item.homeLogo)
                            .placeholder(R.drawable.def_football).into(binding.tvflagLeft)
                        Glide.with(context).load(item.awayLogo)
                            .placeholder(R.drawable.def_football).into(binding.tvflagRight)
                        binding.ivtype.setBackgroundResource(R.drawable.football)

                        when (item.status) {
                            "0" -> {
                                binding.txtMatchAnimation.visibility = View.GONE
                                binding.tvstatus.visibility = View.GONE
                                //  clearAnimation(binding.txtMatchAnimation)
                            }

                            "1" -> {
                                binding.tvvs.text = "VS"
                                binding.txtMatchAnimation.visibility = View.GONE
                                binding.tvstatus.visibility = View.VISIBLE
                                binding.tvstatus.setTextColor(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.c_94999f
                                    )
                                )
                                binding.tvvs.setTextColor(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.c_37373d
                                    )
                                )
                                binding.tvstatus.text =
                                    context.resources.getString(R.string.main_txt_wks)
                                clearAnimation(binding.txtMatchAnimation)
                            }

                            "2" -> {
                                binding.tvstatus.visibility = View.VISIBLE
                                binding.txtMatchAnimation.visibility = View.VISIBLE
                                binding.tvvs.text = item.homeScore + "-" + item.awayScore
                                binding.tvstatus.setTextColor(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.c_e6820c
                                    )
                                )
                                binding.tvvs.setTextColor(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.c_34a853
                                    )
                                )
                                binding.tvstatus.text =
                                    if (item.runTime == null)
                                        "0"
                                    else {
                                        item.runTime
                                    }

                                initAnimation(binding.txtMatchAnimation)
                            }

                            "3" -> {
                                binding.tvstatus.visibility = View.VISIBLE
                                binding.txtMatchAnimation.visibility = View.VISIBLE
                                binding.tvvs.text = item.homeScore + "-" + item.awayScore
                                binding.tvstatus.setTextColor(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.c_e6820c
                                    )
                                )
                                binding.tvvs.setTextColor(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.c_34a853
                                    )
                                )
                                binding.tvstatus.text = context.resources.getString(
                                    R.string.zc
                                )
                                initAnimation(binding.txtMatchAnimation)
                            }

                            "4" -> {
                                binding.tvstatus.visibility = View.VISIBLE
                                binding.txtMatchAnimation.visibility = View.VISIBLE
                                binding.tvvs.text = item.homeScore + "-" + item.awayScore
                                binding.tvstatus.setTextColor(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.c_e6820c
                                    )
                                )
                                binding.tvvs.setTextColor(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.c_34a853
                                    )
                                )
                                binding.tvstatus.text =
                                    if (item.runTime == null)
                                        "0"
                                    else {
                                        item.runTime
                                    }
                                initAnimation(binding.txtMatchAnimation)
                            }

                            "5", "6" -> {
                                binding.tvstatus.visibility = View.VISIBLE
                                binding.txtMatchAnimation.visibility = View.VISIBLE
                                binding.tvvs.text = item.homeScore + "-" + item.awayScore
                                binding.tvstatus.setTextColor(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.c_e6820c
                                    )
                                )
                                binding.tvvs.setTextColor(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.c_34a853
                                    )
                                )
                                binding.tvstatus.text = context.resources.getString(
                                    R.string.over_time
                                )
                                initAnimation(binding.txtMatchAnimation)

                            }

                            "7" -> {
                                binding.tvstatus.visibility = View.VISIBLE
                                binding.txtMatchAnimation.visibility = View.VISIBLE
                                binding.tvvs.text = item.homeScore + "-" + item.awayScore
                                binding.tvstatus.setTextColor(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.c_e6820c
                                    )
                                )
                                binding.tvvs.setTextColor(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.c_34a853
                                    )
                                )
                                binding.tvstatus.text = context.resources.getString(
                                    R.string.main_dqdz
                                )
                                initAnimation(binding.txtMatchAnimation)

                            }

                            "8" -> {
                                binding.txtMatchAnimation.visibility = View.GONE
                                binding.tvvs.text = item.homeScore + "-" + item.awayScore
                                binding.tvstatus.visibility = View.VISIBLE
                                binding.tvstatus.setTextColor(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.c_999999
                                    )
                                )
                                binding.tvvs.setTextColor(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.c_34a853
                                    )
                                )
                                binding.tvstatus.text =
                                    context.resources.getString(R.string.main_txt_over)
                                clearAnimation(binding.txtMatchAnimation)

                            }

                            "9" -> {

                                binding.txtMatchAnimation.visibility = View.GONE
                                binding.tvvs.text = "VS"
                                binding.tvstatus.visibility = View.VISIBLE
                                binding.tvstatus.setTextColor(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.c_8a91a0
                                    )
                                )
                                binding.tvvs.setTextColor(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.c_37373d
                                    )
                                )
                                binding.tvstatus.text =
                                    context.resources.getString(R.string.main_txt_tc)
                                clearAnimation(binding.txtMatchAnimation)

                            }

                            "10" -> {
                                binding.txtMatchAnimation.visibility = View.GONE
                                binding.tvvs.text = "VS"
                                binding.tvstatus.visibility = View.VISIBLE
                                binding.tvstatus.setTextColor(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.c_8a91a0
                                    )
                                )
                                binding.tvvs.setTextColor(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.c_37373d
                                    )
                                )
                                binding.tvstatus.text =
                                    context.resources.getString(R.string.main_txt_zd)
                                clearAnimation(binding.txtMatchAnimation)

                            }

                            "11" -> {
                                binding.txtMatchAnimation.visibility = View.GONE
                                binding.tvvs.text = "VS"
                                binding.tvstatus.visibility = View.VISIBLE
                                binding.tvstatus.setTextColor(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.c_8a91a0
                                    )
                                )
                                binding.tvvs.setTextColor(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.c_37373d
                                    )
                                )
                                binding.tvstatus.text =
                                    context.resources.getString(R.string.main_txt_yz)

                                clearAnimation(binding.txtMatchAnimation)
                            }

                            "12" -> {
                                binding.txtMatchAnimation.visibility = View.GONE
                                binding.tvvs.text = "VS"
                                binding.tvstatus.visibility = View.VISIBLE
                                binding.tvstatus.setTextColor(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.c_8a91a0
                                    )
                                )
                                binding.tvvs.setTextColor(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.c_37373d
                                    )
                                )
                                binding.tvstatus.text =
                                    context.resources.getString(R.string.main_txt_qx)
                                clearAnimation(binding.txtMatchAnimation)
                            }

                            "13" -> {
                                binding.txtMatchAnimation.visibility = View.GONE
                                binding.tvvs.text = "VS"
                                binding.tvstatus.visibility = View.VISIBLE
                                binding.tvstatus.setTextColor(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.c_8a91a0
                                    )
                                )
                                binding.tvvs.setTextColor(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.c_37373d
                                    )
                                )
                                binding.tvstatus.text =
                                    context.resources.getString(R.string.main_txt_dd)
                                clearAnimation(binding.txtMatchAnimation)
                            }

                            else -> {
                                LogUtils.d("走这里了 哈哈哈")
                                binding.txtMatchAnimation.visibility = View.GONE
                                binding.tvstatus.visibility = View.VISIBLE
                                binding.tvvs.text = "VS"
                                binding.tvstatus.setTextColor(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.c_8a91a0
                                    )
                                )
                                binding.tvvs.setTextColor(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.c_37373d
                                    )
                                )
                                clearAnimation(binding.txtMatchAnimation)
                                val date = Date(item.matchTime.toLong())
                                var formatter =
                                    SimpleDateFormat("MM-dd HH:mm", Locale.getDefault())
                                binding.tvstatus.text =
                                    context.resources.getString(R.string.main_txt_over)
                            }
                        }
                    } else {
                        binding.tvhafl.text =
                            context.resources.getString(R.string.hafl_rices) + "" + item.awayHalfScore + "-" + item.homeHalfScore
                        binding.tvtime.text = time1!!.substring(11, 16)
                        binding.tvname.text = item.competitionName
                        binding.tvnameLeft.text = item.awayName
                        binding.tvnameRight.text = item.homeName
                        Glide.with(context).load(item.awayLogo)
                            .placeholder(R.drawable.def_basketball).into(binding.tvflagLeft)
                        Glide.with(context).load(item.homeLogo)
                            .placeholder(R.drawable.def_basketball).into(binding.tvflagRight)
                        binding.ivtype.setBackgroundResource(R.drawable.basketball)
                        when (item.status) {
                            "0" -> {
                                binding.txtMatchAnimation.visibility = View.GONE
                                binding.tvstatus.visibility = View.GONE
                                clearAnimation(binding.txtMatchAnimation)
                            }

                            "1" -> {
                                binding.tvvs.text = "VS"
                                binding.txtMatchAnimation.visibility = View.GONE
                                binding.tvstatus.visibility = View.VISIBLE
                                binding.tvstatus.setTextColor(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.c_94999f
                                    )
                                )
                                binding.tvvs.setTextColor(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.c_37373d
                                    )
                                )
                                binding.tvstatus.text =
                                    context.resources.getString(R.string.main_txt_wks)
                                clearAnimation(binding.txtMatchAnimation)

                            }

                            "2" -> {
                                binding.txtMatchAnimation.visibility = View.VISIBLE
                                binding.tvvs.text = item.awayScore + "-" + item.homeScore
                                binding.tvstatus.visibility = View.VISIBLE
                                binding.tvstatus.setTextColor(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.c_e6820c
                                    )
                                )
                                binding.tvvs.setTextColor(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.c_34a853
                                    )
                                )
                                binding.tvstatus.text =
                                    context.resources.getString(
                                        R.string.main_txt_basketball_phase,
                                        "一"
                                    )
                                initAnimation(binding.txtMatchAnimation)

                            }

                            "3" -> {
                                binding.txtMatchAnimation.visibility = View.VISIBLE
                                binding.tvvs.text = item.awayScore + "-" + item.homeScore
                                binding.tvstatus.visibility = View.VISIBLE
                                binding.tvstatus.setTextColor(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.c_e6820c
                                    )
                                )
                                binding.tvvs.setTextColor(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.c_34a853
                                    )
                                )
                                binding.tvstatus.text =
                                    context.resources.getString(
                                        R.string.main_txt_basketball_phase,
                                        "一"
                                    ) + context.resources.getString(
                                        R.string.finis
                                    )
                                initAnimation(binding.txtMatchAnimation)

                            }

                            "4" -> {
                                binding.txtMatchAnimation.visibility = View.VISIBLE
                                binding.tvvs.text = item.awayScore + "-" + item.homeScore
                                binding.tvstatus.visibility = View.VISIBLE
                                binding.tvstatus.setTextColor(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.c_e6820c
                                    )
                                )
                                binding.tvvs.setTextColor(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.c_34a853
                                    )
                                )
                                binding.tvstatus.text =
                                    context.resources.getString(
                                        R.string.main_txt_basketball_phase,
                                        "二"
                                    )
                                initAnimation(binding.txtMatchAnimation)

                            }

                            "5" -> {
                                binding.txtMatchAnimation.visibility = View.VISIBLE
                                binding.tvvs.text = item.awayScore + "-" + item.homeScore
                                binding.tvstatus.visibility = View.VISIBLE
                                binding.tvstatus.setTextColor(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.c_e6820c
                                    )
                                )
                                binding.tvvs.setTextColor(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.c_34a853
                                    )
                                )
                                binding.tvstatus.text =
                                    context.resources.getString(
                                        R.string.main_txt_basketball_phase,
                                        "二"
                                    ) + context.resources.getString(
                                        R.string.finis
                                    )
                                initAnimation(binding.txtMatchAnimation)

                            }

                            "6" -> {
                                binding.txtMatchAnimation.visibility = View.VISIBLE
                                binding.tvvs.text = item.awayScore + "-" + item.homeScore
                                binding.tvstatus.visibility = View.VISIBLE
                                binding.tvstatus.setTextColor(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.c_e6820c
                                    )
                                )
                                binding.tvvs.setTextColor(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.c_34a853
                                    )
                                )
                                binding.tvstatus.text =
                                    context.resources.getString(
                                        R.string.main_txt_basketball_phase,
                                        "三"
                                    )
                                initAnimation(binding.txtMatchAnimation)

                            }

                            "7" -> {
                                binding.txtMatchAnimation.visibility = View.VISIBLE
                                binding.tvvs.text = item.awayScore + "-" + item.homeScore
                                binding.tvstatus.visibility = View.VISIBLE
                                binding.tvstatus.setTextColor(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.c_e6820c
                                    )
                                )
                                binding.tvvs.setTextColor(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.c_34a853
                                    )
                                )
                                binding.tvstatus.text =
                                    context.resources.getString(
                                        R.string.main_txt_basketball_phase,
                                        "三"
                                    ) + context.resources.getString(
                                        R.string.finis
                                    )
                                initAnimation(binding.txtMatchAnimation)

                            }

                            "8" -> {
                                binding.txtMatchAnimation.visibility = View.VISIBLE
                                binding.tvvs.text = item.awayScore + "-" + item.homeScore
                                binding.tvstatus.visibility = View.VISIBLE
                                binding.tvstatus.setTextColor(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.c_e6820c
                                    )
                                )
                                binding.tvvs.setTextColor(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.c_34a853
                                    )
                                )
                                binding.tvstatus.text =
                                    context.resources.getString(
                                        R.string.main_txt_basketball_phase,
                                        "四"
                                    )
                                initAnimation(binding.txtMatchAnimation)
                            }

                            "9" -> {
                                binding.txtMatchAnimation.visibility = View.VISIBLE
                                binding.tvvs.text = item.awayScore + "-" + item.homeScore
                                binding.tvstatus.visibility = View.VISIBLE
                                binding.tvstatus.setTextColor(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.c_e6820c
                                    )
                                )
                                binding.tvvs.setTextColor(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.c_34a853
                                    )
                                )
                                binding.tvstatus.text =
                                    context.resources.getString(R.string.over_time)
                                initAnimation(binding.txtMatchAnimation)
                            }

                            "10" -> {
                                binding.txtMatchAnimation.visibility = View.GONE
                                binding.tvvs.text = item.awayScore + "-" + item.homeScore
                                binding.tvstatus.visibility = View.VISIBLE
                                binding.tvstatus.setTextColor(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.c_999999
                                    )
                                )
                                binding.tvvs.setTextColor(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.c_34a853
                                    )
                                )
                                binding.tvstatus.text =
                                    context.resources.getString(R.string.main_txt_over)
                                clearAnimation(binding.txtMatchAnimation)

                            }

                            "11", "12", "13", "14", "15" -> {
                                binding.txtMatchAnimation.visibility = View.GONE
                                clearAnimation(binding.txtMatchAnimation)
                                binding.tvvs.text = "VS"
                                binding.tvstatus.visibility = View.VISIBLE
                                binding.tvstatus.setTextColor(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.c_8a91a0
                                    )
                                )
                                binding.tvvs.setTextColor(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.c_37373d
                                    )
                                )
                                when (item.status) {
                                    "11" -> {
                                        binding.tvstatus.text =
                                            context.resources.getString(R.string.main_txt_zd)
                                    }

                                    "12" -> {
                                        binding.tvstatus.text =
                                            context.resources.getString(R.string.main_txt_qx)
                                    }

                                    "13" -> {
                                        binding.tvstatus.text =
                                            context.resources.getString(R.string.main_txt_zd)
                                    }

                                    "14" -> {
                                        binding.tvstatus.text =
                                            context.resources.getString(R.string.main_txt_yz)
                                    }

                                    "15" -> {
                                        binding.tvstatus.text =
                                            context.resources.getString(R.string.main_txt_dd)
                                    }
                                }

                            }
                        }

                    }



                    if (item.anchorList != null && item.anchorList.isNotEmpty()) {
                        if (item.anchorList.size > 5) {
                            for (i in 0 until item.anchorList.size) {
                                if (i > 4) {
                                    item.anchorList.removeAt(i)
                                }
                            }
                            item.anchorList = item.anchorList
                        }
                        binding.conlive.visibility = View.VISIBLE
                        if (binding.rec.itemDecorationCount == 0) {//加个判断
                            binding.rec.run {
                                grid(5)
                                distance(0, 0, 0, 0)
                            }
                        }

                        binding.rec.setup {
                            addType<AnchorBean>(R.layout.item_js)
                            onBind {
                                var binding1 = getBinding<ItemJsBinding>()
                                var item1 = _data as AnchorBean
                                if (item1.nickName.length > 5) {
                                    binding1.tvname.text = item1.nickName.substring(0, 4) + "..."
                                } else {
                                    binding1.tvname.text = item1.nickName
                                }

                                Glide.with(context).load(item1.userLogo)
                                    .placeholder(R.drawable.default_anchor_icon)
                                    .thumbnail(0.1f) // 设置缩略图大小比例（例如0.1表示原图的十分之一）
                                    .override(64, 64) // 指定目标宽度和高度
                                    .into(binding1.ivhead)
                                binding1.linroot.setOnClickListener {
                                    MatchDetailActivity.open(
                                        matchType = item.matchType,
                                        matchId = item.matchId,
                                        matchName = "${item.homeName}VS${item.awayName}",
                                        anchorId = item1.userId,
                                        videoUrl = ""
                                    )

                                }
                            }
                        }.models = item.anchorList


                    } else {
                        binding.conlive.visibility = View.GONE
                    }


                    binding.ivsc.clickNoRepeat(1000) {
                        judgeLogin {
//                            startAn(binding.tvcollect)
//                            stopAn(binding.tvcollect)
                            binding.tvcollect.visibility = View.VISIBLE
                            mview = binding.tvcollect
                            mview1 = binding.ivsc

                            index = modelPosition
                            if (item!!.focus) {
                                mViewModel.getUnnotice(
                                    item!!.matchId,
                                    item!!.matchType
                                )


                            } else {
                                // startAn(binding.tvcollect)

                                mViewModel.getNotice(
                                    item!!.matchId,
                                    item!!.matchType
                                )


                            }
                        }

                    }
                    binding.conroot.setOnClickListener {
                        MatchDetailActivity.open(
                            matchType = item.matchType,
                            matchId = item.matchId,
                            matchName = "${item.homeName}VS${item.awayName}",
                            anchorId = "",
                            videoUrl = ""
                        )

                    }


                }
                itemDifferCallback = object : ItemDifferCallback {
                    override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
                        return (oldItem as MatchBean).matchId == (newItem as MatchBean).matchId
                    }

                    override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
                        return (oldItem as MatchBean).homeHalfScore == (newItem as MatchBean).homeHalfScore
                    }

                    override fun getChangePayload(oldItem: Any, newItem: Any): Any? {
                        return true
                    }
                }
            }.models = listdata
            //mAdapter.isEmptyViewEnable = true
            mViewModel.getMyNoticeList(true)
            mDatabind.smartCommon.setOnRefreshListener { mViewModel.getMyNoticeList(true) }
                .setOnLoadMoreListener { mViewModel.getMyNoticeList(false) }
            appViewModel.appPushMsg.observe(this) {

                for (j in 0 until it.size) {
                    for (i in 0 until mDatabind.rec.models?.size!!) {
                        var bean: MatchBean = mDatabind.rec.models!![i] as MatchBean
                        // if (bean.homeHalfScore == "1") {

                        if (bean.matchId == it[j].matchId.toString() && bean.matchType == it[j].matchType.toString()) {
                            bean.awayHalfScore = it[j].awayHalfScore.toString()
                            bean.awayScore = it[j].awayScore.toString()
                            bean.homeHalfScore = it[j].homeHalfScore.toString()
                            bean.homeScore = it[j].homeScore.toString()
                            bean.runTime = it[j].runTime.toString()
                            bean.status = it[j].status.toString()
                            mDatabind.rec.bindingAdapter.notifyItemChanged(i)

                        }
                    }
                }

            }

            appViewModel.appPushLive.observe(this) {


                for (i in 0 until mDatabind.rec.models?.size!!) {
                    var bean: MatchBean = mDatabind.rec.models!![i] as MatchBean
                    // if (bean.homeHalfScore == "1") {
                    if (bean.matchId == it.matchId) {

                        if (bean.anchorList == null) {
                            var mybean = AnchorBean()
                            mybean.liveId = it.id
                            mybean.userId = it.anchorId
                            mybean.nickName = it.nickName
                            mybean.userLogo = it.userLogo
                            var list = ArrayList<AnchorBean>()
                            list.add(mybean)
                            (mDatabind.rec.models!![i] as MatchBean).anchorList = list
                            mDatabind.rec.bindingAdapter.notifyItemChanged(i)
                        } else {
                            if (it.liveStatus == 2) {//开播

                                var hasdata = false
                                for (i in 0 until bean.anchorList.size) {
                                    if (bean.anchorList[i].userId == it.anchorId) {
                                        hasdata = true
                                        break
                                    }
                                }
                                if (!hasdata) {
                                    var mybean = AnchorBean()
                                    mybean.liveId = it.id
                                    mybean.userId = it.anchorId
                                    mybean.nickName = it.nickName
                                    mybean.userLogo = it.userLogo
                                    bean.anchorList.add(mybean)
                                    mDatabind.rec.bindingAdapter.notifyItemChanged(i)
                                    break
                                }

                            } else {//关播
                                for (i in 0 until bean.anchorList.size) {
                                    if (bean.anchorList[i].userId == it.anchorId) {
                                        bean.anchorList.removeAt(i)
                                        mDatabind.rec.bindingAdapter.notifyItemChanged(
                                            i
                                        )
                                        break
                                    }
                                }
                                break
                            }
                        }
                        break
                    }

                }


            }

        } catch (e: Exception) {
        }
    }

    fun clearAnimation(view: LottieAnimationView) {

    }

    fun initAnimation(view: LottieAnimationView) {

    }

    override fun createObserver() {
        try {
            mViewModel.hotMatchList.observe(this) {
                if (it.isSuccess) {
                    //成功
                    when {
                        //第一页并没有数据 显示空布局界面
                        it.isFirstEmpty -> {
                            if (mDatabind.rec.models?.size != null) {
                                mDatabind.rec.mutable.clear()
                            }
                            mDatabind.smartCommon.finishRefresh()
                            mDatabind.state.showEmpty()
                        }
                        //是第一页
                        it.isRefresh -> {
                            mDatabind.smartCommon.finishRefresh()
                            mDatabind.smartCommon.resetNoMoreData()
                            mDatabind.rec.models = it.listData
                            //mAdapter.submitList(it.listData)
                            //  mAdapter.emptyView
                            //用户类型[1:游客 2:普通用户 3:会员]
                            //  addItemBinder.notifyDataSetChanged()
                            if (it.listData.size < BASE_PAGE_SIZE) {
                                mDatabind.smartCommon.finishLoadMoreWithNoMoreData()
                            }
                            mDatabind.state.showContent()


                        }
                        //不是第一页
                        else -> {
                            if (it.listData.isEmpty()) {
//                            mViewBind.smartCommon.setEnableLoadMore(false)
                                mDatabind.smartCommon.finishLoadMoreWithNoMoreData()
                            } else {
//                            mViewBind.smartCommon.setEnableLoadMore(true)
                                mDatabind.smartCommon.finishLoadMore()
//                            addItemBinder.addData(getNewData(it))
                                mDatabind.rec.addModels(it.listData)
                                //mAdapter.addAll(it.listD.lata)
                                //用户类型[1:游客 2:普通用户 3:会员]
                                mDatabind.state.showContent()
                            }

                        }
                    }
                } else {
                    //失败
                    if (it.isRefresh) {
                        mDatabind.smartCommon.finishRefresh()
                        //如果是第一页，则显示错误界面，并提示错误信息
                        // mAdapter.submitList(null)
                        //  addItemBinder.setEmptyView(empty)
                    } else {
                        mDatabind.smartCommon.finishLoadMore(false)
                    }
                }
            }
            mViewModel.unnoticeData.observe(this) {
                mview!!.setAnimation("shoucang2.json")
                mview!!.loop(false)
                mview!!.playAnimation()
                mview!!.addAnimatorListener(object :
                    Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator) {

                    }

                    override fun onAnimationEnd(animation: Animator) {
                        mview!!.visibility = View.INVISIBLE
                    }

                    override fun onAnimationCancel(animation: Animator) {

                    }

                    override fun onAnimationRepeat(animation: Animator) {

                    }
                })
                (mDatabind.rec.models!![index] as MatchBean).focus = false

                appViewModel.updateCollection.postValue(true)
                //  mDatabind.rec.mutable.removeAt(index)
                // mDatabind.rec.bindingAdapter.notifyItemRemoved(index) // 通知更新
                mview1!!.setBackgroundResource(R.drawable.sc_shoucang_icon1)

            }
            mViewModel.noticeData.observe(this) {

                mview!!.setAnimation("shoucang1.json")
                mview!!.loop(false)
                mview!!.playAnimation()
                mview!!.addAnimatorListener(object :
                    Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator) {

                    }

                    override fun onAnimationEnd(animation: Animator) {
                        mview!!.visibility = View.INVISIBLE
                    }

                    override fun onAnimationCancel(animation: Animator) {

                    }

                    override fun onAnimationRepeat(animation: Animator) {

                    }
                })
                (mDatabind.rec.models!![index] as MatchBean).focus = true
                appViewModel.updateCollection.postValue(false)
                mview1!!.setBackgroundResource(R.drawable.sc_shoucang_icon2)
            }
        } catch (e: Exception) {
        }
    }

}