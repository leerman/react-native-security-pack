package com.securitypack

import com.facebook.react.bridge.ReactApplicationContext

abstract class SecurityPackSpec internal constructor(context: ReactApplicationContext) :
  NativeSecurityPackSpec(context) {
}
