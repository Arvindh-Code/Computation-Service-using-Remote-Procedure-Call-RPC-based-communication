# computation-service-using-remote-procedure-call-RPC-based-communication


## Introduction

This Java project implements a Remote Procedure Call (RPC) system for file synchronization and computation using Java Remote Method Invocation (RMI). The system consists of a server and client, allowing users to perform file upload, download, delete, and rename operations. Additionally, the server provides synchronous and asynchronous RPC calls for adding numbers and sorting an array.

## Project Structure

The project is organized into the following components:

1. **ServerRPC.java**: This class represents the server-side implementation of the RPC system. It handles file synchronization between the server and client, as well as computation operations.

2. **ProjectRPCImpl.java**: This class extends `UnicastRemoteObject` and implements the `ProjectRPC` interface. It provides the actual implementations for file-related operations and computation methods.

3. **ProjectRPC.java**: This interface defines the remote methods that can be invoked through RPC. It includes methods for file upload, download, delete, rename, addition, and array sorting.

4. **ClientRPC.java**: The client-side implementation allows users to interact with the server. It includes options for file upload, download, delete, rename, as well as performing synchronous and asynchronous computation operations.

5. **AsynchronizedCallObject.java**: This class is a helper class providing a structure for handling asynchronous RPC calls. It includes getter and setter methods for retrieving results after asynchronous calls.

## Getting Started

1. **Server Setup**: Run the `ServerRPC` class to start the RPC server. The server uses RMI to bind the `ProjectRPCImpl` implementation to a registry.

   ```java
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
   ```

2. **Client Interaction**: Run the `ClientRPC` class to interact with the server. The client provides a menu for users to choose various file operations and computation methods.

   ```java
   public static void main(String[] args) {
       // Client initialization and interaction with the server
   }
   ```

## File Operations

- **File Upload**: Users can upload files to the server by providing the file name.
- **File Download**: Download files from the server by specifying the file name.
- **File Delete**: Delete a file on the server using the file name.
- **File Rename**: Rename a file on the server by providing the current and new file names.

## Computation Operations

### Synchronous RPC Calls

- **Addition**: Perform synchronous addition of two numbers.
- **Array Sorting**: Synchronously sort an array of integers.

### Asynchronous RPC Calls

- **Asynchronous Addition**: Perform asynchronous addition of two numbers.
- **Asynchronous Array Sorting**: Asynchronously sort an array of integers.

## File Synchronization

The server periodically synchronizes files between the server and client directories. The synchronization interval can be adjusted in the code.

## Dependencies

- Java RMI (Remote Method Invocation)


It's a simple java code.
Can use any IDE - I used IntelliJ IDEA.
Java version - Any version of java 8 later. I used 11.0.17.

## How to run:
  * Import the attached file in any IDE to run in ease way.
  * Before compile change the server and client, set server and client directory ( Server side folder and Client side folder) path according to your system. Code Line number - 17 and 20th line in ProjectRPCImpl.java
  * Change the upload file directory - where upload file locates. Code Line number - 75th line in ClientRPC.java
  * Start with the ServerRPC.java
  * Once ServerRPC class compiled successfully then run the ClientRPC.java file.

  Note: I placed sample.txt for testing purpose. Use any file less than 10mb for standard IDE and system performance to ignore latency(based on cache allocated for the IDE).
  
## Conclusion

This project demonstrates the use of Java RMI for building a simple RPC system. Users can interact with the server to perform file operations and computation tasks synchronously and asynchronously. The periodic file synchronization ensures consistency between the client and server file directories.
