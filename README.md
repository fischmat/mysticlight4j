
# mysticlight4j

mysticlight4j is a Java library for controlling [MSI Mystic Light](https://de.msi.com/Landing/mystic-light-rgb-gaming-pc/) compatible RGB lighting. It provides an object oriented interface as well as bindings to the [Mystic Light SDK](https://de.msi.com/Landing/mystic-light-rgb-gaming-pc/download) API functions via the Java Native Interface (JNI) for low-level access.

The library consists of the following parts:
- The Java code for both object-oriented and direct access to the Mystic Light devices
- A native wrapper DLL written in C++ serving as interface between mysticlight4j and the Mystic Light SDK via JNI.

## How to use it
To use the library put the `mysticlight4j_native.dll` (for x86) and `mysticlight4j_native_x64.dll` files into your project root. Download the Mystic Light SDK DLLs from the (MSI website)[https://de.msi.com/Landing/mystic-light-rgb-gaming-pc/download] and also put those in your project root. Also note that you must run your JVM **with administrator privileges** (this is unfortunately a restriction imposed by the SDK). In your code you can create an instance of `MysticLight4j` and use it to access the RGB devices:
```java
final MysticLight4j mysticlight4j = new MysticLight4j();
final Device mainboard = mysticlight4j.getDevice("MSI_MB");
final LED led = dev.getLED("TOP RIGHT");
led.setStyle("Straight");
led.setColor(255, 0, 0);
```

If you prefer direct access to the Mystic Light SDK functions you can access them via the JNI interface `MysticLightAPI`.
```java
MysticLightAPI.initialize();
MysticLightAPI.setLedStyle("MSI_MB", 2, "Straight");
MysticLightAPI.setColor("MSI_MB", 2, new Color(255, 0, 0));
```
## Thanks
Special thanks go to MSI for providing the SDK and to [SimoDax](https://github.com/SimoDax) for giving valuable insights into how to use it.
