package com.wtz.java.util;

import java.net.BindException;
import java.util.Iterator;

public class _05_Test {
    public static void main(String[] args) throws BindException {
        String portRangeDefinition = "0";
        Iterator<Integer> portsIterator;
        try {
            portsIterator = _04_FromRangePort.get(portRangeDefinition);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid port range definition: " + portRangeDefinition);
        }

        while (portsIterator.hasNext()) {
            final int port = portsIterator.next();

            try {
                System.out.println("port is: " + port);
            } catch (Exception e) {
                // we can continue to try if this contains a netty channel exception
                Throwable cause = e.getCause();
                if (!(cause instanceof org.jboss.netty.channel.ChannelException ||
                        cause instanceof java.net.BindException)) {
                    throw e;
                } // else fall through the loop and try the next port
            }
        }

        // if we come here, we have exhausted the port range
        throw new BindException("Could not start actor system on any port in port range "
                + portRangeDefinition);
    }
}
