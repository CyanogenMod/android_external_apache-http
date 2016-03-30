/*
 * Copyright (c) 2014, The Linux Foundation. All rights reserved.

 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above
      copyright notice, this list of conditions and the following
      disclaimer in the documentation and/or other materials provided
      with the distribution.
    * Neither the name of The Linux Foundation nor the names of its
      contributors may be used to endorse or promote products derived
      from this software without specific prior written permission.

 * THIS SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT
 * ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
 * BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN
 * IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.apache.http.impl.conn;

import dalvik.system.PathClassLoader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.apache.http.conn.ClientConnectionManager;
import java.util.concurrent.TimeUnit;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.quicinc.tcmiface.DpmTcmIface;

/**
 * @hide
 */
public class TcmIdleTimerMonitor implements DpmTcmIface {
  private final Log log = LogFactory.getLog(getClass());
  private org.apache.http.conn.ClientConnectionManager connectionManager;
  private static Object tcmClient = null;
  private static Method mTcmRegisterMethod = null;
  private static Object lockObj = new Object();
  Object result = null;

  /** @hide */
  public TcmIdleTimerMonitor(ClientConnectionManager connManager) {
    synchronized(lockObj) {
      this.connectionManager = connManager;
      //load tcm
      try {
        if (mTcmRegisterMethod == null || tcmClient == null) {
          //load tcm if not already loaded
          PathClassLoader tcmClassLoader =
            new PathClassLoader("/system/framework/tcmclient.jar",
                ClassLoader.getSystemClassLoader());
          Class tcmClass = tcmClassLoader.loadClass("com.qti.tcmclient.DpmTcmClient");
          Method mGetTcmMethod = tcmClass.getDeclaredMethod("getInstance");
          tcmClient = mGetTcmMethod.invoke(null);
          mTcmRegisterMethod = tcmClass.getDeclaredMethod("registerTcmMonitor", DpmTcmIface.class);
        }
        if (mTcmRegisterMethod != null && tcmClient != null) {
          result = mTcmRegisterMethod.invoke(tcmClient, this);
        }
      } catch (ClassNotFoundException e) {
        //Ignore ClassNotFound Exception
      } catch (Exception e) {
        log.debug("Failed to load tcmclient " + e);
      }
    }
  }

  /** @hide */
  public synchronized void OnCloseIdleConn()
  {
    connectionManager.closeIdleConnections(0, TimeUnit.MILLISECONDS);
  }
} // class TcmIdleTimerMonitor
