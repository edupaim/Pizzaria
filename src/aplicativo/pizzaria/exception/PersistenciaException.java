package aplicativo.pizzaria.exception;

public class PersistenciaException extends Exception {

    public PersistenciaException(String msg, Exception exc) {
        super(msg, exc);
    }

    public PersistenciaException(String msg) {
        super(msg);
    }

}
