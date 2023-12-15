import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

/** ProjectRPC is an interface helps to invoke Remote -RMI package for transform file using RPC method.
 * Addition and sorting array Synchronous and Asynchronous RPC call methods are initialized here.
 **/
public interface ProjectRPC extends Remote {
    byte[] downloadFile(String fileName) throws RemoteException, IOException;
    void uploadFile(String fileName, byte[] filedata) throws RemoteException, IOException;
    void renameFile(String fileName, String newName) throws RemoteException,IOException;
    void deleteFile(String fileName) throws RemoteException, IOException;
    int addNumber(int a, int b) throws RemoteException,IOException;
    void addNumber(int a, int b, AsynchronizedCallObject asynCall) throws RemoteException,IOException;
    int[] sortArray(int[] array) throws RemoteException, IOException;
    void sortArray(int[] array, AsynchronizedCallObject asyncCall) throws RemoteException, IOException;
}
