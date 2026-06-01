# Troubleshooting

## iOS

### Swift pod cannot be integrated as static library

**Ошибка при `pod install`:**

```
[!] The following Swift pods cannot yet be integrated as static libraries:

The Swift pod react-native-security-pack depends upon DTTJailbreakDetection,
which does not define modules. To opt into those targets generating module maps
(which is necessary to import them from Swift when building as static libraries),
you may set use_modular_headers! globally in your Podfile, or specify
:modular_headers => true for particular dependencies.
```

**Решение** — добавьте в `ios/Podfile`:

```ruby
pod 'DTTJailbreakDetection', :modular_headers => true
```

Затем выполните `pod install` повторно.
