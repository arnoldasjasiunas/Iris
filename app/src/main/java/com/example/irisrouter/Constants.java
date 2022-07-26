package com.example.irisrouter;

public class Constants {

    public static class CounterApp {
        public static final String MSG_KEY_COUNT_LABEL = "m";
        public static final String MSG_VALUE_COUNT_LABEL = "count";
        public static final String MSG_KEY_COUNT_VALUE = "p";
    }

    public static class SRDServiceOp {
        public static final int MSG_REGISTER_CLIENT = 1;
        public static final int MSG_UNREGISTER_CLIENT = 2;
        /**
         * Command to service to set a new value.  This can be sent to the
         * service to supply a new value, and will be sent by the service to
         * any registered clients with the new value.
         */
        public static final int MSG_SET_VALUE = 3;
        /**
         * Command to test http performance
         */
        public static final int SEND_HTTP = 4;
        public static final int CORE_MSG_STATUS_UPDATE = 10;
        public static final int CORE_MSG_PROCESS_REQUEST = 11;
        public static final int CORE_MSG_PROCESS_LIGHT_REQUEST = 12;
        public static final int CORE_MSG_PROCESS_LIGHT_RESET_REQUEST = 13;
        public static final int CORE_MSG_PROCESS_RESPONSE = 15;
        public static final int CORE_MSG_SEND_REQUEST = 16;


        public static final int ON_CORE_WS_MSG = 20;
        public static final int SEND_REPLY_TO_CORE_WS_MSG = 21;

        public static final int STORAGE_REQUEST_CODE = 52;
        public static final int SYNC_PREFS = 54;


        /**
         * Server Received HTTP request from TCP Server to connected clients
         */
        public static final int ROUTING_MSG_SERVE = 120;
        /**
         * Send Message via Network (HTTP request to a TCP server)
         */
        public static final int ROUTING_MSG_SEND = 121;
        public static final int ROUTING_MSG_SEND_SUCCEEDED = 122;
        public static final int ROUTING_MSG_SEND_FAILED = 123;


        public static final int IP_DISCOVERY_BROADCAST_MESSAGE_AT = 130;
        public static final int IP_DISCOVERY_FOUND = 133;
        public static final int IP_DISCOVERY_STATUS = 134;
        /**
         * for development purposes only, not testing
         */
        public static final int IP_DISCOVERY_BROADCAST = 137;
        /**
         * for development purposes only, not testing
         */
        public static final int IP_DISCOVERY_LISTEN = 138;
    }


    public static class Servers {
        public static final String SERVERS_EXTRA = "SERVERS_EXTRA";
        public static final String SRD_Routing_SERVER = "SRDRouterNanoServer";
        public static final String COMPAT_RESCUE_SERVER = "CompatRescueServer";
        public static final String COMPAT_CORE_SERVER = "CompatCoreServer";
    }

}
