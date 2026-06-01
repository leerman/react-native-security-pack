import { Platform } from 'react-native';
import { NitroModules } from 'react-native-nitro-modules';
import type { SecurityPack as SecurityPackSpec } from './specs/SecurityPack.nitro';

let securityPack: SecurityPackSpec | null = null;

function getSecurityPack(): SecurityPackSpec {
  if (securityPack == null) {
    securityPack =
      NitroModules.createHybridObject<SecurityPackSpec>('SecurityPack');
  }
  return securityPack;
}

async function getSignatures(): Promise<string[]> {
  const signatures = await getSecurityPack().getSignatures();
  return signatures.map((item) => item.toUpperCase());
}

/**
 * Checks whether the app's signing certificate matches any of the given SHA-1
 * fingerprints (hex, case-insensitive).
 *
 * On iOS this always returns `true` because APK signatures are Android-only.
 */
export async function containsSignatures(sigs: string[]): Promise<boolean> {
  if (Platform.OS === 'ios') {
    return true;
  }

  const signatures = await getSignatures();
  const expected = sigs.map((item) => item.toUpperCase());
  return signatures.some((item) => expected.includes(item));
}

/** Returns `true` when the device appears to be rooted (Android) or jailbroken (iOS). */
export async function isRooted(): Promise<boolean> {
  return await getSecurityPack().isRooted();
}
