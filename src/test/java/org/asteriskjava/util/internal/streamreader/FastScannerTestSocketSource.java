package org.asteriskjava.util.internal.streamreader;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class FastScannerTestSocketSource
{

    final static byte[] bytes = FastScannerRandomTest.generateTestData(100_000_000).getBytes();
    public static int portNumber = 2048;

    public static void main(String[] args) throws IOException
    {

        while (true)
        {

            listen();
        }

    }

    static void listen() throws IOException
    {
        System.out.println("Waiting for connect on " + portNumber);

        try (ServerSocket serverSocket = new ServerSocket(portNumber); Socket clientSocket = serverSocket.accept();)
        {
            clientSocket.getOutputStream().write(bytes);
        }
    }

}
