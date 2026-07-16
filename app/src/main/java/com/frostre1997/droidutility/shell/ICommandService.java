/*
 * This file is manually generated to replace AIDL compilation.
 * Original: ICommandService.aidl
 */
package com.frostre1997.droidutility;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface ICommandService extends IInterface {

    static final String DESCRIPTOR = "com.frostre1997.droidutility.ICommandService";

    static final int TRANSACTION_destroy = 16777115;
    static final int TRANSACTION_exec = 1;

    void destroy() throws RemoteException;
    String exec(String command) throws RemoteException;

    abstract class Stub extends Binder implements ICommandService {

        public Stub() {
            this.attachInterface(this, DESCRIPTOR);
        }

        public static ICommandService asInterface(IBinder binder) {
            if (binder == null) {
                return null;
            }
            IInterface iin = binder.queryLocalInterface(DESCRIPTOR);
            if (iin instanceof ICommandService) {
                return (ICommandService) iin;
            }
            return new Proxy(binder);
        }

        @Override
        public IBinder asBinder() {
            return this;
        }

        @Override
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case TRANSACTION_destroy: {
                    data.enforceInterface(DESCRIPTOR);
                    this.destroy();
                    reply.writeNoException();
                    return true;
                }
                case TRANSACTION_exec: {
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0 = data.readString();
                    String _result = this.exec(_arg0);
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                }
            }
            return super.onTransact(code, data, reply, flags);
        }

        private static class Proxy implements ICommandService {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                mRemote = remote;
            }

            @Override
            public IBinder asBinder() {
                return mRemote;
            }

            @Override
            public void destroy() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    mRemote.transact(TRANSACTION_destroy, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override
            public String exec(String command) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                String _result;
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(command);
                    mRemote.transact(TRANSACTION_exec, _data, _reply, 0);
                    _reply.readException();
                    _result = _reply.readString();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }
        }
    }
}
