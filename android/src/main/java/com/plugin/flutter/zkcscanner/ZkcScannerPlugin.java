package com.plugin.flutter.zkcscanner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/**
 * Created by luis901101 on 05/30/19.
 */

/** ZKCScannerPlugin */
public class ZkcScannerPlugin implements MethodCallHandler {
  /** Plugin registration. */
  public static void registerWith(Registrar registrar) {
    new ZkcScannerPlugin(registrar);
  }

  static final String _METHOD_CHANNEL = "zkcscanner";
  static final String _START_SCANNER = "startScanner";
  static final String _RESUME_SCANNER = "resumeScanner";
  static final String _PAUSE_SCANNER = "pauseScanner";
  static final String _STOP_SCANNER = "stopScanner";
  static final String _ON_DECODED = "onDecoded";
  static final String _ON_ERROR = "onError";

  class Constants {
    public static final String ACTION_SCAN_CODE_CALLBACK = "com.zkc.scancode";
    public static final String ACTION_KEY_CODE = "com.zkc.keycode";
    public static final String ACTION_OPEN = "android.intent.action.SCREEN_ON";
    public static final String ACTION_CLOSE = "android.intent.action.SCREEN_OFF";

    public static final String EXTRA_KEY_VALUE = "keyvalue";
    public static final int VALUE_OPEN = 137;
    public static final int VALUE_CLOSE = 122;
  }

  class ScanBroadcastReceiver extends BroadcastReceiver
  {
    @Override
    public void onReceive(Context context, Intent intent) {
      String code = null;
      try
      {
        String result= intent.getExtras().getString("code");
        if(result.contains("{")) {
          int strStart = result.lastIndexOf("{");
          code = result.substring(0, strStart);
        }
        if(code == null) onError(new Exception("Null code")); else
          onDecoded(code);
      }
      catch(Exception e)
      {
        onError(e);
      }
    }
  }


  private MethodChannel channel;
  private Context context;
  private ScanBroadcastReceiver scanBroadcastReceiver;
  private IntentFilter intentFilter;

  public ZkcScannerPlugin(Registrar registrar)
  {
    context = registrar.context();
    channel = new MethodChannel(registrar.messenger(), _METHOD_CHANNEL);
    channel.setMethodCallHandler(this);
    init();
    listeners();
  }

  @Override
  public void onMethodCall(MethodCall call, Result result) {
    try
    {
      switch(call.method){
        case _START_SCANNER:
          startScanner();
          result.success(true);
          break;
        case _RESUME_SCANNER:
          resumeScanner();
          result.success(true);
          break;
        case _PAUSE_SCANNER:
          pauseScanner();
          result.success(true);
          break;
        case _STOP_SCANNER:
          stopScanner();
          result.success(true);
          break;
        default:
          result.notImplemented();
      }
    }
    catch(Exception e)
    {
      e.printStackTrace();
      result.error(e.getMessage(), null, null);
    }
  }

  /**
   * Called when decoder has successfully decoded the code
   * <br>
   * Note that this method always called on a worker thread
   *
   * @param code Encapsulates the result of decoding a barcode within an image
   */
  public void onDecoded(String code)
  {
    channel.invokeMethod(_ON_DECODED, code);
  }

  /**
   * Called when error has occurred
   * <br>
   * Note that this method always called on a worker thread
   *
   * @param error Exception that has been thrown
   */
  public void onError(Exception error)
  {
    channel.invokeMethod(_ON_ERROR, error.getMessage());
  }

  private void init()
  {
    scanBroadcastReceiver = new ScanBroadcastReceiver();
    intentFilter = new IntentFilter();
    intentFilter.addAction(Constants.ACTION_SCAN_CODE_CALLBACK);
  }

  private void listeners()
  {
  }

  public boolean startScanner()
  {
    /**
     * Do something here to enable scanner
     * then start listeners
     */
    resumeScanner();
    return true;
  }

  public boolean resumeScanner()
  {
    registerReceiver();
    return true;
  }

  public boolean pauseScanner()
  {
    unregisterReceiver();
    return true;
  }

  public boolean stopScanner()
  {
    unregisterReceiver();
    /**
     * Do something here to disable scanner
     */
    return true;
  }

  private void registerReceiver(){
    try{context.registerReceiver(scanBroadcastReceiver, intentFilter);}catch(Exception e){}
  }

  private void unregisterReceiver(){
    try{context.unregisterReceiver(scanBroadcastReceiver);}catch(Exception e){}
  }
}
