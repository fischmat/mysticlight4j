#pragma once
#include "pch.h"

#define MLAPI_OK 0 // Success
#define MLAPI_ERROR -1 // Generic error
#define MLAPI_TIMEOUT -2 // Request is timeout
#define MLAPI_NO_IMPLEMENTED -3 // MSI application not found or installed version not supported
#define MLAPI_NOT_INITIALIZED -4 // MLAPI_Initialize has not been called successful.
#define MLAPI_INVALUID_ARGUMENT -101 // The parameter value is not valid.
#define MLAPI_DEVICE_NOT_FOUND -102 // The device is not found
#define MLAPI_NOT_SUPPORTED // Requested feature is not supported in the selected LED.

/*
Struct defining the Mystic Light API functions. The function pointers are set by a call to loadMysticLightLibrary().
*/
typedef struct MysticLightAPI {
	LPMLAPI_Initialize initialize;
	LPMLAPI_GetErrorMessage getErrorMessage;
	LPMLAPI_GetDeviceInfo getDeviceInfo;
	LPMLAPI_GetLedInfo getLedInfo;
	LPMLAPI_GetLedName getLedName;
	LPMLAPI_GetLedColor getLedColor;
	LPMLAPI_GetLedStyle	getLedStyle;
	LPMLAPI_GetLedMaxBright getLedMaxBright;
	LPMLAPI_GetLedBright getLedBright;
	LPMLAPI_GetLedMaxSpeed getLedMaxSpeed;
	LPMLAPI_GetLedSpeed getLedSpeed;
	LPMLAPI_SetLedColor setLedColor;
	LPMLAPI_SetLedStyle setLedStyle;
	LPMLAPI_SetLedBright setLedBright;
	LPMLAPI_SetLedSpeed setLedSpeed;
	LPMLAPI_GetDeviceName getDeviceName;
	LPMLAPI_GetDeviceNameEx getDeviceNameEx;
};

extern MysticLightAPI mysticLight;

/*
* Loads the Mystic Light DLL and assignes the funtions to mysticLight struct.
* Returns true iff initialization was successful.
*/
bool loadMysticLightLibrary();