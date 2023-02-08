package account.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RoleLoader {

    private RoleRepository roleRepository;

    @Autowired
    public RoleLoader(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
        createRoles();
    }



    private void createRoles(){
        try{
            roleRepository.save(new Role("ROLE_ADMINISTRATOR"));
            roleRepository.save(new Role("ROLE_ACCOUNTANT"));
            roleRepository.save(new Role("ROLE_USER"));
            roleRepository.save(new Role("ROLE_AUDITOR"));

        } catch (Exception e) {}
    }



}
