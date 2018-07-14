package com.finitemonkey.dougb.nflcrimewatch.data.converters;

import android.support.annotation.Nullable;

import com.squareup.moshi.FromJson;

public class NullPrimitiveJsonAdapter {
    @FromJson
    public int intFromJson(@Nullable Integer value) {
        if (value == null) {
            return 0;
        }
        return value;
    }
}
