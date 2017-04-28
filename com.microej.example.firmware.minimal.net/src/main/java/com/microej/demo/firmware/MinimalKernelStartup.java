/*
 * Java 7
 *
 * Copyright 2017 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */

package com.microej.demo.firmware;

import com.microej.kf.util.security.KernelSecurityManager;
import com.microej.wadapps.connectivity.WadappsConnectivityManager;
import com.microej.wadapps.kernel.impl.AbstractKernelStartup;

import android.net.ConnectivityManager;
import ej.kf.Feature;
import ej.kf.Feature.State;
import ej.kf.FeatureStateListener;
import ej.kf.Kernel;

/**
 * A simple kernel that starts all registered activities when an application is started.
 */
public class MinimalKernelStartup extends AbstractKernelStartup implements FeatureStateListener {

	/**
	 * Simple main that starts the kernel.
	 *
	 * @param args
	 *            command line arguments.
	 */
	public static void main(String[] args) {
		new MinimalKernelStartup().run();
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
		this.registerKernelToSharedRegistry(ConnectivityManager.class, new WadappsConnectivityManager());
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
		this.log(feature.getName() + " has now state " + feature.getState().toString()); //$NON-NLS-1$
	}

}
