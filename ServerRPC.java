import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Server side code - Sync UPLOAD, DOWNLOAD, DELETE, RENAME file with the client side folder.
 * Multi thread is initialize in this class implemented from ProjectRPCImpl code.
 * Computational server has been implemented in this class to perform addition and  sorting an array.
 * **/


public class ServerRPC extends ProjectRPCImpl {
    /** RMI package has been imported to implement RPC.
     * Registry, and other RMI reference - https://docs.oracle.com/javase/7/docs/technotes/guides/rmi/hello/hello-world.html
     */
    public ServerRPC() throws RemoteException {
    }
    public static void main(String[] args) {
        try {
            ProjectRPCImpl projectRPCImpl = new ProjectRPCImpl();
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("ProjectRPC", projectRPCImpl);
            System.out.print("Running - RPC Server");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**Asynchronous call has been handled by the help of AsynchronizedCallObject class getter and setter for both addition and array sorting operation.
     * **/
    public void sortArray(int[] array, AsynchronizedCallObject asyncCall) throws RemoteException {
        // Using thread help the synchronous operation has been implemented
        //sorting has been done and with thread initialization
        Thread async = new Thread(() ->{
            for(int i = 0; i< array.length; i++){
                for(int j = i+1; j< array.length; j++){
                    if(array[i]> array[j]){
                        int temp = array[i];
                        array[i] = array[j];
                        array[j] = temp;
                    }
                }
            }
        });
        asyncCall.setArrayResult(array);
        async.start();
    }

    public void addNumber(int a, int b, AsynchronizedCallObject asynCall) throws RemoteException, IOException {
        // Using thread help the synchronous operation has been implemented.
        //addition has been done and with thread initialization.
        Thread async = new Thread(() ->{
        });
        a=a+b;
        asynCall.setAddresult(a);
        async.start();
    }
}
