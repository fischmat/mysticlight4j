#include "pch.h"

#define SDK_DLL_NAME_X86 L"MysticLight_SDK.dll"
#define SDK_DLL_NAME_X64 L"MysticLight_SDK_x64.dll"

MysticLightAPI mysticLight = MysticLightAPI();

bool loadMysticLightLibrary() {
	SYSTEM_INFO systemInfo;
	GetNativeSystemInfo(&systemInfo);
	const wchar_t* dllName;
	if (systemInfo.wProcessorArchitecture == PROCESSOR_ARCHITECTURE_INTEL) {
		dllName = SDK_DLL_NAME_X86;
	} else {
		dllName = SDK_DLL_NAME_X64;
	}

	const HINSTANCE  MLinstance = LoadLibrary(dllName);
	if (!MLinstance)
		return false;

	//initialise the struct fields
	mysticLight.initialize = (LPMLAPI_Initialize)GetProcAddress(MLinstance, "MLAPI_Initialize");
	mysticLight.getErrorMessage = (LPMLAPI_GetErrorMessage)GetProcAddress(MLinstance, "MLAPI_GetErrorMessage");
	mysticLight.getDeviceInfo = (LPMLAPI_GetDeviceInfo)GetProcAddress(MLinstance, "MLAPI_GetDeviceInfo");
	mysticLight.getLedInfo = (LPMLAPI_GetLedInfo)GetProcAddress(MLinstance, "MLAPI_GetLedInfo");
	mysticLight.getLedName = (LPMLAPI_GetLedName)GetProcAddress(MLinstance, "MLAPI_GetLedName");
	mysticLight.getLedColor = (LPMLAPI_GetLedColor)GetProcAddress(MLinstance, "MLAPI_GetLedColor");
	mysticLight.getLedStyle = (LPMLAPI_GetLedStyle)GetProcAddress(MLinstance, "MLAPI_GetLedStyle");
	mysticLight.getLedMaxBright = (LPMLAPI_GetLedMaxBright)GetProcAddress(MLinstance, "MLAPI_GetLedMaxBright");
	mysticLight.getLedBright = (LPMLAPI_GetLedBright)GetProcAddress(MLinstance, "MLAPI_GetLedBright");
	mysticLight.getLedMaxSpeed = (LPMLAPI_GetLedMaxSpeed)GetProcAddress(MLinstance, "MLAPI_GetLedMaxSpeed");
	mysticLight.getLedSpeed = (LPMLAPI_GetLedSpeed)GetProcAddress(MLinstance, "MLAPI_GetLedSpeed");
	mysticLight.setLedColor = (LPMLAPI_SetLedColor)GetProcAddress(MLinstance, "MLAPI_SetLedColor");
	mysticLight.setLedStyle = (LPMLAPI_SetLedStyle)GetProcAddress(MLinstance, "MLAPI_SetLedStyle");
	mysticLight.setLedBright = (LPMLAPI_SetLedBright)GetProcAddress(MLinstance, "MLAPI_SetLedBright");
	mysticLight.setLedSpeed = (LPMLAPI_SetLedSpeed)GetProcAddress(MLinstance, "MLAPI_SetLedSpeed");
	mysticLight.getDeviceName = (LPMLAPI_GetDeviceName)GetProcAddress(MLinstance, "MLAPI_GetDeviceName");
	mysticLight.getDeviceNameEx = (LPMLAPI_GetDeviceNameEx)GetProcAddress(MLinstance, "MLAPI_GetDeviceNameEx");

	return true;
}