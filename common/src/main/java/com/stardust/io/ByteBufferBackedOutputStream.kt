package com.stardust.io

import androidx.annotation.Keep
import java.io.IOException
import java.io.OutputStream
import java.nio.ByteBuffer
@Keep

class ByteBufferBackedOutputStream(private var buf: ByteBuffer) : OutputStream() {

    @Throws(IOException::class)
    override fun write(b: Int) {
        buf.put(b.toByte())
    }

    @Throws(IOException::class)
    override fun write(bytes: ByteArray, off: Int, len: Int) {
        buf.put(bytes, off, len)
    }

}