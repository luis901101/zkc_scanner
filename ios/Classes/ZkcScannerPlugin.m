#import "ZkcScannerPlugin.h"
#import <zkc_scanner/zkc_scanner-Swift.h>

@implementation ZkcScannerPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftZkcScannerPlugin registerWithRegistrar:registrar];
}
@end
