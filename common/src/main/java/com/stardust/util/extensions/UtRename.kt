package com.stardust.util.extensions

typealias ResultCallback<T> = (Result<T>) -> Unit

typealias ResultSelfCallback<T> = Result<T>.() -> Unit