package brooklyn.demo

import java.util.List

import brooklyn.entity.basic.AbstractApplication
import brooklyn.entity.messaging.MessageBroker;
import brooklyn.entity.messaging.amqp.AmqpServer
import brooklyn.entity.messaging.qpid.QpidBroker
import brooklyn.event.basic.DependentConfiguration
import brooklyn.location.basic.CommandLineLocations

/** This example starts a Qpid broker, waits for a keypress, then stops it. */
public class StandaloneBrokerExample extends AbstractApplication {

    public static final List<String> DEFAULT_LOCATIONS = [ CommandLineLocations.newLocalhostLocation() ]

    public static final String CUSTOM_CONFIG_PATH = "classpath://custom-config.xml"
    public static final String PASSWD_PATH = "classpath://secure-passwd"
    public static final String BDBSTORE_JAR_PATH = "classpath://je-5.0.34.jar"
        
    public static void main(String[] args) {
        StandaloneBrokerExample app = new StandaloneBrokerExample()
        
	    // Configure the Qpid broker entity
	    QpidBroker broker = new QpidBroker(app,
	        amqpVersion:AmqpServer.AMQP_0_10,
	        runtimeFiles:[ (QpidBroker.CONFIG_XML):CUSTOM_CONFIG_PATH,
                           (QpidBroker.PASSWD):PASSWD_PATH,
                           ("lib/je-5.0.34.jar"):BDBSTORE_JAR_PATH ],
	        queue:"testQueue")
	
        // Start the application
        log.info("starting broker")
        app.start(DEFAULT_LOCATIONS)

        // Wait for BROKER_URL to be set and print it
        String url = DependentConfiguration.attributeWhenReady(app.broker, MessageBroker.BROKER_URL)
        log.info("started broker on ${url}")

        // Wait until ENTER pressed
        log.info("press enter to exit");
        System.in.read();

        // Stop the application
        log.info("stopping broker");
        app.stop();
    }
}
