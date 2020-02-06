import 'package:flutter/services.dart';
import 'package:zkc_scanner/scanner_callback.dart';
import 'dart:async';

export 'package:zkc_scanner/scanner_callback.dart';

class ZKCScanner {
  static const _METHOD_CHANNEL = "zkcscanner";
  static const _START_SCANNER = "startScanner";
  static const _RESUME_SCANNER = "resumeScanner";
  static const _PAUSE_SCANNER = "pauseScanner";
  static const _STOP_SCANNER = "stopScanner";
  static const _ON_DECODED = "onDecoded";
  static const _ON_ERROR = "onError";

  MethodChannel _channel;
  ScannerCallBack _scannerCallBack;

  ZKCScanner({ScannerCallBack scannerCallBack}) {
    _channel = const MethodChannel(_METHOD_CHANNEL);
    _channel.setMethodCallHandler(_onMethodCall);
    this._scannerCallBack = scannerCallBack;
  }

  set scannerCallBack(ScannerCallBack scannerCallBack) => _scannerCallBack = scannerCallBack;

  Future _onMethodCall(MethodCall call) {
    try {
      switch (call.method) {
        case _ON_DECODED:
          onDecoded(call.arguments);
          break;
        case _ON_ERROR:
          onError(Exception(call.arguments));
          break;
        default:
          print(call.arguments);
      }
    }
    catch(e)
    {
      print(e);
    }
    return null;
  }

  /// Called when decoder has successfully decoded the code
  /// <br>
  /// Note that this method always called on a worker thread
  ///
  /// @param code Encapsulates the result of decoding a barcode within an image
  void onDecoded(String code) {
    if(_scannerCallBack != null) _scannerCallBack.onDecoded(code);
  }

  /// Called when error has occurred
  /// <br>
  /// Note that this method always called on a worker thread
  ///
  /// @param error Exception that has been thrown
  void onError(Exception error) {
    if(_scannerCallBack != null) _scannerCallBack.onError(error);
  }


  Future startScanner(){
    return _channel.invokeMethod(_START_SCANNER);
  }

  Future resumeScanner() {
    return _channel.invokeMethod(_RESUME_SCANNER);
  }

  Future pauseScanner() {
    return _channel.invokeMethod(_PAUSE_SCANNER);
  }

  Future stopScanner() {
    return _channel.invokeMethod(_STOP_SCANNER);
  }


}