package com.xcjh.app.adapter

import android.content.Context
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.QuickViewHolder
import com.xcjh.app.R
import com.xcjh.app.bean.LiveTextBean
import com.xcjh.base_lib.utils.view.visibleOrGone
import com.xcjh.base_lib.utils.view.visibleOrInvisible

/**
 * 文字直播
 */
class TextLiveAdapter : BaseQuickAdapter<LiveTextBean, QuickViewHolder>() {

    override fun onCreateViewHolder(
        context: Context, parent: ViewGroup, viewType: Int,
    ): QuickViewHolder {
        return QuickViewHolder(R.layout.item_text_live, parent)
    }

    private var homeLogo: String?=""
    private var awayLogo: String?=""

    fun setLogo(homeLogo: String?, awayLogo: String?) {
        this.homeLogo = homeLogo
        this.awayLogo = awayLogo
    }

    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: LiveTextBean?) {
        if (item != null) {
            val tvContent = holder.getView<TextView>(R.id.tv_content)
            tvContent.text = item.data
            val ivType = holder.getView<ImageView>(R.id.iv_type)
            val ivLogo = holder.getView<ImageView>(R.id.iv_logo)
            val lineView = holder.getView<View>(R.id.lineView)
            //事件发生方，0-中立、1-主队、2-客队
            ivLogo.visibleOrGone(item.position != 0)
            when (item.position) {
                1 -> {//def_football    default_team_logo
                    Glide.with(context).load(homeLogo).placeholder(R.drawable.def_football).into(ivLogo)
                }
                2 -> {
                    Glide.with(context).load(awayLogo).placeholder(R.drawable.def_football).into(ivLogo)
                }
            }
            when (item.type) {
                //技术统计类型：  1-进球  2-角球  3-黄牌  4-红牌  5-越位  
                // 6-任意球  7-球门球  8-点球  9-换人  10-比赛开始  
                // 11-中场  12-结束  13-半场比分  15-两黄变红  
                // 16-点球未进  17-乌龙球  18-助攻  19-伤停补时  
                // 21-射正  22-射偏  23-进攻  24-危险进攻  25-控球率  
                // 26-加时赛结束  27-点球大战结束  28-VAR(视频助理裁判)  
                // 29-点球(点球大战)  30-点球未进(点球大战)
                1 -> {
                    ivType.setImageResource(R.drawable.sk_jingqiu_icon)
                }
                2 -> {
                    ivType.setImageResource(R.drawable.sk_jiaoqiu)
                }
                3 ->
                    ivType.setImageResource(R.drawable.yellow_card)
                4 ->
                    ivType.setImageResource(R.drawable.red_card)
                5 ->
                    ivType.setImageResource(R.drawable.default_txt_placeholder)
                6 ->
                    ivType.setImageResource(R.drawable.default_txt_placeholder)
                7 ->
                    ivType.setImageResource(R.drawable.default_txt_placeholder)
                8 ->
                    ivType.setImageResource(R.drawable.sk_dianqiu_icon)//
                9 ->
                    ivType.setImageResource(R.drawable.sk_huanren_icon)
                10 ->
                    ivType.setImageResource(R.drawable.ic_match_start)
                12 ->
                    ivType.setImageResource(R.drawable.ic_match_start)
                15 ->//两黄变红
                    ivType.setImageResource(R.drawable.sk_lianghuangyihong_icon)
                16 ->
                    ivType.setImageResource(R.drawable.sk_dianqiuweijing_icon)
                17 ->
                    ivType.setImageResource(R.drawable.sk_wulongqiu_icon)//
                18 ->
                    ivType.setImageResource(R.drawable.sk_zhugong_icon)
                21 ->
                    ivType.setImageResource(R.drawable.sk_shezheng_icon)
                22 ->
                    ivType.setImageResource(R.drawable.sk_shepian_icon)
                28 ->
                    ivType.setImageResource(R.drawable.sk_var_icon)
                29 ->
                    ivType.setImageResource(R.drawable.sk_dianqiu_icon)
                30 ->
                    ivType.setImageResource(R.drawable.sk_dianqiuweijing_icon)
                else -> {
                    ivType.setImageResource(R.drawable.default_txt_placeholder)
                }
            }
            //隐藏最后的进度线
            lineView.visibleOrGone(holder.absoluteAdapterPosition != items.size - 1)
        }
    }
}