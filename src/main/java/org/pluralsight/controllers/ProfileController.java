package org.pluralsight.controllers;

import org.pluralsight.data.CategoryDao;
import org.pluralsight.data.ProductDao;
import org.pluralsight.data.ProfileDao;
import org.pluralsight.data.UserDao;
import org.pluralsight.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
@RestController
@RequestMapping("/profile")
@CrossOrigin
public class ProfileController {


        private ProfileDao profileDao;
        private UserDao userDao;


        @Autowired
        public ProfileController(ProfileDao profileDao, UserDao userDao) {
            this.profileDao = profileDao;
            this.userDao = userDao;
        }


        @GetMapping("")
        public ResponseEntity<Profile> getById(Principal principal)
        {
            try
            {
                String userName = principal.getName();
                User user = userDao.getByUserName(userName);
                int userId = user.getId();
                var profile = profileDao.getById(userId);

                if (profile == null) {
                    return ResponseEntity.notFound().build();
                }

                return ResponseEntity.ok(profile);
            }
            catch(Exception ex)
            {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
            }
        }


        @PostMapping()
        @PreAuthorize("permitAll()")
        public ResponseEntity<Profile> addProfile(@RequestBody Profile profile)
        {
            try
            {
                Profile made = profileDao.create(profile);
                return ResponseEntity.status(HttpStatus.CREATED).body(made);
            }
            catch(Exception ex)
            {

                ex.printStackTrace();
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
            }
        }

    @PutMapping("")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Profile> update(@RequestBody Profile profile, Principal principal)
    {
        try
        {
            String userName = principal.getName();
            // find database user by userId
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            Profile updated = profileDao.update(profile, userId);

            if (updated == null) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(updated);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

}
