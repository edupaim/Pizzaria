package aplicativo.pizzaria.exception;

public class NegocioException extends Exception {

    public NegocioException(String msg, Exception exc) {
        super(msg, exc);
    }

    public NegocioException(String msg) {
        super(msg);
    }
}
