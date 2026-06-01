import { Platform } from 'react-native';
import { containsSignatures, isRooted } from '../index';

jest.mock('react-native-nitro-modules', () => ({
  NitroModules: {
    createHybridObject: () => ({
      getSignatures: jest
        .fn()
        .mockResolvedValue(['5E8F16062EA3CD2C4A0D547876BAA6F38CABF625']),
      isRooted: jest.fn().mockResolvedValue(false),
    }),
  },
}));

describe('react-native-security-pack', () => {
  it('containsSignatures returns true on iOS', async () => {
    Platform.OS = 'ios';
    await expect(containsSignatures(['abc'])).resolves.toBe(true);
  });

  it('containsSignatures matches Android signatures', async () => {
    Platform.OS = 'android';
    await expect(
      containsSignatures(['5e8f16062ea3cd2c4a0d547876baa6f38cabf625'])
    ).resolves.toBe(true);
    await expect(containsSignatures(['deadbeef'])).resolves.toBe(false);
  });

  it('isRooted delegates to native', async () => {
    await expect(isRooted()).resolves.toBe(false);
  });
});
