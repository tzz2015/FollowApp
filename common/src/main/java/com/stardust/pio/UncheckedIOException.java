package com.stardust.pio;

import androidx.annotation.Keep;

import java.io.IOException;

/**
 * Created by Stardust on 2017/4/1.
 */
@Keep

public class UncheckedIOException extends RuntimeException {

    public UncheckedIOException(IOException cause) {
        super(cause);
    }

    @Override
    public synchronized IOException getCause() {
        return (IOException) super.getCause();
    }
}
