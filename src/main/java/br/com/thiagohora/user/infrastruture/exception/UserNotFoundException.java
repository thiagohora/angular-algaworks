package br.com.thiagohora.user.infrastruture.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.FORBIDDEN, reason="User or password invalid")
public class UserNotFoundException extends UsernameNotFoundException {
public UserNotFoundException() { super("User or password invalid"); }
}
