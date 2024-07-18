package com.example.demo.exception;

import com.example.demo.dto.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

@RestControllerAdvice
public class HandleExceptionGlobal {
    private final static Logger logger = LoggerFactory.getLogger(HandleExceptionGlobal.class);
    private static ErrorResponse getErrorResponseTemplate(Exception ex,WebRequest request){
        ex.printStackTrace();
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimeStamp(new Date());
        errorResponse.setPath(request.getDescription(false).replace("uri=",""));
        errorResponse.setMessage(ex.getMessage());
        return errorResponse;
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> hanldeException(Exception ex, WebRequest request){
        logger.error(ex.getMessage(),ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(getErrorResponseTemplate(ex,request));
    }
    //hanlde exception when generate key and encrypt data
    @ExceptionHandler({NoSuchAlgorithmException.class, NoSuchPaddingException.class, InvalidKeyException.class, BadPaddingException.class, IllegalBlockSizeException.class})
    public ResponseEntity<ErrorResponse> handleNoSuchAlgorithmException(Exception ex,WebRequest request){
        logger.error(ex.getMessage(),ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(getErrorResponseTemplate(ex,request));
    }
    // hanlde invalid param
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(Exception ex,WebRequest request){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(getErrorResponseTemplate(ex,request));
    }
    //hanlde invalid param my function
    @ExceptionHandler(IllegalArgumentMyFunctionException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentMyFunctionException(Exception ex,WebRequest request){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(getErrorResponseTemplate(ex,request));
    }
    //
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(getErrorResponseTemplate(ex,request));
    }
}
