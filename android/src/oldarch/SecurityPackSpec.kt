package com.leerman.rnsecuritypack

import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.Promise

abstract class SecurityPackSpec internal constructor(context: ReactApplicationContext) :
  ReactContextBaseJavaModule(context) {

 abstract fun getSignatures(promise: Promise)
}
