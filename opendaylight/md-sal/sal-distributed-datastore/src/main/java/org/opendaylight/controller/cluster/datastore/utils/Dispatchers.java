/*
 * Copyright (c) 2015 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.controller.cluster.datastore.utils;

import com.google.common.base.Preconditions;
import scala.concurrent.ExecutionContext;

public class Dispatchers {
    public static final String DEFAULT_DISPATCHER_PATH = "akka.actor.default-dispatcher";
    public static final String CLIENT_DISPATCHER_PATH = "client-dispatcher";
    public static final String TXN_DISPATCHER_PATH = "txn-dispatcher";
    public static final String SHARD_DISPATCHER_PATH = "shard-dispatcher";
    public static final String NOTIFICATION_DISPATCHER_PATH = "notification-dispatcher";

    private final akka.dispatch.Dispatchers dispatchers;

    public static enum DispatcherType {
        Client(CLIENT_DISPATCHER_PATH),
        Transaction(TXN_DISPATCHER_PATH),
        Shard(SHARD_DISPATCHER_PATH),
        Notification(NOTIFICATION_DISPATCHER_PATH);

        private final String path;
        private DispatcherType(String path){
            this.path = path;
        }
        private String path(akka.dispatch.Dispatchers dispatchers){
            if(dispatchers.hasDispatcher(path)){
                return path;
            }
            return DEFAULT_DISPATCHER_PATH;
        }

        private ExecutionContext dispatcher(akka.dispatch.Dispatchers dispatchers){
            if(dispatchers.hasDispatcher(path)){
                return dispatchers.lookup(path);
            }
            return dispatchers.defaultGlobalDispatcher();
        }
    }

    public Dispatchers(akka.dispatch.Dispatchers dispatchers){
        Preconditions.checkNotNull(dispatchers, "dispatchers should not be null");
        this.dispatchers = dispatchers;
    }

    public ExecutionContext getDispatcher(DispatcherType dispatcherType){
        return dispatcherType.dispatcher(this.dispatchers);
    }

    public String getDispatcherPath(DispatcherType dispatcherType){
        return dispatcherType.path(this.dispatchers);
    }
}
