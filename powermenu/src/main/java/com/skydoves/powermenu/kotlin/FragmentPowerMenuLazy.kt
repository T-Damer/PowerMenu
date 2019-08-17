/*
 * Copyright (C) 2017 skydoves
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.skydoves.powermenu.kotlin

import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.skydoves.powermenu.PowerMenu
import kotlin.reflect.KClass

/**
 * An implementation of [Lazy] used by [Fragment].
 *
 * tied to the given [lifecycleOwner], [clazz].
 */
class FragmentPowerMenuLazy<out T : PowerMenu.Factory>(
  private val fragment: Fragment,
  private val lifecycleOwner: LifecycleOwner,
  private val clazz: KClass<T>
) : Lazy<PowerMenu?> {

  private var cached: PowerMenu? = null

  override val value: PowerMenu?
    get() {
      var instance = cached
      if (instance == null && fragment.context != null) {
        val factory = clazz::java.get().newInstance()
        instance = factory.create(fragment.requireContext(), lifecycleOwner)
        cached = instance
      }

      return instance
    }

  override fun isInitialized() = cached != null
}
