package moka.land.base.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

abstract class _BaseAdapter<DATA : _ItemData, VIEW : _RecyclerItemView<DATA>> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onClickItem: ((DATA) -> Unit)? = null

    var items = mutableListOf<DATA>()
        private set

    fun setItems(items: List<DATA>) {
        this.items.clear()
        this.items.addAll(items)
        this.notifyDataSetChanged()
    }

    fun replaceItems(items: List<DATA>) {
        val diffCallback = getDiffCallback(items)
        val result = DiffUtil.calculateDiff(diffCallback)

        this.items.clear()
        this.items.addAll(items)
        result.dispatchUpdatesTo(this)
    }

    fun addItems(items: List<DATA>) {
        val startIndex = this.items.size
        val offsetIndex = items.size

        this.items.addAll(items)
        this.notifyItemRangeInserted(startIndex, offsetIndex)
    }

    open fun getDiffCallback(items: List<DATA>): DiffUtil.Callback {
        return _DiffUtilCallback(this.items, items)
    }

    override fun getItemCount(): Int = items.size

    abstract fun onCreateItemViewHolder(parent: ViewGroup, viewType: Int): VIEW

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return onCreateItemViewHolder(parent, viewType).apply {
            itemView.setOnClickListener {
                onClickItem()
                onClickItem?.invoke(data)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemView = holder as VIEW
        if (0 < position) {
            itemView.preData = items[position - 1]
        }
        else {
            itemView.preData = null
        }

        if (items.size > position + 1) {
            itemView.afterData = items[position + 1]
        }
        else {
            itemView.afterData = null
        }

        val data = items[position]
        itemView.index = position
        itemView.data = data
        itemView.refreshView()
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        val itemView = holder as? VIEW
        itemView?.onRecycled()
        super.onViewRecycled(holder)
    }

}
