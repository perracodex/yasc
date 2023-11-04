/*
 * Copyright (c) 2023 Perraco Labs. All rights reserved.
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>
 */

package com.perracolabs.yasc.system.debug

import android.util.Log
import com.perracolabs.yasc.BuildConfig

/**
 * A simple [Log] wrapper that abstracts application tracing from the default implementation.
 * <p>
 * In addition allows to easily replace the application default tracing by other logging
 * implementations, or even show more customized logs.
 */
@Suppress("unused")
object Tracer {

    private val IS_ENABLED = BuildConfig.DEBUG

    fun v(tag: String, msg: String) {
        if (IS_ENABLED)
            Log.v(tag, msg)
    }

    fun v(tag: String, msg: String, tr: Throwable) {
        if (IS_ENABLED)
            Log.v(tag, msg, tr)
    }

    fun d(tag: String, msg: String) {
        if (IS_ENABLED)
            Log.d(tag, msg)
    }

    fun d(tag: String, msg: String, tr: Throwable) {
        if (IS_ENABLED)
            Log.d(tag, msg, tr)
    }

    fun i(tag: String, msg: String) {
        if (IS_ENABLED)
            Log.i(tag, msg)
    }

    fun i(tag: String, msg: String, tr: Throwable) {
        if (IS_ENABLED)
            Log.i(tag, msg, tr)
    }

    fun w(tag: String, msg: String) {
        if (IS_ENABLED)
            Log.w(tag, msg)
    }

    fun w(tag: String, msg: String, tr: Throwable) {
        if (IS_ENABLED)
            Log.w(tag, msg, tr)
    }

    fun e(tag: String, msg: String) {
        if (IS_ENABLED)
            Log.e(tag, msg)
    }

    fun e(tag: String, msg: String, tr: Throwable) {
        if (IS_ENABLED)
            Log.e(tag, msg, tr)
    }
}
