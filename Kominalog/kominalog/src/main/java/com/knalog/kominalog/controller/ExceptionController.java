package com.knalog.kominalog.controller;

import com.knalog.kominalog.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class ExceptionController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody       //@ControllerAdvice 는 @ResponseBody를 포함하고 있지 않음 <--> @RestController 는 포함하고 있음
    public ErrorResponse invalidRequestHandler(MethodArgumentNotValidException e){

//            FieldError fieldError = e.getFieldError();
//            String field = fieldError.getField();
//            String message = fieldError.getDefaultMessage();
//        ErrorResponse response = new ErrorResponse("400","잘못된 요청입니다.");

        // builder 어노테이션을 활용하는 방식
        ErrorResponse response =ErrorResponse.builder()
                .code("400")
                .message("잘못된 요청입니다.")
                .build();

        for (FieldError fieldError : e.getFieldErrors()){
            //response.addValidation(필드명, 에러메시지);
            response.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return response;

//        Map<String,String> response = new HashMap<>();
//        response.put(field, message);

    }
}
