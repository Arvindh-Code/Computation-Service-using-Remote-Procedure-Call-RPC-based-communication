import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

/** Server and client RPC call methods - Upload, Download, Delete, Rename has been implemented and defined in this class.**/
public class ProjectRPCImpl extends UnicastRemoteObject implements ProjectRPC {
    public ProjectRPCImpl() throws RemoteException {
        periodicalThread();
    }
    /** Path for server directory*/
    String serverPath = "/Users/aravindh/Downloads/ServerFile/";

    /** Path for client directory*/
    String clientPath = "/Users/aravindh/Downloads/ClientFile/";
    Map<String,Long> filecheck = new HashMap<>();

    /**
     * Periodically file will sync for every 30 seconds in infinite loop
     * **/
    private void periodicalThread() {
        Thread fileSyncThread = new Thread(() -> {
            for(;;){
                try {
                    fileSync();
                    /**
                     * Timing for sync file from client directory to server directory can be changed here.
                     *                  ↓↓↓                                                            **/
                    Thread.sleep(30000);
                } catch (InterruptedException e) {
                    System.err.println("File synchronization interrupted: " + e.getMessage());
                    return;
                }
            }
        });
        fileSyncThread.setDaemon(true);
        fileSyncThread.start();
    }

    private void fileSync() {
        File[] clientfiles = new File(clientPath).listFiles();
        if (clientfiles != null) {
            for (File file : clientfiles) {
                long lastcheck = file.lastModified();
                    try {
                        /** For file sync in the server folder I am checking last modification time with our own map data-
                         * -where last modification done by file syn will be stored in the map - filecheck. And it will update after every sync
                         *  Map will initialize all file check time in map for its sync uses.**/
                        if(filecheck.get(file.getName())==null)
                            filecheck.put(file.getName(),Integer.toUnsignedLong(0));
                        if(lastcheck > filecheck.get(file.getName())){
                        byte[] data = Files.readAllBytes(file.toPath());
                        //while sync files - Upload operation
                        uploadFileSync(file.getName(), data);
                        filecheck.put(file.getName(),file.lastModified());
                        //while sync files - delete operation
                        Files.deleteIfExists(Paths.get(serverPath + file));}
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }

        }
        File[] serverfile = new File(serverPath).listFiles();
        if(serverfile!=null){
            for(File serverFile: serverfile ){
                    try{
                        String copy = clientPath+serverFile.getName();
                        File clientFile = new File(copy);
                        if(!clientFile.exists()){
                            serverFile.delete();
                        }
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                }
        }
    }
    /** while sync file - upload operation has been implemented in server directory from client directory
     **/
    public void uploadFileSync(String fileName, byte[] data) throws RemoteException, IOException {
        FileOutputStream outputStream = new FileOutputStream(serverPath + fileName);
        outputStream.write(data);
        outputStream.close();
        System.out.println("File uploaded: " + fileName);
    }

    /** Upload file
     *  Reference - https://docs.oracle.com/javaee/6/tutorial/doc/glraq.html **/
    @Override
    public void uploadFile(String fileName, byte[] filedata) throws RemoteException, IOException {
        FileOutputStream outputStream = new FileOutputStream(clientPath + fileName);
        outputStream.write(filedata);
        outputStream.close();
        System.out.println("File uploaded: " + fileName);
    }

    /** Download file
     * Reference - * https://docs.oracle.com/javase/tutorial/essential/io/copy.html
     *             * https://docs.oracle.com/javase/tutorial/essential/io/fileOps.html**/
    @Override
    public byte[] downloadFile(String fileName) throws RemoteException, IOException {
        File file = new File(serverPath + fileName);
        return Files.readAllBytes(file.toPath());
    }

    /** Delete file
     *  Reference - https://docs.oracle.com/javase/tutorial/essential/io/delete.html**/
    @Override
    public void deleteFile(String fileName) throws RemoteException, IOException {
        File file = new File(clientPath + fileName);
        if (file.exists()) {
            if (file.delete())
                System.out.println("File deleted: " + fileName);
            else
                throw new IOException("unable to rename the file. ");
        } else
            throw new IOException("File not found: " + fileName);
    }

    /** Rename file
     *  Reference - https://docs.oracle.com/javase%2F7%2Fdocs%2Fapi%2F%2F/java/io/File.html**/
    @Override
    public void renameFile(String fileName, String newName) throws RemoteException, IOException{
        File file = new File(clientPath+fileName);
        if(file.exists()){
            if(file.renameTo(new File(clientPath+newName)))
                System.out.println("File has been renamed successfully ");
            else
                throw new IOException("unable to rename the file. ");
        }
        else
            throw new IOException("File not found: "+ fileName);
    }

    /**Add operation for computation server - Synchronous RPC call**/
    @Override
    public int addNumber(int a, int b) throws RemoteException, IOException {
        return a+b;
    }

    /**Add operation for computation server - Asynchronous RPC call**/
    @Override
    public void addNumber(int a, int b, AsynchronizedCallObject asynCall) throws RemoteException, IOException {
        Thread async = new Thread(() ->{
        });
        a=a+b;
        asynCall.setAddresult(a);
        async.start();
    }

    /**sorting array operation for computation server - Synchronous RPC call **/
    @Override
    public int[] sortArray(int[] array) throws RemoteException, IOException {
        for(int i=0;i<array.length;i++){
            for(int j=i+1;j< array.length;j++){
                if(array[i]>array[j]){
                    int temp = array[i];
                    array[i] = array[j];
                    array[j] = temp;
                }
            }
        }
        return array;
    }

    /**Sorting array  operation for computation server - Asynchronous RPC call**/
    @Override
    public void sortArray(int[] array, AsynchronizedCallObject asyncCall) throws RemoteException {
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
}
