/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

#include <jni.h>

//#include <string.h>

void secondFuncA(jlong);
void secondFuncB(jlong);
void secondFuncC(jlong);
void secondFuncD(jlong);
void secondFuncE(jlong);
void secondFuncE1(jlong);
void secondFuncE2(jlong);
void secondFuncE3(jlong);

/* This is a trivial JNI example where we use a native method
 * to return a new VM String. See the corresponding Java source
 * file located at:
 *
 *   apps/samples/hello-jni/project/src/com/example/hellojni/HelloJni.java
 */
jstring
Java_com_example_hellojni_Connector_secondStringFromJNI( JNIEnv* env,
                                                  jobject thiz )
{
#if defined(__arm__)
  #if defined(__ARM_ARCH_7A__)
    #if defined(__ARM_NEON__)
      #define ABI "armeabi-v7a/NEON"
    #else
      #define ABI "armeabi-v7a"
    #endif
  #else
   #define ABI "armeabi"
  #endif
#elif defined(__i386__)
   #define ABI "x86"
#elif defined(__mips__)
   #define ABI "mips"
#else
   #define ABI "unknown"
#endif


    return (*env)->NewStringUTF(env, "Compiled with ABI " ABI ".");
}



jstring
Java_com_example_hellojni_Connector_secondJniButtonClick( JNIEnv* env,
                                                  jobject thiz, jint funcSelect, jlong max, jint probeNr)
{
	char * probes;
	if(probeNr == 10)
		probes = "10";
	else if(probeNr == 100)
		probes = "100";
	else if(probeNr == 500)
		probes = "500";
	else if(probeNr == 1000)
		probes = "1000";
	else if(probeNr == 2000)
		probes = "2000";
	else if(probeNr == 4000)
		probes = "4000";
	else
		probes = "100";


	setenv("CPUPROFILE_FREQUENCY", probes, 1);
	monstartup("libsecond-jni.so");


	if(funcSelect == 1)
		secondFuncA(max);
	else if(funcSelect == 2)
		secondFuncB(max);
	else if(funcSelect == 3)
		secondFuncC(max);
	else if(funcSelect == 4)
		secondFuncD(max);
	else if(funcSelect == 5)
		secondFuncE(max);

	setenv("CPUPROFILE", "/storage/sdcard0/gmonSecond.out", 1);
	moncleanup();
	return (*env)->NewStringUTF(env, "Eureka_2");
}


void secondFuncA(jlong max)
{
	jlong i = 0;
    for(;i<max;i++);
    return;
}

void secondFuncB(jlong max)
{
	jlong i = 0;
    for(;i<max;i++);
    	secondFuncA(max);
    return;
}

void secondFuncC(jlong max)
{
	jlong i = 0;
    for(;i<max;i++);
    	secondFuncA(max);
    	secondFuncB(max);
    return;
}

void secondFuncD(jlong max)
{
	jlong i = 0;
    for(;i<max;i++);
    	secondFuncA(max);
    	secondFuncB(max);
    	secondFuncC(max);
    return;
}

void secondFuncE(jlong max)
{
	jlong i=0;
	for(;i<max;i++)
	secondFuncE1(max);
	secondFuncE2(max);
}

void secondFuncE1(jlong max)
{
	jlong i=0;
	for(;i<max*max;i++);
	for(i=0;i<10;i++){
		secondFuncE2(max);
		secondFuncE3(max);
	}
}


void secondFuncE2(jlong max)
{
	jlong i=0;
	for(;i<max*max*max;i++);
	for(i=0;i<10;i++){
		secondFuncE3(max);
	}
}


void secondFuncE3(jlong max)
{
	jlong i=0;
	for(;i<max*max*max;i++);
}


