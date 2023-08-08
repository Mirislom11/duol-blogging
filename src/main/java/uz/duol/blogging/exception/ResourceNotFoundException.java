package uz.duol.blogging.exception;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String message) {
    super(message);
    }
}
