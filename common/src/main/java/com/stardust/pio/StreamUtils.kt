package com.stardust.pio

import androidx.annotation.Keep
import java.io.InputStream
import java.io.OutputStream
@Keep

fun InputStream.copyToAndClose(out:OutputStream){
    this.use { input->
        out.use { out->
            input.copyTo(out)
        }
    }
}