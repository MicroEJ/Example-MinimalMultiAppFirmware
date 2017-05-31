<!--
	Markdown
	Copyright 2017 IS2T. All rights reserved.
	Use of this source code is subject to license terms.
-->

# Overview

This repository provides an example of a minimal MicroEJ Multi-App Firmware that features:
* a default implementation of the Wadapps framework.
* an administration server for managing the applications lifecycle (INSTALL, START, STOP, UNINSTALL).
* the exposure of a set of default APIs for applications (`EDC` + a communication channel - `NET` or `ECOM-COMM`).
* the virtual device configuration including Applications Local Deployment and Wadapps Administration Console tools.

The minimal Multi-App Firmware is declined into two distinct projects, depending on the communication channel used by the administration server:
* `com.microej.example.firmware.minimal.comm`: communication over TCP/IP network (`NET` API).
* `com.microej.example.firmware.minimal.net`: communication over a serial link (`ECOM-COMM` API).

The minimal Multi-App Firmware is headless, meaning it does not expose nor use user interfaces (such as GUI, buttons, ...). It is an entry point for developers that want, in only few steps, build their first Multi-App Firmware and test deploying an application on it.

# Requirements

* MicroEJ SDK 4.1.1 or higher.
* A MicroEJ 4.1 Multi-App platform (binary) imported into the MicroEJ repository. Please consult (<http://developer.microej.com>) for a list available evaluation platforms.
* An activated Evaluation or Production license.
* For `NET` example, a board connected to the same sub-network than the PC.
* For `COMM` example, a board with a USB-TTL cable plugged to the PC and connected to RX/TX pins.

# Dependencies

All dependencies are retrieved from _MicroEJ Central Repository 4.1_ (<http://developer.microej.com/ivy/4.1/>) using Apache Ivy. Those dependencies include:
* A set of foundation libraries, required for any Multi-App Firmware (such as `EDC` and `KF` libraries).
* A foundation library specific to each project, depending on the administration server back-end (`NET` or `ECOM-COMM`).
* Wadapps add-on libraries (_Wadapps Application Framework_, _Storage_ back-ends, ...).
* A firmware bootstrap library, that simplifies firmware development.
* A set of APIs exposed to applications.
* A set of Resident Applications, that are linked together with the Kernel to produce the Multi-App Firmware.
* A set of Virtual Device tools, such as Local Deployment and Wadapps Administration Console.

# Usage

## Import Firmware Projects

Start MicroEJ SDK on an empty workspace and clone this Git repository (`File > Import > Git > Projects From Git`).

At the end of the process, two projects have been imported:
  * `com.microej.example.firmware.minimal.net`
  * `com.microej.example.firmware.minimal.comm`

In the rest of the document, the term `[backend]` has to be replaced with either `net` or `comm` depending on the chosen firmware project.

## Select and Configure the Firmware

In case of the `net` back-end, the project is already configured.

In case of the `comm` back-end, the COM port on which the administration server will be connected must be set. Please consult the documentation of the selected platform to get the list of available platform COM ports. Edit `com.microej.example.firmware.minimal.comm/build/common.properties` and update with the appropriate value:

    ej.ecom.com.0.port=[PLATFORM_COM_VALUE]

## Setup a Platform

Before building the firmware, a target platform must be configured in the MicroEJ workspace.

* Go to `Window > Preferences > MicroEJ > Platforms` and put the mouse pointer over the desired platform.
* A tooltip with some information should appear. Press `F2` to show more information.
* Select the the platform _Path_ and copy it to the clipboard.
* Go to `Window > Preferences > Ant > Runtime` and select the `Properties` tab.
* Click on `Add Property...` button and set a new property named `platform-loader.target.platform.dir` with the platform path pasted from the clipboard.

There are other ways to setup the input platform for building the firmware. Please consult the _Multi-App Firmware Developer's Guide_ for more informations.

## Build the Firmware

* Right-click on the chosen firmware project and select `Build With EasyAnt`. This may take several minutes.

After successful build, the firmware artifacts are available in firmware project build folder `target~/artifacts` and contains:
* the firmware executable file (`minimal.[backend].out`).
* the corresponding virtual device (`minimal.[backend].jpf`).
* the firmware package for the MicroEJ Store (`minimal.[backend].kpk`)

## Program the Firmware on the Device

The procedure to program a firmware is platform specific. Please refer to the platform documentation for the detailed firmware flashing procedure.

To flash the firmware, open `Run Configurations` menu, then create a `MicroEJ Tool` run configuration named `Flash Firmware`.
* In `Execution` tab:
	* In `Target Platform`, select the platform used to build the firmware,
	* In `Settings`, select `Program with [Flasher Tool]` (e.g `ST Link` or `Segger J-Link`),
	* Set `Output Folder` to firmware project folder.
* In `Configuration` tab:
	* Set the application binary file to `target~/artifacts/minimal-[backend].out` file.
* Click on `Run` and wait until the flashing procedure is terminated.

# Develop and Deploy an Application

Prior to develop an application, the virtual device has to be imported:

* Go to `File > Import > MicroEJ > Platforms, Virtual Devices and Architectures`
* Browse the file `\target~\artifacts\minimal.[backend].jpf` and put the mouse pointer over the desired platform.

Note that the application development can be done in a dedicated MicroEJ Studio instance (the virtual device can be freely distributed).

To create and deploy a basic application, please refer to the _Sandboxed Application Developer's Guide_ (sections _Background Service Application_ and _Wadapps Administration Console_).


# Additional Resources

## Developer Resources

MicroEJ developer web site (<http://developer.microej.com>) is the entry point to find examples, tools and documentation.

Specifically, foundation libraries javadoc can be found at <http://developer.microej.com/javadoc/microej_4.1/foundation/>, and addon libraries (such as _Wadapps Application Framework_) javadoc can be found at <http://developer.microej.com/javadoc/microej_4.1/addons/>.

## License

Examples are subject to license agreement. See `LICENSE.txt` in firmware projects.

## Change Log

Please consult `CHANGELOG.md` in firmware projects for the firmware content versioning.

### 1.0.0 (2017 June 1st)
Features:
	  - Initial revision for MicroEJ 4.1.
