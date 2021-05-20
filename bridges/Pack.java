package bridges;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class Pack implements Serializable {
    public ArrayList<Object> nodes = new ArrayList<>();

    public Pack(Object... objects){
        nodes.addAll(Arrays.asList(objects));
    }
}
