/*
Copyright (c) 2015, The Linux Foundation. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are
met:
    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above
      copyright notice, this list of conditions and the following
      disclaimer in the documentation and/or other materials provided
      with the distribution.
    * Neither the name of The Linux Foundation nor the names of its
      contributors may be used to endorse or promote products derived
      from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESS OR IMPLIED
WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT
ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS
BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN
IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package org.apache.http.impl.auth;

import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.auth.AuthenticationException;

import dalvik.system.PathClassLoader;
import java.lang.reflect.Constructor;

/**
 * This class is the main entrance to communicate with gba service,
 * once created, it will load the private library.
 * Client should get the single instance of this class.
 */

/**@hide */
public class GbaCredentialsCreator {

    private GbaInfoProvider gip = null;
    /*private library name and path*/
    private final String realProvider = "com.qualcomm.qti.GBAHttpAuthentication.QtiGbaInfoProvider";
    private final String realProviderPath = "/system/framework/com.qualcomm.qti.GBAHttpAuthentication.jar";

    private GbaUsernamePassword mBtidCache = null;

    private static GbaCredentialsCreator mInstance = null;

    /**
     * singleton pattern, every process should only have one instance.
     * @return the single instance
     */
    public static GbaCredentialsCreator getInstance(){
        if(mInstance == null){
            synchronized(GbaCredentialsCreator.class){
                if(mInstance == null){
                    mInstance = new GbaCredentialsCreator();
                }
            }
        }
        return mInstance;
    }

    /**
     * private constructor, load private gba library.
     */
    private GbaCredentialsCreator(){
        loadPrivateGbaInfoProvider();
    }

    /**
     * load private gba library, if it should be disabled, just remove
     * the jar at defined path.
     */
    private void loadPrivateGbaInfoProvider() {
        PathClassLoader classLoader = new PathClassLoader(realProviderPath,ClassLoader.getSystemClassLoader());
        try{
            Class provider = classLoader.loadClass(realProvider);
            gip = (GbaInfoProvider)provider.newInstance();
        }catch(Exception e){
        }
    }

    /**
     * return credential based on naf and ciphersuite.
     * @param naf network application function name
     * @param param ciphersuite cipher algorithm name that ssl is using
     * @return credential
     * @throws AuthenticationException if gba service can't return credential
     */
    public Credentials getUsernamePassword(String naf,String ciphersuite) throws AuthenticationException{
        String btid="";
        String pw="";
        try{
            mBtidCache = gip.getUsernamePassword(naf, ciphersuite);
            btid = mBtidCache.getUsername();
            pw = mBtidCache.getPassword();
        }catch(AuthenticationException ae){
            throw ae;
        }catch(Exception e){
            throw new AuthenticationException("fail to get info from gba");
        }

        StringBuilder sb = new StringBuilder();
        sb.append(btid);
        sb.append(":");
        sb.append(pw);
        return new UsernamePasswordCredentials(sb.toString());
    }

    /**
     * return gba specific user agent
     * @return gba user agent, null if not applicable
     */
    public String getGbaUserAgent() {
        String gbaUserAgent = null;
        try {
            gbaUserAgent = gip.getGbaUserAgent();
        } catch (Exception e) {
        }
        return gbaUserAgent;
    }
}


