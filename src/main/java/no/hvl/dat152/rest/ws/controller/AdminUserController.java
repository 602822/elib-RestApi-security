/**
 *
 */
package no.hvl.dat152.rest.ws.controller;


import no.hvl.dat152.rest.ws.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import no.hvl.dat152.rest.ws.exceptions.UserNotFoundException;
import no.hvl.dat152.rest.ws.model.User;
import no.hvl.dat152.rest.ws.service.AdminUserService;

import java.util.Set;

/**
 * @author tdoy
 */
@RestController
@RequestMapping("/elibrary/api/v1/admin")
public class AdminUserController {

    @Autowired
    private AdminUserService userService;

    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    @PutMapping("/users/{id}")
    public ResponseEntity<Object> updateUserRole(@PathVariable("id") Long id, @RequestParam("role") String role)
            throws UserNotFoundException {

        try {
            userService.updateUserRole(id, role);
            User user = userService.findUser(id);

            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }


    }

    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Object> deleteUserRole(@PathVariable("id") Long id,
                                                 @RequestParam("role") String role) throws UserNotFoundException {

        try {

            userService.deleteUserRole(id, role);
            User user = userService.findUser(id);

            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }


    }

}
