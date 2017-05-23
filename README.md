<!--
	Markdown
-->

# Overview

This repository provides two MicroEJ Firmware projects, first one using administration command server over _NET API_, other one using administration command server over _COMM API_.

Both firmwares are headless, meaning they do not expose nor use user interfaces (such as GUI, buttons, ...). They are entry points for developers that want, in only two click, build and deploy their first firmware.
 
# Requirements

* MicroEJ SDK 4.1.0 or higher,
* A MicroEJ evaluation platform imported into the MicroEJ repository,
* A valid evaluation license for imported platform,
* For _NET_ example, a development board,
* For _COMM_ example, a development board with a TTL cable plugged in its RX/TX pins.

# Dependencies

All dependencies are retrieved from _MicroEJ Central Repository 4.1_ (<http://developer.microej.com/ivy/4.1/>) using Apache Ivy. Those dependencies include:
* A set of foundation libraries, essentials for any firmwares (such as _EDC_ and _KF_ libraries),
* A foundation library specific to each project, one for each back-end (_NET_ or _COMM_),
* A set of sandboxed-application-oriented addon libraries (_Wadapps Application Framework_, filesystem back-ends, ...),
* A firmware bootstrap library, that simplify firmware development,
* A set of runtime APIs, to expose functionalities to firmware end users,
* A set of resident applications, that will be linked together with produced firmware,
* A set of virtual device tools, such as local deployment tools and administration command client.

# Usage

First of all, clone this Git repository and import firmware projects into a MicroEJ SDK workspace. Two projects should be available:
* `com.microej.example.firmware.minimal.net`, the project with _NET_ back-end, and
* `com.microej.example.firmware.minimal.comm`, the project with _COMM_ back-end.

Both projects can be compiled and run the same way. There is little difference between them; main ones are how to administer the firmware from MicroEJ SDK and traces that each firmware prints on standard output. As a consequences, following steps can be applied to _NET_ and _COMM_ firmware examples. 

Next step is to build a firmware for your evaluation board. This requires to specify, before firmware build, the location of the platform to use to build it. Fortunately, that is easy to accomplish. Go to MicroEJ SDK preferences, in _MicroEJ_ section, _Platforms_ subsection. Put you mouse pointer over the platform you want to use, a tooltip with some information should appear. Press _F2_ to show more information about this platform; its location should now be available in  _Path_ entry; copy it. Open `module.ivy` file from firmware you want to build; you should see some properties already declared in Ivy `info` section. Add another property called `platform-loader.target.platform.dir` and set the path to the platform to use as its value.

It is now time to build your first firmware. Right-click on firmware project, in MicroEJ SDK _Package Explorer_ view, then click on _Build With EasyAnt_. Be patient, grab a cup of coffee, build can take 2 or 3 minutes. After successful build, firmware artifact is available in firmware project build folder `target~/artifacts`; depending which example you built, firmware artifact is named `firmware.net.out`or `firmware.comm.out`. A virtual device is also available in the same folder, with same name as the firmware and extension `.jpf`.  Built virtual device can be imported into MicroEJ Repository; this allows to local deploy sandboxed applications on built firmware, or use tools such as the administration console. To import the virtual device, go to MicroEJ SDK preferences, in _MicroEJ_ section, _Platforms_ subsection. Click on _Import_ button, and select virtual device in `artifacts` folder. 

To flash built firmware, open _Run Configurations_ menu, then create a _MicroEJ Tools_ run configuration named `Flash Firmware`. 
* In _Execution_ tab:
	* In _Target Platform_, select imported virtual device,
	* In _Settings_, select `Program with [Flasher Name]` (for example, `ST Link` or `Segger J-Link`, depending on the platform used to build the firmware),
	* Set _Output Folder_ to firmware project folder. 
* In _Configuration_ tab: 
	* Set __Binary file_ to built firmware file.
Ensure your board is plugged to your computer, then run the _Flash Firmware_ tool to flash built firmware into your board.

# Next step

Your are now ready to develop application and deploy it using _MicroEJ Tool_ _Local Deployment_ (over socket or COMM depending on the firmware).

# External Resources

Our developer website (<http://developer.microej.com>) contains a lot of resources and is a good entry point to find examples, tools and documentation. 

Specifically, foundation libraries javadoc can be found at <http://developer.microej.com/javadoc/microej_4.1/foundation/>, and addon libraries (such as _Wadapps Application Framework_) javadoc can be found at <http://developer.microej.com/javadoc/microej_4.1/addons/>.

# Restrictions

Examples are subject to license agreement. See `LICENSE.txt` in firmware projects.
