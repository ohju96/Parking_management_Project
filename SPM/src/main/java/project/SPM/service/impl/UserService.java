package project.SPM.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.SPM.Entity.UserEntity;
import project.SPM.dto.UserSaveForm;
import project.SPM.repository.IUserRepository;
import project.SPM.service.IUserService;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService implements IUserService {

    @Autowired
    IUserRepository iUserRepository;


    @Override
    public int createUser(UserEntity userEntity) throws Exception {

        /**
         * res = 0 , 기타 에러 발생
         * res = 1 , 회원가입 성공
         * res = 2 , 아이디 중복
         */
        int res = 0;

        iUserRepository.save(userEntity);

        if (userEntity != null) {
            res = 1;
        } else {
            res = 0;
        }

        log.info("서비스에서 체크 res ={}", res);

        return res;
    }

}