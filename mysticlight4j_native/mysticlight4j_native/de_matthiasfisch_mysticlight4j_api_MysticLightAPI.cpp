#include "pch.h"
#include <string.h>

jclass loadClass(const char* clazzName, JNIEnv* env) {
    jclass clazz = env->FindClass(clazzName);
    if (clazz == NULL) {
        jclass classNotFoundEx = env->FindClass("Ljava/lang/ClassNotFoundException;");
        env->ThrowNew(classNotFoundEx, clazzName);
    }
    return clazz;
}

jobjectArray createStringArrayFromSafeArray(CComSafeArray<BSTR>* safearray, JNIEnv* env) {
    long lowerBound = safearray->GetLowerBound();
    long upperBound = safearray->GetUpperBound();
    long total = upperBound - lowerBound + 1;
    jobjectArray jarray = env->NewObjectArray(total, loadClass("Ljava/lang/String;", env), NULL);
    for (int i = lowerBound; i <= upperBound; i++) {
        BSTR value = safearray->GetAt(i);
        jstring jvalue = env->NewString((jchar*)value, wcslen(value));
        env->SetObjectArrayElement(jarray, i - lowerBound, jvalue);
    }
    return jarray;
}

bool checkStatus(int status, JNIEnv* env) {
    if (status == MLAPI_OK) {
        return true;
    }
    
    // Get the error description from MLAPI
    BSTR errorMessage;
    mysticLight.getErrorMessage(status, &errorMessage);
    jstring errorMessageString = env->NewString((jchar*) errorMessage, wcslen(errorMessage));

    // Instantiate and throw the exception
    jclass exClass = loadClass("Lde/matthiasfisch/mysticlight4j/api/MysticLightAPIException;", env);
    jmethodID ctorId = env->GetMethodID(exClass, "<init>", "(Ljava/lang/String;I)V");
    jobject ex = env->NewObject(exClass, ctorId, errorMessageString, status);
    env->Throw((jthrowable) ex);
    return false;
}

JNIEXPORT jboolean JNICALL Java_de_matthiasfisch_mysticlight4j_api_MysticLightAPI_isProcessElevated(JNIEnv* env, jclass jthis)
{
    BOOL isElevated = FALSE;
    HANDLE hToken = NULL;
    TOKEN_ELEVATION elevation;
    DWORD dwSize;

    if (!OpenProcessToken(GetCurrentProcess(), TOKEN_QUERY, &hToken)) {
        if (hToken)
        {
            CloseHandle(hToken);
            hToken = NULL;
        }
        return isElevated;
    }


    if (!GetTokenInformation(hToken, TokenElevation, &elevation, sizeof(elevation), &dwSize)) {
        if (hToken) {
            CloseHandle(hToken);
            hToken = NULL;
        }
        return isElevated;
    }

    isElevated = elevation.TokenIsElevated;
    if (hToken) {
        CloseHandle(hToken);
        hToken = NULL;
    }
    
    return isElevated;
}

JNIEXPORT void JNICALL Java_de_matthiasfisch_mysticlight4j_api_MysticLightAPI_initialize(JNIEnv* env, jclass jthis)
{
    int status = mysticLight.initialize();
    if (!checkStatus(status, env)) return;
}

JNIEXPORT jobjectArray JNICALL Java_de_matthiasfisch_mysticlight4j_api_MysticLightAPI_getDeviceInfo(JNIEnv* env, jclass jthis)
{
    LPSAFEARRAY pDeviceTypes, pLedCounts;
    int status = mysticLight.getDeviceInfo(&pDeviceTypes, &pLedCounts);
    if (!checkStatus(status, env)) return NULL;

    CComSafeArray<BSTR> deviceTypes;
    CComSafeArray<BSTR> ledCounts;
    deviceTypes.Attach(pDeviceTypes);
    ledCounts.Attach(pLedCounts);

    long lowerBound = deviceTypes.GetLowerBound();
    long upperBound = deviceTypes.GetUpperBound();
    long totalDevices = upperBound - lowerBound + 1;

    jclass deviceInfoClass = loadClass("Lde/matthiasfisch/mysticlight4j/api/DeviceInfo;", env);
    jobjectArray deviceInfos = env->NewObjectArray(totalDevices, deviceInfoClass, NULL);

    for (int i = lowerBound; i <= upperBound; i++) {
        BSTR devType = deviceTypes.GetAt(i);
        unsigned long ledCount = wcstoul(ledCounts.GetAt(i), nullptr, 10);
        
        jmethodID ctorId = env->GetMethodID(deviceInfoClass, "<init>", "(Ljava/lang/String;I)V");
        if (ctorId == NULL) return NULL;
        jstring devTypeString = env->NewString((jchar*) devType, wcslen(devType));
        jobject deviceInfo = env->NewObject(deviceInfoClass, ctorId, devTypeString, ledCount);
        env->SetObjectArrayElement(deviceInfos, i - lowerBound, deviceInfo);
    }

    return deviceInfos;
}

JNIEXPORT jobjectArray JNICALL Java_de_matthiasfisch_mysticlight4j_api_MysticLightAPI_getDeviceName(JNIEnv* env, jclass jthis, jstring jdeviceStr)
{
    LPSAFEARRAY pDeviceNames;
    BSTR device = (BSTR) env->GetStringChars(jdeviceStr, NULL);
    int status = mysticLight.getDeviceName(device, &pDeviceNames);
    if (!checkStatus(status, env)) return NULL;

    CComSafeArray<BSTR> deviceNames;
    deviceNames.Attach(pDeviceNames);

    long lowerBound = deviceNames.GetLowerBound();
    long upperBound = deviceNames.GetUpperBound();
    long totalNames = upperBound - lowerBound + 1;

    jobjectArray jDeviceNames = env->NewObjectArray(totalNames, loadClass("Ljava/lang/String;", env), NULL);
    for (int i = lowerBound; i <= upperBound; i++) {
        BSTR name = deviceNames.GetAt(i);
        jstring jname = env->NewString((jchar*) name, wcslen(name));
        env->SetObjectArrayElement(jDeviceNames, i - lowerBound, jname);
    }
    return jDeviceNames;
}

JNIEXPORT jstring JNICALL Java_de_matthiasfisch_mysticlight4j_api_MysticLightAPI_getDeviceNameEx(JNIEnv* env, jclass jthis, jstring jdevice, jint jledIndex)
{
    BSTR device = (BSTR) env->GetStringChars(jdevice, NULL);
    BSTR deviceName;
    int status = mysticLight.getDeviceNameEx(device, jledIndex, &deviceName);
    if (!checkStatus(status, env)) return NULL;
    return env->NewString((jchar*) deviceName, wcslen(deviceName));
}

JNIEXPORT jobject JNICALL Java_de_matthiasfisch_mysticlight4j_api_MysticLightAPI_getLedInfo(JNIEnv* env, jclass jthis, jstring jdevice, jint jledIndex)
{
    LPSAFEARRAY pLedStyles;
    BSTR device = (BSTR)env->GetStringChars(jdevice, NULL);
    BSTR deviceName;
    int status = mysticLight.getLedInfo(device, jledIndex, &deviceName, &pLedStyles);
    if (!checkStatus(status, env)) return NULL;

    CComSafeArray<BSTR> ledStyles;
    ledStyles.Attach(pLedStyles);

    jstring jname = env->NewString((jchar*)deviceName, wcslen(deviceName));
    jobjectArray jstyles = createStringArrayFromSafeArray(&ledStyles, env);

    jclass ledInfoClazz = loadClass("Lde/matthiasfisch/mysticlight4j/api/LedInfo;", env);
    jmethodID ctorId = env->GetMethodID(ledInfoClazz, "<init>", "(Ljava/lang/String;ILjava/lang/String;[Ljava/lang/String;)V");
    if (ctorId == NULL) return NULL;
    return env->NewObject(ledInfoClazz, ctorId, jdevice, jledIndex, jname, jstyles);
}

JNIEXPORT jobjectArray JNICALL Java_de_matthiasfisch_mysticlight4j_api_MysticLightAPI_getLedName(JNIEnv* env, jclass jthis, jstring jdevice)
{
    LPSAFEARRAY pLedNames;
    BSTR device = (BSTR)env->GetStringChars(jdevice, NULL);
    int status = mysticLight.getLedName(device, &pLedNames);

    CComSafeArray<BSTR> ledNames;
    ledNames.Attach(pLedNames);

    return createStringArrayFromSafeArray(&ledNames, env);
}

JNIEXPORT jobject JNICALL Java_de_matthiasfisch_mysticlight4j_api_MysticLightAPI_getLedColor(JNIEnv* env, jclass jthis, jstring jdevice, jint jledIndex)
{
    BSTR device = (BSTR)env->GetStringChars(jdevice, NULL);
    DWORD red, green, blue;
    int status = mysticLight.getLedColor(device, jledIndex, &red, &green, &blue);
    if (!checkStatus(status, env)) return NULL;

    jclass colorClass = loadClass("Lde/matthiasfisch/mysticlight4j/api/Color;", env);
    jmethodID ctorId = env->GetMethodID(colorClass, "<init>", "(SSS)V");
    if (ctorId == NULL) return NULL;
    return env->NewObject(colorClass, ctorId, red, green, blue);
}

JNIEXPORT jstring JNICALL Java_de_matthiasfisch_mysticlight4j_api_MysticLightAPI_getLedStyle(JNIEnv* env, jclass jthis, jstring jdevice, jint jledIndex)
{
    BSTR device = (BSTR)env->GetStringChars(jdevice, NULL);
    BSTR style;
    int status = mysticLight.getLedStyle(device, jledIndex, &style);
    if (!checkStatus(status, env)) return NULL;

    return env->NewString((jchar*)style, wcslen(style));
}

JNIEXPORT jint JNICALL Java_de_matthiasfisch_mysticlight4j_api_MysticLightAPI_getLedMaxBright(JNIEnv* env, jclass jthis, jstring jdevice, jint jledIndex)
{
    BSTR device = (BSTR)env->GetStringChars(jdevice, NULL);
    DWORD value;
    int status = mysticLight.getLedMaxBright(device, jledIndex, &value);
    if (!checkStatus(status, env)) return NULL;
    return value;
}

JNIEXPORT jint JNICALL Java_de_matthiasfisch_mysticlight4j_api_MysticLightAPI_getLedBright(JNIEnv* env, jclass jthis, jstring jdevice, jint jledIndex)
{
    BSTR device = (BSTR)env->GetStringChars(jdevice, NULL);
    DWORD value;
    int status = mysticLight.getLedBright(device, jledIndex, &value);
    if (!checkStatus(status, env)) return NULL;
    return value;
}

JNIEXPORT jint JNICALL Java_de_matthiasfisch_mysticlight4j_api_MysticLightAPI_getLedMaxSpeed(JNIEnv* env, jclass jthis, jstring jdevice, jint jledIndex)
{
    BSTR device = (BSTR)env->GetStringChars(jdevice, NULL);
    DWORD value;
    int status = mysticLight.getLedMaxSpeed(device, jledIndex, &value);
    if (!checkStatus(status, env)) return NULL;
    return value;
}

JNIEXPORT jint JNICALL Java_de_matthiasfisch_mysticlight4j_api_MysticLightAPI_getLedSpeed(JNIEnv* env, jclass jthis, jstring jdevice, jint jledIndex)
{
    BSTR device = (BSTR)env->GetStringChars(jdevice, NULL);
    DWORD value;
    int status = mysticLight.getLedSpeed(device, jledIndex, &value);
    if (!checkStatus(status, env)) return NULL;
    return value;
}

JNIEXPORT void JNICALL Java_de_matthiasfisch_mysticlight4j_api_MysticLightAPI_setLedColor(JNIEnv* env, jclass jthis, jstring jdevice, jint jledIndex, jobject jcolor)
{
    BSTR device = (BSTR)env->GetStringChars(jdevice, NULL);
    jclass colorClazz = loadClass("Lde/matthiasfisch/mysticlight4j/api/Color;", env);
    jshort red = env->CallShortMethod(jcolor, env->GetMethodID(colorClazz, "getRed", "()S"));
    jshort green = env->CallShortMethod(jcolor, env->GetMethodID(colorClazz, "getGreen", "()S"));
    jshort blue = env->CallShortMethod(jcolor, env->GetMethodID(colorClazz, "getBlue", "()S"));

    int status = mysticLight.setLedColor(device, jledIndex, red, green, blue);
    if (!checkStatus(status, env)) return;
}

JNIEXPORT void JNICALL Java_de_matthiasfisch_mysticlight4j_api_MysticLightAPI_setLedColors(JNIEnv*, jclass, jstring, jint, jobjectArray, jobject)
{
    return; // FIXME
}

JNIEXPORT void JNICALL Java_de_matthiasfisch_mysticlight4j_api_MysticLightAPI_setLedColorEx(JNIEnv*, jclass, jstring, jint, jstring, jobject, jboolean)
{
    return; // FIXME
}

JNIEXPORT void JNICALL Java_de_matthiasfisch_mysticlight4j_api_MysticLightAPI_setLedColorSync(JNIEnv*, jclass, jstring, jint, jstring, jobject, jboolean)
{
    return; // FIXME
}

JNIEXPORT void JNICALL Java_de_matthiasfisch_mysticlight4j_api_MysticLightAPI_setLedStyle(JNIEnv* env, jclass jthis, jstring jdevice, jint jledIndex, jstring jstyle)
{
    BSTR device = (BSTR)env->GetStringChars(jdevice, NULL);
    BSTR style = (BSTR)env->GetStringChars(jstyle, NULL);

    int status = mysticLight.setLedStyle(device, jledIndex, style);
    if (!checkStatus(status, env)) return;
}

JNIEXPORT void JNICALL Java_de_matthiasfisch_mysticlight4j_api_MysticLightAPI_setLedBright(JNIEnv* env, jclass jthis, jstring jdevice, jint jledIndex, jint jbright)
{
    BSTR device = (BSTR)env->GetStringChars(jdevice, NULL);

     int status = mysticLight.setLedBright(device, jledIndex, jbright);
     if (!checkStatus(status, env)) return;
}

JNIEXPORT void JNICALL Java_de_matthiasfisch_mysticlight4j_api_MysticLightAPI_setLedSpeed(JNIEnv* env, jclass jthis, jstring jdevice, jint jledIndex, jint jspeed)
{
    BSTR device = (BSTR)env->GetStringChars(jdevice, NULL);

    int status = mysticLight.setLedSpeed(device, jledIndex, jspeed);
    if (!checkStatus(status, env)) return;
}
