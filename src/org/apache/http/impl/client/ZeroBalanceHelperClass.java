/*
 ** Copyright (c) 2015-16, The Linux Foundation. All rights reserved.

 ** Redistribution and use in source and binary forms, with or without
 ** modification, are permitted provided that the following conditions are
 ** met:
 **     * Redistributions of source code must retain the above copyright
 **       notice, this list of conditions and the following disclaimer.
 **     * Redistributions in binary form must reproduce the above
 **       copyright notice, this list of conditions and the following
 **       disclaimer in the documentation and/or other materials provided
 **       with the distribution.
 **     * Neither the name of The Linux Foundation nor the names of its
 **       contributors may be used to endorse or promote products derived
 **       from this software without specific prior written permission.

 ** THIS SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESS OR IMPLIED
 ** WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 ** MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT
 ** ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS
 ** BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 ** CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 ** SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
 ** BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 ** WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 ** OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN
 ** IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.apache.http.impl.client;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class ZeroBalanceHelperClass {

    protected static boolean getFeatureFlagValue() {
        try {
          Class<?> ZeroBalanceHelper = Class
              .forName("android.net.ZeroBalanceHelper");
          Object helperObject = ZeroBalanceHelper.newInstance();
          boolean isFeatureEnabled = false;
          Method featureFlagValue = ZeroBalanceHelper.getMethod(
              "getFeatureConfigValue");
          featureFlagValue.setAccessible(true);
          isFeatureEnabled = (Boolean)featureFlagValue.invoke(helperObject);
          return isFeatureEnabled;
        } catch (ClassNotFoundException ex) {
           ex.printStackTrace();
        } catch (LinkageError ex) {
           ex.printStackTrace();
        } catch (NoSuchMethodException ex) {
           ex.printStackTrace();
        } catch (InstantiationException ex) {
           ex.printStackTrace();
        } catch (IllegalAccessException ex) {
           ex.printStackTrace();
        } catch (InvocationTargetException ex) {
           ex.printStackTrace();
        }
        return false;
    }

    protected static boolean getBackgroundDataProperty() {
        try {
            Class<?> ZeroBalanceHelper = Class
                .forName("android.net.ZeroBalanceHelper");
            Object helperObject = ZeroBalanceHelper.newInstance();
            boolean isDisabled = false;
            Method bgDataProperty = ZeroBalanceHelper.getMethod(
                "getBgDataProperty");
            bgDataProperty.setAccessible(true);
            isDisabled = Boolean.valueOf(((String)bgDataProperty.invoke(helperObject)));
            return isDisabled;
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (LinkageError ex) {
            ex.printStackTrace();
        } catch (NoSuchMethodException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InvocationTargetException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    protected static void setHttpRedirectCount(String url) {
        try {
            Class<?> ZeroBalanceHelper = Class
                .forName("android.net.ZeroBalanceHelper");
            Object helperObject = ZeroBalanceHelper.newInstance();
            Method setHttpRedirectCount = ZeroBalanceHelper.getMethod(
                "setHttpRedirectCount", String.class);
            setHttpRedirectCount.setAccessible(true);
            setHttpRedirectCount.invoke(helperObject, url);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (LinkageError ex) {
            ex.printStackTrace();
        } catch (NoSuchMethodException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InvocationTargetException ex) {
            ex.printStackTrace();
        }
    }
}
