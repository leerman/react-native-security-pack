import { NativeModules, Platform } from 'react-native';

const LINKING_ERROR =
  `The package 'react-native-security-pack' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

// @ts-expect-error
const isTurboModuleEnabled = global.__turboModuleProxy != null;

const SecurityPackModule = isTurboModuleEnabled
  ? require('./NativeSecurityPack').default
  : NativeModules.SecurityPack;

const SecurityPack = SecurityPackModule
  ? SecurityPackModule
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

async function getSignatures(): Promise<string[]> {
  return (await SecurityPack.getSignatures()).map((item: string) =>
    item.toUpperCase()
  );
}

export async function containsSignatures(sigs: string[]): Promise<boolean> {
  if (Platform.OS === 'ios') return true;

  const signatures: string[] = await getSignatures();
  console.log({ signatures });
  const sigUpper = sigs.map((item) => item.toUpperCase());
  return signatures.some((item) => sigUpper.includes(item));
}

export async function isRooted(): Promise<boolean> {
  return SecurityPack.isRooted();
}
