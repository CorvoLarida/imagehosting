package kz.am.imagehosting.controllers.error;

import org.springframework.security.core.AuthenticationException;

public class UnauthorizedAccessException extends AuthenticationException {
   public UnauthorizedAccessException(String msg) {
      super(msg);
   }

   public UnauthorizedAccessException(String msg, Throwable cause) {
      super(msg, cause);
   }
}

