package com.margelo.nitro.rnsecuritypack

import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.annotation.Keep
import com.facebook.proguard.annotations.DoNotStrip
import com.kimchangyoun.rootbeerFresh.RootBeer
import com.margelo.nitro.NitroModules
import com.margelo.nitro.core.Promise
import java.security.MessageDigest

@Keep
@DoNotStrip
class HybridSecurityPack : HybridSecurityPackSpec() {
  private val context =
    NitroModules.applicationContext
      ?: throw IllegalStateException("Android application context is not available")

  override fun getSignatures(): Promise<Array<String>> {
    Log.d(TAG, "getSignatures()")
    return Promise.async {
      try {
        val packageName = context.packageName
        val signatures =
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val signingInfo =
              context.packageManager.getPackageInfo(
                packageName,
                PackageManager.GET_SIGNING_CERTIFICATES,
              ).signingInfo
                ?: return@async emptyArray()

            if (signingInfo.hasMultipleSigners()) {
              signingInfo.apkContentsSigners
            } else {
              signingInfo.signingCertificateHistory
            }
          } else {
            @Suppress("DEPRECATION")
            context.packageManager.getPackageInfo(
              packageName,
              PackageManager.GET_SIGNATURES,
            ).signatures ?: emptyArray()
          }

        signatures
          .map { certificate ->
            val digest = MessageDigest.getInstance("SHA")
            digest.update(certificate.toByteArray())
            bytesToHex(digest.digest())
          }
          .toTypedArray()
      } catch (e: Exception) {
        Log.e(TAG, "getSignatures failed", e)
        emptyArray()
      }
    }
  }

  override fun isRooted(): Promise<Boolean> {
    Log.d(TAG, "isRooted()")
    return Promise.async {
      RootBeer(context).isRooted
    }
  }

  private fun bytesToHex(bytes: ByteArray): String {
    val hexArray = charArrayOf(
      '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
      'A', 'B', 'C', 'D', 'E', 'F',
    )
    val hexChars = CharArray(bytes.size * 2)
    for (j in bytes.indices) {
      val v = bytes[j].toInt() and 0xFF
      hexChars[j * 2] = hexArray[v ushr 4]
      hexChars[j * 2 + 1] = hexArray[v and 0x0F]
    }
    return String(hexChars)
  }

  companion object {
    private const val TAG = "HybridSecurityPack"
  }
}
