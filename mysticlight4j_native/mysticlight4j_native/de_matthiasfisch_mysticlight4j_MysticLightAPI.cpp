#include "pch.h"
#include <string.h>
#include <iostream>

using namespace std;

jobject createDeviceInfo(JNIEnv* env, const wchar_t* devType, unsigned int ledCount) {
	jclass deviceInfoClass = env->FindClass("Lde/matthiasfisch/mysticlight4j/MysticLightAPI$DeviceInfo;");
	if (deviceInfoClass == NULL) return NULL;
	jmethodID ctorId = env->GetMethodID(deviceInfoClass, "<init>", "(Ljava/lang/String;I)V");
	if (ctorId == NULL) return NULL;

	jstring devTypeString = env->NewString((jchar*)devType, wcslen(devType));
	env->NewObject(deviceInfoClass, ctorId, devTypeString, ledCount);
}

JNIEXPORT jint JNICALL Java_de_matthiasfisch_mysticlight4j_MysticLightAPI_initialize
(JNIEnv* env, jobject jthis) {
	return mysticLight.initialize();
}

JNIEXPORT jobjectArray JNICALL Java_de_matthiasfisch_mysticlight4j_MysticLightAPI_getDeviceInfo
(JNIEnv* env, jobject jthis) {
	jobject e1 = createDeviceInfo(env, L"MSI_MB", 5);
	jobject e2 = createDeviceInfo(env, L"MSI_VGA", 3);

	jclass deviceInfoClass = env->FindClass("Lde/matthiasfisch/mysticlight4j/MysticLightAPI$DeviceInfo;");
	jobjectArray result = env->NewObjectArray(2, deviceInfoClass, NULL);
	env->SetObjectArrayElement(result, 0, e1);
	env->SetObjectArrayElement(result, 1, e2);
	return result;
}