package se.lexicon.course_manager_assignment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class SchoolManagerExceptionHandler {

    private ModelAndView buildErrorModel(HttpStatus status, Exception exception){
        Map<Object, Object> errorMap = new HashMap<>();
        errorMap.put("name", status.getReasonPhrase());
        errorMap.put("code", status.value());
        errorMap.put("message", exception.getMessage());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("errors/error_response");
        modelAndView.addObject("errorMap", errorMap);
        modelAndView.setStatus(status);
        return modelAndView;
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ModelAndView handleIllegalArgumentException(IllegalArgumentException ex){
        return buildErrorModel(HttpStatus.BAD_REQUEST, ex);
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ModelAndView handleResourceNotFoundException(ResourceNotFoundException ex){
        return buildErrorModel(HttpStatus.NOT_FOUND, ex);
    }

    @ResponseStatus(value = HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ModelAndView handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex){
        return buildErrorModel(HttpStatus.METHOD_NOT_ALLOWED, ex);
    }
}
