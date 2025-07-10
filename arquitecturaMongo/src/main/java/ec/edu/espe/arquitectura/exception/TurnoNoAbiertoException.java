package ec.edu.espe.arquitectura.exception;

public class TurnoNoAbiertoException extends RuntimeException {

    private final Integer errorCode;

    public TurnoNoAbiertoException(String message) {
        super(message);
        this.errorCode = 400;
    }

    @Override
    public String getMessage() {
        return "Error code: " + this.errorCode + ", message: " + super.getMessage();
    }

    public Integer getErrorCode() {
        return errorCode;
    }
} 