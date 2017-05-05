/*
 * Java 7
 *
 * Copyright 2017 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */

package com.microej.demo.firmware;

import java.util.HashMap;
import java.util.Map;

import com.microej.kf.util.security.KernelSecurityManager;
import com.microej.wadapps.kernel.impl.AbstractKernelStartup;

import ej.kf.Feature;
import ej.kf.Feature.State;
import ej.kf.FeatureStateListener;
import ej.kf.Kernel;

/**
 * A simple kernel that starts all registered activities when an application is started.
 */
public class MinimalKernelStartup extends AbstractKernelStartup implements FeatureStateListener {

	private final Map<Feature, String> featureDescriptions;

	/**
	 * Simple main that starts the kernel.
	 *
	 * @param args
	 *            command line arguments.
	 */
	public static void main(String[] args) {
		new MinimalKernelStartup().run();
	}

	/**
	 * Instantiates a new minimal kernel.
	 */
	public MinimalKernelStartup() {
		this.featureDescriptions = new HashMap<>();
	}

	@Override
	public void run() {
		Kernel.addFeatureStateListener(this);
		super.run();
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
	protected void log(String message) {
		// Message can be redirected to another output, for example to a file or a socket.
		System.out.println("[MinimalKernel] " + message); //$NON-NLS-1$
	}

	@Override
	public void stateChanged(Feature feature, State previousState) {
		// Allows to react to application state changes. Policies can be implemented here
		// to react to application start/stop. For example, a critical application that passes to stopped
		// state can automatically be restarted here.
		State state = feature.getState();
		if (state == Feature.State.INSTALLED) {
			this.featureDescriptions.put(feature, feature.getName() + " (" + feature.getVersion() + ")"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		this.log(this.featureDescriptions.get(feature) + " has now state " + state.toString()); //$NON-NLS-1$
		if (state == Feature.State.UNINSTALLED) {
			this.featureDescriptions.remove(feature);
		}
	}

}
