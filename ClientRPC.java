import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;
/**
 * Client side code - support UPLOAD, DOWNLOAD, DELETE, RENAME file in the client side folder.
 * Multi thread is initialize in the Server side code, as any changes occurred in the client side will impact in the server side too.
 * Client making RPC call to computational server to perform addition and  sorting an array.
 * **/
public class ClientRPC {
    public static void main(String[] args) {
        boolean exit = true;
        try {
            /** RMI package has been imported to implement RPC.
             * Registry, and other RMI reference - https://docs.oracle.com/javase/7/docs/technotes/guides/rmi/hello/hello-world.html
            */
            Registry registry = LocateRegistry.getRegistry("localhost");
            ProjectRPC fileServer = (ProjectRPC) registry.lookup("ProjectRPC");
            String fileName;
            Path path ;
            int option =1;
            //Synchronous call for add
            int sync_add_result = fileServer.addNumber(8538,8538);
            System.out.println("Synchronous call add result : "+sync_add_result);
            //Synchronous call for sort
            int[] a = new int[]{89,1,56,22,9,34};
            int[] arr = fileServer.sortArray(a);
            for (int i=0;i<arr.length;i++){
                System.out.println(arr[i]+" ");
            }
            //Asynchronous call
            try{
                AsynchronizedCallObject syncCall = new AsynchronizedCallObject();
                ServerRPC asyn = new ServerRPC();
                //Asynchronous call for adding two numbers
                asyn.addNumber(8538,8538,syncCall);
                //Asynchronous call for array sort
                asyn.sortArray(a,syncCall);
                //initializing waiting time for thread process using wait() method.
                synchronized (syncCall){
                    syncCall.wait(8);
                }
                System.out.println("Asynchronous add two numbers : "+syncCall.getAddresult());
                System.out.println("Asynchronous array sort : ");
                int[] arrResult = syncCall.getArrayResult();
                for(int i=0;i<arrResult.length;i++)
                    System.out.println(arrResult[i]+" ");
            }catch (IOException e){e.printStackTrace();}
            Scanner scan = new Scanner(System.in);
            while(exit){
                System.out.println("\n List operation available to perform on a file. \n 1.Upload file.\n 2.Download.\n 3.Delete a file. \n 4.Rename a file.\n 5.Exit.");
                System.out.println("Please enter a operation to perform : ");
                if(scan.hasNextLine()){
                    int temp = Integer.parseInt(scan.nextLine());
                    option = temp;
                }
                switch (option){
                    /** Upload file
                     *  Reference - https://docs.oracle.com/javaee/6/tutorial/doc/glraq.html **/
                    case 1: System.out.println("Enter the file name to be uploaded: ");
                        if(scan.hasNextLine()){
                            String filee = scan.nextLine();
                            fileName = filee;
                        }
                        else {
                            fileName = scan.nextLine();
                        }
                        /**                       Change the directory where the upload file is located.
                         *                                ↓↓↓↓↓↓↓↓↓↓↓                                      **/
                        uploadFile(Paths.get("/Users/aravindh/Downloads/"+fileName),fileName, fileServer);
                        break;

                    /** Download file
                     * Reference - * https://docs.oracle.com/javase/tutorial/essential/io/copy.html
                     *             * https://docs.oracle.com/javase/tutorial/essential/io/fileOps.html**/
                    case 2: System.out.println("Enter the file name to be download: ");
                        fileName = scan.nextLine();
                        downloadFile(Paths.get("/Users/aravindh/Downloads/ClientFile/"+fileName),fileName, fileServer);
                        break;

                    /** Delete file
                     *  Reference - https://docs.oracle.com/javase/tutorial/essential/io/delete.html**/
                    case 3: System.out.println("Enter the file name to be deleted: ");
                        fileName = scan.nextLine();
                        deleteFile(Paths.get("/Users/aravindh/Downloads/ServerFile/"+fileName),fileName, fileServer);
                        break;

                    /** Rename file
                     *  Reference - https://docs.oracle.com/javase%2F7%2Fdocs%2Fapi%2F%2F/java/io/File.html**/
                    case 4:
                        System.out.println("Enter the file name to be rename: ");
                        fileName = scan.nextLine();
                        System.out.println("Enter the new name for file - "+fileName+" : ");
                        String newName = scan.nextLine();
                        renameFile(Paths.get("/Users/aravindh/Downloads/ServerFile/"+fileName),fileName,newName, fileServer);
                        break;

                    case 5:
                        exit = false;
                        break;
                }
            }


        } catch (RemoteException e) {
            e.printStackTrace();
        }catch (NotBoundException e)
        {
         e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /** Upload file
     *  Reference - https://docs.oracle.com/javaee/6/tutorial/doc/glraq.html **/
    public static void uploadFile(Path path, String fileName,ProjectRPC fileService){
        try{
            byte[] data = Files.readAllBytes(path);
            fileService.uploadFile(fileName, data);
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            System.out.println("File upload completed.");
        }
    }

    /** Download file
     * Reference - * https://docs.oracle.com/javase/tutorial/essential/io/copy.html
     *             * https://docs.oracle.com/javase/tutorial/essential/io/fileOps.html**/
    public static void downloadFile(Path path, String fileName,ProjectRPC fileService){
        try{
            byte[] downloadedData = fileService.downloadFile(fileName);
            Files.write(path, downloadedData);
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            System.out.println("File download completed.");
        }
    }

    /** Delete file
     *  Reference - https://docs.oracle.com/javase/tutorial/essential/io/delete.html**/
    public static void deleteFile(Path path, String fileName,ProjectRPC fileService){
        try{
            fileService.deleteFile(fileName);
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            System.out.println("File delete completed.");
        }
    }

    /** Rename file
     *  Reference - https://docs.oracle.com/javase%2F7%2Fdocs%2Fapi%2F%2F/java/io/File.html**/
    public static void renameFile(Path path, String fileName,String newName, ProjectRPC fileService){
        try{
            fileService.renameFile(fileName,newName);
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            System.out.println("File rename completed.");
        }
    }


}