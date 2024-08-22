#import "SecurityPack.h"
#import <DTTJailbreakDetection/DTTJailbreakDetection.h>

@implementation SecurityPack
RCT_EXPORT_MODULE()

// Example method
// See // https://reactnative.dev/docs/native-modules-ios
RCT_EXPORT_METHOD(isRooted:(RCTPromiseResolveBlock)resolve
                  reject:(RCTPromiseRejectBlock)reject)
{
    BOOL rooted = [DTTJailbreakDetection isJailbroken];
    resolve(@(rooted));
}

// Don't compile this code when we build for the old architecture.
#ifdef RCT_NEW_ARCH_ENABLED
- (std::shared_ptr<facebook::react::TurboModule>)getTurboModule:
    (const facebook::react::ObjCTurboModule::InitParams &)params
{
    return std::make_shared<facebook::react::NativeSecurityPackSpecJSI>(params);
}
#endif

@end
