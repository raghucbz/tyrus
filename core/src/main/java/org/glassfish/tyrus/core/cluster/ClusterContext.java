/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2014 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * http://glassfish.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package org.glassfish.tyrus.core.cluster;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

import javax.websocket.CloseReason;
import javax.websocket.SendHandler;

/**
 * Cluster related context.
 * <p/>
 * There is exactly one instance per cluster node and all communication is realized using this instance.
 *
 * @author Pavel Bucek (pavel.bucek at oracle.com)
 */
public abstract class ClusterContext {

    /**
     * ClusterContext registration property.
     * <p/>
     * ClusterContext is registered to the Server container via properties passed to {@link org.glassfish.tyrus.spi.ServerContainerFactory#createServerContainer(java.util.Map)}.
     */
    public static final String CLUSTER_CONTEXT = "org.glassfish.tyrus.core.cluster.ClusterContext";

    /**
     * Send text message.
     *
     * @param sessionId remote session id.
     * @param text      text to be sent.
     * @return future representing the send event. {@link java.util.concurrent.Future#get()} returns when there is an acknowledge
     * from the other node that the message has been successfully sent. If there is any exception, it will be wrapped into
     * {@link java.util.concurrent.ExecutionException} and thrown.
     */
    public abstract Future<Void> sendText(String sessionId, String text);

    /**
     * Send partial text message.
     *
     * @param sessionId remote session id.
     * @param text      text to be sent.
     * @param isLast    {@code true} when the partial message being sent is the last part of the message.
     * @return future representing the send event. {@link java.util.concurrent.Future#get()} returns when there is an acknowledge
     * from the other node that the message has been successfully sent. If there is any exception, it will be wrapped into
     * {@link java.util.concurrent.ExecutionException} and thrown.
     */
    public abstract Future<Void> sendText(final String sessionId, final String text, boolean isLast);

    /**
     * Send binary message.
     *
     * @param sessionId remote session id.
     * @param data      data to be sent.
     * @return future representing the send event. {@link java.util.concurrent.Future#get()} returns when there is an acknowledge
     * from the other node that the message has been successfully sent. If there is any exception, it will be wrapped into
     * {@link java.util.concurrent.ExecutionException} and thrown.
     */
    public abstract Future<Void> sendBinary(String sessionId, byte[] data);

    /**
     * Send partial binary message.
     *
     * @param sessionId remote session id.
     * @param data      data to be sent.
     * @param isLast    {@code true} when the partial message being sent is the last part of the message.
     * @return future representing the send event. {@link java.util.concurrent.Future#get()} returns when there is an acknowledge
     * from the other node that the message has been successfully sent. If there is any exception, it will be wrapped into
     * {@link java.util.concurrent.ExecutionException} and thrown.
     */
    public abstract Future<Void> sendBinary(final String sessionId, final byte[] data, boolean isLast);

    /**
     * Send ping message.
     *
     * @param sessionId remote session id.
     * @param data      data to be sent as "body" of ping message.
     * @return future representing the send event. {@link java.util.concurrent.Future#get()} returns when there is an acknowledge
     * from the other node that the message has been successfully sent. If there is any exception, it will be wrapped into
     * {@link java.util.concurrent.ExecutionException} and thrown.
     */
    public abstract Future<Void> sendPing(String sessionId, byte[] data);

    /**
     * Send pong message.
     *
     * @param sessionId remote session id.
     * @param data      data to be sent as "body" of pong message.
     * @return future representing the send event. {@link java.util.concurrent.Future#get()} returns when there is an acknowledge
     * from the other node that the message has been successfully sent. If there is any exception, it will be wrapped into
     * {@link java.util.concurrent.ExecutionException} and thrown.
     */
    public abstract Future<Void> sendPong(String sessionId, byte[] data);

    /**
     * Send text message with {@link javax.websocket.SendHandler}.
     *
     * @param sessionId   remote session id.
     * @param text        text to be sent.
     * @param sendHandler sendhandler instance on which {@link javax.websocket.SendHandler#onResult(javax.websocket.SendResult)} will be invoked.
     * @see javax.websocket.SendHandler
     */
    public abstract void sendText(String sessionId, String text, SendHandler sendHandler);

    /**
     * Send binary message with {@link javax.websocket.SendHandler}.
     *
     * @param sessionId   remote session id.
     * @param data        data to be sent.
     * @param sendHandler sendhandler instance on which {@link javax.websocket.SendHandler#onResult(javax.websocket.SendResult)} will be invoked.
     * @see javax.websocket.SendHandler
     */
    public abstract void sendBinary(String sessionId, byte[] data, SendHandler sendHandler);

    /**
     * Close remote session.
     *
     * @param sessionId remote session id.
     * @return future representing the event. {@link java.util.concurrent.Future#get()} returns when there is an acknowledge
     * from the other node that the command was successfully executed. If there is any exception, it will be wrapped into
     * {@link java.util.concurrent.ExecutionException} and thrown.
     */
    public abstract Future<Void> close(String sessionId);

    /**
     * Close remote session with custom {@link javax.websocket.CloseReason}.
     *
     * @param sessionId   remote session id.
     * @param closeReason custom close reason.
     * @return future representing the event. {@link java.util.concurrent.Future#get()} returns when there is an acknowledge
     * from the other node that the command was successfully executed. If there is any exception, it will be wrapped into
     * {@link java.util.concurrent.ExecutionException} and thrown.
     */
    public abstract Future<Void> close(String sessionId, CloseReason closeReason);

    /**
     * Get set containing session ids of all remote sessions registered to given endpoint path.
     *
     * @param endpointPath endpoint path identifying endpoint within the cluster.
     * @return set of sessions ids.
     */
    public abstract Set<String> getRemoteSessionIds(String endpointPath);

    /**
     * Create session id. It has to ne unique among all cluster nodes.
     *
     * @return session id.
     */
    public abstract String createSessionId();

    /**
     * Initializes cluster session.
     * <p/>
     * Used for registering local session - session id will be broadcasted to other nodes which will call {@link #getDistributedSessionProperties(String)}
     * and process its values. The map must be ready before this method is invoked.
     *
     * @param sessionId    session id to be registered.
     * @param endpointPath endpoint path identifying sessions alignment to the endpoint.
     * @param listener     session event listener. When remote node sends a message to this session, it will be invoked.
     * @see org.glassfish.tyrus.core.cluster.SessionEventListener
     */
    public abstract void initClusteredSession(String sessionId, String endpointPath, SessionEventListener listener);

    /**
     * Register session listener.
     * <p/>
     * Gets notification about session creation {@link org.glassfish.tyrus.core.cluster.SessionListener#onSessionOpened(String)}
     * and destruction {@link org.glassfish.tyrus.core.cluster.SessionListener#onSessionClosed(String)}.
     *
     * @param endpointPath endpoint path identifying sessions alignment to the endpoint.
     * @param listener     listener instance.
     * @see org.glassfish.tyrus.core.cluster.SessionListener
     */
    public abstract void registerSessionListener(String endpointPath, SessionListener listener);

    /**
     * Get the map containing session properties to be shared among nodes.
     * <p/>
     * Changes must be propagated to remote instances.
     *
     * @param sessionId remote session id.
     * @return distributed map containing session properties.
     */
    public abstract Map<ClusterSession.DistributedMapKey, Object> getDistributedSessionProperties(String sessionId);

    /**
     * Remove session from this Cluster context.
     *
     * @param sessionId    session id.
     * @param endpointPath endpoint path identifying sessions alignment to the endpoint.
     */
    public abstract void removeSession(String sessionId, String endpointPath);

    /**
     * Shutdown this ClusterContext.
     * <p/>
     * This will stop whole clustered node, any operation related to this cluster context will fail after this method is invoked.
     */
    public abstract void shutdown();
}