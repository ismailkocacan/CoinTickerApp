package com.app.binancealarm;

import java.io.Serializable;

public interface Refreshable extends Serializable {
    void doRefresh();
}