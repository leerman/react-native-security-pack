require "json"

package = JSON.parse(File.read(File.join(__dir__, "package.json")))

Pod::Spec.new do |s|
  s.name         = "react-native-security-pack"
  s.version      = package["version"]
  s.summary      = package["description"]
  s.homepage     = package["homepage"]
  s.license      = package["license"]
  s.authors      = package["author"]

  s.platforms    = { :ios => min_ios_version_supported }
  s.source       = { :git => package["repository"]["url"] + ".git", :tag => "#{s.version}" }
  s.module_name  = "RNSecurityPack"

  s.source_files = [
    "ios/**/*.{swift}",
    "ios/**/*.{m,mm}",
  ]

  s.pod_target_xcconfig = {
    "GCC_PREPROCESSOR_DEFINITIONS" => "$(inherited) FOLLY_NO_CONFIG FOLLY_CFG_NO_COROUTINES",
  }

  load "nitrogen/generated/ios/RNSecurityPack+autolinking.rb"
  add_nitrogen_files(s)

  s.dependency "React-jsi"
  s.dependency "React-callinvoker"
  s.dependency "DTTJailbreakDetection"

  install_modules_dependencies(s)
end
