
# mysticlight4j

mysticlight4j is a Java library for controlling [MSI Mystic Light](https://de.msi.com/Landing/mystic-light-rgb-gaming-pc/) compatible RGB lighting. It provides an object oriented interface as well as bindings to the [Mystic Light SDK](https://de.msi.com/Landing/mystic-light-rgb-gaming-pc/download) API functions via the Java Native Interface (JNI) for low-level access.

The library consists of the following parts:
- The Java code for both object-oriented and direct access to the Mystic Light devices
- A native wrapper DLL written in C++ serving as interface between mysticlight4j and the Mystic Light SDK via JNI.

Unfortunately the documentation on the SDK functions is very sparse. The information provided on this page and the implementation is based on experiments on my system. As it was not tested on any other system it is easily possible that you may encounter problems when using it in a different configuration. If so please create an issue in the [issue tracker]([https://github.com/fischmat/mysticlight4j/issues](https://github.com/fischmat/mysticlight4j/issues)) of the GitHub project.

## Thanks
Special thanks go to MSI for providing the SDK and to [SimoDax](https://github.com/SimoDax) for giving valuable insights into how to use it.

## How to set up
Before using mysticlight4j library please consider the following requirements:
- The Mystic Light SDK and therefore mysticlight4j is only available for Microsoft Windows operating systems. The library can be loaded on other operating systems though, but initialization calls to the API will lead to an error.
- The JVM must run with elevated (administrator) priviledges. This is a constraint imposed by the Mystic Light SDK.
- The native bindings and the SDK DLLs are both available for x86 and x64 system architectures. Make sure that you load the correct binding DLL or use the autodetection (see below).

In order to use mysticlight4j in your project please follow these steps:
- Add the library as dependency to your `pom.xml`
```xml
<dependency>
    <groupId>de.matthiasfisch</groupId>
    <artifactId>mysticlight4j</artifactId>
    <version>0.2.3</version>
</dependency>
```

- Put the native binding DLLs `mysticlight4j_native.dll` (for x86) and `mysticlight4j_native_x64.dll` (for x64) into your project root.
- Download the [MSI Mystic Light SDK](http://download.msi.com/uti_exe/Mystic_light_SDK.zip) and extract the DLLs into your project root

## How to use the object oriented API
The easiest way to use mysticlight4j is the object oriented API. If you want to directly access the SDK functions instead, please read on with the next section.
The first thing you need to do is to create a new instance of `MysticLight4j`. This object then serves as main entry point for accessing the devices and LEDs of your system. It will automatically load the native DLL from the current working directory and initialize the Mystic Light SDK if needed. If you placed the DLLs elsewhere you can also specify the path to the directory in the constructor.
```java
MysticLight4j mysticlight4j = new MysticLight4j(Paths.get("C:\\path\\to\\dlls"));
```
With this object you can now access the devices installed in your system. Each device represents a component in your rig and has one or more LED groups. Furthermore every `Device` has an unique identifier and a display name.
```java
Collection<Device> devices = mysticlight4j.getAllAvailableDevices();
for (Device device : devices) {
    String id = device.getIdentifier(); // Unique ID of the device
    String name = device.getName(); // Display name of the device
    List<LED> leds = device.getLEDs(); // The LED groups on the device
}
```
A LED group of a device can either be a single physical LEDs or a group of physical LEDs that are controlled as a single entity (e.g. an array of LEDs on a mainboard). Every LED group has the following properties that can be controlled:
- The style of the group, i.e. the its animation. Each `LED` has an own set of supported styles that can be retrieved with the `getAvailableStyles()` method.
- The color of the LED. This is only relevant for some styles. Others will ignore it.
- A brightness level. This is a value between 0 (off) and a certain maximum that can be retrieved with `getMaximumBrightnessLevel()`.
- A speed level. This is a value between 0 (slowest) and a certain maximum that can be retrieved with `getMaximumSpeedLevel()`.

Apart from that each `LED` has a display name and an unique index on the device. You can change the state of the LEDs by calling the respective setter methods.

For reference please have a look at this small example that allows you set the animation of a LED group on a specific device:
```java
// Create a mysticlight4j object. It will load the native DLLs and initialize the SDK  
MysticLight4j mysticLight4j = new MysticLight4j();  
  
// Get the available devices  
List<Device> devices = mysticLight4j.getAllAvailableDevices();  
System.out.println("The following Mystic Light devices were found in your system:");  
for (int i = 0; i < devices.size(); i++) {  
    Device device = devices.get(i);  
  System.out.println(String.format("[%d] %s", i, device.getName()));  
}  
System.out.print("Choose a device:");  
Device device = chooseEntry(devices);  
  
// Get the LED groups on the device  
List<LED> leds = device.getLEDs();  
System.out.println("The device has the following LED groups:");  
for (int i = 0; i < leds.size(); i++) {  
    LED led = leds.get(i);  
  System.out.println(String.format("[%d] %s (bright: %s, speed: %s, style: %s)", i, led.getName(), led.getBrightnessLevel(), led.getSpeedLevel(), led.getStyle()));  
}  
System.out.print("Choose a LED group:");  
LED led = chooseEntry(leds);  
  
// Get the styles that this LED group supports  
System.out.println("For this LED the following styles are available:");  
List<String> styles = led.getAvailableStyles();  
for (int i = 0; i < styles.size(); i++) {  
    String style = styles.get(i);  
  System.out.println(String.format("[%d] %s", i, style));  
}  
// Set a style  
System.out.print("Choose a style:");  
String style = chooseEntry(styles);  
led.setStyle(style);  
// After a short period of time the LED style should change
```

For further details on the object oriented API have a look at the [JavaDoc](https://fischmat.github.io/mysticlight4j/de/matthiasfisch/mysticlight4j/package-summary.html).

## How to use the Low-Level API
Apart from the object-oriented API described above, mysticlight4j also provides direct access to the functions of the Mystic Light SDK. For this you can use the static methods in the class `MysticLightAPI`. Please make sure that you call `initialize()` before calling any other method.

This is an example on how to use the low-level API.
```java
MysticLightAPI.initialize(pathToDllDirectory);  
DeviceInfo[] devices = MysticLightAPI.getDeviceInfo();  
for (DeviceInfo device : devices) {  
    // Prints the unique identifier of the device  
  System.out.println(device.getDeviceType());  
  System.out.println("LED groups for this device:");  
 for (int i = 0; i < device.getLedCount(); i++) {  
        LedInfo led = MysticLightAPI.getLedInfo(device.getDeviceType(), i);  
  System.out.println(led.getName());  
  }  
}
```

For further details on the low-level API have a look at the [JavaDoc](https://fischmat.github.io/mysticlight4j/de/matthiasfisch/mysticlight4j/api/MysticLightAPI.html).

## Tested hardware
As stated above I was not yet able to test the library on any other system than my personal one. If you encounter any issues or successfully use it on your system please tell me via the issue tracker and I will add it to this list of tested hardware.
| Type | Brand | Name |
|--|--|--|
| Mainboard | MSI | MSI B450 Carbon Gaming Pro AC |
| Graphics Card | MSI | MSI Radeon HD 5700 XT Gaming X |
| Display | MSI | MSI Optix MAG 271CQR |
