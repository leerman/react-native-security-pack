import type { HybridObject } from 'react-native-nitro-modules';

export interface SecurityPack
  extends HybridObject<{ ios: 'swift'; android: 'kotlin' }> {
  getSignatures(): Promise<string[]>;
  isRooted(): Promise<boolean>;
}
