/**
 * BreadWallet
 * <p/>
 * Created by Drew Carlson <drew.carlson@breadwallet.com> on 6/2/2019.
 * Copyright (c) 2019 breadwallet LLC
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.breadwallet.legacy.pricealert

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isVisible

import com.breadwallet.R
import com.breadwallet.legacy.presenter.customviews.BaseTextView
import com.breadwallet.model.TokenItem
import com.breadwallet.tools.util.TokenUtil
import com.squareup.picasso.Picasso

import java.io.File
import java.util.Locale

class SelectAlertTokenAdapter(
    private val context: Context,
    private val onSelectListener: (@ParameterName("token") TokenItem) -> Unit
) : RecyclerView.Adapter<SelectAlertTokenAdapter.TokenItemViewHolder>() {

    companion object {
        private val TAG = SelectAlertTokenAdapter::class.java.simpleName
    }

    var tokenItems: List<TokenItem> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onBindViewHolder(holder: TokenItemViewHolder, position: Int) {
        val item = tokenItems[position]
        val currencyCode = item.symbol.toLowerCase(Locale.ROOT)
        val tokenIconPath = TokenUtil.getTokenIconPath(currencyCode, true)

        val iconDrawable = holder.iconParent.background as GradientDrawable

        if (tokenIconPath == null) {
            // If no icon is present, then use the capital first letter of the token currency code instead.
            holder.iconLetter.visibility = View.VISIBLE
            iconDrawable.setColor(Color.parseColor(item.startColor))
            holder.iconLetter.text = currencyCode.substring(0, 1).toUpperCase()
            holder.logo.visibility = View.GONE
        } else {
            val iconFile = File(tokenIconPath)
            Picasso.get().load(iconFile).into(holder.logo)
            holder.iconLetter.visibility = View.GONE
            holder.logo.visibility = View.VISIBLE
            iconDrawable.setColor(Color.TRANSPARENT)
        }

        holder.name.text = item.name
        holder.symbol.text = item.symbol

        holder.itemView.setOnClickListener {
            onSelectListener.invoke(tokenItems[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int = tokenItems.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TokenItemViewHolder {
        val inflater = (context as Activity).layoutInflater
        val convertView = inflater.inflate(R.layout.token_list_item, parent, false)

        val holder = TokenItemViewHolder(convertView)
        holder.setIsRecyclable(false)

        return holder
    }

    inner class TokenItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val logo: ImageView = view.findViewById(R.id.token_icon)
        val symbol: BaseTextView = view.findViewById(R.id.token_symbol)
        val name: BaseTextView = view.findViewById(R.id.token_name)
        val iconParent: View = view.findViewById(R.id.icon_parent)
        val iconLetter: BaseTextView = view.findViewById(R.id.icon_letter)

        init {
            view.findViewById<View>(R.id.add_remove_button).isVisible = false
        }
    }
}
