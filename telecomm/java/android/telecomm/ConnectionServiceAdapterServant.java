/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 R* limitations under the License.
 */

package android.telecomm;

import android.app.PendingIntent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;

import com.android.internal.os.SomeArgs;
import com.android.internal.telecomm.IConnectionServiceAdapter;
import com.android.internal.telecomm.IVideoCallProvider;
import com.android.internal.telecomm.RemoteServiceCallback;

import java.util.List;

/**
 * A component that provides an RPC servant implementation of {@link IConnectionServiceAdapter},
 * posting incoming messages on the main thread on a client-supplied delegate object.
 *
 * TODO: Generate this and similar classes using a compiler starting from AIDL interfaces.
 *
 * @hide
 */
final class ConnectionServiceAdapterServant {
    private static final int MSG_HANDLE_CREATE_CONNECTION_SUCCESSFUL = 1;
    private static final int MSG_HANDLE_CREATE_CONNECTION_FAILED = 2;
    private static final int MSG_HANDLE_CREATE_CONNECTION_CANCELLED = 3;
    private static final int MSG_SET_ACTIVE = 4;
    private static final int MSG_SET_RINGING = 5;
    private static final int MSG_SET_DIALING = 6;
    private static final int MSG_SET_DISCONNECTED = 7;
    private static final int MSG_SET_ON_HOLD = 8;
    private static final int MSG_SET_REQUESTING_RINGBACK = 9;
    private static final int MSG_SET_CALL_CAPABILITIES = 10;
    private static final int MSG_SET_IS_CONFERENCED = 11;
    private static final int MSG_ADD_CONFERENCE_CALL = 12;
    private static final int MSG_REMOVE_CALL = 13;
    private static final int MSG_ON_POST_DIAL_WAIT = 14;
    private static final int MSG_QUERY_REMOTE_CALL_SERVICES = 15;
    private static final int MSG_SET_VIDEO_STATE = 16;
    private static final int MSG_SET_VIDEO_CALL_PROVIDER = 17;
    private static final int MSG_SET_AUDIO_MODE_IS_VOIP = 18;
    private static final int MSG_SET_STATUS_HINTS = 19;
    private static final int MSG_SET_HANDLE = 20;
    private static final int MSG_SET_CALLER_DISPLAY_NAME = 21;
    private static final int MSG_START_ACTIVITY_FROM_IN_CALL = 22;
    private static final int MSG_SET_CONFERENCEABLE_CONNECTIONS = 23;

    private final IConnectionServiceAdapter mDelegate;

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                internalHandleMessage(msg);
            } catch (RemoteException e) {
            }
        }

        // Internal method defined to centralize handling of RemoteException
        private void internalHandleMessage(Message msg) throws RemoteException {
            switch (msg.what) {
                case MSG_HANDLE_CREATE_CONNECTION_SUCCESSFUL: {
                    SomeArgs args = (SomeArgs) msg.obj;
                    try {
                        mDelegate.handleCreateConnectionSuccessful(
                                (ConnectionRequest) args.arg1,
                                (ParcelableConnection) args.arg2);
                    } finally {
                        args.recycle();
                    }
                    break;
                }
                case MSG_HANDLE_CREATE_CONNECTION_FAILED: {
                    SomeArgs args = (SomeArgs) msg.obj;
                    try {
                        mDelegate.handleCreateConnectionFailed(
                                (ConnectionRequest) args.arg1,
                                args.argi1,
                                (String) args.arg2);
                    } finally {
                        args.recycle();
                    }
                    break;
                }
                case MSG_HANDLE_CREATE_CONNECTION_CANCELLED: {
                    mDelegate.handleCreateConnectionCancelled((ConnectionRequest) msg.obj);
                    break;
                }
                case MSG_SET_ACTIVE:
                    mDelegate.setActive((String) msg.obj);
                    break;
                case MSG_SET_RINGING:
                    mDelegate.setRinging((String) msg.obj);
                    break;
                case MSG_SET_DIALING:
                    mDelegate.setDialing((String) msg.obj);
                    break;
                case MSG_SET_DISCONNECTED: {
                    SomeArgs args = (SomeArgs) msg.obj;
                    try {
                        mDelegate.setDisconnected(
                                (String) args.arg1, args.argi1, (String) args.arg2);
                    } finally {
                        args.recycle();
                    }
                    break;
                }
                case MSG_SET_ON_HOLD:
                    mDelegate.setOnHold((String) msg.obj);
                    break;
                case MSG_SET_REQUESTING_RINGBACK:
                    mDelegate.setRequestingRingback((String) msg.obj, msg.arg1 == 1);
                    break;
                case MSG_SET_CALL_CAPABILITIES:
                    mDelegate.setCallCapabilities((String) msg.obj, msg.arg1);
                    break;
                case MSG_SET_IS_CONFERENCED: {
                    SomeArgs args = (SomeArgs) msg.obj;
                    try {
                        mDelegate.setIsConferenced((String) args.arg1, (String) args.arg2);
                    } finally {
                        args.recycle();
                    }
                    break;
                }
                case MSG_ADD_CONFERENCE_CALL:
                    mDelegate.addConferenceCall((String) msg.obj);
                    break;
                case MSG_REMOVE_CALL:
                    mDelegate.removeCall((String) msg.obj);
                    break;
                case MSG_ON_POST_DIAL_WAIT: {
                    SomeArgs args = (SomeArgs) msg.obj;
                    try {
                        mDelegate.onPostDialWait((String) args.arg1, (String) args.arg2);
                    } finally {
                        args.recycle();
                    }
                    break;
                }
                case MSG_QUERY_REMOTE_CALL_SERVICES:
                    mDelegate.queryRemoteConnectionServices((RemoteServiceCallback) msg.obj);
                    break;
                case MSG_SET_VIDEO_STATE:
                    mDelegate.setVideoState((String) msg.obj, msg.arg1);
                    break;
                case MSG_SET_VIDEO_CALL_PROVIDER: {
                    SomeArgs args = (SomeArgs) msg.obj;
                    try {
                        mDelegate.setVideoCallProvider((String) args.arg1,
                                (IVideoCallProvider) args.arg2);
                    } finally {
                        args.recycle();
                    }
                    break;
                }
                case MSG_SET_AUDIO_MODE_IS_VOIP:
                    mDelegate.setAudioModeIsVoip((String) msg.obj, msg.arg1 == 1);
                    break;
                case MSG_SET_STATUS_HINTS: {
                    SomeArgs args = (SomeArgs) msg.obj;
                    try {
                        mDelegate.setStatusHints((String) args.arg1, (StatusHints) args.arg2);
                    } finally {
                        args.recycle();
                    }
                    break;
                }
                case MSG_SET_HANDLE: {
                    SomeArgs args = (SomeArgs) msg.obj;
                    try {
                        mDelegate.setHandle((String) args.arg1, (Uri) args.arg2, args.argi1);
                    } finally {
                        args.recycle();
                    }
                    break;
                }
                case MSG_SET_CALLER_DISPLAY_NAME: {
                    SomeArgs args = (SomeArgs) msg.obj;
                    try {
                        mDelegate.setCallerDisplayName(
                                (String) args.arg1, (String) args.arg2, args.argi1);
                    } finally {
                        args.recycle();
                    }
                    break;
                }
                case MSG_START_ACTIVITY_FROM_IN_CALL: {
                    SomeArgs args = (SomeArgs) msg.obj;
                    try {
                        mDelegate.startActivityFromInCall(
                                (String) args.arg1, (PendingIntent) args.arg2);
                    } finally {
                        args.recycle();
                    }
                    break;
                }
                case MSG_SET_CONFERENCEABLE_CONNECTIONS: {
                    SomeArgs args = (SomeArgs) msg.obj;
                    try {
                        mDelegate.setConferenceableConnections(
                                (String) args.arg1, (List<String>) args.arg2);
                    } finally {
                        args.recycle();
                    }
                    break;
                }
            }
        }
    };

    private final IConnectionServiceAdapter mStub = new IConnectionServiceAdapter.Stub() {
        @Override
        public void handleCreateConnectionSuccessful(
                ConnectionRequest request, ParcelableConnection connection) {
            SomeArgs args = SomeArgs.obtain();
            args.arg1 = request;
            args.arg2 = connection;
            mHandler.obtainMessage(MSG_HANDLE_CREATE_CONNECTION_SUCCESSFUL, args).sendToTarget();
        }

        @Override
        public void handleCreateConnectionFailed(
                ConnectionRequest request, int errorCode, String errorMessage) {
            SomeArgs args = SomeArgs.obtain();
            args.arg1 = request;
            args.argi1 = errorCode;
            args.arg2 = errorMessage;
            mHandler.obtainMessage(MSG_HANDLE_CREATE_CONNECTION_FAILED, args).sendToTarget();
        }

        @Override
        public void handleCreateConnectionCancelled(ConnectionRequest request) {
            mHandler.obtainMessage(MSG_HANDLE_CREATE_CONNECTION_CANCELLED, request).sendToTarget();
        }

        @Override
        public void setActive(String connectionId) {
            mHandler.obtainMessage(MSG_SET_ACTIVE, connectionId).sendToTarget();
        }

        @Override
        public void setRinging(String connectionId) {
            mHandler.obtainMessage(MSG_SET_RINGING, connectionId).sendToTarget();
        }

        @Override
        public void setDialing(String connectionId) {
            mHandler.obtainMessage(MSG_SET_DIALING, connectionId).sendToTarget();
        }

        @Override
        public void setDisconnected(
                String connectionId, int disconnectCause, String disconnectMessage) {
            SomeArgs args = SomeArgs.obtain();
            args.arg1 = connectionId;
            args.arg2 = disconnectMessage;
            args.argi1 = disconnectCause;
            mHandler.obtainMessage(MSG_SET_DISCONNECTED, args).sendToTarget();
        }

        @Override
        public void setOnHold(String connectionId) {
            mHandler.obtainMessage(MSG_SET_ON_HOLD, connectionId).sendToTarget();
        }

        @Override
        public void setRequestingRingback(String connectionId, boolean ringback) {
            mHandler.obtainMessage(MSG_SET_REQUESTING_RINGBACK, ringback ? 1 : 0, 0, connectionId)
                    .sendToTarget();
        }

        @Override
        public void setCallCapabilities(String connectionId, int callCapabilities) {
            mHandler.obtainMessage(MSG_SET_CALL_CAPABILITIES, callCapabilities, 0, connectionId)
                    .sendToTarget();
        }

        @Override
        public void setIsConferenced(String callId, String conferenceCallId) {
            SomeArgs args = SomeArgs.obtain();
            args.arg1 = callId;
            args.arg2 = conferenceCallId;
            mHandler.obtainMessage(MSG_SET_IS_CONFERENCED, args).sendToTarget();
        }

        @Override
        public void addConferenceCall(String callId) {
            mHandler.obtainMessage(MSG_ADD_CONFERENCE_CALL, callId).sendToTarget();
        }

        @Override
        public void removeCall(String connectionId) {
            mHandler.obtainMessage(MSG_REMOVE_CALL, connectionId).sendToTarget();
        }

        @Override
        public void onPostDialWait(String connectionId, String remainingDigits) {
            SomeArgs args = SomeArgs.obtain();
            args.arg1 = connectionId;
            args.arg2 = remainingDigits;
            mHandler.obtainMessage(MSG_ON_POST_DIAL_WAIT, args).sendToTarget();
        }

        @Override
        public void queryRemoteConnectionServices(RemoteServiceCallback callback) {
            mHandler.obtainMessage(MSG_QUERY_REMOTE_CALL_SERVICES, callback).sendToTarget();
        }

        @Override
        public void setVideoState(String connectionId, int videoState) {
            mHandler.obtainMessage(MSG_SET_VIDEO_STATE, videoState, 0, connectionId).sendToTarget();
        }

        @Override
        public void setVideoCallProvider(
                String connectionId, IVideoCallProvider videoCallProvider) {
            SomeArgs args = SomeArgs.obtain();
            args.arg1 = connectionId;
            args.arg2 = videoCallProvider;
            mHandler.obtainMessage(MSG_SET_VIDEO_CALL_PROVIDER, args).sendToTarget();
        }

        @Override
        public final void setAudioModeIsVoip(String connectionId, boolean isVoip) {
            mHandler.obtainMessage(MSG_SET_AUDIO_MODE_IS_VOIP, isVoip ? 1 : 0, 0,
                    connectionId).sendToTarget();
        }

        @Override
        public final void setStatusHints(String connectionId, StatusHints statusHints) {
            SomeArgs args = SomeArgs.obtain();
            args.arg1 = connectionId;
            args.arg2 = statusHints;
            mHandler.obtainMessage(MSG_SET_STATUS_HINTS, args).sendToTarget();
        }

        @Override
        public final void setHandle(String connectionId, Uri handle, int presentation) {
            SomeArgs args = SomeArgs.obtain();
            args.arg1 = connectionId;
            args.arg2 = handle;
            args.argi1 = presentation;
            mHandler.obtainMessage(MSG_SET_HANDLE, args).sendToTarget();
        }

        @Override
        public final void setCallerDisplayName(
                String connectionId, String callerDisplayName, int presentation) {
            SomeArgs args = SomeArgs.obtain();
            args.arg1 = connectionId;
            args.arg2 = callerDisplayName;
            args.argi1 = presentation;
            mHandler.obtainMessage(MSG_SET_CALLER_DISPLAY_NAME, args).sendToTarget();
        }

        @Override
        public final void startActivityFromInCall(String connectionId, PendingIntent intent) {
            SomeArgs args = SomeArgs.obtain();
            args.arg1 = connectionId;
            args.arg2 = intent;
            mHandler.obtainMessage(MSG_START_ACTIVITY_FROM_IN_CALL, args).sendToTarget();
        }

        @Override
        public final void setConferenceableConnections(
                String connectionId, List<String> conferenceableConnectionIds) {
            SomeArgs args = SomeArgs.obtain();
            args.arg1 = connectionId;
            args.arg2 = conferenceableConnectionIds;
            mHandler.obtainMessage(MSG_SET_CONFERENCEABLE_CONNECTIONS, args).sendToTarget();
        }
    };

    public ConnectionServiceAdapterServant(IConnectionServiceAdapter delegate) {
        mDelegate = delegate;
    }

    public IConnectionServiceAdapter getStub() {
        return mStub;
    }
}
