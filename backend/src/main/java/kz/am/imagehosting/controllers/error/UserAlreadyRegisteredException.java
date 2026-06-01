package kz.am.imagehosting.controllers.error;

import org.springframework.security.core.AuthenticationException;

public class UserAlreadyRegisteredException extends AuthenticationException {
   public UserAlreadyRegisteredException(String msg) {
      super(msg);
   }

   public UserAlreadyRegisteredException(String msg, Throwable cause) {
      super(msg, cause);
   }
}

