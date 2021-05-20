package server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ConnectionHandler {
    ObjectInputStream inputStream;
    ObjectOutputStream outputStream;
    public ConnectionHandler(ObjectInputStream in,ObjectOutputStream out){
        inputStream = in;
        outputStream = out;
    }
}
