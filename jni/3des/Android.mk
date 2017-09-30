LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_LDLIBS := -L$(SYSROOT)/usr/lib -llog
LOCAL_MODULE    := 3DES
LOCAL_SRC_FILES := 3DESAndroid.c 3des.c

include $(BUILD_SHARED_LIBRARY)
