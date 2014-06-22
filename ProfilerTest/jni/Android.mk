# Copyright (C) 2009 The Android Open Source Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
LOCAL_PATH := $(call my-dir)

###########################################################
include $(CLEAR_VARS)

LOCAL_MODULE    := second-jni
LOCAL_SRC_FILES := second-jni.c

# compile with profiling
LOCAL_CFLAGS := -pg -g
LOCAL_STATIC_LIBRARIES := android-ndk-profiler

#BUILD_SHARED_LIBRARY na końcu pliku przed import-module
include $(BUILD_SHARED_LIBRARY)

###########################################################
include $(CLEAR_VARS)

LOCAL_MODULE    := hello-jni
LOCAL_SRC_FILES := hello-jni.c

# compile with profiling
LOCAL_CFLAGS := -pg -g
LOCAL_STATIC_LIBRARIES := android-ndk-profiler

#BUILD_SHARED_LIBRARY na końcu pliku przed import-module
include $(BUILD_SHARED_LIBRARY)
###########################################################



# at the end of Android.mk
$(call import-module,android-ndk-profiler)

