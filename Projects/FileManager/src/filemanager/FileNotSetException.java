package filemanager;

/**
 * FileNotSetException
 *
 * @author matthew.towles
 * @date Sep 17, 2019
 */
public class FileNotSetException extends Exception {

    /**
     * Creates a new instance of <code>FileNotSetException</code> without detail message.
     */
    public FileNotSetException() {
    }


    /**
     * Constructs an instance of <code>FileNotSetException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public FileNotSetException(String msg) {
        super(msg);
    }
}
