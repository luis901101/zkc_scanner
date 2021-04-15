# zkc_scanner

A Flutter plugin for barcode scanning with ZKC PDAs.

## How to use

```dart
final zkcScanner = ZKCScanner();
zkcScanner.scannerCallBack = this; // this must implement ScannerCallBack

@override
  void onDecoded(String? result) {
    setState(() {
      _scannedCode = result;
    });
  }

  @override
  void onError(Exception error) {
    setState(() {
      _scannedCode = error.toString();
    });
  }

zkcScanner.startScanner();
zkcScanner.stopScanner();
```