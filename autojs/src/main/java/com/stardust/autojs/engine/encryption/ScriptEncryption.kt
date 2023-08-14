package com.stardust.autojs.engine.encryption

import androidx.annotation.Keep
import com.stardust.util.AdvancedEncryptionStandard
@Keep
object ScriptEncryption{
    var mKey: String = ""
    var mInitVector: String = ""

    fun decrypt(bytes: ByteArray, start: Int = 0, end: Int = bytes.size): ByteArray {
//        Log.d(TAG, "key: $mKey")
//        Log.d(TAG, "iv: $mInitVector")
        return AdvancedEncryptionStandard(mKey.toByteArray(), mInitVector).decrypt(
            bytes,
            start,
            end
        )
    }

    val TAG=ScriptEncryption::class.java.simpleName
}