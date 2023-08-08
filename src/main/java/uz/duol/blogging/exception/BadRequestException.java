package uz.duol.blogging.exception;

public class BadRequestException extends RuntimeException{
    public BadRequestException(String message) {
    super(message);
    }
}
