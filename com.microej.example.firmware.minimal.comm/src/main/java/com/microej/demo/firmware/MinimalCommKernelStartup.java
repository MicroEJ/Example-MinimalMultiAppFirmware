/*
 * Java 7
 *
 * Copyright 2017 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */

package com.microej.demo.firmware;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.microej.kf.util.security.KernelSecurityManager;
import com.microej.wadapps.kernel.impl.AbstractKernelStartup;

import ej.ecom.DeviceManager;
import ej.ecom.io.CommPort;
import ej.kf.Feature;
import ej.kf.Feature.State;
import ej.kf.FeatureStateListener;
import ej.kf.Kernel;

/**
 * A simple kernel that starts all registered activities when an application is started.
 */
public class MinimalCommKernelStartup extends AbstractKernelStartup implements FeatureStateListener {

	private final Map<Feature, String> featureDescriptions;

	/**
	 * Simple main that starts the kernel.
	 *
	 * @param args
	 *            command line arguments.
	 */
	public static void main(String[] args) {
		new MinimalCommKernelStartup().run();
	}

	/**
	 * Instantiates a new minimal kernel.
	 */
	public MinimalCommKernelStartup() {
		this.featureDescriptions = new HashMap<>();
	}

	@Override
	@SuppressWarnings("nls")
	public void run() {
		Kernel.addFeatureStateListener(this);
		super.run();
		this.log("Available COM ports:");
		Iterator<CommPort> ecomPorts = DeviceManager.list(CommPort.class);
		while (ecomPorts.hasNext()) {
			CommPort p = ecomPorts.next();
			this.log("- " + p.getName().toUpperCase() + " (baudrate=115200)");
		}
	}

	@Override
	protected void registerAdditionalKernelServices() {
		// Registers additional services to expose to applications through the shared registry
		super.registerAdditionalKernelServices();
	}

	@Override
	protected KernelSecurityManager createSecurityManager() {
		// A security manager can be defined here for the whole kernel.
		return super.createSecurityManager();
	}

	@Override
	@SuppressWarnings("nls")
	protected void log(String message) {
		// Message can be redirected to another output, for example to a file or a COMM port.
		System.out.println("[MinimalCommKernel] " + message);
	}

	@Override
	@SuppressWarnings("nls")
	public void stateChanged(Feature feature, State previousState) {
		// Allows to react to application state changes. Policies can be implemented here
		// to react to application start/stop. For example, a critical application that passes to stopped
		// state can automatically be restarted here.
		State state = feature.getState();
		if (state == State.INSTALLED
				|| (!this.featureDescriptions.containsKey(feature) && feature.getState() == State.STARTED)) {
			this.featureDescriptions.put(feature, feature.getName() + " (" + feature.getVersion() + ")");
		}
		this.log(this.featureDescriptions.get(feature) + " has now state " + state.toString());
		if (state == Feature.State.UNINSTALLED) {
			this.featureDescriptions.remove(feature);
		}
	}

}
