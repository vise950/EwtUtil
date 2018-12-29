package com.ewt.nicola.common.base

import android.os.Bundle
import android.view.*
import androidx.annotation.MenuRes
import androidx.fragment.app.Fragment
import com.ewt.nicola.common.extension.hideKeyboard

abstract class BaseFragment(
    private val layoutRes: Int? = null,
    @MenuRes private val menuRes: Int? = null
) : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        menuRes?.let { setHasOptionsMenu(true) }

        container?.setOnTouchListener { _, _ ->
            requireActivity().hideKeyboard()
            return@setOnTouchListener false
        }

        return layoutRes?.let {
            inflater.inflate(layoutRes, container, false)
        } ?: run {
            super.onCreateView(inflater, container, savedInstanceState)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        menuRes?.let {
            inflater.inflate(it, menu)
        } ?: run {
            super.onCreateOptionsMenu(menu, inflater)
        }
    }

    open fun initView() {}
}