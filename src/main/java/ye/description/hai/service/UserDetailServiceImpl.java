package ye.description.hai.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ye.description.hai.entity.User;
import ye.description.hai.repository.UserRepo;

@Component
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);
        if (user != null){
            UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .roles(user.getRoles().toArray(new String[0]))//new String[0] is technically an empty array, but in the context of .toArray(...), it’s just a type hint. Java will return a new array of the correct size filled with the list’s elements
                    .build();
            return userDetails;
        }
        throw new UsernameNotFoundException("user not found with username: "+ username);
    }
}
