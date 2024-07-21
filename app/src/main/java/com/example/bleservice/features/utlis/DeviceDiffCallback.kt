package com.example.bleservice.features.utlis

import androidx.recyclerview.widget.DiffUtil
import com.example.bleservice.domain.model.Device

class DeviceDiffCallback : DiffUtil.ItemCallback<Device>() {
    override fun areItemsTheSame(oldItem: Device, newItem: Device): Boolean {
        return oldItem.number == newItem.number
    }

    override fun areContentsTheSame(oldItem: Device, newItem: Device): Boolean {
        return oldItem == newItem
    }
}