import javax.security.auth.callback.Callback;
import java.io.Serializable;
import java.rmi.RemoteException;

/** Getter and setter for Asynchronous call method and parameter**/
public class AsynchronizedCallObject implements Callback, Serializable {
    boolean resultReady;
    private int addresult;
    private int[] arrayResult;
    protected AsynchronizedCallObject() throws RemoteException {
        super();
        resultReady = false;
    }
    public synchronized int[] getArrayResult() throws InterruptedException {
        while (!resultReady) {
            wait();
        }
        return arrayResult;
    }
    public synchronized void setAddresult(int addresult) {
        this.addresult = addresult;
        resultReady = true;
        notify();
    }
    public synchronized int getAddresult() throws InterruptedException {
        while (!resultReady) {
            wait();
        }
        return addresult;
    }
    public synchronized void setArrayResult(int[] arrayResult) {
        this.arrayResult = arrayResult;
        resultReady = true;
        notify();
    }
}
