package com.github.goldy1992.mp3player.client.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.github.goldy1992.mp3player.client.databinding.MaterialDropdownListBinding
import com.google.android.material.textfield.TextInputLayout

class MaterialDropdownList

    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr : Int): TextInputLayout(context, attributeSet, defStyleAttr) {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)

    var adapter : ArrayAdapter<String>? = null

    private val editTextFilledExposedDropdown : AutoCompleteTextView

    init {
        val binding = MaterialDropdownListBinding.inflate(LayoutInflater.from(context))
        this.editTextFilledExposedDropdown = binding.filledExposedDropdown
        addView(binding.root)
    }

    fun addOnItemSelectedListener(onItemSelectedListener: AdapterView.OnItemSelectedListener) {
        this.editTextFilledExposedDropdown.onItemSelectedListener = onItemSelectedListener
    }
}