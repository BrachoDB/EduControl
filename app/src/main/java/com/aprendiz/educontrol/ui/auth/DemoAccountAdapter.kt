package com.aprendiz.educontrol.ui.auth

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aprendiz.educontrol.databinding.ItemDemoAccountBinding

class DemoAccountAdapter(
    private val accounts: List<DemoAccountModel>,
    private val onAccountClick: (DemoAccountModel) -> Unit
) : RecyclerView.Adapter<DemoAccountAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDemoAccountBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(accounts[position])
    }

    override fun getItemCount(): Int = accounts.size

    inner class ViewHolder(private val binding: ItemDemoAccountBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(account: DemoAccountModel) {
            binding.tvAccountName.text = account.name
            binding.tvAccountDesc.text = account.description
            binding.tvAvatarIcon.text = account.avatarEmoji

            binding.cardAccount.setOnClickListener {
                onAccountClick(account)
            }
        }
    }
}
