import DTTJailbreakDetection
import NitroModules

class HybridSecurityPack: HybridSecurityPackSpec {
  func getSignatures() throws -> Promise<[String]> {
    // APK signing certificates are Android-only.
    return Promise.async {
      []
    }
  }

  func isRooted() throws -> Promise<Bool> {
    return Promise.async {
      DTTJailbreakDetection.isJailbroken()
    }
  }
}
