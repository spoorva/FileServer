package edu.coursera.distributed;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.CharBuffer;
import java.util.regex.*;
/**
 * A basic and very limited implementation of a file server that responds to GET
 * requests from HTTP clients.
 */
public final class FileServer {
    /**
     * Main entrypoint for the basic file server.
     *
     * @param socket Provided socket to accept connections on.
     * @param fs     A proxy filesystem to serve files from. See the PCDPFilesystem
     *               class for more detailed documentation of its usage.
     * @throws IOException If an I/O error is detected on the server. This should be
     *                     a fatal error, your file server implementation is not
     *                     expected to ever throw IOExceptions during normal
     *                     operation.
     */
    public void run(final ServerSocket socket, final PCDPFilesystem fs) throws IOException, SocketException {
        /*
         * Enter a spin loop for handling client requests to the provided ServerSocket
         * object.
         */
        while (true) {

            
            Socket obj = socket.accept();
            obj.setSoTimeout(60000);
            int c;
            String msg = "";
            do {
                c = obj.getInputStream().read();
                msg = msg +(char)c;
            } while (obj.getInputStream().available() > 0);

          
            PCDPPath path = new PCDPPath(msg.split(" ")[1].trim());

            String resp = fs.readFile(path);
            OutputStream out = obj.getOutputStream();
            PrintWriter printer = new PrintWriter(out);

            if (resp!=null) {

                printer.write("HTTP/1.0 200 OK\r\n");
                printer.write("Server: FileServer\r\n");
                printer.write("\r\n");
                printer.write(resp+"\r\n");
        
            }

            else {
                printer.write("HTTP/1.0 404 Not Found\r\n");
                printer.write("Server: FileServer\r\n");
                printer.write("\r\n");
            }

            printer.close(); 
            

        }

         
    }
}
