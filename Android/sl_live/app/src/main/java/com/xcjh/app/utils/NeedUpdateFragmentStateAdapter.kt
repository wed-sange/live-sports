package com.xcjh.app.utils


import androidx.collection.LongSparseArray
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewpager2.adapter.FragmentStateAdapter
import java.lang.Integer.min
import java.util.concurrent.atomic.AtomicLong

/**
 * @author allan
 * Description 用于需要更新Fragment的ViewPager2的Adapter。给你的Fragment实现IFragmentNeedUpdate，
 * 会在生命周期onStart自动回调或者有动态更新的时候通知。因此，不需要编写data和onCreateView的代码。
 */
class NeedUpdateFragmentStateAdapter<T>(fragment: Fragment) : FragmentStateAdapter(fragment) {
    private var datas: ArrayList<T> = ArrayList()
    //与datas对应的Id
    private var myIds:ArrayList<Long> = ArrayList()

    private val idGen:AtomicLong = AtomicLong(0)
    private val nextId
        get() = idGen.incrementAndGet()

    //如果你的是android.util.LongSparseArray自行调整。
    private var mFragmentsParent: LongSparseArray<Fragment>? = null
    private fun mFragmentsGet() : LongSparseArray<Fragment> {
        val mFragments = mFragmentsParent
        if (mFragments == null) {
           // mFragmentsParent = ReflectionUtils.iteratorGetPrivateFieldValue(this, "mFragments").asOrNull<LongSparseArray<Fragment>>()
            return mFragmentsParent!!
        }
        return mFragments
    }

    /**
     * 数据比较器。如果只要出现需要更新的任意局部数据，都返回true。不需要更新则是false。
     */
    var differComparator:((d1:T, d2:T)->Boolean)? = null

    /**
     * Fragment构建器。根据data来创建。
     * 请创建出Fragment，实现IFragmentNeedUpdate。
     */
    var fragmentCreator:((data:T)->IFragmentNeedUpdate<T>)? = null

    fun submitData(newDatas:List<T>) {
        if (fragmentCreator == null) {
            throw RuntimeException("you have not set fragmentCreator.")
        }
        if (differComparator == null) {
            throw RuntimeException("you have not set differComparator.")
        }

        if (idGen.get() == 0L) {
            initData(newDatas)
        } else {
            updateData(newDatas)
        }
    }

    /**
     * 初始化数据
     */
    private fun initData(datas:List<T>) {
        this.datas.clear()
        this.myIds.clear()
        this.datas.addAll(datas)
        var sz = datas.size
        while (sz-->0) {
            myIds.add(nextId)
        }

        notifyDataSetChanged()
    }

    //完全按照新列表，顺序来显示。
    private fun updateData(newDatas:List<T>) {
        val minSize = min(datas.size, newDatas.size)
        val deltaSize = kotlin.math.abs(datas.size - newDatas.size)
        //更新前面的Fragment
        for (i in 0 until minSize) {
            val newData = newDatas[i]
            if (differComparator?.invoke(datas[i], newData) == true) {
                //现在就存在的Fragment，我们需要更新它
                //先拿到老id进行更新
                val value = mFragmentsGet().get(myIds[i]) as IFragmentNeedUpdate<T>
                value. onNeedUpdate (newData)
                myIds[i] = nextId //更新id。放到后面，否则提取不匹配。
            }
            datas[i] = newData //不论如何都要换新数据的。
        }

        //处理后面的数据
        if (newDatas.size < datas.size) {
            var delta = deltaSize
            while(delta-- > 0) {
                datas.removeLast()
                myIds.removeLast()
            }
            notifyItemRangeRemoved(minSize, deltaSize)
        } else if (newDatas.size > datas.size) {
            var delta = deltaSize
            var min = minSize
            while (delta-- > 0) {
                datas.add(newDatas[min++])
                myIds.add(nextId)
            }
            notifyItemRangeInserted(minSize, deltaSize)
        }
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    override fun createFragment(position: Int): Fragment {
        val data = datas[position]
        val fc = fragmentCreator?.invoke(data)
        val fragment = fc as Fragment
        fragment.lifecycle.addObserver(DataLifecycleObserver(data))
        return fragment
    }

    override fun containsItem(itemId: Long): Boolean {
        return myIds.contains(itemId)
    }

    override fun getItemId(position: Int): Long {
        return myIds[position]
    }

    /**
     * 请将你的Fragment实现它。将会通知你更新
     */
    interface IFragmentNeedUpdate<T> {
        fun onNeedUpdate(data:T)
    }

    class DataLifecycleObserver<T>(private val data:T) : DefaultLifecycleObserver {
        private var isInit = false

        override fun onStart(owner: LifecycleOwner) {
            if (!isInit && owner is IFragmentNeedUpdate<*>) {
                isInit = true
                val f = owner as IFragmentNeedUpdate<T>
                f.onNeedUpdate(data)
            }
        }
    }

}
