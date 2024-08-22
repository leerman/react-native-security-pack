import type { TurboModule } from 'react-native';
import { TurboModuleRegistry } from 'react-native';

export interface Spec extends TurboModule {
  getSignatures(): Promise<string[]>;
  isRooted(): Promise<boolean>;
}

export default TurboModuleRegistry.getEnforcing<Spec>('SecurityPack');
