
#ifdef RCT_NEW_ARCH_ENABLED
#import "RNSecurityPackSpec.h"

@interface SecurityPack : NSObject <NativeSecurityPackSpec>
#else
#import <React/RCTBridgeModule.h>

@interface SecurityPack : NSObject <RCTBridgeModule>
#endif

@end
