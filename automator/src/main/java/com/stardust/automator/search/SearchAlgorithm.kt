package com.stardust.automator.search

import androidx.annotation.Keep
import com.stardust.automator.UiObject
import com.stardust.automator.filter.Filter
@Keep
interface SearchAlgorithm {

    fun search(root: UiObject, filter: Filter, limit: Int = Int.MAX_VALUE): ArrayList<UiObject>
}