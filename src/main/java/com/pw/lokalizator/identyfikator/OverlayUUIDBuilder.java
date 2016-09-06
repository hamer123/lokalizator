package com.pw.lokalizator.identyfikator;

import java.util.regex.Pattern;

/**
 * Created by wereckip on 30.08.2016.
 */

public interface OverlayUUIDBuilder {

    String regex();
    String uuid();
    Pattern pattern();
}
