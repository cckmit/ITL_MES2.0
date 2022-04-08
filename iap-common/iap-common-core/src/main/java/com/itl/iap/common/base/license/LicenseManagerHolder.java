package com.itl.iap.common.base.license;

import de.schlichtherle.license.LicenseManager;
import de.schlichtherle.license.LicenseParam;

/**
 *
 *
 * @author 汤俊
 * @date 2020-1-9 14:31
 * @since 1.0.0
 */
public class LicenseManagerHolder {

    private static volatile LicenseManager licenseManager = null;

    private LicenseManagerHolder() {
    }

    public static LicenseManager getLicenseManager(LicenseParam param) {
        if (licenseManager == null) {
            synchronized (LicenseManagerHolder.class) {
                if (licenseManager == null) {
                    licenseManager = new LicenseManager(param);
                }
            }
        }
        return licenseManager;
    }
}
