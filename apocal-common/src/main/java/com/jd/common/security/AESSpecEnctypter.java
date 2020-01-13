package com.jd.common.security;

import java.security.Key;
import javax.crypto.spec.SecretKeySpec;

public class AESSpecEnctypter extends AESEncrypter {

  public AESSpecEnctypter(String aesKey) throws RuntimeException {
    super(aesKey);
  }

  public AESSpecEnctypter(String aesKey, boolean useHex) throws RuntimeException {
    super(aesKey, useHex);
  }

  public AESSpecEnctypter(String aesKey, String b64Key, String workPattern, String paddingPattern,
      String ivParameter) throws RuntimeException {
    super(aesKey, b64Key, workPattern, paddingPattern, ivParameter);
  }

  @Override
  public Key generateKey() throws Exception {
    return new SecretKeySpec(aesKey.getBytes(), KEY_ALGORITHM);
  }

}
