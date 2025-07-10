package ec.edu.espe.arquitectura.exception;

public class TurnoYaAbiertoException extends RuntimeException {

    private final Integer errorCode;

    public TurnoYaAbiertoException(String message) {
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