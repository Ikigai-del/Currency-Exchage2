package org.example.Utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.DTO.ErrorResponseDto;
import org.example.exception.DataBaseOperationErrorException;
import org.example.exception.EntityExistException;
import org.example.exception.NotFoundException;

import java.io.IOException;
import java.security.InvalidParameterException;

import static jakarta.servlet.http.HttpServletResponse.*;
import static jakarta.servlet.http.HttpServletResponse.SC_NOT_FOUND;

@WebFilter("/*")
public class ExceptionFilter extends HttpFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();



    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws ServletException, IOException {
        try {
            super.doFilter(req, res, chain);
        }
        catch (EntityExistException e) {
            writeErrorResponse(res, SC_CONFLICT, e);
        }
        catch (DataBaseOperationErrorException e) {
            writeErrorResponse(res, SC_INTERNAL_SERVER_ERROR, e);
        }
        catch (InvalidParameterException e) {
            writeErrorResponse(res, SC_BAD_REQUEST, e);
        }
        catch (NotFoundException e) {
            writeErrorResponse(res, SC_NOT_FOUND, e);
        }
    }

    private void writeErrorResponse(HttpServletResponse response, int errorCode, RuntimeException e) throws IOException {
        response.setStatus(errorCode);

        objectMapper.writeValue(response.getWriter(), new ErrorResponseDto(errorCode, e.getMessage()));
    }
}
