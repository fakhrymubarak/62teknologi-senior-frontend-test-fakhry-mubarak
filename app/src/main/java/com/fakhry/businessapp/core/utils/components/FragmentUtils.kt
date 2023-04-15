package com.fakhry.businessapp.core.utils.components

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch

fun Fragment.onBackPressed() {
    this.requireActivity().onBackPressedDispatcher.onBackPressed()
}

fun Fragment.finishAffinity() {
    this.requireActivity().finishAffinity()
}

/**
 * Flow collector for Fragment
 * @see [launch]
 *
 * @param flow flow to be collected
 * @param collect lambda function to collect items
 * */
fun <T> Fragment.collectLifecycleFlow(flow: Flow<T>, collect: FlowCollector<T>) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collect(collect)
        }
    }
}


/**
 * Flow collector for BottomSheetDialogFragment
 * @see [launch]
 *
 * @param flow flow to be collected
 * @param collect lambda function to collect items
 * */
fun <T> BottomSheetDialogFragment.collectLifecycleFlow(flow: Flow<T>, collect: FlowCollector<T>) {
    this.lifecycleScope.launch {
        flow.collect(collect)
    }
}

fun Fragment.showToast(message: String) {
    this.requireContext().showToast(message)
}