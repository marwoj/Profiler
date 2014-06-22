package com.example.hellojni;

public class Connector {

public native String stringFromJNI();
public native String jniButtonClick(int funcSelect, long max, int probeNr);
public native String secondJniButtonClick(int funcSelect, long max, int probeNr);

public  String firstTest(int funcSelect, long max, int probeNr)
{
	return jniButtonClick(funcSelect, max, probeNr);
}

public String secondTest(int funcSelect, long max, int probeNr)
{
	return secondJniButtonClick(funcSelect,max,probeNr);
}

public String getString() {
	return stringFromJNI();
}


static {
	System.loadLibrary("hello-jni");
	System.loadLibrary("second-jni");
}

}