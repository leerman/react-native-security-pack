# react-native-security-pack

Библиотека для React Native с проверками безопасности на устройстве:

- **Android** — определение root и сверка SHA-1 отпечатков подписи APK
- **iOS** — определение jailbreak (проверка подписи APK не применима)

Реализована на [Nitro Modules](https://nitro.margelo.com/) (JSI, New Architecture). Используйте **v1** для Bridge-архитектуры RN.

## Требования

| Зависимость | Версия |
|-------------|--------|
| React Native | `>= 0.75` |
| New Architecture | **обязательна** (`newArchEnabled=true`) |
| [react-native-nitro-modules](https://github.com/mrousavy/nitro) | `>= 0.35` |
| iOS | `>= 15.1` |
| Android | `>= 24` (Android 7.0 Nougat) |

## Установка

```sh
yarn add react-native-security-pack react-native-nitro-modules
# или
npm install react-native-security-pack react-native-nitro-modules
```

### Android

В `android/gradle.properties` приложения:

```properties
newArchEnabled=true
```

Пересоберите проект:

```sh
cd android && ./gradlew clean
```

### iOS

```sh
cd ios && pod install
```

Убедитесь, что в Podfile включена New Architecture (по умолчанию в RN 0.76+).

## API

### `isRooted(): Promise<boolean>`

Возвращает `true`, если устройство, по мнению нативных эвристик, скомпрометировано:

- **Android** — [RootBeer Fresh](https://github.com/kimchangyoun/rootbeerFresh)
- **iOS** — [DTTJailbreakDetection](https://github.com/thii/DTTJailbreakDetection)

```ts
import { isRooted } from 'react-native-security-pack';

const rooted = await isRooted();
if (rooted) {
  // заблокировать чувствительный сценарий
}
```

### `containsSignatures(signatures: string[]): Promise<boolean>`

Проверяет, совпадает ли **хотя бы один** переданный SHA-1 отпечаток с сертификатом, которым подписан установленный APK.

- Отпечатки можно передавать в любом регистре — сравнение нечувствительно к регистру.
- На **iOS** всегда возвращает `true` (у iOS нет APK-подписи; используйте другие механизмы защиты).

```ts
import { containsSignatures } from 'react-native-security-pack';

const releaseKey = '5E8F16062EA3CD2C4A0D547876BAA6F38CABF625';

const valid = await containsSignatures([releaseKey]);
if (!valid) {
  // возможная пересборка / репак APK
}
```

#### Как получить SHA-1 подписи

```sh
# debug keystore (пример)
keytool -list -v -keystore android/app/debug.keystore -alias androiddebugkey -storepass android -keypass android

# release (ваш keystore)
keytool -list -v -keystore your-release.keystore -alias your-alias
```

Скопируйте значение **SHA1** без двоеточий или с ними — библиотека нормализует регистр.

## Пример

```tsx
import { useEffect, useState } from 'react';
import { containsSignatures, isRooted } from 'react-native-security-pack';

export function SecurityCheck() {
  const [state, setState] = useState({ rooted: false, signatureOk: false });

  useEffect(() => {
    isRooted().then((rooted) => setState((s) => ({ ...s, rooted })));
    containsSignatures(['YOUR_RELEASE_SHA1']).then((signatureOk) =>
      setState((s) => ({ ...s, signatureOk }))
    );
  }, []);

  return null;
}
```

Запуск example-приложения из корня репозитория:

```sh
yarn install
yarn prepare   # nitrogen + сборка JS
yarn example android
yarn example ios
```

## Миграция с v1

| v1 | v2 |
|----|-----|
| TurboModule / legacy bridge | Nitro Hybrid Object |
| `codegenConfig` в package.json | `nitro.json` + Nitrogen |
| Работало без New Architecture | **Только** New Architecture |
| Без peer `react-native-nitro-modules` | Peer-зависимость обязательна |

Публичный JS API (`isRooted`, `containsSignatures`) **не изменился**.

## Разработка библиотеки

```sh
yarn install
yarn specs    # npx nitrogen — регенерация nitrogen/generated
yarn prepare  # specs + react-native-builder-bob
yarn test
yarn typecheck
```

Нативные реализации:

- `android/src/main/java/com/leerman/rnsecuritypack/HybridSecurityPack.kt`
- `ios/HybridSecurityPack.swift`
- Спека: `src/specs/SecurityPack.nitro.ts`

После изменения `.nitro.ts` снова выполните `yarn specs`.

## Тестирование (Jest)

Добавьте `moduleNameMapper` в конфигурацию Jest:

```js
// jest.config.js
module.exports = {
  // ...
  moduleNameMapper: {
    'react-native-security-pack': 'react-native-security-pack/jest/mock',
  },
};
```

По умолчанию мок возвращает:

| Функция | Значение |
|---------|----------|
| `isRooted()` | `false` |
| `containsSignatures(...)` | `true` |

Мок построен на `jest.fn()`, поэтому можно переопределять поведение в конкретных тестах:

```ts
import { isRooted, containsSignatures } from 'react-native-security-pack';

jest.mocked(isRooted).mockResolvedValueOnce(true);
jest.mocked(containsSignatures).mockResolvedValueOnce(false);
```

## Устранение неполадок

Частые проблемы и их решения собраны в [TROUBLESHOOTING.md](TROUBLESHOOTING.md).

## Ограничения

Проверки root/jailbreak и подписи APK — **эвристики**. Опытный атакующий может их обойти. Используйте как один слой защиты вместе с серверной валидацией, certificate pinning, Play Integrity / App Attest и т.д.

## Лицензия

MIT — см. [LICENSE](LICENSE).

## Авторы

Andrew Timofeev ([@leerman](https://github.com/leerman))
Aleksandr Nikolaevich ([@AleksandrNikolaevich](https://github.com/AleksandrNikolaevich))
