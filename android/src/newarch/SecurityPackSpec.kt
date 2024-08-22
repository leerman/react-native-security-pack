package com.leerman.rnsecuritypack

import com.facebook.react.bridge.ReactApplicationContext

abstract class SecurityPackSpec internal constructor(context: ReactApplicationContext) :
  NativeSecurityPackSpec(context) {
}
