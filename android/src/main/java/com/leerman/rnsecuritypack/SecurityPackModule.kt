package com.leerman.rnsecuritypack

import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.Promise

import android.os.Build
import android.content.pm.PackageManager

import java.security.MessageDigest

class SecurityPackModule internal constructor(context: ReactApplicationContext) :
  SecurityPackSpec(context) {

  override fun getName(): String {
    return NAME
  }

  companion object {
    const val NAME = "SecurityPack"
  }

  private val mContext: ReactApplicationContext

  init {
    mContext = context
  }

  @ReactMethod
  override fun getSignatures(promise: Promise) {
    val signatureList: List<String>
    try {
      val packageName = mContext.getPackageName()
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
          // New signature
          val sig = mContext.packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNING_CERTIFICATES).signingInfo
          signatureList = if (sig.hasMultipleSigners()) {
              // Send all with apkContentsSigners
              sig.apkContentsSigners.map {
                  val digest = MessageDigest.getInstance("SHA")
                  digest.update(it.toByteArray())
                  bytesToHex(digest.digest())
              }
          } else {
              // Send one with signingCertificateHistory
              sig.signingCertificateHistory.map {
                  val digest = MessageDigest.getInstance("SHA")
                  digest.update(it.toByteArray())
                  bytesToHex(digest.digest())
              }
          }
      } else {
          val sig = mContext.packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES).signatures
          signatureList = sig.map {
              val digest = MessageDigest.getInstance("SHA")
              digest.update(it.toByteArray())
              bytesToHex(digest.digest())
          }
      }
      val array = Arguments.fromList(signatureList)
      promise.resolve(array)
    } catch (e: Exception) {
        // Handle error
    }

    val empty = Arguments.fromList(emptyList<String>())
    promise.resolve(empty)
  }

  fun bytesToHex(bytes: ByteArray): String {
    val hexArray = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F')
    val hexChars = CharArray(bytes.size * 2)
    var v: Int
    for (j in bytes.indices) {
        v = bytes[j].toInt() and 0xFF
        hexChars[j * 2] = hexArray[v.ushr(4)]
        hexChars[j * 2 + 1] = hexArray[v and 0x0F]
    }
    return String(hexChars)
  }
}
