/*
 * Java 7
 *
 * Copyright 2017 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */

package com.microej.demo.firmware;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import com.microej.kf.util.security.KernelSecurityManager;
import com.microej.wadapps.kernel.impl.AbstractKernelStartup;

import android.net.Network;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import ej.bon.Timer;
import ej.components.dependencyinjection.ServiceLoaderFactory;
import ej.kf.Feature;
import ej.kf.Feature.State;
import ej.kf.FeatureStateListener;
import ej.kf.Kernel;
import ej.net.ConnectivityManager;
import ej.net.PollerConnectivityManager;

/**
 * A simple kernel that starts all registered activities when an application is started.
 */
public class MinimalNetKernelStartup extends AbstractKernelStartup implements FeatureStateListener {

	private final Map<Feature, String> featureDescriptions;

	/**
	 * Simple main that starts the kernel.
	 *
	 * @param args
	 *            command line arguments.
	 */
	public static void main(String[] args) {
		new MinimalNetKernelStartup().run();
	}

	/**
	 * Instantiates a new minimal kernel.
	 */
	public MinimalNetKernelStartup() {
		this.featureDescriptions = new HashMap<>();
	}

	@Override
	public void run() {
		Kernel.addFeatureStateListener(this);
		super.run();
		registerLogConnectivity();
	}

	/**
	 * Registers a network connectivity callback that logs available network
	 * interfaces
	 */
	private void registerLogConnectivity() {
		ConnectivityManager connectivityManager = new PollerConnectivityManager(
				ServiceLoaderFactory.getServiceLoader().getService(Timer.class), 0, 5000);
		NetworkRequest request = new NetworkRequest.Builder().build();
		connectivityManager.registerNetworkCallback(request, new ConnectivityManager.NetworkCallback() {
			@Override
			public void onAvailable(Network network) {
				logNetworkInterfaces();
			}

			@Override
			public void onLost(Network network) {
				logNetworkInterfaces();
			}
		});
		NetworkInfo info = connectivityManager.getActiveNetworkInfo();
		if (info.isConnected()) {
			logNetworkInterfaces();
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
		// Message can be redirected to another output, for example to a file or a socket.
		System.out.println("[MinimalNetKernel] " + message);
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

	/**
	 * Logs the list of registered {@link NetworkInterface}.
	 */
	private void logNetworkInterfaces() {
		this.log("Available Network interfaces:");
		boolean noInterface = true;
		Enumeration<NetworkInterface> interfaces = null;
		try {
			interfaces = NetworkInterface.getNetworkInterfaces();
		} catch (SocketException e) {
		}
		if (interfaces != null) {
			while (interfaces.hasMoreElements()) {
				NetworkInterface iface = interfaces.nextElement();
				// filters out 127.0.0.1 and inactive interfaces
				try {
					if (!iface.isUp() || iface.isLoopback()) {
						continue;
					}
				} catch (SocketException e) {
					continue;
				}

				Enumeration<InetAddress> addresses = iface.getInetAddresses();
				while (addresses.hasMoreElements()) {
					noInterface = false;
					InetAddress addr = addresses.nextElement();
					this.log("- " + addr.getHostAddress());
				}
			}
		}
		if (noInterface) {
			this.log("(none)");
		}
	}

}
