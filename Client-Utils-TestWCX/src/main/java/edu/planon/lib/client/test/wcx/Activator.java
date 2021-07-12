package edu.planon.lib.client.test.wcx;

import java.util.ArrayList;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class Activator implements BundleActivator {
	@SuppressWarnings("rawtypes")
	private final ArrayList<ServiceRegistration> serviceRegisterList = new ArrayList<ServiceRegistration>();
	
	@Override
	public void start(BundleContext aContext) {
		// Register the Client Extension as a service . The right interface name has to be used here, because
		// Planon Application retrieves the service using this interface name. This name also has
		// to be given when the CUX gets configured in the field definer against a Business Object
		// in the Planon Application.
		
		this.serviceRegisterList.add(aContext.registerService(TestWCX.class.getName(), new TestWCX(), null));
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public void stop(BundleContext aContext) {
		for (ServiceRegistration registration : this.serviceRegisterList) {
			registration.unregister();
		}
		this.serviceRegisterList.clear();
	}
}
