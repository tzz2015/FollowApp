package com.stardust.autojs.engine.preprocess;

import androidx.annotation.Keep;

import java.io.IOException;
import java.io.Reader;

/**
 * Created by Stardust on 2017/5/15.
 */
@Keep
public interface Preprocessor {

    Reader preprocess(Reader reader) throws IOException;
}
