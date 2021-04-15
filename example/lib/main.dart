import 'package:flutter/material.dart';
import 'package:zkc_scanner/zkc_scanner.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> implements ScannerCallBack{
  final zkcScanner = ZKCScanner();
  String? _scannedCode = 'Empty';
  String _scannedStatus = 'Stopped';

  @override
  void initState() {
    super.initState();
    zkcScanner.scannerCallBack = this;
  }

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

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            Text('Scanner: $_scannedStatus'),
            Divider(color: Colors.transparent,),
            Text('Scanned code: $_scannedCode'),
            Divider(color: Colors.transparent,),
            ElevatedButton(
              child: Text("Start Scanner"),
              onPressed: (){
                zkcScanner.startScanner();
                _scannedStatus = "Started";
                setState(() {});
              },
            ),
            SizedBox(height: 16,),
            ElevatedButton(
              child: Text("Stop Scanner"),
              onPressed: (){
                zkcScanner.stopScanner();
                _scannedStatus = "Stopped";
                setState(() {});
              },
            ),
          ],
        ),
      ),
    );
  }
}
